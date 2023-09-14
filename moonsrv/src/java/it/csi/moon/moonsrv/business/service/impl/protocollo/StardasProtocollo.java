/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.protocollo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ProtocolloMetadatoConfJson;
import it.csi.moon.commons.dto.ReportVerificaFirma;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ProtocolloRichiestaEntity;
import it.csi.moon.commons.entity.ProtocolloRichiestaFilter;
import it.csi.moon.commons.entity.RepositoryFileLazyEntity;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.ProtocolloAttributoKeys;
import it.csi.moon.moonsrv.business.service.FirmaDigitaleService;
import it.csi.moon.moonsrv.business.service.helper.DatiIstanzaHelper;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ProtocolloRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.StardasDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.stardas.cxfclient.ConfigurazioneChiamanteType;
import it.csi.stardas.cxfclient.DatiSmistaDocumentoType;
import it.csi.stardas.cxfclient.DocumentoElettronicoType;
import it.csi.stardas.cxfclient.EmbeddedBinaryType;
import it.csi.stardas.cxfclient.EmbeddedXMLType;
import it.csi.stardas.cxfclient.MetadatiType;
import it.csi.stardas.cxfclient.MetadatoType;
import it.csi.stardas.cxfclient.RiferimentoECMType;
import it.csi.stardas.cxfclient.SmistaDocumentoRequestType;
import it.csi.stardas.cxfclient.SmistaDocumentoResponseType;

public class StardasProtocollo extends BaseProtocollo implements Protocollo {

	private static final String CLASS_NAME = "StardasProtocollo";
	protected static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final String PROTOCOLLO_BUSINESS_EXCEPTION = "ProtocolloBusinessException";
	private static final Integer ID_PROTOCOLLATORE = ProtocolloRichiestaEntity.Protocollatore.STARDAS.getId();
	private static final String PRTATTR_ADDITIONAL_ALLEGATI_CONFKEY_REPOSITORY_FILE_TIPOLOGIA = "repository_file_tipologia";
	private static final String APPLICATION_PKCS7_MIME = "application/pkcs7-mime";
	
	protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private List<AllegatoLazyEntity> allegati = null;
	private List<RepositoryFileLazyEntity> allegatiAdditionali = null;
	private List<RepositoryFileLazyEntity> allegatiIntegrazione = null;
	
	private Istanza istanza;
	private DatiIstanzaHelper datiIstanzaHelper;
	class PrResponse {
		String messageUUIDPrincipale;
		boolean protocollatoAdesso;
		//
		public PrResponse(String messageUUIDPrincipale, boolean protocollatoAdesso) {
			super();
			this.messageUUIDPrincipale = messageUUIDPrincipale;
			this.protocollatoAdesso = protocollatoAdesso;
		}
		// GET
		public String getMessageUUIDPrincipale() {
			return messageUUIDPrincipale;
		}
		public boolean isProtocollatoAdesso() {
			return protocollatoAdesso;
		}
	}
	
