/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.MyDocsRichiestaEntity;
import it.csi.moon.commons.entity.MyDocsRichiestaFilter;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso al tracciamento delle richieste di Pubblicazione su MyDocs (DocMe)
 * <br>
 * <br>Tabella principale : moon_md_t_richiesta
 * 
 * @see MyDocsRichiestaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 03/02/2023 - versione iniziale
 */

public interface MyDocsRichiestaDAO {
	
	public MyDocsRichiestaEntity findById(Long idRichiesta) throws ItemNotFoundDAOException,DAOException;
	public MyDocsRichiestaEntity findByUuidMydocs(String uuidMydocs) throws ItemNotFoundDAOException,DAOException;	
	public List<MyDocsRichiestaEntity> find(MyDocsRichiestaFilter filter) throws DAOException;

	public Long insert(MyDocsRichiestaEntity entity) throws DAOException;
	int updateResponseById(MyDocsRichiestaEntity entity) throws DAOException;		
	int updateResponseByUuidMydocs(MyDocsRichiestaEntity entity) throws DAOException;
}
