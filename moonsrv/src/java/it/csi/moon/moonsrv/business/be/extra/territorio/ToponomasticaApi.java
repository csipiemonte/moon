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

import it.csi.moon.commons.dto.extra.territorio.Civico;
import it.csi.moon.commons.dto.extra.territorio.Piano;
import it.csi.moon.commons.dto.extra.territorio.PianoNUI;
import it.csi.moon.commons.dto.extra.territorio.UiuCivicoIndirizzo;
import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;

@Path("/extra/territorio/toponomastica")
@Produces({ "application/json" })
public interface ToponomasticaApi {

	//
	// Entity Via
    @GET
    @Path("/vie")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Via.class, responseContainer = "List", tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco vie", response = Via.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getVie( @QueryParam("limit") int limit, @QueryParam("skip") int skip, @QueryParam("filter") String filter, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException;

    @GET
    @Path("/vie/{codiceVia}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Via.class, tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "la via corrispondente al codice fornito nel path", response = Via.class),
	    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la via non esiste", response = Error.class),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getViaById( @PathParam("codiceVia") String codiceVia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException;

    
    //
    // Numero Radici
    @GET
    @Path("/vie/{codiceVia}/numeri")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Integer.class, responseContainer = "List", tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco dei numeri radici corrispondente al codice fornito nel path", response = Integer.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la via non esiste", response = Error.class),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
	public Response getNumeriRadiceByVia( @PathParam("codiceVia") String codiceVia,
	    @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException; 
    
    
    //
	// Entity Civico
    @GET
    @Path("/vie/{codiceVia}/civici")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Civico.class, responseContainer = "List", tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco civici", response = Civico.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getCiviciByVia( @PathParam("codiceVia") String codiceVia, @QueryParam("tipologieCivico") String tipologieCivico, @QueryParam("stati") String stati, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException;
    
    @GET
    @Path("/vie/{codiceVia}/civici/{codiceCivico}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Civico.class, tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "la regione corrispondente al codice fornito nel path", response = Civico.class),
	    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la regione non esiste", response = Error.class),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getCivicoById( @PathParam("codiceVia") String codiceVia, @PathParam("codiceCivico") String codiceCivico, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException;
    
    @GET
    @Path("/vie/{codiceVia}/numeri/{numero}/civici")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Civico.class, responseContainer = "List", tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco civici", response = Civico.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getCiviciByViaNumero( @PathParam("codiceVia") String codiceVia, @PathParam("numero") String numero, @QueryParam("tipologieCivico") String tipologieCivico, @QueryParam("stati") String stati, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException;

    
    //
	// Entity PianoNUI
    @GET
    @Path("/vie/{codiceVia}/civici/{codiceCivico}/pianinui")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = PianoNUI.class, responseContainer = "List", tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco piani nui", response = PianoNUI.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getPianiNuiByCivico( @PathParam("codiceVia") String codiceVia, @PathParam("codiceCivico") String codiceCivico, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException;
    
    @GET
    @Path("/vie/{codiceVia}/civici/{codiceCivico}/pianinui/{codicePianoNui}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = PianoNUI.class, tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "il piano nui", response = PianoNUI.class),
	    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per il piano nui non esiste", response = Error.class),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getPianoNuiById( @PathParam("codiceVia") String codiceVia, @PathParam("codiceCivico") String codiceCivico, @PathParam("codicePianoNui") String codicePianoNui, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException;

   
    //
	// Entity Piano
    @GET
    @Path("/piani")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Piano.class, responseContainer = "List", tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco piani nui", response = Piano.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getPiani(
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException;

    @GET
    @Path("/piani/{codicePiano}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Piano.class, tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "la via corrispondente al codice fornito nel path", response = Piano.class),
	    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la via non esiste", response = Error.class),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getPianoById( @PathParam("codicePiano") String codicePiano,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException;

    @GET
    @Path("/vie/{codiceVia}/civici/{codiceCivico}/piani")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Piano.class, responseContainer = "List", tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco piani", response = Piano.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getPianiByCivico( @PathParam("codiceVia") String codiceVia, @PathParam("codiceCivico") String codiceCivico, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException;

    @GET
    @Path("/civici/foglio/{foglio}/numero/{numero}/subalterno/{subalterno}/indirizzo")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = UiuCivicoIndirizzo.class, responseContainer = "List", tags={ "territorio", "toponomastica" })
    @io.swagger.annotations.ApiResponses(value = { 
	    @io.swagger.annotations.ApiResponse(code = 200, message = "elenco UiuCivicoIndirizzo", response = UiuCivicoIndirizzo.class, responseContainer = "List"),
	    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getUiuCivicoIndirizzoByTriplettaCatastale( @PathParam("foglio") String foglio, @PathParam("numero") String numero, @PathParam("subalterno") String subalterno, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException;
 }
