/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai stati delle istanze
 * <br>
 * <br>Tabella principale : moon_wf_d_stato
 * 
 * @see StatoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface StatoDAO {
	
	public void initCache();
	
	public StatoEntity findById(Integer idStato) throws ItemNotFoundDAOException, DAOException;
	public StatoEntity findByCd(String codiceStato) throws ItemNotFoundDAOException, DAOException;
	public StatoEntity findByNome(String stato) throws ItemNotFoundDAOException, DAOException;
	public List<StatoEntity> find(ModuliFilter filter) throws DAOException;
	
	public Integer insert(StatoEntity entity) throws DAOException;
	int delete(StatoEntity entity) throws DAOException;
	int delete(Integer idStatoWf) throws DAOException;
	int update(StatoEntity entity) throws DAOException;

}
