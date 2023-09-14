/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonfobl.business.be.extra.istat;

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

import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;

@Path("/be/extra/istat")
@Produces({ "application/json" })
public interface IstatApi  {
   
	public static final int CACHE_MAX_AGE = 600; // 1d:86400, 30min:1800, 10min:600

	//
	// Nazioni - Stati Esteri
    @GET
    @Path("/nazioni")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getNazioni( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;

    @GET
    @Path("/nazioni/{codice}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getNazioneById( @PathParam("codice") String codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;


    //
    // Regioni
    @GET
    @Path("/regioni")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getRegioni( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;

    @GET
    @Path("/regioni/{codice}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getRegioneById( @PathParam("codice") String codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;

    
    //
    // Province
    // OLD NAME
    @GET
    @Path("/provincie")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getProvincie( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;
    
    @GET
    @Path("/province")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getProvince( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;
    
    @GET
    @Path("/province/{codice}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getProvinciaById( @PathParam("codice") String codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    
    @GET
    @Path("/regioni/{codiceRegione}/provincie")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getProvincieByRegione( @PathParam("codiceRegione") String codiceRegione, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;
    @GET
    @Path("/regioni/{codiceRegione}/province")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getProvinceByRegione( @PathParam("codiceRegione") String codiceRegione, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;
    
    @GET
    @Path("/regioni/{codiceRegione}/province/{codiceProvincia}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getProvinciaByRegione( @PathParam("codiceRegione") String codiceRegione, @PathParam("codiceProvincia") String codiceProvincia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;

    @GET
    @Path("/province/sigla/{siglaProvincia}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getProvinciaBySigla( @PathParam("siglaProvincia") String siglaProvincia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    //
    // Comuni
    @GET
    @Path("/comuni")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getComuni( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;

    @GET
    @Path("/comuni/{codice}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getComuneById( @PathParam("codice") String codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/province/{codiceProvincia}/comuni")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getComuniByProvincia( @PathParam("codiceProvincia") String codiceProvincia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;

    @GET
    @Path("/province/{codiceProvincia}/comuni/{codiceComune}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getComuneByProvincia( @PathParam("codiceProvincia") String codiceProvincia, @PathParam("codiceComune") String codiceComune, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/regioni/{codiceRegione}/province/{codiceProvincia}/comuni")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getComuniByRegioneProvincia( @PathParam("codiceRegione") String codiceRegione, @PathParam("codiceProvincia") String codiceProvincia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;
    @GET
    @Path("/regioni/{codiceRegione}/provincie/{codiceProvincia}/comuni")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getComuniByRegioneProvinciaOLD( @PathParam("codiceRegione") String codiceRegione, @PathParam("codiceProvincia") String codiceProvincia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;

    @GET
    @Path("/regioni/{codiceRegione}/province/{codiceProvincia}/comuni/{codiceComune}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getComuneByRegioneProvincia( @PathParam("codiceRegione") String codiceRegione, @PathParam("codiceProvincia") String codiceProvincia, @PathParam("codiceComune") String codiceComune, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;

    
    //
    // Ateco
    @GET
    @Path("/ateco")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getCodiciAteco( @QueryParam("sezioni") String sezioni, @QueryParam("divisioni") String divisioni, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;

    @GET
    @Path("/ateco/{codiceAteco}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getCodiceAteco( @PathParam("codiceAteco") String codiceAteco,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    
    //
    // FormeGiuridica
    @GET
    @Path("/forme-giuridiche")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getFormeGiuridiche(
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;

    @GET
    @Path("/forme-giuridiche/{idFormeGiuridica}")
    @Cache(maxAge = CACHE_MAX_AGE)
    public Response getFormeGiuridica( @PathParam("idFormeGiuridica") String idFormeGiuridicaPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;

}
