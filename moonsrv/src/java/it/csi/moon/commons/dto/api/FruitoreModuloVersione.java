/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.Date;
import java.util.List;

import it.csi.moon.commons.dto.Categoria;
import it.csi.moon.commons.dto.StatoModulo;

public class FruitoreModuloVersione {

	private String codice = null;
	private String versione = null;
	private String oggetto = null;
	private String descrizione = null;
	private Date dataIns = null;
	private Date dataUpd = null;
	private Categoria categoria = null;
	private StatoModulo statoAttuale = null;
	private List<StatoModulo> stati = null;
	private List<String> versioni = null;
	  
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getVersione() {
		return versione;
	}
	public void setVersione(String versione) {
		this.versione = versione;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public StatoModulo getStatoAttuale() {
		return statoAttuale;
	}
	public void setStatoAttuale(StatoModulo statoAttuale) {
		this.statoAttuale = statoAttuale;
	}
	public List<StatoModulo> getStati() {
		return stati;
	}
	public void setStati(List<StatoModulo> stati) {
		this.stati = stati;
	}
	public List<String> getVersioni() {
		return versioni;
	}
	public void setVersioni(List<String> versioni) {
		this.versioni = versioni;
	}
	
	@Override
	public String toString() {
		return "FruitoreModuloVersione [codice=" + codice + ", versione=" + versione + ", oggetto=" + oggetto
				+ ", descrizione=" + descrizione + ", dataIns=" + dataIns + ", dataUpd=" + dataUpd
				+ ", categoria=" + categoria + ", statoAttuale=" + statoAttuale + ", stati=" + stati
				+ ", versioni=" + versioni + "]";
	}
	
}
