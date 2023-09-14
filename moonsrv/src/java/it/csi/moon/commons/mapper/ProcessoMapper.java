/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.Processo;
import it.csi.moon.commons.entity.ProcessoEntity;

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
}
