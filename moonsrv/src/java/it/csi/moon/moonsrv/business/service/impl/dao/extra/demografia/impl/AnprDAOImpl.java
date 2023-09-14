/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.impl;

import java.net.URL;

import javax.xml.namespace.QName; // se Problems : WinPreferences > Java Compiler > Compliance  Java 8

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
import it.csi.moon.moonsrv.util.LoggerAccessor;


/**
* Implementazione DAO ANPR via WebService Direct (no API Manager)
* 
* @author Laurent Pissard
* 
* @since 1.0.0
*/
@Deprecated
@Component
@Qualifier("orchanprWS")
public class AnprDAOImpl implements AnprDAO {
	private static final String CLASS_NAME = "AnprDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	
    private static final String QNAME_LOCALPART;
    private static final URL ORCHANPR_WSDL_LOCATION;
    private static final QName ORCHANPR_SERVICENAME;

    private static final String ORCHANPR_IDENTIFICATIVO_APPLICATIVO = "elixforms";
//    private static final String ORCHANPR_BASCI_AUTH_USERNAME_PROPERTY = "moon";
//    private static final String ORCHANPR_BASCI_AUTH_PASSWORD_PROPERTY = "mypass0.";
    private static final String ORCHANPR_OPZIONI_PROFILO_OUTPUT = "CIND";
    
    static {
        URL tempURLwsdl = null;
        try {
        	tempURLwsdl = new java.net.URL("http://tst-applogic.reteunitaria.piemonte.it/orchanpr/RicercheANPRService?wsdl");
        } catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::static] Exception ", e);
		}
        ORCHANPR_WSDL_LOCATION = tempURLwsdl;
        QNAME_LOCALPART = "RicercheANPRService";
    	ORCHANPR_SERVICENAME = new QName("http://it/csi/nao/orchanpr/business/naoricercheanpr/ricercheanpr", QNAME_LOCALPART);
    }
    
    
//	@Override
	public FamigliaANPR getFamigliaANPR(String coficeFiscale) throws DAOException {
		FamigliaANPR ris = null;
		try {
			RicercaSoggettiANPRFilter filter = new RicercaSoggettiANPRFilter();
			filter.setCodiceFiscale(coficeFiscale);
			ris = getFamigliaANPR(filter);
		} catch (Exception_Exception e) {
			LOG.error("[" + CLASS_NAME + "::getFamigliaANPR] Exception_Exception ", e);
			throw new DAOException(" Exception_Exception "+ e.getMessage());
		}
		return ris;
	}

    private FamigliaANPR getFamigliaANPR(RicercaSoggettiANPRFilter filter) throws Exception_Exception {
        long start = System.currentTimeMillis();
        try {
        	RicercheANPRService service = new RicercheANPRService(ORCHANPR_WSDL_LOCATION, ORCHANPR_SERVICENAME);
        	RicercheANPRWS port = service.getPort(RicercheANPRWS.class);
//        	((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, ORCHANPR_BASCI_AUTH_USERNAME_PROPERTY);
//        	((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, ORCHANPR_BASCI_AUTH_PASSWORD_PROPERTY);
            return port.getFamigliaANPR(getCallerApplicativo(), filter, getOpzioni());
        } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("getFamigliaANPR("+filter+") in " + sec + " milliseconds.");
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
