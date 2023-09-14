/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.doc.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.apimint.mydocs.be.v1.dto.AmbitoResponse;
import it.csi.apimint.mydocs.be.v1.dto.DocumentiResponse;
import it.csi.apimint.mydocs.be.v1.dto.Documento;
import it.csi.apimint.mydocs.be.v1.dto.FiltroDocumento;
import it.csi.apimint.mydocs.be.v1.dto.Tipologia;
import it.csi.apimint.mydocs.be.v1.dto.TipologiaResponse;
import it.csi.moon.commons.dto.Ambito;
import it.csi.moon.commons.dto.Area;
import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ProtocolloParametro;
import it.csi.moon.commons.dto.extra.doc.RichiestaPubblicazioneMyDocs;
import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.IstanzaPdfEntity;
import it.csi.moon.commons.entity.MyDocsParametroEntity;
import it.csi.moon.commons.entity.MyDocsRichiestaEntity;
import it.csi.moon.commons.entity.MyDocsRichiestaFilter;
import it.csi.moon.commons.entity.ProtocolloRichiestaEntity;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.RepositoryFileLazyEntity;
import it.csi.moon.commons.mapper.MyDocsParametroMapper;
import it.csi.moon.commons.util.MapMyDocsAttributi;
import it.csi.moon.commons.util.MyDocsAttributoKeys;
import it.csi.moon.commons.util.ProtocolloAttributoKeys;
import it.csi.moon.moonsrv.business.service.AreeService;
import it.csi.moon.moonsrv.business.service.EntiService;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.doc.MyDocsService;
import it.csi.moon.moonsrv.business.service.helper.task.SendEmailMyDocsTask;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.MyDocsParametroDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.MyDocsRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.MyDocsDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class MyDocsServiceImpl implements MyDocsService {
	
	private static final String CLASS_NAME = "MyDocsServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private final SimpleDateFormat SDF_ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
	
	// Ambiti
	private static final int MYDOCS_AMBITO_ID_VISIBILITA_DEFAULT = 1;
	private static final String MYDOCS_AMBITO_COLOR_DEFAULT = "blue";
	private static final String MYDOCS_AMBITO_ATTORE_DEFAULT = "MOOn";
	// Tipologie
	private static final Long MYDOCS_ID_ESTENSIONE_PDF = 1L; // 1-PDF
	private static final String MYDOCS_MIME_TYPE_APPLICATION_PDF = "application/pdf";
	
//	private class MyDocsConf {
//		private Long idAmbito;
//		private Long idTipologia;
//		public MyDocsConf(Long idAmbito, Long idTipologia) {this.idAmbito=idAmbito; this.idTipologia=idTipologia;}
//		public Long getIdAmbito() {
//			return idAmbito;
//		}
//		public Long getIdTipologia() {
//			return idTipologia;
//		}
//	}
//	ConcurrentHashMap<Long, Map<Integer,MyDocsConf>> confByEnti = new ConcurrentHashMap<>();
	
	class MdResponse {
		String uuidPrincipale;
		boolean pubblicatoAdesso;
		//
		public MdResponse(String uuidPrincipale, boolean pubblicatoAdesso) {
			super();
			this.uuidPrincipale = uuidPrincipale;
			this.pubblicatoAdesso = pubblicatoAdesso;
		}
		// GET
		public String getUuidPrincipale() {
			return uuidPrincipale;
		}
		public boolean isPubblicatoAdesso() {
			return pubblicatoAdesso;
		}
	}
	
	@Autowired
	@Qualifier("apimint")
	private MyDocsDAO mydocsDAO = null;
	@Autowired
	private MyDocsRichiestaDAO mydocsRichiestaDAO = null;
	@Autowired
	EnteDAO enteDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	AreaModuloDAO areaModuloDAO;
	@Autowired
	MyDocsParametroDAO mydocsParametroDAO;
	@Autowired
	EntiService entiService;
	@Autowired
	AreeService areeService;
	
	/**
	 * Pubblica un documento su MyDocs
	 * Return uuid di MyDocs
	 */
//	@Override
//	public String pubblicaDocumento(RichiestaPubblicazioneMyDocs richiesta) throws BusinessException {
//		return pubblicaDocumentoPrincipale(richiesta).getUuidPrincipale();
//	}
	
	private MdResponse pubblicaDocumentoPrincipale(RichiestaPubblicazioneMyDocs richiesta, MapMyDocsAttributi attributi, Long idRichiesta) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::pubblicaDocumentoPrincipale] IN richiesta: " + richiesta);
			validaRichiesta(richiesta);
			boolean pubblicatoAdesso = false;
			String codiceIpaEnte = getCodiceIpaEnte(richiesta.getIdEnte());
			Documento documento = buildDocumento(codiceIpaEnte, richiesta, attributi);
			MyDocsRichiestaEntity logRichiesta = buildMyDocsRichiestaEntity(richiesta, documento);
//			String responseUUID = getUuidIfDocPrincipalePubblicato(logRichiesta);
			
			String responseUUID = null;
