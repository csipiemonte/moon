/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.features;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/extra/features/jms")
@Produces(MediaType.APPLICATION_JSON)
public interface JmsApi {

	public static final String JMS_MOONSRV_PRT_STARDAS_V1 = "MOOnSrvPrtStardasV1Queue";
	public static final String JMS_MOONSRV_PRT_MAGGIOLI_SOAP_V1 = "MOOnSrvPrtMaggioliSoapV1Queue";
	public static final String JMS_MOONSRV_PRT_AKP_V1 = "MOOnSrvPrtApkV1Queue";
	public static final String JMS_MOONSRV_PRT_DOQUI_ACARIS_V1 = "MOOnSrvPrtDoQuiAcarisV1Queue";
	
    @GET
    @Path("/ping")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response ping(
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@POST
	@Path("/send/{destination}")
    public Response sendMessagePrt( @PathParam("destination") String destination, @QueryParam("multiple") String multiple, String payload,
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
}
