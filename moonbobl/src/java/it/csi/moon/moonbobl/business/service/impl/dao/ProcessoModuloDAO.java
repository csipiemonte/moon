/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.ProcessoModuloEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * Entity della tabella di relazione modulo processo
 * <br>
 * <br>Tabella moon_wf_r_modulo_processo
 * <br>PK: idModulo idProcesso
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface ProcessoModuloDAO {
	
	public ProcessoModuloEntity findById(Long idProcesso, Long idModulo) throws ItemNotFoundDAOException,DAOException;
	public List<ProcessoModuloEntity> find(ProcessoModuloEntity filter) throws DAOException;
	
	public int insert(ProcessoModuloEntity processoModulo) throws DAOException;
	public int updateProcesso(ProcessoModuloEntity processoModuloEntity) throws DAOException;
	public int delete(Long idProcesso, Long idModulo) throws DAOException;
	public int deleteAllByIdModulo(Long idModulo) throws DAOException;
	
}
