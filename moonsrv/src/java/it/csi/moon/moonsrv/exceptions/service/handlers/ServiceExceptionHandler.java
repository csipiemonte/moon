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
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.handlers.dto.ServiceResponse;

@Provider
@Component
public class ServiceExceptionHandler  implements ExceptionMapper<ServiceException> {

	@Override
	public Response toResponse(ServiceException serviceException ) {
		ServiceResponse sr = new ServiceResponse(serviceException.getCode(), serviceException.getMessage(), serviceException.getTitle() );
		return Response.status(Status.BAD_REQUEST).entity(JsonHelper.getJsonFromObject(sr) ).build();  
	}

}