//			if (responseUUID == null || responseUUID.equals("")) {
				// Chiama MyDocs per il documento principale
				try {
					responseUUID = mydocsDAO.insertDocumento(codiceIpaEnte, documento);
					logRichiesta.setUuidMydocs(responseUUID);
					logRichiesta.setStato(MyDocsRichiestaEntity.Stato.TX.name());
//					mydocsRichiestaDAO.insert(logRichiesta);
					pubblicatoAdesso = true;					
					logRichiesta.setCodiceEsito(MyDocsRichiestaEntity.Esito.PUB.name());
					logRichiesta.setDescEsito("File pubblicato");					
				} catch (Exception e) {							
					logRichiesta.setStato(MyDocsRichiestaEntity.Stato.KO.name());
					logRichiesta.setCodiceEsito(MyDocsRichiestaEntity.Esito.ERR.name());
					logRichiesta.setDescEsito("File non pubblicato");
				}
				
				if (idRichiesta != null) {
					logRichiesta.setIdRichiesta(idRichiesta);
					mydocsRichiestaDAO.updateResponseById(logRichiesta);	
				}
				else {
					mydocsRichiestaDAO.insert(logRichiesta);
				}
				
				if (logRichiesta.getStato().equals(MyDocsRichiestaEntity.Stato.KO.name())) {								
					//Email di notifica a supporto.moon@csi.it					
					Istanza istanza = istanzeService.getIstanzaById(richiesta.getIdIstanza());   
					new SendEmailMyDocsTask(logRichiesta, istanza).call();
				}

//				
//			}
			return new MdResponse(responseUUID, pubblicatoAdesso);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::pubblicaDocumentoPrincipale] DAOException "+daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::pubblicaDocumentoPrincipale] BusinessException "+(be.getMessage()==null?be:be.getMessage()));
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::pubblicaDocumentoPrincipale] Exception "+(e.getMessage()==null?e:e.getMessage()));
			throw new BusinessException();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::pubblicaDocumentoPrincipale] END");
		}
	}
	
	private void validaRichiesta(RichiestaPubblicazioneMyDocs richiesta) throws BusinessException {
		if (richiesta==null || 
			richiesta.getContent()==null ||
			richiesta.getIdEnte()==null ||
			StringUtils.isEmpty(richiesta.getCfCittadino()) ||
			StringUtils.isEmpty(richiesta.getFilename()) ||
			StringUtils.isEmpty(richiesta.getDescrizione())
			) {
			LOG.error("[" + CLASS_NAME + "::validaRichiesta] richiesta invalida " + richiesta);
			throw new BusinessException("RichiestaPubblicazioneMyDocs invalida");
		}
	}

	private Documento buildDocumento(String codiceIpaEnte, RichiestaPubblicazioneMyDocs richiesta, MapMyDocsAttributi attributi) {
		Date now = new Date();
		Documento result = new Documento();
		result.setCfCittadino(richiesta.getCfCittadino());
		result.setDataCreazione(SDF_ddMMyyyy.format(now));
		result.setDataInizio(SDF_ddMMyyyy.format(now));
		result.setDescrizione(richiesta.getDescrizione());
		result.setFilename(richiesta.getFilename());
		result.setIdAmbito(retrieveIdAmbito(codiceIpaEnte, richiesta, attributi));		
		//TEST ambito non esistente per generare errore
//		result.setIdAmbito(Long.valueOf(44));
		
		result.setIdTipologia(retrieveIdTipologia(codiceIpaEnte, richiesta, attributi));
		result.setContent(richiesta.getContent());
		return result;
	}
	
	//
//	private MyDocsConf getMyDocsConf(RichiestaPubblicazioneMyDocs richiesta) {
//		Map<Integer,MyDocsConf> confEnte = confByEnti.get(richiesta.getIdEnte());
//		if (confEnte==null || confEnte.get(richiesta.getTipoDoc().getId())==null) {
//			confEnte = retrieveMyDocsConf(confEnte, richiesta);
//			confByEnti.put(richiesta.getIdEnte(), confEnte);
//		}
//		MyDocsConf result = confEnte.get(richiesta.getTipoDoc().getId());
//		return result;
//	}

