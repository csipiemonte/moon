/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonsrv.business.rs.annotations.DateFormat;



@Path("/index")
public interface IndexApi {

	@GET
    @Path("/{uid}")
    public Response getContentByUid( @PathParam("uid") String uid, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	
	@GET
    @Path("/archivia-modulo/{codiceOrIdModulo}")
    public Response archiviaIndexByModulo( @PathParam("codiceOrIdModulo") String codiceOrIdModulo, @QueryParam("fino_al") @DateFormat Date data, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@GET
    @Path("/delete-content-modulo/{codiceOrIdModulo}")
    public Response deleteIndexByModulo( @PathParam("codiceOrIdModulo") String codiceOrIdModulo,  @QueryParam("fino_al") @DateFormat Date data,
    		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	
	@GET
    @Path("/getpath/{uid}")
    public Response getPathByUid( @PathParam("uid") String uid, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	


	
	//X TEST
	@GET
    @Path("/test-search/path")
    public Response ricercaByPath( @QueryParam("path") String path, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@GET
    @Path("/test/delete-content-istanza/{idIstanza}")
    public Response deleteContentIstanza( @PathParam("idIstanza") Long idIStanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@GET
    @Path("/test-search")
	public Response ricerca(@QueryParam("q") String q, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest);


}
