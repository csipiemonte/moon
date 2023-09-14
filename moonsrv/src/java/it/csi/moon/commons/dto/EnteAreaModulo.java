/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.csi.moon.commons.entity.AreaEntity;
import it.csi.moon.commons.entity.EnteEntity;

/**
 * Entity della tabella di collocazione di un modulo nell'area di un ente per la funzionalita di EXPORT
 * <br>Oggetti Ente e Area sono completo in formato Entity
 * <br>Un modulo potra essere solo su un area di un ente, ma potra essere su piu area di diversi enti.
 * <br>
 * <br>Tabella moon_fo_r_area_modulo
 * <br>PK: idArea,idModulo
 * <br>AK: idModulo,idEnte
 * 
 * @see AreaEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class EnteAreaModulo {

	private EnteEntity ente;
	private AreaEntity area;
	private Long idModulo;
	private Date dataUpd;
	private String attoreUpd;
	
	public EnteAreaModulo() {	
	}
	
	public EnteAreaModulo(EnteEntity ente, AreaEntity area, Long idModulo, Date dataUpd, String attoreUpd) {
		this.ente = ente;
		this.area = area;
		this.idModulo = idModulo;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public EnteEntity getEnte() {
		return ente;
	}
	public void setEnte(EnteEntity ente) {
		this.ente = ente;
	}
	public AreaEntity getArea() {
		return area;
	}
	public void setArea(AreaEntity area) {
		this.area = area;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
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
		return "EnteAreaModulo [ente=" + ente + ", area=" + area + ", idModulo=" + idModulo + ", dataUpd=" + dataUpd
				+ ", attoreUpd=" + attoreUpd + "]";
	}
	
}
