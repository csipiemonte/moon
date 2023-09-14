/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.ticketcrm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.csi.apimint.nextcrm.v1.dto.ArticleNew;
import it.csi.apimint.nextcrm.v1.dto.ArticleNewAttachments;
import it.csi.apimint.nextcrm.v1.dto.NewTicket;
import it.csi.apimint.nextcrm.v1.dto.NewTicket.PriorityIdEnum;
import it.csi.apimint.nextcrm.v1.dto.NewTicket.UtenteRiconosciutoEnum;
import it.csi.apimint.nextcrm.v1.dto.Ticket;
import it.csi.apimint.nextcrm.v1.dto.User;
import it.csi.moon.commons.dto.DatiAggiuntivi;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.AllegatoEntity;
import it.csi.moon.commons.entity.TicketingSystemRichiestaEntity;
import it.csi.moon.commons.entity.TicketingSystemRichiestaFilter;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.TicketingSystemRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.NextcrmApimintDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.stardas.cxfclient.MetadatoType;

public class NextcrmTicketingSystem extends BaseTicketingSystem implements TicketingSystem {

	private static final String CLASS_NAME = "NextcrmTicketingSystem";
	protected static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public enum NextCrmConfKeys {
		ADDITIONAL_INFO("additional_info", String.class, null, null, null),
		TITLE("title", String.class, "@@CODICE_ISTANZA@@", null, null),
		GROUP_ID("group_id", String.class, null, null, null), // "7"
		PRIORITY_ID("priority_id", Integer.class, null, null, 2), // 1: critical, 2: high, 3: medium, 4: low
		NOTE("note", String.class, null, null, null),
		TYPE_ID("type_id", Integer.class, null, null, 2), // 1: Richiesta informativa generica  2: Domanda sul servizio applicativo  3: Segnalazione di malfunzionamento
		ASSET_ID("asset_id", String.class, null, null, null), // "1" // Applicativi su quale si richiede assistemza CFI di AnaPROD (terzo valore dell tripletta)
		
		ARTICLE_ISTANZA_SUBJECT("article_istanza_subject", String.class, "Istanza @@CODICE_ISTANZA@@", null, null),
		ARTICLE_ISTANZA_BODY("article_istanza_body", String.class, "@@CODICE_ISTANZA@@ (allegato pdf)", null, null),
		INCLUDE_ALLEGATI("include_allegati", Boolean.class, null, false, null),
//		ARTICLE_ALLEGATO_SUBJECT("article_allegato_subject", String.class, "Allegato di @@CODICE_ISTANZA@@", null, null),

		CODICE_FISCALE("codiceFiscale", String.class, "@@CODICE_FISCALE@@", null, null),
		FIRSTNAME("firstname", String.class, "@@NOME@@", null, null),
		LASTNAME("lastname", String.class, "@@COGNOME@@", null, null),
		EMAIL("email", String.class, "##email##", null, null),
		PHONE("phone", String.class, "##phone##", null, null),
		MOBILE("mobile", String.class, "##mobile##", null, null),
		;

		private <T> NextCrmConfKeys(String key, Class<T> clazz, String textDefaultValue, Boolean booleanDefaultValue, Integer integerDefaultValue) {
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
	
	private Istanza istanza;
	private JsonNode conf;
	private StrReplaceHelper strReplaceHelper;
	private DatiAggiuntivi datiAggiuntivi;
	private static ObjectMapper mapper; // datiAggiuntivi
//	private DatiIstanzaHelper datiIstanzaHelper;   // inglobato in StrReplaceHelper utilizzando ##key##
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
	@Autowired
	@Qualifier("apimint")  // wso001 OR apimint // verificare presenza URL nei file .properties
	NextcrmApimintDAO nextcrmDAO;
	@Autowired
	TicketingSystemRichiestaDAO ticketingSystemRichiestaDAO;
	@Autowired
	AllegatoDAO allegatoDAO;

	
	/**
	 * Crea un Ticket sulla base di un istanza (il suo pdf gia generato passato in params)
	 * {
	 * 	"title": "MOOn - TRIB_DICH_IMU.97371533",
	 * 	"group_id": "1",
	 * 	"priority_id": "1",
	 * 	"note": "",
	 * 	"type_id": 1,                                                1-Richiesta Informativa / 2-    Domanda di Servizio      Segnalazione di malfunzionamento
	 * 	"asset_id": "1",                                             Applicativi su quale si richiede assistemza CFI di AnaPROD (terzo valore dell tripletta)
	 * 	"asset_id": "##asset_id##",                    (FINDOM)      Applicativi su quale si richiede assistemza CFI di AnaPROD (terzo valore dell tripletta)
	 * 	"additional_info": ,
	 * 	"article_istanza_subject": "Subject Article Istanza",
	 * 	"article_allegato": true,
	 * 	"article_allegato_subject": "Subject Article Allegato",
	 * }
	 */
	@Override
	public String creaTicketIstanza(Istanza istanza, TicketingSystemParams params) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::creaTicketIstanza] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::creaTicketIstanza] IN istanza="+istanza);
				LOG.debug("[" + CLASS_NAME + "::creaTicketIstanza] IN params="+params);
			}
			this.istanza = istanza;
			this.conf = retrieveConf(istanza);	// throw new BusinessException("PSIT_CRM_CONF non trovata nei attributi del modulo","MOONSRV-30700");
												// throw new BusinessException("PSIT_CRM_CONF non valid JSON","MOONSRV-30701");
			this.strReplaceHelper = new StrReplaceHelper(istanza);
			
			// creaTiket principale dell'istanza
			CrmResponse respTsPrincipale = creaTicketIstanzaPrincipale(istanza, params);
			
			// creaTiket XML resoconto se necessario

			// creaTiket sugli allegati se necessario
