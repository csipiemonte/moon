/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonsrv.business.be;

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

@Path("/cmd")
@Produces({ "application/json" })
public interface CmdApi  {
   
    @GET
    @Path("/ping")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response ping(@Context SecurityContext securityContext);  
    
    @GET
    @Path("/ping/moonprint")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response pingMoonprint(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/version")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response version(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/build-info")
    public Response buildInfo(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/attribute-list")
    public Response getAttributeList(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/sys-info")
    public Response sysInfo(@QueryParam("fields") String fields, 
    		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/prop")
    public Response getProp(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/env")
    public Response getEnv(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    /**************************************
     * cache
     *************************************/
    @GET
    @Path("/cache/{codice}/keys")
    public Response keysCacheByCodice(@PathParam("codice") String codice, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/cache/{codice}/count")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response countCacheByCodice(@PathParam("codice") String codice, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/cache/{codice}/lastResetCache")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response lastResetCacheByCodice(@PathParam("codice") String codice, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/cache/{codice}/resetAll")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response resetAll(@PathParam("codice") String codice, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/cache/{codice}/reset/{key}")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response reset(@PathParam("codice") String codice,@PathParam("key") String key, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
    
    @GET
    @Path("/cache-manager/info")
    public Response info( @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders );
   
}
