/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio;

import java.util.List;

import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.territorio.ginevra.lanci.civico.cxfclient.Civico;
import it.csi.territorio.ginevra.lanci.sedime.cxfclient.Sedime;
import it.csi.territorio.ginevra.lanci.via.cxfclient.ViaComunale;
import it.csi.territorio.ginevra.lanci.via.cxfclient.ViaSovracomunale;


public interface GinevraDAO {

	//
	// SEDIMI
    public List<Sedime> getSedimi() throws DAOException;
    public Sedime getSedimeById(Long idTipoVia) throws DAOException;

	//
	// VIE
    public List<ViaComunale> getViePerNomeContiene(String nome, Long idComune) throws DAOException;
    public ViaComunale getViaByIdIndirizzoComunale(Long idVia) throws DAOException;
	public ViaSovracomunale getViaByIdIndirizzoSovracomunale(Long idVia) throws DAOException;
	
	//
	// CIVICI
    public List<Civico> getCivici(Long idL2, Long numero) throws DAOException;
    public Civico getCivicoById(Long idCivico) throws DAOException;

}
