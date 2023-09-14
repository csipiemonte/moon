/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util;

/**
 * Decodifica dei attributi di configurazione per la pubblicazione su MyDocs (DOCME) presenti nella tabella moon_md_d_parametri.
 * La conversione dei tipi è gestita nella creazione della Mappa (MapMyDocsAttributi)
 * 
 * @see MapMyDocsAttributi
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 07/02/2023 - versione iniziale
 */
public enum MyDocsAttributoKeys implements MapKeyHolder {

	/** IdAmbito                             Type String */
	ID_AMBITO(String.class, 1),
	/** DenominazioneAmbito                  Type String */
	AMBITO(String.class, 2),
	/** IdTipologia                          Type String */
	ID_TIPOLOGIA_ISTANZA(String.class, 3),
	/** DescrizioneTipologia                 Type String */
	TIPOLOGIA_ISTANZA(String.class, 4),
	;
	
	private String key;
	private Class<?> type;
	private Integer order;
	
	private MyDocsAttributoKeys(Class<?> type, Integer order) {
		this.type = type;
		this.key = /*this.getClass().getSimpleName() + "_KEY_" +*/ this.name();
		this.order = order;
	}

	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static MyDocsAttributoKeys byName(String nome) {
		for(MyDocsAttributoKeys k : values()) {
			if(k.name().equals(nome)) {
				return k;
			}
		}
		return null;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}
	public Class<?> getType() {
		return this.type;
	}
	public Integer getOrder() {
		return this.order;
	}
	
	@Override
	public boolean isCorrectType(Object obj) {
		return type.isInstance(obj);
	}

	@Override
	public boolean isNullOrCorrectType(Object obj) {
		return obj == null || type.isInstance(obj);
	}

}
