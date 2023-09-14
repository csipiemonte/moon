/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonfobl.business.service.impl.dto.ConsumerParameterEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai parametri Consumer
 * <br>
 * <br>Tabella principale : moon_fo_t_consumer_parameter
 * 
 * @see ConsumerParameterEntity
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */

public interface ConsumerDAO {
	
    public <P> List<P> getParameters(String consumer, String codiceEnte) throws ItemNotFoundDAOException, DAOException;	
	public <P> P getParameter(String consumer, String codiceEnte,String idParameter, String type) throws ItemNotFoundDAOException, DAOException;

}
