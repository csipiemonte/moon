/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.api.FruitoreIstanza;
import it.csi.moon.commons.dto.api.FruitoreModuloVersione;
import it.csi.moon.commons.dto.api.StatoAcquisizioneRequest;
import it.csi.moon.moonsrv.business.be.ApiTest;
import it.csi.moon.moonsrv.business.service.impl.dao.ApiTestApimintDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.FormatBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnauthorizedException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ApiTestImpl extends MoonBaseApiImpl implements ApiTest {

	private static final String CLASS_NAME = "ApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String X_REQUEST_ID_HEADER = "X-Request-Id";

	@Autowired
	ApiTestApimintDAO apiTestApimintDAO;

	@Override
	public Response getIstanze(String codiceModulo, String stato, 
		String versioneModulo, String codiceEnte,
		Date dataDa, Date dataA,
		boolean test,
		String apimintUrl,
		String clientProfile,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN stato: " + stato);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN dataA: " + dataA);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN test: " + test);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN apimintUrl: " + apimintUrl);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN clientProfile: " + clientProfile);
			}
			// validazione dati
			codiceModulo = validaStringCodeRequired(codiceModulo);
			stato = validaStringCodeRequired(stato);
			
			List<String> result = apiTestApimintDAO.getIstanze(codiceModulo, stato, test, apimintUrl, clientProfile);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] OUT " + result);
			}
			return Response.ok(result).build();
			
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();			
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore di sicurezza getIstanze", ex);
			throw new UnauthorizedException();			
		} catch (FormatBusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore formato data non corretto", e);
			throw new ServiceException("Errore formato data non corretto");
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze", e);
			throw new ServiceException("Errore servizio elenco Istanze");	
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio getIstanze", ex);
			throw new ServiceException("Errore generico servizio elenco Istanze");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] END");
			}
		}
	}
	
	
	
	@Override
	public Response getIstanza(String codice, 
		String apimintUrl,
		String clientProfile, String xRequestId, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanza] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN codice: " + codice);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN apimintUrl: " + apimintUrl);
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codice = validaStringCodeRequired(codice);
			
			FruitoreIstanza result = apiTestApimintDAO.getIstanza(codice, apimintUrl, clientProfile);

			return Response.ok(result).header(X_REQUEST_ID_HEADER, xRequestId).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore di sicurezza getIstanza", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore servizio getIstanza", e);
			throw new ServiceException("Errore servizio getIstanza");
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore servizio getIstanza");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore generico servizio getIstanza", ex);
			throw new ServiceException("Errore generico servizio getIstanza");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanza] END");
			}
		}
	}
	
	
	@Override
	public Response postStatoAcquisizione(String codiceIstanza, String codiceAzione, 
		String apimintUrl,
		StatoAcquisizioneRequest body, 
		String clientProfile, String xRequestId, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN codiceIstanza: " + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN codiceAzione: " + codiceAzione);
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN apimintUrl: " + apimintUrl);
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN body: " + body);
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codiceIstanza = validaStringCodeRequired(codiceIstanza);
			codiceAzione = validaStringCodeRequired(codiceAzione);
			body.setCodice(validaStringCodeRequired(body.getCodice()));
			body.setDescrizione(validaStringRequired(body.getDescrizione()));

			String result = apiTestApimintDAO.updateStatoAcquisizione(codiceIstanza, codiceAzione, apimintUrl, body, clientProfile);

			return Response.ok(result).header(X_REQUEST_ID_HEADER, xRequestId).build();

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postStatoAcquisizione] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::postStatoAcquisizione] Errore di sicurezza postStatoAcquisizione", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::postStatoAcquisizione] Errore servizio postStatoAcquisizione", e);
			throw new ServiceException("Errore servizio postStatoAcquisizione");
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::postStatoAcquisizione] Errore servizio postStatoAcquisizione");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postStatoAcquisizione] Errore generico servizio postStatoAcquisizione",
					ex);
			throw new ServiceException("Errore generico servizio postStatoAcquisizione");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] END");
			}
		}
	}


	@Override
	public Response getModuli(String codiceModulo, String versioneModulo, 
			String oggettoModulo, String descrizioneModulo,
			String stato, String conPresenzaIstanzeUtente, String nomePortale,
			String codiceEnte, String codiceArea, String codiceAmbito, 
			String apimintUrl,
			String clientProfile, String xRequestId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN stato: " + stato);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN conPresenzaIstanzeUtente: " + conPresenzaIstanzeUtente);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN nomePortale: " + nomePortale);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceArea: " + codiceArea);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceAmbito: " + codiceAmbito);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN apimintUrl: " + apimintUrl);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN xRequestId: " + xRequestId);
			}
			Map<String, String> queryParams = null;
			queryParams = addQueryParamsIfPresent(queryParams, "codice_modulo", codiceModulo);
			queryParams = addQueryParamsIfPresent(queryParams, "versione_modulo", versioneModulo);
			queryParams = addQueryParamsIfPresent(queryParams, "stato", stato);
			queryParams = addQueryParamsIfPresent(queryParams, "oggetto_modulo", oggettoModulo);
			queryParams = addQueryParamsIfPresent(queryParams, "descrizione_modulo", descrizioneModulo);
			queryParams = addQueryParamsIfPresent(queryParams, "con_presenza_istanze_utente", conPresenzaIstanzeUtente);
			queryParams = addQueryParamsIfPresent(queryParams, "nome_portale", nomePortale);
			queryParams = addQueryParamsIfPresent(queryParams, "codice_ente", codiceEnte);
			queryParams = addQueryParamsIfPresent(queryParams, "codice_area", codiceArea);
			queryParams = addQueryParamsIfPresent(queryParams, "codice_ambito", codiceAmbito);
			List<FruitoreModuloVersione> result = apiTestApimintDAO.getModuli(queryParams, apimintUrl, clientProfile);

			return Response.ok(result).header(X_REQUEST_ID_HEADER, xRequestId).build();

		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore di sicurezza getModuli", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore servizio getModuli", e);
			throw new ServiceException("Errore servizio getModuli");
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore servizio getModuli");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore generico servizio getModuli", ex);
			throw new ServiceException("Errore generico servizio getModuli");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] END");
			}
		}
	}


	private Map<String, String> addQueryParamsIfPresent(Map<String, String> queryParams, String key, String value) {
		if (StringUtils.isEmpty(value)) {
			return queryParams;
		}
		if (queryParams==null) {
			queryParams = new HashMap<>();
		}
		queryParams.put(key, value);
		return queryParams;
	}


	@Override
	public Response getModulo(String codiceModulo, String versioneModulo, String fields,
			String apimintUrl,
			String clientProfile, String xRequestId, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModulo] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN fields: " + fields);
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN apimintUrl: " + apimintUrl);
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codiceModulo = validaStringCodeRequired(codiceModulo);
			versioneModulo = validaStringRequired(versioneModulo);
			FruitoreModuloVersione result = apiTestApimintDAO.getModulo(codiceModulo, versioneModulo, apimintUrl, clientProfile);

			return Response.ok(result).header(X_REQUEST_ID_HEADER, xRequestId).build();

		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getModulo] Errore di sicurezza getModulo", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getModulo] Errore servizio getModulo", e);
			throw new ServiceException("Errore servizio getModulo");
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getModulo] Errore servizio getModulo");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getModulo] Errore generico servizio getModulo", ex);
			throw new ServiceException("Errore generico servizio getModulo");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModulo] END");
			}
		}
	}
	

}
