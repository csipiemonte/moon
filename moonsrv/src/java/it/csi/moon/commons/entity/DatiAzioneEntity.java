/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;


/**
 * Entity della tabella che contiene la struttura di form.io di una determinata azione
 * 
 * Tabella moon_wf_d_dati_azione
 * PK: idDatiAzione
 * 
 * @author Alberto
 *
 */
public class DatiAzioneEntity {
	
	private Long idDatiazione = null;
	private String codiceDatiazione = null;
	private String versioneDatiazione = null;
	private String struttura;
	private String descrizioneDatiazione = null;
	
	public Long getIdDatiazione() {
		return idDatiazione;
	}
	public void setIdDatiazione(Long idDatiazione) {
		this.idDatiazione = idDatiazione;
	}
	public String getCodiceDatiazione() {
		return codiceDatiazione;
	}
	public void setCodiceDatiazione(String codiceDatiazione) {
		this.codiceDatiazione = codiceDatiazione;
	}
	public String getVersioneDatiazione() {
		return versioneDatiazione;
	}
	public void setVersioneDatiazione(String versioneDatiazione) {
		this.versioneDatiazione = versioneDatiazione;
	}
	public String getStruttura() {
		return struttura;
	}
	public void setStruttura(String struttura) {
		this.struttura = struttura;
	}
	public String getDescrizioneDatiazione() {
		return descrizioneDatiazione;
	}
	public void setDescrizioneDatiazione(String descrizioneDatiazione) {
		this.descrizioneDatiazione = descrizioneDatiazione;
	}
	
	@Override
	public String toString() {
		return "DatiAzioneEntity [idDatiazione=" + idDatiazione + ", codiceDatiazione=" + codiceDatiazione
				+ ", versioneDatiazione=" + versioneDatiazione + ", struttura=" + struttura + ", descrizioneDatiazione="
				+ descrizioneDatiazione + "]";
	}

}
