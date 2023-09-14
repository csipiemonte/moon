/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.protocollo.util;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.stardas.cxfclient.MetadatiType;
import it.csi.stardas.cxfclient.MetadatoType;

public class StardasMetadatiTypeBuilder {

	private static final String CLASS_NAME = "StardasMetadatiTypeBuilder";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	// Documento
	private static final String OGGETTO_DOCUMENTO = "OGGETTO_DOCUMENTO";
	private static final String PAROLE_CHIAVE_DOCUMENTO = "PAROLE_CHIAVE_DOCUMENTO";
	private static final String AUTORE_GIURIDICO_DOCUMENTO = "AUTORE_GIURIDICO_DOCUMENTO";
	private static final String AUTORE_FISICO_DOCUMENTO = "AUTORE_FISICO_DOCUMENTO";
	private static final String MITTENTE_DENOMINAZIONE = "MITTENTE_DENOMINAZIONE";
	private static final String DATA_CRONICA_DOCUMENTO = "DATA_CRONICA_DOCUMENTO";
	private static final String ANNOTAZIONE_FORMALE_1 = "ANNOTAZIONE_FORMALE_1";
	private static final String ANNOTAZIONE_FORMALE_2 = "ANNOTAZIONE_FORMALE_2";
	private static final String STATO_DI_EFFICACIA_DOCUMENTO = "STATO_DI_EFFICACIA_DOCUMENTO";
	private static final String FORMA_DOCUMENTARIA_DOCUMENTO = "FORMA_DOCUMENTARIA_DOCUMENTO";
	private static final String DESTINATARIO_FISICO_DOCUMENTO = "DESTINATARIO_FISICO_DOCUMENTO";
	private static final String SCRITTORE_DOCUMENTO = "SCRITTORE_DOCUMENTO";
	//
	private static final String DESCRIZIONE_VOL_SERIE_FASCICOLI = "DESCRIZIONE_VOL_SERIE_FASCICOLI";
	private static final String CODICE_FASCICOLO = "CODICE_FASCICOLO";
	private static final String OGGETTO_FASCICOLO = "OGGETTO_FASCICOLO";
	private static final String PAROLA_CHIAVE_FASCICOLO = "PAROLA_CHIAVE_FASCICOLO";
	private static final String DESCRIZIONE_VOL_FASCICOLI = "DESCRIZIONE_VOL_FASCICOLI";
	private static final String PAROLA_CHIAVE_SERIE_FASCICOLI = "PAROLA_CHIAVE_SERIE_FASCICOLI";
	private static final String DESCRIZIONE_SOTTO_FASCICOLI = "DESCRIZIONE_SOTTO_FASCICOLI";
	private static final String PAROLA_CHIAVE_TITOLARIO = "PAROLA_CHIAVE_TITOLARIO";
	private static final String CODICE_SERIE_TIPOL_DOCUMENTI = "CODICE_SERIE_TIPOL_DOCUMENTI";
	private static final String DESCRIZIONE_VOL_SERIE_DOCUMENTI = "DESCRIZIONE_VOL_SERIE_DOCUMENTI";
	private static final String PAROLA_CHIAVE_VOL_SERIE_DOCUMENTI = "PAROLA_CHIAVE_VOL_SERIE_DOCUMENTI";
	private static final String UTENTE_STRUTTURA = "UTENTE_STRUTTURA";
	private static final String UTENTE_NODO = "UTENTE_NODO";
	
	private static final String CODICE_SERIE_FASCICOLI = "CODICE_SERIE_FASCICOLI";
	private static final String DESCRIZIONE_SERIE_FASCICOLI = "DESCRIZIONE_SERIE_FASCICOLI";
	private static final String ORIGINATORE_DOCUMENTO = "ORIGINATORE_DOCUMENTO";
	private static final String PAROLA_CHIAVE_SERIE_DOCUMENTI = "PAROLA_CHIAVE_SERIE_DOCUMENTI";
	
