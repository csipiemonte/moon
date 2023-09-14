/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Modulo versione ( moon_io_d_versione_modulo )
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class ModuloVersioneStato {

	private String codice;
	private String descrizione;
	private Date dataInizioValidita;
	private Date dataFineValidita;
	private String versioneModulo;

	/**
	 * @return the codice
	 */
	public java.lang.String getCodice() {
		return codice;
	}
	/**
	 * @return the descrizione
	 */
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	/**
	 * @return the dataInizioValidita
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public java.util.Date getDataInizioValidita() {
		return dataInizioValidita;
	}
	/**
	 * @return the dataFineValidita
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public java.util.Date getDataFineValidita() {
		return dataFineValidita;
	}
	/**
	 * @return the versioneModulo
	 */
	public String getVersioneModulo() {
		return versioneModulo;
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(java.lang.String codice) {
		this.codice = codice;
	}
	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(java.lang.String descrizione) {
		this.descrizione = descrizione;
	}
	/**
	 * @param dataInizioValidita the dataInizioValidita to set
	 */
	public void setDataInizioValidita(java.util.Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	/**
	 * @param dataFineValidita the dataFineValidita to set
	 */
	public void setDataFineValidita(java.util.Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	/**
	 * @param versioneModulo the versioneModulo to set
	 */
	public void setVersioneModulo(String versioneModulo) {
		this.versioneModulo = versioneModulo;
	}



}
