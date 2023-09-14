/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

public class EpayRicevutaTelematicaEntity {

	Long idRicevutaTelemematica;
	Long idRicevutaTelemTestata;
	String id;
	String xml;
	Date dataIns;
	
	public Long getIdRicevutaTelemematica() {
		return idRicevutaTelemematica;
	}
	public void setIdRicevutaTelemematica(Long idRicevutaTelemematica) {
		this.idRicevutaTelemematica = idRicevutaTelemematica;
	}
	public Long getIdRicevutaTelemTestata() {
		return idRicevutaTelemTestata;
	}
	public void setIdRicevutaTelemTestata(Long idRicevutaTelemTestata) {
		this.idRicevutaTelemTestata = idRicevutaTelemTestata;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	
	@Override
	public String toString() {
		return "EpayRicevutaTelematicaEntity [idRicevutaTelemematica=" + idRicevutaTelemematica
				+ ", idRicevutaTelemTestata=" + idRicevutaTelemTestata + ", id=" + id + ", xml=" + xml + ", dataIns="
				+ dataIns + "]";
	}
	
}
