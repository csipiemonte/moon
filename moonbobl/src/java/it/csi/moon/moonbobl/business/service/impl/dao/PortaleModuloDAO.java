/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.PortaleModuloEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * Entity della tabelladi relazione portale modulo
 * <br>
 * <br>Tabella moon_fo_r_portale_modulo
 * <br>PK: idPortale idModulo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface PortaleModuloDAO {
	
	public PortaleModuloEntity findById(Long idPortale, Long idModulo) throws ItemNotFoundDAOException,DAOException;
	public List<PortaleModuloEntity> find(PortaleModuloEntity filter) throws DAOException;
	
	public int insert(PortaleModuloEntity portaleModulo) throws DAOException;
	public int delete(Long idPortale, Long idModulo) throws DAOException;
	public int deleteAllByIdModulo(Long idModulo) throws DAOException;
	
}
