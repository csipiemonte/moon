/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.regp;

import java.util.List;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.DipendentiRPEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

	
public interface DipendentiAbilitatiDAO {
		
		public List<DipendentiRPEntity> findByCF(String cf) throws DAOException;
		
		public List<DipendentiRPEntity> findAbilitati(Long idModulo) throws DAOException;
		
	}

