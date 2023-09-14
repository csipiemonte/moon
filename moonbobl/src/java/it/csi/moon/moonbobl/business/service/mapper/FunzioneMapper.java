/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.FunzioneEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Funzione;

/**
 * Contruttore di oggetto JSON Funzione
 *  da FunzioneEntity {@code entity}
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */

public class FunzioneMapper {
	
	public static Funzione buildFromEntity (FunzioneEntity entity) {
		
		Funzione funzione = new Funzione();
		funzione.setIdFunzione(entity.getIdFunzione());
		funzione.setCodice(entity.getCodiceFunzione());
		funzione.setNome(entity.getNomeFunzione());
		funzione.setDescrizione(entity.getDescrizioneFunzione());
		funzione.setFlagAttivo("S".equals(entity.getFlAttivo()));
		funzione.setDataUpd(entity.getDataUpd());
		funzione.setAttoreUpd(entity.getAttoreUpd());
		
		return funzione;	
	}
}
