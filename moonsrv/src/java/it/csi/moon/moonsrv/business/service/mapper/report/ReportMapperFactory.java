/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.report;

import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_NOAUT;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_BACKOFFICE;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UD_ATT;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UD_CESS;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UD_INFO;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UD_RECL;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UD_VAR;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UND_ATT;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UND_CESS;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UND_INFO;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UND_RECL;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.ReportMapper_TARI_UND_VAR;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class ReportMapperFactory {
	
	private final static String CLASS_NAME = "ReportMapperFactory";
	private static Logger log = LoggerAccessor.getLoggerBusiness();
	
	public class CODICI_TIPO_ESTRAZIONE {
		public static final String TARI_APERTE = "APERTE_01";
		public static final String TARI_CHIUSE = "CHIUSE_01";
	}

	public class CODICI_MODULO {
		public static final String CODICE_MODULO_TARI_UND_VAR = "TARI_UND_VAR";
		public static final String CODICE_MODULO_TARI_UD_ATT = "TARI_UD_ATT";
		public static final String CODICE_MODULO_TARI_UND_CESS = "TARI_UND_CESS";
		public static final String CODICE_MODULO_TARI_UD_CESS = "TARI_UD_CESS";
		public static final String CODICE_MODULO_TARI_UD_VAR = "TARI_UD_VAR";
		public static final String CODICE_MODULO_TARI_BACKOFFICE = "TARI_BACKOFFICE";
		public static final String CODICE_MODULO_TARI_UND_INFO = "TARI_UND_INFO";
		public static final String CODICE_MODULO_TARI_UD_INFO = "TARI_UD_INFO";
		public static final String CODICE_MODULO_TARI_UND_RECL = "TARI_UND_RECL";
		public static final String CODICE_MODULO_TARI_UD_RECL = "TARI_UD_RECL";
		public static final String CODICE_MODULO_TARI_NOAUT = "TARI_NOAUT";
		public static final String CODICE_MODULO_TARI_UND_ATT = "TARI_UND_ATT";
	}
	
    public ReportMapper getReportMapper(String codiceModulo, String codiceEstrazione) {
    	ReportMapper result = null;
    	
    	try {
    		switch (codiceEstrazione.toUpperCase()) {
     			case CODICI_TIPO_ESTRAZIONE.TARI_APERTE:
     			case CODICI_TIPO_ESTRAZIONE.TARI_CHIUSE:
     				result = getTARIReportMapper(codiceModulo, codiceEstrazione);
    				break;
    			default:
//    				result = new ReportMapperDefault();
    				result = getTARIReportMapper(codiceModulo, codiceEstrazione);
			}
	    	
    	} catch (Exception e) {
    		log.error("[" + CLASS_NAME + "::getReportMapper] Errore getReportMapper ", e);
		} finally {
			log.info("[" + CLASS_NAME + "::getReportMapper] OUT result = " + result);
		}
    	
    	return result;
    }
    
 
   private ReportMapper getTARIReportMapper(String codiceModulo, String codiceEstrazione) {    	
    	ReportMapper result = null;    	
    	        
    	try {
    		switch (codiceModulo.toUpperCase()) {
     			case CODICI_MODULO.CODICE_MODULO_TARI_UND_VAR :   		
     				result = new ReportMapper_TARI_UND_VAR(codiceEstrazione);
    				break; 
     			case CODICI_MODULO.CODICE_MODULO_TARI_UD_ATT :   		
     				result = new ReportMapper_TARI_UD_ATT(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_UND_CESS :   		
     				result = new ReportMapper_TARI_UND_CESS(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_UD_CESS :   		
     				result = new ReportMapper_TARI_UD_CESS(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_UD_VAR :   		
     				result = new ReportMapper_TARI_UD_VAR(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_BACKOFFICE :   		
     				result = new ReportMapper_TARI_BACKOFFICE(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_UND_INFO :   		
     				result = new ReportMapper_TARI_UND_INFO(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_UD_INFO :   		
     				result = new ReportMapper_TARI_UD_INFO(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_UND_RECL :   		
     				result = new ReportMapper_TARI_UND_RECL(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_UD_RECL :   		
     				result = new ReportMapper_TARI_UD_RECL(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_NOAUT :   		
     				result = new ReportMapper_TARI_NOAUT(codiceEstrazione);
    				break;   
     			case CODICI_MODULO.CODICE_MODULO_TARI_UND_ATT :   		
     				result = new ReportMapper_TARI_UND_ATT(codiceEstrazione);
    				break;            			
    			default:
    				result = new ReportMapperDefault();
			}
	    	
    	} catch (Exception e) {
    		log.error("[" + CLASS_NAME + "::getReportMapper] Errore getReportMapper ", e);
		} finally {
			log.info("[" + CLASS_NAME + "::getReportMapper] OUT result = " + result);
		}
    	return result;
    }

}
