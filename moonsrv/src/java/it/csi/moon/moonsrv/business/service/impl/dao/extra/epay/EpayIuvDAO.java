/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.epay;

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
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * EPAY via servizi REST diretto (no API Manager)
 *
 * @author Laurent Pissard
 */
public interface EpayIuvDAO {

	public IUVChiamanteEsternoResponse getIUVChiamanteEsterno(IUVChiamanteEsternoRequest request) throws DAOException;
	public PagamentoIUVResponse pagamentoIUV(PagamentoIUVRequest request) throws DAOException;
	public GetRtResponse getRT(GetRtRequest request) throws DAOException;

	//
	// OrganizationsApi
	public DebtPositionReference createDebtPosition(String organization, String paymentType, DebtPositionData debtPositionData) throws DAOException;
	public void deleteDebtPositions(String organization, String paymentType, String iuv) throws DAOException;
	public PaymentDataResult getDebtPositionData(String organization, String paymentType, String iuv) throws DAOException;
	public Status getDebtPositionStatus(String organization, String paymentType, String iuv) throws DAOException;
	public PaymentNotice getPaymentNotice(String organization, String paymentType, String iuv) throws DAOException;
	public PaymentReferences getPaymentUrl(String organization, String paymentType, PaymentData paymentData) throws DAOException;

}
