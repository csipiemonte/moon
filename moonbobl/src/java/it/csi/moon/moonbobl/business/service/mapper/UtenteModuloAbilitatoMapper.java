/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.UtenteModuloAbilitatoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.UtenteModuloAbilitato;

/**
 * Costruttore di oggetto JSON Utente
 *  da UtenteModuloAbilitatoEntity {@code entity}
 * 
 * @author Laurent
*/
public class UtenteModuloAbilitatoMapper {
	
	public static UtenteModuloAbilitato buildFromEntity (UtenteModuloAbilitatoEntity entity) {
		
		UtenteModuloAbilitato utente = new UtenteModuloAbilitato();
		utente.setIdUtente(entity.getIdUtente());
		utente.setIdentificativoUtente(entity.getIdentificativoUtente());
		utente.setNome(entity.getNome());
		utente.setCognome(entity.getCognome());
		utente.setEmail(entity.getEmail());
		utente.setFlagAttivo("S".equals(entity.getFlAttivo()));
		utente.setTipoUtente(TipoUtenteMapper.buildFromIdTipoUtente(entity.getIdTipoUtente()));
		utente.setDataUpdAbilitazione(entity.getDataUpdAbilitazione());
		utente.setAttoreUpdAbilitazione(entity.getAttoreUpdAbilitazione());
		
		return utente;	
	}
}
