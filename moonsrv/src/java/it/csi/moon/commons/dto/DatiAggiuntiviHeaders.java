/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.io.Serializable;

public class DatiAggiuntiviHeaders implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String shibIdentitaCodiceFiscale;
	private String shibIdentitaNome;
	private String shibIdentitaCognome;
	private String shibMail;
	private String shibEmail;
	private String shibMobilePhone;
	private String shibDatadinascita;
	private String shibcommunity;
	private String shibIdentitaMatricola; // (solo per CSI e CRP)
	private String shibTipoRisorsa; // (solo per CSI e CRP : dipedente / consultente)
	private String shibIdentitaLivAuth;
	private String shibIdentitaloa;
	private String shibIdentitaTimeStamp;
	private String shibAuthenticationInstant;
	private String shibIdentityProvider;
	private String shibIdentitaProvider;
	private String codiceidentificativoSPID;
	private String shibIdentitaRiscontro;
	private String shibHandler;
	private String host;
	
	public DatiAggiuntiviHeaders() {
		super();
	}
	public DatiAggiuntiviHeaders(DatiAggiuntiviHeaders datiAggiuntiviHeadersToClone) {
		super();
		this.shibIdentitaCodiceFiscale = datiAggiuntiviHeadersToClone.getShibIdentitaCodiceFiscale();
		this.shibIdentitaNome = datiAggiuntiviHeadersToClone.getShibIdentitaNome();
		this.shibIdentitaCognome = datiAggiuntiviHeadersToClone.getShibIdentitaCognome();
		this.shibMail = datiAggiuntiviHeadersToClone.getShibMail();
		this.shibEmail = datiAggiuntiviHeadersToClone.getShibEmail();
		this.shibMobilePhone = datiAggiuntiviHeadersToClone.getShibMobilePhone();
		this.shibDatadinascita = datiAggiuntiviHeadersToClone.getShibDatadinascita();
		this.shibcommunity = datiAggiuntiviHeadersToClone.getShibcommunity();
		this.shibIdentitaMatricola = datiAggiuntiviHeadersToClone.getShibIdentitaMatricola(); // (solo per CSI e CRP)
		this.shibTipoRisorsa = datiAggiuntiviHeadersToClone.getShibTipoRisorsa(); // (solo per CSI e CRP : dipedente / consultente)
		this.shibIdentitaLivAuth = datiAggiuntiviHeadersToClone.getShibIdentitaLivAuth();
		this.shibIdentitaloa = datiAggiuntiviHeadersToClone.getShibIdentitaloa();
		this.shibIdentitaTimeStamp = datiAggiuntiviHeadersToClone.getShibIdentitaTimeStamp();
		this.shibAuthenticationInstant = datiAggiuntiviHeadersToClone.getShibAuthenticationInstant();
		this.shibIdentityProvider = datiAggiuntiviHeadersToClone.getShibIdentityProvider();
		this.shibIdentitaProvider = datiAggiuntiviHeadersToClone.getShibIdentitaProvider();
		this.codiceidentificativoSPID = datiAggiuntiviHeadersToClone.getCodiceidentificativoSPID();
		this.shibIdentitaRiscontro = datiAggiuntiviHeadersToClone.getShibIdentitaRiscontro();
		this.shibHandler = datiAggiuntiviHeadersToClone.getShibHandler();
		this.host = datiAggiuntiviHeadersToClone.getHost();
	}
	
	public String getShibIdentitaCodiceFiscale() {
		return shibIdentitaCodiceFiscale;
	}
	public String getShibIdentitaNome() {
		return shibIdentitaNome;
	}
	public String getShibIdentitaCognome() {
		return shibIdentitaCognome;
	}
	public String getShibMail() {
		return shibMail;
	}
	public String getShibEmail() {
		return shibEmail;
	}
	public String getShibMobilePhone() {
		return shibMobilePhone;
	}
	public String getShibDatadinascita() {
		return shibDatadinascita;
	}
	public String getShibcommunity() {
		return shibcommunity;
	}
	public String getShibIdentitaMatricola() {
		return shibIdentitaMatricola;
	}
	public String getShibTipoRisorsa() {
		return shibTipoRisorsa;
	}
	public String getShibIdentitaLivAuth() {
		return shibIdentitaLivAuth;
	}
	public String getShibIdentitaloa() {
		return shibIdentitaloa;
	}
	public String getShibIdentitaTimeStamp() {
		return shibIdentitaTimeStamp;
	}
	public String getShibAuthenticationInstant() {
		return shibAuthenticationInstant;
	}
	public String getShibIdentityProvider() {
		return shibIdentityProvider;
	}
	public String getShibIdentitaProvider() {
		return shibIdentitaProvider;
	}
	public String getCodiceidentificativoSPID() {
		return codiceidentificativoSPID;
	}
	public String getShibIdentitaRiscontro() {
		return shibIdentitaRiscontro;
	}
	public String getShibHandler() {
		return shibHandler;
	}
	public String getHost() {
		return host;
	}
	public void setShibIdentitaCodiceFiscale(String shibIdentitaCodiceFiscale) {
		this.shibIdentitaCodiceFiscale = shibIdentitaCodiceFiscale;
	}
	public void setShibIdentitaNome(String shibIdentitaNome) {
		this.shibIdentitaNome = shibIdentitaNome;
	}
	public void setShibIdentitaCognome(String shibIdentitaCognome) {
		this.shibIdentitaCognome = shibIdentitaCognome;
	}
	public void setShibMail(String shibMail) {
		this.shibMail = shibMail;
	}
	public void setShibEmail(String shibEmail) {
		this.shibEmail = shibEmail;
	}
	public void setShibMobilePhone(String shibMobilePhone) {
		this.shibMobilePhone = shibMobilePhone;
	}
	public void setShibDatadinascita(String shibDatadinascita) {
		this.shibDatadinascita = shibDatadinascita;
	}
	public void setShibcommunity(String shibcommunity) {
		this.shibcommunity = shibcommunity;
	}
	public void setShibIdentitaMatricola(String shibIdentitaMatricola) {
		this.shibIdentitaMatricola = shibIdentitaMatricola;
	}
	public void setShibTipoRisorsa(String shibTipoRisorsa) {
		this.shibTipoRisorsa = shibTipoRisorsa;
	}
	public void setShibIdentitaLivAuth(String shibIdentitaLivAuth) {
		this.shibIdentitaLivAuth = shibIdentitaLivAuth;
	}
	public void setShibIdentitaloa(String shibIdentitaloa) {
		this.shibIdentitaloa = shibIdentitaloa;
	}
	public void setShibIdentitaTimeStamp(String shibIdentitaTimeStamp) {
		this.shibIdentitaTimeStamp = shibIdentitaTimeStamp;
	}
	public void setShibAuthenticationInstant(String shibAuthenticationInstant) {
		this.shibAuthenticationInstant = shibAuthenticationInstant;
	}
	public void setShibIdentityProvider(String shibIdentityProvider) {
		this.shibIdentityProvider = shibIdentityProvider;
	}
	public void setShibIdentitaProvider(String shibIdentitaProvider) {
		this.shibIdentitaProvider = shibIdentitaProvider;
	}
	public void setCodiceidentificativoSPID(String codiceidentificativoSPID) {
		this.codiceidentificativoSPID = codiceidentificativoSPID;
	}
	public void setShibIdentitaRiscontro(String shibIdentitaRiscontro) {
		this.shibIdentitaRiscontro = shibIdentitaRiscontro;
	}
	public void setShibHandler(String shibHandler) {
		this.shibHandler = shibHandler;
	}
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String toString() {
		return "DatiAggiuntiviHeaders [shibIdentitaCodiceFiscale=" + shibIdentitaCodiceFiscale + ", shibIdentitaNome="
				+ shibIdentitaNome + ", shibIdentitaCognome=" + shibIdentitaCognome + ", shibMail=" + shibMail
				+ ", shibEmail=" + shibEmail
				+ ", shibMobilePhone=" + shibMobilePhone + ", shibDatadinascita=" + shibDatadinascita
				+ ", shibcommunity=" + shibcommunity + ", shibIdentitaMatricola=" + shibIdentitaMatricola
				+ ", shibTipoRisorsa=" + shibTipoRisorsa + ", shibIdentitaLivAuth=" + shibIdentitaLivAuth
				+ ", shibIdentitaloa=" + shibIdentitaloa + ", shibIdentitaTimeStamp=" + shibIdentitaTimeStamp
				+ ", shibAuthenticationInstant=" + shibAuthenticationInstant + ", shibIdentityProvider="
				+ shibIdentityProvider + ", shibIdentitaProvider=" + shibIdentitaProvider
				+ ", codiceidentificativoSPID=" + codiceidentificativoSPID + ", shibIdentitaRiscontro="
				+ shibIdentitaRiscontro + ", shibHandler=" + shibHandler + ", host=" + host + "]";
	}

}

