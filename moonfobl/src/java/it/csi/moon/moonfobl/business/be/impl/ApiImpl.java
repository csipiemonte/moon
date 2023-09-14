/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.ResponsePaginated;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.commons.entity.IstanzeSorterBuilder;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonfobl.business.be.Api;
import it.csi.moon.moonfobl.business.service.ApiService;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.IstanzeService;
import it.csi.moon.moonfobl.business.service.ModuliService;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonfobl.business.service.login.LoginApiIdpShibbolethImpl;
import it.csi.moon.moonfobl.dto.moonfobl.LoginApiRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.dto.moonfobl.ModuloVersioneStato;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.FormatBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class ApiImpl extends MoonBaseApiImpl implements Api {

	private static final String CLASS_NAME = "ApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final String HEADER_X_REQUEST_ID = "X-Request-Id";
//	private static final int MAX_COOKIE_LENGTH = 4096;
	
	@Autowired
	ApiService apiService;
	@Autowired 
	AuditService auditService;
	@Autowired
	ModuliService moduliService;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	@Qualifier("nocache")
	ModuloAttributiDAO moduloAttributiDAO;
	
	@Autowired
	IstanzeService istanzeService;

	@Override
	public Response postMoonIdentita(String logonMode, String provider, 
			String nomePortale, 
			String identificativoUtente, String codiceFiscale, String cognome, String nome, String email, 
			String idIride, String shibIdentitaJwt, 
			String codiceEnte, String codiceAmbito, String gruppoOperatore, 
			String codiceModulo, String versioneModulo, 
			String clientProfile, String xRequestId, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		LoginResponse loginResponse = null;
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN xRequestId: " + xRequestId);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN nomePortale: " + nomePortale);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN identificativoUtente: " + identificativoUtente);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN codiceFiscale: " + codiceFiscale);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN cognome: " + cognome);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN nome: " + nome);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN email: " + email);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN idIride: " + idIride);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN shibIdentitaJwt: " + shibIdentitaJwt);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN codiceAmbito: " + codiceAmbito);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN gruppoOperatore: " + gruppoOperatore);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] IN versioneModulo: " + versioneModulo);
			}
			LoginApiRequest loginRequest = new LoginApiRequest();
			loginRequest.setClientProfile(validaStringRequiredApi(clientProfile,"client-profile"));
			loginRequest.setxRequestId(validaStringRequiredApi(xRequestId,"X-Request-Id"));
			loginRequest.setLogonMode(validaStringRequiredApi(logonMode,"logon_mode"));
			loginRequest.setProvider(provider);
			loginRequest.setNomePortale(validaStringPortaleName(validaStringRequiredApi(nomePortale,"nome_portale")));
			loginRequest.setCodiceFiscale(identificativoUtente);
			loginRequest.setCognome(cognome);
			loginRequest.setNome(nome);
			loginRequest.setEmail(email);
			loginRequest.setIdIride(idIride);
			loginRequest.setShibIdentitaJwt(shibIdentitaJwt);
			loginRequest.setCodiceEnte(codiceEnte);
			loginRequest.setCodiceAmbito(codiceAmbito);
			loginRequest.setGruppoOperatore(gruppoOperatore);
			loginRequest.setCodiceModulo(codiceModulo);
			loginRequest.setVersioneModulo(versioneModulo);

			// Previsto Factory by logonMode, actuale: API_IDP_SHIB
			loginResponse = new LoginApiIdpShibbolethImpl().login(loginRequest, httpHeaders, httpRequest);
