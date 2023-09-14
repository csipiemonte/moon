/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.LogPraticaCosmo;
import it.csi.moon.commons.entity.CosmoLogPraticaEntity;

/**
 * Costruttore di oggetto JSON LogPraticaCosmo
 *  da CosmoLogPraticaEntity {@code entity}
 * 
 * @author Laurent
 */

public class LogPraticaCosmoMapper {
	
	public static LogPraticaCosmo buildFromEntity (CosmoLogPraticaEntity entity) {
		
		LogPraticaCosmo obj = new LogPraticaCosmo();
		obj.setIdLogPratica(entity.getIdLogPratica());
		obj.setIdIstanza(entity.getIdIstanza());
		obj.setIdPratica(entity.getIdPratica());
		obj.setIdx(entity.getIdx());
		obj.setIdModulo(entity.getIdModulo());
		obj.setDataIns(entity.getDataIns());
		obj.setDataAvvio(entity.getDataAvvio());
		obj.setDataUpd(entity.getDataUpd());
		obj.setCreaRichiesta(entity.getCreaRichiesta());
		obj.setCreaRisposta(entity.getCreaRisposta());
		obj.setCreaDocumentoRichiesta(entity.getCreaDocumentoRichiesta());
		obj.setCreaDocumentoRisposta(entity.getCreaDocumentoRisposta());
		obj.setAvviaProcessoRichiesta(entity.getAvviaProcessoRichiesta());
		obj.setAvviaProcessoRisposta(entity.getAvviaProcessoRisposta());
		obj.setErrore(entity.getErrore());
		obj.setPratica(entity.getPratica());
		
		return obj;	
	}
}
