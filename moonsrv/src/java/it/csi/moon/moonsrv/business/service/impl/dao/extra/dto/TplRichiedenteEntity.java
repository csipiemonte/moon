/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dto;

import java.util.Date;

public class TplRichiedenteEntity {

	Integer idRichiedente;
	String cfPiva;
	String nome;
	String cognome;
	String denominazione;
	String codiceVoucher;
	String tipoAbbonamento;
	Date dataRichiesta;
	Date dataScadenza;
	String cfUtilizzatore;
	String statoUtilizzoVoucher;
	String statoUtilizzoAbbonamento;
	
	public final Integer getIdRichiedente() {
		return idRichiedente;
	}
	public final String getCfPiva() {
		return cfPiva;
	}
	public final String getNome() {
		return nome;
	}
	public final String getCognome() {
		return cognome;
	}
	public final String getDenominazione() {
		return denominazione;
	}
	public final String getCodiceVoucher() {
		return codiceVoucher;
	}
	public final String getTipoAbbonamento() {
		return tipoAbbonamento;
	}
	public final Date getDataRichiesta() {
		return dataRichiesta;
	}
	public final Date getDataScadenza() {
		return dataScadenza;
	}
	public final String getCfUtilizzatore() {
		return cfUtilizzatore;
	}
	public final String getStatoUtilizzoVoucher() {
		return statoUtilizzoVoucher;
	}
	public final String getStatoUtilizzoAbbonamento() {
		return statoUtilizzoAbbonamento;
	}
	public final void setIdRichiedente(Integer idRichiedente) {
		this.idRichiedente = idRichiedente;
	}
	public final void setCfPiva(String cfPiva) {
		this.cfPiva = cfPiva;
	}
	public final void setNome(String nome) {
		this.nome = nome;
	}
	public final void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public final void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public final void setCodiceVoucher(String codiceVoucher) {
		this.codiceVoucher = codiceVoucher;
	}
	public final void setTipoAbbonamento(String tipoAbbonamento) {
		this.tipoAbbonamento = tipoAbbonamento;
	}
	public final void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}
	public final void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public final void setCfUtilizzatore(String cfUtilizzatore) {
		this.cfUtilizzatore = cfUtilizzatore;
	}
	public final void setStatoUtilizzoVoucher(String statoUtilizzoVoucher) {
		this.statoUtilizzoVoucher = statoUtilizzoVoucher;
	}
	public final void setStatoUtilizzoAbbonamento(String statoUtilizzoAbbonamento) {
		this.statoUtilizzoAbbonamento = statoUtilizzoAbbonamento;
	}
	
	@Override
	public String toString() {
		return "TplRichiedenteEntity [idRichiedente=" + idRichiedente + ", cfPiva=" + cfPiva + ", nome=" + nome
				+ ", cognome=" + cognome + ", denominazione=" + denominazione + ", codiceVoucher=" + codiceVoucher
				+ ", tipoAbbonamento=" + tipoAbbonamento + ", dataRichiesta=" + dataRichiesta + ", dataScadenza="
				+ dataScadenza + ", cfUtilizzatore=" + cfUtilizzatore + ", statoUtilizzoVoucher=" + statoUtilizzoVoucher
				+ ", statoUtilizzoAbbonamento=" + statoUtilizzoAbbonamento + "]";
	}
	
}
