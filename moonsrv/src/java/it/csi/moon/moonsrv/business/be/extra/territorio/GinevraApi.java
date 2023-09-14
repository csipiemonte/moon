/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.territorio;

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

import it.csi.moon.commons.dto.extra.territorio.Via;

@Path("/extra/territorio/ginevra")
@Produces({ "application/json" })
public interface GinevraApi {
    
	//
	// SEDIMI
    @GET
    @Path("/sedimi") // comuni/{idComune}/
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Via.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco sedimi", response = Via.class, responseContainer = "List"),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getSedimi( //@PathParam("idComune") String idComunePP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/sedimi/{id}") // comuni/{idComune}/
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Via.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco sedimi", response = Via.class, responseContainer = "List"),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getSedimeById( @PathParam("id") String idPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
	
	//
	// VIE
    @GET
    @Path("/comuni/{idComune}/vie")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Via.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco vie", response = Via.class, responseContainer = "List"),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getVie( @PathParam("idComune") String idComunePP,
    	@QueryParam("nomeVia") String nomeViaQP, //@QueryParam("modoRicerca") String modoRicercaQP, 
    	@QueryParam("limit") int limit, @QueryParam("skip") int skip, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/comuni/{idComune}/vie/{idVia}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Via.class, tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "via", response = Via.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getViaById( @PathParam("idComune") String idComunePP, @PathParam("idVia") String idViaPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );    
    
	//
	// CIVICI
    @GET
    @Path("/civici/idL2/{idL2}/numero/{numero}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Via.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco civici", response = Via.class, responseContainer = "List"),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getCivici( @PathParam("idL2") String idL2PP, @PathParam("numero") String numeroPP,
    	@QueryParam("limit") int limit, @QueryParam("skip") int skip, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/civici/{idCivico}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Via.class, tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "civico", response = Via.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getCivicoById( @PathParam("idCivico") String idCivicoPP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

}
