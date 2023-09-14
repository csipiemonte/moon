/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella dei file allegati delle istanze
 * <br>
 * <br>Tabella moon_fo_t_file
 * <br>PK: idFile 
 * <br>Usato per salvare gli file allegati
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class FileEntity {

	private Long idFile = null;
	private String codiceFile = null;
	private String hashFile = null;
	private String nomeFile = null;
	private byte[] contenutoFile = null;
	private String contentType = null;
	private String estensione = null;
	private String uuidIndex = null;
	private Date dataCreazione = null;
	
	public FileEntity() {
	}
	
	public FileEntity(Long idFile, String codiceFile, String hashFile, String nomeFile, byte[] contenutoFile,
			String contentType, String estensione, String uuidIndex, Date dataCreazione) {
		super();
		this.idFile = idFile;
		this.codiceFile = codiceFile;
		this.hashFile = hashFile;
		this.nomeFile = nomeFile;
		this.contenutoFile = contenutoFile;
		this.contentType = contentType;
		this.estensione = estensione;
		this.uuidIndex = uuidIndex;
		this.dataCreazione = dataCreazione;
	}

	public Long getIdFile() {
		return idFile;
	}

	public void setIdFile(Long idFile) {
		this.idFile = idFile;
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

	public byte[] getContenutoFile() {
		return contenutoFile;
	}

	public void setContenutoFile(byte[] contenutoFile) {
		this.contenutoFile = contenutoFile;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
}
