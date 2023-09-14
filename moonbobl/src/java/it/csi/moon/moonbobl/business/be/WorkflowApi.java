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
import javax.ws.rs.PATCH;
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

import it.csi.moon.moonbobl.dto.moonfobl.Azione;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;

@Path("/be/workflows")
@Produces({ "application/json" })
public interface WorkflowApi  {
   
    @GET
    @Path("/processi")
    public Response getElencoProcessi(
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/istanza/{idIstanza}")
    public Response getWorkflowByIdIstanza( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
   
    @GET
    @Path("/istanza/{idIstanza}/ultimo")
    public Response getUltimoWorkflowByIdIstanza( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/istanza/{idIstanza}/storico")
    public Response getStoricoWorkflowByIdIstanza( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
   
    @GET
    @Path("/istanza/{idIstanza}/struttura/{idWorkflow}")
    public Response getStrutturaByIdWorkflow( @PathParam("idIstanza") Long idIstanza, @PathParam("idWorkflow") Long idWorkflow,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idWorkflow}")
    @Produces({ "application/json" })
    public Response getWorkflowByIdWorkflow( @PathParam("idWorkflow") Long idWorkflow, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/storico/{idStoricoWorkflow}")
    public Response getStoricoWorkflowById( @PathParam("idStoricoWorkflow") Long idWorkflow,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    
    @POST
    @Path("/istanza/{idIstanza}/compieAzione")
    public Response compieAzione( @PathParam("idIstanza") Long idIstanza, Azione body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
 
    @POST
    @Path("/istanza/{idIstanza}/completaAzione")
    @Consumes({ "application/json" })
    public Response completaAzione( @PathParam("idIstanza") Long idIstanza, Azione body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @POST
    @Path("/istanza/{idIstanza}/motivaModifica")
    @Consumes({ "application/json" })
    public Response motivaModifica( @PathParam("idIstanza") Long idIstanza, Azione body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @POST
    @Path("/istanza/{idIstanza}/annullaAzione")
    @Consumes({ "application/json" })
    public Response annullaAzione( @PathParam("idIstanza") Long idIstanza, Azione body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/istanza/{idIstanza}/init/{idWorkflow}")
    public Response getInitDatiAzioneaByIdWorkflow( @PathParam("idIstanza") Long idIstanza, @PathParam("idWorkflow") Long idWorkflow, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/struttura/{codiceAzione}")
	Response getStrutturaByCodiceAzione( @PathParam("codiceAzione") String codiceAzione,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/modulo/{idModulo}/dopo-inviata")
    public Response getAzioneWorkflowDaEseguireDopoInviata( @PathParam("idModulo") Long idModulo,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    

    @POST
    @Path("/mydocs/{idFile}")
	Response pubblicaFileMyDocs(@PathParam("idFile") Long idFile,
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @POST
    @Path("/mydocs/{idFile}/log-richiesta/{idRichiesta}")
	Response pubblicaFileMyDocs(@PathParam("idFile") Long idFile,@PathParam("idRichiesta") Long idRichiesta,
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
	// CRUD DettaglioProcesso
    @GET
    public Response getWorkflowsByIdProcesso(@QueryParam("idProcesso") String idProcessoQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @POST
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveWorkflow(Workflow workflow,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PATCH
    @Path("/{idWorkflow}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response patchWorkflowById(@PathParam("idWorkflow") Long idWorkflow, Workflow partialWorkflow,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @DELETE
    @Path("/{idWorkflow}")
    public Response deleteWorkflow(@PathParam("idWorkflow") String idWorkflowPP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}
