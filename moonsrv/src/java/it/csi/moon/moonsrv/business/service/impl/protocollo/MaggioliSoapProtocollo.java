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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maggioli.prt.cxfclient.ArrayOfMittenteDestinatarioIn;
import com.maggioli.prt.cxfclient.ArrayOfRecapitoIn;
import com.maggioli.prt.cxfclient.MittenteDestinatarioIn;
import com.maggioli.prt.cxfclient.ProtocolloIn;
import com.maggioli.prt.cxfclient.ProtocolloOut;
import com.maggioli.prt.cxfclient.RecapitoIn;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ProtocolloMetadatoConfJson;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ProtocolloRichiestaEntity;
import it.csi.moon.commons.entity.ProtocolloRichiestaFilter;
import it.csi.moon.commons.entity.RepositoryFileLazyEntity;
import it.csi.moon.commons.util.MapProtocolloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.ProtocolloAttributoKeys;
import it.csi.moon.moonsrv.business.service.FirmaDigitaleService;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ProtocolloRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.MaggioliProtocolloSoapDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.dto.maggioli.soap.InserisciDocumentoRequest;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.stardas.cxfclient.MetadatiType;
import it.csi.stardas.cxfclient.MetadatoType;

public class MaggioliSoapProtocollo extends BaseProtocollo implements Protocollo {

	private static final String CLASS_NAME = "MaggioliSoapProtocollo";
	protected static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String PROTOCOLLO_BUSINESS_EXCEPTION = "ProtocolloBusinessException";
	private static final Integer ID_PROTOCOLLATORE = ProtocolloRichiestaEntity.Protocollatore.MAGGIOLI_SOAP.getId();
	
	protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private Istanza istanza;
	private MapProtocolloAttributi conf;
	private StrReplaceHelper strReplaceHelper;
	
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
	MaggioliProtocolloSoapDAO maggioliProtocolloSoapDAO;
	@Autowired
	ProtocolloRichiestaDAO protocolloRichiestaDAO;
	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	FirmaDigitaleService firmaDigitaleService;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	
	/**
	 * Protocolla un istanza e il suo pdf gia generato passato in params
	 * insieme alla configurazione MAGGIOLI ProtocolloSoap necessaria :
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
			this.conf = params.getConf();
			this.strReplaceHelper = new StrReplaceHelper(istanza);
			
			// Protocolla il documento principale dell'istanza
			PrResponse respPrPrincipale = protocollaIstanzaPrincipale(istanza, params);
			
			// Protocolla XML resoconto se necessario
//			protocollaXmlResocontoIfNecessary(istanza, params, respPrPrincipale);
			
			// Protocolla gli allegati se necessario
//			protocollaIstanzaAllegatiIfNecessary(istanza, params, respPrPrincipale);
			
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
			InserisciDocumentoRequest req = buildInserisciDocumentoRequestIstanza(istanza, params);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaIstanza(istanza, params, req);
			String messageUUID = getMessageUUIDiFDocPrincipaleProtocollato(istanza);
			if (messageUUID == null || messageUUID.equals("")) {
				LOG.debug("[" + CLASS_NAME + "::protocollaIstanzaPrincipale] call maggioliProtocolloSoapDAO.inserisciDocumentoEAnagraficheString with " + req);
				// Chiama STARDAS per il documento principale
				ProtocolloOut protocolloOut = maggioliProtocolloSoapDAO.inserisciProtocolloEAnagraficheString(req);
				richiesta = readResponseString(richiesta, protocolloOut);
				protocolloRichiestaDAO.insert(richiesta);
				boolean protocollatoAdesso = true;
				return new PrResponse(String.valueOf(protocolloOut==null?"":protocolloOut.getIdDocumento()), protocollatoAdesso);
			} else {
				boolean protocollatoAdesso = false;
				return new PrResponse(messageUUID,protocollatoAdesso);
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
	
	private boolean isFileUscitaNonProtocollato (Istanza istanza, Long idFile) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::isFileUscitaNonProtocollato] BEGIN");
		boolean isNonProtocollato = true;

		ProtocolloRichiestaFilter filter = new ProtocolloRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(ProtocolloRichiestaEntity.TipoDoc.RICEVUTA.getId());
		filter.setIdFile(idFile);
		List<ProtocolloRichiestaEntity> elencoRichieste = protocolloRichiestaDAO.find(filter);
		if (elencoRichieste != null && elencoRichieste.size() > 0) {
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
		if (elencoRichieste != null && elencoRichieste.size() > 0) {
			for (ProtocolloRichiestaEntity richiesta: elencoRichieste) {
				if (!StringUtils.isEmpty(richiesta.getUuidProtocollatore()) && 
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
		if (elencoRichieste != null && elencoRichieste.size() > 0) {
			for (ProtocolloRichiestaEntity richiesta: elencoRichieste) {
				if (!StringUtils.isEmpty(richiesta.getUuidProtocollatore()) && 
						("000".equals(richiesta.getCodiceEsito()) || "001".equals(richiesta.getCodiceEsito())) ) {
					messageUUID = richiesta.getUuidProtocollatore();
				}
			}
		}
		return messageUUID;
	}

	private ProtocolloRichiestaEntity readResponseString(ProtocolloRichiestaEntity richiesta, ProtocolloOut protocolloOut) {
		String respResCodice = null;
		String respResMessaggio = null;
		String respUuidProtocollatore = null;
		if (protocolloOut!=null) {
			LOG.debug("[" + CLASS_NAME + "::readResponseString] protocolloOut=" + protocolloOut);
			respUuidProtocollatore = String.valueOf(protocolloOut.getIdDocumento());
			respResMessaggio = protocolloOut.getMessaggio();
			respResCodice = StringUtils.isEmpty(protocolloOut.getErrore())?"000":"100";
			if (!StringUtils.isEmpty(protocolloOut.getErrore())) {
				LOG.warn("[" + CLASS_NAME + "::readResponseString] idIstanza=" + richiesta.getIdIstanza() + " codiceIstanza:" + richiesta.getCodiceRichiesta() + " ERRORE: "+ protocolloOut);
			}
			richiesta = completeProtocolloRichiesta(richiesta, respResCodice, respResMessaggio, respUuidProtocollatore);
		} else {
			LOG.warn("[" + CLASS_NAME + "::readResponseString] String null from maggioliSoapProtocolloDAO !");
			richiesta = completeProtocolloRichiesta(richiesta, "response null");
		}
		return richiesta;
	}


	private InserisciDocumentoRequest buildInserisciDocumentoRequestIstanza(Istanza istanza, ProtocolloParams params) {
		InserisciDocumentoRequest result = new InserisciDocumentoRequest();
		result.protocolloIn = new ProtocolloIn();
		result.protocolloIn.setClassifica(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CLASSIFICA));
		result.protocolloIn.setOggetto(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.OGGETTO)); // "oggetto": "TEST MODIFICA",
		result.protocolloIn.setOrigine(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.ORIGINE)); // "origine": "I",
		result.protocolloIn.setTipoDocumento(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CODICE_TIPO_DOCUMENTO)); // "tipoDocumento": "BANDO",
		result.protocolloIn.setMittenteInterno(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITTENTE_INTERNO)); // "mittenteInterno": "SEGRETERIA",
	    MittenteDestinatarioIn mitDestIn = new MittenteDestinatarioIn();
	    mitDestIn.setCodiceFiscale(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_CODICE_FISCALE)); // "partitaIVA": "ABCDEFGHILMNOPQU",
	    mitDestIn.setCognomeNome(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_COGNOME_NOME)); // "cognomeSoggetto": "Test Utente semplice",
	    mitDestIn.setNome(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_NOME)); // "cognomeSoggetto": "Test Utente semplice",
        mitDestIn.setIndirizzo(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_INDIRIZZO)); // "indirizzo": "via milano 11",
        mitDestIn.setLocalita(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_LOCALITA));
        mitDestIn.setCodiceComuneResidenza(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_CD_CM_RESID)); // "codiceComuneResidenza": "12894"
        String email = retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_RECAPITO_EMAIL);
        if (!StringUtils.isEmpty(email)) {
	        RecapitoIn recapito = new RecapitoIn();
	        recapito.setTipoRecapito("EMAIL");
	        recapito.setValoreRecapito(email);
	        ArrayOfRecapitoIn recapiti = new ArrayOfRecapitoIn();
	        recapiti.getItem().add(recapito);
	        mitDestIn.setRecapiti(recapiti);
        }
        ArrayOfMittenteDestinatarioIn mittenti = new ArrayOfMittenteDestinatarioIn();
		mittenti.getMittenteDestinatario().add(mitDestIn);
		result.protocolloIn.setMittentiDestinatari(mittenti); // "mittenteDestinatari": [ ]
		result.codiceAmministrazione = retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CODICE_AMMINISTRAZIONE);
		result.codiceAOO = retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CODICE_AOO);
		return result;
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
	private ProtocolloRichiestaEntity buildProtocolloRichiestaIstanza(Istanza istanza, ProtocolloParams params,	InserisciDocumentoRequest req) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(istanza.getCodiceIstanza());
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
	private ProtocolloRichiestaEntity buildProtocolloRichiestaIntegrazione(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params, InserisciDocumentoRequest req) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(istanza.getCodiceIstanza()+"-"+idStoricoWorkflow);
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
	 * Completa un entity di tracciamento delle richieste di protocollo dopo esito positivo di STARDAS
	 * @param richiesta
	 * @param codiceEsito
	 * @param descEsito
	 * @param uuidProtocollatore
	 * @return
	 */
	private ProtocolloRichiestaEntity completeProtocolloRichiesta(ProtocolloRichiestaEntity richiesta, String codiceEsito, String descEsito, String uuidProtocollatore) {
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
			this.conf = params.getConf();
			this.strReplaceHelper = new StrReplaceHelper(istanza);
			
			// Protocolla il documento principale dell'istanza
			PrResponse respPrPrincipale = protocollaIntegrazionePrincipale(istanza, idStoricoWorkflow, params);
			
			// Protocolla gli allegati se necessario
//			protocollaAllegatiIntegrazioneIfNecessary(istanza, idStoricoWorkflow, params, respPrPrincipale);
			
			return respPrPrincipale.getMessageUUIDPrincipale();
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
			InserisciDocumentoRequest req = buildInserisciDocumentoRequestIntegrazione(istanza, idStoricoWorkflow, params);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaIntegrazione(istanza, idStoricoWorkflow, params, req);
			String messageUUID = getMessageUUIDiFIntegrazionePrincipaleProtocollato(istanza, idStoricoWorkflow);
			if (StringUtils.isEmpty(messageUUID)) {
				ProtocolloOut protocolloOut = maggioliProtocolloSoapDAO.inserisciProtocolloEAnagraficheString(req);
				richiesta = readResponseString(richiesta, protocolloOut);
				protocolloRichiestaDAO.insert(richiesta);
				boolean protocollatoAdesso = true;
				return new PrResponse(String.valueOf(protocolloOut==null?"":protocolloOut.getIdDocumento()), protocollatoAdesso);
			} else {
				boolean protocollatoAdesso = false;
				return new PrResponse(messageUUID, protocollatoAdesso);
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
	private InserisciDocumentoRequest buildInserisciDocumentoRequestIntegrazione(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params) {
		InserisciDocumentoRequest result = new InserisciDocumentoRequest();
		result.protocolloIn = new ProtocolloIn();
		result.protocolloIn.setOggetto(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.OGGETTO)); // "oggetto": "TEST MODIFICA",
		result.protocolloIn.setOrigine(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.ORIGINE)); // "origine": "I",
		result.protocolloIn.setTipoDocumento(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CODICE_TIPO_DOCUMENTO)); // "tipoDocumento": "BANDO",
		result.protocolloIn.setMittenteInterno(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITTENTE_INTERNO)); // "mittenteInterno": "SEGRETERIA",
	    MittenteDestinatarioIn mitDestIn = new MittenteDestinatarioIn();
	    mitDestIn.setCodiceFiscale(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_CODICE_FISCALE)); // "partitaIVA": "ABCDEFGHILMNOPQU",
	    mitDestIn.setCognomeNome(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_COGNOME_NOME)); // "cognomeSoggetto": "Test Utente semplice",
        mitDestIn.setIndirizzo(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_INDIRIZZO)); // "indirizzo": "via milano 11",
        mitDestIn.setCodiceComuneResidenza(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_CD_CM_RESID)); // "codiceComuneResidenza": "12894"
		ArrayOfMittenteDestinatarioIn mittenti = new ArrayOfMittenteDestinatarioIn();
		mittenti.getMittenteDestinatario().add(mitDestIn);
		result.protocolloIn.setMittentiDestinatari(mittenti); // "mittenteDestinatari": [ ]
		result.codiceAmministrazione = retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CODICE_AMMINISTRAZIONE);
		result.codiceAOO = retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CODICE_AOO);
		return result;
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
			this.conf = params.getConf();
			this.strReplaceHelper = new StrReplaceHelper(istanza);
			
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
			InserisciDocumentoRequest req = buildInserisciDocumentoRequestFile(istanza, params);
			ProtocolloRichiestaEntity richiesta = buildProtocolloRichiestaFile(istanza, params, req);
			if (isFileUscitaNonProtocollato(istanza, params.getRepositoryFile().getIdFile())) {
				// Chiama STARDAS per il documento principale
				ProtocolloOut protocolloOut = maggioliProtocolloSoapDAO.inserisciProtocolloEAnagraficheString(req);
				richiesta = readResponseString(richiesta, protocolloOut);
				protocolloRichiestaDAO.insert(richiesta);
				boolean protocollatoAdesso = true;
				return new PrResponse(String.valueOf(protocolloOut==null?"":protocolloOut.getIdDocumento()), protocollatoAdesso);
			}
			else {
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
	private InserisciDocumentoRequest buildInserisciDocumentoRequestFile(Istanza istanza, ProtocolloParams params) {
		InserisciDocumentoRequest result = new InserisciDocumentoRequest();
		result.protocolloIn = new ProtocolloIn();
		result.protocolloIn.setOggetto(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.OGGETTO)); // "oggetto": "TEST MODIFICA",
		result.protocolloIn.setOrigine(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.ORIGINE)); // "origine": "I",
		result.protocolloIn.setTipoDocumento(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CODICE_TIPO_DOCUMENTO)); // "tipoDocumento": "BANDO",
		result.protocolloIn.setMittenteInterno(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITTENTE_INTERNO)); // "mittenteInterno": "SEGRETERIA",
	    MittenteDestinatarioIn mitDestIn = new MittenteDestinatarioIn();
	    mitDestIn.setCodiceFiscale(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_CODICE_FISCALE)); // "partitaIVA": "ABCDEFGHILMNOPQU",
	    mitDestIn.setCognomeNome(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_COGNOME_NOME)); // "cognomeSoggetto": "Test Utente semplice",
        mitDestIn.setIndirizzo(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_INDIRIZZO)); // "indirizzo": "via milano 11",
        mitDestIn.setCodiceComuneResidenza(retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.MITDEST_CD_CM_RESID)); // "codiceComuneResidenza": "12894"
		ArrayOfMittenteDestinatarioIn mittenti = new ArrayOfMittenteDestinatarioIn();
		mittenti.getMittenteDestinatario().add(mitDestIn);
		result.protocolloIn.setMittentiDestinatari(mittenti); // "mittenteDestinatari": [ ]
		result.codiceAmministrazione = retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CODICE_AMMINISTRAZIONE);
		result.codiceAOO = retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys.CODICE_AOO);
		return result;
	}

	/**
	 * Builder dell'entity di tracciamento delle richieste di protocollo in uscita per un file (Ricevuta)
	 * @param istanza
	 * @param params
	 * @param datiSmistaDocumento
	 * @return
	 */
	private ProtocolloRichiestaEntity buildProtocolloRichiestaFile(Istanza istanza, ProtocolloParams params, InserisciDocumentoRequest datiSmistaDocumento) {
		ProtocolloRichiestaEntity result = new ProtocolloRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(istanza.getCodiceIstanza());
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
			if (list==null || list.size()==0) {
				return null;
			}
	        Optional<ProtocolloMetadatoConfJson> metadatiConf = list.stream().filter(m -> tipoDoc.name().equals(m.getType())).findFirst();
	        if(metadatiConf.isEmpty()) {
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
	
	//
	private String retrieveConfValueWithReplaceDinamici(ProtocolloAttributoKeys keyConf) {
		return strReplaceHelper.replaceDinamici(retrieveConfTextValue(keyConf), istanza);
	}

	private String retrieveConfTextValue(ProtocolloAttributoKeys keyConf) {
		return this.conf.getWithCorrectType(keyConf);
	}
}
