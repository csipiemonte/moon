/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.AllegatoMessaggioSupportoEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

public interface AllegatoMessaggioSupportoDAO {
	
	public AllegatoMessaggioSupportoEntity findById(Long id) throws ItemNotFoundDAOException, DAOException;
	
	public List<AllegatoMessaggioSupportoEntity> findByIdMessaggioSupporto(Long idMessaggioSupporto) throws DAOException;
	
	public Long insert(AllegatoMessaggioSupportoEntity allegato) throws DAOException;
	
}
