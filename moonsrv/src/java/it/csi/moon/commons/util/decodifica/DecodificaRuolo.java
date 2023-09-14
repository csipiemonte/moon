/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.entity.EnteAreaRuoloFlatDTO;
import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica dei Ruoli, corrispondente alla tabella (moon_fo_d_ruolo)
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 09/07/2020 - versione iniziale
 */
public enum DecodificaRuolo implements DecodificaMOON {

	/** 1 - ADMIN */
	ADMIN(1, "ADMIN", "Amministratore del sistema","Utente con pieni privilegi su tutte le funzioni e su tutti gli enti"),
	/** 2 - RESP */
	RESP(2, "RESP", "Responsabile di un ente","Responsabile di un ente che può agire su tutte le funzioni delle istanze e dei moduli e può configurare gli utenti"),
	/** 3 - OP_ADV */
	OP_ADV(3, "OP_ADV", "Operatore avanzato","Operatore che agisce su istanze e configurazioni"),
	/** 4 - OP_SIMP */
	OP_SIMP(4, "OP_SIMP", "Operatore semplice","Utente che può gestire le istanze ma non agisce sulle configurazioni"),
	/** 5 - BLDR_ADV */
	BLDR_ADV(5, "BLDR_ADV", "Modellatore avanzato","tente abilitato a creare moduli e a gestire la pubblicazione"),
	/** 6 - BLDR_SIMP */
	BLDR_SIMP(6, "BLDR_SIMP", "Modellatore semplice","Utente abilitato a creare moduli"),
	/** 7 - OP_BLDR */
	OP_BLDR(7, "OP_BLDR", "Operatore modifica Moduli","Operatore modifica Moduli"),
	/** 8 - FO_SIMP */
	FO_SIMP(8, "FO_SIMP", "Compilatore semplice", "Utente abilitato a compilare modulo da FO"),
	/** 8 - FO_SIMP */
	OP_ADM(9, "OP_ADM", "Operatore amministratore", "Operatore che accede a tutte le operazioni di amministrazione"),
	/** 8 - FO_SIMP */
	OP_CON(10, "OP_CON", "Operatore di consultazione", "Operatore con permessi di sola lettura"),
	;
		
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	private final String codice;
	private final String nome;
	private final String descrizione;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaRuolo(Integer id, String codice, String nome, String descrizione) {
		this.id = id;
		this.codice = codice;
		this.nome = nome;
		this.descrizione = descrizione;
	}
	
	@Override
	public String getCodice() {
		return codice;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaRuolo byCodice(String codice) {
		for(DecodificaRuolo d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
	public static DecodificaRuolo byId(Integer id) {
		for(DecodificaRuolo d : values()) {
			if(d.id.equals(id)) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Confronta con UtenteEntity
	 */
	public boolean isCorrectRuolo(Integer idRuolo) {
		if (idRuolo==null) return false;
		if (getId().equals(idRuolo))
			return true;
		return false;
	}
	public boolean isCorrectRuolo(EnteAreaRuoloFlatDTO enteAreaRuoloFlat) {
		if (enteAreaRuoloFlat==null) return false;
		if (getId().equals(enteAreaRuoloFlat.getIdRuolo()))
			return true;
		return false;
	}
	
	public boolean isCorrectRuoloByCodice(EnteAreaRuoloFlatDTO enteAreaRuoloFlat) {
		if (enteAreaRuoloFlat==null) return false;
		if (getCodice().equals(enteAreaRuoloFlat.getCodiceRuolo()))
			return true;
		return false;
	}
}
