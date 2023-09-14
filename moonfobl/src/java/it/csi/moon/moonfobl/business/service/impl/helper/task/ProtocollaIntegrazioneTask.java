/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper.task;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class ProtocollaIntegrazioneTask implements Callable<String> {

	private static final String CLASS_NAME = "ProtocollaIntegrazioneTask";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private UserInfo user;
	private Long idIstanza;
	private Long idStoricoWorkflow;
	
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
    public ProtocollaIntegrazioneTask(UserInfo user, Long idIstanza, Long idStoricoWorkflow) {
		super();
		this.user = user;
		this.idIstanza = idIstanza;
		this.idStoricoWorkflow = idStoricoWorkflow;
		LOG.debug("[" + CLASS_NAME + "::ProtocollaIntegrazioneTask] CONSTRUCTOR idIstanza="+idIstanza+"  idStoricoWorkflow="+idStoricoWorkflow);
		
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
    
    /**
     * Richiede la Protocollazione l'istanza
     * 
     */
    public String call() throws BusinessException {
    	try {
	    	String result = null;
	        LOG.debug("[" + CLASS_NAME + "::call] BEGIN Task...");

	        moonsrvDAO.protocollaIntegrazione(idIstanza, idStoricoWorkflow);
	        result = "Richiesta di Protocollazione Integrazione con successo";
	        
	        LOG.debug("[" + CLASS_NAME + "::call] END result=" + result);
	        return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::call] ERROR ");
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::call] ERROR ", e);
			throw new BusinessException();
		}
    }
    
}
