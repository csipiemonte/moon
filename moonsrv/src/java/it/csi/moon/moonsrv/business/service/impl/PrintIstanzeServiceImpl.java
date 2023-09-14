/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 *
 */
package it.csi.moon.moonsrv.business.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.DatiAggiuntivi;
import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.MoonError;
import it.csi.moon.commons.dto.ResocontoAllegato;
import it.csi.moon.commons.dto.StoricoWorkflow;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.commons.entity.AllegatoEntity;
import it.csi.moon.commons.entity.DatiAzioneEntity;
import it.csi.moon.commons.entity.ErroreEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzaPdfEntity;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.ModuloStrutturaEntity;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.RepositoryFileLazyEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.commons.mapper.DocumentoMapper;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.commons.util.decodifica.DecodificaError;
import it.csi.moon.commons.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.commons.util.decodifica.DecodificaTipoRepositoryFile;
import it.csi.moon.commons.util.decodifica.EnumComponent;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.RepositoryFileService;
import it.csi.moon.moonsrv.business.service.WorkflowService;
import it.csi.moon.moonsrv.business.service.helper.ResocontoXMLBuilder;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ErroreDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IndexDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaPdfDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloStrutturaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.MoonprintDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.wf.CosmoDAO;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.PrintIstanzaMapper;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.PrintIstanzaMapperFactory;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.others.PrintIstanzaDocIntegrazioneMapperDefault;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.others.PrintIstanzaMapperDefault;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.Constants;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi alla stampa delle istanze
 *
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
@Configurable
public class PrintIstanzeServiceImpl  implements PrintIstanzeService {

	private static final String CLASS_NAME = "PrintIstanzeServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final byte[] FOURTH_HEAD_BYTES_PDF = new byte[] { 0x25, 0x50, 0x44, 0x46 }; // %PDF
	private static final byte[] FIFTH_TAIL_BYTES_PDF = new byte[] { 0x25, 0x25, 0x45, 0x4f, 0x46 }; //%%EOF
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	ModuloStrutturaDAO moduloStrutturaDAO;
	@Autowired
	MoonprintDAO moonprintDAO;
	@Autowired
	IstanzaPdfDAO istanzaPdfDAO;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	WorkflowDAO workflowDAO;
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	RepositoryFileService repositoryFileService;
	@Autowired
	@Qualifier("apimint")
	CosmoDAO cosmoDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	ErroreDAO erroreDAO;
	@Autowired
	IndexDAO indexDAO;
	@Autowired
	ModuloDAO moduloDAO;
	
	@Override
	public byte[] generaSalvaPdf(Long idIstanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::generaSalvaPdf] IN idIstanza: "+idIstanza);
			// Recupero l'istanza e richiama il secondo servizo
			Istanza istanza = istanzeService.getIstanzaById(idIstanza);
			Integer idStato = istanza.getStato().getIdStato();
			if (DecodificaStatoIstanza.BOZZA.isCorrectStato(idStato ) || DecodificaStatoIstanza.COMPLETATA.isCorrectStato(idStato)) {
				LOG.error("[" + CLASS_NAME + "::generaSalvaPdf] NON si genera pdf persistente su istanza in Bozza o Completata.");
				throw new BusinessException("NON si genera pdf persistente su istanza in Bozza o Completata.","MOONSRV-10050");
			}
			// call locale service printPdf
			byte[] bytes = printPdf(istanza);
			// salva
			istanzaPdfDAO.insert(makeIstanzaPdf(istanza, bytes));
			
