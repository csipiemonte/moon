/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.dto.Processo;
import it.csi.moon.commons.entity.ProcessoEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;


/**
 * DAO per l'accesso ai processi
 * <br>
 * <br>Tabella moon_wf_d_processo
 * <br>PK: idProcesso
 * <br>AK: codiceProcesso
 * 
 * @see Processo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface ProcessoDAO {
	
	public ProcessoEntity findById(Long idProcesso) throws ItemNotFoundDAOException,DAOException;
	public ProcessoEntity findByCd(String codiceProcesso) throws ItemNotFoundDAOException,DAOException;
	public ProcessoEntity findByNome(String nomePortale) throws ItemNotFoundDAOException,DAOException;
	public List<ProcessoEntity> find() throws DAOException;
	public List<ProcessoEntity> findByIdModulo(long idModulo) throws DAOException;
	
	public Long insert(ProcessoEntity entity) throws DAOException;
	int delete(ProcessoEntity entity) throws DAOException;
	int delete(Long idProcesso) throws DAOException;
	int update(ProcessoEntity entity) throws DAOException;
	
}
