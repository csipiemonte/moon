/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.ticketcrm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

import it.csi.apimint.troubleticketing.v1.dto.CategoriaOperativaBaseModel;
import it.csi.apimint.troubleticketing.v1.dto.CategorizzazioneTicket;
import it.csi.apimint.troubleticketing.v1.dto.EnteUser;
import it.csi.apimint.troubleticketing.v1.dto.InfoNotaWLog;
import it.csi.apimint.troubleticketing.v1.dto.LavorazioneTicket;
import it.csi.apimint.troubleticketing.v1.dto.RichiedenteDaAnagrafica;
import it.csi.apimint.troubleticketing.v1.dto.RichiedentePerAnagrafica;
import it.csi.apimint.troubleticketing.v1.dto.RichiedenteTicket;
import it.csi.apimint.troubleticketing.v1.dto.Ticket;
import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.entity.CfiCompanyEntity;
import it.csi.moon.commons.entity.TicketingSystemRichiestaEntity;
import it.csi.moon.commons.entity.TicketingSystemRichiestaFilter;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonsrv.business.service.AllegatiService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.CfiCompanyDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.TicketingSystemRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.RemedyApimintDAO;
import it.csi.moon.moonsrv.business.service.mail.EmailService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class ReadyToUseTicketingSystem extends BaseTicketingSystem implements TicketingSystem {

	private static final String CLASS_NAME = "ReadyToUseTicketingSystem";
	protected static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private Istanza istanza;
	private JsonNode conf;
	private StrReplaceHelper strReplaceHelper;
	private boolean richiedenteCreatoAdesso = false;
	
	@Autowired
	RemedyApimintDAO remedyDAO;
	@Autowired
	TicketingSystemRichiestaDAO ticketingSystemRichiestaDAO;
	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	EmailService emailService;
	@Autowired
	CfiCompanyDAO cfiCompanyDAO;
	@Autowired
	AllegatiService allegatiService;

	
	public enum ReadyToUseConfKeys {
		DETTAGLIO("dettaglio", String.class, "##dettaglio##", null, null),
		RIEPILOGO("riepilogo", String.class, "##riepilogo##", null, null),
		URGENZA("urgenza", String.class, "Bassa", null, null),
		TIPOLOGIA("tipologia", String.class, "Richiesta utente", null, null),
		IMPATTO("impatto", String.class, "Minimo/Localizzato", null, null),
		
		NOME("nome", String.class, "##nome##", null, null),
		COGNOME("cognome", String.class, "##cognome##", null, null),
		EMAIL("email", String.class, "##email##", null, null),
		TELEFONO("telefono", String.class, "##telefono##", null, null),
		MOBILE("mobile", String.class, "##mobile##", null, null), 
		
		COMPANY_NAME("companyName", String.class, "##companyName##", null, null),
		//COMPANY_ID("companyId", String.class, "##companyId##", null, null),
		CFI("cfi", String.class, null, null, null),
		
		LIVELLO1("livello1", String.class, "##livello1##", null, null),
		LIVELLO2("livello2", String.class, "##livello2##", null, null),
		LIVELLO3("livello3", String.class, "##livello3##", null, null),
		ASSEGNATARIO("assegnatario", String.class, null, null, null),
		
		EMAIL_FOR_FAILURE("emailForFailure", String.class, null, null, null),
		OGGETTO_EMAIL("oggettoEmail", String.class, null, null, null),
		BODY_EMAIL("bodyEmail", String.class, null, null, null),
		
		MEX_NEW_USER("mexNewUser", String.class, "ATTENZIONE - ANAGRAFICA DA VALIDARE", null, null),
		
		INCLUDE_ALLEGATI("include_allegati", Boolean.class, null, false, null),
		;

		private <T> ReadyToUseConfKeys(String key, Class<T> clazz, String textDefaultValue, Boolean booleanDefaultValue, Integer integerDefaultValue) {
			this.key = key;
			this.textDefaultValue = textDefaultValue;
			this.booleanDefaultValue = booleanDefaultValue;
			this.intDefaultValue = integerDefaultValue;
		}

		private String key;
		private String textDefaultValue;
		private Boolean booleanDefaultValue;
		private Integer intDefaultValue;
		
		public String getKey() {
			return key;
		}
		public String getTextDefaultValue() {
			return textDefaultValue;
		} 
		public boolean getBooleanDefaultValue() {
			return booleanDefaultValue;
		} 
		public Integer getIntDefaultValue() {
			return intDefaultValue;
		} 
	};
	  /*
	class CrmResponse {
		String ticketUUIDPrincipale;
		boolean creatoAdesso;
		//
		public CrmResponse(String ticketUUIDPrincipale, boolean creatoAdesso) {
			super();
			this.ticketUUIDPrincipale = ticketUUIDPrincipale;
			this.creatoAdesso = creatoAdesso;
		}
		// GET
		public String getTicketUUIDPrincipale() {
			return ticketUUIDPrincipale;
		}
		public boolean iscreatoAdesso() {
			return creatoAdesso;
		}
	}
	*/
	@Override
	protected String getModuloAttributoKeysName() {
		return ModuloAttributoKeys.PSIT_CRM_CONF_R2U.name();
	}
	
	@Override
	public String creaTicketIstanza(Istanza istanza, TicketingSystemParams params) throws BusinessException {
		boolean sendEmailForFailure = false;
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::creaTicketIstanza] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::creaTicketIstanza] IN istanza="+istanza);
				LOG.debug("[" + CLASS_NAME + "::creaTicketIstanza] IN params="+params);
			}
			this.istanza = istanza;
			this.conf = retrieveConf(istanza);
			this.strReplaceHelper = new StrReplaceHelper(istanza);
			
			// creaTiket principale dell'istanza
			CrmResponse respTsPrincipale = creaTicketIstanzaPrincipale(istanza, params);
			
			// creaTiket XML resoconto se necessario

			// creaTiket sugli allegati se necessario
			inviaIstanzaAllegatiIfNecessary(respTsPrincipale, istanza, params);
			
			return respTsPrincipale.getTicketUUIDPrincipale();
		} catch (BusinessException be) {
			sendEmailForFailure = true;
			LOG.error("[" + CLASS_NAME + "::creaTicketIstanza] BusinessException "+be.getMessage());
			throw be;
		} catch (Exception e) {
			sendEmailForFailure = true;
			LOG.error("[" + CLASS_NAME + "::creaTicketIstanza] ERRORE "+e.getMessage());
			throw new BusinessException("ReadytouseTicketingSystemBusinessException","MOONSRV-30700");
		} finally {
			if(sendEmailForFailure == true) {
				sendEmailForFailure(istanza);
			}
			LOG.debug("[" + CLASS_NAME + "::creaTicketIstanza] END");
		}
	}

	private void inviaIstanzaAllegatiIfNecessary(CrmResponse respTsPrincipale, Istanza istanza, TicketingSystemParams params) {
		try {
			LOG.debug("[" + CLASS_NAME + "::inviaIstanzaAllegatiIfNecessary] BEGIN");
			String ticketId = respTsPrincipale.getTicketUUIDPrincipale();
			String tipologia = "General";
			String riepilogo = istanza.getCodiceIstanza();
			TicketingSystemRichiestaEntity richiesta = null;
			TicketingSystemRichiestaEntity allegatoRichiestaEntity = null;
			
			if (Boolean.TRUE.equals(retrieveConfBooleanValue(ReadyToUseConfKeys.INCLUDE_ALLEGATI))) {
				List<AllegatoLazyEntity> allegati = allegatoDAO.findLazyByIdIstanza(istanza.getIdIstanza());
				if (allegati != null) {
					for (AllegatoLazyEntity a : allegati) {
						allegatoRichiestaEntity = getTicketingSystemRichiestaEntity(istanza, a);
						if (allegatoRichiestaEntity != null && allegatoRichiestaEntity.getCodiceEsito().equals("201")) {
							//allegato inviato e risp OK, quindi skip perche R2U non accetta reinvi di file uguali
							continue;
						} else {
							byte[] pdf = allegatiService.getById(a.getIdAllegato()).getContenuto();
							String nomeFile = a.getFormioNameFile();
							try {
								InfoNotaWLog response = remedyDAO.putInfoDettagli(ticketId,tipologia,riepilogo,nomeFile,pdf);
								richiesta = buildRichiestaEntityPerAllegato(istanza, ticketId, a.getIdAllegato(),a.getCodiceFile());
								richiesta = completeTicketingSystemRichiesta(richiesta, TicketingSystemRichiestaEntity.Stato.TX.name(), "201", "OK", ticketId, response.getLogId());
							}catch (Exception e) {
								LOG.warn("[" + CLASS_NAME + "::inviaIstanzaAllegatiIfNecessary] errore invio allegato="+a.getIdAllegato()+" "+e.getMessage());
								richiesta = buildRichiestaEntityPerAllegato(istanza, ticketId, a.getIdAllegato(),a.getCodiceFile());
								richiesta = completeTicketingSystemRichiesta(richiesta, TicketingSystemRichiestaEntity.Stato.KO.name(),"ERR", "Errore", ticketId, null);
							}
							if (allegatoRichiestaEntity != null && allegatoRichiestaEntity.getCodiceEsito().equals("ERR")) {
								//allegato inviato ma risp errore, quindi posso riprovare
								ticketingSystemRichiestaDAO.updateResponseById(richiesta);
							}
							if (allegatoRichiestaEntity == null) {
								//invio allegato 1 volta
								ticketingSystemRichiestaDAO.insert(richiesta);
							}
						}						
					}
				}
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::inviaIstanzaAllegatiIfNecessary] DAOException "+daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::inviaIstanzaAllegatiIfNecessary] BusinessException "+be.getMessage());
			throw be;
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::inviaIstanzaAllegatiIfNecessary] Exception ", e);
			throw new BusinessException();
		}
	}

	private void sendEmailForFailure(Istanza istanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::sendEmailForFailure] BEGIN");
			String email = retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.EMAIL_FOR_FAILURE);
			if( email != null && !StringUtils.isEmpty(email)) {
				EmailRequest emailRequest = new EmailRequest();
				emailRequest.setTo(email);
				emailRequest.setSubject(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.OGGETTO_EMAIL));
				emailRequest.setText(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.BODY_EMAIL));
				emailService.sendMail(emailRequest);
			}
			LOG.info("[" + CLASS_NAME + "::sendEmailForFailure] email inviata per istanza="+istanza.getCodiceIstanza());
		} catch (BusinessException b) {
			LOG.error("[" + CLASS_NAME + "::sendEmailForFailure] "+b.getMessage());
			throw b;
		} 
		catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::sendEmailForFailure] ERRORE "+e.getMessage());
			throw new BusinessException("ReadytouseTicketingSystemBusinessException","MOONSRV-30700");
		}
	}

	private String allegaIstanzaPdf(CrmResponse respTsPrincipale, Istanza istanza, TicketingSystemParams params) {
		try {
			LOG.debug("[" + CLASS_NAME + "::allegaIstanzaPdf] BEGIN");
			if(respTsPrincipale !=null) {
				String ticketId = respTsPrincipale.getTicketUUIDPrincipale();
				String tipologia = "General";
				String riepilogo = istanza.getCodiceIstanza();
				byte[] pdf = printIstanzeService.getPdfById(istanza.getIdIstanza());
				String nomeFile = istanza.getCodiceIstanza()+".pdf";
				InfoNotaWLog response = remedyDAO.putInfoDettagli(ticketId,tipologia,riepilogo,nomeFile,pdf);
				LOG.info("[" + CLASS_NAME + "::allegaIstanzaPdf] "+response.toString());
				return response.getLogId();
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::allegaIstanzaPdf] DAOException "+daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::allegaIstanzaPdf] BusinessException "+be.getMessage());
			throw be;
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::allegaIstanzaPdf] Exception ", e);
			throw new BusinessException();
		}
		return null;
	}

	private CrmResponse creaTicketIstanzaPrincipale(Istanza istanza, TicketingSystemParams params) {
		try {
			LOG.debug("[" + CLASS_NAME + "::creaTicketIstanzaPrincipale] BEGIN");
			Ticket newTicket = buildTicket(istanza);
			TicketingSystemRichiestaEntity richiesta = buildTicketingSystemRichiestaEntity(istanza, params, newTicket);
			TicketingSystemRichiestaEntity richiestaEsistente = getTicketingSystemRichiestaEntity(istanza);
			CrmResponse response = null;
			boolean creatoAdesso = false;
			if (richiestaEsistente==null || StringUtils.isEmpty(richiestaEsistente.getUuidTicketingSystem())) {
				Ticket ticket = remedyDAO.createTicket(newTicket);
				richiesta = aggiornaRichiestaConResponse(richiesta, ticket);
				Long idRichiesta = ticketingSystemRichiestaDAO.insert(richiesta);
				richiesta.setIdRichiesta(idRichiesta);
				creatoAdesso = true;
				response = new CrmResponse(richiesta.getUuidTicketingSystem(),creatoAdesso);
			} else {
				creatoAdesso = false;
				response = new CrmResponse(richiestaEsistente.getUuidTicketingSystem(),creatoAdesso);
			}
			
			// PUT per bytes del PDF istanza
			inviaIstanzaPdf(istanza, params, creatoAdesso?richiesta:richiestaEsistente, response);
			
			return response;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::creaTicketIstanzaPrincipale] DAOException "+daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::creaTicketIstanzaPrincipale] BusinessException "+be.getMessage());
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaTicketIstanzaPrincipale] Exception ", e);
			throw new BusinessException();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::creaTicketIstanzaPrincipale] END");
		}
	}

	private void inviaIstanzaPdf(Istanza istanza, TicketingSystemParams params,
		TicketingSystemRichiestaEntity richiesta, CrmResponse response) {
		try {
			if (response!=null && "200".equals(richiesta.getCodiceEsito())) {
				String logId = allegaIstanzaPdf(response, istanza, params);
				richiesta.setCodiceEsito("201");
				richiesta.setNote(logId);
				ticketingSystemRichiestaDAO.updateResponseById(richiesta);
			}
		} catch (DAOException dao) {
			richiesta.setNote(dao.getMessage());
			ticketingSystemRichiestaDAO.updateResponseById(richiesta);
		}
	}

	
	private TicketingSystemRichiestaEntity getTicketingSystemRichiestaEntity (Istanza istanza) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::getTicketingSystemRichiestaEntity] BEGIN");
		TicketingSystemRichiestaEntity result = null;

		TicketingSystemRichiestaFilter filter = new TicketingSystemRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(TicketingSystemRichiestaEntity.TipoDoc.ISTANZA.getId());
		filter.setIdTicketingSystem(TicketingSystemRichiestaEntity.TicketingSystem.R2U.getId());

		List<TicketingSystemRichiestaEntity> elencoRichieste = ticketingSystemRichiestaDAO.find(filter);
		if (elencoRichieste != null && elencoRichieste.size() > 0) {
			for (TicketingSystemRichiestaEntity richiesta: elencoRichieste) {
				if (richiesta.getCodiceEsito().equals("201") || richiesta.getCodiceEsito().equals("200")) {
					result = richiesta;
				}
			}
		}
		return result;
	}
	
	private TicketingSystemRichiestaEntity getTicketingSystemRichiestaEntity (Istanza istanza, AllegatoLazyEntity allegatoLazy) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::getTicketUUIDPrincipale] BEGIN");
		TicketingSystemRichiestaEntity result = null;

		TicketingSystemRichiestaFilter filter = new TicketingSystemRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setIdAllegato(allegatoLazy.getIdAllegato());
		filter.setTipoDoc(TicketingSystemRichiestaEntity.TipoDoc.ISTANZA_ALLEGATO.getId());
		filter.setIdTicketingSystem(TicketingSystemRichiestaEntity.TicketingSystem.R2U.getId());

		//deve tornare 1 solo record
		List<TicketingSystemRichiestaEntity> elencoRichieste = ticketingSystemRichiestaDAO.find(filter);
		if (elencoRichieste != null && elencoRichieste.size() > 0) {
			for (TicketingSystemRichiestaEntity richiesta: elencoRichieste) {
					result = richiesta;
			}
		}
		return result;
	}
	

	private TicketingSystemRichiestaEntity aggiornaRichiestaConResponse(TicketingSystemRichiestaEntity richiesta, Ticket response) {
		if (response!=null) {
			LOG.debug("[" + CLASS_NAME + "::aggiornaRichiestaConResponse] ticketingSystemDAO.creaTicket response=" + response);
			richiesta = completeTicketingSystemRichiesta(richiesta, TicketingSystemRichiestaEntity.Stato.TX.name(), "200", "OK", response.getTicketId(),null);
//			LOG.debug("[" + CLASS_NAME + "::readResponse] stardasDAO.smistaDocumento response.getMessageUUID :: response.getMessageUUID()=" + response.getMessageUUID());
		} else {
			LOG.warn("[" + CLASS_NAME + "::aggiornaRichiestaConResponse] Ticket null from crmDAO !");
			richiesta = completeTicketingSystemRichiesta(richiesta, "Ticket null");
		}
		return richiesta;
	}
	
	
	/**
	 * Builder dell'entity di tracciamento delle richieste di apertura ticket CRM in ingresso per un istanza
	 * @param istanza2
	 * @param params
	 * @param newTicket
	 * @return
	 */
	private TicketingSystemRichiestaEntity buildTicketingSystemRichiestaEntity(Istanza istanza,
			TicketingSystemParams params, Ticket newTicket) {
		TicketingSystemRichiestaEntity result = new TicketingSystemRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(istanza.getCodiceIstanza());
		result.setUuidRichiesta(UUID.randomUUID().toString());
		result.setStato(TicketingSystemRichiestaEntity.Stato.EL.name());
		result.setTipoDoc(TicketingSystemRichiestaEntity.TipoDoc.ISTANZA.getId());
		result.setIdIstanza(istanza.getIdIstanza());
		result.setIdModulo(istanza.getModulo().getIdModulo());
		result.setIdArea(istanza.getIdArea());
		result.setIdEnte(istanza.getIdEnte());
		result.setIdTicketingSystem(TicketingSystemRichiestaEntity.TicketingSystem.R2U.getId());
		return result;
	}
	
	private TicketingSystemRichiestaEntity buildRichiestaEntityPerAllegato(Istanza istanza, String uuidTicketingSystem, Long idAllegato, String codiceFile) {
		TicketingSystemRichiestaEntity result = new TicketingSystemRichiestaEntity();
		result.setDataRichiesta(new Date());
		result.setCodiceRichiesta(istanza.getCodiceIstanza()+"-"+codiceFile);
		result.setUuidRichiesta(UUID.randomUUID().toString());
		result.setStato(TicketingSystemRichiestaEntity.Stato.EL.name());
		result.setTipoDoc(TicketingSystemRichiestaEntity.TipoDoc.ISTANZA_ALLEGATO.getId());
		result.setIdIstanza(istanza.getIdIstanza());
		result.setIdAllegatoIstanza(idAllegato);
		result.setIdModulo(istanza.getModulo().getIdModulo());
		result.setIdArea(istanza.getIdArea());
		result.setIdEnte(istanza.getIdEnte());
		result.setIdTicketingSystem(TicketingSystemRichiestaEntity.TicketingSystem.R2U.getId());
		result.setUuidTicketingSystem(uuidTicketingSystem);
		return result;
	}

	private Ticket buildTicket(Istanza istanza) {
		Ticket t = new Ticket();
		t.setRichiedente(buildRichiedente());
		t.setCategorizzazione(buildCategorizzazione());
		t.setRiepilogo(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.RIEPILOGO));
		String dettaglio = retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.DETTAGLIO);
		if(richiedenteCreatoAdesso==true) {
			dettaglio = dettaglio.concat("\n").concat(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.MEX_NEW_USER));
		}
		t.setDettaglio(dettaglio);	
		t.setInsertDate(new Date());
		//t.setTicketId(ticketId);
		//t.setImpatto(getImpattoEnum(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.IMPATTO)));
		t.setImpatto(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.IMPATTO));
		t.setTipologia(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.TIPOLOGIA));
		t.setUrgenza(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.URGENZA));
		//
		t.setLavorazione(buildLavorazioneTicket());
		validaTicket(t);
		return t;
	}

	private LavorazioneTicket buildLavorazioneTicket() {
		String assegnatario = retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.ASSEGNATARIO);
		if( !StringUtils.isEmpty(assegnatario)) {
			LavorazioneTicket l = new LavorazioneTicket();
			l.setAssegnatario(assegnatario);
			return l;
		}
		return null;

	}

	private void validaTicket(Ticket t) {
		//check campi obbligatori
		List<String> l = new ArrayList<>();
		if(StringUtils.isEmpty(t.getRiepilogo())) l.add("riepilogo");
		if(t.getImpatto()==null) l.add("impatto");
		if(t.getUrgenza()==null) l.add("urgenza");
		if(t.getTipologia()==null) l.add("tipologia");
		if(StringUtils.isEmpty(t.getRichiedente().getPersonId())) l.add("personId");
		if(StringUtils.isEmpty(t.getCategorizzazione().getCategoriaOperativa().getLivello1())) l.add("livello1");
		if(StringUtils.isEmpty(t.getCategorizzazione().getCategoriaOperativa().getLivello2())) l.add("livello2");
		if(StringUtils.isEmpty(t.getCategorizzazione().getCategoriaOperativa().getLivello3())) l.add("livello3");
		
		if(!l.isEmpty())
			throw new BusinessException(CLASS_NAME+"::validaTicket:"+l.toString()+" campi obbligatori mancanti!");
	}
