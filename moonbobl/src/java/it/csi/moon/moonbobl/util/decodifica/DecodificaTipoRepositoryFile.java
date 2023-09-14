/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

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
	BO_RICEVUTA(1),
	/** 2 - MOOnBO-RichiestaItegrazione */
	BO_ALLEGATO_RICHIESTA_INTEGRAZIONE(2),
	/** 3 - MOOnFO-WorkflowServiceImpl RispostaIntegrazione */
	FO_ALLEGATO_RISPOSTA_INTEGRAZIONE(3),
	/** 4 - Allegato inviato cambio stato via API */
	API_ALLEGATO_AZIONE_CAMBIO_STATO(4),
	/** 5 - Allegato notifica via API */
	API_ALLEGATO_NOTIFICA(5),
	/** 6 - MOOnFO-Ricevuta CAF */
	FO_RICEVUTA_CAF(6),
	/** 7 - MOOnFO RispostaIntegrazione */
	FO_RISPOSTA_INTEGRAZIONE(7),
	;

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipoRepositoryFile(Integer id) {
		this.id = id;
	}
	
	@Override
	public String getCodice() {
		return id.toString();
	}
	
	public Integer getId() {
		return id;
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
