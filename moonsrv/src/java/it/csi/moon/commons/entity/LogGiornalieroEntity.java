/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella logGiornaliero 
 * <br>
 * <br>Tabella moon_fo_t_log_giornaliero
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class LogGiornalieroEntity {
	
	
	private Date giorno;
	private Integer numeroAccessi;
	private Integer countCreati;
	private Integer countAvviati;
	private Integer countEliminati;
	private String codiceModulo;
	
	public LogGiornalieroEntity() {
		super();
	}

	public LogGiornalieroEntity(Date giorno, Integer numeroAccessi, Integer countCreati, Integer countAvviati,
			Integer countEliminati, String codiceModulo) {
		super();
		this.giorno = giorno;
		this.numeroAccessi = numeroAccessi;
		this.countCreati = countCreati;
		this.countAvviati = countAvviati;
		this.countEliminati = countEliminati;
		this.codiceModulo = codiceModulo;
	}

	public Date getGiorno() {
		return giorno;
	}

	public void setGiorno(Date giorno) {
		this.giorno = giorno;
	}

	public Integer getNumeroAccessi() {
		return numeroAccessi;
	}

	public void setNumeroAccessi(Integer numeroAccessi) {
		this.numeroAccessi = numeroAccessi;
	}

	public Integer getCountCreati() {
		return countCreati;
	}

	public void setCountCreati(Integer countCreati) {
		this.countCreati = countCreati;
	}

	public Integer getCountAvviati() {
		return countAvviati;
	}

	public void setCountAvviati(Integer countAvviati) {
		this.countAvviati = countAvviati;
	}

	public Integer getCountEliminati() {
		return countEliminati;
	}

	public void setCountEliminati(Integer countEliminati) {
		this.countEliminati = countEliminati;
	}

	public String getCodiceModulo() {
		return codiceModulo;
	}

	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
	
	
}
