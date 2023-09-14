/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

/**
 * Entity della tabella degli metadati di configurazione del protocollo
 * <br>
 * <br>Tabella moon_pr_d_metadato
 * <br>PK: idMetadato
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 12/05/2022 - versione iniziale
 */
public class ProtocolloMetadatoEntity {

	private Long idMetadato;
	private String nomeMetadato;
	private String defaultValue;
	private Integer ordine;
	
	public Long getIdMetadato() {
		return idMetadato;
	}
	public void setIdMetadato(Long idMetadato) {
		this.idMetadato = idMetadato;
	}
	public String getNomeMetadato() {
		return nomeMetadato;
	}
	public void setNomeMetadato(String nomeMetadato) {
		this.nomeMetadato = nomeMetadato;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Integer getOrdine() {
		return ordine;
	}
	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}
	
	@Override
	public String toString() {
		return "ProtocolloMetadatoEntity [idMetadato=" + idMetadato + ", nomeMetadato=" + nomeMetadato
				+ ", defaultValue=" + defaultValue + ", ordine=" + ordine + "]";
	}

}
