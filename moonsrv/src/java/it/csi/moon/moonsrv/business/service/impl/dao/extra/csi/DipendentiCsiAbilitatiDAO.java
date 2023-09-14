/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.csi;

import java.util.List;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.DipendentiCsiEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

	
public interface DipendentiCsiAbilitatiDAO {
		
		public List<DipendentiCsiEntity> findByCF(String cf) throws DAOException;
		
		public List<DipendentiCsiEntity> findAbilitati(Long idModulo, String cf) throws DAOException;
		
	}

