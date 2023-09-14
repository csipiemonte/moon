/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonfobl.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonfobl.dto.moonfobl.EmbeddedOptions;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.HttpRequestUtils;

@Path("/emb")
@Produces({ "application/json" })
public interface EmbeddedApi {

	@GET
	@Path("/istanze/{codice-istanza}/view")
	public Response getViewIstanza(@PathParam("codice-istanza") String codiceIstanzaPP,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
	
	@GET
	@Path("/istanze/{codice-istanza}/view/nav")
	@Produces({ MediaType.TEXT_PLAIN })
	public Response getViewIstanzaNav(@PathParam("codice-istanza") String codiceIstanzaPP,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;	

	@GET
	@Path("/istanze/{codice-istanza}/edit")
	public Response getEditIstanza(@PathParam("codice-istanza") String codiceIstanzaPP,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
	
	@GET
	@Path("/istanze/{codice-istanza}/edit/nav")
	public Response getEditIstanzaNav(@PathParam("codice-istanza") String codiceIstanzaPP,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;

	@GET
	@Path("/moduli/{codice-modulo}/new")
	public Response getNewIstanza(@PathParam("codice-modulo") String codiceModuloQP,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
	
	@GET
	@Path("/moduli/{codice-modulo}/new/nav")
	public Response getNewIstanzaNav(@PathParam("codice-modulo") String codiceModuloQP,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;	

	@GET
	@Path("/istanze/{codice-istanza}")
	Response getIstanza(@PathParam("codice-istanza") String codiceIstanzaPP,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
	
	@GET
	@Path("/istanze/{codice-istanza}/nav")
	Response getIstanzaNav(@PathParam("codice-istanza") String codiceIstanzaPP,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;	
	
	@GET
	@Path("/istanze")
	Response getIstanze(
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;	
	
	@GET
	@Path("/istanze/nav")
	Response getIstanzeNav(
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;		
	
	@POST
	@Path("/istanze/{codice-istanza}/view")
	@Produces({ MediaType.TEXT_PLAIN })
    @Consumes(MediaType.APPLICATION_JSON) 
	public Response postViewIstanza(@PathParam("codice-istanza") String codiceIstanzaPP,
			EmbeddedOptions body,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, 
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;

	@POST
	@Path("/istanze/{codice-istanza}/edit")
	@Produces({ MediaType.TEXT_PLAIN })
    @Consumes(MediaType.APPLICATION_JSON) 
	public Response postEditIstanza(@PathParam("codice-istanza") String codiceIstanzaPP,
			EmbeddedOptions body,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, 
			 @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;

	@POST
	@Path("/moduli/{codice-modulo}/new")
	@Produces({ MediaType.TEXT_PLAIN })
    @Consumes(MediaType.APPLICATION_JSON) 
	public Response postNewIstanza(@PathParam("codice-modulo") String codiceModuloQP,
			EmbeddedOptions body,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, 
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;

	@POST
	@Path("/istanze/{codice-istanza}")
	@Produces({ MediaType.TEXT_PLAIN })
    @Consumes(MediaType.APPLICATION_JSON) 
	Response postIstanza(@PathParam("codice-istanza") String codiceIstanzaPP,
			EmbeddedOptions body,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, 
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
	
	@POST
	@Path("/istanze")
	@Produces({ MediaType.TEXT_PLAIN })
    @Consumes(MediaType.APPLICATION_JSON)
	Response postIstanze(
			EmbeddedOptions body,
			@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, 
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;		
    
   

}
