/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be;

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

@Path("/be/ticket-crm-richieste")
@Produces({ "application/json" })
public interface TicketCrmRichiesteApi  {
   
    @GET
    public Response getTicketCrmRichieste(
		@QueryParam("idRichiesta") String idRichiestaQP, @QueryParam("codice") String codiceQP,
		@QueryParam("idIstanza") String idIstanzaQP, @QueryParam("idModulo") String idModuloQP,
		@QueryParam("tipoDoc") String tipoDocQP, @QueryParam("stato") String statoQP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/istanza-tx/{idIstanza}")
    public Response getTicketCrmRichiesteIstanzaTx(@PathParam("idIstanza") String idIstanzaPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/log/{idIstanza}")
    public Response getLogTicket(@PathParam("idIstanza") String idIstanzaPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
}
