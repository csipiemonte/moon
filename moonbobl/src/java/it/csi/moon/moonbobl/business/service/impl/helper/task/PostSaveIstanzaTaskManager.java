/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.task;

import java.time.Duration;
import java.time.Instant;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaSaveResponse;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.MapModuloAttributi;
import it.csi.moon.moonbobl.util.ModuloAttributoKeys;


public class PostSaveIstanzaTaskManager implements Runnable {
	
	private final static String CLASS_NAME = "PostSaveIstanzaTaskManager";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	private UserInfo user;
	private IstanzaSaveResponse istanzaSaveResponse;
	private ModuloEntity moduloE;
	private MapModuloAttributi mapModuloAttributi;
	private String ipAddress;
	
    public PostSaveIstanzaTaskManager(UserInfo user, IstanzaSaveResponse saveResponse, ModuloEntity moduloE, MapModuloAttributi mapModuloAttributi, String ipAddress) {
		super();
		this.user = user;
		this.istanzaSaveResponse = saveResponse;
		this.moduloE = moduloE;
		this.mapModuloAttributi = mapModuloAttributi;
		this.ipAddress = ipAddress;
		log.info("[" + CLASS_NAME + "::PostSaveIstanzaTaskManager] CONSTRUCTOR for id_istanza: "+saveResponse.getIstanza().getIdIstanza()+"\n"+saveResponse.getIstanza()+"\n"+mapModuloAttributi);
	}

