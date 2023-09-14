/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.jms.ConnectionFactory;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.EpayFakeNotifia;
import it.csi.moon.commons.dto.VerificaPagamento;
import it.csi.moon.commons.dto.extra.epay.GetRtRequest;
import it.csi.moon.commons.dto.extra.epay.GetRtResponse;
import it.csi.moon.commons.dto.extra.epay.IUVChiamanteEsternoRequest;
import it.csi.moon.commons.entity.EpayNotificaPagamentoEntity;
import it.csi.moon.commons.entity.EpayRichiestaEntity;
import it.csi.moon.commons.util.MoonJmsTypes;
import it.csi.moon.moonsrv.business.be.EpayApi;
import it.csi.moon.moonsrv.business.service.epay.EpayService;
import it.csi.moon.moonsrv.business.service.epay.EpayServiceCallback;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonsrv.business.ws.epay.type.CorpoNotifichePagamentoType;
import it.csi.moon.moonsrv.business.ws.epay.type.CorpoNotifichePagamentoType.ElencoNotifichePagamento;
import it.csi.moon.moonsrv.business.ws.epay.type.DatiTransazionePSPType;
import it.csi.moon.moonsrv.business.ws.epay.type.NotificaPagamentoType;
import it.csi.moon.moonsrv.business.ws.epay.type.ResponseType;
import it.csi.moon.moonsrv.business.ws.epay.type.TestataNotifichePagamentoType;
import it.csi.moon.moonsrv.business.ws.epay.type.TrasmettiNotifichePagamentoRequest;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Profile("epay")
@Component
public class EpayApiImpl extends MoonBaseApiImpl implements EpayApi {

	private static final String CLASS_NAME = "EpayTApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private ObjectMapper mapper;
	
	@Autowired
	private ConnectionFactory jbmCachingConnectionFactory;
	@Autowired
	private JmsTemplate jmsTemplate;
	//
	@Autowired
	private EpayServiceCallback epayServiceCallback;
	@Autowired
	private EpayRichiestaDAO epayRichiestaDAO;
	@Autowired
	private EpayService epayService;

	public EpayApiImpl() {
		super();
		LOG.info("[" + CLASS_NAME + "::EpayApiImpl] EpayApiImpl actived. @Profile(\"epay\") presente.");
//		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	// FOR TEST
	@Override
	public Response notifyToFrontOfficeViaJMSTopic(String iuv,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.info("[" + CLASS_NAME + "::notifyToFrontOfficeViaJMSTopic] iuv = " + iuv);
		EpayNotificaPagamentoEntity npE = new EpayNotificaPagamentoEntity();
		npE.setIuv(iuv);
		npE.setAnnoDiRiferimento(2021);
		npE.setCodiceAvviso("AAASSS");
		npE.setDataEsitoPagamento(new Date());
		npE.setDescCausaleVersamento("Test di pagamento");
		npE.setImportoPagato(BigDecimal.valueOf(100.34) );
		npE.setIdNotificaPagamento(100L);
		try {
			sendMessage(npE);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::notifyToFrontOfficeViaJMSTopic] Errore sendmessage" + e.getMessage());
		}
		if (jbmCachingConnectionFactory != null) {
			return Response.ok().build();
		}
		return Response.accepted().build();
	}

	private void sendMessage(EpayNotificaPagamentoEntity npe) throws Exception {
    	LOG.info("[" + CLASS_NAME + "::sendMessage] IUV = " + npe.getIuv());
		jmsTemplate.setPubSubDomain(true);
		String payload = getMapper().writeValueAsString(npe);
		jmsTemplate.convertAndSend(getNotificaPagamentoJmsName(), payload, msg -> {
			msg.setStringProperty("iuv", npe.getIuv());
			msg.setJMSType(MoonJmsTypes.EPAY_NOTIFICATION);
			return msg;
		});
	}

