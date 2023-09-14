/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FruitorePagamento {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [implicit-camel-case]

    private String iuv;
    private String codiceAvviso;
    private String codiceVersamento;
    private BigDecimal importo;
    private Date dataRicevutaPagamento;
    private String identificativoPagamento;
    private String descrizioneEsito;
    private String codiceFiscaleEnte;
    private String causale;
    private String nome;
    private String cognome;
    private String ragioneSociale;
    private String email;
    private String codiceFiscalePartitaIVAPagatore;
    private List<FruitoreDettaglioImporto> dettaglioImporti = null;
	/**
	 * @return the iuv
	 */
	public String getIuv() {
		return iuv;
	}
	/**
	 * @return the codiceAvviso
	 */
	public String getCodiceAvviso() {
		return codiceAvviso;
	}
	/**
	 * @return the codiceVersamento
	 */
	public String getCodiceVersamento() {
		return codiceVersamento;
	}
	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}
	/**
	 * @return the dataRicevutaPagamento
	 */
	public Date getDataRicevutaPagamento() {
		return dataRicevutaPagamento;
	}
	/**
	 * @return the identificativoPagamento
	 */
	public String getIdentificativoPagamento() {
		return identificativoPagamento;
	}
	/**
	 * @return the descrizioneEsito
	 */
	public String getDescrizioneEsito() {
		return descrizioneEsito;
	}
	/**
	 * @return the codiceFiscaleEnte
	 */
	public String getCodiceFiscaleEnte() {
		return codiceFiscaleEnte;
	}
	/**
	 * @return the causale
	 */
	public String getCausale() {
		return causale;
	}
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @return the cognome
	 */
	public String getCognome() {
		return cognome;
	}
	/**
	 * @return the ragioneSociale
	 */
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the codiceFiscalePartitaIVAPagatore
	 */
	public String getCodiceFiscalePartitaIVAPagatore() {
		return codiceFiscalePartitaIVAPagatore;
	}
	/**
	 * @return the dettaglioImporti
	 */
	public List<FruitoreDettaglioImporto> getDettaglioImporti() {
		return dettaglioImporti;
	}
	/**
	 * @param iuv the iuv to set
	 */
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	/**
	 * @param codiceAvviso the codiceAvviso to set
	 */
	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}
	/**
	 * @param codiceVersamento the codiceVersamento to set
	 */
	public void setCodiceVersamento(String codiceVersamento) {
		this.codiceVersamento = codiceVersamento;
	}
	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	/**
	 * @param dataRicevutaPagamento the dataRicevutaPagamento to set
	 */
	public void setDataRicevutaPagamento(Date dataRicevutaPagamento) {
		this.dataRicevutaPagamento = dataRicevutaPagamento;
	}
	/**
	 * @param identificativoPagamento the identificativoPagamento to set
	 */
	public void setIdentificativoPagamento(String identificativoPagamento) {
		this.identificativoPagamento = identificativoPagamento;
	}
	/**
	 * @param descrizioneEsito the descrizioneEsito to set
	 */
	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}
	/**
	 * @param codiceFiscaleEnte the codiceFiscaleEnte to set
	 */
	public void setCodiceFiscaleEnte(String codiceFiscaleEnte) {
		this.codiceFiscaleEnte = codiceFiscaleEnte;
	}
	/**
	 * @param causale the causale to set
	 */
	public void setCausale(String causale) {
		this.causale = causale;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @param cognome the cognome to set
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	/**
	 * @param ragioneSociale the ragioneSociale to set
	 */
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @param codiceFiscalePartitaIVAPagatore the codiceFiscalePartitaIVAPagatore to set
	 */
	public void setCodiceFiscalePartitaIVAPagatore(String codiceFiscalePartitaIVAPagatore) {
		this.codiceFiscalePartitaIVAPagatore = codiceFiscalePartitaIVAPagatore;
	}
	/**
	 * @param dettaglioImporti the dettaglioImporti to set
	 */
	public void setDettaglioImporti(List<FruitoreDettaglioImporto> dettaglioImporti) {
		this.dettaglioImporti = dettaglioImporti;
	}
	
	@Override
	public String toString() {
		return "FruitorePagamento [iuv=" + iuv + ", codiceAvviso=" + codiceAvviso + ", codiceVersamento="
				+ codiceVersamento + ", importo=" + importo + ", dataRicevutaPagamento=" + dataRicevutaPagamento
				+ ", identificativoPagamento=" + identificativoPagamento + ", descrizioneEsito=" + descrizioneEsito
				+ ", codiceFiscaleEnte=" + codiceFiscaleEnte + ", causale=" + causale + ", nome=" + nome + ", cognome="
				+ cognome + ", ragioneSociale=" + ragioneSociale + ", email=" + email
				+ ", codiceFiscalePartitaIVAPagatore=" + codiceFiscalePartitaIVAPagatore + ", dettaglioImporti="
				+ dettaglioImporti + "]";
	}

}
