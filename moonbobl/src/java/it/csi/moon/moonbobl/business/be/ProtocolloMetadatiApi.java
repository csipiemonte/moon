/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;

@Path("/be/protocollo-metadati")
@Produces(MediaType.APPLICATION_JSON)
public interface ProtocolloMetadatiApi {

    @GET
    public Response getMetadati( 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ServiceException;

    @GET
    @Path("/{idMetadato}")
    public Response getMetadatoById(@PathParam("idMetadato") Long idMetadato, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

}
