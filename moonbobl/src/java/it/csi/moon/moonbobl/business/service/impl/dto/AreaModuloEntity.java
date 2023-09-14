/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabella di collocazione di un modulo nell'area di un ente
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
public class AreaModuloEntity {

	private Long idArea;
	private Long idEnte;
	private Long idModulo;
	private Date dataUpd;
	private String attoreUpd;
	
	public AreaModuloEntity() {	
	}
	
	public AreaModuloEntity(Long idArea, Long idEnte, Long idModulo, Date dataUpd, String attoreUpd) {
		this.idArea = idArea;
		this.idEnte = idEnte;
		this.idModulo = idModulo;
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
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attoreUpd == null) ? 0 : attoreUpd.hashCode());
		result = prime * result + ((dataUpd == null) ? 0 : dataUpd.hashCode());
		result = prime * result + ((idArea == null) ? 0 : idArea.hashCode());
		result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
		result = prime * result + ((idModulo == null) ? 0 : idModulo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AreaModuloEntity other = (AreaModuloEntity) obj;
		if (attoreUpd == null) {
			if (other.attoreUpd != null)
				return false;
		} else if (!attoreUpd.equals(other.attoreUpd))
			return false;
		if (dataUpd == null) {
			if (other.dataUpd != null)
				return false;
		} else if (!dataUpd.equals(other.dataUpd))
			return false;
		if (idArea == null) {
			if (other.idArea != null)
				return false;
		} else if (!idArea.equals(other.idArea))
			return false;
		if (idEnte == null) {
			if (other.idEnte != null)
				return false;
		} else if (!idEnte.equals(other.idEnte))
			return false;
		if (idModulo == null) {
			if (other.idModulo != null)
				return false;
		} else if (!idModulo.equals(other.idModulo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AreaEntity [idArea=" + idArea + ", idEnte=" + idEnte + ", idModulo="
				+ idModulo + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}
	
}
