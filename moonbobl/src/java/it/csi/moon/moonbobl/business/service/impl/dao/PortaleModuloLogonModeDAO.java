/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.PortaleModuloLogonModeEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso via Modulistica
 * <br>
 * <br>Tabella moon_ml_r_portale_modulo_logon_mode
 * 
 * @see PortaleModuloLogonModeEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface PortaleModuloLogonModeDAO {

	List<PortaleModuloLogonModeEntity> findByIdModulo(Long idModulo) throws DAOException;

	int insert(PortaleModuloLogonModeEntity entity) throws DAOException;
	
	int delete(Long idPortale, Long idModulo) throws DAOException;

}
