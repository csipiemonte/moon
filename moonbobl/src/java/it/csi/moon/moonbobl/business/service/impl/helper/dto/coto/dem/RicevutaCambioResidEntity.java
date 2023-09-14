/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.dem;

/**
 * Entity per la creazione della ricevuta di cambio indirizzo
 * <br>
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class RicevutaCambioResidEntity {

	private String richiedente;
	private String indirizzo;
	private int numeroComponenti;
	private String dataPresentazione;

	private String operatore;
	private String funzionario;
	private String numeroPraticaNao;
	private String dataRegistrazione;
	private String telefono;
	private String fax;

			
	public String getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(String richiedente) {
		this.richiedente = richiedente;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public int getNumeroComponenti() {
		return numeroComponenti;
	}

	public void setNumeroComponenti(int numeroComponenti) {
		this.numeroComponenti = numeroComponenti;
	}

	public String getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(String dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public String getFunzionario() {
		return funzionario;
	}

	public void setFunzionario(String funzionario) {
		this.funzionario = funzionario;
	}

	public String getNumeroPraticaNao() {
		return numeroPraticaNao;
	}

	public void setNumeroPraticaNao(String numeroPraticaNao) {
		this.numeroPraticaNao = numeroPraticaNao;
	}

	public String getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Override
	public String toString() {
		return "RicevutaCambioResidEntity [richiedente=" + richiedente + ", indirizzo=" + indirizzo
				+ ", numeroComponenti=" + numeroComponenti + ", dataPresentazione=" + dataPresentazione + ", operatore="
				+ operatore + ", funzionario=" + funzionario + ", numeroPraticaNao=" + numeroPraticaNao
				+ ", dataRegistrazione=" + dataRegistrazione + ", telefono=" + telefono + ", fax=" + fax + "]";
	}
		
}
