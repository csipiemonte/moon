/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio;

import java.util.List;

import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.territorio.svista.limamm.ente.cxfclient.Comune;
import it.csi.territorio.svista.limamm.ente.cxfclient.Provincia;
import it.csi.territorio.svista.limamm.ente.cxfclient.Regione;

public interface LimammDAO {

	//
	// REGIONI
    public List<Regione> getRegioni() throws DAOException;
    public Regione getRegioneById(Long idRegione) throws ItemNotFoundDAOException, DAOException;
    public Regione getRegioneByIstat(String istat) throws ItemNotFoundDAOException, DAOException;

	//
	// PROVINCE
	public List<Provincia> getProvince() throws DAOException;
	public Provincia getProvinciaById(Long idProvincia) throws ItemNotFoundDAOException, DAOException;
	public Provincia getProvinciaByIstat(String istat) throws ItemNotFoundDAOException, DAOException;
	public List<Provincia> getProvincePerIdRegione(Long idRegione) throws DAOException;
	
	//
	// COMUNI
	public List<Comune> getComuni() throws DAOException;
	public Comune getComuneById(Long idComune) throws ItemNotFoundDAOException, DAOException;
	public Comune getComuneByIstat(String istat) throws ItemNotFoundDAOException, DAOException;
	public List<Comune> getComuniPerIdProvincia(Long idProvincia) throws DAOException;
}
