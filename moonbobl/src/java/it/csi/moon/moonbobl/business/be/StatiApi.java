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
import javax.ws.rs.DELETE;
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

import it.csi.moon.moonbobl.dto.moonfobl.Stato;


@Path("/be/stati")
@Produces(MediaType.APPLICATION_JSON)
public interface StatiApi  {

    @GET
    public Response getStati( @QueryParam("idModulo") Long idModulo,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/bo")
    public Response getStatiBoByModulo( @QueryParam("idModulo") Long idModulo, @QueryParam("sort") String sort, @QueryParam("filtroRicerca") String filtroRicerca,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idStato}")
    public Response getStatoById(@PathParam("idStato") Integer idStato, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/codice/{codiceStato}")
    public Response getStatoByCd(@PathParam("codiceStato") String codiceStato, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createStato( Stato body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PUT
    @Path("/{idStato}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putStato( @PathParam("idStato") Integer idStato, Stato body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @DELETE
    @Path("/{idStato}")
    public Response deleteStatoById(@PathParam("idStato") Integer idStato, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}
