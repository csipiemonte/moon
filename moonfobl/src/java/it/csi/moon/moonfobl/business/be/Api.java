/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonfobl.business.be;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
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

import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.HttpRequestUtils;

@Path("/be/api/v1")
@Produces(MediaType.APPLICATION_JSON)
public interface Api  {
   
    @POST
    @Path("/moon-identita")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMoonIdentita(
    	@QueryParam("logon_mode") String logonMode, @QueryParam("provider") String provider,
    	@QueryParam("nome_portale") String nomePortale,
    	@QueryParam("identificativo_utente") String identificativoUtente, 
    	@QueryParam("codice_fiscale") String codiceFiscale, @QueryParam("cognome") String cognome, @QueryParam("nome") String nome, 
    	@QueryParam("email") String email,
    	@QueryParam("id_iride") String idIride,
    	@QueryParam("shib-Identita-JWT") String shibIdentitaJwt,
    	@QueryParam("codice_ente") String codiceEnte, @QueryParam("codice_ambito") String codiceAmbito, @QueryParam("gope") String gruppoOperatore,
    	@QueryParam("codice_modulo") String codiceModulo, @QueryParam("versione_modulo") String versioneModulo,
    	@HeaderParam("client-profile") String clientProfile, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, @Context HttpServletResponse response );
    
    @GET
    @Path("/istanze")
    public Response getIstanze( 
    	@QueryParam("id_tab_fo") String idTabFoQP, 
    	@QueryParam("stato") String statoQP, @QueryParam("stati_istanza") List<String> statiIstanzaQP,
    	@QueryParam("importanza") String importanzaQP, 
		@QueryParam("codice_istanza") String codiceIstanzaQP, 
		@QueryParam("codice_modulo") String codiceModuloQP, @QueryParam("versione_modulo") String versioneModuloQP, @QueryParam("titoloModulo") String titoloModuloQP, 
		@QueryParam("created_start") Date createdStart, @QueryParam("created_end") Date createdEnd,
		@QueryParam("codice_fiscale") String codiceFiscale, @QueryParam("cognome") String cognome, @QueryParam("nome") String nome,
		@QueryParam("sort") String sort, 
    	@QueryParam("test") boolean test,
    	@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @HeaderParam("X-Request-Id") String xRequestId,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/istanze-paginate")
    @Produces({ "application/json" })
    public Response getIstanzePaginate( 
        @QueryParam("id_tab_fo") String idTabFoQP,  
    	@QueryParam("stato") String statoQP, @QueryParam("stati_istanza") List<String> statiIstanzaQP,
    	@QueryParam("importanza") String importanzaQP, 
		@QueryParam("codice_istanza") String codiceIstanzaQP, 
		@QueryParam("codice_modulo") String codiceModuloQP, @QueryParam("versione_modulo") String versioneModuloQP, @QueryParam("titoloModulo") String titoloModuloQP, 
		@QueryParam("created_start") Date createdStart, @QueryParam("created_end") Date createdEnd, 
		@QueryParam("codice_fiscale") String codiceFiscale, @QueryParam("cognome") String cognome, @QueryParam("nome") String nome,
		@QueryParam("sort") String sort, 
		@QueryParam("offset") String offsetQP, @QueryParam("limit") String limitQP, 
    	@QueryParam("test") boolean test,
    	@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @HeaderParam("X-Request-Id") String xRequestId,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


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
    public Response getIstanza(
    	@PathParam("codice-istanza") String codiceIstanzaPP, 
       	@HeaderParam("client-profile") String clientProfile, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );


    /**
	 * Restituisce il pdf dell'istanza
	 * @param codiceIstanza codice istanza
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
    @GET
    @Path("/istanze/{codice-istanza}/pdf")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getIstanzaPdf( @PathParam("codice-istanza") String codiceIstanza, 
        @HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    /**
     * Elenco dei moduli
     * @param codiceModuloQP
     * @param idModuloQP
     * @param conPresenzaIstanzeQP
     * @param fieldsQP
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     * @throws ResourceNotFoundException
     * @throws UnprocessableEntityException
     * @throws ServiceException
     */
    @GET
    @Path("/moduli")
    public Response getModuli( @QueryParam("codice_modulo") String codiceModuloQP, @QueryParam("versione_modulo") String versioneModuloQP,
    	@QueryParam("oggetto_modulo") String oggettoModulo, @QueryParam("descrizione_modulo") String descrizioneModulo, 
    	@QueryParam("stato") String statoQP, 
    	@QueryParam("codice_ambito") String codiceAmbitoQP,
    	@QueryParam("con_presenza_istanze") String conPresenzaIstanzeQP, @QueryParam("fields") String fieldsQP, 
    	@HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;  

    /**
     * Recupero di un modulo
     * @param codiceModuloPP
     * @param versioneModuloPP
     * @param fields
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     * @throws ResourceNotFoundException
     * @throws UnprocessableEntityException
     * @throws ServiceException
     */
    @GET
    @Path("/moduli/{codice-modulo}/v/{versione-modulo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModuloByCodice( @PathParam("codice-modulo") String codiceModuloPP, @PathParam("versione-modulo") String versioneModuloPP, @QueryParam("fields") String fields,
        @HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
    
    /**
     * Duplicazione di una istanza
     * @param codiceIstanzaPP
     * @param duplicaAllegati
     * @return
     * @throws ResourceNotFoundException
     * @throws UnprocessableEntityException
     * @throws ServiceException
     */
    @GET
    @Path("/istanze/{codice-istanza}/duplica")
    @Produces(MediaType.APPLICATION_JSON)
    public Response duplicaIstanza( @PathParam("codice-istanza") String codiceIstanzaPP,  @QueryParam("duplica_allegati") String duplicaAllegati,
        @HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
    
    
    
    /**
     * Cancellazione di una istanza
     * @param codiceIstanzaPP
     * @return
     * @throws ResourceNotFoundException
     * @throws UnprocessableEntityException
     * @throws ServiceException
     */
    @DELETE
    @Path("/istanze/{codice-istanza}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteIstanza( @PathParam("codice-istanza") String codiceIstanzaPP,
        @HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;

 
    
    /**
     * Recupero cronologia stati di un modulo
     * @param codiceModuloPP
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     * @throws ResourceNotFoundException
     * @throws UnprocessableEntityException
     * @throws ServiceException
     */
    @GET
    @Path("/moduli/{codice-modulo}/versioni")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCronologiaStatiModulo( @PathParam("codice-modulo") String codiceModuloPP, @QueryParam("filtro_stato") String stato,
        @HeaderParam(HttpRequestUtils.HEADER_MOON_ID_JWT) String moonId, @HeaderParam("X-Request-Id") String xRequestId,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, UnprocessableEntityException, ServiceException;
    
}
