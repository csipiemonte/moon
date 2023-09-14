/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonfobl.business.be.extra.demografia;

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

import org.jboss.resteasy.annotations.cache.Cache;

@Path("/be/extra/demografia/anpr/nazioni")
@Produces({ "application/json" })
public interface NazioniApi  {
   
    @GET
    @Cache(maxAge = 1800)
    @Produces({ "application/json" })
    public Response getNazioni( @QueryParam("uso") String uso, @QueryParam("ue") String ue, @QueryParam("nome") String nome, @QueryParam("fields") String fields, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest/*, @Context Request request, @Context ServerCache cache*/ );
    
    @GET
    @Cache(maxAge = 1800)
    @Path("/{codice}")
    @Produces({ "application/json" })
    public Response getNazioneById( @PathParam("codice") Integer codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest/*, @Context Request request, @Context ServerCache cache*/ );

}
