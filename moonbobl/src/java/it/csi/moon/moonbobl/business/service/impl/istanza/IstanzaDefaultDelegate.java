/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.istanza;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.CodiceIstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaSaveResponse;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloAttributoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.EstraiDichiaranteHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.task.AssociaAllegatiAdIstanzaTask;
import it.csi.moon.moonbobl.business.service.impl.helper.task.PostSaveIstanzaTaskManager;
import it.csi.moon.moonbobl.business.service.mapper.IstanzaMapper;
import it.csi.moon.moonbobl.business.service.mapper.StatoMapper;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntivi;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntiviHeaders;
import it.csi.moon.moonbobl.dto.moonfobl.DocumentoRiconoscimento;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonbobl.util.IstanzaUtils;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.MapModuloAttributi;
import it.csi.moon.moonbobl.util.ModuloAttributoKeys;
import it.csi.moon.moonbobl.util.StrUtils;
import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoModificaDati;


/**
 * IstanzaDefaultDelegate - Operazioni di default per i moduli che non hanno specializzazione.
 * 
 * Delegate delle operazioni di IstanzeServiceImpl che possono essere specializzate per Modulo
 * Attualmente 2 metodi :
 * - getInitIstanzaByIdModulo : generalmente per bloccare l'accesso al modulo per esaurimento delle risorsa
 * - saveIstanza : generalmente per salvataggio ulteriore in altre tabelle
 * 
 * saveIstanza rimane transazionale da IstanzeServiceImpl, quindi qualsiasi Exception annulla l'inserimento dell'istanza
 * 
 * @author laurent
 *
 */
public class IstanzaDefaultDelegate implements IstanzaServiceDelegate {

	private final static String CLASS_NAME = "IstanzaDefaultDelegate";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	static ObjectMapper mapper;
	
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	StatoDAO statoDAO;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	ModuloProgressivoDAO moduloProgressivoDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	@Qualifier("nocache")
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	CodiceIstanzaDAO codiceIstanzaDAO;
	@Autowired
	WorkflowDAO workflowDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	
	
	public IstanzaDefaultDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	//
	// INIT ISTANZA
	//
	@Override
	public Istanza getInitIstanza(UserInfo user, ModuloVersionatoEntity moduloEntity, MapModuloAttributi attributi, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getInitIstanza] IN moduloEntity: " + moduloEntity);
			
			// TODO validare 1.Stato modulo PUB e 2. accessibile dall'utente

			Istanza istanza = moonsrvDAO.initIstanza(httpRequest.getRemoteAddr(), user, moduloEntity.getIdModulo(), moduloEntity.getIdVersioneModulo(), params);

			return istanza;
		} catch (ItemNotFoundDAOException nfe) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] Soggetto non trovato - ", nfe);
			throw new BusinessException("Dati non trovati","404");
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] Errore invocazione DAO - " + daoe);
			throw new BusinessException(daoe);
		}
	}
		
	// SAVE ISTANZA
	//
	@Override
