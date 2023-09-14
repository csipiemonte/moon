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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;
import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.ResponsePaginated;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.commons.entity.IstanzeSorterBuilder;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonfobl.business.be.IstanzeApi;
import it.csi.moon.moonfobl.business.service.AllegatiService;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.IstanzeService;
import it.csi.moon.moonfobl.business.service.RepositoryFileService;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class IstanzeApiImpl extends MoonBaseApiImpl implements IstanzeApi {

	private static final String CLASS_NAME = "IstanzeApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	IstanzeService istanzeService;
	@Autowired
	AuditService auditService;
	@Autowired
	AllegatiService allegatiService;
	@Autowired
	RepositoryFileService repositoryFileService;
	@Autowired
	@Qualifier("nocache")
	ModuloAttributiDAO moduloAttributiDAO;
	
	public Response getIstanze(String idTabFoQP, String statoQP, List<String> statiIstanza, String importanzaQP, String codiceIstanza,
			String idModuloQP, String titoloModulo, Date createdStart, Date createdEnd, String codiceFiscale,	
			String cognome, String nome, String sort, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			StringBuilder keyOp = new StringBuilder().append("\nIN idTabFoQP: ").append(idTabFoQP)
					.append("\nIN statoQP: ").append(statoQP).append("\nIN importanzaQP: ").append(importanzaQP)
					.append("\nIN codiceIstanza: ").append(codiceIstanza).append("\nIN idModuloQP: ").append(idModuloQP)
					.append("\nIN titoloModulo: ").append(titoloModulo).append("\nIN createdStart: ")
					.append(createdStart).append("\nIN createdEnd: ").append(createdEnd).append("\nIN codiceFiscale: ")
					.append(codiceFiscale).append("\nIN cognome: ").append(cognome).append("\nIN nome: ").append(nome)	
					.append("\nIN sort: ").append(sort);

			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] BEGIN" + keyOp.toString());
			}
			Integer idTabFo = validaInteger(idTabFoQP);
			Integer stato = validaInteger(statoQP);
			Integer importanza = validaInteger(importanzaQP);
			Long idModulo = validaLong(idModuloQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			String nomePortale = retrievePortalName(httpRequest);

			// Filter
			IstanzeFilter filter = new IstanzeFilter();
			filter.setIdTabFo(idTabFo); // 1-Bozze 2-Invate+
			// Qui siamo nel Front-Office, quindi visualizziamo solo quelli dellutente
			// corrente CodiceFiscaleDichiarante
			if (user.isOperatore()) {
				// per operatore
				filter.setGruppoOperatoreFo(user.getGruppoOperatoreFo());
				// altri possibili filtri di un operatore
				if (cercaDichiaranteInJson(idModulo))	{
					filter.setCodiceFiscaleJson(codiceFiscale);
					filter.setCognomeJson(cognome);
					filter.setNomeJson(nome);
					setDichiaranteJsonKeys(idModulo,filter);
				} else {
					filter.setCodiceFiscaleDichiarante(codiceFiscale);
					filter.setCognomeDichiarante(cognome);
					filter.setNomeDichiarante(nome);
				}
			} else {
				filter.setIdentificativoUtente(user.getIdentificativoUtente());
			}
			
			filter.setNomePortale(nomePortale);
			if (stato != null) {
				filter.setStatiIstanza(Arrays.asList(stato));
			}
			filter.setImportanza(importanza);
			filter.setCodiceIstanza(codiceIstanza);
			filter.setIdModulo(idModulo);
			filter.setTitoloModulo(titoloModulo);
			filter.setCreatedStart(createdStart);
			filter.setCreatedEnd(createdEnd);
			filter.setIdEnte(user.isMultiEntePortale() ? user.getEnte().getIdEnte() : null);
			filter.setIdAmbito(user.getIdAmbito());
			filter.setIdVisibilitaAmbito(
					filter.getIdAmbito().isPresent() ? null : ModuliFilter.VISIBILITA_AMBITO_PUBLIC);
			
			if (statiIstanza != null && !statiIstanza.isEmpty()) {
				filter.setStatiIstanza(statiIstanza.stream().map(Integer::parseInt).collect(Collectors.toList()));

			} else {
				if (stato != null) {
					filter.setStatiIstanza(Arrays.asList(stato));
				}
			}

			// Sorter
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();

			//
			auditService.insertSearchIstanze(httpRequest.getRemoteAddr(), user, keyOp.toString());
			List<Istanza> elenco = istanzeService.getElencoIstanze(filter, optSorter);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getIstanze] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio getIstanze", ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		}
	}

	public Response getIstanzePaginated(List<String> statiIstanza, String idTabFoQP, String statoQP,
			String importanzaQP, String codiceIstanza, String idModuloQP, String titoloModulo, Date createdStart,
			Date createdEnd, String codiceFiscale, String cognome, String nome, String sort, String offsetQP,
			String limitQP, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		StringBuilder keyOp;
		try {

			keyOp = new StringBuilder().append("\nIN statiIstanza: ").append(statiIstanza).append("\nIN idTabFoQP: ")
					.append(idTabFoQP).append("\nIN statoQP: ").append(statoQP).append("\nIN importanzaQP: ")
					.append(importanzaQP).append("\nIN codiceIstanza: ").append(codiceIstanza)
					.append("\nIN idModuloQP: ").append(idModuloQP).append("\nIN titoloModulo: ").append(titoloModulo)
					.append("\nIN createdStart: ").append(createdStart).append("\nIN createdEnd: ").append(createdEnd)
					.append("\nIN codiceFiscale: ").append(codiceFiscale).append("\nIN cognome: ").append(cognome)
					.append("\nIN nome: ").append(nome).append("\nIN sort: ").append(sort);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginated] BEGIN" + keyOp.toString());
			}
			Integer idTabFo = validaInteger(idTabFoQP);
			Integer stato = validaInteger(statoQP);
			Integer importanza = validaInteger(importanzaQP);
			Long idModulo = validaLong(idModuloQP);
			Integer offset = validaInteger0Based(offsetQP);
			Integer limit = validaInteger1Based(limitQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			String nomePortale = retrievePortalName(httpRequest);

			// Filter
			IstanzeFilter filter = new IstanzeFilter();
			filter.setIdTabFo(idTabFo); // 1-Bozze 2-Invate+
			// Qui siamo nel Front-Office, quindi visualizziamo solo quelli dell' utente
			// corrente CodiceFiscaleDichiarante
			if (user.isOperatore()) {
				// per operatore
				filter.setGruppoOperatoreFo(user.getGruppoOperatoreFo());
				// altri possibili filtri di un operatore
				if (cercaDichiaranteInJson(idModulo))	{
					filter.setCodiceFiscaleJson(codiceFiscale);
					filter.setCognomeJson(cognome);
					filter.setNomeJson(nome);
					setDichiaranteJsonKeys(idModulo,filter);
				} else {
					filter.setCodiceFiscaleDichiarante(codiceFiscale);
					filter.setCognomeDichiarante(cognome);
					filter.setNomeDichiarante(nome);
				}
			} else {
				filter.setIdentificativoUtente(user.getIdentificativoUtente());
			}
			filter.setNomePortale(nomePortale);

			if (statiIstanza != null && !statiIstanza.isEmpty()) {
				filter.setStatiIstanza(statiIstanza.stream().map(Integer::parseInt).collect(Collectors.toList()));

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
			filter.setIdModulo(idModulo);
			filter.setTitoloModulo(titoloModulo);
			filter.setCreatedStart(createdStart);
			filter.setCreatedEnd(createdEnd);
			// Paginazione
			filter.setUsePagination(true);
			filter.setOffset(offset);
			filter.setLimit(limit);
			filter.setIdEnte(user.isMultiEntePortale() ? user.getEnte().getIdEnte() : null);
			filter.setIdAmbito(user.getIdAmbito());
			filter.setIdVisibilitaAmbito(
					filter.getIdAmbito().isPresent() ? null : ModuliFilter.VISIBILITA_AMBITO_PUBLIC);

			// Sorter
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();

			//
			auditService.insertSearchIstanze(httpRequest.getRemoteAddr(), user, keyOp.toString());
			ResponsePaginated<Istanza> response = (ResponsePaginated<Istanza>) istanzeService
					.getElencoIstanzePaginated(filter, null, optSorter);
			return Response.ok(response).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzePaginated] Errore servizio getIstanze", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzePaginated] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginated] Errore generico servizio getIstanze", ex);
			throw new ServiceException("Errore generico servizio elenco istanze paginate");
		}
	}

	private boolean cercaDichiaranteInJson(Long idModulo) {
		if (idModulo==null)
			return false;
		boolean result = false;
		try {
			ModuloAttributoEntity ma = moduloAttributiDAO.findByNome(idModulo, ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE.getKey());
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
	
	private void setDichiaranteJsonKeys(Long idModulo, IstanzeFilter filter) {
		
		String codiceFiscaleKey = "";
		String nomeKey = "";
		String cognomeKey = "";
				
		ModuloAttributoEntity ma = moduloAttributiDAO.findByNome(idModulo, ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE_CONF.getKey());
		String confJson = ma.getValore();		
		try {		
			
			filter.setFindInJsonData(true);
			
			JsonNode conf = getJsonNodeFromString(confJson);
			codiceFiscaleKey = conf.has("codice_fiscale_dichiarante_data_key")?conf.get("codice_fiscale_dichiarante_data_key").asText():"";
			nomeKey = conf.has("nome_dichiarante_data_key")?conf.get("nome_dichiarante_data_key").asText():"";
			cognomeKey = conf.has("cognome_dichiarante_data_key")?conf.get("cognome_dichiarante_data_key").asText():"";

						
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::setDichiaranteJsonKeys] Errore generico parsing configurazione estrai dichiarante", e);
		}		
		filter.setCodiceFiscaleJsonKey(codiceFiscaleKey);
		filter.setCognomeJsonKey(cognomeKey);
		filter.setNomeJsonKey(nomeKey);		
	}
	
	public Response getIstanzaById(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaById] IN idIstanzaQP: " + idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			auditService.getIstanza(httpRequest.getRemoteAddr(), user, idIstanza);
			Istanza istanza = istanzeService.getIstanzaById(user, idIstanza);
			return Response.ok(istanza).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaById] modulo non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaById] Errore servizio getIstanzaById", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaById] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] Errore generico servizio getIstanzaById", ex);
			throw new ServiceException("Errore generico servizio getIstanzaById");
		}
	}
	
	public Response getIstanzaByCodice(String codiceIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaByCodice] IN codiceIstanzaQP: " + codiceIstanzaQP);
			String codiceIstanza = validaStringRequired(codiceIstanzaQP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			auditService.getIstanza(httpRequest.getRemoteAddr(), user, codiceIstanza);
	
			Istanza istanza = istanzeService.getIstanzaByCd(user, codiceIstanza);
		
			return Response.ok(istanza).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaByCodice] modulo non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaByCodice] Errore servizio getIstanzaById", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaByCodice] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaByCodice] Errore generico servizio getIstanzaById", ex);
			throw new ServiceException("Errore generico servizio getIstanzaByCodice");
		}
	}

	public Response getInitIstanza(String idModuloPP, String idVersioneModuloPP, IstanzaInitBLParams params,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN idModuloPP: " + idModuloPP);
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN idVersioneModuloPP: " + idVersioneModuloPP);
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN params: " + params);
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN user: " + user);
//			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN IdMoonToken: "+jwtTokenUtil.toString(user.getIdMoonToken()));
			//
			Istanza istanza = istanzeService.getInitIstanza(user, idModulo, idVersioneModulo, params, httpRequest);
			return Response.ok(istanza).build();
		} catch (UnivocitaIstanzaBusinessException uibe) {
			LOG.warn("[" + CLASS_NAME + "::getInitIstanza] UnivocitaIstanzaBusinessException getInitIstanza " + uibe);
			throw new ServiceException(uibe.getMessage(), uibe.getCode());
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getInitIstanza] Errore servizio getInitIstanza" + be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getInitIstanza] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] Errore generico servizio getInitIstanza", ex);
			throw new ServiceException("Errore generico servizio inizializzazione istanza");
		}
	}

	public Response getPdf(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPdf] IN idIstanzaQP: " + idIstanzaQP + "\n");
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			auditService.getPdf(httpRequest.getRemoteAddr(), user, idIstanza);
			byte[] istanzaAsPdf = istanzeService.getPdfIstanza(user, idIstanza);
			return Response.ok(istanzaAsPdf).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPdf] Errore servizio getPdf", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getPdf] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getPdf] Errore generico servizio getPdf", ex);
			throw new ServiceException("Errore generico servizio getPdf");
		}
	}

	public Response putIstanza(String idIstanzaQP, Istanza body, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::putIstanza] IN idIstanzaQP: " + idIstanzaQP + "\nIstanza body: " + body);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			auditService.saveIstanza(httpRequest.getRemoteAddr(), user, idIstanza);
			IstanzaSaveResponse result = istanzeService.saveIstanza(user, idIstanza, body);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::putIstanza] Errore servizio putIstanza", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::putIstanza] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::putIstanza] Errore generico servizio putIstanza", ex);
			throw new ServiceException("Errore generico servizio aggiorna istanza");
		}
	}

	public Response saveIstanza(Istanza body, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::saveIstanza] IN Istanza body: " + body);
			UserInfo user = retrieveUserInfo(httpRequest);
			auditService.saveIstanza(httpRequest.getRemoteAddr(), user, body.getIdIstanza());
			IstanzaSaveResponse result = istanzeService.saveIstanza(user, body);
			return Response.ok(result).build();
		} catch (UnivocitaIstanzaBusinessException uibe) {
			LOG.warn("[" + CLASS_NAME + "::saveIstanza] UnivocitaIstanzaBusinessException saveIstanza " + uibe);
			throw new ServiceException(uibe.getMessage(), uibe.getCode());
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::saveIstanza] Errore servizio saveIstanza", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::saveIstanza] Errore generico servizio saveIstanza", ex);
			throw new ServiceException("Errore generico servizio salva istanza");
		}
	}

	@Override
	public Response deleteIstanzaById(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteIstanzaById] IN idIstanzaQP: " + idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			auditService.deleteIstanza(httpRequest.getRemoteAddr(), user, idIstanza);
			Istanza istanza = istanzeService.deleteIstanza(user, idIstanza);
			return Response.ok(istanza).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.warn("[" + CLASS_NAME + "::deleteIstanzaById] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::deleteIstanzaById] Errore servizio deleteIstanzaById", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::deleteIstanzaById] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanzaById] Errore generico servizio deleteIstanzaById", ex);
			throw new ServiceException("Errore generico servizio delete istanza");
		}
	}

	@Override
	public Response patchIstanzaById(String idIstanzaQP, Istanza partialIstanza, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::patchIstanzaById] IN idIstanza:" + idIstanzaQP + "\nIstanza body: "
					+ partialIstanza);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			Istanza istanza = istanzeService.patchIstanza(user, idIstanza, partialIstanza);
			return Response.ok(istanza).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::patchIstanzaById] Errore servizio patchIstanzaById", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::patchIstanzaById] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::patchIstanzaById] Errore generico servizio patchIstanzaById", ex);
			throw new ServiceException("Errore generico servizio aggiorna partial istanza");
		}
	}

	@Override
	public Response starIstanza(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::starIstanza] IN idIstanzaQP:" + idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			Istanza istanzaStared = new Istanza();
			istanzaStared.setImportanza(1);
			Istanza istanza = istanzeService.patchIstanza(user, idIstanza, istanzaStared);
			return Response.ok(istanza).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::starIstanza] Errore servizio starIstanza", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::starIstanza] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::starIstanza] Errore generico servizio starIstanza", ex);
			throw new ServiceException("Errore generico servizio star istanza");
		}
	}

	@Override
	public Response unstarIstanza(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::unstarIstanza] IN idIstanzaQP:" + idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			Istanza istanzaUnstared = new Istanza();
			istanzaUnstared.setImportanza(0);
			Istanza istanza = istanzeService.patchIstanza(user, idIstanza, istanzaUnstared);
			return Response.ok(istanza).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::unstarIstanza] Errore servizio unstarIstanza", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::unstarIstanza] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::unstarIstanza] Errore generico servizio unstarIstanza", ex);
			throw new ServiceException("Errore generico servizio unstar istanza");
		}
	}

	@Override
	public Response riportaInBozza(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::riportaInBozza] IN idIstanzaQP:" + idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			Istanza istanza = istanzeService.riportaInBozza(user, idIstanza);
			return Response.ok(istanza).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::riportaInBozza] Errore servizio riportaInBozza", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::riportaInBozza] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::riportaInBozza] Errore generico servizio riportaInBozza", ex);
			throw new ServiceException("Errore generico servizio riportaInBozza");
		}
	}

	@Override
	public Response invia(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::invia] IN idIstanzaQP:" + idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
