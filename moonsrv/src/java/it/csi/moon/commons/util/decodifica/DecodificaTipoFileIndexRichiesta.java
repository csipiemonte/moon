/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

public enum DecodificaTipoFileIndexRichiesta implements DecodificaMOON {
	
	PDF_ISTANZA(1,"PDF_ISTANZA"),
	ALLEGATO_ISTANZA(2,"ALLEGATO_ISTANZA"),
	FILE_ALLEGATO_RISPOSTA_INTEGRAZIONE(3,"FILE_ALLEGATO_RISPOSTA_INTEGRAZIONE")
	;

	public static final String VALORI_POSSIBILI;
	private final Integer id;
	private final String codiceTipologia;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipoFileIndexRichiesta(Integer id,String codiceTipologia) {
		this.id = id;
		this.codiceTipologia = codiceTipologia;
	}
	
	@Override
	public String getCodice() {
		return id.toString();
	}
	
	public Integer getId() {
		return id;
	}
	public String getCodiceTipologia() {
		return codiceTipologia;
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaTipoFileIndexRichiesta byCodice(String codice) {
		for(DecodificaTipoFileIndexRichiesta d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
	public static DecodificaTipoFileIndexRichiesta byId(Integer id) {
		for(DecodificaTipoFileIndexRichiesta d : values()) {
			if(d.id.equals(id)) {
				return d;
			}
		}
		return null;
	}
	public static DecodificaTipoFileIndexRichiesta byCodiceTipologia(String codiceTipologia) {
		for(DecodificaTipoFileIndexRichiesta d : values()) {
			if(d.getCodiceTipologia().equals(codiceTipologia)) {
				return d;
			}
		}
		return null;
	}

}
