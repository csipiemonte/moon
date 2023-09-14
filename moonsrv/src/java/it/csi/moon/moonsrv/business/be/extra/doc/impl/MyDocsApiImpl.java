/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.doc.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.apimint.mydocs.be.v1.dto.AmbitoResponse;
import it.csi.apimint.mydocs.be.v1.dto.DocumentiResponse;
import it.csi.apimint.mydocs.be.v1.dto.FiltroDocumento;
import it.csi.apimint.mydocs.be.v1.dto.TipologiaResponse;
import it.csi.moon.commons.entity.MyDocsRichiestaEntity;
import it.csi.moon.moonsrv.business.be.extra.doc.MyDocsApi;
import it.csi.moon.moonsrv.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonsrv.business.service.doc.MyDocsService;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class MyDocsApiImpl extends MoonBaseApiImpl implements MyDocsApi {
	
	private static final String CLASS_NAME = "MyDocsApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	private MyDocsService myDocsService = null;
	
	@Autowired
	private RepositoryFileDAO repositoryFileDAO = null;
	

    //
	// PING
	@Override
	public Response ping( 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			String ris = myDocsService.ping();
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::ping] Errore generico servizio ping", ex);
			throw new ServiceException("Errore generico servizio elenco ping");
		} 
	}

	//
	// AMBITI
	//
	@Override
	public Response listAmbitiByIdEnte( Long idEnte, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<AmbitoResponse> ris = myDocsService.listAmbitiByIdEnte(idEnte);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::listAmbitiByIdEnte] Errore generico servizio listAmbitiByIdEnte", ex);
			throw new ServiceException("Errore generico servizio elenco listAmbitiByIdEnte");
		} 
    }
	@Override
	public Response listAmbitiByCodiceEnte( String codiceEnte, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<AmbitoResponse> ris = myDocsService.listAmbitiByCodiceEnte(codiceEnte);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::listAmbitiByCodiceEnte] Errore generico servizio listAmbitiByCodiceEnte", ex);
			throw new ServiceException("Errore generico servizio elenco listAmbitiByCodiceEnte");
		} 
    }
	@Override
	public Response listAmbitiByCodiceIpaEnte( String codiceIpaEnte, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<AmbitoResponse> ris = myDocsService.listAmbitiByCodiceIpaEnte(codiceIpaEnte);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::listAmbitiByCodiceIpaEnte] Errore generico servizio listAmbitiByCodiceIpaEnte", ex);
			throw new ServiceException("Errore generico servizio elenco listAmbitiByCodiceIpaEnte");
		} 
    }
	@Override
	public Response getAmbitoById( Long idEnte, Long idAmbito,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			AmbitoResponse ris = myDocsService.getAmbitoById(idEnte, idAmbito);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAmbitoById] Errore generico servizio getAmbitoById", ex);
			throw new ServiceException("Errore generico servizio elenco getAmbitoById");
		} 
    }

	//
	// TIPOLOGIE
	//    
	@Override
	public Response listTipologieByIdEnte( Long idEnte, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<TipologiaResponse> ris = myDocsService.listTipologieByIdEnte(idEnte);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::listTipologieByIdEnte] Errore generico servizio listTipologieByIdEnte", ex);
			throw new ServiceException("Errore generico servizio elenco listTipologieByIdEnte");
		} 
    }
	@Override
	public Response listTipologieByCodiceEnte( String codiceEnte, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<TipologiaResponse> ris = myDocsService.listTipologieByCodiceEnte(codiceEnte);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::listTipologieByCodiceEnte] Errore generico servizio listTipologieByCodiceEnte", ex);
			throw new ServiceException("Errore generico servizio elenco listTipologieByCodiceEnte");
		} 
    }
	@Override
	public Response listTipologieByCodiceIpaEnte( String codiceIpaEnte, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<TipologiaResponse> ris = myDocsService.listTipologieByCodiceIpaEnte(codiceIpaEnte);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::listTipologieByCodiceIpaEnte] Errore generico servizio listTipologieByCodiceIpaEnte", ex);
			throw new ServiceException("Errore generico servizio elenco listTipologieByCodiceIpaEnte");
		} 
    }
	@Override
	public Response getTipologiaById( Long idEnte, Long idTipologia,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			TipologiaResponse ris = myDocsService.getTipologiaById(idEnte, idTipologia);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getTipologiaById] Errore generico servizio getTipologiaById", ex);
			throw new ServiceException("Errore generico servizio elenco getTipologiaById");
		} 
    }
	
	//
	// DOCUMENTI
    // generici
	@Override
    public Response getDocumenti( Long idEnte, FiltroDocumento filtro,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			DocumentiResponse ris = myDocsService.findDocumenti(idEnte, filtro);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getDocumenti] Errore generico servizio getDocumenti", ex);
			throw new ServiceException("Errore generico servizio elenco getDocumenti");
		}
    }
	//
	// DOCUMENTI
	// specifici
	@Override
    public Response pubblicaIstanza( Long idIstanza, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			String ris = myDocsService.pubblicaIstanza(idIstanza, null);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::pubblicaIstanza] Errore generico servizio pubblicaIstanza", ex);
			throw new ServiceException("Errore generico servizio elenco pubblicaIstanza");
		}
    }
	@Override
    public Response pubblicaFile( Long idFile, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			String ris = myDocsService.pubblicaFile(idFile, null);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::pubblicaFile] Errore generico servizio pubblicaFile", ex);
			throw new ServiceException("Errore generico servizio elenco pubblicaFile");
		}
    }
	
