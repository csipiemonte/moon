/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.ParametroEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

public interface ParametroDAO {

	public static final String COMPONENTE_SRV = "SRV";
	public static final String PARAMETRO_AMQ_NODES = "AMQ_NODES";
	
	public ParametroEntity findById(long idParametro) throws ItemNotFoundDAOException, DAOException;
	public ParametroEntity findByCodici(String codiceComponente, String codiceParametro) throws ItemNotFoundDAOException, DAOException;
	public List<ParametroEntity> find() throws DAOException;
	
	public Long insert(ParametroEntity entity) throws DAOException;
	public int delete(ParametroEntity entity) throws DAOException;
	public int delete(long idParametro) throws DAOException;
	public int update(ParametroEntity entity) throws DAOException;
	
}
