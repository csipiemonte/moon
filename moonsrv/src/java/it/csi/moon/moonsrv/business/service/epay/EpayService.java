/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.epay;

import it.csi.moon.commons.dto.CreaIuvResponse;
import it.csi.moon.commons.dto.EPayPagoPAParams;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.VerificaPagamento;
import it.csi.moon.commons.dto.extra.epay.GetRtRequest;
import it.csi.moon.commons.dto.extra.epay.GetRtResponse;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

public interface EpayService {

	public CreaIuvResponse creaIUV(Long idIstanza) throws BusinessException;
	public CreaIuvResponse creaIUV(Istanza istanza) throws BusinessException;
	
	public CreaIuvResponse pagoPA(Long idIstanza, EPayPagoPAParams pagoPAParams) throws BusinessException;
	public CreaIuvResponse pagoPA(Istanza istanza, EPayPagoPAParams pagoPAParams) throws BusinessException;
	
	public CreaIuvResponse annullaIUV(Long idIstanza, String iuv) throws BusinessException;
	
	public GetRtResponse getRT(GetRtRequest request) throws BusinessException;
	public VerificaPagamento verificaPagamento(String idEpay) throws ItemNotFoundBusinessException, BusinessException;
	
	public String getEpayManagerName(String codiceModulo) throws BusinessException;
	
	//
	//OrganizationsApi
	public byte[] notificaPagamento(String iuv);
	
}
