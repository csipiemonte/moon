/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.CosmoLogServizioEntity;

/**
 * DAO per l'accesso alla tabella dei log delle chiamate ai servizi COSMO
 * <br>
 * <br>Tabella moon_cs_t_servizio
 * <br>PK: idLogServizio
 * <br>
 * <br>Tabella principale : moon_cs_t_log_servizio
 * 
 * @see CosmoLogServizioEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 12/01/2022 - versione iniziale
 */
public interface CosmoLogServizioDAO {
	
	public CosmoLogServizioEntity findById(Long idLogServizio);
	public List<CosmoLogServizioEntity> findByIdServizio(String idServizio);
	public List<CosmoLogServizioEntity> findByIdIstanza(Long idIstanza);
	public Long insert(CosmoLogServizioEntity entity);
	public int update(CosmoLogServizioEntity entity);
	public CosmoLogServizioEntity findByIdIstanzaServizio(Long idIstanza, String servizio);
}
