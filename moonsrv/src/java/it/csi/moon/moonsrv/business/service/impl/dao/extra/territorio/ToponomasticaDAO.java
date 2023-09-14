/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio;

import java.util.ArrayList;
import java.util.List;

import it.csi.moon.commons.dto.extra.territorio.Civico;
import it.csi.moon.commons.dto.extra.territorio.PianoNUI;
import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
* DAO Toponomastica del Comune di Torino via servizi Mock 
* Usare ToponomasticaApiRestDAO per i oggetti della Fonte Toponomastica
* Meglio, usare i ToponomasticaService con @Qualifier("RS") che torna oggetti Moon (impl: ToponomasticaServiceRSImpl)
*  
* @author Francesco Zucaro
* @author Laurent Pissard
* 
* @since 1.0.0
*/

public interface ToponomasticaDAO {

	//
	// Entity Via
	public List<Via> getVie() throws DAOException;
	public List<Via> getVie(int limit, int skip) throws DAOException;
	public Via getViaByPK(Integer codiceVia) throws DAOException;
	public Via findVieByDesc(String descVia) throws DAOException;
	
    //
    // Numero Radici
	
    //
	// Entity Civico
	public ArrayList<Civico> findCiviciByCodiceVia(Integer codiceVia) throws DAOException;
	public Civico findCivicoByPK(Integer codiceVia, Integer codiceCivico) throws DAOException;

    //
	// Entity PianoNUI
	public ArrayList<PianoNUI> findPianiNUIByCivico(Integer codiceVia, Integer codiceCivico) throws DAOException;
	
    //
	// Entity Piano


}
