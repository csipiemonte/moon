/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/*
 * Api relative al recupero degli stati delle istanze di Moon
 */

@Path("/be/parameters")
@Produces({ "application/json" })
public interface ConsumerApi {
	@GET
    @Produces({ "application/json" })
	@Path("/{consumer}/{codiceEnte}")
    public Response getParameters(@PathParam("consumer") String consumer, @PathParam("codiceEnte") String codiceEnte,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
	@GET
    @Produces({ "application/json" })
	@Path("/{consumer}/{codiceEnte}/{idParameter}/{type}")
    public Response getParameter(@PathParam("consumer") String consumer, @PathParam("codiceEnte") String codiceEnte, 
    	@PathParam("idParameter") String id, @PathParam("type") String type,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
