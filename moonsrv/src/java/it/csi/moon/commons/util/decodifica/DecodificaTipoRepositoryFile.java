/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica di Tipologie dei RepositoryFile, non c'è alla tabella specifica, viene salvato nel database in moon_fo_t_repository_file.id_tipologia
 * 
 * @see RepositoryFileEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 07/09/2020 - versione iniziale
 */
public enum DecodificaTipoRepositoryFile implements DecodificaMOON {

	/** 1 - MOOnBO-Ricevuta */
	BO_RICEVUTA(1, 2, "Ricevuta emessa dalla PA"),
	/** 2 - MOOnBO-RichiestaItegrazione */
	BO_ALLEGATO_RICHIESTA_INTEGRAZIONE(2, 2, "Allegato di una richiesta di integrazione"),
	/** 3 - MOOnFO-WorkflowServiceImpl RispostaIntegrazione */
	FO_ALLEGATO_RISPOSTA_INTEGRAZIONE(3, 1, "Allegato di una risposta di integrazione"),
	/** 4 - Allegato inviato cambio stato via API */
	API_ALLEGATO_AZIONE_CAMBIO_STATO(4, 2, "Allegato su azione di cambio stato via API"),
	/** 5 - Allegato notifica via API */
	API_ALLEGATO_NOTIFICA(5, 2, "Notifica emessa dalla PA (via API)"),
	/** 6 - MOOnFO-Ricevuta CAF */
	FO_RICEVUTA_CAF(6, 1, "Ricevuta CAF"),
	/** 7 - MOOnFO RispostaIntegrazione */
	FO_RISPOSTA_INTEGRAZIONE(7, 1, "Risposta di integrazione (rendering PDF)"),
	;

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	private final Integer tipoIngUsc;
	private final String descrizione;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipoRepositoryFile(Integer id, Integer tipoIngUsc, String descrizione) {
		this.id = id;
		this.tipoIngUsc = tipoIngUsc;
		this.descrizione = descrizione;
	}
	
	@Override
	public String getCodice() {
		return id.toString();
	}
	
	public Integer getId() {
		return id;
	}
	public Integer getTipoIngUsc() {
		return tipoIngUsc;
	}
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaTipoRepositoryFile byCodice(String codice) {
		for(DecodificaTipoRepositoryFile d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
	public static DecodificaTipoRepositoryFile byId(Integer id) {
		for(DecodificaTipoRepositoryFile d : values()) {
			if(d.id.equals(id)) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Confronta con RepositoryFileEntity
	 */
	public boolean isCorrectTipologia(Integer idTipologiaToCompare) {
		if (idTipologiaToCompare==null) return false;
		if (getId().equals(idTipologiaToCompare))
			return true;
		return false;
	}
	public boolean isCorrectTipologia(RepositoryFileEntity file) {
		if (file==null) return false;
		if (getId().equals(file.getIdTipologia()))
			return true;
		return false;
	}
}
