/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonfobl.business.be.extra.dirittostudio;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonfobl.exceptions.service.ServiceException;

@Path("/be/extra/dirittostudio/scuole")
@Produces({ "application/json" })
public interface ScuoleApi  {
   

	@GET
	@Produces({ "application/json" })
	public Response getScuole(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws ServiceException;

	@GET
	@Path("/istat/{codiceIstat}/ordini/{idOrdineScuola}")
	@Produces({ "application/json" })
	public Response getScuoleCodiceIstatOrdineScuola(@PathParam("codiceIstat") String codiceIstat,
			@PathParam("idOrdineScuola") Integer idOrdineScuola, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;

	@GET
	@Path("/istat/province/{codiceIstat}/comuni")
	@Produces({ "application/json" })
	public Response getComuniCodiceIstatProvincia(@PathParam("codiceIstat") String codiceIstat,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws ServiceException;

    /***  V2: gestione anno scolastico ***/

	@GET
	@Path("/v2")
	@Produces({ "application/json" })
	public Response getScuoleV2(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws ServiceException;

	@GET
	@Path("/v2/istat/{codiceIstat}/ordini/{idOrdineScuola}/anno/{anno}")
	@Produces({ "application/json" })
	public Response getScuoleV2CodiceIstatOrdineScuolaAnno(@PathParam("codiceIstat") String codiceIstat,
			@PathParam("idOrdineScuola") Integer idOrdineScuola, @PathParam("anno") String annoScolastico,  @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;
	
	@GET
	@Path("/v2/istat/in/{codiceIstat}/ordini/{idOrdineScuola}/anno/{anno}")
	@Produces({ "application/json" })
	public Response getScuoleV2CodiceIstatInOrdineScuolaAnno(@PathParam("codiceIstat") String codiceIstat,
			@PathParam("idOrdineScuola") Integer idOrdineScuola, @PathParam("anno") String annoScolastico, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;
	
	
	@GET
	@Path("/v2/istat/not-in/{codiceIstat}/ordini/{idOrdineScuola}/anno/{anno}")
	@Produces({ "application/json" })
	public Response getScuoleV2CodiceIstatNotInOrdineScuolaAnno(@PathParam("codiceIstat") String codiceIstat,
			@PathParam("idOrdineScuola") Integer idOrdineScuola, @PathParam("anno") String annoScolastico, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws ServiceException;
		

	@GET
	@Path("/v2/istat/province/{codiceIstat}/comuni")
	@Produces({ "application/json" })
	public Response getScuoleV2ComuniCodiceIstatProvincia(@PathParam("codiceIstat") String codiceIstat,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws ServiceException;
    
}
