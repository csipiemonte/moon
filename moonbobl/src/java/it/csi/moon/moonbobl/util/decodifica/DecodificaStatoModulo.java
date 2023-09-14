/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import java.util.Collections;
import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica dello StatoModelo.
 * @author Laurent Pissard
 * @version 1.0.0 - 05/12/2019 - versione iniziale
 * @version 2.0.0 - 12/01/2020 - versioning del modulo
 *
 */
public enum DecodificaStatoModulo implements DecodificaMOON {

	/** In Costruzione */
	IN_COSTRUZIONE(1, "INIT", "IN COSTRUZIONE", false, false
		, Collections.unmodifiableList(Collections.emptyList())
		, Collections.unmodifiableList(List.of("TST"))),
	/** In Test */
	IN_TEST(4, "TST", "IN TEST", false, true
		, Collections.unmodifiableList(List.of("INIT"))
		, Collections.unmodifiableList(List.of("ELI","PUB"))),
	/** In Modifica */
	IN_MODIFICA(8, "MOD", "IN MODIFICA", false, false
		, Collections.unmodifiableList(List.of("SOSP","PUB"))
		, Collections.unmodifiableList(List.of("TST"))),
	/** In Modifica */
	SOSPESO(12, "SOSP", "SOSPESO TEMPORANEAMENTE", false, true
		, Collections.unmodifiableList(List.of("PUB"))
		, Collections.unmodifiableList(List.of("ELI","RIT","MOD","PUB"))),
	/** In Modifica */
	PUBBLICATO(40, "PUB", "PUBBLICATO", true, true
		, Collections.unmodifiableList(List.of("TST","SOSP"))
		, Collections.unmodifiableList(List.of("ELI","RIT","MOD","SOSP"))),
	/** Ritirato da Pubblicazione */
	RITIRATO_DA_PUBBLICAZIONE(-5, "RIT", "RITIRATO DA PUBBLICAZIONE", false, false
		, Collections.unmodifiableList(List.of("SOSP","PUB"))
		, Collections.unmodifiableList(List.of("ELI"))),
	/** Eliminato */
	ELIMINATO(-10, "ELI","ELIMINATO", false, false
		, Collections.unmodifiableList(List.of("RIT","TST","SOSP","PUB"))
		, Collections.unmodifiableList(Collections.emptyList()))
	;

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	private final String codice;
	private final String descrizione;
	private final boolean compilabile;
	private final boolean compilabileTest;
	private final List<String> statiProvenienza;
	private final List<String> statiDestinazione;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaStatoModulo(Integer id, String codice, String descrizione, boolean compilabile, boolean compilabileTest, 
			List<String> statiProvenienza, List<String> statiDestinazione) {
		this.id = id;
		this.codice = codice;
		this.descrizione = descrizione;
		this.compilabile = compilabile;
		this.compilabileTest = compilabileTest;
		this.statiProvenienza = statiProvenienza;
		this.statiDestinazione = statiDestinazione;
	}
	
	@Override
	public String getCodice() {
		return codice;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public boolean isCompilabile() {
		return compilabile;
	}
	public boolean isCompilabileTest() {
		return compilabileTest;
	}
	
	public List<String> getStatiProvenienza() {
		return statiProvenienza;
	}
	
	public List<String> getStatiDestinazione() {
		return statiDestinazione;
	}
	
	/**
	 * Ottiene la decodifica a partire dal ID.
	 * 
	 * @param id identificativo da cercare
	 * @return la codifica corrispondente al id, se presente altrimenti null
	 */
	public static DecodificaStatoModulo byId(Integer idStatoModulo) {
		for(DecodificaStatoModulo d : values()) {
			if(d.getId().equals(idStatoModulo)) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaStatoModulo byCodice(String codice) {
		for(DecodificaStatoModulo d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
	
	public boolean isCorrectId(Integer idDaConfrontare) {
		if(this.id.equals(idDaConfrontare)) {
			return true;
		}
		return false;
	}
	public boolean isCorrectId(ModuloVersionatoEntity moduloVersionato) {
		return isCorrectId(moduloVersionato.getIdStato());
	}
	
}
