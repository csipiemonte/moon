/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

public class FruitoreAllegato {

	private String formioNameFile = null;
	private String codiceFile = null;
	private String nomeFile = null;
	private Integer lunghezza = null;
	private String contentType = null;
	private String mediaType = null;
	private String subMediaType = null;
	private String estensione = null;
	private String uuidIndex = null;

	public String getFormioNameFile() {
		return formioNameFile;
	}

	public void setFormioNameFile(String formioNameFile) {
		this.formioNameFile = formioNameFile;
	}

	public String getCodiceFile() {
		return codiceFile;
	}

	public void setCodiceFile(String codiceFile) {
		this.codiceFile = codiceFile;
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

}
