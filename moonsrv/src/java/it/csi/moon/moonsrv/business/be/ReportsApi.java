/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonsrv.business.be;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
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

import it.csi.moon.moonsrv.business.rs.annotations.DateFormat;

@Path("/reports")
public interface ReportsApi {
   
    /**
	 * Restituisce un tipo di report del modulo
	 * @param codiceModulo codice modulo
	 * @param codiceEstrazione codice report
	 * @param dataDa data inizio estrazione
	 * @param dataA data fine estrazione
	 * @return
	 */
    @GET
    @Path("/modulo/{codice-modulo}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getReport( @PathParam("codice-modulo") String codiceModulo, @QueryParam("codice_estrazione") String codiceEstrazione, 
    	@QueryParam("data_da") @DateFormat Date dataDa, @QueryParam("data_a") @DateFormat Date dataA,
        @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
}
