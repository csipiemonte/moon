/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.CallerApplicativo;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.Exception_Exception;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.FamigliaANPR;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.OpzioniReperimentoFamiglia;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.RicercaSoggettiANPRFilter;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.RicercheANPRService;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.RicercheANPRWS;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.AnprDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.wso2.apiman.oauth2.helper.GenericWrapperFactoryBean;
import it.csi.wso2.apiman.oauth2.helper.OauthHelper;
import it.csi.wso2.apiman.oauth2.helper.TokenRetryManager;
import it.csi.wso2.apiman.oauth2.helper.WsProvider;
import it.csi.wso2.apiman.oauth2.helper.extra.cxf.CxfImpl;

/**
* Implementazione DAO ANPR via WebService via API Manager Entreprise
* 
* @deprecated (Use AnprApimintDAO)
* 
* @author Laurent Pissard
* 
* @since 1.0.0
*/
@Deprecated
@Component
@Qualifier("orchanprApiManagerWS")
public class AnprDAOImplApiMan implements AnprDAO {
	private static final String CLASS_NAME = "AnprDAOImplApiMan";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
    private String oauthUrl;
    private String consumerKey;
    private String consumerSecret;
    
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
	
    private static final String ORCHANPR_IDENTIFICATIVO_APPLICATIVO = "elixforms";
//    private static final String ORCHANPR_BASIC_AUTH_USR = "moon"; //"elixforms"; // "apimanager"
//    private static final String ORCHANPR_BASIC_AUTH_PWD = "mypass0."; //"elixforms"; // "apimanagerpass"
//    private static final String ORCHANPR_BASIC_AUTH_PROPERTY = ORCHANPR_BASIC_AUTH_USR + ":" + ORCHANPR_BASIC_AUTH_PWD;
    private static final String ORCHANPR_BASIC_AUTH_PROPERTY = "";
    private static final String ORCHANPR_OPZIONI_PROFILO_OUTPUT = "CIND";

	@Override
	public FamigliaANPR getFamigliaANPR(String codiceFiscale) throws DAOException {
		FamigliaANPR ris = null;
		try {
			RicercaSoggettiANPRFilter filter = new RicercaSoggettiANPRFilter();
			filter.setCodiceFiscale(codiceFiscale);
			ris = getFamigliaANPR(filter);
		} catch (Exception_Exception e) {
			LOG.error("[" + CLASS_NAME + "::getFamigliaANPR] Exception "+e.getMessage());
			throw new DAOException(" Exception_Exception ");
		}
		return ris;
	}
	
    private FamigliaANPR getFamigliaANPR(RicercaSoggettiANPRFilter filter) throws Exception_Exception {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] BEGIN");
        	RicercheANPRService ss = new RicercheANPRService();
        	RicercheANPRWS port = ss.getRicercheANPRWSPort();
		    
			setOauthUrl("http://tst-api-ent.ecosis.csi.it:80/api/token");
			setConsumerKey("fWJ6G3RhqgnwNnJhEGruaMwiIy8a");
			setConsumerSecret("TfwxINcPaDbUhhaQ3s5t51OBkuoa");
			OauthHelper oauthHelper = new OauthHelper(getOauthUrl(), getConsumerKey(), getConsumerSecret());
		
			TokenRetryManager trm = new TokenRetryManager();
//			System.out.println(trm.getVersion());
			trm.setOauthHelper(oauthHelper);
			WsProvider wsp = new CxfImpl();
//			System.out.println("WsProvider id " + wsp.getProviderId());
			trm.setWsProvider(wsp);

			// Header JASS per autenticazione username/password
//			System.out.println("Header JASS per autenticazione "+ORCHANPR_BASIC_AUTH_PROPERTY);
			String encoded = new String(Base64.encodeBase64(ORCHANPR_BASIC_AUTH_PROPERTY.getBytes()));
			trm.putHeader(TokenRetryManager.X_AUTH,"Basic " + encoded);
//			System.out.println("encodedBytes   used: "+encoded);

			GenericWrapperFactoryBean gwfb = new GenericWrapperFactoryBean();
			gwfb.setEndPoint("http://tst-api-ent.ecosis.csi.it:80/api/DEMOG_nao_RicercheANPR/1.0");
			gwfb.setWrappedInterface(RicercheANPRWS.class);
			gwfb.setPort(port);
			// port non ha piu' nessun uso
			// quindi lo annullo per evitare di usarlo per errore (accederei direttamente al backend)
			port = null;
			gwfb.setTokenRetryManager(trm);

//			System.out.println("GenericWrapperFactoryBean gwfb.create() ...");
			RicercheANPRWS wrapped = (RicercheANPRWS) gwfb.create();
//			System.out.println("GenericWrapperFactoryBean gwfb  created.");

			return wrapped.getFamigliaANPR(getCallerApplicativo(), filter, getOpzioni());
			
        } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getFamigliaANPR] Errore generico servizio getFamigliaANPR", e);
			throw new ServiceException("Errore generico servizio getFamigliaANPR");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            System.out.println("getFamigliaANPR("+filter+") in " + sec + " milliseconds.");
            LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] END");
        }
    }

	private static CallerApplicativo getCallerApplicativo() {
		CallerApplicativo caller = new CallerApplicativo();
		caller.setIdentificativoApplicativo(ORCHANPR_IDENTIFICATIVO_APPLICATIVO);
		return caller;
	}

	private static OpzioniReperimentoFamiglia getOpzioni() {
		OpzioniReperimentoFamiglia opzioni = new OpzioniReperimentoFamiglia();
		opzioni.setProfiloOutput(ORCHANPR_OPZIONI_PROFILO_OUTPUT);
		return opzioni;
	}
}
