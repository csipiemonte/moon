/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore;

import java.util.List;

import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.NotifyParametroEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

public interface NotifyParametroDAO {

	public List<NotifyParametroEntity> findForModulo(Long idEnte, Long idArea, Long idModulo) throws DAOException;
	public List<NotifyParametroEntity> findForModulo(AreaModuloEntity areaModulo) throws DAOException;
	
	public NotifyParametroEntity findById(Long idParametro) throws ItemNotFoundDAOException, DAOException;
	public Long insert(NotifyParametroEntity entity) throws DAOException;
	public int update(NotifyParametroEntity entity) throws DAOException;
	
}
