/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;
import java.util.Optional;

/**
 * Filter DTO usato dal ModuloDAO per le ricerche delle richieste di pagamento effettuati su EPAY
 * 
 * @author Laurent
 */
public class EpayRichiestaFilter {

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
	private Boolean deleted;
	
	public Optional<Long> getIdRichiesta() {
		return Optional.ofNullable(idRichiesta);
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public Optional<Long> getIdIstanza() {
		return Optional.ofNullable(idIstanza);
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Optional<Long> getIdModulo() {
		return Optional.ofNullable(idModulo);
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Optional<Integer> getIdTipologiaEpay() {
		return Optional.ofNullable(idTipologiaEpay);
	}
	public void setIdTipologiaEpay(Integer idTipologiaEpay) {
		this.idTipologiaEpay = idTipologiaEpay;
	}
	public Optional<Long> getIdStoricoWorkflow() {
		return Optional.ofNullable(idStoricoWorkflow);
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public Optional<String> getIdEpay() {
		return Optional.ofNullable(idEpay);
	}
	public void setIdEpay(String idEpay) {
		this.idEpay = idEpay;
	}
	public Optional<String> getIuv() {
		return Optional.ofNullable(iuv);
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public Optional<String> getCodiceAvviso() {
		return Optional.ofNullable(codiceAvviso);
	}
	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}
	public Optional<Date> getDataIns() {
		return Optional.ofNullable(dataIns);
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	public Optional<Date> getDataDel() {
		return Optional.ofNullable(dataDel);
	}
	public void setDataDel(Date dataDel) {
		this.dataDel = dataDel;
	}
	public Optional<String> getAttoreIns() {
		return Optional.ofNullable(attoreIns);
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
	}
	public Optional<String> getAttoreDel() {
		return Optional.ofNullable(attoreDel);
	}
	public void setAttoreDel(String attoreDel) {
		this.attoreDel = attoreDel;
	}
	public Optional<String> getRichiesta() {
		return Optional.ofNullable(richiesta);
	}
	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}
	public Optional<String> getRisposta() {
		return Optional.ofNullable(risposta);
	}
	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}
	public Optional<String> getCodiceEsito() {
		return Optional.ofNullable(codiceEsito);
	}
	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}
	public Optional<String> getDescEsito() {
		return Optional.ofNullable(descEsito);
	}
	public void setDescEsito(String descEsito) {
		this.descEsito = descEsito;
	}
	public Optional<Long> getIdRicevutaTelematicaPositiva() {
		return Optional.ofNullable(idRicevutaTelematicaPositiva);
	}
	public void setIdRicevutaTelematicaPositiva(Long idRicevutaTelematicaPositiva) {
		this.idRicevutaTelematicaPositiva = idRicevutaTelematicaPositiva;
	}
	public Optional<Date> getDataRicevutaTelematicaPositiva() {
		return Optional.ofNullable(dataRicevutaTelematicaPositiva);
	}
	public void setDataRicevutaTelematicaPositiva(Date dataRicevutaTelematicaPositiva) {
		this.dataRicevutaTelematicaPositiva = dataRicevutaTelematicaPositiva;
	}
	public Optional<Long> getIdNotificaPagamento() {
		return Optional.ofNullable(idNotificaPagamento);
	}
	public void setIdNotificaPagamento(Long idNotificaPagamento) {
		this.idNotificaPagamento = idNotificaPagamento;
	}
	public Optional<Date> getDataNotificaPagamento() {
		return Optional.ofNullable(dataNotificaPagamento);
	}
	public void setDataNotificaPagamento(Date dataNotificaPagamento) {
		this.dataNotificaPagamento = dataNotificaPagamento;
	}
	public Optional<Boolean> getDeleted() {
		return Optional.ofNullable(deleted);
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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
				+ ", dataNotificaPagamento=" + dataNotificaPagamento + ", deleted=" + deleted +"]";
	}
	
}
