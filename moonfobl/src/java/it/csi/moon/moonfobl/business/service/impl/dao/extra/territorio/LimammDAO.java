/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.territorio;

import java.util.List;

import it.csi.moon.commons.dto.extra.territorio.regp.ComuneLA;
import it.csi.moon.commons.dto.extra.territorio.regp.ProvinciaLA;
import it.csi.moon.commons.dto.extra.territorio.regp.RegioneLA;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;


public interface LimammDAO {

	//
	// REGIONI
    public List<RegioneLA> getRegioni() throws DAOException;
    public RegioneLA getRegioneById(Long idRegione) throws ItemNotFoundDAOException, DAOException;
    public RegioneLA getRegioneByIstat(String istat) throws ItemNotFoundDAOException, DAOException;

	//
	// PROVINCE
	public List<ProvinciaLA> getProvince() throws DAOException;
	public ProvinciaLA getProvinciaById(Long idProvincia) throws ItemNotFoundDAOException, DAOException;
	public ProvinciaLA getProvinciaByIstat(String istat) throws ItemNotFoundDAOException, DAOException;
	public List<ProvinciaLA> getProvinceByRegione(Long idRegione) throws DAOException;
	public List<ProvinciaLA> getProvinceByIstatRegione(String istatRegione) throws DAOException;
	public ProvinciaLA getProvinciaByRegione(Long idRegione, Long idProvincia) throws ItemNotFoundDAOException, DAOException;
	public ProvinciaLA getProvinciaByIstatRegione(String istatRegione, String istatProvincia) throws ItemNotFoundDAOException, DAOException;
	
	//
	// COMUNI
	public List<ComuneLA> getComuni() throws DAOException;
	public ComuneLA getComuneById(Long idComune) throws ItemNotFoundDAOException, DAOException;
	public ComuneLA getComuneByIstat(String istat) throws ItemNotFoundDAOException, DAOException;
	public List<ComuneLA> getComuniByProvincia(Long idProvincia) throws DAOException;
	public List<ComuneLA> getComuniByIstatProvincia(String istatProvincia) throws ItemNotFoundDAOException, DAOException;
	public List<ComuneLA> getComuniByRegioneProvincia(Long idRegione, Long idProvincia) throws ItemNotFoundDAOException, DAOException;
	public List<ComuneLA> getComuniByIstatRegioneProvincia(String istatRegione, String istatProvincia) throws ItemNotFoundDAOException, DAOException;
	public ComuneLA getComuneByRegioneProvincia(Long idRegione, Long idProvincia, Long idComune) throws ItemNotFoundDAOException, DAOException;
	public ComuneLA getComuneByIstatRegioneProvincia(String istatRegione, String istatProvincia, String istatComune) throws ItemNotFoundDAOException, DAOException;
	
}
