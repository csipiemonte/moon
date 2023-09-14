/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.entity.AllegatoEntity;
import it.csi.moon.commons.entity.AllegatoLazyEntity;

/**
 * Contruttore di oggetto JSON Allegato
 *  da AllegatoEntity {@code entity}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public class AllegatoMapper {
	
	public static Allegato buildFromEntity (AllegatoEntity entity) {
		
		Allegato obj = new Allegato();
		obj.setIdAllegato(entity.getIdAllegato());
		obj.setFormioKey(entity.getFormioKey());
		obj.setFormioNameFile(entity.getFormioNameFile());
		obj.setFormioDir(entity.getFormioDir());
		obj.setCodiceFile(entity.getCodiceFile());
		obj.setHashFile(entity.getHashFile());
		obj.setNomeFile(entity.getNomeFile());
		obj.setLunghezza(entity.getLunghezza());
		obj.setCodiceFile(entity.getCodiceFile());
		obj.setContenuto(entity.getContenuto());
		obj.setContentType(entity.getContentType());
		obj.setMediaType(entity.getMediaType());
		obj.setMediaSubType(entity.getSubMediaType());
		obj.setIpAddress(entity.getIpAddress());
		obj.setEstensione(entity.getEstensione());
		obj.setUuidIndex(entity.getUuidIndex());
		obj.setDataCreazione(entity.getDataCreazione());
		obj.setIdIstanza(entity.getIdIstanza());
		obj.setKey(entity.getKey());
		obj.setFullKey(entity.getFullKey());
		obj.setLabel(entity.getLabel());
		obj.setFlagFirmato("S".equals(entity.getFlFirmato()));

		return obj;	
	}
	
	public static Allegato buildFromLazyEntity (AllegatoLazyEntity entity) {
		
		Allegato obj = new Allegato();
		obj.setIdAllegato(entity.getIdAllegato());
		obj.setFormioKey(entity.getFormioKey());
		obj.setFormioNameFile(entity.getFormioNameFile());
		obj.setFormioDir(entity.getFormioDir());
		obj.setCodiceFile(entity.getCodiceFile());
		obj.setHashFile(entity.getHashFile());
		obj.setNomeFile(entity.getNomeFile());
		obj.setLunghezza(entity.getLunghezza());
		obj.setCodiceFile(entity.getCodiceFile());
		obj.setContentType(entity.getContentType());
		obj.setMediaType(entity.getMediaType());
		obj.setMediaSubType(entity.getSubMediaType());
		obj.setIpAddress(entity.getIpAddress());
		obj.setEstensione(entity.getEstensione());
		obj.setUuidIndex(entity.getUuidIndex());
		obj.setDataCreazione(entity.getDataCreazione());
		obj.setIdIstanza(entity.getIdIstanza());
		obj.setKey(entity.getKey());
		obj.setFullKey(entity.getFullKey());
		obj.setLabel(entity.getLabel());
		obj.setFlagFirmato("S".equals(entity.getFlFirmato()));

		return obj;	
	}

	public static AllegatoEntity buildFromObj (Allegato obj) {
		
		AllegatoEntity entity = new AllegatoEntity();
		entity.setIdAllegato(obj.getIdAllegato());
		entity.setFormioKey(obj.getFormioKey());
		entity.setFormioNameFile(obj.getFormioNameFile());
		entity.setFormioDir(obj.getFormioDir());
		entity.setCodiceFile(obj.getCodiceFile());
		entity.setContenuto(obj.getContenuto());
		entity.setHashFile(obj.getHashFile());
		entity.setNomeFile(obj.getNomeFile());
		entity.setLunghezza(obj.getLunghezza());
		entity.setCodiceFile(obj.getCodiceFile());
		entity.setContentType(obj.getContentType());
		entity.setMediaType(obj.getMediaType());
		entity.setSubMediaType(obj.getMediaSubType());
		entity.setIpAddress(obj.getIpAddress());
		entity.setEstensione(obj.getEstensione());
		entity.setUuidIndex(obj.getUuidIndex());
		entity.setDataCreazione(obj.getDataCreazione());
		entity.setIdIstanza(obj.getIdIstanza());
		entity.setKey(obj.getKey());
		entity.setFullKey(obj.getFullKey());
		entity.setLabel(obj.getLabel());
		entity.setFlFirmato(obj.isFlagFirmato()?"S":"N");

		return entity;
	}
}
