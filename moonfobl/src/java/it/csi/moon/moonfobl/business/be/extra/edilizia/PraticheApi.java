/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonfobl.business.be.extra.edilizia;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonfobl.exceptions.service.ServiceException;

@Path("/be/extra/edilizia/pratiche")
@Produces({ "application/json" })
public interface PraticheApi  {
   
	@GET
	@Path("/anno/{anno}/registro/{numRegistro}")
	@Produces({ "application/json" })
	public Response getProgressivi(@PathParam("anno") String anno, @PathParam("numRegistro") Integer numRegistro,
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;

	@GET
	@Path("/v1/anno/{anno}/registro/{numRegistro}/progressivo/{progressivo}")
	@Produces({ "application/json" })
	public Response getPratica(@PathParam("anno") String anno, @PathParam("numRegistro") Integer numRegistro,
			@PathParam("progressivo") String progressivo, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;
	
	@GET
	@Path("/anno/{anno}/registro/{numRegistro}/progressivo/{progressivo}")
	@Produces({ "application/json" })
	public Response getElencoPratiche(@PathParam("anno") String anno, @PathParam("numRegistro") Integer numRegistro,
			@PathParam("progressivo") String progressivo, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;
	
	@GET
	@Path("v2/anno/{anno}/registro/{numRegistro}/progressivo/{progressivo}")
	@Produces({ "application/json" })
	public Response getElencoPraticheV2(@PathParam("anno") String anno, @PathParam("numRegistro") Integer numRegistro,
			@PathParam("progressivo") String progressivo, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;

    
}
