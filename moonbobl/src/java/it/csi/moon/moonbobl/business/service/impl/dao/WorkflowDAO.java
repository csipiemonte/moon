/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.DatiAzioneEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowFilter;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla configurazione del workflow
 * 
 * 
 * @author Alberto
 *
 */
public interface WorkflowDAO {

	public List<WorkflowEntity> find(WorkflowFilter filter) throws DAOException;
	
	public WorkflowEntity findById(Long id) throws ItemNotFoundDAOException, DAOException;
	public WorkflowEntity findByProcessoAzione(Long idProcesso, Integer idStatoPartenza, Long idAzione) throws ItemNotFoundDAOException, DAOException;
	
	public DatiAzioneEntity findDatiAzioneById(Long id) throws ItemNotFoundDAOException, DAOException;
	public DatiAzioneEntity findDatiAzioneByCodice(String codice) throws ItemNotFoundDAOException, DAOException;
	
	public Long insert(WorkflowEntity entity) throws DAOException;
	int delete(WorkflowEntity entity) throws DAOException;
	int delete(Long idWorkflow) throws DAOException;
	int update(WorkflowEntity entity) throws DAOException;
	
}
