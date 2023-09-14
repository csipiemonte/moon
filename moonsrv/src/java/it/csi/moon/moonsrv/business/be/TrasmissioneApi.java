/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.http.MediaType;

/**
 * API per trasmissione buoni taxi
 * 
 * @author Danilo
 *
 */
@Path("/trasmetti")
@Produces(MediaType.APPLICATION_JSON_VALUE)
public interface TrasmissioneApi {

    @GET
    @Path("/buoni-taxi") 
    public Response trasmettiBuoniTaxi(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/download/buoni-taxi")
//    @Produces({"text/csv"})
    @Produces("application/octet-stream")
    public Response downloadBuoniTaxi(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

}
