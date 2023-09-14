/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonfobl.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import it.csi.moon.moonfobl.dto.moonfobl.MultipartBodyFormIo;
import it.csi.moon.moonfobl.exceptions.service.FileUploadException;

@Path("/be/file")
public interface FileApi  {
   
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json;charset=UTF-8"})
    public Response uploadFile(@QueryParam("baseUrl") String baseUrl, @PathParam("form") String form, @PathParam("project") String project,
    	@QueryParam("filter") String filter, @QueryParam("err-msg") String errMsg, @QueryParam("signed") String signed, MultipartFormDataInput input, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws FileUploadException;
    
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@QueryParam("baseUrl") String baseUrl, @QueryParam("form") String nameFile, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) ;
    
    @POST
    @Path("/notifica")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json;charset=UTF-8"})
    public Response uploadFileNotifica(@QueryParam("baseUrl") String baseUrl, @PathParam("form") String form, @PathParam("project") String project, MultipartFormDataInput input, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws FileUploadException;

    @GET
    @Path("/notifica")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFileNotifica(@QueryParam("baseUrl") String baseUrl, @QueryParam("form") String nameFile, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @GET
    @Path("/notifica/fruitore")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFileNotificaFruitore(@QueryParam("baseUrl") String refUrl, @QueryParam("form") String nomeFile, @QueryParam("idIstanza") Long idIstanza, @QueryParam("idStoricoWorkflow") Long idStoricoWorkflow, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest);
    
    @DELETE
    public Response deleteFile(@QueryParam("baseUrl") String baseUrl, @PathParam("form") String form, @PathParam("project") String project, String paylod, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


    @POST
    @Path("/repository-file/tipologia/{idTipologia}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json;charset=UTF-8"})
    public Response uploadRepositoryFile(@QueryParam("baseUrl") String baseUrl, @PathParam("form") String form, @PathParam("project") String project, MultipartFormDataInput input, @PathParam("idTipologia") String idTipologiaPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws FileUploadException;

    @GET
    @Path("/repository-file/tipologia/{idTipologia}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getRepositoryFile(@QueryParam("baseUrl") String baseUrl, @QueryParam("form") String nameFile, @PathParam("idTipologia") String idTipologiaPP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @POST
    @Path("/retrieve-content-type")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response retrieveContentType(@MultipartForm MultipartBodyFormIo data, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
}
