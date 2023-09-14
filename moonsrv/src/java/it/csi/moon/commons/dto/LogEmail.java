/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * LogEmail (moon_fo_t_log_email)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class LogEmail {

	private Long idLogEmail;
	private Date dataLogEmail;
	private TipoLogEmail tipologia;
	private Long idEnte;
	private Long idModulo;
	private Long idIstanza;
	private String emailDestinatario;
	private String tipoEmail; // text, text-attach, html, html-attach
	private String esito;
	
	public LogEmail() {
		super();
	}

	public Long getIdLogEmail() {
		return idLogEmail;
	}
	public void setIdLogEmail(Long idLogEmail) {
		this.idLogEmail = idLogEmail;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataLogEmail() {
		return dataLogEmail;
	}
	public void setDataLogEmail(Date dataLogEmail) {
		this.dataLogEmail = dataLogEmail;
	}
	public TipoLogEmail getTipologia() {
		return tipologia;
	}
	public void setTipologia(TipoLogEmail tipologia) {
		this.tipologia = tipologia;
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

	@Override
	public String toString() {
		return "LogEmail [idLogEmail=" + idLogEmail + ", dataLogEmail=" + dataLogEmail + ", tipologia="
				+ tipologia + ", idEnte=" + idEnte + ", idModulo=" + idModulo + ", idIstanza=" + idIstanza
				+ ", emailDestinatario=" + emailDestinatario + ", tipoEmail=" + tipoEmail + ", esito=" + esito + "]";
	}

	
}