	@Autowired
	@Qualifier("apimint")  // wso001 OR apimint // verificare presenza URL nei file .properties
	StardasDAO stardasDAO;
	@Autowired
	ProtocolloRichiestaDAO protocolloRichiestaDAO;
	@Autowired
	protected AllegatoDAO allegatoDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	FirmaDigitaleService firmaDigitaleService;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	
	/**
	 * Protocolla un istanza e il suo pdf gia generato passato in params
	 * insieme alla configurazione stardas necessaria :
	 * - responsabileTrattamento   GRMLTR69M65L219J
	 * - codiceFiscaleEnte         00514490010
	 * - codiceFruitore            MOON
	 * - codiceApplicativo         MOONSRV
	 * - codiceTipoDocumento       TRIB_DICH_IMU
	 */
	@Override
	public String protocollaIstanza(Istanza istanza, ProtocolloParams params) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::protocollaIstanza] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::protocollaIstanza] IN istanza="+istanza);
				LOG.debug("[" + CLASS_NAME + "::protocollaIstanza] IN params="+params);
			}
			this.istanza = istanza;
			
			// Protocolla il documento principale dell'istanza
			PrResponse respPrPrincipale = protocollaIstanzaPrincipale(istanza, params);
			
			// Protocolla XML resoconto se necessario
			protocollaXmlResocontoIfNecessary(istanza, params, respPrPrincipale);
			
			// Protocolla gli allegati se necessario
			protocollaIstanzaAllegatiIfNecessary(istanza, params, respPrPrincipale);
			
			return respPrPrincipale.getMessageUUIDPrincipale();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaIstanza] BusinessException "+be.getMessage());
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::protocollaIstanza] ERRORE "+ (e.getMessage()==null?e:e.getMessage()));
			throw new BusinessException(PROTOCOLLO_BUSINESS_EXCEPTION,"MOONSRV-30600");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaIstanza] END");
		}
	}


	private PrResponse protocollaIstanzaPrincipale(Istanza istanza, ProtocolloParams params) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaIstanzaPrincipale] BEGIN");
			DatiSmistaDocumentoType datiSmistaDocumento = buildDatiSmistaDocumentoTypeIstanza(istanza, params);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaIstanza(istanza, params, datiSmistaDocumento);
			String messageUUID = getMessageUUIDiFDocPrincipaleProtocollato(istanza);
			if (messageUUID == null || messageUUID.equals("")) {
				// Chiama STARDAS per il documento principale
				SmistaDocumentoResponseType response = stardasDAO.smistaDocumento(makeSmistaDocumentoRequest(retrieveConfIstanza(params), datiSmistaDocumento));
				richiesta = readResponse(richiesta, response);
				protocolloRichiestaDAO.insert(richiesta);
				boolean protocollatoAdesso = true;
				return new PrResponse(response==null?"":response.getMessageUUID(),protocollatoAdesso);
			} else {
				boolean protocollatoAdesso = false;
				return new PrResponse(messageUUID, protocollatoAdesso);
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::protocollaIstanzaPrincipale] DAOException "+daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaIstanzaPrincipale] BusinessException "+(be.getMessage()==null?be:be.getMessage()));
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::protocollaIstanzaPrincipale] Exception "+(e.getMessage()==null?e:e.getMessage()));
			throw new BusinessException();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaIstanzaPrincipale] END");
		}
	}

	private void protocollaXmlResocontoIfNecessary(Istanza istanza, ProtocolloParams params, PrResponse responsePrPrincipale) {
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaXmlResocontoIfNecessary] BEGIN");
			if (Boolean.TRUE.equals(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.PROTOCOLLA_XML_RESOCONTO)) &&
				(responsePrPrincipale.isProtocollatoAdesso() || isXmlResocontoNonProtocollato(istanza)) ) {
				protocollaXmlResoconto(istanza, params, responsePrPrincipale.getMessageUUIDPrincipale());
			}
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaIstanzaAllegatiIfNecessary] END");
		}
	}
	
	private boolean isXmlResocontoNonProtocollato (Istanza istanza) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::isXmlResocontoNonProtocollato] BEGIN");
		boolean isNonProtocollato = true;

		ProtocolloRichiestaFilter filter = new ProtocolloRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.XML_RESOCONTO.getId());
		List<ProtocolloRichiestaEntity> elencoRichieste = protocolloRichiestaDAO.find(filter);
		if (elencoRichieste != null && !elencoRichieste.isEmpty()) {
			for (ProtocolloRichiestaEntity richiesta: elencoRichieste) {
				if ("000".equals(richiesta.getCodiceEsito()) || "001".equals(richiesta.getCodiceEsito())) {
					isNonProtocollato = false;
				}
			}
		}
		return isNonProtocollato;
	}
	
	private void protocollaIstanzaAllegatiIfNecessary(Istanza istanza, ProtocolloParams params, PrResponse responsePrPrincipale) {
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaIstanzaAllegatiIfNecessary] BEGIN");
			if (Boolean.TRUE.equals(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.PROTOCOLLA_ALLEGATI))) {
				List<AllegatoLazyEntity> localAllegati = getAllegati(istanza, params);
				if (localAllegati != null) {
					for (AllegatoLazyEntity a : localAllegati) {
						/* gestione retry sul singolo allegato
						 * se l'allegato non ha codiceEsito == 000 e ha messageUidProtocollatore a null
						 * allora viene inviato
						 */
						if (responsePrPrincipale.isProtocollatoAdesso() || isAllegatoNonProtocollato(istanza, a)) {
							protocollaAllegato(istanza, a, params, responsePrPrincipale.getMessageUUIDPrincipale());
						}
					}
				}
				//
				protocollaIstanzaAllegatiAdditionali(istanza, params, responsePrPrincipale);
			}
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaIstanzaAllegatiIfNecessary] END");
		}
	}


	private void protocollaIstanzaAllegatiAdditionali(Istanza istanza, ProtocolloParams params, PrResponse responsePrPrincipale) {
		List<RepositoryFileLazyEntity> localAllegatiAdditionali = getAllegatiAdditionali(istanza, params);
		if (localAllegatiAdditionali != null) {
			for (RepositoryFileLazyEntity a : localAllegatiAdditionali) {
				/* gestione retry sul singolo allegato
				 * se l'allegato non ha codiceEsito == 000 e ha messageUidProtocollatore a null
				 * allora viene inviato
				 */
				if (responsePrPrincipale.isProtocollatoAdesso() || isAllegatoAdditionalNonProtocollato(istanza, a)) {
					protocollaAllegatoAdditional(istanza, a, params, responsePrPrincipale.getMessageUUIDPrincipale());
				}
			}
		}
	}
	
	private boolean isAllegatoNonProtocollato (Istanza istanza, AllegatoLazyEntity allegato) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::isAllegatoNonProtocollato] BEGIN");
		boolean isNonProtocollato = true;

		ProtocolloRichiestaFilter filter = new ProtocolloRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.ISTANZA_ALLEGATO.getId());
		filter.setIdAllegato(allegato.getIdAllegato());
		List<ProtocolloRichiestaEntity> elencoRichieste = protocolloRichiestaDAO.find(filter);
		if (elencoRichieste != null && !elencoRichieste.isEmpty()) {
			for (ProtocolloRichiestaEntity richiesta: elencoRichieste) {
				if ("000".equals(richiesta.getCodiceEsito()) || "001".equals(richiesta.getCodiceEsito())) {
					isNonProtocollato = false;
				}
			}
		}
		return isNonProtocollato;
	}
	
	private boolean isAllegatoAdditionalNonProtocollato (Istanza istanza, RepositoryFileLazyEntity allegatoAdditional) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::isAllegatoAdditionalNonProtocollato] BEGIN");
		boolean isNonProtocollato = true;

		ProtocolloRichiestaFilter filter = new ProtocolloRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.ADDITIONAL_ALLEGATO.getId());
		filter.setIdFile(allegatoAdditional.getIdFile());
		List<ProtocolloRichiestaEntity> elencoRichieste = protocolloRichiestaDAO.find(filter);
		if (elencoRichieste != null && !elencoRichieste.isEmpty()) {
			for (ProtocolloRichiestaEntity richiesta: elencoRichieste) {
				if ("000".equals(richiesta.getCodiceEsito()) || "001".equals(richiesta.getCodiceEsito())) {
					isNonProtocollato = false;
				}
			}
		}
		return isNonProtocollato;
	}
	
	private boolean isFileUscitaNonProtocollato(Istanza istanza, Long idFile) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::isFileUscitaNonProtocollato] BEGIN");
		boolean isNonProtocollato = true;

		ProtocolloRichiestaFilter filter = new ProtocolloRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.RICEVUTA.getId());
		filter.setIdFile(idFile);
		List<ProtocolloRichiestaEntity> elencoRichieste = protocolloRichiestaDAO.find(filter);
		if (elencoRichieste != null && !elencoRichieste.isEmpty()) {
			for (ProtocolloRichiestaEntity richiesta: elencoRichieste) {
				if ("000".equals(richiesta.getCodiceEsito()) || "001".equals(richiesta.getCodiceEsito())) {
					isNonProtocollato = false;
				}
			}
		}
		return isNonProtocollato;
	}
	
	
	private String getMessageUUIDiFIntegrazionePrincipaleProtocollato (Istanza istanza, Long idStoricoWorkflow) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::getMessageUUIDiFIntegrazionePrincipaleProtocollato] BEGIN");
		String messageUUID = "";

		ProtocolloRichiestaFilter filter = new ProtocolloRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.INTEGRAZIONE.getId());
		filter.setIdStoricoWorkflow(idStoricoWorkflow);
		List<ProtocolloRichiestaEntity> elencoRichieste = protocolloRichiestaDAO.find(filter);
		if (elencoRichieste != null && !elencoRichieste.isEmpty()) {
			for (ProtocolloRichiestaEntity richiesta: elencoRichieste) {
				if (! StringUtils.isEmpty(richiesta.getUuidProtocollatore()) && 
						("000".equals(richiesta.getCodiceEsito()) || "001".equals(richiesta.getCodiceEsito())) ) {
					messageUUID = richiesta.getUuidProtocollatore();
				}
			}
		}
		return messageUUID;
	}
	
	private String getMessageUUIDiFDocPrincipaleProtocollato (Istanza istanza) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::getMessageUUIDiFDocPrincipaleProtocollato] BEGIN");
		String messageUUID = "";

		ProtocolloRichiestaFilter filter = new ProtocolloRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.ISTANZA.getId());
		filter.setStato(ProtocolloRichiestaEntity.Stato.TX.name());
		List<ProtocolloRichiestaEntity> elencoRichieste = protocolloRichiestaDAO.find(filter);
		if (elencoRichieste != null && !elencoRichieste.isEmpty()) {
			for (ProtocolloRichiestaEntity richiesta: elencoRichieste) {
				if (! StringUtils.isEmpty(richiesta.getUuidProtocollatore()) && 
						("000".equals(richiesta.getCodiceEsito()) || "001".equals(richiesta.getCodiceEsito())) ) {
					messageUUID = richiesta.getUuidProtocollatore();
				}
			}
		}
		return messageUUID;
	}
	
	private List<AllegatoLazyEntity> getAllegati(Istanza istanza, ProtocolloParams params) throws DAOException {
		LOG.debug("[" + CLASS_NAME + "::getAllegati] BEGIN");
		if (this.allegati==null && Boolean.TRUE.equals(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.PROTOCOLLA_ALLEGATI))) {
			this.allegati = allegatoDAO.findLazyByIdIstanza(istanza.getIdIstanza());
		}
		return this.allegati;
	}
	
	private List<RepositoryFileLazyEntity> getAllegatiAdditionali(Istanza istanza, ProtocolloParams params) throws DAOException, BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getAllegatiAdditionali] BEGIN");
		if (allegatiAdditionali==null) {
			String additionalAllegatiConf = (String) params.getConf().getWithCorrectType(ProtocolloAttributoKeys.ADDITIONAL_ALLEGATI);
			if (!StringUtils.isEmpty(additionalAllegatiConf)) {
				String[] confSplited = additionalAllegatiConf.split(":");
				String additionalAllegatiConfKey = confSplited[0];
				if (PRTATTR_ADDITIONAL_ALLEGATI_CONFKEY_REPOSITORY_FILE_TIPOLOGIA.equalsIgnoreCase(additionalAllegatiConfKey.trim())) {
					Integer idTipologiaRepositoryFile = Integer.valueOf(confSplited[1]);
					allegatiAdditionali = repositoryFileDAO.findLazyByIdIstanzaTipologia(istanza.getIdIstanza(), idTipologiaRepositoryFile);
				} else {
					LOG.error("[" + CLASS_NAME + "::getAllegatiAdditionali] Errore di configurazione ProtocolloAttributo ADDITIONAL_ALLEGATI: " + additionalAllegatiConf);
					throw new BusinessException("Errore di configurazione ProtocolloAttributo ADDITIONAL_ALLEGATI");
				}
			}
		}
		LOG.info("[" + CLASS_NAME + "::getAllegatiAdditionali] for idIstanza:" + istanza.getIdIstanza() + "  allegatiAdditionaliSize:" + (allegatiAdditionali!=null?allegatiAdditionali.size():0));
		return allegatiAdditionali;
	}
	
	private String protocollaXmlResoconto(Istanza istanza, ProtocolloParams params, String messageUUIDPrincipale) {
		String result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaXmlResoconto] BEGIN");
			DatiSmistaDocumentoType datiSmistaDocumento = buildDatiSmistaDocumentoTypeXmlResoconto(istanza, params, messageUUIDPrincipale);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaXmlResoconto(istanza, params, datiSmistaDocumento);
			
			//
			// Chiama STARDAS per il documento principale
			SmistaDocumentoResponseType response = stardasDAO.smistaDocumento(makeSmistaDocumentoRequest(retrieveConfAllegato(params), datiSmistaDocumento));
			richiesta = readResponse(richiesta, response);
			protocolloRichiestaDAO.insert(richiesta);
			result = response==null?null:response.getMessageUUID();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::protocollaXmlResoconto] DAOException "+e.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaXmlResoconto] BusinessException "+be.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::protocollaXmlResoconto] Exception "+e.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaXmlResoconto] END");
		}
		return result;
	}
	
	
	private String protocollaAllegato(Istanza istanza, AllegatoLazyEntity allegato, ProtocolloParams params, String messageUUIDPrincipale) {
		String result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaAllegato] BEGIN");
			DatiSmistaDocumentoType datiSmistaDocumento = buildDatiSmistaDocumentoTypeAllegato(istanza, allegato, params, messageUUIDPrincipale);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaAllegato(istanza, allegato, params, datiSmistaDocumento);
			
			//
			// Chiama STARDAS per il documento principale
			SmistaDocumentoResponseType response = stardasDAO.smistaDocumento(makeSmistaDocumentoRequest(retrieveConfAllegato(params), datiSmistaDocumento));
			richiesta = readResponse(richiesta, response);
			protocolloRichiestaDAO.insert(richiesta);
			result = response==null?"":response.getMessageUUID();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::protocollaAllegato] DAOException "+e.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaAllegato] BusinessException "+be.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::protocollaAllegato] Exception "+e.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaAllegato] END");
		}
		return result;
	}
	
	private String protocollaAllegatoAdditional(Istanza istanza, RepositoryFileLazyEntity allegatoAdditional, ProtocolloParams params, String messageUUIDPrincipale) {
		String result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaAllegatoAdditional] BEGIN");
			DatiSmistaDocumentoType datiSmistaDocumento = buildDatiSmistaDocumentoTypeAllegatoAdditional(istanza, allegatoAdditional, params, messageUUIDPrincipale);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaAllegatoAdditional(istanza, allegatoAdditional, params, datiSmistaDocumento);
			
			//
			// Chiama STARDAS per il documento principale
			SmistaDocumentoResponseType response = stardasDAO.smistaDocumento(makeSmistaDocumentoRequest(retrieveConfAllegato(params), datiSmistaDocumento));
			richiesta = readResponse(richiesta, response);
			protocolloRichiestaDAO.insert(richiesta);
			result = response==null?"":response.getMessageUUID();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::protocollaAllegatoAdditional] DAOException " + e.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaAllegatoAdditional] BusinessException " + be.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::protocollaAllegatoAdditional] Exception " + e.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaAllegatoAdditional] END");
		}
		return result;
	}

	private ProtocolloRichiestaEntity readResponse(ProtocolloRichiestaEntity richiesta,	SmistaDocumentoResponseType response) {
		String respResCodice = null;
		String respResMessaggio = null;
		if (response!=null) {
			LOG.debug("[" + CLASS_NAME + "::readResponse] stardasDAO.smistaDocumento response=" + response);
			if (response.getResult()!=null) {
				LOG.debug("[" + CLASS_NAME + "::readResponse] stardasDAO.smistaDocumento response.result=" + response.getResult().getCodice() + " - " + response.getResult().getMessaggio());
				respResCodice = response.getResult().getCodice();
				respResMessaggio = response.getResult().getMessaggio();
			}
			richiesta = completeProtocolloRichiesta(richiesta, respResCodice, respResMessaggio, response.getMessageUUID());
			LOG.debug("[" + CLASS_NAME + "::readResponse] stardasDAO.smistaDocumento response.getMessageUUID :: response.getMessageUUID()=" + response.getMessageUUID());
		} else {
			LOG.warn("[" + CLASS_NAME + "::readResponse] SmistaDocumentoResponseType null from stardasDAO !");
			richiesta = completeProtocolloRichiesta(richiesta, "SmistaDocumentoResponseType null");
		}
		return richiesta;
	}


	private DatiSmistaDocumentoType buildDatiSmistaDocumentoTypeIstanza(Istanza istanza, ProtocolloParams params) {
		DatiSmistaDocumentoType datiSmistaDocumento = new DatiSmistaDocumentoType();
		datiSmistaDocumento.setResponsabileTrattamento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_RESPONSABILE_TRATTAMENTO));
		datiSmistaDocumento.setIdDocumentoFruitore(istanza.getCodiceIstanza()); // o UUID per generalizzare ?
		DocumentoElettronicoType doc = new DocumentoElettronicoType();
		doc.setNomeFile(istanza.getCodiceIstanza()+".pdf");
		doc.setContenutoBinario(buildEmbeddedBinary(params.getContent()));
		doc.setRiferimentoECM(retrieveRifECM(istanza, params)); // null
		doc.setDocumentoFirmato(false);
		doc.setMimeType(MediaType.APPLICATION_PDF_VALUE);
		datiSmistaDocumento.setDocumentoElettronico(doc);
		datiSmistaDocumento.setDatiDocumentoXML(retrieveXML(istanza, params)); // null
		boolean isDocFirmato = false;
		datiSmistaDocumento.setMetadati(_retrieveMetadati(istanza, params, ProtocolloRichiestaEntity.TipoDoc.ISTANZA, isDocFirmato));
		datiSmistaDocumento.setNumAllegati(getNumAllegati(istanza, params));
		logDebug("buildDatiSmistaDocumentoTypeIstanza", istanza, datiSmistaDocumento);
		return datiSmistaDocumento;
	}


	private Integer getNumAllegati(Istanza istanza, ProtocolloParams params) {
		List<AllegatoLazyEntity> localAllegati = getAllegati(istanza, params);
		Integer result = localAllegati!=null?localAllegati.size():0;
		if (Boolean.TRUE.equals(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.PROTOCOLLA_XML_RESOCONTO))) {
			result++;
		}
		//
		List<RepositoryFileLazyEntity> localAllegatiAdditionali = getAllegatiAdditionali(istanza, params);
		result += localAllegatiAdditionali!=null?localAllegatiAdditionali.size():0;
		return result;
	}


	private DatiSmistaDocumentoType buildDatiSmistaDocumentoTypeXmlResoconto(Istanza istanza, ProtocolloParams params, String messageUUIDPrincipale) {
		if (params.getResoconto()==null) {
			LOG.warn("[" + CLASS_NAME + "::buildDatiSmistaDocumentoTypeXmlResoconto] resotonco NULL for istanza " + istanza.getIdIstanza() + " - " + istanza.getCodiceIstanza());
		}
		DatiSmistaDocumentoType datiSmistaDocumento = new DatiSmistaDocumentoType();
		datiSmistaDocumento.setResponsabileTrattamento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_RESPONSABILE_TRATTAMENTO));
		datiSmistaDocumento.setIdDocumentoFruitore(istanza.getCodiceIstanza()+"-DISTINTA_DI_PRESENTAZIONE");
		DocumentoElettronicoType doc = new DocumentoElettronicoType();
		doc.setNomeFile(istanza.getCodiceIstanza()+"-DISTINTA_DI_PRESENTAZIONE.xml");
		doc.setContenutoBinario(buildEmbeddedBinary(params.getResoconto().getBytes()));
		doc.setRiferimentoECM(retrieveRifECM(istanza, params)); // null
		doc.setDocumentoFirmato(false);
		doc.setMimeType("application/xml");
		datiSmistaDocumento.setDocumentoElettronico(doc);
		datiSmistaDocumento.setDatiDocumentoXML(retrieveXML(istanza, params)); // null
		boolean isDocFirmato = false;
		/*
		 * vecchia gestione in cui 
		 * XML Resoconto viene mandato come Allegato
		 */
		// datiSmistaDocumento.setMetadati(retrieveMetadati(istanza, params, ProtocolloRichiestaEntity.TipoDoc.ISTANZA_ALLEGATO, isDocFirmato)); 
		AllegatoLazyEntity allegato = new AllegatoLazyEntity();
		allegato.setNomeFile("DISTINTA_DI_PRESENTAZIONE.xml");
		datiSmistaDocumento.setMetadati(retrieveMetadatiAllegato(istanza, params, ProtocolloRichiestaEntity.TipoDoc.ISTANZA_ALLEGATO, isDocFirmato, allegato));
		datiSmistaDocumento.setMessageUUIDPrincipale(messageUUIDPrincipale);
		logDebug("buildDatiSmistaDocumentoTypeXmlResoconto", istanza, datiSmistaDocumento);
		return datiSmistaDocumento;
	}


	private DatiSmistaDocumentoType buildDatiSmistaDocumentoTypeAllegato(Istanza istanza, AllegatoLazyEntity allegato, ProtocolloParams params, String messageUUIDPrincipale) {
		DatiSmistaDocumentoType datiSmistaDocumento = new DatiSmistaDocumentoType();
		datiSmistaDocumento.setResponsabileTrattamento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_RESPONSABILE_TRATTAMENTO));
		datiSmistaDocumento.setIdDocumentoFruitore(istanza.getCodiceIstanza()+"-"+allegato.getCodiceFile()); // codiceIstanza(50) + "-" + codiceFileUUID(50)
		DocumentoElettronicoType doc = new DocumentoElettronicoType();

		String nomeFileFormio = allegato.getFormioNameFile();
		String extension = getFileExtension(allegato.getNomeFile());
		String nomeFile = getFormIoFileName(nomeFileFormio);
		LOG.debug("[" + CLASS_NAME + "::buildDatiSmistaDocumentoTypeAllegato] nomefile allegato: " +nomeFile);
		doc.setNomeFile(nomeFile+"."+extension); // o usare getFormioNameFile() se si vuole un univocita
		
		doc.setContenutoBinario(buildEmbeddedBinary(allegatoDAO.findById(allegato.getIdAllegato()).getContenuto()));
		doc.setRiferimentoECM(retrieveRifECM(istanza, params)); // null	
		// se flag a no  controllo se firmato e aggiorno il flag 
		// altrimento vado avanti
		boolean isDocFirmato = "S".equals(allegato.getFlFirmato());
		if (!isDocFirmato) {
			boolean verificaFirmaDosign = Boolean.TRUE.equals(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.VERIFICA_FIRMA_DOSIGN));
			if (verificaFirmaDosign) {
				isDocFirmato = checkDocumentSignature(allegato, nomeFile);
				allegato.setFlFirmato(isDocFirmato?"S":"N");
				allegatoDAO.updateFlFirmato(allegato);
			} else {
				if ("p7m".equalsIgnoreCase(extension) && !"S".equalsIgnoreCase(allegato.getFlFirmato())) {
					isDocFirmato = true;
					allegato.setFlFirmato("S");
					allegatoDAO.updateFlFirmato(allegato);
				}
			}
		}
		doc.setDocumentoFirmato(isDocFirmato);
		doc.setMimeType(("p7m".equalsIgnoreCase(extension)) ? APPLICATION_PKCS7_MIME : allegato.getContentType());
		datiSmistaDocumento.setDocumentoElettronico(doc);
		datiSmistaDocumento.setDatiDocumentoXML(retrieveXML(istanza, params)); // null
		datiSmistaDocumento.setMetadati(_retrieveMetadatiAllegato(istanza, params, ProtocolloRichiestaEntity.TipoDoc.ISTANZA_ALLEGATO, isDocFirmato, allegato));
		datiSmistaDocumento.setMessageUUIDPrincipale(messageUUIDPrincipale);
		logDebug("buildDatiSmistaDocumentoTypeAllegato", istanza, datiSmistaDocumento);
		return datiSmistaDocumento;
	}

	protected boolean checkDocumentSignature(AllegatoLazyEntity allegato, String nomeFile) {
		try {
			boolean isDocFirmato = false;
			ReportVerificaFirma reportFirma = firmaDigitaleService.checkDocumentSignatureByIdAllegato(allegato.getIdAllegato());
			if (reportFirma != null && reportFirma.isFirmato()) {
				LOG.info("[" + CLASS_NAME + "::checkDocumentSignature] allegato: " +nomeFile + "firmato ");
				LOG.info("[" + CLASS_NAME + "::checkDocumentSignature] formato firma: " +nomeFile + " " + reportFirma.getFormatoFirma().getCodice());
				LOG.info("[" + CLASS_NAME + "::checkDocumentSignature] tipo firma: " +nomeFile + " " + reportFirma.getTipoFirma().getCodice());
				isDocFirmato = true;
			}
			return isDocFirmato;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::checkDocumentSignature] Errore log formato firma for allegato = " + allegato.getIdAllegato(), e);
			return false;
		}
	}


	private DatiSmistaDocumentoType buildDatiSmistaDocumentoTypeAllegatoAdditional(Istanza istanza, RepositoryFileLazyEntity allegatoAdditional, ProtocolloParams params, String messageUUIDPrincipale) {
		DatiSmistaDocumentoType datiSmistaDocumento = new DatiSmistaDocumentoType();
		datiSmistaDocumento.setResponsabileTrattamento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_RESPONSABILE_TRATTAMENTO));
		datiSmistaDocumento.setIdDocumentoFruitore(istanza.getCodiceIstanza()+"-"+allegatoAdditional.getCodiceFile()); // codiceIstanza(50) + "-" + codiceFileUUID(50)
		DocumentoElettronicoType doc = new DocumentoElettronicoType();

		String nomeFileFormio = allegatoAdditional.getFormioNameFile();
		String extension = getFileExtension(allegatoAdditional.getNomeFile());
		String nomeFile = getFormIoFileName(nomeFileFormio);
		LOG.debug("[" + CLASS_NAME + "::buildDatiSmistaDocumentoTypeAllegatoAdditional] nomefile allegato: " +nomeFile);
		doc.setNomeFile(nomeFile+"."+extension); // o usare getFormioNameFile() se si vuole un univocita
		
		doc.setContenutoBinario(buildEmbeddedBinary(repositoryFileDAO.findById(allegatoAdditional.getIdFile()).getContenuto()));
		doc.setRiferimentoECM(retrieveRifECM(istanza, params)); // null	
		// se flag a no  controllo se firmato e aggiorno il flag 
		// altrimento vado avanti
		boolean isDocFirmato = "S".equals(allegatoAdditional.getFlFirmato());
		if (!isDocFirmato) {
			boolean verificaFirmaDosign = Boolean.TRUE.equals(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.VERIFICA_FIRMA_DOSIGN));
			if (verificaFirmaDosign) {
				isDocFirmato = checkDocumentSignature(allegatoAdditional, nomeFile);
				allegatoAdditional.setFlFirmato(isDocFirmato?"S":"N");
				repositoryFileDAO.updateFlFirmato(allegatoAdditional);
			} else {	
				if ("p7m".equalsIgnoreCase(extension) && !"S".equalsIgnoreCase(allegatoAdditional.getFlFirmato())) {
					isDocFirmato = true;
					allegatoAdditional.setFlFirmato("S");
					repositoryFileDAO.updateFlFirmato(allegatoAdditional);
				}
			}
		}
		doc.setDocumentoFirmato(isDocFirmato);
		doc.setMimeType(("p7m".equalsIgnoreCase(extension)) ? APPLICATION_PKCS7_MIME : allegatoAdditional.getContentType());
		datiSmistaDocumento.setDocumentoElettronico(doc);
		datiSmistaDocumento.setDatiDocumentoXML(retrieveXML(istanza, params)); // null
		datiSmistaDocumento.setMetadati(_retrieveMetadatiAllegatoAdditional(istanza, params, ProtocolloRichiestaEntity.TipoDoc.ADDITIONAL_ALLEGATO, isDocFirmato, allegatoAdditional));
		datiSmistaDocumento.setMessageUUIDPrincipale(messageUUIDPrincipale);
		logDebug("buildDatiSmistaDocumentoTypeAllegatoAdditional", istanza, datiSmistaDocumento);
		return datiSmistaDocumento;
	}

	protected boolean checkDocumentSignature(RepositoryFileLazyEntity allegatoAdditional, String nomeFile) {
		try {
			boolean isDocFirmato = false;
			ReportVerificaFirma reportFirma = firmaDigitaleService.checkDocumentSignatureByIdFile(allegatoAdditional.getIdFile());
			if (reportFirma != null && reportFirma.isFirmato()) {
				LOG.info("[" + CLASS_NAME + "::checkDocumentSignature] allegato: " +nomeFile + "firmato ");
				LOG.info("[" + CLASS_NAME + "::checkDocumentSignature] formato firma: " +nomeFile + " " + reportFirma.getFormatoFirma().getCodice());
				LOG.info("[" + CLASS_NAME + "::checkDocumentSignature] tipo firma: " +nomeFile + " " + reportFirma.getTipoFirma().getCodice());
				isDocFirmato = true;
			}
			return isDocFirmato;				
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::checkDocumentSignature] Errore log formato firma for idFile = " + allegatoAdditional.getIdFile(), e);
			return false;
		}
	}

	private SmistaDocumentoRequestType makeSmistaDocumentoRequest(ConfigurazioneChiamanteType conf, DatiSmistaDocumentoType datiSmistaDocumento) {
		SmistaDocumentoRequestType smistaDocumentoRequest = new SmistaDocumentoRequestType();
		smistaDocumentoRequest.setConfigurazioneChiamante(conf);
		smistaDocumentoRequest.setDatiSmistaDocumento(datiSmistaDocumento);
		return smistaDocumentoRequest;
	}

	private ConfigurazioneChiamanteType retrieveConfIstanza(ProtocolloParams params) throws BusinessException {
		try {
	        LOG.debug("[" + CLASS_NAME + "::retrieveConfIstanza] IN params=" + params);
			ConfigurazioneChiamanteType result = new ConfigurazioneChiamanteType();
			result.setCodiceFiscaleEnte(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_ENTE));
			result.setCodiceFruitore(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FRUITORE));
			result.setCodiceApplicazione(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_APPLICAZIONE));
			result.setCodiceTipoDocumento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_TIPO_DOCUMENTO));
			LOG.debug("[" + CLASS_NAME + "::retrieveConfIstanza] OUT result=" + result);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveConfIstanza] Exception "+e.getMessage());
			throw new BusinessException("Errore recupero configurazione da ProtocolloParams per generazione ConfigurazioneChiamanteType su Istanza","MOONSRV-30603");
		}
	}
	private ConfigurazioneChiamanteType retrieveConfAllegato(ProtocolloParams params) throws BusinessException {
		try {
	        LOG.debug("[" + CLASS_NAME + "::retrieveConfAllegato] IN params=" + params);
			ConfigurazioneChiamanteType result = new ConfigurazioneChiamanteType();
			result.setCodiceFiscaleEnte(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_ENTE));
			result.setCodiceFruitore(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FRUITORE));
			result.setCodiceApplicazione(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_APPLICAZIONE));
			result.setCodiceTipoDocumento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_TIPO_DOCUMENTO_ALLEGATI));
			LOG.debug("[" + CLASS_NAME + "::retrieveConfAllegato] OUT result=" + result);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveConfAllegato] Exception "+e.getMessage());
			throw new BusinessException("Errore recupero configurazione da ProtocolloParams per generazione ConfigurazioneChiamanteType su Allegato","MOONSRV-30604");
		}
	}
	
	private EmbeddedBinaryType buildEmbeddedBinary(byte[] bytes) {
		EmbeddedBinaryType result = new EmbeddedBinaryType();
		result.setContent(bytes);
		return result;
	}

	private RiferimentoECMType retrieveRifECM(Istanza istanza, ProtocolloParams params) {
		return null;
	}

	private EmbeddedXMLType retrieveXML(Istanza istanza, ProtocolloParams params) {
		return null;
	}

	/**
	 * Funzione di recupero dei metadati specifico per Modulo : DEVE essere Overwriten nella Class specifica
	 * @param istanza
	 * @param params
	 * @param tipoDoc
	 * @return MetadatiType
	 */
	protected MetadatiType retrieveMetadati(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato) {
		LOG.warn("[" + CLASS_NAME + "::retrieveMetadati] NESSUN METADATI per protocollazione istanza " + istanza.getCodiceIstanza());
		return null;
	}
	/**
	 * Funzione di recupero dei metadati di un Allegato (non XML resoconto) specifico per Modulo : DEVE essere Overwriten nella Class specifica 
	 * se ci sono piu allegati nel modulo ed se e' necessario identificare ciascuni dei allegati es.: CI, PERM.SOGG., PATENTE utilizzando il key o full_key dell'attributo
	 * @param istanza
	 * @param params
	 * @param tipoDoc
	 * @return MetadatiType
	 */
	protected MetadatiType retrieveMetadatiAllegato(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato, AllegatoLazyEntity allegato) {
		return retrieveMetadati(istanza, params, tipoDoc, isDocFirmato);
	}
	
	protected MetadatiType retrieveMetadatiAllegatoAdditional(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato, RepositoryFileLazyEntity allegatoAdditional) {
		AllegatoLazyEntity allegatoFittizio = new AllegatoLazyEntity();
		allegatoFittizio.setCodiceFile(allegatoAdditional.getCodiceFile());
		allegatoFittizio.setContentType(allegatoAdditional.getContentType());
		allegatoFittizio.setDataCreazione(allegatoAdditional.getDataCreazione());
		allegatoFittizio.setFlEliminato(allegatoAdditional.getFlEliminato());
		allegatoFittizio.setFlFirmato(allegatoAdditional.getFlFirmato());
		allegatoFittizio.setFormioNameFile(allegatoAdditional.getFormioNameFile());
		allegatoFittizio.setFullKey(allegatoAdditional.getFullKey());
		allegatoFittizio.setHashFile(allegatoAdditional.getHashFile());
		allegatoFittizio.setKey(allegatoAdditional.getKey());
		allegatoFittizio.setLabel(allegatoAdditional.getLabel());
		allegatoFittizio.setLunghezza(allegatoAdditional.getLunghezza());
		allegatoFittizio.setNomeFile(allegatoAdditional.getNomeFile());
		return retrieveMetadatiAllegato(istanza, params, tipoDoc, isDocFirmato, allegatoFittizio);
	}
	
	protected String getNomeCognomeDichiarante(Istanza istanza) {
		String result = (istanza.getNomeDichiarante()==null?"":istanza.getNomeDichiarante().toUpperCase()) + " " + 
	                    (istanza.getCognomeDichiarante()==null?"":istanza.getCognomeDichiarante().toUpperCase());
		return result.trim();
	}
	protected String getCognomeNomeDichiarante(Istanza istanza) {
		String result = (istanza.getCognomeDichiarante()==null?"":istanza.getCognomeDichiarante().toUpperCase()) + " " + 
						(istanza.getNomeDichiarante()==null?"":istanza.getNomeDichiarante().toUpperCase());
		return result.trim();
	}
	protected String getNomeMenoCognomeDichiarante(Istanza istanza) {
		String result = (istanza.getNomeDichiarante()==null?"":istanza.getNomeDichiarante().toUpperCase()) + "-" + 
	                    (istanza.getCognomeDichiarante()==null?"":istanza.getCognomeDichiarante().toUpperCase());
		return result.trim();
	}

	protected String getCognomeNomeCodicefiscaleDichiarante(Istanza istanza) {
		String result = (istanza.getCognomeDichiarante()==null?"":istanza.getCognomeDichiarante().toUpperCase()) + " " + 
						(istanza.getNomeDichiarante()==null?"":istanza.getNomeDichiarante().toUpperCase()) + " " +
						(istanza.getCodiceFiscaleDichiarante()==null?"":istanza.getCodiceFiscaleDichiarante().toUpperCase());
		return result.trim();
	}
	
	protected MetadatoType makeNewMetadato(String nome, String valore) {
		MetadatoType result = new MetadatoType();
		result.setNome(nome);
		result.setValore(valore);
		return result;
	}
	
	//
	// Tracciamento Richiesta
	// - inizializzazione                      buildProtocolloRichiestaIstanza
	// - completamento dopo chiamata STARDAS   completeProtocolloRichiesta
	// - salavataggio
	//
	/**
	 * Builder dell'entity di tracciamento delle richieste di protocollo in ingresso per un istanza
	 * @param istanza
	 * @param params
	 * @param datiSmistaDocumento
	 * @return
	 */
	private ProtocolloRichiestaEntity buildProtocolloRichiestaIstanza(Istanza istanza, ProtocolloParams params,	DatiSmistaDocumentoType datiSmistaDocumento) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(datiSmistaDocumento.getIdDocumentoFruitore());
		result.setUuidRichiesta(UUID.randomUUID().toString());
		result.setStato(ProtocolloRichiestaEntity.Stato.EL.name());
		result.setTipoIngUsc(ProtocolloRichiestaEntity.TipoInOut.IN.getId());
		result.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.ISTANZA.getId());
		result.setIdIstanza(istanza.getIdIstanza());
		result.setIdModulo(istanza.getModulo().getIdModulo());
		result.setIdArea(istanza.getIdArea());
		result.setIdEnte(istanza.getIdEnte());
		result.setIdProtocollatore(ID_PROTOCOLLATORE);
		return result;
	}
	/**
	 * Builder dell'entity di tracciamento delle richieste di protocollo in ingresso per un istanza
	 * @param istanza
	 * @param params
	 * @param datiSmistaDocumento
	 * @return
	 */
	private ProtocolloRichiestaEntity buildProtocolloRichiestaIntegrazione(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params,	DatiSmistaDocumentoType datiSmistaDocumento) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(datiSmistaDocumento.getIdDocumentoFruitore());
		result.setUuidRichiesta(UUID.randomUUID().toString());
		result.setStato(ProtocolloRichiestaEntity.Stato.EL.name());
		result.setTipoIngUsc(ProtocolloRichiestaEntity.TipoInOut.IN.getId());
		result.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.INTEGRAZIONE.getId());
		result.setIdIstanza(istanza.getIdIstanza());
		result.setIdModulo(istanza.getModulo().getIdModulo());
		result.setIdArea(istanza.getIdArea());
		result.setIdEnte(istanza.getIdEnte());
		result.setIdProtocollatore(ID_PROTOCOLLATORE);
		result.setIdStoricoWorkflow(idStoricoWorkflow);
		return result;
	}
	/**
	 * Builder dell'entity di tracciamento delle richieste di protocollo in ingresso per un allegato di un istanza
	 * @param istanza
	 * @param params
	 * @param datiSmistaDocumento
	 * @return
	 */
	private ProtocolloRichiestaEntity buildProtocolloRichiestaXmlResoconto(Istanza istanza, ProtocolloParams params, DatiSmistaDocumentoType datiSmistaDocumento) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(datiSmistaDocumento.getIdDocumentoFruitore());
		result.setUuidRichiesta(UUID.randomUUID().toString());
		result.setStato(ProtocolloRichiestaEntity.Stato.EL.name());
		result.setTipoIngUsc(ProtocolloRichiestaEntity.TipoInOut.IN.getId());
		result.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.XML_RESOCONTO.getId());
		result.setIdIstanza(istanza.getIdIstanza());
		result.setIdAllegatoIstanza(null);
		result.setIdModulo(istanza.getModulo().getIdModulo());
		result.setIdArea(istanza.getIdArea());
		result.setIdEnte(istanza.getIdEnte());
		result.setIdProtocollatore(ID_PROTOCOLLATORE);
		return result;
	}
	/**
	 * Builder dell'entity di tracciamento delle richieste di protocollo in ingresso per un allegato di un istanza
	 * @param istanza
	 * @param params
	 * @param datiSmistaDocumento
	 * @return
	 */
	private ProtocolloRichiestaEntity buildProtocolloRichiestaAllegato(Istanza istanza, AllegatoLazyEntity allegato, ProtocolloParams params, DatiSmistaDocumentoType datiSmistaDocumento) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(datiSmistaDocumento.getIdDocumentoFruitore());
		result.setUuidRichiesta(UUID.randomUUID().toString());
		result.setStato(ProtocolloRichiestaEntity.Stato.EL.name());
		result.setTipoIngUsc(ProtocolloRichiestaEntity.TipoInOut.IN.getId());
		result.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.ISTANZA_ALLEGATO.getId());
		result.setIdIstanza(istanza.getIdIstanza());
		result.setIdAllegatoIstanza(allegato.getIdAllegato());
		result.setIdModulo(istanza.getModulo().getIdModulo());
		result.setIdArea(istanza.getIdArea());
		result.setIdEnte(istanza.getIdEnte());
		result.setIdProtocollatore(ID_PROTOCOLLATORE);
		return result;
	}
	/**
	 * Builder dell'entity di tracciamento delle richieste di protocollo in ingresso per un allegato di un istanza
	 * @param istanza
	 * @param params
	 * @param datiSmistaDocumento
	 * @return
	 */
	private ProtocolloRichiestaEntity buildProtocolloRichiestaAllegatoAdditional(Istanza istanza, RepositoryFileLazyEntity allegatoAdditional, ProtocolloParams params,	DatiSmistaDocumentoType datiSmistaDocumento) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(datiSmistaDocumento.getIdDocumentoFruitore());
		result.setUuidRichiesta(UUID.randomUUID().toString());
		result.setStato(ProtocolloRichiestaEntity.Stato.EL.name());
		result.setTipoIngUsc(ProtocolloRichiestaEntity.TipoInOut.IN.getId());
		result.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.ADDITIONAL_ALLEGATO.getId());
		result.setIdIstanza(istanza.getIdIstanza());
		result.setIdFile(allegatoAdditional.getIdFile());
		result.setIdModulo(istanza.getModulo().getIdModulo());
		result.setIdArea(istanza.getIdArea());
		result.setIdEnte(istanza.getIdEnte());
		result.setIdProtocollatore(ID_PROTOCOLLATORE);
		return result;
	}
	
	/**
	 * Completa un entity di tracciamento delle richieste di protocollo dopo esito positivo di STARDAS
	 * @param richiesta
	 * @param codiceEsito
	 * @param descEsito
	 * @param uuidProtocollatore
	 * @return
	 */
	private ProtocolloRichiestaEntity completeProtocolloRichiesta(ProtocolloRichiestaEntity richiesta, String codiceEsito, String descEsito, String uuidProtocollatore) {
//		richiesta.setCodiceEsito(codiceEsito); // campo di esito della protocollazione e NON dell'invio della richiesta di protocollazione, non si valorizzano qui
//		richiesta.setDescEsito(descEsito); // campo di esito della protocollazione e NON dell'invio della richiesta di protocollazione, non si valorizzano qui
		richiesta.setNote(String.join(" - ", codiceEsito, descEsito));
		richiesta.setUuidProtocollatore(uuidProtocollatore);
		richiesta.setStato(ProtocolloRichiestaEntity.Stato.TX.name());
		return richiesta;
	}
	/**
	 * Completa un entity di tracciamento delle richieste di protocollo del campo 'note' nel caso di Exception
	 * @param richiesta
	 * @param note
	 * @return
	 */
	private ProtocolloRichiestaEntity completeProtocolloRichiesta(ProtocolloRichiestaEntity richiesta, String note) {
		richiesta.setNote(note);
		return richiesta;
	}

	public DatiIstanzaHelper getDatiIstanzaHelper() {
		if (datiIstanzaHelper==null) {
			datiIstanzaHelper = new DatiIstanzaHelper();
			datiIstanzaHelper.initDataNode(istanza);
		}
		return datiIstanzaHelper;
	}
	
	private static String getFileExtension(String fileName) {
		String extension = "";
        if (fileName != null) {
	        int index = fileName.lastIndexOf('.');
	        if (index > 0) {
	            extension = fileName.substring(index + 1);
	        }
        }
        return extension;
    }
	protected static String getFormIoFileName(String fileName) {
		String name = "";
        if (fileName != null) {
	        int index = fileName.lastIndexOf('.');
	        if (index > 37) {
	        	name = fileName.substring(0,index-37);
	        }
        }
        return name;

    }
	
	protected static String getFileName(String fileName) {
		String name = "";
        if (fileName != null) {
	        int index = fileName.lastIndexOf('.');
	        if (index > 0) {
	        	name = fileName.substring(0,index);
	        }
	        else {
	        	name = fileName;
	        }
        }
        return name;

    }
	
	//
	//
	public String protocollaIntegrazione(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazione] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazione] IN istanza="+istanza);
				LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazione] IN idStoricoWorkflow="+idStoricoWorkflow);
				LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazione] IN params="+params);
			}
			this.istanza = istanza;
			
			// Protocolla il documento principale dell'istanza
			PrResponse respPrPrincipale = protocollaIntegrazionePrincipale(istanza, idStoricoWorkflow, params);
			
			// Protocolla gli allegati se necessario
			protocollaAllegatiIntegrazioneIfNecessary(istanza, idStoricoWorkflow, params, respPrPrincipale);
			
			return respPrPrincipale==null?null:respPrPrincipale.getMessageUUIDPrincipale();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaIntegrazione] BusinessException "+be.getMessage());
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::protocollaIntegrazione] ERRORE "+e.getMessage());
			throw new BusinessException(PROTOCOLLO_BUSINESS_EXCEPTION,"MOONSRV-30607");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazione] END");
		}
	}
	
	private PrResponse protocollaIntegrazionePrincipale(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazionePrincipale] BEGIN");
			DatiSmistaDocumentoType datiSmistaDocumento = buildDatiSmistaDocumentoTypeIntegrazione(istanza, idStoricoWorkflow, params);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaIntegrazione(istanza, idStoricoWorkflow, params, datiSmistaDocumento);
			String messageUUID = getMessageUUIDiFIntegrazionePrincipaleProtocollato(istanza, idStoricoWorkflow);
			if (StringUtils.isEmpty(messageUUID)) {
				// Chiama STARDAS per il documento principale
				SmistaDocumentoResponseType response = stardasDAO.smistaDocumento(makeSmistaDocumentoRequest(retrieveConfIntegrazione(params), datiSmistaDocumento));
				richiesta = readResponse(richiesta, response);
				protocolloRichiestaDAO.insert(richiesta);
				boolean protocollatoAdesso = true;
				return new PrResponse(response==null?"":response.getMessageUUID(),protocollatoAdesso);
			} else {
				boolean protocollatoAdesso = false;
				return new PrResponse(messageUUID,protocollatoAdesso);
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::protocollaIntegrazionePrincipale] DAOException "+daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaIntegrazionePrincipale] BusinessException "+be.getMessage());
			throw be;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazionePrincipale] END");
		}
	}
	private DatiSmistaDocumentoType buildDatiSmistaDocumentoTypeIntegrazione(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params) {
		DatiSmistaDocumentoType datiSmistaDocumento = new DatiSmistaDocumentoType();
		datiSmistaDocumento.setResponsabileTrattamento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_RESPONSABILE_TRATTAMENTO));
		datiSmistaDocumento.setIdDocumentoFruitore(istanza.getCodiceIstanza() + "-" + idStoricoWorkflow); // o UUID per generalizzare ?
		DocumentoElettronicoType doc = new DocumentoElettronicoType();
		doc.setNomeFile(istanza.getCodiceIstanza() + "-" + idStoricoWorkflow + ".pdf");
		doc.setContenutoBinario(buildEmbeddedBinary(params.getContent()));
		doc.setRiferimentoECM(retrieveRifECM(istanza, params)); // null
		doc.setDocumentoFirmato(false);
		doc.setMimeType(MediaType.APPLICATION_PDF_VALUE);
		datiSmistaDocumento.setDocumentoElettronico(doc);
		datiSmistaDocumento.setDatiDocumentoXML(retrieveXML(istanza, params)); // null
		boolean isDocFirmato = false;
		datiSmistaDocumento.setMetadati(_retrieveMetadati(istanza, params, ProtocolloRichiestaEntity.TipoDoc.INTEGRAZIONE, isDocFirmato));
		datiSmistaDocumento.setNumAllegati(getNumAllegatiIntegrazione(istanza, idStoricoWorkflow, params));
		logDebug("buildDatiSmistaDocumentoTypeIntegrazione", istanza, datiSmistaDocumento);
		return datiSmistaDocumento;
	}
	private Integer getNumAllegatiIntegrazione(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params) {
		List<RepositoryFileLazyEntity> allegatiIntegrazione = getAllegatiIntegrazione(istanza, idStoricoWorkflow, params);
		return allegatiIntegrazione!=null?allegatiIntegrazione.size():0;
	}
	private List<RepositoryFileLazyEntity> getAllegatiIntegrazione(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params) throws DAOException {
		LOG.debug("[" + CLASS_NAME + "::getAllegatiIntegrazione] BEGIN");
		if (Boolean.TRUE.equals(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.PROTOCOLLA_ALLEGATI)) && allegatiIntegrazione==null) {
			allegatiIntegrazione = repositoryFileDAO.findLazyByIdIstanzaStoricoWf(istanza.getIdIstanza(), idStoricoWorkflow);
		}
		return allegatiIntegrazione;
	}
	private void protocollaAllegatiIntegrazioneIfNecessary(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params, PrResponse responsePrPrincipale) {
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaAllegatiIntegrazioneIfNecessary] BEGIN");
			if (Boolean.TRUE.equals(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.PROTOCOLLA_ALLEGATI))) {
				List<RepositoryFileLazyEntity> allegatiIntegrazione = getAllegatiIntegrazione(istanza, idStoricoWorkflow, params);
				if (allegatiIntegrazione!=null) {
					for (RepositoryFileLazyEntity a : allegatiIntegrazione) {
						/* gestione retry sul singolo allegato
						 * se l'allegato non ha codiceEsito == 000 e ha messageUidProtocollatore a null
						 * allora viene inviato
						 */
						if (responsePrPrincipale.isProtocollatoAdesso() || isAllegatoIntegrazioneNonProtocollato(istanza, a)) {
							protocollaAllegatoIntegrazione(istanza, a, params, responsePrPrincipale.getMessageUUIDPrincipale());
						}
					}
				}
			}
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaAllegatiIntegrazioneIfNecessary] END");
		}
	}
	
	private boolean isAllegatoIntegrazioneNonProtocollato (Istanza istanza, RepositoryFileLazyEntity a) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::isAllegatoIntegrazioneNonProtocollato] BEGIN");
		boolean isNonProtocollato = true;

		ProtocolloRichiestaFilter filter = new ProtocolloRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.INTEGRAZIONE_ALLEGATO.getId());
		filter.setIdFile(a.getIdFile());
		List<ProtocolloRichiestaEntity> elencoRichieste = protocolloRichiestaDAO.find(filter);
		if (elencoRichieste != null && !elencoRichieste.isEmpty()) {
			for (ProtocolloRichiestaEntity richiesta: elencoRichieste) {
				if ("000".equals(richiesta.getCodiceEsito())) {
					isNonProtocollato = false;
				}
			}
		}
		return isNonProtocollato;
	}
	
	private String protocollaAllegatoIntegrazione(Istanza istanza, RepositoryFileLazyEntity a, ProtocolloParams params, String messageUUIDPrincipale) {
		String result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaAllegatoIntegrazione] BEGIN");
			DatiSmistaDocumentoType datiSmistaDocumento = buildDatiSmistaDocumentoTypeAllegatoIntegrazione(istanza, a, params, messageUUIDPrincipale);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaAllegato(istanza, a, params, datiSmistaDocumento);
			
			//
			// Chiama STARDAS per il documento principale
			SmistaDocumentoResponseType response = stardasDAO.smistaDocumento(makeSmistaDocumentoRequest(retrieveConfAllegatoIntegrazione(params), datiSmistaDocumento));
			richiesta = readResponse(richiesta, response);
			protocolloRichiestaDAO.insert(richiesta);
			result = response==null?"":response.getMessageUUID();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::protocollaAllegatoIntegrazione] DAOException "+e.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaAllegatoIntegrazione] BusinessException "+be.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::protocollaAllegatoIntegrazione] Exception "+e.getMessage());
			// NON faccio il throws dell'Excpetion, provo con gli altri allegati
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaAllegatoIntegrazione] END");
		}
		return result;
	}
	private DatiSmistaDocumentoType buildDatiSmistaDocumentoTypeAllegatoIntegrazione(Istanza istanza, RepositoryFileLazyEntity a, ProtocolloParams params, String messageUUIDPrincipale) {
		DatiSmistaDocumentoType datiSmistaDocumento = new DatiSmistaDocumentoType();
		datiSmistaDocumento.setResponsabileTrattamento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_RESPONSABILE_TRATTAMENTO));
		datiSmistaDocumento.setIdDocumentoFruitore(istanza.getCodiceIstanza()+"-"+a.getIdStoricoWorkflow()+"-"+a.getCodiceFile()); // codiceIstanza(50) + "-" + codiceFileUUID(50)
		DocumentoElettronicoType doc = new DocumentoElettronicoType();
		
		//doc.setNomeFile(repositoryFile.getNomeFile()); // o usare getFormioNameFile() se si vuole un univocita
		String nomeFileFormio = a.getFormioNameFile();
		String extension = getFileExtension(a.getNomeFile());
		String nomeFile = getFormIoFileName(nomeFileFormio);
		LOG.debug("[" + CLASS_NAME + "::buildDatiSmistaDocumentoTypeAllegatoIntegrazione] nomefile allegato: " +nomeFile);
		doc.setNomeFile(nomeFile+"."+extension); // o usare getFormioNameFile() se si vuole un univocita
		
		doc.setContenutoBinario(buildEmbeddedBinary(repositoryFileDAO.findById(a.getIdFile()).getContenuto()));
		doc.setRiferimentoECM(retrieveRifECM(istanza, params)); // null
		boolean isDocFirmato = false;
		if (extension.equalsIgnoreCase("p7m") || "S".equals(a.getFlFirmato()) ) {
			isDocFirmato = true;
		}
		doc.setDocumentoFirmato(isDocFirmato);
		doc.setMimeType(a.getContentType());
		datiSmistaDocumento.setDocumentoElettronico(doc);
		datiSmistaDocumento.setDatiDocumentoXML(retrieveXML(istanza, params)); // null
		datiSmistaDocumento.setMetadati(_retrieveMetadati(istanza, params, ProtocolloRichiestaEntity.TipoDoc.INTEGRAZIONE_ALLEGATO, isDocFirmato));
		datiSmistaDocumento.setMessageUUIDPrincipale(messageUUIDPrincipale);
		logDebug("buildDatiSmistaDocumentoTypeAllegatoIntegrazione", istanza, datiSmistaDocumento);
		return datiSmistaDocumento;
	}
	/**
	 * Builder dell'entity di tracciamento delle richieste di protocollo in ingresso per un repositoryFile di un istanza
	 * @param istanza
	 * @param repositoryFile
	 * @param params
	 * @param datiSmistaDocumento
	 * @return
	 */
	private ProtocolloRichiestaEntity buildProtocolloRichiestaAllegato(Istanza istanza,	RepositoryFileLazyEntity repositoryFile, ProtocolloParams params, DatiSmistaDocumentoType datiSmistaDocumento) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(datiSmistaDocumento.getIdDocumentoFruitore());
		result.setUuidRichiesta(UUID.randomUUID().toString());
		result.setStato(ProtocolloRichiestaEntity.Stato.EL.name());
		result.setTipoIngUsc(ProtocolloRichiestaEntity.TipoInOut.IN.getId());
		result.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.INTEGRAZIONE_ALLEGATO.getId());
		result.setIdIstanza(istanza.getIdIstanza());
		result.setIdAllegatoIstanza(null);
		result.setIdFile(repositoryFile.getIdFile());
		result.setIdModulo(istanza.getModulo().getIdModulo());
		result.setIdArea(istanza.getIdArea());
		result.setIdEnte(istanza.getIdEnte());
		result.setIdProtocollatore(ID_PROTOCOLLATORE);
		result.setIdStoricoWorkflow(repositoryFile.getIdStoricoWorkflow());
		return result;
	}
	private ConfigurazioneChiamanteType retrieveConfIntegrazione(ProtocolloParams params) throws BusinessException {
		try {
	        LOG.debug("[" + CLASS_NAME + "::retrieveConfIntegrazione] IN params=" + params);
			ConfigurazioneChiamanteType result = new ConfigurazioneChiamanteType();
			result.setCodiceFiscaleEnte(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_ENTE));
			result.setCodiceFruitore(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FRUITORE));
			result.setCodiceApplicazione(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_APPLICAZIONE));
			result.setCodiceTipoDocumento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_TIPO_DOC_INTEGRAZIONE)); // TODO
			LOG.debug("[" + CLASS_NAME + "::retrieveConfIntegrazione] OUT result=" + result);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveConfIntegrazione] Exception "+e.getMessage());
			throw new BusinessException("Errore recupero configurazione da ProtocolloParams per generazione ConfigurazioneChiamanteType su Integrazione","MOONSRV-30608");
		}
	}
	private ConfigurazioneChiamanteType retrieveConfAllegatoIntegrazione(ProtocolloParams params) throws BusinessException {
		try {
	        LOG.debug("[" + CLASS_NAME + "::retrieveConfAllegatoIntegrazione] IN params=" + params);
			ConfigurazioneChiamanteType result = new ConfigurazioneChiamanteType();
			result.setCodiceFiscaleEnte(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_ENTE));
			result.setCodiceFruitore(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FRUITORE));
			result.setCodiceApplicazione(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_APPLICAZIONE));
			result.setCodiceTipoDocumento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_TIPO_DOC_INTEGRAZIONE_ALLEGATI));
			LOG.debug("[" + CLASS_NAME + "::retrieveConfAllegatoIntegrazione] OUT result=" + result);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveConfAllegatoIntegrazione] Exception "+e.getMessage());
			throw new BusinessException("Errore recupero configurazione da ProtocolloParams per generazione ConfigurazioneChiamanteType su AllegatoIntegrazione","MOONSRV-30609");
		}
	}


	//
	//
	@Override
	public String protocollaFile(Istanza istanza, ProtocolloParams params) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::protocollaFile] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::protocollaFile] IN istanza="+istanza);
				LOG.debug("[" + CLASS_NAME + "::protocollaFile] IN params="+params);
			}
			this.istanza = istanza;
			
			// Protocolla il documento principale (file)
			PrResponse respPrPrincipale = protocollaFilePrincipale(istanza, params);
		
			return respPrPrincipale==null?"":respPrPrincipale.getMessageUUIDPrincipale();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaFile] BusinessException "+be.getMessage());
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::protocollaFile] ERRORE "+e.getMessage());
			throw new BusinessException(PROTOCOLLO_BUSINESS_EXCEPTION,"MOONSRV-30610");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaFile] END");
		}
	}
	private PrResponse protocollaFilePrincipale(Istanza istanza, ProtocolloParams params) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaFilePrincipale] BEGIN");
			DatiSmistaDocumentoType datiSmistaDocumento = buildDatiSmistaDocumentoTypeFile(istanza, params);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaFile(istanza, params, datiSmistaDocumento);
			if (isFileUscitaNonProtocollato(istanza, params.getRepositoryFile().getIdFile())) {
				// Chiama STARDAS per il documento principale
				SmistaDocumentoResponseType response = stardasDAO.smistaDocumento(makeSmistaDocumentoRequest(retrieveConfFileUscita(params), datiSmistaDocumento));
				richiesta = readResponse(richiesta, response);
				protocolloRichiestaDAO.insert(richiesta);
				boolean protocollatoAdesso = true;
				return new PrResponse(response==null?"":response.getMessageUUID(),protocollatoAdesso);
			} else {
				LOG.debug("[" + CLASS_NAME + "::protocollaFilePrincipale] esiste già una richiesta di protocollo per idFile " +params.getRepositoryFile().getIdFile() );
				return null;
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::protocollaFilePrincipale] DAOException "+daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaFilePrincipale] BusinessException "+be.getMessage());
			throw be;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::protocollaFilePrincipale] END");
		}
	}
	private DatiSmistaDocumentoType buildDatiSmistaDocumentoTypeFile(Istanza istanza, ProtocolloParams params) {
		DatiSmistaDocumentoType datiSmistaDocumento = new DatiSmistaDocumentoType();
		datiSmistaDocumento.setResponsabileTrattamento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_RESPONSABILE_TRATTAMENTO));
		datiSmistaDocumento.setIdDocumentoFruitore(istanza.getCodiceIstanza() + "-Ricevuta-" + params.getRepositoryFile().getCodiceFile());
		DocumentoElettronicoType doc = new DocumentoElettronicoType();
		doc.setNomeFile(istanza.getCodiceIstanza() + "-Ricevuta-" + params.getRepositoryFile().getCodiceFile() + ".pdf");
		doc.setContenutoBinario(buildEmbeddedBinary(params.getContent()));
		doc.setRiferimentoECM(null);
		doc.setDocumentoFirmato(false);
		doc.setMimeType(MediaType.APPLICATION_PDF_VALUE);
		datiSmistaDocumento.setDocumentoElettronico(doc);
		datiSmistaDocumento.setDatiDocumentoXML(null);
		boolean isDocFirmato = false;
		datiSmistaDocumento.setMetadati(_retrieveMetadati(istanza, params, ProtocolloRichiestaEntity.TipoDoc.RICEVUTA, isDocFirmato));
		datiSmistaDocumento.setNumAllegati(0);
		logDebug("buildDatiSmistaDocumentoTypeFile", istanza, datiSmistaDocumento);
		return datiSmistaDocumento;
	}
	private void logDebug(final String methodName, Istanza istanza, DatiSmistaDocumentoType datiSmistaDocumento) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::"+methodName+"] " + istanza.getCodiceIstanza() + " datiSmistaDocumento=" + datiSmistaDocumento);
		}
	}


	/**
	 * Builder dell'entity di tracciamento delle richieste di protocollo in uscita per un file (Ricevuta)
	 * @param istanza
	 * @param params
	 * @param datiSmistaDocumento
	 * @return
	 */
	private ProtocolloRichiestaEntity buildProtocolloRichiestaFile(Istanza istanza, ProtocolloParams params, DatiSmistaDocumentoType datiSmistaDocumento) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(datiSmistaDocumento.getIdDocumentoFruitore());
		result.setUuidRichiesta(UUID.randomUUID().toString());
		result.setStato(ProtocolloRichiestaEntity.Stato.EL.name());
		result.setTipoIngUsc(ProtocolloRichiestaEntity.TipoInOut.OUT.getId());
		result.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.RICEVUTA.getId());
		result.setIdIstanza(istanza.getIdIstanza());
		result.setIdModulo(istanza.getModulo().getIdModulo());
		result.setIdArea(istanza.getIdArea());
		result.setIdEnte(istanza.getIdEnte());
		result.setIdProtocollatore(ID_PROTOCOLLATORE);
		result.setIdFile(params.getRepositoryFile().getIdFile());
		return result;
	}
	/**
	 * Funzione di recupero dei metadati di un File (Ricevuta in Uscita) specifico per Modulo : DEVE essere Overwriten nella Class specifica 
	 * @param istanza
	 * @param params
	 * @param tipoDoc
	 * @return MetadatiType
	 */
	protected MetadatiType retrieveMetadatiFile(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato) {
		return retrieveMetadati(istanza, params, tipoDoc, isDocFirmato);
	}
	
	private ConfigurazioneChiamanteType retrieveConfFileUscita(ProtocolloParams params) throws BusinessException {
		try {
	        LOG.debug("[" + CLASS_NAME + "::retrieveConfFileUscita] IN params=" + params);
			ConfigurazioneChiamanteType result = new ConfigurazioneChiamanteType();
			result.setCodiceFiscaleEnte(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_ENTE));
			result.setCodiceFruitore(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_FRUITORE));
			result.setCodiceApplicazione(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_APPLICAZIONE));
			result.setCodiceTipoDocumento(params.getConf().getWithCorrectType(ProtocolloAttributoKeys.CODICE_TIPO_DOCUMENTO_USCITA));
			LOG.debug("[" + CLASS_NAME + "::retrieveConfFileUscita] OUT result=" + result);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveConfFileUscita] Exception "+e.getMessage());
			throw new BusinessException("Errore recupero configurazione da ProtocolloParams per generazione ConfigurazioneChiamanteType su File in Uscita","MOONSRV-30611");
		}
	}

	// public only for TEST
	@Override
	public MetadatiType _retrieveMetadati(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato) throws BusinessException {
		return retrieveMetadatiSwitch(istanza, params, tipoDoc, isDocFirmato, null, null);
	}

	// public only for TEST
	@Override
	public MetadatiType _retrieveMetadatiAllegato(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato, AllegatoLazyEntity allegato) {
		return retrieveMetadatiSwitch(istanza, params, tipoDoc, isDocFirmato, allegato, null);
	}
	
	private MetadatiType _retrieveMetadatiAllegatoAdditional(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato, RepositoryFileLazyEntity allegatoAdditional) {
		return retrieveMetadatiSwitch(istanza, params, tipoDoc, isDocFirmato, null, allegatoAdditional);
	}
	
	private MetadatiType retrieveMetadatiSwitch(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato,
			AllegatoLazyEntity allegato, RepositoryFileLazyEntity allegatoAdditional) throws BusinessException {
		MetadatiType result = null;
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiSwitch] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiSwitch] IN istanza="+istanza);
				LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiSwitch] IN params="+params);
			}
			this.istanza = istanza;
			Boolean useMetadatiConfigured = params.getConf().getWithCorrectType(ProtocolloAttributoKeys.USE_METADATI_CONFIGURED);
			if (Boolean.TRUE.equals(useMetadatiConfigured)) {
				result = retrieveMetadatiConfigured(istanza, params, tipoDoc, isDocFirmato, Optional.ofNullable(allegato));
			} else {
				switch(tipoDoc) {
					case ISTANZA_ALLEGATO:
//					case INTEGRAZIONE_ALLEGATO:
						result = retrieveMetadatiAllegato(istanza, params, tipoDoc, isDocFirmato, allegato);
						break;
					case ADDITIONAL_ALLEGATO:
						result = retrieveMetadatiAllegatoAdditional(istanza, params, tipoDoc, isDocFirmato, allegatoAdditional);
						break;
					case RICEVUTA:
						result = retrieveMetadatiFile(istanza, params, tipoDoc, isDocFirmato);
						break;
					default:
						result = retrieveMetadati(istanza, params, tipoDoc, isDocFirmato);
				}
			}
			return result;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiSwitch] BusinessException "+be.getMessage());
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiSwitch] ERRORE "+e.getMessage());
			throw new BusinessException(PROTOCOLLO_BUSINESS_EXCEPTION,"MOONSRV-30600");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiSwitch] END");
		}
	}
	
	private MetadatiType retrieveMetadatiConfigured(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato, Optional<AllegatoLazyEntity> optional) {
		MetadatiType result = null;
		try {	
			// 1. Recuperare ma ModuloAttributoKeys.PRT_METADATI di tutti tipiDoc
			ModuloAttributoEntity maPrtMetadati = moduloAttributiDAO.findByNome(istanza.getModulo().getIdModulo(), ModuloAttributoKeys.PRT_METADATI.name());
			if (maPrtMetadati==null || StringUtils.isEmpty(maPrtMetadati.getValore())) {
				return null;
			}
			
			// 2. Read JSON and retrieve object metadati e identificare la conf del tipoDoc
			ObjectMapper mapper = new ObjectMapper();
			List<ProtocolloMetadatoConfJson> list = mapper.readValue(maPrtMetadati.getValore(), new TypeReference<List<ProtocolloMetadatoConfJson>>(){});
			if (list==null || list.isEmpty()) {
				return null;
			}
	        Optional<ProtocolloMetadatoConfJson> metadatiConf = list.stream().filter(m -> tipoDoc.name().equals(m.getType())).findFirst();
	        if (metadatiConf.isEmpty()) {
				return null;
	        }
	        
			// 3. Ciclare ed effettuare i replace dei valori
			StrReplaceHelper strReplaceHelper = new StrReplaceHelper(istanza);
			result = new MetadatiType();
			for (Map.Entry<String, String> entry : metadatiConf.get().getMetadati().entrySet()) {
				String value = strReplaceHelper.replaceDinamici(entry.getValue(), istanza);
		        LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiConfigured] id_istanza=" + istanza.getIdIstanza() + " " + entry.getKey() + ":" + entry.getValue() + " => " + value);
		        result.getMetadato().add(makeNewMetadato(entry.getKey(), value));
		    }
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiConfigured] id_istanza=" + istanza.getIdIstanza()
				+ " ; modulo=" + istanza.getModulo().getIdModulo() + "-" + istanza.getModulo().getCodiceModulo(), e);
		}
		return result;
	}
	
}
