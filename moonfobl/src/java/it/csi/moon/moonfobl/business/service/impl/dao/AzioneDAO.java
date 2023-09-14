/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.AzioneEntity;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle azioni del workflow
 * 
 * @see AzioneEntity
 * 
 * @author Danilo
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface AzioneDAO {
	
	public void initCache();
	
	public AzioneEntity findById(Long idAzione) throws ItemNotFoundDAOException,DAOException;
	public AzioneEntity findByNome(String nomeAzione) throws ItemNotFoundDAOException,DAOException;
	public AzioneEntity findByCd(String codiceAzione) throws ItemNotFoundDAOException,DAOException;
	
	public Long findIdByCd(String codiceAzione) throws ItemNotFoundDAOException, DAOException;
	public Long findId(DecodificaAzione decodificaAzione) throws ItemNotFoundDAOException, DAOException;
	
	public List<AzioneEntity> find() throws DAOException;
	
	public Long insert(AzioneEntity entity) throws DAOException;
	int delete(AzioneEntity entity) throws DAOException;
	int delete(Long idEnte) throws DAOException;
	int update(AzioneEntity entity) throws DAOException;

}
