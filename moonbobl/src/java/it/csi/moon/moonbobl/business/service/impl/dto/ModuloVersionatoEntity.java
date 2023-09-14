/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * DTO di risultato della ricerca sui moduli
 * <br>Deve essere presente almeno un record di Versione, Cronologia, Struttura, Progresso 
 * <br>
 * <br>Tabella moon_io_d_modulo
 * <br>PK: idVersioneModulo
 * 
 * @see moon_io_r_cronologia_statomodulo
 * @see ModuloStrutturaEntity
 * @see ModuloProgressivoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloVersionatoEntity extends ModuloEntity {
	
	private Long idVersioneModulo;
	private String versioneModulo;
	//
	private Long idCron;
	private Integer idStato;
	private Date dataInizioValidita;
	private Date dataFineValidita;
	//
	private Integer idCategoria;
	private String nomeCategoria;
	
	//
	public String getVersioneModulo() {
		return versioneModulo;
	}
	public void setVersioneModulo(String versioneModulo) {
		this.versioneModulo = versioneModulo;
	}
	public Long getIdVersioneModulo() {
		return idVersioneModulo;
	}
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}
	//
	public Long getIdCron() {
		return idCron;
	}
	public void setIdCron(Long idCron) {
		this.idCron = idCron;
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
	//
	public Integer getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getNomeCategoria() {
		return nomeCategoria;
	}
	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
	
}
