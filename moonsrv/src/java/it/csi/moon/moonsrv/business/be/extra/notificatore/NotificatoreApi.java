/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.notificatore;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.http.MediaType;

/**
 * API per l'uso del Notificatore - NOTIFY Message Broker
 * 
 * @author Laurent
 *
 */
@Path("/extra/notificatore")
public interface NotificatoreApi {

	/**
	 * Invia una notifica
	 * @param body
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
/* 
	@POST
    @Path("/topics/messages")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public Response inviaMessaggio( Message body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/preferences/contacts")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getContacts( @QueryParam("identita_digitale") String identitaDigitale, @QueryParam("codice_fiscale") String codiceFiscale,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/preferences/services")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getServices(@QueryParam("identita_digitale") String identitaDigitale,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/messages/status")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getStatus(@QueryParam("id_messaggio") String idMessaggio,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
*/    
    //**********************************************************************
    //API x TEST
    @GET
    @Path("/preferences/contacts/{cf}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getContacts( 
    		@QueryParam("endpoint_url") String endpointUrl,
    		@HeaderParam("token") String token,
    		@QueryParam("identita_digitale") String identitaDigitale,
    		@PathParam("cf") String cf,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/preferences/services")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getAllServices(
    		@QueryParam("endpoint_url") String endpointUrl,
    		@HeaderParam("token") String token,
    		@QueryParam("identita_digitale") String identitaDigitale, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/preferences")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getPreferences(
    		@QueryParam("endpoint_url") String endpointUrl,
    		@HeaderParam("token") String token,
    		@QueryParam("identita_digitale") String identitaDigitale, 
    		@QueryParam("cf") String cf,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/preferences/{service_name}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getPreferencesByServiceName(
    		@QueryParam("endpoint_url") String endpointUrl,
    		@HeaderParam("token") String token,
    		@QueryParam("identita_digitale") String identitaDigitale, 
    		@PathParam("service_name") String serviceName,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/messages/status/{id_payload}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getStatus(
    		@QueryParam("endpoint_url") String endpointUrl,
    		@HeaderParam("token") String token,
    		@QueryParam("identita_digitale") String identitaDigitale, 
    		@PathParam("id_payload") String idPayload,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    
    //**********************************************************************
    @POST
    @Path("/{idIstanza}")
    public Response inviaRichiestaNotify( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
  
}