	private static final String DATI_SENSIBILI_DOCUMENTO = "DATI_SENSIBILI_DOCUMENTO";
	private static final String DATI_RISERVATI_DOCUMENTO = "DATI_RISERVATI_DOCUMENTO";
	
	
	// Documento
	private String oggettoDocumento;
	private String paroleChiaveDocumento;
	private String autoreGiuridicoDocumento;
	private String autoreFisicoDocumento;
	private String mittenteDenominazione;
	private String dataCronicaDocumento;
	private String annotazioneFormale1;
	private String annotazioneFormale2;
	private String statoDiEfficaciaDocumento;
	private String formaDocumentariaDocumento;
	private String destinatarioFisicoDocumento;
	private String scrittoreDocumento;
	//
	private String descrizioneVolSerieFascicoli;
	private String codiceFascicolo;
	private String oggettoFascicolo;
	private String parolaChiaveFascicolo;
	private String descrizioneVolFascicoli;
	private String parolaChiaveSerieFascicoli;
	private String descrizioneSottoFascicoli;
	private String parolaChiaveTitolario;
	private String codiceSerieTipolDocumenti;
	private String descrizioneVolSerieDocumenti;
	private String parolaChiaveVolSerieDocumenti;
	private String utenteStruttura;
	private String utenteNodo;
	
	private String codiceSerieFascicoli;
	private String descrizioneSerieFascicoli;
	private String originatoreDocumento;
	private String parolaChiaveSerieDocumenti;
	
	private String datiSensibiliDocumento;
	private String datiRiservatiDocumento;
	
	public StardasMetadatiTypeBuilder() {
		super();
	}
	
	// Documento
	public StardasMetadatiTypeBuilder oggettoDocumento(String oggettoDocumento) {
		this.oggettoDocumento = oggettoDocumento;
		return this;
	}
	public StardasMetadatiTypeBuilder paroleChiaveDocumento(String paroleChiaveDocumento) {
		this.paroleChiaveDocumento = paroleChiaveDocumento;
		return this;
	}
	public StardasMetadatiTypeBuilder autoreGiuridicoDocumento(String autoreGiuridicoDocumento) {
		this.autoreGiuridicoDocumento = autoreGiuridicoDocumento;
		return this;
	}
	public StardasMetadatiTypeBuilder autoreFisicoDocumento(String autoreFisicoDocumento) {
		this.autoreFisicoDocumento = autoreFisicoDocumento;
		return this;
	}
	public StardasMetadatiTypeBuilder mittenteDenominazione(String mittenteDenominazione) {
		this.mittenteDenominazione = mittenteDenominazione;
		return this;
	}
	public StardasMetadatiTypeBuilder dataCronicaDocumento(String dataCronicaDocumento) {
		this.dataCronicaDocumento = dataCronicaDocumento;
		return this;
	}
	public StardasMetadatiTypeBuilder annotazioneFormale1(String annotazioneFormale1) {
		this.annotazioneFormale1 = annotazioneFormale1;
		return this;
	}
	/**
	 * @Since 24/03/2023
	 */
	public StardasMetadatiTypeBuilder annotazioneFormale2(String annotazioneFormale2) {
		this.annotazioneFormale2 = annotazioneFormale2;
		return this;
	}
	public StardasMetadatiTypeBuilder statoDiEfficaciaDocumento(String statoDiEfficaciaDocumento) {
		this.statoDiEfficaciaDocumento = statoDiEfficaciaDocumento;
		return this;
	}
	public StardasMetadatiTypeBuilder formaDocumentariaDocumento(String formaDocumentariaDocumento) {
		this.formaDocumentariaDocumento = formaDocumentariaDocumento;
		return this;
	}
	public StardasMetadatiTypeBuilder destinatarioFisicoDocumento(String destinatarioFisicoDocumento) {
		this.destinatarioFisicoDocumento = destinatarioFisicoDocumento;
		return this;
	}
	public StardasMetadatiTypeBuilder scrittoreDocumento(String scrittoreDocumento) {
		this.scrittoreDocumento = scrittoreDocumento;
		return this;
	}

