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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import it.csi.moon.commons.dto.api.StatoAcquisizioneRequest;
import it.csi.moon.moonsrv.business.rs.annotations.DateFormat;

@Path("/api-test")
//@Produces(MediaType.APPLICATION_JSON)
public interface ApiTest {
   
	/**
	 * Restituisce un elenco di codici delle istanze inoltrate
	 * @param codiceModulo filtro per codice modulo
	 * @param stato filtro per stato istanza
	 * @param dataDa filtro per data iniziale
	 * @param dataA filtro per data finale
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
    @GET
    @Path("/istanze")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIstanze( @QueryParam("codice_modulo") String codiceModulo, @QueryParam("stato") String stato, @QueryParam("versione_modulo") String versioneModulo, @QueryParam("codice_ente") String codiceEnte,
    	@QueryParam("data_da")  @DateFormat Date dataDa, @QueryParam("data_a")  @DateFormat Date dataA, @QueryParam("test") boolean test,
    	@QueryParam("apimint_url") String apimintUrl,
    	@HeaderParam("client-profile") String clientProfile,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
	 * Restituisce un oggetto istanza
	 * @param codice istanza
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
    @GET
    @Path("/istanze/{codice-istanza}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIstanza( @PathParam("codice-istanza") String codice,
        @QueryParam("apimint_url") String apimintUrl,
        @HeaderParam("client-profile") String clientProfile, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
	 * Aggiorna cambio di stato dell'istanza
	 * @param body
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
    @POST
    @Path("/istanze/{codice-istanza}/azione/{codice-azione}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON) 
    public Response postStatoAcquisizione( @PathParam("codice-istanza") String codiceIstanza, @PathParam("codice-azione") String codiceAzione,
       	@QueryParam("apimint_url") String apimintUrl,
        StatoAcquisizioneRequest body,
        @HeaderParam("client-profile") String clientProfile, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    

    /**
     * Elenco Moduli per un fruitore
     * @param codice_modulo
     * @param versione_modulo
     * @param oggetto_modulo
     * @param descrizione_modulo
     * @param stato
     * @param con_presenza_istanze_user
     * @param nome_portale
     * @param codice_ente
     * @param codice_area
     * @param codice_ambito
     * @return
     */
    @GET
    @Path("/moduli")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModuli(
    	@QueryParam("codice_modulo") String codiceModulo, @QueryParam("versione_modulo") String versioneModulo,
    	@QueryParam("oggetto_modulo") String oggettoModulo, @QueryParam("descrizione_modulo") String descrizioneModulo, 
    	@QueryParam("stato") String stato, 
    	@QueryParam("con_presenza_istanze_utente") String conPresenzaIstanzeUtente, @QueryParam("nome_portale") String nomePortale,
    	@QueryParam("codice_ente") String codiceEnte, @QueryParam("codice_area") String codiceArea, @QueryParam("codice_ambito") String codiceAmbito, 
    	@QueryParam("apimint_url") String apimintUrl,
        @HeaderParam("client-profile") String clientProfile, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
	 * Restituisce un oggetto modulo
	 * @param codice modulo
	 * @return
	 */
    @GET
    @Path("/moduli/{codice-modulo}/v/{versione-modulo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModulo( @PathParam("codice-modulo") String codiceModulo, @PathParam("versione-modulo") String versioneModulo, @QueryParam("fields") String fields, 
        @QueryParam("apimint_url") String apimintUrl,
        @HeaderParam("client-profile") String clientProfile, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
}
