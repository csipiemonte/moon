/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.RepositoryFileApi;
import it.csi.moon.moonbobl.business.service.RepositoryFileService;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.filter.IrideIdAdapterFilter;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class RepositoryFileApiImpl extends MoonBaseApiImpl implements RepositoryFileApi {

	private final static String CLASS_NAME = "RepositoryFileApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	@Autowired
	RepositoryFileService repositoryFileService;

	@Override
	public Response getFile(Long idFile, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		log.debug("[" + CLASS_NAME + "::getFile] idFile = " + idFile);
		try {
			UserInfo user = retrieveUserInfo(httpRequest);			
			RepositoryFileEntity file = repositoryFileService.getRepositoryFile(idFile);
			byte[] bytes = file.getContenuto();

			log.debug("[" + CLASS_NAME + "::getFile] bytes = " + bytes);

			return Response.ok(bytes).header("Cache-Control", "no-cache, no-store, must-revalidate")
					.header("Pragma", "no-cache").header("Expires", "0")
					.header(HttpHeaders.CONTENT_TYPE, "application/pdf")
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=RicevutaAccettazione.pdf")
					.header(HttpHeaders.CONTENT_LENGTH, bytes.length).build();

		} catch (ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getFile] file non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getFile] Errore servizio getFile", e);
			throw new ServiceException("Errore servizio getFile");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getFile] Errore generico servizio getFile", ex);
			throw new ServiceException("Errore generico servizio getFile");
		}
	}

}
