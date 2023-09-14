/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.Utente;
import it.csi.moon.commons.entity.UtenteEntity;

/**
 * Costruttore di oggetto JSON Utente
 *  da UtenteEntity {@code entity}
 * 
 * @author Mario
 */

public class UtenteMapper {
	
	public static Utente buildFromEntity (UtenteEntity entity) {
		
		Utente utente = new Utente();
		utente.setIdUtente(entity.getIdUtente());
		utente.setIdentificativoUtente(entity.getIdentificativoUtente());
		utente.setNome(entity.getNome());
		utente.setCognome(entity.getCognome());
		utente.setUsername(entity.getUsername());
		utente.setPassword(entity.getPassword());
		utente.setEmail(entity.getEmail());
		utente.setFlagAttivo("S".equals(entity.getFlAttivo()));
		utente.setTipoUtente(TipoUtenteMapper.buildFromIdTipoUtente(entity.getIdTipoUtente()));
		utente.setDataIns(entity.getDataIns());
		utente.setDataUpd(entity.getDataUpd());
		utente.setAttoreIns(entity.getAttoreIns());
		utente.setAttoreUpd(entity.getAttoreUpd());
		
		return utente;	
	}
}
