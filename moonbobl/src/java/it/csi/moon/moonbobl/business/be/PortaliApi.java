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

import it.csi.moon.moonbobl.dto.moonfobl.Portale;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;

@Path("/be/portali")
@Produces(MediaType.APPLICATION_JSON)
public interface PortaliApi  {

    @GET
    public Response getPortali( 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idPortale}")
    public Response getPortaleById(@PathParam("idPortale") Long idPortale, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/codice/{codicePortale}")
    public Response getPortaleByCd(@PathParam("codicePortale") String codicePortale, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/nome/{nomePortale}")
    public Response getPortaleByNome(@PathParam("nomePortale") String nomePortale, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPortale( Portale body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PUT
    @Path("/{idPortale}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPortale( @PathParam("idPortale") Long idPortale, Portale body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    
    //
    // Moduli
    //
    @GET
    @Path("/{idPortale}/moduli")
    public Response getModuliByIdPortale(@PathParam("idPortale") Long idPortale, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}
