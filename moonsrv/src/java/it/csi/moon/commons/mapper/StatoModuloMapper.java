/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.StatoModulo;
import it.csi.moon.commons.entity.ModuloCronologiaStatiEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;

/**
 * Contruttore di oggetto JSON StatoModulo 
 *  da DecodificaStatoModulo {@code decodificaStatoModulo}
 *  da String {@code codiceStatoModulo}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class StatoModuloMapper {
	
	public static StatoModulo buildFromDecodifica(DecodificaStatoModulo decodificaStatoModulo) {
		
		StatoModulo obj = new StatoModulo();
		obj.setCodice(decodificaStatoModulo.getCodice());
		obj.setDescrizione(decodificaStatoModulo.getDescrizione());
		return obj;		
	}

	public static StatoModulo buildFromCodiceStato(String codiceStatoModulo) {
		
		DecodificaStatoModulo decodificaStatoModulo = DecodificaStatoModulo.byCodice(codiceStatoModulo);

		return buildFromDecodifica(decodificaStatoModulo);
	}
	
	public static StatoModulo buildFromIdStato(Integer idStatoModulo) {
		
		DecodificaStatoModulo decodificaStatoModulo = DecodificaStatoModulo.byId(idStatoModulo);

		return buildFromDecodifica(decodificaStatoModulo);
	}

	public static StatoModulo buildFromModuloCronologiaStatiEntity(ModuloCronologiaStatiEntity currentCron) {
		StatoModulo result = buildFromIdStato(currentCron.getIdStato());
		result.setDataInizioValidita(currentCron.getDataInizioValidita());
		result.setDataFineValidita(currentCron.getDataFineValidita());
		return result;
	}

	public static StatoModulo buildFromModuloVersionatoEntity(ModuloVersionatoEntity moduloVersionato) {
		StatoModulo result = buildFromIdStato(moduloVersionato.getIdStato());
		result.setDataInizioValidita(moduloVersionato.getDataInizioValidita());
		result.setDataFineValidita(moduloVersionato.getDataFineValidita());
		return result;
	}
}
