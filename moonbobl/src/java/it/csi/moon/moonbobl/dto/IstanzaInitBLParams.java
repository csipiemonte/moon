/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto;

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

	private Boolean isConcessionario;
	
	private Integer tipoDocumento;
	private String numeroDocumento;
	private String dataRilascioDocumento;
	
	private String idFamigliaConvivenzaANPR;
	private String chiaveUnivocita;
	
	private String nome;
	private String cognome;
	
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
		return "IstanzaInitBLParams [codiceFiscale=" + codiceFiscale + ", isConcessionario=" + isConcessionario
				+ ", tipoDocumento=" + tipoDocumento + ", numeroDocumento=" + numeroDocumento
				+ ", dataRilascioDocumento=" + dataRilascioDocumento + ", idFamigliaConvivenzaANPR="
				+ idFamigliaConvivenzaANPR + ", chiaveUnivocita=" + chiaveUnivocita + ", nome=" + nome +", cognome=" + cognome +"]";
	}

}

