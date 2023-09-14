/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella di decodifica degli ambiti (padre delle categorie)
 * <br>
 * <br>Tabella moon_fo_d_ambito
 * <br>PK: idAmbito
 * <br>AK: codiceAmbito
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class AmbitoEntity {

	private Integer idAmbito = null;
	private String codiceAmbito = null;
	private String nomeAmbito = null;
	private Integer idVisibilitaAmbito = null;
	private String color = null;
	private Date dataUpd;
	private String attoreUpd;
	
	public Integer getIdAmbito() {
		return idAmbito;
	}
	public void setIdAmbito(Integer idAmbito) {
		this.idAmbito = idAmbito;
	}
	public String getCodiceAmbito() {
		return codiceAmbito;
	}
	public void setCodiceAmbito(String codiceAmbito) {
		this.codiceAmbito = codiceAmbito;
	}
	public String getNomeAmbito() {
		return nomeAmbito;
	}
	public void setNomeAmbito(String nomeAmbito) {
		this.nomeAmbito = nomeAmbito;
	}
	public Integer getIdVisibilitaAmbito() {
		return idVisibilitaAmbito;
	}
	public void setIdVisibilitaAmbito(Integer idVisibilitaAmbito) {
		this.idVisibilitaAmbito = idVisibilitaAmbito;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
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
		return "AmbitoEntity [idAmbito=" + idAmbito + ", codiceAmbito=" + codiceAmbito + ", nomeAmbito=" + nomeAmbito
				+ ", idVisibilitaAmbito=" + idVisibilitaAmbito + ", color=" + color + ", dataUpd=" + dataUpd
				+ ", attoreUpd=" + attoreUpd + "]";
	}

}