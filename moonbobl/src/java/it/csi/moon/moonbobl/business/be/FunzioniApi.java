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

import it.csi.moon.moonbobl.dto.moonfobl.Funzione;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;

@Path("/be/funzioni")
@Produces({ "application/json" })
public interface FunzioniApi  {

    @GET
    public Response getFunzioni( 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ServiceException;

    @GET
    @Path("/{idFunzione}")
    public Response getFunzioneById(@PathParam("idFunzione") Integer idFunzione, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveFunzione( Funzione body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ServiceException;

    @PUT
    @Path("/{idFunzione}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putFunzione( @PathParam("idFunzione") Integer idFunzione, Funzione body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ServiceException;

}
