/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Parametri payload per la richiesta di inizializzazione di un istanza per un determinato modulo identificato
 * <br>Servizio /istanze/init/{@code idModulo}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaInitBLParams { 
	
	private String codiceFiscale;
	private String nome;
	private String cognome;

	private Boolean isConcessionario;
	
	private Integer tipoDocumento;
	private String numeroDocumento;
	private String dataRilascioDocumento;
	
	private String idFamigliaConvivenzaANPR;
	private String chiaveUnivocita;
	
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public Boolean getIsConcessionario() {
		return isConcessionario;
	}
	public void setIsConcessionario(Boolean isConcessionario) {
		this.isConcessionario = isConcessionario;
	}

	public Integer getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(Integer tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public String getDataRilascioDocumento() {
		return dataRilascioDocumento;
	}
	public void setDataRilascioDocumento(String dataRilascioDocumento) {
		this.dataRilascioDocumento = dataRilascioDocumento;
	}
	public String getIdFamigliaConvivenzaANPR() {
		return idFamigliaConvivenzaANPR;
	}
	public void setIdFamigliaConvivenzaANPR(String idFamigliaConvivenzaANPR) {
		this.idFamigliaConvivenzaANPR = idFamigliaConvivenzaANPR;
	}
	public String getChiaveUnivocita() {
		return chiaveUnivocita;
	}
	public void setChiaveUnivocita(String chiaveUnivocita) {
		this.chiaveUnivocita = chiaveUnivocita;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	
	@Override
	public String toString() {
		return "IstanzaInitBLParams [codiceFiscale=" + codiceFiscale + ", nome=" + nome +", cognome=" + cognome +", isConcessionario=" + isConcessionario
				+ ", tipoDocumento=" + tipoDocumento + ", numeroDocumento=" + numeroDocumento
				+ ", dataRilascioDocumento=" + dataRilascioDocumento + ", idFamigliaConvivenzaANPR="
				+ idFamigliaConvivenzaANPR + ", chiaveUnivocita=" + chiaveUnivocita + "]";
	}


}

