/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;
/*
 * Rappresenta allegato inviato al servizio upload
 * 
 * 
 */

import java.util.Date;

public class AllegatoLazyEntity {
	
	private Long idAllegato;
	private String formioKey;
	private String formioNameFile;
	private String formioDir;
	private String codiceFile;
	private String hashFile;
	private String nomeFile;
	private Integer lunghezza;
	private String contentType;
	private String mediaType;
	private String subMediaType;
	private String ipAddress;
	private String estensione;
	private String uuidIndex;
	private String flEliminato;
	private Date dataCreazione;
	private Long idIstanza;
	private String key;
	private String fullKey;
	private String label;
	private String flFirmato;
	
	public AllegatoLazyEntity() {
		super();
	}
	public AllegatoLazyEntity(AllegatoLazyEntity allegatoLazyEntityToClone) {
		super();
		this.idAllegato=allegatoLazyEntityToClone.getIdAllegato();
		this.formioKey=allegatoLazyEntityToClone.getFormioKey();
		this.formioNameFile=allegatoLazyEntityToClone.getFormioNameFile();
		this.formioDir=allegatoLazyEntityToClone.getFormioDir();
		this.codiceFile=allegatoLazyEntityToClone.getCodiceFile();
		this.hashFile=allegatoLazyEntityToClone.getHashFile();
		this.nomeFile=allegatoLazyEntityToClone.getNomeFile();
		this.lunghezza=allegatoLazyEntityToClone.getLunghezza();
		this.contentType=allegatoLazyEntityToClone.getContentType();
		this.mediaType=allegatoLazyEntityToClone.getMediaType();
		this.subMediaType=allegatoLazyEntityToClone.getSubMediaType();
		this.ipAddress=allegatoLazyEntityToClone.getIpAddress();
		this.estensione=allegatoLazyEntityToClone.getEstensione();
		this.uuidIndex=allegatoLazyEntityToClone.getUuidIndex();
		this.flEliminato=allegatoLazyEntityToClone.getFlEliminato();
		this.dataCreazione=allegatoLazyEntityToClone.getDataCreazione();
		this.idIstanza=allegatoLazyEntityToClone.getIdIstanza();
		this.key=allegatoLazyEntityToClone.getKey();
		this.fullKey=allegatoLazyEntityToClone.getFullKey();
		this.label=allegatoLazyEntityToClone.getLabel();
		this.flFirmato=allegatoLazyEntityToClone.getFlFirmato();
	}
	
	public Long getIdAllegato() {
		return idAllegato;
	}
	public void setIdAllegato(Long idAllegato) {
		this.idAllegato = idAllegato;
	}
	public String getFormioKey() {
		return formioKey;
	}
	public void setFormioKey(String formioKey) {
		this.formioKey = formioKey;
	}
	public String getFormioNameFile() {
		return formioNameFile;
	}
	public void setFormioNameFile(String formioNameFile) {
		this.formioNameFile = formioNameFile;
	}
	public String getFormioDir() {
		return formioDir;
	}
	public void setFormioDir(String formioDir) {
		this.formioDir = formioDir;
	}
	public String getCodiceFile() {
		return codiceFile;
	}
	public void setCodiceFile(String codiceFile) {
		this.codiceFile = codiceFile;
	}
	public String getHashFile() {
		return hashFile;
	}
	public void setHashFile(String hashFile) {
		this.hashFile = hashFile;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public Integer getLunghezza() {
		return lunghezza;
	}
	public void setLunghezza(Integer lunghezza) {
		this.lunghezza = lunghezza;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getSubMediaType() {
		return subMediaType;
	}
	public void setSubMediaType(String subMediaType) {
		this.subMediaType = subMediaType;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getEstensione() {
		return estensione;
	}
	public void setEstensione(String estensione) {
		this.estensione = estensione;
	}
	public String getUuidIndex() {
		return uuidIndex;
	}
	public void setUuidIndex(String uuidIndex) {
		this.uuidIndex = uuidIndex;
	}
	public String getFlEliminato() {
		return flEliminato;
	}
	public void setFlEliminato(String flEliminato) {
		this.flEliminato = flEliminato;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}

	public String getKey() {
		return key;
	}
	public String getFullKey() {
		return fullKey;
	}

	public String getLabel() {
		return label;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public void setFullKey(String fullKey) {
		this.fullKey = fullKey;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public String getFlFirmato() {
		return flFirmato;
	}
	public void setFlFirmato(String flFirmato) {
		this.flFirmato = flFirmato;
	}

	@Override
	public String toString() {
		return "AllegatoLazyEntity [idAllegato=" + idAllegato + ", formioKey=" + formioKey + ", formioNameFile=" + formioNameFile
				+ ", codiceFile=" + codiceFile + ", hashFile=" + hashFile + ", nomeFile=" + nomeFile + ", lunghezza="
				+ lunghezza + ", contentType=" + contentType + ", mediaType=" + mediaType + ", subMediaType="
				+ subMediaType + ", ipAddress=" + ipAddress + ", estensione=" + estensione + ", uuidIndex=" + uuidIndex
				+ ", flEliminato=" + flEliminato
				+ ", dataCreazione=" + dataCreazione + ", key=" + key + ", fullKey=" + fullKey + ", label=" + label 
				+ ", flFirmato=" + flFirmato + ", idIstanza=" + idIstanza + "]";
	}

	public String toStringFULL() {
		return "AllegatoLazyEntity [idAllegato=" + idAllegato + ", formioKey=" + formioKey + ", formioNameFile=" + formioNameFile
				+ ", codiceFile=" + codiceFile + ", hashFile=" + hashFile + ", nomeFile=" + nomeFile + ", lunghezza="
				+ lunghezza + ", contentType=" + contentType + ", mediaType=" + mediaType + ", subMediaType="
				+ subMediaType + ", ipAddress=" + ipAddress + ", estensione=" + estensione + ", uuidIndex=" + uuidIndex
				+ ", flEliminato=" + flEliminato
				+ ", dataCreazione=" + dataCreazione + ", key=" + key + ", fullKey=" + fullKey + ", label=" + label 
				+ ", flFirmato=" + flFirmato + ", idIstanza=" + idIstanza + "]";
	}
	
	public boolean isValid() {
		return (nomeFile != null && nomeFile.length() > 0 ) && (formioNameFile != null && formioNameFile.length() > 0 );
	}
	
}
