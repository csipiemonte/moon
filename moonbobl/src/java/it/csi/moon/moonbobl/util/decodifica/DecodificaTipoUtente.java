/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.dto.moonfobl.TipoUtente;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica di TipoUtente, corrispondente alla tabella (moon_fo_d_tipo_utente)
 * Ancora da capire l'utilizzo e utilita, probabilmente ancora nessun filtro su quest informazione
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 20/05/2020 - versione iniziale
 */
public enum DecodificaTipoUtente implements DecodificaMOON {
	
	/** 1 - ADM */
	ADM(1, "ADM", "utente admin"),
	/** 2 - PA */
	PA(2, "PA", "utente di backoffice"),
	/** 3 - RUP */
	RUP(3, "RUP", "utente rupar di frontoffice"),
	/** 4 - CIT */
	CIT(4, "CIT", "utente internet di frontoffice"),
	;

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	private final String codice;
	private final String descrizione;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipoUtente(Integer id, String codice, String descrizione) {
		this.id = id;
		this.codice = codice;
		this.descrizione = descrizione;
	}
	
	@Override
	public String getCodice() {
		return codice;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public Integer getId() {
		return id;
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaTipoUtente byCodice(String codice) {
		for(DecodificaTipoUtente d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
	public static DecodificaTipoUtente byId(Integer id) {
		for(DecodificaTipoUtente d : values()) {
			if(d.id.equals(id)) {
				return d;
			}
		}
		return null;
	}
	
	
	/**
	 * Confronta con UtenteEntity
	 */
	public boolean isCorrectType(UtenteEntity utente) {
		if (utente==null) return false;
		if (getId().equals(utente.getIdTipoUtente()))
			return true;
		return false;
	}

	public boolean isCorrectType(TipoUtente tipoUtente) {
		if (tipoUtente==null) return false;
		if (getCodice().equals(tipoUtente.getCodice()))
			return true;
		return false;
	}

	public boolean isCorrectType(UserInfo user) {
		if (user==null) return false;
		return isCorrectType(user.getTipoUtente());
	}
}
