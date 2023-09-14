/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import java.util.Base64;

import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.entity.RepositoryFileEntity;

/**
 * Contruttore di oggetto JSON Documento
 *  da RepositoryFileEntity {@code entity}
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */

public class DocumentoMapper {
	
	public static Documento buildFromEntity (RepositoryFileEntity entity) {
		
		Documento obj = new Documento();	
		obj.setFormioKey(entity.getFormioKey());
		obj.setFormioNameFile(entity.getFormioNameFile());	
		obj.setCodiceFile(entity.getCodiceFile());
		obj.setHashFile(entity.getHashFile());
		obj.setNomeFile(entity.getNomeFile());
		obj.setLunghezza(entity.getLunghezza());
		obj.setCodiceFile(entity.getCodiceFile());
		obj.setContenuto(Base64.getEncoder().encodeToString(entity.getContenuto()));
		obj.setContentType(entity.getContentType());
		obj.setDataCreazione(entity.getDataCreazione());
		obj.setIdIstanza(entity.getIdIstanza());
		obj.setFlFirmato(entity.getFlFirmato());
		obj.setFlEliminato(entity.getFlEliminato());
		obj.setHashFile(entity.getHashFile());
		obj.setIdFile(entity.getIdFile());
		obj.setIdStoricoWorkflow(entity.getIdStoricoWorkflow());
		obj.setIdTipologia(entity.getIdTipologia());
		obj.setNumeroProtocollo(entity.getNumeroProtocollo());
		obj.setDataProtocollo(entity.getDataProtocollo());
		obj.setRefUrl(entity.getRefUrl());
		obj.setTipologiaFruitore(entity.getTipologiaFruitore());
		obj.setDescrizione(entity.getDescrizione());

		return obj;	
	}

	public static RepositoryFileEntity buildFromObj (Documento obj) {
		
		RepositoryFileEntity entity = new RepositoryFileEntity();
		
		entity.setFormioKey(obj.getFormioKey());
		entity.setFormioNameFile(obj.getFormioNameFile());	
		entity.setCodiceFile(obj.getCodiceFile());
		entity.setHashFile(obj.getHashFile());
		entity.setNomeFile(obj.getNomeFile());
		entity.setLunghezza(obj.getLunghezza());
		entity.setCodiceFile(obj.getCodiceFile());
		entity.setContenuto(Base64.getEncoder().encode(obj.getContenuto().getBytes()));
		entity.setContentType(obj.getContentType());
		entity.setDataCreazione(obj.getDataCreazione());
		entity.setIdIstanza(obj.getIdIstanza());
		entity.setFlFirmato(obj.getFlFirmato());
		entity.setFlEliminato(obj.getFlEliminato());
		entity.setHashFile(obj.getHashFile());
		entity.setIdFile(obj.getIdFile());
		entity.setIdStoricoWorkflow(obj.getIdStoricoWorkflow());
		entity.setIdTipologia(obj.getIdTipologia());
		entity.setNumeroProtocollo(obj.getNumeroProtocollo());
		entity.setDataProtocollo(obj.getDataProtocollo());
		entity.setRefUrl(obj.getRefUrl());
		entity.setTipologiaFruitore(obj.getTipologiaFruitore());
		entity.setDescrizione(obj.getDescrizione());

		return entity;
	}
}
