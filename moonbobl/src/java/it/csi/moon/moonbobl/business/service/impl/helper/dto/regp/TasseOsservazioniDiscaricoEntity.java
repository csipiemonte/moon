/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto.regp;

/*import java.util.ArrayList;
import java.util.List;*/

/**
 * Entity per la creazione della ricevuta di cambio indirizzo
 * <br>
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class TasseOsservazioniDiscaricoEntity {

	private String tipoPersona;
	private String pFnome;
	private String pFcognome;
	private String pFcodiceFiscale;
	private String pFdataNascita;
	private String pFluogoNascita;
	private String pFstatoNascita;
	private String pFprovincia;
	private String pFcomune;
	private String lRnome;
	private String lRcognome;
	private String lRcodiceFiscale;
	private String lRdataNascita;
	private String lRluogoNascita;
	private String lRstatoNascita;
	private String lRprovincia;
	private String indirizzo;
	private String provincia;
	private String comune;
	private String cap;
	private String oggettoDomanda;
	private String numeroDomanda;
	private String annoRiferimento;
	
	
	public TasseOsservazioniDiscaricoEntity() {
		super();
	}

	public TasseOsservazioniDiscaricoEntity( String tipoPersona,String pFnome,	String pFcognome, String pFcodiceFiscale, String pFdataNascita, String pFluogoNascita,
	 String pFstatoNascita, String pFprovincia,	String pFcomune, String lRnome,	String lRcognome, String lRcodiceFiscale,	String lRdataNascita,
	 String lRluogoNascita,	String lRstatoNascita, String lRprovincia, String indirizzo, String provincia,	String comune,	String cap, String oggettoDomanda,String numeroDomanda, String annoRiferimento) {
			super();
			this.tipoPersona=tipoPersona;
			this.pFnome = pFnome;
			this.pFcognome = pFcognome;
			this.pFcodiceFiscale = pFcodiceFiscale;
			this.pFdataNascita = pFdataNascita;
			this.pFluogoNascita = pFluogoNascita;
			this.pFstatoNascita = pFstatoNascita;
			this.pFprovincia = pFprovincia;
			this.pFcomune = pFcomune;
			this.lRnome = lRnome;
			this.lRcognome = lRcognome; 
			this.lRcodiceFiscale = lRcodiceFiscale;
			this.lRdataNascita = lRdataNascita;
			this.lRluogoNascita = lRluogoNascita;
			this.lRstatoNascita = lRstatoNascita;
			this.lRprovincia = lRprovincia;
			this.indirizzo = indirizzo;
			this.provincia = provincia;
			this.comune = comune;
			this.cap = cap;
			this.oggettoDomanda=oggettoDomanda;
			this.numeroDomanda=numeroDomanda;
			this.annoRiferimento = annoRiferimento;
		}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getlRnome() {
		return lRnome;
	}

	public void setlRnome(String lRnome) {
		this.lRnome = lRnome;
	}

	public String getlRcognome() {
		return lRcognome;
	}

	public void setlRcognome(String lRcognome) {
		this.lRcognome = lRcognome;
	}

	public String getlRcodiceFiscale() {
		return lRcodiceFiscale;
	}

	public void setlRcodiceFiscale(String lRcodiceFiscale) {
		this.lRcodiceFiscale = lRcodiceFiscale;
	}

	public String getlRdataNascita() {
		return lRdataNascita;
	}

	public void setlRdataNascita(String lRdataNascita) {
		this.lRdataNascita = lRdataNascita;
	}

	public String getlRluogoNascita() {
		return lRluogoNascita;
	}

	public void setlRluogoNascita(String lRluogoNascita) {
		this.lRluogoNascita = lRluogoNascita;
	}

	public String getlRstatoNascita() {
		return lRstatoNascita;
	}

	public void setlRstatoNascita(String lRstatoNascita) {
		this.lRstatoNascita = lRstatoNascita;
	}

	public String getlRprovincia() {
		return lRprovincia;
	}

	public void setlRprovincia(String lRprovincia) {
		this.lRprovincia = lRprovincia;
	}

	public String getpFnome() {
		return pFnome;
	}

	public void setpFnome(String pFnome) {
		this.pFnome = pFnome;
	}

	public String getpFcognome() {
		return pFcognome;
	}

	public void setpFcognome(String pFcognome) {
		this.pFcognome = pFcognome;
	}

	public String getpFcodiceFiscale() {
		return pFcodiceFiscale;
	}

	public void setpFcodiceFiscale(String pFcodiceFiscale) {
		this.pFcodiceFiscale = pFcodiceFiscale;
	}

	public String getpFdataNascita() {
		return pFdataNascita;
	}

	public void setpFdataNascita(String pFdataNascita) {
		this.pFdataNascita = pFdataNascita;
	}

	public String getpFluogoNascita() {
		return pFluogoNascita;
	}

	public void setpFluogoNascita(String pFluogoNascita) {
		this.pFluogoNascita = pFluogoNascita;
	}

	public String getpFstatoNascita() {
		return pFstatoNascita;
	}

	public void setpFstatoNascita(String pFstatoNascita) {
		this.pFstatoNascita = pFstatoNascita;
	}

	public String getpFprovincia() {
		return pFprovincia;
	}

	public void setpFprovincia(String pFprovincia) {
		this.pFprovincia = pFprovincia;
	}

	public String getpFcomune() {
		return pFcomune;
	}

	public void setpFcomune(String pFcomune) {
		this.pFcomune = pFcomune;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getOggettoDomanda() {
		return oggettoDomanda;
	}

	public void setOggettoDomanda(String oggettoDomanda) {
		this.oggettoDomanda = oggettoDomanda;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(String annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}



/*	@Override
	public String toString() {
		return "CambioResidenzaEntity [richiedente=" + richiedente + ", indirizzo=" + indirizzo  + "]";
	}
*/		
}
