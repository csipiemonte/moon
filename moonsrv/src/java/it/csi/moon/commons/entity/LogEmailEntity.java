/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

import it.csi.moon.commons.util.decodifica.DecodificaTipoLogEmail;

/**
 * Entity della tabella logEmail
 * <br>
 * <br>Tabella moon_fo_t_log_email
 * <br>
 * <br>Si prevede di inserire un log email subito dopo avere effettuato la richiesta di invio mail, quindi avendo gia l'esito
 * <br>Si prevede di inserire un log email alla richiesta di integrazione da parte della PA dal BO
 * <br>Si prevede di inserire un log email all'inserimento della risposta di di integrazione da parte del cittadino sul FO
 * 
 * @see DecodificaTipoLogEmail per la decodifica delle tipologie
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class LogEmailEntity {

	private Long idLogEmail;
	private Date dataLogEmail;
	private Integer idTipologia; // 1 MOOnFO-PostSaveIstanzaTaskManager.SendEmailDichiaranteIstanzaTask, 2 MOOnBO-RichiestaItegrazione, 3 MOOnFO-WorkflowServiceImpl RispostaIntegrazione
	private Long idEnte;
	private Long idModulo;
	private Long idIstanza;
	private String emailDestinatario;
	private String tipoEmail; // text, text-attach, html, html-attach
	private String esito;
	private Long idStoricoWorkflow;
	
	public LogEmailEntity() {
		super();
	}

	public LogEmailEntity(Integer idTipologia, Long idEnte, Long idModulo, Long idIstanza, String emailDestinatario,
			String tipoEmail, String esito) {
		super();
		this.idTipologia = idTipologia;
		this.idEnte = idEnte;
		this.idModulo = idModulo;
		this.idIstanza = idIstanza;
		this.emailDestinatario = emailDestinatario;
		this.tipoEmail = tipoEmail;
		this.esito = esito;
	}

	public Long getIdLogEmail() {
		return idLogEmail;
	}
	public void setIdLogEmail(Long idLogEmail) {
		this.idLogEmail = idLogEmail;
	}
	public Date getDataLogEmail() {
		return dataLogEmail;
	}
	public void setDataLogEmail(Date dataLogEmail) {
		this.dataLogEmail = dataLogEmail;
	}
	public Integer getIdTipologia() {
		return idTipologia;
	}
	public void setIdTipologia(Integer idTipologia) {
		this.idTipologia = idTipologia;
	}
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public String getEmailDestinatario() {
		return emailDestinatario;
	}
	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}
	public String getTipoEmail() {
		return tipoEmail;
	}
	public void setTipoEmail(String tipoEmail) {
		this.tipoEmail = tipoEmail;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}

	@Override
	public String toString() {
		return "LogEmailEntity [idLogEmail=" + idLogEmail + ", dataLogEmail=" + dataLogEmail + ", idTipologia="
				+ idTipologia + ", idEnte=" + idEnte + ", idModulo=" + idModulo + ", idIstanza=" + idIstanza
				+ ", emailDestinatario=" + emailDestinatario + ", tipoEmail=" + tipoEmail + ", esito=" + esito + ", idStoricoWorkflow=" + idStoricoWorkflow+"]";
	}
	
}
