/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore;

import java.util.List;

import it.csi.moon.commons.entity.NotifyLogEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

public interface NotifyLogDAO {

	public List<NotifyLogEntity> findByIdIstanza(Long idIstanza) throws DAOException;
	public int insert(NotifyLogEntity entity) throws DAOException;
}
