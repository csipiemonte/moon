/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.doc;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.http.MediaType;

import it.csi.apimint.mydocs.be.v1.dto.FiltroDocumento;

@Path("/extra/doc/mydocs")
@Produces(MediaType.APPLICATION_JSON_VALUE)
public interface MyDocsApi {

	//
	// PING
    @GET
    @Path("/ping")
    @Produces({ "text/plain" })
	public Response ping(
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
	//
	// AMBITI
	//
    @GET
    @Path("/enti/{idEnte}/ambiti")
    @Produces({ "application/json" })
	public Response listAmbitiByIdEnte(@PathParam("idEnte") Long idEnte, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    @GET
    @Path("/enti/codice/{codiceEnte}/ambiti")
    @Produces({ "application/json" })
	public Response listAmbitiByCodiceEnte(@PathParam("codiceEnte") String codiceEnte, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    @GET
    @Path("/enti/codice-ipa/{codiceIpaEnte}/ambiti")
    @Produces({ "application/json" })
	public Response listAmbitiByCodiceIpaEnte(@PathParam("codiceIpaEnte") String codiceIpaEnte, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    @GET
    @Path("/enti/{idEnte}/ambiti/{idAmbito}")
    @Produces({ "application/json" })
	public Response getAmbitoById(@PathParam("idEnte") Long idEnte, @PathParam("idAmbito") Long idAmbito,
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	//
	// TIPOLOGIE
	//
    @GET
    @Path("/enti/{idEnte}/tipologie")
    @Produces({ "application/json" })
	public Response listTipologieByIdEnte(@PathParam("idEnte") Long idEnte, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    @GET
    @Path("/enti/codice/{codiceEnte}/tipologie")
    @Produces({ "application/json" })
	public Response listTipologieByCodiceEnte(@PathParam("codiceEnte") String codiceEnte, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    @GET
    @Path("/enti/codice-ipa/{codiceIpaEnte}/tipologie")
    @Produces({ "application/json" })
	public Response listTipologieByCodiceIpaEnte(@PathParam("codiceIpaEnte") String codiceIpaEnte, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    @GET
    @Path("/enti/{idEnte}/tipologie/{idTipologia}")
    @Produces({ "application/json" })
	public Response getTipologiaById(@PathParam("idEnte") Long idEnte, @PathParam("idTipologia") Long idTipologia,
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
	//
	// DOCUMENTI
    // generici
    @GET
    @Path("/enti/{idEnte}/documenti")
    @Produces({ "application/json" })
    public Response getDocumenti(@PathParam("idEnte") Long idEnte, FiltroDocumento filtro,
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	//
	// DOCUMENTI
	// specifici
    @POST
    @Path("/pubblica-istanza/{idIstanza}")
//    @Produces({ "plain/text" })
    @Produces({ "application/json" })
	public Response pubblicaIstanza(@PathParam("idIstanza") Long idIstanza, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    @POST
    @Path("/pubblica-file/{idFile}")
//    @Produces({ "plain/text" })
    @Produces({ "application/json" })
	public Response pubblicaFile(@PathParam("idFile") Long idFile, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @POST
    @Path("/pubblica-istanza/{idIstanza}/log-richiesta/{idRichiesta}")
//    @Produces({ "plain/text" })
    @Produces({ "application/json" })
	public Response pubblicaIstanza(@PathParam("idIstanza") Long idIstanza, @PathParam("idRichiesta") Long idRichiesta,
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    @POST
    @Path("/pubblica-file/{idFile}/log-richiesta/{idRichiesta}")
//    @Produces({ "plain/text" })
    @Produces({ "application/json" })
	public Response pubblicaFile(@PathParam("idFile") Long idFile, @PathParam("idRichiesta") Long idRichiesta, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
//    @POST
//    @Path("/pubblica-file/{idFile}/istanza/{idIstanza}/storico/{idStoricoWorkflow}")
////    @Produces({ "plain/text" })
//    @Produces({ "application/json" })
//	public Response pubblicaFile(@PathParam("idFile") Long idFile, @PathParam("idIstanza") Long idIstanza, @PathParam("idStoricoWorkflow") Long idStoricoWorkflow,
//	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
//    @POST
//    @Path("/pubblica-files/istanza/{idIstanza}/storico/{idStoricoWorkflow}")
//    @Produces({ "application/json" })
//	public Response pubblicaFiles(@PathParam("idIstanza") Long idIstanza, @PathParam("idStoricoWorkflow") Long idStoricoWorkflow,
//	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @POST
    @Path("/pubblica-mydocs/istanza/{idIstanza}/storico/{idStoricoWorkflow}")
    @Produces({ "application/json" })
	Response pubblicaMyDocs(@PathParam("idIstanza") Long idIstanza, @PathParam("idStoricoWorkflow") Long idStoricoWorkflow, 
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @GET
    @Path("/richieste/istanza/{idIstanza}")
    @Produces({ "application/json" })
	public Response listRichiesteMyDocs(@PathParam("idIstanza") Long idIstanza, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);    

}