//	private Map<Integer, MyDocsConf> retrieveMyDocsConf(Map<Integer,MyDocsConf> confEnte, RichiestaPubblicazioneMyDocs richiesta) {
//		if (confEnte==null) {
//			confEnte = new HashMap<Integer,MyDocsConf>();
//		}
//		if (confEnte.get(richiesta.getTipoDoc().getId())!=null) {
//			return confEnte;
//		}
//		Long idAmbito = retrieveIdAmbito(richiesta);
//		Long idTipologia = retrieveIdTipologia(richiesta);
//		if (idAmbito==null || idAmbito==null) {
//			LOG.error("[" + CLASS_NAME + "::retrieveMyDocsConf] MyDocsConf NOT FOUND! idEnte:" + richiesta.getIdEnte() +
//				" idTipoDoc:" + richiesta.getTipoDoc().getId() +
//				" idAmbito:" + idAmbito +
//				" idTipologia:" + idTipologia);
//			throw new BusinessException("MyDocsConf NOT FOUND");
//		}
//		confEnte.put(richiesta.getTipoDoc().getId(), new MyDocsConf(idAmbito, idTipologia));
//		return confEnte;
//	}

	
	//
	// Ambito
	private Long retrieveIdAmbito(String codiceIpaEnte, RichiestaPubblicazioneMyDocs richiesta, MapMyDocsAttributi attributi) throws BusinessException {
		Long result = null;
		// Sol.1 per CONF ID_AMBITO
		String idAmbitoStr = null;
		try {
			idAmbitoStr = attributi.getWithCorrectType(MyDocsAttributoKeys.ID_AMBITO);
			if (StringUtils.isNotBlank(idAmbitoStr)) {
				return Long.parseLong(idAmbitoStr.trim());
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveIdAmbito] ERROR parsing md_d_parametri ID_AMBITO: " + idAmbitoStr);
			throw new BusinessException("retrieveIdAmbito sol.1");
		}
		// Sol.2 per CONF AMBITO (quindi una chiamata di ricerca su mydocs in piu)
		String denominazioneAmbito = null;
		try {
			denominazioneAmbito = attributi.getWithCorrectType(MyDocsAttributoKeys.AMBITO);
			LOG.info("[" + CLASS_NAME + "::retrieveIdAmbito] Sol.2 denominazioneAmbito: " + denominazioneAmbito);
			if (StringUtils.isNotBlank(denominazioneAmbito)) {
				return findIdAmbitoOrElseThrow(codiceIpaEnte, denominazioneAmbito);
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveIdAmbito] ERROR Not found AMBITO: " + denominazioneAmbito + " in codiceIpaEnte: " + codiceIpaEnte);
			throw new BusinessException("retrieveIdAmbito sol.2");
		}
		// Sol.3 per AMBITO DEFAULT per tipologia (quindi una chiamata di ricerca su mydocs in piu)
		String denominazioneAmbitoDefault = null;
		try {
			denominazioneAmbitoDefault = richiesta.getTipoDoc().getCodiceAmbitoMydocs();
			LOG.info("[" + CLASS_NAME + "::retrieveIdAmbito] Sol.3 denominazioneAmbitoDefault: " + denominazioneAmbitoDefault);
			if (StringUtils.isNotBlank(denominazioneAmbito)) {
				return findIdAmbitoOrElseThrow(codiceIpaEnte, denominazioneAmbito);
			}
		} catch (Exception e) {
//			AmbitoResponse nuovoAmbito = creaAmbito(codiceIpaEnte, codiceAmbito);
			LOG.error("[" + CLASS_NAME + "::retrieveIdAmbito] ERROR Not found AMBITO: " + denominazioneAmbitoDefault + " in codiceIpaEnte: " + codiceIpaEnte + " per tipoDoc:" + richiesta.getTipoDoc().getId());
			throw new BusinessException("retrieveIdAmbito sol.3");
		}
		return result;
	}
	private Long findIdAmbitoOrElseThrow(String codiceIpaEnte, String codiceAmbito) {
		return mydocsDAO.listAmbiti(codiceIpaEnte).stream()
				.filter(a -> codiceAmbito.equals(a.getDenominazione()))
				.findFirst()
				.map(a -> a.getIdAmbito())
				.orElseThrow();
	}
	
	//
	// Tipologia
	private Long retrieveIdTipologia(String codiceIpaEnte, RichiestaPubblicazioneMyDocs richiesta, MapMyDocsAttributi attributi) {
		Long result = null;
		if (richiesta.getIdTipologiaMydocs()!=null)
			return richiesta.getIdTipologiaMydocs().longValue();
		//
		if (RichiestaPubblicazioneMyDocs.TipoDoc.ISTANZA.isCorrectId(richiesta.getTipoDoc().getId())) {
			result = retrieveIdTipologiaIstanza(codiceIpaEnte, attributi);
		}
		if (RichiestaPubblicazioneMyDocs.TipoDoc.DOCUMENTO_PA.isCorrectId(richiesta.getTipoDoc().getId())) {
			result = retrieveIdTipologiaDocumentoPA(richiesta);
		}
		if (result != null)
			return result;
		return retrieveIdTipologiaByDefaultDescOfTipoDoc(codiceIpaEnte, richiesta, result);
	}

	protected Long retrieveIdTipologiaByDefaultDescOfTipoDoc(String codiceIpaEnte,
			RichiestaPubblicazioneMyDocs richiesta, Long result) {
		// Sol.3 per TIPOLOGIA DEFAULT per tipologia (quindi una chiamata di ricerca su mydocs in piu)
		String descrizioneTipologiaDefault = null;
		try {
			descrizioneTipologiaDefault = richiesta.getTipoDoc().getCodiceTipologiaMydocs();
			LOG.info("[" + CLASS_NAME + "::retrieveIdTipologia] Sol.3 descrizioneTipologiaDefault: " + descrizioneTipologiaDefault);
			if (StringUtils.isNotBlank(descrizioneTipologiaDefault)) {
				result = findOrInsertIdTipologiaOrElseThrow(codiceIpaEnte, descrizioneTipologiaDefault);
//				return findIdTipologiaOrElseThrow(codiceIpaEnte, descrizioneTipologiaDefault);
			}
		} catch (Exception e) {
//			AmbitoResponse nuovoAmbito = creaAmbito(codiceIpaEnte, codiceAmbito);
			LOG.error("[" + CLASS_NAME + "::retrieveIdTipologia] ERROR Not found AMBITO: " + descrizioneTipologiaDefault + " in codiceIpaEnte: " + codiceIpaEnte + " per tipoDoc:" + richiesta.getTipoDoc().getId());
			throw new BusinessException("retrieveIdTipologia sol.3");
		}
		return result;
	}

	protected Long retrieveIdTipologiaIstanza(String codiceIpaEnte, MapMyDocsAttributi attributi) {
		Long result = null;
		// Sol.1 per CONF ID_TIPOLOGIA
		String idTipologiaStr = null;
		try {
			idTipologiaStr = attributi.getWithCorrectType(MyDocsAttributoKeys.ID_TIPOLOGIA_ISTANZA);
			if (StringUtils.isNotBlank(idTipologiaStr)) {
				result = Long.parseLong(idTipologiaStr.trim());
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveIdTipologiaIstanza] ERROR parsing md_d_parametri ID_TIPOLOGIA_ISTANZA: " + idTipologiaStr);
			throw new BusinessException("retrieveIdTipologiaIstanza ISTANZA sol.1");
		}
		// Sol.2 per CONF TIPOLOGIA (quindi una chiamata di ricerca su mydocs in piu)
		String descrizioneTipologia = null;
		try {
			descrizioneTipologia = attributi.getWithCorrectType(MyDocsAttributoKeys.TIPOLOGIA_ISTANZA);
			LOG.info("[" + CLASS_NAME + "::retrieveIdTipologiaIstanza] Sol.2 descrizioneTipologia: " + descrizioneTipologia);
			if (StringUtils.isNotBlank(descrizioneTipologia)) {
				result = findOrInsertIdTipologiaOrElseThrow(codiceIpaEnte, descrizioneTipologia);
//				return findIdTipologiaOrElseThrow(codiceIpaEnte, descrizioneTipologia);
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveIdTipologiaIstanza] ERROR Not found AMBITO: " + descrizioneTipologia + " in codiceIpaEnte: " + codiceIpaEnte);
			throw new BusinessException("retrieveIdTipologiaIstanza ISTANZA sol.2");
		}
		return result;
	}
	
	// pubblicazioneFile (repositoryFile) prendiamo idTipologia dalla richiesta
	protected Long retrieveIdTipologiaDocumentoPA(RichiestaPubblicazioneMyDocs richiesta) {
		Long result;
		// Sol.1 per CONF ID_TIPOLOGIA
		if (richiesta.getIdTipologiaMydocs()==null) {
			LOG.error("[" + CLASS_NAME + "::retrieveIdTipologiaDocumentoPA] ERROR DOCUMENTO_PA richiesta.getIdTipologiaMydocs() null  richiesta: " + richiesta);
			throw new BusinessException("retrieveIdTipologiaDocumentoPA DOCUMENTO_PA sol.1");
		}
		result = richiesta.getIdTipologiaMydocs().longValue();
		return result;
	}
	
	private Long findOrInsertIdTipologiaOrElseThrow(String codiceIpaEnte, String descrizioneTipologia) {
		try {
			LOG.info("[" + CLASS_NAME + "::findOrInsertIdTipologiaOrElseThrow] IN codiceIpaEnte: " + codiceIpaEnte + " descrizioneTipologia: " + descrizioneTipologia);
			Optional<Long> resultOpt = findIdTipologiaOpt(codiceIpaEnte, descrizioneTipologia);
			if (resultOpt.isPresent()) {
				return resultOpt.get();
			}
			return creaTipologia(codiceIpaEnte, descrizioneTipologia).getIdTipologia();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findOrInsertIdTipologiaOrElseThrow] ERROR codiceIpaEnte: " + codiceIpaEnte + " descrizioneTipologia: " + descrizioneTipologia, e);
			throw new BusinessException("Errore generico servizio findOrInsertIdTipologiaOrElseThrow");	
		}
	}

	private Optional<Long> findIdTipologiaOpt(String codiceIpaEnte, String codiceTipologia) {
		return mydocsDAO.listTipologie(codiceIpaEnte).stream()
				.filter(t -> codiceTipologia.equals(t.getDescrizione()))
				.findFirst()
				.map(t -> t.getIdTipologia());
	}
	private Long findIdTipologiaOrElseThrow(String codiceIpaEnte, String codiceTipologia) {
		return mydocsDAO.listTipologie(codiceIpaEnte).stream()
				.filter(t -> codiceTipologia.equals(t.getDescrizione()))
				.findFirst()
				.map(t -> t.getIdTipologia())
				.orElseThrow();
	}	
	
	private MyDocsRichiestaEntity buildMyDocsRichiestaEntity(RichiestaPubblicazioneMyDocs richiesta, Documento documento) {
		MyDocsRichiestaEntity result = new MyDocsRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setStato(MyDocsRichiestaEntity.Stato.EL.name());
		result.setTipoDoc(richiesta.getTipoDoc().getId());
		result.setIdIstanza(richiesta.getIdIstanza());
		result.setIdModulo(richiesta.getIdModulo());
		result.setIdArea(richiesta.getIdArea());
		result.setIdEnte(richiesta.getIdEnte());
		result.setIdFile(richiesta.getIdFile());
		result.setIdStoricoWorkflow(richiesta.getIdStoricoWorkflow());
		result.setIdAmbitoMydocs(documento.getIdAmbito());		
		result.setIdTipologiaMydocs(documento.getIdTipologia());
		return result;
	}
	
	private String getUuidIfDocPrincipalePubblicato(MyDocsRichiestaEntity richiesta) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::getUuidIfDocPrincipalePubblicato] BEGIN");
		String resultUUID = "";

		MyDocsRichiestaFilter filter = new MyDocsRichiestaFilter();
		filter.setIdIstanza(richiesta.getIdIstanza());
		filter.setTipoDoc(richiesta.getTipoDoc());
		filter.setStato(ProtocolloRichiestaEntity.Stato.TX.name());
		List<MyDocsRichiestaEntity> elencoRichieste = mydocsRichiestaDAO.find(filter);
		if (elencoRichieste != null && elencoRichieste.size() > 0) {
			for (MyDocsRichiestaEntity r: elencoRichieste) {
				if (! StringUtils.isEmpty(r.getUuidMydocs()) && 
						("000".equals(r.getCodiceEsito()) || "001".equals(r.getCodiceEsito())) ) {
					resultUUID = r.getUuidMydocs();
				}
			}
		}
		return resultUUID;
	}
	
	
	//
	// PING
	//  
	@Override
	public String ping() throws BusinessException {
		try {
			String result = mydocsDAO.ping();
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::ping] Errore generico servizio ping", ex);
			throw new BusinessException("Errore generico servizio elenco ping");
		} 
	}
	
	
	//
	// AMBITI
	//
	@Override
	public List<AmbitoResponse> listAmbitiByIdEnte(Long idEnte) throws BusinessException {
		try {
			List<AmbitoResponse> result = mydocsDAO.listAmbiti(getCodiceIpaEnte(idEnte));
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::listAmbitiByIdEnte] Errore generico servizio listAmbitiByIdEnte", ex);
			throw new BusinessException("Errore generico servizio elenco listAmbitiByIdEnte");
		} 
    }
	@Override
	public List<AmbitoResponse> listAmbitiByCodiceEnte(String codiceEnte) throws BusinessException {
		try {
			List<AmbitoResponse> result = mydocsDAO.listAmbiti(getCodiceIpaEnte(codiceEnte));
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::listAmbitiByCodiceEnte] Errore generico servizio listAmbitiByCodiceEnte", ex);
			throw new BusinessException("Errore generico servizio elenco listAmbitiByCodiceEnte");
		} 
    }
	@Override
	public List<AmbitoResponse> listAmbitiByCodiceIpaEnte(String codiceIpaEnte) throws BusinessException {
		try {
			List<AmbitoResponse> result = mydocsDAO.listAmbiti(codiceIpaEnte);
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::listAmbitiByCodiceIpaEnte] Errore generico servizio listAmbitiByCodiceIpaEnte", ex);
			throw new BusinessException("Errore generico servizio elenco listAmbitiByCodiceIpaEnte");
		} 
    }
	@Override
	public AmbitoResponse getAmbitoById(Long idEnte, Long idAmbito) throws BusinessException {
		try {
			AmbitoResponse result = mydocsDAO.getAmbitoById(getCodiceIpaEnte(idEnte), idAmbito);
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getAmbitoById] Errore generico servizio getAmbitoById", ex);
			throw new BusinessException("Errore generico servizio elenco getAmbitoById");
		} 
    }
	private AmbitoResponse creaAmbito(String codiceIpaEnte, String descrizioneAmbito) {
		try {
			Ambito ambito = new Ambito();
			ambito.setNomeAmbito(descrizioneAmbito);
			ambito.setIdVisibilitaAmbito(MYDOCS_AMBITO_ID_VISIBILITA_DEFAULT);
			ambito.setCodiceAmbito(descrizioneAmbito); // TODO ??
			ambito.setColor(MYDOCS_AMBITO_COLOR_DEFAULT);
			ambito.setAttoreUpd(MYDOCS_AMBITO_ATTORE_DEFAULT);
			AmbitoResponse result = mydocsDAO.insertAmbito(codiceIpaEnte, ambito);
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::creaAmbito] Errore generico servizio creaAmbito " + descrizioneAmbito + " con codiceIpaEnte:" + codiceIpaEnte, ex);
			throw new BusinessException("Errore generico servizio elenco creaAmbito");
		} 
	}
	
	
	//
	// TIPOLOGIE
	//    
	@Override
	public List<TipologiaResponse> listTipologieByIdEnte(Long idEnte) throws BusinessException {
		try {
			List<TipologiaResponse> result = mydocsDAO.listTipologie(getCodiceIpaEnte(idEnte));
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::listTipologieByIdEnte] Errore generico servizio listTipologieByIdEnte", ex);
			throw new BusinessException("Errore generico servizio elenco listTipologieByIdEnte");
		} 
    }
	@Override
	public List<TipologiaResponse> listTipologieByCodiceEnte(String codiceEnte) throws BusinessException {
		try {
			List<TipologiaResponse> result = mydocsDAO.listTipologie(getCodiceIpaEnte(codiceEnte));
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::listTipologieByCodiceEnte] Errore generico servizio listTipologieByCodiceEnte", ex);
			throw new BusinessException("Errore generico servizio elenco listTipologieByCodiceEnte");
		} 
    }
	@Override
	public List<TipologiaResponse> listTipologieByCodiceIpaEnte(String codiceIpaEnte) throws BusinessException {
		try {
			List<TipologiaResponse> result = mydocsDAO.listTipologie(codiceIpaEnte);
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::listTipologieByCodiceIpaEnte] Errore generico servizio listTipologieByCodiceIpaEnte", ex);
			throw new BusinessException("Errore generico servizio elenco listTipologieByCodiceIpaEnte");
		} 
    }
	@Override
	public TipologiaResponse getTipologiaById(Long idEnte, Long idTipologia) throws BusinessException {
		try {
			TipologiaResponse result = mydocsDAO.getTipologiaById(getCodiceIpaEnte(idEnte), idTipologia);
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getTipologiaById] Errore generico servizio getTipologiaById", ex);
			throw new BusinessException("Errore generico servizio elenco getTipologiaById");
		} 
    }
	private TipologiaResponse creaTipologia(String codiceIpaEnte, String codiceTipologia) {
		try {
			Tipologia tipologia = new Tipologia();
			tipologia.setDescrizione(codiceTipologia);
			tipologia.setIdEstensione(MYDOCS_ID_ESTENSIONE_PDF); // 1-PDF
			tipologia.setMimetype(MYDOCS_MIME_TYPE_APPLICATION_PDF);
			tipologia.setDimensioneMassima(0);
			tipologia.setGiorniVita(1825); // 5 anni = 365*5
			TipologiaResponse result = mydocsDAO.insertTipologia(codiceIpaEnte, tipologia);
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::creaTipologia] Errore generico servizio creaTipologia " + codiceTipologia + " con codiceIpaEnte:" + codiceIpaEnte, ex);
			throw new BusinessException("Errore generico servizio elenco creaTipologia");
		} 
	}
	
	
	//
	// DOCUMENTI
	// generici
	@Override
	public DocumentiResponse findDocumenti(Long idEnte, FiltroDocumento filtro) throws BusinessException {
		try {
			DocumentiResponse result = mydocsDAO.findDocumenti(getCodiceIpaEnte(idEnte), filtro);
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::findDocumenti] Errore generico servizio findDocumenti", ex);
			throw new BusinessException("Errore generico servizio elenco findDocumenti");
		}
	}

	
	//
	// Ente
	private String getCodiceIpaEnte(Long idEnte) throws BusinessException {
		try {
			String result = enteDAO.findById(idEnte).getCodiceIpa();
			LOG.debug("[" + CLASS_NAME + "::getCodiceIpaEnte] idEnte: " + idEnte + " OUT codiceIpa: " + result);
			if (StringUtils.isEmpty(result)) {
				LOG.error("[" + CLASS_NAME + "::getCodiceIpaEnte] codiceIpaEnte vuoto per idEnte: " + idEnte);
				throw new BusinessException();
			}
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCodiceIpaEnte] Identificazione codiceIpaEnte impossibile idEnte: " + idEnte, e);
			throw new BusinessException(e);
		}
	}
	private String getCodiceIpaEnte(String codiceEnte) throws BusinessException {
		try {
			String result = enteDAO.findByCodice(codiceEnte).getCodiceIpa();
			LOG.debug("[" + CLASS_NAME + "::getCodiceIpaEnte] codiceEnte: " + codiceEnte + " OUT codiceIpa: " + result);
			if (StringUtils.isEmpty(result)) {
				LOG.error("[" + CLASS_NAME + "::getCodiceIpaEnte] codiceIpaEnte vuoto per codiceEnte: " + codiceEnte);
				throw new BusinessException();
			}
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCodiceIpaEnte] Identificazione codiceIpaEnte impossibile per codiceEnte: " + codiceEnte, e);
			throw new BusinessException(e);
		}
	}
	
	//
	// DOCUMENTI
	// specifici
	@Override
	public String pubblicaIstanza(Long idIstanza, Long idRichiesta) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::pubblicaIstanza] IN idIstanza: " + idIstanza);
			return pubblicaIstanza(istanzeService.getIstanzaById(idIstanza), idRichiesta);
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::pubblicaIstanza] ");
			throw e;
		}
	}
	@Override
	public String pubblicaIstanza(Istanza istanza, Long idRichiesta) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::pubblicaIstanza] IN istanza: "+istanza);
				LOG.debug("[" + CLASS_NAME + "::pubblicaIstanza] IN istanza.idIstanza: "+istanza.getIdIstanza());
				LOG.debug("[" + CLASS_NAME + "::pubblicaIstanza] IN istanza.idModulo: "+istanza.getModulo().getIdModulo());
			}
			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte());
			istanza.setIdArea(areaModulo.getIdArea()); // TODO Da vedere se valorizzarlo da sempre molto prima, per il momento per sicurezza si rivalorizza
			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
			MapMyDocsAttributi attributi = getMapMyDocsAttributi(areaModulo);
			//
			IstanzaPdfEntity pdfE = printIstanzeService.getIstanzaPdfEntityById(istanza.getIdIstanza());
			LOG.debug("[" + CLASS_NAME + "::pubblicaIstanza] pdfE.getContenuto().length=" + pdfE.getContenutoPdf().length);
			//
			RichiestaPubblicazioneMyDocs richiesta = new RichiestaPubblicazioneMyDocs();
			richiesta.setCfCittadino(istanza.getCodiceFiscaleDichiarante());
			richiesta.setContent(pdfE.getContenutoPdf());
			richiesta.setDescrizione(RichiestaPubblicazioneMyDocs.TipoDoc.ISTANZA.getCodiceTipologiaMydocs()); // From conf by modulo ??
			richiesta.setFilename(istanza.getCodiceIstanza() + ".pdf");
			richiesta.setIdFile(null);
			richiesta.setIdEnte(istanza.getIdEnte());
			richiesta.setIdArea(areaModulo.getIdArea());
			richiesta.setIdIstanza(istanza.getIdIstanza());
			richiesta.setIdModulo(istanza.getModulo().getIdModulo());
			richiesta.setIdStoricoWorkflow(null);
			richiesta.setTipoDoc(RichiestaPubblicazioneMyDocs.TipoDoc.ISTANZA);
			return pubblicaDocumentoPrincipale(richiesta, attributi, idRichiesta).getUuidPrincipale();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::pubblicaIstanza] Errore generico servizio pubblicaIstanza", ex);
			throw new BusinessException("Errore generico servizio elenco pubblicaIstanza");
		}
	}
	@Override
	public String pubblicaFile(Long idFile, Long idRichiesta) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::pubblicaFile] IN idFile: " + idFile);
			RepositoryFileEntity repositoryFile = repositoryFileDAO.findById(idFile);
			LOG.debug("[" + CLASS_NAME + "::pubblicaFile] repositoryFile.getContenuto().length=" + repositoryFile.getContenuto().length);
			//
			Istanza istanza = istanzeService.getIstanzaById(repositoryFile.getIdIstanza());
			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte()); // Un modulo DEVE essere solo una volta in un ente.
			istanza.setIdArea(areaModulo.getIdArea()); // TODO Da vedere se valorizzarlo da sempre molto prima, per il momento per sicurezza si rivalorizza
			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
			MapMyDocsAttributi attributi = getMapMyDocsAttributi(areaModulo);
			//
			RichiestaPubblicazioneMyDocs richiesta = new RichiestaPubblicazioneMyDocs();
			richiesta.setCfCittadino(istanza.getCodiceFiscaleDichiarante());
			richiesta.setContent(repositoryFile.getContenuto());
			richiesta.setDescrizione(RichiestaPubblicazioneMyDocs.TipoDoc.DOCUMENTO_PA.getCodiceTipologiaMydocs());
			richiesta.setFilename(repositoryFile.getNomeFile());
			richiesta.setIdFile(idFile);
			richiesta.setIdEnte(istanza.getIdEnte());
			richiesta.setIdArea(areaModulo.getIdArea());
			richiesta.setIdIstanza(repositoryFile.getIdIstanza());
			richiesta.setIdModulo(istanza.getModulo().getIdModulo());
			richiesta.setIdStoricoWorkflow(repositoryFile.getIdStoricoWorkflow());
			richiesta.setTipoDoc(RichiestaPubblicazioneMyDocs.TipoDoc.DOCUMENTO_PA);
			richiesta.setIdTipologiaMydocs(repositoryFile.getIdTipologia());
			return pubblicaDocumentoPrincipale(richiesta, attributi, idRichiesta).getUuidPrincipale();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::pubblicaFile] Errore generico servizio pubblicaFile", ex);
			throw new BusinessException("Errore generico servizio elenco pubblicaFile");
		}
	}
	
