/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util;

/**
 * Contenitore per le chiavi della mappa dei parametri.
 * <br/>
 * Contiene dei metodi per validazione formale di tipo.
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 20/02/2020 - versione iniziale
 *
 */
public interface MapKeyHolder {
	/**
	 * La chiave da usare nella mappa
	 * 
	 * @return la chiave
	 */
	public String getKey();
	
	/**
	 * Controlla se l'oggetto fornito sia di tipo formalmente corretto.
	 * 
	 * @param obj l'oggetto da controllare
	 * @return <code>true</code> se l'oggetto &eacute; assegnabile al tipo della chiave; <code>false</code> altrimenti
	 */
	public boolean isCorrectType(Object obj);
	
	/**
	 * Controlla se l'oggetto fornito sia di tipo formalmente corretto, o <code>null</code>.
	 * 
	 * @param obj l'oggetto da controllare
	 * @return <code>true</code> se l'oggetto &eacute; <code>null</code> o assegnabile al tipo della chiave; <code>false</code> altrimenti
	 */
	public boolean isNullOrCorrectType(Object obj);
	
}
