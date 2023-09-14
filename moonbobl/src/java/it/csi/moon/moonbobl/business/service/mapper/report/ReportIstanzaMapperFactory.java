/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper.report;

import org.apache.log4j.Logger;

import it.csi.moon.moonbobl.business.service.mapper.report.coto.ReportIstanzaMapper_APL_WORLDSKILLS;
import it.csi.moon.moonbobl.business.service.mapper.report.coto.ReportIstanzaMapper_DISP_SCRUT_PRESID;
import it.csi.moon.moonbobl.business.service.mapper.report.coto.ReportIstanzaMapper_ELET_RICH_ACCREDITO;
import it.csi.moon.moonbobl.business.service.mapper.report.coto.ReportIstanzaMapper_IREN_2023;
import it.csi.moon.moonbobl.business.service.mapper.report.coto.ReportIstanzaMapper_RPCCSR;
import it.csi.moon.moonbobl.business.service.mapper.report.csi.ReportIstanzaMapper_AGL_2023;
import it.csi.moon.moonbobl.business.service.mapper.report.csi.ReportIstanzaMapper_CSI_PART_TIME;
import it.csi.moon.moonbobl.business.service.mapper.report.csi.ReportIstanzaMapper_CSI_TELELAV;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;



public class ReportIstanzaMapperFactory {

	private final static String CLASS_NAME = "PrintIstanzaMapperFactory";
	private static Logger log = LoggerAccessor.getLoggerBusiness();
	
	/**
	 * Torna il DatiIstanzaInitializer secondo il modulo
	 * Permette in seguito richiamare 
	 *  - getDatiIstanza()
	 * Torna null se il modulo non prevede un inizializzatore
	 * 
	 * @param modulo
	 * @return
	 */
    public ReportIstanzaMapper getReportIstanzaMapper(String codiceModulo) {
    	ReportIstanzaMapper result = null;
    	
    	try {
    		switch (codiceModulo) {

     			case Constants.CODICE_MODULO_DISP_SCRUT_PRESID:
    				result = new ReportIstanzaMapper_DISP_SCRUT_PRESID();    				
    				break;      				
     			case Constants.CODICE_MODULO_ELET_RICH_ACCREDITO:
    				result = new ReportIstanzaMapper_ELET_RICH_ACCREDITO();    				
    				break;     				
     			case Constants.CODICE_MODULO_RPCCSR:
    				result = new ReportIstanzaMapper_RPCCSR();    				
    				break;  
     			case Constants.CODICE_MODULO_APL_WORLDSKILLS:
    				result = new ReportIstanzaMapper_APL_WORLDSKILLS();    				
    				break;  
     			case Constants.CODICE_MODULO_IREN_2023:
    				result = new ReportIstanzaMapper_IREN_2023();    				
    				break; 
     			case Constants.CODICE_MODULO_AGL_2023:
    				result = new ReportIstanzaMapper_AGL_2023();    				
    				break;
     			case Constants.CODICE_MODULO_CSI_PART_TIME:
    				result = new ReportIstanzaMapper_CSI_PART_TIME();    				
    				break;
     			case Constants.CODICE_MODULO_CSI_TELELAV:
    				result = new ReportIstanzaMapper_CSI_TELELAV();    				
    				break;    				
    				
    			default:
    				result = new ReportIstanzaMapperDefault();
			}
	    	
    	} catch (Exception e) {
    		log.error("[" + CLASS_NAME + "::getPrintIstanzaMapper] Errore PrintIstanzaMapperFactory ", e);
		} finally {
			log.info("[" + CLASS_NAME + "::getPrintIstanzaMapper] OUT result = " + result);
		}
    	
    	return result;
    }
}
