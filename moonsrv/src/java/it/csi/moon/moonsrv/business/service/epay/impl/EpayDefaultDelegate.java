/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.epay.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.CreaIuvResponse;
import it.csi.moon.commons.dto.EPayPagoPAParams;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ModuloAttributo;
import it.csi.moon.commons.dto.extra.epay.ComponentePagamento;
import it.csi.moon.commons.dto.extra.epay.IUVChiamanteEsternoRequest;
import it.csi.moon.commons.dto.extra.epay.IUVChiamanteEsternoResponse;
import it.csi.moon.commons.dto.extra.epay.PagamentoIUVRequest;
import it.csi.moon.commons.dto.extra.epay.PagamentoIUVResponse;
import it.csi.moon.commons.entity.EpayComponentePagamentoEntity;
import it.csi.moon.commons.entity.EpayRichiestaEntity;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayComponentePagamentoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.epay.EpayIuvDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class EpayDefaultDelegate implements EpayDelegate {

	private static final String CLASS_NAME = "EpayDefaultDelegate";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final BigDecimal IMPORTO_MIN = new BigDecimal("0.01");
	private Istanza istanza;
	private JsonNode conf;
	private StrReplaceHelper strReplaceHelper;
	
	@Autowired
	@Qualifier("applogic")
//	@Qualifier("mock")
	EpayIuvDAO epayIuvDAO;
	@Autowired
	EpayRichiestaDAO epayRichiestaDAO;	
	@Autowired
	EpayComponentePagamentoDAO epayComponentePagamentoDAO;
	
	public EpayDefaultDelegate(Istanza istanza) {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		this.istanza = istanza;
		if (this.istanza!=null && this.istanza.getCodiceIstanza()!=null) { // per getEpayManagerName, viene chiamato con un istanzaFake
			this.conf = retrieveConf(istanza);	// throw new BusinessException("PSIT_EPAY_CONF non trovata nei attributi del modulo","MOONSRV-30801");
												// throw new BusinessException("PSIT_EPAY_CONF non valid JSON","MOONSRV-30802");
			this.strReplaceHelper = new StrReplaceHelper(istanza);
		}
	}
	
	public Istanza getIstanza() {
		return this.istanza;
	}
	public JsonNode getConf() {
		return this.conf;
	}
	
	public CreaIuvResponse creaIUV() throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::creaIUV] BEGIN");
			IUVChiamanteEsternoRequest request = buildIUVChiamanteEsternoRequest();
			validaCreaIuvRequest(request);
			EpayRichiestaEntity richiestaE = buildEpayRichiesta(request);
			IUVChiamanteEsternoResponse creaIuvResponse = epayIuvDAO.getIUVChiamanteEsterno(request);
			if (!"000".equals(creaIuvResponse.getCodiceEsito())) {
				throw new BusinessException(creaIuvResponse.getCodiceEsito()+"-"+creaIuvResponse.getDescrizioneEsito(),"MOONSRV-30800");
			}
			richiestaE = salvaRichiestaConResponse(richiestaE, creaIuvResponse);
			return new CreaIuvResponse(richiestaE.getIdRichiesta(), richiestaE.getCodiceAvviso(), richiestaE.getIuv(), creaIuvResponse.getUrlWisp());
		} catch (DAOException dao) {
			throw new BusinessException(dao);
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			LOG.fatal("[" + CLASS_NAME + "::creaIUV] Exception", e);
			throw new BusinessException("Errore generica di Pagamento","MOONSRV-30800");
		}
	}

	protected IUVChiamanteEsternoRequest buildIUVChiamanteEsternoRequest() {
		IUVChiamanteEsternoRequest result = new IUVChiamanteEsternoRequest();
		result.setIdentificativoPagamento(UUID.randomUUID().toString());
		result.setCausale(retrieveConfValueWithReplaceDinamici(ConfKeys.CAUSALE));
		result.setCodiceFiscaleEnte(trimUpper(retrieveConfValueWithReplaceDinamici(ConfKeys.CODICE_FISCALE_ENTE)));
		result.setCodiceFiscalePartitaIVAPagatore(trimUpper(retrieveConfValueWithReplaceDinamici(ConfKeys.CODICE_FISCALE_PIVA)));
		result.setCognome(trimUpper(retrieveConfValueWithReplaceDinamici(ConfKeys.COGNOME)));
		result.setNome(trimUpper(retrieveConfValueWithReplaceDinamici(ConfKeys.NOME)));
		result.setRagioneSociale(trim(retrieveConfValueWithReplaceDinamici(ConfKeys.RAGIONE_SOCIALE)));
		result.setEmail(trimLower(retrieveConfValueWithReplaceDinamici(ConfKeys.EMAIL)));
		result.setImporto(retrieveImporto(retrieveConfValueWithReplaceDinamici(ConfKeys.IMPORTO)));
		result.setTipoPagamento(trim(retrieveConfValueWithReplaceDinamici(ConfKeys.TIPO_PAGAMENTO)));
		result.setComponentiPagamento(retrieveComponentiPagamento());
		return result;
	}

	
	protected List<ComponentePagamento> retrieveComponentiPagamento() {
		List<EpayComponentePagamentoEntity> componentiPagamento = null;
		try {
			componentiPagamento = epayComponentePagamentoDAO
					.findByIdModulo(this.getIstanza().getModulo().getIdModulo());			
			List<ComponentePagamento> dettaglioImporti = componentiPagamento.stream()
					.map(epayCompPag -> new ComponentePagamento(
							Integer.valueOf(epayCompPag.getOrdine().intValue()),
							epayCompPag.getImporto(), 
							epayCompPag.getCausale(), 
							((epayCompPag.getDatiSpecificiRiscossione()==null)?"":epayCompPag.getDatiSpecificiRiscossione()),
							retreiveAnnoAccertamento(epayCompPag.getAnnoAccertamento()), 
							epayCompPag.getNumeroAccertamento()))
					.collect(Collectors.toList());
           return dettaglioImporti;							
		} catch (EmptyResultDataAccessException e) {
			LOG.debug("[" + CLASS_NAME + "::retrieveComponentiPagamento] pagamenti non presenti: " + e.getMessage(), e);
			return null;
		}
	}

	private String retreiveAnnoAccertamento(String annoAccertamento) {		
		String result = annoAccertamento;
		if (result==null)
			return null;  
		if (result.contains("@@")) {
			result = result.replace("@@ANNO_CORRENTE@@", String.valueOf(LocalDate.now().getYear()));
		}		
		return result;
	}

	private String trim(String str) {
		if (str==null) return null;
		return str.trim();
	}
	private String trimUpper(String str) {
		if (str==null) return null;
		return str.trim().toUpperCase(Locale.ITALY);
	}
	private String trimLower(String str) {
		if (str==null) return null;
		return str.trim().toLowerCase(Locale.ITALY);
	}

	@Override
	public CreaIuvResponse pagoPA(EPayPagoPAParams pagoPAparams) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::pagoPA] IN istanza = " + istanza);
		EpayRichiestaEntity richiestaE = epayRichiestaDAO.findLastValideByIdIstanza(istanza.getIdIstanza());
		PagamentoIUVRequest request = buildPagamentoIUVRequest(richiestaE, pagoPAparams);
		PagamentoIUVResponse response = epayIuvDAO.pagamentoIUV(request);
		return new CreaIuvResponse(richiestaE.getIdRichiesta(), richiestaE.getCodiceAvviso(), request.getIuv(), response.getUrlWisp());
	}

	private PagamentoIUVRequest buildPagamentoIUVRequest(EpayRichiestaEntity richiestaE, EPayPagoPAParams pagoPAparams) {
		PagamentoIUVRequest result = new PagamentoIUVRequest();
		result.setIdentificativoPagamento(richiestaE.getIdEpay());
		result.setCodiceFiscale(retrieveConfValueWithReplaceDinamici(ConfKeys.CODICE_FISCALE_PIVA));
		result.setIuv(richiestaE.getIuv());
		return result;
	}
	
	
	private IUVChiamanteEsternoRequest validaCreaIuvRequest(IUVChiamanteEsternoRequest request) {
		if (StringUtils.isBlank(request.getCodiceFiscaleEnte())) {
			LOG.error("CreaIuvRequest con codiceFiscaleEnte vuoto.");
			throw new BusinessException("CreaIuvRequest con codiceFiscaleEnte vuoto.","MOONSRV-30810");
		}
		if (StringUtils.isBlank(request.getCausale())) {
			LOG.error("CreaIuvRequest con causale vuota.");
			throw new BusinessException("CreaIuvRequest con causale vuota.","MOONSRV-30811");
		}
		if (StringUtils.isBlank(request.getTipoPagamento())) {
			LOG.error("CreaIuvRequest con tipoPagamento vuoto.");
			throw new BusinessException("CreaIuvRequest con tipoPagamento vuoto.","MOONSRV-30812");
		}
		validaImporto(request);
		validaCreaIuvRequestCognomeNome(request);
		validaCodiceFiscalePartitaIVAPagatore(request);
		validaIdentificativoPagamento(request);
		validaEmail(request);
		return request;
	}

	protected void validaImporto(IUVChiamanteEsternoRequest request) {
		if (request.getImporto()==null || request.getImporto().compareTo(IMPORTO_MIN) < 0 ) {
			LOG.error("CreaIuvRequest con importo vuoto, zero o negativo.");
			throw new BusinessException("CreaIuvRequest con importo vuoto, zero o negativo.","MOONSRV-30813");
		}
	}

	protected void validaCreaIuvRequestCognomeNome(IUVChiamanteEsternoRequest request) {
		if (StringUtils.isBlank(request.getRagioneSociale()) && 
				StringUtils.isBlank(request.getNome()) &&
				StringUtils.isBlank(request.getCognome())) {
			LOG.error("CreaIuvRequest con nome,cognome,ragioneSociale vuoti.");
			throw new BusinessException("CreaIuvRequest con nome,cognome,ragioneSociale vuoti.","MOONSRV-30814");
		} else {
			if (!StringUtils.isBlank(request.getNome()) && StringUtils.isBlank(request.getCognome())) {
				LOG.error("CreaIuvRequest con nome, ma con cognome vuoto.");
				throw new BusinessException("CreaIuvRequest con nome, ma con cognome vuoto.","MOONSRV-30815");
			}
			if (StringUtils.isBlank(request.getNome()) && !StringUtils.isBlank(request.getCognome())) {
				LOG.error("CreaIuvRequest con cognome, ma con nome vuoto.");
				throw new BusinessException("CreaIuvRequest con cognome, ma con nome vuoto.","MOONSRV-30816");
			}
			if (!StringUtils.isBlank(request.getNome()) && !StringUtils.isBlank(request.getCognome())) { 
				pruneRagioneSociale(request);
			} else {
				pruneNomeCognome(request);
			}
		}
	}

	protected void validaCodiceFiscalePartitaIVAPagatore(IUVChiamanteEsternoRequest request) {
		if (StringUtils.isBlank(request.getCodiceFiscalePartitaIVAPagatore())) {
			LOG.error("CreaIuvRequest con codiceFiscalePartitaIVAPagatore vuoto.");
			throw new BusinessException("CreaIuvRequest con codiceFiscalePartitaIVAPagatore vuoto.");
		}
	}

	protected void validaIdentificativoPagamento(IUVChiamanteEsternoRequest request) {
		if (StringUtils.isBlank(request.getIdentificativoPagamento())) {
			LOG.error("CreaIuvRequest con identificativoPagamento vuoto.");
			throw new BusinessException("CreaIuvRequest con identificativoPagamento vuoto.");
		}
	}

	protected void validaEmail(IUVChiamanteEsternoRequest request) {
		if (StringUtils.isBlank(request.getEmail())) {
			LOG.error("CreaIuvRequest con email vuoto.");
			throw new BusinessException("CreaIuvRequest con email vuoto.");
		}
	}
	
	private void pruneRagioneSociale(IUVChiamanteEsternoRequest request) {
		request.setRagioneSociale(null);
	}
	private void pruneNomeCognome(IUVChiamanteEsternoRequest request) {
		request.setNome(null);
		request.setCognome(null);
	}

	private EpayRichiestaEntity buildEpayRichiesta(IUVChiamanteEsternoRequest request) throws JsonGenerationException, JsonMappingException, IOException {
		EpayRichiestaEntity result = new EpayRichiestaEntity();
		result.setIdIstanza(this.istanza.getIdIstanza());
		result.setIdModulo(this.istanza.getModulo().getIdModulo());
		result.setIdTipologiaEpay(1); // 1-Istanza
		result.setIdEpay(request.getIdentificativoPagamento());
		result.setDataIns(new Timestamp(System.currentTimeMillis()));
		result.setAttoreIns(request.getCodiceFiscalePartitaIVAPagatore());
		result.setRichiesta(mapper.writeValueAsString(request));
		return result;
	}
	

	private EpayRichiestaEntity salvaRichiestaConResponse(EpayRichiestaEntity richiestaE, IUVChiamanteEsternoResponse creaIuvResponse) throws JsonGenerationException, JsonMappingException, IOException {
		richiestaE.setIuv(creaIuvResponse.getIuv());
		richiestaE.setCodiceAvviso(creaIuvResponse.getCodiceAvviso());
		richiestaE.setRisposta(mapper.writeValueAsString(creaIuvResponse));
		richiestaE.setDataIns(new Timestamp(System.currentTimeMillis()));
		richiestaE.setCodiceEsito(creaIuvResponse.getCodiceEsito());
		richiestaE.setDescEsito(creaIuvResponse.getDescrizioneEsito());
		Long idLogEpay = epayRichiestaDAO.insert(richiestaE);
		richiestaE.setIdRichiesta(idLogEpay);
		return richiestaE;
	}

	
	
	private String retrieveConfValueWithReplaceDinamici(ConfKeys keyConf) {
		return strReplaceHelper.replaceDinamici(retrieveConfTextValue(keyConf), istanza);
	}
	private String retrieveConfTextValue(ConfKeys keyConf) {
		return conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asText():keyConf.getTextDefaultValue();
	}
	private boolean retrieveConfBooleanValue(ConfKeys keyConf) {
		return conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asBoolean():keyConf.getBooleanDefaultValue();
	}
	private BigDecimal retrieveImporto(String importoOrKey) {
	    if (importoOrKey == null) {
	        return null;
	    }
	    BigDecimal result = null;
	    try {
	        result = BigDecimal.valueOf(Double.parseDouble(importoOrKey)).setScale(2, RoundingMode.HALF_UP);
	    } catch (NumberFormatException nfe) {
	    	LOG.error("[" + CLASS_NAME + "::retrieveImporto] NumberFormatException with " + importoOrKey + " for istanza "  + this.istanza.getIdIstanza() + "  " + this.istanza.getCodiceIstanza());
	        throw new BusinessException("Impossible identificare l'importo", "MOONSRV-30803");
	    }
		return result;
	}


	//
	//
	private JsonNode retrieveConf(Istanza istanza) throws BusinessException {
		String strJson = findValueConfInAttributi(istanza.getModulo().getAttributi(), ModuloAttributoKeys.PSIT_EPAY_CONF);
		if(StringUtils.isBlank(strJson)) {
			LOG.error("[" + CLASS_NAME + "::retrieveConf] PSIT_EPAY_CONF non trovata o vuota nei attributi del modulo for idIstanza=" + istanza.getIdIstanza() + "\nmodulo=" + istanza.getModulo());
		    throw new BusinessException("PSIT_EPAY_CONF non trovata o vuota nei attributi del modulo","MOONSRV-30801");
		}
		return readConfJson(strJson);
	}

	private String findValueConfInAttributi(List<ModuloAttributo> attributi, ModuloAttributoKeys maKeyRicercata) {
		if(attributi==null)
			return null;
		return attributi.stream()
			.filter(a -> maKeyRicercata.name().equals(a.getNome()))
			.map(a -> a.getValore())
			.findAny()
			.orElse(null);
	}

	private JsonNode readConfJson(String strJson) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: " + strJson);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//			objectMapper.configure(
//				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readConfJson] IOException " + e.getMessage());
		    throw new BusinessException("PSIT_EPAY_CONF non valid JSON","MOONSRV-30802");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}
	
}
