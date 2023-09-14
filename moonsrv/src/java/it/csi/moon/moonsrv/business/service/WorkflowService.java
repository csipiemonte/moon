/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service;

import java.util.List;
import java.util.Optional;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.StoricoWorkflow;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * @author franc
 * Metodi di business relativi al workflow
 */
public interface WorkflowService {
	
	public List<Workflow> getElencoAzioniPossibili(WorkflowFilter filter) throws BusinessException;	

	public Optional<StoricoWorkflow> getUltimoStepWorkflow(Long idIstanza) throws BusinessException;
	
	public List<StoricoWorkflow> getElencoStorico(Long idIstanza) throws BusinessException;
	
	public Azione compieAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException;
	
	public Workflow getWorkflowById(Long idWorkflow) throws BusinessException;	
	
	public DatiAzione getStrutturaByIdWorkflow(Long idWorkflow) throws BusinessException;
	
	public Workflow getWorkflowByFilter(WorkflowFilter filter) throws BusinessException;
	
	public Azione annullaAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException;
	
	public StoricoWorkflow getStoricoWorkflowById(Long idStoricoWorkflow) throws BusinessException;

	public void compieAzioneAutomaticaIfPresente(Long idIstanza) throws BusinessException;

	public Azione verificaAzionePossibile(Istanza istanza, String codiceAzione) throws BusinessException;
}
