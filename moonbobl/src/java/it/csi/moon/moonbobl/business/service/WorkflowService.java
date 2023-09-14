/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service;

import java.util.List;
import java.util.Optional;

import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowFilter;
import it.csi.moon.moonbobl.dto.moonfobl.Azione;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAzione;
import it.csi.moon.moonbobl.dto.moonfobl.StoricoWorkflow;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;

/**
 * @author franc
 * Metodi di business relativi al workflow
 */
public interface WorkflowService {
	
	public static final String FIELD_STATO_PARTENZA = "statoPartenza";
	public static final String FIELD_STATO_ARRIVO = "statoArrivo";
	
	public List<Workflow> getElencoAzioniPossibili(WorkflowFilter filter) throws BusinessException;
	public List<Workflow> getElencoWorkflow(WorkflowFilter filter, String fields) throws BusinessException;

	public Optional<StoricoWorkflow> getUltimoStepWorkflow(Long idIstanza) throws BusinessException;
	
	public List<StoricoWorkflow> getElencoStorico(Long idIstanza) throws BusinessException;
	
	public Azione compieAzione(UserInfo user, Long idIstanza, Azione azione, String ipAddress) throws BusinessException;
	
	public Azione motivaModifica(UserInfo user, Long idIstanza, Azione azione) throws BusinessException;
	
	public Workflow getWorkflowById(Long idWorkflow, String fileds) throws BusinessException;
	public Workflow getWorkflowById(Long idWorkflow) throws BusinessException;	
	
	public DatiAzione getStrutturaByIdWorkflow(Long idWorkflow) throws BusinessException;
	
	public Workflow getWorkflowByFilter(WorkflowFilter filter) throws BusinessException;
	
	public Azione annullaAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException;
	
	public StoricoWorkflow getStoricoWorkflowById(Long idStoricoWorkflow) throws BusinessException;
	
	public DatiAzione getStrutturaByFilter(WorkflowFilter filter) throws BusinessException;
	
	public DatiAzione initDatiAzione(UserInfo user, Long idIstanza, Long idWorkflow, String ipAddress) throws BusinessException;

	public List<Workflow> getElencoAzioniPossibili(UserInfo user, Long idIstanza, WorkflowFilter filter, String ipAddress)
			throws BusinessException;

	public DatiAzione getStrutturaByCodicedAzione(String codice) throws BusinessException;

	public Workflow identificaAzioneWorkflowDaEseguireDopoInviata(Long idModulo) throws ItemNotFoundBusinessException, BusinessException;
	
	public StoricoWorkflow getStoricoWorkflowByFilter(StoricoWorkflowFilter filter) throws BusinessException;

	// CRUD DettaglioProcesso
	public List<Workflow> getWorkflowsByIdProcesso(Long idProcesso) throws BusinessException;
	public Workflow saveWorkflow(UserInfo user, Workflow workflow) throws BusinessException;
	public Workflow patchWorkflow(UserInfo user, Long idWorkflow, Workflow partialWorkflow) throws BusinessException;
	public void deleteWorkflow(UserInfo user, Long idWorkflow) throws BusinessException;

}
