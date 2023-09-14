/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;

/**
 * Entity della tabella di decodifica delle azioni che hanno generato movimentazione delle istanza 
 * Viene salavato in ogni cronologia di istanza
 * 
 * Tabella moon_wf_d_azione
 * PK: idAzione
 * Usato prevalentamente da enum DecodificaAzione
 * 
 * @see DecodificaAzione
 * @see IstanzaCronologiaStatiEntity#getIdAzioneSvolta()
 * 
 * @author Laurent
 *
 */
public class AzioneEntity {

	private Long idAzione = null;
	private String codiceAzione = null;
	private String nomeAzione = null;
	private String descAzione = null;
	
	public AzioneEntity() {	
	}
	
	public AzioneEntity(Long idAzione, String codiceAzione, String nomeAzione, String descAzione) {
		this.idAzione = idAzione;
		this.codiceAzione = codiceAzione;
		this.nomeAzione = nomeAzione;
		this.descAzione = descAzione;
	}
	
	public Long getIdAzione() {
		return idAzione;
	}
	public void setIdAzione(Long idAzione) {
		this.idAzione = idAzione;
	}
	
	public String getCodiceAzione() {
		return codiceAzione;
	}

	public void setCodiceAzione(String codiceAzione) {
		this.codiceAzione = codiceAzione;
	}

	public String getNomeAzione() {
		return nomeAzione;
	}
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}
	public String getDescAzione() {
		return descAzione;
	}
	public void setDescAzione(String descAzione) {
		this.descAzione = descAzione;
	}
	
}
