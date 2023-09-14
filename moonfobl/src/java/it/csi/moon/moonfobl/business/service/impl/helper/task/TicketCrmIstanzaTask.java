/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper.task;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class TicketCrmIstanzaTask implements Callable<String> {

	private static final String CLASS_NAME = "TicketCrmIstanzaTask";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private String threadName = null;
	
	private UserInfo user;
	private Istanza istanzaSaved;
	private String conf;
	
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
    public TicketCrmIstanzaTask(UserInfo user, Istanza istanzaSaved, String conf) {
		super();
		this.user = user;
		this.istanzaSaved = istanzaSaved;
		this.conf = conf;
		this.threadName = Thread.currentThread().getName();
		LOG.debug("[" + CLASS_NAME + "::TicketCrmIstanzaTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): "+istanzaSaved.getIdIstanza());
		
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
    
    /**
     * Richiede la Ticket CRM l'istanza
     * 
     */
    public String call() throws BusinessException {
    	try {
	    	String result = null;
	        LOG.debug("[" + CLASS_NAME + "::call] BEGIN Task...");

	        moonsrvDAO.creaTicketCrm(istanzaSaved.getIdIstanza());
	        result = "Richiesta di Ticket CRM con successo";
	        
	        LOG.debug("[" + CLASS_NAME + "::call] END result=" + result);
	        return result;
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::call] DAOException ");
			throw new BusinessException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::call] ERROR ", e);
			throw new BusinessException();
		}
    }
    
}