//			httpResponse.addCookie(buildCookie("loginResponse", getLoginResponsePrunedAndEncoded(loginResponse)));
			LOG.debug("[" + CLASS_NAME + "::postMoonIdentita] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
			return Response.ok(loginResponse).header(HEADER_X_REQUEST_ID, xRequestId).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::postMoonIdentita] UnprocessableEntityException " + uee.getMessage());
			throw uee;	
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postMoonIdentita] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException unauthorizedEx) {
			LOG.error("[" + CLASS_NAME + "::postMoonIdentita] UnauthorizedException");
			throw unauthorizedEx;
		} catch (UnauthorizedBusinessException ube) {
			LOG.error("[" + CLASS_NAME + "::postMoonIdentita] UnauthorizedBusinessException");
			throw new UnauthorizedException(ube);
		} catch (ForbiddenException forbidenEx) {
			LOG.error("[" + CLASS_NAME + "::postMoonIdentita] ForbiddenException");
			throw forbidenEx;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postMoonIdentita] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::postMoonIdentita] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
			try {
				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::postMoonIdentita] Errore servizio Audit", e);
			}
		}
	}
	
	private String validaStringRequiredApi(String input, String fieldName) {
		return validaStringRequired(input, fieldName, "MOONFOBL-10900", "Errore generica di gestione API");
	}

	private UserInfo retrieveUserInfoApi(HttpServletRequest httpRequest) throws UnauthorizedBusinessException, BusinessException {
		UserInfo result = retrieveUserInfo(httpRequest);
		result.setApi(true);
		return result;
	}
	
	@Override
	public Response getIstanze(String idTabFoQP, 
			String statoQP, List<String> statiIstanzaQP, String importanzaQP,
			String codiceIstanzaQP, 
			String codiceModuloQP, String versioneModuloQP, String titoloModuloQP,
			Date createdStart, Date createdEnd, 
			String codiceFiscale, String cognome, String nome, 
			String sort,
			boolean test, 
			String moonId, String xRequestId, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			StringBuilder keyOp = new StringBuilder().append("\nIN idTabFo: ").append(idTabFoQP)
					.append("\nIN stato: ").append(statoQP).append("\nIN statiIstanza: ").append(statiIstanzaQP).append("\nIN importanza: ").append(importanzaQP)
					.append("\nIN codiceIstanza: ").append(codiceIstanzaQP)
					.append("\nIN codiceModulo: ").append(codiceModuloQP).append("\nIN versioneModuloQP: ").append(versioneModuloQP).append("\nIN titoloModuloQP: ").append(titoloModuloQP)
					.append("\nIN createdStart: ").append(createdStart).append("\nIN createdEnd: ").append(createdEnd)
					.append("\nIN codiceFiscale: ").append(codiceFiscale).append("\nIN cognome: ").append(cognome).append("\nIN nome: ").append(nome)
					.append("\nIN sort: ").append(sort)
					.append("\nIN test: ").append(test);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] BEGIN" + keyOp.toString());
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN moonId: " + moonId);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN xRequestId: " + xRequestId);
			}
			UserInfo user = retrieveUserInfoApi(httpRequest);
			// validazione dati
			Integer idTabFo = validaInteger(idTabFoQP);
			Integer stato = validaInteger(statoQP);
			Integer importanza = validaInteger(importanzaQP);
			String codiceIstanza = validaStringCode(codiceIstanzaQP);
			String codiceModulo = validaStringCode(codiceModuloQP);
			String versioneModulo = validaStringCode(versioneModuloQP);
			// Filter
			IstanzeFilter filter = new IstanzeFilter();
			filter.setIdTabFo(idTabFo); // 1-Bozze 2-Invate+
			// Qui siamo nel Front-Office, quindi visualizziamo solo quelli dellutente
			// corrente CodiceFiscaleDichiarante
			
			if (user.isOperatore()) {
				// per operatore
				filter.setGruppoOperatoreFo(user.getGruppoOperatoreFo());
				// altri possibili filtri di un operatore
				if (cercaDichiaranteInJson(filter.getIdModulo()))	{
					filter.setCodiceFiscaleJson(codiceFiscale);
					filter.setCognomeJson(cognome);
					filter.setNomeJson(nome);
				} else {
					filter.setCodiceFiscaleDichiarante(codiceFiscale);
					filter.setCognomeDichiarante(cognome);
					filter.setNomeDichiarante(nome);
				}
			} else {
				filter.setIdentificativoUtente(user.getIdentificativoUtente());
			}			
			
			
			filter.setNomePortale(user.getPortalName());
			filter.setIdEnte(user.getEnte().getIdEnte());

			if (statiIstanzaQP != null) {
				filter.setStatiIstanza(statiIstanzaQP.stream().map(Integer::parseInt).collect(Collectors.toList()));
			} else {
				if (stato != null) {
					filter.setStatiIstanza(Arrays.asList(stato));
				}
			}

