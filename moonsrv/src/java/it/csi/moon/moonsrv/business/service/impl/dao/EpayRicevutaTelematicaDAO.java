/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.EpayRicevutaTelematicaEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle ricevute telematiche
 * <br>
 * <br>Tabella moon_ep_t_ricevuta_telemematica
 * <br>PK: idRicevutaTelematica
 * <br>
 * 
 * @see EpayRicevutaTelematicaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 26/11/2021 - versione iniziale
 */
public interface EpayRicevutaTelematicaDAO {

	public EpayRicevutaTelematicaEntity findById(Long idRicevutaTelematica) throws ItemNotFoundDAOException, DAOException;
	public List<EpayRicevutaTelematicaEntity> findByIdTestata(Long idTestata) throws DAOException;
	public Long insert(EpayRicevutaTelematicaEntity entity) throws DAOException;
	public int update(EpayRicevutaTelematicaEntity entity) throws DAOException;

}