//	public String pubblicaFile(Long idFile, Long idIstanza, Long idStoricoWorkflow) throws BusinessException {
//		try {
//			LOG.debug("[" + CLASS_NAME + "::pubblicaFile] IN idFile: " + idFile);
//			LOG.debug("[" + CLASS_NAME + "::pubblicaFile] IN idIstanza: " + idIstanza);
//			LOG.debug("[" + CLASS_NAME + "::pubblicaFile] IN idStoricoWorkflow: " + idStoricoWorkflow);
//			RepositoryFileEntity repositoryFile = repositoryFileDAO.findById(idFile);
//			LOG.debug("[" + CLASS_NAME + "::pubblicaFile] repositoryFile.getContenuto().length=" + repositoryFile.getContenuto().length);
//			//
//			Istanza istanza = istanzeService.getIstanzaById(idIstanza);
//			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte()); // Un modulo DEVE essere solo una volta in un ente.
//			istanza.setIdArea(areaModulo.getIdArea()); // TODO Da vedere se valorizzarlo da sempre molto prima, per il momento per sicurezza si rivalorizza
//			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
//			MapMyDocsAttributi attributi = getMapMyDocsAttributi(areaModulo);
//			//
//			RichiestaPubblicazioneMyDocs richiesta = new RichiestaPubblicazioneMyDocs();
//			richiesta.setCfCittadino(istanza.getCodiceFiscaleDichiarante());
//			richiesta.setContent(repositoryFile.getContenuto());
//			richiesta.setDescrizione(RichiestaPubblicazioneMyDocs.TipoDoc.DOCUMENTO_PA.getCodiceTipologiaMydocs());
//			richiesta.setFilename(repositoryFile.getNomeFile());
//			richiesta.setIdFile(idFile);
//			richiesta.setIdEnte(istanza.getIdEnte());
//			richiesta.setIdArea(areaModulo.getIdArea());
//			richiesta.setIdIstanza(idIstanza);
//			richiesta.setIdModulo(istanza.getModulo().getIdModulo());
//			richiesta.setIdStoricoWorkflow(idStoricoWorkflow);
//			richiesta.setTipoDoc(RichiestaPubblicazioneMyDocs.TipoDoc.DOCUMENTO_PA);
//			richiesta.setIdTipologiaMydocs(repositoryFile.getIdTipologia()); //
//			return pubblicaDocumentoPrincipale(richiesta, attributi).getUuidPrincipale();
//		} catch (Exception ex) {
//			LOG.error("[" + CLASS_NAME + "::pubblicaFile] Errore generico servizio pubblicaFile", ex);
//			throw new BusinessException("Errore generico servizio elenco pubblicaFile");
//		}
//	}
	
	
	public String pubblicaMyDocs(Long idIstanza, Long idStoricoWorkflow) throws BusinessException {
	
			LOG.debug("[" + CLASS_NAME + "::pubblicaMyDocs] - id istanza: "+ idIstanza+" - id storico workflow: " + idStoricoWorkflow);		
			List<RepositoryFileLazyEntity> listFiles = repositoryFileDAO.findLazyMydocsByIdIstanzaStoricoWf(idIstanza, idStoricoWorkflow);
			
			String res = "OK";
			for (RepositoryFileLazyEntity file : listFiles) {			
				try {
					res = pubblicaFile(file.getIdFile(), null);
					LOG.info("[" + CLASS_NAME + "::pubblicaMyDocs] idFile=" + file.getIdFile() + " call result: "+res);
				} catch (DAOException e) {
					LOG.error("[" + CLASS_NAME + "::pubblicaMyDocs] idFile=" + file.getIdFile() + " call failed: "+ e.getCode()+ " - "+e.getMessage());
//					throw new BusinessException(e);
				}
			}
			return res;		
	}

	
	//
	//
	private MapMyDocsAttributi getMapMyDocsAttributi(AreaModuloEntity areaModulo) {
		List<MyDocsParametroEntity> listAttributi = mydocsParametroDAO.findForModulo(areaModulo);
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getMapMyDocsAttributi] listAttributi (non filtrati, con possibile dupplicati) ma ordinata ! \n" + listAttributi);
		}
		if (listAttributi==null || listAttributi.size()==0) {
			LOG.error("[" + CLASS_NAME + "::getMapMyDocsAttributi] ERROR Impossibile effettuare la pubblicazione. Nessun configurazione trovata per ente/area/modulo = " + areaModulo.getIdEnte() + "/" + areaModulo.getIdArea() + "/" + areaModulo.getIdModulo());
			throw new BusinessException("Impossibile effettuare la pubblicazione. Nessun configurazione trovata per ente/area/modulo = " + areaModulo.getIdEnte() + "/" + areaModulo.getIdArea() + "/" + areaModulo.getIdModulo(), "MOONSRV-30612");
		}
		MapMyDocsAttributi attributi = new MapMyDocsAttributi(listAttributi);
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getMapMyDocsAttributi] attributi (senza dupplicati) \n" + attributi);
		}
		return attributi;
	}

	@Override
	public List<MyDocsRichiestaEntity> findByIdIstanza(Long idIstanza) throws BusinessException {
		try {			
			MyDocsRichiestaFilter filter = new MyDocsRichiestaFilter();	
			filter.setIdIstanza(idIstanza);
			List<MyDocsRichiestaEntity> elencoRichieste = mydocsRichiestaDAO.find(filter);
					
			return elencoRichieste;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanza] Errore generico servizio findByIdIstanza", ex);
			throw new BusinessException("Errore generico servizio elenco findByIdIstanza");
		} 
	}
	
	public List<ProtocolloParametro> getMyDocsParametri(Long idModulo) {
		List<ProtocolloParametro> result = new ArrayList<>();
		List<MyDocsParametroEntity> listAttributi = mydocsParametroDAO.findAllByIdModulo(idModulo);
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getMyDocsParametri] listAttributi (non filtrati, con possibile duplicati) ma ordinata ! \n" + listAttributi);
		}
		if (listAttributi==null || listAttributi.size()==0) {
			return result;
		}