//			if (stato!=null) {
//				filter.setStatiIstanza(Arrays.asList(stato));
//			}
			filter.setImportanza(importanza);
			filter.setCodiceIstanza(codiceIstanza);
			if (!StringUtils.isBlank(codiceModulo) && !StringUtils.isBlank(versioneModulo)) {
				Modulo m = moduliService.getModuloByCodice(codiceModulo, versioneModulo);
				filter.setIdModulo(m.getIdModulo());
				filter.setIdVersioneModulo(m.getIdVersioneModulo());
			} else {
				if (!StringUtils.isBlank(codiceModulo)) {
					filter.setIdModulo(moduliService.getIdModuloByCodice(codiceModulo));
				}
			}
			filter.setTitoloModulo(titoloModuloQP);
			filter.setCreatedStart(createdStart);
			filter.setCreatedEnd(createdEnd);
			filter.setIdEnte(user.isMultiEntePortale() ? user.getEnte().getIdEnte() : null);
			filter.setIdAmbito(user.getIdAmbito());
			filter.setIdVisibilitaAmbito(
					filter.getIdAmbito().isPresent() ? null : ModuliFilter.VISIBILITA_AMBITO_PUBLIC);

			// Sorter
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			
			//
			auditService.insertSearchIstanze(httpRequest.getRemoteAddr(), user, keyOp.toString());
			List<Istanza> result = apiService.getIstanze(filter, optSorter, user);
			
//			String[] ignorableFieldNames = {"flagArchiviata","riportaInBozzaAbilitato"};
//		    SimpleBeanPropertyFilter foApiFilter = SimpleBeanPropertyFilter
//		    	      .serializeAllExcept(ignorableFieldNames);
//		    FilterProvider filters = new SimpleFilterProvider()
//		          .addFilter("foApiFilter", foApiFilter);
		    
//		    MappingJacksonValue mapping = new MappingJacksonValue(result);
//		    mapping.setFilters(filters);
		    
			return Response.ok(result).header(HEADER_X_REQUEST_ID, xRequestId).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();			
		} catch (UnauthorizedBusinessException ube) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] UnauthorizedBusinessException");
			throw new UnauthorizedException(ube);			
		} catch (FormatBusinessException fbe) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore formato data non corretto", fbe);
			throw new ServiceException(fbe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze", be);
			throw new ServiceException(be);	
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze");
			throw e;			
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio getIstanze", ex);
			throw new ServiceException("Errore generico servizio elenco Istanze");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] END");
			}
		}
	}


