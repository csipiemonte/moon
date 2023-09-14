/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.commons.dto.Azione;

@Path("/be/workflows")
@Produces({ "application/json" })
public interface WorkflowApi  {
   
    @GET
    @Path("/istanza/{idIstanza}")
    @Produces({ "application/json" })
    public Response getWorkflowByIdIstanza( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
   
    @GET
    @Path("/istanza/{idIstanza}/ultimo")
    @Produces({ "application/json" })
    public Response getUltimoWorkflowByIdIstanza( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/istanza/{idIstanza}/storico")
    @Produces({ "application/json" })
    public Response getStoricoWorkflowByIdIstanza( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
   
    @GET
    @Path("/istanza/{idIstanza}/struttura/{idWorkflow}")
    @Produces({ "application/json" })
    public Response getStrutturaByIdWorkflow( @PathParam("idIstanza") Long idIstanza, @PathParam("idWorkflow") Long idWorkflow,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idWorkflow}")
    @Produces({ "application/json" })
    public Response getWorkflowByIdWorkflow( @PathParam("idWorkflow") Long idWorkflow,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/storico/{idStoricoWorkflow}")
    @Produces({ "application/json" })
    public Response getStoricoWorkflowById( @PathParam("idStoricoWorkflow") Long idWorkflow,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    
    @POST
    @Path("/istanza/{idIstanza}/compieAzione")
    @Produces({ "application/json" })
    public Response compieAzione( @PathParam("idIstanza") Long idIstanza, Azione body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
 
    @POST
    @Path("/istanza/{idIstanza}/completaAzione")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response completaAzione( @PathParam("idIstanza") Long idIstanza, Azione body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @POST
    @Path("/istanza/{idIstanza}/annullaAzione")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response annullaAzione( @PathParam("idIstanza") Long idIstanza, Azione body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/istanza/{idIstanza}/init/{idWorkflow}")
    @Produces({ "application/json" })
    public Response getInitDatiAzioneaByIdWorkflow( @PathParam("idIstanza") Long idIstanza, @PathParam("idWorkflow") Long idWorkflow, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/azione/{idAzione}")
    @Produces({ "application/json" })
    public Response getWorkflowByIdAzione( @PathParam("idAzione") Long idAzione,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );    

}
