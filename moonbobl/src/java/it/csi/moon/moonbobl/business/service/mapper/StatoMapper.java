/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.StatoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Stato;

/**
 * Contruttore di oggetto JSON Stato per le istanze 
 *  da StatoEntity {@code entity}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class StatoMapper {
	
	public static Stato buildFromEntity(StatoEntity entity) {
		Stato obj = new Stato();
		obj.setIdStato(entity.getIdStatoWf());
		obj.setCodice(entity.getCodiceStatoWf());
		obj.setNome(entity.getNomeStatoWf());
		obj.setDescrizione(entity.getDescStatoWf());
		obj.setIdTabFo(entity.getIdStatoWf());
		return obj;
	}
	
	public static Stato buildFromEntityForApi(StatoEntity entity) {
		Stato obj = new Stato();
//		obj.setIdStato(entity.getIdStatoWf());
		obj.setCodice(entity.getCodiceStatoWf());
		obj.setNome(entity.getNomeStatoWf());
		obj.setDescrizione(entity.getDescStatoWf());
		return obj;
	}

	public static StatoEntity buildFromObj(Stato obj) {
		StatoEntity entity = new StatoEntity();
		entity.setIdStatoWf(obj.getIdStato());
		entity.setCodiceStatoWf(obj.getCodice());
		entity.setNomeStatoWf(obj.getNome());
		entity.setDescStatoWf(obj.getDescrizione());
		entity.setIdTabFo(obj.getIdTabFo());
		return entity;
	}

}
