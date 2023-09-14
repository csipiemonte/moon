/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import it.csi.moon.commons.entity.ModuloStrutturaEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso alla Struttura di un modulo
 * 
 * @see ModuloStrutturaEntity
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ModuloStrutturaDAO {

	public ModuloStrutturaEntity findByIdVersioneModulo(Long idVersioneModulo) throws DAOException;
	public ModuloStrutturaEntity findById(Long idStruttura) throws DAOException;
	public Long insert(ModuloStrutturaEntity struttura) throws DAOException;
	public int update(ModuloStrutturaEntity struttura) throws DAOException;
	public int delete(Long idVersioneModulo) throws DAOException;
}
