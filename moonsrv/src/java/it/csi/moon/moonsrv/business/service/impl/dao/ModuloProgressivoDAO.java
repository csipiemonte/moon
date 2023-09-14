/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import it.csi.moon.commons.entity.ModuloProgressivoEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso al progressivo di un modulo
 * 
 * @see ModuloProgressivoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ModuloProgressivoDAO {
	public ModuloProgressivoEntity findByIdModulo(Long idModulo, Integer idTipoCodiceIstanza) throws DAOException;
//	public ModuloProgressivoEntity findByModuloForUpdate(ModuloEntity modulo) throws DAOException;
	public void insert(ModuloProgressivoEntity entity) throws DAOException;
	public int update(ModuloProgressivoEntity entity) throws DAOException;
	public int delete(Long id) throws DAOException;
	public String generateCodiceIstanzaForIdModulo(ModuloVersionatoEntity moduloE, String descTipoCodiceIstanza) throws DAOException;

}
