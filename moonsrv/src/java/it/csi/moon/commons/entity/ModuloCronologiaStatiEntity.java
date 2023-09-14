/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella della cronologia del modulo 
 * <br>Almeno un record presente per ogni modulo
 * <br>Solo un record per modulo con dataFine NULL
 * <br>
 * <br>Tabella moon_io_r_cronologia_statomodulo
 * <br>PK: idCronologiaStati
 * <br>AK: idVersioneModulo,dataFine NULL  per identificare l'ultimo stato del moduloVersione
 * 
 * @see ModuloEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloCronologiaStatiEntity {

	private Long idCron;
	private Long idVersioneModulo;
	private Integer idStato;
	private Date dataInizioValidita;
	private Date dataFineValidita;
	
	public Long getIdCron() {
		return idCron;
	}
	public void setIdCron(Long idCron) {
		this.idCron = idCron;
	}
	public Long getIdVersioneModulo() {
		return idVersioneModulo;
	}
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}
	public Integer getIdStato() {
		return idStato;
	}
	public void setIdStato(Integer idStato) {
		this.idStato = idStato;
	}
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public Date getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
		
}