//			DatiAggiuntiviHeaders daHeaders = HeadersUtils.readFromHeaders(httpHeaders);
			//
			IstanzaSaveResponse istanza = istanzeService.invia(user, idIstanza, httpRequest.getRemoteAddr());
			return Response.ok(istanza).build();
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::invia] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::invia] Errore servizio invia", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::invia] Errore generico servizio invia", ex);
			throw new ServiceException("Errore generico servizio invia");
		}
	}

	@Override
	public Response compieAzione(String idIstanzaQP, String idAzioneQP, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug(
					"[" + CLASS_NAME + "::compieAzione] IN idIstanzaQP:" + idIstanzaQP + "  idAzioneQP:" + idAzioneQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			Long idAzione = validaLongRequired(idAzioneQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			IstanzaSaveResponse istanza = istanzeService.compieAzione(user, idIstanza, idAzione, httpRequest.getRemoteAddr());
			return Response.ok(istanza).build();
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::compieAzione] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::compieAzione] Errore servizio compieAzione", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] Errore generico servizio compieAzione", ex);
			throw new ServiceException("Errore generico servizio compieAzione");
		}
	}

	//
	// PDF
	//
	public Response getPdfById(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPdfById] IN idIstanzaQP:" + idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			// Tracing chiamata
			try {
				auditService.getPdf(httpRequest.getRemoteAddr(), user, idIstanza);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getPdfById] errore servizio Audit", e);
			}
			byte[] bytes = istanzeService.getPdfIstanza(user, idIstanza);
			LOG.debug("[" + CLASS_NAME + "::getPdfById] bytes.length=" + bytes.length);
			return Response.ok(bytes).build();
