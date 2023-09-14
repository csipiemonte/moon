/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

/**
 * Entity della tabella logIncrementale 
 * <br>
 * <br>Tabella moon_fo_t_log_incrementale
 * 
 * @author Laurent
 *
 */
public class LogIncrementaleEntity {
	private Long idModulo;
	private Integer countCreati;
	private Integer countAvviati;
	private Integer countEliminati;
	
	public LogIncrementaleEntity() {
		super();
	}

	public LogIncrementaleEntity(Long idModulo, Integer countCreati, Integer countAvviati, Integer countEliminati) {
		super();
		this.idModulo = idModulo;
		this.countCreati = countCreati;
		this.countAvviati = countAvviati;
		this.countEliminati = countEliminati;
	}

	public Long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
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
	
	

}
