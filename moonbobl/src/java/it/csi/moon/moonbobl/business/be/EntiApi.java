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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonbobl.dto.moonfobl.Ente;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;

@Path("/be/enti")
@Produces(MediaType.APPLICATION_JSON)
public interface EntiApi {

    @GET
    public Response getEnti( @QueryParam("idEnte") String idEnteQP, @QueryParam("codiceEnte") String codiceEnteQP, @QueryParam("nomeEnte") String nomeEnteQP,
		@QueryParam("descrizioneEnte") String descrizioneEnteQP, @QueryParam("flagAttivo") String flagAttivoQP,	@QueryParam("idTipoEnte") String idTipoEnteQP,
		@QueryParam("logo") String logoQP, /*@QueryParam("nomePortale") String nomePortaleQP,*/ @QueryParam("idPortale") String idPortaleQP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ServiceException;

    @GET
    @Path("/{idEnte}")
    public Response getEnteById(@PathParam("idEnte") Long idEnte, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/codice/{codiceEnte}")
    public Response getEnteByCd(@PathParam("codiceEnte") String codiceEnte, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEnte( Ente body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;

    @PUT
    @Path("/{idEnte}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putEnte( @PathParam("idEnte") Long idEnte, Ente body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ServiceException;

    @GET
    @Path("/{idEnte}/aree")
    public Response getAreeByIdEnte(@PathParam("idEnte") Long idEnte, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;

    @GET
    @Path("/{idEnte}/aree/{idArea}")
    public Response getAreaById(@PathParam("idEnte") Long idEnte, @PathParam("idArea") Long idArea, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

}
