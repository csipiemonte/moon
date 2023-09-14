/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonbobl.business.service.impl.dto.PortaleModuloLogonModeEntity;

@Path("/be/modulistica")
@Produces({ "application/json" })
public interface ModulisticaApi  {
   
    @GET
    @Path("/logon-mode")
    public Response getElencoLogonMode(
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/portali-logon-mode-by-modulo/{idModulo}")
    public Response getPortaliLogonModeByIdModulo( @PathParam("idModulo") String idModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @POST
    @Path("/portale-modulo-logon-mode")
    public Response postPortaleModuloLogonMode( PortaleModuloLogonModeEntity body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @DELETE
    @Path("/portale/{idPortale}/modulo/{idModulo}")
    public Response deletePortaleModuloLogonMode( @PathParam("idPortale") String idPortalePP, @PathParam("idModulo") String idModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
}
