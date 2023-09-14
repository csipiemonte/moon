/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.csi.moon.commons.dto.Istanza;

public class IstanzaReport extends Istanza {
	
	private String numeroTicketOtrs = null;
	private Date dataChiusura = null;
	private Date dataUltimoInvioIntegrazione = null;
	private String causaMancatoRipsettoStandard = null;
	private String causaMancatoObbligoRisposta = null;
	private String nomeOperatore = null;
	private String noteLavorazione = null;
	private String datiAzione = null;

	public IstanzaReport() {
		super();
	}

	/**
	 * @return the numeroTicketOtrs
	 */
	public String getNumeroTicketOtrs() {
		return numeroTicketOtrs;
	}

	/**
	 * @return the dataChiusura
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataChiusura() {
		return dataChiusura;
	}

	/**
	 * @return the dataUltimoInvioIntegrazione
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataUltimoInvioIntegrazione() {
		return dataUltimoInvioIntegrazione;
	}

	/**
	 * @return the causaMancatoRipsettoStandard
	 */
	public String getCausaMancatoRipsettoStandard() {
		return causaMancatoRipsettoStandard;
	}

	/**
	 * @return the causaMancatoObbligoRisposta
	 */
	public String getCausaMancatoObbligoRisposta() {
		return causaMancatoObbligoRisposta;
	}

	/**
	 * @return the nomeOperatore
	 */
	public String getNomeOperatore() {
		return nomeOperatore;
	}

	/**
	 * @return the noteLavorazione
	 */
	public String getNoteLavorazione() {
		return noteLavorazione;
	}

	/**
	 * @return the datiAzione
	 */
	public String getDatiAzione() {
		return datiAzione;
	}

	/**
	 * @param numeroTicketOtrs the numeroTicketOtrs to set
	 */
	public void setNumeroTicketOtrs(String numeroTicketOtrs) {
		this.numeroTicketOtrs = numeroTicketOtrs;
	}

	/**
	 * @param dataChiusura the dataChiusura to set
	 */
	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	/**
	 * @param dataUltimoInvioIntegrazione the dataUltimoInvioIntegrazione to set
	 */
	public void setDataUltimoInvioIntegrazione(Date dataUltimoInvioIntegrazione) {
		this.dataUltimoInvioIntegrazione = dataUltimoInvioIntegrazione;
	}

	/**
	 * @param causaMancatoRipsettoStandard the causaMancatoRipsettoStandard to set
	 */
	public void setCausaMancatoRipsettoStandard(String causaMancatoRipsettoStandard) {
		this.causaMancatoRipsettoStandard = causaMancatoRipsettoStandard;
	}

	/**
	 * @param causaMancatoObbligoRisposta the causaMancatoObbligoRisposta to set
	 */
	public void setCausaMancatoObbligoRisposta(String causaMancatoObbligoRisposta) {
		this.causaMancatoObbligoRisposta = causaMancatoObbligoRisposta;
	}

	/**
	 * @param nomeOperatore the nomeOperatore to set
	 */
	public void setNomeOperatore(String nomeOperatore) {
		this.nomeOperatore = nomeOperatore;
	}

	/**
	 * @param noteLavorazione the noteLavorazione to set
	 */
	public void setNoteLavorazione(String noteLavorazione) {
		this.noteLavorazione = noteLavorazione;
	}

	/**
	 * @param datiAzione the datiAzione to set
	 */
	public void setDatiAzione(String datiAzione) {
		this.datiAzione = datiAzione;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((causaMancatoObbligoRisposta == null) ? 0 : causaMancatoObbligoRisposta.hashCode());
		result = prime * result
				+ ((causaMancatoRipsettoStandard == null) ? 0 : causaMancatoRipsettoStandard.hashCode());
		result = prime * result + ((dataChiusura == null) ? 0 : dataChiusura.hashCode());
		result = prime * result + ((dataUltimoInvioIntegrazione == null) ? 0 : dataUltimoInvioIntegrazione.hashCode());
		result = prime * result + ((datiAzione == null) ? 0 : datiAzione.hashCode());
		result = prime * result + ((nomeOperatore == null) ? 0 : nomeOperatore.hashCode());
		result = prime * result + ((noteLavorazione == null) ? 0 : noteLavorazione.hashCode());
		result = prime * result + ((numeroTicketOtrs == null) ? 0 : numeroTicketOtrs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		IstanzaReport other = (IstanzaReport) obj;
		if (causaMancatoObbligoRisposta == null) {
			if (other.causaMancatoObbligoRisposta != null)
				return false;
		} else if (!causaMancatoObbligoRisposta.equals(other.causaMancatoObbligoRisposta))
			return false;
		if (causaMancatoRipsettoStandard == null) {
			if (other.causaMancatoRipsettoStandard != null)
				return false;
		} else if (!causaMancatoRipsettoStandard.equals(other.causaMancatoRipsettoStandard))
			return false;
		if (dataChiusura == null) {
			if (other.dataChiusura != null)
				return false;
		} else if (!dataChiusura.equals(other.dataChiusura))
			return false;
		if (dataUltimoInvioIntegrazione == null) {
			if (other.dataUltimoInvioIntegrazione != null)
				return false;
		} else if (!dataUltimoInvioIntegrazione.equals(other.dataUltimoInvioIntegrazione))
			return false;
		if (datiAzione == null) {
			if (other.datiAzione != null)
				return false;
		} else if (!datiAzione.equals(other.datiAzione))
			return false;
		if (nomeOperatore == null) {
			if (other.nomeOperatore != null)
				return false;
		} else if (!nomeOperatore.equals(other.nomeOperatore))
			return false;
		if (noteLavorazione == null) {
			if (other.noteLavorazione != null)
				return false;
		} else if (!noteLavorazione.equals(other.noteLavorazione))
			return false;
		if (numeroTicketOtrs == null) {
			if (other.numeroTicketOtrs != null)
				return false;
		} else if (!numeroTicketOtrs.equals(other.numeroTicketOtrs))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IstanzaReport [numeroTicketOtrs=" + numeroTicketOtrs + ", dataChiusura=" + dataChiusura
				+ ", dataUltimoInvioIntegrazione=" + dataUltimoInvioIntegrazione + ", causaMancatoRipsettoStandard="
				+ causaMancatoRipsettoStandard + ", causaMancatoObbligoRisposta=" + causaMancatoObbligoRisposta
				+ ", nomeOperatore=" + nomeOperatore + ", noteLavorazione=" + noteLavorazione + ", datiAzione="
				+ datiAzione + "]";
	}

}
