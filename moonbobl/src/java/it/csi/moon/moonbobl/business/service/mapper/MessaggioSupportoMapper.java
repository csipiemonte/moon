/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.MessaggioSupportoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.MessaggioSupporto;

/**
 * Contruttore di oggetto JSON MessaggioSupporto
 *  da MessaggioSupportoEntity {@code entity}
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 24/08/2020 - versione iniziale
 */
public class MessaggioSupportoMapper {
	
	public static MessaggioSupporto buildFromEntity (MessaggioSupportoEntity entity) {
		
		MessaggioSupporto result = new MessaggioSupporto();
		result.setIdMessaggioSupporto(entity.getIdMessaggioSupporto());
//		result.setIdRichiestaSupporto(entity.getIdRichiestaSupporto());
		result.setContenuto(entity.getContenuto());
		result.setProvenienza(entity.getProvenienza());
		result.setDataIns(entity.getDataIns());
		result.setAttoreIns(entity.getAttoreIns());
		return result;	
	}

}