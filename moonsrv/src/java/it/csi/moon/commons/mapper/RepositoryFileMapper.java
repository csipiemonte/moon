/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import it.csi.moon.commons.dto.RepositoryFile;
import it.csi.moon.commons.dto.TipoRepositoryFile;
import it.csi.moon.commons.dto.api.FruitoreAllegatoAzione;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.RepositoryFileLazyEntity;
import it.csi.moon.commons.util.decodifica.DecodificaTipoRepositoryFile;

public class RepositoryFileMapper {

	/**
	 * RepositoryFileMapper
	 * 
	 * @author Danilo
	 *
	 * @since 1.0.0
	 */

	public static RepositoryFile buildFromEntity(RepositoryFileLazyEntity entity) {
		RepositoryFile obj = new RepositoryFile();
		obj.setIdFile(entity.getIdFile());
		obj.setNomeFile(entity.getNomeFile());
		obj.setFlEliminato("S".equalsIgnoreCase(entity.getFlEliminato()));
		obj.setDataCreazione(entity.getDataCreazione());
		obj.setIdIstanza(entity.getIdIstanza());
		obj.setFormioKey(entity.getFormioKey());
		obj.setFormioNameFile(entity.getFormioNameFile());
		obj.setCodiceFile(entity.getCodiceFile());
		obj.setHashFile(entity.getHashFile());
		obj.setContentType(entity.getContentType());
		obj.setTipologia(null);
		obj.setTipologia(buildTipoRepositoryFile(DecodificaTipoRepositoryFile.byId(entity.getIdTipologia())));
		obj.setLunghezza(entity.getLunghezza());
		obj.setDescrizione(entity.getDescrizione());
		obj.setIdStoricoWorkflow(entity.getIdStoricoWorkflow());
		obj.setFlFirmato("S".equalsIgnoreCase(entity.getFlFirmato()));
		obj.setTipologiaFruitore(entity.getTipologiaFruitore());
		obj.setRefUrl(entity.getRefUrl());
		obj.setNumeroProtocollo(entity.getNumeroProtocollo());
		obj.setDataProtocollo(entity.getDataProtocollo());
		obj.setUuidIndex(entity.getUuidIndex());
		obj.setKey(entity.getKey());
		obj.setFullKey(entity.getFullKey());
		obj.setLabel(entity.getLabel());
		obj.setIdTipologiaMydocs(entity.getIdTipologiaMydocs());
//		obj.setContenuto(entity.getContenuto());
		obj.setCodiceFile(entity.getCodiceFile());
		return obj;
	}
	
	public static RepositoryFile buildFromEntity(RepositoryFileEntity entity) {

		RepositoryFile obj = new RepositoryFile();
		obj.setIdFile(entity.getIdFile());
		obj.setNomeFile(entity.getNomeFile());
		obj.setFlEliminato("S".equalsIgnoreCase(entity.getFlEliminato()));
		obj.setDataCreazione(entity.getDataCreazione());
		obj.setIdIstanza(entity.getIdIstanza());
		obj.setFormioKey(entity.getFormioKey());
		obj.setFormioNameFile(entity.getFormioNameFile());
		obj.setCodiceFile(entity.getCodiceFile());
		obj.setHashFile(entity.getHashFile());
		obj.setContentType(entity.getContentType());
		obj.setTipologia(null);
		obj.setTipologia(buildTipoRepositoryFile(DecodificaTipoRepositoryFile.byId(entity.getIdTipologia())));
		obj.setLunghezza(entity.getLunghezza());
		obj.setDescrizione(entity.getDescrizione());
		obj.setIdStoricoWorkflow(entity.getIdStoricoWorkflow());
		obj.setFlFirmato("S".equalsIgnoreCase(entity.getFlFirmato()));
		obj.setTipologiaFruitore(entity.getTipologiaFruitore());
		obj.setRefUrl(entity.getRefUrl());
		obj.setNumeroProtocollo(entity.getNumeroProtocollo());
		obj.setDataProtocollo(entity.getDataProtocollo());
		obj.setUuidIndex(entity.getUuidIndex());
		obj.setKey(entity.getKey());
		obj.setFullKey(entity.getFullKey());
		obj.setLabel(entity.getLabel());
		obj.setIdTipologiaMydocs(entity.getIdTipologiaMydocs());
//		obj.setContenuto(Base64.getEncoder().encode(entity.getContenuto()));
		obj.setContenuto(entity.getContenuto());
		obj.setCodiceFile(entity.getCodiceFile());
		return obj;
	}

	private static TipoRepositoryFile buildTipoRepositoryFile(DecodificaTipoRepositoryFile dTipologia) {
		TipoRepositoryFile result = new TipoRepositoryFile();
		result.setCodice(dTipologia.name());
		result.setDescrizione(dTipologia.getDescrizione());
		result.setTipoIngUsc(dTipologia.getTipoIngUsc());
		return result;
	}

