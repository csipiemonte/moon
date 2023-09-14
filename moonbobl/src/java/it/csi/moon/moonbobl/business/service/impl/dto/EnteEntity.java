/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attoreUpd == null) ? 0 : attoreUpd.hashCode());
		result = prime * result + ((codiceEnte == null) ? 0 : codiceEnte.hashCode());
		result = prime * result + ((dataUpd == null) ? 0 : dataUpd.hashCode());
		result = prime * result + ((descrizioneEnte == null) ? 0 : descrizioneEnte.hashCode());
		result = prime * result + ((flAttivo == null) ? 0 : flAttivo.hashCode());
		result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
		result = prime * result + ((idEntePadre == null) ? 0 : idEntePadre.hashCode());
		result = prime * result + ((idTipoEnte == null) ? 0 : idTipoEnte.hashCode());
		result = prime * result + ((indirizzo == null) ? 0 : indirizzo.hashCode());
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
		result = prime * result + ((nomeEnte == null) ? 0 : nomeEnte.hashCode());
		result = prime * result + ((codiceIpa == null) ? 0 : codiceIpa.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnteEntity other = (EnteEntity) obj;
		if (attoreUpd == null) {
			if (other.attoreUpd != null)
				return false;
		} else if (!attoreUpd.equals(other.attoreUpd))
			return false;
		if (codiceEnte == null) {
			if (other.codiceEnte != null)
				return false;
		} else if (!codiceEnte.equals(other.codiceEnte))
			return false;
		if (dataUpd == null) {
			if (other.dataUpd != null)
				return false;
		} else if (!dataUpd.equals(other.dataUpd))
			return false;
		if (descrizioneEnte == null) {
			if (other.descrizioneEnte != null)
				return false;
		} else if (!descrizioneEnte.equals(other.descrizioneEnte))
			return false;
		if (flAttivo == null) {
			if (other.flAttivo != null)
				return false;
		} else if (!flAttivo.equals(other.flAttivo))
			return false;
		if (idEnte == null) {
			if (other.idEnte != null)
				return false;
		} else if (!idEnte.equals(other.idEnte))
			return false;
		if (idEntePadre == null) {
			if (other.idEntePadre != null)
				return false;
		} else if (!idEntePadre.equals(other.idEntePadre))
			return false;
		if (idTipoEnte == null) {
			if (other.idTipoEnte != null)
				return false;
		} else if (!idTipoEnte.equals(other.idTipoEnte))
			return false;
		if (indirizzo == null) {
			if (other.indirizzo != null)
				return false;
		} else if (!indirizzo.equals(other.indirizzo))
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		if (nomeEnte == null) {
			if (other.nomeEnte != null)
				return false;
		} else if (!nomeEnte.equals(other.nomeEnte))
			return false;
		if (codiceIpa == null) {
			if (other.codiceIpa != null)
				return false;
		} else if (!codiceIpa.equals(other.codiceIpa))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Ente [idEnte=" + idEnte + ", codiceEnte=" + codiceEnte + ", nomeEnte=" + nomeEnte
				+ ", descrizioneEnte=" + descrizioneEnte + ", flAttivo=" + flAttivo + ", idTipoEnte=" + idTipoEnte
				+ ", logo=" + logo + ", indirizzo=" + indirizzo + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd
				+ ", idEntePadre=" + idEntePadre + ", codiceIpa="  + codiceIpa + "]";
	}
	
}
