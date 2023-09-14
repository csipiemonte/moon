/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.territorio.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.territorio.regp.ComuneLA;
import it.csi.moon.commons.dto.extra.territorio.regp.ProvinciaLA;
import it.csi.moon.commons.dto.extra.territorio.regp.RegioneLA;
import it.csi.moon.moonfobl.business.service.impl.dao.component.MoonsrvTemplateImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.territorio.LimammDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

@Component
@Qualifier("moonsrv")
public class LimammDAOImpl extends MoonsrvTemplateImpl implements LimammDAO {

	public LimammDAOImpl() {
	}

	@Override
	protected String getPathExtra() {
		return "/extra/territorio/limamm";
	}
	
	//
	// REGIONI
	@Override
	public List<RegioneLA> getRegioni() throws DAOException {
		return getMoonsrvJson("/regioni", List.class);
	}

	@Override
	public RegioneLA getRegioneById(Long idRegione) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/regioni/"+idRegione, RegioneLA.class);
	}

	@Override
	public RegioneLA getRegioneByIstat(String istat) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/regioni/istat/"+istat, RegioneLA.class);
	}

	//
	// PROVINCE
	@Override
	public List<ProvinciaLA> getProvince() throws DAOException {
		return getMoonsrvJson("/province", List.class);
	}

	@Override
	public ProvinciaLA getProvinciaById(Long idProvincia) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/province/"+idProvincia, ProvinciaLA.class);
	}

	@Override
	public ProvinciaLA getProvinciaByIstat(String istat) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/province/istat/"+istat, ProvinciaLA.class);
	}

	@Override
	public List<ProvinciaLA> getProvinceByRegione(Long idRegione) throws DAOException {
		return getMoonsrvJson("/regioni/"+idRegione+"/province", List.class);
	}

	@Override
	public List<ProvinciaLA> getProvinceByIstatRegione(String istatRegione) throws DAOException {
		return getMoonsrvJson("/regioni/istat/"+istatRegione+"/province", List.class);
	}

	@Override
	public ProvinciaLA getProvinciaByRegione(Long idRegione, Long idProvincia) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/regioni/"+idRegione+"/province/"+idProvincia, ProvinciaLA.class);
	}

	@Override
	public ProvinciaLA getProvinciaByIstatRegione(String istatRegione, String istatProvincia) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/regioni/istat/"+istatRegione+"/province/istat/"+istatProvincia, ProvinciaLA.class);
	}
	
	//
	// COMUNI
	@Override
	public List<ComuneLA> getComuni() throws DAOException {
		return getMoonsrvJson("/comuni", List.class);
	}

	@Override
	public ComuneLA getComuneById(Long idComune) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/comuni/"+idComune, ComuneLA.class);
	}

	@Override
	public ComuneLA getComuneByIstat(String istat) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/comuni/istat/"+istat, ComuneLA.class);
	}

	@Override
	public List<ComuneLA> getComuniByProvincia(Long idProvincia) throws DAOException {
		return getMoonsrvJson("/province/"+idProvincia+"/comuni", List.class);
	}

	@Override
	public List<ComuneLA> getComuniByIstatProvincia(String istatProvincia) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/province/istat/"+istatProvincia+"/comuni", List.class);
	}

	@Override
	public List<ComuneLA> getComuniByRegioneProvincia(Long idRegione, Long idProvincia) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/regioni/"+idRegione+"/province/"+idProvincia+"/comuni", List.class);
	}

	@Override
	public List<ComuneLA> getComuniByIstatRegioneProvincia(String istatRegione, String istatProvincia) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/regioni/istat/"+istatRegione+"/province/istat/"+istatProvincia+"/comuni", List.class);
	}

	@Override
	public ComuneLA getComuneByRegioneProvincia(Long idRegione, Long idProvincia, Long idComune) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/regioni/"+idRegione+"/province/"+idProvincia+"/comuni/"+idComune, ComuneLA.class);
	}

	@Override
	public ComuneLA getComuneByIstatRegioneProvincia(String istatRegione, String istatProvincia, String istatComune) throws ItemNotFoundDAOException, DAOException {
		return getMoonsrvJson("/regioni/istat/"+istatRegione+"/province/istat/"+istatProvincia+"/comuni/istat/"+istatComune, ComuneLA.class);
	}
	
}
