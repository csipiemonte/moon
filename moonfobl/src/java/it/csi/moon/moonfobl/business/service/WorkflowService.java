/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import java.util.List;
import java.util.Optional;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.StoricoWorkflow;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

/**
 * @author franc
 * Metodi di business relativi al workflow
 */
public interface WorkflowService {
	
	public List<Workflow> getElencoAzioniPossibili(WorkflowFilter filter) throws BusinessException;	
	
	public List<Workflow> getElencoAzioniPossibili(UserInfo user, Long idIstanza, WorkflowFilter filter)
			throws BusinessException;

	public Optional<StoricoWorkflow> getUltimoStepWorkflow(Long idIstanza) throws BusinessException;
	
	public List<StoricoWorkflow> getElencoStorico(Long idIstanza) throws BusinessException;
	
	public CompieAzioneResponse compieAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException;
	
	public Workflow getWorkflowById(Long idWorkflow) throws BusinessException;	
	
	public DatiAzione getStrutturaByIdWorkflow(Long idWorkflow) throws BusinessException;
	
	public Workflow getWorkflowByFilter(WorkflowFilter filter) throws BusinessException;
	
	public Azione annullaAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException;
	
	public StoricoWorkflow getStoricoWorkflowById(Long idStoricoWorkflow) throws BusinessException;
	
	public DatiAzione getStrutturaByFilter(WorkflowFilter filter) throws BusinessException;
	
	public DatiAzione initDatiAzione(UserInfo user, Long idIstanza, Long idWorkflow, String ipAddress) throws BusinessException;

	public List<Workflow> getWorkflow(UserInfo user, WorkflowFilter filter) throws BusinessException;

}
