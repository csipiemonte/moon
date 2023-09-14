/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.ticketcrm;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

import it.csi.apirest.otrs.v1.dto.Article;
import it.csi.apirest.otrs.v1.dto.Attachment;
import it.csi.apirest.otrs.v1.dto.DynamicField;
import it.csi.apirest.otrs.v1.dto.Ticket;
import it.csi.apirest.otrs.v1.dto.TicketRequest;
import it.csi.apirest.otrs.v1.dto.TicketResponse;
import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.TicketingSystemRichiestaEntity;
import it.csi.moon.commons.entity.TicketingSystemRichiestaFilter;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonsrv.business.service.AllegatiService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.TicketingSystemRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.OtrsApiRestDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class OtrsTicketingSystem extends BaseTicketingSystem implements TicketingSystem{

	private static final String CLASS_NAME = "OtrsTicketingSystem";
	protected static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private Istanza istanza;
	private JsonNode conf;
	private StrReplaceHelper strReplaceHelper;
	
	@Autowired
	OtrsApiRestDAO otrsDAO;
	@Autowired
	TicketingSystemRichiestaDAO ticketingSystemRichiestaDAO;
	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	AllegatiService allegatiService;
	

	public enum OtrsConfKeys {
		TITOLO("titolo", String.class, "@@CODICE_ISTANZA@@", null, null),
		QUEQUE("queue", String.class, null, null, null),
		TYPE("type", String.class, "Unclassified", null, null),
		STATE("state", String.class, "new", null, null),
		PRIORITY("priority", String.class, "3 normal", null, null),
		
		CUSTOMER_ID("customerID", String.class, null, null, null),
		CUSTOMER_USER("customerUser", String.class, null, null, null),
		  
		COMMUNICATION_CHANNEL("communicationChannel", String.class, "Email", null, null),
		SENDER_TYPE("senderType", String.class, "customer", null, null),
		FROM("from", String.class, null, null, null), 
		SUBJECT("subject", String.class, "@@CODICE_ISTANZA@@", null, null),
		BODY("body", String.class, "", null, null),
		CONTENT_TYPE("contentType", String.class, "text/plain; charset=utf8", null, null),
		
		DYNAMIC_FIELDS("campiDinamici", List.class, null, null, null),
		
		INCLUDE_ALLEGATI("includeAllegati", Boolean.class, null, false, null),
		;
		
		private <T> OtrsConfKeys(String key, Class<T> clazz, String textDefaultValue, Boolean booleanDefaultValue, Integer integerDefaultValue) {
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
	
	@Override
	protected String getModuloAttributoKeysName() {
		return ModuloAttributoKeys.PSIT_CRM_CONF_OTRS.name();
	}
		
	@Override
	public String creaTicketIstanza(Istanza istanza, TicketingSystemParams params) throws BusinessException {
		try {
			this.istanza = istanza;
			this.conf = retrieveConf(istanza);
			this.strReplaceHelper = new StrReplaceHelper(istanza);
			
			CrmResponse respTsPrincipale = creaTicketIstanzaPrincipale(istanza, params);
			
			return respTsPrincipale.getTicketUUIDPrincipale();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::creaTicketIstanza] BusinessException "+be.getMessage());
			throw be;
		}
	}

	private CrmResponse creaTicketIstanzaPrincipale(Istanza istanza, TicketingSystemParams params) {
		try {
			LOG.debug("[" + CLASS_NAME + "::creaTicketIstanzaPrincipale] BEGIN");	
			boolean creatoAdesso = false;
			CrmResponse response = null;
			TicketingSystemRichiestaEntity richiesta = buildTicketingSystemRichiestaEntity(istanza);
			TicketingSystemRichiestaEntity richiestaEsistente = getTicketingSystemRichiestaEntity(istanza);
			
			if( richiestaEsistente!=null && "200".equals(richiestaEsistente.getCodiceEsito()) ) {
				response = new CrmResponse(richiestaEsistente.getUuidTicketingSystem(),creatoAdesso);
				return response;
			}
			
			TicketRequest newTicketRequest = buildTicketRequest(istanza);
			TicketResponse ticketResponse = otrsDAO.createTicket(newTicketRequest);
			
			if( richiestaEsistente==null ) {
				richiesta = aggiornaRichiestaConResponse(richiesta, ticketResponse);
				Long idRichiesta = ticketingSystemRichiestaDAO.insert(richiesta);
				richiesta.setIdRichiesta(idRichiesta);
				creatoAdesso = true;
			} else if( richiestaEsistente!=null && !"200".equals(richiestaEsistente.getCodiceEsito()) ) {
				richiesta = aggiornaRichiestaConResponse(richiestaEsistente, ticketResponse);
				ticketingSystemRichiestaDAO.updateResponseById(richiesta);
			}
			response = new CrmResponse(richiesta.getUuidTicketingSystem(),creatoAdesso);
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

	private TicketRequest buildTicketRequest(Istanza istanza) {
		TicketRequest request = new TicketRequest();
		request.setTicket(buildTicket());
		request.setArticle(buildArticle());
		request.setAttachment(buildAttachment(istanza));
		request.setDynamicField(buildDynamicField());
		return request;
	}

	private List<DynamicField> buildDynamicField() {
		List<DynamicField> result = new ArrayList<>();
		JsonNode confCampi = conf.get(OtrsConfKeys.DYNAMIC_FIELDS.getKey());
		
		if(confCampi == null) { 
			return null;
		}
		
		for(JsonNode c : confCampi) {
			DynamicField df = new DynamicField();
			df.setName(c.get("chiave").asText());
			df.setValue(strReplaceHelper.replaceDinamici(c.get("valore").asText(), istanza));
			result.add( df);
		}
		return result;
	}

	private List<Attachment> buildAttachment(Istanza istanza) {
		List<Attachment> listAttach = new ArrayList<>();
		Attachment istanzaPdf = new Attachment();
		byte[] pdf = printIstanzeService.getPdfById(istanza.getIdIstanza());
		istanzaPdf.setContent(new String(Base64.getEncoder().encode(pdf)));
		istanzaPdf.setContentType("application/pdf");
		istanzaPdf.setFilename(istanza.getCodiceIstanza()+".pdf");
		listAttach.add(istanzaPdf);
		
		if(Boolean.TRUE.equals(retrieveConfBooleanValue(OtrsConfKeys.INCLUDE_ALLEGATI))) {
			List<Allegato> listAllegati = allegatiService.findByIdIstanza(istanza.getIdIstanza());
			for(Allegato a : listAllegati) {
				Attachment attach = new Attachment();
				attach.setContent(new String(Base64.getEncoder().encode(a.getContenuto())));
				attach.setContentType(a.getContentType());
				attach.setFilename(a.getFormioNameFile());
				listAttach.add(attach);
			}
		}
		return listAttach;
	}

	private Article buildArticle() {
		String communicationChannel = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.COMMUNICATION_CHANNEL);
		String senderType = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.SENDER_TYPE);
		String from = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.FROM);
		String subject = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.SUBJECT);
		String body = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.BODY);
		String contentType = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.CONTENT_TYPE);	
		Article article = new Article();
		article.setCommunicationChannel(communicationChannel);
		article.setSenderType(senderType);
		article.setFrom(from);
		article.setSubject(subject);
		article.setBody(body);
		article.setContentType(contentType);
		return article;
	}

	private Ticket buildTicket() {
		String title = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.TITOLO);
		String queue = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.QUEQUE);
		String type = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.TYPE);
		String state = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.STATE);
		String priority = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.PRIORITY);
		String customerID = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.CUSTOMER_ID);
		String customerUser = retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys.CUSTOMER_USER);
		Ticket t = new Ticket();
		t.setTitle(title);
		t.setQueue(queue);
		t.setType(type);
		t.setState(state);
		t.setPriority(priority);
		t.setCustomerID(customerID);
		t.setCustomerUser(customerUser);
		return t;
	}

	private TicketingSystemRichiestaEntity aggiornaRichiestaConResponse(TicketingSystemRichiestaEntity richiesta,TicketResponse response) {
		if (response!=null) {
			LOG.debug("[" + CLASS_NAME + "::aggiornaRichiestaConResponse] ticketingSystemDAO.creaTicket response=" + response);
			if(response.getError() != null) {
				richiesta = completeTicketingSystemRichiesta(richiesta, TicketingSystemRichiestaEntity.Stato.KO.name(), "300", response.getError().getErrorCode(), null,response.getError().getErrorMessage());
			}else {
				richiesta = completeTicketingSystemRichiesta(richiesta, TicketingSystemRichiestaEntity.Stato.TX.name(), "200", "OK", response.getTicketNumber(), response.getTicketID());
			}
		} else {
			LOG.warn("[" + CLASS_NAME + "::aggiornaRichiestaConResponse] Ticket null from crmDAO !");
			richiesta = completeTicketingSystemRichiesta(richiesta, "Ticket null");
		}
		return richiesta;
	}
	private TicketingSystemRichiestaEntity completeTicketingSystemRichiesta(TicketingSystemRichiestaEntity richiesta, String stato, String codiceEsito, String descEsito, String uuidTicketingSystem, String note) {
		richiesta.setCodiceEsito(codiceEsito);
		richiesta.setDescEsito(descEsito);
		richiesta.setUuidTicketingSystem(uuidTicketingSystem);
		richiesta.setNote(note);
		richiesta.setStato(stato);
		return richiesta;
	}
	private TicketingSystemRichiestaEntity completeTicketingSystemRichiesta(TicketingSystemRichiestaEntity richiesta, String note) {
		richiesta.setNote(note);
		return richiesta;
	}
	private TicketingSystemRichiestaEntity buildTicketingSystemRichiestaEntity(Istanza istanza) {
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
		result.setIdTicketingSystem(TicketingSystemRichiestaEntity.TicketingSystem.OTRS.getId());
		return result;
	}
	
	private TicketingSystemRichiestaEntity getTicketingSystemRichiestaEntity(Istanza istanza) {
		LOG.debug("[" + CLASS_NAME + "::getTicketingSystemRichiestaEntity] BEGIN");
		TicketingSystemRichiestaEntity result = null;

		TicketingSystemRichiestaFilter filter = new TicketingSystemRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(TicketingSystemRichiestaEntity.TipoDoc.ISTANZA.getId());
		filter.setIdTicketingSystem(TicketingSystemRichiestaEntity.TicketingSystem.OTRS.getId());

		List<TicketingSystemRichiestaEntity> elencoRichieste = ticketingSystemRichiestaDAO.find(filter);
		if (elencoRichieste != null) {
			if( elencoRichieste.size() > 1) {
				String mex ="[" + CLASS_NAME + "::getTicketingSystemRichiestaEntity] idIstanza="+istanza.getIdIstanza()+" elencoRichieste.size()="+elencoRichieste.size();
				throw new BusinessException(mex);
			}
			if( elencoRichieste.size() == 1) {
				result = elencoRichieste.get(0);
			}
		}
		return result;
	}

	private String retrieveConfTextValueWithReplaceDinamici(OtrsConfKeys keyConf) throws BusinessException {
		if (LOG.isDebugEnabled())
			LOG.debug("[" + CLASS_NAME + "::retrieveConfTextValueWithReplaceDinamici] " + keyConf.name());
		String result = strReplaceHelper.replaceDinamici(retrieveConfTextValue(keyConf), istanza);
		LOG.info("[" + CLASS_NAME + "::retrieveConfTextValueWithReplaceDinamici] keyConf:"+keyConf.name()+" result:" + result);
		return result;
				
	}
	
	private String retrieveConfTextValue(OtrsConfKeys keyConf) {
		String result = conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asText():keyConf.getTextDefaultValue();
		//LOG.info("[" + CLASS_NAME + "::retrieveConfTextValue] keyConf:"+keyConf.name()+" result: " +result);		
		return result;
	}
	private boolean retrieveConfBooleanValue(OtrsConfKeys keyConf) {
		return conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asBoolean():keyConf.getBooleanDefaultValue();
	}
}
