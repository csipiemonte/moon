/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;



/**
 * Entity della tabella che contiene la struttura di form.io di una determinata azione
 * 
 * Tabella moon_wf_d_dati_azione
 * PK: idDatiAzione
 * 
 * 
 * @author Alberto
 *
 */
public class DatiAzione {

	private Integer idDatiAzione = null;
	private String codiceDatiAzione = null;
	private String versioneDatiAzione = null;
	private String struttura;
	private String descrizioneDatiAzione = null;
	private Object data = null;
	
	public DatiAzione() {	
	}
	
	public DatiAzione(Integer idDatiAzione, String struttura) {
		this.struttura = struttura;
		this.idDatiAzione = idDatiAzione;
	}

	public Integer getIdDatiAzione() {
		return idDatiAzione;
	}

	public void setIdDatiAzione(Integer idDatiAzione) {
		this.idDatiAzione = idDatiAzione;
	}

	public String getCodiceDatiAzione() {
		return codiceDatiAzione;
	}

	public void setCodiceDatiAzione(String codiceDatiAzione) {
		this.codiceDatiAzione = codiceDatiAzione;
	}

	public String getVersioneDatiAzione() {
		return versioneDatiAzione;
	}

	public void setVersioneDatiAzione(String versioneDatiAzione) {
		this.versioneDatiAzione = versioneDatiAzione;
	}

	public String getStruttura() {
		return struttura;
	}

	public void setStruttura(String struttura) {
		this.struttura = struttura;
	}

	public String getDescrizioneDatiAzione() {
		return descrizioneDatiAzione;
	}

	public void setDescrizioneDatiAzione(String descrizioneDatiAzione) {
		this.descrizioneDatiAzione = descrizioneDatiAzione;
	}
	
	public Object getData() {
	    return data;
	}
	
	public void setData(Object data) {
	    this.data = data;
	}

	@Override
	public String toString() {
		return "DatiAzione [idDatiAzione=" + idDatiAzione + ", codiceDatiAzione=" + codiceDatiAzione
				+ ", versioneDatiAzione=" + versioneDatiAzione + ", struttura=" + struttura + ", descrizioneDatiAzione="
				+ descrizioneDatiAzione + ", data=" + data + "]";
	}
	
}