//		MapProtocolloAttributi attributi = new MapProtocolloAttributi(listAttributi);
		Map<String, MyDocsParametroEntity> attributiMap = new HashMap<>();
		listAttributi.forEach( mae -> { attributiMap.put(mae.getNomeAttributo(), mae); });

		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getMyDocsParametri] attributiMap (senza dupplicati) \n" + attributiMap);
		}
		Map<Long, Ente> entiCacheMap = new HashMap<>();
		Map<Long, Area> areeCacheMap = new HashMap<>();
		
		result = List.copyOf(attributiMap.values()).stream()
			.map(MyDocsParametroMapper::buildFromEntity)
			.map(pp -> completaEnte(pp, entiCacheMap))
			.map(pp -> completaArea(pp, areeCacheMap))
			//.map(pp -> completaOrder(pp))
			.collect(Collectors.toList());

		return result;
	}	
	
	
	private ProtocolloParametro completaEnte(ProtocolloParametro pp, Map<Long, Ente> cacheMap) throws DAOException {
		Ente ente = null;
		if (pp.getEnte()!=null && pp.getEnte().getIdEnte()!=null) {
			ente = cacheMap.get(pp.getEnte().getIdEnte());
			if (ente==null) {
				ente = entiService.getEnteById(pp.getEnte().getIdEnte());
				cacheMap.put(pp.getEnte().getIdEnte(), ente);
			}
		}
		pp.setEnte(ente);
		return pp;
	}
	private ProtocolloParametro completaArea(ProtocolloParametro pp, Map<Long, Area> cacheMap) throws DAOException {
		Area area = null;
		if (pp.getArea()!=null && pp.getArea().getIdArea()!=null) {
			area = cacheMap.get(pp.getArea().getIdArea());
			if (area==null) {
				area = areeService.getAreaById(pp.getArea().getIdArea());
				cacheMap.put(pp.getArea().getIdArea(), area);
			}
		}
		pp.setArea(area);
		return pp;
	}
	private ProtocolloParametro completaOrder(ProtocolloParametro pp) throws DAOException {
		pp.setOrder(ProtocolloAttributoKeys.byName(pp.getNomeAttributo()).getOrder());
		return pp;
	}
	
	
	
