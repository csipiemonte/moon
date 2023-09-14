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

public class ProtocollaIstanzaTask implements Callable<String> {

	private final static String CLASS_NAME = "ProtocollaIstanzaTask";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	private String threadName = null;
	
	private UserInfo user;
	private Long idIstanza;
//	private Istanza istanzaSaved;
	
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
    public ProtocollaIstanzaTask(UserInfo user, /*Istanza istanzaSaved*/ Long idIstanza) {
		super();
		this.user = user;
		this.idIstanza = idIstanza;
//		this.istanzaSaved = istanzaSaved;
		this.threadName = Thread.currentThread().getName();
		log.debug("[" + CLASS_NAME + "::ProtocollaIstanzaTask] " + threadName + " CONSTRUCTOR idIstanza: " + idIstanza);
		
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
	        log.debug("[" + CLASS_NAME + "::call] " + threadName + " : Started Task...");

	        moonsrvDAO.protocolla(idIstanza);
	        result = "Richiesta di Protocollazione con successo";
	        
	        log.debug("[" + CLASS_NAME + "::call] " + threadName + " : Started Completed. " + result);
	        return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::call] " + threadName + " ERROR ", e);
			throw new BusinessException();
		}
    }
    
}
