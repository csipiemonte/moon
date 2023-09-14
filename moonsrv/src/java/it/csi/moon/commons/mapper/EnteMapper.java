/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.entity.EnteEntity;

/**
 * Contruttore di oggetto JSON Ente
 *  da EnteEntity {@code entity}
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/06/2020 - versione iniziale
 */
public class EnteMapper {
	
	public static Ente buildFromId(Long idEnte) {
		Ente ente = new Ente();
		ente.setIdEnte(idEnte);
		return ente;
	}
	
	public static Ente buildFromEntity(EnteEntity entity) {
		
		Ente ente = new Ente();
		
		ente.setIdEnte(entity.getIdEnte());
		ente.setCodiceEnte(entity.getCodiceEnte());
		ente.setNomeEnte(entity.getNomeEnte());
		ente.setDescrizioneEnte(entity.getDescrizioneEnte());
		ente.setFlAttivo("S".equals(entity.getFlAttivo()));
		ente.setIdTipoEnte(entity.getIdTipoEnte());
		ente.setLogo(entity.getLogo());
		ente.setIndirizzo(entity.getIndirizzo());
		ente.setDataUpd(entity.getDataUpd());
		ente.setAttoreUpd(entity.getAttoreUpd());
		ente.setIdEntePadre(entity.getIdEntePadre());
		ente.setCodiceIpa(entity.getCodiceIpa());
		
		return ente;
	}

}
