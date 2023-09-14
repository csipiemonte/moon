/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.entity.AzioneEntity;

/**
 * Contruttore di oggetto JSON azione
 *  da areaEntity {@code entity}
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 25/10/2022 - versione iniziale
 */
public class AzioneMapper {
	
	public static Azione buildFromId(Long idAzione) {
		Azione azione = new Azione();
		azione.setIdAzione(idAzione);
		return azione;
	}
	
	public static Azione buildFromEntity(AzioneEntity entity) {
		Azione azione = new Azione();
		azione.setIdAzione(entity.getIdAzione());
		azione.setCodiceAzione(entity.getCodiceAzione());
		azione.setNomeAzione(entity.getNomeAzione());
		azione.setDescAzione(entity.getDescAzione());
		return azione;
	}
	
	public static Azione buildFromEntity(AzioneEntity entity, Long idIstanza, Long idWorkflow) {
		Azione azione = new Azione();
		azione.setIdAzione(entity.getIdAzione());
		azione.setCodiceAzione(entity.getCodiceAzione());
		azione.setNomeAzione(entity.getNomeAzione());
		azione.setIdIstanza(idIstanza);
		azione.setIdWorkflow(idWorkflow);
		return azione;
	}

	public static AzioneEntity buildFromObj(Azione obj) {
		AzioneEntity entity = new AzioneEntity();
		entity.setIdAzione(obj.getIdAzione());
		entity.setCodiceAzione(obj.getCodiceAzione());
		entity.setNomeAzione(obj.getNomeAzione());
		entity.setDescAzione(obj.getDescAzione());
		return entity;
	}
	
}
