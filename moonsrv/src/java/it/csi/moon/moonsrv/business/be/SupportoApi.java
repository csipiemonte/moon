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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.commons.dto.RichiestaSupporto;

@Path("/supporto")
@Produces(MediaType.APPLICATION_JSON)
public interface SupportoApi  {

    @GET
    public Response getElencoSupporto( @QueryParam("idRichiestaSupporto") Long idRichiestaSupporto, @QueryParam("idIstanza") Long idIstanza, 
    	@QueryParam("idModulo") Long idModulo, @QueryParam("flagInAttesaDiRisposta") String flagInAttesaDiRisposta, 
    	@QueryParam("descMittente") String descMittente, @QueryParam("emailMittente") String emailMittente, @QueryParam("attoreIns") String attoreIns, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idRichiestaSupporto}")
    public Response getRichiestaSupportoById( @PathParam("idRichiestaSupporto") Long idRichiestaSupporto, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRichiesta( RichiestaSupporto body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

//    @PUT
//    @Path("/{idRichiestaSupporto}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response putRichiesta( @PathParam("idRichiestaSupporto") Long idRichiestaSupporto, RichiestaSupporto body, 
//    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}
