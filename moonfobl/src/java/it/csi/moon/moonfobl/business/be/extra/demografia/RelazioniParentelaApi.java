/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonfobl.business.be.extra.demografia;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.commons.dto.extra.demografia.RelazioneParentela;

@Path("/be/extra/demografia")
@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the demografia API")

public interface RelazioniParentelaApi  {

    @GET
    @Path("/relazioniParentela")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = RelazioneParentela.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "le relazioni di parentela", response = RelazioneParentela.class, responseContainer = "List"),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getRelazioniParentela( @QueryParam("fields") String fields,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/relazioniParentela/{codice}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = RelazioneParentela.class, tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "la relazione di parentela corrispondente al codice fornito nel path", response = RelazioneParentela.class),
    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la relazione di parentela non esiste", response = Error.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getRelazioneParentelaById( @PathParam("codice") Integer codice, @QueryParam("fields") String fields,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
}
