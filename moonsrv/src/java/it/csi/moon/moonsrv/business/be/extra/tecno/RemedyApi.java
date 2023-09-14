/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.tecno;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.apimint.troubleticketing.v1.dto.Ticket;

@Path("/extra/tecno/remedy")
@Produces({ "application/json" })
public interface RemedyApi {

	@GET
	@Path("/users")
    @Produces({ "application/json" })
	public Response getRichiedenteDaAnagrafica( @QueryParam("email") String email, @QueryParam("cognome") String cognome, @QueryParam("nome") String nome,
	    @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("/users/{personId}")
    @Produces({ "application/json" })
	public Response getRichiedenteDaAnagraficaById( @PathParam("personId") String personId,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@GET
	@Path("/enti")
    @Produces({ "application/json" })
	public Response getEnti(
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@GET
	@Path("/categorie-operative")
    @Produces({ "application/json" })
	public Response getCategorieOperative(
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@GET
	@Path("/categorie-applicative")
    @Produces({ "application/json" })
	public Response getCategorieApplicative(
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@GET
	@Path("/users/{personId}/configuration-items")
    @Produces({ "application/json" })
	public Response getConfigurationItems( @PathParam("personId") String personId,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	
	//
	//
	// Entity Ticket
	@POST
	@Path("/tickets")
    @Produces({ "application/json" })
	public Response createTicket( Ticket ticket,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	@GET
	@Path("/tickets/{ticketId}/stato")
    @Produces({ "application/json" })
	public Response getWorkinfoTicket( @PathParam("ticketId") String ticketId,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	@GET
	@Path("/tickets/monitoraggio")
    @Produces({ "application/json" })
	public Response getLastUpdated(
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	@GET
	@Path("/tickets/esposizione")
    @Produces({ "application/json" })
	public Response getLastRegistered(
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}
