/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabella di decodifica dei portali
 * <br>
 * <br>Tabella moon_fo_d_portale
 * <br>PK: idPortale
 * <br>AK1: codicePortale
 * <br>AK2: nomePortale (il piu usato vista che ci arriva nel HttpRequest)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class PortaleEntity {

	private Long idPortale;
	private String codicePortale;
	private String nomePortale;
	private String descrizionePortale;
	private String flagAttivo;
	private Date dataUpd;
	private String attoreUpd;
	
	public PortaleEntity() {	
	}
	public PortaleEntity(Long idPortale, String codicePortale, String nomePortale, String descrizionePortale) {
		super();
		this.idPortale = idPortale;
		this.codicePortale = codicePortale;
		this.nomePortale = nomePortale;
		this.descrizionePortale = descrizionePortale;
	}
	public PortaleEntity(Long idPortale, String codicePortale, String nomePortale, String descrizionePortale,
			String flagAttivo, Date dataUpd, String attoreUpd) {
		super();
		this.idPortale = idPortale;
		this.codicePortale = codicePortale;
		this.nomePortale = nomePortale;
		this.descrizionePortale = descrizionePortale;
		this.flagAttivo = flagAttivo;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdPortale() {
		return idPortale;
	}
	public void setIdPortale(Long idPortale) {
		this.idPortale = idPortale;
	}
	public String getCodicePortale() {
		return codicePortale;
	}
	public void setCodicePortale(String codicePortale) {
		this.codicePortale = codicePortale;
	}
	public String getNomePortale() {
		return nomePortale;
	}
	public void setNomePortale(String nomePortale) {
		this.nomePortale = nomePortale;
	}
	public String getDescrizionePortale() {
		return descrizionePortale;
	}
	public void setDescrizionePortale(String descrizionePortale) {
		this.descrizionePortale = descrizionePortale;
	}
	public String getFlagAttivo() {
		return flagAttivo;
	}
	public void setFlagAttivo(String flagAttivo) {
		this.flagAttivo = flagAttivo;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attoreUpd == null) ? 0 : attoreUpd.hashCode());
		result = prime * result + ((codicePortale == null) ? 0 : codicePortale.hashCode());
		result = prime * result + ((dataUpd == null) ? 0 : dataUpd.hashCode());
		result = prime * result + ((descrizionePortale == null) ? 0 : descrizionePortale.hashCode());
		result = prime * result + ((flagAttivo == null) ? 0 : flagAttivo.hashCode());
		result = prime * result + ((idPortale == null) ? 0 : idPortale.hashCode());
		result = prime * result + ((nomePortale == null) ? 0 : nomePortale.hashCode());
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
		PortaleEntity other = (PortaleEntity) obj;
		if (attoreUpd == null) {
			if (other.attoreUpd != null)
				return false;
		} else if (!attoreUpd.equals(other.attoreUpd))
			return false;
		if (codicePortale == null) {
			if (other.codicePortale != null)
				return false;
		} else if (!codicePortale.equals(other.codicePortale))
			return false;
		if (dataUpd == null) {
			if (other.dataUpd != null)
				return false;
		} else if (!dataUpd.equals(other.dataUpd))
			return false;
		if (descrizionePortale == null) {
			if (other.descrizionePortale != null)
				return false;
		} else if (!descrizionePortale.equals(other.descrizionePortale))
			return false;
		if (flagAttivo == null) {
			if (other.flagAttivo != null)
				return false;
		} else if (!flagAttivo.equals(other.flagAttivo))
			return false;
		if (idPortale == null) {
			if (other.idPortale != null)
				return false;
		} else if (!idPortale.equals(other.idPortale))
			return false;
		if (nomePortale == null) {
			if (other.nomePortale != null)
				return false;
		} else if (!nomePortale.equals(other.nomePortale))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Portale [idPortale=" + idPortale + ", codicePortale=" + codicePortale + ", nomePortale="
				+ nomePortale + ", descrizionePortale=" + descrizionePortale + ", flagAttivo=" + flagAttivo
				+ ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}
	

}
