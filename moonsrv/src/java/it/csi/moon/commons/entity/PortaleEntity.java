/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

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
	public String toString() {
		return "Portale [idPortale=" + idPortale + ", codicePortale=" + codicePortale + ", nomePortale="
				+ nomePortale + ", descrizionePortale=" + descrizionePortale + ", flagAttivo=" + flagAttivo
				+ ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}
	
}
