/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.AllegatiSummary;
import it.csi.moon.commons.dto.CreaIuvResponse;
import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.dto.EPayPagoPAParams;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.LogEmail;
import it.csi.moon.commons.dto.LogPraticaCosmo;
import it.csi.moon.commons.dto.ResponseOperazioneMassiva;
import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.commons.entity.IstanzeSorterBuilder;
import it.csi.moon.moonsrv.business.be.IstanzeApi;
import it.csi.moon.moonsrv.business.service.IndexService;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.LogEmailService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.ProtocolloService;
import it.csi.moon.moonsrv.business.service.TicketCrmService;
import it.csi.moon.moonsrv.business.service.WorkflowService;
import it.csi.moon.moonsrv.business.service.epay.EpayService;
import it.csi.moon.moonsrv.business.service.wf.CosmoService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.stardas.cxfclient.MetadatiType;

@Component
public class IstanzeApiImpl extends MoonBaseApiImpl implements IstanzeApi {
	
	private static final String CLASS_NAME = "IstanzeApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String IDISTANZA = "idIstanza";
	private static final String IDTAG = "idTag";

	@Autowired
	IstanzeService istanzeService;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	ProtocolloService protocolloService;
	@Autowired
	CosmoService cosmoService;
	@Autowired
	TicketCrmService ticketCrmService;
	@Autowired
	LogEmailService logEmailService;
	@Autowired
	EpayService epayService;
	@Autowired
	IndexService indexService;
	@Autowired
	WorkflowService workflowService;
  
