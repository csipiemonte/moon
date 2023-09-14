/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabella logEmail
 * <br>
 * <br>Tabella moon_fo_t_log_email
 * <br>
 * <br>Si prevede di inserire un log email subito dopo avere effettuato la richiesta di invio mail, quindi avendo gia l'esito
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class LogEmailEntity {

	private Long idLogEmail;
	private Date dataLogEmail;
	private Integer idTipologia; // 1 MOOnFO-PostSaveIstanzaTaskManager.SendEmailDichiaranteIstanzaTask
	private Long idEnte;
	private Long idModulo;
	private Long idIstanza;
	private String emailDestinatario;
	private String tipoEmail; // text, text-attach, html, html-attach
	private String esito;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataLogEmail == null) ? 0 : dataLogEmail.hashCode());
		result = prime * result + ((emailDestinatario == null) ? 0 : emailDestinatario.hashCode());
		result = prime * result + ((esito == null) ? 0 : esito.hashCode());
		result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
		result = prime * result + ((idIstanza == null) ? 0 : idIstanza.hashCode());
		result = prime * result + ((idLogEmail == null) ? 0 : idLogEmail.hashCode());
		result = prime * result + ((idModulo == null) ? 0 : idModulo.hashCode());
		result = prime * result + ((idTipologia == null) ? 0 : idTipologia.hashCode());
		result = prime * result + ((tipoEmail == null) ? 0 : tipoEmail.hashCode());
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
		LogEmailEntity other = (LogEmailEntity) obj;
		if (dataLogEmail == null) {
			if (other.dataLogEmail != null)
				return false;
		} else if (!dataLogEmail.equals(other.dataLogEmail))
			return false;
		if (emailDestinatario == null) {
			if (other.emailDestinatario != null)
				return false;
		} else if (!emailDestinatario.equals(other.emailDestinatario))
			return false;
		if (esito == null) {
			if (other.esito != null)
				return false;
		} else if (!esito.equals(other.esito))
			return false;
		if (idEnte == null) {
			if (other.idEnte != null)
				return false;
		} else if (!idEnte.equals(other.idEnte))
			return false;
		if (idIstanza == null) {
			if (other.idIstanza != null)
				return false;
		} else if (!idIstanza.equals(other.idIstanza))
			return false;
		if (idLogEmail == null) {
			if (other.idLogEmail != null)
				return false;
		} else if (!idLogEmail.equals(other.idLogEmail))
			return false;
		if (idModulo == null) {
			if (other.idModulo != null)
				return false;
		} else if (!idModulo.equals(other.idModulo))
			return false;
		if (idTipologia == null) {
			if (other.idTipologia != null)
				return false;
		} else if (!idTipologia.equals(other.idTipologia))
			return false;
		if (tipoEmail == null) {
			if (other.tipoEmail != null)
				return false;
		} else if (!tipoEmail.equals(other.tipoEmail))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LogEmailEntity [idLogEmail=" + idLogEmail + ", dataLogEmail=" + dataLogEmail + ", idTipologia="
				+ idTipologia + ", idEnte=" + idEnte + ", idModulo=" + idModulo + ", idIstanza=" + idIstanza
				+ ", emailDestinatario=" + emailDestinatario + ", tipoEmail=" + tipoEmail + ", esito=" + esito + "]";
	}

	
}