	//
	public StardasMetadatiTypeBuilder descrizioneVolSerieFascicoli(String descrizioneVolSerieFascicoli) {
		this.descrizioneVolSerieFascicoli = descrizioneVolSerieFascicoli;
		return this;
	}
	public StardasMetadatiTypeBuilder codiceFascicolo(String codiceFascicolo) {
		this.codiceFascicolo = codiceFascicolo;
		return this;
	}
	public StardasMetadatiTypeBuilder oggettoFascicolo(String oggettoFascicolo) {
		this.oggettoFascicolo = oggettoFascicolo;
		return this;
	}
	public StardasMetadatiTypeBuilder parolaChiaveFascicolo(String parolaChiaveFascicolo) {
		this.parolaChiaveFascicolo = parolaChiaveFascicolo;
		return this;
	}
	public StardasMetadatiTypeBuilder descrizioneVolFascicoli(String descrizioneVolFascicoli) {
		this.descrizioneVolFascicoli = descrizioneVolFascicoli;
		return this;
	}
	public StardasMetadatiTypeBuilder parolaChiaveSerieFascicoli(String parolaChiaveSerieFascicoli) {
		this.parolaChiaveSerieFascicoli = parolaChiaveSerieFascicoli;
		return this;
	}
	public StardasMetadatiTypeBuilder descrizioneSottoFascicoli(String descrizioneSottoFascicoli) {
		this.descrizioneSottoFascicoli = descrizioneSottoFascicoli;
		return this;
	}
	public StardasMetadatiTypeBuilder parolaChiaveTitolario(String parolaChiaveTitolario) {
		this.parolaChiaveTitolario = parolaChiaveTitolario;
		return this;
	}
	public StardasMetadatiTypeBuilder codiceSerieTipolDocumenti(String codiceSerieTipolDocumenti) {
		this.codiceSerieTipolDocumenti = codiceSerieTipolDocumenti;
		return this;
	}
	public StardasMetadatiTypeBuilder descrizioneVolSerieDocumenti(String descrizioneVolSerieDocumenti) {
		this.descrizioneVolSerieDocumenti = descrizioneVolSerieDocumenti;
		return this;
	}
	public StardasMetadatiTypeBuilder parolaChiaveVolSerieDocumenti(String parolaChiaveVolSerieDocumenti) {
		this.parolaChiaveVolSerieDocumenti = parolaChiaveVolSerieDocumenti;
		return this;
	}
	public StardasMetadatiTypeBuilder utenteStruttura(String utenteStruttura) {
		this.utenteStruttura = utenteStruttura;
		return this;
	}
	public StardasMetadatiTypeBuilder utenteNodo(String utenteNodo) {
		this.utenteNodo = utenteNodo;
		return this;
	}
	public StardasMetadatiTypeBuilder codiceSerieFascicoli(String codiceSerieFascicoli) {
		this.codiceSerieFascicoli = codiceSerieFascicoli;
		return this;
	}
	public StardasMetadatiTypeBuilder descrizioneSerieFascicoli(String descrizioneSerieFascicoli) {
		this.descrizioneSerieFascicoli = descrizioneSerieFascicoli;
		return this;
	}
	public StardasMetadatiTypeBuilder originatoreDocumento(String originatoreDocumento) {
		this.originatoreDocumento = originatoreDocumento;
		return this;
	}
	/**
	 * @Since 24/03/2023
	 */
	public StardasMetadatiTypeBuilder parolaChiaveSerieDocumenti(String parolaChiaveSerieDocumenti) {
		this.parolaChiaveSerieDocumenti = parolaChiaveSerieDocumenti;
		return this;
	}
	/**
	 * @Since 10/07/2023
	 */
	public StardasMetadatiTypeBuilder datiSensibiliDocumento(String datiSensibiliDocumento) {
		this.datiSensibiliDocumento = datiSensibiliDocumento;
		return this;
	}
	public StardasMetadatiTypeBuilder datiRiservatiDocumento(String datiRiservatiDocumento) {
		this.datiRiservatiDocumento = datiRiservatiDocumento;
		return this;
	}
	
