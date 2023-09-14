/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto.coto;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity per la creazione di comunicazioni per rinnovo concessioni
 * <br>
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class TributiGeneralEntity {

	private String nome;
	private String cognome;
	private String ragioneSociale;
	private String codiceFiscale;
	private String email;
	private String tipoContribuente;
	private List<String> formIoFileNames;
	private String testoRisposta;
	
	public TributiGeneralEntity() {
		super();
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

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTipoContribuente() {
		return tipoContribuente;
	}

	public void setTipoContribuente(String tipoContribuente) {
		this.tipoContribuente = tipoContribuente;
	}

	public List<String> getFormIoFileNames() {
		return formIoFileNames;
	}

	public void setFormIoFileNames(List<String> formIoFileNames) {
		this.formIoFileNames = formIoFileNames;
	}
	
	public String getTestoRisposta() {
		return testoRisposta;
	}

	public void setTestoRisposta(String testoRisposta) {
		this.testoRisposta = testoRisposta;
	}
	
	@Override
	public String toString() {
		return "TariAreraEntity [nome=" + nome + ", "
				+ "cognome=" + cognome + ", "
				+ "ragioneSociale=" + ragioneSociale + ", "
				+ "codiceFiscale=" + codiceFiscale +"]";
	}
}
