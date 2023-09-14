/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be.extra.demografia;

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

import it.csi.moon.moonbobl.dto.extra.demografia.Cittadinanza;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;

@Path("/be/extra/demografia/cittadinanze")
@Produces({ "application/json" })
public interface CittadinanzeApi  {
   
//    @GET
//    @Produces({ "application/json" })
//    public Response getCittadinanze( @QueryParam("fields") String fields,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );   

    @GET
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Cittadinanza.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco cittadinanze", response = Cittadinanza.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getCittadinanze( @QueryParam("limit") int limit, @QueryParam("skip") int skip, @QueryParam("fields") String fields, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    
    @GET
    @Path("/{codice}")
    @Produces({ "application/json" })
    public Response getCittadinanzeById( @PathParam("codice") Integer codice, @QueryParam("fields") String fields, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException;
}
