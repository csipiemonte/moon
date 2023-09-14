/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.ProcessiApi;
import it.csi.moon.moonbobl.business.service.ProcessiService;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.Processo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class ProcessiApiImpl extends MoonBaseApiImpl implements ProcessiApi {
	
	private final static String CLASS_NAME = "ProcessiApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ProcessiService processiService;
	
	@Override
	public Response getProcessi(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getProcessi] BEGIN");
			List<Processo> elenco = processiService.getElencoProcessi();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getProcessi] Errore servizio getProcessi",e);
			throw new ServiceException("Errore servizio elenco processi");
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::getProcessi] Errore generico servizio getProcessi",ex);
			throw new ServiceException("Errore generico servizio elenco processi");
		}  
	}
	
	@Override
	public Response getProcessoById(Long idProcesso, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Processo result = processiService.getProcessoById(idProcesso, fields);
			return Response.ok(result).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getProcessoById] processo non trovato per idProcesso" + idProcesso);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getProcessoById] Errore servizio getProcessoById",e);
			throw new ServiceException("Errore servizio get processo");
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::getProcessoById] Errore generico servizio getProcessoById",ex);
			throw new ServiceException("Errore generico servizio get processo");
		} 
	}
	
	@Override
	public Response getProcessoByCd(String codiceProcesso, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Processo result = processiService.getProcessoByCd(codiceProcesso);
			return Response.ok(result).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getProcessoByCd] processo non trovato per codiceProcesso: " + codiceProcesso);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getProcessoByCd] Errore servizio getProcessoByCd",e);
			throw new ServiceException("Errore servizio get processo");
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::getProcessoByCd] Errore generico servizio getProcessoByCd",ex);
			throw new ServiceException("Errore generico servizio get processo by Codice");
		} 
	}
	
	@Override
	public Response getProcessoByNome(String nomeProcesso, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Processo result = processiService.getProcessoByNome(nomeProcesso);
			return Response.ok(result).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getProcessoByNome] processo non trovato per nomeProcesso " + nomeProcesso);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getProcessoByNome] Errore servizio getProcessoByNome",e);
			throw new ServiceException("Errore servizio get processo");
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::getProcessoByNome] Errore generico servizio getProcessoByNome",ex);
			throw new ServiceException("Errore generico servizio get processo by nome");
		} 
	}
	
	@Override
	public Response createProcesso(Processo body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::createProcesso] IN Processo body: "+body);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Processo result = processiService.createProcesso(user, body);
			return Response.ok(result).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::createProcesso] Errore servizio createProcesso",e);
			throw new ServiceException("Errore servizio crea processo");
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::createProcesso] Errore generico servizio createProcesso",ex);
			throw new ServiceException("Errore generico servizio crea processo");
		} 
	}
	
	@Override
	public Response putProcesso(String idProcessoPP, Processo body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::putProcesso] IN idProcessoPP:" + idProcessoPP + "\nProcesso body: "+body);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idProcesso = validaLongRequired(idProcessoPP);
			if(body==null || !idProcesso.equals(body.getIdProcesso())) {
				log.error("[" + CLASS_NAME + "::putProcesso] UnprocessableEntityException  not correct between pathParam and body: idProcessoPP:" + idProcessoPP + "\nProcesso body: "+body);
				throw new UnprocessableEntityException("id_processo not correct between pathParam and body");
			}
			Processo result = processiService.updateProcesso(user, body);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::putProcesso] UnprocessableEntityException" + uee.getMessage());
			throw uee;
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::putProcesso] Errore servizio putProcesso",e);
			throw new ServiceException("Errore servizio aggiorna processo");
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::putProcesso] Errore generico servizio putProcesso",ex);
			throw new ServiceException("Errore generico servizio aggiorna processo");
		}
	}
	
	@Override
	public Response getImageById(Long idProcesso, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			byte[] imageData = processiService.getByteArrayImageById(idProcesso);

//		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		    ImageIO.write(image, "png", baos);
//		    byte[] imageData = baos.toByteArray();

		    // uncomment line below to send non-streamed
		    return Response.ok(imageData).build();

		    // uncomment line below to send streamed
		    // return Response.ok(new ByteArrayInputStream(imageData)).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.warn("[" + CLASS_NAME + "::getImageById] processo non trovato per idProcesso" + idProcesso);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.warn("[" + CLASS_NAME + "::getImageById] Errore servizio getImageById", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::getImageById] Errore generico servizio getImageById",ex);
			throw new ServiceException("Errore generico servizio getImageById");
		} 
	}
	
	//
	// Moduli
	//
	@Override
	public Response getModuliByIdProcesso(Long idProcesso, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getUtentiByIdProcesso] BEGIN");
			List<Modulo> elenco = processiService.getModuliByIdProcesso(idProcesso);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getUtentiByIdProcesso] Errore servizio getUtentiByIdProcesso",e);
			throw new ServiceException("Errore servizio elenco moduli per idProcesso");
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::getUtentiByIdProcesso] Errore generico servizio getUtentiByIdProcesso",ex);
			throw new ServiceException("Errore generico servizio elenco moduli per idProcesso");
		} 
	}
	
}
