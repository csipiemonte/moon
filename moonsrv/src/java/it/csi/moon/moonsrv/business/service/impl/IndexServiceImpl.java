/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.entity.IndexRichiestaDettaglioEntity;
import it.csi.moon.commons.entity.IndexRichiestaEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzaPdfEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.commons.entity.IstanzeSorterBuilder;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.RepositoryFileFilter;
import it.csi.moon.commons.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.commons.util.decodifica.DecodificaTipoFileIndexRichiesta;
import it.csi.moon.commons.util.decodifica.DecodificaTipoRepositoryFile;
import it.csi.moon.moonsrv.business.service.AllegatiService;
import it.csi.moon.moonsrv.business.service.IndexService;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.ModuliService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.RepositoryFileService;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IndexDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IndexRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IndexRichiestaDettaglioDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaPdfDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.AspectIndex;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;


@Component
public class IndexServiceImpl implements IndexService{

	private static final String CLASS_NAME = "IndexServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	private static final String PATH_APP_COMPANY_HOME = "PATH:\"/app:company_home";
	
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	IndexDAO indexDAO;
	@Autowired
	EnteDAO enteDAO;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	AllegatiService allegatiService;
	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	IstanzaPdfDAO istanzaPdfDAO;
	@Autowired
	ModuliService moduliService;
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	IndexRichiestaDAO indexRichiestaDAO;
	@Autowired
	IndexRichiestaDettaglioDAO indexRichiestaDettaglioDAO;
	@Autowired
	RepositoryFileService repoFileService;
	
	
	@Override
	public String salvaIstanzaIndexById(Long idIstanza, Long idRichiesta) {
		return salvaIstanzaIndexById(idIstanza, idRichiesta, null);
	}
	
