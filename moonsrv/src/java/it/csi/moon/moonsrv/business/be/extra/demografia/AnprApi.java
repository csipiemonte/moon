/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonsrv.business.be.extra.demografia;

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

import it.csi.apimint.demografia.v1.dto.Soggetto;
import it.csi.moon.commons.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.commons.dto.extra.demografia.RelazioneParentela;

@Path("/extra/demografia/anpr")
@Produces({ "application/json" })
public interface AnprApi  {

    //
    // RELAZIONI PARENTELA - ANPR
    //
    @GET
    @Path("/relazioniParentela")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = RelazioneParentela.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "le relazioni di parentela", response = RelazioneParentela.class, responseContainer = "List"),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getRelazioniParentela( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/relazioniParentela/{codice}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = RelazioneParentela.class, tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "la relazione di parentela corrispondente al codice fornito nel path", response = RelazioneParentela.class),
    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la relazione di parentela non esiste", response = Error.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getRelazioneParentelaById( @PathParam("codice") Integer codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
        
    //
    // NAZIONI - DA SATI ESTERI ANPR
    // uso:	RES, CIT, NAS   ue: UE, NONUE, UENOITA
    //
    @GET
    @Path("/nazioni")
    @Produces({ "application/json" })
    public Response getNazioni( @QueryParam("uso") String uso, @QueryParam("ue") String ue, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/nazioni/{codice}")
    @Produces({ "application/json" })
    public Response getNazioneById( @PathParam("codice") Integer codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    //
    // getComponentiFamigliaANPR
    //
    @GET
    @Path("/componentiFamigliaByCF/{codiceFiscale}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = ComponenteFamiglia.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "le componenti della famiglia", response = ComponenteFamiglia.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per un componente non esiste", response = Error.class),
	    @io.swagger.annotations.ApiResponse(code = 400, message = "errore generico", response = Error.class) })
    public Response getComponentiFamigliaANPR( @PathParam("codiceFiscale") String codiceFiscale, 
    		@QueryParam("userJwt") String userJwt, @QueryParam("clientProfileKey") String clientProfileKey, 
    		@QueryParam("fields") String fields,
    		@QueryParam("ipAdress") String ipAdress, @QueryParam("utente") String utente,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    //
    // getSoggettiFamigliaByCF
    //
    @GET
    @Path("/soggettiFamigliaByCF/{codiceFiscale}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Soggetto.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "le componenti della famiglia", response = Soggetto.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per un componente non esiste", response = Error.class),
	    @io.swagger.annotations.ApiResponse(code = 400, message = "errore generico", response = Error.class) })
    public Response getSoggettiFamigliaByCF( @PathParam("codiceFiscale") String codiceFiscale, 
    		@QueryParam("userJwt") String userJwt, @QueryParam("clientProfileKey") String clientProfileKey, 
    		@QueryParam("fields") String fields, 
    		@QueryParam("ipAdress") String ipAdress, @QueryParam("utente") String utente,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    //
    // getSoggetto
    // USE by LogonModeEnum.CF_DOCUMENTO Modulistica
    @GET
    @Path("/soggetti/{codiceFiscale}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Soggetto.class, tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "il soggetto", response = Soggetto.class),
	    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per il soggetto non esiste", response = Error.class),
	    @io.swagger.annotations.ApiResponse(code = 400, message = "errore generico", response = Error.class) })
    public Response getSoggettoANPR(@PathParam("codiceFiscale") String codiceFiscale, @QueryParam("userJwt") String userJwt, 
    		@QueryParam("clientProfileKey") String clientProfileKey, 
    		@QueryParam("fields") String fields, 
    		@QueryParam("ipAdress") String ipAdress, @QueryParam("utente") String utente,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
}
