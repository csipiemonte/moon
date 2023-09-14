/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.component;

import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.wso2.apiman.oauth2.helper.GenericWrapperFactoryBean;
import it.csi.wso2.apiman.oauth2.helper.OauthHelper;
import it.csi.wso2.apiman.oauth2.helper.TokenRetryManager;
import it.csi.wso2.apiman.oauth2.helper.WsProvider;
import it.csi.wso2.apiman.oauth2.helper.extra.cxf.CxfImpl;

/**
 * Template per i DAO verso API Manager API WS con uso Token-Retry 
 *
 * @author Laurent Pissard
 * 
 * @since 1.0.0
 */
public class ApimanWSTemplateImpl {
	
	private static final String CLASS_NAME = "ApimanWSTemplateImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private String pathExtra;
    private String oauthUrl;
    private String consumerKey;
    private String consumerSecret;
    public String getPathExtra() {
		return pathExtra;
	}
	public void setPathExtra(String pathExtra) {
		this.pathExtra = pathExtra;
	}
	public String getOauthUrl() {
		return oauthUrl;
	}
	public void setOauthUrl(String oauthUrl) {
		this.oauthUrl = oauthUrl;
	}
	public String getConsumerKey() {
		return consumerKey;
	}
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	public String getConsumerSecret() {
		return consumerSecret;
	}
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	protected Integer getTimeout() {
		return EnvProperties.readIntegerFromFile(EnvProperties.APIMAN_TIMEOUT);
	}
	
	protected <T> Object getPortService(Object port, Class<T> wrappedInterface) {
		try {
			setOauthUrl(EnvProperties.readFromFile(EnvProperties.APIMAN_ENDPOINT) + "/token");
			setConsumerKey(EnvProperties.readFromFile(EnvProperties.APIMAN_PROD_CONSUMERKEY));
			setConsumerSecret(EnvProperties.readFromFile(EnvProperties.APIMAN_PROD_CONSUMERSECRET));
			OauthHelper oauthHelper = null;
			Integer timeout = getTimeout();
			if (timeout!=null) {
				oauthHelper = new OauthHelper(getOauthUrl(), getConsumerKey(), getConsumerSecret(), getTimeout());
			} else {
				oauthHelper = new OauthHelper(getOauthUrl(), getConsumerKey(), getConsumerSecret());
			}
			
			TokenRetryManager trm = new TokenRetryManager();
		    
//			System.out.println(trm.getVersion());
			trm.setOauthHelper(oauthHelper);
			WsProvider wsp = new CxfImpl();
//			System.out.println("WsProvider id " + wsp.getProviderId());
			
			trm.setWsProvider(wsp);
			
			GenericWrapperFactoryBean gwfb = new GenericWrapperFactoryBean();
			gwfb.setEndPoint(EnvProperties.readFromFile(EnvProperties.APIMAN_ENDPOINT) + getPathExtra());
			gwfb.setWrappedInterface(wrappedInterface);
			gwfb.setPort(port);
			
			// port non ha piu' nessun uso
			// quindi lo annullo per evitare di usarlo per errore (accederei direttamente al backend)
			port = null;
			gwfb.setTokenRetryManager(trm);
	    	Object result = gwfb.create();
//		    System.out.println("GenericWrapperFactoryBean gwfb created.");
		    return result;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getPortService] Errore generico getPortService", e);
			throw new DAOException("Errore generico servizio getPortService");
	    }
	}
	
}
