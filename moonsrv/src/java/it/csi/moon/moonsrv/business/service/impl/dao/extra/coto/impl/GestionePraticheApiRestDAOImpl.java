/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.impl;

import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.component.ApiRestTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.GestionePraticheApiRestDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;

/**
* Implementazione DAO Pratiche edilizie
* 
* @author Danilo
* 
* @since 1.0.0
*/
//@Qualifier("applogic")
@Component
public class GestionePraticheApiRestDAOImpl extends ApiRestTemplateImpl implements GestionePraticheApiRestDAO {
	
	private static final String CLASS_NAME = "GestionePraticheApiRestDAOImpl";
	
	private static final String METHOD_DETAIL = "detailPratica";
	
	
    public GestionePraticheApiRestDAOImpl() {
    	super();
    	String endpoint = EnvProperties.readFromFile(EnvProperties.PRAEDI_ENDPOINT);
    	setEndpoint(endpoint);
    }
  
//	@Override
//	public String getUsername() {
//		return EnvProperties.readFromFile(EnvProperties.PRAEDI_USR);
//	}
//	@Override
//	public String getPassword() {
//		return EnvProperties.readFromFile(EnvProperties.PRAEDI_PWD);
//	}
//	
//	@Override
//	public String getPathExtra() {
//		return "";
//	}
	
    private String getUrl(Integer registro, Integer progressivo, Integer anno) {    
    	
//    	String endpoint = EnvProperties.readFromFile(EnvProperties.PRAEDI_ENDPOINT);
    	String usr = EnvProperties.readFromFile(EnvProperties.PRAEDI_USR);
    	String pwd = EnvProperties.readFromFile(EnvProperties.PRAEDI_PWD);
    	String ente = EnvProperties.readFromFile(EnvProperties.PRAEDI_ENTE);
    	String method = METHOD_DETAIL;
    	String codicePratica = registro+"-"+progressivo+"-"+anno;
    	
    	String url = "?username="+usr+"&password="+pwd+"&ente="+ente+"&method="+method+"&codice_pratica="+codicePratica;
    	return url;
    }

	@Override
	public String getJsonPratica(Integer registro, Integer progressivo,Integer anno)
			throws DAOException {
		final String methodName = "getJsonPratica";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
			String url = getUrl(registro,progressivo,anno);					
			ResponseUUID<String> result = getJson(url, String.class);
			
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + result.getResponse());
			return result.getResponse();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(methodName +"() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
	}


}