//			creaTiketIstanzaAllegatiIfNecessary(istanza, params, respTsPrincipale);
			
			return respTsPrincipale.getTicketUUIDPrincipale();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::creaTicketIstanza] BusinessException "+be.getMessage());
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaTicketIstanza] ERRORE "+e.getMessage());
			throw new BusinessException("NextcrmTicketingSystemBusinessException","MOONSRV-30700");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::creaTicketIstanza] END");
		}
	}


	private CrmResponse creaTicketIstanzaPrincipale(Istanza istanza, TicketingSystemParams params) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::creaTicketIstanzaPrincipale] BEGIN");
			NewTicket newTicket = buildNewTicketIstanza(istanza, params);
			TicketingSystemRichiestaEntity richiesta = buildTicketingSystemRichiestaEntity(istanza, params, newTicket);
			String ticketUUID = getTicketUUIDPrincipale(istanza);
			if (StringUtils.isEmpty(ticketUUID)) {
				Ticket response = nextcrmDAO.creaTicket(newTicket);
				richiesta = aggiornaRichiestaConResponse(richiesta, response);
				ticketingSystemRichiestaDAO.insert(richiesta);
				boolean creatoAdesso = true;
				return new CrmResponse(richiesta.getUuidTicketingSystem(),creatoAdesso);
			} else {
				boolean creatoAdesso = false;
				return new CrmResponse(ticketUUID,creatoAdesso);
			}
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


	private String getTicketUUIDPrincipale (Istanza istanza) throws DAOException  {
		LOG.debug("[" + CLASS_NAME + "::getTicketUUIDPrincipale] BEGIN");
		String messageUUID = "";

		TicketingSystemRichiestaFilter filter = new TicketingSystemRichiestaFilter();
		filter.setIdIstanza(istanza.getIdIstanza());
		filter.setTipoDoc(TicketingSystemRichiestaEntity.TipoDoc.ISTANZA.getId());
		filter.setIdTicketingSystem(TicketingSystemRichiestaEntity.TicketingSystem.NEXTCRM.getId());

		List<TicketingSystemRichiestaEntity> elencoRichieste = ticketingSystemRichiestaDAO.find(filter);
		if (elencoRichieste != null && elencoRichieste.size() > 0) {
			for (TicketingSystemRichiestaEntity richiesta: elencoRichieste) {
				if (richiesta.getCodiceEsito().equals("201")) {
					messageUUID = richiesta.getUuidTicketingSystem();
				}
			}
		}
		return messageUUID;
	}


	private NewTicket buildNewTicketIstanza(Istanza istanza, TicketingSystemParams params) {
		NewTicket result = new NewTicket();
		result.setAdditionalInfo(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.ADDITIONAL_INFO));
		result.setArticle(buildArticleIstanza(istanza, params));
		result.setAssetId(retrieveConfText2IntValueWithReplaceDinamici(NextCrmConfKeys.ASSET_ID));
		result.setCustomer(buildUserIstanza(istanza, params));
		result.setGroupId(retrieveConfText2IntValueWithReplaceDinamici(NextCrmConfKeys.GROUP_ID));
		result.setNote(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.NOTE));
		result.setNotificationEmail(null);
		result.setPriorityId(getPriorityIdEnum(retrieveConfIntValue(NextCrmConfKeys.PRIORITY_ID)));
		result.setRecallPhone(null);
		result.setTitle(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.TITLE));
		result.setTypeId(retrieveConfIntValue(NextCrmConfKeys.TYPE_ID));
		result.setUtenteRiconosciuto(retrieveUtenteRiconosciuto(istanza));
		if(LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::buildNewTicketIstanza] " + istanza.getCodiceIstanza() + " newTicket=" + result);
		}
		return result;
	}
	private PriorityIdEnum getPriorityIdEnum(Integer priotrity) {
		NewTicket.PriorityIdEnum result = null;
		if (priotrity==null)
			return null;
		if (priotrity<2)
			return NewTicket.PriorityIdEnum.NUMBER_1;
		if (priotrity<3)
			return NewTicket.PriorityIdEnum.NUMBER_2;
		if (priotrity<4)
			return NewTicket.PriorityIdEnum.NUMBER_3;
		return NewTicket.PriorityIdEnum.NUMBER_4;
	}
	/**
	 * Confronta il ShibIdentitaCodiceFiscale con il customerCF èer valorizzare UtenteRiconosciuto a 1 se equals
	 * @param istanza
	 * @param params
	 * @return
	 */
	private UtenteRiconosciutoEnum retrieveUtenteRiconosciuto(Istanza istanza) {
		UtenteRiconosciutoEnum result = UtenteRiconosciutoEnum.NUMBER_0;
//		try {
//			String customerCF = retrieveConfValueWithReplaceDinamici(ConfKeys.CODICE_FISCALE);
//			LOG.debug("[" + CLASS_NAME + "::retrieveUtenteRiconosciuto] customerCF=" + customerCF);
//			if (getDatiAggiuntivi(istanza)==null || getDatiAggiuntivi(istanza).getHeaders()==null) {
//				return result;
//			}
//			String shibIdentitaCodiceFiscale = getDatiAggiuntivi(istanza).getHeaders().getShibIdentitaCodiceFiscale();
//			LOG.debug("[" + CLASS_NAME + "::retrieveUtenteRiconosciuto] shibIdentitaCodiceFiscale=" + shibIdentitaCodiceFiscale);
//			if (!StringUtils.isEmpty(shibIdentitaCodiceFiscale) && shibIdentitaCodiceFiscale.equalsIgnoreCase(customerCF)) {
//				result = UtenteRiconosciutoEnum.NUMBER_1;
//			}
//		} catch (Exception e) {
//			LOG.warn("[" + CLASS_NAME + "::retrieveUtenteRiconosciuto] Exception for idIstanza=" + istanza.getIdIstanza(), e);
//		}
		return result;
	}

	private ArticleNew buildArticleIstanza(Istanza istanza, TicketingSystemParams params) {
		ArticleNew result = new ArticleNew();
		List<ArticleNewAttachments> attachments = new ArrayList<>();
		attachments.add(buildArticleNewAttachmentsIstanza(istanza, params));
		attachments.addAll(buildArticleNewAttachmentsAllegatiIfRequired(istanza, params));
		result.setAttachments(attachments);
		result.setBody(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.ARTICLE_ISTANZA_BODY));
		result.setCc(null);
		result.setContentType("text/html");
		result.setCreatedAt(new Date());
