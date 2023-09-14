/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be;

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

import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;

@Path("/be/istanze")
@Produces({ "application/json" })
public interface IstanzeApi {
   
    @GET
    @Produces({ "application/json" })
    public Response getIstanze( @QueryParam("stato") Integer stato,  @QueryParam("sort") String sort,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idIstanza}")
    @Produces({ "application/json" })
    public Response getIstanzaById( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/bozza/{idIstanza}")
    @Produces({ "application/json" })
    public Response getIstanzaBozzaById( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

	@GET
	@Path("/moduli/{idModulo}")
	@Produces({ "application/json" })
	public Response getIstanzeByModulo(@PathParam("idModulo") Long idModulo, @QueryParam("stato") Integer stato,
			@QueryParam("sort") String sort, @QueryParam("filtroRicerca") String filtroRicerca,
			@QueryParam("cfDichiarante") String cfDichiarante,@QueryParam("nomeDichiarante") String nomeDichiarante,@QueryParam("cognomeDichiarante") String cognomeDichiarante,
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/paginate")
    @Produces({ "application/json" })
    public Response getIstanzePaginate( 
    		@QueryParam("idModulo") Long idModulo, @QueryParam("idEnte") Long idEnte, 
    		@QueryParam("cfDichiarante") String cfDichiarante,@QueryParam("nomeDichiarante") String nomeDichiarante,@QueryParam("cognomeDichiarante") String cognomeDichiarante, 
    		@QueryParam("stato") Integer stato,
    		@QueryParam("codiceIstanza") String codiceIstanza, @QueryParam("protocollo") String protocollo, 
    		@QueryParam("dataDal") Date startDate, @QueryParam("dataAl") Date endDate, 
    		@QueryParam("sort") String sort,  
    		@QueryParam("filtroRicerca") String filtroRicerca, 
    		@QueryParam("filtroEpay") String filtroEpay, 
    		@QueryParam("offset") String offset, 
    		@QueryParam("limit") String limit,
    		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
	@GET
	@Path("/lav/moduli/{idModulo}")
	@Produces({ "application/json" })
	public Response getIstanzeInLavByModulo(@PathParam("idModulo") Long idModulo,
			@QueryParam("filtroRicerca") String filtroRicerca, 
			@QueryParam("cfDichiarante") String cfDichiarante,@QueryParam("nomeDichiarante") String nomeDichiarante,@QueryParam("cognomeDichiarante") String cognomeDichiarante,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

    @POST
    @Path("/init/{idModulo}/v/{idVersioneModulo}")
    @Produces({ "application/json" })
    public Response getInitIstanza( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, IstanzaInitBLParams params,
        	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @POST
    @Path("/pdf")
    @Consumes({ "application/json" })
    @Produces({ "application/pdf" })
    public Response getPdf( Istanza body,@Context SecurityContext securityContext,
    	@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @PUT
    @Path("/{idIstanza}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response putIstanza( @PathParam("idIstanza") Long idIstanza, Istanza body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response saveIstanza( Istanza body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @POST
    @Path("compila")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response saveIstanzaCompila( Istanza body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @DELETE
    @Path("/{idIstanza}")
    @Produces({ "application/json" })
    public Response deleteIstanzaById( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PATCH
    @Path("/{idIstanza}")
    @Produces({ "application/json" })
    public Response patchIstanzaById( @PathParam("idIstanza") Long idIstanza, Istanza partialIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @POST
    @Path("/{idIstanza}/star")
    @Produces({ "application/json" })
    public Response starIstanza( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @POST
    @Path("/{idIstanza}/unstar")
    @Produces({ "application/json" })
    public Response unstarIstanza( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
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
    public Response getPdfById( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    
    /**
     * Ritorna il json di un istanza già salvata e identificata con la sua PK idIstanza
     * 
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return pdf
     */
    @GET
    @Path("/{idIstanza}/json")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getJsonById( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );    
    
    /**
     * Ritorna il pdf dell'ultima ricevuta identificata con sua PK idIstanza
     * 
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return pdf
     */
    @GET
    @Path("/{idIstanza}/ricevutaPdf")
    @Produces(MediaType.APPLICATION_PDF_VALUE)
    public Response getRicevutaPdfById( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
             
	@GET
	@Path("/modulo/{idModulo}/json")
	@Produces({ "application/json" })
	public Response getJsonIstanzeByModulo(@PathParam("idModulo") Long idModulo, @QueryParam("idEnte") Long idEnte,
			@QueryParam("stati") List<String> stati, 
	    	@QueryParam("state_start") Date stateStartDate, 
	    	@QueryParam("state_end") Date stateEndDate, 
			@QueryParam("created_start") Date createdStart,
			@QueryParam("created_end") Date createdEnd, 
			@QueryParam("sort") String sort,
			@QueryParam("filtroRicerca") String filtroRicerca, 
			@QueryParam("filtroEpay") String filtroEpay, 
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
	@GET
	@Path("/modulo/{idModulo}/custom-json")
    @Produces(MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public Response getCustomJsonIstanzeByModulo(@PathParam("idModulo") Long idModulo,
			@QueryParam("codice_estrazione") String codiceEstrazione,
			@QueryParam("created_start") String createdStart,
			@QueryParam("created_end") String createdEnd,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @GET
    @Path("/paginate/modulo/{idModulo}/json")
    @Produces({ "application/json" })
    public Response getJsonIstanzePaginateByModulo( @PathParam("idModulo") Long idModulo,  
    	@QueryParam("idEnte") Long idEnte, 
    	@QueryParam("stati") List<String> stati, 
    	@QueryParam("state_start") Date stateStartDate, 
    	@QueryParam("state_end") Date stateEndDate, 
    	@QueryParam("created_start")  Date createdStart, 
    	@QueryParam("created_end") Date createdEnd, 
    	@QueryParam("sort") String sort,  
    	@QueryParam("filtroRicerca") String filtroRicerca,
    	@QueryParam("filtroEpay") String filtroEpay, 
    	@QueryParam("offset") String offset,
		@QueryParam("limit") String limit,    	
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );    
        
    
	@GET
	@Path("/modulo/{idModulo}/json/count")
	@Produces({ "application/json" })
	public Response getCountJsonIstanzeByModulo(@PathParam("idModulo") Long idModulo, 
			@QueryParam("idEnte") Long idEnte,
			@QueryParam("stati") List<String> stati, 
			@QueryParam("state_start") Date stateStartDate, 
			@QueryParam("state_end") Date stateEndDate, 
			@QueryParam("created_start") Date createdStart,
			@QueryParam("created_end") Date createdEnd, 
			@QueryParam("sort") String sort,
			@QueryParam("filtroRicerca") String filtroRicerca, 
			@QueryParam("filtroEpay") String filtroEpay, 
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @GET
    @Path("/archivio/paginate")
    @Produces({ "application/json" })
    public Response getIstanzeArchivioPaginate( 
		@QueryParam("idModulo") Long idModulo, 
		@QueryParam("idEnte") Long idEnte, 
		@QueryParam("stati") List<String> stati,
		@QueryParam("codiceIstanza") String codiceIstanza, 
		@QueryParam("protocollo") String protocollo, 
		@QueryParam("state_start") Date stateStartDate, @QueryParam("state_end") Date stateEndDate, 
		@QueryParam("created_start") Date startDate, @QueryParam("created_end") Date endDate,
		@QueryParam("cfDichiarante") String codiceFiscale, @QueryParam("cognome") String cognome, @QueryParam("nome") String nome,
		@QueryParam("sort") String sort,  
		@QueryParam("filtroRicerca") String filtroRicerca, 
		@QueryParam("filtroEpay") String filtroEpay, 
		@QueryParam("offset") String offset, 
		@QueryParam("limit") String limit,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );  
    
    @GET
    @Path("/{idIstanza}/allegati")
    @Produces({ "application/json" })
    public Response getAllegati( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
    
    @GET
    @Path("/allegato/{formioFileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Response getAllegato(@PathParam("formioFileName") String formioFileName,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest) ;
    
    
    @GET
    @Path("/lav/moduli/paginate/{idModulo}")
    @Produces({ "application/json" })
    public Response getIstanzeInLavByModuloPaginate( @PathParam("idModulo") Long idModulo, @QueryParam("idEnte") Long idEnte, 
		@QueryParam("filtroRicerca") String filtroRicerca,
		@QueryParam("filtroEpay") String filtroEpay, 
		@QueryParam("sort") String sort,  
		@QueryParam("cfDichiarante") String cfDichiarante,@QueryParam("nomeDichiarante") String nomeDichiarante,@QueryParam("cognomeDichiarante") String cognomeDichiarante,
		@QueryParam("offset") String offset,
		@QueryParam("limit") String limit,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    
    @GET
    @Path("/bozza/moduli/paginate/{idModulo}")
    @Produces({ "application/json" })
    public Response getIstanzeInBozzaByModuloPaginate( @PathParam("idModulo") Long idModulo, @QueryParam("idEnte") Long idEnte, 
		@QueryParam("filtroRicerca") String filtroRicerca,
		@QueryParam("sort") String sort,  
		@QueryParam("cfDichiarante") String cfDichiarante,@QueryParam("nomeDichiarante") String nomeDichiarante,@QueryParam("cognomeDichiarante") String cognomeDichiarante, 
		@QueryParam("offset") String offset,
		@QueryParam("limit") String limit,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @GET
    @Path("/dacompletare/moduli/paginate/{idModulo}")
    @Produces({ "application/json" })
    public Response getIstanzeDaCompletareByModuloPaginate( @PathParam("idModulo") Long idModulo, @QueryParam("idEnte") Long idEnte, 
		@QueryParam("filtroRicerca") String filtroRicerca,
		@QueryParam("sort") String sort,  
		@QueryParam("cfDichiarante") String cfDichiarante,@QueryParam("nomeDichiarante") String nomeDichiarante,@QueryParam("cognomeDichiarante") String cognomeDichiarante, 
		@QueryParam("offset") String offset,
		@QueryParam("limit") String limit,
		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);    
    
    @POST
    @Path("/protocolla/{idIstanza}")
    public Response postProtocolla( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

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

    @POST
    @Path("/{idIstanza}/rinvia-email")
    public Response postRinviaEmail( @PathParam("idIstanza") Long idIstanza, @QueryParam("dest") String dest,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @POST
    @Path("/rinvia-emails")
    public Response postRinviaEmails( @QueryParam("idTag") String idTag, @QueryParam("dest") String dest,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @PATCH
    @Path("/{idIstanza}/riportaInBozza")
    @Produces({ "application/json" })
    public Response riportaInBozza( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PATCH
    @Path("/{idIstanza}/invia")
    @Produces({ "application/json" })
    public Response invia( @PathParam("idIstanza") String idIstanzaQP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idIstanza}/log-email")
    public Response getLogEmail( @PathParam("idIstanza") Long idIstanza,
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
    
    
    /**
     * recupera LOG Servizio COSMO
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @GET
    @Path("/{idIstanza}/log-servizio-cosmo")
    public Response getLogServizioCosmo( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    /**
     * Invia Integrazione Cosmo
     * 
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    
    @POST
    @Path("/{idIstanza}/integrazione-cosmo")
    public Response inviaRispostaIntegrazioneCosmo( @PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @POST
    @Path("/mydocs/{idIstanza}/storico/{idStoricoWorkflow}")
	Response pubblicaMyDocs(@PathParam("idIstanza") Long idIstanza, @PathParam("idStoricoWorkflow") Long idStoricoWorkflow, 
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @POST
    @Path("/mydocs/{idIstanza}/log-richiesta/{idRichiesta}")
	Response pubblicaIstanzaMyDocs(@PathParam("idIstanza") Long idIstanza, @PathParam("idRichiesta") Long idRichiesta, 
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    /**
     * recupera LOG MyDocs
     * @param idIstanza
     * @param securityContext
     * @param httpHeaders
     * @param httpRequest
     * @return
     */
    @GET
    @Path("/mydocs/richieste/{idIstanza}")
    public Response getLogMyDocs(@PathParam("idIstanza") Long idIstanza,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );    

  
    
}
