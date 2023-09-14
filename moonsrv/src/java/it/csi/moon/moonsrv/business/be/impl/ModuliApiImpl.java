/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.ModuloClass;
import it.csi.moon.commons.dto.ProtocolloParametro;
import it.csi.moon.commons.dto.ReportVerificaAttributiModulo;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonsrv.business.be.ModuliApi;
import it.csi.moon.moonsrv.business.service.ModuliService;
import it.csi.moon.moonsrv.business.service.ModuloClassService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.ProtocolloService;
import it.csi.moon.moonsrv.business.service.doc.MyDocsService;
import it.csi.moon.moonsrv.business.service.epay.EpayService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ModuliApiImpl extends MoonBaseApiImpl implements ModuliApi {
	
	private static final String CLASS_NAME = "ModuliApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ModuliService moduliService;
	@Autowired
	ProtocolloService protocolloService;
	@Autowired
	EpayService epayService;
	@Autowired
	ModuloClassService moduloClassService;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	MyDocsService myDocsService;
	
	
	public Response getModuli(String idModuloQP, String idVersioneModuloQP, 
			String codiceModulo, String versioneModulo, String oggettoModulo, String descrizioneModulo, 
			String flagIsRiservato, Integer idTipoCodiceIstanza, String flagProtocolloIntegrato, String stato, 
			Boolean dataEntroIntDiPubblicazione, String conPresenzaIstanzeUser, String nomePortale,
			String idEnteQP, String idAreaQP, String idAmbitoQP, String idVisibilitaAmbitoQP,
			String codiceEnte, String codiceArea, String codiceAmbito,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException {
		final String methodName = "getModuli";
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN idModulo: "+idModuloQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN idVersioneModulo: "+idVersioneModuloQP);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN codiceModulo: "+codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN versioneModulo: "+versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN oggettoModulo: "+oggettoModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN descrizioneModulo: "+descrizioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN flagIsRiservato: "+flagIsRiservato);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN idTipoCodiceIstanza: "+idTipoCodiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN flagProtocolloIntegrato: "+flagProtocolloIntegrato);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN stato: "+stato);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN dataEntroIntDiPubblicazione: "+dataEntroIntDiPubblicazione);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN conPresenzaIstanzeUser: "+conPresenzaIstanzeUser);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN nomePortale: "+nomePortale);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN idEnte/codiceEnte: "+ idEnteQP + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN idArea/codiceArea: "+ idAreaQP + codiceArea);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN idAmbito/codiceAmbito: "+ idAmbitoQP + codiceAmbito);
				LOG.debug("[" + CLASS_NAME + "::getModuli] IN idVisibilitaAmbito: "+idVisibilitaAmbitoQP);
			}
			ModuliFilter filter = new ModuliFilter();
			Long idModulo = validaLong(idModuloQP);
			Long idVersioneModulo = validaLong(idVersioneModuloQP);
			Long idEnte = validaLong(idEnteQP);
			Long idArea = validaLong(idAreaQP);
			Integer idAmbito = validaInteger(idAmbitoQP);
			Integer idVisibilitaAmbito = validaInteger(idVisibilitaAmbitoQP);
			DecodificaStatoModulo dStato = validaDecodificaStatoModulo(stato);
			filter.setIdModulo(idModulo);
			filter.setIdVersioneModulo(idVersioneModulo);
			filter.setCodiceModulo(codiceModulo);
			filter.setVersioneModulo(versioneModulo);
			filter.setOggettoModulo(oggettoModulo);
			filter.setDescrizioneModulo(descrizioneModulo);
			filter.setFlagIsRiservato(flagIsRiservato);
			filter.setIdTipoCodiceIstanza(idTipoCodiceIstanza);
			filter.setFlagProtocolloIntegrato(flagProtocolloIntegrato);
			filter.setStatoModulo(dStato);