	public Response getIstanze(String cfDichiarante, Integer stato, Integer importanza, Long idModulo, String sort,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanze] IN cfDichiarante: "+cfDichiarante);
			LOG.debug("[" + CLASS_NAME + "::getIstanze] IN stato: "+stato);
			LOG.debug("[" + CLASS_NAME + "::getIstanze] IN importanza: "+importanza);
			LOG.debug("[" + CLASS_NAME + "::getIstanze] IN idModulo: "+idModulo);
			LOG.debug("[" + CLASS_NAME + "::getIstanze] IN sort: "+sort);
			IstanzeFilter filter = new IstanzeFilter();
			if (cfDichiarante!=null) {
				filter.setCodiceFiscaleDichiarante(cfDichiarante);
			}
			if (stato!=null) {
				filter.setStatiIstanza(Arrays.asList(stato));
			}
			filter.setIdModulo(idModulo);
			filter.setImportanza(importanza);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			List<Istanza> elenco = istanzeService.getElencoIstanze(filter, optSorter);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze",e);
			throw new ServiceException("Errore servizio elenco moduli");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio getIstanze",ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		} 
	}

	public Response getIstanzaById(Long idIstanza, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaById] IN idIstanza: "+idIstanza);
			LOG.debug("[" + CLASS_NAME + "::getIstanzaById] IN fields: "+fields);
			Istanza istanza = istanzeService.getIstanzaById(idIstanza, fields);
			return Response.ok(istanza).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaById] istanza non trovata idIstanza=" + idIstanza);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] Errore servizio getIstanzaById",e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] Errore generico servizio getIstanzaById", ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		} 
	}


	public Response getIstanzaDataById(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaDataById] IN idIstanza: "+idIstanza);
			String data = istanzeService.getIstanzaDataById(idIstanza);
			return Response.ok(data).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaDataById] istanza non trovata idIstanza=" + idIstanza);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaDataById] Errore servizio getIstanzaDataById",e);
			throw new ServiceException("Errore servizio data istanza");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaDataById] Errore generico servizio getIstanzaDataById",ex);
			throw new ServiceException("Errore generico data istanza");
		} 
	}
	
	//
	// INIT
	//
	public Response getInitIstanza( IstanzaInitParams initParams, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN initParams body: "+initParams);
			Istanza istanza = istanzeService.getInitIstanza(initParams);
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] OUT istanza : "+istanza);
			return Response.ok(istanza).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] ItemNotFoundBusinessException - Errore servizio getInitIstanzaByIdModulo ", notFoundEx);
			throw new ResourceNotFoundException("MOONSRV-002");
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] BusinessException - Errore servizio getInitIstanzaByIdModulo ", be);
			throw new ServiceException(be);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] Exception - Errore generico servizio getInitIstanzaByIdModulo ", e);
			throw new ServiceException("Errore generico servizio inizializzazione istanza");
		} 
	}
	

	//
	// Genera PDF
	//
	@Override
	public Response generaSalvaPdf(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::generaSalvaPdf] IN idIstanza: " + idIstanza);
			printIstanzeService.generaSalvaPdf(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::generaSalvaPdf] END");
	        return Response.ok("OK").build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::generaSalvaPdf] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::generaSalvaPdf] Errore servizio", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::generaSalvaPdf] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio generaSalvaPdf istanza");
		}
	}
	
	//
	// PDF
	//
	public Response getPdfById(Long idIstanza, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPdfById] IN idIstanza: " + idIstanza);
			byte[] bytes = printIstanzeService.getPdfById(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::getPdfById] bytes.length=" + bytes.length);
	        return Response.ok(bytes).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPdfById] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getPdfById] Errore servizio", e);
			throw new ServiceException("Errore servizio getPdfById");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPdfById] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getPdfById istanza");
		} 
	}
	
	public Response getNotificaById(Long idIstanza, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getNotificaById] IN idIstanza: " + idIstanza);
			byte[] bytes = printIstanzeService.getNotificaById(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::getNotificaById] bytes.length=" + bytes.length);
	        return Response.ok(bytes).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getNotificaById] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getNotificaById] Errore servizio", e);
			throw new ServiceException("Errore servizio getNotificaById");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getNotificaById] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getNotificaById istanza");
		} 
	}
	
	public Response getDocumentoNotificaById(Long idIstanza,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDocumentoNotificaById] IN idIstanza: " + idIstanza);
			Documento documento = printIstanzeService.getFirstDocumentoNotificaByIdIstanza(idIstanza);
			return Response.ok(documento).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoNotificaById] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoNotificaById] Errore servizio", e);
			throw new ServiceException("Errore servizio getNotificaById");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoNotificaById] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getNotificaById istanza");
		}
	}
	
	public Response getDocumentoByFormioNameFile(String formioNameFile, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] IN formioNameFile: " + formioNameFile);
			byte[] bytes = printIstanzeService.getDocumentoByFormioNameFile(formioNameFile);					
			return Response.ok(bytes).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] documento non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] Errore servizio", e);
			throw new ServiceException("Errore servizio getDocumentoByFormioNameFile");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getDocumentoByFormioNameFile");
		}
	}
	
	
	public Response getDocumentoByIdFile(Long idFile, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDocumentoByIdFile] IN idFile: " + idFile+"");
			byte[] bytes = printIstanzeService.getDocumentoByIdFile(idFile);					
			return Response.ok(bytes).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] documento non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] Errore servizio", e);
			throw new ServiceException("Errore servizio getDocumentoByIdFile");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getDocumentoByIdFile");
		}
	}
			
	public Response generaPdfById(Long idIstanza, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				LOG.debug("[" + CLASS_NAME + "::generaPdfById] IN idIstanza: " + idIstanza);
				byte[] bytes = printIstanzeService.printPdf(idIstanza);
				LOG.debug("[" + CLASS_NAME + "::generaPdfById] bytes.length=" + bytes.length);
		        return Response.ok(bytes).build();
			} catch(ItemNotFoundBusinessException notFoundEx) {
				LOG.error("[" + CLASS_NAME + "::generaPdfById] istanza non trovata", notFoundEx);
				throw new ResourceNotFoundException();
			} catch (BusinessException e) {
				LOG.error("[" + CLASS_NAME + "::generaPdfById] Errore servizio", e);
				throw new ServiceException("Errore servizio generaPdfById");
			} catch (Exception ex) {
				LOG.error("[" + CLASS_NAME + "::generaPdfById] Errore generico servizio", ex);
				throw new ServiceException("Errore generico servizio generaPdfById");
			} 
		}
	//x test
	public Response generaPdfClassLoaderById(Long idIstanza, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				LOG.debug("[" + CLASS_NAME + "::generaPdfClassLoaderById] IN idIstanza: " + idIstanza);
				byte[] bytes = printIstanzeService.printPdfClassLoader(idIstanza);
				LOG.debug("[" + CLASS_NAME + "::generaPdfClassLoaderById] bytes.length=" + bytes.length);
		        return Response.ok(bytes).build();
			} catch(ItemNotFoundBusinessException notFoundEx) {
				LOG.error("[" + CLASS_NAME + "::generaPdfClassLoaderById] istanza non trovata", notFoundEx);
				throw new ResourceNotFoundException();
			} catch (BusinessException e) {
				LOG.error("[" + CLASS_NAME + "::generaPdfClassLoaderById] Errore servizio", e);
				throw new ServiceException("Errore servizio generaPdfClassLoaderById");
			} catch (Exception ex) {
				LOG.error("[" + CLASS_NAME + "::generaPdfClassLoaderById] Errore generico servizio", ex);
				throw new ServiceException("Errore generico servizio generaPdfClassLoaderById");
			} 
		}

	public Response generaPdfFromBody(Istanza body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::generaPdfFromBody] IN Istanza body: "+body);
			byte[] bytes = printIstanzeService.printPdf(body);
			LOG.debug("[" + CLASS_NAME + "::generaPdfFromBody] bytes.length=" + bytes.length);
	        return Response.ok(bytes)
	        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
	        		.header("Pragma", "no-cache")
	        		.header("Expires", "0")
	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=response.pdf")
	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
	        		.build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::generaPdfFromBody] Errore servizio",e);
			throw new ServiceException("Errore servizio print istanza");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::generaPdfFromBody] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio generaPdfFromBody istanza");
		} 
	}

	
	//
	// ONLY FOR TEST
    public Response getMoonprintDocumentJson(Long idIstanza, 
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getMoonprintDocumentJson] IN idIstanza: " + idIstanza);
			MoonprintDocument moonprintDocument = printIstanzeService.remap(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::getMoonprintDocumentJson] moonprintDocument.getDocument()=" + moonprintDocument.getDocument());
	        return Response.ok(moonprintDocument.getDocument()).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getMoonprintDocumentJson] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getMoonprintDocumentJson] Errore servizio getMoonprintDocumentJson", e);
			throw new ServiceException("Errore servizio getMoonprintDocumentJson");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getMoonprintDocumentJson] Errore generico servizio getMoonprintDocumentJson", ex);
			throw new ServiceException("Errore generico servizio getMoonprintDocumentJson");
		} 
    }

    
    //
    // PROTOCOLLO
    //
	@Override
	public Response postProtocolla(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::postProtocolla] IN idIstanza: " + idIstanza);
			protocolloService.protocollaIstanza(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::postProtocolla] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postProtocolla] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postProtocolla] Errore servizio postProtocolla", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postProtocolla] Errore generico servizio postProtocolla", ex);
			throw new ServiceException("Errore generico servizio protocolla istanza");
		} 
	}

	@Override
	public Response retrieveMetadatiIstanza( Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiIstanza] IN idIstanza: " + idIstanza);
			MetadatiType ris = protocolloService.retrieveMetadatiIstanza(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiIstanza] END");
	        return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiIstanza] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiIstanza] Errore servizio retrieveMetadatiIstanza", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiIstanza] Errore generico servizio retrieveMetadatiIstanza", ex);
			throw new ServiceException("Errore generico servizio retrieveMetadatiIstanza");
		} 
	}

	@Override
	public Response retrieveMetadatiAllegato( Long idIstanza, Long idAllegato,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiAllegato] IN idIstanza: " + idIstanza);
			LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiAllegato] IN idAllegato: " + idAllegato);
			MetadatiType ris = protocolloService.retrieveMetadatiAllegato(idIstanza, idAllegato);
			LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiAllegato] END");
	        return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiAllegato] istanza / allegato non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiAllegato] Errore servizio retrieveMetadatiAllegato", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiAllegato] Errore generico servizio retrieveMetadatiAllegato", ex);
			throw new ServiceException("Errore generico servizio retrieveMetadatiAllegato");
		} 
	}
	
	@Override
	public Response getProtocollaMassivo(Long idTag,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getProtocollaMassivo] IN idTag: " + idTag);
			}
			required(idTag, IDTAG);
			ResponseOperazioneMassiva ris = protocolloService.protocollaMassivo(idTag);
			LOG.debug("[" + CLASS_NAME + "::getProtocollaMassivo] END");
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getProtocollaMassivo] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProtocollaMassivo] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getProtocollaMassivo] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getProtocollaMassivo");
		}
	}
	
	
	//
	// ARCHIVIA
	//
