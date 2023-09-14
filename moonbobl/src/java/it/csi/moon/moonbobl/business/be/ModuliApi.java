/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.moon.moonbobl.business.be;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloAttributo;
import it.csi.moon.moonbobl.dto.moonfobl.NuovaVersioneModuloRequest;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;

@Path("/be/moduli")
@Produces({ "application/json" })
public interface ModuliApi {
   
    @GET
    public Response getModuli( @QueryParam("idModulo") String idModuloQP, @QueryParam("onlyLastVersione") String onlyLastVersioneQP, @QueryParam("otherIdentificativoUtente") String otherIdentificativoUtenteQP, @QueryParam("pubblicatoBO") String pubblicatoBO, @QueryParam("fields") String fields, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/{idModulo}/v/{idVersioneModulo}")
    public Response getModuloById( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, @QueryParam("fields") String fields, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/struttura/{idStruttura}")
    public Response getStrutturaByIdStruttura( @PathParam("idStruttura") String idStrutturaPP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @POST
    @Consumes({ "application/json" })
    public Response saveModulo( Modulo body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @PUT
    @Path("/{idModulo}/v/{idVersioneModulo}")
    @Consumes({ "application/json" })
    public Response putModulo( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, Modulo body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idModulo}/v/{idVersioneModulo}/initCambiaStato")
    public Response getInitCambiaStatoById( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @PUT
    @Path("/pubblica/{idModulo}/v/{idVersioneModulo}")
    @Consumes({ "application/json" })
    public Response pubblicaModulo( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @PUT
    @Path("/cambiaStato/{idModulo}/v/{idVersioneModulo}/newStato/{newStato}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response cambiaStatoModulo( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, @PathParam("newStato") String newStatoPP, 
    	@QueryParam("inDataOra") String inDataOraQP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ServiceException;

    @PUT
    @Path("/{idModulo}/v/{idVersioneModulo}/struttura")
    @Consumes({ "application/json" })
    public Response putStruttura( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, Modulo body, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idModulo}/v/{idVersioneModulo}/campi")
    public Response getCampiModulo( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, @QueryParam("onlyFirstLevel") String onlyFirstLevel,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/{idModulo}/campi")
    public Response getCampiModuloVersioneCorrente( @PathParam("idModulo") String idModuloPP, @QueryParam("onlyFirstLevel") String onlyFirstLevel,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/{idModulo}/attributi")
    public Response getAttributiModulo( @PathParam("idModulo") String idModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/{idModulo}/attributi-BO")
    public Response getObjAttributiModuloBO( @PathParam("idModulo") String idModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Path("/{idModulo}/attributi-generali")
    public Response postAttributiGenerali( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Path("/{idModulo}/attributi-email")
    public Response postAttributiEmail( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Path("/{idModulo}/attributi-notificatore")
    public Response postAttributiNotificatore( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Path("/{idModulo}/attributi-protocollo")
    public Response postAttributiProtocollo( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Path("/{idModulo}/attributi-cosmo")
    public Response postAttributiCosmo( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Path("/{idModulo}/attributi-azione")
    public Response postAttributiAzione( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Path("/{idModulo}/attributi-estrai-dichiarante")
    public Response postAttributiEstraiDichiarante( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Path("/{idModulo}/attributi-crm")
    public Response postAttributiCrm( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;
    
    @POST
    @Path("/{idModulo}/attributi-epay")
    public Response postAttributiEpay( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/stati")
	public Response getElencoStatiModulo(@QueryParam("codiceProvenienza") String codiceProvenienza, @QueryParam("codiceDestinazione") String codiceDestinazione,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/{idModulo}/initNuovaVersioneModulo")
    public Response getinitNuovaVersioneModuloById( @PathParam("idModulo") String idModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @DELETE
    @Path("/{idModulo}/v/{idVersioneModulo}")
    public Response deleteModulo( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @POST
    @Path("/{idModulo}/v/{idVersioneModulo}/portali")
    public Response savePortaliModulo( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP, List<Long> portali,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @POST
    @Path("/{idModulo}/nuovaVersione")
    public Response nuovaVersione( @PathParam("idModulo") String idModuloPP, NuovaVersioneModuloRequest body,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;
    
    @GET
    @Path("/{idModulo}/versioni")
    public Response getVersioniModuloById( @PathParam("idModulo") String idModuloPP, @QueryParam("fields") String fields, 
       	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idModulo}/utenti")
    public Response getUtentiAbilitati( @PathParam("idModulo") String idModuloPP, @QueryParam("fields") String fields, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException;

    @GET
    @Path("/{idModulo}/v/{idVersioneModulo}/protocollo-parametri")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrtParametri( @PathParam("idModulo") String idModuloPP, @PathParam("idVersioneModulo") String idVersioneModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/{idModulo}/has-protocollo-parametri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hasPrtParametri( @PathParam("idModulo") String idModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    
    @POST
    @Path("/{idModulo}/modulo-class-for-tipologia/{idTipologia}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postModuloClass( @PathParam("idModulo") String idModuloPP, @PathParam("idTipologia") int idTipologia, MultipartFormDataInput file,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @DELETE
    @Path("/{idModulo}/modulo-class-for-tipologia/{idTipologia}")
    public Response deleteModuloClass( @PathParam("idModulo") String idModuloPP, @PathParam("idTipologia") int idTipologia,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idModulo}/modulo-class-for-tipologia/{idTipologia}")
    public Response getFileClassByIdModuloTipologia( @PathParam("idModulo") String idModuloPP, @PathParam("idTipologia") int idTipologia,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idModulo}/modulo-class")
    public Response getFileClassByIdModulo( @PathParam("idModulo") String idModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/codice/{codiceModulo}/print-mapper-name")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response getPrintMapperName( @PathParam("codiceModulo") String codiceModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/codice/{codiceModulo}/protocollo-manager-name")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response getProtocolloManagerName( @PathParam("codiceModulo") String codiceModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{idModulo}/has-protocollo-bo")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hasPrtBo( @PathParam("idModulo") String idModuloPP,
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

    @GET
    @Path("/codice/{codiceModulo}/epay-manager-name")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response getEpayManagerName( @PathParam("codiceModulo") String codiceModuloPP, 
    	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
}
