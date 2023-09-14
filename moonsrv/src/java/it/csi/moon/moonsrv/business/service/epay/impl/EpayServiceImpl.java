/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.epay.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.apirest.epay.v1.dto.PaymentNotice;
import it.csi.moon.commons.dto.CreaIuvResponse;
import it.csi.moon.commons.dto.EPayPagoPAParams;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.VerificaPagamento;
import it.csi.moon.commons.dto.extra.epay.GetRtRequest;
import it.csi.moon.commons.dto.extra.epay.GetRtResponse;
import it.csi.moon.commons.entity.EpayRichiestaEntity;
import it.csi.moon.commons.entity.EpayRichiestaFilter;
import it.csi.moon.commons.mapper.VerificaPagamentoMapper;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.epay.EpayService;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.epay.EpayIuvDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class EpayServiceImpl implements EpayService {

	private static final String CLASS_NAME = "EpayServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	EpayRichiestaDAO epayRichiestaDAO;
	@Autowired
	@Qualifier("applogic")
	EpayIuvDAO epayIuvDAO;
	
	public EpayServiceImpl() {
		super();
		System.out.println("-------------------------------------------------------------------");
		LOG.info("[" + CLASS_NAME + "::EpayServiceImpl] EpayServiceImpl +++++++++++++++++++");
	}
	
	@Override
	public CreaIuvResponse creaIUV(Long idIstanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::creaIUV] IN idIstanza = " + idIstanza);
		return creaIUV(istanzeService.getIstanzaById(idIstanza,"attributiEPAY"));
	}
	
	@Override
	public CreaIuvResponse creaIUV(Istanza istanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::creaIUV] IN istanza = " + istanza);
		return new EpayDelegateFactory().getDelegate(istanza).creaIUV();
	}
	
	@Override
	public CreaIuvResponse pagoPA(Long idIstanza, EPayPagoPAParams pagoPAParams) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::pagoPA] IN idIstanza = " + idIstanza);
		return pagoPA(istanzeService.getIstanzaById(idIstanza,"attributiEPAY"), pagoPAParams);
	}
	
	@Override
	public CreaIuvResponse pagoPA(Istanza istanza, EPayPagoPAParams pagoPAParams) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::pagoPA] IN istanza = " + istanza);
		return new EpayDelegateFactory().getDelegate(istanza).pagoPA(pagoPAParams);
	}

	@Override
	public CreaIuvResponse annullaIUV(Long idIstanza, String iuv) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::annullaIUV] IN idIstanza = " + idIstanza);
		LOG.debug("[" + CLASS_NAME + "::annullaIUV] IN iuv = " + iuv);
		EpayRichiestaFilter filter = new EpayRichiestaFilter();
		filter.setIdIstanza(idIstanza);
		filter.setIuv(iuv);
		filter.setDeleted(Boolean.FALSE);
		List<EpayRichiestaEntity> richieste = epayRichiestaDAO.find(filter);
		if (richieste==null || richieste.isEmpty()) {
			LOG.error("[" + CLASS_NAME + "::annullaIUV] Impossibile annullare il IUV. Richiesta non identificata.");
			throw new BusinessException("Impossibile annullare il IUV. Richiesta non identificata.","MOONSRV-30823");
		}
		if (richieste.size()>1) {
			LOG.error("[" + CLASS_NAME + "::annullaIUV] Impossibile annullare il IUV. Piu di una Richiesta presente.");
			throw new BusinessException("Impossibile annullare il IUV. Piu di una Richiesta presente.","MOONSRV-30824");
		}
		EpayRichiestaEntity richiestaDaAnnullare = richieste.get(0);
		if (richiestaDaAnnullare.getIdNotificaPagamento()!=null) {
			LOG.error("[" + CLASS_NAME + "::annullaIUV] Impossibile annullare il IUV. Risulta presente una Notifica di pagamento.");
			throw new BusinessException("Impossibile annullare il IUV. Risulta presente una Notifica di pagamento.","MOONSRV-30825");
		}
		richiestaDaAnnullare.setDataDel(new Date());
		richiestaDaAnnullare.setAttoreDel(null); // TODO
		epayRichiestaDAO.update(richiestaDaAnnullare);
		return creaIUV(idIstanza);
	}


	@Override
	public GetRtResponse getRT(GetRtRequest request) throws BusinessException {
		try {
			GetRtResponse result = epayIuvDAO.getRT(request);
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::verificaPagamento] BusinessException ");
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::verificaPagamento] Exception ", e);
			throw new BusinessException(e);
		}
	}
	
	
	@Override
	public VerificaPagamento verificaPagamento(String idEpay) throws ItemNotFoundBusinessException, BusinessException {
		try {
			GetRtRequest request = buildGetRtRequest(idEpay);
			GetRtResponse rt = epayIuvDAO.getRT(request);
			VerificaPagamento result = VerificaPagamentoMapper.buildFromRT(rt);
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::verificaPagamento] BusinessException ");
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::verificaPagamento] Exception ", e);
			throw new BusinessException(e);
		}
	}

	protected GetRtRequest buildGetRtRequest(String idEpay) throws BusinessException {
		try {
			EpayRichiestaEntity richiesta = epayRichiestaDAO.findByIdEpay(idEpay);
			GetRtRequest request = new GetRtRequest();
			request.setIdentificativoPagamento(idEpay);
			request.setIuv(richiesta.getIuv());
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jnRichiesta = mapper.readValue(richiesta.getRichiesta(), JsonNode.class);
			request.setCodiceFiscale(jnRichiesta.get("codiceFiscalePartitaIVAPagatore").asText());
			request.setCodiceFiscaleEnte(jnRichiesta.get("codiceFiscaleEnte").asText());
			request.setFormatoRT("XML");
			return request;
		} catch (ItemNotFoundDAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::buildGetRtRequest] ItemNotFoundDAOException ", daoe);
			throw new ItemNotFoundBusinessException(daoe);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::buildGetRtRequest] Exception ", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public String getEpayManagerName(String codiceModulo) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getEpayManagerName] IN codiceModulo: "+codiceModulo);
			}
			if (StringUtils.isEmpty(codiceModulo)) {
				LOG.error("[" + CLASS_NAME + "::getEpayManagerName] CODICE_MODULO mancate.");
				throw new BusinessException("CODICE_MODULO non trovata.","MOONSRV-3XXXX");
			}
			Modulo modulo = new Modulo();
			modulo.setCodiceModulo(codiceModulo);
			Istanza istanzaFake = new Istanza();
			istanzaFake.setModulo(modulo);
			EpayDelegate epay = new EpayDelegateFactory().getDelegate(istanzaFake);
			String response = epay.getClass().getName();
			LOG.info("[" + CLASS_NAME + "::getEpayManagerName] response = " + response);
			return response;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getEpayManagerName] ERROR " + be.getMessage());
			throw be;
		}
	}

	//OrganizationsApi
	//ricevuta avviso pagamento pdf
	@Override
	public byte[] notificaPagamento(String iuv) {
		try {
			EpayRichiestaEntity richiesta = epayRichiestaDAO.findByIuv(iuv);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsnRichiesta = mapper.readValue(richiesta.getRichiesta(), JsonNode.class);
			PaymentNotice paymentNotice = epayIuvDAO.getPaymentNotice(jsnRichiesta.get("codiceFiscaleEnte").asText(), jsnRichiesta.get("tipoPagamento").asText(), iuv);
			return paymentNotice.getPaymentnotice();
		} catch (ItemNotFoundDAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::notificaPagamento] ItemNotFoundDAOException ", daoe);
			throw new ItemNotFoundBusinessException(daoe);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::notificaPagamento] Exception ", e);
			throw new BusinessException(e);
		}
	}
	
	
}
