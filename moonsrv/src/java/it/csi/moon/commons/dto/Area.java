/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;

import it.csi.moon.commons.entity.AreaModuloEntity;

/**
 * Entity della tabella di decodifica delle area di un ente su quale sara collocato i moduli
 * <br>Un modulo potra essere solo su un area di un ente, ma potra essere su piu area di diversi enti.
 * <br>
 * <br>Tabella moon_fo_d_area
 * <br>PK: idArea
 * <br>AK: idEnte,codiceArea
 * 
 * @see AreaModuloEntity#getIdArea()
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Area {

	private Long idArea;
	private Long idEnte;
	private String codiceArea;
	private String nomeArea;
	private Date dataUpd;
	private String attoreUpd;
	
	public Area() {	
	}
	
	public Area(Long idArea, Long idEnte, String codiceArea, String nomeArea, Date dataUpd, String attoreUpd) {
		this.idArea = idArea;
		this.idEnte = idEnte;
		this.codiceArea = codiceArea;
		this.nomeArea = nomeArea;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public String getCodiceArea() {
		return codiceArea;
	}
	public void setCodiceArea(String codiceArea) {
		this.codiceArea = codiceArea;
	}
	public String getNomeArea() {
		return nomeArea;
	}
	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
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
		return "Area [idArea=" + idArea + ", idEnte=" + idEnte + ", codiceArea=" + codiceArea + ", nomeArea="
				+ nomeArea + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}
	
}
