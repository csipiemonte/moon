/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import it.csi.moon.moonbobl.business.be.WorkflowApi;
import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.IstanzeService;
import it.csi.moon.moonbobl.business.service.ModuliService;
import it.csi.moon.moonbobl.business.service.ProcessiService;
import it.csi.moon.moonbobl.business.service.WorkflowService;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloAttributoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowFilter;
import it.csi.moon.moonbobl.dto.moonfobl.Azione;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAzione;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.Processo;
import it.csi.moon.moonbobl.dto.moonfobl.StoricoWorkflow;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.MapModuloAttributi;
import it.csi.moon.moonbobl.util.ModuloAttributoKeys;
import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;
import it.csi.moon.moonbobl.util.decodifica.DecodificaRuolo;


@Component
public class WorkflowApiImpl extends MoonBaseApiImpl implements WorkflowApi {
	
	private final static String CLASS_NAME = "WorkflowApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();	
	
	private final static String COD_DATIAZIONE_NOTIFICA = "NOTIFICA";
	
	@Autowired
	private IstanzeService istanzeService;
	@Autowired
	private ModuliService moduliService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ProcessiService processiService;
	@Autowired 
	AuditService auditService;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;	
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	@Autowired
	RepositoryFileDAO repositoryFileDAO;

	  
	@Override
	public Response getElencoProcessi( 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getElencoProcessi] BEGIN");
			UserInfo user = retrieveUserInfo(httpRequest);
			List<Processo> elenco = processiService.getElencoProcessi();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getElencoProcessi] Errore servizio getElencoProcessi",e);
			throw new ServiceException("Errore servizio elenco processi");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoProcessi] Errore generico servizio getElencoProcessi",ex);
			throw new ServiceException("Errore generico servizio elenco processi");
		} 
	}
		
	public Response getWorkflowByIdIstanza(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getWorkflowByIdIstanza] idIstanza: "+ idIstanza);
			UserInfo user = retrieveUserInfoWithRoles(httpRequest);
			log.debug("[" + CLASS_NAME + "::getWorkflowByIdIstanza] utente bo: "+ user.getIdentificativoUtente());
			
			Istanza istanza = istanzeService.getIstanzaById(user, idIstanza);
			
			Boolean isUtenteAbilitato = moduliService.verificaAbilitazioneModulo(istanza.getModulo().getIdModulo(), user);
			if (!isUtenteAbilitato) {
				log.error("[" + CLASS_NAME + "::getWorkflowByIdIstanza] Errore servizio getWorkflowByIdIstanza in quanto utente non abilitato al modello");
				throw new ServiceException("Utente non abilitato ad agire sull'istanza indicata");
			}
			
			StoricoWorkflow lastStoricoWf =  workflowService.getUltimoStepWorkflow(istanza.getIdIstanza())
					.orElseThrow(ItemNotFoundBusinessException::new);
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdModulo(istanza.getModulo().getIdModulo());
			filter.setIdProcesso(lastStoricoWf.getIdProcesso());
			filter.setIdStatoWfPartenza(lastStoricoWf.getIdStatoWfArrivo());
			filter.setEscludiAzioniDiSistema(true);
			
			//escludi azione utente compilante id_tipo_utente_destinatario = 4 ( utente front office ) solo quando 
			// non c'è la configurazione utente operatore compilaBO in modo che l'utente compilaBO possa
			// accedere alle azioni di invio e riporta in bozza
			Modulo modulo = istanza.getModulo();						
			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(modulo.getIdModulo());
			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
			String compilaBo = attributi.getWithCorrectType(ModuloAttributoKeys.COMPILA_BO);
			
			if (!user.hasRuolo(DecodificaRuolo.OP_COMP.getId()) && StringUtils.isEmpty(compilaBo)) {
				filter.setEscludiAzioniUtenteCompilante(true);
			}
			
			log.info("[" + CLASS_NAME + "::getWorkflowByIdIstanza] filter stato arrivo: "+ lastStoricoWf.getIdStatoWfArrivo());
			
			List<Workflow> elenco = workflowService.getElencoAzioniPossibili(user,idIstanza,filter, httpRequest.getRemoteAddr());
			