			return bytes;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::generaSalvaPdf] ");
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::generaSalvaPdf] Errore generico", e);
			throw new BusinessException("Errore generico generaSalvaPdf");
		}
	}

	private IstanzaPdfEntity makeIstanzaPdf(Istanza istanza, byte[] bytes) {
		try {
			IstanzaPdfEntity istanzaPdf = new IstanzaPdfEntity();
			istanzaPdf.setIdIstanza(istanza.getIdIstanza());
			istanzaPdf.setIdModulo(istanza.getModulo().getIdModulo());
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(bytes);
			String hashPdfIstanza = Base64.getEncoder().encodeToString(hash);
			istanzaPdf.setHashPdf(hashPdfIstanza);
			istanzaPdf.setContenutoPdf(bytes);
			istanzaPdf.setResoconto(generateResoconto(istanza,hashPdfIstanza));
			istanzaPdf.setAttoreIns("ADMIN");
			istanzaPdf.setDataIns(new Date());
			return istanzaPdf;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::makeIstanzaPdf] Errore generico", e);
			throw new BusinessException();
		}
	}
	
	/**
	 * Xml creato all’invio dell’istanza, deve contenere 
	 * - Codice istanza 
	 * - Utente compilante nome, cognome, cf, 
	 * - data e ora di login 
	 * - dati provider (presi dal jwt) 
	 * - nome del pdf dell’istanza 
	 * - hash del file 
	 * - nomi di ciascun allegato 
	 * - hash Data 
	 * - ora di invio pdf generato 
	 * - hash 
	 * @param istanza
	 * @param hashPdfIstanza 
	 * @return
	 */
	private String generateResoconto(Istanza istanza, String hashPdfIstanza)  {
		String result = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			ResocontoXMLBuilder resocontoBuilder = new ResocontoXMLBuilder();
			DatiAggiuntivi datiAggiuntivi = retrieveDatiAggiuntivi(istanza);
			if (datiAggiuntivi != null) {
				resocontoBuilder.setProvider(datiAggiuntivi.getProvider());
				resocontoBuilder.setDataLogin(datiAggiuntivi.getDataOraLogin()!=null?sf.format(datiAggiuntivi.getDataOraLogin()):null);
			}
			// test se dati aggiuntivi not null 
			resocontoBuilder.setCodiceIstanza(istanza.getCodiceIstanza());
			resocontoBuilder.setCognomeUtenteCompilante(istanza.getCognomeDichiarante());
			resocontoBuilder.setNomeUtenteCompilante(istanza.getNomeDichiarante());
			resocontoBuilder.setCfUtenteCompilante(istanza.getCodiceFiscaleDichiarante());
			resocontoBuilder.setDataInvio(sf.format(istanza.getCreated()));	
			resocontoBuilder.setNomePdfIstanza(istanza.getCodiceIstanza() + ".pdf");
			resocontoBuilder.setHashPdfIstanza(hashPdfIstanza);
			
			List<AllegatoEntity> allegati = allegatoDAO.findByIdIstanza(istanza.getIdIstanza());
			for (AllegatoEntity allegato : allegati) {
				resocontoBuilder.addResocontoAllegato(new ResocontoAllegato(allegato.getNomeFile(), allegato.getHashFile()));
			}
			return resocontoBuilder.buildXmlIndented(); 				
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::generateResoconto] DAOException", e);
			return null;
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::generateResoconto] Exception", e);
			return null;
		}
	}
	
	private DatiAggiuntivi retrieveDatiAggiuntivi(Istanza istanza) throws Exception  {
		if (istanza==null || StringUtils.isEmpty(istanza.getDatiAggiuntivi())) {
			return null;
		}
		DatiAggiuntivi datiAggiuntivi = objectMapper.readValue(istanza.getDatiAggiuntivi(),DatiAggiuntivi.class);
		return datiAggiuntivi;
	}

	
	/**
	 * Genera PDF e lo ritorna 
	 * Lo salva (insert or update) se stato diverso da bozza o completato
	 * @param idIstanza
	 * @return
	 */