//	private Long retrieveIdModulo(String codiceModulo) {
//		try {
//			if (StringUtils.isBlank(codiceModulo)) return null;
//			LOG.debug("[" + CLASS_NAME + "::retreiveModulo] IN codice modulo: " + codiceModulo);
//			return moduloDAO.findByCodice(codiceModulo).getIdModulo();
//		} catch (ItemNotFoundDAOException nfdaoe) {
//			LOG.error("[" + CLASS_NAME + "::retreiveModulo] Modulo not found for " + codiceModulo);
//			throw new ItemNotFoundBusinessException(nfdaoe);
//		} catch (DAOException daoe) {
//			LOG.error("[" + CLASS_NAME + "::retreiveModulo] errore validazione modulo");
//			throw new BusinessException(daoe);
//		}
//	}

	@Override
	public Response getIstanzePaginate(String idTabFoQP, 
			String statoQP, List<String> statiIstanzaQP,
			String importanzaQP, 
			String codiceIstanzaQP, 
			String codiceModuloQP, String versioneModuloQP, String titoloModuloQP, 
			Date createdStart, Date createdEnd,
			String codiceFiscale, String cognome, String nome, 
			String sort, String offsetQP, String limitQP, 
			boolean test,
			String moonId, String xRequestId, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			StringBuilder keyOp = new StringBuilder().append("\nIN idTabFo: ").append(idTabFoQP)
					.append("\nIN stato: ").append(statoQP).append("\nIN statiIstanza: ").append(statiIstanzaQP).append("\nIN importanza: ").append(importanzaQP)
					.append("\nIN codiceIstanza: ").append(codiceIstanzaQP)
					.append("\nIN codiceModulo: ").append(codiceModuloQP).append("\nIN versioneModuloQP: ").append(versioneModuloQP).append("\nIN titoloModuloQP: ").append(titoloModuloQP)
					.append("\nIN createdStart: ").append(createdStart).append("\nIN createdEnd: ").append(createdEnd)
					.append("\nIN codiceFiscale: ").append(codiceFiscale).append("\nIN cognome: ").append(cognome).append("\nIN nome: ").append(nome)
					.append("\nIN sort: ").append(sort)
					.append("\nIN test: ").append(test);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] BEGIN" + keyOp.toString());
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN moonId: " + moonId);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN xRequestId: " + xRequestId);
			}
			UserInfo user = retrieveUserInfoApi(httpRequest);
			// validazione dati
			Integer idTabFo = validaInteger(idTabFoQP);
			Integer stato = validaInteger(statoQP);
			Integer importanza = validaInteger(importanzaQP);
			String codiceIstanza = validaStringCode(codiceIstanzaQP);
			String codiceModulo = validaStringCode(codiceModuloQP);
			String versioneModulo = validaStringCode(versioneModuloQP);
			Integer offset = validaInteger0BasedRequired(offsetQP);
			Integer limit = validaInteger1BasedRequired(limitQP);

			// Filter
			IstanzeFilter filter = new IstanzeFilter();
			if (!StringUtils.isBlank(codiceModulo) && !StringUtils.isBlank(versioneModulo)) {
				Modulo m = moduliService.getModuloByCodice(codiceModulo, versioneModulo);
				filter.setIdModulo(m.getIdModulo());
				filter.setIdVersioneModulo(m.getIdVersioneModulo());
			} else {
				if (!StringUtils.isBlank(codiceModulo)) {
					filter.setIdModulo(moduliService.getIdModuloByCodice(codiceModulo));
				}
			}
			filter.setIdTabFo(idTabFo); // 1-Bozze 2-Invate+
			// Qui siamo nel Front-Office, quindi visualizziamo solo quelli dellutente
			// corrente CodiceFiscaleDichiarante
			if (user.isOperatore()) {
				// per operatore
				filter.setGruppoOperatoreFo(user.getGruppoOperatoreFo());
				// altri possibili filtri di un operatore
				if (cercaDichiaranteInJson(filter.getIdModulo()))	{
					filter.setCodiceFiscaleJson(codiceFiscale);
					filter.setCognomeJson(cognome);
					filter.setNomeJson(nome);
					setDichiaranteJsonKeys(filter.getIdModulo(),filter);
				} else {
					filter.setCodiceFiscaleDichiarante(codiceFiscale);
					filter.setCognomeDichiarante(cognome);
					filter.setNomeDichiarante(nome);
				}
			} else {
				filter.setIdentificativoUtente(user.getIdentificativoUtente());
			}
			filter.setNomePortale(null); // ?? sostituito con setIdEnte()
			filter.setIdEnte(user.getEnte().getIdEnte());

			if (statiIstanzaQP != null) {
				filter.setStatiIstanza(statiIstanzaQP.stream().map(Integer::parseInt).collect(Collectors.toList()));
			} else {
				if (stato != null) {
					filter.setStatiIstanza(Arrays.asList(stato));
				}
			}

