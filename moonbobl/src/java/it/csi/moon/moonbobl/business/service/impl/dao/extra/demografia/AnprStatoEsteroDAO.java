/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia;


import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.AnprStatoEsteroEntity;


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

}
