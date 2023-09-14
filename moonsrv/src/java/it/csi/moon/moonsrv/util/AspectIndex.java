/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util;

import java.util.Date;

public class AspectIndex {

	private String identificativoUtente;
	private String codiceFiscaleDichiarante = null;
	private String cognomeDichiarante = null;
	private String nomeDichiarante = null;
	
	private String codiceIstanza; 
	private Date dataCreazione;
	
	private String numeroProtocollo;
	private Date dataProtocollo;
	
	private String codiceModulo = null;
	private String versioneModulo = null;
	private String oggettoModulo = null;
	
	private String codiceEnte = null;
	
	//proprieta x i file
	private String codiceFile = null;
	private String hashFile = null;
	private String nomeFile = null;
	private String codiceTipologia = null;
	
	public String getIdentificativoUtente() {
		return identificativoUtente;
	}
	public void setIdentificativoUtente(String identificativoUtente) {
		this.identificativoUtente = identificativoUtente;
	}
	public String getCodiceFiscaleDichiarante() {
		return codiceFiscaleDichiarante;
	}
	public void setCodiceFiscaleDichiarante(String codiceFiscaleDichiarante) {
		this.codiceFiscaleDichiarante = codiceFiscaleDichiarante;
	}
	public String getCognomeDichiarante() {
		return cognomeDichiarante;
	}
	public void setCognomeDichiarante(String cognomeDichiarante) {
		this.cognomeDichiarante = cognomeDichiarante;
	}
	public String getNomeDichiarante() {
		return nomeDichiarante;
	}
	public void setNomeDichiarante(String nomeDichiarante) {
		this.nomeDichiarante = nomeDichiarante;
	}
	public String getCodiceIstanza() {
		return codiceIstanza;
	}
	public void setCodiceIstanza(String codiceIstanza) {
		this.codiceIstanza = codiceIstanza;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	public Date getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	public String getCodiceModulo() {
		return codiceModulo;
	}
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
	public String getVersioneModulo() {
		return versioneModulo;
	}
	public void setVersioneModulo(String versioneModulo) {
		this.versioneModulo = versioneModulo;
	}
	public String getOggettoModulo() {
		return oggettoModulo;
	}
	public void setOggettoModulo(String oggettoModulo) {
		this.oggettoModulo = oggettoModulo;
	}
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public String getCodiceFile() {
		return codiceFile;
	}
	public void setCodiceFile(String codiceFile) {
		this.codiceFile = codiceFile;
	}
	public String getHashFile() {
		return hashFile;
	}
	public void setHashFile(String hashFile) {
		this.hashFile = hashFile;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public String getCodiceTipologia() {
		return codiceTipologia;
	}
	public void setCodiceTipologia(String codiceTipologia) {
		this.codiceTipologia = codiceTipologia;
	}
	
}
