/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonsrv.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.commons.dto.EmailRequest;

@Path("/email")
public interface EmailApi  {
   
	/**
	 * Invia una email di testo
	 * USED by MOOnBO
	 * @param body
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
    @POST
    @Path("/text")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inviaMessaggio( EmailRequest body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	
    /**
	 * Invia una email di testo
	 * @param body
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
    @POST
    @Path("/text-attach")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inviaMessaggioWithAttach( EmailRequest body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
	 * Invia una email HTML (e Text in alternative)
	 * @param body
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
    @POST
    @Path("/html")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inviaMessaggioHtml( EmailRequest body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    /**
	 * Invia una email HTML (e Text in alternative)
	 * @param body
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
    @POST
    @Path("/html-attach")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inviaMessaggioHtmlWithAttach( EmailRequest body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
 
}
