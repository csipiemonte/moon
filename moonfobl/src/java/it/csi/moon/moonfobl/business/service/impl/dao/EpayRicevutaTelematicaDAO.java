/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.EpayRicevutaTelematicaEntity;

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

	public EpayRicevutaTelematicaEntity findById(Long idRicevutaTelematica);
	public List<EpayRicevutaTelematicaEntity> findByIdTestata(Long idTestata);
	public Long insert(EpayRicevutaTelematicaEntity entity);
	public int update(EpayRicevutaTelematicaEntity entity);

}
