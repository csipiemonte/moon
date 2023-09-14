/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.moon.moonbobl.dto.moonfobl.Utente;

@Path("/be/utenti")
@Produces({ "application/json" })
public interface UtentiApi  {

    @GET
    public Response getUtenti( 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/ente-abilitato")
    public Response getUtentiEnteAbilitato( 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/ente-no-abilitato")
    public Response getUtentiEnteNoAbilitato( 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idUtente}")
    public Response getUtenteById( @PathParam("idUtente") Long idUtente, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/byIdentificativo/{identificativoUtente}")
    public Response getUtenteByIdentificativo( @PathParam("identificativoUtente") String identificativoUtente, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUtente( Utente body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @PUT
    @Path("/{idUtente}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUtente( @PathParam("idUtente") Long idUtente, Utente body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @PATCH
    @Path("/{idUtente}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response patchUtenteById( @PathParam("idUtente") Long idUtente, Utente partialUtente, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    
    //
    // Ruoli
    //
    @GET
    @Path("/{idUtente}/ruoli")
    public Response getRuoliByIdUtente(@PathParam("idUtente") Long idUtente, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @POST
    @Path("/{idUtente}/ente/{idEnte}/area/{idArea}/ruolo/{idRuolo}")
    public Response postEnteAreaRuolo(@PathParam("idUtente") String idUtente, @PathParam("idEnte") String idEnte, @PathParam("idArea") String idArea, @PathParam("idRuolo") String idRuolo, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @DELETE
    @Path("/{idUtente}/ente/{idEnte}/area/{idArea}/ruolo/{idRuolo}")
    public Response deleteEnteAreaRuolo(@PathParam("idUtente") String idUtente, @PathParam("idEnte") String idEnte, @PathParam("idArea") String idArea, @PathParam("idRuolo") String idRuolo, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    //
    // Funzioni
    //
    @GET
    @Path("/{idUtente}/funzioni")
    public Response getFunzioniByIdUtente(@PathParam("idUtente") Long idUtente, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    //
    // Gruppi
    //
    @GET
    @Path("/{idUtente}/gruppi")
    public Response getGruppiByIdUtente(@PathParam("idUtente") Long idUtente, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
   
    // Comuni Abilitati
    @GET
    @Path("/comuni-abilitati/")
    @Produces({ "application/json" })
    public Response getComuniAbilitati( @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    
    // Enti Abilitati
    @GET
    @Path("/enti-abilitati/")
    @Produces({ "application/json" })
    public Response getEntiAbilitati( @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    
    // Moduli Abilitati
    @POST
    @Path("/{idUtente}/modulo/{idModulo}")
    public Response postUtenteModulo(@PathParam("idUtente") String idUtente, @PathParam("idModulo") String idModulo,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @DELETE
    @Path("/{idUtente}/modulo/{idModulo}")
    public Response deleteUtenteModulo(@PathParam("idUtente") String idUtente, @PathParam("idModulo") String idModulo,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}
