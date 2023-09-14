/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.ProtocolloMetadatoEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai metadati del protocollo
 * <br>
 * <br>Tabella principale : moon_pr_d_metadato
 * 
 * @see ProtocolloMetadatoEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 12/05/2022 - versione iniziale
 */
public interface ProtocolloMetadatoDAO {

	public List<ProtocolloMetadatoEntity> findAll() throws DAOException;
	
	public ProtocolloMetadatoEntity findById(Long idMetadato) throws ItemNotFoundDAOException, DAOException;
	
	public Long insert(ProtocolloMetadatoEntity metadato) throws DAOException;
	public int update(ProtocolloMetadatoEntity metadato) throws DAOException;
	public int delete(Long idMetadato) throws DAOException;

}
