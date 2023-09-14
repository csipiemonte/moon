/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.ticketcrm;

import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Factory di TicketingSystem gestita secondo codiceTicketingSystem (presi da attributi modullo).
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public class TicketingSystemFactory {

	private static final String CLASS_NAME = "TicketingSystemFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	/**
	 * Torna il TicketingSystem (secondo il codiceTicketingSystem e volendo anche il codiceModulo)
	 * 
	 * @param codiceTicketingSystem  codice del sistema di ticketing CRM es.: NEXTCRM
	 * @param codiceModulo
	 * @return
	 */
    public TicketingSystem getTicketingSystem(String codiceTicketingSystem, String codiceModulo) {
    	TicketingSystem result = null;
    	try {
    		switch (codiceTicketingSystem) {
    			case "NEXTCRM" : 
    				result = new NextcrmTicketingSystem();
    				break;
    			case "R2U" : 
    				result = new ReadyToUseTicketingSystem();
    				break;
    			case "OTRS" : 
    				result = new OtrsTicketingSystem();
    				break;	
    			default :
    				LOG.error("[" + CLASS_NAME + "::getTicketingSystem] Nessun TicketingSystem per "+codiceTicketingSystem);
    		}
    	} catch (Exception e) {
    		LOG.error("[" + CLASS_NAME + "::getTicketingSystem] Errore TicketingSystemFactory - ", e);
		} finally {
			LOG.info("[" + CLASS_NAME + "::getTicketingSystem] OUT ticketingSystem = " + result);
		}
    	return result;
    }

}
