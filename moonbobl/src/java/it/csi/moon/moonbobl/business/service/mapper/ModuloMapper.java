/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper;


import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloMoonsrv;

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
	
	@Deprecated
	public static Modulo buildFromModuloEntity(ModuloEntity entity) {
		
		Modulo modulo = new Modulo();
		
		modulo.setIdModulo(entity.getIdModulo());
		modulo.setCodiceModulo(entity.getCodiceModulo());
		modulo.setOggettoModulo(entity.getOggettoModulo());
		modulo.setDescrizioneModulo(entity.getDescrizioneModulo());
		modulo.setDataIns(entity.getDataIns());
		modulo.setDataUpd(entity.getDataUpd());
		modulo.setFlagIsRiservato("S".equalsIgnoreCase(entity.getFlagIsRiservato()));
		modulo.setFlagProtocolloIntegrato("S".equalsIgnoreCase(entity.getFlagProtocolloIntegrato()));
		modulo.setIdTipoCodiceIstanza(entity.getIdTipoCodiceIstanza());
		modulo.setAttoreUpd(entity.getAttoreUpd());
		modulo.setIdModuloStruttura(null);
		modulo.setTipoStruttura(null);
		modulo.setStruttura(null);

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
	
//	public static Modulo buildFromModuloEntity(ModuloVersionatoEntity moduloEntity, MapModuloAttributi mapModuloAttributi) {
//		Modulo modulo = buildFromModuloVersionatoEntity(moduloEntity);
//		Boolean flagRiportaInBozza = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.RIPORTA_IN_BOZZA_ABILITATO);
//		modulo.setRiportaInBozzaAbilitato(Boolean.TRUE.equals(flagRiportaInBozza));
//		return modulo;
//	}

	public static ModuloMoonsrv buildModuloMoosrvFromModuloVersionatoEntity(ModuloVersionatoEntity moduloVersionato) {
		
		ModuloMoonsrv modulo = new ModuloMoonsrv();
		
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
	
}
