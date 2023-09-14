/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;
import it.csi.moon.commons.entity.FruitoreDatiAzioneEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;


public interface FruitoreDatiAzioneDAO {

	public Integer insert(FruitoreDatiAzioneEntity entity) throws DAOException;
	public FruitoreDatiAzioneEntity findById(long idFruitoreDatiAzione) throws DAOException;
	public FruitoreDatiAzioneEntity findByIdStoricoWorkflow(long idStoricoWorkflow) throws DAOException;

}
