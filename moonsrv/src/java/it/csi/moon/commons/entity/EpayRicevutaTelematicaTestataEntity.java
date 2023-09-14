/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

public class EpayRicevutaTelematicaTestataEntity {

	Long idRicevutaTelemTestata;
	String idMessaggio;
	String cfEnteCreditore;
	String codiceVersamento;
	Integer numeroRt;
	Date dataIns;
	
	public Long getIdRicevutaTelemTestata() {
		return idRicevutaTelemTestata;
	}
	public void setIdRicevutaTelemTestata(Long idRicevutaTelemTestata) {
		this.idRicevutaTelemTestata = idRicevutaTelemTestata;
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
	public Integer getNumeroRt() {
		return numeroRt;
	}
	public void setNumeroRt(Integer numeroRt) {
		this.numeroRt = numeroRt;
	}
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	
	@Override
	public String toString() {
		return "EpayRicevutaTelematicaTestataEntity [idRicevutaTelemTestata=" + idRicevutaTelemTestata
				+ ", idMessaggio=" + idMessaggio + ", cfEnteCreditore=" + cfEnteCreditore + ", codiceVersamento="
				+ codiceVersamento + ", numeroRt=" + numeroRt + ", dataIns=" + dataIns + "]";
	}

}
