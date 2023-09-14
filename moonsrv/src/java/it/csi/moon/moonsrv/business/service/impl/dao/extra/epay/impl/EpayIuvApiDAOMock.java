/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.epay.impl;

import java.util.UUID;

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

/**
 * EPAY via servizi REST MOCK
 * 
 * @author Laurent Pissard
 */
@Qualifier("mock")
@Component
public class EpayIuvApiDAOMock extends ApiRestTemplateImpl implements EpayIuvDAO {
	
	private static final String CLASS_NAME = "EpayIuvApiDAOMock";

	//
	// Documenti
	@Override
	public IUVChiamanteEsternoResponse getIUVChiamanteEsterno(IUVChiamanteEsternoRequest request) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::getIUVChiamanteEsterno] BEGIN " + request);
        	
        	String url = "/getIUVChiamanteEsterno";
        	ResponseUUID<IUVChiamanteEsternoResponse> response = makeFakeIUVChiamanteEsternoResponse(request);
        	LOG.debug("[" + CLASS_NAME + "::getIUVChiamanteEsterno] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getIUVChiamanteEsterno] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getIUVChiamanteEsterno] Errore generico servizio getIUVChiamanteEsterno", e);
			throw new DAOException("Errore generico servizio getIUVChiamanteEsterno");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("getIUVChiamanteEsterno() in " + sec + " milliseconds.");
            LOG.debug("[" + CLASS_NAME + "::getIUVChiamanteEsterno] END");
        }
	}

	private ResponseUUID<IUVChiamanteEsternoResponse> makeFakeIUVChiamanteEsternoResponse(IUVChiamanteEsternoRequest request) {
		ResponseUUID<IUVChiamanteEsternoResponse> responseUUID = new ResponseUUID<>();
		responseUUID.setUuid(UUID.randomUUID());
		IUVChiamanteEsternoResponse response = new IUVChiamanteEsternoResponse();
		response.setIdentificativoPagamento(request.getIdentificativoPagamento());
		response.setCodiceEsito("000");
		response.setDescrizioneEsito("FAKE OK");
		String uid8 = UUID.randomUUID().toString().substring(0, 8);
		response.setCodiceAvviso("AVV-" + uid8);
		response.setIuv("IUV-" + uid8);
		responseUUID.setResponse(response);
		return responseUUID;
	}

	@Override
	public PagamentoIUVResponse pagamentoIUV(PagamentoIUVRequest request) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::pagamentoIUV] BEGIN " + request);
        	
        	String url = "/getIUVChiamanteEsterno";
        	ResponseUUID<PagamentoIUVResponse> response = makeFakePagamentoIUVResponse(request);
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

	private ResponseUUID<PagamentoIUVResponse> makeFakePagamentoIUVResponse(PagamentoIUVRequest request) {
		ResponseUUID<PagamentoIUVResponse> responseUUID = new ResponseUUID<>();
		responseUUID.setUuid(UUID.randomUUID());
		PagamentoIUVResponse response = new PagamentoIUVResponse();
		response.setIdentificativoPagamento(request.getIdentificativoPagamento());
		response.setCodiceEsito("000");
		response.setDescrizioneEsito("FAKE OK");
		String uid8 = UUID.randomUUID().toString().substring(0, 8);
		response.setCodiceAvviso("AVV-" + uid8);
		response.setIuv("IUV-" + uid8);
		response.setUrlWisp("https://acardste.vaservices.eu/wallet/welcome?idSession="+responseUUID.getUuid());
		responseUUID.setResponse(response);
		return responseUUID;
	}

	@Override
	public GetRtResponse getRT(GetRtRequest request) throws DAOException {
		// TODO Auto-generated method stub
		return null;
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
		throw new BusinessException("TODO");		
	}
	public PaymentReferences getPaymentUrl(String organization, String paymentType, PaymentData paymentData) {
		throw new BusinessException("TODO");		
	}
}
