/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio;

import java.util.List;

import it.csi.moon.commons.dto.extra.territorio.Civico;
import it.csi.moon.commons.dto.extra.territorio.Piano;
import it.csi.moon.commons.dto.extra.territorio.PianoNUI;
import it.csi.moon.commons.dto.extra.territorio.UiuCivicoIndirizzo;
import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Metodi di business della Toponomastica di Torino
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ToponomasticaService {
	
    //
	// Entity Via
	public List<Via> getVie() throws BusinessException;
	public List<Via> getVie(String filter) throws BusinessException;
	public List<Via> getVie(Integer limit, Integer skip, String filter) throws BusinessException;
	public Via getViaById(Integer codiceVia) throws BusinessException;
	public Via findViaByDesc(String descVia) throws BusinessException;
	public List<Via> findVieByDesc(String descVia) throws BusinessException;
	
    //
    // Integer - Numero Radici
	public List<Integer> getNumeriRadiceByVia(Integer codiceVia) throws BusinessException;
	
    //
	// Entity Civico
	public List<Civico> getCiviciByVia(Integer codiceVia, String tipologieCivico, List<Integer> stati) throws BusinessException;
	public List<Civico> getCiviciByViaNumero(Integer codiceVia, Integer numero, String tipologieCivico, List<Integer> stati) throws BusinessException;
	public Civico getCivicoById(Integer codiceVia, Integer codiceCivico) throws BusinessException;
	public it.csi.apimint.toponomastica.v1.dto.Civico getCivicoById(Integer codiceCivico) throws BusinessException;
	
    //
	// Entity PianoNUI
	public List<PianoNUI> getPianiNuiByCivico(Integer codiceVia, Integer codiceCivico) throws BusinessException;
	public PianoNUI getPianoNuiById(Integer codiceVia, Integer codiceCivico, Integer nui) throws BusinessException;
	
    //
	// Entity Piano
	public List<Piano> getPiani() throws BusinessException;
	public Piano getPianoById(String codicePiano) throws BusinessException;
	public List<Piano> getPianiByCivico(Integer codiceVia, Integer codiceCivico) throws BusinessException;
	
	//
	// Catasto
	public List<UiuCivicoIndirizzo> getUiuCivicoIndirizzoByTriplettaCatastale(String foglio, String numero, String subalterno) throws BusinessException;
	
}
