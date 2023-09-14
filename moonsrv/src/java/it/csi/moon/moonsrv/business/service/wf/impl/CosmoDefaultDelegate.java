/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.wf.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntPredicate;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.apimint.cosmo.v1.dto.AvviaProcessoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.AvviaProcessoFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.CreaDocumentiFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaDocumentoFruitoreContenutoRequest;
import it.csi.apimint.cosmo.v1.dto.CreaDocumentoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaPraticaFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaPraticaFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.EsitoCreazioneDocumentiFruitore;
import it.csi.apimint.cosmo.v1.dto.FileUploadResult;
import it.csi.apimint.cosmo.v1.dto.InviaSegnaleFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.InviaSegnaleFruitoreResponse;
import it.csi.cosmo.callback.v1.dto.AggiornaStatoPraticaRequest;
import it.csi.cosmo.callback.v1.dto.Esito;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ModuloAttributo;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.entity.CosmoLogPraticaEntity;
import it.csi.moon.commons.entity.CosmoLogServizioEntity;
import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.entity.RepositoryFileLazyEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.util.JsonHelper;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.CosmoLogPraticaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.CosmoLogServizioDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.wf.CosmoDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business dele chiamate verso COSMO
 * 
 * @author Laurent
 */

public class CosmoDefaultDelegate implements CosmoDelegate {

	private static final String CLASS_NAME = "CosmoDefaultDelegate";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final String COSMO_LOG_SERVIZIO_SEGNALA = "segnala";
	private static final String COSMO_LOG_SERVIZIO_DOCUMENTO = "documento";

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final String CODICE_SEGNALE_INTEGRAZIONE = "RICHIESTA_INTEGRAZIONE_DOCUMENTI";
	private Istanza istanza;
	private JsonNode conf;
	private StrReplaceHelper strReplaceHelper;
	private CosmoLogPraticaEntity logCreaPratica;
	private Optional<CosmoLogServizioEntity> logIntegraDocumento;
	private Optional<CosmoLogServizioEntity> logInviaSegnale;

//	public enum EnumCodiceTipoDocumento { ISTANZA, ALLEGATO };

//	public enum EnumCodiceTipoDocumento {
//		ISTANZA("richiesta-patrocinio"), ALLEGATO("allegati-richiesta-patrocinio");
//
//		private EnumCodiceTipoDocumento(String description) {
//			this.description = description;
//		}
//
//		public String getDescription() {
//			return description;
//		}
//
//		private final String description;
//	}

	@Autowired
//	@Qualifier("applogic") // diretto su applogic con basicAuth - Funziona ma verificare se presente la conf nell'environmente (usato solo in test quindi dev & tst-coto-01)
	@Qualifier("apimint") // via APIM internet con token
	CosmoDAO cosmoDAO;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	CosmoLogPraticaDAO cosmoLogPraticaDAO;
	@Autowired
	CosmoLogServizioDAO cosmoLogServizioDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;

	// TEST
	@Autowired
	EnteDAO enteDAO;

	public CosmoDefaultDelegate(Istanza istanza) {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		this.istanza = istanza;
		this.conf = retrieveConf(istanza); // throw new BusinessException("PSIT_COSMO_CONF non trovata nei attributi del
											// modulo","MOONSRV-30500");
											// throw new BusinessException("PSIT_COSMO_CONF non valid
											// JSON","MOONSRV-30501");
		this.strReplaceHelper = new StrReplaceHelper(istanza);
	}

