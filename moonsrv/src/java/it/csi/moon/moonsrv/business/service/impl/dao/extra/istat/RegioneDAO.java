/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.istat;

import java.util.List;

import it.csi.moon.commons.dto.extra.istat.Regione;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle regioni
 * <br>Cache Locale di 4 ore
 * 
 * @see Regione
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
public interface RegioneDAO {

	/**
	 * Recupera la lista delle 20 regioni italiane
	 * @return la lista delle 20 regioni italiane
	 */
	public List<Regione> findAll() throws DAOException;
	
	/**
	 * Recupera una Regione per chiave usando la cache
	 * @param codice ISTAT della regione ricercata
	 * @return la regione ricercata
	 */
	public Regione findByPK(Integer codice) throws ItemNotFoundDAOException, DAOException;
	
	/**
	 * Recupera una Regione per chiave senza usare la cache
	 * @param codice ISTAT della regione ricercata
	 * @return la regione ricercata
	 */
	public Regione findByPKForce(Integer codice) throws ItemNotFoundDAOException, DAOException;
}
