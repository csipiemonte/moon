/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.commons.dto.EpayFakeNotifia;
import it.csi.moon.commons.dto.extra.epay.GetRtRequest;

@Path("/epay")
@Produces(MediaType.APPLICATION_JSON)
public interface EpayApi {

	// FOR TEST
	@POST
	@Path("/notify/{iuv}")
	public Response notifyToFrontOfficeViaJMSTopic( @PathParam("iuv") String iuv, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	
	// FOR TEST
	@POST
	@Path("/fake-notifica")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvaFakeNotificaFromPpay( EpayFakeNotifia body, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	
	@POST
	@Path("/get-rt")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getRT( GetRtRequest body, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("/verifica-pagamento/{idEpay}")
	public Response getVerificaPagamento( @PathParam("idEpay") String idEpay, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	//OrganizationsApi
	//get notifica pdf pagamento x sportello
	@GET
	@Path("/avviso-pagamento/{iuv}")
	public Response getNotificaPagamento( @PathParam("iuv") String iuv, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	//createDebtPosition
	
	//getDebtPositionData
	@GET
	@Path("/debt-position/{iuv}")
	public Response getDebtPositionData( @PathParam("iuv") String iuv, 
	    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}
