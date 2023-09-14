/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonsrv.business.be;

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

import it.csi.moon.commons.dto.Processo;

@Path("/processi")
@Produces(MediaType.APPLICATION_JSON)
public interface ProcessiApi  {

    @GET
    public Response getProcessi( 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idProcesso}")
    public Response getProcessoById(@PathParam("idProcesso") Long idProcesso, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/codice/{codiceProcesso}")
    public Response getProcessoByCd(@PathParam("codiceProcesso") String codiceProcesso, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/nome/{nomeProcesso}")
    public Response getProcessoByNome(@PathParam("nomeProcesso") String nomeProcesso, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProcesso( Processo body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PUT
    @Path("/{idProcesso}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putProcesso( @PathParam("idProcesso") Long idProcesso, Processo body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    
    //
    // Moduli
    //
    @GET
    @Path("/{idProcesso}/moduli")
    public Response getModuliByIdProcesso(@PathParam("idProcesso") Long idProcesso, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}
