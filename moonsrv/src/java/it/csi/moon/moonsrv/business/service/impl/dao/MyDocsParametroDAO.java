/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.MyDocsParametroEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai parametri di configurazione per la pubblicazione su MyDocs (DOCME)
 * <br>
 * <br>Tabella principale : moon_md_d_parametri
 * 
 * @see MyDocsParametroEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 07/02/2023 - versione iniziale
 */
public interface MyDocsParametroDAO {
	
	public List<MyDocsParametroEntity> findForModulo(Long idEnte, Long idArea, Long idModulo) throws DAOException;
	public List<MyDocsParametroEntity> findForModulo(AreaModuloEntity areaModulo) throws DAOException;
	public List<MyDocsParametroEntity> findAllByIdModulo(Long idModulo) throws DAOException; //For Export
	
	public MyDocsParametroEntity findById(Long idParametro) throws ItemNotFoundDAOException, DAOException;
	
	public Long insert(MyDocsParametroEntity attributo) throws DAOException;
	public int update(MyDocsParametroEntity parametro) throws DAOException;
	public int delete(Long idParametro) throws DAOException;

}
