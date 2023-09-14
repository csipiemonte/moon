/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.dto.MoonError;
import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica dei errori
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 17/02/2022 - versione iniziale
 */
public enum DecodificaError implements DecodificaMOON {

	MOONSRV_00400_EMAIL_GENERICA("MOONSRV-00400", "Errore generica di invio email", null),
	MOONSRV_00401_EMAIL_MESSAGING("MOONSRV-00401", "Errore messaging su invio email", null), 
	MOONSRV_00402_EMAIL_INIT_FROMTOSUBJECT("MOONSRV-00402", "Errore initMessage FromToSubject email", null),
	MOONSRV_10050_PRINT_ISTANZA_MAPPER("MOONSRV-10050", "REMAP Impossible generare il json per moonprint", null),
	MOONSRV_10051_IMPOSSIBLE_GENERARE_STAMPA_PDF("MOONSRV-10051", "Impossible generare la Stampa PDF", null), 
	MOONSRV_10052_NOT_VALID_PDF("MOONSRV-10052", "NOT_VALID_PDF", null),
	MOONSRV_10053_NOT_FOUND("MOONSRV-10053", "REMAP_NOT_FOUND", null),
	MOONSRV_10054_DAO("MOONSRV-10054", "REMAP_DAO", null),
	;
		
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final String code;
	private final String message;
	private final String title;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaError(String code, String message, String title) {
		this.code = code;
		this.message = message;
		this.title = title;
	}
	
	@Override
	public String getCodice() {
		return code;
	}
	
	public MoonError getError() {
		return new MoonError(this.message, this.code, this.title);
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getTitle() {
		return title;
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaError byCodice(String codice) {
		for(DecodificaError d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}



}
