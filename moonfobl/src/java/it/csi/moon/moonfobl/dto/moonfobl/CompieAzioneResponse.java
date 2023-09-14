/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

public class CompieAzioneResponse {
	
	private String nomeAzione = null;
	private String descDestinatario = null;
	private String codEsitoAzione = null;
	private String descEsitoAzione = null;
	private String url = null;
	
	public String getNomeAzione() {
		return nomeAzione;
	}

	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}

	public String getDescDestinatario() {
		return descDestinatario;
	}

	public void setDescDestinatario(String descDestinatario) {
		this.descDestinatario = descDestinatario;
	}

	public String getCodEsitoAzione() {
		return codEsitoAzione;
	}

	public void setCodEsitoAzione(String codEsitoAzione) {
		this.codEsitoAzione = codEsitoAzione;
	}

	public String getDescEsitoAzione() {
		return descEsitoAzione;
	}

	public void setDescEsitoAzione(String descEsitoAzione) {
		this.descEsitoAzione = descEsitoAzione;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public CompieAzioneResponse() {
		// TODO Auto-generated constructor stub
	}

}
