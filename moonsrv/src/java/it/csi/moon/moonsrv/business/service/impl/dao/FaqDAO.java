/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.FaqEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle FAQ
 * 
 * @see FaqEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface FaqDAO {
	public FaqEntity findById(Long idFaq) throws ItemNotFoundDAOException, DAOException;
	public List<FaqEntity> findByIdModulo(Long idModulo) throws DAOException;
	
	public Long insert(FaqEntity entity) throws DAOException;
	public int delete(FaqEntity entity) throws DAOException;
	public int delete(Long idFaq) throws DAOException;
	public int update(FaqEntity entity) throws DAOException;
	
	//
	public int insertFaqModulo(Long idFaq, Long idModulo, Integer ordine) throws DAOException;
	public int delete(Long idFaq, Long idModulo) throws DAOException;
}

