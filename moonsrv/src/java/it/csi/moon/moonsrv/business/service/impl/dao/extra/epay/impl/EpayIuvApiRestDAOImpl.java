/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.epay.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.apirest.epay.v1.dto.DebtPositionData;
import it.csi.apirest.epay.v1.dto.DebtPositionReference;
import it.csi.apirest.epay.v1.dto.PaymentData;
import it.csi.apirest.epay.v1.dto.PaymentDataResult;
import it.csi.apirest.epay.v1.dto.PaymentNotice;
import it.csi.apirest.epay.v1.dto.PaymentReferences;
import it.csi.apirest.epay.v1.dto.Status;
import it.csi.moon.commons.dto.extra.epay.GetRtRequest;
import it.csi.moon.commons.dto.extra.epay.GetRtResponse;
import it.csi.moon.commons.dto.extra.epay.IUVChiamanteEsternoRequest;
import it.csi.moon.commons.dto.extra.epay.IUVChiamanteEsternoResponse;
import it.csi.moon.commons.dto.extra.epay.PagamentoIUVRequest;
import it.csi.moon.commons.dto.extra.epay.PagamentoIUVResponse;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApiRestTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.epay.EpayIuvDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;

/**
 * EPAY via servizi REST diretto (no API Manager)
 * 
 * @author Laurent Pissard
 */
@Qualifier("applogic")
@Component
public class EpayIuvApiRestDAOImpl extends ApiRestTemplateImpl implements EpayIuvDAO {
	
	private static final String CLASS_NAME = "EpayIuvApiRestDAOImpl";
	private static final String ENDPOINT = EnvProperties.readFromFile(EnvProperties.EPAY_IUV_REST_APPLOGIC_ENDPOINT);
	private static final String USERNAME = EnvProperties.readFromFile(EnvProperties.EPAY_IUV_REST_APPLOGIC_USERNAME);
	private static final String PASSWORD = EnvProperties.readFromFile(EnvProperties.EPAY_IUV_REST_APPLOGIC_PASSWORD);
	
    public EpayIuvApiRestDAOImpl() {
    	super();
    	setEndpoint(ENDPOINT);
    }
    
    /*
     * Setter Getter dei Parametri specifici a Cosmo
     */
    @Override
	public String getUsername() {
		return USERNAME;
	}
    @Override
	public String getPassword() {
		return PASSWORD;
	}
	
//	private static final Map<Integer,String> EPAY_MAP_STATUS_CODE_MESSAGE = new HashMap(){
//        {
//			put(401, "401 - Unauthorized - il fruitore non e' stato autenticato correttamente oppure non e' stato registrato come fruitore abilitato");
//			put(403, "403 - Forbidden - il fruitore non e' autorizzato alla specifica operazione richiesta");
//			put(404, "404 - NotFound - la risorsa richiesta non esiste");
//			put(409, "409 - Conflict - una risorsa corrispondente agli identificativi univoci");
//			put(500, "500 - InternalServerError - si e' verificato un errore interno.");
//        }
//    };
			

	//
	// Crea IUV
	@Override
	public IUVChiamanteEsternoResponse getIUVChiamanteEsterno(IUVChiamanteEsternoRequest request) throws DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
        try {
        	LOG.debug("[" + CLASS_NAME + "::getIUVChiamanteEsterno] BEGIN " + request);
//        	setMapStatusCodeMessage(EPAY_MAP_STATUS_CODE_MESSAGE);
        	
        	String url = "/getIUVChiamanteEsterno";
        	ResponseUUID<IUVChiamanteEsternoResponse> response = postJson(url, IUVChiamanteEsternoResponse.class, request);
        	LOG.debug("[" + CLASS_NAME + "::getIUVChiamanteEsterno] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getIUVChiamanteEsterno] DAOException " + daoe.getMessage() + " for request: " + request);
			errore = "DAOException";
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getIUVChiamanteEsterno] Errore generico servizio getIUVChiamanteEsterno", e);
			errore = "Exception";
			throw new DAOException("Errore generico servizio getIUVChiamanteEsterno");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::getIUVChiamanteEsterno] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
        }
	}

	@Override
	public PagamentoIUVResponse pagamentoIUV(PagamentoIUVRequest request) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::pagamentoIUV] BEGIN " + request);
