/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.notificatore.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.csi.apirest.notify.mb.v1.dto.Message;
import it.csi.apirest.notify.mb.v1.dto.Notification;
import it.csi.apirest.notify.mb.v1.dto.NotificationEmail;
import it.csi.apirest.notify.mb.v1.dto.NotificationIo;
import it.csi.apirest.notify.mb.v1.dto.NotificationIoContent;
import it.csi.apirest.notify.mb.v1.dto.NotificationIoDefaultAddresses;
import it.csi.apirest.notify.mb.v1.dto.NotificationMemo;
import it.csi.apirest.notify.mb.v1.dto.NotificationMex;
import it.csi.apirest.notify.mb.v1.dto.NotificationPush;
import it.csi.apirest.notify.mb.v1.dto.NotificationSms;
import it.csi.apirest.notify.preferences.v1.dto.ContactPreference;
import it.csi.apirest.notify.preferences.v1.dto.UserPreferencesService;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.NotifyLogEntity;
import it.csi.moon.commons.entity.NotifyParametroEntity;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.dto.IstanzaInitCompletedParams;
import it.csi.moon.moonsrv.business.service.dto.NotificatoreDatiInit;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotificatoreMbDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotificatorePrefDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotificatoreStatusDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotifyLogDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotifyParametroDAO;
import it.csi.moon.moonsrv.business.service.notificatore.NotificatoreService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business del Notificatore
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 15/05/2020 - Versione initiale
 */
@Component
public class NotificatoreServiceImpl implements NotificatoreService {
	
	private static final String CLASS_NAME = "NotificatoreServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
   
	private Map<String,Boolean> mapChannelUsed = new HashMap<>();
	
	@Autowired
	NotificatoreMbDAO notificatoreDAO;
	@Autowired
	NotificatorePrefDAO notificatorePrefDAO;
	@Autowired
	NotificatoreStatusDAO notificatoreStatusDAO;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	NotifyLogDAO notifyLogDAO;
	@Autowired
	IstanzeService istanzaService;
	@Autowired
	NotifyParametroDAO notifyParametroDAO;
	@Autowired
	AreaModuloDAO areaModuloDAO;
	
/*	
	@Override
	public String inviaMessaggio(Message message) throws BusinessException {
		try {
			//message = completaMessagePayloadWithUUid(message);
			return notificatoreDAO.invia(null,null,message);
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::inviaMessaggio] DAOException " + daoe.getMessage());
	    	throw new BusinessException("Messaggio non inviato");
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::inviaMessaggio] Exception " + e.getMessage());
	    	throw new BusinessException("Errore inviaMessaggio generica");
		}
	}
	@Override
	public ContactPreference contacts(String identitaDigitale, String codiceFiscale) throws BusinessException {
		try {
			return notificatorePrefDAO.contatti(null,null,identitaDigitale, codiceFiscale);			
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::contatti] DAOException " + daoe.getMessage());
	    	throw new BusinessException("Errore contatti");
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::contatti] Exception " + e.getMessage());
	    	throw new BusinessException("Errore contatti generica");
		}
	}
	@Override
	public List<Service> services(String identitaDigitale) throws BusinessException {
		try {
			return notificatorePrefDAO.services(null,null,identitaDigitale);			
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::services] DAOException " + daoe.getMessage());
	    	throw new BusinessException("Errore services");
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::services] Exception " + e.getMessage());
	    	throw new BusinessException("Errore services generica");
		}
	}
	@Override
	public Status status(String idMessaggio) throws BusinessException {
		try {
			return notificatoreStatusDAO.status(null,null,null,idMessaggio);
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::status] DAOException " + daoe.getMessage());
			throw new BusinessException("Errore status");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::status] Exception " + e.getMessage());
			throw new BusinessException("Errore status generica");
		}
	}
	@Override
	public List<UserPreferencesService> servicesByUser(String cfTracciamento, String codiceFiscale) throws BusinessException {
		try {
			return notificatorePrefDAO.servicesByUser(null,null,cfTracciamento, codiceFiscale);		
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::servicesByUser] DAOException " + daoe.getMessage());
	    	throw new BusinessException("Errore services");
		}
	}
	@Override
	public UserPreferencesService serviceByUserServiceName(String cfTracciamento, String codiceFiscale, String serviveName) throws ItemNotFoundBusinessException, BusinessException {
		try {
			return notificatorePrefDAO.serviceByUserServiceName(null,null,cfTracciamento, codiceFiscale, serviveName);
		} catch (ItemNotFoundDAOException daonfe) {
	    	LOG.warn("[" + CLASS_NAME + "::serviceByUserServiceName] DAOException " + daonfe.getMessage());
	    	throw new ItemNotFoundBusinessException(daonfe);
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::serviceByUserServiceName] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		}
	}
*/
	