//			List<Workflow> elenco = workflowService.getElencoAzioniPossibili(filter);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"getWorkflowByidIstanza-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(elenco).build();
		} catch (ItemNotFoundBusinessException infbe) {
			log.error("[" + CLASS_NAME + "::getWorkflowByIdIstanza] ItemNotFoundBusinessException idIstanza=" + idIstanza);
			throw new ServiceException(infbe);
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getWorkflowByIdIstanza] Errore servizio getWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio elenco azioni possibili");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getWorkflowByIdIstanza] Errore generico servizio getWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco azioni possibili");
		} 
	}
  

	@Override
	public Response getStoricoWorkflowByIdIstanza(Long idIstanza, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		try {
			log.debug("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] idIstanza: "+ idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] utente bo: "+ user.getIdentificativoUtente());
			
			Istanza istanza = istanzeService.getIstanzaById(user, idIstanza);
			
			Boolean isUtenteAbilitato = moduliService.verificaAbilitazioneModulo(istanza.getModulo().getIdModulo(), user);
			if (!isUtenteAbilitato) {
				log.error("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] Errore servizio getWorkflowByIdIstanza in quanto utente non abilitato al modello");
				throw new ServiceException("Utente non abilitato ad agire sull'istanza indicata");
			}
	
			List<StoricoWorkflow> elenco = workflowService.getElencoStorico(istanza.getIdIstanza());
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"getStoricoWorkflowByidIstanza-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(elenco).build();

		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] ItemNotFoundBusinessException idIstanza="  + idIstanza);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] Errore servizio getStoricoWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio elenco storico workflow");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getStoricoWorkflowByIdIstanza] Errore generico servizio getStoricoWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco azioni possibili");
		} 
	}


	@Override
	public Response compieAzione(Long idIstanza, Azione azione, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		try {
			log.debug("[" + CLASS_NAME + "::doAzione] IN idIstanza:"+idIstanza+"\nAzione body: "+ azione);
			UserInfo user = retrieveUserInfo(httpRequest);
			Azione azioneCompiuta = workflowService.compieAzione(user, idIstanza, azione, httpRequest.getRemoteAddr());
						
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"compieAzione-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(azioneCompiuta).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::doAzione] Errore servizio doAzione",e);
			throw new ServiceException("Errore servizio compie azione");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::doAzione] Errore generico servizio doAzione",ex);
			throw new ServiceException("Errore generico servizio compie azione");
		}
		
	}

	@Override
	public Response getStrutturaByIdWorkflow(Long idIstanza, Long idWorkflow, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			DatiAzione datiAzione = workflowService.getStrutturaByIdWorkflow(idWorkflow);
			 
			// Audit Operazione
			 try {
					AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
							auditService.retrieveUser(user), 
							AuditEntity.EnumOperazione.READ,
							"getStrutturaByIdWorkflow-idIstanza-idWorkflow", 
							idIstanza.toString() + "-" + idWorkflow.toString());
					auditService.traceOperazione(auditEntity);
			} catch (Exception e) {
				log.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] struttura non trovata", e );
			}
				
			 return Response.ok(datiAzione).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] Errore servizio getStrutturaByIdStruttura",e);
			throw new ServiceException("Errore servizio getStrutturaByIdWorkflow");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] Errore generico servizio getStrutturaByIdStruttura",ex);
			throw new ServiceException("Errore generico servizio getStrutturaByIdWorkflow");
		} 
	}
	
	@Override
	public Response getStrutturaByCodiceAzione(String codice, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			DatiAzione datiAzione = workflowService.getStrutturaByCodicedAzione(codice);
			 
			// Audit Operazione
			 try {
					AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
							auditService.retrieveUser(user), 
							AuditEntity.EnumOperazione.READ,
							"getStrutturaByCodicedAzione-codiceAzione",codice
							);
					auditService.traceOperazione(auditEntity);
			} catch (Exception e) {
				log.error("[" + CLASS_NAME + "::getStrutturaByIdCodiceAzione] struttura non trovata", e );
			}
				
			 return Response.ok(datiAzione).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdCodiceAzione] struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdCodiceAzione] Errore servizio getStrutturaByIdCodiceAzione",e);
			throw new ServiceException("Errore servizio getStrutturaByIdCodiceAzione");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdCodiceAzione] Errore generico servizio getStrutturaByIdCodiceAzione",ex);
			throw new ServiceException("Errore generico servizio getStrutturaByIdCodiceAzione");
		} 
	}


	@Override
	public Response getWorkflowByIdWorkflow(Long idWorkflow, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getWorkflowByIdWorkflow] IN idWorkflow: "+ idWorkflow + "  fields: "+ fields);
			UserInfo user = retrieveUserInfo(httpRequest);
			Workflow workflow = workflowService.getWorkflowById(idWorkflow, fields);
			try {
					AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
							auditService.retrieveUser(user), 
							AuditEntity.EnumOperazione.READ,
							"getWorkflowByIdWorkflow-idWorkflow", 
							idWorkflow.toString());
					auditService.traceOperazione(auditEntity);
			} catch (Exception e) {
				log.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] struttura non trovata", e );
			}
			return Response.ok(workflow).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getWorkflowByIdWorkflow] Errore servizio getWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio elenco azioni possibili");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getWorkflowByIdWorkflow] Errore generico servizio getWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco azioni possibili");
		} 
	}


	@Override
	public Response completaAzione(Long idIstanza, Azione azione,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		try {
			log.debug("[" + CLASS_NAME + "::completaAzione] IN body: "+ azione);
			UserInfo user = retrieveUserInfo(httpRequest);
			Azione azioneCompiuta = workflowService.compieAzione(user, idIstanza, azione, httpRequest.getRemoteAddr());
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"completaAzione-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
		
//			repositoryFileDAO.commit();
//			//pubblicazione verso mydocs (in quanto azione con dati) se previsto
			moonsrvDAO.pubblicaMyDocs(azioneCompiuta.getIdIstanza(), azioneCompiuta.getIdStoricoWorkflow());
			
			return Response.ok(azioneCompiuta).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::completaAzione] Errore servizio completaAzione",e);
			throw new ServiceException("Errore servizio completaAzione");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::completaAzione] Errore generico servizio completaAzione",ex);
			throw new ServiceException("Errore generico servizio completaAzione");
		} 
	}
	
	@Override
	public Response motivaModifica(Long idIstanza, Azione azione,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::motivaModifica] IN body: "+ azione);
			UserInfo user = retrieveUserInfo(httpRequest);
			Azione azioneCompiuta = workflowService.motivaModifica(user, idIstanza, azione);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"motivaModifica-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(azioneCompiuta).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::motivaModifica] struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::motivaModifica] Errore servizio motivaModifica",e);
			throw new ServiceException("Errore servizio motivaModifica");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::motivaModifica] Errore generico servizio motivaModifica",ex);
			throw new ServiceException("Errore generico servizio motivaModifica");
		} 
	}


	@Override
	public Response getUltimoWorkflowByIdIstanza(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] idIstanza: "+ idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			
			Istanza istanza = istanzeService.getIstanzaById(user, idIstanza);
			
			Boolean isUtenteAbilitato = moduliService.verificaAbilitazioneModulo(istanza.getModulo().getIdModulo(), user);
			if (!isUtenteAbilitato) {
				log.error("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] Errore servizio getUltimoWorkflowByIdIstanza in quanto utente non abilitato al modello");
				throw new ServiceException("Utente non abilitato ad agire sull'istanza indicata");
			}
			
			StoricoWorkflow lastStoricoWf =  workflowService.getUltimoStepWorkflow(istanza.getIdIstanza())
					.orElseThrow(ItemNotFoundBusinessException::new);
			log.info("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] ultimo step eseguito: "+ lastStoricoWf.getIdStoricoWorkflow());
			
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdProcesso(lastStoricoWf.getIdProcesso());
			filter.setIdStatoWfPartenza(lastStoricoWf.getIdStatoWfPartenza());
			filter.setIdStatoWfArrivo(lastStoricoWf.getIdStatoWfArrivo());
			filter.setIdAzione(lastStoricoWf.getIdAzione());
			filter.setEscludiAzioniUtenteCompilante(false);
			filter.setEscludiAzioniDiSistema(true);

			Workflow ultimo = workflowService.getWorkflowByFilter(filter);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"getUltimoWorkflowByIdIstanza-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(ultimo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] ItemNotFoundBusinessException idIstanza=" + idIstanza);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] Errore servizio getUltimoWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio getUltimoWorkflowByIdIstanza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getUltimoWorkflowByIdIstanza] Errore generico servizio getUltimoWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio getUltimoWorkflowByIdIstanza");
		} 
	}


	@Override
	public Response annullaAzione(Long idIstanza, Azione azione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::annullaAzione] IN body: "+ azione.getIdWorkflow());
			UserInfo user = retrieveUserInfo(httpRequest);
			Azione azioneAnnullata = workflowService.annullaAzione(user, idIstanza, azione);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"annullaAzione-idIstanza-codiceAzione", 
					idIstanza.toString() + "-" + azione.getCodiceAzione() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(azioneAnnullata).build();
			
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::annullaAzione] Errore servizio annullaAzione",e);
			throw new ServiceException("Errore servizio annullaAzione");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::annullaAzione] Errore generico servizio annullaAzione",ex);
			throw new ServiceException("Errore generico servizio annullaAzione");
		} 
	}
	
	@Override
	public Response getStoricoWorkflowById(Long idStoricoWorkflow, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getStoricoWorkflowById] idStoricoWorkflow: "+ idStoricoWorkflow);
			UserInfo user = retrieveUserInfo(httpRequest);
		
							
			StoricoWorkflow storicoWorkflow = workflowService.getStoricoWorkflowById(idStoricoWorkflow);
			
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdProcesso(storicoWorkflow.getIdProcesso());
			filter.setIdStatoWfPartenza(storicoWorkflow.getIdStatoWfPartenza());
			filter.setIdStatoWfArrivo(storicoWorkflow.getIdStatoWfArrivo());
			filter.setIdAzione(storicoWorkflow.getIdAzione());
			filter.setEscludiAzioniUtenteCompilante(false);
			filter.setEscludiAzioniDiSistema(false);
			
			
			DatiAzione datiAzione = null;
			
			//gestione caso specifico per azione generale di modifica d'Ufficio operatore BO ( non configurata nei workflow )
			if (storicoWorkflow.getIdAzione().intValue() == DecodificaAzione.MODIFICA.getIdAzione().intValue()){				
				datiAzione = workflowService.getStrutturaByCodicedAzione(COD_DATIAZIONE_NOTIFICA);
				storicoWorkflow.setStrutturaDatiAzione(datiAzione.getStruttura());
				storicoWorkflow.setDatiAzione("{\"data\": "+storicoWorkflow.getDatiAzione()+"}");
			}
			else {
				datiAzione = workflowService.getStrutturaByFilter(filter);
				storicoWorkflow.setStrutturaDatiAzione(datiAzione.getStruttura());
			}
			
	
			try {
					AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
							auditService.retrieveUser(user), 
							AuditEntity.EnumOperazione.READ,
							"getStoricoWorkflowById-idStoricoWorkflow", 
							idStoricoWorkflow.toString() );
					auditService.traceOperazione(auditEntity);
			} catch (Exception e) {
				log.error("[" + CLASS_NAME + "::getStoricoWorkflowById] struttura non trovata", e );
			}
			return Response.ok(storicoWorkflow).build();
			
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getStoricoWorkflowById] Errore servizio getWorkflowByIdIstanza",e);
			throw new ServiceException("Errore servizio elenco azioni possibili");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getStoricoWorkflowById] Errore generico servizio getWorkflowByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco azioni possibili");
		} 
	}

	@Override
	public Response getInitDatiAzioneaByIdWorkflow(Long idIstanza, Long idWorkflow, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try 
		{
			UserInfo user = retrieveUserInfo(httpRequest);
			DatiAzione datiAzione = workflowService.initDatiAzione(user, idIstanza, idWorkflow, getIpAddress(httpRequest));
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"getInitDatiAzioneaByIdWorkflow-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity); 		 
			return Response.ok(datiAzione).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore servizio getStrutturaByIdStruttura",e);
			throw new ServiceException("Errore servizio getStrutturaByIdStruttura");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore generico servizio getStrutturaByIdStruttura",ex);
			throw new ServiceException("Errore generico servizio getStrutturaByIdStruttura");
		} 
	}

	@Override
	public Response getAzioneWorkflowDaEseguireDopoInviata(Long idModulo,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			Workflow workflow = workflowService.identificaAzioneWorkflowDaEseguireDopoInviata(idModulo);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"getAzioneWorkflowDaEseguireDopoInviata-idModulo", 
					idModulo.toString() );
			auditService.traceOperazione(auditEntity); 		 
			return Response.ok(workflow).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.info("[" + CLASS_NAME + "::getAzioneWorkflowDaEseguireDopoInviata] workflow non trovata per idModulo=" + idModulo); // Non è un errore ma torniamo un 404
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getAzioneWorkflowDaEseguireDopoInviata] Errore servizio getAzioneWorkflowDaEseguireDopoInviata", e);
			throw new ServiceException("Errore servizio getStrutturaByIdStruttura");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getAzioneWorkflowDaEseguireDopoInviata] Errore generico servizio getAzioneWorkflowDaEseguireDopoInviata", ex);
			throw new ServiceException("Errore generico servizio getAzioneWorkflowDaEseguireDopoInviata");
		} 
	}
	
	@Override
	public Response pubblicaFileMyDocs(Long idFile,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::pubblicaFileMyDocs] IN idFile: " + idFile);
		
			moonsrvDAO.pubblicaFileMyDocs(idFile);
			log.debug("[" + CLASS_NAME + "::pubblicaFileMyDocs] END");
	        return Response.ok().build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::pubblicaFileMyDocs] Errore servizio pubblicaFileMyDocs", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::pubblicaMyDocs] Errore generico servizio pubblicaFileMyDocs", ex);
			throw new ServiceException("Errore generico servizio pubblicaFileMyDocs");
		} 
	}
	
	@Override
	public Response pubblicaFileMyDocs(Long idFile, Long idRichiesta,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::pubblicaFileMyDocs] IN idFile: " + idFile);
		
			moonsrvDAO.pubblicaFileMyDocs(idFile, idRichiesta);
			log.debug("[" + CLASS_NAME + "::pubblicaFileMyDocs] END");
	        return Response.ok().build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::pubblicaFileMyDocs] Errore servizio pubblicaFileMyDocs", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::pubblicaMyDocs] Errore generico servizio pubblicaFileMyDocs", ex);
			throw new ServiceException("Errore generico servizio pubblicaFileMyDocs");
		} 
	}

	@Override
	public Response getWorkflowsByIdProcesso(String idProcessoQP, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getWorkflowsByIdProcesso] IN idProcessoQP: "+idProcessoQP);
			//
			Long idProcesso = validaLongRequired(idProcessoQP);
			List<Workflow> result = workflowService.getWorkflowsByIdProcesso(idProcesso);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getWorkflowsByIdProcesso] BusinessException getWorkflowsByIdProcesso", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::saveWorkflow] Exception getWorkflowsByIdProcesso", ex);
			throw new ServiceException("Errore generico servizio getWorkflowsByIdProcesso");
		} 
	}

	@Override
	public Response saveWorkflow(Workflow body, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::saveWorkflow] IN Workflow body: "+body);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Workflow result = workflowService.saveWorkflow(user, body);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::saveWorkflow] Errore servizio saveWorkflow", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::saveWorkflow] Errore generico servizio saveWorkflow", ex);
			throw new ServiceException("Errore generico servizio saveWorkflow");
		} 
	}

	@Override
	public Response patchWorkflowById(Long idWorkflow, Workflow partialWorkflow, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::patchWorkflowById] IN Workflow partialWorkflow: " + partialWorkflow);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Workflow result = workflowService.patchWorkflow(user, idWorkflow, partialWorkflow);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::patchWorkflowById] Errore servizio patchWorkflowById", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::patchWorkflowById] Errore generico servizio patchWorkflowById", ex);
			throw new ServiceException("Errore generico servizio patchWorkflowById");
		} 
	}

	@Override
	public Response deleteWorkflow(String idWorkflowPP, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::deleteWorkflow] IN idWorkflowPP: "+idWorkflowPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idWorkflow = validaLongRequired(idWorkflowPP);
			workflowService.deleteWorkflow(user, idWorkflow);
			return Response.ok().build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::deleteWorkflow] Errore servizio deleteWorkflow", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::deleteWorkflow] Errore generico servizio deleteWorkflow", ex);
			throw new ServiceException("Errore generico servizio deleteWorkflow");
		} 
	}
	
}