//	@Override
	public IstanzaPdfEntity generaPdfProvaSalvareSeStatoLoPermette(Long idIstanza) {
		try {
			IstanzaPdfEntity istanzaPdf;
			LOG.debug("[" + CLASS_NAME + "::generaPdfProvaSalvareSeStatoLoPermette] IN idIstanza: "+idIstanza);
			// Recupero l'istanza e richiama il secondo servizo
			Istanza istanza = istanzeService.getIstanzaById(idIstanza);
			Integer idStato = istanza.getStato().getIdStato();
			// call locale service printPdf
			byte[] bytes = printPdf(istanza);
			if (DecodificaStatoIstanza.BOZZA.isCorrectStato(idStato ) || DecodificaStatoIstanza.COMPLETATA.isCorrectStato(idStato)) {
				istanzaPdf = new IstanzaPdfEntity();
				istanzaPdf.setContenutoPdf(bytes);
				return istanzaPdf; // si ritorna un oggetto con solo byte[]
			}
			// salva
			istanzaPdf = makeIstanzaPdf(istanza, bytes);
			try {
				istanzaPdfDAO.insert(istanzaPdf);
			} catch (DAOException daoe) {
				if (daoe.getMessage().contains("AKConstraintViolation")) {
					istanzaPdf.setAttoreUpd("ADMIN");
					istanzaPdf.setDataUpd(new Date());
					istanzaPdfDAO.update(istanzaPdf);
				} else {
					LOG.error("[" + CLASS_NAME + "::generaPdfProvaSalvareSeStatoLoPermette] Impossibile salvare il pdf generato per idIstanza = " + idIstanza);
				}
			}
			return istanzaPdf;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::generaPdfProvaSalvareSeStatoLoPermette] ");
			throw e;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::generaPdfProvaSalvareSeStatoLoPermette] Errore generico", e);
			throw new BusinessException("Errore generico generaPdfProvaSalvareSeStatoLoPermette");
		}
	}

	@Override
	public byte[] getPdfById(Long idIstanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getPdfById] IN idIstanza: "+idIstanza);
		return getIstanzaPdfEntityById(idIstanza).getContenutoPdf();
	}
	
	@Override
	public byte[] getNotificaById(Long idIstanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getNotificaById] IN idIstanza: "+idIstanza);
		RepositoryFileLazyEntity fileAllegato = getFirstRepositoryFileNotificaByIdIstanza(idIstanza);
		byte[] contenuto = null;
		if (fileAllegato != null &&  fileAllegato.getRefUrl() != null && !fileAllegato.getRefUrl().contentEquals("null")) {
			String[] urlParts = fileAllegato.getRefUrl().split("/");
			String idPratica = urlParts[urlParts.length - 2];
			contenuto = cosmoDAO.getContenuto(idPratica);
		} else if (fileAllegato != null){
			contenuto = repositoryFileDAO.findById(fileAllegato.getIdFile()).getContenuto();
		}
		return contenuto;
	}
	
	
	@Override
	public byte[] getDocumentoByFormioNameFile(String formioNameFile) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getNotificaByFormioNameFile] IN formioNameFile: "+formioNameFile);
		RepositoryFileEntity fileAllegato = repositoryFileDAO.findByFormioNameFile(formioNameFile);
		byte[] contenuto = null;
		if (fileAllegato != null &&  fileAllegato.getRefUrl() != null && !fileAllegato.getRefUrl().contentEquals("null")) {
			String[] urlParts = fileAllegato.getRefUrl().split("/");
			String idPratica = urlParts[urlParts.length - 2];
			contenuto = cosmoDAO.getContenuto(idPratica);
		} else if (fileAllegato != null){
			contenuto = fileAllegato.getContenuto();
		}		
		return contenuto;
	}
	
	@Override
	public byte[] getDocumentoByIdFile(Long idFile) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getNotificaByIdFile] IN idFile: "+idFile);
		RepositoryFileEntity fileAllegato = repositoryFileDAO.findById(idFile);
		byte[] contenuto = null;
		if (fileAllegato != null &&  fileAllegato.getRefUrl() != null && !fileAllegato.getRefUrl().contentEquals("null")) {
			String[] urlParts = fileAllegato.getRefUrl().split("/");
			String idPratica = urlParts[urlParts.length - 2];
			contenuto = cosmoDAO.getContenuto(idPratica);
		} else if (fileAllegato != null){
			contenuto = fileAllegato.getContenuto();
		}		
		return contenuto;
	}
	
	@Override
	public Documento getFirstDocumentoNotificaByIdIstanza(Long idIstanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getFirstDocumentoNotificaByIdIstanza] IN idIstanza: "+idIstanza);
		RepositoryFileEntity repoFileNotifica = getFirstRepositoryFileNotificaByIdIstanza(idIstanza);		
		Documento result = (repoFileNotifica!=null) ? DocumentoMapper.buildFromEntity(repoFileNotifica) : null;
		return result;
	}
	
	private RepositoryFileEntity getFirstRepositoryFileNotificaByIdIstanza(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getFirstRepositoryFileNotificaByIdIstanza] IN idIstanza: " + idIstanza);
			List<String> listAzioni = Arrays.asList("CREA_RISPOSTA_CON_PROTOCOLLO", "ACCOGLI", "RESPINGI");
			StoricoWorkflowEntity storicoRicevuta = storicoWorkflowDAO.findLastStoricoListAzioni(idIstanza, listAzioni);
			RepositoryFileEntity fileEntity = null;
			if (storicoRicevuta.getIdFileRendering() != null) {
				fileEntity = repositoryFileService.getRepositoryFileEntity(storicoRicevuta.getIdFileRendering());
			} else {
				List<RepositoryFileLazyEntity> allegati = repositoryFileDAO.findLazyByIdIstanzaStoricoWf(idIstanza,	storicoRicevuta.getIdStoricoWorkflow());
				fileEntity = (allegati != null && allegati.size() > 0) ? repositoryFileDAO.findById(allegati.get(0).getIdFile()) : null;
			}
			return fileEntity;
		} catch (BusinessException be) {
			throw be;
		} catch (DAOException daoe) {
			throw new BusinessException(daoe);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getFirstRepositoryFileNotificaByIdIstanza] Error");
			throw e;
		}
	}
	
	@Override
	public IstanzaPdfEntity getIstanzaPdfEntityById(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaPdfEntityById] IN idIstanza: "+idIstanza);
			// Recupero l'istanza e richiama il secondo servizo
			Optional<IstanzaPdfEntity> pdf = istanzaPdfDAO.findByIdIstanza(idIstanza);
			if (pdf.isPresent()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdfEntityById] PDF trovato.");
				return valorizzaContentByIndex(pdf.get());
			} else {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdfEntityById] PDF da generare.");
				return generaPdfProvaSalvareSeStatoLoPermette(idIstanza);
			}
		} catch (BusinessException be) {
			throw be;
		} catch (DAOException daoe) {
			throw new BusinessException(daoe);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdfEntityById] Error");
			throw e;
		}
	}
	
	private IstanzaPdfEntity valorizzaContentByIndex(IstanzaPdfEntity istanzaPdfEntity) {
		if(istanzaPdfEntity.getContenutoPdf() == null && istanzaPdfEntity.getUuidIndex() != null) {
			istanzaPdfEntity.setContenutoPdf(indexDAO.getContentByUid(istanzaPdfEntity.getUuidIndex()));
		}
		return istanzaPdfEntity;
	}
	
	@Override
	public RepositoryFileEntity getRenderedFileEntityByIdSw(Long idIstanza, Long idStoricoWorkflow, DecodificaTipoRepositoryFile dTipoRepositoryFile) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getRenderedFileEntityByIdSw] IN idIstanza: "+idIstanza+" idStoricoWorkflow: "+idStoricoWorkflow);
			// Recupero l'istanza e richiama il secondo servizo
			Optional<RepositoryFileEntity> pdf = repositoryFileDAO.findRenderedFileByIdIstanzaStoricoTipo(idIstanza, idStoricoWorkflow, dTipoRepositoryFile.getId());
			if (pdf.isPresent()) {
				LOG.debug("[" + CLASS_NAME + "::getRenderedFileEntityByIdSw] PDF trovato.");
				return valorizzaContentByIndex(pdf.orElseThrow());
			} else {
				LOG.debug("[" + CLASS_NAME + "::getRenderedFileEntityByIdSw] PDF da generare.");
				return renderPdfProvaSalvare(idIstanza, idStoricoWorkflow, dTipoRepositoryFile);
			}
		} catch (BusinessException be) {
			throw be;
		} catch (DAOException daoe) {
			throw new BusinessException(daoe);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRenderedFileEntityByIdSw] Error");
			throw e;
		}
	}
	
	private RepositoryFileEntity valorizzaContentByIndex(RepositoryFileEntity repositoryFileEntity) {
		if(repositoryFileEntity.getContenuto() == null && repositoryFileEntity.getUuidIndex() != null) {
			repositoryFileEntity.setContenuto(indexDAO.getContentByUid(repositoryFileEntity.getUuidIndex()));
		}
		return repositoryFileEntity;
	}

	public RepositoryFileEntity renderPdfProvaSalvare(Long idIstanza, Long idStoricoWorkflow, DecodificaTipoRepositoryFile dTipoRepositoryFile) {
		try {
			RepositoryFileEntity resultRenderedFile;
			LOG.debug("[" + CLASS_NAME + "::renderPdfProvaSalvare] IN idIstanza: "+idIstanza);
			byte[] bytes = printPdfIntegrazione(idIstanza, idStoricoWorkflow);
			// salva
			resultRenderedFile = makeRenderedFile(idIstanza, idStoricoWorkflow, dTipoRepositoryFile, bytes);
			try {
				Long idRepositoryFileEntity = repositoryFileDAO.insert(resultRenderedFile);
				resultRenderedFile.setIdFile(idRepositoryFileEntity);
			} catch (DAOException daoe) {
					LOG.error("[" + CLASS_NAME + "::renderPdfProvaSalvare] Impossibile salvare il pdf generato per idIstanza = " + idIstanza + " idStoricoWorkflow = " + idStoricoWorkflow, daoe);
			}
			try {
				storicoWorkflowDAO.updateIdFileRendered(idStoricoWorkflow, resultRenderedFile.getIdFile());
			} catch (DAOException daoe) {
					LOG.error("[" + CLASS_NAME + "::renderPdfProvaSalvare] Impossibile salvare id_file_rendering per idIstanza = " + idIstanza + " idStoricoWorkflow = " + idStoricoWorkflow + " resultRenderedFile.getIdFile() = " + resultRenderedFile.getIdFile(), daoe);
			}
			return resultRenderedFile;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::renderPdfProvaSalvare] ");
			throw e;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::renderPdfProvaSalvare] Errore generico", e);
			throw new BusinessException("Errore generico renderPdfProvaSalvare");
		}
	}
	
	private RepositoryFileEntity makeRenderedFile(Long idIstanza, Long idStoricoWorkflow, DecodificaTipoRepositoryFile dTipoRepositoryFile, byte[] bytes) {
		try {
			RepositoryFileEntity renderedFile = new RepositoryFileEntity();
			renderedFile.setIdIstanza(idIstanza);
			renderedFile.setIdStoricoWorkflow(idIstanza);
			renderedFile.setNomeFile("Integrazione_" + idStoricoWorkflow + ".pdf");
			renderedFile.setCodiceFile(UUID.randomUUID().toString());
			renderedFile.setContenuto(bytes);
			renderedFile.setFlEliminato("N");
			renderedFile.setFlFirmato("N");
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(bytes);
			String hashPdfIstanza = Base64.getEncoder().encodeToString(hash);
			renderedFile.setHashFile(hashPdfIstanza);
			renderedFile.setContentType("application/pdf");
			renderedFile.setLunghezza(bytes.length);
			renderedFile.setIdTipologia(dTipoRepositoryFile.getId());
			renderedFile.setDataCreazione(new Date());
			return renderedFile;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::makeIstanzaPdf] Errore generico", e);
			throw new BusinessException();
		}
	}

	@Override
	public byte[] printPdf(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::printPDF] IN idIstanza: "+idIstanza);
			// Recupero l'istanza e richiama il secondo servizo
			return printPdf(istanzeService.getIstanzaById(idIstanza));
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::printPdf] ");
			throw e;
		}
	}

	@Override
	public byte[] printPdfClassLoader(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::printPdfClassLoader] IN idIstanza: "+idIstanza);
			// Recupero l'istanza e richiama il secondo servizo
			Istanza istanza = istanzeService.getIstanzaById(idIstanza);
			
			final String methodName = "printPdfClassLoader";
			final long start = System.currentTimeMillis();
			byte[] result = null;
			MoonprintDocument moonPrintParam = null;
			try {
				try {
					moonPrintParam = _remapClassLoader(istanza);
				} catch (BusinessException be) {
					LOG.warn("[" + CLASS_NAME + "::" + methodName + "] BusinessException idIstanza="+istanza.getIdIstanza(), be);
					// already logged
					return result;
				}
				moonPrintParam.setTemplate(retrieveTemplateByStato(istanza.getStato().getIdStato(),istanza.getModulo().getCodiceModulo()));
				result = validaPdf(moonprintDAO.printPdf(moonPrintParam));
			} catch (BusinessException be) {
				LOG.error("[" + CLASS_NAME + "::" + methodName + "] BusinessException idIstanza="+istanza.getIdIstanza(), be);
				trace(be, DecodificaError.MOONSRV_10052_NOT_VALID_PDF.getError(), CLASS_NAME, methodName, istanza, moonPrintParam, start);
			} catch (DAOException e) {
				LOG.error("[" + CLASS_NAME + "::" + methodName + "] DAOException idIstanza="+istanza.getIdIstanza(), e);
				trace(e, DecodificaError.MOONSRV_10051_IMPOSSIBLE_GENERARE_STAMPA_PDF.getError(), CLASS_NAME, methodName, istanza, moonPrintParam, start);
			}
			return result;
			
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::printPdfClassLoader] ");
			throw e;
		}
	}
	/**
	 * Servizio principale di stampa PDF, converte e richiama moonprint
	 */
	@Override
	public byte[] printPdf(Istanza istanza) throws BusinessException {
		final String methodName = "printPdf";
		final long start = System.currentTimeMillis();
		byte[] result = null;
		MoonprintDocument moonPrintParam = null;
		try {
			try {
				moonPrintParam = _remap(istanza);
			} catch (BusinessException be) {
				LOG.warn("[" + CLASS_NAME + "::" + methodName + "] BusinessException idIstanza="+istanza.getIdIstanza());
				// already logged
				return result;
			}
			moonPrintParam.setTemplate(retrieveTemplateByStato(istanza.getStato().getIdStato(),istanza.getModulo().getCodiceModulo()));
			byte[] bytes = moonprintDAO.printPdf(moonPrintParam);
			if (bytes!=null && bytes.length==74) {
				LOG.warn("[" + CLASS_NAME + "::" + methodName + "] bytes.length==74 retry ...");
				bytes = moonprintDAO.printPdf(moonPrintParam);
				LOG.info("[" + CLASS_NAME + "::" + methodName + "] after retry bytes.length=" + (bytes!=null?bytes.length:"null"));
			}
			result = validaPdf(bytes);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] BusinessException idIstanza="+istanza.getIdIstanza(), be);
			trace(be, DecodificaError.MOONSRV_10052_NOT_VALID_PDF.getError(), CLASS_NAME, methodName, istanza, moonPrintParam, start);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] DAOException idIstanza="+istanza.getIdIstanza(), e);
			trace(e, DecodificaError.MOONSRV_10051_IMPOSSIBLE_GENERARE_STAMPA_PDF.getError(), CLASS_NAME, methodName, istanza, moonPrintParam, start);
		}
		return result;
	}

	private String retrieveTemplateByStato(Integer idStato, String codiceModulo) {
		String tipoTemplate = MoonprintDocument.TEMPLATE_DEFAULT;
		if (DecodificaStatoIstanza.BOZZA.isCorrectStato(idStato) || DecodificaStatoIstanza.COMPLETATA.isCorrectStato(idStato)) {			
			switch (codiceModulo.toUpperCase()) {
				case Constants.CODICE_MODULO_DEM_CINDCAF:
				case Constants.CODICE_MODULO_DEM_CRESCAF:
					tipoTemplate = "BozzaCaf";
					break;
				default:
					tipoTemplate = MoonprintDocument.TEMPLATE_BOZZA;
			}
		}
		return tipoTemplate;
	}

	private MoonprintDocument _remap(Istanza istanza) throws BusinessException {
		final String methodName = "printPdf";
		final long start = System.currentTimeMillis();
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::_remap] IN istanza: "+istanza);
				LOG.debug("[" + CLASS_NAME + "::_remap] IN istanza.idIstanza: "+istanza.getIdIstanza());
				LOG.debug("[" + CLASS_NAME + "::_remap] IN istanza.idModulo: "+istanza.getModulo().getIdModulo());
				LOG.debug("[" + CLASS_NAME + "::_remap] IN istanza.idEnte: "+istanza.getIdEnte());
			}
			ModuloStrutturaEntity strutturaEntity = moduloStrutturaDAO.findByIdVersioneModulo(istanza.getModulo().getIdVersioneModulo());
			PrintIstanzaMapper mapper = getPrintMapper(istanza);
			return mapper.remap(istanza, strutturaEntity);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::_remap] ItemNotFoundDAOException for idVersioneModulo=" + istanza.getModulo().getIdVersioneModulo() + 
				" idModulo=" + istanza.getModulo().getIdModulo() + " idIstanza=" + istanza.getIdIstanza() + " codicedIstanza=" + istanza.getCodiceIstanza());
			trace(notFoundEx, DecodificaError.MOONSRV_10053_NOT_FOUND.getError(), CLASS_NAME, methodName, istanza, null, start);
			throw new BusinessException(DecodificaError.MOONSRV_10053_NOT_FOUND.getError());
		} catch (DAOException daoEx) {
			LOG.error("[" + CLASS_NAME + "::_remap] DAOException for idVersioneModulo=" + istanza.getModulo().getIdVersioneModulo() + 
				" idModulo=" + istanza.getModulo().getIdModulo() + " idIstanza=" + istanza.getIdIstanza() + " codicedIstanza=" + istanza.getCodiceIstanza());
			trace(daoEx, DecodificaError.MOONSRV_10054_DAO.getError(), CLASS_NAME, methodName, istanza, null, start);
			throw new BusinessException(DecodificaError.MOONSRV_10054_DAO.getError());
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::_remap] Exception for idVersioneModulo=" + istanza.getModulo().getIdVersioneModulo() +
				" idModulo=" + istanza.getModulo().getIdModulo() + " idIstanza=" + istanza.getIdIstanza() + " codicedIstanza=" + istanza.getCodiceIstanza(), e);
			trace(e, DecodificaError.MOONSRV_10050_PRINT_ISTANZA_MAPPER.getError(), CLASS_NAME, methodName, istanza, null, start);
			throw new BusinessException(DecodificaError.MOONSRV_10050_PRINT_ISTANZA_MAPPER.getError());
		}
	}

	protected PrintIstanzaMapper getPrintMapper(Istanza istanza) {
		PrintIstanzaMapper mapper = null;
		List<ModuloAttributoEntity> listAttributi = moduloAttributiDAO.findByIdModulo(istanza.getModulo().getIdModulo());
		MapModuloAttributi attributi = new MapModuloAttributi(listAttributi);
		Boolean forceStampaDinamica = attributi.getWithCorrectType(ModuloAttributoKeys.STAMPA_DINAMICA); // null ; true
		if (Boolean.TRUE.equals(forceStampaDinamica)) {
			mapper = new PrintIstanzaMapperDefault();
		} else {
			mapper = new ClassLoaderModulo().findPrintMapperByIdModuloNullable(istanza.getModulo().getIdModulo());
			if (mapper==null) {
				if(istanza.getModulo().getCodiceModulo()==null) {
					ModuloEntity moduloE = moduloDAO.findById(istanza.getModulo().getIdModulo());
					istanza.getModulo().setCodiceModulo(moduloE.getCodiceModulo());
				}
				mapper = new PrintIstanzaMapperFactory().getPrintIstanzaMapper(istanza.getModulo().getCodiceModulo(), istanza.getModulo().getVersioneModulo()); // , istanza.getIdEnte()
			}
		}
		return mapper;
	}
	
	private MoonprintDocument _remapClassLoader(Istanza istanza) throws BusinessException {
		final String methodName = "printPdf";
		final long start = System.currentTimeMillis();
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::_remapClassLoader] IN istanza: "+istanza);
				LOG.debug("[" + CLASS_NAME + "::_remapClassLoader] IN istanza.idIstanza: "+istanza.getIdIstanza());
				LOG.debug("[" + CLASS_NAME + "::_remapClassLoader] IN istanza.idModulo: "+istanza.getModulo().getIdModulo());
				LOG.debug("[" + CLASS_NAME + "::_remapClassLoader] IN istanza.idEnte: "+istanza.getIdEnte());
			}
			ModuloStrutturaEntity strutturaEntity = moduloStrutturaDAO.findByIdVersioneModulo(istanza.getModulo().getIdVersioneModulo());
			PrintIstanzaMapper mapper = getPrintMapperNoFactory(istanza);
			return mapper.remap(istanza, strutturaEntity);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::_remapClassLoader] ItemNotFoundDAOException for idVersioneModulo=" + istanza.getModulo().getIdVersioneModulo() + 
				" idModulo=" + istanza.getModulo().getIdModulo());
			trace(notFoundEx, DecodificaError.MOONSRV_10053_NOT_FOUND.getError(), CLASS_NAME, methodName, istanza, null, start);
			throw new BusinessException(DecodificaError.MOONSRV_10053_NOT_FOUND.getError());
		} catch (DAOException daoEx) {
			LOG.error("[" + CLASS_NAME + "::_remapClassLoader] DAOException for idVersioneModulo=" + istanza.getModulo().getIdVersioneModulo() + 
				" idModulo=" + istanza.getModulo().getIdModulo());
			trace(daoEx, DecodificaError.MOONSRV_10054_DAO.getError(), CLASS_NAME, methodName, istanza, null, start);
			throw new BusinessException(DecodificaError.MOONSRV_10054_DAO.getError());
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::_remapClassLoader] Exception for idVersioneModulo=" + istanza.getModulo().getIdVersioneModulo() +
				" idModulo=" + istanza.getModulo().getIdModulo(), e);
			trace(e, DecodificaError.MOONSRV_10050_PRINT_ISTANZA_MAPPER.getError(), CLASS_NAME, methodName, istanza, null, start);
			throw new BusinessException(DecodificaError.MOONSRV_10050_PRINT_ISTANZA_MAPPER.getError());
		}
	}

	protected PrintIstanzaMapper getPrintMapperNoFactory(Istanza istanza) {
		List<ModuloAttributoEntity> listAttributi = moduloAttributiDAO.findByIdModulo(istanza.getModulo().getIdModulo());
		MapModuloAttributi attributi = new MapModuloAttributi(listAttributi);
		
		Boolean forceStampaDinamica = attributi.getWithCorrectType(ModuloAttributoKeys.STAMPA_DINAMICA);
		PrintIstanzaMapper mapper = Boolean.TRUE.equals(forceStampaDinamica)?
			new PrintIstanzaMapperDefault():
			new ClassLoaderModulo().findPrintMapperByIdModulo(istanza.getModulo().getIdModulo());
		return mapper;
	}

	private void trace(Exception e, MoonError error, String className, String methodName, Istanza istanza, MoonprintDocument moonPrintParam) throws BusinessException {
		trace(e, error, className, methodName, istanza, moonPrintParam, Optional.empty());
	}
	private void trace(Exception e, MoonError error, String className, String methodName, Istanza istanza, MoonprintDocument moonPrintParam, long start) throws BusinessException {
		trace(e, error, className, methodName, istanza, moonPrintParam, Optional.of(start));
	}
	
	//
	private void trace(Exception e, MoonError error, String className, String methodName, Istanza istanza, MoonprintDocument moonPrintParam, Optional<Long> start) throws BusinessException {
		final long end = System.currentTimeMillis();
		final long msec = start.isPresent()?(end - start.get()):0;
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::trace] =================================================================================");
				LOG.debug("[" + CLASS_NAME + "::trace] className = " + className);
				LOG.debug("[" + CLASS_NAME + "::trace] methodName = " + methodName);
				LOG.debug("[" + CLASS_NAME + "::trace] error = " + error);
				LOG.debug("[" + CLASS_NAME + "::trace] e.name = " + e.getClass().getName());
				LOG.debug("[" + CLASS_NAME + "::trace] e.length() = " + ExceptionUtils.getStackTrace(e).length());
				LOG.debug("[" + CLASS_NAME + "::trace] e.stackTrace = " + ExceptionUtils.getStackTrace(e));
				LOG.debug("[" + CLASS_NAME + "::trace] e.message = " + ExceptionUtils.getMessage(e));
				LOG.debug("[" + CLASS_NAME + "::trace] e.rootCauseMessage = " + ExceptionUtils.getRootCauseMessage(e));
				LOG.debug("[" + CLASS_NAME + "::trace] moonPrintParam = " + moonPrintParam);
				LOG.debug("[" + CLASS_NAME + "::trace] getInetAddress = " + getInetAddress());
				LOG.debug("[" + CLASS_NAME + "::trace] elaps (ms) = " + msec);
				LOG.debug("[" + CLASS_NAME + "::trace] =================================================================================");
			}
		} catch (Exception ex) {
			LOG.warn("[" + CLASS_NAME + "::trace] Exception LOG.debug ", ex);
		}
		try {
			ErroreEntity ee = new ErroreEntity();
			ee.setUuid(UUID.randomUUID().toString());
			ee.setIdComponente(EnumComponent.MOONSRV.value);
			ee.setInetAdress(getInetAddress());
			ee.setDataIns(new Timestamp(System.currentTimeMillis()));
			ee.setAttoreIns(null);
			ee.setCodice(error!=null?error.getCode():null);
			ee.setMessage(error!=null?error.getMsg():null);
			ee.setClassName(className);
			ee.setMethodName(methodName);
			ee.setIdIstanza(istanza!=null?istanza.getIdIstanza():null);
			ee.setIdModulo(istanza!=null&&istanza.getModulo()!=null?istanza.getModulo().getIdModulo():null);
			ee.setExClassName(e.getClass().getName());
			ee.setExMessage(pruneExceptionString(ExceptionUtils.getMessage(e)));
			ee.setExCause(pruneExceptionString(ExceptionUtils.getRootCauseMessage(e)));
			ee.setExTrace(pruneExceptionString(ExceptionUtils.getStackTrace(e)));
			ee.setInfo(buildInfo(moonPrintParam));
			ee.setElapsedTimeMs(msec);
			LOG.debug("[" + CLASS_NAME + "::trace] INSERT ...");
// TODO scomment for tracing on DEVMODE-TRACE
			erroreDAO.insert(ee);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::trace] TraceException", ex);
		}
	}

	private String buildInfo(MoonprintDocument moonPrintParam) {
		try {
			if (moonPrintParam==null)
				return null;
			return objectMapper.writeValueAsString(moonPrintParam);
		} catch (JsonProcessingException e) {
			LOG.error("[" + CLASS_NAME + "::buildInfo] JsonProcessingException", e);
			return null;
		}
	}

	private String pruneExceptionString(String strEx) {
		return strEx==null?null:strEx.replace("\\$", "");
	}

	protected void logDebugCharAt(String trace, int pos) {
		LOG.debug("[" + CLASS_NAME + "::trace] charAt(1898) = " + trace.charAt(pos) + " - " + (int)trace.charAt(pos));
	}
	
	private static String getInetAddress() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			return "" + ip;
		} catch (UnknownHostException e) {
			LOG.error("[" + CLASS_NAME + "::getInetAddress] UnknownHostException ", e);
			return null;
		}
	}
	
	
	// for test
	@Override
	public MoonprintDocument remap(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::remap] IN idIstanza: "+idIstanza);

			// Recupero l'istanza
			Istanza istanza = istanzeService.getIstanzaById(idIstanza);

			// Richiama il secondo servizo
			return _remap(istanza);

		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::remap] ", e);
			throw e;
		}
	}

	@Override
	public byte[] printPdfIntegrazione(Long idIstanza, Long idStoricoWorkflow) {
		try {
			LOG.info("[" + CLASS_NAME + "::printPdfIntegrazione] IN idIstanza: "+idIstanza+"  idStoricoWorkflow: "+idStoricoWorkflow);
			StoricoWorkflow storicoWorkflow = retrieveStoricoWorkflow(idIstanza, idStoricoWorkflow);
			DatiAzioneEntity datiAzioneE = retrieveModuloIntegrazione(storicoWorkflow);
			IstanzaEntity istanzaE = istanzaDAO.findById(idIstanza);
			MoonprintDocument moonPrintDocIntegrazione = generateMoonPrintDocIntegrazione(storicoWorkflow, datiAzioneE, istanzaE);
			return validaPdf(moonprintDAO.printPdf(moonPrintDocIntegrazione));
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::printPdfIntegrazione] BusinessException ");
			throw be;
		}
	}


	private StoricoWorkflow retrieveStoricoWorkflow(Long idIstanza, Long idStoricoWorkflow) {
		StoricoWorkflow storicoWorkflow = workflowService.getStoricoWorkflowById(idStoricoWorkflow); // DATI
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::retrieveStoricoWorkflow] storicoWorkflow: "+storicoWorkflow);
		}
		if (StrUtils.isEmpty(storicoWorkflow.getDatiAzione())) {
			throw new BusinessException("StoricoWorkflow.datiAzione vuoto.");
		}
		if (!idIstanza.equals(storicoWorkflow.getIdIstanza())) {
			throw new BusinessException("StoricoWorkflow non dell'istanza.");
		}
		return storicoWorkflow;
	}

	private DatiAzioneEntity retrieveModuloIntegrazione(StoricoWorkflow storicoWorkflow) {
		WorkflowFilter workflowFilter = new WorkflowFilter();
		workflowFilter.setIdProcesso(storicoWorkflow.getIdProcesso());
		workflowFilter.setIdStatoWfPartenza(storicoWorkflow.getIdStatoWfPartenza());
		workflowFilter.setIdStatoWfArrivo(storicoWorkflow.getIdStatoWfArrivo());
		Workflow workflow = workflowService.getWorkflowByFilter(workflowFilter);
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::retrieveModuloIntegrazione] workflow: "+workflow);
		}
		DatiAzioneEntity datiAzioneE = workflowDAO.findDatiAzioneById(workflow.getIdDatiAzione()); // MODULO
		return datiAzioneE;
	}

	private MoonprintDocument generateMoonPrintDocIntegrazione(StoricoWorkflow storicoWorkflow,	DatiAzioneEntity datiAzioneE, IstanzaEntity istanzaE) {
		PrintIstanzaDocIntegrazioneMapperDefault mapper = new PrintIstanzaDocIntegrazioneMapperDefault();
		MoonprintDocument moonPrintDocIntegrazione = mapper.remap(storicoWorkflow, datiAzioneE, istanzaE);
		return moonPrintDocIntegrazione;
	}
	
	/**
	 * Valida che bytes inizia con %PDF
	 * Commentato il controllo sulla fine del file, sembra che venga generato con un file DOS con \n in piu final
	 * @param bytes
	 * @return bytes se inizia con %PDF altrimenti Exception
	 */
	private byte[] validaPdf(byte[] bytes) {
		if (bytes==null)
			throw new BusinessException("NOT_VALID_PDF_NULL","MOONSRV-10053");
		byte[] header = new byte[] { bytes[0], bytes[1], bytes[2], bytes[3] };
		var isHeaderValid = header[0] == FOURTH_HEAD_BYTES_PDF[0] && 
				header[1] == FOURTH_HEAD_BYTES_PDF[1] && 
				header[2] == FOURTH_HEAD_BYTES_PDF[2] && 
				header[3] == FOURTH_HEAD_BYTES_PDF[3];
		if (!(isHeaderValid)) {
			LOG.error("[" + CLASS_NAME + "::validaPdf] header  actual: " + header + "  =>  " + new String(header) + " expected: %PDF");
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::validaPdf] NOT_VALID_PDF bytes = " + bytes);
			}
			throw new BusinessException("NOT_VALID_PDF","MOONSRV-10052");
		}
		return bytes;
	}

	@Override
	public String getPrintMapperName(String codiceModulo, String versioneModulo) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getPrintMapperName] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getPrintMapperName] IN versioneModulo: " + versioneModulo);
			}
			PrintIstanzaMapper mapper = new PrintIstanzaMapperFactory().getPrintIstanzaMapper(codiceModulo, versioneModulo);
			String response = mapper.getClass().getName();
			LOG.info("[" + CLASS_NAME + "::getPrintMapperName] response = " + response);
			return response;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getPrintMapperName] ERROR for codiceModulo:" + codiceModulo + "  " + be.getMessage());
			throw be;
		}
	}


}
