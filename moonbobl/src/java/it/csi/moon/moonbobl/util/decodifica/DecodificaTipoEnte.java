/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.business.service.impl.dto.EnteAreaRuoloFlatDTO;
import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica di TipoEnte, corrispondente alla tabella (moon_fo_d_tipoente)
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 09/07/2020 - versione iniziale
 */
public enum DecodificaTipoEnte implements DecodificaMOON {

	/** REGIONE - ENTE REGIONE */
	REGIONE(1, "REGIONE", "ENTE REGIONE"),
	/** COMUNE - ENTE COMUNALE */
	COMUNE(2, "COMUNE", "ENTE COMUNALE"),
	/** PROV - PROVINCIA */
	PROVINCIA(3, "PROV", "PROVINCIA"),
	/** GEST - GESTORE (CSI PIEMONTE) */
	GESTORE(4, "GESTORE", "ENTE GESTORE"),
	/** CAF - CAF */
	CAF(5, "CAF", "CAF"),
	;

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	private final String codice;
	private final String nome;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipoEnte(Integer id, String codice, String nome) {
		this.id = id;
		this.codice = codice;
		this.nome = nome;
	}
	
	@Override
	public String getCodice() {
		return codice;
	}
	
	public String getNome() {
		return nome;
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
	public static DecodificaTipoEnte byCodice(String codice) {
		for(DecodificaTipoEnte d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
	public static DecodificaTipoEnte byId(Integer id) {
		for(DecodificaTipoEnte d : values()) {
			if(d.id.equals(id)) {
				return d;
			}
		}
		return null;
	}
	
	
	/**
	 * Confronta con UtenteEntity
	 */
	public boolean isCorrectType(Integer idTipoEnte) {
		if (idTipoEnte==null) return false;
		if (getId().equals(idTipoEnte))
			return true;
		return false;
	}
	public boolean isCorrectType(EnteAreaRuoloFlatDTO enteAreaRuoloFlat) {
		if (enteAreaRuoloFlat==null) return false;
		if (getId().equals(enteAreaRuoloFlat.getIdTipoEnte()))
			return true;
		return false;
	}
}
