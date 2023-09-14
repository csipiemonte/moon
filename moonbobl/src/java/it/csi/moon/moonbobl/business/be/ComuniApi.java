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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonbobl.dto.extra.istat.Comune;
import it.csi.moon.moonbobl.dto.extra.istat.Provincia;
import it.csi.moon.moonbobl.dto.extra.istat.Regione;

@Deprecated // DA CONTROLLARE SE IL PATH VIENE USATO IN ALCUNI MODULI, PRIMA DI ELIMINARE USARE IstatApi /extra/istat/
@Path("/be/extra/regioni")
@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the regioni API")
public interface ComuniApi  {
   
    @GET
    @Path("/{codiceRegione}/province/{codiceProvincia}/comuni/{codiceComune}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Comune.class, tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "il comune selezionato", response = Comune.class),
    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la regione/provincia/comune non esiste/ono", response = Error.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getComuneById( @PathParam("codiceRegione") Integer codiceRegione, @PathParam("codiceProvincia") Integer codiceProvincia, @PathParam("codiceComune") Integer codiceComune, @QueryParam("fields") String fields,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{codiceRegione}/province/{codiceProvincia}/comuni")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Comune.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "i comuni della provincia selezionata", response = Comune.class, responseContainer = "List"),
    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la regione/provincia non esiste/ono", response = Error.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getComuniByProvincia( @PathParam("codiceRegione") Integer codiceRegione, @PathParam("codiceProvincia") Integer codiceProvincia, @QueryParam("fields") String fields,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{codiceRegione}/province/{codiceProvincia}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Provincia.class, tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "la provincia selezionata", response = Provincia.class),
    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la regione/provincia non esiste/ono", response = Error.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getProvinciaById( @PathParam("codiceRegione") Integer codiceRegione, @PathParam("codiceProvincia") Integer codiceProvincia, @QueryParam("fields") String fields,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{codiceRegione}/province")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Provincia.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "le province della regione corrispondente al codice fornito nel path", response = Provincia.class, responseContainer = "List"),
    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la regione non esiste", response = Error.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getProvinceByRegione( @PathParam("codiceRegione") Integer codiceRegione, @QueryParam("fields") String fields,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{codice}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Regione.class, tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "la regione corrispondente al codice fornito nel path", response = Regione.class),
    @io.swagger.annotations.ApiResponse(code = 404, message = "il codice fornito per la regione non esiste", response = Error.class),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getRegioneById( @PathParam("codice") Integer codice, @QueryParam("fields") String fields,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Regione.class, responseContainer = "List", tags={ "business", })
    @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "le regioni italiane", response = Regione.class, responseContainer = "List"),
    @io.swagger.annotations.ApiResponse(code = 200, message = "errore generico", response = Error.class) })
    public Response getRegioni( @QueryParam("fields") String fields,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
}
