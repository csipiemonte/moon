/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia.impl;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.demografia.Nazione;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.moonfobl.business.service.impl.dao.component.MoonsrvTemplateImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia.NazioneDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;


/**
 *
 */
@Component
@Qualifier("moonsrv")
public class NazioneDAOImpl extends MoonsrvTemplateImpl implements NazioneDAO {
	
	private static final String CLASS_NAME = "NazioneDAOImpl";
//	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	/**
	 *
	 */
	public NazioneDAOImpl() {
	}


	@Override
	protected String getPathExtra() {
		return "/extra/demografia/anpr/nazioni";
	}


	/**
	 *
	 * @return
	 */
	public List<Nazione> findAll(String uso, String ue) throws DAOException {
		LOG.debug("[" + CLASS_NAME + "::findAll]");
		MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
		if(!StrUtils.isEmpty(uso)) {
			queryParams.add("uso", uso);
		}
		if(!StrUtils.isEmpty(ue)) {
			queryParams.add("ue", ue);
		}
		return List.of(getMoonsrvJson("", Nazione[].class, queryParams));
		
	}

	public List<Nazione> findAll() throws DAOException {
		LOG.debug("[" + CLASS_NAME + "::findAll]");
		return List.of(getMoonsrvJson("", Nazione[].class));
	}
	
	/**
	 * 
	 * @param codice
	 * @return
	 */
	public Nazione findByPK(Integer codice) throws DAOException {
		LOG.debug("[" + CLASS_NAME + "::findByPK]");
		return getMoonsrvJson("/"+codice, Nazione.class);
	}


//TODO da migliorare aggiugendo nuovo servizio moonsrv con query by nome
	@Override
	public List<Nazione> findByNome(String nomeNazione) throws DAOException {
		List<Nazione> result = new ArrayList<>();
		for (Nazione naz : findAll()) {
			if (naz.getNome().toUpperCase().contains(nomeNazione.toUpperCase())) {
				result.add(naz);
				if (naz.getNome().equalsIgnoreCase(nomeNazione)) {
					return List.of(naz); // Array di un element
				}
			}
		}
		return result;
	}
	

}
