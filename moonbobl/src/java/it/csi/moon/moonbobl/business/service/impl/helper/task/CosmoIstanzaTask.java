/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.task;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


public class CosmoIstanzaTask implements Callable<String> {

	private final static String CLASS_NAME = "CosmoIstanzaTask";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	private String threadName = null;
	
	private UserInfo user;
	private Istanza istanzaSaved;
	private String confCosmo;
	
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
    public CosmoIstanzaTask(UserInfo user, Istanza istanzaSaved, String confCosmo) {
		super();
		this.user = user;
		this.istanzaSaved = istanzaSaved;
		this.confCosmo = confCosmo;
		this.threadName = Thread.currentThread().getName();
		log.debug("[" + CLASS_NAME + "::CosmoIstanzaTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): "+istanzaSaved.getIdIstanza());

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
	        log.debug("[" + CLASS_NAME + "::call] BEGIN Task...");

	        moonsrvDAO.creaPraticaEdAvviaProcesso(istanzaSaved.getIdIstanza());
	        result = "Richiesta di Creazione Pratica COSMO ed avvio Processo effettuata con successo";
	        
	        log.debug("[" + CLASS_NAME + "::call] END result=" + result);
	        return result;
		} catch (BusinessException be) {
			log.warn("[" + CLASS_NAME + "::call] ERROR ");
			throw be;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::call] ERROR ", e);
			throw new BusinessException();
		}
    }
    
}
