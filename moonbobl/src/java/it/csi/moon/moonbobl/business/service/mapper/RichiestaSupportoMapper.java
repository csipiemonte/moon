/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.RichiestaSupportoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.RichiestaSupporto;

/**
 * Contruttore di oggetto JSON RichiestaSupporto
 *  da RichiestaSupportoEntity {@code entity}
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public class RichiestaSupportoMapper {
	
	public static RichiestaSupporto buildFromEntity (RichiestaSupportoEntity entity) {
		
		RichiestaSupporto result = new RichiestaSupporto();
		result.setIdRichiestaSupporto(entity.getIdRichiestaSupporto());
		result.setIdIstanza(entity.getIdIstanza());
		result.setIdModulo(entity.getIdModulo());
		result.setFlagInAttesaDiRisposta(entity.getFlagInAttesaDiRisposta());
		result.setDescMittente(entity.getDescMittente());
		result.setEmailMittente(entity.getEmailMittente());
		result.setDataIns(entity.getDataIns());
		result.setAttoreIns(entity.getAttoreIns());
		result.setDataUpd(entity.getDataUpd());
		result.setAttoreUpd(entity.getAttoreUpd());
		return result;	
	}

}