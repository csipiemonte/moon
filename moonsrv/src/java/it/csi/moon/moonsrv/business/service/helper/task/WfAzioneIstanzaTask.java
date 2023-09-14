/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.helper.task;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonsrv.business.service.WorkflowService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;


public class WfAzioneIstanzaTask implements Callable<String> {

	private static final String CLASS_NAME = "WfAzioneIstanzaTask";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private String threadName = null;
	private UserInfo user;
	private Istanza istanza;
	private String confAzione;
	
	@Autowired
	WorkflowService workflowService;
	
    public WfAzioneIstanzaTask(UserInfo user, Istanza istanza, String confAzione) {
		super();
		this.user = user;
		this.istanza = istanza;
		this.confAzione = confAzione;
		this.threadName = Thread.currentThread().getName();
		LOG.debug("[" + CLASS_NAME + "::WFAzioneIstanzaTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): "+istanza.getIdIstanza());
		
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
	    	LOG.debug("[" + CLASS_NAME + "::call] " + threadName + " : Started Task...");
	    	LOG.info("[" + CLASS_NAME + "::call] " + threadName + " : Started Task... " + confAzione + " on " + this.istanza.getIdIstanza() + " - " + this.istanza.getCodiceIstanza());

			final String codiceAzione = confAzione; // "SALVA_PROTOCOLLO";
			Azione azione = workflowService.verificaAzionePossibile(istanza, codiceAzione);
			LOG.debug("[" + CLASS_NAME + "::call] " + threadName + " : azione = " + azione);
			Azione azioneResult = workflowService.compieAzione(UserInfo.ADMIN, istanza.getIdIstanza(), azione);
			LOG.debug("[" + CLASS_NAME + "::call] " + threadName + " : azioneResult = " + azioneResult);

			LOG.debug("[" + CLASS_NAME + "::call] " + threadName + " : Started Completed. " + result);
	        return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::call] " + threadName + " ERROR ");
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::call] " + threadName + " ERROR ", e);
			throw new BusinessException();
		}
    }
    
}
