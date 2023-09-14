/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.RepositoryFile;
import it.csi.moon.commons.entity.RepositoryFileFilter;
import it.csi.moon.moonsrv.business.be.RepositoryFileApi;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.ProtocolloService;
import it.csi.moon.moonsrv.business.service.RepositoryFileService;
import it.csi.moon.moonsrv.business.service.doc.MyDocsService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class RepositoryFileApiImpl extends MoonBaseApiImpl implements RepositoryFileApi {

	private static final String CLASS_NAME = "RepositoryFileApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	RepositoryFileService repositoryFileService;
	@Autowired
	ProtocolloService protocolloService;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	MyDocsService mydocsService;

	@Override
	public Response getElencoRepositoryFile(String idIstanzaQP, String fieldsQP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[" + CLASS_NAME + "::getElencoRepositoryFile] idIstanzaQP = " + idIstanzaQP + "  fields = " + fieldsQP);
		try {
			Long idIstanza = validaLongRequired(idIstanzaQP);
			RepositoryFileFilter filter = new RepositoryFileFilter();
			filter.setIdIstanza(idIstanza);
			List<RepositoryFile> elenco = repositoryFileService.getElencoRepositoryFile(filter, fieldsQP);
			return Response.ok(elenco).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getElencoRepositoryFile] idIstanzaQP Long required", uee);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoRepositoryFile] Errore servizio getElencoRepositoryFile", e);
			throw new ServiceException("Errore servizio getElencoRepositoryFile");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoRepositoryFile] Errore generico servizio getElencoRepositoryFile", ex);
			throw new ServiceException("Errore generico servizio getElencoRepositoryFile");
		}
	}
	
	@Override
	public Response getRepositoryFileById(String idFilePP, String fieldsQP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[" + CLASS_NAME + "::getFile] idFilePP = " + idFilePP + "  fields = " + fieldsQP);
		try {
			Long idFile = validaLongRequired(idFilePP);
			RepositoryFile ris = repositoryFileService.getRepositoryFileById(idFile, fieldsQP);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileById] idFile Long required", uee);
			throw new ResourceNotFoundException();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileById] file non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileById] Errore servizio getRepositoryFileById", e);
			throw new ServiceException("Errore servizio getRepositoryFileById");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileById] Errore generico servizio getRepositoryFileById", ex);
			throw new ServiceException("Errore generico servizio getRepositoryFileById");
		}
	}
	
	@Override
	public Response getFile(Long idFile, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[" + CLASS_NAME + "::getFile] idFile = " + idFile);
		try {
			byte[] bytes = repositoryFileService.getContenutoRepositoryFile(idFile);
			
			LOG.debug("[" + CLASS_NAME + "::getFile] bytes = " + bytes);

			return Response.ok(bytes).header("Cache-Control", "no-cache, no-store, must-revalidate")
					.header("Pragma", "no-cache").header("Expires", "0")
					.header(HttpHeaders.CONTENT_TYPE, "application/pdf")
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=RicevutaAccettazione.pdf")
					.header(HttpHeaders.CONTENT_LENGTH, bytes.length).build();

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getFile] file non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getFile] Errore servizio getFile", e);
			throw new ServiceException("Errore servizio getFile");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getFile] Errore generico servizio getFile", ex);
			throw new ServiceException("Errore generico servizio getFile");
		}
	}

	@Override
	public Response postProtocolla(Long idFile,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::postProtocolla] IN idFile: " + idFile);
			protocolloService.protocollaFile(idFile);
			LOG.debug("[" + CLASS_NAME + "::postProtocolla] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postProtocolla] file non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postProtocolla] Errore servizio postProtocolla", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postProtocolla] Errore generico servizio postProtocolla", ex);
			throw new ServiceException("Errore generico servizio protocolla file");
		} 
	}

//	@Override
//	public Response getDocumentoByFormioNameFile(String formioNameFile, SecurityContext securityContext,
//			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//		try {
//			LOG.debug("[" + CLASS_NAME + "::getNotificaByFormioNameFile] IN idIstanza: " + formioNameFile);
//			byte[] bytes = printIstanzeService.getDocumentoByFormioNameFile(formioNameFile);					
//			return Response.ok(bytes).build();
//		} catch (ItemNotFoundBusinessException notFoundEx) {
//			LOG.error("[" + CLASS_NAME + "::getNotificaByFormioNameFile] dovcumento non trovato", notFoundEx);
//			throw new ResourceNotFoundException();
//		} catch (BusinessException e) {
//			LOG.error("[" + CLASS_NAME + "::getNotificaByFormioNameFile] Errore servizio", e);
//			throw new ServiceException("Errore servizio getNotificaByFormioNameFile");
//		} catch (Exception ex) {
//			LOG.error("[" + CLASS_NAME + "::getNotificaByFormioNameFile] Errore generico servizio", ex);
//			throw new ServiceException("Errore generico servizio getNotificaByFormioNameFile");
//		}
//	}
//
//	@Override
//	public Response getDocumentoByIdFile(Long idFile, SecurityContext securityContext, HttpHeaders httpHeaders,
//			HttpServletRequest httpRequest) {
//		try {
//			LOG.debug("[" + CLASS_NAME + "::getDocumentoByIdFile] IN idIstanza: " + idFile);
//			byte[] bytes = printIstanzeService.getDocumentoByIdFile(idFile);					
//			return Response.ok(bytes).build();
//		} catch (ItemNotFoundBusinessException notFoundEx) {
//			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] dovcumento non trovato", notFoundEx);
//			throw new ResourceNotFoundException();
//		} catch (BusinessException e) {
//			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] Errore servizio", e);
//			throw new ServiceException("Errore servizio getDocumentoByIdFile");
//		} catch (Exception ex) {
//			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] Errore generico servizio", ex);
//			throw new ServiceException("Errore generico servizio getDocumentoByIdFile");
//		}
//			
//	}

//	@Override
//	public Response postPubblicaMydocs(Long idFile,
//		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//		try {
//			LOG.debug("[" + CLASS_NAME + "::postPubblicaMydocs] IN idFile: " + idFile);
//			String ris = mydocsService.pubblicaFile(idFile);
//			LOG.debug("[" + CLASS_NAME + "::postPubblicaMydocs] END");
//	        return Response.ok(ris).build();
//		} catch(ItemNotFoundBusinessException notFoundEx) {
//			LOG.error("[" + CLASS_NAME + "::postPubblicaMydocs] file non trovato", notFoundEx);
//			throw new ResourceNotFoundException();
//		} catch (BusinessException be) {
//			LOG.error("[" + CLASS_NAME + "::postPubblicaMydocs] Errore servizio postProtocolla", be);
//			throw new ServiceException(be);
//		} catch (Exception ex) {
//			LOG.error("[" + CLASS_NAME + "::postPubblicaMydocs] Errore generico servizio postPubblicaMydocs", ex);
//			throw new ServiceException("Errore generico servizio pubblica on Mydocs file");
//		} 
//	}
	
}
