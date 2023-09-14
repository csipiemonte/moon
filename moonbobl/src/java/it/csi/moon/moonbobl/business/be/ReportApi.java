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

import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;

@Path("/be/report")
@Produces({ "application/json" })
public interface ReportApi  {
   
    @GET
    @Produces({ "application/json" })
    public Response getNumeroModuliInviati( @QueryParam("filtro") String filtro, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/numComuni")
    @Produces({ "application/json" })
    public Response getNumeroComuniCompilanti( @QueryParam("filtro") String filtro, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/numServizi02")
    @Produces({ "application/json" })
    public Response getNumServizi02( @QueryParam("filtro") String filtro, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/totFreq02")
    @Produces({ "application/json" })
    public Response getTotFreq02( @QueryParam("filtro") String filtro, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/numServizi36")
    @Produces({ "application/json" })
    public Response getNumServizi36( @QueryParam("filtro") String filtro, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/totFreq36")
    @Produces({ "application/json" })
    public Response getTotFreq36( @QueryParam("filtro") String filtro, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/downloadReport/{idModulo}")
    @Produces({"text/csv"})
    public Response getCSVReport( @QueryParam("filtro") String filtro, @PathParam("idModulo") Long idModulo, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/downloadLargeReport/{idModulo}")
    @Produces({"text/csv"})
    public Response getLargeCSVReport( @QueryParam("filtro") String filtro, @PathParam("idModulo") String idModuloPP, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

}