	public void run() {
        Instant start = java.time.Instant.now();
        log.info("[" + CLASS_NAME + "::run] BEGIN Task...");
        try {
        	//runEstraiDichiaranteIfRequested();
        	runSendEmailDichiaranteIfRequested();
        	//runNotificaIstanzaIfRequested();
        	runProtocollaIstanzaIfRequested();
        	runWfAzioneIstanzaIfRequested();
        	runWFCosmoIstanzaIfRequested();
        	runTicketCrmIfRequested();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::run] Exception "+e.getMessage());
			throw new BusinessException("istanzaSaveResponseNull");
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        log.info("[" + CLASS_NAME + "::run] END " + String.format("Completed Task in %02d:%02d.%04d \n", between.toMinutes(), between.getSeconds(), between.toMillis())); 
		}
    }

	public void runEstraiDichiaranteIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        log.info("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] BEGIN Task...");
        try {
        	// Estrai Dichiarante
        	Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE);
    		if (Boolean.TRUE.equals(activated)) {
				String conf = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE_CONF);
				if (StringUtils.isNotEmpty(conf)) {
					log.info("[" + CLASS_NAME + "::run] new EstraiDichiaranteTask().call() ...");
					response = new EstraiDichiaranteTask(user, istanzaSaveResponse.getIstanza(), conf).call();
				}
    		}
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] Exception ", e);
//			throw new BusinessException();
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        log.info("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runSendEmailDichiaranteIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        log.info("[" + CLASS_NAME + "::runSendEmailDichiaranteIfRequested] BEGIN Task...");
        try {
        	// Email
        	Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_EMAIL);
    		if (Boolean.TRUE.equals(activated)) {
    			String conf = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_EMAIL_CONF);
    			if (StringUtils.isNotEmpty(conf)) {
    				log.info("[" + CLASS_NAME + "::run] new SendEmailDichiaranteIstanzaTask().call() ...");
    				response = new SendEmailDichiaranteIstanzaTask(user, istanzaSaveResponse, conf, "user,protocollo", false).call();
    			} else {
    				log.error("[" + CLASS_NAME + "::run] attributo PSIT_EMAIL_CONF mancante per il modulo " + moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
    			}
    		}
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::runSendEmailDichiaranteIfRequested] Exception ", e);
//			throw new BusinessException();
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        log.info("[" + CLASS_NAME + "::runSendEmailDichiaranteIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runNotificaIstanzaIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        log.info("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] BEGIN Task...");
        try {
        	// Notify
        	Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_NOTIFY);
    		if (Boolean.TRUE.equals(activated)) {
    			String conf = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_NOTIFY_CONF);
    			if (StringUtils.isNotEmpty(conf)) {
    				log.info("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] new NotificaIstanzaTask().call() ...");
    				response = new NotificaIstanzaTask(user, istanzaSaveResponse, conf).call();
    			} else {
    				log.error("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] attributo PSIT_NOTIFICA_CONF mancante per il modulo " + moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
    			}
    		}
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] Exception ", e);
//			throw new BusinessException();
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        log.info("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runProtocollaIstanzaIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        log.info("[" + CLASS_NAME + "::runProtocollaIstanzaIfRequested] BEGIN Task...");
        try {
        	// Protocollo
    		Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_PROTOCOLLO);
    		if (Boolean.TRUE.equals(activated)) {
    			log.info("[" + CLASS_NAME + "::runProtocollaIstanzaIfRequested] new ProtocollaIstanzaTask().call() ...");
    			response = new ProtocollaIstanzaTask(user, getIstanzaSaved().getIdIstanza()).call();
    		}
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::runProtocollaIstanzaIfRequested] Exception ", e);
//			throw new BusinessException();
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        log.info("[" + CLASS_NAME + "::runProtocollaIstanzaIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runWfAzioneIstanzaIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        log.info("[" + CLASS_NAME + "::runWfAzioneIstanzaIfRequested] BEGIN Task...");
        try {
    		// Azione
    		// Gestione parametrica cambio di stato automatico in relazione alla configurazione delle azioni 
    		Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_AZIONE);
    		if (Boolean.TRUE.equals(activated)) {
    			String conf = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_AZIONE_CONF);
    			if (StringUtils.isNotEmpty(conf)) {
    				log.info("[" + CLASS_NAME + "::runWfAzioneIstanzaIfRequested] new WFAzioneIstanzaTask().call() ...");
    				response = new WfAzioneIstanzaTask(user, getIstanzaSaved(), conf, ipAddress).call();
    			} else {
    				log.error("[" + CLASS_NAME + "::runWfAzioneIstanzaIfRequested] attributo PSIT_AZIONE_CONF mancante per il modulo " + moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
    			}
    		}
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::runWfAzioneIstanzaIfRequested] Exception ", e);
//			throw new BusinessException();
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        log.info("[" + CLASS_NAME + "::runWfAzioneIstanzaIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runWFCosmoIstanzaIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        log.info("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] BEGIN Task...");
        try {
    		// Cosmo
    		Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_COSMO);
    		if (Boolean.TRUE.equals(activated)) {
    			String conf = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_COSMO_CONF);
    			if (StringUtils.isNotEmpty(conf)) {
    				log.info("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] new CosmoIstanzaTask().call() ...");
    				response = new CosmoIstanzaTask(user, getIstanzaSaved(), conf).call();
    			} else {
    				log.error("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] attributo PSIT_COSMO_CONF mancante per il modulo " + moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
    			}
    		}
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] Exception ", e);
//			throw new BusinessException();
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        log.info("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runTicketCrmIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        log.info("[" + CLASS_NAME + "::runTicketCrmIfRequested] BEGIN Task...");
        try {
        	log.debug("[" + CLASS_NAME + "::runTicketCrmIfRequested] mapModuloAttributi="+mapModuloAttributi);
    		// CRM
    		Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_CRM);
    		if (Boolean.TRUE.equals(activated)) {
    			String conf = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_CRM_CONF_NEXTCRM);
    			if (StringUtils.isNotEmpty(conf)) {
    				log.info("[" + CLASS_NAME + "::runTicketCrmIfRequested] new TicketCrmIstanzaTask().call() ...");
    				response = new TicketCrmIstanzaTask(user, getIstanzaSaved(), conf).call();
    			} else {
    				log.error("[" + CLASS_NAME + "::runTicketCrmIfRequested] attributo PSIT_CRM_CONF mancante per il modulo " + moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
    			}
    		}
		} catch (Throwable e) {
			log.error("[" + CLASS_NAME + "::runTicketCrmIfRequested] Exception mapModuloAttributi="+mapModuloAttributi, e);
//			throw new BusinessException();
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        log.info("[" + CLASS_NAME + "::runTicketCrmIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	private Istanza getIstanzaSaved() throws BusinessException {
		if (istanzaSaveResponse==null) {
			log.error("[" + CLASS_NAME + "::getIstanzaSaved] istanzaSaveResponse null.");
			throw new BusinessException("istanzaSaveResponseNull");
		}
		if (istanzaSaveResponse.getIstanza()==null) {
			log.error("[" + CLASS_NAME + "::getIstanzaSaved] istanzaSaveResponse.getIstanza() null.");
			throw new BusinessException("istanzaSaveResponseGetIstanzaNull");
		}
		return istanzaSaveResponse.getIstanza();
	}
    
}