//	@Override
//    public Response pubblicaFile( Long idFile, Long idIstanza, Long idStoricoWorkflow,
//			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//		try {
//			String ris = myDocsService.pubblicaFile(idFile,idIstanza,idStoricoWorkflow);
//			return Response.ok(ris).build();
//		} catch (Exception ex) {
//			LOG.error("[" + CLASS_NAME + "::pubblicaFile] Errore generico servizio pubblicaFile", ex);
//			throw new ServiceException("Errore generico servizio elenco pubblicaFile");
//		}
//    }
	
	@Override
    public Response pubblicaMyDocs( Long idIstanza, Long idStoricoWorkflow,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			
			LOG.debug("[" + CLASS_NAME + "::pubblicaMyDocs] - id istanza: "+ idIstanza+" - id storico workflow: " + idStoricoWorkflow);			
		    String ris = myDocsService.pubblicaMyDocs(idIstanza,idStoricoWorkflow);
		    return Response.ok(ris).build();
		
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::pubblicaMyDocs] Errore generico servizio pubblicaMyDocs", ex);
			throw new ServiceException("Errore generico servizio elenco pubblicaMyDocs");
		}
    }

	@Override
	public Response listRichiesteMyDocs(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			
			LOG.debug("[" + CLASS_NAME + "::listRichiesteMyDocs] - id istanza: "+ idIstanza);			
		    List<MyDocsRichiestaEntity> ris = myDocsService.findByIdIstanza(idIstanza);
		    return Response.ok(ris).build();
		
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::listRichiesteMyDocs] Errore generico servizio listRichiesteMyDocs", ex);
			throw new ServiceException("Errore generico servizio elenco listRichiesteMyDocs ");
		}
	}


	
	@Override
    public Response pubblicaIstanza( Long idIstanza, Long idRichiesta,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			String ris = myDocsService.pubblicaIstanza(idIstanza, idRichiesta);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::pubblicaIstanza] Errore generico servizio pubblicaIstanza", ex);
			throw new ServiceException("Errore generico servizio elenco pubblicaIstanza");
		}
    }
	@Override
    public Response pubblicaFile( Long idFile, Long idRichiesta,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			String ris = myDocsService.pubblicaFile(idFile, idRichiesta);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::pubblicaFile] Errore generico servizio pubblicaFile", ex);
			throw new ServiceException("Errore generico servizio elenco pubblicaFile");
		}
    }
	
	
}
