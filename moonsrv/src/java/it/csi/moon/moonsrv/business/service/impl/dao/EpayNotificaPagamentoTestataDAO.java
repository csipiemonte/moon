/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import it.csi.moon.commons.entity.EpayNotificaPagamentoTestataEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella delle testata di notifica di pagamento
 * <br>
 * <br>Tabella moon_ep_t_notifica_pagam_testa
 * <br>PK: idNotificaPagamTestata
 * <br>
 * 
 * @see EpayNotificaPagamentoTestataEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 26/11/2021 - versione iniziale
 */
public interface EpayNotificaPagamentoTestataDAO {

	public EpayNotificaPagamentoTestataEntity findById(Long idRichiesta) throws ItemNotFoundDAOException, DAOException;
	public Long insert(EpayNotificaPagamentoTestataEntity entity) throws DAOException;
	public int update(EpayNotificaPagamentoTestataEntity entity) throws DAOException;

}
