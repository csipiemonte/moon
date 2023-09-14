/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
  * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonsrv.business.be;

import java.io.InputStream;
import java.util.concurrent.CompletionStage;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
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

@Path("/moduli")
@Produces(MediaType.APPLICATION_JSON)
public interface ModuliApi {
   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModuli( @QueryParam("idModulo") String idModulo, @QueryParam("idVersioneModulo") String idVersioneModulo, 
    	@QueryParam("codiceModulo") String codiceModulo, @QueryParam("versioneModulo") String versioneModulo, @QueryParam("oggettoModulo") String oggettoModulo, @QueryParam("descrizioneModulo") String descrizioneModulo, 
    	@QueryParam("flagIsRiservato") String flagIsRiservato, @QueryParam("idTipoCodiceIstanza") Integer idTipoCodiceIstanza, @QueryParam("flagProtocolloIntegrato") String flagProtocolloIntegrato, @QueryParam("stato") String stato, 
    	@QueryParam("dataEntroIntDiPub") Boolean dataEntroIntDiPub, @QueryParam("conPresenzaIstanzeUser")  String conPresenzaIstanzeUser, @QueryParam("nomePortale") String nomePortale,
    	@QueryParam("idEnte") String idEnte, @QueryParam("idArea") String idArea, @QueryParam("idAmbito") String idAmbito, @QueryParam("idVisibilitaAmbito") String idVisibilitaAmbito,
    	@QueryParam("codiceEnte") String codiceEnte, @QueryParam("codiceArea") String codiceArea, @QueryParam("codiceAmbito") String codiceAmbito, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/async/")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> getModuliAsync( @QueryParam("idModulo") String idModulo, @QueryParam("idVersioneModulo") String idVersioneModulo, 
    	@QueryParam("codiceModulo") String codiceModulo, @QueryParam("versioneModulo") String versioneModulo, @QueryParam("oggettoModulo") String oggettoModulo, @QueryParam("descrizioneModulo") String descrizioneModulo, 
    	@QueryParam("flagIsRiservato") String flagIsRiservato, @QueryParam("idTipoCodiceIstanza") Integer idTipoCodiceIstanza, @QueryParam("flagProtocolloIntegrato") String flagProtocolloIntegrato, @QueryParam("stato") String stato, 
    	@QueryParam("dataEntroIntDiPub") Boolean dataEntroIntDiPub, @QueryParam("conPresenzaIstanzeUser")  String conPresenzaIstanzeUser, @QueryParam("nomePortale") String nomePortale,
    	@QueryParam("idEnte") String idEnte, @QueryParam("idArea") String idArea, @QueryParam("idAmbito") String idAmbito, @QueryParam("idVisibilitaAmbito") String idVisibilitaAmbito,
    	@QueryParam("codiceEnte") String codiceEnte, @QueryParam("codiceArea") String codiceArea, @QueryParam("codiceAmbito") String codiceAmbito, 
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idModulo}/v/{idVersioneModulo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModuloById( @PathParam("idModulo") Long idModuloPP, @PathParam("idVersioneModulo") Long idVersioneModuloPP, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idModulo}/v/{idVersioneModulo}/campi")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCampiModulo( @PathParam("idModulo") Long idModuloPP, @PathParam("idVersioneModulo") Long idVersioneModuloPP, @QueryParam("onlyFirstLevel") String onlyFirstLevel, @QueryParam("type") String type,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
 
    @GET
    @Path("/dati-azione/{idDatiAzione}/campi")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCampiDatiAzione( @PathParam("idDatiAzione") Long idDatiAzionePP, @QueryParam("onlyFirstLevel") String onlyFirstLevel, @QueryParam("type") String type,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/codice/{codiceModulo}/versione/{versione}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModuloByCd( @PathParam("codiceModulo") String codiceModuloPP, @PathParam("versione") String versionePP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
  @GET
  @Path("/codice/{codiceModulo}/pubblicato")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getModuloPubblicatoByCodice( @PathParam("codiceModulo") String codiceModuloPP, 
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
  @GET
  @Path("/{idModulo}/v/{idVersioneModulo}/protocollo-parametri")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPrtParametri( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP,
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
  
  
  @GET
  @Path("/{idModulo}/v/{idVersioneModulo}/mydocs-parametri")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMyDocsParametri( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP,
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );  
  
  @POST
  @Path("/{idModulo}/modulo-class-for-tipologia/{idTipologia}") 
  public Response postModuloClass( @PathParam("idModulo") String idModuloPP, @PathParam("idTipologia") int idTipologia, InputStream file,
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

  @DELETE
  @Path("/{idModulo}/modulo-class-for-tipologia/{idTipologia}")
  public Response deleteModuloClass( @PathParam("idModulo") String idModuloPP, @PathParam("idTipologia") int idTipologia,
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
  
  @GET
  @Path("/codice/{codiceModulo}/print-mapper-name")
  @Produces({ MediaType.TEXT_PLAIN })
  public Response getPrintMapperName100( @PathParam("codiceModulo") String codiceModuloPP, 
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
  
  @GET
  @Path("/codice/{codiceModulo}/print-mapper-name/{versioneModulo}")
  @Produces({ MediaType.TEXT_PLAIN })
  public Response getPrintMapperName( @PathParam("codiceModulo") String codiceModuloPP, @PathParam("versioneModulo") String versioneModuloPP,
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
  
  //
  // PROTOCOLLO
  //
  @GET
  @Path("/codice/{codiceModulo}/protocollo-manager-name")
  @Produces({ MediaType.TEXT_PLAIN })
  public Response getProtocolloManagerName( @PathParam("codiceModulo") String codiceModuloPP, 
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

  //
  // EPAY
  //
  @GET
  @Path("/codice/{codiceModulo}/epay-manager-name")
  @Produces({ MediaType.TEXT_PLAIN })
  public Response getEpayManagerName( @PathParam("codiceModulo") String codiceModuloPP, 
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
  
  @GET
  @Path("/{idModulo}/valida-attributi/{categoriaAttributi}")
  public Response validaAttributiModulo( @PathParam("idModulo") String idModuloPP, @PathParam("categoriaAttributi") String categoriaAttributiPP,
  	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
}
