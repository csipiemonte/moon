/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/be/cmd")
@Produces({ "application/json" })
public interface CmdApi {
   
    @GET
    @Path("/ping")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response ping(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/ping/moonsrv")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response pingMoonsrv(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/version")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response version(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/build-info")
    public Response buildInfo(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );

}
