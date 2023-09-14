/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.component.MoonsrvTemplateImpl;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.NazioneDAO;
import it.csi.moon.moonbobl.dto.extra.demografia.Nazione;
import it.csi.moon.moonbobl.exceptions.business.DAOException;


/**
 *
 */
@Component
@Qualifier("moonsrv")
public class NazioneDAOImpl extends MoonsrvTemplateImpl implements NazioneDAO {

	/**
	 *
	 */
	public NazioneDAOImpl() {
	}


	@Override
	protected String getPathExtra() {
		return "/extra/demografia/nazioni";
	}


	/**
	 *
	 * @return
	 */
	public List<Nazione> findAll() throws DAOException {
		return getMoonsrvJson("", List.class);
	}


	/**
	 * 
	 * @param codice
	 * @return
	 */
	public Nazione findByPK(Integer codice) throws DAOException {
		return getMoonsrvJson("/"+codice, Nazione.class);
	}


//TODO da migliorare aggiugendo nuovo servizio moonsrv con query by nome
	@Override
	public Nazione findByNome(String nomeNazione) throws DAOException {
		for (Nazione naz : findAll()) {
			if (naz.getNome().equals(nomeNazione)) {
				return naz;
			}
		}
		return null;
	}
	

}
