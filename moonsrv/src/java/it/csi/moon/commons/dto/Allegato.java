/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;
/*
 * Rappresenta allegato inviato al servizio upload
 * 
 * 
 */

import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Allegato {
	
	private Long idAllegato = null;
	private String formioKey = null;
	private String formioNameFile = null;
	private String formioDir = null;
	private String codiceFile = null;
	private String hashFile = null;
	private String nomeFile = null;
	private Integer lunghezza = null;
	private byte[] contenuto = null;
	private String contentType = null;
	private String mediaType = null;
	private String mediaSubType = null;
	private String ipAddress = null;
	private String estensione = null;
	private String uuidIndex = null;
	private Date dataCreazione = null;
	private Long idIstanza = null;
	private String key = null;
	private String fullKey = null;
	private String label = null;
	private boolean flagFirmato = false;
	
	public Allegato() {
		super();
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
	public byte[] getContenuto() {
		return contenuto;
	}
	public void setContenuto(byte[] contenuto) {
		this.contenuto = contenuto;
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
	public String getMediaSubType() {
		return mediaSubType;
	}
	public void setMediaSubType(String mediaSubType) {
		this.mediaSubType = mediaSubType;
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
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
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

	public boolean isFlagFirmato() {
		return flagFirmato;
	}
	public void setFlagFirmato(boolean flagFirmato) {
		this.flagFirmato = flagFirmato;
	}

	@Override
	public String toString() {
		return "Allegato senzaContenuto [idAllegato=" + idAllegato + ", formioKey=" + formioKey + ", formioNameFile=" + formioNameFile
				+ ", codiceFile=" + codiceFile + ", hashFile=" + hashFile + ", nomeFile=" + nomeFile + ", lunghezza="
				+ lunghezza + ", contentType=" + contentType + ", mediaType=" + mediaType + ", mediaSubType="
				+ mediaSubType + ", ipAddress=" + ipAddress + ", estensione=" + estensione + ", uuidIndex=" + uuidIndex
				+ ", dataCreazione=" + dataCreazione + ", key=" + key + ", fullKey=" + fullKey + ", label=" + label 
				+ ", contenuto=" + ((contenuto==null)?"null":(Arrays.toString(contenuto).substring(0, 10)+"... len="+contenuto.length))
				+ ", flagFirmato=" + flagFirmato + "]";
	}

	public String toStringFULL() {
		return "Allegato senzaContenuto [idAllegato=" + idAllegato + ", formioKey=" + formioKey + ", formioNameFile=" + formioNameFile
				+ ", codiceFile=" + codiceFile + ", hashFile=" + hashFile + ", nomeFile=" + nomeFile + ", lunghezza="
				+ lunghezza + ", contentType=" + contentType + ", mediaType=" + mediaType + ", mediaSubType="
				+ mediaSubType + ", ipAddress=" + ipAddress + ", estensione=" + estensione + ", uuidIndex=" + uuidIndex
				+ ", dataCreazione=" + dataCreazione + ", key=" + key + ", fullKey=" + fullKey + ", label=" + label 
				+ ", contenuto=" + contenuto
				+ ", flagFirmato=" + flagFirmato + "]";
	}
	
	public boolean isValid() {
		return (contenuto != null && contenuto.length > 0) && (nomeFile != null && nomeFile.length() > 0 ) && (formioNameFile != null && formioNameFile.length() > 0 );
	}
}
