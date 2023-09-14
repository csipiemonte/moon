/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonprint.business.be;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/ping")
@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the ping API")
public interface PingApi  {
   
    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    @io.swagger.annotations.ApiOperation(value = "servizio di ping del backend", notes = "Restituisce una stringa per confermare la disponibilità del backend", response = String.class, tags={ "TOH", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "stringa di conferma funzionamento", response = String.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = String.class) })
    public Response ping(@Context SecurityContext securityContext);
}
