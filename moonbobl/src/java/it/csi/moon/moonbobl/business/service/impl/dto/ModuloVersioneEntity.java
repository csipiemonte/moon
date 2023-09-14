/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabella della versione di un modulo del modulo cronologia
 * <br>Almeno un record presente per ogni modulo
 * <br>idVersioneModulo referenziato in ModuloCronologiaEntity
 * <br>
 * <br>Tabella moon_io_d_versione_modulo
 * <br>PK: idVersioneModulo
 * 
 * @see ModuloCronologiaEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloVersioneEntity {

	private Long idVersioneModulo;
	private Long idModulo;
	private String versioneModulo;
	private Date dataUpd;
	private String attoreUpd;
	
	public Long getIdVersioneModulo() {
		return idVersioneModulo;
	}
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public String getVersioneModulo() {
		return versioneModulo;
	}
	public void setVersioneModulo(String versioneModulo) {
		this.versioneModulo = versioneModulo;
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

}
