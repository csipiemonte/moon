/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.math.BigDecimal;

public class FruitoreDettaglioImporto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [implicit-camel-case]

	private String numeroAccertamento = null;
	private String codiceTipoVersamento = null;
	private BigDecimal importo = null;

	public FruitoreDettaglioImporto() {
		super();
	}
	public FruitoreDettaglioImporto(String numeroAccertamento, String codiceTipoVersamento, BigDecimal importo) {
		super();
		this.numeroAccertamento = numeroAccertamento;
		this.codiceTipoVersamento = codiceTipoVersamento;
		this.importo = importo;
	}
	/**
	 * @return the numeroAccertamento
	 */
	public String getNumeroAccertamento() {
		return numeroAccertamento;
	}
	/**
	 * @return the codiceTipoVersamento
	 */
	public String getCodiceTipoVersamento() {
		return codiceTipoVersamento;
	}
	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}
	/**
	 * @param numeroAccertamento the numeroAccertamento to set
	 */
	public void setNumeroAccertamento(String numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	/**
	 * @param codiceTipoVersamento the codiceTipoVersamento to set
	 */
	public void setCodiceTipoVersamento(String codiceTipoVersamento) {
		this.codiceTipoVersamento = codiceTipoVersamento;
	}
	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	@Override
	public String toString() {
		return "FruitoreDettaglioImporto [numeroAccertamento=" + numeroAccertamento + ", codiceTipoVersamento="
				+ codiceTipoVersamento + ", importo=" + importo + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codiceTipoVersamento == null) ? 0 : codiceTipoVersamento.hashCode());
		result = prime * result + ((importo == null) ? 0 : importo.hashCode());
		result = prime * result + ((numeroAccertamento == null) ? 0 : numeroAccertamento.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FruitoreDettaglioImporto other = (FruitoreDettaglioImporto) obj;
		if (codiceTipoVersamento == null) {
			if (other.codiceTipoVersamento != null)
				return false;
		} else if (!codiceTipoVersamento.equals(other.codiceTipoVersamento))
			return false;
		if (importo == null) {
			if (other.importo != null)
				return false;
		} else if (!importo.equals(other.importo))
			return false;
		if (numeroAccertamento == null) {
			if (other.numeroAccertamento != null)
				return false;
		} else if (!numeroAccertamento.equals(other.numeroAccertamento))
			return false;
		return true;
	}
	
	
}
