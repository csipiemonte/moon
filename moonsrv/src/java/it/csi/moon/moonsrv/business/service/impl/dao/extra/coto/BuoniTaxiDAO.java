/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto;


import java.util.List;

import it.csi.moon.commons.entity.ext.ExtTaxiEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

public interface BuoniTaxiDAO {
	public ExtTaxiEntity findByCf(String cf) throws DAOException;
	
	public Integer insert(ExtTaxiEntity entity) throws DAOException;
	
	public List<ExtTaxiEntity> findByCodiceBuono(String codice) throws DAOException;

	public Integer insertRefIstanza(Long idIstanza, Integer idBuono) throws DAOException;
	
	public List<ExtTaxiEntity> findByIdIstanza(Long idIstanza) throws DAOException;

	public List<ExtTaxiEntity> findAll();

}
