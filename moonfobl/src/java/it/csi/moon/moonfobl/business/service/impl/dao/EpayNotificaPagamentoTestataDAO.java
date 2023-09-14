/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import it.csi.moon.commons.entity.EpayNotificaPagamentoTestataEntity;

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

	public EpayNotificaPagamentoTestataEntity findById(Long idRichiesta);
	public Long insert(EpayNotificaPagamentoTestataEntity entity);
	public int update(EpayNotificaPagamentoTestataEntity entity);

}
