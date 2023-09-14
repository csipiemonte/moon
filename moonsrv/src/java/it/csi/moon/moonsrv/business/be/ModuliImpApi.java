/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonsrv.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.commons.dto.ModuloExported;

@Path("/imp-moduli")
@Produces(MediaType.APPLICATION_JSON)
public interface ModuliImpApi {
   
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response impModulo( 
    	@QueryParam("modalita") String modalita, 
    	@QueryParam("codiceModuloTarget") String codiceModuloTarget, 
    	ModuloExported modulo,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

}
