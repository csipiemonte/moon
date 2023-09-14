/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonbobl.dto.moonfobl.UserChangeRequest;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;

@Path("/be/currentUser")

// resource class
/*
 * Devon avere un @Path e almeno un request method designator: 
 */
@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the currentUser API")

public interface CurrentUserApi  {
   
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "get current user", notes = "restituisce l'utente corrente", response = UserInfo.class, tags={ "TOH", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "l'utente corrente", response = UserInfo.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = String.class) })
    public Response getCurrentUser(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest req );
    
    
    @PATCH
    @Produces({ "application/json" })
    public Response patchCurrentUser( UserChangeRequest userChangeRequest,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest req, @Context HttpServletResponse httpResponse );
}