//	@Override
//	public Response getArchiviaDoquiMassivo(Long idTag,
//		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//		try {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("[" + CLASS_NAME + "::getArchiviaDoquiMassivo] IN idTag: " + idTag);
//			}
//			required(idTag, IDTAG);
//			ResponseOperazioneMassiva ris = archiviaDoquiService.archiviaDoquiMassivo(idTag);
//			LOG.debug("[" + CLASS_NAME + "::getArchiviaDoquiMassivo] END");
//	        return Response.ok(ris).build();
//		} catch (UnprocessableEntityException uee) {
//			LOG.error("[" + CLASS_NAME + "::getArchiviaDoquiMassivo] Errore UnprocessableEntityException",uee);
//			throw uee;
//		} catch (BusinessException be) {
//			LOG.error("[" + CLASS_NAME + "::getArchiviaDoquiMassivo] BusinessException");
//			throw new ServiceException(be);
//		} catch (Exception ex) {
//			LOG.error("[" + CLASS_NAME + "::getArchiviaDoquiMassivo] Errore generico servizio", ex);
//			throw new ServiceException("Errore generico servizio getArchiviaDoquiMassivo");
//		}
//	}
	
	@Override
	public Response generaHashUnivocitaMassivo(Long idTag,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] IN idTag: " + idTag);
			}
			required(idTag, IDTAG);
			ResponseOperazioneMassiva ris = istanzeService.generaHashUnivocitaMassivo(idTag);
			LOG.debug("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] END");
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio generaHashUnivocitaMassivo");
		}
	}	


	@Override
	public Response postCreaPraticaEdAvviaProcessoCosmo(Long idIstanza, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] IN idIstanza: " + idIstanza);
			cosmoService.creaPraticaEdAvviaProcesso(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] Errore generico servizio postCreaPraticaEdAvviaProcessoCosmo", ex);
			throw new ServiceException("Errore generico servizio CreaPraticaEdAvviaProcessoCosmo");
		} 
	}


    //
    // Email
    //
	@Override
	public Response postRinviaEmail(Long idIstanza, String dest,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postRinviaEmail] IN idIstanza: " + idIstanza);
				LOG.debug("[" + CLASS_NAME + "::postRinviaEmail] IN dest: " + dest);
			}
			istanzeService.rinviaEmail(idIstanza, dest);
			LOG.debug("[" + CLASS_NAME + "::postRinviaEmail] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postRinviaEmail] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postRinviaEmail] Errore servizio postRinviaEmail", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postRinviaEmail] Errore generico servizio postRinviaEmail", ex);
			throw new ServiceException("Errore generico servizio postRinviaEmail");
		} 
	}

	@Override
	public Response postRinviaEmails(Long idTag, String dest, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postRinviaEmails] IN idTag: " + idTag);
				LOG.debug("[" + CLASS_NAME + "::postRinviaEmails] IN dest: " + dest);
			}
			required(idTag, IDTAG);
			istanzeService.rinviaEmails(idTag, dest);
			LOG.debug("[" + CLASS_NAME + "::postRinviaEmails] END");
	        return Response.ok().build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::postRinviaEmails] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postRinviaEmails] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postRinviaEmails] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio postRinviaEmails");
		} 
	}
	
	@Override
    public Response postEstraiDichiaranteUpdate(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postEstraiDichiaranteUpdate] IN idIstanza: " + idIstanza);
			}
			istanzeService.estraiDichiaranteUpdate(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::postEstraiDichiaranteUpdate] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postEstraiDichiaranteUpdate] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postEstraiDichiaranteUpdate] Errore servizio", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postEstraiDichiaranteUpdate] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio postEstraiDichiaranteUpdate");
		} 
	}
	
	public Response generaPdfIntegrazione( Long idIstanza, Long idStoricoWorkflow,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::generaPdfIntegrazione] IN idIstanza: " + idIstanza + "   idStoricoWorkflow: " + idStoricoWorkflow);
			}
			byte[] bytes = printIstanzeService.printPdfIntegrazione(idIstanza, idStoricoWorkflow);
			LOG.debug("[" + CLASS_NAME + "::generaPdfIntegrazione] bytes.length=" + bytes.length);
	        return Response.ok(bytes).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::generaPdfIntegrazione] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::generaPdfIntegrazione] Errore servizio", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::generaPdfIntegrazione] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio generaPdfIntegrazione");
		} 
	}

	public Response postProtocollaIntegrazione( Long idIstanza, Long idStoricoWorkflow,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postProtocollaIntegrazione] IN idIstanza: " + idIstanza + "   idStoricoWorkflow: " + idStoricoWorkflow);
			}
			protocolloService.protocollaIntegrazione(idIstanza, idStoricoWorkflow);
			LOG.debug("[" + CLASS_NAME + "::postProtocollaIntegrazione] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postProtocollaIntegrazione] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postProtocollaIntegrazione] Errore servizio", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postProtocollaIntegrazione] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio postProtocollaIntegrazione");
		} 
	}


    //
    // Ticket CRM
    //
	@Override
	public Response postCreaTicketCrmIstanza(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] IN idIstanza: " + idIstanza);
			ticketCrmService.creaTicketCrmIstanza(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] Errore servizio postCreaTicketCrmIstanza", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] Errore generico servizio postCreaTicketCrmIstanza", ex);
			throw new ServiceException("Errore generico servizio postCreaTicketCrmIstanza istanza");
		} 
	}


	@Override
	public Response postCreaTicketCrmIstanzaMassivo(Long idTag,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::postCreaTicketCrmIstanzaMassivo] IN idTag: " + idTag);
			}
			required(idTag, IDTAG);
			ResponseOperazioneMassiva ris = ticketCrmService.creaTicketCrmIstanzaMassivo(idTag);
			LOG.debug("[" + CLASS_NAME + "::postCreaTicketCrmIstanzaMassivo] END");
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::postCreaTicketCrmIstanzaMassivo] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postCreaTicketCrmIstanzaMassivo] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::postCreaTicketCrmIstanzaMassivo] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio postCreaTicketCrmIstanzaMassivo");
		}
	}

	
	@Override
	public Response getLogEmail(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getLogEmail] IN idIstanza: " + idIstanza);
			}
			required(idIstanza, IDISTANZA);
			List<LogEmail> ris = logEmailService.getElencoLogEmailByIdIstanza(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::getLogEmail] END");
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getLogEmail] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getLogEmail] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getLogEmail] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getLogEmail");
		}
	}
	
	@Override
	public Response getLogPraticaCosmo(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getLogPraticaCosmo] IN idIstanza: " + idIstanza);
			}
			required(idIstanza, IDISTANZA);
			List<LogPraticaCosmo> ris = cosmoService.getElencoLogPraticaByIdIstanza(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::getLogPraticaCosmo] END");
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getLogPraticaCosmo] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getLogPraticaCosmo] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getLogPraticaCosmo] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getLogPraticaCosmo");
		}
	}


    //
    // EPAY
    //
	@Override
	public Response creaIUV(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.info("[" + CLASS_NAME + "::creaIUV] IN idIstanza: " + idIstanza);
			required(idIstanza, IDISTANZA);
			CreaIuvResponse ris = epayService.creaIUV(idIstanza);
			LOG.info("[" + CLASS_NAME + "::creaIUV] END ris = " + ris);
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::creaIUV] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::creaIUV] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::creaIUV] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio creaIUV");
		}
	}
	
	@Override
	public Response pagoPA(Long idIstanza, EPayPagoPAParams pagoPAParams,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.info("[" + CLASS_NAME + "::pagoPA] IN idIstanza: " + idIstanza);
			required(idIstanza, IDISTANZA);
			CreaIuvResponse ris = epayService.pagoPA(idIstanza, pagoPAParams);
			LOG.info("[" + CLASS_NAME + "::pagoPA] END ris = " + ris);
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::pagoPA] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::pagoPA] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::pagoPA] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio pagoPA");
		}
	}
	
	@Override
	public Response annullaIUV(Long idIstanza, String iuv,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.info("[" + CLASS_NAME + "::annullaIUV] IN idIstanza: " + idIstanza);
			required(idIstanza, IDISTANZA);
			CreaIuvResponse ris = epayService.annullaIUV(idIstanza, iuv);
			LOG.info("[" + CLASS_NAME + "::annullaIUV] END ris = " + ris);
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::annullaIUV] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::annullaIUV] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::annullaIUV] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio annullaIUV");
		}
	}
	
	@Override
	public Response inviaRispostaIntegrazioneCosmo(Long idIstanza, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.info("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] IN idIstanza: " + idIstanza);
			required(idIstanza, IDISTANZA);
			
			String res = cosmoService.inviaRispostaIntegrazione(idIstanza);
			
			LOG.debug("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] END result = " + res);
	        return Response.ok(res).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] BusinessException");
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio inviaRispostaIntegrazioneCosmo");
		}
	}

	@Override
	public Response downloadAllFileIstanzeByIdModulo(Long idModulo, Date dataDal, Date dataAl,
			SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.info("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] BEGIN");
			
			String res = istanzeService.downloadAllFileIstanzeByIdModulo(idModulo, dataDal, dataAl);
			
			LOG.debug("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] END res: " + res);
	        return Response.ok(res).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] Errore servizio download archivio ",e);
			throw new ServiceException("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] ");
		} catch (IOException io) {
			LOG.error("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] Errore IOException"+ io);
			throw new ServiceException("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] Errore IOException");
		}
	}
	
	@Override
	public Response downloadRepositoryFileByIdModulo(Long idModulo, Date dataDal, Date dataAl,
			SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.info("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] BEGIN");
			
			String res = istanzeService.downloadRepositoryFileByIdModulo(idModulo, dataDal, dataAl);
			
			LOG.debug("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] END res: " + res);
	        return Response.ok(res).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] Errore servizio download archivio ",e);
			throw new ServiceException("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] ");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] Errore Exception"+ e);
			throw new ServiceException("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] Errore Exception");
		}
	}
	
	@Override
	public Response downloadRepositoryFileByIdIstanza(Long idIstanza,
			SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.info("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] BEGIN");
			
			String res = istanzeService.downloadRepositoryFileByIdIstanza(idIstanza);
			
			LOG.debug("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] END res: " + res);
	        return Response.ok(res).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] Errore servizio download archivio ",e);
			throw new ServiceException("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] ");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] Errore Exception"+ e);
			throw new ServiceException("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] Errore Exception");
		}
	}

	@Override
	public Response salvaIstanzaIndexById(Long idIstanza, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::salvaIstanzaIndexById] IN idIstanza: " + idIstanza);
			String ris = indexService.salvaIstanzaIndexById(idIstanza,null);
			return Response.ok(ris).build();
		} catch (BusinessException e) {
			throw new ServiceException("[" + CLASS_NAME + "::salvaIstanzaIndexById] Errore BusinessException");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::salvaIstanzaIndexById] Errore "+ e);
			throw new ServiceException("[" + CLASS_NAME + "::salvaIstanzaIndexById] Errore Exception");
		}
	}

	// FOR TEST
	@Override
	public Response compieAzioneAutomatica(Long idIstanza, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::compieAzioneAutomatica] IN idIstanza: " + idIstanza);
			workflowService.compieAzioneAutomaticaIfPresente(idIstanza);
			return Response.ok().build();
		} catch (BusinessException e) {
			throw new ServiceException("[" + CLASS_NAME + "::compieAzioneAutomatica] Errore BusinessException");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::compieAzioneAutomatica] Errore "+ e);
			throw new ServiceException("[" + CLASS_NAME + "::compieAzioneAutomatica] Errore Exception");
		}
	}
	
	@Override
	public Response getAllegatiSummary(Long idIstanza, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getAllegatiSummary] IN idIstanza: " + idIstanza);
			AllegatiSummary ris = istanzeService.getAllegatiSummary(idIstanza);
			return Response.ok(ris).build();
		} catch (BusinessException e) {
			throw new ServiceException("[" + CLASS_NAME + "::getAllegatiSummary] BusinessException");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getAllegatiSummary] Errore "+ e);
			throw new ServiceException("[" + CLASS_NAME + "::getAllegatiSummary] Exception");
		}
	}
	
}
