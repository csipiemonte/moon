/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.AmbitoEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

public interface AmbitoDAO {
	
	public AmbitoEntity findById(long idAmbito) throws ItemNotFoundDAOException, DAOException;
	public AmbitoEntity findByCodice(String codiceAmbito) throws ItemNotFoundDAOException, DAOException;
	public List<AmbitoEntity> find() throws DAOException;
	public List<AmbitoEntity> findByIdModulo(long idModulo) throws DAOException;
	
}
