/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia.impl;


import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.demografia.Cittadinanza;
import it.csi.moon.moonfobl.business.service.impl.dao.component.MoonsrvTemplateImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia.CittadinanzeDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;


/**
 *
 */
@Component
@Qualifier("moonsrv")
public class CittadinanzeDAOImpl extends MoonsrvTemplateImpl implements CittadinanzeDAO {

	/**
	 *
	 */
	public CittadinanzeDAOImpl() {

	}


	@Override
	protected String getPathExtra() {
		return "/extra/demografia/cittadinanze";
	}

	/**
	 *
	 * @return
	 */
	public List<Cittadinanza> findAll() throws DAOException {
		return getMoonsrvJson("", List.class);
	}
	public List<Cittadinanza> findAll(int limit, int skip) throws DAOException {
		MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
		queryParams.add("limit", limit);
		queryParams.add("skip", skip);
		return getMoonsrvJson("", List.class, queryParams);
	}

	/**
	 * 
	 * @param codice
	 * @return
	 */
	public Cittadinanza findByPK(Integer codice) throws DAOException {
		return getMoonsrvJson("/"+codice, Cittadinanza.class);
	}


//TODO da migliorare aggiugendo nuovo servizio moonsrv con query by nome
	@Override
	public Cittadinanza findByDesc(String descMaschile) throws DAOException {
		for (Cittadinanza cittd : findAll()) {
			if (cittd.getNome().equals(descMaschile)) {
				return cittd;
			}
		}
		return null;
	}




	

}
