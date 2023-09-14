/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.AreaEntity;
import it.csi.moon.commons.entity.AreeFilter;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle aree di un ente su quale sara collocato i moduli
 * <br>Un modulo potra essere solo su un area di un ente, ma potra essere su piu area di diversi enti.
 * <br>
 * <br>Tabella moon_fo_d_area
 * <br>PK: idArea
 * <br>AK: idEnte,codiceArea
 * 
 * @see AreaEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface AreaDAO {
	
	public AreaEntity findById(Long idArea) throws ItemNotFoundDAOException,DAOException;
	public AreaEntity findByCd(Long idEnte, String codiceArea) throws ItemNotFoundDAOException,DAOException;	
	public List<AreaEntity> find() throws DAOException;
	public List<AreaEntity> find(AreeFilter filter) throws DAOException;
	public List<AreaEntity> findByIdEnte(Long idEnte) throws DAOException;
	public Long insert(AreaEntity entity) throws DAOException;
	int delete(AreaEntity entity) throws DAOException;
	int delete(Long idArea) throws DAOException;
	int update(AreaEntity entity) throws DAOException;
	
}