/*
	private ImpattoEnum getImpattoEnum(String value) {
		for(ImpattoEnum e : ImpattoEnum.values()) {
			if(e.toString().equals(value))
				return e;
		}
		return null;
	}
	private TipologiaEnum getTipologiaEnum(String value) {
		for(TipologiaEnum e : TipologiaEnum.values()) {
			if(e.toString().equals(value))
				return e;
		}
		return null;
	}
	private UrgenzaEnum getUrgenzaEnum(String value) {
		for(UrgenzaEnum e : UrgenzaEnum.values()) {
			if(e.toString().equals(value))
				return e;
		}
		return null;
	}
*/
	private RichiedenteTicket buildRichiedente() {
		RichiedenteTicket rich = new RichiedenteTicket();
		rich.setPersonId(ricercaCreaRichiedentePersonId());
		return rich;
	}

	private String ricercaCreaRichiedentePersonId() {
		richiedenteCreatoAdesso = false;
		RichiedenteDaAnagrafica rich = cercaRichiedentePerEmail();
		if(rich == null) {
			return creaRichiedente();
		}
		LOG.info("[" + CLASS_NAME + "::ricercaCreaRichiedentePersonId] RICHIEDENTE "+rich.getNome()+" "+rich.getCognome()+" "+rich.getEmail()+" TROVATO!");
		return rich.getPersonId();	
	}

	private String creaRichiedente() {
		RichiedentePerAnagrafica u = new RichiedentePerAnagrafica();
		u.setNome(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.NOME));
		u.setCognome(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.COGNOME));
		u.setEmail(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.EMAIL));
		u.setTelefono(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.TELEFONO));
		u.setTelefonoMobile(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.MOBILE));
		u.setCompany(buildCompany());
		RichiedentePerAnagrafica richPerAnag = remedyDAO.createUser(u);
		richiedenteCreatoAdesso = true;
		LOG.info("[" + CLASS_NAME + "::creaRichiedente] RICHIEDENTE "+richPerAnag.getNome()+" "+richPerAnag.getCognome()+" "+richPerAnag.getEmail()+" CREATO!");
		return richPerAnag.getPersonId();
	}

	private RichiedenteDaAnagrafica cercaRichiedentePerEmail() {
		// TODO da migliorare
		String email = retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.EMAIL);
		if(StringUtils.isEmpty(email)) {
			throw new BusinessException(CLASS_NAME+"::cercaRichiedentePerEmail: email non trovata!"); 
		}
		List<RichiedenteDaAnagrafica> richiedenti = remedyDAO.findUsers(email, null, null);
		String companyName = retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.COMPANY_NAME);
		//String companyId = retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.COMPANY_ID);
		for(RichiedenteDaAnagrafica r : richiedenti) {
			//cerco nella company di default
			if(r.getCompany().getCompanyName().equals(companyName) /*&& r.getCompany().getCompanyId().equals(companyId)*/) {
				return r;
			}else {
				//cerco tra la lista delle company possibili
				String cfi = retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.CFI);
				if(!StringUtils.isEmpty(cfi)) {
					List<CfiCompanyEntity> listCompany = cfiCompanyDAO.findByCfi(cfi);
					for(CfiCompanyEntity c : listCompany) {
						if(c.getCompanyName().equals(r.getCompany().getCompanyName())) {
							return r;
						}
					}
				}
			}
		}
		//return  (richiedenti!=null && !richiedenti.isEmpty()) ? richiedenti.get(0) : null;
		return null;
	}

	private EnteUser buildCompany() {
		EnteUser enteU = new EnteUser();
		enteU.setCompanyName(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.COMPANY_NAME));
		//enteU.setCompanyId(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.COMPANY_ID));
		return enteU;
	}

	private CategorizzazioneTicket buildCategorizzazione() {
		CategorizzazioneTicket cat = new CategorizzazioneTicket();
		CategoriaOperativaBaseModel catBaseModel = new CategoriaOperativaBaseModel();
		//catBaseModel.setEnte(ente);
		catBaseModel.setLivello1(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.LIVELLO1));
		catBaseModel.setLivello2(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.LIVELLO2));
		catBaseModel.setLivello3(retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys.LIVELLO3));
		//catBaseModel.setTipologia(tipologia);
		cat.setCategoriaOperativa(catBaseModel);
		return cat;
	}

	
	/**
	 * Completa un entity di tracciamento delle richieste dopo esito positivo di STARDAS
	 * @param richiesta
	 * @param codiceEsito
	 * @param descEsito
	 * @param uuidProtocollatore
	 * @return
	 */
	private TicketingSystemRichiestaEntity completeTicketingSystemRichiesta(TicketingSystemRichiestaEntity richiesta, String stato, String codiceEsito, String descEsito, String uuidTicketingSystem, String note) {
		richiesta.setCodiceEsito(codiceEsito);
		richiesta.setDescEsito(descEsito);
		richiesta.setUuidTicketingSystem(uuidTicketingSystem);
		//richiesta.setStato(TicketingSystemRichiestaEntity.Stato.TX.name());
		richiesta.setNote(note);
		richiesta.setStato(stato);
		return richiesta;
	}
	/**
	 * Completa un entity di tracciamento delle richieste del campo 'note' nel caso di Exception
	 * @param richiesta
	 * @param note
	 * @return
	 */
	private TicketingSystemRichiestaEntity completeTicketingSystemRichiesta(TicketingSystemRichiestaEntity richiesta, String note) {
		richiesta.setNote(note);
		return richiesta;
	}
	
	
	//
	private String retrieveConfTextValueWithReplaceDinamici(ReadyToUseConfKeys keyConf) throws BusinessException {
		if (LOG.isDebugEnabled())
			LOG.debug("[" + CLASS_NAME + "::retrieveConfTextValueWithReplaceDinamici] " + keyConf.name());
		String result = strReplaceHelper.replaceDinamici(retrieveConfTextValue(keyConf), istanza);
		LOG.info("[" + CLASS_NAME + "::retrieveConfTextValueWithReplaceDinamici] keyConf:"+keyConf.name()+" result:" + result);
		return result;
				
	}
	private Integer retrieveConfText2IntValueWithReplaceDinamici(ReadyToUseConfKeys keyConf) {
		String str = null;
		try {
			str = retrieveConfTextValueWithReplaceDinamici(keyConf);
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			LOG.error("[" + CLASS_NAME + "::retrieveConfText2IntValueWithReplaceDinamici] NumberFormatException on " + str + "  for idIstanza="+istanza.getIdIstanza(), e);
			throw new BusinessException();
		}
	}
	
	
	//
	private String retrieveConfTextValue(ReadyToUseConfKeys keyConf) {
		String result = conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asText():keyConf.getTextDefaultValue();
		//LOG.info("[" + CLASS_NAME + "::retrieveConfTextValue] keyConf:"+keyConf.name()+" result: " +result);		
		return result;
	}
	private boolean retrieveConfBooleanValue(ReadyToUseConfKeys keyConf) {
		return conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asBoolean():keyConf.getBooleanDefaultValue();
	}
	private Integer retrieveConfIntValue(ReadyToUseConfKeys keyConf) {
		return conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asInt():keyConf.getIntDefaultValue();
	}
	
}
