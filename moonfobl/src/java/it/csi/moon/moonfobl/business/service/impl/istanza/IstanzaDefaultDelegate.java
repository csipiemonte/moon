/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.istanza;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.CreaIuvResponse;
import it.csi.moon.commons.dto.EPayPagoPAParams;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;
import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.dto.extra.demografia.DocumentoRiconoscimento;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.ProcessoEntity;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.entity.WorkflowEntity;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.commons.mapper.IstanzaMapper;
import it.csi.moon.commons.mapper.StatoMapper;
import it.csi.moon.commons.mapper.WorkflowMapper;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.commons.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.commons.util.decodifica.DecodificaTipoModificaDati;
import it.csi.moon.moonfobl.business.service.impl.dao.AzioneDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.CodiceIstanzaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ProcessoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiAzioneHelper;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonfobl.business.service.impl.helper.dto.IntegrazioneActionEntity;
import it.csi.moon.moonfobl.business.service.impl.helper.task.AssociaAllegatiAdIstanzaTask;
import it.csi.moon.moonfobl.business.service.impl.helper.task.EstraiDichiaranteTask;
import it.csi.moon.moonfobl.business.service.impl.helper.task.PostSaveIstanzaTaskManager;
import it.csi.moon.moonfobl.business.service.impl.processo.ProcessoServiceDelegate;
import it.csi.moon.moonfobl.business.service.impl.processo.ProcessoServiceDelegateFactory;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

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

	private static final String CLASS_NAME = "IstanzaDefaultDelegate";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
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
	@Autowired
	AzioneDAO azioneDAO;
	@Autowired
	EpayRichiestaDAO epayRichiestaDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	ProcessoDAO processoDAO;
	
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
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN moduloEntity: " + moduloEntity);
			
			// TODO validare 1.Stato modulo PUB e 2. accessibile dall'utente

			Istanza istanza = moonsrvDAO.initIstanza(httpRequest.getRemoteAddr(), user, moduloEntity.getIdModulo(), moduloEntity.getIdVersioneModulo(), params);

			return istanza;
		} catch (ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] Soggetto non trovato - ", nfe);
			throw new BusinessException("Dati non trovati","404");
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] Errore invocazione DAO - " + daoe);
			throw new BusinessException(daoe);
		}
	}
		
	// SAVE ISTANZA
	//
	@Override
