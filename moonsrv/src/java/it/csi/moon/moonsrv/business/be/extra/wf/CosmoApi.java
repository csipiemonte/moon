/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.wf;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.http.MediaType;

import it.csi.cosmo.callback.v1.dto.AggiornaStatoPraticaRequest;

/**
 * API per l'uso di COSMO
 * Endpoint /extra/wf/cosmo/callback/v1
 * 
 * @author Laurent
 *
 */
@Path("/extra/wf/cosmo")
public interface CosmoApi {

    @PUT
    @Path("/callback/v1/stato-pratica/{idPratica}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response callbackPutStatoPraticaV1( @PathParam("idPratica") String idPratica, AggiornaStatoPraticaRequest body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idPratica}/contenuto")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Response getAllegato( @PathParam("idPratica") String idPratica,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

}
