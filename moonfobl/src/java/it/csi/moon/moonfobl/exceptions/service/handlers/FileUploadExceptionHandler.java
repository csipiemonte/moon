/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.exceptions.service.handlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import it.csi.moon.moonfobl.exceptions.service.FileUploadException;

@Provider
@Component
public class FileUploadExceptionHandler implements ExceptionMapper<FileUploadException> {
	@Override
	public Response toResponse(FileUploadException ex) {
		Status status = "ERR".equals(ex.getCode())?Status.INTERNAL_SERVER_ERROR:Status.BAD_REQUEST;
		return Response.status(status)
	            .entity(ex.getMessage()).type(MediaType.TEXT_PLAIN)
	            .build();
	}
}
