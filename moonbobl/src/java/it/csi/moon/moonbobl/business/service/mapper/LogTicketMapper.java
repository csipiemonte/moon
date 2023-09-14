/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.TicketingSystemRichiestaEntity;
import it.csi.moon.moonbobl.dto.moonfobl.LogTicket;

public class LogTicketMapper {
	
	public static LogTicket buildFromEntity (TicketingSystemRichiestaEntity entity) {
		
		LogTicket logTicket = new LogTicket();
		logTicket.setIdRichiesta(entity.getIdRichiesta());
		logTicket.setDataRichiesta(entity.getDataRichiesta());
		logTicket.setCodiceRichiesta(entity.getCodiceRichiesta());
		logTicket.setUuidRichiesta(entity.getUuidRichiesta());
		logTicket.setStato(entity.getStato());
		logTicket.setTipoDoc(entity.getTipoDoc());
		logTicket.setIdIstanza(entity.getIdIstanza());
		logTicket.setIdAllegatoIstanza(entity.getIdAllegatoIstanza());
		logTicket.setIdFile(entity.getIdFile());
		logTicket.setIdModulo(entity.getIdModulo());
		logTicket.setIdArea(entity.getIdArea());
		logTicket.setIdEnte(entity.getIdEnte());
		logTicket.setIdTicketingSystem(entity.getIdTicketingSystem());
		logTicket.setUuidTicketingSystem(entity.getUuidTicketingSystem());
		logTicket.setNote(entity.getNote());
		logTicket.setCodiceEsito(entity.getCodiceEsito());
		logTicket.setDescEsito(entity.getDescEsito());
		logTicket.setDataUpd(entity.getDataUpd());
		
		return logTicket;	
	}
	
	public static LogTicket buildFromEntity (TicketingSystemRichiestaEntity entity, String nomeFile) {
		
		LogTicket logTicket = buildFromEntity(entity);
		logTicket.setNomeFile(nomeFile);
		
		return logTicket;	
	}
	
}