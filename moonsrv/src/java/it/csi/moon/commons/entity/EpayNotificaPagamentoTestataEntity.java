/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.math.BigDecimal;
import java.util.Date;

public class EpayNotificaPagamentoTestataEntity {

	Long idNotificaPagamTestata;
	String idMessaggio;
	String cfEnteCreditore;
	String codiceVersamento;
	String pagamentiSpontanei;
	Integer numeroPagamenti;
	BigDecimal importoTotalePagamenti;
	Date dataIns;
	
	public Long getIdNotificaPagamTestata() {
		return idNotificaPagamTestata;
	}
	public void setIdNotificaPagamTestata(Long idNotificaPagamTestata) {
		this.idNotificaPagamTestata = idNotificaPagamTestata;
	}
	public String getIdMessaggio() {
		return idMessaggio;
	}
	public void setIdMessaggio(String idMessaggio) {
		this.idMessaggio = idMessaggio;
	}
	public String getCfEnteCreditore() {
		return cfEnteCreditore;
	}
	public void setCfEnteCreditore(String cfEnteCreditore) {
		this.cfEnteCreditore = cfEnteCreditore;
	}
	public String getCodiceVersamento() {
		return codiceVersamento;
	}
	public void setCodiceVersamento(String codiceVersamento) {
		this.codiceVersamento = codiceVersamento;
	}
	public String getPagamentiSpontanei() {
		return pagamentiSpontanei;
	}
	public void setPagamentiSpontanei(String pagamentiSpontanei) {
		this.pagamentiSpontanei = pagamentiSpontanei;
	}
	public Integer getNumeroPagamenti() {
		return numeroPagamenti;
	}
	public void setNumeroPagamenti(Integer numeroPagamenti) {
		this.numeroPagamenti = numeroPagamenti;
	}
	public BigDecimal getImportoTotalePagamenti() {
		return importoTotalePagamenti;
	}
	public void setImportoTotalePagamenti(BigDecimal importoTotalePagamenti) {
		this.importoTotalePagamenti = importoTotalePagamenti;
	}
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	
	@Override
	public String toString() {
		return "EpayNotificaPagamentoTestataEntity [idNotificaPagamTestata=" + idNotificaPagamTestata + ", idMessaggio="
				+ idMessaggio + ", cfEnteCreditore=" + cfEnteCreditore + ", codiceVersamento=" + codiceVersamento
				+ ", pagamentiSpontanei=" + pagamentiSpontanei + ", numeroPagamenti=" + numeroPagamenti
				+ ", importoTotalePagamenti=" + importoTotalePagamenti + ", dataIns=" + dataIns + "]";
	}
	
}
