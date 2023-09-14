/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.AllegatoMessaggioSupportoEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

public interface AllegatoMessaggioSupportoDAO {
	
	public AllegatoMessaggioSupportoEntity findById(Long id) throws ItemNotFoundDAOException, DAOException;
	
	public List<AllegatoMessaggioSupportoEntity> findByIdMessaggioSupporto(Long idMessaggioSupporto) throws DAOException;
	
	public Long insert(AllegatoMessaggioSupportoEntity allegato) throws DAOException;
	
}
