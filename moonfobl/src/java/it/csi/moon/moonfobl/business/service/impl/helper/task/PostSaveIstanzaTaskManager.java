/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper.task;

import java.time.Duration;
import java.time.Instant;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class PostSaveIstanzaTaskManager implements Runnable {
	
	private static final String CLASS_NAME = "PostSaveIstanzaTaskManager";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
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
		LOG.info("[" + CLASS_NAME + "::PostSaveIstanzaTaskManager] CONSTRUCTOR for id_istanza: "+saveResponse.getIstanza().getIdIstanza()+"\n"+saveResponse.getIstanza()+"\n"+mapModuloAttributi);
	}

	public void run() {
        Instant start = java.time.Instant.now();
        LOG.info("[" + CLASS_NAME + "::run] BEGIN Task...");
        try {
//        	runEstraiDichiaranteIfRequested(); // Spostato come Task Sync in IstanzaDefaultDelegate::invia()
        	runSendEmailDichiaranteIfRequested();
        	runNotificaIstanzaIfRequested();
        	runProtocollaIstanzaIfRequested();
        	runWfAzioneIstanzaIfRequested();
        	runWFCosmoIstanzaIfRequested();
        	runTicketCrmIfRequested();
        	runPublishOnMyDocsIfRequested();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::run] Exception "+e.getMessage());
			throw new BusinessException("istanzaSaveResponseNull");
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::run] END " + String.format("Completed Task in %02d:%02d.%04d \n", between.toMinutes(), between.getSeconds(), between.toMillis())); 
		}
    }

	public void runEstraiDichiaranteIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        LOG.info("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] BEGIN Task...");
        try {
        	// Estrai Dichiarante
        	Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE);
    		if (Boolean.TRUE.equals(activated)) {
				String conf = retrieveModuloAttributiStringRequired(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE_CONF);
				LOG.info("[" + CLASS_NAME + "::run] new EstraiDichiaranteTask().call() ...");
				response = new EstraiDichiaranteTask(user, istanzaSaveResponse.getIstanza(), conf).call();
    		}
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] BusinessException " + be.getMessage());
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] Exception ", e);
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::runEstraiDichiaranteIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runSendEmailDichiaranteIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        LOG.info("[" + CLASS_NAME + "::runSendEmailDichiaranteIfRequested] BEGIN Task...");
        try {
        	// Email
        	Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_EMAIL);
    		if (Boolean.TRUE.equals(activated)) {
    			String conf = retrieveModuloAttributiStringRequired(ModuloAttributoKeys.PSIT_EMAIL_CONF);
   				LOG.info("[" + CLASS_NAME + "::run] new SendEmailDichiaranteIstanzaTask().call() ...");
    			response = new SendEmailDichiaranteIstanzaTask(user, istanzaSaveResponse, conf).call();
    		}
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::runSendEmailDichiaranteIfRequested] BusinessException " + be.getMessage());
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::runSendEmailDichiaranteIfRequested] Exception ", e);
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::runSendEmailDichiaranteIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runNotificaIstanzaIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        LOG.info("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] BEGIN Task...");
        try {
        	// Notify
        	Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_NOTIFY);
    		if (Boolean.TRUE.equals(activated)) {
    			String conf = retrieveModuloAttributiStringRequired(ModuloAttributoKeys.PSIT_NOTIFY_CONF);
    			LOG.info("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] new NotificaIstanzaTask().call() ...");
    			response = new NotificaIstanzaTask(user, istanzaSaveResponse, conf).call();
    		}
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] BusinessException " + be.getMessage());
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] Exception ", e);
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::runNotificaIstanzaIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runProtocollaIstanzaIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        LOG.info("[" + CLASS_NAME + "::runProtocollaIstanzaIfRequested] BEGIN Task...");
        try {
        	// Protocollo
    		Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_PROTOCOLLO);
    		if (Boolean.TRUE.equals(activated)) {
    			LOG.info("[" + CLASS_NAME + "::runProtocollaIstanzaIfRequested] new ProtocollaIstanzaTask().call() ...");
    			response = new ProtocollaIstanzaTask(user, getIstanzaSaved()).call();
    		}
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::runProtocollaIstanzaIfRequested] BusinessException " + be.getMessage());
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::runProtocollaIstanzaIfRequested] Exception ", e);
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::runProtocollaIstanzaIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runWfAzioneIstanzaIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        LOG.info("[" + CLASS_NAME + "::runWfAzioneIstanzaIfRequested] BEGIN Task...");
        try {
    		// Azione
    		// Gestione parametrica cambio di stato automatico in relazione alla configurazione delle azioni 
    		Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_AZIONE);
    		if (Boolean.TRUE.equals(activated)) {
    			String conf = null;
//    			String conf = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_AZIONE_CONF);
    			LOG.info("[" + CLASS_NAME + "::runWfAzioneIstanzaIfRequested] new WFAzioneIstanzaTask().call() ...");
    			response = new WfAzioneIstanzaTask(user, getIstanzaSaved(), conf, ipAddress).call();
    		}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::runWfAzioneIstanzaIfRequested] Exception ", e);
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::runWfAzioneIstanzaIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runWFCosmoIstanzaIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        LOG.info("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] BEGIN Task...");
        try {
    		// Cosmo
    		Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_COSMO);
    		if (Boolean.TRUE.equals(activated)) {
    			String conf = retrieveModuloAttributiStringRequired(ModuloAttributoKeys.PSIT_COSMO_CONF);
   				LOG.info("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] new CosmoIstanzaTask().call() ...");
    			response = new CosmoIstanzaTask(user, getIstanzaSaved(), conf).call();
    		}
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] BusinessException " + be.getMessage());
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] Exception ", e);
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::runWFCosmoIstanzaIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runTicketCrmIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        LOG.info("[" + CLASS_NAME + "::runTicketCrmIfRequested] BEGIN Task...");
        try {
        	LOG.debug("[" + CLASS_NAME + "::runTicketCrmIfRequested] mapModuloAttributi="+mapModuloAttributi);
    		// CRM
    		Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_CRM);
    		if (Boolean.TRUE.equals(activated)) {
    			String system = retrieveModuloAttributiStringRequired(ModuloAttributoKeys.PSIT_CRM_SYSTEM);
   				String conf = retrieveModuloAttributiStringRequired(retrieveModuloAttributoKeysConf("PSIT_CRM_CONF_"+system));
	    		LOG.info("[" + CLASS_NAME + "::runTicketCrmIfRequested] new TicketCrmIstanzaTask().call() ...");
	    		response = new TicketCrmIstanzaTask(user, getIstanzaSaved(), conf).call();
    		}
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::runTicketCrmIfRequested] BusinessException " + be.getMessage());
		} catch (Throwable e) {
			LOG.error("[" + CLASS_NAME + "::runTicketCrmIfRequested] Exception ", e);
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::runTicketCrmIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	public void runPublishOnMyDocsIfRequested() {
        Instant start = java.time.Instant.now();
        String response = null;
        LOG.info("[" + CLASS_NAME + "::runPublishOnMyDocsIfRequested] BEGIN Task...");
        try {
        	LOG.debug("[" + CLASS_NAME + "::runPublishOnMyDocsIfRequested] mapModuloAttributi="+mapModuloAttributi);
    		// CRM
    		Boolean activated = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_MYDOCS);
    		if (Boolean.TRUE.equals(activated)) {
//    			String conf = retrieveModuloAttributiStringRequired(ModuloAttributoKeys.PSIT_MYDOCS_CONF);    			
	    		String conf = null;
	    		LOG.info("[" + CLASS_NAME + "::runPublishOnMyDocsIfRequested] new PublishOnMyDocsTask().call() ...");	    		
	    		response = new PublishOnMyDocsTask(user, getIstanzaSaved(), conf).call();
    		}
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::runPublishOnMyDocsIfRequested] BusinessException " + be.getMessage());
		} catch (Throwable e) {
			LOG.error("[" + CLASS_NAME + "::runPublishOnMyDocsIfRequested] Exception ", e);
		} finally {
	        Instant end = java.time.Instant.now();
	        Duration between = java.time.Duration.between(start, end);
	        LOG.info("[" + CLASS_NAME + "::runPublishOnMyDocsIfRequested] END " + String.format("Completed Task in %02d:%02d.%04d  %s", between.toMinutes(), between.getSeconds(), between.toMillis(), response)); 
		}
	}
	
	protected String retrieveModuloAttributiStringRequired(ModuloAttributoKeys maConf) throws BusinessException {
		String result = null;
		try {
			result = mapModuloAttributi.getWithCorrectType(maConf);
			if (StringUtils.isEmpty(result)) {
				LOG.error("[" + CLASS_NAME + "::retrieveModuloAttributiStringRequired] attributo " + maConf.name() + " isEmpty per il modulo " + moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
				throw new BusinessException("MODULO_ATTRIBUTO_EMPTY");
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveModuloAttributiStringRequired] attributo " + maConf.name() + " mancante per il modulo " + moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
			throw new BusinessException("MODULO_ATTRIBUTO_NOT_FOUND");
		}
		return result;
	}

	protected ModuloAttributoKeys retrieveModuloAttributoKeysConf(String moduloAttrobutoName) {
		ModuloAttributoKeys maConf = ModuloAttributoKeys.byName(moduloAttrobutoName);
		return maConf;
	}
	
	private Istanza getIstanzaSaved() throws BusinessException {
		if (istanzaSaveResponse==null) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaSaved] istanzaSaveResponse null.");
			throw new BusinessException("istanzaSaveResponseNull");
		}
		if (istanzaSaveResponse.getIstanza()==null) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaSaved] istanzaSaveResponse.getIstanza() null.");
			throw new BusinessException("istanzaSaveResponseGetIstanzaNull");
		}
		return istanzaSaveResponse.getIstanza();
	}
    
}
