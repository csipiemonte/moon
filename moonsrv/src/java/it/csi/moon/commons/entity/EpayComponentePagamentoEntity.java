/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.math.BigDecimal;

/**
 * Entity della tabella delle componente pagamento EPAY
 * <br>
 * <br>Tabella moon_ep_t_componente_pagamento
 * <br>PK: id_componente_pagamento
 * 
 * 
 * @author Danilo
 */
public class EpayComponentePagamentoEntity {

	private Long idComponentePagamento;
	private Long idModulo;
	private String annoAccertamento;
	private String numeroAccertamento;
	private String causale;
	private BigDecimal importo;
	private String datiSpecificiRiscossione;
	private String codiceTipoVersamento;
	private Long ordine;
	/**
	 * @return the idComponentePagamento
	 */
	public Long getIdComponentePagamento() {
		return idComponentePagamento;
	}
	/**
	 * @return the idModulo
	 */
	public Long getIdModulo() {
		return idModulo;
	}
	/**
	 * @return the annoAccertamento
	 */
	public String getAnnoAccertamento() {
		return annoAccertamento;
	}
	/**
	 * @return the numeroAccertamento
	 */
	public String getNumeroAccertamento() {
		return numeroAccertamento;
	}
	/**
	 * @return the causale
	 */
	public String getCausale() {
		return causale;
	}
	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}
	/**
	 * @return the datiSpecificiRiscossione
	 */
	public String getDatiSpecificiRiscossione() {
		return datiSpecificiRiscossione;
	}
	/**
	 * @return the codiceTipoVersamento
	 */
	public String getCodiceTipoVersamento() {
		return codiceTipoVersamento;
	}
	/**
	 * @param idComponentePagamento the idComponentePagamento to set
	 */
	public void setIdComponentePagamento(Long idComponentePagamento) {
		this.idComponentePagamento = idComponentePagamento;
	}
	/**
	 * @param idModulo the idModulo to set
	 */
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	/**
	 * @param annoAccertamento the annoAccertamento to set
	 */
	public void setAnnoAccertamento(String annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}
	/**
	 * @param numeroAccertamento the numeroAccertamento to set
	 */
	public void setNumeroAccertamento(String numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	/**
	 * @param causale the causale to set
	 */
	public void setCausale(String causale) {
		this.causale = causale;
	}
	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	/**
	 * @param datiSpecificiRiscossione the datiSpecificiRiscossione to set
	 */
	public void setDatiSpecificiRiscossione(String datiSpecificiRiscossione) {
		this.datiSpecificiRiscossione = datiSpecificiRiscossione;
	}
	/**
	 * @param codiceTipoVersamento the codiceTipoVersamento to set
	 */
	public void setCodiceTipoVersamento(String codiceTipoVersamento) {
		this.codiceTipoVersamento = codiceTipoVersamento;
	}

	/**
	 * @return the ordine
	 */
	public Long getOrdine() {
		return ordine;
	}
	/**
	 * @param ordine the ordine to set
	 */
	public void setOrdine(Long ordine) {
		this.ordine = ordine;
	}
	@Override
	public String toString() {
		return "EpayComponentePagamentoEntity [idComponentePagamento=" + idComponentePagamento + ", idModulo="
				+ idModulo + ", annoAccertamento=" + annoAccertamento + ", numeroAccertamento=" + numeroAccertamento
				+ ", causale=" + causale + ", importo=" + importo + ", datiSpecificiRiscossione="
				+ datiSpecificiRiscossione + ", codiceTipoVersamento=" + codiceTipoVersamento + ", ordine=" + ordine
				+ "]";
	}
	
}
