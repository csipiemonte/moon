/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.edilizia;

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

import it.csi.moon.moonsrv.exceptions.service.ServiceException;


@Path("/extra/edilizia/pratiche")
@Produces({ "application/json" })
public interface GestionePraticheApi {
			
	@GET
	@Path("/registro/{registro}/progressivo/{progressivo}/anno/{anno}")
	@Produces({ "application/json" })
	public Response getPratica(@PathParam("registro") String registro, @PathParam("progressivo") String progressivo, @PathParam("anno") String anno,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;
	
	@GET
	@Path("/registro/{registro}/progressivo/{progressivo}/anno/{anno}/string")
	@Produces({ "plain/text" })
	public Response getPraticaJson(@PathParam("registro") String registro, @PathParam("progressivo") String progressivo,@PathParam("anno") String anno,  
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;	
	
//	@GET
//	@Path("/elenco/registro/{registro}/progressivo/{progressivo}/anno/{anno}")
//	@Produces({ "application/json" })
//	public Response getPratiche(@PathParam("registro") String registro, @PathParam("progressivo") String progressivo,@PathParam("anno") String anno,  
//			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;	
	
	@GET
	@Produces({ "application/json" })
	public Response getPratiche(@QueryParam("registro") String registro, @QueryParam("progressivo") String progressivo,@QueryParam("anno") String anno,  
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;	
	
	@GET
	@Path("/ping")
	@Produces({ "application/json" })
	public Response getPing(
			@Context SecurityContext securityContext,@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;	

	
}