	@Override
	public String creaPraticaEdAvviaProcesso() throws BusinessException {
		try {
			byte[] pdf = printIstanzeService.getPdfById(istanza.getIdIstanza());
			
			// 1. CreaPratica
			CreaPraticaFruitoreResponse pratica = creaPraticaCosmo();
			LOG.info("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] creaPraticaCosmo OK " + pratica.getIdPratica());

			if (StringUtils.isEmpty(logCreaPratica.getCreaDocumentoRisposta())) {
				// 2. UploadFiles
				List<CreaDocumentoFruitoreRequest> uploadDocs = new ArrayList<>();
				FileUploadResult fileUploadResult = cosmoDAO.fileUpload(pdf, new MediaType("application", "pdf"),
						getNomeFileDocPrincipale());
				LOG.debug("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] fileUploadResult = " + fileUploadResult);
				uploadDocs.add(newDocPrincipale(pratica, fileUploadResult));
				boolean includeAllegati = retrieveConfBooleanValue(ConfKeys.INCLUDE_ALLEGATI);
				if (includeAllegati) {
					List<AllegatoLazyEntity> allegati = allegatoDAO.findLazyByIdIstanza(istanza.getIdIstanza());
					for (AllegatoLazyEntity a : allegati) {
						fileUploadResult = cosmoDAO.fileUpload(allegatoDAO.findById(a.getIdAllegato()).getContenuto(), retrieveMediaType(a),
								a.getFormioNameFile());						
//						TEST 
//						fileUploadResult = cosmoDAO.fileUpload(null, retrieveMediaType(a),
//								a.getFormioNameFile());						
						LOG.debug("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] fileUploadResult = "
								+ fileUploadResult);
						uploadDocs.add(newDocAllegato(pratica, fileUploadResult, a));
					}
				}

				// 3. CreaDocumento
				EsitoCreazioneDocumentiFruitore creaDocumentoResponse = creaDocumentoCosmo(pratica, uploadDocs);
				valida(creaDocumentoResponse);
				LOG.info("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] creaDocumentoCosmo OK ");
			}

			// 4. Avvia Workflow
			avviaProcessoCosmo(pratica);
			LOG.info("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] avviaProcesso OK ");

			return "OK";
		} catch (BusinessException be) {			
			if (logCreaPratica != null ) {logErrorePratica(be);};
			LOG.error("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] BusinessException " + be.getMessage() + "\n"
					+ be.getCause());
			throw be;
		} catch (Exception e) {
			if (logCreaPratica != null ) {logErrorePratica(e);};
			LOG.error("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] Exception " + e.getMessage() + "\n"
					+ e.getCause());
			throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] END");
		}
	}

	@Override
	public String inviaIntegrazione() throws BusinessException {
		try {

			List<CosmoLogPraticaEntity> logs = cosmoLogPraticaDAO.findByIdIstanza(istanza.getIdIstanza());			
			CosmoLogPraticaEntity logPratica = logs.get(0);			
										
			logIntegraDocumento = getLogServizio(COSMO_LOG_SERVIZIO_DOCUMENTO);
			if (logIntegraDocumento.isEmpty() || (logIntegraDocumento.get().getRisposta() == null)) {
				inviaDocumentiIntegrazione(logPratica);
			}
				
			logInviaSegnale = getLogServizio(COSMO_LOG_SERVIZIO_SEGNALA);
			if (logInviaSegnale.isEmpty() || (logInviaSegnale.get().getRisposta() == null)) {
				segnalaIntegrazioneCosmo(logPratica);
			}
						
			return "OK";
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::inviaIntegrazione] BusinessException " + be.getMessage() + "\n"
					+ be.getCause());
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::inviaIntegrazione] Exception " + e.getMessage() + "\n" + e.getCause());
			throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::inviaIntegrazione] END");
		}
	}
	
	
	private void inviaDocumentiIntegrazione(CosmoLogPraticaEntity logPratica) throws BusinessException {	
		try {
			LOG.debug("[" + CLASS_NAME + "::inviaDocumentiIntegrazione] IN idIstanza = " + istanza.getIdIstanza());

			String idPratica = logPratica.getIdPratica();

			StoricoWorkflowEntity storico = storicoWorkflowDAO.findLastStoricoAzione(istanza.getIdIstanza(),
					DecodificaAzione.INVIA_INTEGRAZIONE.getIdAzione());

			// recupera elenco allegati della risposta
			List<RepositoryFileLazyEntity> allegatiRisposta = repositoryFileDAO.findLazyByIdIstanzaStoricoWf(istanza.getIdIstanza(),
					storico.getIdStoricoWorkflow());

			// trasmissione su cosmo del pdf e degli allegati opzionali
			List<CreaDocumentoFruitoreRequest> uploadDocs = new ArrayList<>();

			FileUploadResult fileUploadResult;

			for (RepositoryFileLazyEntity a : allegatiRisposta) {
				fileUploadResult = cosmoDAO.fileUpload(repositoryFileDAO.findById(a.getIdFile()).getContenuto(), retrieveMediaType(a), a.getFormioNameFile());
				LOG.debug("[" + CLASS_NAME + "::inviaDocumentiIntegrazione] fileUploadResult = " + fileUploadResult);
				uploadDocs.add(newDocAllegatoIntegrazione(idPratica, fileUploadResult, a));
			}

			// Esito creazione documenti su Cosmo
			CreaPraticaFruitoreResponse pratica = new CreaPraticaFruitoreResponse();
			EnteEntity ente = enteDAO.findById(istanza.getIdEnte());
			pratica.setCodiceIpaEnte(ente.getCodiceIpa());

			pratica.setIdPratica(idPratica);
			EsitoCreazioneDocumentiFruitore esitoDocumenti = creaDocumentoCosmoIntegrazione(logPratica, pratica,
					uploadDocs);
			if (esitoDocumenti != null) {

				LOG.debug("[" + CLASS_NAME + "::inviaDocumentiIntegrazione] response segnalazione inviata = "
						+ JsonHelper.getJsonFromObject(esitoDocumenti));
			} else {
				LOG.debug("[" + CLASS_NAME
						+ "::inviaDocumentiIntegrazione] non sono presenti documenti in allegato alla risposta di integrazione");
			}
		} catch (DAOException | BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::inviaIntegrazione] Exception " + e.getMessage() + "\n" + e.getCause());
			logServizioInviaDocumento(logPratica, null, null, e);			
			throw e;
		}

	}
	
	private Optional getLogServizio(String servizio) {
		try {
			CosmoLogServizioEntity logServizioDocumento = cosmoLogServizioDAO
					.findByIdIstanzaServizio(istanza.getIdIstanza(), servizio);
			return Optional.of(logServizioDocumento);
		} catch (ItemNotFoundDAOException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::checkLogServizio] Elemento non trovato: " + emptyEx.getMessage(),
					emptyEx);
			return Optional.empty();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::checkLogServizio] Errore database: " + e.getMessage(), e);
			return Optional.empty();
		}
	}
	

	private InviaSegnaleFruitoreResponse segnalaIntegrazioneCosmo(CosmoLogPraticaEntity logPratica) {
		InviaSegnaleFruitoreRequest segnaleReq = null;
		InviaSegnaleFruitoreResponse response = null;
		boolean servizioDone = false;
		try {
			segnaleReq = new InviaSegnaleFruitoreRequest();
			segnaleReq.setCodiceSegnale(CODICE_SEGNALE_INTEGRAZIONE);
			segnaleReq.setRichiediCallback(null);
			segnaleReq.setVariabili(null);

			response = cosmoDAO.inviaSegnale(segnaleReq, logPratica.getIdPratica());
			logServizioInviaSegnale(logPratica, segnaleReq, response, null);			
			return response;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::segnalaIntegrazioneCosmo] DAOException " + daoe.getMessage());
			if (!servizioDone)
				logServizioInviaSegnale(logPratica, segnaleReq, response, daoe);
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::segnalaIntegrazioneCosmo] BusinessException " + be.getMessage());
			logServizioInviaSegnale(logPratica, segnaleReq, response, be);
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::segnalaIntegrazioneCosmo] Exception " + e.getMessage() + "\n" + e.getCause());
			logServizioInviaSegnale(logPratica, segnaleReq, response, e);
			throw new BusinessException();
		}
	}

	private void logServizioInviaSegnale(CosmoLogPraticaEntity logPratica, InviaSegnaleFruitoreRequest request,
			InviaSegnaleFruitoreResponse response, Exception e) {
		try {
			CosmoLogServizioEntity cosmoLog = null;
			if (logInviaSegnale.isEmpty()) {
				cosmoLog = newLogServizioInitByLogPratica(logPratica);
				cosmoLog.setServizio(COSMO_LOG_SERVIZIO_SEGNALA);
				cosmoLog.setDataIns(new Timestamp(System.currentTimeMillis()));
				cosmoLog.setDataUpd(new Timestamp(System.currentTimeMillis()));
				cosmoLog.setRichiesta(request != null ? mapper.writeValueAsString(request) : null);
				cosmoLog.setRisposta(response != null ? mapper.writeValueAsString(response) : null);
				cosmoLog.setErrore(e != null ? e.getMessage() : null);
				Long idLogServizio = cosmoLogServizioDAO.insert(cosmoLog);
				LOG.debug("[" + CLASS_NAME + "::logServizioInviaSegnale] inserted with id = " + idLogServizio);
				cosmoLog.setIdLogServizio(idLogServizio);
			} else {
				cosmoLog = logInviaSegnale.get();
				cosmoLog.setRichiesta(request != null ? mapper.writeValueAsString(request) : null);
				cosmoLog.setRisposta(response != null ? mapper.writeValueAsString(response) : null);
				cosmoLog.setDataUpd(new Timestamp(System.currentTimeMillis()));
				cosmoLog.setErrore((e != null) ? e.getMessage() : null);
				int numRecordUpdated = cosmoLogServizioDAO.update(cosmoLog);
				LOG.debug("[" + CLASS_NAME + "::logServizioInviaSegnale] num record updated = " + numRecordUpdated);
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::logServizioInviaSegnale] DAOException " + daoe.getMessage());
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::logServizioInviaSegnale] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::logServizioInviaSegnale] Exception " + ex.getMessage() + "\n"
					+ ex.getCause());
			throw new BusinessException();
		}
	}
	
	private void logServizioInviaDocumento(CosmoLogPraticaEntity logPratica, CreaDocumentiFruitoreRequest request,
			EsitoCreazioneDocumentiFruitore response, Exception e) {
		try {
			CosmoLogServizioEntity cosmoLog = null;
			if (logIntegraDocumento.isEmpty()) {
				cosmoLog = newLogServizioInitByLogPratica(logPratica);
				cosmoLog.setServizio(COSMO_LOG_SERVIZIO_DOCUMENTO);
				cosmoLog.setDataIns(new Timestamp(System.currentTimeMillis()));
				cosmoLog.setDataUpd(new Timestamp(System.currentTimeMillis()));
				cosmoLog.setRichiesta(request != null ? mapper.writeValueAsString(request) : null);
				cosmoLog.setRisposta(response != null ? mapper.writeValueAsString(response) : null);
				cosmoLog.setErrore(e != null ? e.getMessage() : null);
				Long idLogServizio = cosmoLogServizioDAO.insert(cosmoLog);
				cosmoLog.setIdLogServizio(idLogServizio);
				LOG.debug("[" + CLASS_NAME + "::logServizioInviaDocumento] inserted with id = " + idLogServizio);
			} else {
				cosmoLog = logIntegraDocumento.get();
				cosmoLog.setRichiesta(request != null ? mapper.writeValueAsString(request) : null);
				cosmoLog.setRisposta(response != null ? mapper.writeValueAsString(response) : null);
				cosmoLog.setDataUpd(new Timestamp(System.currentTimeMillis()));
				cosmoLog.setErrore((e != null) ? e.getMessage() : null);
				int numRecordUpdated = cosmoLogServizioDAO.update(cosmoLog);
				LOG.debug("[" + CLASS_NAME + "::logServizioInviaDocumento] num record updated = " + numRecordUpdated);
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::logServizioInviaDocumento] DAOException " + daoe.getMessage());
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::logServizioInviaDocumento] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::logServizioInviaDocumento] Exception " + ex.getMessage() + "\n"
					+ ex.getCause());
			throw new BusinessException();
		}
	}

	private CosmoLogServizioEntity newLogServizioInitByLogPratica(CosmoLogPraticaEntity logPratica) {
		CosmoLogServizioEntity cosmoLog = new CosmoLogServizioEntity();
		cosmoLog.setIdLogPratica(logPratica.getIdLogPratica());
		cosmoLog.setIdPratica(logPratica.getIdPratica());
		cosmoLog.setIdIstanza(logPratica.getIdIstanza());
		cosmoLog.setIdModulo(logPratica.getIdModulo());
		return cosmoLog;
	}

	/*
	 * application/vnd.openxmlformats-officedocument.presentationml.presentation
	 * application application/json application application/vnd.adobe.xdp+xml
	 * application text/plain text application/zip application
	 * application/octet-stream application application/pkcs7 application
	 * application/vnd.openxmlformats-officedocument.wordprocessingml.document
	 * application image/jpeg image application/pdf application image/png image
	 * application/vnd.oasis.opendocument.text application application/pkcs7-mime
	 * application application/msword application application/vnd.ms-excel
	 * application application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
	 * application text/xml text image/gif image
	 */
	private MediaType retrieveMediaType(AllegatoLazyEntity a) {
		if (a == null)
			return null;
		MediaType result = null;
		if (StringUtils.isNotBlank(a.getContentType())) {
			result = MediaType.valueOf(a.getContentType());
			LOG.debug("[" + CLASS_NAME + "::retrieveMediaType] FormioNameFile=" + a.getFormioNameFile() + "ContentType="
					+ a.getContentType() + "   MediaType=" + a.getMediaType() + "   SubMediaType=" + a.getSubMediaType()
					+ "   result=" + result);
		}
		return result;
	}

	private MediaType retrieveMediaType(RepositoryFileLazyEntity a) {
		if (a == null)
			return null;
		MediaType result = null;
		if (StringUtils.isNotBlank(a.getContentType())) {
			result = MediaType.valueOf(a.getContentType());
			LOG.debug("[" + CLASS_NAME + "::retrieveMediaType] FormioNameFile=" + a.getFormioNameFile() + "ContentType="
					+ a.getContentType() + "   MediaType=" + a.getContentType() + "   result=" + result);
		}
		return result;
	}

	private CreaPraticaFruitoreResponse creaPraticaFacke(String idPratica) {
		CreaPraticaFruitoreRequest practicaRequest = makeCreaPraticaReqValida();
		CreaPraticaFruitoreResponse result = new CreaPraticaFruitoreResponse();
		result.setCodiceIpaEnte(practicaRequest.getCodiceIpaEnte());
		result.setIdPratica(idPratica);
		result.setCodiceTipologia(practicaRequest.getCodiceTipologia());
		result.setOggetto(practicaRequest.getOggetto());
		result.setUtenteCreazionePratica(practicaRequest.getUtenteCreazionePratica());
		return result;
	}

	private CreaPraticaFruitoreResponse creaPraticaCosmo() throws BusinessException {
		CreaPraticaFruitoreRequest practicaRequest = null;
		CreaPraticaFruitoreResponse result = null;
		try {
			// 1. Make CreaPraticaFruitoreRequest
			practicaRequest = makeCreaPraticaReqValida();
			LOG.debug("[" + CLASS_NAME + "::creaPraticaCosmo] creaPratica Request = " + practicaRequest);

			// 2. Read or Make logCreaPratica
			logCreaPratica = readOrMakeNewLogCreaPratica(practicaRequest);
			LOG.debug("[" + CLASS_NAME + "::creaPraticaCosmo] logCreaPratica = " + logCreaPratica);

			if (StringUtils.isEmpty(logCreaPratica.getCreaRisposta())) {
				// 3. Call COSMO if necessary
				result = cosmoDAO.creaPratica(practicaRequest);
				LOG.debug("[" + CLASS_NAME + "::creaPraticaCosmo] creaPratica Response = " + result);

				// 4. log CreaPratica (insert OR update)
				logCreaPratica(practicaRequest, result, null);
			} else {
				LOG.debug(
						"[" + CLASS_NAME + "::creaPraticaCosmo] Try readValue of " + logCreaPratica.getCreaRisposta());
				result = mapper.readValue(logCreaPratica.getCreaRisposta(), CreaPraticaFruitoreResponse.class);
			}

			return result;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::creaPraticaCosmo] DAOException " + daoe.getMessage());
			logCreaPratica(practicaRequest, result, daoe);
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::creaPraticaCosmo] BusinessException " + be.getMessage());
			logCreaPratica(practicaRequest, result, be);
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaPraticaCosmo] Exception " + e.getMessage() + "\n" + e.getCause());
			logCreaPratica(practicaRequest, result, e);
			throw new BusinessException();
		}
	}

	private CosmoLogPraticaEntity readOrMakeNewLogCreaPratica(CreaPraticaFruitoreRequest practicaRequest) {
		try {
			CosmoLogPraticaEntity logCreaPratica = null;
			List<CosmoLogPraticaEntity> lastCalls = cosmoLogPraticaDAO.findByIdIstanza(istanza.getIdIstanza());
			if (lastCalls.isEmpty()) {
				logCreaPratica = makeNewLogCreaPratica(practicaRequest);
			} else {
				if (lastCalls.size() == 1) {
					logCreaPratica = lastCalls.get(0);
					if (StringUtils.isNotEmpty(logCreaPratica.getErrore())
							|| StringUtils.isEmpty(logCreaPratica.getAvviaProcessoRisposta())) {
						LOG.info("[" + CLASS_NAME + "::readOrMakeNewLogCreaPratica] RETRY LAST logCreaPratica "
								+ logCreaPratica.getIdLogPratica() + " " + logCreaPratica.getIdPratica() + " idIstanza="
								+ logCreaPratica.getIdIstanza());
					} else {
						LOG.error("[" + CLASS_NAME
								+ "::readOrMakeNewLogCreaPratica] Pratica COSMO e processo già avviato.");
						throw new BusinessException("Pratica COSMO e processo già avviato", "MOONSRV-30505");
					}
				} else {
					LOG.error("[" + CLASS_NAME
							+ "::readOrMakeNewLogCreaPratica] NOT IMPLEMENTED manage NEW call with idx if forse OR retryLast ?");
					throw new BusinessException(
							"cosmoLogPraticaDAO già existente per la pratica.  ReCall OR Retry NOT IMPLEMENTED YET !",
							"MOONSRV-30504");
				}
			}
			return logCreaPratica;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::readOrMakeNewLogCreaPratica] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::readOrMakeNewLogCreaPratica] Exception " + ex.getMessage() + "\n"
					+ ex.getCause());
			throw new BusinessException();
		}
	}
	
	private CosmoLogServizioEntity readOrMakeNewLogCreaServizio(CosmoLogPraticaEntity logPratica) {
		try {
			CosmoLogServizioEntity logServizio = null;
			List<CosmoLogServizioEntity> logs = cosmoLogServizioDAO.findByIdIstanza(istanza.getIdIstanza());
			if (logs.isEmpty()) {
				logServizio = newLogServizioInitByLogPratica(logPratica);
			} else {
				
				
//				if (lastCalls.size() == 1) {
//					logCreaPratica = lastCalls.get(0);
//					if (StringUtils.isNotEmpty(logCreaPratica.getErrore())
//							|| StringUtils.isEmpty(logCreaPratica.getAvviaProcessoRisposta())) {
//						LOG.info("[" + CLASS_NAME + "::readOrMakeNewLogCreaPratica] RETRY LAST logCreaPratica "
//								+ logCreaPratica.getIdLogPratica() + " " + logCreaPratica.getIdPratica() + " idIstanza="
//								+ logCreaPratica.getIdIstanza());
//					} else {
//						LOG.error("[" + CLASS_NAME
//								+ "::readOrMakeNewLogCreaPratica] Pratica COSMO e processo già avviato.");
//						throw new BusinessException("Pratica COSMO e processo già avviato", "MOONSRV-30505");
//					}
//				} else {
//					LOG.error("[" + CLASS_NAME
//							+ "::readOrMakeNewLogCreaPratica] NOT IMPLEMENTED manage NEW call with idx if forse OR retryLast ?");
//					throw new BusinessException(
//							"cosmoLogPraticaDAO già existente per la pratica.  ReCall OR Retry NOT IMPLEMENTED YET !",
//							"MOONSRV-30504");
//				}
			}
			return logServizio;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::readOrMakeNewLogCreaPratica] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::readOrMakeNewLogCreaPratica] Exception " + ex.getMessage() + "\n"
					+ ex.getCause());
			throw new BusinessException();
		}
	}

	private CosmoLogPraticaEntity makeNewLogCreaPratica(CreaPraticaFruitoreRequest practicaRequest)
			throws IOException, JsonGenerationException, JsonMappingException {
		CosmoLogPraticaEntity logCreaPratica = new CosmoLogPraticaEntity();
		logCreaPratica.setIdPratica(practicaRequest.getIdPratica());
		logCreaPratica.setIdIstanza(istanza.getIdIstanza());
		logCreaPratica.setIdx(1); // per gestione di un retry cambianto anche idPratica se diverso da 1
		logCreaPratica.setIdModulo(istanza.getModulo().getIdModulo());
		logCreaPratica.setDataIns(new Timestamp(System.currentTimeMillis()));
		logCreaPratica.setCreaRichiesta(mapper.writeValueAsString(practicaRequest));
		LOG.debug("[" + CLASS_NAME + "::makeNewLogCreaPratica] IN " + practicaRequest.getIdPratica() + "  OUT="
				+ logCreaPratica);
		return logCreaPratica;
	}

	private void logCreaPratica(CreaPraticaFruitoreRequest practicaRequest,
			CreaPraticaFruitoreResponse practicaResponse, Exception e) {
		try {
			logCreaPratica
					.setCreaRisposta(practicaResponse != null ? mapper.writeValueAsString(practicaResponse) : null);
			if (logCreaPratica.getIdLogPratica() == null) {
				logInsertWithErrore(e);
			} else {
				logUpdateWithErrore(e);
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::logCreaPratica] DAOException " + daoe.getMessage());
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::logCreaPratica] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::logCreaPratica] Exception " + ex.getMessage() + "\n" + ex.getCause());
			throw new BusinessException();
		}
	}

	private void logInsertWithErrore(Exception e) {
		logCreaPratica.setErrore(e != null ? e.getMessage() : null);
		Long idLogPratica = cosmoLogPraticaDAO.insert(logCreaPratica);
		logCreaPratica.setIdLogPratica(idLogPratica);
	}

	private void logUpdateWithErrore(Exception e) {
		logCreaPratica.setErrore(e != null ? e.getMessage() : null);
		logCreaPratica.setDataUpd(new Timestamp(System.currentTimeMillis()));
		cosmoLogPraticaDAO.update(logCreaPratica);
	}

	private CreaPraticaFruitoreRequest makeCreaPraticaReqValida() {
		CreaPraticaFruitoreRequest praticaReq = new CreaPraticaFruitoreRequest();
		praticaReq.setCodiceIpaEnte(retrieveConfValueWithReplaceDinamici(ConfKeys.CODICE_IPA_ENTE));
		praticaReq.setIdPratica(retrieveConfValueWithReplaceDinamici(ConfKeys.ID_PRATICA));
		praticaReq.setCodiceTipologia(retrieveConfValueWithReplaceDinamici(ConfKeys.CODICE_TIPOLOGIA)); // codice_modulo
		praticaReq.setOggetto(retrieveConfValueWithReplaceDinamici(ConfKeys.OGGETTO)); // codice_istanza cognome nome
		praticaReq.setRiassunto(retrieveConfValueWithReplaceDinamici(ConfKeys.RIASSUNTO)); // null
		praticaReq.setUtenteCreazionePratica(retrieveConfValueWithReplaceDinamici(ConfKeys.UTENTE_CREAZIONE)); // cf
		praticaReq.setMetadati(istanza.getData().toString()); // data dell'istanza
		return validaCreaPratica(praticaReq);
	}

	private CreaPraticaFruitoreRequest validaCreaPratica(CreaPraticaFruitoreRequest pratica) throws BusinessException {
		StringBuilder strB = new StringBuilder();
		if (StrUtils.isEmpty(pratica.getCodiceIpaEnte()) || pratica.getCodiceIpaEnte().contains("@@")) {
			strB.append(ConfKeys.CODICE_IPA_ENTE.getKey() + ";");
		}
		if (StrUtils.isEmpty(pratica.getIdPratica()) || pratica.getCodiceIpaEnte().contains("@@")) {
			strB.append(ConfKeys.ID_PRATICA.getKey() + ";");
		}
		if (StrUtils.isEmpty(pratica.getCodiceTipologia()) || pratica.getCodiceIpaEnte().contains("@@")) {
			strB.append(ConfKeys.CODICE_TIPOLOGIA.getKey() + ";");
		}
		if (StrUtils.isEmpty(pratica.getOggetto()) || pratica.getCodiceIpaEnte().contains("@@")) {
			strB.append(ConfKeys.OGGETTO.getKey() + ";");
		}
		if (StrUtils.isEmpty(pratica.getUtenteCreazionePratica()) || pratica.getCodiceIpaEnte().contains("@@")) {
			strB.append(ConfKeys.UTENTE_CREAZIONE.getKey() + ";");
		}
		if (strB.length() > 0) {
			throw new BusinessException("COSMO Pratica non valida : " + strB.toString(), "MOONSRV-30502");
		}
		return pratica;
	}

	private CreaDocumentiFruitoreRequest makeCreaDocumentiReqValida(CreaPraticaFruitoreResponse pratica,
			List<CreaDocumentoFruitoreRequest> uploadDocs) {
		CreaDocumentiFruitoreRequest documentiReq = new CreaDocumentiFruitoreRequest();
		documentiReq.setCodiceIpaEnte(pratica.getCodiceIpaEnte());
		documentiReq.setIdPratica(pratica.getIdPratica());
		documentiReq.setDocumenti(uploadDocs);
		return validaCreaDocumenti(documentiReq);
	}

	private CreaDocumentiFruitoreRequest validaCreaDocumenti(CreaDocumentiFruitoreRequest documenti)
			throws BusinessException {
		StringBuilder strB = new StringBuilder();
		if (StrUtils.isEmpty(documenti.getIdPratica())) {
			strB.append(ConfKeys.ID_PRATICA.getKey() + ";");
		}
		if (StrUtils.isEmpty(documenti.getCodiceIpaEnte())) {
			strB.append(ConfKeys.CODICE_IPA_ENTE.getKey() + ";");
		}
		if (documenti.getDocumenti() == null || documenti.getDocumenti().size() == 0) {
			strB.append("documenti");
		}
		if (strB.length() > 0) {
			throw new BusinessException("COSMO creaDocumenti non valida : " + strB.toString(), "MOONSRV-30503");
		}
		return documenti;
	}

	private CreaDocumentiFruitoreRequest makeCreaDocumentiReqValidaIntegrazione(CreaPraticaFruitoreResponse pratica,
			List<CreaDocumentoFruitoreRequest> uploadDocs) throws BusinessException {
		CreaDocumentiFruitoreRequest documentiReq = new CreaDocumentiFruitoreRequest();
		documentiReq.setCodiceIpaEnte(pratica.getCodiceIpaEnte());
		documentiReq.setIdPratica(pratica.getIdPratica());
		documentiReq.setDocumenti(uploadDocs);
		return validaCreaDocumentiIntegrazione(documentiReq);
	}

	private CreaDocumentiFruitoreRequest validaCreaDocumentiIntegrazione(CreaDocumentiFruitoreRequest documenti)
			throws BusinessException {
		StringBuilder strB = new StringBuilder();
		if (StrUtils.isEmpty(documenti.getIdPratica())) {
			strB.append(ConfKeys.ID_PRATICA.getKey() + ";");
		}
		if (StrUtils.isEmpty(documenti.getCodiceIpaEnte())) {
			strB.append(ConfKeys.CODICE_IPA_ENTE.getKey() + ";");
		}

		if (strB.length() > 0) {
			throw new BusinessException("COSMO creaDocumenti per integrazione non valida : " + strB.toString(),
					"MOONSRV-30503");
		}
		return documenti;
	}

	private String retrieveConfValueWithReplaceDinamici(ConfKeys keyConf) {
		return strReplaceHelper.replaceDinamici(retrieveConfTextValue(keyConf), istanza);
	}

	private String retrieveConfTextValue(ConfKeys keyConf) {
		return conf.get(keyConf.getKey()) != null ? conf.get(keyConf.getKey()).asText()
				: keyConf.getTextDefaultValue();
	}

	private boolean retrieveConfBooleanValue(ConfKeys keyConf) {
		return conf.get(keyConf.getKey()) != null ? conf.get(keyConf.getKey()).asBoolean()
				: keyConf.getBooleanDefaultValue();
	}

	//
	//
	private CreaDocumentoFruitoreRequest newDocPrincipale(CreaPraticaFruitoreResponse pratica,
			FileUploadResult fileUploadResult) {
		CreaDocumentoFruitoreRequest result = new CreaDocumentoFruitoreRequest();
		result.setId(pratica.getIdPratica());
		result.setIdPadre(null);
		result.setCodiceTipo(retrieveConfTextValue(ConfKeys.CODICE_TIPO_DOC_ISTANZA));
//		result.setContenuto(newCreaDocumentoFruitoreContenutoRequest(getNomeFileDocPrincipale(), "applicatio/pdf", 
//			String.format("Istanza di %s - %s %s", istanza.getCodiceFiscaleDichiarante(), istanza.getCognomeDichiarante(), istanza.getNomeDichiarante())));
		result.setTitolo(null);
		result.setDescrizione(null);
		result.setAutore(istanza.getCodiceFiscaleDichiarante());
		result.setUploadUUID(fileUploadResult.getUploadUUID());
		return result;
	}

	private CreaDocumentoFruitoreRequest newDocAllegato(CreaPraticaFruitoreResponse pratica,
			FileUploadResult fileUploadResult, AllegatoLazyEntity a) {
		CreaDocumentoFruitoreRequest result = new CreaDocumentoFruitoreRequest();
		result.setId(pratica.getIdPratica() + "-" + a.getCodiceFile());
//		boolean allegatoFiglio = conf.get("allegato_figlio")!=null?conf.get("allegato_figlio").asBoolean():false;
		boolean allegatoFiglio = retrieveConfBooleanValue(ConfKeys.ALLEGATO_FIGLIO);
		result.setIdPadre(allegatoFiglio ? pratica.getIdPratica() : null);
		result.setCodiceTipo(retrieveConfTextValue(ConfKeys.CODICE_TIPO_DOC_ALLEGATO));
//		result.setContenuto(newCreaDocumentoFruitoreContenutoRequest(a.getNomeFile(), a.getContentType(), 
//			String.format("Allegato %s", a.getNomeFile())));
		result.setTitolo(a.getNomeFile());
		result.setDescrizione(null);
		result.setAutore(istanza.getCodiceFiscaleDichiarante());
		result.setUploadUUID(fileUploadResult.getUploadUUID());
		return result;
	}

	private CreaDocumentoFruitoreRequest newDocPrincipaleIntegrazione(String idIntegrazione,
			FileUploadResult fileUploadResult) {
		CreaDocumentoFruitoreRequest result = new CreaDocumentoFruitoreRequest();
		result.setId(idIntegrazione);
		result.setIdPadre(null);
		result.setCodiceTipo(retrieveConfTextValue(ConfKeys.CODICE_TIPO_DOC_INTEGRAZIONE));
		result.setTitolo(null);
		result.setDescrizione(null);
		result.setAutore(istanza.getCodiceFiscaleDichiarante());
		result.setUploadUUID(fileUploadResult.getUploadUUID());
		return result;
	}

	private CreaDocumentoFruitoreRequest newDocAllegatoIntegrazione(String idPratica, FileUploadResult fileUploadResult,
			RepositoryFileLazyEntity a) {
		CreaDocumentoFruitoreRequest result = new CreaDocumentoFruitoreRequest();
		result.setId(idPratica + "-" + a.getCodiceFile());
//		boolean allegatoFiglio = retrieveConfBooleanValue(ConfKeys.ALLEGATO_FIGLIO);
//		result.setIdPadre(allegatoFiglio?idPratica:null);
		result.setIdPadre(idPratica);
		result.setCodiceTipo(retrieveConfTextValue(ConfKeys.CODICE_TIPO_DOC_INTEGRAZIONE_ALLEGATO));
		result.setTitolo(a.getNomeFile());
		result.setDescrizione(null);
		result.setAutore(istanza.getCodiceFiscaleDichiarante());
		result.setUploadUUID(fileUploadResult.getUploadUUID());
		return result;
	}

	private CreaDocumentoFruitoreContenutoRequest newCreaDocumentoFruitoreContenutoRequest(String nomeFile,
			String mimeType, String contenutoFisico) {
		CreaDocumentoFruitoreContenutoRequest result = new CreaDocumentoFruitoreContenutoRequest();
		result.setNomeFile(nomeFile);
		result.setMimeType(mimeType);
		result.setContenutoFisico(contenutoFisico);
		return result;
	}

	private String getNomeFileDocPrincipale() {
		return istanza.getCodiceIstanza() + ".pdf";
	}

	private String getNomeFileRispostaIntegrazione() {
		return istanza.getCodiceIstanza() + "_" + "rispostaIntegrazione" + ".pdf";
	}

	//
	// CREA DOCUMENTO
	//

	private EsitoCreazioneDocumentiFruitore creaDocumentoCosmo(CreaPraticaFruitoreResponse pratica,
			List<CreaDocumentoFruitoreRequest> uploadDocs) {
		CreaDocumentiFruitoreRequest creaDocumentoReq = null;
		EsitoCreazioneDocumentiFruitore result = uploadDocs.isEmpty() ? new EsitoCreazioneDocumentiFruitore(): null;
		boolean creaDocumentoDone = false;
		try {
			creaDocumentoReq = makeCreaDocumentiReqValida(pratica, uploadDocs);
			result = cosmoDAO.creaDocumento(creaDocumentoReq);
			creaDocumentoDone = true;
			logCreaDocumento(creaDocumentoReq, result, null);
			return result;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::|creaDocumentoCosmo] DAOException " + daoe.getMessage());
			if (!creaDocumentoDone)
				logCreaDocumento(creaDocumentoReq, result, daoe);
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::creaDocumentoCosmo] BusinessException " + be.getMessage());
			logCreaDocumento(creaDocumentoReq, result, be);
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaDocumentoCosmo] Exception " + e.getMessage() + "\n" + e.getCause());
			logCreaDocumento(creaDocumentoReq, result, e);
			throw new BusinessException();
		}
	}

	private EsitoCreazioneDocumentiFruitore creaDocumentoCosmoIntegrazione(CosmoLogPraticaEntity logPratica, CreaPraticaFruitoreResponse pratica,
			List<CreaDocumentoFruitoreRequest> uploadDocs) throws BusinessException {
		CreaDocumentiFruitoreRequest creaDocumentoReq = null;
		EsitoCreazioneDocumentiFruitore result = uploadDocs.isEmpty() ? new EsitoCreazioneDocumentiFruitore(): null;
		try {
			creaDocumentoReq = makeCreaDocumentiReqValidaIntegrazione(pratica, uploadDocs);
			if (creaDocumentoReq.getDocumenti().size() > 0) {
				result = cosmoDAO.creaDocumento(creaDocumentoReq);
			}
			logServizioInviaDocumento(logPratica, creaDocumentoReq, result, null);	
			return result;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::|creaDocumentoCosmo] DAOException " + daoe.getMessage());
			logServizioInviaDocumento(logPratica, creaDocumentoReq, result, daoe);	
			throw new BusinessException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::creaDocumentoCosmo] BusinessException " + be.getMessage());
			logServizioInviaDocumento(logPratica, creaDocumentoReq, result, be);	
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaDocumentoCosmo] Exception " + e.getMessage() + "\n" + e.getCause());
			logServizioInviaDocumento(logPratica, creaDocumentoReq, result, e);	
			throw new BusinessException();
		}
	}

	private void logCreaDocumento(CreaDocumentiFruitoreRequest request, EsitoCreazioneDocumentiFruitore response,
			Exception e) {
		try {
			logCreaPratica.setCreaDocumentoRichiesta(request != null ? mapper.writeValueAsString(request) : null);
			logCreaPratica.setCreaDocumentoRisposta(response != null ? mapper.writeValueAsString(response) : null);
			logUpdateWithErrore(e);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::logCreaDocumento] DAOException " + daoe.getMessage());
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::logCreaDocumento] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::logCreaDocumento] Exception " + ex.getMessage() + "\n" + ex.getCause());
			throw new BusinessException();
		}
	}

	private void valida(EsitoCreazioneDocumentiFruitore creaDocumentoResponse) {
		creaDocumentoResponse.getEsiti().stream().mapToInt(e -> e.getEsito().getStatus())
				.filter((IntPredicate) status -> status != 201).findFirst().ifPresent(i -> {
					throw new BusinessException("COSMO creaDocumento, almeno un documento non creato", "MOONSRV-30506");
				});
	}

	//
	// AVVIA PROCESSO
	//
	private AvviaProcessoFruitoreResponse avviaProcessoCosmo(CreaPraticaFruitoreResponse pratica) {
		AvviaProcessoFruitoreRequest avviaProcessoReq = null;
		AvviaProcessoFruitoreResponse result = null;
		try {
			avviaProcessoReq = new AvviaProcessoFruitoreRequest();
			avviaProcessoReq.setCodiceIpaEnte(pratica.getCodiceIpaEnte());
			avviaProcessoReq.setIdPratica(pratica.getIdPratica());
			result = cosmoDAO.avviaProcesso(avviaProcessoReq);
			logAvviaProcesso(avviaProcessoReq, result, null);
			return result;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::avviaProcessoCosmo] DAOException " + daoe.getMessage());
			logAvviaProcesso(avviaProcessoReq, result, daoe);
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::avviaProcessoCosmo] BusinessException " + be.getMessage());
			logAvviaProcesso(avviaProcessoReq, result, be);
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::avviaProcessoCosmo] Exception " + e.getMessage() + "\n" + e.getCause());
			logAvviaProcesso(avviaProcessoReq, result, e);
			throw new BusinessException();
		}
	}

	private void logAvviaProcesso(AvviaProcessoFruitoreRequest request, AvviaProcessoFruitoreResponse response,
			Exception e) {
		try {
			logCreaPratica.setAvviaProcessoRichiesta(request != null ? mapper.writeValueAsString(request) : null);
			logCreaPratica.setAvviaProcessoRisposta(response != null ? mapper.writeValueAsString(response) : null);
			logCreaPratica.setDataAvvio(new Timestamp(System.currentTimeMillis()));
			logUpdateWithErrore(e);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::logAvviaProcesso] DAOException " + daoe.getMessage());
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::logAvviaProcesso] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::logAvviaProcesso] Exception " + ex.getMessage() + "\n" + ex.getCause());
			throw new BusinessException();
		}
	}
	
	
	private void logErrorePratica( Exception e) {
		try {		
			if (logCreaPratica.getIdLogPratica() == null) {
				logInsertWithErrore(e);
			} else {
				logUpdateWithErrore(e);
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::logCreaPratica] DAOException " + daoe.getMessage());
			throw daoe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::logCreaPratica] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::logCreaPratica] Exception " + ex.getMessage() + "\n" + ex.getCause());
			throw new BusinessException();
		}
	}

	

	//
	// CallBack
	//
	@Override
	public Esito callbackPutStatoPraticaV1(AggiornaStatoPraticaRequest pratica, CosmoLogPraticaEntity cosmoLogPratica) {
		try {
//			List<CosmoLogPraticaEntity> lastCalls = cosmoLogPraticaDAO.findByIdIstanza(istanza.getIdIstanza());
//			if (lastCalls.isEmpty()) {
//				LOG.error("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] idPratica=" + pratica.getId() + "  idIstanza=" + istanza.getIdIstanza());
//				throw new BusinessException("cosmoLogPratica non trovato.","MOONSRV-30507");
//			}
//			if (lastCalls.size()!=1) {
//				LOG.error("[" + CLASS_NAME + "::callbackPutStatoPratica] idPratica=" + pratica.getId() + "  idIstanza=" + istanza.getIdIstanza());
//				throw new BusinessException("cosmoLogPratica non univoca. NOT IMPLEMENTED multi log with idx","MOONSRV-30508");
//			}
//			CosmoLogPraticaEntity uniquelog = lastCalls.get(0);
			cosmoLogPratica.setDataUpd(new Timestamp(System.currentTimeMillis()));
			ObjectMapper mapper = new ObjectMapper();
			cosmoLogPratica.setPratica(mapper.writeValueAsString(pratica));
			cosmoLogPraticaDAO.update(cosmoLogPratica);
			Esito result = new Esito();
			result.setStatus(200);
			result.setCode("OK");
			result.setTitle("StatoPratica aggiornato su MOOn.");
			return result;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] BusinessException " + be.getMessage());
			throw be;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] BusinessException " + daoe.getMessage());
			throw daoe;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] Exception " + ex.getMessage() + "\n"
					+ ex.getCause());
			throw new BusinessException();
		}
	}

	//
	//
	private JsonNode retrieveConf(Istanza istanza) throws BusinessException {
		String strJson = findValueCosmoConfInAttributi(istanza.getModulo().getAttributi());
		if (StringUtils.isBlank(strJson)) {
			LOG.error("[" + CLASS_NAME
					+ "::retrieveConf] PSIT_COSMO_CONF non trovata nei attributi del modulo for idIstanza="
					+ istanza.getIdIstanza() + "\nmodulo=" + istanza.getModulo());
			throw new BusinessException("PSIT_COSMO_CONF non trovata nei attributi del modulo", "MOONSRV-30500");
		}
		return readConfJson(strJson);
	}

	private String findValueCosmoConfInAttributi(List<ModuloAttributo> attributi) {
		if (attributi == null)
			return null;
		return attributi.stream().filter(a -> ModuloAttributoKeys.PSIT_COSMO_CONF.name().equals(a.getNome()))
				.map(a -> a.getValore()).findAny().orElse(null);
	}

	private JsonNode readConfJson(String strJson) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: " + strJson);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);
			return result;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::readConfJson] IOException " + e.getMessage());
			throw new BusinessException("PSIT_COSMO_CONF non valid JSON", "MOONSRV-30501");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}

}