//	@Transactional // Deve rimanere in IstanzaService
	public IstanzaSaveResponse saveIstanza(UserInfo user, Istanza istanza) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::saveIstanza] IN istanza: "+istanza);
			validateForSaving(user, istanza);
			
			boolean insertMode = true;
			Date now = new Date();
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(istanza.getModulo().getIdModulo(), istanza.getModulo().getIdVersioneModulo());
			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
			
			IstanzaEntity eIstanza = null;
			Integer lastStatoWf = null;
			if (istanza.getIdIstanza()!=null) {
				eIstanza = istanzaDAO.findById(istanza.getIdIstanza());
				lastStatoWf = eIstanza.getIdStatoWf();
				insertMode = false;
				validateForModifying(user, istanza, eIstanza);
			} else {
				eIstanza = new IstanzaEntity();
			}
			
			// 1-BOZZA 2-INVIATA
			DecodificaStatoIstanza dStato = DecodificaStatoIstanza.byIdStatoWf(istanza.getStato().getIdStato()); // BOZZA o COMPLETATA
			StatoEntity statoE = dStato.getStatoEntity();
			boolean completaMode = DecodificaStatoIstanza.COMPLETATA.equals(dStato);
			
			// ISTANZE
			eIstanza.setIdentificativoUtente(user.getIdentificativoUtente());
			eIstanza.setCodiceFiscaleDichiarante(user.getIdentificativoUtente());
			eIstanza.setCognomeDichiarante(user.getCognome());
			eIstanza.setNomeDichiarante(user.getNome());
			eIstanza.setIdStatoWf(statoE.getIdStatoWf());
			
			// gestione eventuale operatore di compilazione che compila per se stesso 			
			eIstanza.setAttoreIns((istanza.getAttoreIns() != null) ? user.getIdentificativoUtente()+"_"+istanza.getAttoreIns(): user.getIdentificativoUtente());
			
			if (StringUtils.isEmpty(eIstanza.getCodiceIstanza())) {
				String descTipoCodiceIstanza = codiceIstanzaDAO.findById(moduloE.getIdTipoCodiceIstanza());
				eIstanza.setCodiceIstanza(moduloProgressivoDAO.generateCodiceIstanzaForIdModulo(moduloE, descTipoCodiceIstanza));
			}
			eIstanza.setDataCreazione(now);
			eIstanza.setFlagEliminata(istanza.getFlagEliminata()?"S":"N");
			eIstanza.setFlagArchiviata(istanza.getFlagArchiviata()?"S":"N");
			eIstanza.setFlagTest(istanza.getFlagTest()?"S":"N");
			eIstanza.setImportanza(istanza.getImportanza()!=null?istanza.getImportanza():0);
			eIstanza.setCurrentStep(istanza.getCurrentStep());
			eIstanza.setIdModulo(istanza.getModulo().getIdModulo());
			eIstanza.setIdVersioneModulo(istanza.getModulo().getIdVersioneModulo());
			eIstanza.setIdEnte(istanza.getIdEnte()!=null?istanza.getIdEnte():user.getEnte().getIdEnte());
			if (insertMode) {
				Long idIstanza = istanzaDAO.insert(eIstanza);
				istanza.setIdIstanza(idIstanza);
				eIstanza.setIdIstanza(idIstanza);
			} else {
				istanzaDAO.update(eIstanza);
			}
			
			estraiDichiaranteIfRequested(user, istanza,attributi);
			
			// CRON
			IstanzaCronologiaStatiEntity eIstanzaCronologia = null;
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = null;
			if (!insertMode) {
				lastIstanzaCronologia = istanzaDAO.findLastCronologia(eIstanza.getIdIstanza());
			}
			if (insertMode || (lastIstanzaCronologia!=null && completaMode)) {
				if (lastIstanzaCronologia!=null) {
					lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
					lastIstanzaCronologia.setDataFine(now);
					istanzaDAO.updateCronologia(lastIstanzaCronologia);
				}
				eIstanzaCronologia = new IstanzaCronologiaStatiEntity();
				eIstanzaCronologia.setIdIstanza(eIstanza.getIdIstanza());
				eIstanzaCronologia.setIdStatoWf(statoE.getIdStatoWf());
				eIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
				eIstanzaCronologia.setDataInizio(now);
				eIstanzaCronologia.setIdAzioneSvolta(completaMode?DecodificaAzione.COMPLETA.getIdAzione():DecodificaAzione.SALVA_BOZZA.getIdAzione());
				Long idCronologiaStati = istanzaDAO.insertCronologia(eIstanzaCronologia);
				eIstanzaCronologia.setIdCronologiaStati(idCronologiaStati);
			} else {
				if (lastIstanzaCronologia!=null) {
					eIstanzaCronologia = lastIstanzaCronologia;
					eIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
					istanzaDAO.updateCronologia(eIstanzaCronologia);
				}
			}
			
			// DATI
			IstanzaDatiEntity eIstanzaDati = null;
			IstanzaDatiEntity lastIstanzaDati = null;
			if (!insertMode) {
				lastIstanzaDati = istanzaDAO.findDati(eIstanza.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
			}
			if (insertMode || (lastIstanzaDati!=null && completaMode)) {
				eIstanzaDati = new IstanzaDatiEntity();
				eIstanzaDati.setIdIstanza(eIstanza.getIdIstanza());
				eIstanzaDati.setAttoreUpd(user.getIdentificativoUtente());
				eIstanzaDati.setDataUpd(now);
				eIstanzaDati.setDatiIstanza(IstanzaUtils.modificaContestoBoToFo(String.valueOf(istanza.getData())));
				eIstanzaDati.setIdCronologiaStati(eIstanzaCronologia.getIdCronologiaStati());
				eIstanzaDati.setIdStepCompilazione(null);
				eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.INI.getIdTipoModifica());
				Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
				eIstanzaDati.setIdDatiIstanza(idIstanzaDati);
			} else {
				if (lastIstanzaDati!=null) {
					eIstanzaDati = lastIstanzaDati;
					eIstanzaDati.setDataUpd(now);
					eIstanzaDati.setDatiIstanza(IstanzaUtils.modificaContestoBoToFo(String.valueOf(istanza.getData())));
					istanzaDAO.updateDati(eIstanzaDati);
				}
			}
			
			//
			// STORICO WORKFLOW
			Long idProcesso = null;
			if (completaMode) {
				String descDestinatario = "compilatore";
				idProcesso = moduloDAO.findIdProcesso(istanza.getModulo().getIdModulo());
				StoricoWorkflowEntity eStoricoWf = new StoricoWorkflowEntity(null, eIstanza.getIdIstanza(), idProcesso, 
					DecodificaStatoIstanza.BOZZA, DecodificaStatoIstanza.COMPLETATA, DecodificaAzione.COMPLETA,
					descDestinatario, now, user.getIdentificativoUtente());
				storicoWorkflowDAO.updateDataFine(now, eIstanza.getIdIstanza());
				Long idStoricoWf = storicoWorkflowDAO.insert(eStoricoWf);
				eStoricoWf.setIdStoricoWorkflow(idStoricoWf);
			}
			
			// RESPONSE
			Istanza istanzaSaved = IstanzaMapper.buildFromIstanzaEntity(eIstanza, eIstanzaCronologia, eIstanzaDati,statoE, moduloE);
			IstanzaSaveResponse result = new IstanzaSaveResponse();
			result.setIstanza(istanzaSaved);
			result.setCodice("SUCCESS");
			result.setDescrizione(null); // "Istanza salvata correttamente.");
			
			// saveExt
			saveUlteriori(user, null, result, insertMode, completaMode, idProcesso);
			if (completaMode) {
	        	new AssociaAllegatiAdIstanzaTask(user, istanzaSaved, moduloE).call();
			}
			
			return result;

		} catch (UnivocitaIstanzaBusinessException univocitaBEx) {
			log.error("[" + CLASS_NAME + "::saveIstanza] UnivocitaIstanzaBusinessException");
			throw univocitaBEx;
//			IstanzaSaveResponse result = new IstanzaSaveResponse();
//			result.setCodice("NO_ALREADY_SEND");
//			result.setDescrizione("Attenzione, la sua istanza non può essere accolta per presenza di un'istanza precedente.");
//			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::saveIstanza] ", e);
			throw e;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore generico servizio saveIstanza", ex);
			throw new BusinessException("Errore generico saveIstanza");
		}

	}

	
	/**
	 * Calcola e verifica l'HASH di univocita di un istanza (istanza.hashUnivocita) solo se la configurazione del modulo lo richiede (HASH_UNIVOCITA=S)
	 * Vedere calcHashUnivocita() per il calcolo del HASH
	 * Se il HASH è già presente nel DB di MOOn allora torna UnivocitaIstanzaBusinessException
	 * @param user
	 * @param istanza
	 * @param moduloE
	 * @param attributi
	 * @return
	 * @throws UnivocitaIstanzaBusinessException  se il HASH è già presente nel DB di MOOn per questo Modulo
	 * @throws BusinessException
	 */
	protected String calcAndVerifHashUnivocita(UserInfo user, Istanza istanza, ModuloVersionatoEntity moduloE, MapModuloAttributi attributi) throws UnivocitaIstanzaBusinessException, BusinessException {
		Boolean flagHashUnivocita = attributi.getWithCorrectType(ModuloAttributoKeys.HASH_UNIVOCITA);
		String msgUnivocita = attributi.getWithCorrectType(ModuloAttributoKeys.MSG_UNIVOCITA);
		if(!Boolean.TRUE.equals(flagHashUnivocita)) {
			return null;
		}
		String hash = calcHash(getAK(user, istanza, moduloE, attributi));
		verifyHashAK(moduloE.getIdModulo(), hash, msgUnivocita, istanza.getCodiceIstanza());
		return hash;
	}