	/**
	 * build an it.csi.stardas.cxfclient.MetadatiType Object to send to STARDAS
	 * @return MetadatiType
	 */
	public MetadatiType build() {
		MetadatiType result = new MetadatiType();
		// Documento
		result = addMetadatoNonNull(result, OGGETTO_DOCUMENTO, oggettoDocumento);
		result = addMetadatoNonNull(result, PAROLE_CHIAVE_DOCUMENTO, paroleChiaveDocumento);
		result = addMetadatoNonNull(result, AUTORE_GIURIDICO_DOCUMENTO, autoreGiuridicoDocumento);
		result = addMetadatoNonNull(result, AUTORE_FISICO_DOCUMENTO, autoreFisicoDocumento);
		result = addMetadatoNonNull(result, MITTENTE_DENOMINAZIONE, mittenteDenominazione);
		result = addMetadatoNonNull(result, DATA_CRONICA_DOCUMENTO, dataCronicaDocumento);
		result = addMetadatoNonNull(result, ANNOTAZIONE_FORMALE_1, annotazioneFormale1);
		result = addMetadatoNonNull(result, ANNOTAZIONE_FORMALE_2, annotazioneFormale2);
		result = addMetadatoNonNull(result, STATO_DI_EFFICACIA_DOCUMENTO, statoDiEfficaciaDocumento);
		result = addMetadatoNonNull(result, FORMA_DOCUMENTARIA_DOCUMENTO, formaDocumentariaDocumento);
		result = addMetadatoNonNull(result, DESTINATARIO_FISICO_DOCUMENTO, destinatarioFisicoDocumento);
		result = addMetadatoNonNull(result, SCRITTORE_DOCUMENTO, scrittoreDocumento);
		//
		result = addMetadatoNonNull(result, DESCRIZIONE_VOL_SERIE_FASCICOLI, descrizioneVolSerieFascicoli);
		result = addMetadatoNonNull(result, CODICE_FASCICOLO, codiceFascicolo);
		result = addMetadatoNonNull(result, OGGETTO_FASCICOLO, oggettoFascicolo);
		result = addMetadatoNonNull(result, PAROLA_CHIAVE_FASCICOLO, parolaChiaveFascicolo);
		result = addMetadatoNonNull(result, DESCRIZIONE_VOL_FASCICOLI, descrizioneVolFascicoli);
		result = addMetadatoNonNull(result, PAROLA_CHIAVE_SERIE_FASCICOLI, parolaChiaveSerieFascicoli);
		result = addMetadatoNonNull(result, DESCRIZIONE_SOTTO_FASCICOLI, descrizioneSottoFascicoli);
		result = addMetadatoNonNull(result, PAROLA_CHIAVE_TITOLARIO, parolaChiaveTitolario);
		result = addMetadatoNonNull(result, CODICE_SERIE_TIPOL_DOCUMENTI, codiceSerieTipolDocumenti);
		result = addMetadatoNonNull(result, DESCRIZIONE_VOL_SERIE_DOCUMENTI, descrizioneVolSerieDocumenti);
		result = addMetadatoNonNull(result, PAROLA_CHIAVE_VOL_SERIE_DOCUMENTI, parolaChiaveVolSerieDocumenti);
		result = addMetadatoNonNull(result, UTENTE_STRUTTURA, utenteStruttura);
		result = addMetadatoNonNull(result, UTENTE_NODO, utenteNodo);
		
		result = addMetadatoNonNull(result, CODICE_SERIE_FASCICOLI, codiceSerieFascicoli);
		result = addMetadatoNonNull(result, DESCRIZIONE_SERIE_FASCICOLI, descrizioneSerieFascicoli);
		result = addMetadatoNonNull(result, ORIGINATORE_DOCUMENTO, originatoreDocumento);
		result = addMetadatoNonNull(result, PAROLA_CHIAVE_SERIE_DOCUMENTI, parolaChiaveSerieDocumenti);
		
		result = addMetadatoNonNull(result, DATI_SENSIBILI_DOCUMENTO, datiSensibiliDocumento);
		result = addMetadatoNonNull(result, DATI_RISERVATI_DOCUMENTO, datiRiservatiDocumento);
		
		return result;
	}
	
	private MetadatiType addMetadatoNonNull(MetadatiType result, String nome, String valore) {
		if (StringUtils.isEmpty(valore)) {
			return result;
		}
		result.getMetadato().add(makeNewMetadato(nome, valore));
		return result;
	}
	
	private MetadatoType makeNewMetadato(String nome, String valore) {
		MetadatoType result = new MetadatoType();
		result.setNome(nome);
		result.setValore(valore);
		return result;
	}
	
}