//	public List<MyDocsParametro> getMyDocsParametri(Long idModulo) {
//		List<ProtocolloParametro> result = new ArrayList<ProtocolloParametro>();
//		List<MyDocsParametroEntity> listAttributi = mydocsParametroDAO.findAllByIdModulo(idModulo);
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("[" + CLASS_NAME + "::getProtocolloParametri] listAttributi (non filtrati, con possibile dupplicati) ma ordinata ! \n" + listAttributi);
//		}
//		if (listAttributi==null || listAttributi.size()==0) {
//			return result;
//		}
////		MapMyDocsAttributi attributi = new MapMyDocsAttributi(listAttributi);
//		Map<String, MyDocsParametroEntity> attributiMap = new HashMap<String, MyDocsParametroEntity>();
//		listAttributi.forEach( mae -> { attributiMap.put(mae.getNomeAttributo(), mae); });
//
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("[" + CLASS_NAME + "::getProtocolloParametri] attributiMap (senza dupplicati) \n" + attributiMap);
//		}
//		Map<Long, Ente> entiCacheMap = new HashMap<Long, Ente>();
//		Map<Long, Area> areeCacheMap = new HashMap<Long, Area>();
//		result = List.copyOf(attributiMap.values()).stream()
//			.map(ProtocolloParametroMapper::buildFromEntity)
//			.map(pp -> completaEnte(pp, entiCacheMap))
//			.map(pp -> completaArea(pp, areeCacheMap))
//			.map(pp -> completaOrder(pp))
//			.collect(Collectors.toList());
//
//		return result;
//	}
//	private ProtocolloParametro completaEnte(ProtocolloParametro pp, Map<Long, Ente> cacheMap) throws DAOException {
//		Ente ente = null;
//		if (pp.getEnte()!=null && pp.getEnte().getIdEnte()!=null) {
//			ente = cacheMap.get(pp.getEnte().getIdEnte());
//			if (ente==null) {
//				ente = entiService.getEnteById(pp.getEnte().getIdEnte());
//				cacheMap.put(pp.getEnte().getIdEnte(), ente);
//			}
//		}
//		pp.setEnte(ente);
//		return pp;
//	}
//	private ProtocolloParametro completaArea(ProtocolloParametro pp, Map<Long, Area> cacheMap) throws DAOException {
//		Area area = null;
//		if (pp.getArea()!=null && pp.getArea().getIdArea()!=null) {
//			area = cacheMap.get(pp.getArea().getIdArea());
//			if (area==null) {
//				area = areeService.getAreaById(pp.getArea().getIdArea());
//				cacheMap.put(pp.getArea().getIdArea(), area);
//			}
//		}
//		pp.setArea(area);
//		return pp;
//	}
//	private ProtocolloParametro completaOrder(ProtocolloParametro pp) throws DAOException {
//		pp.setOrder(ProtocolloAttributoKeys.byName(pp.getNomeAttributo()).getOrder());
//		return pp;
//	}
}
