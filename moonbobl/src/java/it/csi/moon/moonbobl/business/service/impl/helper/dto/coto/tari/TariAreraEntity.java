/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.tari;

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
public class TariAreraEntity {

	private String nome;
	private String cognome;
	private String ragioneSociale;
	private String codiceUtente;
	private String tipoContribuente;
	private String codiceUtenza;
	private String email;
	private String tipologiaUtenza;
	private String tipologiaRichiesta;
	private String DataRicezione;
	private String DataChiusura;
	private String CausaDelMancatoRispettoDelloStandardDiQualita;
	private String causaDelMancatoObbligoDiRisposta;
	private String testoRisposta;
	private String noteDiLavorazione;

	private List<String> formIoFileNames;
	private List<String> emailCc;
	private List<String> emailCcn;
	
	public TariAreraEntity() {
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

	public String getCodiceUtente() {
		return codiceUtente;
	}

	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = codiceUtente;
	}

	public String getTipoContribuente() {
		return tipoContribuente;
	}

	public void setTipoContribuente(String tipoContribuente) {
		this.tipoContribuente = tipoContribuente;
	}

	public String getCodiceUtenza() {
		return codiceUtenza;
	}

	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}

	public String getTipologiaUtenza() {
		return tipologiaUtenza;
	}

	public void setTipologiaUtenza(String tipoUtenza) {
		this.tipologiaUtenza = tipoUtenza;
	}

	public String getTipologiaRichiesta() {
		return tipologiaRichiesta;
	}

	public void setTipologiaRichiesta(String tipologiaRichiesta) {
		this.tipologiaRichiesta = tipologiaRichiesta;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDataRicezione() {
		return DataRicezione;
	}

	public void setDataRicezione(String dataRicezione) {
		DataRicezione = dataRicezione;
	}

	public String getDataChiusura() {
		return DataChiusura;
	}

	public void setDataChiusura(String dataChiusura) {
		DataChiusura = dataChiusura;
	}

	public String getCausaDelMancatoRispettoDelloStandardDiQualita() {
		return CausaDelMancatoRispettoDelloStandardDiQualita;
	}

	public void setCausaDelMancatoRispettoDelloStandardDiQualita(String causaDelMancatoRispettoDelloStandardDiQualita) {
		CausaDelMancatoRispettoDelloStandardDiQualita = causaDelMancatoRispettoDelloStandardDiQualita;
	}

	public String getCausaDelMancatoObbligoDiRisposta() {
		return causaDelMancatoObbligoDiRisposta;
	}

	public void setCausaDelMancatoObbligoDiRisposta(String causaDelMancatoObbligoDiRisposta) {
		this.causaDelMancatoObbligoDiRisposta = causaDelMancatoObbligoDiRisposta;
	}

	public String getTestoRisposta() {
		return testoRisposta;
	}

	public void setTestoRisposta(String testoRisposta) {
		this.testoRisposta = testoRisposta;
	}

	public String getNoteDiLavorazione() {
		return noteDiLavorazione;
	}

	public void setNoteDiLavorazione(String noteDiLavorazione) {
		this.noteDiLavorazione = noteDiLavorazione;
	}

	public List<String> getFormIoFileNames() {
		return formIoFileNames;
	}

	public void setFormIoFileNames(List<String> formIoFileNames) {
		this.formIoFileNames = formIoFileNames;
	}

	public List<String> getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(List<String> emailCc) {
		this.emailCc = emailCc;
	}
	
	public List<String> getEmailCcn() {
		return emailCcn;
	}

	public void setEmailCcn(List<String> emailCcn) {
		this.emailCcn = emailCcn;
	}
	
	@Override
	public String toString() {
		return "TariAreraEntity [nome=" + nome + ", "
				+ "cognome=" + cognome + ", "
				+ "ragioneSociale=" + ragioneSociale + ", "
				+ "codiceUtente=" + codiceUtente +"]";
	}
}
