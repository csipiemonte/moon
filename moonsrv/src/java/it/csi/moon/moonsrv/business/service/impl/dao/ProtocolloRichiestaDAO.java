/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.ProtocolloRichiestaEntity;
import it.csi.moon.commons.entity.ProtocolloRichiestaFilter;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso al tracciamento delle richieste di Protollocazione
 * <br>
 * <br>Tabella principale : moon_pr_t_richiesta
 * 
 * @see ProtocolloRichiestaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 12/06/2020 - versione iniziale
 */

public interface ProtocolloRichiestaDAO {
	
	public ProtocolloRichiestaEntity findById(Long idRichiesta) throws ItemNotFoundDAOException,DAOException;
	public ProtocolloRichiestaEntity findByUuidProtocollatore(String uuidProtocollatore) throws ItemNotFoundDAOException,DAOException;	
	public List<ProtocolloRichiestaEntity> find(ProtocolloRichiestaFilter filter) throws DAOException;

	public Long insert(ProtocolloRichiestaEntity entity) throws DAOException;
	int updateResponseById(ProtocolloRichiestaEntity entity) throws DAOException;		
	int updateResponseByUuidProtocollatore(ProtocolloRichiestaEntity entity) throws DAOException;
}