	@Override
	public Optional<NotificatoreDatiInit> getDatiInit(IstanzaInitCompletedParams completedParams) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getDatiInit]");
		NotificatoreDatiInit result = null;
		try {
			String codiceFiscale = completedParams.getIstanzaInitParams().getCodiceFiscale();
			NotificatoreConfHelper conf = null;
			try {
				conf = retrieveConfForDatiInit(completedParams);
			} catch (BusinessException be) {
				LOG.warn("[" + CLASS_NAME + "::getDatiInit] "+be.getMessage());
				return Optional.empty();
			}
			
			String initAzione = conf.readKey("initAzione");
			if(initAzione == null) return Optional.empty(); 
			
			String initAzioneRequired = conf.readKey("initAzioneRequired");
			if(initAzioneRequired != null) {
				result = retrieveRequiredDatiInit(initAzioneRequired,codiceFiscale,conf);
			} else
			{
				result = retrieveContatti(codiceFiscale,conf);
			}	
			/*
			if (!conf.isINITRequiredWhen()) {
    			return Optional.empty();
    		}
			String requiredLevel = conf.readRequiredLevel();
			if (NotificatoreConfHelper.REQUIRED_LEVEL_PORTALE.equalsIgnoreCase(requiredLevel)) {
				result = retrieveDatiInitPortale(codiceFiscale, conf);
			}
			if (NotificatoreConfHelper.REQUIRED_LEVEL_SERVIZIO.equalsIgnoreCase(requiredLevel)) {
				result = retrieveDatiInitServizio(codiceFiscale, conf);
			}
			*/
    		return Optional.ofNullable(result);
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::isServiceSubscribed] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::retreiveNotifyEmailRequired] Errore ", e);
	    	throw new BusinessException("ERROR Generica getDatiInit","MOONSRV-30410");
	    }
	}

	private NotificatoreDatiInit retrieveRequiredDatiInit(String initAzioneRequired, String codiceFiscale, NotificatoreConfHelper conf) {
		NotificatoreDatiInit result = null;
		switch (initAzioneRequired) {
		case "PORTALE":
			result = retrieveDatiInitPortale(codiceFiscale,conf);
			break;
		case "SERVIZIO":
			result = retrieveDatiInitServizio(codiceFiscale, conf);
			break;
		default:
			break;
		}
		return result;
	}
	
	private NotificatoreDatiInit retrieveContatti(String codiceFiscale, NotificatoreConfHelper confHelper) {
		NotificatoreDatiInit result = null;
		String token = confHelper.getTokenMoon();
		String endpoint = confHelper.getEndpoint();
		try {
			ContactPreference prefs = notificatorePrefDAO.contatti(endpoint,token,codiceFiscale, codiceFiscale);
			result = buildNotificatoreDatiInit(prefs);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	// PORTALE
	private NotificatoreDatiInit retrieveDatiInitPortale(String codiceFiscale, NotificatoreConfHelper confHelper) {
		NotificatoreDatiInit result = null;
		String token = confHelper.getTokenMoon();
		String endpoint = confHelper.getEndpoint();
		try {
			ContactPreference prefs = notificatorePrefDAO.contatti(endpoint,token,codiceFiscale, codiceFiscale);
			result = buildNotificatoreDatiInit(prefs);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveDatiInitPortale] DAOException contatti for " + codiceFiscale);						
			String defaultMex = String.format("Contatti utente %s non trovate ", codiceFiscale);
			throw buildBusinessException(defaultMex, "MOONSRV-30417",confHelper);
		}
		return result;
	}

	// SERVIZIO
	private NotificatoreDatiInit retrieveDatiInitServizio(String codiceFiscale, NotificatoreConfHelper confHelper) {
		String serviceName = confHelper.readRequiredKey("serviceName");
		try {
			NotificatoreDatiInit result = null;
			String token = confHelper.getTokenMoon();
			String endpoint = confHelper.getEndpoint();
		
			UserPreferencesService serviceSubscribed = notificatorePrefDAO.serviceByUserServiceName(endpoint,token,codiceFiscale, codiceFiscale, serviceName);
			confHelper.validaPrefRequired(serviceSubscribed);
			try {
				ContactPreference prefs = notificatorePrefDAO.contatti(endpoint,token,codiceFiscale, codiceFiscale);
				result = buildNotificatoreDatiInit(prefs);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::retrieveDatiInitServizio] DAOException contatti for " + codiceFiscale);						
				String defaultMex = String.format("Contatti utente %s non trovate ", codiceFiscale);
				throw buildBusinessException(defaultMex, "MOONSRV-30417",confHelper);
			}
			return result;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::retrieveDatiInitServizio] DAOException contatti for " + codiceFiscale);						
			String defaultMex = String.format("Preferences contatto utente %s non trovate sul serviceName %s", codiceFiscale, serviceName);
			throw buildBusinessException(defaultMex, "MOONSRV-30415",confHelper);
		} catch (ItemNotFoundBusinessException nfe) {
			LOG.error("[" + CLASS_NAME + "::retrieveDatiInitServizio] ItemNotFoundBusinessException pref of " + serviceName + " for " + codiceFiscale);						
			String defaultMex = String.format("Servizio %s non sottoscritto per l'utente %s", serviceName, codiceFiscale);
			throw buildBusinessException(defaultMex, "MOONSRV-30414",confHelper);
		} 
	}
	
	private BusinessException buildBusinessException(String defaultMex,String code,NotificatoreConfHelper confHelper)  {
		String mex = confHelper.getMexErroreByCode(code);
		if(mex ==null || mex.isBlank()) {
			mex = defaultMex;
		}
		BusinessException ex = new BusinessException(mex, code);
		return ex;
	}

	private NotificatoreDatiInit buildNotificatoreDatiInit(ContactPreference prefs) {
		NotificatoreDatiInit result = new NotificatoreDatiInit();
		result.setEmail(prefs.getEmail());
		result.setSms(prefs.getSms());
		return result;
	}

	@Override
	public void inviaRichiestaNotify(Long idIstanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::inviaRichiestaNotify]");
			Istanza istanza = istanzaService.getIstanzaById(idIstanza);
						
			//recupero NOTIFY CONF e i parametri da db
			NotificatoreConfHelper confHelper = retrieveConfByIdModulo(istanza);

			if( !confHelper.isSendEnable() ) {
				LOG.info("[" + CLASS_NAME + "::inviaRichiestaNotify] SEND NOT ENABLE");
				return;
			}
			
			String cfTracciamento = getCFTracciamento(istanza);
			String serviceName = confHelper.readRequiredKey("serviceName");
			String endpoint = confHelper.getEndpoint();
			//String token = confHelper.readRequiredKey("serviceToken");
			String token = confHelper.getTokenMoon();
			UserPreferencesService s = notificatorePrefDAO.serviceByUserServiceName(endpoint,token,cfTracciamento, istanza.getCodiceFiscaleDichiarante(), serviceName);
			List<String> channels = List.of(StringUtils.split(s.getChannels(),","));
		
			Message message = buildMessage(channels,istanza.getCodiceFiscaleDichiarante(),confHelper);
			inviaMessaggio(confHelper,message);
		
		    //salvo richiesta notify del post mex a db
		    NotifyLogEntity logEntity = new NotifyLogEntity(istanza.getIdEnte(), istanza.getModulo().getIdModulo(), idIstanza,
		    		message.getUuid().toString(), message.getPayload().getId(),"201",
		    		this.mapChannelUsed.get("email"),this.mapChannelUsed.get("sms"),
		    		this.mapChannelUsed.get("push"),this.mapChannelUsed.get("mex"),
		    		this.mapChannelUsed.get("io"));
		    notifyLogDAO.insert(logEntity);
		    	
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::call] ERROR ");
			throw be;				
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}



	private String inviaMessaggio(NotificatoreConfHelper confHelper, Message message) throws BusinessException {
		try {
			String url = confHelper.getEndpoint();
			String serviceToken= confHelper.readRequiredKey("serviceToken");	
			return notificatoreDAO.invia(url,serviceToken,message);
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::inviaMessaggio] DAOException " + daoe.getMessage());
	    	throw new BusinessException("Messaggio non inviato");
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::inviaMessaggio] Exception " + e.getMessage());
	    	throw new BusinessException("Errore inviaMessaggio generica");
		}
	}
	
	private NotificatoreConfHelper retrieveConfByIdModulo(Istanza istanza) {
		List<ModuloAttributoEntity> listAttributi = moduloAttributiDAO.findByIdModulo(istanza.getModulo().getIdModulo());
		MapModuloAttributi attributi = new MapModuloAttributi(listAttributi);
		Boolean activated = attributi.getWithCorrectType(ModuloAttributoKeys.PSIT_NOTIFY);
		if (!Boolean.TRUE.equals(activated)) {
			LOG.info("[" + CLASS_NAME + "::inviaRichiestaNotify] Servizio NOTIFY NON ATTIVO");
			throw new BusinessException("Servizio NOTIFY NON ATTIVO");
		}
		String confStr = attributi.getWithCorrectType(ModuloAttributoKeys.PSIT_NOTIFY_CONF);
		
		Long idArea = istanza.getIdArea() !=null ? istanza.getIdArea() 
				: areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte()).getIdArea();
		List<NotifyParametroEntity> listParametri = notifyParametroDAO.findForModulo(istanza.getIdEnte(),idArea, istanza.getModulo().getIdModulo());
		
		NotificatoreConfHelper confHelper = new NotificatoreConfHelper(confStr,listParametri);
		return confHelper;
	}
	public NotificatoreConfHelper retrieveConfForDatiInit(IstanzaInitCompletedParams completedParams) {
		MapModuloAttributi attributi = completedParams.getModuloAttributi();
		Boolean activated = attributi.getWithCorrectType(ModuloAttributoKeys.PSIT_NOTIFY);
		if (!Boolean.TRUE.equals(activated)) {
			LOG.info("[" + CLASS_NAME + "::inviaRichiestaNotify] Servizio NOTIFY NON ATTIVO");
			throw new BusinessException("Servizio NOTIFY NON ATTIVO");
		}
		String confStr = attributi.getWithCorrectType(ModuloAttributoKeys.PSIT_NOTIFY_CONF);
		
		Long idEnte = completedParams.getIstanzaInitParams().getUserInfo().getEnte().getIdEnte();
		Long idModulo = completedParams.getIstanzaInitParams().getIdModulo();
		Long idArea = areaModuloDAO.findByIdModuloEnte(idModulo, idEnte).getIdArea();
		List<NotifyParametroEntity> listParametri = notifyParametroDAO.findForModulo(idEnte,idArea, idModulo);
		
		NotificatoreConfHelper confHelper = new NotificatoreConfHelper(confStr,listParametri);
		return confHelper;
	}
	
	private Message buildMessage(List<String> channels, String codiceFiscaleDichiarante,
			NotificatoreConfHelper confHelper) {
		Notification payload = buildPayload(channels,codiceFiscaleDichiarante,confHelper);		
		Message message = new Message();
		message.setUuid(UUID.randomUUID());
		message.setPayload(payload);		
		//message.setExpireAt("2025-04-23T20:00:00");
		return message;
	}
	private Notification buildPayload( List<String> channels, String userId, NotificatoreConfHelper confHelper) {
		Notification payload = new Notification();
		payload.setId(UUID.randomUUID().toString());
		payload.setUserId(userId);
		inizializeMap();
		
		for (String channel : channels) {
			LOG.debug("[" + CLASS_NAME + "::call] channel= "+channel);
			JsonNode confChannel = confHelper.readJsonNode(channel);
			if (confChannel != null) {
				switch (channel) {
				case "push":
					if(confChannel.get("send").asBoolean()) {
						NotificationPush notPush = new NotificationPush();
						notPush.setTitle(confChannel.get("title") != null ? confChannel.get("title").asText() : "");
						notPush.setBody(confChannel.get("body") != null ? confChannel.get("body").asText() : "");
						notPush.setCallToAction(confChannel.get("call_to_action") != null ? confChannel.get("call_to_action").asText() : "");
						notPush.setToken("");
						payload.setPush(notPush);
						this.mapChannelUsed.put("push", Boolean.TRUE);	
					}
					break;
				case "email":
					if(confChannel.get("send").asBoolean()) {
						NotificationEmail notEmail = new NotificationEmail();
						notEmail.setSubject(confChannel.get("subject") != null ? confChannel.get("subject").asText() : "");
						notEmail.setBody(confChannel.get("body") != null ? confChannel.get("body").asText() : "");
						notEmail.setTemplateId(confChannel.get("template_id") != null ? confChannel.get("template_id").asText() : "");
						//notEmail.setTo(confChannel.get("to") != null ? confChannel.get("to").asText() : "");
						payload.setEmail(notEmail);
						this.mapChannelUsed.put("email", Boolean.TRUE);	
					}				
					break;
				case "sms":
					if(confChannel.get("send").asBoolean()) {
						NotificationSms notSms = new NotificationSms();
						notSms.setContent(confChannel.get("content") != null ? confChannel.get("content").asText() : "");
						notSms.setPhone(confChannel.get("phone") != null ? confChannel.get("phone").asText() : "");
						payload.setSms(notSms);
						this.mapChannelUsed.put("sms", Boolean.TRUE);	
					}					
					break;
				case "mex":
					if(confChannel.get("send").asBoolean()) {
						NotificationMex notMex = new NotificationMex();
						notMex.setTitle(confChannel.get("title") != null ? confChannel.get("title").asText() : "");
						notMex.setBody(confChannel.get("body") != null ? confChannel.get("body").asText() : "");
						notMex.setCallToAction(confChannel.get("call_to_action") != null ? confChannel.get("call_to_action").asText() : "");
						payload.setMex(notMex);
						this.mapChannelUsed.put("mex", Boolean.TRUE);
					}
					break;
				case "io":
					if(confChannel.get("send").asBoolean()) {
						NotificationIo notIo = new NotificationIo();
						NotificationIoContent notContent = new NotificationIoContent();
						NotificationIoDefaultAddresses notDefAddress = new NotificationIoDefaultAddresses();
						notContent.setSubject(confChannel.has("content") ? confChannel.get("content").get("subject").asText()	: "");
						notContent.setDueDate(confChannel.has("content") ? confChannel.get("content").get("due_date").asText() : "");
						notContent.setMarkdown(confChannel.has("content") ? confChannel.get("content").get("markdown").asText() : "");
//	            		  NotificationIoContentPaymentData payment = new NotificationIoContentPaymentData();            		  
//	            		  payment.setAmount(amount);
//	            		  payment.setNoticeNumber(noticeNumber);            		  
//	            		  notContent.setPaymentData(payment);            		              		
//	            		  notDefAddress.setEmail("");            		 
						notIo.setContent(notContent);
						notIo.setDefaultAddresses(notDefAddress);
						notIo.setTimeToLive(new BigDecimal(confChannel.get("time_to_live") != null ? confChannel.get("time_to_live").asText()	: "0"));
						payload.setIo(notIo);
						this.mapChannelUsed.put("io", Boolean.TRUE);
					}
					break;
				case "memo":
					NotificationMemo notMemo = new NotificationMemo();
					notMemo.setStart(confChannel.get("start") != null ? confChannel.get("start").asText() : "");
					notMemo.setEnd(confChannel.get("end") != null ? confChannel.get("end").asText() : "");
					notMemo.setSummary(confChannel.get("summary") != null ? confChannel.get("summary").asText() : "");
					notMemo.setDescription(confChannel.get("description") != null ? confChannel.get("description").asText() : "");
					notMemo.setLocation(confChannel.get("location") != null ? confChannel.get("location").asText() : "");
					notMemo.setOrganizer(confChannel.get("organizer") != null ? confChannel.get("organizer").asText()	: "");
					boolean allDay = confChannel.get("all_day") != null	? confChannel.get("all_day").asBoolean() : false;
					notMemo.setAllDay(allDay ? Boolean.TRUE : Boolean.FALSE);
					payload.setMemo(notMemo);
					break;
				default:
					LOG.debug("[" + CLASS_NAME + "::call] no channel");
				}
			}
		}
		return payload;
	}
	
	private String getCFTracciamento(Istanza istanza) {
		String cf = istanza.getAttoreIns();
		if( cf.length() == 16 ) return cf;
		if( cf.length() > 16 ) return cf.substring(0, 16);
		if( cf.length() < 16 ) return istanza.getCodiceFiscaleDichiarante();
		return cf;
	}
	private void inizializeMap() {
		this.mapChannelUsed.put("email",Boolean.FALSE);
		this.mapChannelUsed.put("sms",Boolean.FALSE);
		this.mapChannelUsed.put("push",Boolean.FALSE);
		this.mapChannelUsed.put("mex",Boolean.FALSE);
		this.mapChannelUsed.put("io",Boolean.FALSE);
	}
}
