/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.entity.ModuloClassEntity;
import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica delle cache
 * 
 * @version 1.0.0 - 20/05/2022 - versione iniziale
 */
public enum DecodificaMoonCache implements DecodificaMOON {

	/** 1 - ADMIN */
	MODULO_CLASS("Modulo Class", String.class, ModuloClassEntity.class),
	DUMMY("Dummy cache", String.class, Object.class),
	;
		
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final String nome;
	private final Class  key;
	private final Class valueClass;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}

	private <T,E> DecodificaMoonCache(String nome, Class key, Class<E> valueClass) {
		this.nome = nome;
		this.key = key;
		this.valueClass = valueClass;
	}
	
	@Override
	public String getCodice() {
		return name();
	}
	
	public String getNome() {
		return nome;
	}
	
	public Class getKey() {
		return key;
	}
	
	public Class getValueClass() {
		return valueClass;
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaMoonCache byCodice(String codice) {
		for(DecodificaMoonCache d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
}
