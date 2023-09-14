/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.entity.StoricoWorkflowFilter;
import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso allo storicoworkflow
 *
 * @author Alberto
 *
 */
public interface StoricoWorkflowDAO {

	public Optional<StoricoWorkflowEntity> findLastStorico(Long idIstanza) throws DAOException;
	
	public List<StoricoWorkflowEntity> findByIdIstanza(Long idIstanza) throws DAOException;
	
	public Long insert(StoricoWorkflowEntity entity) throws DAOException;
	
	public int updateDataFine(Date data, Long idIstanza) throws DAOException;
	public int updateDataFineById(Date data, Long idStoricoWorkflow) throws DAOException;
	public int setDataFineNull(Long idIstanza) throws DAOException;
	
	public StoricoWorkflowEntity findStoricoWorkflowById(Long idStoricoWorkflow) throws ItemNotFoundDAOException, DAOException;

	public StoricoWorkflowEntity findLastStoricoAzione(Long idIstanza, Long idAzione) throws ItemNotFoundDAOException, DAOException;
	
	public UtenteEntity findOperatore(Long idIstanza) throws DAOException;
	
	public StoricoWorkflowEntity findLastStoricoListAzioni(Long idIstanza, List<String> listAzioni) throws ItemNotFoundDAOException, DAOException;

	public List<StoricoWorkflowEntity> find(StoricoWorkflowFilter filter) throws DAOException;
	
	public int updateIdFileRendered(Long idStoricoWorkflow, Long idFile) throws DAOException;
}
