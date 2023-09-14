/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.EpayRichiestaEntity;

/**
 * DAO per l'accesso alla tabella delle richieste di pagamenti EPAY
 * <br>
 * <br>Tabella moon_ep_t_log_epay
 * <br>PK: idLogEpay
 * <br>
 * <br>Tabella principale : moon_ep_t_richiesta
 * 
 * @see EpayRichiestaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 22/11/2021 - versione iniziale
 */
public interface EpayRichiestaDAO {

	public EpayRichiestaEntity findById(Long idRichiesta);
	public EpayRichiestaEntity findLastValideByIdIstanza(Long idIstanza);
	public List<EpayRichiestaEntity> findByIdIstanza(Long idIstanza);
	public Long insert(EpayRichiestaEntity entity);
	public int update(EpayRichiestaEntity entity);
	public int updateIdStoricoWF(Long idRichiesta, Long idStoricoWF);

}
