/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.istat;

import java.util.List;

import it.csi.moon.moonbobl.dto.extra.istat.Provincia;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle province
 * <br>Cache Locale di 4 ore, dove viene caricate per regioni
 * 
 * @see Provincia
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
public interface ProvinciaDAO {

	/**
	 *
	 * @return
	 */
	public List<Provincia> findAll() throws DAOException;

	/**
	 * 
	 * @param codiceProvincia
	 * @return
	 */
	public Provincia findByPK(Integer codiceProvincia) throws ItemNotFoundDAOException, DAOException;
	public Provincia findByPKidx(Integer codiceRegione, Integer codiceProvincia) throws ItemNotFoundDAOException, DAOException;
	
	/**
	 *
	 * @param codiceRegione
	 * @return
	 */
	public List<Provincia> listByCodiceRegione(Integer codiceRegione);

	/**
	 * 
	 * @param codiceProvincia
	 * @return
	 */
	public Provincia findBySiglia(String siglaProvincia) throws ItemNotFoundDAOException, DAOException;
	
	/**
	 * 
	 * @param provincia
	 * @return codiceRegione
	 */
	public Integer getCodiceRegione(Provincia provincia);
}
