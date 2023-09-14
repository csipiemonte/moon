/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.impl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.StardasDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.stardas.cxfclient.SmistaDocumentoRequestType;
import it.csi.stardas.cxfclient.SmistaDocumentoResponseType;
import it.csi.stardas.cxfclient.StardasServiceProxy;
import it.csi.stardas.cxfclient.StardasServiceProxyPortType;

// Commento cosi da non inizializzare il Bean unitilmente, adesso si usa StardasApimintDAO
//@Qualifier("wso001")
//@Component
public class StardasDAOImpl implements StardasDAO {
	
	private static final String CLASS_NAME = "StardasDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
    public static final URL WSDL_LOCATION;
    public static final QName SERVICE = new QName("http://www.csi.it/stardas/wso2/StardasService", "StardasServiceProxy");
    
    static {
        URL url = null;
        try {
            url = new URL(EnvProperties.readFromFile(EnvProperties.PROTOCOLLO_STARDAS_WSDL));
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(StardasServiceProxy.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", EnvProperties.readFromFile(EnvProperties.PROTOCOLLO_STARDAS_WSDL));
        }
        WSDL_LOCATION = url;
    }
    
	public SmistaDocumentoResponseType smistaDocumento(SmistaDocumentoRequestType smistaDocumentoRequest) throws DAOException {
		
		SmistaDocumentoResponseType result = null;
		
	    try {
	    	//StardasService
//	    	StardasServiceProxy service = new StardasServiceProxy();
	    	StardasServiceProxy service = new StardasServiceProxy(WSDL_LOCATION, SERVICE);
	    	StardasServiceProxyPortType port = service.getStardasServiceProxyHttpSoap11Endpoint();
	    	
			// Chiamata
	    	if(LOG.isDebugEnabled())
	    		LOG.debug("[" + CLASS_NAME + "::smistaDocumento] smistaDocumentoRequest =\n" + smistaDocumentoRequest);
			result = port.smistaDocumento(smistaDocumentoRequest);

		    return result;
	    } catch (SOAPFaultException fe) {
	    	LOG.error("SOAPFaultException "+fe.getMessage(), fe);
	    	throw new DAOException();
	    } catch (Exception e) {
	    	 LOG.error("Exception "+e.getMessage(), e);
	    	 throw new DAOException();
	     }

	}
	
}
