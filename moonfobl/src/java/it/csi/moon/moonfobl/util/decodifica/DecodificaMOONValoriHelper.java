/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util.decodifica;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe di helper per il calcolo dei valori possibili per la decodifica MOON
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 05/12/2019 - versione iniziale
 *
 */
public class DecodificaMOONValoriHelper {

	/** Classe di utilit&agrave;, da non instanziare */
	private DecodificaMOONValoriHelper() {
	}
	
	/**
	 * Calcolo dei valori possibili.
	 * 
	 * @param arr gli elementid a cui ottenere i valori
	 * @return i valori possibili
	 */
	public static String getValoriPossibili(DecodificaMOON[] arr) {
		int length = arr.length;
		List<String> list = new ArrayList<>();
		for(int i = 0; i < length; i++) {
			list.add(arr[i].getCodice());
		}
		Iterable<String> iterable = list;
		return String.join(", ", iterable);
	}
	
}
