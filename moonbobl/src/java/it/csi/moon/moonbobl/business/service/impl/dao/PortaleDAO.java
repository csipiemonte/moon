/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.PortaleEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai Portali del FO
 * <br>
 * <br>Tabella moon_fo_d_portale
 * <br>PK: idPortale
 * <br>AK1: codicePortale
 * <br>AK2: nomePortale (il piu usato vista che ci arriva nel HttpRequest)
 * 
 * @see PortaleEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface PortaleDAO {
	
	public PortaleEntity findById(Long idPortale) throws ItemNotFoundDAOException,DAOException;
	public PortaleEntity findByCd(String codicePortale) throws ItemNotFoundDAOException,DAOException;
	public PortaleEntity findByNome(String nomePortale) throws ItemNotFoundDAOException,DAOException;
	public List<PortaleEntity> find() throws DAOException;
	public List<PortaleEntity> findByIdModulo(long idModulo) throws DAOException;
	
}
