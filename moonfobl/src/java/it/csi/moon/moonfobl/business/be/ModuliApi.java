/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonfobl.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;

@Path("/be/moduli")
@Produces({ "application/json" })
public interface ModuliApi {
   
    @GET
    public Response getModuli( @QueryParam("codiceModulo") String codiceModuloQP, @QueryParam("idModulo") String idModuloQP, 
    	@QueryParam("conPresenzaIstanze") String conPresenzaIstanzeQP, @QueryParam("fields") String fieldsQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;  

    @GET
    @Path("/{idModulo}/v/{idVersioneModulo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModuloById( @PathParam("idModulo") Long idModuloPP, @PathParam("idVersioneModulo") Long idVersioneModuloPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
    

    @GET
    @Path("/codice/{codiceModulo}/versione/{versione}")
    public Response getModuloByCodice( @PathParam("codiceModulo") String codiceModuloPP, @PathParam("versione") String versionePP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
    
    @GET
    @Path("/{idModulo}/v/{idVersioneModulo}/stato")
    public Response getStatoModuloById( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;

}
