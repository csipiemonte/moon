/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
 * Entity relativo alla tabella moon_fo_d_ente
 * 
 */
public class Ente implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idEnte;
	private String codiceEnte;
	private String nomeEnte;
	private String descrizioneEnte;
	private Boolean flagAttivo;
	private Integer idTipoEnte;
	private String logo;
	private String indirizzo;
	private Date dataUpd;
	private String attoreUpd;
	private Long idEntePadre;
	private String codiceIpa;
	
	public Ente() {
		super();
	}
	public Ente(Long idEnte) {
		super();
		this.idEnte = idEnte;
	}
	public Ente(Ente enteToClone) {
		super();
		this.idEnte = enteToClone.getIdEnte();
		this.codiceEnte = enteToClone.getCodiceEnte();
		this.nomeEnte = enteToClone.getNomeEnte();
		this.descrizioneEnte = enteToClone.getDescrizioneEnte();
		this.flagAttivo = enteToClone.getFlAttivo();
		this.idTipoEnte = enteToClone.getIdTipoEnte();
		this.logo = enteToClone.getLogo();
		this.indirizzo = enteToClone.getIndirizzo();
		this.dataUpd = enteToClone.getDataUpd();
		this.attoreUpd = enteToClone.getAttoreUpd();
		this.idEntePadre = enteToClone.getIdEntePadre();
		this.codiceIpa = enteToClone.getCodiceIpa();
	}
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public String getNomeEnte() {
		return nomeEnte;
	}
	public void setNomeEnte(String nomeEnte) {
		this.nomeEnte = nomeEnte;
	}
	public String getDescrizioneEnte() {
		return descrizioneEnte;
	}
	public void setDescrizioneEnte(String descrizioneEnte) {
		this.descrizioneEnte = descrizioneEnte;
	}
	public Boolean getFlAttivo() {
		return flagAttivo;
	}
	public void setFlAttivo(Boolean flagAttivo) {
		this.flagAttivo = flagAttivo;
	}
	public Integer getIdTipoEnte() {
		return idTipoEnte;
	}
	public void setIdTipoEnte(Integer idTipoEnte) {
		this.idTipoEnte = idTipoEnte;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}
	public Long getIdEntePadre() {
		return idEntePadre;
	}
	public void setIdEntePadre(Long idEntePadre) {
		this.idEntePadre = idEntePadre;
	}
	public String getCodiceIpa() {
		return codiceIpa;
	}
	public void setCodiceIpa(String codiceIpa) {
		this.codiceIpa = codiceIpa;
	}
	
	@Override
	public String toString() {
		return "Ente [idEnte=" + idEnte + ", codiceEnte=" + codiceEnte + ", nomeEnte=" + nomeEnte
				+ ", descrizioneEnte=" + descrizioneEnte + ", flagAttivo=" + flagAttivo + ", idTipoEnte=" + idTipoEnte
				+ ", logo=" + logo + ", indirizzo=" + indirizzo + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd
				+ ", idEntePadre=" + idEntePadre + ", codiceIpa="  + codiceIpa + "]";
	}
	
}
