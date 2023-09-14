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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.commons.dto.moonprint.MoonprintDocument;

@Path("/be/moonprint-mock-unsecure")
//@Produces(MediaType.APPLICATION_OCTET_STREAM)
@Produces("application/pdf")
public interface MoonPrintApi {

    @GET
    @Path("/pdf")
    public Response getPdf(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @POST
    @Path("/pdf")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generatePdf(MoonprintDocument body, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @POST
    @Path("/pdfb64")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateBase64(MoonprintDocument body, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
}
