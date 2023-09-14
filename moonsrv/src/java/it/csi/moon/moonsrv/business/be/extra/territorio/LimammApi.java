/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.territorio;

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

import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;

@Path("/extra/territorio/limamm")
@Produces({ "application/json" })
public interface LimammApi {

	//
	// REGIONI
	@GET
    @Path("/regioni")
    public Response getRegioni( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;
    
    @GET
    @Path("/regioni/{idRegione}")
    public Response getRegioneById( @PathParam("idRegione") String idRegionePP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/regioni/istat/{istat}")
    public Response getRegioneByIstat( @PathParam("istat") String istatPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    
	//
	// PROVINCE
    @GET
    @Path("/province")
    public Response getProvince( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;
    
    @GET
    @Path("/province/{idProvincia}")
    public Response getProvinciaById( @PathParam("idProvincia") String idProvinciaPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/province/istat/{istat}")
    public Response getProvinciaByIstat( @PathParam("istat") String istatPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;

    @GET
    @Path("/regioni/{idRegione}/province")
    public Response getProvinceByRegione( @PathParam("idRegione") String idRegionePP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;
    
    @GET
    @Path("/regioni/istat/{istatRegione}/province")
    public Response getProvinceByIstatRegione( @PathParam("istatRegione") String istatRegionePP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/regioni/{idRegione}/province/{idProvincia}")
    public Response getProvinciaByRegione( @PathParam("idRegione") String idRegionePP, @PathParam("idProvincia") String idProvinciaPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;

    @GET
    @Path("/regioni/istat/{istatRegione}/province/istat/{istatProvincia}")
    public Response getProvinciaByIstatRegione( @PathParam("istatRegione") String istatRegionePP, @PathParam("istatProvincia") String istatProvinciaPP, String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    //
	// COMUNI
    @GET
    @Path("/comuni")
    public Response getComuni( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;
    
    @GET
    @Path("/comuni/{idComune}")
    public Response getComuneById( @PathParam("idComune") String idComunePP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/comuni/istat/{istat}")
    public Response getComuneByIstat( @PathParam("istat") String istatPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    // List comuni
    @GET
    @Path("/province/{idProvincia}/comuni")
    public Response getComuniByProvincia( @PathParam("idProvincia") String idProvinciaPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;
    
    @GET
    @Path("/province/istat/{istatProvincia}/comuni")
    public Response getComuniByIstatProvincia( @PathParam("istatProvincia") String istatProvinciaPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/regioni/{idRegione}/province/{idProvincia}/comuni")
    public Response getComuniByRegioneProvincia( @PathParam("idRegione") String idRegionePP,  @PathParam("idProvincia") String idProvinciaPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;
    
    @GET
    @Path("/regioni/istat/{istatRegione}/province/istat/{istatProvincia}/comuni")
    public Response getComuniByIstatRegioneProvincia( @PathParam("istatRegione") String istatRegionePP, @PathParam("istatProvincia") String istatProvinciaPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;

    // Comuni singoli
    @GET
    @Path("/regioni/{idRegione}/province/{idProvincia}/comuni/{idComune}")
    public Response getComuneByRegioneProvincia( @PathParam("idRegione") String idRegionePP, @PathParam("idProvincia") String idProvinciaPP, @PathParam("idComune") String idComunePP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/regioni/istat/{istatRegione}/province/istat/{istatProvincia}/comuni/istat/{istatComune}")
    public Response getComuneByIstatRegioneProvincia(@PathParam("istatRegione") String istatRegionePP, @PathParam("istatProvincia") String istatProvinciaPP, @PathParam("istatComune") String istatComunePP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;

}
