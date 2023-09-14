/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.LogServizioCosmo;
import it.csi.moon.commons.entity.CosmoLogServizioEntity;

/**
 * Costruttore di oggetto JSON LogServizioCosmo
 *  da CosmoLogServizioEntity {@code entity}
 * 
 * @author Laurent
 */

public class LogServizioCosmoMapper {
	
	public static LogServizioCosmo buildFromEntity (CosmoLogServizioEntity entity) {
		
		LogServizioCosmo obj = new LogServizioCosmo();
		obj.setIdLogServizio(entity.getIdLogServizio());
		obj.setIdLogPratica(entity.getIdLogPratica());
		obj.setIdIstanza(entity.getIdIstanza());
		obj.setIdPratica(entity.getIdPratica());
		obj.setIdModulo(entity.getIdModulo());
		obj.setDataIns(entity.getDataIns());
		obj.setDataUpd(entity.getDataUpd());
		obj.setRichiesta(entity.getRichiesta());
		obj.setRisposta(entity.getRisposta());
		obj.setErrore(entity.getErrore());
		
		return obj;	
	}
}
