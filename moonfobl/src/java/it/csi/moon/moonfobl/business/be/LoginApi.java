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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.util.HttpRequestUtils;

@Path("/auth/login")
@Produces({ "application/json" })
public interface LoginApi  {
	
	@GET
   	@Path("/idp/mode/{loginMode}")
   	public Response loginIdpMode(@PathParam ("loginMode") String provider, @QueryParam(HttpRequestUtils.SIMULATE_PORTALE_ID_MARKER) String simulatePortale,
   	    @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest request, @Context HttpServletResponse response );	
	  
	@GET
    @POST
    @Path("/idp/{provider}")
    public Response loginIdpShibboleth(@PathParam ("provider") String provider, @QueryParam(HttpRequestUtils.SIMULATE_PORTALE_ID_MARKER) String simulatePortale, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest request, @Context HttpServletResponse response );	

	@GET
    @Path("/idp/user-pwd")
    public Response loginIdpUserPwd(@QueryParam(HttpRequestUtils.SIMULATE_PORTALE_ID_MARKER) String simulatePortale, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest request, @Context HttpServletResponse response );	
	
    @POST
    @Path("/request")
    public Response postLoginRequest(@QueryParam(HttpRequestUtils.SIMULATE_PORTALE_ID_MARKER) String simulatePortale, LoginRequest loginRequest,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest request, @Context HttpServletResponse response );	

    @POST
	@Path("/idp-social/{provider}")
    public Response loginIdpGoogle(@PathParam ("provider") String provider, @QueryParam(HttpRequestUtils.SIMULATE_PORTALE_ID_MARKER) String simulatePortale, LoginRequest loginRequest,
	    @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest request, @Context HttpServletResponse response );	

    @GET
    @Path("/mode")
    public Response getDirectLogonMode( @QueryParam("codice_modulo") String codiceModulo, @QueryParam("codice_ente") String codiceEnte, @QueryParam("simulatePortale") String simulatePortale,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest request, @Context HttpServletResponse response );

}
