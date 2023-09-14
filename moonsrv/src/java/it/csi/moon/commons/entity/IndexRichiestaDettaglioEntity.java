/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

public class IndexRichiestaDettaglioEntity {

	private Long idRichiesta;
	private int tipoFile;
	private Long idIstanza;
	private Long idAllegato;
	private Long idFile;
	private String stato;
	private String desc_stato;
	
	public Long getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public int getTipoFile() {
		return tipoFile;
	}
	public void setTipoFile(int tipoFile) {
		this.tipoFile = tipoFile;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Long getIdAllegato() {
		return idAllegato;
	}
	public void setIdAllegato(Long idAllegato) {
		this.idAllegato = idAllegato;
	}
	public Long getIdFile() {
		return idFile;
	}
	public void setIdFile(Long idFile) {
		this.idFile = idFile;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getDesc_stato() {
		return desc_stato;
	}
	public void setDesc_stato(String desc_stato) {
		if(desc_stato.length()>254) {
			this.desc_stato = desc_stato.substring(0, 254);
		}else {
			this.desc_stato = desc_stato;
		}
	}
	
}
