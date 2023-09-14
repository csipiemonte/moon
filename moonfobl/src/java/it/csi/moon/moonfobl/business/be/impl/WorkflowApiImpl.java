/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.StoricoWorkflow;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.moonfobl.business.be.WorkflowApi;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.IstanzeService;
import it.csi.moon.moonfobl.business.service.WorkflowService;
import it.csi.moon.moonfobl.business.service.impl.dao.ProcessoDAO;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class WorkflowApiImpl extends MoonBaseApiImpl implements WorkflowApi {
	
	private static final String CLASS_NAME = "WorkflowApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();	
	
	@Autowired
	private IstanzeService istanzeService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired 
	AuditService auditService;
	@Autowired
	ProcessoDAO processoDAO;
  
	public Response getWorkflowByIdIstanza(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getWorkflowByIdIstanza] idIstanza: "+ idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			LOG.debug("[" + CLASS_NAME + "::getWorkflowByIdIstanza] utente: "+ user.getIdentificativoUtente());
			List<Workflow> elenco = null;
			Istanza istanza = istanzeService.getIstanzaById(user, idIstanza);

			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdModulo(istanza.getModulo().getIdModulo());
			StoricoWorkflow ultimoStep = null;
			try {
				ultimoStep = workflowService.getUltimoStepWorkflow(istanza.getIdIstanza())
						.orElseThrow(ItemNotFoundBusinessException::new);
				LOG.info("[" + CLASS_NAME + "::getWorkflowByIdIstanza] filter stato partenza: "+ ultimoStep.getIdStatoWfArrivo());
				filter.setIdProcesso(ultimoStep.getIdProcesso());
				filter.setIdStatoWfPartenza(ultimoStep.getIdStatoWfArrivo());
				elenco = workflowService.getElencoAzioniPossibili(user, idIstanza,filter);
			} catch (Exception e) {
				LOG.warn("[" + CLASS_NAME + "::getWorkflowByIdIstanza] Nessun ultimo stato.");
			}
			return Response.ok(elenco).build();
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getWorkflowByIdIstanza] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getWorkflowByIdIstanza] Errore servizio getWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio elenco azioni possibili");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getWorkflowByIdIstanza] Errore generico servizio getWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco azioni possibili");
		} 
	}
  

	@Override
	public Response getStoricoWorkflowByIdIstanza(Long idIstanza, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		try {
			LOG.debug("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] idIstanza: "+ idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			LOG.debug("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] utente: "+ user.getIdentificativoUtente());
			
			Istanza istanza = istanzeService.getIstanzaById(user, idIstanza);
	
			List<StoricoWorkflow> elenco = workflowService.getElencoStorico(istanza.getIdIstanza());
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] Errore servizio getStoricoWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio elenco storico workflow");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] Errore generico servizio getStoricoWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco azioni possibili");
		} 
	}


	@Override
	public Response compieAzione(Long idIstanza, Azione azione, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		try {
			LOG.debug("[" + CLASS_NAME + "::compieAzione] IN idIstanza:"+idIstanza+"\nAzione body: "+ azione);
			UserInfo user = retrieveUserInfo(httpRequest);
			CompieAzioneResponse azioneCompiuta = workflowService.compieAzione(user, idIstanza, azione);
			return Response.ok(azioneCompiuta).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] Errore servizio compieAzione",e);
			throw new ServiceException("Errore servizio compie azione");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] Errore generico servizio compieAzione",ex);
			throw new ServiceException("Errore generico servizio compie azione");
		}
		
	}

	@Override
	public Response getStrutturaByIdWorkflow(Long idIstanza, Long idWorkflow, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			DatiAzione datiAzione = workflowService.getStrutturaByIdWorkflow(idWorkflow);
			return Response.ok(datiAzione).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] - struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore servizio getStrutturaByIdStruttura",e);
			throw new ServiceException("Errore servizio getStrutturaByIdStruttura");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore generico servizio getStrutturaByIdStruttura",ex);
			throw new ServiceException("Errore generico servizio getStrutturaByIdStruttura");
		} 
	}


	@Override
	public Response getWorkflowByIdWorkflow(Long idWorkflow, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getWorkflowByIdWorkflow] idWorkflow: "+ idWorkflow);
			UserInfo user = retrieveUserInfo(httpRequest);
			Workflow workflow = workflowService.getWorkflowById(idWorkflow);
			return Response.ok(workflow).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getWorkflowByIdWorkflow] Errore servizio getWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio elenco azioni possibili");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getWorkflowByIdWorkflow] Errore generico servizio getWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco azioni possibili");
		} 
	}


	@Override
	public Response completaAzione(Long idIstanza, Azione azione,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::completaAzione] IN body: "+ azione);
			UserInfo user = retrieveUserInfo(httpRequest);
			CompieAzioneResponse azioneCompiuta = workflowService.compieAzione(user, idIstanza, azione);
			return Response.ok(azioneCompiuta).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::completaAzione] Errore servizio completaAzione",e);
			throw new ServiceException("Errore servizio completaAzione");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::completaAzione] Errore generico servizio completaAzione",ex);
			throw new ServiceException("Errore generico servizio completaAzione");
		} 
	}


	@Override
	public Response getUltimoWorkflowByIdIstanza(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] idIstanza: "+ idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			
			Istanza istanza = istanzeService.getIstanzaById(user, idIstanza);
			Modulo modulo = istanza.getModulo();
			StoricoWorkflow ultimoStep =  workflowService.getUltimoStepWorkflow(istanza.getIdIstanza())
					.orElseThrow(ItemNotFoundBusinessException::new);
			LOG.info("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] ultimo step eseguito: "+ ultimoStep.getIdStoricoWorkflow());
			
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdProcesso(ultimoStep.getIdProcesso());
			filter.setIdStatoWfPartenza(ultimoStep.getIdStatoWfPartenza());
			filter.setIdStatoWfArrivo(ultimoStep.getIdStatoWfArrivo());
			filter.setIdAzione(ultimoStep.getIdAzione());
			filter.setIdModulo(modulo.getIdModulo()); // ??? TOVERIFY
			
			Workflow ultimo = workflowService.getWorkflowByFilter(filter);
			return Response.ok(ultimo).build();
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] Errore servizio getUltimoWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio getUltimoWorkflowByIdIstanza");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] Errore generico servizio getUltimoWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio getUltimoWorkflowByIdIstanza");
		} 
	}


	@Override
	public Response annullaAzione(Long idIstanza, Azione azione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::annullaAzione] IN body: "+ azione.getIdWorkflow());
			UserInfo user = retrieveUserInfo(httpRequest);
			Azione azioneAnnullata = workflowService.annullaAzione(user, idIstanza, azione);
			return Response.ok(azioneAnnullata).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::annullaAzione] Errore servizio annullaAzione",e);
			throw new ServiceException("Errore servizio annullaAzione");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::annullaAzione] Errore generico servizio annullaAzione",ex);
			throw new ServiceException("Errore generico servizio annullaAzione");
		} 
	}
	
	@Override
	public Response getStoricoWorkflowById(Long idStoricoWorkflow, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getStoricoWorkflowById] idStoricoWorkflow: "+ idStoricoWorkflow);
			UserInfo user = retrieveUserInfo(httpRequest);
		
							
			StoricoWorkflow storicoWorkflow = workflowService.getStoricoWorkflowById(idStoricoWorkflow);
			
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdProcesso(storicoWorkflow.getIdProcesso());
			filter.setIdStatoWfPartenza(storicoWorkflow.getIdStatoWfPartenza());
			filter.setIdStatoWfArrivo(storicoWorkflow.getIdStatoWfArrivo());
			filter.setIdAzione(storicoWorkflow.getIdAzione());
			filter.setEscludiAzioniUtenteCompilante(false);
			filter.setMostraContenutoAzione(true);
			DatiAzione datiAzione = workflowService.getStrutturaByFilter(filter);
			storicoWorkflow.setStrutturaDatiAzione(datiAzione.getStruttura());
			return Response.ok(storicoWorkflow).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getStoricoWorkflowById] Errore servizio getWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio elenco azioni possibili");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getStoricoWorkflowById] Errore generico servizio getWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco azioni possibili");
		} 
	}

	@Override
	public Response getInitDatiAzioneaByIdWorkflow(Long idIstanza, Long idWorkflow, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try 
		{
			UserInfo user = retrieveUserInfo(httpRequest);
			DatiAzione datiAzione = workflowService.initDatiAzione(user, idIstanza, idWorkflow, httpRequest.getRemoteAddr());
			return Response.ok(datiAzione).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] - struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore servizio getStrutturaByIdStruttura",e);
			throw new ServiceException("Errore servizio getStrutturaByIdStruttura");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore generico servizio getStrutturaByIdStruttura",ex);
			throw new ServiceException("Errore generico servizio getStrutturaByIdStruttura");
		} 
	}
	
	@Override
	public Response getWorkflowByIdAzione(Long idAzione,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				LOG.debug("[" + CLASS_NAME + "::getWorkflowByIdAzione] idIstanza: "+ idAzione);
				UserInfo user = retrieveUserInfo(httpRequest);
				LOG.debug("[" + CLASS_NAME + "::getWorkflowByIdAzione] utente: "+ user.getIdentificativoUtente());
						
				WorkflowFilter filter = new WorkflowFilter();
				filter.setIdAzione(idAzione);
		
				LOG.info("[" + CLASS_NAME + "::getWorkflowByIdAzione] filter id azione: "+ idAzione);
				
				List<Workflow> elenco = workflowService.getWorkflow(user,filter);
				return Response.ok(elenco).build();
				
			} catch (BusinessException e) {
				LOG.error("[" + CLASS_NAME + "::getWorkflowByIdAzione] Errore servizio getWorkflowByIdAzione",e);
				throw new ServiceException("Errore servizio elenco azioni possibili");
			} catch (Throwable ex) {
				LOG.error("[" + CLASS_NAME + "::getWorkflowByIdAzione] Errore generico servizio getWorkflowByIdAzione",ex);
				throw new ServiceException("Errore generico servizio elenco azioni possibili");
			} 
		}
	  


}