//	@Transactional // Deve rimanere in IstanzaService
	public IstanzaSaveResponse saveIstanza(UserInfo user, Istanza istanza) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::saveIstanza] IN istanza: "+istanza);
			validateForSaving(user, istanza);
			
			boolean insertMode = true;
			Date now = new Date();
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(istanza.getModulo().getIdModulo(), istanza.getModulo().getIdVersioneModulo());
//			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
//			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
			
			IstanzaEntity eIstanza = null;
			Integer lastStatoWf = null;
			if (istanza.getIdIstanza()!=null) {
				eIstanza = istanzaDAO.findById(istanza.getIdIstanza());
				lastStatoWf = eIstanza.getIdStatoWf();
				insertMode = false;
				validateForModifying(user, istanza, eIstanza);
			} else {
				eIstanza = new IstanzaEntity();
				eIstanza.setGruppoOperatoreFo(user.getGruppoOperatoreFo());
			}
			
			// 1-BOZZA 2-INVIATA
			DecodificaStatoIstanza dStato = DecodificaStatoIstanza.byIdStatoWf(istanza.getStato().getIdStato()); // BOZZA o COMPLETATA
			StatoEntity statoE = dStato.getStatoEntity();
			boolean completaMode = DecodificaStatoIstanza.COMPLETATA.equals(dStato);
			
			// ISTANZE
			eIstanza.setIdentificativoUtente(user.getIdentificativoUtente()); // Per Visibilità dell'istanza nel caso di uso FO da parte di area / ente  e non Cittadino
			eIstanza.setCodiceFiscaleDichiarante(user.getCodFiscDichIstanza());
			eIstanza.setCognomeDichiarante(user.getCognomeDich());
			eIstanza.setNomeDichiarante(user.getNomeDich());
			eIstanza.setIdStatoWf(statoE.getIdStatoWf());
			eIstanza.setAttoreIns(user.getIdentificativoUtente());
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
			if (!insertMode && lastIstanzaCronologia!=null) {
				lastIstanzaDati = istanzaDAO.findDati(eIstanza.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
			}
			if (insertMode || (lastIstanzaDati!=null && completaMode)) {
				eIstanzaDati = new IstanzaDatiEntity();
				eIstanzaDati.setIdIstanza(eIstanza.getIdIstanza());
				eIstanzaDati.setAttoreUpd(user.getIdentificativoUtente());
				eIstanzaDati.setDataUpd(now);
				eIstanzaDati.setDatiIstanza(String.valueOf(istanza.getData()));
				eIstanzaDati.setIdCronologiaStati(eIstanzaCronologia.getIdCronologiaStati());
				eIstanzaDati.setIdStepCompilazione(null);
				eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.INI.getIdTipoModifica());
				Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
				eIstanzaDati.setIdDatiIstanza(idIstanzaDati);
			} else {
				if (lastIstanzaDati!=null) {
					eIstanzaDati = lastIstanzaDati;
					eIstanzaDati.setDataUpd(now);
					eIstanzaDati.setDatiIstanza(String.valueOf(istanza.getData()));
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
			Istanza istanzaSaved = IstanzaMapper.buildFromIstanzaEntity(eIstanza, eIstanzaCronologia, eIstanzaDati, statoE, moduloE/*, attributi*/);
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
			LOG.error("[" + CLASS_NAME + "::saveIstanza] UnivocitaIstanzaBusinessException");
			throw univocitaBEx;
//			IstanzaSaveResponse result = new IstanzaSaveResponse();
//			result.setCodice("NO_ALREADY_SEND");
//			result.setDescrizione("Attenzione, la sua istanza non può essere accolta per presenza di un'istanza precedente.");
//			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::saveIstanza] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::saveIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::saveIstanza] ", e);
			throw e;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::saveIstanza] Errore generico servizio saveIstanza", ex);
			throw new BusinessException("Errore generico saveIstanza");
		}

	}

	private String retrieveCodiceAzioneProssimaAlCompletamento(Long idProcesso) {
		WorkflowFilter filterNextAfterCompletamento = new WorkflowFilter();
		filterNextAfterCompletamento.setIdProcesso(idProcesso);
		filterNextAfterCompletamento.setIdStatoWfPartenza(DecodificaStatoIstanza.COMPLETATA.getIdStatoWf());
		return workflowDAO.find(filterNextAfterCompletamento).stream()
			.filter(w -> w.getIdStatoWfArrivo()>1)
			.findFirst()
			.map(w -> azioneDAO.findById(w.getIdAzione()).getCodiceAzione())
			.orElse(null);
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
				LOG.error("[" + CLASS_NAME + "::verifyHashAK] ALREADY_SEND ");
								
				throw new UnivocitaIstanzaBusinessException(msgUnivocita);
			}
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::verifyHashAK] DAOException ", e);
			throw new BusinessException("Errore countIstanzeByModuloHash from verifyHashAK");
		}
	}
	
	private String calcHash(String ak) throws BusinessException {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] sha256 = digest.digest(ak.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(sha256);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("[" + CLASS_NAME + "::calcHash] NoSuchAlgorithmException SHA-256");
			throw new BusinessException("calcHash NoSuchAlgorithmException");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::calcHash] Exception " + e.getMessage(), e);
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
		List<String> results = new ArrayList<>();
		DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
		datiIstanzaHelper.initDataNode(istanza);
		for (int i = 0 ; i < fields.length ; i++) {
			String field = fields[i].trim();
	    	LOG.debug("[" + CLASS_NAME + "::getAK] LOOP " + field);
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
    	LOG.debug("[" + CLASS_NAME + "::getAK] END " + result);
		return result;
	}

	protected void validateForSaving(UserInfo user, Istanza istanza) throws BusinessException {
		if (istanza==null || istanza.getStato()==null || istanza.getStato().getIdStato()==null) {
			LOG.error("[" + CLASS_NAME + "::validateForSaving] istanza.getStato().getIdStato() NULL user:"+user.getIdentificativoUtente()+"  istanza:"+istanza );
			throw new BusinessException("Dato Mancante : istanza.stato.idStato");
		}
		if (istanza==null || istanza.getModulo()==null || istanza.getModulo().getIdModulo()==null) {
			LOG.error("[" + CLASS_NAME + "::validateForSaving] istanza.getModulo().getIdModulo() NULL  user:"+user.getIdentificativoUtente()+"  istanza:"+istanza );
			throw new BusinessException("Dato Mancante : istanza.modulo.idModulo");
		}
		if (istanza==null || istanza.getData()==null || String.valueOf(istanza.getData()).isEmpty()) {
			LOG.error("[" + CLASS_NAME + "::validateForSaving] istanza.getData() NULL  user:"+user.getIdentificativoUtente()+"  istanza:"+istanza );
			throw new BusinessException("Dato Mancante : istanza.data");
		}
	}
	
	

	private void validateForModifying(UserInfo user, Istanza istanza, IstanzaEntity eIstanza) throws BusinessException {
		// Visibilità per CFDichiarante che puo essere un area o un ente (calcolato all'init della session in Iride Filter e forse riaclcolato nel' cambio ente FO quando ci sara')
		if(!(user.getCodFiscDichIstanza().equals(eIstanza.getCodiceFiscaleDichiarante()) || user.isOperatore())) {
			LOG.error("[" + CLASS_NAME + "::validateForModifying] user.codFiscDichIst:"+user.getCodFiscDichIstanza()+"  eIstanza:"+eIstanza.getCodiceFiscaleDichiarante() );
			throw new BusinessException("Istanze Validation : codiceFiscaleDichiaranteIstanza");
		}

		if(!eIstanza.getIdModulo().equals(istanza.getModulo().getIdModulo())) {
			LOG.error("[" + CLASS_NAME + "::validateForModifying] idModuloDB:"+eIstanza.getIdModulo()+"  idModulo:"+istanza.getModulo().getIdModulo() );
			throw new BusinessException("Istanze Validation : idModulo");
		}
		if(!DecodificaStatoIstanza.BOZZA.getIdStatoWf().equals(eIstanza.getIdStatoWf())) {
			LOG.error("[" + CLASS_NAME + "::validateForModifying] IdStatoWfDB:"+eIstanza.getIdStatoWf()+"  NOT BOZZA" );
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

	public IstanzaSaveResponse invia(UserInfo user, IstanzaEntity istanzaE, ModuloVersionatoEntity moduloE, String ipAddress) throws BusinessException {
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::invia] IN istanzaE: " + istanzaE);
			}
//			validaInvia(istanzaE);
			
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
			istanzaEDati.setDatiIstanza(istanzaEDati.getDatiIstanza());
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
			Istanza istanzaSaved = IstanzaMapper.buildFromIstanzaEntity(istanzaE, istanzaECronologia, istanzaEDati, statoInviataE, moduloE/*, attributi*/);
			IstanzaSaveResponse result = new IstanzaSaveResponse();
			result.setIstanza(istanzaSaved);
			result.setCodice("SUCCESS");
			result.setDescrizione(null); // "Istanza inviata correttamente.");

			// Spostato per avere oggetto istanzaSaved
			istanzaE.setHashUnivocita(calcAndVerifHashUnivocita(user, istanzaSaved, moduloE, attributi));
			istanzaE.setDatiAggiuntivi(creaDatiAggiuntivi(user));
			istanzaDAO.update(istanzaE);
			
			changeStateIfNeeded(user, istanzaSaved); // with exec Azione con flag_auto (senza init datiAzione)
			
			// saveExt
			runEstraiDichiaranteIfRequested(user, result, attributi);
			inviaUlteriori(user, null, result, idProcesso);
				
			// azioniUlterioi ASYNC :: invioMail, Protocollazione
			Executors.newSingleThreadExecutor().execute(new PostSaveIstanzaTaskManager(user, result, moduloE, attributi, ipAddress));
			
			return result;
			
		} catch (UnivocitaIstanzaBusinessException univocitaBEx) {
			LOG.error("[" + CLASS_NAME + "::invia] UnivocitaIstanzaBusinessException");
			throw univocitaBEx;					
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::invia] Errore servizio invia", e);
			throw new BusinessException("Errore servizio invia");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::invia] Errore generico servizio invia", ex);
			throw new BusinessException("Errore generico invia");
		}
	}
	
	private String creaDatiAggiuntivi(UserInfo user) throws Exception  {
		return getMapper().writeValueAsString(user.getDatiAggiuntivi());
	}
	
	public void runEstraiDichiaranteIfRequested(UserInfo user, IstanzaSaveResponse istanzaSaveResponse, MapModuloAttributi mapModuloAttributi) {
        Instant start = java.time.Instant.now();
        String response = null;
        LOG.info("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] BEGIN Task...");
        try {
        	// Estrai Dichiarante
        	Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE);
    		if (Boolean.TRUE.equals(activated)) {
				String conf = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE_CONF);
				if (StringUtils.isNotEmpty(conf)) {
					LOG.info("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] new EstraiDichiaranteTask().call() ...");
					response = new EstraiDichiaranteTask(user, istanzaSaveResponse.getIstanza(), conf).call();
				}
    		}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] Exception ", e);
			throw new BusinessException();
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	/**
	 * gestisciPagamento ha come vocazione di chiudere l'istanza alla modifica come lo fa COMPLETA
	 */
	public IstanzaSaveResponse gestisciPagamento(UserInfo user, Istanza istanza) throws BusinessException {
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::gestisciPagamento] IN istanza: " + istanza);
			}

			CreaIuvResponse creaIuvResponse = moonsrvDAO.creaIUV(istanza.getIdIstanza());
			if (StringUtils.isBlank(creaIuvResponse.getIuv())) {
				LOG.error("[" + CLASS_NAME + "::gestisciPagamento] Errore invocazione DAO per moonsrv - IUV blank");
				throw new BusinessException("Errore gestisciPagamento - IUV blank per istanza=" + istanza.getIdIstanza());
			}
			
			Long idProcesso = moduloDAO.findIdProcesso(istanza.getModulo().getIdModulo());
			StoricoWorkflowEntity storicoWF = cambiaStatoIstanza(user, istanza, idProcesso, DecodificaAzione.GESTISCI_PAGAMENTO);
			aggiornaStoricoWFsuRichiestaIUV(creaIuvResponse, storicoWF);
			
			// RESPONSE
			IstanzaSaveResponse result = new IstanzaSaveResponse();
			istanza.setCodiceAvviso(creaIuvResponse.getCodiceAvviso());
			istanza.setIuv(creaIuvResponse.getIuv());
			result.setIstanza(istanza);
			result.setCodice("SUCCESS");
			result.setDescrizione(null); // "Istanza salvata correttamente.");
			result.setUrlRedirect(creaIuvResponse.getUrlRedirect());

			return result;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::gestisciPagamento] Errore servizio gestisciPagamento", e);
			throw new BusinessException("Errore servizio invia");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::gestisciPagamento] Errore generico servizio gestisciPagamento", ex);
			throw new BusinessException("Errore generico gestisciPagamento");
		}
	}

	private void aggiornaStoricoWFsuRichiestaIUV(CreaIuvResponse creaIuvResponse, StoricoWorkflowEntity storicoWF) {
		epayRichiestaDAO.updateIdStoricoWF(creaIuvResponse.getIdRichiesta(), storicoWF.getIdStoricoWorkflow());
	}

	/**
	 * pagaOnline ha come vocazione di recuperare la urlRedirect verso PagoPA ed effettuare cambio di stato
	 */
	public IstanzaSaveResponse pagaOnline(UserInfo user, Istanza istanza) throws BusinessException {
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::pagaOnline] IN istanza: " + istanza);
			}
			
			CreaIuvResponse epayResponse = moonsrvDAO.pagoPA(istanza.getIdIstanza(), new EPayPagoPAParams.Builder().codiceFiscale(user.getIdentificativoUtente()).build());
			if (StringUtils.isBlank(epayResponse.getUrlRedirect())) {
				LOG.error("[" + CLASS_NAME + "::pagaOnline] Errore invocazione DAO per moonsrv - getUrlRedirect() blank");
				throw new BusinessException("Errore pagaOnline - getUrlRedirect() blank per istanza=" + istanza.getIdIstanza());
			}
			
			Long idProcesso = moduloDAO.findIdProcesso(istanza.getModulo().getIdModulo());
			cambiaStatoIstanza(user, istanza, idProcesso, DecodificaAzione.PAGA_ONLINE);
			
			// RESPONSE
			IstanzaSaveResponse result = new IstanzaSaveResponse();
			result.setIstanza(istanza);
			result.setCodice("SUCCESS");
			result.setDescrizione(null);
			result.setUrlRedirect(epayResponse.getUrlRedirect());
			return result;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::pagaOnline] Errore servizio pagaOnline", e);
			throw new BusinessException("Errore servizio pagaOnline");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::pagaOnline] Errore generico servizio pagaOnline", ex);
			throw new BusinessException("Errore generico pagaOnline");
		}
	}

	/**
	 * pagaSportello ha come vocazione di recuperare la urlRedirect verso PagoPA ed effettuare cambio di stato
	 */
	public IstanzaSaveResponse pagaSportello(UserInfo user, Istanza istanza) throws BusinessException {
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::pagaSportello] IN istanza: " + istanza);
			}
			
			Long idProcesso = moduloDAO.findIdProcesso(istanza.getModulo().getIdModulo());
			cambiaStatoIstanza(user, istanza, idProcesso, DecodificaAzione.PAGA_SPORTELLO);
			
			// RESPONSE
			IstanzaSaveResponse result = new IstanzaSaveResponse();
			result.setIstanza(istanza);
			result.setCodice("SUCCESS");
			result.setDescrizione(null);
			result.setUrlRedirect(null);
			return result;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::pagaSportello] Errore servizio pagaSportello", e);
			throw new BusinessException("Errore servizio pagaSportello");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::pagaSportello] Errore generico servizio pagaSportello", ex);
			throw new BusinessException("Errore generico pagaSportello");
		}
	}

	/**
	 * annulla effettua il cambio di stato e secondo gli stati provenienza/destinazione :
	 * - annullare un pagamento IUV ed effettuare cambio di stato
	 */
	public IstanzaSaveResponse annulla(UserInfo user, Istanza istanza) throws BusinessException {
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::annulla] IN istanza: " + istanza);
			}
		
			Long idProcesso = moduloDAO.findIdProcesso(istanza.getModulo().getIdModulo());
			StoricoWorkflowEntity storicoWF = cambiaStatoIstanza(user, istanza, idProcesso, DecodificaAzione.ANNULLA);

			// Se nuovo stato è DA PAGARE, annulo il precedente e genero un nuovo IUV
			annullaIuvIfNecessary(istanza, storicoWF);
			
			// RESPONSE
			IstanzaSaveResponse result = new IstanzaSaveResponse();
			result.setIstanza(istanza);
			result.setCodice("SUCCESS");
			result.setDescrizione(null);
			result.setUrlRedirect(null);
			return result;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::annulla] Errore servizio annulla", e);
			throw new BusinessException("Errore servizio annulla");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::annulla] Errore generico servizio annulla", ex);
			throw new BusinessException("Errore generico annulla");
		}
	}

	protected void annullaIuvIfNecessary(Istanza istanza, StoricoWorkflowEntity storicoWF) {
		if (DecodificaStatoIstanza.DA_PAGARE.isCorrectStato(istanza.getStato().getIdStato())) {
			LOG.info("[" + CLASS_NAME + "::annullaIuvIfNecessary] Necessary on istanza " + istanza.getIdIstanza() + " - "+ istanza.getCodiceIstanza());
			CreaIuvResponse annullaIuvResponse = moonsrvDAO.annullaIUV(istanza.getIdIstanza(), istanza.getIuv());
			if (StringUtils.isBlank(annullaIuvResponse.getIuv())) {
				LOG.error("[" + CLASS_NAME + "::annullaIuvIfNecessary] Errore invocazione DAO per moonsrv - IUV blank");
				throw new BusinessException("Errore annulla - IUV blank per istanza=" + istanza.getIdIstanza());
			}
			aggiornaStoricoWFsuRichiestaIUV(annullaIuvResponse, storicoWF);

			istanza.setCodiceAvviso(annullaIuvResponse.getCodiceAvviso());
			istanza.setIuv(annullaIuvResponse.getIuv());
			istanza.setDataEsitoPagamento(null);
		}
	}

	private void changeStateIfNeeded(UserInfo user, Istanza istanza) {

		StoricoWorkflowEntity lastStoricoWf = storicoWorkflowDAO.findLastStorico(istanza.getIdIstanza())
				.orElseThrow(ItemNotFoundBusinessException::new);
		WorkflowFilter filter = new WorkflowFilter();
		filter.setIdModulo(istanza.getModulo().getIdModulo());
		filter.setIdProcesso(lastStoricoWf.getIdProcesso());
		filter.setIdStatoWfPartenza(lastStoricoWf.getIdStatoWfArrivo());
		filter.setEscludiAzioniUtenteCompilante(true);
		List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
		
		if (elencoWorkflow.size() == 1) {
			WorkflowEntity wf = elencoWorkflow.get(0);
			if ("S".equals(wf.getFlagAutomatico()) && wf.getIdStatoWfPartenza().intValue() >= 2) {
				Azione azione = new Azione();
				azione.setIdAzione(wf.getIdAzione());
				azione.setIdIstanza(istanza.getIdIstanza());
				azione.setIdWorkflow(wf.getIdWorkflow());
				azione.setDatiAzione("");
				compieAzione(user, istanza.getIdIstanza(), azione);
			}
		}
	}
	
	private void compieAzione(UserInfo user,Long idIstanza,Azione azione) throws BusinessException{
		
		try {
			LOG.debug("[" + CLASS_NAME + "::compieAzione] IN istanza: "+idIstanza + " id workflow: " + azione.getIdWorkflow());
			
			Date now = new Date();

//			Workflow workflow = getWorkflowById(azione.getIdWorkflow());
			WorkflowEntity workflowE = workflowDAO.findById(azione.getIdWorkflow());
			Workflow workflow = WorkflowMapper.buildFromWorkflowEntity(workflowE);
			IstanzaEntity istanzaE =  istanzaDAO.findById(idIstanza);

			StatoEntity statoPartenzaE = statoDAO.findById(workflow.getIdStatoWfPartenza());
			StatoEntity statoArrivoE = statoDAO.findById(workflow.getIdStatoWfArrivo());
			
			String descDestinatario = identificaDestinatario(workflow);
			
			StoricoWorkflowEntity storicoEntity = new StoricoWorkflowEntity() ;
			storicoEntity.setAttoreUpd(user.getIdentificativoUtente());
			storicoEntity.setIdIstanza(idIstanza);
			storicoEntity.setIdProcesso(workflow.getIdProcesso());
			storicoEntity.setIdStatoWfPartenza(workflow.getIdStatoWfPartenza());
			storicoEntity.setIdStatoWfArrivo(workflow.getIdStatoWfArrivo());
			storicoEntity.setIdAzione(workflow.getIdAzione());
			storicoEntity.setDataInizio(now);
			storicoEntity.setNomeStatoWfPartenza(statoPartenzaE.getNomeStatoWf());
			storicoEntity.setNomeStatoWfArrivo(statoArrivoE.getNomeStatoWf());
			storicoEntity.setNomeAzione(workflow.getNomeAzione());
			storicoEntity.setDescDestinatario(descDestinatario);
			storicoEntity.setIdDatiazione(workflow.getIdDatiAzione());
			
			LOG.info("[" + CLASS_NAME + "::compieAzione] dati Azione: " + azione.getDatiAzione());
			if (azione != null && azione.getDatiAzione() != null && !azione.getDatiAzione().equals(""))
			{
				storicoEntity.setDatiAzione(azione.getDatiAzione());
			}
			
			// Chiusura storicoWorkflow precedente e inserimento del nuovo record in moon_wf_t_storico_workflow
			int numRecordAggiornati = storicoWorkflowDAO.updateDataFine(now, idIstanza);
			Long idStoricoWorkflow = storicoWorkflowDAO.insert(storicoEntity);
			storicoEntity.setIdStoricoWorkflow(idStoricoWorkflow);
			LOG.info("[" + CLASS_NAME + "::compieAzione] NEW idStoricoWorkflow: " + idStoricoWorkflow);
			updateIdIstanzaStoricoWorkflowOnRepositoryFileByWf(user, azione, workflow, storicoEntity);

			String url = null;
			CompieAzioneResponse azioneCompiuta = null;
			if (azione != null && azione.getDatiAzione()!=null && !azione.getDatiAzione().equals(""))
			{
				ProcessoEntity processo = processoDAO.findById(workflow.getIdProcesso());
				ProcessoServiceDelegate processoServiceDelegate = new ProcessoServiceDelegateFactory().getDelegate(processo.getCodiceProcesso());
				azioneCompiuta = processoServiceDelegate.compieAzione(user, istanzaE, azione, workflow, processo, storicoEntity);
			}
			
			//update stato istanza e cronologia se richiesto da workflow
			updateStatoIstanzaIfRequestedByWf(user, now, idIstanza, workflowE);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] DAOException " + e.getMessage());
			throw new BusinessException("Errore inserimento nuovo step workflow");
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] BusinessException " + e.getMessage());
			throw e;
		}
	}
	
	private void updateStatoIstanzaIfRequestedByWf(UserInfo user, Date now, Workflow workflow, Azione azione) throws DAOException {
		WorkflowEntity workflowE = workflowDAO.findById(azione.getIdWorkflow());
		updateStatoIstanzaIfRequestedByWf(user, now, azione.getIdIstanza(), workflowE);
	}
	
	private void updateStatoIstanzaIfRequestedByWf(UserInfo user, Date now, Long idIstanza, WorkflowEntity workflowE) throws DAOException{
		if ("S".equals(workflowE.getFlagStatoIstanza())) {
			istanzaDAO.updateStato(idIstanza, workflowE.getIdStatoWfArrivo());		
			// CRON
			IstanzaCronologiaStatiEntity istanzaECronologia = null;
			// last CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(idIstanza);
			lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
			lastIstanzaCronologia.setDataFine(now);
			istanzaDAO.updateCronologia(lastIstanzaCronologia);
			// new CRON
			istanzaECronologia = new IstanzaCronologiaStatiEntity();
			istanzaECronologia.setIdIstanza(idIstanza);
			istanzaECronologia.setIdStatoWf(workflowE.getIdStatoWfArrivo());
			istanzaECronologia.setAttoreIns(user.getIdentificativoUtente());
			istanzaECronologia.setDataInizio(now);
			istanzaECronologia.setIdAzioneSvolta(workflowE.getIdAzione());
			Long idCronologiaStati = istanzaDAO.insertCronologia(istanzaECronologia);
			istanzaECronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI
			IstanzaDatiEntity istanzaEDati = null;
			istanzaEDati = istanzaDAO.findDati(idIstanza, lastIstanzaCronologia.getIdCronologiaStati());
			istanzaEDati.setDataUpd(now);
			
	        istanzaEDati.setDatiIstanza(istanzaEDati.getDatiIstanza());
			
			istanzaEDati.setIdCronologiaStati(istanzaECronologia.getIdCronologiaStati());
			istanzaEDati.setIdStepCompilazione(null);
			istanzaEDati.setIdTipoModifica(DecodificaTipoModificaDati.NON.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(istanzaEDati);
			istanzaEDati.setIdDatiIstanza(idIstanzaDati);
		} else {
			LOG.info("[" + CLASS_NAME + "::updateStatoIstanzaIfRequestedByWf] NO istanzaDAO.updateStato for " + idIstanza);
		}
	}

	private void updateIdIstanzaStoricoWorkflowOnRepositoryFileByWf(UserInfo user, Azione azione, Workflow workflow, /*Istanza istanza,*/ StoricoWorkflowEntity storicoEntity) {
		try {
			if (azione!=null && !azione.getDatiAzione().equals("")) {

				if (workflow.getCodiceAzione().equals(DecodificaAzione.INVIA_INTEGRAZIONE.getCodice()) || 
						workflow.getCodiceAzione().equals(DecodificaAzione.INVIA_RICEVUTA.getCodice()) ||
						workflow.getCodiceAzione().equals(DecodificaAzione.INVIA_OSSERVAZIONI.getCodice())						
						){
				
					LOG.info("[" + CLASS_NAME + "::updateIdIstanzaStoricoWorkflowOnRepositoryFileByWf] Prova assegnazione IdStoricoWorkflow su RepositoryFile ...");
					// FROM Azione_Default
					IntegrazioneActionEntity notificaActionEntity = DatiAzioneHelper.parseAzioneNotificaIntegrazione(azione.getDatiAzione());
					List<String> formIoFileNames = notificaActionEntity.getIntegrazioneFormIoFileNames();
					for (String formioNomeFileName : formIoFileNames) {
						int numRecordAggiornati = repositoryFileDAO.updateIdStoricoWorkflowIdIstanzaByFormioFileName(formioNomeFileName, storicoEntity.getIdIstanza(), storicoEntity.getIdStoricoWorkflow());
						LOG.info("[" + CLASS_NAME + "::updateIdIstanzaStoricoWorkflowOnRepositoryFileByWf] idIstanza=" + storicoEntity.getIdIstanza() + " fileName=" + formioNomeFileName + " sono aggiornati " + numRecordAggiornati + " records.");
					}
				}
			}
		} catch (BusinessException e) {
			LOG.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowOnRepositoryFileByWf] BusinessException " + e.getMessage());
		}
	}
	
	private String identificaDestinatario(Workflow workflow) throws BusinessException {
		String descDestinatario = "";
		LOG.debug("[" + CLASS_NAME + "::identificaDestinatario] id utente destinatario: "+ workflow.getIdUtenteDestinatario() + 
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
			mapper = new ObjectMapper()
				.setSerializationInclusion(Include.NON_EMPTY)
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		}
		return mapper;
	}
	
	protected void inviaUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, Long idProcesso) throws BusinessException {
		// IMPLEMENTED in specific Class
	}

}
