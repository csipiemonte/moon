/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.TipoUtente;
import it.csi.moon.commons.util.decodifica.DecodificaTipoUtente;

/**
 * Contruttore di oggetto JSON TipoUtente 
 *  da DecodificaTipoUtente {@code decodificaTipoUtente}
 *  da Integer {@code idTipoUtente}
 *  da String {@code codiceTipoUtente}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class TipoUtenteMapper {
	
	public static TipoUtente buildFromDecodifica(DecodificaTipoUtente decodificaTipoUtente) {
		
		TipoUtente obj = new TipoUtente();
		obj.setCodice(decodificaTipoUtente.getCodice());
		obj.setDescrizione(decodificaTipoUtente.getDescrizione());

		return obj;		
	}

	public static TipoUtente buildFromIdTipoUtente(Integer idTipoUtente) {
		
		DecodificaTipoUtente decodificaTipoUtente = DecodificaTipoUtente.byId(idTipoUtente);

		return buildFromDecodifica(decodificaTipoUtente);
	}
	
	public static TipoUtente buildFromCodiceTipoUtente(String codiceTipoUtente) {
		
		DecodificaTipoUtente decodificaTipoUtente = DecodificaTipoUtente.byCodice(codiceTipoUtente);

		return buildFromDecodifica(decodificaTipoUtente);
	}
	
}
