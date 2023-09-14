/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import it.csi.moon.commons.entity.EpayRicevutaTelematicaTestataEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella delle testate di ricevuta telematica
 * <br>
 * <br>Tabella moon_ep_t_ricevuta_telem_testa
 * <br>PK: idRicevutaTelemTesta
 * <br>
 * 
 * @see EpayRicevutaTelematicaTestataEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 26/11/2021 - versione iniziale
 */
public interface EpayRicevutaTelematicaTestataDAO {

	public EpayRicevutaTelematicaTestataEntity findById(Long idRicevutaTelematica) throws ItemNotFoundDAOException, DAOException;
	public Long insert(EpayRicevutaTelematicaTestataEntity entity) throws DAOException;
	public int update(EpayRicevutaTelematicaTestataEntity entity) throws DAOException;

}
