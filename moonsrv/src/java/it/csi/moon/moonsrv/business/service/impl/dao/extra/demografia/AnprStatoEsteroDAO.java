/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia;


import java.util.List;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.AnprStatoEsteroEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;


/**
 *
 * @author Laurent Pissard
 */
public interface AnprStatoEsteroDAO {

	/**
	 *
	 * @return
	 */
	public List<AnprStatoEsteroEntity> findAll();
	
	public List<AnprStatoEsteroEntity> find(String uso, String ue);

	public AnprStatoEsteroEntity findByName(String name) throws DAOException;

}
