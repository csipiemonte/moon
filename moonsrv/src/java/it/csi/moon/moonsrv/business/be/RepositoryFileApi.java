/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/repository")
@Produces({ "application/json" })
public interface RepositoryFileApi {

	@GET
	public Response getElencoRepositoryFile( @QueryParam("idIstanza") String idIstanzaQP, @QueryParam("fields") String fieldsQP,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("/{idFile}")
	public Response getRepositoryFileById( @PathParam("idFile") String idFilePP, @QueryParam("fields") String fieldsQP,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
	
	@GET
	@Path("/file/{idFile}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFile( @PathParam("idFile") Long idFile,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	/**
     * Protocolla un file (inizialmente per Ricevute)
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     */
    @POST
    @Path("/file/protocolla/{idFile}")
    public Response postProtocolla( @PathParam("idFile") Long idFile,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
//    @GET
//    @Path("/file/documento-by-name/{formioNameFile}")
//    public Response getDocumentoByFormioNameFile( @PathParam("formioNameFile") String formioNameFile, 
//    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
//    
//    @GET
//    @Path("/file/documento-by-id/{idFile}")
//    public Response getDocumentoByIdFile( @PathParam("idFile") Long idFile, 
//    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

//    /**
//     * Pubblica un file (inizialmente per Ricevute) su MyDocs (DOCME)
//     * @param idFile
//     * @param securityContext
//     * @param httpHeaders
//     * @param httpRequest
//     */
//    @POST
//    @Path("/file/{idFile}/pubblica-mydocs")
//    public Response postPubblicaMydocs( @PathParam("idFile") Long idFile,
//    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
}
