/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import it.csi.moon.moonbobl.dto.moonfobl.Istanza;

public class IstanzaSaveResponse {

	private Istanza istanza = null;
	private String codice = null;
	private String titolo = null;
	private String descrizione = null;
	private Boolean includeDescrizioneInEmail = null;
	
	public IstanzaSaveResponse() {
		super();
	}
	public IstanzaSaveResponse(Istanza istanza, String codice, String titolo, String descrizione) {
		super();
		this.istanza = istanza;
		this.codice = codice;
		this.titolo = titolo;
		this.descrizione = descrizione;
	}
	
	public Istanza getIstanza() {
		return istanza;
	}
	public void setIstanza(Istanza istanza) {
		this.istanza = istanza;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Boolean getIncludeDescrizioneInEmail() {
		return includeDescrizioneInEmail;
	}
	public void setIncludeDescrizioneInEmail(Boolean includeDescrizioneInEmail) {
		this.includeDescrizioneInEmail = includeDescrizioneInEmail;
	}
	
	@Override
	public String toString() {
		return "IstanzaSaveResponse [istanza=" + istanza + ", codice=" + codice + ", titolo=" + titolo + ", descrizione=" + descrizione + ", includeDescrizioneInEmail=" + includeDescrizioneInEmail + "]";
	}

}
