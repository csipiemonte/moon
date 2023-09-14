/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper.task;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.JsonNode;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;


/**
 * PostSaveInstanzaTask NotificaIstanzaTask
 *
 * Necessita configuration json in attributiModulo PSIT_NOTIFICA_CONF
 * - subject 
 * - body
 * - ...
 *
 * {
 * 	 "subject": "?",
 *   "body": ?",
 *   ...
 * }
 *
 *
 * @author Danilo
 *
 */
public class NotificaIstanzaTask implements Callable<String> {

	private static final String CLASS_NAME = "NotificaIstanzaTask";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private JsonNode conf = null;

	private UserInfo user;
	private IstanzaSaveResponse istanzaSaveResponse;
	private String confNotifica;

	private DatiIstanzaHelper datiIstanzaHelper;

	@Autowired
	MoonsrvDAO moonsrvDAO;	
	@Autowired
	ModuloDAO moduloDAO;		

    public NotificaIstanzaTask(UserInfo user, IstanzaSaveResponse istanzaSaveResponse, String confNotifica) {
		super();
		this.user = user;
		this.istanzaSaveResponse = istanzaSaveResponse;
		this.confNotifica = confNotifica;
		LOG.debug("[" + CLASS_NAME + "::NotificaIstanzaTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): "+getIdIstanza());

        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	private String getIdIstanza() {
		return getIstanzaSaved()!=null?getIstanzaSaved().getIdIstanza().toString():"";
	}

	private Istanza getIstanzaSaved() {
		return istanzaSaveResponse.getIstanza();
	}

    /**
     * Invia una comunicazione mediante Notificatore
     *
     */
	public String call() throws BusinessException {
		try {
			String result = null;
			LOG.debug("[" + CLASS_NAME + "::call] Started Task...");
			//getConf();
			if (getIstanzaSaved()!=null) {
				moonsrvDAO.richiestaNotifyByIdIstanza(getIstanzaSaved().getIdIstanza());
			}
		    result = "Richiesta NOTIFY con successo";
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
