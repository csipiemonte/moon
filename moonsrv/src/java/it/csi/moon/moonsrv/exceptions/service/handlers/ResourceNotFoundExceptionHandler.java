/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.exceptions.service.handlers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import it.csi.moon.commons.util.JsonHelper;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.handlers.dto.ServiceResponse;


@Provider
@Component
public class ResourceNotFoundExceptionHandler implements ExceptionMapper<ResourceNotFoundException> {

	@Override
	public Response toResponse(ResourceNotFoundException resEx) {
		ServiceResponse sr = new ServiceResponse(resEx.getCode(),resEx.getMessage() );		
		return Response.status(Status.NOT_FOUND).entity(JsonHelper.getJsonFromObject(sr) ).build(); 
	}

}
