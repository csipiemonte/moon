/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.dto.ResponsePaginated;
import it.csi.moon.commons.dto.api.FruitoreIstanza;
import it.csi.moon.commons.dto.api.FruitoreModuloVersione;
import it.csi.moon.commons.dto.api.StatoAcquisizioneRequest;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonsrv.business.be.Api;
import it.csi.moon.moonsrv.business.service.ApiService;
import it.csi.moon.moonsrv.business.service.mapper.report.ReportMapperFactory.CODICI_TIPO_ESTRAZIONE;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.FormatBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnauthorizedException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.HttpRequestUtils;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ApiImpl extends MoonBaseApiImpl implements Api {

	private static final String CLASS_NAME = "ApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String X_REQUEST_ID_HEADER = "X-Request-Id";
	private static final String EXPIRES_HEADER = "Expires";
	private static final String PRAGMA_HEADER = "Pragma";
	private static final String CACHE_CONTROL_HEADER = "Cache-Control";
	
	@Autowired
	ApiService apiService;

	@Override
	public Response getIstanze(String codiceModulo, String stato, String versioneModulo,
		String codiceEnte, String codiceAmbito, 
    	String identificativoUtente,
		Date dataDa, Date dataA,
		boolean test,
		String clientProfile, String xRequestId, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN stato: " + stato);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceAmbito: " + codiceAmbito);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN identificativoUtente: " + identificativoUtente);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN dataA: " + dataA);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN test: " + test);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			//codiceModulo = validaStringCode(codiceModulo); // Spostato obbligatorieta secondo conf Fruitore
			stato = validaStringCode(stato); // Spostato obbligatorieta secondo conf Fruitore
			HttpRequestUtils.logInfoAllHeaders(httpRequest);
			List<String> result = apiService.getIstanze(codiceModulo, stato, versioneModulo, codiceEnte, codiceAmbito, identificativoUtente, dataDa, dataA, test, clientProfile, xRequestId);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] OUT " + result);
			}
			return Response.ok(result).header(X_REQUEST_ID_HEADER, xRequestId).build();
			
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();			
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore di sicurezza getIstanze", ex);
			throw new UnauthorizedException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze", be);
			throw new ServiceException(be);	
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
	public Response getIstanzeOrdinate(String codiceModulo, String stato, String versioneModulo, String codiceEnte,
			String codiceAmbito, String identificativoUtente, Date dataDa, Date dataA, String ordinamento, boolean test,
			String clientProfile, String xRequestId, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN stato: " + stato);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN codiceAmbito: " + codiceAmbito);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN identificativoUtente: " + identificativoUtente);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN dataA: " + dataA);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN ordinamento: " + ordinamento);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN test: " + test);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			//codiceModulo = validaStringCode(codiceModulo); // Spostato obbligatorieta secondo conf Fruitore
			stato = validaStringCode(stato); // Spostato obbligatorieta secondo conf Fruitore
			HttpRequestUtils.logInfoAllHeaders(httpRequest);
			List<FruitoreIstanza> result = apiService.getIstanze(codiceModulo, stato, versioneModulo, codiceEnte, codiceAmbito, identificativoUtente, dataDa, dataA, ordinamento, test, clientProfile, xRequestId);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] OUT " + result);
			}
			return Response.ok(result).header(X_REQUEST_ID_HEADER, xRequestId).build();
			
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeOrdinate] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();			
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeOrdinate] Errore di sicurezza getIstanze", ex);
			throw new UnauthorizedException();			
		} catch (FormatBusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeOrdinate] Errore formato data non corretto", e);
			throw new ServiceException("Errore formato data non corretto");
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeOrdinate] Errore servizio getIstanze", be);
			throw new ServiceException(be);	
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeOrdinate] Errore servizio getIstanze");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeOrdinate] Errore generico servizio getIstanzeOrdinate", ex);
			throw new ServiceException("Errore generico servizio elenco Istanze ordinate");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzeOrdinate] END");
			}
		}
	}

	
	@Override
	public Response getIstanzePaginate(String codiceModulo, String stato, String versioneModulo,
		String codiceEnte, String codiceAmbito, 
    	String identificativoUtente,
		Date dataDa, Date dataA, 
		boolean test,
		String offsetQP, String limitQP,
		String clientProfile, String xRequestId, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN stato: " + stato);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN codiceAmbito: " + codiceAmbito);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN identificativoUtente: " + identificativoUtente);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN dataA: " + dataA);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN test: " + test);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN offset: " + offsetQP);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN limit: " + limitQP);				
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codiceModulo = validaStringCode(codiceModulo); // Spostato obbligatorieta secondo conf Fruitore
			stato = validaStringCode(stato); // Spostato obbligatorieta secondo conf Fruitore
			Integer offset = validaInteger0BasedRequired(offsetQP);
			Integer limit = validaInteger1BasedRequired(limitQP);

			List<String> istanzePaginate = apiService.getIstanzePaginate(codiceModulo, stato,versioneModulo, codiceEnte, codiceAmbito, identificativoUtente, dataDa, dataA, test, offset, limit ,clientProfile, xRequestId);
			Integer countIstanzePaginate = apiService.getCountIstanze(codiceModulo, stato,versioneModulo, codiceEnte, codiceAmbito, identificativoUtente, dataDa, dataA, test, clientProfile, xRequestId);
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] OUT " + countIstanzePaginate);
			}
			
			ResponsePaginated<String> result = new ResponsePaginated<>();
			result.setItems(istanzePaginate);
			result.setPage(((Double) Math.floor(offset.doubleValue()/limit.doubleValue())).intValue());
			result.setPageSize(limit);
			result.setTotalElements(countIstanzePaginate);
			result.setTotalPages(((Double) Math.ceil(countIstanzePaginate.doubleValue()/limit.doubleValue())).intValue());			
			
			return Response.ok(result).header(X_REQUEST_ID_HEADER, xRequestId).build();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore di sicurezza getIstanzePaginate", ex);
			throw new UnauthorizedException();			
		} catch (FormatBusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore formato data non corretto", e);
			throw new ServiceException("Errore formato data non corretto");
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore servizio getIstanzePaginate", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore servizio getIstanzePaginate");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore generico servizio getIstanzePaginate", ex);
			throw new ServiceException("Errore generico servizio elenco Istanze");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] END");
			}
		}
	}	
	
	@Override
	public Response getIstanza(String codice, 
		String clientProfile, String xRequestId, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanza] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN codice: " + codice);
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codice = validaStringCodeRequired(codice);
			
			FruitoreIstanza result = apiService.getIstanza(codice, clientProfile, xRequestId);

			return Response.ok(result).header(X_REQUEST_ID_HEADER, xRequestId).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore di sicurezza getIstanza", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore servizio getIstanza", be);
			throw new ServiceException(be);
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
	public Response postStatoAcquisizione(String codiceIstanza, String codiceAzione, StatoAcquisizioneRequest body, 
		String clientProfile, String xRequestId, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN codiceIstanza: " + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN codiceAzione: " + codiceAzione);
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN body: " + body);
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codiceIstanza = validaStringCodeRequired(codiceIstanza);
			codiceAzione = validaStringCodeRequired(codiceAzione);
			body.setCodice(validaStringCodeRequired(body.getCodice()));
			body.setDescrizione(validaStringRequired(body.getDescrizione()));

			String result = apiService.updateStatoAcquisizione(codiceIstanza, codiceAzione, body, clientProfile, xRequestId);
			LOG.info("[" + CLASS_NAME + "::postStatoAcquisizione] istanza " + codiceIstanza + " " + result);
			return Response.ok().header(X_REQUEST_ID_HEADER, xRequestId).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postStatoAcquisizione] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::postStatoAcquisizione] Errore di sicurezza postStatoAcquisizione", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postStatoAcquisizione] Errore servizio postStatoAcquisizione", be);
			throw new ServiceException(be);
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
	public Response postNotifica(String codiceIstanza, StatoAcquisizioneRequest body, 
		String clientProfile, String xRequestId, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postNotifica] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::postNotifica] IN codiceIstanza: " + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::postNotifica] IN body: " + body);
				LOG.debug("[" + CLASS_NAME + "::postNotifica] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::postNotifica] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codiceIstanza = validaStringCodeRequired(codiceIstanza);
			body.setCodice(validaStringCodeRequired(body.getCodice()));
			body.setDescrizione(validaStringRequired(body.getDescrizione()));

			String result = apiService.getNotifica(codiceIstanza, body, clientProfile, xRequestId);
			LOG.info("[" + CLASS_NAME + "::postNotifica] istanza " + codiceIstanza + " " + result);
			return Response.ok().header(X_REQUEST_ID_HEADER, xRequestId).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postNotifica] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::postNotifica] Errore di sicurezza postStatoAcquisizione", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postNotifica] Errore servizio postStatoAcquisizione", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::postNotifica] Errore servizio postStatoAcquisizione");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postNotifica] Errore generico servizio postStatoAcquisizione",
					ex);
			throw new ServiceException("Errore generico servizio postStatoAcquisizione");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postStatoAcquisizione] END");
			}
		}

	}
	
	
	@Override
	public Response getAllegato(String codiceIstanza, String codiceFile, String formioNomeFile, 
		String clientProfile, String xRequestId, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getAllegato] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN codiceIstanza: " + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN codiceFile: " + codiceFile);
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN formIoNameFile: " + formioNomeFile);
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codiceIstanza = validaStringCodeRequired(codiceIstanza);

			Allegato allegato = apiService.getAllegato(codiceIstanza, codiceFile, formioNomeFile, clientProfile, xRequestId);
			String nome = allegato.getNomeFile();
			String tipo = allegato.getContentType();
			byte[] bytes = allegato.getContenuto();
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getAllegato] bytes = "+bytes);
			}
			
	        return Response.ok(bytes)
        		.header(CACHE_CONTROL_HEADER, "no-cache, no-store, must-revalidate")
        		.header(PRAGMA_HEADER, "no-cache")
        		.header(EXPIRES_HEADER, "0")
        		.header(HttpHeaders.CONTENT_TYPE, tipo)
        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+nome)
        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
        		.build();			

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] Errore di sicurezza getAllegato", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] Errore servizio getAllegato", be);
			throw new ServiceException(be);	
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] Errore servizio getAllegato");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] Errore generico servizio getAllegato", ex);
			throw new ServiceException("Errore generico servizio getAllegato");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getAllegato] END");
			}
		}
	}


	@Override
	public Response getIstanzaPdf(String codiceIstanza, 
			String clientProfile, String xRequestId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN codiceIstanza: " + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codiceIstanza = validaStringCodeRequired(codiceIstanza);
				
			byte[] bytes = apiService.getIstanzaPdf(codiceIstanza, clientProfile, xRequestId);
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] bytes = "+bytes);
			}
			
	        return Response.ok(bytes)
	        		.header(CACHE_CONTROL_HEADER, "no-cache, no-store, must-revalidate")
	        		.header(PRAGMA_HEADER, "no-cache")
	        		.header(EXPIRES_HEADER, "0")
	        		.header(HttpHeaders.CONTENT_TYPE, "application/pdf")
	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+codiceIstanza+".pdf")
	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
	        		.build();			

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] Errore di sicurezza getIstanzaPdf", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] Errore servizio getIstanzaPdf", be);
			throw new ServiceException(be);	
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] Errore servizio getIstanzaPdf");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] Errore generico servizio getIstanzaPdf", ex);
			throw new ServiceException("Errore generico servizio getIstanzaPdf");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] END");
			}
		}
	}

    
	public Response getModuli(
			String codiceModulo, String versioneModulo, 
			String oggettoModulo, String descrizioneModulo, 
			String stato, 
			String conPresenzaIstanzeUtente, String nomePortale,
			String codiceEnte, String codiceArea, String codiceAmbito,
			String clientProfile, String xRequestId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException {
		final String methodName = "getModuli";
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceModulo: "+codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN versioneModulo: "+versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN oggettoModulo: "+oggettoModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN descrizioneModulo: "+descrizioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN stato: "+stato);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN conPresenzaIstanzeUtente: "+conPresenzaIstanzeUtente);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN nomePortale: "+nomePortale);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceEnte: "+ codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceArea: "+ codiceArea);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceAmbito: "+ codiceAmbito);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN xRequestId: " + xRequestId);
			}
			ModuliFilter filter = new ModuliFilter();
			DecodificaStatoModulo dStato = validaDecodificaStatoModulo(stato);
			filter.setCodiceModulo(codiceModulo);
			filter.setVersioneModulo(versioneModulo);
			filter.setOggettoModulo(oggettoModulo);
			filter.setDescrizioneModulo(descrizioneModulo);
			filter.setStatoModulo(dStato);
