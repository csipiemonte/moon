/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.impl;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimintTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.StardasDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.stardas.cxfclient.SmistaDocumentoRequestType;
import it.csi.stardas.cxfclient.SmistaDocumentoResponseType;
import it.csi.stardas.cxfclient.StardasServiceProxy;
import it.csi.stardas.cxfclient.StardasServiceProxyPortType;
import it.csi.wso2.apiman.oauth2.helper.GenericWrapperFactoryBean;
import it.csi.wso2.apiman.oauth2.helper.OauthHelper;
import it.csi.wso2.apiman.oauth2.helper.TokenRetryManager;
import it.csi.wso2.apiman.oauth2.helper.WsProvider;
import it.csi.wso2.apiman.oauth2.helper.extra.cxf.CxfImpl;

@Qualifier("apimint")
@Component
public class StardasApimintDAOImpl extends ApimintTemplateImpl implements StardasDAO {

	private static final String CLASS_NAME = "StardasApimintDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private final String apimintStardasPath = EnvProperties.readFromFile(EnvProperties.PROTOCOLLO_STARDAS_APIMINT_PATH);

	public StardasApimintDAOImpl() {
		super();
    	setEndpoint(EnvProperties.readFromFile(EnvProperties.APIMINT_ENDPOINT));
	}
    
	public SmistaDocumentoResponseType smistaDocumento(SmistaDocumentoRequestType smistaDocumentoRequest) throws DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
		SmistaDocumentoResponseType result = null;
		
	    try {
	    	LOG.info("[" + CLASS_NAME + "::smistaDocumento] BEGIN");
	    	
	    	//StardasService
	    	LOG.debug("[" + CLASS_NAME + "::smistaDocumento] new StardasServiceProxy() ...");
	    	StardasServiceProxy ss = new StardasServiceProxy();
	    	StardasServiceProxyPortType port = ss.getStardasServiceProxyHttpSoap11Endpoint();
	        
			TokenRetryManager trm = new TokenRetryManager();
			LOG.debug("[" + CLASS_NAME + "::smistaDocumento] TokenRetryManager.getVersion=" + trm.getVersion());
			OauthHelper oauthHelper = new OauthHelper(getEndpoint() + "/token", getConsumerKey(), getConsumerSecret());
			trm.setOauthHelper(oauthHelper);
			WsProvider wsp = new CxfImpl();
			LOG.debug("[" + CLASS_NAME + "::smistaDocumento] CxfImpl of WsProvider id " + wsp.getProviderId());
			trm.setWsProvider(wsp);
			
			// Forzatura Header
			trm.putHeader("SOAPAction", "http://www.csi.it/stardas/services/StardasService/SmistaDocumento");
			
			LOG.debug("[" + CLASS_NAME + "::smistaDocumento] new GenericWrapperFactoryBean() ...");
			GenericWrapperFactoryBean gwfb = new GenericWrapperFactoryBean();
			gwfb.setEndPoint(getEndpoint() + apimintStardasPath); // "/documentale/stardasws-test/v1"
			gwfb.setWrappedInterface(StardasServiceProxyPortType.class);
			gwfb.setPort(port);
			gwfb.setTokenRetryManager(trm);
	    	
			StardasServiceProxyPortType wrapped = (StardasServiceProxyPortType) gwfb.create(); // NON è "castabile" a BindingProvider
			LOG.debug("[" + CLASS_NAME + "::smistaDocumento] gwfb created for "+ wrapped.getClass().getName());

			// Chiamata
	    	if(LOG.isDebugEnabled())
	    		LOG.debug("[" + CLASS_NAME + "::smistaDocumento] smistaDocumentoRequest =\n" + smistaDocumentoRequest);
			result = wrapped.smistaDocumento(smistaDocumentoRequest);
		    return result;
	    } catch (SOAPFaultException fe) {
	    	LOG.error("[" + CLASS_NAME + "::smistaDocumento] SOAPFaultException " + fe.getMessage());
			errore = "SOAPFaultException";
	    	throw new DAOException();
	    } catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::smistaDocumento] Exception " + e.getMessage());
			errore = "Exception";
	    	throw new DAOException();
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::smistaDocumento] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
        }
	}
	   
}