//    		.header("Cache-Control", "no-cache, no-store, must-revalidate")
//    		.header("Pragma", "no-cache")
//    		.header("Expires", "0")
//    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=response.pdf")
//    		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
//    		.build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.warn("[" + CLASS_NAME + "::getPdfById] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPdfById] Errore servizio getPdfById", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getPdfById] Errore generico servizio getPdfById", ex);
			throw new ServiceException("Errore generico servizio print istanza");
		}
	}

	public Response getNotificaById(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getNotificaById] IN idIstanzaQP:" + idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			// Tracing chiamata
			try {
				auditService.getNotifica(httpRequest.getRemoteAddr(), user, idIstanza);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getNotificaById] errore servizio Audit", e);
			}
			byte[] bytes = istanzeService.getNotificaIstanza(user, idIstanza);
			LOG.debug("[" + CLASS_NAME + "::getNotificaById] bytes.length=" + bytes.length);
			return Response.ok(bytes).build();

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getNotificaById] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getNotificaById] Errore servizio getPdfById", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getNotificaById] Errore generico servizio getPdfById", ex);
			throw new ServiceException("Errore generico servizio print istanza");
		}
	}

	public Response getDocumentoByFormioNameFile(String formioNameFileQP, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] IN formioNameFile:" + formioNameFileQP);
			String formioNameFile = validaStringRequired(formioNameFileQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			// Tracing chiamata
			try {
				auditService.getDocumentoByFormioNameFile(httpRequest.getRemoteAddr(), user, formioNameFile);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] errore servizio Audit", e);
			}
			byte[] bytes = istanzeService.getDocumentoByFormioNameFile(user, formioNameFile);
			LOG.debug("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] bytes.length=" + bytes.length);
			return Response.ok(bytes).build();

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] Errore servizio getDocumentoByFormioNameFile",
					be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error(
					"[" + CLASS_NAME
							+ "::getDocumentoByFormioNameFile] Errore generico servizio getDocumentoByFormioNameFile",
					ex);
			throw new ServiceException("Errore generico servizio getDocumentoByFormioNameFile");
		}
	}

	public Response getDocumentoByIdFile(Long idFile, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDocumentoByIdFile] IN idFile:" + idFile);
