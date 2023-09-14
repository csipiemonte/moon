/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.moon.commons.entity.ModuloAttributoEntity;

/**
 * Mappa degli attributi di un Modulo tipizzato
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 20/02/2020 - versione iniziale
 *
 */
public class MapModuloAttributi {

	public static Map<String, Object> EMPTY_MAP = new HashMap<>();
	private Map<String, Object> attributiMap = new HashMap<>();

	public MapModuloAttributi() {
		super();
	}
	public MapModuloAttributi(List<ModuloAttributoEntity> listAttributi) {
		super();
		if (listAttributi != null && !listAttributi.isEmpty()) {	
			listAttributi.forEach( mae -> {
				ModuloAttributoKeys key = ModuloAttributoKeys.byName(mae.getNomeAttributo());
				if (String.class.equals(key.getType())) {
					String valueString = (String) mae.getValore();
					put(key, valueString);
				} else if (Boolean.class.equals(key.getType())) {
					Boolean valBoolean = "S".equals(mae.getValore())?Boolean.TRUE:
						("N".equals(mae.getValore())?Boolean.FALSE:null);
					put(key, valBoolean);
				}
			});
		}
	}
	
	
	/**
	 * Aggiungi un element nella mappa
	 * @param key di tipo ModuloAttributoKeys
	 * @param obj del tipo definito nella chiave ModuloAttributoKeys.type
	 */
	public void put(MapKeyHolder key, Object obj) {
		this.attributiMap.put(key.getKey(), obj);
	}
	
	
	/**
	 * Controlla se il dato sia contenuto con il tipo corretto.
	 * 
	 * @param holder il contenitore della chiave
	 * @return <code>true</code> se il valore corrispondente alla chiave &eacute; presente e di tipo corretto; <code>false</code> altrimenti
	 */
	public boolean containsWithCorrectType(MapKeyHolder holder) {
		return attributiMap.containsKey(holder.getKey()) && holder.isCorrectType(attributiMap.get(holder.getKey()));
	}
	
	/**
	 * Ottiene il valore con il corretto tipo
	 * 
	 * @param holder il contenitore della chiave
	 * @return l'oggetto correttamente castato, se presente e di tipo corretto; <code>false</code> altrimenti
	 */
	@SuppressWarnings("unchecked")
	public <T> T getWithCorrectType(MapKeyHolder holder) {
		if(!containsWithCorrectType(holder)) {
			return null;
		}
		return (T) attributiMap.get(holder.getKey());
	}
	
	
	/**
	 * Ottiene il valore con il corretto tipo
	 * 
	 * @param holder il contenitore della chiave
	 * @param clazz la classe del risultato
	 * @return l'oggetto correttamente castato, se presente e di tipo corretto; <code>false</code> altrimenti
	 */
	public <T> T getWithCorrectType(MapKeyHolder holder, Class<T> clazz) {
		return getWithCorrectType(holder);
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MapModuloAttributi [\n");
		attributiMap.entrySet().forEach(entry->{
			sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");  
		 });
		sb.append("]");
		return sb.toString();
	}
	
}
