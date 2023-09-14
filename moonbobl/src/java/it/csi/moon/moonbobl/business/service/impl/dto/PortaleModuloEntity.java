/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabelladi relazione portale modulo
 * <br>
 * <br>Tabella moon_fo_r_portale_modulo
 * <br>PK: idPortale idModulo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class PortaleModuloEntity {

	private Long idPortale;
	private Long idModulo;
	private Date dataUpd;
	private String attoreUpd;
	
	
	public PortaleModuloEntity() {
		super();
	}
	public PortaleModuloEntity(Long idPortale, Long idModulo, Date dataUpd, String attoreUpd) {
		super();
		this.idPortale = idPortale;
		this.idModulo = idModulo;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}
	
	public Long getIdPortale() {
		return idPortale;
	}
	public void setIdPortale(Long idPortale) {
		this.idPortale = idPortale;
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
	public String toString() {
		return "PortaleModuloEntity [idPortale=" + idPortale + ", idModulo=" + idModulo + ", dataUpd=" + dataUpd
				+ ", attoreUpd=" + attoreUpd + "]";
	}
	
	
	

}
