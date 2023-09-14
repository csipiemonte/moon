/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.regp;

import java.util.List;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.FineConcEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

	
public interface FineConcDAO {
		
		public List<FineConcEntity> findByCF(String cf) throws DAOException;
		
	}