	private ObjectMapper getMapper() {
		if (mapper==null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}
	
	private static String getNotificaPagamentoJmsName() {
		return EnvProperties.readFromFile(EnvProperties.MOONFO_JMS_NAME);
	}

	
	//
	// FOR TEST
	@Override
	public Response salvaFakeNotificaFromPpay( EpayFakeNotifia body,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			EpayRichiestaEntity richiesta = null;
			IUVChiamanteEsternoRequest richiestaDettaglio = null;
			if (body.getIdIstanza()!=null) {
				richiesta = epayRichiestaDAO.findLastValideByIdIstanza(body.getIdIstanza());
				richiestaDettaglio = getMapper().readValue(richiesta.getRichiesta(), IUVChiamanteEsternoRequest.class);
//				JsonNode richiestaDettaglio = objectMapper.readValue(richiesta.getRichiesta(), JsonNode.class);
			}
			ResponseType resp = epayServiceCallback.riceviNotificaPagamento(buildNotifichePagamento(body, richiesta, richiestaDettaglio));
			return Response.ok(resp).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::salvaFakeNotificaFromPpay] Errore servizio salvaFakeNotificaFromPpay",e);
			throw new ServiceException("Errore servizio POST salvaFakeNotificaFromPpay");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::salvaFakeNotificaFromPpay] Errore generico servizio salvaFakeNotificaFromPpay",ex);
			throw new ServiceException("Errore generico servizio POST salvaFakeNotificaFromPpay");
		}
	}

	private TrasmettiNotifichePagamentoRequest buildNotifichePagamento(EpayFakeNotifia body, EpayRichiestaEntity richiesta, IUVChiamanteEsternoRequest richiestaDettaglio) {
		TrasmettiNotifichePagamentoRequest result = new TrasmettiNotifichePagamentoRequest();
		result.setTestata(buildTestata(body, richiesta, richiestaDettaglio));
		result.setCorpoNotifichePagamento(buildCorpo(body, richiesta, richiestaDettaglio));
		return result ;
	}

	private TestataNotifichePagamentoType buildTestata(EpayFakeNotifia body, EpayRichiestaEntity richiesta, IUVChiamanteEsternoRequest richiestaDettaglio) {
		TestataNotifichePagamentoType result = new TestataNotifichePagamentoType();
		result.setCFEnteCreditore(richiestaDettaglio.getCodiceFiscaleEnte());
		result.setCodiceVersamento(richiestaDettaglio.getTipoPagamento());
		result.setNumeroPagamenti(1);
		result.setIdMessaggio(generateFakeIdMessaggio());
		result.setImportoTotalePagamenti(richiestaDettaglio.getImporto());
		result.setPagamentiSpontanei(false);
		return result;
	}

	private String generateFakeIdMessaggio() {
		return "FAKE_" + Instant.now();
	}

	private CorpoNotifichePagamentoType buildCorpo(EpayFakeNotifia body, EpayRichiestaEntity richiesta, IUVChiamanteEsternoRequest richiestaDettaglio) {
		CorpoNotifichePagamentoType result = new CorpoNotifichePagamentoType();
		ElencoNotifichePagamento elenco = new ElencoNotifichePagamento();
		NotificaPagamentoType notificaPagamento = new NotificaPagamentoType();
		notificaPagamento.setCodiceAvviso(richiesta.getCodiceAvviso());
		notificaPagamento.setIUV(richiesta.getIuv());
		notificaPagamento.setImportoPagato(richiestaDettaglio.getImporto());
		notificaPagamento.setDataEsitoPagamento(generateXMLGregorianCalendar());
		notificaPagamento.setNote("FAKE");
		notificaPagamento.setDescrizioneCausaleVersamento(richiestaDettaglio.getCausale());
		DatiTransazionePSPType datiTransazionePSP = new DatiTransazionePSPType();
		datiTransazionePSP.setIdPSP("FAKE");
		datiTransazionePSP.setRagioneSocialePSP("Nexi");
		datiTransazionePSP.setDataOraAvvioTransazione(null);
		datiTransazionePSP.setImportoTransato(richiestaDettaglio.getImporto());
		datiTransazionePSP.setImportoCommissioni(BigDecimal.valueOf(0));
		notificaPagamento.setDatiTransazionePSP(datiTransazionePSP);
		elenco.getNotificaPagamento().add(notificaPagamento);
		result.setElencoNotifichePagamento(elenco);
		return result;
	}

	private XMLGregorianCalendar generateXMLGregorianCalendar() {
	    XMLGregorianCalendar xgc = null;
		try {
		    GregorianCalendar gc = new GregorianCalendar();
		    gc.setTime(new Date());
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			LOG.error("[" + CLASS_NAME + "::generateXMLGregorianCalendar] Errore generico servizio generateXMLGregorianCalendar", e);
			throw new ServiceException("Errore generico servizio generateXMLGregorianCalendar");
		}
	    return xgc;
	}

	//
	// Prova getRT
	@Override
	public Response getRT(GetRtRequest body,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			GetRtResponse responseRT = null;
			if (body != null && body.getIuv() != null) {
				responseRT = epayService.getRT(body);
			}
			return Response.ok(responseRT).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getRT] Errore servizio getRT", e);
			throw new ServiceException("Errore servizio getRT");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getRT] Errore generico servizio getRT", ex);
			throw new ServiceException("Errore generico servizio getRT");
		}
	}
	
	//
	//
	@Override
	public Response getVerificaPagamento( String idEpay,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			VerificaPagamento response = epayService.verificaPagamento(idEpay);
			return Response.ok(response).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getVerificaPagamento] Errore servizio getVerificaPagamento", e);
			throw new ServiceException("Errore servizio getVerificaPagamento");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getVerificaPagamento] Errore generico servizio getVerificaPagamento", ex);
			throw new ServiceException("Errore generico servizio getVerificaPagamento");
		}
	}

	//
	//OrganizationsApi
	@Override
	public Response getNotificaPagamento(String iuv, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			byte[] ris = epayService.notificaPagamento(iuv);
			return Response.ok(ris).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getNotificaPagamento] Errore servizio getNotificaPagamento", e);
			throw new ServiceException("Errore servizio getNotificaPagamento");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getNotificaPagamento] Errore generico servizio getNotificaPagamento", ex);
			throw new ServiceException("Errore generico servizio getNotificaPagamento");
		}
	}

	@Override
	public Response getDebtPositionData(String iuv, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}
}