	@Override
	public String salvaIstanzaIndexById(Long idIstanza, Long idRichiesta, Modulo modulo) {
		try {
			LOG.debug("[" + CLASS_NAME + "::salvaIstanzaIndexById] IN idIstanza: "+idIstanza);
			
			IndexRichiestaEntity richiestaEntity = null;
			boolean archiviaSoloUnaIstanza = false;
	
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);
			String code_ente = enteDAO.findById(entity.getIdEnte()).getCodiceEnte();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(entity.getDataCreazione());
			int anno = calendar.get(Calendar.YEAR);
			String code_istanza = entity.getCodiceIstanza();
			
			//if called by IstanzeApiImpl.salvaIstanzaIndexById(id,null)
			if(idRichiesta == null) {
				richiestaEntity = new IndexRichiestaEntity();
				richiestaEntity.setDataInizio(new Date());
				richiestaEntity.setDataFilter(null);
				richiestaEntity.setIstanzeProc(1);
				richiestaEntity.setIdModulo(entity.getIdModulo());
				idRichiesta = indexRichiestaDAO.insert(richiestaEntity);
				archiviaSoloUnaIstanza = true;
			}
			// create aspect 
			AspectIndex aspect = new AspectIndex();
			if(modulo == null) {
				modulo = moduliService.getModuloById(entity.getIdModulo(), entity.getIdVersioneModulo(), null);
			}
			setFieldsAspect(aspect,entity,modulo,code_ente);
			
			String uid_fold_istanza = cercaOcreaFoldIstanza(code_ente, anno, code_istanza, aspect); 
			if(uid_fold_istanza == null) {
				//exception x uscire
				LOG.error("[" + CLASS_NAME + "::salvaIstanzaIndexById] Errore creazione folder istanza ");
				throw new BusinessException();
			}
			String fullPath = PATH_APP_COMPANY_HOME+"/cm:"+EnvProperties.readFromFile(EnvProperties.DOQUI_INDEX_ROOT)+
							"/cm:"+code_ente+"/cm:"+anno+"/cm:"+code_istanza;
			salvaIstanzaPdf(entity,uid_fold_istanza,fullPath,idRichiesta,aspect);
			salvaAllegatiIstanza(entity,uid_fold_istanza,fullPath,idRichiesta,aspect);
			salvaAltriFile(entity,uid_fold_istanza,fullPath,idRichiesta,aspect);
			
			if(archiviaSoloUnaIstanza == true) {
				richiestaEntity.setDataFine(new Date());
				indexRichiestaDAO.update(richiestaEntity);
			}
			
			return "OK";
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::salvaIstanzaIndexById] DAOException on idIstanza=" + idIstanza);
			throw new BusinessException(daoe);
		}
	}

	private void salvaAltriFile(IstanzaEntity entity, String uid_fold_istanza, String path, Long idRichiesta, AspectIndex aspect) {
		IndexRichiestaDettaglioEntity indexRichDettEntity = new IndexRichiestaDettaglioEntity();
		try {
			LOG.debug("[" + CLASS_NAME + "::salvaAllegatiIstanza] idIstanza: "+entity.getIdIstanza());
			String ris = null;
			indexRichDettEntity.setIdRichiesta(idRichiesta);
			indexRichDettEntity.setTipoFile(DecodificaTipoFileIndexRichiesta.FILE_ALLEGATO_RISPOSTA_INTEGRAZIONE.getId());
			indexRichDettEntity.setIdIstanza(entity.getIdIstanza());
			
			RepositoryFileFilter filter = new RepositoryFileFilter();
			filter.setIdIstanza(entity.getIdIstanza());
			filter.setTipiFile(List.of(DecodificaTipoRepositoryFile.FO_ALLEGATO_RISPOSTA_INTEGRAZIONE));
			
			List<RepositoryFileEntity> elenco = repoFileService.getElencoRepositoryFileEntity(filter);
			for(RepositoryFileEntity r : elenco) {
				indexRichDettEntity.setIdFile(r.getIdFile());
				String nomeFile = null;
				nomeFile = r.getFormioNameFile();
				if( nomeFile == null) {
					nomeFile = r.getNomeFile();
				}
				//x evitare errore creazione duplicati
				try {
					ris = indexDAO.luceneSearch(path+"/cm:"+nomeFile+"\"");
				} catch (DAOException e) {
					insertIndexRichiestaDettaglio(indexRichDettEntity,"200",e.getMessage());
					continue;
				}
				if(ris != null) {
					if( ! ris.equals(r.getUuidIndex())  ) {//in caso di incogruenze
						r.setUuidIndex(ris);
						repoFileService.updateUuidIndex(r);
					}
					insertIndexRichiestaDettaglio(indexRichDettEntity,"003","OK");
					continue;
				} 
				//salvo file su index
				String mimeType = r.getContentType();
				byte[] bytes = r.getContenuto();
				String uidFile = null;
				try {
					setFieldsFileAspect(aspect, nomeFile, r.getCodiceFile(), r.getHashFile(), DecodificaTipoFileIndexRichiesta.FILE_ALLEGATO_RISPOSTA_INTEGRAZIONE.getCodiceTipologia());
					uidFile = indexDAO.creaDocumento(uid_fold_istanza, nomeFile, bytes, mimeType, aspect);
				} catch (DAOException e) {
					insertIndexRichiestaDettaglio(indexRichDettEntity,"200",e.getMessage());
					continue;
				}
				//salvo uuid_index
				r.setUuidIndex(uidFile);
				repoFileService.updateUuidIndex(r);
				
				//inserisco dettaglio
				insertIndexRichiestaDettaglio(indexRichDettEntity,"000","OK");
			}//end loop
		} catch (Exception e) {
			insertIndexRichiestaDettaglio(indexRichDettEntity,"100",e.getMessage());
			throw new BusinessException(e);
		}
	}

	private void salvaAllegatiIstanza(IstanzaEntity entity, String uid_fold_istanza, String path, Long idRichiesta, AspectIndex aspect) {
		IndexRichiestaDettaglioEntity indexRichDettEntity = new IndexRichiestaDettaglioEntity();
		try {
			LOG.debug("[" + CLASS_NAME + "::salvaAllegatiIstanza] idIstanza: "+entity.getIdIstanza());
			String ris = null;
			indexRichDettEntity.setIdRichiesta(idRichiesta);
			indexRichDettEntity.setTipoFile(DecodificaTipoFileIndexRichiesta.ALLEGATO_ISTANZA.getId());
			indexRichDettEntity.setIdIstanza(entity.getIdIstanza());
			
			List<AllegatoLazyEntity> allegati = allegatoDAO.findLazyByIdIstanza(entity.getIdIstanza());
			for(AllegatoLazyEntity a : allegati) {
				indexRichDettEntity.setIdAllegato(a.getIdAllegato());
				String nomeFile = a.getFormioNameFile();
				//x evitare errore creazione duplicati
				try {
					ris = indexDAO.luceneSearch(path+"/cm:"+nomeFile+"\"");
				} catch (DAOException e) {
					insertIndexRichiestaDettaglio(indexRichDettEntity,"200",e.getMessage());
					continue;
				}
				if(ris != null) {
					if( !ris.equals(a.getUuidIndex())  ) {//in caso di incogruenze
						a.setUuidIndex(ris);
						allegatoDAO.updateUuidIndex(a);
					}
					insertIndexRichiestaDettaglio(indexRichDettEntity,"002","OK");
					continue;
				}
				//salvo allegato su index
				String mimeType = a.getContentType();
				byte[] bytes = allegatoDAO.findById(a.getIdAllegato()).getContenuto();
				String uidAllegato = null;
				try {
					setFieldsFileAspect(aspect, nomeFile, a.getCodiceFile(), a.getHashFile(), DecodificaTipoFileIndexRichiesta.ALLEGATO_ISTANZA.getCodiceTipologia());
					uidAllegato = indexDAO.creaDocumento(uid_fold_istanza, nomeFile, bytes, mimeType, aspect);
				} catch (DAOException e) {
					insertIndexRichiestaDettaglio(indexRichDettEntity,"200",e.getMessage());
					continue;
				}
				//salvo uuid_index
				a.setUuidIndex(uidAllegato);
				allegatoDAO.updateUuidIndex(a);
				//inserisco dettaglio
				insertIndexRichiestaDettaglio(indexRichDettEntity,"000","OK");
			}//end loop
		} catch (Exception e) {
			insertIndexRichiestaDettaglio(indexRichDettEntity,"100",e.getMessage());
			throw new BusinessException(e);
		}
	}

	private void salvaIstanzaPdf(IstanzaEntity entity, String uid_fold_istanza, String path, Long idRichiesta, AspectIndex aspect) {
		IndexRichiestaDettaglioEntity indexRichDettEntity = new IndexRichiestaDettaglioEntity();
		try {
			LOG.debug("[" + CLASS_NAME + "::salvaIstanzaPdf] idIstanza: " + entity.getIdIstanza());	
			indexRichDettEntity.setIdRichiesta(idRichiesta);
			indexRichDettEntity.setTipoFile(DecodificaTipoFileIndexRichiesta.PDF_ISTANZA.getId());
			indexRichDettEntity.setIdIstanza(entity.getIdIstanza());
			
			String ris = null;
			String uidPdf = null;
			IstanzaPdfEntity entityPdf = null;
			String nomeFile = entity.getCodiceIstanza()+".pdf";
			//x evitare errore creazione duplicati
			try {
				ris = indexDAO.luceneSearch(path+"/cm:"+nomeFile+"\"");
			} catch (DAOException e) {
				insertIndexRichiestaDettaglio(indexRichDettEntity,"200",e.getMessage());
			}
			
			try {
				entityPdf = printIstanzeService.getIstanzaPdfEntityById(entity.getIdIstanza());
			} catch (Exception e) {
				insertIndexRichiestaDettaglio(indexRichDettEntity,"101","printIstanzeService.getIstanzaPdfEntityById()");
				return;
			}
			if(ris != null) {//se e' gia presente su index
				if( ! ris.equals(entityPdf.getUuidIndex())  ) {//in caso di incogruenze
					entityPdf.setUuidIndex(ris);
					istanzaPdfDAO.updateUuidIndex(entityPdf);
				}
				insertIndexRichiestaDettaglio(indexRichDettEntity,"001","OK");
				return;
			} 
			//salvo pdf su index		
			try {
				// campo=null xk pdf nn ha codice_file
				setFieldsFileAspect(aspect, nomeFile, null, entityPdf.getHashPdf(), DecodificaTipoFileIndexRichiesta.PDF_ISTANZA.getCodiceTipologia());
				uidPdf = indexDAO.creaDocumento(uid_fold_istanza, nomeFile, entityPdf.getContenutoPdf(), "application/pdf", aspect);
			} catch (DAOException e) {
				insertIndexRichiestaDettaglio(indexRichDettEntity,"200",e.getMessage());
			}
			if(uidPdf != null) {
				entityPdf.setUuidIndex(uidPdf);
				istanzaPdfDAO.updateUuidIndex(entityPdf);
				
				insertIndexRichiestaDettaglio(indexRichDettEntity,"000","OK");
			}
		} catch (Exception e) {
			// save error without throw BusinessException
			LOG.error("[" + CLASS_NAME + "::salvaIstanzaPdf] idIstanza: " + entity.getIdIstanza(), e);
			insertIndexRichiestaDettaglio(indexRichDettEntity,"100",e.getMessage());
		}
	}

	private void insertIndexRichiestaDettaglio(IndexRichiestaDettaglioEntity indexRichDettEntity, String stato, String statoDesc) {
		indexRichDettEntity.setStato(stato);
		indexRichDettEntity.setDesc_stato(statoDesc);
		indexRichiestaDettaglioDAO.insert(indexRichDettEntity);
	}

	private String cercaOcreaFoldIstanza(String code_ente, int anno, String code_istanza, AspectIndex aspect) {
		String ris = null;
		String root = PATH_APP_COMPANY_HOME+"/cm:"+EnvProperties.readFromFile(EnvProperties.DOQUI_INDEX_ROOT);
		//cerco full path
		ris = indexDAO.luceneSearch(root+"/cm:"+code_ente+"/cm:"+anno+"/cm:"+code_istanza+"/\"");
		if(ris != null) {
			return ris;
		}else {
			//cerco codiece ente/anno
			String uid_fold_anno = indexDAO.luceneSearch(root+"/cm:"+code_ente+"/cm:"+anno+"/\"");
			if(uid_fold_anno != null) {
				//creo nodo istanza e return uid istanza
				ris = indexDAO.creaFolderWithAspect(uid_fold_anno,code_istanza, aspect);
				return ris;
			}else {
				//cerco codice ente
				String uid_fold_ente = indexDAO.luceneSearch(root+"/cm:"+code_ente+"/\"");
				if(uid_fold_ente != null) {
					//creo anno, nodo istanza e return uid istanza
					uid_fold_anno = indexDAO.creaFolder(uid_fold_ente, String.valueOf(anno));
					ris = indexDAO.creaFolderWithAspect(uid_fold_anno,code_istanza, aspect);
					return ris;
				}else {
					//cerco root index
					String uid_root_index = indexDAO.luceneSearch(root+"/\"");
					if(uid_root_index != null ) {
						//CREO codice ente, anno, istanza
						uid_fold_ente = indexDAO.creaFolder(uid_root_index,code_ente);
						uid_fold_anno = indexDAO.creaFolder(uid_fold_ente, String.valueOf(anno));
						ris = indexDAO.creaFolderWithAspect(uid_fold_anno,code_istanza, aspect);
						return ris;
					}else {
					//CREO root index,ente, anno, nodo istanza e return uid istanza
					String app_company_home = indexDAO.luceneSearch("PATH:\"/app:company_home/\"");
					uid_root_index = indexDAO.creaFolder(app_company_home,EnvProperties.readFromFile(EnvProperties.DOQUI_INDEX_ROOT));
					uid_fold_ente = indexDAO.creaFolder(uid_root_index,code_ente);
					uid_fold_anno = indexDAO.creaFolder(uid_fold_ente, String.valueOf(anno));
					ris = indexDAO.creaFolderWithAspect(uid_fold_anno,code_istanza, aspect);
					return ris;
					}
				}
			} 		
		}
	}
	
	@Override
	public	byte[] getContentByUid(String uid) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getContentByUid] IN uid: "+uid);
			byte[] ris = indexDAO.getContentByUid(uid);
			return ris;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getContentByUid] "+daoe);
			throw new BusinessException(daoe);
		}
	}

	@Override
	public String archiviaIndexByModulo(String codiceOrIdModulo, Date data) {
		IndexRichiestaEntity richiestaEntity = new IndexRichiestaEntity();
		int countIstanzeProc=0;
		try {
			LOG.debug("[" + CLASS_NAME + "::archiviaIndexByModulo] codiceOrIdModulo: "+codiceOrIdModulo+" data: "+data);
			Long idRichiesta;
			Long idModulo;
			IstanzeFilter filter = new IstanzeFilter();
			filter.setNotStatiBo(List.of(DecodificaStatoIstanza.ELIMINATA.getIdStatoWf(),DecodificaStatoIstanza.COMPLETATA.getIdStatoWf(),
					DecodificaStatoIstanza.BOZZA.getIdStatoWf(),DecodificaStatoIstanza.DA_PAGARE.getIdStatoWf()));
			if(codiceOrIdModulo.matches("[0-9]+")) {
				idModulo = Long.parseLong(codiceOrIdModulo);
				filter.setIdModulo(idModulo);
			}else {
				idModulo = moduliService.getIdModuloByCodice(codiceOrIdModulo);
				filter.setIdModulo(idModulo);
			}
			if(data != null) {
				//truncate solo data e aggiungo 1 giorno
				Calendar c = Calendar.getInstance();
			    c.setTime(data);
			    c.add(Calendar.DATE, 1);
				Date dataFilter = c.getTime();
			    filter.setCreatedEnd(dataFilter);
			}
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder("").build();
			List<Istanza> elenco = istanzeService.getElencoIstanze(filter, optSorter);
			
			//salvo richiesta archivizione
			richiestaEntity.setDataInizio(new Date());
			richiestaEntity.setIdModulo(idModulo);
			richiestaEntity.setDataFilter(data);
			idRichiesta = indexRichiestaDAO.insert(richiestaEntity);
			
			Map<String,Modulo> cacheModuli = new HashMap<>();
			
			//loop istanze da archiviare
			for(Istanza i : elenco) {
				String key = i.getModulo().getIdModulo()+"-"+i.getModulo().getIdVersioneModulo();
				if(! cacheModuli.containsKey(key)) {
					Modulo modulo = moduliService.getModuloById(i.getModulo().getIdModulo(), i.getModulo().getIdVersioneModulo(), null);
					if(modulo != null) {
						cacheModuli.put(key, modulo);
					}
				}
				salvaIstanzaIndexById(i.getIdIstanza(), idRichiesta, cacheModuli.get(key));
				countIstanzeProc++;
				key = null;
			}
			
			richiestaEntity.setDataFine(new Date());
			richiestaEntity.setIstanzeProc(countIstanzeProc);
			indexRichiestaDAO.update(richiestaEntity);
			
			return String.valueOf(countIstanzeProc);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::archiviaIndexByModulo] "+daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException e) {
			richiestaEntity.setDataFine(new Date());
			richiestaEntity.setIstanzeProc(countIstanzeProc);
			indexRichiestaDAO.update(richiestaEntity);
			throw e;
		}
	}

	@Override
	public String deleteContentByModulo(String codiceOrIdModulo, Date data) {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteContentIndexByModulo] codiceOrIdModulo: "+codiceOrIdModulo);
	
			StringBuilder risp = new StringBuilder("Istanze processate: ");
			Long idModulo;
			IstanzeFilter filter = new IstanzeFilter();
			filter.setNotStatiBo(List.of(DecodificaStatoIstanza.ELIMINATA.getIdStatoWf(),DecodificaStatoIstanza.COMPLETATA.getIdStatoWf(),
					DecodificaStatoIstanza.BOZZA.getIdStatoWf(),DecodificaStatoIstanza.DA_PAGARE.getIdStatoWf()));
			
			if(codiceOrIdModulo.matches("[0-9]+")) {
				idModulo = Long.parseLong(codiceOrIdModulo);
				filter.setIdModulo(idModulo);
			}else {
				idModulo = moduliService.getIdModuloByCodice(codiceOrIdModulo);
				filter.setIdModulo(idModulo);
			}
			if(data != null) {
				//truncate solo data e aggiungo 1 giorno
				Calendar c = Calendar.getInstance();
			    c.setTime(data);
			    c.add(Calendar.DATE, 1);
			    data = c.getTime();
				filter.setCreatedEnd(data);
			}
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder("").build();
			List<Istanza> elenco = istanzeService.getElencoIstanze(filter, optSorter);
			//loop istanze da cancellare
			for(Istanza i : elenco) {
				try {
					deleteContentIstanzaById(i.getIdIstanza());
					risp.append(i.getIdIstanza()+" ");
				} catch (BusinessException e) {
					continue;
				}
			}
			return risp.toString();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::deleteContentIndexByModulo] "+e);
			throw new BusinessException(e);
		}
		
	}

	public String deleteContentIstanzaById(Long idIstanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteContentIstanzaIndexById] idIstanza: "+idIstanza);
			Optional<IstanzaPdfEntity> entityPdfOpt = istanzaPdfDAO.findByIdIstanza(idIstanza);
			if (entityPdfOpt.isEmpty()) {
				LOG.error("[" + CLASS_NAME + "::deleteContentIstanzaIndexById] entityPdfOpt Not found. idIstanza: " + idIstanza);
				throw new BusinessException("entityPdf not found");
			}
			IstanzaPdfEntity entityPdf = entityPdfOpt.get();
			try {
				checkAndPruneContentOnMoon(entityPdf);
			} catch (Exception e) {
				LOG.info("[" + CLASS_NAME + "::deleteContentIstanzaIndexById] "+e);
			}
			//
			List<Allegato> allegati = allegatiService.findLazyByIdIstanza(idIstanza);
			for(Allegato a : allegati) {
				try {
					checkAndPruneContentOnMoon(a);
				} catch (Exception e) {
					LOG.info("[" + CLASS_NAME + "::deleteContentIstanzaIndexById] "+e);
					continue;
				}
			}
			//
			RepositoryFileFilter filter = new  RepositoryFileFilter();
			filter.setIdIstanza(idIstanza);
			filter.setTipiFile(List.of(DecodificaTipoRepositoryFile.FO_ALLEGATO_RISPOSTA_INTEGRAZIONE));
			List<RepositoryFileEntity> files = repoFileService.getElencoRepositoryFileEntity(filter);
			for(RepositoryFileEntity r : files) {
				try {
					checkAndPruneContentOnMoon(r);
				} catch (Exception e) {
					LOG.info("[" + CLASS_NAME + "::deleteContentIstanzaIndexById] "+e);
					continue;
				}
			}
			return "OK";		
		} catch (BusinessException be) {
				throw be;
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	protected void checkAndPruneContentOnMoon(IstanzaPdfEntity entityPdf) {
		if(checkPdfBeforeDelete(entityPdf)) {
			entityPdf.setContenutoPdf(null);
			istanzaPdfDAO.update(entityPdf);
		} else {
			LOG.warn("[" + CLASS_NAME + "::deleteContentIstanzaIndexById] checkPdfBeforeDelete id_istanza="+entityPdf.getIdIstanza());
		}
	}

	protected void checkAndPruneContentOnMoon(Allegato a) {
		if(checkAllegatoBeforeDelete(a.getIdAllegato())) {
			allegatiService.deleteContenuto(a.getIdAllegato());
		} else {
			LOG.warn("[" + CLASS_NAME + "::deleteContentIstanzaIndexById] checkAllegatoBeforeDelete id_allegato="+a.getIdAllegato());
		}
	}

	protected void checkAndPruneContentOnMoon(RepositoryFileEntity r) {
		if(checkFileBeforeDelete(r)) {
			r.setContenuto(null);
			repoFileService.update(r);
		} else {
			LOG.warn("[" + CLASS_NAME + "::deleteContentIstanzaIndexById] checkFileBeforeDelete id_file="+r.getIdFile());
		}
	}

	private boolean checkFileBeforeDelete(RepositoryFileEntity r) {
		if( r.getContenuto() != null && r.getUuidIndex() != null) {
			byte[] b = getContentByUid(r.getUuidIndex());
			return Arrays.equals(b, r.getContenuto());
		}
		return false;
	}

	private boolean checkAllegatoBeforeDelete(Long idAllegato) {
		Allegato allegato = allegatiService.getById(idAllegato);
		if( allegato.getContenuto() != null && allegato.getUuidIndex() != null) {
			byte[] contenutoIndex = getContentByUid(allegato.getUuidIndex());
			return Arrays.equals(contenutoIndex, allegato.getContenuto());
		}
		return false;
	}

	private boolean checkPdfBeforeDelete(IstanzaPdfEntity entityPdf) {
		if(entityPdf.getContenutoPdf() != null && entityPdf.getUuidIndex() != null) {
			byte[] b = getContentByUid(entityPdf.getUuidIndex());
			return Arrays.equals(b, entityPdf.getContenutoPdf());
		}
		return false;
	}
	
	private void setFieldsAspect(AspectIndex aspect,IstanzaEntity entity,Modulo modulo,String codeEnte) {
		aspect.setIdentificativoUtente(entity.getIdentificativoUtente());
		aspect.setCodiceFiscaleDichiarante(entity.getCodiceFiscaleDichiarante());
		aspect.setCognomeDichiarante(entity.getCognomeDichiarante());
		aspect.setNomeDichiarante(entity.getNomeDichiarante());
		aspect.setDataCreazione(entity.getDataCreazione());
		aspect.setNumeroProtocollo(entity.getNumeroProtocollo());
		aspect.setDataProtocollo(entity.getDataProtocollo());
		aspect.setCodiceIstanza(entity.getCodiceIstanza());
		
		aspect.setCodiceModulo(modulo.getCodiceModulo());
		aspect.setOggettoModulo(modulo.getOggettoModulo());
		aspect.setVersioneModulo(modulo.getVersioneModulo());
		
		aspect.setCodiceEnte(codeEnte);
	}
	
	private void setFieldsFileAspect(AspectIndex aspect, String nomeFile, String codiceFile, String hashFile,
			String codiceTipologia) {
		
		aspect.setNomeFile(nomeFile);
		aspect.setCodiceFile(codiceFile);
		aspect.setHashFile(hashFile);
		aspect.setCodiceTipologia(codiceTipologia);
	}

	
}
