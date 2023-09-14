/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonbobl.dto.moonfobl.Ruolo;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;


@Path("/be/ruoli")
@Produces(MediaType.APPLICATION_JSON)
public interface RuoliApi  {

    @GET
    public Response getRuoli( 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idRuolo}")
    public Response getRuoloById(@PathParam("idRuolo") Integer idRuolo, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRuolo( Ruolo body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PUT
    @Path("/{idRuolo}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putRuolo( @PathParam("idRuolo") Integer idRuolo, Ruolo body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    
    //
    // Utenti
    //
    @GET
    @Path("/{idRuolo}/utenti")
    public Response getUtentiByIdRuolo(@PathParam("idRuolo") Integer idRuolo, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}
