/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.NotificaEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;


/**
 * DAO per l'accesso alla notifica di invio email
 * <br>
 * <br>Tabella moon_fo_t_notifica
 * <br>PK: idNotifica
 *
 * 
 * @see NotificaEntity
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */

public interface NotificaDAO {
	
	public NotificaEntity findById(Long idNotifica) throws ItemNotFoundDAOException,DAOException;
	public List<NotificaEntity> findByIdIstanza(Long idIstanza) throws DAOException;	
	public Long insert(NotificaEntity entity) throws DAOException;
	int update(NotificaEntity entity) throws DAOException;		
	
	public Long insertAllegatoNotifica(Long idNotifica, Long idFile) throws DAOException;
	
}
