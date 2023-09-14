/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabelladi relazione utente modulo
 * <br>
 * <br>Tabella moon_fo_r_utente_modulo
 * <br>PK: idUtente idModulo
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class UtenteModuloEntity {

	private Long idUtente;
	private Long idModulo;
	private Date dataUpd;
	private String attoreUpd;
	
	public Long getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
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
		return "UtenteModuloEntity [idUtente=" + idUtente + ", idModulo=" + idModulo + ", dataUpd=" + dataUpd
				+ ", attoreUpd=" + attoreUpd + "]";
	}
	
	
	

}