//		result.setFrom(extractTextValueFromIstanzaByKey(ConfKeys.CUSTOMER_EMAIL_ISTANZA_KEY)); // TOVERIFY Need or 422 Need at least article: { from: &quot;&lt;string&gt;&quot;}
		result.setFrom(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.EMAIL)); // TOVERIFY Need or 422 Need at least article: { from: &quot;&lt;string&gt;&quot;}
//		Must be a valid email:  article: { from:
		result.setId(null);
		result.setInReplyTo(null);
		result.setOriginById(null);
		result.setReplyTo(null);
		result.setSender(null);
		result.setSubject(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.ARTICLE_ISTANZA_SUBJECT));
		result.setTicketId(null);
		result.setTo(null);
		result.setType(null);
		result.setTypeId(ArticleNew.TypeIdEnum.NUMBER_13);  // FISSO 13 - web (via api)
		result.setUpdatedAt(null);
		return result;
	}


	private ArticleNewAttachments buildArticleNewAttachmentsIstanza(Istanza istanza, TicketingSystemParams params) {
		ArticleNewAttachments istanzaAttach = new ArticleNewAttachments();
		istanzaAttach.setId(null);
		istanzaAttach.setFilename(istanza.getCodiceIstanza() + ".pdf");
		istanzaAttach.setMimeType("application/pdf");
		istanzaAttach.setData(new String(Base64.getEncoder().encode(params.getIstanzaPdf().getContenutoPdf())));
		return istanzaAttach;
	}
	private List<ArticleNewAttachments> buildArticleNewAttachmentsAllegatiIfRequired(Istanza istanza, TicketingSystemParams params) {
		List<ArticleNewAttachments> result = new ArrayList<>();
		if (retrieveConfBooleanValue(NextCrmConfKeys.INCLUDE_ALLEGATI)) {
			for (AllegatoEntity a : getAllegati(istanza, params)) {
				ArticleNewAttachments attach = new ArticleNewAttachments();
				attach.setId(null);
				attach.setFilename(a.getNomeFile());
				attach.setMimeType(a.getContentType());
				attach.setData(new String(Base64.getEncoder().encode(a.getContenuto())));
				result.add(attach);
			}
		}
		LOG.debug("[" + CLASS_NAME + "::buildArticleNewAttachmentsAllegatiIfRequired] result.size() = " + result.size());
		return result;
	}
	private List<AllegatoEntity> getAllegati(Istanza istanza, TicketingSystemParams params) throws DAOException {
		LOG.debug("[" + CLASS_NAME + "::getAllegati] BEGIN");
		return allegatoDAO.findByIdIstanza(istanza.getIdIstanza());
	}
	private User buildUserIstanza(Istanza istanza, TicketingSystemParams params) {
		User result = new User();
		result.setActive(Boolean.TRUE);
//		result.setCodiceFiscale(istanza.getCodiceFiscaleDichiarante());
//		result.setFirstname(istanza.getNomeDichiarante()==null?"":istanza.getNomeDichiarante().toUpperCase());
//		result.setLastname(istanza.getCognomeDichiarante()==null?"":istanza.getCognomeDichiarante().toUpperCase());
		result.setCodiceFiscale(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.CODICE_FISCALE));
		result.setFirstname(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.FIRSTNAME));
		result.setLastname(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.LASTNAME));
		result.setEmail(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.EMAIL));
		result.setPhone(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.PHONE));
		result.setMobile(retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys.MOBILE));
		result.setNote(null);
		return result;
	}
	
