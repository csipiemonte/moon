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

@Path("/extra/demografia/cittadinanze")
@Produces({ "application/json" })
public interface CittadinanzeApi  {
   
    @GET
    public Response getCittadinanze( @QueryParam("limit") int limit, @QueryParam("skip") int skip, @QueryParam("fields") String fields, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{codice}")
    @Produces({ "application/json" })
    public Response getCittadinanzeById( @PathParam("codice") Integer codice, @QueryParam("fields") String fields, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
}
