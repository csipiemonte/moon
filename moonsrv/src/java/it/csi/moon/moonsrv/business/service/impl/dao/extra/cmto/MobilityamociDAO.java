/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.cmto;


import it.csi.moon.commons.entity.MobilityamociEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

public interface MobilityamociDAO {
	
	public MobilityamociEntity findByCF(String cf) throws DAOException;
	
}
