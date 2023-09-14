/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.EpayNotificaPagamentoEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella delle notifiche di pagamento
 * <br>
 * <br>Tabella moon_ep_t_notifica_pagamento
 * <br>PK: idNotificaPagamento
 * <br>
 * 
 * @see EpayNotificaPagamentoEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 26/11/2021 - versione iniziale
 */
public interface EpayNotificaPagamentoDAO {

	public EpayNotificaPagamentoEntity findById(Long idNotificaPagamento) throws ItemNotFoundDAOException, DAOException;
	public List<EpayNotificaPagamentoEntity> findByIdTestata(Long idTestata) throws DAOException;
	public Long insert(EpayNotificaPagamentoEntity entity) throws DAOException;
	public int update(EpayNotificaPagamentoEntity entity) throws DAOException;

}
