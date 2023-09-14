/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dto.ext;

import java.util.Date;

/**
 * Entity della tabella di storage dell' Iban relativo  alla raccolta dati operatori comuni per contributo costi di costruzione
 * <br>
 * <br>Tabella moon_ext_oneri_costr_iban
 * <br>PK: 
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class OneriCostrIbanEntity {

	private String codIstat;
	private String nomeComune;
	private String iban;	
	private Date dataIns;
	private Date dataUpd;
	private String attoreIns;
	private String attoreUpd;
	
	
	public String getCodIstat() {
		return codIstat;
	}
	public void setCodIstat(String codIstat) {
		this.codIstat = codIstat;
	}
	public String getNomeComune() {
		return nomeComune;
	}
	public void setNomeComune(String nomeComune) {
		this.nomeComune = nomeComune;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getAttoreIns() {
		return attoreIns;
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
	}
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}
	
	@Override
	public String toString() {
		return "OneriCostrIbanEntity [codIstat=" + codIstat + ", nomeComune=" + nomeComune + ", iban=" + iban + ", dataIns="
				+ dataIns + ", dataUpd=" + dataUpd + ", attoreIns=" + attoreIns + ", attoreUpd=" + attoreUpd + "]";
	}
		
}
