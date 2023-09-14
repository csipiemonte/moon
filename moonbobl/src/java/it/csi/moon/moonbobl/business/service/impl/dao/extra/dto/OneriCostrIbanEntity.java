/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.dto;

import java.util.Date;

/**
 * Entity della tabella instanza relativa alle richieste di contributi di Oneri Construzione
 * <br>
 * <br>Tabella moon_ext_oneri_costr_domanda
 * <br>PK: idExtDomanda
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class OneriCostrIbanEntity {

	private String codIstat;
	private String nomeComune;
	private String iban;
	private Date dataIns;
	private String attoreIns;
	private Date dataUpd;
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
	public String getAttoreIns() {
		return attoreIns;
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
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
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}

	
	@Override
	public String toString() {
		return "OneriCostrDomandaEntity [" +
				"codIstat=" + codIstat + 
				", nomeComune=" + nomeComune + 
				", iban="+ iban + 
				", dataIns=" + dataIns + ", dataUpd=" + dataUpd +
				", attoreUpd=" + attoreUpd + ", attoreIns=" + attoreIns + "]";
	}
		
}