//	protected String calcAndVerifHashUnivocita(UserInfo user, 
//			IstanzaEntity istanzaE, IstanzaCronologiaStatiEntity cronE, IstanzaDatiEntity datiE, StatoEntity statoE, 
//			ModuloVersionatoEntity moduloE, MapModuloAttributi attributi) throws UnivocitaIstanzaBusinessException, BusinessException {
//		
//		Boolean flagHashUnivocita = attributi.getWithCorrectType(ModuloAttributoKeys.HASH_UNIVOCITA);		
//		String msgUnivocita = attributi.getWithCorrectType(ModuloAttributoKeys.MSG_UNIVOCITA);
//		
//		if(!Boolean.TRUE.equals(flagHashUnivocita)) {
//			return null;
//		}
//		Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(istanzaE, cronE, datiE, statoE, moduloE, attributi);
//		String hash = calcHash(getAK(user, istanza, moduloE, attributi));
//		verifyHashAK(moduloE.getIdModulo(), hash, msgUnivocita);
//		return hash;
//	}

	/**
	 * Verifica la presenza di un istanza con lo stesso Hash sulla base di una chiave AK in chiaro
	 * @param idModulo
	 * @param hash
	 * @throws UnivocitaIstanzaBusinessException  se già presnete un istanza con la chiave AK su il modulo idModulo
	 * @throws BusinessException nel caso di errore (lato DB)
	 */
	protected String verifHashUnivocitaFromAk(String ak, ModuloEntity moduloE, MapModuloAttributi attributi) throws UnivocitaIstanzaBusinessException, BusinessException {
		Boolean flagHashUnivocita = attributi.getWithCorrectType(ModuloAttributoKeys.HASH_UNIVOCITA);
		String msgUnivocita = attributi.getWithCorrectType(ModuloAttributoKeys.MSG_UNIVOCITA);
		if(StrUtils.isEmpty(ak) || !Boolean.TRUE.equals(flagHashUnivocita)) {
			return null;
		}
		String hash = calcHash(ak);
		verifyHashAK(moduloE.getIdModulo(), hash, msgUnivocita, null);
		return hash;
	}

	
	/**
	 * Verifica la presenza di un istanza con lo stesso Hash 
	 * @param idModulo
	 * @param hash
	 * @param msgUnivocita 
	 * @throws UnivocitaIstanzaBusinessException  se già presnete un istanza con la chiave AK su il modulo idModulo
	 * @throws BusinessException nel caso di errore (lato DB)
	 */
	private void verifyHashAK(Long idModulo, String hash, String msgUnivocita, String codiceIstanza) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			Integer giaPresente = istanzaDAO.countIstanzeByModuloHash(idModulo,hash,codiceIstanza);
			if (giaPresente>0) {
				log.error("[" + CLASS_NAME + "::verifyHashAK] ALREADY_SEND ");
								
				throw new UnivocitaIstanzaBusinessException(msgUnivocita);
			}
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::verifyHashAK] DAOException ", e);
			throw new BusinessException("Errore countIstanzeByModuloHash from verifyHashAK");
		}
	}
	
	private String calcHash(String ak) throws BusinessException {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] sha256 = digest.digest(ak.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(sha256);
		} catch (NoSuchAlgorithmException e) {
			log.error("[" + CLASS_NAME + "::calcHash] NoSuchAlgorithmException SHA-256");
			throw new BusinessException("calcHash NoSuchAlgorithmException");
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::calcHash] Exception " + e.getMessage(), e);
			throw new BusinessException("calcHash Exception");
		}
	}
	
	/**
	 * Costruisce la String di dati che dovrebbe essere univoca : La AK
	 * Viene effettuata la concatenazione di diversi campi dell'istanza stessa o dell'utente collegato (quando possibile : non su Modulistica, solo la dove c'è Auth forte di tipo SPID es. @@CODICE_FISCALE@@)
	 * L'elenco dei campi da considerare nella creazione della chiave univoca è configurato nel attributo del modulo HASH_UNIVOCITA_FIELDS
	 * es.1 : cognome
	 * es.2 : pf.codiceFiscale|pg.piva
	 * @param user
	 * @param istanza
	 * @param moduloE
	 * @param attributi
	 * @return
	 * @throws BusinessException
	 */
	protected String getAK(UserInfo user, Istanza istanza, ModuloEntity moduloE, MapModuloAttributi attributi) throws BusinessException {
		String hashUnivocitaFields = attributi.getWithCorrectType(ModuloAttributoKeys.HASH_UNIVOCITA_FIELDS);
		String[] fields;
		if (hashUnivocitaFields.contains("|"))
			fields = hashUnivocitaFields.split("\\|");
		else
			fields = new String[]{ hashUnivocitaFields };
		List<String> results = new ArrayList<String>();
		DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
		datiIstanzaHelper.initDataNode(istanza);
		for (int i = 0 ; i < fields.length ; i++) {
			String field = fields[i].trim();
	    	log.debug("[" + CLASS_NAME + "::getAK] LOOP " + field);
    		switch(field) {
    			case "" : 
    				results.add("*");
    				break;
	    		case "@@CODICE_FISCALE@@" : 
	    			results.add(istanza.getCodiceFiscaleDichiarante());
    				break;
	    		default :
	    			results.add(datiIstanzaHelper.extractedTextValueFromDataNodeByKey(field));
    		}
		}
		String result = String.join("|", results);
    	log.debug("[" + CLASS_NAME + "::getAK] END " + result);
		return result;
	}

	protected void validateForSaving(UserInfo user, Istanza istanza) throws BusinessException {
		if (istanza==null || istanza.getStato()==null || istanza.getStato().getIdStato()==null) {
			log.error("[" + CLASS_NAME + "::validateForSaving] istanza.getStato().getIdStato() NULL user:"+user.getIdentificativoUtente()+"  istanza:"+istanza );
			throw new BusinessException("Dato Mancante : istanza.stato.idStato");
		}
		if (istanza==null || istanza.getModulo()==null || istanza.getModulo().getIdModulo()==null) {
			log.error("[" + CLASS_NAME + "::validateForSaving] istanza.getModulo().getIdModulo() NULL  user:"+user.getIdentificativoUtente()+"  istanza:"+istanza );
			throw new BusinessException("Dato Mancante : istanza.modulo.idModulo");
		}
		if (istanza==null || istanza.getData()==null || String.valueOf(istanza.getData()).isEmpty()) {
			log.error("[" + CLASS_NAME + "::validateForSaving] istanza.getData() NULL  user:"+user.getIdentificativoUtente()+"  istanza:"+istanza );
			throw new BusinessException("Dato Mancante : istanza.data");
		}
	}
	
	

	private void validateForModifying(UserInfo user, Istanza istanza, IstanzaEntity eIstanza) throws BusinessException {
		// Visibilità per CFDichiarante che puo essere un area o un ente (calcolato all'init della session in Iride Filter e forse riaclcolato nel' cambio ente FO quando ci sara')
//		if(!user.getCodFiscDichIstanza().equals(eIstanza.getCodiceFiscaleDichiarante())) {
//			log.error("[" + CLASS_NAME + "::validateForModifying] user.codFiscDichIst:"+user.getCodFiscDichIstanza()+"  eIstanza:"+eIstanza.getCodiceFiscaleDichiarante() );
//			throw new BusinessException("Istanze Validation : codiceFiscaleDichiaranteIstanza");
//		}

		if(!eIstanza.getIdModulo().equals(istanza.getModulo().getIdModulo())) {
			log.error("[" + CLASS_NAME + "::validateForModifying] idModuloDB:"+eIstanza.getIdModulo()+"  idModulo:"+istanza.getModulo().getIdModulo() );
			throw new BusinessException("Istanze Validation : idModulo");
		}
		if(!DecodificaStatoIstanza.BOZZA.getIdStatoWf().equals(eIstanza.getIdStatoWf())) {
			log.error("[" + CLASS_NAME + "::validateForModifying] IdStatoWfDB:"+eIstanza.getIdStatoWf()+"  NOT BOZZA" );
			throw new BusinessException("Istanze Validation : idStatoWf");
		}
	}
	
	
	protected void saveUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, boolean insertMode,
			boolean completaMode, Long idProcesso) throws BusinessException {
		// IMPLEMENTED in specific Class
	}
	

	protected StoricoWorkflowEntity cambiaStatoIstanza(UserInfo user, Istanza istanza, Long idProcesso, DecodificaAzione azione) throws ItemNotFoundDAOException, DAOException { // , DecodificaStatoIstanza statoArrivo
		WorkflowEntity azioneProcesso = workflowDAO.findByProcessoAzione(idProcesso, istanza.getStato().getIdStato(), azione.getIdAzione());
		// StoricoWorkflow
		Date now = new Date();
		StoricoWorkflowEntity newStoricoWf = new StoricoWorkflowEntity(null, istanza.getIdIstanza(), idProcesso, 
			azioneProcesso.getIdStatoWfPartenza(), azioneProcesso.getIdStatoWfArrivo(), azione.getIdAzione(),
			getNomeStatoWf(azioneProcesso.getIdStatoWfPartenza()), getNomeStatoWf(azioneProcesso.getIdStatoWfArrivo()), azioneProcesso.getNomeAzione(),
			null, now, user.getIdentificativoUtente());
		storicoWorkflowDAO.updateDataFine(now, istanza.getIdIstanza());
		Long idStoricoWf = storicoWorkflowDAO.insert(newStoricoWf);
		newStoricoWf.setIdStoricoWorkflow(idStoricoWf);
		//
		if ("S".equals(azioneProcesso.getFlagStatoIstanza())) {
			istanzaDAO.updateStato(istanza.getIdIstanza(), azioneProcesso.getIdStatoWfArrivo());
			istanza.setStato(StatoMapper.buildFromEntity(statoDAO.findById(azioneProcesso.getIdStatoWfArrivo())));
		}
		return newStoricoWf;
	}
	
	private String getNomeStatoWf(Integer idStatoWf) {
		return statoDAO.findById(idStatoWf).getNomeStatoWf();
	}

	public IstanzaSaveResponse invia(UserInfo user, IstanzaEntity istanzaE, ModuloVersionatoEntity moduloE, DatiAggiuntiviHeaders daHeaders, String ipAddress) throws BusinessException {
		try {
			if(log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::invia] IN istanzaE: " + istanzaE);
			}
			validaInvia(istanzaE); // TODO la validazione è da fare sul modulo moduloDAO via attributi

			Date now = new Date();
			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);

			// x-yyyyy 2-INVIATA
			DecodificaStatoIstanza dStatoPartenza = DecodificaStatoIstanza.byIdStatoWf(istanzaE.getIdStatoWf());
			StatoEntity statoInviataE = DecodificaStatoIstanza.INVIATA.getStatoEntity();

			// ISTANZE
			istanzaE.setAttoreUpd(user.getIdentificativoUtente());
			istanzaE.setDataProtocollo(null);
			istanzaE.setNumeroProtocollo(null);
			istanzaE.setIdStatoWf(statoInviataE.getIdStatoWf());
			istanzaE.setCurrentStep(0);
			istanzaE.setDataCreazione(now);
			// Per valorizzazione univocità vedere istruzioni al termine
			
			// CRON
			IstanzaCronologiaStatiEntity istanzaECronologia = null;
			// last CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(istanzaE.getIdIstanza());
			lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
			lastIstanzaCronologia.setDataFine(now);
			istanzaDAO.updateCronologia(lastIstanzaCronologia);
			// new CRON
			istanzaECronologia = new IstanzaCronologiaStatiEntity();
			istanzaECronologia.setIdIstanza(istanzaE.getIdIstanza());
			istanzaECronologia.setIdStatoWf(statoInviataE.getIdStatoWf());
			istanzaECronologia.setAttoreIns(user.getIdentificativoUtente());
			istanzaECronologia.setDataInizio(now);
