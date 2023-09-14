/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.exceptions.service.handlers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonbobl.exceptions.service.handlers.dto.ServiceResponse;
import it.csi.moon.moonbobl.util.JsonHelper;

@Provider
@Component
public class UnauthorizedExceptionHandler implements ExceptionMapper<UnauthorizedException> {

	@Override
	public Response toResponse(UnauthorizedException resEx) {
		ServiceResponse sr = new ServiceResponse(resEx.getCode(),resEx.getMessage() );		
		return Response.status(401).entity(JsonHelper.getJsonFromObject(sr) ).build(); 
	}

}
