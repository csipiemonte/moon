/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileLazyEntity;
import it.csi.moon.moonbobl.dto.moonfobl.CampoModuloFormioFileName;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

public interface RepositoryFileDAO {

	public RepositoryFileEntity findById(Long id) throws ItemNotFoundDAOException, DAOException;
	public RepositoryFileEntity findByNomeFileIstanzaIdStWf(String nomeFile, Long idIstanza, Long idStoricoWorkflow) throws ItemNotFoundDAOException, DAOException;
	public RepositoryFileEntity findByFormioNameFile(String formioNameFile) throws ItemNotFoundDAOException, DAOException;
	
	public List<RepositoryFileEntity> findByFilter(RepositoryFileFilter filter);
	public List<RepositoryFileEntity> findByIdIstanza(Long idIstanza) throws DAOException;
	public List<RepositoryFileEntity> findProtocollatiByIdIstanza(Long idIstanza) throws DAOException;
	
	public List<RepositoryFileLazyEntity> findByIdIstanzaStoricoWf(Long idIstanza, Long idStoricoWorkflow) throws DAOException;
	public List<RepositoryFileLazyEntity> findLazyByIdIstanzaTipologia(Long idIstanza, Integer idTipologia) throws DAOException;
	public List<RepositoryFileLazyEntity> findLazyByIdNotifica(Long idNotifica) throws DAOException;

	public Long insert(RepositoryFileEntity entity) throws DAOException;
	public int elimina(Long id) throws DAOException;
	public int delete(Long id) throws DAOException;
	public int update(RepositoryFileEntity entity) throws DAOException;

	public int updateIdIstanza(Long idFile, Long idIstanza) throws DAOException;
	public int updateFlFirmato(RepositoryFileLazyEntity entity) throws DAOException;
	public int updateIdStoricoWorkflowIdIstanzaByFormioFileName(Long idIstanza, String formioNomeFile, Long idStoricoWorkflow) throws DAOException;
	public int updateIdStoricoWorkflowByIdFile(Long idIstanza, Long idFile, Long idStoricoWorkflow) throws DAOException;
	public int updateIdIstanza(CampoModuloFormioFileName campoFormioNomeFile, Long idIstanza) throws DAOException;
	public int updateProtocollo(Long idFileRendering, String numeroProtocollo, Date dataProtocollo) throws ItemNotFoundDAOException, DAOException;
	public int updateUuidIndex(RepositoryFileEntity entity) throws DAOException;

	public Optional<RepositoryFileEntity> findRenderedFileByIdIstanzaStoricoTipo(Long idIstanza, Long idStoricoWorkflow, Integer idTipologia) throws DAOException;
	
	public void commit() throws DAOException;

}
