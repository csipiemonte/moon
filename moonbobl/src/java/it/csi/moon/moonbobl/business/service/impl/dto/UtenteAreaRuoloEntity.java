/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabella di relazione utente area ruolo
 * <br>
 * <br>Tabella moon_fo_r_utente_area_ruolo
 * <br>PK: idUtente idArea idRuolo
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class UtenteAreaRuoloEntity {

	private Long idUtente;
	private Long idArea;
	private Integer idRuolo;	
	private Date dataUpd;
	private String attoreUpd;
	
	public Long getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
	}
	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Integer getIdRuolo() {
		return idRuolo;
	}
	public void setIdRuolo(Integer idRuolo) {
		this.idRuolo = idRuolo;
	}
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}
	
	@Override
	public String toString() {
		return "UtenteAreaRuoloEntity [idUtente=" + idUtente + ", idArea=" + idArea + ", idRuolo=" + idRuolo
				+ ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}
	


}
