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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.http.MediaType;

import it.csi.moon.commons.dto.EPayPagoPAParams;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.moonsrv.business.rs.annotations.DateFormat;

/**
 * API sulle istanze
 * Usato per :
 * - Inizializzazione delle istanze all'inizio di una compilazione di un modulo
 * - Richiesta di generazione del PDF dell'istanza
 * 
 * La letture delle istanze gia salvate, il salvataggio delle istanze viene realizzato direttamente dai backEnd BL dei diversi applicativi
 * 
 * @author Laurent
 * @author franc
 *
 */
@Path("/istanze")
@Produces(MediaType.APPLICATION_JSON_VALUE)
public interface IstanzeApi {

    @GET
    public Response getIstanze( @QueryParam("cfDichiarante") String cfDichiarante, @QueryParam("stato") Integer stato,  @QueryParam("importanza") Integer importanza, @QueryParam("idModulo") Long idModulo, @QueryParam("sort") String sort, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idIstanza}")
    public Response getIstanzaById( @PathParam("idIstanza") Long idIstanza, @QueryParam("fields") String fields,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idIstanza}/data")
    public Response getIstanzaDataById( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @POST
    @Path("/init")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public Response getInitIstanza( IstanzaInitParams initParams, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    /**
     * Genera e salva il pdf di un istanza già salvata ed inviata identificata con sua PK idIstanza
     * 
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return String OK
     */
    @POST
    @Path("/{idIstanza}/generaSalvaPdf")
    public Response generaSalvaPdf( @PathParam("idIstanza") Long idIstanza, 
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
    public Response getPdfById( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idIstanza}/notifica")
    @Produces(MediaType.APPLICATION_PDF_VALUE)
    public Response getNotificaById( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/documenti/by-name/{formioNameFile}")
    public Response getDocumentoByFormioNameFile( @PathParam("formioNameFile") String formioNameFile, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/documenti/by-id/{idFile}")
    public Response getDocumentoByIdFile( @PathParam("idFile") Long idFile, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idIstanza}/documento-notifica")
    public Response getDocumentoNotificaById( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );    
    
    @GET
    @Path("/{idIstanza}/generaPdf")
    @Produces(MediaType.APPLICATION_PDF_VALUE)
    public Response generaPdfById( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    //x test
    @GET
    @Path("/{idIstanza}/generaPdfClassLoader")
    @Produces(MediaType.APPLICATION_PDF_VALUE)
    public Response generaPdfClassLoaderById( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    @GET
    @Path("/{idIstanza}/moonprintDocument")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getMoonprintDocumentJson( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    /**
     * Ritorna il pdf di un istanza passata nel body
     * 
     * @param body
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @POST
    @Path("/generaPdf")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_PDF_VALUE)
    public Response generaPdfFromBody( Istanza body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    //
    // PROTOCOLLO
    //
    
    /**
     * Protocolla un idIstanza
     * 
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     */
    @POST
    @Path("/protocolla/{idIstanza}")
    public Response postProtocolla( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    //x test
    @GET
    @Path("/{idIstanza}/retrieveMetadatiIstanza")
    public Response retrieveMetadatiIstanza( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    //x test
    @GET
    @Path("/{idIstanza}/retrieveMetadatiAllegato/{idAllegato}")
    public Response retrieveMetadatiAllegato( @PathParam("idIstanza") Long idIstanza, @PathParam("idAllegato") Long idAllegato, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/protocolla-massivo")
    public Response getProtocollaMassivo( @QueryParam("idTag") Long idTag,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
//    @GET
//    @Path("/archiviadoqui-massivo")
//    public Response getArchiviaDoquiMassivo( @QueryParam("idTag") Long idTag,
//    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
   @GET
    @Path("/generaHashUnivocita-massivo")
    public Response generaHashUnivocitaMassivo( @QueryParam("idTag") Long idTag,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    
    //
    // EMAIL
    //
    
    /**
     * Rinvia Email
     * @param idIstanza
     * @param dest
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @POST
    @Path("/{idIstanza}/rinvia-email")
    public Response postRinviaEmail( @PathParam("idIstanza") Long idIstanza, @QueryParam("dest") String dest,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
     * Rinvia Email
     * @param idTag
     * @param dest
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @GET
    @POST
    @Path("/rinvia-emails")
    public Response postRinviaEmails( @QueryParam("idTag") Long idTag, @QueryParam("dest") String dest,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
     * recupera log email di un istanza
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return log email
     */
    @GET
    @Path("/{idIstanza}/log-email")
    public Response getLogEmail( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
    //
    // ESTRAI DICH
    //
    
    /**
     * Aggiorna CF,Nom,Cognome dell'istanza estraiendole dai dati istanza (per CAF)
     * @param idIstanza
     * @param dest
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @POST
    @Path("/{idIstanza}/estrai-dichiarante-update")
    public Response postEstraiDichiaranteUpdate( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    //
    // INTEGRAZIONE
    //
    
    /**
     * Ritorna il pdf di un integrazione di un istanza
     * 
     * @param body
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @POST
    @Path("/{idIstanza}/generaPdfIntegrazione/{idStoricoWorkflow}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_PDF_VALUE)
    public Response generaPdfIntegrazione( @PathParam("idIstanza") Long idIstanza, @PathParam("idStoricoWorkflow") Long idStoricoWorkflow,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
     * Protocolla un integrazione di un istanza
     * 
     * @param idIstanza
     * @param idStoricoWorkflow
     * @param prtParams
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     */
    @POST
    @Path("/{idIstanza}/protocolla-integrazione/{idStoricoWorkflow}")
    public Response postProtocollaIntegrazione( @PathParam("idIstanza") Long idIstanza, @PathParam("idStoricoWorkflow") Long idStoricoWorkflow,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    
    //
    // WF COSMO
    //
    
    /**
     * CreaPraticaEdAvviaProcessoCosmo
     * 
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @POST
    @Path("/creaPraticaEdAvviaProcesso/{idIstanza}")
    public Response postCreaPraticaEdAvviaProcessoCosmo( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
     * recupera LOG Pratica COSMO
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @GET
    @Path("/{idIstanza}/log-pratica-cosmo")
    public Response getLogPraticaCosmo( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @POST
    @Path("/{idIstanza}/integrazione-cosmo")
    public Response inviaRispostaIntegrazioneCosmo( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    
    //
    // CRM Ticketing System
    //
    
    /**
     * crea Ticket su CRM
     * 
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     */
    @POST
    @Path("/crea-ticket-crm/{idIstanza}")
    public Response postCreaTicketCrmIstanza( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/crea-ticket-crm-massivo")
    public Response postCreaTicketCrmIstanzaMassivo( @QueryParam("idTag") Long idTag,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
        
    
    //
    // EPAY
    //
    
    /**
     * crea un IUV su PPay
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @POST
    @Path("/{idIstanza}/crea-iuv")
    public Response creaIUV( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
     * richiedi il pagamento online via PagoPA di uno IUV
     * @param idIstanza
     * @param pagoPAParams
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return response con URL wisp per redirect
     */
    @POST
    @Path("/{idIstanza}/pago-pa")
    public Response pagoPA( @PathParam("idIstanza") Long idIstanza, EPayPagoPAParams pagoPAParams,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
     * annulla un IUV su PPay e ne crea uno nuovo
     * @param idIstanza
     * @param iuv
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return nuovo IUV valido
     */
    @POST
    @Path("/{idIstanza}/annulla-iuv/{iuv}")
    public Response annullaIUV( @PathParam("idIstanza") Long idIstanza, @PathParam("iuv") String iuv,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/download-archive/{idModulo}")
    @Produces(MediaType.TEXT_PLAIN_VALUE)
    public Response downloadAllFileIstanzeByIdModulo( @PathParam("idModulo") Long idModulo,
    		@QueryParam("data_dal") @DateFormat Date dataDal, @QueryParam("data_al") @DateFormat Date dataAl,
        	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/download-archive/repository-file/{idModulo}")
    @Produces(MediaType.TEXT_PLAIN_VALUE)
	Response downloadRepositoryFileByIdModulo(@PathParam("idModulo") Long idModulo,
			@QueryParam("data_dal") @DateFormat Date dataDal, @QueryParam("data_al") @DateFormat Date dataAl, 
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/download-archive/repository-file-istanza/{idIstanza}")
    @Produces(MediaType.TEXT_PLAIN_VALUE)
	Response downloadRepositoryFileByIdIstanza(@PathParam("idIstanza") Long idIstanza, 
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			HttpServletRequest httpRequest);
	
    @GET
    @Path("/{idIstanza}/archivia-doqui-index")
    @Produces(MediaType.TEXT_PLAIN_VALUE)
    public Response salvaIstanzaIndexById( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
 // FOR TEST
    @POST
    @Path("/{idIstanza}/compie-azione-automatica")
    @Produces(MediaType.TEXT_PLAIN_VALUE)
    public Response compieAzioneAutomatica( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    /**
     * Ritorna il riepilogo degli allegati di un istanza già salvata e identificata con sua PK idIstanza
     * 
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return AllegatiSummary riepilogo degli alletati (numero di allegati e dimensione totale)
     */
    @GET
    @Path("/{idIstanza}/allegati-summary")
    public Response getAllegatiSummary( @PathParam("idIstanza") Long idIstanza, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    
}
