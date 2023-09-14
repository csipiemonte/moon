/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.exceptions.service.handlers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import it.csi.moon.commons.util.JsonHelper;
import it.csi.moon.moonfobl.exceptions.service.LoginException;
import it.csi.moon.moonfobl.exceptions.service.handlers.dto.ServiceResponse;

@Provider
@Component
public class LoginExceptionHandler  implements ExceptionMapper<LoginException> {

	@Override
	public Response toResponse(LoginException loginException ) {
		ServiceResponse sr = new ServiceResponse(loginException.getErrCode(),loginException.getMessage() );
		// 401
		return Response.status(403).entity(JsonHelper.getJsonFromObject(sr) ).build();  
	}
	
}
