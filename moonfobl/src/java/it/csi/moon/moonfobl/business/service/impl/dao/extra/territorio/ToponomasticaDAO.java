/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.territorio;

import java.util.List;

import it.csi.moon.commons.dto.extra.territorio.Civico;
import it.csi.moon.commons.dto.extra.territorio.Piano;
import it.csi.moon.commons.dto.extra.territorio.PianoNUI;
import it.csi.moon.commons.dto.extra.territorio.UiuCivicoIndirizzo;
import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.commons.util.decodifica.DecodificaTipologieCivico;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;


public interface ToponomasticaDAO {
	
	//
	// Entity Via
	public List<Via> getVie() throws DAOException;
	public List<Via> getVie(int limit, int skip, String filter) throws DAOException;
	public Via getViaById(Integer codiceVia) throws ItemNotFoundBusinessException, DAOException;
	
    //
    // Numero Radici
	public List<Integer> getNumeriRadiceByVia(Integer codiceVia) throws DAOException;
    
    //
	// Entity Civico
	public List<Civico> getCiviciByVia(Integer codiceVia, DecodificaTipologieCivico tipologieCivico, String stati) throws DAOException;
	public Civico getCivicoById(Integer codiceVia, Integer codiceCivico) throws ItemNotFoundBusinessException, DAOException;
	public List<Civico> getCiviciByViaNumero(Integer codiceVia, Integer numero, DecodificaTipologieCivico tipologieCivico, String stati) throws DAOException;
    
    //
	// Entity PianoNUI
	public List<PianoNUI> getPianiNuiByCivico(Integer codiceVia, Integer codiceCivico) throws DAOException;
	public PianoNUI getPianoNuiById(Integer codiceVia, Integer codiceCivico, Integer codicePianoNUI) throws ItemNotFoundBusinessException, DAOException;
	
    //
	// Entity Piano
	public List<Piano> getPiani() throws DAOException;
	public Piano getPianoById(String codicePiano) throws ItemNotFoundBusinessException, DAOException;
	public List<Piano> getPianiByCivico(Integer codiceVia, Integer codiceCivico) throws DAOException;

	//
	// Catasto
	public List<UiuCivicoIndirizzo> getUiuCivicoIndirizzoByTriplettaCatastale(Integer foglio, Integer numero, Integer subalterno) throws DAOException;
}
