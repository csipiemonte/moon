/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.business.service.impl.dto.TipoModificaDatiEntity;
import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica del TipoModificaDati. (moon_fo_d_tipo_modifica_dati)
 *   Used in  moon_fo_t_dati_istanza.id_tipo_modifica_dati
 *   
 * @author Laurent Pissard
 * @version 1.0.0 - 23/12/2019 - versione iniziale
 *
 */
public enum DecodificaTipoModificaDati implements DecodificaMOON {

	/** 1 - INI - inserimento iniziale */
	INI(1, "INI", "inserimento iniziale", "S"),
	/** 2 - NON - nessuna modifica ai dati esistenti */
	NON(2, "NON", "nessuna modifica ai dati esistenti", "S"),
	/** 3 - UPD - modifica di dati esistenti */
	UPD(3, "UPD", "modifica di dati esistenti", "S"),
	/** 4 - ADD - aggiunta di nuovi elementi */
	ADD(4, "ADD", "aggiunta di nuovi elementi", "S"),
	/** 5 - AUP - aggiunta di nuovi elementi e modifica di quelli esistenti */
	AUP(5, "AUP", "aggiunta di nuovi elementi e modifica di quelli esistenti", "S"),
	;

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private TipoModificaDatiEntity tipo = null;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipoModificaDati(Integer idTipoModifica, String codTipoModificaDati, String descrizioneTipoModificaTipoDati, String flAttivo) {
		this.tipo = new TipoModificaDatiEntity(idTipoModifica, codTipoModificaDati, descrizioneTipoModificaTipoDati, flAttivo);
	}
	
	@Override
	public String getCodice() {
		return tipo.getCodTipoModificaDati();
	}
	
	public TipoModificaDatiEntity getTipoModificaDatiEntity() {
		return tipo;
	}
	public Integer getIdTipoModifica() {
		return tipo.getIdTipoModifica();
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaTipoModificaDati byCodice(String codice) {
		for(DecodificaTipoModificaDati d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	public static DecodificaTipoModificaDati byId(Integer idTipoModifica) {
		for(DecodificaTipoModificaDati d : values()) {
			if(d.getTipoModificaDatiEntity().getIdTipoModifica().equals(idTipoModifica)) {
				return d;
			}
		}
		return null;
	}
}
