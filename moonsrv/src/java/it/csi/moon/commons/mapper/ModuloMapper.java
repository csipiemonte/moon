/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;

/**
 * Contruttore di oggetto JSON Modulo per i moduli 
 *  da ModuloEntity {@code entity}
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloMapper {
	
	public static Modulo buildFromId(Long idModulo, Long idVersioneModulo) {
		Modulo modulo = new Modulo();
		modulo.setIdModulo(idModulo);
		modulo.setIdVersioneModulo(idVersioneModulo);
		return modulo;
	}

	public static Modulo buildFromModuloVersionatoEntity(ModuloVersionatoEntity moduloVersionato) {
		
		Modulo modulo = new Modulo();
		
		modulo.setIdModulo(moduloVersionato.getIdModulo());
		modulo.setCodiceModulo(moduloVersionato.getCodiceModulo());
		modulo.setOggettoModulo(moduloVersionato.getOggettoModulo());
		modulo.setDescrizioneModulo(moduloVersionato.getDescrizioneModulo());
		modulo.setDataIns(moduloVersionato.getDataIns());
		modulo.setDataUpd(moduloVersionato.getDataUpd());
		modulo.setFlagIsRiservato("S".equalsIgnoreCase(moduloVersionato.getFlagIsRiservato()));
		modulo.setFlagProtocolloIntegrato("S".equalsIgnoreCase(moduloVersionato.getFlagProtocolloIntegrato()));
		modulo.setIdTipoCodiceIstanza(moduloVersionato.getIdTipoCodiceIstanza());
		modulo.setAttoreUpd(moduloVersionato.getAttoreUpd());
		modulo.setIdModuloStruttura(null);
		modulo.setTipoStruttura(null);
		modulo.setStruttura(null);
		//
		modulo.setIdVersioneModulo(moduloVersionato.getIdVersioneModulo());
		modulo.setVersioneModulo(moduloVersionato.getVersioneModulo());
		modulo.setStato(StatoModuloMapper.buildFromModuloVersionatoEntity(moduloVersionato));
		modulo.setCategoria(CategoriaMapper.buildFromModuloVersionatoEntity(moduloVersionato));
		
		return modulo;
	}

//	public static Modulo buildFromModuloEntity(ModuloVersionatoEntity moduloEntity,
//			MapModuloAttributi mapModuloAttributi) {
//		Modulo modulo = buildFromModuloVersionatoEntity(moduloEntity);
//		if (mapModuloAttributi != null) {
//			Boolean flagRiportaInBozza = mapModuloAttributi
//					.getWithCorrectType(ModuloAttributoKeys.RIPORTA_IN_BOZZA_ABILITATO);
//			modulo.setRiportaInBozzaAbilitato(Boolean.TRUE.equals(flagRiportaInBozza));
//		}
//		return modulo;
//	}

}
