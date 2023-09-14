/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.CosmoLogPraticaEntity;

/**
 * DAO per l'accesso alla tabella dei log delle pratiche che devono essere create su COSMO
 * <br>
 * <br>Tabella moon_cs_t_log_pratica
 * <br>PK: idLogPratica
 * <br>AK: idPratica
 * <br>AK: idIstanza,idx
 * <br>
 * <br>Tabella principale : moon_cs_t_log_pratica
 * 
 * @see CosmoLogPraticaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 21/04/2021 - versione iniziale
 */
public interface CosmoLogPraticaDAO {
	
	public CosmoLogPraticaEntity findById(Long idLogPratica);
	public List<CosmoLogPraticaEntity> findByIdPratica(String idPratica);
	public List<CosmoLogPraticaEntity> findByIdIstanza(Long idIstanza);
	public Long insert(CosmoLogPraticaEntity entity);
	public int update(CosmoLogPraticaEntity entity);
}
