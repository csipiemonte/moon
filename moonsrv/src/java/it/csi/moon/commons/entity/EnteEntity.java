/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/*
 * Entity relativo alla tabella moon_fo_d_ente
 * 
 */
public class EnteEntity {
	
	private Long idEnte;
	private String codiceEnte;
	private String nomeEnte;
	private String descrizioneEnte;
	private String flAttivo;
	private Integer idTipoEnte;
	private String logo;
	private String indirizzo;
	private Date dataUpd;
	private String attoreUpd;
	private Long idEntePadre;
	private String codiceIpa;
	
	public EnteEntity() {
	}	
	
	public EnteEntity(Long idEnte, String codiceEnte, String nomeEnte, String descrizioneEnte, String flAttivo,
			Integer idTipoEnte, String logo, String indirizzo, Date dataUpd, String attoreUpd, Long idEntePadre,
			String codiceIpa) {
		super();
		this.idEnte = idEnte;
		this.codiceEnte = codiceEnte;
		this.nomeEnte = nomeEnte;
		this.descrizioneEnte = descrizioneEnte;
		this.flAttivo = flAttivo;
		this.idTipoEnte = idTipoEnte;
		this.logo = logo;
		this.indirizzo = indirizzo;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
		this.idEntePadre = idEntePadre;
		this.codiceIpa = codiceIpa;
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
	public String getFlAttivo() {
		return flAttivo;
	}
	public void setFlAttivo(String flAttivo) {
		this.flAttivo = flAttivo;
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
				+ ", descrizioneEnte=" + descrizioneEnte + ", flAttivo=" + flAttivo + ", idTipoEnte=" + idTipoEnte
				+ ", logo=" + logo + ", indirizzo=" + indirizzo + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd
				+ ", idEntePadre=" + idEntePadre + ", codiceIpa="  + codiceIpa + "]";
	}
	
}
