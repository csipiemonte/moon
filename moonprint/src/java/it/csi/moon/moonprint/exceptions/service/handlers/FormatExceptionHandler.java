/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.exceptions.service.handlers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import it.csi.moon.moonprint.exceptions.service.FormatException;
import it.csi.moon.moonprint.exceptions.service.handlers.dto.ServiceResponse;
import it.csi.moon.moonprint.util.JsonHelper;

@Provider
@Component
public class FormatExceptionHandler implements ExceptionMapper<FormatException> {
	@Override
	public Response toResponse(FormatException resEx) {
		ServiceResponse sr = new ServiceResponse(resEx.getErrCode(),resEx.getMessage() );		
		return Response.status(Status.NOT_FOUND).entity(JsonHelper.getJsonFromObject(sr) ).build(); 
	}
}