//			if (stato!=null) {
//				filter.setStatiIstanza(Arrays.asList(stato));
//			}
			filter.setImportanza(importanza);
			filter.setCodiceIstanza(codiceIstanza);

			filter.setTitoloModulo(titoloModuloQP);
			filter.setCreatedStart(createdStart);
			filter.setCreatedEnd(createdEnd);
			filter.setIdEnte(user.isMultiEntePortale() ? user.getEnte().getIdEnte() : null);
			filter.setIdAmbito(user.getIdAmbito());
			filter.setIdVisibilitaAmbito(
					filter.getIdAmbito().isPresent() ? null : ModuliFilter.VISIBILITA_AMBITO_PUBLIC);

			// Paginazione
			filter.setUsePagination(true);
			filter.setOffset(offset);
			filter.setLimit(limit);
			
			// Sorter
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			
			//
			auditService.insertSearchIstanze(httpRequest.getRemoteAddr(), user, keyOp.toString());
			ResponsePaginated<Istanza> result = (ResponsePaginated<Istanza>) apiService.getIstanzePaginate(filter, optSorter, user);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] OUT " + result);
			}
			return Response.ok(result).header(HEADER_X_REQUEST_ID, xRequestId).build();
		} catch (UnauthorizedBusinessException ube) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] UnauthorizedBusinessException");
			throw new UnauthorizedException(ube);			
		} catch (FormatBusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore formato data non corretto", e);
			throw new ServiceException("Errore formato data non corretto");
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore servizio getIstanzePaginate", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore servizio getIstanzePaginate");
			throw e;			
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore generico servizio getIstanzePaginate", ex);
			throw new ServiceException("Errore generico servizio elenco Istanze paginate");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] END");
			}
		}
	}	

	private boolean cercaDichiaranteInJson(Optional<Long> optIdModulo) {
		if (optIdModulo.isEmpty())
			return false;
		boolean result = false;
		try {
			ModuloAttributoEntity ma = moduloAttributiDAO.findByNome(optIdModulo.orElseThrow(), ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE.getKey());
			if ("S".equalsIgnoreCase(ma.getValore()))
				result = true;
			return result;
		} catch (ItemNotFoundDAOException daoe) {
			return false;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::cercaDichiaranteInJson] Errore generico servizio moduloAttributiDAO");
			throw new BusinessException(daoe);
		}
	}
	
	private void setDichiaranteJsonKeys(Optional<Long> optIdModulo, IstanzeFilter filter) {
		
		String codiceFiscaleKey = "";
		String nomeKey = "";
		String cognomeKey = "";
				
		ModuloAttributoEntity ma = moduloAttributiDAO.findByNome(optIdModulo.orElseThrow(), ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE_CONF.getKey());
		String confJson = ma.getValore();		
		try {		
			
			filter.setFindInJsonData(true);
			
			JsonNode conf = getJsonNodeFromString(confJson);
			codiceFiscaleKey = conf.get("codice_fiscale_dichiarante_data_key")!=null?conf.get("codice_fiscale_dichiarante_data_key").asText():"";
			nomeKey = conf.get("nome_dichiarante_data_key")!=null?conf.get("nome_dichiarante_data_key").asText():"";
			cognomeKey = conf.get("cognome_dichiarante_data_key")!=null?conf.get("cognome_dichiarante_data_key").asText():"";
						
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::setDichiaranteJsonKeys] Errore generico parsing configurazione estrai dichiarante", e);
		}		
		filter.setCodiceFiscaleJsonKey(codiceFiscaleKey);
		filter.setCognomeJsonKey(cognomeKey);
		filter.setNomeJsonKey(nomeKey);		
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
			UserInfo user = retrieveUserInfoApi(httpRequest);
			
			Istanza result = apiService.getIstanza(codice, user);

			return Response.ok(result).header(HEADER_X_REQUEST_ID, xRequestId).build();
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
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore generico servizio getIstanza", ex);
			throw new ServiceException("Errore generico servizio getIstanza");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanza] END");
			}
		}
	}


	@Override
	public Response getIstanzaPdf(String codiceIstanza, 
			String moonId, String xRequestId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN codiceIstanza: " + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN moonId: " + moonId);
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN xRequestId: " + xRequestId);
			}
			UserInfo user = retrieveUserInfoApi(httpRequest);
			// validazione dati
			codiceIstanza = validaStringCodeRequired(codiceIstanza);
			
			byte[] bytes = apiService.getIstanzaPdf(codiceIstanza, user, httpRequest.getRemoteAddr());
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] bytes = "+bytes);
			}
			
	        return Response.ok(bytes)
	        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
	        		.header("Pragma", "no-cache")
	        		.header("Expires", "0")
	        		.header(HttpHeaders.CONTENT_TYPE, "application/pdf")
	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+codiceIstanza+".pdf")
	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
	        		.header(HEADER_X_REQUEST_ID, xRequestId)
	        		.build();			

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ube) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] UnauthorizedBusinessException");
			throw new UnauthorizedException(ube);		
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] Errore servizio getIstanzaPdf", be);
			throw new ServiceException(be);	
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] Errore servizio getIstanzaPdf");
			throw e;			
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] Errore generico servizio getIstanzaPdf", ex);
			throw new ServiceException("Errore generico servizio getIstanzaPdf");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] END");
			}
		}
	}
	


	@Override
	public Response getModuli(String codiceModuloQP, String versioneModuloQP, 
	    	String oggettoModulo, String descrizioneModulo, 
	    	String statoQP, 
	    	String codiceAmbitoQP, 
	    	String conPresenzaIstanzeQP, String fieldsQP, 
			String moonId, String xRequestId, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceModuloQP: "+codiceModuloQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN versioneModuloQP: "+versioneModuloQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN oggettoModulo: "+oggettoModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN descrizioneModulo: "+descrizioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN statoQP: "+statoQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN conPresenzaIstanzeQP: "+conPresenzaIstanzeQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN fieldsQP: "+fieldsQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN moonId: " + moonId);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN xRequestId: " + xRequestId);
			}
			UserInfo user = retrieveUserInfoApi(httpRequest);
			String codiceModulo = validaCodiceModulo(codiceModuloQP);
			String versioneModulo = validaCodiceModulo(versioneModuloQP);
			String stato = validaCodiceModulo(statoQP);
			Boolean conPresenzaIstanze = validaBoolean(conPresenzaIstanzeQP);
			String fields = validaFields(fieldsQP);
			String nomePortale = user.getPortalName();
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN nomePortale: "+nomePortale);
			}
			
			//
			ModuliFilter filter = new ModuliFilter();
			filter.setCodiceModulo(codiceModulo);
			filter.setVersioneModulo(versioneModulo);
			filter.setOggettoModulo(oggettoModulo);
			filter.setDescrizioneModulo(descrizioneModulo);
			filter.setStatoModulo(retrieveDecodificaStatoModulo(stato));
			filter.setConPresenzaIstanzeUser(Boolean.TRUE.equals(conPresenzaIstanze)?user.getIdentificativoUtente():null);
			filter.setNomePortale((nomePortale.equals("localhost")) ? "*" : nomePortale);
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
			filter.setIdAmbito(user.getIdAmbito());
			filter.setIdVisibilitaAmbito(filter.getIdAmbito().isPresent()?null:ModuliFilter.VISIBILITA_AMBITO_PUBLIC);
			
			List<Modulo> elenco = apiService.getElencoModuli(filter);
			LOG.info("[" + CLASS_NAME + "::getModuli] " + (elenco!=null?elenco.size():0) + " moduli trovati.");
			return Response.ok(elenco).build();
		} catch (UnauthorizedBusinessException ube) {
			LOG.error("[" + CLASS_NAME + "::getModuli] UnauthorizedBusinessException");
			throw new UnauthorizedException(ube);
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore servizio getModuli", e);
			throw new ServiceException("Errore servizio elenco moduli");
		} catch (ResourceNotFoundException notFoundServEx) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Nessun modulo non trovato ", notFoundServEx);
			throw notFoundServEx;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore generico servizio getModuli", ex);
			throw new ServiceException("Errore generico servizio elenco moduli");
		}
	}

	protected DecodificaStatoModulo retrieveDecodificaStatoModulo(String stato) {
		try {
			if (StringUtils.isBlank(stato))
				return null;
			return DecodificaStatoModulo.byCodice(stato);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveDecodificaStatoModulo] Errore generico servizio per stato " + stato, e);
			throw new UnprocessableEntityException("Stato modulo " + stato + " sconosciuto.");
		}
	}

	@Override
	public Response getModuloByCodice(String codiceModuloPP, String versioneModuloPP, String fields,
			String moonId, String xRequestId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] IN codiceModuloPP: " + codiceModuloPP);
				LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] IN versioneModuloPP: " + versioneModuloPP);
				LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] IN fields: " + fields);
				LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] IN moonId: " + moonId);
				LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] IN xRequestId: " + xRequestId);
			}
			String codiceModulo = validaStringCodeRequired(codiceModuloPP);
			String versioneModulo = validaStringCodeRequired(versioneModuloPP);
			UserInfo user = retrieveUserInfoApi(httpRequest);
			Modulo modulo = moduliService.getModuloByCodice(codiceModulo, versioneModulo);
			return Response.ok(modulo).build();
		} catch (UnauthorizedBusinessException ube) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] UnauthorizedBusinessException");
			throw new UnauthorizedException(ube);
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] modulo non trovato " + codiceModuloPP + "/" + versioneModuloPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] Errore servizio getModuloByCodice " + codiceModuloPP + "/" + versioneModuloPP, e);
			throw new ServiceException("Errore servizio getModuloByCodice");
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] Errore UnprocessableEntityException " + codiceModuloPP + "/" + versioneModuloPP, uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] Errore generico servizio getModuloByCodice " + codiceModuloPP + "/" + versioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio getModuloByCodice");
		}
	}
	
	private JsonNode getJsonNodeFromString(String strJson) throws Exception {
		try {	
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);
			return result;
		} catch (IOException e) {
		 
		    throw e;
		} finally {
			
		}
	}

	@Override
	public Response duplicaIstanza(String codiceIstanzaPP, String duplicaAllegati, String moonId, String xRequestId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::duplicaIstanza] IN codiceIstanzaPP: " + codiceIstanzaPP);
				LOG.debug("[" + CLASS_NAME + "::duplicaIstanza] IN duplicaAllegati: " + duplicaAllegati);
				LOG.debug("[" + CLASS_NAME + "::duplicaIstanza] IN moonId: " + moonId);
				LOG.debug("[" + CLASS_NAME + "::duplicaIstanza] IN xRequestId: " + xRequestId);
			}
			String codiceIstanza= validaStringCodeRequired(codiceIstanzaPP);
			Boolean duplica = validaBoolean(duplicaAllegati, true);

			UserInfo user = retrieveUserInfoApi(httpRequest);			
			Istanza result = istanzeService.duplicaForApi(user, codiceIstanzaPP, duplica);
			
			return Response.ok(result).build();
		} catch (UnauthorizedBusinessException ube) {
			LOG.error("[" + CLASS_NAME + "::duplicaIstanza] UnauthorizedBusinessException");
			throw new UnauthorizedException(ube);
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::duplicaIstanza] istanza non trovata " + codiceIstanzaPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::duplicaIstanza] Errore servizio duplicaIstanza " + codiceIstanzaPP, e);
			throw new ServiceException("Errore servizio getModuloByCodice");
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::duplicaIstanza] Errore UnprocessableEntityException " + codiceIstanzaPP, uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::duplicaIstanza] Errore generico servizio duplicaIstanza " + codiceIstanzaPP, ex);
			throw new ServiceException("Errore generico servizio duplicaIstanza");
		}
	}

	@Override
	public Response deleteIstanza(String codiceIstanzaPP, String moonId, String xRequestId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::deleteIstanza] IN codiceIstanzaPP: " + codiceIstanzaPP);
				LOG.debug("[" + CLASS_NAME + "::deleteIstanza] IN moonId: " + moonId);
				LOG.debug("[" + CLASS_NAME + "::deleteIstanza] IN xRequestId: " + xRequestId);
			}
			String codiceIstanza= validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfoApi(httpRequest);	
			
			Istanza result = istanzeService.deleteIstanzaForApi(user, codiceIstanza);
		    			
			return Response.ok(result).build();
		} catch (UnauthorizedBusinessException ube) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanza] UnauthorizedBusinessException");
			throw new UnauthorizedException(ube);
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanza] istanza non trovata " + codiceIstanzaPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanza] Errore servizio duplicaIstanza " + codiceIstanzaPP, e);
			throw new ServiceException("Errore servizio deleteIstanza");
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanza] Errore UnprocessableEntityException " + codiceIstanzaPP, uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanza] Errore generico servizio deleteIstanza " + codiceIstanzaPP, ex);
			throw new ServiceException("Errore generico servizio deleteIstanza");
		}
	}

	@Override
	public Response getCronologiaStatiModulo(String codiceModuloPP, String stato, String moonId, String xRequestId, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getCronologiaStatiModulo] IN codiceModuloPP: " + codiceModuloPP);
				LOG.debug("[" + CLASS_NAME + "::getCronologiaStatiModulo] IN stato: " +stato);
				LOG.debug("[" + CLASS_NAME + "::getCronologiaStatiModulo] IN moonId: " + moonId);
				LOG.debug("[" + CLASS_NAME + "::getCronologiaStatiModulo] IN xRequestId: " + xRequestId);
			}
			String codiceModulo = validaStringCodeRequired(codiceModuloPP);	
			UserInfo user = retrieveUserInfoApi(httpRequest);			
			Long idModulo = moduliService.getIdModuloByCodice(codiceModulo);
			List<ModuloVersioneStato> versioni = moduliService.getVersioniModuloById(idModulo,stato);
			
			return Response.ok(versioni).build();
		} catch (UnauthorizedBusinessException ube) {
			LOG.error("[" + CLASS_NAME + "::getCronologiaStatiModulo] UnauthorizedBusinessException");
			throw new UnauthorizedException(ube);
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getCronologiaStatiModulo] modulo non trovato " + codiceModuloPP , notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getCronologiaStatiModulo] Errore servizio getCronologiaStatiModulo " + codiceModuloPP , e);
			throw new ServiceException("Errore servizio getCronologiaStatiModulo");
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getCronologiaStatiModulo] Errore UnprocessableEntityException " + codiceModuloPP, uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getCronologiaStatiModulo] Errore generico servizio getCronologiaStatiModulo " + codiceModuloPP, ex);
			throw new ServiceException("Errore generico servizio getCronologiaStatiModulo");
		}
	}

}
