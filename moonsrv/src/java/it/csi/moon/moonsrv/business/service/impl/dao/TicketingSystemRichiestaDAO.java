/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.TicketingSystemRichiestaEntity;
import it.csi.moon.commons.entity.TicketingSystemRichiestaFilter;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso al tracciamento delle richieste verso i systemi di ticketing CRM es.: NextCRM
 * <br>
 * <br>Tabella principale : moon_ts_t_richiesta
 * 
 * @see TicketingSystemRichiestaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 22/09/2021 - versione iniziale
 */

public interface TicketingSystemRichiestaDAO {
	
	public TicketingSystemRichiestaEntity findById(Long idRichiesta) throws ItemNotFoundDAOException,DAOException;
	public TicketingSystemRichiestaEntity findByUuidTicketingSystem(String uuidProtocollatore) throws ItemNotFoundDAOException,DAOException;	
	public List<TicketingSystemRichiestaEntity> find(TicketingSystemRichiestaFilter filter) throws DAOException;

	public Long insert(TicketingSystemRichiestaEntity entity) throws DAOException;
	int updateResponseById(TicketingSystemRichiestaEntity entity) throws DAOException;		
	int updateResponseByUuidTicketingSystem(TicketingSystemRichiestaEntity entity) throws DAOException;
}
