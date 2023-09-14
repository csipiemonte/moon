/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.MessaggioSupportoEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai Messaggi di Supporto (Chat)
 * 
 * @see MessaggioSupportoEntity
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 24/08/2020 - versione iniziale
 */
public interface MessaggioSupportoDAO {
	public MessaggioSupportoEntity findById(Long idMessaggioSupportoEntity) throws ItemNotFoundDAOException, DAOException;
	public List<MessaggioSupportoEntity> findByIdRichista(Long idRichiestaSupporto) throws DAOException;
	
	public Long insert(MessaggioSupportoEntity entity) throws DAOException;
	public int delete(MessaggioSupportoEntity entity) throws DAOException;
	public int delete(Long idMessaggioSupporto) throws DAOException;
	public int update(MessaggioSupportoEntity entity) throws DAOException;
}

