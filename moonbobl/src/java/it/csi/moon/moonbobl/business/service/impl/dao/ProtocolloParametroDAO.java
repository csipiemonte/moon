/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.AreaModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ProtocolloParametroEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai parametri del protocollo
 * <br>
 * <br>Tabella principale : moon_pr_d_parametri
 * 
 * @see ProtocolloParametroEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/06/2020 - versione iniziale
 */
public interface ProtocolloParametroDAO {
	
	public List<ProtocolloParametroEntity> findForModulo(Long idEnte, Long idArea, Long idModulo) throws DAOException;
	public List<ProtocolloParametroEntity> findForModulo(AreaModuloEntity areaModulo) throws DAOException;
	public List<ProtocolloParametroEntity> findAllByIdModulo(Long idModulo) throws DAOException; //For Export
	
	public ProtocolloParametroEntity findById(Long idParametro) throws ItemNotFoundDAOException, DAOException;
	
	public Long insert(ProtocolloParametroEntity attributo) throws DAOException;
	public int update(ProtocolloParametroEntity parametro) throws DAOException;
	public int delete(Long idParametro) throws DAOException;
	
	public int countByIdModulo(Long idModulo) throws DAOException;

}