//        	setMapStatusCodeMessage(EPAY_MAP_STATUS_CODE_MESSAGE);
        	
        	String url = "/pagamentoIUV";
        	ResponseUUID<PagamentoIUVResponse> response = postJson(url, PagamentoIUVResponse.class, request);
        	LOG.debug("[" + CLASS_NAME + "::pagamentoIUV] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::pagamentoIUV] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::pagamentoIUV] Errore generico servizio pagamentoIUV", e);
			throw new DAOException("Errore generico servizio pagamentoIUV");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("pagamentoIUV() in " + sec + " milliseconds.");
            LOG.debug("[" + CLASS_NAME + "::pagamentoIUV] END");
        }
	}
	

	@Override
	public GetRtResponse getRT(GetRtRequest request) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::getRT] BEGIN " + request);
//        	setMapStatusCodeMessage(EPAY_MAP_STATUS_CODE_MESSAGE);
        	
        	String url = "/getRT";
        	ResponseUUID<GetRtResponse> response = postJson(url, GetRtResponse.class, request);
        	LOG.debug("[" + CLASS_NAME + "::getRT] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getRT] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRT] Errore generico servizio getRT", e);
			throw new DAOException("Errore generico servizio getRT");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("getRT() in " + sec + " milliseconds.");
            LOG.debug("[" + CLASS_NAME + "::getRT] END");
        }
	}
	
	
	
	//
	// OrganizationsApi
	public DebtPositionReference createDebtPosition(String organization, String paymentType, DebtPositionData debtPositionData) {
		throw new BusinessException("TODO");
	}
	public void deleteDebtPositions(String organization, String paymentType, String iuv) {
		throw new BusinessException("TODO");
	}
	public PaymentDataResult getDebtPositionData(String organization, String paymentType, String iuv) {
		throw new BusinessException("TODO");		
	}
	public Status getDebtPositionStatus(String organization, String paymentType, String iuv) {
		throw new BusinessException("TODO");		
	}
	public PaymentNotice getPaymentNotice(String organization, String paymentType, String iuv) {
		 long start = System.currentTimeMillis();
	        String errore = "";
	        try {
	        	LOG.debug("[" + CLASS_NAME + "::getPaymentNotice] BEGIN organization="+organization+", paymentType="+paymentType+", iuv="+iuv);	        	
	        	String url = "/organizations/"+organization+"/paymenttypes/"+paymentType+"/debtpositions/"+iuv+"/paymentnotice";
	        	ResponseUUID<PaymentNotice> response = getJson(url, PaymentNotice.class);
	        	LOG.debug("[" + CLASS_NAME + "::getPaymentNotice] 200 :: " + response.getResponse());
	    	    return response.getResponse();
	        } catch(DAOException daoe) {
				LOG.warn("[" + CLASS_NAME + "::getPaymentNotice] DAOException " + daoe.getMessage() + " for request organization="+organization+", paymentType="+paymentType+", iuv="+iuv);
				errore = "DAOException";
				throw daoe;
		    } catch(Exception e) {
				LOG.error("[" + CLASS_NAME + "::getPaymentNotice] Errore generico servizio getPaymentNotice", e);
				errore = "Exception";
				throw new DAOException("Errore generico servizio getPaymentNotice");
		    } finally {
	            long end = System.currentTimeMillis();
	            float sec = (end - start);
	            LOG.info("[" + CLASS_NAME + "::getPaymentNotice] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
	        }	
	}
	public PaymentReferences getPaymentUrl(String organization, String paymentType, PaymentData paymentData) {
		throw new BusinessException("TODO");		
	}
	
}
