/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonsrv.business.be.extra.istat;

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

@Path("/extra/istat")
@Produces({ "application/json" })
public interface IstatApi  {
   
	//
	// Nazioni - Stati Esteri
    @GET
    @Path("/nazioni")
    public Response getNazioni( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/nazioni/{codice}")
    public Response getNazioneById( @PathParam("codice") String codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );


    //
    // Regioni
    @GET
    @Path("/regioni")
    public Response getRegioni( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/regioni/{codice}")
    public Response getRegioneById( @PathParam("codice") String codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    //
    // Province
    @GET
    @Path("/province")
    public Response getProvince( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/province/{codice}")
    public Response getProvinciaById( @PathParam("codice") String codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/regioni/{codiceRegione}/province")
    public Response getProvinceByRegione( @PathParam("codiceRegione") String codiceRegione, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/regioni/{codiceRegione}/province/{codiceProvincia}")
    public Response getProvinciaByRegione( @PathParam("codiceRegione") String codiceRegione, @PathParam("codiceProvincia") String codiceProvincia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/province/sigla/{siglaProvincia}")
    public Response getProvinciaBySigla( @PathParam("siglaProvincia") String siglaProvincia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    //
    // Comuni
    @GET
    @Path("/comuni")
    public Response getComuni( @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/comuni/{codice}")
    public Response getComuneById( @PathParam("codice") String codice, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/province/{codiceProvincia}/comuni")
    public Response getComuniByProvincia( @PathParam("codiceProvincia") String codiceProvincia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/province/{codiceProvincia}/comuni/{codiceComune}")
    public Response getComuneByProvincia( @PathParam("codiceProvincia") String codiceProvincia, @PathParam("codiceComune") String codiceComune, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/regioni/{codiceRegione}/province/{codiceProvincia}/comuni")
    public Response getComuniByRegioneProvincia( @PathParam("codiceRegione") String codiceRegione, @PathParam("codiceProvincia") String codiceProvincia, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/regioni/{codiceRegione}/province/{codiceProvincia}/comuni/{codiceComune}")
    public Response getComuneByRegioneProvincia( @PathParam("codiceRegione") String codiceRegione, @PathParam("codiceProvincia") String codiceProvincia, @PathParam("codiceComune") String codiceComune, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    //
    // Ateco
    @GET
    @Path("/ateco")
    public Response getCodiciAteco( @QueryParam("sezioni") String sezioni, @QueryParam("divisioni") String divisioni, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;

    @GET
    @Path("/ateco/{codiceAteco}")
    public Response getCodiceAteco( @PathParam("codiceAteco") String codiceAteco,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;
    
    
    //
    // FormeGiuridica
    @GET
    @Path("/forme-giuridiche")
    public Response getFormeGiuridiche(
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException;

    @GET
    @Path("/forme-giuridiche/{idFormeGiuridica}")
    public Response getFormeGiuridica( @PathParam("idFormeGiuridica") String idFormeGiuridicaPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException;

}
