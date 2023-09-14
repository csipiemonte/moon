/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.FaqEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Faq;

/**
 * Contruttore di oggetto JSON Faq
 *  da FaqEntity {@code entity}
 * 
 * @author Laurent
 */

public class FaqMapper {
	
	public static Faq buildFromEntity (FaqEntity entity) {
		
		Faq faq = new Faq();
		faq.setIdFaq(entity.getIdFaq());
		faq.setTitolo(entity.getTitolo());
		faq.setContenuto(entity.getContenuto());
		
		faq.setDataIns(entity.getDataIns());
		faq.setAttoreIns(entity.getAttoreIns());
		faq.setDataUpd(entity.getDataUpd());
		faq.setAttoreUpd(entity.getAttoreUpd());
		
		return faq;	
	}
}
