/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica delle LogonMode.
 * 
 * @see LogonModeEntity
 * Tabella moon_ml_d_logon_mode
 * PK: idLogonMode
 * AK: codiceLogonMode
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 03/03/2021 - versione iniziale
 *
 */
public enum DecodificaLogonMode implements DecodificaMOON {

	NO_AUTH(1, "NO_AUTH", "Nessuna autenticazione"),
	LOGIN_PIN(2, "LOGIN_PIN", "Login/pinLogin/pin"),
	CF_DOCUMENTO(3, "CF_DOCUMENTO", "Codice fiscale/Numero documento"),
	GIA_AUTH(4, "GIA_AUTH", "Autenticazione effettuata"),
	CF_CI(5, "CF_CI", "Codice fiscale/Carta Identita"),
	LOGIN_PWD(6, "LOGIN_PWD", "Login User e Password"),
	GOOGLE(7, "GOOGLE", "Google"),
	;

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	private final String codice;
	private final String descrizione;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaLogonMode(Integer id, String codice, String descrizione) {
		this.id = id;
		this.codice = codice;
		this.descrizione = descrizione;
	}
	
	@Override
	public String getCodice() {
		return this.codice;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getDescrizione() {
		return this.descrizione;
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaLogonMode byCodice(String codice) {
		for(DecodificaLogonMode d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	public static DecodificaLogonMode byIdLogonMode(Integer idLogonMode) {
		for(DecodificaLogonMode d : values()) {
			if(d.getId().equals(idLogonMode)) {
				return d;
			}
		}
		return null;
	}
}