//			String formioNameFile = validaStringRequired(idFile);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			// Tracing chiamata
			try {
				auditService.getDocumentoByIdFile(httpRequest.getRemoteAddr(), user, idFile);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] errore servizio Audit", e);
			}
			byte[] bytes = istanzeService.getDocumentoByIdFile(user, idFile);
			LOG.debug("[" + CLASS_NAME + "::getDocumentoByIdFile] bytes.length=" + bytes.length);
			return Response.ok(bytes).build();

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] Errore servizio getDocumentoByIdFile", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] Errore generico servizio getDocumentoByIdFile", ex);
			throw new ServiceException("Errore generico servizio getDocumentoByIdFile");
		}
	}

	public Response getDocumentoNotificaById(String idIstanzaQP, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDocumentoNotificaById] IN idIstanzaQP:" + idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			// Tracing chiamata
			try {
				auditService.getDocumentoNotifica(httpRequest.getRemoteAddr(), user, idIstanza);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getDocumentoNotificaById] errore servizio Audit", e);
			}
			Documento documento = istanzeService.getDocumentoNotificaIstanza(user, idIstanza);
			return Response.ok(documento).build();

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.warn("[" + CLASS_NAME + "::getDocumentoNotificaById] Notifica not found per idIstanza: " + idIstanzaQP);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoNotificaById] Errore servizio getDocumentoNotificaById", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoNotificaById] Errore generico servizio getDocumentoNotificaById", ex);
			throw new ServiceException("Errore generico servizio getDocumentoNotificaById");
		}
	}

	@Override
	public Response getAllegati(String idIstanzaPP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getAllegati] IN idIstanzaPP:" + idIstanzaPP);
			Long idIstanza = validaLongRequired(idIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			List<Allegato> elenco = allegatiService.findLazyByIdIstanza(idIstanza);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getAllegati] Errore servizio getAllegati", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getAllegati] Errore generico servizio getAllegati", ex);
			throw new ServiceException("Errore generico servizio elenco allegati per istanza");
		}
	}

	@Override
	public Response getDocumenti(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDocumenti] IN idIstanza: " + idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			List<Documento> elenco = repositoryFileService.findByIdIstanza(idIstanza);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getDocumenti] Errore servizio getDocumenti", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getDocumenti] Errore generico servizio getDocumenti", ex);
			throw new ServiceException("Errore generico servizio elenco docuemnti per istanza");
		}
	}

	@Override
	public Response getDocumentiProtocollati(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDocumentiProtocollati] IN idIstanza: " + idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			List<Documento> elenco = repositoryFileService.findProtocollatiByIdIstanza(idIstanza);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getDocumentiProtocollati] Errore servizio getDocumenti", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error(
					"[" + CLASS_NAME + "::getDocumentiProtocollati] Errore generico servizio getDocumentiProtocollati",
					ex);
			throw new ServiceException("Errore generico servizio elenco docuemnti protocollati per istanza");
		}
	}
	
	@Override
	public Response getDocumentiEmessi(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDocumentiEmessi] IN idIstanza: " + idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			List<Documento> elenco = repositoryFileService.findEmessiDaUfficioByIdIstanza(idIstanza);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getDocumentiEmessi] Errore servizio getDocumentiEmessi", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error(
					"[" + CLASS_NAME + "::getDocumentiEmessi] Errore generico servizio getDocumentiEmessi",
					ex);
			throw new ServiceException("Errore generico servizio elenco documenti emessi da Ufficio per istanza");
		}
	}


	@Override
	public Response getAllegato(String formioNameFile, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getAllegato] IN formioFileName: " + formioNameFile);
			UserInfo user = retrieveUserInfo(httpRequest);
			Allegato fileAllegato = allegatiService.getByFormIoNameFile(formioNameFile);
			return Response.ok(fileAllegato.getContenuto())
					.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"")
					.header("Content-Type", fileAllegato.getContentType()).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getAllegato] Errore servizio getAllegato", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] Errore generico servizio getAllegato", ex);
			throw new ServiceException("Errore generico servizio getAllegato");
		}
	}

	@Override
	public Response inviaRispostaIntegrazioneCosmo(Long idIstanza, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] IN idIstanza: " + idIstanza);
//			UserInfo user = retrieveUserInfo(httpRequest);						
			String result = istanzeService.inviaRispostaIntegrazioneCosmo(idIstanza);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] Errore servizio getAllegato", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] Errore generico servizio getAllegato", ex);
			throw new ServiceException("Errore generico servizio getAllegato");
		}
	}

	@Override
	public Response duplicaIstanza(String idIstanzaPP, String duplicaAllegatiQP, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::duplicaIstanza] BEGIN");
			Long idIstanza = validaLongRequired(idIstanzaPP);
			// di default si duplica cn gli allegati
			Boolean duplicatiAllegati = validaBoolean(duplicaAllegatiQP, true);
			UserInfo user = retrieveUserInfo(httpRequest);

			auditService.duplicaIstanza(httpRequest.getRemoteAddr(), user, idIstanza);
			IstanzaSaveResponse result = istanzeService.duplica(user, idIstanza, duplicatiAllegati, getIpAddress(httpRequest));
			return Response.ok(result).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::duplicaIstanza] Errore servizio duplicaIstanza", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::duplicaIstanza] Errore generico servizio duplicaIstanza", ex);
			throw new ServiceException("Errore generico servizio duplica istanza");
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


}
