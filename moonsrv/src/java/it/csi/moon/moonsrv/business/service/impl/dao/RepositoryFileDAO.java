/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.csi.moon.commons.dto.CampoModuloFormioFileName;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.RepositoryFileFilter;
import it.csi.moon.commons.entity.RepositoryFileLazyEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

public interface RepositoryFileDAO {

	public RepositoryFileEntity findById(Long id) throws ItemNotFoundDAOException, DAOException;
	public RepositoryFileEntity findByNomeFileIstanzaIdStWf(String nomeFile, Long idIstanza, Long idStoricoWorkflow) throws ItemNotFoundDAOException, DAOException;
	public RepositoryFileEntity findByFormioNameFile(String formioNameFile) throws ItemNotFoundDAOException, DAOException;
	
	public RepositoryFileLazyEntity findLazyById(Long id) throws ItemNotFoundDAOException, DAOException;
	
	public List<RepositoryFileEntity> findByFilter(RepositoryFileFilter filter) throws DAOException;
	public List<RepositoryFileEntity> findByIdIstanza(Long idIstanza) throws DAOException;
	public List<RepositoryFileEntity> findProtocollatiByIdIstanza(Long idIstanza) throws DAOException;
	
	public List<RepositoryFileLazyEntity> findLazyByFilter(RepositoryFileFilter filter) throws DAOException;
	public List<RepositoryFileLazyEntity> findLazyByIdIstanzaStoricoWf(Long idIstanza, Long idStoricoWorkflow) throws DAOException;
	public List<RepositoryFileLazyEntity> findLazyMydocsByIdIstanzaStoricoWf(Long idIstanza, Long idStoricoWorkflow) throws DAOException;
	public List<RepositoryFileLazyEntity> findLazyByIdIstanzaTipologia(Long idIstanza, Integer idTipologia) throws DAOException;
	public List<RepositoryFileLazyEntity> findLazyByIdNotifica(Long idNotifica) throws DAOException;
	
	public Long insert(RepositoryFileEntity entity) throws DAOException;
	public int elimina(Long id) throws DAOException;
	public int delete(Long id) throws DAOException;
	public int update(RepositoryFileEntity entity) throws DAOException;

	public int updateIdIstanza(Long idFile, Long idIstanza) throws DAOException;
	public int updateFlFirmato(RepositoryFileLazyEntity entity) throws DAOException;
	public int updateIdStoricoWorkflowIdIstanzaByFormioFileName(String formioNomeFile, Long idIstanza, Long idStoricoWorkflow) throws DAOException;
	public int updateIdStoricoWorkflowByIdFile(Long idIstanza, Long idFile, Long idStoricoWorkflow) throws DAOException;
	public int updateIdIstanza(CampoModuloFormioFileName campoFormioNomeFile, Long idIstanza) throws DAOException;
	public int updateProtocollo(Long idFileRendering, String numeroProtocollo, Date dataProtocollo) throws ItemNotFoundDAOException, DAOException;
	public int updateUuidIndex(RepositoryFileEntity entity) throws DAOException;

	public Optional<RepositoryFileEntity> findRenderedFileByIdIstanzaStoricoTipo(Long idIstanza, Long idStoricoWorkflow, Integer idTipologia) throws DAOException;


}
