/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.StatoModulo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.moonfobl.business.be.ModuliApi;
import it.csi.moon.moonfobl.business.service.ModuliService;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class ModuliApiImpl extends MoonBaseApiImpl implements ModuliApi {

	private static final String CLASS_NAME = "ModuliApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	ModuliService moduliService;

	@Override
	public Response getModuli(String codiceModuloQP, String idModuloQP, String conPresenzaIstanzeQP, String fieldsQP,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceModuloQP: "+codiceModuloQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN idModuloQP: "+idModuloQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN conPresenzaIstanzeQP: "+conPresenzaIstanzeQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN fieldsQP: "+fieldsQP);
			}
			String codiceModulo = validaCodiceModulo(codiceModuloQP);
			Long idModulo = null;
			try {
				idModulo = validaLong(idModuloQP);
			} catch (UnprocessableEntityException uee) {
				if (StringUtils.isEmpty(codiceModulo)) {
					codiceModulo = idModuloQP; // Per Accesso dirretto a FO con il codice
					LOG.debug("[" + CLASS_NAME + "::getModuli] IN idModulo: "+idModulo);
					LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceModulo: "+codiceModulo);
				}
			}
			Boolean conPresenzaIstanze = validaBoolean(conPresenzaIstanzeQP);
			String fields = validaFields(fieldsQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			String nomePortale = retrievePortalName(httpRequest);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN nomePortale: "+nomePortale);
			}
			
			//
			ModuliFilter filter = new ModuliFilter();
			if (codiceModulo!=null) {
				filter.setCodiceModulo(codiceModulo);
			}
			if (idModulo!=null) {
				filter.setIdModulo(idModulo);
			}
//			filter.setDataEntroIntDiPubblicazione(true);
			filter.setConPresenzaIstanzeUser(Boolean.TRUE.equals(conPresenzaIstanze)?user.getIdentificativoUtente():null);
			filter.setNomePortale((nomePortale.equals("localhost")) ? "*" : nomePortale);
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
			filter.setIdAmbito(user.getIdAmbito());
			filter.setIdVisibilitaAmbito(filter.getIdAmbito().isPresent()?null:ModuliFilter.VISIBILITA_AMBITO_PUBLIC);
			
			List<Modulo> elenco = moduliService.getElencoModuli(filter);
			LOG.info("[" + CLASS_NAME + "::getModuli] " + (elenco!=null?elenco.size():0) + " moduli trovati.");
			return Response.ok(elenco).build();
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

	
	@Override
	public Response getModuloById(Long idModuloPP, Long idVersioneModuloPP, String fields,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		try {

			if (LOG.isDebugEnabled()) {

				LOG.debug("[" + CLASS_NAME + "::getModuloById] IN idModuloPP: " + idModuloPP);
				LOG.debug("[" + CLASS_NAME + "::getModuloById] IN idVersioneModuloPP: " + idVersioneModuloPP);
				LOG.debug("[" + CLASS_NAME + "::getModuloById] IN fields: " + fields);

			}

			Modulo modulo = moduliService.getModuloById(idModuloPP, idVersioneModuloPP, fields);
			return Response.ok(modulo).build();

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] modulo non trovato " + idModuloPP + "/" + idVersioneModuloPP,
					notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore servizio getModuloById " + idModuloPP + "/"
					+ idVersioneModuloPP, e);
			throw new ServiceException("Errore servizio get modulo");
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] Errore UnprocessableEntityException " + idModuloPP + "/"
					+ idVersioneModuloPP, uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore generico servizio getModuloById " + idModuloPP + "/"
					+ idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio get modulo");
		}
	}

	@Override
	public Response getModuloByCodice( String codiceModuloPP, String versionePP,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] IN codiceModuloPP: "+codiceModuloPP);
				LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] IN versionePP: "+versionePP);
			}
			String codiceModulo = validaStringRequired(codiceModuloPP);
			String versione = validaStringRequired(versionePP);
			//
			Modulo modulo = moduliService.getModuloByCodice(codiceModulo, versione);
			return Response.ok(modulo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] modulo non trovato" + codiceModuloPP + "/" + versionePP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] Errore servizio getModuloByCodice " + codiceModuloPP + "/" + versionePP, e);
			throw new ServiceException("Errore servizio get modulo by codice");
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] Errore UnprocessableEntityException " + codiceModuloPP + "/" + versionePP, uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] Errore generico servizio getModuloByCodice "  + codiceModuloPP + "/" + versionePP, ex);
			throw new ServiceException("Errore generico servizio get modulo by codice");
		}
	}
	
	@Override
	public Response getStatoModuloById(String idModuloPP, String idVersioneModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuloById] IN idModuloPP: "+idModuloPP);
				LOG.debug("[" + CLASS_NAME + "::getModuloById] IN idVersioneModuloPP: "+idVersioneModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			StatoModulo statoModulo = moduliService.getStatoModuloById(idModulo, idVersioneModulo);
			return Response.ok(statoModulo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] modulo non trovato " + idModuloPP + "/" + idVersioneModuloPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore servizio getModuloById " + idModuloPP + "/" + idVersioneModuloPP, e);
			throw new ServiceException("Errore servizio get modulo");
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] Errore UnprocessableEntityException " + idModuloPP + "/" + idVersioneModuloPP, uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore generico servizio getModuloById " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio get modulo");
		}
	}


}
