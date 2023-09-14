/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.exceptions.service.handlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import it.csi.moon.commons.util.JsonHelper;
import it.csi.moon.moonfobl.exceptions.service.ForbiddenException;
import it.csi.moon.moonfobl.exceptions.service.handlers.dto.ServiceResponse;

@Provider
@Component
public class ForbiddenExceptionHandler implements ExceptionMapper<ForbiddenException> {

	@Override
	public Response toResponse(ForbiddenException e) {
		ServiceResponse sr = new ServiceResponse(e.getCode(), e.getMessage(), e.getTitle() );
		return Response.status(403).entity(JsonHelper.getJsonFromObject(sr) ).type(MediaType.APPLICATION_JSON).build(); 
	}

}
