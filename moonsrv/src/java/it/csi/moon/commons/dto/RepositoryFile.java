/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Arrays;
import java.util.Date;

/**
 * Repository File (moon_fo_t_repository_file)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public class RepositoryFile {
	
	private Long idFile;
	private String nomeFile;
	private Boolean flEliminato;
	private Date dataCreazione;
	private Long idIstanza;
	private String formioKey;
	private String formioNameFile;
	private String codiceFile;
	private String hashFile;
	private String contentType;
	private TipoRepositoryFile tipologia;
	private Integer lunghezza;
	private String descrizione;
	private Long idStoricoWorkflow; 
	private Boolean flFirmato;
	private String tipologiaFruitore;
	private String refUrl;
	private String numeroProtocollo;
	private Date dataProtocollo;
	private String uuidIndex;
	private String key;
	private String fullKey;
	private String label;
	private Long idTipologiaMydocs;
	private byte[] contenuto;
	
	public Long getIdFile() {
		return idFile;
	}
	public void setIdFile(Long idFile) {
		this.idFile = idFile;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public Boolean getFlEliminato() {
		return flEliminato;
	}
	public void setFlEliminato(Boolean flEliminato) {
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
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public TipoRepositoryFile getTipologia() {
		return tipologia;
	}
	public void setTipologia(TipoRepositoryFile tipologia) {
		this.tipologia = tipologia;
	}
	public Integer getLunghezza() {
		return lunghezza;
	}
	public void setLunghezza(Integer lunghezza) {
		this.lunghezza = lunghezza;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public Boolean getFlFirmato() {
		return flFirmato;
	}
	public void setFlFirmato(Boolean flFirmato) {
		this.flFirmato = flFirmato;
	}
	public String getTipologiaFruitore() {
		return tipologiaFruitore;
	}
	public void setTipologiaFruitore(String tipologiaFruitore) {
		this.tipologiaFruitore = tipologiaFruitore;
	}
	public String getRefUrl() {
		return refUrl;
	}
	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	public Date getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	public String getUuidIndex() {
		return uuidIndex;
	}
	public void setUuidIndex(String uuidIndex) {
		this.uuidIndex = uuidIndex;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getFullKey() {
		return fullKey;
	}
	public void setFullKey(String fullKey) {
		this.fullKey = fullKey;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Long getIdTipologiaMydocs() {
		return idTipologiaMydocs;
	}
	public void setIdTipologiaMydocs(Long idTipologiaMydocs) {
		this.idTipologiaMydocs = idTipologiaMydocs;
	}
	public byte[] getContenuto() {
		return contenuto;
	}
	public void setContenuto(byte[] contenuto) {
		this.contenuto = contenuto;
	}
	
	@Override
	public String toString() {
		return "RepositoryFile [idFile=" + idFile + ", nomeFile=" + nomeFile + ", flEliminato=" + flEliminato
				+ ", dataCreazione=" + dataCreazione + ", idIstanza=" + idIstanza + ", formioKey=" + formioKey
				+ ", formioNameFile=" + formioNameFile + ", codiceFile=" + codiceFile + ", hashFile=" + hashFile
				+ ", contentType=" + contentType + ", tipologia=" + tipologia + ", lunghezza=" + lunghezza
				+ ", descrizione=" + descrizione + ", idStoricoWorkflow=" + idStoricoWorkflow + ", flFirmato="
				+ flFirmato + ", tipologiaFruitore=" + tipologiaFruitore + ", refUrl=" + refUrl + ", numeroProtocollo="
				+ numeroProtocollo + ", dataProtocollo=" + dataProtocollo + ", uuidIndex=" + uuidIndex + ", key=" + key
				+ ", fullKey=" + fullKey + ", label=" + label + ", idTipologiaMydocs=" + idTipologiaMydocs
				+ ", contenuto=" + Arrays.toString(contenuto) + "]";
	}
	

}
