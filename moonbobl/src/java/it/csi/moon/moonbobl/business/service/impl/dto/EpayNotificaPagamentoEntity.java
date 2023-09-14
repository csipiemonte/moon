/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity della tabella delle notificahe di pagamento effettuati su EPAY
 * <br>
 * <br>Tabella moon_ep_t_notifica_pagamento
 * <br>PK: idNotificaPagamamento
 * <br>AK: iuv identificativo di business inviato nella richiesta di pagamento
 * 
 * @author Laurent
 */
public class EpayNotificaPagamentoEntity {

	Long idNotificaPagamento;
	Long idNotificaPagamTesta;
	String idPosizioneDebitoria;
	Integer annoDiRiferimento;
	String iuv;
	BigDecimal importoPagato;
	Date dataScadenza;
	
	String descCausaleVersamento;
	Date dataEsitoPagamento;
	String soggettoDebitore;
	String soggettoVersante;
	String datiTransazionePsp;
	String datiSpecificiRiscossione;
	String note;
	String codiceAvviso;
	Date dataIns;
	
	public Long getIdNotificaPagamento() {
		return idNotificaPagamento;
	}
	public void setIdNotificaPagamento(Long idNotificaPagamento) {
		this.idNotificaPagamento = idNotificaPagamento;
	}
	public Long getIdNotificaPagamTesta() {
		return idNotificaPagamTesta;
	}
	public void setIdNotificaPagamTesta(Long idNotificaPagamTesta) {
		this.idNotificaPagamTesta = idNotificaPagamTesta;
	}
	public String getIdPosizioneDebitoria() {
		return idPosizioneDebitoria;
	}
	public void setIdPosizioneDebitoria(String idPosizioneDebitoria) {
		this.idPosizioneDebitoria = idPosizioneDebitoria;
	}
	public Integer getAnnoDiRiferimento() {
		return annoDiRiferimento;
	}
	public void setAnnoDiRiferimento(Integer annoDiRiferimento) {
		this.annoDiRiferimento = annoDiRiferimento;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public BigDecimal getImportoPagato() {
		return importoPagato;
	}
	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public String getDescCausaleVersamento() {
		return descCausaleVersamento;
	}
	public void setDescCausaleVersamento(String descCausaleVersamento) {
		this.descCausaleVersamento = descCausaleVersamento;
	}
	public Date getDataEsitoPagamento() {
		return dataEsitoPagamento;
	}
	public void setDataEsitoPagamento(Date dataEsitoPagamento) {
		this.dataEsitoPagamento = dataEsitoPagamento;
	}
	public String getSoggettoDebitore() {
		return soggettoDebitore;
	}
	public void setSoggettoDebitore(String soggettoDebitore) {
		this.soggettoDebitore = soggettoDebitore;
	}
	public String getSoggettoVersante() {
		return soggettoVersante;
	}
	public void setSoggettoVersante(String soggettoVersante) {
		this.soggettoVersante = soggettoVersante;
	}
	public String getDatiTransazionePsp() {
		return datiTransazionePsp;
	}
	public void setDatiTransazionePsp(String datiTransazionePSP) {
		this.datiTransazionePsp = datiTransazionePSP;
	}
	public String getDatiSpecificiRiscossione() {
		return datiSpecificiRiscossione;
	}
	public void setDatiSpecificiRiscossione(String datiSpecificiRiscossione) {
		this.datiSpecificiRiscossione = datiSpecificiRiscossione;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCodiceAvviso() {
		return codiceAvviso;
	}
	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	
	@Override
	public String toString() {
		return "EpayNotificaPagamentoEntity [idNotificaPagamento=" + idNotificaPagamento
				+ ", idNotificaPagamTesta=" + idNotificaPagamTesta + ", idPosizioneDebitoria="
				+ idPosizioneDebitoria + ", annoDiRiferimento=" + annoDiRiferimento + ", iuv=" + iuv
				+ ", importoPagato=" + importoPagato + ", dataScadenza=" + dataScadenza + ", descCausaleVersamento="
				+ descCausaleVersamento + ", dataEsitoPagamento=" + dataEsitoPagamento + ", soggettoDebitore="
				+ soggettoDebitore + ", soggettoVersante=" + soggettoVersante + ", datiTransazionePsp="
				+ datiTransazionePsp + ", datiSpecificiRiscossione=" + datiSpecificiRiscossione + ", note=" + note
				+ ", codiceAvviso=" + codiceAvviso + ", dataIns=" + dataIns + "]";
	}
	
}
