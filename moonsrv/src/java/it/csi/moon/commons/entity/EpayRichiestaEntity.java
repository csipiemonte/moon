/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella delle richieste di pagamento effettuati su EPAY
 * <br>
 * <br>Tabella moon_ep_t_richiesta
 * <br>PK: idRichiesta
 * <br>AK: idEpay identificativo di business inviato nella richiesta CODICEISTANZA_TIMESTAMP_LINEACLIENTE
 * 
 * @author Laurent
 */
public class EpayRichiestaEntity {

	private Long idRichiesta;
	private Long idIstanza;
	private Long idModulo;
	private Integer idTipologiaEpay;
	private Long idStoricoWorkflow;
	private String idEpay;
	private String iuv;
	private String codiceAvviso;
	private Date dataIns;
	private Date dataDel;
	private String attoreIns;
	private String attoreDel;
	private String richiesta;
	private String risposta;
	private String codiceEsito;
	private String descEsito;
	private Long idRicevutaTelematicaPositiva;
	private Date dataRicevutaTelematicaPositiva;
	private Long idNotificaPagamento;
	private Date dataNotificaPagamento;
	
	public Long getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Integer getIdTipologiaEpay() {
		return idTipologiaEpay;
	}
	public void setIdTipologiaEpay(Integer idTipologiaEpay) {
		this.idTipologiaEpay = idTipologiaEpay;
	}
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public String getIdEpay() {
		return idEpay;
	}
	public void setIdEpay(String idEpay) {
		this.idEpay = idEpay;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
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
	public Date getDataDel() {
		return dataDel;
	}
	public void setDataDel(Date dataDel) {
		this.dataDel = dataDel;
	}
	public String getAttoreIns() {
		return attoreIns;
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
	}
	public String getAttoreDel() {
		return attoreDel;
	}
	public void setAttoreDel(String attoreDel) {
		this.attoreDel = attoreDel;
	}
	public String getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}
	public String getRisposta() {
		return risposta;
	}
	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}
	public String getCodiceEsito() {
		return codiceEsito;
	}
	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}
	public String getDescEsito() {
		return descEsito;
	}
	public void setDescEsito(String descEsito) {
		this.descEsito = descEsito;
	}
	public Long getIdRicevutaTelematicaPositiva() {
		return idRicevutaTelematicaPositiva;
	}
	public void setIdRicevutaTelematicaPositiva(Long idRicevutaTelematicaPositiva) {
		this.idRicevutaTelematicaPositiva = idRicevutaTelematicaPositiva;
	}
	public Date getDataRicevutaTelematicaPositiva() {
		return dataRicevutaTelematicaPositiva;
	}
	public void setDataRicevutaTelematicaPositiva(Date dataRicevutaTelematicaPositiva) {
		this.dataRicevutaTelematicaPositiva = dataRicevutaTelematicaPositiva;
	}
	public Long getIdNotificaPagamento() {
		return idNotificaPagamento;
	}
	public void setIdNotificaPagamento(Long idNotificaPagamento) {
		this.idNotificaPagamento = idNotificaPagamento;
	}
	public Date getDataNotificaPagamento() {
		return dataNotificaPagamento;
	}
	public void setDataNotificaPagamento(Date dataNotificaPagamento) {
		this.dataNotificaPagamento = dataNotificaPagamento;
	}
	
	@Override
	public String toString() {
		return "EpayRichiestaEntity [idRichiesta=" + idRichiesta + ", idIstanza=" + idIstanza + ", idModulo=" + idModulo
				+ ", idTipologiaEpay=" + idTipologiaEpay + ", idStoricoWorkflow=" + idStoricoWorkflow + ", idEpay="
				+ idEpay + ", iuv=" + iuv + ", codiceAvviso=" + codiceAvviso + ", dataIns=" + dataIns + ", dataDel="
				+ dataDel + ", attoreIns=" + attoreIns + ", attoreDel=" + attoreDel + ", richiesta=" + richiesta
				+ ", risposta=" + risposta + ", codiceEsito=" + codiceEsito + ", descEsito=" + descEsito
				+ ", idRicevutaTelematicaPositiva=" + idRicevutaTelematicaPositiva + ", dataRicevutaTelematicaPositiva="
				+ dataRicevutaTelematicaPositiva + ", idNotificaPagamento=" + idNotificaPagamento
				+ ", dataNotificaPagamento=" + dataNotificaPagamento + "]";
	}
	
}
