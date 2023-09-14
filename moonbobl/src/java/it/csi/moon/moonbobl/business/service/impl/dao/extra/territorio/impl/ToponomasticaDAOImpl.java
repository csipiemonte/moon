/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.territorio.impl;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.component.MoonsrvTemplateImpl;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.territorio.ToponomasticaDAO;
import it.csi.moon.moonbobl.dto.extra.territorio.Civico;
import it.csi.moon.moonbobl.dto.extra.territorio.Piano;
import it.csi.moon.moonbobl.dto.extra.territorio.PianoNUI;
import it.csi.moon.moonbobl.dto.extra.territorio.Via;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipologieCivico;

@Component
@Qualifier("moonsrv")
public class ToponomasticaDAOImpl extends MoonsrvTemplateImpl implements ToponomasticaDAO {

	public ToponomasticaDAOImpl() {
	}

	@Override
	protected String getPathExtra() {
		return "/extra/territorio/toponomastica";
	}
	
	//
	// Entity Via
	//
	@Override
	public List<Via> getVie() throws DAOException {
		return getMoonsrvJson("/vie", List.class);
	}

	@Override
	public List<Via> getVie(int limit, int skip) throws DAOException {
		MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
		queryParams.add("limit", limit);
		queryParams.add("skip", skip);
		return getMoonsrvJson("/vie", List.class, queryParams);
	}
	
	@Override
	public Via getViaById(Integer codiceVia) throws DAOException {
		return getMoonsrvJson("/vie/"+codiceVia, Via.class);
	}
	
    //
    // Numeri
	//
	public List<Integer> getNumeriRadiceByVia(Integer codiceVia) throws DAOException {
		return getMoonsrvJson("/vie/"+codiceVia+"/numeri", List.class);
	}

    //
	// Entity Civico
	//
	@Override
	public List<Civico> getCiviciByVia(Integer codiceVia, DecodificaTipologieCivico tipologieCivico) throws DAOException {
		MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
		if (tipologieCivico!=null) {
			queryParams.add("tipologieCivico", tipologieCivico.getCodice());
		}
		return getMoonsrvJson("/vie/"+codiceVia+"/civici", List.class, queryParams);
	}
	
	@Override
	public Civico getCivicoById(Integer codiceVia, Integer codiceCivico) throws DAOException {
		return getMoonsrvJson("/vie/"+codiceVia+"/civici/"+codiceCivico, Civico.class);
	}
	
	@Override
	public List<Civico> getCiviciByViaNumero(Integer codiceVia, Integer numero, DecodificaTipologieCivico tipologieCivico) throws DAOException {
		MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
		if (tipologieCivico!=null) {
			queryParams.add("tipologieCivico", tipologieCivico.getCodice());
		}
		return getMoonsrvJson("/vie/"+codiceVia+"/numeri/"+numero+"/civici", List.class, queryParams);
	}
	
    //
	// Entity PianoNUI
	//
	@Override
	public List<PianoNUI> getPianiNuiByCivico(Integer codiceVia, Integer codiceCivico) throws DAOException {
		return getMoonsrvJson("/vie/"+codiceVia+"/civici/"+codiceCivico+"/pianinui", List.class);
	}
	@Override
	public PianoNUI getPianoNuiById(Integer codiceVia, Integer codiceCivico, Integer codicePianoNui) throws DAOException {
		return getMoonsrvJson("/vie/"+codiceVia+"/civici/"+codiceCivico+"/pianinui/"+codicePianoNui, PianoNUI.class);
	}
	
    //
	// Entity Piano
	//
	@Override
	public List<Piano> getPiani() throws DAOException {
		return getMoonsrvJson("/piani/", List.class);
	}
	
	@Override
	public Piano getPianoById(String codicePiano) throws DAOException {
		return getMoonsrvJson("/piani/"+codicePiano, Piano.class);
	}

}