//			azioneDAO.initCache();
			istanzaECronologia.setIdAzioneSvolta(DecodificaAzione.INVIA.getIdAzione()); //   azioneDAO.findIdByCd(DecodificaAzione.INVIA.getCodice()));
			Long idCronologiaStati = istanzaDAO.insertCronologia(istanzaECronologia);
			istanzaECronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI
			IstanzaDatiEntity istanzaEDati = null;
			istanzaEDati = istanzaDAO.findDati(istanzaE.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
			istanzaEDati.setDataUpd(now);
			istanzaEDati.setDatiIstanza(IstanzaUtils.modificaContestoBoToFo(istanzaEDati.getDatiIstanza()));
			istanzaEDati.setIdCronologiaStati(istanzaECronologia.getIdCronologiaStati());
			istanzaEDati.setIdStepCompilazione(null);
			istanzaEDati.setIdTipoModifica(DecodificaTipoModificaDati.NON.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(istanzaEDati);
			istanzaEDati.setIdDatiIstanza(idIstanzaDati);

			// STORICO WORKFLOW
			Long idProcesso = moduloDAO.findIdProcesso(istanzaE.getIdModulo());
			String descDestinatario = "pa";
			StoricoWorkflowEntity eStoricoWf = new StoricoWorkflowEntity(null, istanzaE.getIdIstanza(), idProcesso,
				dStatoPartenza, DecodificaStatoIstanza.INVIATA, DecodificaAzione.INVIA,
				descDestinatario, now, user.getIdentificativoUtente());
			storicoWorkflowDAO.updateDataFine(now, istanzaE.getIdIstanza());
			Long idStoricoWf = storicoWorkflowDAO.insert(eStoricoWf);
			eStoricoWf.setIdStoricoWorkflow(idStoricoWf);

			// RESPONSE
			Istanza istanzaSaved = IstanzaMapper.buildFromIstanzaEntity(istanzaE, istanzaECronologia, istanzaEDati,statoInviataE, moduloE);
			IstanzaSaveResponse result = new IstanzaSaveResponse();
			result.setIstanza(istanzaSaved);
			result.setCodice("SUCCESS");
			result.setDescrizione(null); // "Istanza inviata correttamente.");

			// Spostato per avere oggetto istanzaSaved
			istanzaE.setHashUnivocita(calcAndVerifHashUnivocita(user, istanzaSaved, moduloE, attributi));
			istanzaE.setDatiAggiuntivi(creaDatiAggiuntivi(user, daHeaders));
			istanzaDAO.update(istanzaE);
			
			// saveExt
			inviaUlteriori(user, null, result, idProcesso);
				
			// azioniUlterioi ASYNC :: invioMail, Protocollazione
			Executors.newSingleThreadExecutor().execute(new PostSaveIstanzaTaskManager(user, result, moduloE, attributi, ipAddress));
			
			return result;
			
		} catch (UnivocitaIstanzaBusinessException univocitaBEx) {
			log.error("[" + CLASS_NAME + "::invia] UnivocitaIstanzaBusinessException");
			throw univocitaBEx;					
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::invia] Errore servizio invia", e);
			throw new BusinessException("Errore servizio invia");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::invia] Errore generico servizio invia", ex);
			throw new BusinessException("Errore generico invia");
		}
	}
	
	private String creaDatiAggiuntivi(UserInfo user, DatiAggiuntiviHeaders daHeaders) throws Exception  {
		DatiAggiuntivi datiAggiuntivi = new DatiAggiuntivi();
		datiAggiuntivi.setDataOraLogin(user.getDataOraLogin());
		datiAggiuntivi.setProvider(user.getProvider());
		datiAggiuntivi.setHeaders(daHeaders);
		return getMapper().writeValueAsString(datiAggiuntivi);
	}

	private void validaInvia(IstanzaEntity entity) throws BusinessException {
		boolean validate = DecodificaStatoIstanza.COMPLETATA.isCorrectStato(entity);
		log.debug("[" + CLASS_NAME + "::validaInvia] idIstanza:" + entity.getIdIstanza() + "  validate:" + validate);
		if (!validate) {
			throw new BusinessException("Istanze Non piu possibile inviare");
		}
	}
	
	private void estraiDichiaranteIfRequested(UserInfo user,Istanza istanza, MapModuloAttributi mapModuloAttributi) {
        try {
        	// Estrai Dichiarante
        	Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE);
    		if (Boolean.TRUE.equals(activated)) {
				String conf = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE_CONF);
				if (StringUtils.isNotEmpty(conf)) {
					log.info("[" + CLASS_NAME + "::estraiDichiaranteIfRequested] ");
					new EstraiDichiaranteHelper(user, istanza, conf).updateDichiarante();
				}
    		}
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::estraiDichiaranteIfRequested] Exception ", e);
//			throw new BusinessException();
		}
	}

	private String identificaDestinatario(Workflow workflow) throws BusinessException {
		String descDestinatario = "";
		log.debug("[" + CLASS_NAME + "::identificaDestinatario] id utente destinatario: "+ workflow.getIdUtenteDestinatario() + 
				"  tipo utente destinatario: "+ workflow.getIdTipoUtenteDestinatario() +
				"  gruppo utenti destinatari: " + workflow.getIdGruppoUtentiDestinatari() );

		if (workflow.getIdUtenteDestinatario() != null)	{
			// TODO acquisire nome e cognome dell'utente destinatario
			descDestinatario = "nome e cognome utente destinatario";
		} else if (workflow.getIdTipoUtenteDestinatario() != null) {
			// TODO acquisire il nome della tipologia di utente destinatario
			descDestinatario = "tipologia destinatario";
		} else if (workflow.getIdGruppoUtentiDestinatari() != null) {
			// TODO acquisire il nome del gruppo di utenti destinatari
			descDestinatario = "nome gruppo destinatari";
		}
		return descDestinatario;
	}


	public static ObjectMapper getMapper() {
		if(mapper == null) {
			mapper = new ObjectMapper();
			SerializationConfig config = mapper
				.getSerializationConfig()
				.withSerializationInclusion(Inclusion.NON_EMPTY)
				.without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
			mapper.setSerializationConfig(config);
		}
		return mapper;
	}
	
	protected void inviaUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, Long idProcesso) throws BusinessException {
		// IMPLEMENTED in specific Class
	}

}
