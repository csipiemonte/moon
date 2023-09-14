/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia;


import java.util.List;

import it.csi.moon.commons.dto.extra.demografia.RelazioneParentela;


/**
 * Servizio di esposizione dei dati Relazioni Parentela di ANPR
 * Fonte dati ANPR (fonte Istat) : https://www.anpr.interno.it/portale/documents/20182/50186/tabella_5_relazioni_parentela.xlsx
 * Ultimo aggiornamento ANPR dal 20 gennaio 2017 con aggiunta di "Unito civilmente"
 * Dati locali /dem-anpr-relazioni_parentela.json 
 * 
 * Presenti 26 rows su 30
 * Escluse le seguente 4 valori :
 *   24	Responsabile della convivenza non affettiva
 *   26	Tutore
 *   99	Non definito/comunicato
 *   80	Adottato
 *
 * @author Laurent Pissard
 */
public interface AnprRelazioniParentelaDAO {

	/**
	 *
	 * @return
	 */
	public List<RelazioneParentela> findAll();
	
	/**
	 * 
	 * @param codice
	 * @return
	 */
	public RelazioneParentela findByPK(Integer codice);

}