	public static RepositoryFileEntity buildFromObj(FruitoreAllegatoAzione obj) {

		RepositoryFileEntity entity = new RepositoryFileEntity();
		byte[] contenuto = Base64.getDecoder().decode(obj.getContenuto());

		entity.setCodiceFile(getCodiceFile());
		entity.setContentType(obj.getContentType());
	    entity.setContenuto(Base64.getDecoder().decode(obj.getContenuto()));
		entity.setDataCreazione(new Date());
		entity.setFlEliminato("N");
		entity.setFlFirmato((obj.getDocFirmato() != null) ? obj.getDocFirmato() ? "S" : "N": null);
		entity.setFormioKey(null);
		entity.setFormioNameFile(null);
		entity.setHashFile(getHashFile(contenuto));
//	entity.setIdIstanza(idIstanza);
//	entity.setIdStoricoWorkflow(idStoricoWorkflow);
//	entity.setIdTipologia(obj.getIdTipologia());
		entity.setIdTipologia(DecodificaTipoRepositoryFile.API_ALLEGATO_AZIONE_CAMBIO_STATO.getId());
		entity.setNomeFile(obj.getNomeFile());
		entity.setNumeroProtocollo(obj.getNumeroProtocollo());
		entity.setDataProtocollo(obj.getDataProtocollo());

		return entity;

	}
	
	public static RepositoryFileEntity buildFromObj(FruitoreAllegatoAzione obj, Long idIstanza, Long idStoricoWorkflow, String codiceAzione) {

		RepositoryFileEntity entity = new RepositoryFileEntity();
		
		byte[] contenuto = "NULL".getBytes();
		String refUrl = null;
		String codiceFile = getCodiceFile();
				
		if (obj.getContenuto() != null && StringUtils.isNotEmpty(obj.getContenuto()))
		{	
			contenuto = Base64.getDecoder().decode(obj.getContenuto());
		}
		else {
//			contenuto = convertUrlToContent(obj.getRefUrl());
			refUrl = obj.getRefUrl();
		}
		
		String[] nameParts = obj.getNomeFile().split("[.]"); 
				
		String formatExt = nameParts[nameParts.length -1];
		String formioNameFile = (nameParts.length > 1) ? nameParts[0]+"_"+codiceFile+"."+formatExt: codiceFile;
		entity.setCodiceFile(codiceFile);
		entity.setContentType(obj.getContentType());	
	    entity.setContenuto(contenuto);
		entity.setDataCreazione(new Date());
		entity.setFlEliminato("N");
		entity.setFlFirmato((obj.getDocFirmato() != null) ? obj.getDocFirmato() ? "S" : "N": null);
		entity.setHashFile(getHashFile(contenuto));
		entity.setFormioKey(null);
		entity.setFormioNameFile(formioNameFile);
		entity.setIdIstanza(idIstanza);
		entity.setIdStoricoWorkflow(idStoricoWorkflow);		
		entity.setLunghezza(obj.getLunghezza());
		entity.setDescrizione(obj.getDescrizione());
		entity.setIdTipologia(DecodificaTipoRepositoryFile.API_ALLEGATO_AZIONE_CAMBIO_STATO.getId());
		entity.setNomeFile(obj.getNomeFile());
		entity.setTipologiaFruitore(obj.getTipo());
		entity.setRefUrl(refUrl);
		entity.setNumeroProtocollo(obj.getNumeroProtocollo());
		entity.setDataProtocollo(obj.getDataProtocollo());
		
		if (codiceAzione != null) {
		switch (codiceAzione){
		  case "NOTIFICA_FRUITORE":	
			  entity.setIdTipologia(DecodificaTipoRepositoryFile.API_ALLEGATO_NOTIFICA.getId());
			    break;
		  default:
			  entity.setIdTipologia(DecodificaTipoRepositoryFile.API_ALLEGATO_AZIONE_CAMBIO_STATO.getId());
			}
		}

		return entity;

	}
	
//	private static byte[] convertUrlToContent(String url) {
//		byte[] content = null;
//		if (url != null && StringUtils.isNotEmpty(url)) {
//			try {
//				content = IOUtils.toByteArray((new URL(url)).openStream());
//			} catch (MalformedURLException e) {
//				LOG.debug("[" + CLASS_NAME + "::convertUrlToContent] IN URL = " + url);
//			} catch (IOException e) {
//				LOG.debug("[" + CLASS_NAME + "::convertUrlToContent] IN URL = " + url);
//			}
//		}
//		return content;
//	}
	
	private static String getHashFile(byte[] contenuto) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(contenuto);
			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			return "";
		}
	}
	
	private static String getCodiceFile() {
		return UUID.randomUUID().toString();

	}
	

}
