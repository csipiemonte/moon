/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.TipoCodiceIstanza;
import it.csi.moon.commons.entity.TipoCodiceIstanzaEntity;

/**
 * Contruttore di oggetto JSON TipoCodiceIstanza
 *  da TipoCodiceIstanzaEntity {@code entity}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public class TipoCodiceIstanzaMapper {
	
	public static TipoCodiceIstanza buildFromEntity (TipoCodiceIstanzaEntity entity) {
		
		TipoCodiceIstanza obj = new TipoCodiceIstanza();
		obj.setIdTipoCodiceIstanza(entity.getIdTipoCodiceIstanza());
		obj.setDescCodice(entity.getDescCodice());
		obj.setDescrizioneTipo(entity.getDescrizioneTipo());
		
		return obj;
	}

}
