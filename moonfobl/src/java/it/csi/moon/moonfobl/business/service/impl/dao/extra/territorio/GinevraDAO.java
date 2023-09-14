/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.territorio;

import java.util.List;

import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;


public interface GinevraDAO {

	//
	// SEDIMI
    public List<Via> getSedimi() throws DAOException;
    public Via getSedimeById(Long idSedime) throws ItemNotFoundDAOException, DAOException;

	//
	// VIE
    public List<Via> getVie(Long idComune, String nomeVia) throws DAOException;
    public Via getViaById(Long idComune, Long idVia) throws ItemNotFoundDAOException, DAOException;
	
	//
	// CIVICI
    public List<Via> getCivici(Long idL2, Long numero) throws DAOException;
    public Via getCivicoById(Long idCivico) throws ItemNotFoundDAOException, DAOException;
	
}
