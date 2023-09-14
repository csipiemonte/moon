/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/firma")
@Produces(MediaType.APPLICATION_JSON)
public interface FirmaDigitaleApi {

 	@GET
    @Path("/test")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response testResouces(@Context SecurityContext securityContext);  
 	
 	@GET
    @Path("/verifica-firma-allegato/{idAllegato}")
 	public Response verificaFirmaByIdAllegato(@PathParam("idAllegato") Long idAllegato);
 	
 	@GET
    @Path("/verifica-firma-file/{idFile}")
 	public Response verificaFirmaByIdFile(@PathParam("idFile") Long idFile);
 	
 	@POST
    @Path("/verifica-firma")
 	@Consumes("multipart/form-data")
 	public Response verificaFirmaByContenuto(MultipartFormDataInput file);
 	
 	@GET
    @Path("/has-firma-pades-allegato/{idAllegato}")
 	public boolean checkFirmaPadesByIdAllegato(@PathParam("idAllegato") Long idAllegato);
 	
 	@GET
    @Path("/has-firma-pades-file/{idFile}")
 	public boolean checkFirmaPadesByIdFile(@PathParam("idFile") Long idFile);
 	
 	@POST
    @Path("/has-firma-pades")
 	@Consumes("multipart/form-data")
 	public boolean checkFirmaPades(MultipartFormDataInput file);
 	
 	@GET
 	@POST
    @Path("/content-type")
 	@Consumes("multipart/form-data")
 	public Response retrieveContentType(MultipartFormDataInput file);
}
