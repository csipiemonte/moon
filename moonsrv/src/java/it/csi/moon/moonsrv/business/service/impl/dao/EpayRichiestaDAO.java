/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.Date;
import java.util.List;

import it.csi.moon.commons.entity.EpayRichiestaEntity;
import it.csi.moon.commons.entity.EpayRichiestaFilter;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

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

	public EpayRichiestaEntity findById(Long idRichiesta) throws ItemNotFoundDAOException, DAOException;
	public EpayRichiestaEntity findByIdEpay(String idEpay) throws ItemNotFoundDAOException, DAOException;
	public EpayRichiestaEntity findLastValideByIdIstanza(Long idIstanza) throws ItemNotFoundDAOException, DAOException;
	public EpayRichiestaEntity findByCodiceAvviso(String codiceAvviso) throws ItemNotFoundDAOException, DAOException;
	
	public List<EpayRichiestaEntity> findByIdIstanza(Long idIstanza);
	public List<EpayRichiestaEntity> find(EpayRichiestaFilter filter);
	
	public Long insert(EpayRichiestaEntity entity);
	public int update(EpayRichiestaEntity entity);
	public int updateIdStoricoWF(Long idRichiesta, Long idStoricoWF);
	
	public int updateNotifica(String codiceAvviso, Long idNotificaPagamento, Date dataNotificaPagamento);
	public int updateRicevuta(String codiceAvviso, Long idRicevutaTelematica, Date dataRicevutaTelematica);
	
	public EpayRichiestaEntity findByIuv(String iuv);

}
