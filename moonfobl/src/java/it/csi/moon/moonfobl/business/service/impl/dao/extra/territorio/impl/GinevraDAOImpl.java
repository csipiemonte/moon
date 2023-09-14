/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.territorio.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonfobl.business.service.impl.dao.component.MoonsrvTemplateImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.territorio.GinevraDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

@Component
@Qualifier("moonsrv")
public class GinevraDAOImpl extends MoonsrvTemplateImpl implements GinevraDAO {

	public GinevraDAOImpl() {
	}

	@Override
	protected String getPathExtra() {
		return "/extra/territorio/ginevra";
	}
	
	
	//
	// SEDIMI
	@Override
    public List<Via> getSedimi() throws DAOException {
		return getMoonsrvJson("/sedimi", List.class);
	}
    
	@Override
    public Via getSedimeById(Long idSedime) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/sedimi/"+idSedime, Via.class);
	}
	
	//
	// VIE
	@Override
    public List<Via> getVie(Long idComune, String nomeVia) throws DAOException {
		return getMoonsrvJson("/comuni/"+idComune+"/vie?nomeVia="+nomeVia, List.class);
	}
	
	@Override
    public Via getViaById(Long idComune, Long idVia) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/comuni/"+idComune+"/vie/"+idVia, Via.class);
	}
	
	//
	// CIVICI
	@Override
    public List<Via> getCivici(Long idL2, Long numero) throws DAOException {
		return getMoonsrvJson("/civici/idL2/"+idL2+"/numero/"+numero, List.class);
	}
	
	@Override
    public Via getCivicoById(Long idCivico) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/civici/"+idCivico, Via.class);
	}
    	
}
