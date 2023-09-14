/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.ProcessoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Processo;

/**
 * Contruttore di oggetto JSON Processo
 *  da ProcessoEntity {@code entity}
 * 
 * @author Laurent
 */

public class ProcessoMapper {
	
	public static Processo buildFromEntity (ProcessoEntity entity) {
		
		Processo processo = new Processo();
		processo.setIdProcesso(entity.getIdProcesso());
		processo.setCodiceProcesso(entity.getCodiceProcesso());
		processo.setNomeProcesso(entity.getNomeProcesso());
		processo.setDescProcesso(entity.getDescrizioneProcesso());
		processo.setFlagAttivo(entity.getFlagAttivo());
		processo.setAttoreUpd(entity.getAttoreUpd());
		processo.setDataUpd(entity.getDataUpd());		
		return processo;	
	}
	
	public static ProcessoEntity buildFromObj(Processo obj) {
		
		ProcessoEntity entity = new ProcessoEntity();
		entity.setIdProcesso(obj.getIdProcesso());
		entity.setCodiceProcesso(obj.getCodiceProcesso());
		entity.setNomeProcesso(obj.getNomeProcesso());
		entity.setDescrizioneProcesso(obj.getDescProcesso());
		entity.setFlagAttivo(obj.getFlagAttivo());
		entity.setAttoreUpd(obj.getAttoreUpd());
		entity.setDataUpd(obj.getDataUpd());		
		return entity;	
	}
	
}