//			filter.setDataEntroIntDiPubblicazione(dataEntroIntDiPubblicazione);
			filter.setConPresenzaIstanzeUser(conPresenzaIstanzeUtente);
			filter.setNomePortale(nomePortale);
			filter.setCodiceEnte(codiceEnte);
			filter.setCodiceArea(codiceArea);
			filter.setCodiceAmbito(codiceAmbito);
			List<FruitoreModuloVersione> elenco = apiService.getElencoModuli(filter, clientProfile, xRequestId);
			//logInfoElencoModuliByFilter(methodName, filter, elenco);
			return Response.ok(elenco).build();
		} catch (ResourceNotFoundException nfe) {
			throw nfe;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getModuli] Errore servizio getModuli" + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::getModuli] Errore servizio getModuli");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore generico servizio getModuli", ex);
			throw new ServiceException("Errore generico servizio elenco moduli");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] END");
			}
		}
	}
	
	
	@Override
    public Response getModulo( String codiceModulo, String versioneModulo, String fields,
			String clientProfile, String xRequestId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModulo] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN fields: " + fields);
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getModulo] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codiceModulo = validaStringCodeRequired(codiceModulo);
			
			FruitoreModuloVersione result = apiService.getModuloVersione(codiceModulo, versioneModulo, fields, clientProfile, xRequestId);

			return Response.ok(result).header(X_REQUEST_ID_HEADER, xRequestId).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModulo] modulo non trovato" + codiceModulo, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getModulo] Errore di sicurezza getModulo", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getModulo] Errore servizio getModulo", be);
			throw new ServiceException(be);
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

	
//	@Override
//	public Response getReport(String codiceModulo, String codice, Date dataDa, Date dataA,
//			String clientProfile, String xRequestId,
//			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//		try {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("[" + CLASS_NAME + "::getReport] BEGIN");
//				LOG.debug("[" + CLASS_NAME + "::getReport] IN codiceModulo: " + codiceModulo);
//				LOG.debug("[" + CLASS_NAME + "::getReport] IN codice: " + codice);
//				LOG.debug("[" + CLASS_NAME + "::getReport] IN dataDa: " + dataDa);
//				LOG.debug("[" + CLASS_NAME + "::getReport] IN dsataA: " + dataA);
//				LOG.debug("[" + CLASS_NAME + "::getReport] IN clientProfile: " + clientProfile);
//				LOG.debug("[" + CLASS_NAME + "::getReport] IN xRequestId: " + xRequestId);
//			}
//			// validazione dati
//			codiceModulo = validaStringCodeRequired(codiceModulo);			
//			//codice
//			codice = "";
//				
//			byte[] bytes = apiService.getReport(codiceModulo, codice, dataDa, dataA, clientProfile, xRequestId);
//						
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("[" + CLASS_NAME + "::getReport] bytes = "+bytes);
//			}
//			
//	        return Response.ok(bytes)
//	        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
//	        		.header("Pragma", "no-cache")
//	        		.header("Expires", "0")
//	        		.header(HttpHeaders.CONTENT_TYPE, "text/csv")
//	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+codiceModulo+".csv")
//	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
//	        		.build();			
//
//		} catch (ItemNotFoundBusinessException notFoundEx) {
//			LOG.error("[" + CLASS_NAME + "::getReport] istanza non trovata", notFoundEx);
//			throw new ResourceNotFoundException();
//		} catch (UnauthorizedBusinessException ex) {
//			LOG.error("[" + CLASS_NAME + "::getReport] Errore di sicurezza getReport", ex);
//			throw new UnauthorizedException();			
//		} catch (BusinessException be) {
//			LOG.error("[" + CLASS_NAME + "::getReport] Errore servizio getReport", be);
//			throw new ServiceException(be);	
//		} catch (UnprocessableEntityException e) {
//			LOG.error("[" + CLASS_NAME + "::getReport] Errore servizio getReport");
//			throw e;			
//		} catch (Exception ex) {
//			LOG.error("[" + CLASS_NAME + "::getReport] Errore generico servizio getReport", ex);
//			throw new ServiceException("Errore generico servizio getReport");
//		} finally {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("[" + CLASS_NAME + "::getReport] END");
//			}
//		}
//	}

	
	@Override
	public Response getReport(String codiceModulo, String codiceEstrazione, Date dataDa, Date dataA,
			String clientProfile, String xRequestId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getReport] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getReport] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN codiceEstrazione: " + codiceEstrazione);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN dsataA: " + dataA);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN xRequestId: " + xRequestId);
			}
			// validazione dati
			codiceModulo = validaStringCodeRequired(codiceModulo);		
			codiceEstrazione = validaStringCodeRequired(codiceEstrazione);	
			
			StreamingOutput stream = apiService.getStreamReport(codiceModulo, codiceEstrazione, dataDa, dataA, clientProfile, xRequestId);
			
			String contentType = null;
			String fileName = null;
			String ext = null;
			
			switch (codiceEstrazione.toUpperCase()) {
			case CODICI_TIPO_ESTRAZIONE.TARI_APERTE:
			case CODICI_TIPO_ESTRAZIONE.TARI_CHIUSE:
				contentType = "text/csv";
                fileName = codiceModulo+"_"+codiceEstrazione;
				ext = "csv";
				break;
			default:
				contentType = "text/csv";
				fileName = codiceModulo;
				ext = "csv";
			}
		    				
	        return Response.ok(stream)
	        		.header(CACHE_CONTROL_HEADER, "no-cache, no-store, must-revalidate")
	        		.header(PRAGMA_HEADER, "no-cache")
	        		.header(EXPIRES_HEADER, "0")
	        		.header(HttpHeaders.CONTENT_TYPE, contentType)
	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+fileName+"."+ext)
	        		.build();			

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getReport] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getReport] Errore di sicurezza getReport", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getReport] Errore servizio getReport", be);
			throw new ServiceException(be);	
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getReport] Errore servizio getReport");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getReport] Errore generico servizio getReport", ex);
			throw new ServiceException("Errore generico servizio getReport");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getReport] END");
			}
		}
	}


	
}
