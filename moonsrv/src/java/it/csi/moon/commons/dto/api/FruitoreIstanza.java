/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FruitoreIstanza {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [implicit-camel-case]
	
	
	public FruitoreIstanza() {
		super();
		
	}

	public FruitoreIstanza(String codice, String codiceFiscaleDichiarante, String cognomeDichiarante,
			String nomeDichiarante, Date dataCreazione, FruitoreStato stato) {
		super();
		this.codice = codice;
		this.codiceFiscaleDichiarante = codiceFiscaleDichiarante;
		this.cognomeDichiarante = cognomeDichiarante;
		this.nomeDichiarante = nomeDichiarante;
		this.dataCreazione = dataCreazione;
		this.stato = stato;
	}
	private String codice = null;
	private String codiceFiscaleDichiarante = null;
	private String cognomeDichiarante = null;
	private String nomeDichiarante = null;
	private Date dataCreazione = null;
	private FruitoreStato stato = null;
	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
	/**
	 * @return the codiceFiscaleDichiarante
	 */
	public String getCodiceFiscaleDichiarante() {
		return codiceFiscaleDichiarante;
	}
	/**
	 * @return the cognomeDichiarante
	 */
	public String getCognomeDichiarante() {
		return cognomeDichiarante;
	}
	/**
	 * @return the nomeDichiarante
	 */
	public String getNomeDichiarante() {
		return nomeDichiarante;
	}
	/**
	 * @return the dataCreazione
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataCreazione() {
		return dataCreazione;
	}
	/**
	 * @return the stato
	 */
	public FruitoreStato getStato() {
		return stato;
	}
	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}
	/**
	 * @param codiceFiscaleDichiarante the codiceFiscaleDichiarante to set
	 */
	public void setCodiceFiscaleDichiarante(String codiceFiscaleDichiarante) {
		this.codiceFiscaleDichiarante = codiceFiscaleDichiarante;
	}
	/**
	 * @param cognomeDichiarante the cognomeDichiarante to set
	 */
	public void setCognomeDichiarante(String cognomeDichiarante) {
		this.cognomeDichiarante = cognomeDichiarante;
	}
	/**
	 * @param nomeDichiarante the nomeDichiarante to set
	 */
	public void setNomeDichiarante(String nomeDichiarante) {
		this.nomeDichiarante = nomeDichiarante;
	}
	/**
	 * @param dataCreazione the dataCreazione to set
	 */
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	/**
	 * @param stato the stato to set
	 */
	public void setStato(FruitoreStato stato) {
		this.stato = stato;
	}
	
	@Override
	public String toString() {
		return "FruitoreIstanza [codice=" + codice + ", codiceFiscaleDichiarante=" + codiceFiscaleDichiarante
				+ ", cognomeDichiarante=" + cognomeDichiarante + ", nomeDichiarante=" + nomeDichiarante
				+ ", dataCreazione=" + dataCreazione + ", stato=" + stato + "]";
	}
	
}
