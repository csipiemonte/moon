/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrDomandaEntity;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrIbanEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso alle istanze direttamente su Database
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface OneriCostrDAO {
	public OneriCostrDomandaEntity findById(Long id) throws DAOException;
	
	public OneriCostrDomandaEntity findByIdIstanza(Long id) throws DAOException;
	
	public Long insert(OneriCostrDomandaEntity entity) throws DAOException;

	public int update(OneriCostrDomandaEntity entity) throws DAOException;

	public Long sumPagato() throws DAOException;

	public Integer countCfPiva(OneriCostrDomandaEntity entity) throws DAOException;

	public void lock() throws DAOException;
	
}
