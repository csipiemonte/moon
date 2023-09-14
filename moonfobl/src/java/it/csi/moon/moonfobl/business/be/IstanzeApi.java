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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.http.MediaType;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;

@Path("/be/istanze")
@Produces({ "application/json" })
public interface IstanzeApi {
   
    @GET
    @Produces({ "application/json" })
    public Response getIstanze( @QueryParam("idTabFo") String idTabFoQP, 
    	@QueryParam("stato") String statoQP, @QueryParam("statiIstanza") List<String> statiIstanza,  @QueryParam("importanza") String importanzaQP, 
		@QueryParam("codiceIstanza") String codiceIstanza, 
		@QueryParam("idModulo") String idModuloQP, @QueryParam("titoloModulo") String titoloModulo, 
		@QueryParam("created_start") Date createdStart, @QueryParam("created_end") Date createdEnd,
		@QueryParam("codiceFiscale") String codiceFiscale, @QueryParam("cognome") String cognome, @QueryParam("nome") String nome,
		@QueryParam("sort") String sort, 
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/paginate")
    @Produces({ "application/json" })
    public Response getIstanzePaginated( 
    	@QueryParam("statiIstanza") List<String> statiIstanza,
    	@QueryParam("idTabFo") String idTabFoQP, 
    	@QueryParam("stato") String statoQP,  @QueryParam("importanza") String importanzaQP, 
		@QueryParam("codiceIstanza") String codiceIstanza, 
		@QueryParam("idModulo") String idModuloQP, @QueryParam("titoloModulo") String titoloModulo, 
		@QueryParam("created_start") Date createdStart, @QueryParam("created_end") Date createdEnd, 
		@QueryParam("codiceFiscale") String codiceFiscale, @QueryParam("cognome") String cognome, @QueryParam("nome") String nome,
		@QueryParam("sort") String sort, 
		@QueryParam("offset") String offsetQP, @QueryParam("limit") String limitQP, 
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/{idIstanza}")
    @Produces({ "application/json" })
    public Response getIstanzaById( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/codice/{codiceIstanza}")
    @Produces({ "application/json" })
    public Response getIstanzaByCodice( @PathParam("codiceIstanza") String codiceIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );


    @POST
    @Path("/init/{idModulo}/v/{idVersioneModulo}")
    @Produces({ "application/json" })
    public Response getInitIstanza( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, IstanzaInitBLParams params,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response saveIstanza( Istanza body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PUT
    @Path("/{idIstanza}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response putIstanza( @PathParam("idIstanza") String idIstanzaQP, Istanza body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @DELETE
    @Path("/{idIstanza}")
    @Produces({ "application/json" })
    public Response deleteIstanzaById( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PATCH
    @Path("/{idIstanza}")
    @Produces({ "application/json" })
    public Response patchIstanzaById( @PathParam("idIstanza") String idIstanzaQP, Istanza partialIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    @GET
    @Path("/pdf/{idIstanza}")
    @Consumes({ "application/json" })
    @Produces({ "application/pdf" })
    public Response getPdf( @PathParam("idIstanza") String idIstanzaQP, @Context SecurityContext securityContext, 
    	@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    @POST
    @Path("/{idIstanza}/star")
    @Produces({ "application/json" })
    public Response starIstanza( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @POST
    @Path("/{idIstanza}/unstar")
    @Produces({ "application/json" })
    public Response unstarIstanza( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @PATCH
    @Path("/{idIstanza}/riportaInBozza")
    @Produces({ "application/json" })
    public Response riportaInBozza( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PATCH
    @Path("/{idIstanza}/invia")
    @Produces({ "application/json" })
    public Response invia( @PathParam("idIstanza") String idIstanzaPP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @POST
    @Path("/{idIstanza}/compieAzione/{idAzione}")
    @Produces({ "application/json" })
    public Response compieAzione( @PathParam("idIstanza") String idIstanzaQP, @PathParam("idAzione") String idAzioneQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
     * Ritorna il pdf di un istanza già salvata e identificata con sua PK idIstanza
     * 
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return pdf
     */
    @GET
    @Path("/{idIstanza}/pdf")
    @Produces(MediaType.APPLICATION_PDF_VALUE)
    public Response getPdfById( @PathParam("idIstanza") String idIstanzaPP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idIstanza}/notifica")
    @Produces(MediaType.APPLICATION_PDF_VALUE)
    @Deprecated
    public Response getNotificaById( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idIstanza}/documento-notifica")
    @Deprecated
    public Response getDocumentoNotificaById( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/documento-by-name/{formioNameFile}")
    @Deprecated
    public Response getDocumentoByFormioNameFile( @PathParam("formioNameFile") String formioNameFile, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );    
    
    @GET
    @Path("/documento-by-id/{idFile}")
    public Response getDocumentoByIdFile( @PathParam("idFile") Long idFile, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );  

    @GET
    @Path("/{idIstanza}/allegati")
    @Produces({ "application/json" })
    public Response getAllegati( @PathParam("idIstanza") String idIstanzaPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @GET
    @Path("/{idIstanza}/documenti")
    @Produces({ "application/json" })
    public Response getDocumenti( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @GET
    @Path("/allegato/{formioFileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Response getAllegato(@PathParam("formioFileName") String formioFileName,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) ;
    
    @POST
    @Path("/{idIstanza}/integrazione-cosmo")
    public Response inviaRispostaIntegrazioneCosmo( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );


    @GET
    @Path("/{idIstanza}/documenti-protocollati")
    @Produces({ "application/json" })
    public Response getDocumentiProtocollati( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @GET
    @Path("/{idIstanza}/documenti-emessi-ufficio")
    @Produces({ "application/json" })
    public Response getDocumentiEmessi( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	

    
    @POST
    @Path("{idIstanza}/duplica")
    public Response duplicaIstanza( @PathParam("idIstanza") String idIstanza,  @QueryParam("duplicaAllegati") String duplicaAllegati,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
}
