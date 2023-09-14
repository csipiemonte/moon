/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.istat;


import java.util.List;

import it.csi.moon.commons.dto.extra.istat.Comune;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai comuni italiani
 * 
 * @see Comune
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
public interface ComuneDAO {

	/**
	 *
	 * @return
	 * @throws DAOException 
	 */
	public List<Comune> findAll() throws DAOException;

	/**
	 * 
	 * @param codiceComune
	 * @return
	 */
	public Comune findByPK(Integer codiceComune) throws ItemNotFoundDAOException, DAOException;
	public Comune findByPKidx(Integer codiceProvincia, Integer codiceComune) throws ItemNotFoundDAOException, DAOException;
	
	/**
	 *
	 * @param codiceProvincia
	 * @return
	 * @throws DAOException 
	 */
	public List<Comune> listByCodiceProvincia(Integer codiceProvincia) throws DAOException;

	/**
	 *
	 * @param nome
	 * @param codiceProvincia
	 * @return
	 */
	public Comune findByNomeCodiceProvincia(String nome, Integer codiceProvincia) throws ItemNotFoundDAOException, DAOException;
	
	/**
	 * 
	 * @param codiceComune
	 * @return
	 */
	public String findEmailAslByCodiceComune(Integer codiceComune) throws ItemNotFoundDAOException, DAOException;
		
}