//	private String retrieveShibEmail(Istanza istanza) {
//		String result;
//		if (getDatiAggiuntivi(istanza)==null || getDatiAggiuntivi(istanza).getHeaders()==null) {
//			return null;		
//		}
//		result = getDatiAggiuntivi(istanza).getHeaders().getShibMail();
//		return result;
//	}
//	private String retrieveShibMobile(Istanza istanza) {
//		String result;
//		if (getDatiAggiuntivi(istanza)==null || getDatiAggiuntivi(istanza).getHeaders()==null) {
//			return null;		
//		}
//		result = getDatiAggiuntivi(istanza).getHeaders().getShibMobilePhone();
//		return result;
//	}


	protected String getNomeCognomeDichiarante(Istanza istanza) {
		String result = (istanza.getNomeDichiarante()==null?"":istanza.getNomeDichiarante().toUpperCase()) + " " + 
	                    (istanza.getCognomeDichiarante()==null?"":istanza.getCognomeDichiarante().toUpperCase());
		return result.trim();
	}


	protected MetadatoType makeNewMetadato(String nome, String valore) {
		MetadatoType result = new MetadatoType();
		result.setNome(nome);
		result.setValore(valore);
		return result;
	}
	

	/**
	 * Builder dell'entity di tracciamento delle richieste di apertura ticket CRM in ingresso per un istanza
	 * @param istanza
	 * @param params
	 * @param newTicket
	 * @return
	 */
	private TicketingSystemRichiestaEntity buildTicketingSystemRichiestaEntity(Istanza istanza, TicketingSystemParams params, NewTicket newTicket) {
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
		result.setIdTicketingSystem(TicketingSystemRichiestaEntity.TicketingSystem.NEXTCRM.getId());
		return result;
	}
	


	private TicketingSystemRichiestaEntity aggiornaRichiestaConResponse(TicketingSystemRichiestaEntity richiesta, Ticket response) {
		if (response!=null) {
			LOG.debug("[" + CLASS_NAME + "::aggiornaRichiestaConResponse] ticketingSystemDAO.creaTicket response=" + response);
			richiesta = completeTicketingSystemRichiesta(richiesta, "201", "OK", String.valueOf(response.getId()));
//			LOG.debug("[" + CLASS_NAME + "::readResponse] stardasDAO.smistaDocumento response.getMessageUUID :: response.getMessageUUID()=" + response.getMessageUUID());
		} else {
			LOG.warn("[" + CLASS_NAME + "::aggiornaRichiestaConResponse] Ticket null from nextcrmDAO !");
			richiesta = completeTicketingSystemRichiesta(richiesta, "Ticket null");
		}
		return richiesta;
	}
	
	/**
	 * Completa un entity di tracciamento delle richieste dopo esito positivo di STARDAS
	 * @param richiesta
	 * @param codiceEsito
	 * @param descEsito
	 * @param uuidProtocollatore
	 * @return
	 */
	private TicketingSystemRichiestaEntity completeTicketingSystemRichiesta(TicketingSystemRichiestaEntity richiesta, String codiceEsito, String descEsito, String uuidTicketingSystem) {
		richiesta.setCodiceEsito(codiceEsito);
		richiesta.setDescEsito(descEsito);
		richiesta.setUuidTicketingSystem(uuidTicketingSystem);
		richiesta.setStato(TicketingSystemRichiestaEntity.Stato.TX.name());
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

	


	public DatiAggiuntivi getDatiAggiuntivi(Istanza istanza) {
		if (datiAggiuntivi==null) {
			try {
				datiAggiuntivi = getMapper().readValue(istanza.getDatiAggiuntivi(), DatiAggiuntivi.class);
			} catch (Exception e) {
				LOG.warn("[" + CLASS_NAME + "::getDatiAggiuntivi] idIstanza="+istanza.getIdIstanza()+"  datiAggiuntivi"+istanza.getDatiAggiuntivi(), e);
			}
		}
		return datiAggiuntivi;
	}
	public static ObjectMapper getMapper() {
		if(mapper == null) {
			mapper = new ObjectMapper()
					.setSerializationInclusion(Include.NON_EMPTY)
					.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//			SerializationConfig config = mapper
//				.getSerializationConfig()
//				.withSerializationInclusion(Inclusion.NON_EMPTY)
//				.without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
//			mapper.setSerializationConfig(config);
		}
		return mapper;
	}
	
	
//	public DatiIstanzaHelper getDatiIstanzaHelper() {
//		if (datiIstanzaHelper==null) {
//			datiIstanzaHelper = new DatiIstanzaHelper();
//			datiIstanzaHelper.initDataNode(istanza);
//		}
//		return datiIstanzaHelper;
//	}
//	
//	private static String getFileExtension(String fileName) {
//		String extension = "";
//        if (fileName != null) {
//	        int index = fileName.lastIndexOf('.');
//	        if (index > 0) {
//	            extension = fileName.substring(index + 1);
//	        }
//        }
//        return extension;
//
//    }
//	private static String getFileName(String fileName) {
//		String name = "";
//        if (fileName != null) {
//	        int index = fileName.lastIndexOf('.');
//	        if (index > 37) {
//	        	name = fileName.substring(0,index-37);
//	        }
//        }
//        return name;
//
//    }
	
	
	//
	private String retrieveConfTextValueWithReplaceDinamici(NextCrmConfKeys keyConf) throws BusinessException {
		if (LOG.isDebugEnabled())
			LOG.debug("[" + CLASS_NAME + "::retrieveConfTextValueWithReplaceDinamici] " + keyConf.name());
		return strReplaceHelper.replaceDinamici(retrieveConfTextValue(keyConf), istanza);
	}
	private Integer retrieveConfText2IntValueWithReplaceDinamici(NextCrmConfKeys keyConf) {
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
	private String retrieveConfTextValue(NextCrmConfKeys keyConf) {
		return conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asText():keyConf.getTextDefaultValue();
	}
	private boolean retrieveConfBooleanValue(NextCrmConfKeys keyConf) {
		return conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asBoolean():keyConf.getBooleanDefaultValue();
	}
	private Integer retrieveConfIntValue(NextCrmConfKeys keyConf) {
		return conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asInt():keyConf.getIntDefaultValue();
	}
	
	//
//	private String extractTextValueFromIstanzaByKey(ConfKeys keyConf) {
//		return getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(conf.get(keyConf.getKey()).asText());
//	}
	
}
