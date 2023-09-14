/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import it.csi.moon.moonbobl.business.service.impl.dto.ModuloProgressivoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso al progressivo di un modulo
 * 
 * @see ModuloProgressivoEntity
 * 
 * @author Laurent
 *
 */
public interface ModuloProgressivoDAO {
	public ModuloProgressivoEntity findByIdModulo(Long idModulo, Integer idTipoCodiceIstanza) throws DAOException;
	public ModuloProgressivoEntity findByIdModuloForUpdate(Long idModulo) throws DAOException;
	public void insert(ModuloProgressivoEntity entity) throws DAOException;
	public int update(ModuloProgressivoEntity entity) throws DAOException;
	public int delete(Long id) throws DAOException;
	public String generateCodiceIstanzaForIdModulo(ModuloVersionatoEntity moduloE, String descTipoCodiceIstanza) throws DAOException;
}
