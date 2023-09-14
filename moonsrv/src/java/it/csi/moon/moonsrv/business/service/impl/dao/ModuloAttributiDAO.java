/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai attributi di un modulo
 * 
 * @see ModuloAttributoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ModuloAttributiDAO {
	
	public enum ModuloAttributoFilter {ALL, WCL, COSMO, EMAIL, EXTRAI_DICHIARANTE, CRM, EPAY,DURATA_PROCEDIMENTO_CONF, PROTOCOLLO};
	public List<ModuloAttributoEntity> findByIdModulo(Long idModulo) throws DAOException;
	public List<ModuloAttributoEntity> findByIdModuloFilter(Long idModulo, ModuloAttributoFilter filter) throws DAOException;
	
	public ModuloAttributoEntity findById(Long idAttributo) throws ItemNotFoundDAOException, DAOException ;
	public ModuloAttributoEntity findByNome(Long idModulo, String nomeAttributo) throws ItemNotFoundDAOException, DAOException ;
	
	public Long insert(ModuloAttributoEntity attributo) throws DAOException;
	public int update(ModuloAttributoEntity attributo) throws DAOException;
	public int delete(Long idAttributo) throws DAOException;
	public int deleteAllByIdModulo(Long idModulo) throws DAOException;

}