//			filter.setDataEntroIntDiPubblicazione(dataEntroIntDiPubblicazione);
			filter.setConPresenzaIstanzeUser(conPresenzaIstanzeUser);
			filter.setNomePortale(nomePortale);
			filter.setIdEnte(idEnte);
			filter.setCodiceEnte(codiceEnte);
			filter.setIdArea(idArea);
			filter.setCodiceArea(codiceArea);
			filter.setIdAmbito(idAmbito);
			filter.setCodiceAmbito(codiceAmbito);
			filter.setIdVisibilitaAmbito(idVisibilitaAmbito);
			List<Modulo> elenco = moduliService.getElencoModuli(filter);
			logInfoElencoModuliByFilter(methodName, filter, elenco);
			return Response.ok(elenco).build();
		} catch (ResourceNotFoundException nfe) {
			throw nfe;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getModuli] Errore servizio getModuli" + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::getModuli] Errore servizio getModuli");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore generico servizio getModuli", ex);
			throw new ServiceException("Errore generico servizio elenco moduli");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuli] END");
			}
		}
	}


	protected void logInfoElencoModuliByFilter(final String methodName, ModuliFilter filter, List<Modulo> elenco) {
		LOG.info("[" + CLASS_NAME + "::" + methodName + "] " + (elenco!=null?elenco.size():0) + " moduli trovati." + ((elenco==null||elenco.size()==0)?(" with filter="+filter):""));
	}

	
	public CompletionStage<Response> getModuliAsync(String idModuloQP, String idVersioneModuloQP, 
			String codiceModulo, String versioneModulo, String oggettoModulo, String descrizioneModulo, 
			String flagIsRiservato, Integer idTipoCodiceIstanza, String flagProtocolloIntegrato, String stato, 
			Boolean dataEntroIntDiPubblicazione, String conPresenzaIstanzeUser, String nomePortale,
			String idEnteQP, String idAreaQP, String idAmbitoQP, String idVisibilitaAmbitoQP,
			String codiceEnte, String codiceArea, String codiceAmbito,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException {
		final String methodName = "getModuliAsync";
		final CompletableFuture<Response> response = new CompletableFuture<>();
	      Thread t = new Thread()
	      {
	         @Override
	         public void run()
	         {
				try {
					if (LOG.isDebugEnabled()) {
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] BEGIN");
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN idModulo: "+idModuloQP);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN idVersioneModulo: "+idVersioneModuloQP);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN codiceModulo: "+codiceModulo);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN versioneModulo: "+versioneModulo);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN oggettoModulo: "+oggettoModulo);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN descrizioneModulo: "+descrizioneModulo);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN flagIsRiservato: "+flagIsRiservato);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN idTipoCodiceIstanza: "+idTipoCodiceIstanza);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN flagProtocolloIntegrato: "+flagProtocolloIntegrato);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN stato: "+stato);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN dataEntroIntDiPubblicazione: "+dataEntroIntDiPubblicazione);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN conPresenzaIstanzeUser: "+conPresenzaIstanzeUser);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN nomePortale: "+nomePortale);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN idEnte/codiceEnte: "+ idEnteQP + codiceEnte);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN idArea/codiceArea: "+ idAreaQP + codiceArea);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN idAmbito/codiceAmbito: "+ idAmbitoQP + codiceAmbito);
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] IN idVisibilitaAmbito: "+idVisibilitaAmbitoQP);
					}
					ModuliFilter filter = new ModuliFilter();
					Long idModulo = validaLong(idModuloQP);
					Long idVersioneModulo = validaLong(idVersioneModuloQP);
					Long idEnte = validaLong(idEnteQP);
					Long idArea = validaLong(idAreaQP);
					Integer idAmbito = validaInteger(idAmbitoQP);
					Integer idVisibilitaAmbito = validaInteger(idVisibilitaAmbitoQP);
					DecodificaStatoModulo dStato = validaDecodificaStatoModulo(stato);
					filter.setIdModulo(idModulo);
					filter.setIdVersioneModulo(idVersioneModulo);
					filter.setCodiceModulo(codiceModulo);
					filter.setVersioneModulo(versioneModulo);
					filter.setOggettoModulo(oggettoModulo);
					filter.setDescrizioneModulo(descrizioneModulo);
					filter.setFlagIsRiservato(flagIsRiservato);
					filter.setIdTipoCodiceIstanza(idTipoCodiceIstanza);
					filter.setFlagProtocolloIntegrato(flagProtocolloIntegrato);
					filter.setStatoModulo(dStato);
		//			filter.setDataEntroIntDiPubblicazione(dataEntroIntDiPubblicazione);
					filter.setConPresenzaIstanzeUser(conPresenzaIstanzeUser);
					filter.setNomePortale(nomePortale);
					filter.setIdEnte(idEnte);
					filter.setCodiceEnte(codiceEnte);
					filter.setIdArea(idArea);
					filter.setCodiceArea(codiceArea);
					filter.setIdAmbito(idAmbito);
					filter.setCodiceAmbito(codiceAmbito);
					filter.setIdVisibilitaAmbito(idVisibilitaAmbito);
					List<Modulo> elenco = moduliService.getElencoModuli(filter);
					logInfoElencoModuliByFilter(methodName, filter, elenco);
					Response jaxrs =  Response.ok(elenco).build();
					response.complete(jaxrs);
				} catch (ResourceNotFoundException nfe) {
					response.completeExceptionally(nfe);
				} catch (BusinessException be) {
					LOG.warn("[" + CLASS_NAME + "::getModuliAsync] Errore servizio getModuliAsync" + be.getMessage());
					response.completeExceptionally(new ServiceException(be));
				} catch (UnprocessableEntityException ue) {
					LOG.warn("[" + CLASS_NAME + "::getModuliAsync] Errore servizio getModuliAsync");
					response.completeExceptionally(ue);
				} catch (Exception ex) {
					LOG.error("[" + CLASS_NAME + "::getModuliAsync] Errore generico servizio getModuliAsync",ex);
					response.completeExceptionally(new ServiceException("Errore generico servizio elenco getModuliAsync"));
				} finally {
					if (LOG.isDebugEnabled()) {
						LOG.debug("[" + CLASS_NAME + "::getModuliAsync] END");
					}
				}
	         }

	      };
	      t.start();
	      return response;
	}
	
	@Override
	public Response getModuloById(Long idModuloPP, Long idVersioneModuloPP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Modulo modulo = moduliService.getModuloById(idModuloPP, idVersioneModuloPP, fields);
			return Response.ok(modulo).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] modulo non trovato " + idModuloPP + "/" + idVersioneModuloPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore servizio getModuloById " + idModuloPP + "/" + idVersioneModuloPP, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore generico servizio getModuloById " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio getModuloById");
		} 
	}


	@Override
    public Response getCampiModulo( Long idModuloPP, Long idVersioneModuloPP, String onlyFirstLevel, String type,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			List<CampoModulo> campi = moduliService.getCampiModulo(idModuloPP,idVersioneModuloPP,onlyFirstLevel,type);
			return Response.ok(campi).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getCampiModulo] modulo non trovato " + idModuloPP + "/" + idVersioneModuloPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getCampiModulo] Errore servizio getCampiModulo " + idModuloPP + "/" + idVersioneModuloPP, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCampiModulo] Errore generico servizio getCampiModulo " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio getCampiModulo");
		} 
	}
    
	@Override
    public Response getCampiDatiAzione( Long idDatiAzionePP, String onlyFirstLevel, String type,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			List<CampoModulo> campi = moduliService.getCampiDatiAzione(idDatiAzionePP,onlyFirstLevel,type);
			return Response.ok(campi).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getCampiDatiAzione] DatiAzione non trovato " + idDatiAzionePP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getCampiDatiAzione] Errore servizio getCampiDatiAzione " + idDatiAzionePP, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCampiDatiAzione] Errore generico servizio getCampiDatiAzione " + idDatiAzionePP, ex);
			throw new ServiceException("Errore generico servizio getCampiDatiAzione");
		} 
	}


	@Override
	public Response getModuloByCd(String codicedModuloPP, String versionePP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Modulo modulo = moduliService.getModuloByCodice(codicedModuloPP, versionePP);
			return Response.ok(modulo).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCd] modulo non trovato" + codicedModuloPP + "/" + versionePP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCd] Errore servizio getModuloByCd" + codicedModuloPP + "/" + versionePP, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCd] Errore generico servizio getModuloByCd" + codicedModuloPP + "/" + versionePP, ex);
			throw new ServiceException("Errore generico servizio getModuloByCd");
		} 
	}
	

	@Override
	public Response getModuloPubblicatoByCodice(String codicedModuloPP, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Modulo modulo = moduliService.getModuloPubblicatoByCodice(codicedModuloPP);
			return Response.ok(modulo).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] modulo non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] Errore servizio getModuloPubblicatoByCodice", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] Errore generico servizio getModuloPubblicatoByCodice", ex);
			throw new ServiceException("Errore generico servizio getModuloPubblicatoByCodice");
		} 
	}

	@Override
	public Response getPrtParametri( String idModuloPP, String idVersioneModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPrtParametri] IN idModuloPP=" + idModuloPP + " idVersioneModuloPP=" + idVersioneModuloPP);
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			List<ProtocolloParametro> ris = protocolloService.getProtocolloParametri(idModulo);
			return Response.ok(ris).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPrtParametri] modulo non trovato " + idModuloPP + "/" + idVersioneModuloPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPrtParametri] Errore servizio getPrtParametri" + idModuloPP + "/" + idVersioneModuloPP + "  " + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::getPrtParametri] Errore servizio getPrtParametri");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPrtParametri] Errore generico servizio getPrtParametri " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio getPrtParametri");
		} 
	}
	
	@Override
	public Response getMyDocsParametri( String idModuloPP, String idVersioneModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getMyDocsParametri] IN idModuloPP=" + idModuloPP + " idVersioneModuloPP=" + idVersioneModuloPP);
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			List<ProtocolloParametro> ris = myDocsService.getMyDocsParametri(idModulo);
			return Response.ok(ris).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getMyDocsParametri] modulo non trovato " + idModuloPP + "/" + idVersioneModuloPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getMyDocsParametri] Errore servizio getMyDocsParametri" + idModuloPP + "/" + idVersioneModuloPP + "  " + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::getMyDocsParametri] Errore servizio getMyDocsParametri");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getMyDocsParametri] Errore generico servizio getMyDocsParametri " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio getMyDocsParametri");
		} 
	}
	

	//called by BOBL
	@Override
	public Response postModuloClass(String idModuloPP, int idTipologia, InputStream file,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::postModuloClass] IN idModuloPP=" + idModuloPP + " idTipologia=" + idTipologia);
			Long idModulo = validaLongRequired(idModuloPP);
			ModuloClass ris = moduloClassService.uploadModuloClass(idModulo,idTipologia,file.readAllBytes());
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::postModuloClass] Errore servizio postModuloClass" + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::postModuloClass] Errore servizio postModuloClass");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postModuloClass] Errore generico servizio postModuloClass", ex);
			throw new ServiceException("Errore generico servizio postModuloClass");
		} 
	}
	
	@Override
	public Response deleteModuloClass(String idModuloPP, int idTipologia,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteModuloClass] IN idModuloPP=" + idModuloPP + " idTipologia=" + idTipologia);
			Long idModulo = validaLongRequired(idModuloPP);
			moduloClassService.delete(idModulo,idTipologia);
			return Response.ok().build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::deleteModuloClass] Errore servizio deleteModuloClass" + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::deleteModuloClass] Errore servizio deleteModuloClass");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::deleteModuloClass] Errore generico servizio deleteModuloClass", ex);
			throw new ServiceException("Errore generico servizio deleteModuloClass");
		} 
	}
	
	@Override
	public Response getPrintMapperName100(String codiceModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPrintMapperName100] IN codiceModuloPP: " + codiceModuloPP);
			String codiceModulo = validaStringRequired(codiceModuloPP);
			
			String ris = printIstanzeService.getPrintMapperName(codiceModulo, "1.0.0");
			LOG.debug("[" + CLASS_NAME + "::getPrintMapperName100] END");
	        return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPrintMapperName100] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::getPrintMapperName100] UnprocessableEntityException ");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPrintMapperName100] Exception", ex);
			throw new ServiceException("Errore generico servizio print mapper name 1.0.0");
		} 
	}
	
	@Override
	public Response getPrintMapperName(String codiceModuloPP, String versioneModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPrintMapperName] IN codiceModuloPP: " + codiceModuloPP);
			LOG.debug("[" + CLASS_NAME + "::getPrintMapperName] IN versioneModuloPP: " + versioneModuloPP);
			String codiceModulo = validaStringRequired(codiceModuloPP);
			String versioneModulo = validaStringRequired(versioneModuloPP);
			
			String ris = printIstanzeService.getPrintMapperName(codiceModulo, versioneModulo);
			LOG.debug("[" + CLASS_NAME + "::getPrintMapperName] END");
	        return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPrintMapperName] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::getPrintMapperName] UnprocessableEntityException ");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPrintMapperName] Errore generico servizio getPrintMapperName", ex);
			throw new ServiceException("Errore generico servizio print mapper name");
		}
	}
	
	//
	// PROTOCOLLO
	//
	@Override
	public Response getProtocolloManagerName(String codiceModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getProtocolloManagerName] IN codiceModuloPP: " + codiceModuloPP);
			String codiceModulo = validaStringRequired(codiceModuloPP);
			
			String ris = protocolloService.getProtocolloManagerName(codiceModulo);
			LOG.debug("[" + CLASS_NAME + "::getProtocolloManagerName] END");
	        return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getProtocolloManagerName] Errore servizio getProtocolloManagerName" + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::getProtocolloManagerName] Errore servizio getProtocolloManagerName");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getProtocolloManagerName] Errore generico servizio getProtocolloManagerName", ex);
			throw new ServiceException("Errore generico servizio protocolla istanza");
		} 
	}
	
	//
	// EPAY
	//
	@Override
	public Response getEpayManagerName(String codiceModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getEpayManagerName] IN codiceModuloPP: " + codiceModuloPP);
			String codiceModulo = validaStringRequired(codiceModuloPP);
			
			String ris = epayService.getEpayManagerName(codiceModulo);
			LOG.debug("[" + CLASS_NAME + "::getEpayManagerName] END");
	        return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getEpayManagerName] Errore servizio getEpayManagerName" + be.getMessage());
			throw new ServiceException(be);
		} catch (UnprocessableEntityException ue) {
			LOG.warn("[" + CLASS_NAME + "::getEpayManagerName] Errore servizio getEpayManagerName");
			throw ue;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getEpayManagerName] Errore generico servizio getProtocolloManagerName", ex);
			throw new ServiceException("Errore generico servizio epay manager name");
		} 
	}
	
	@Override	
	public Response validaAttributiModulo(String idModuloPP, String categoriaAttributiPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::validaAttributi] IN idModuloPP: " + idModuloPP + " categoriaAttributiPP: " + categoriaAttributiPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			String categoriaAttributi = validaStringRequired(categoriaAttributiPP);
			//
			ReportVerificaAttributiModulo ris = moduliService.validaAttributiModulo(idModulo, categoriaAttributi);
			LOG.info("[" + CLASS_NAME + "::validaAttributiModulo] IN idModuloPP: " + idModuloPP + " OUT: " + ris);
			return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::validaAttributiModulo] ItemNotFoundBusinessException idModuloPP: " + idModuloPP + " categoriaAttributiPP: " + categoriaAttributiPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::validaAttributiModulo] BusinessException idModuloPP: " + idModuloPP + " categoriaAttributiPP: " + categoriaAttributiPP, e);
			throw new ServiceException("Errore servizio hasPrtBo");
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::validaAttributiModulo] UnprocessableEntityException idModuloPP: " + idModuloPP + " categoriaAttributiPP: " + categoriaAttributiPP);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::validaAttributiModulo] Errore generico servizio validaAttributiModulo idModuloPP: " + idModuloPP + " categoriaAttributiPP: " + categoriaAttributiPP,ex);
			throw new ServiceException("Errore generico servizio validaAttributiModulo");
		} 
	}
}
