/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util.demografia.anpr.decodifica;

import it.csi.apimint.demografia.v1.dto.Residenza;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.TipoResidenza;

/**
 * Decodifica per il tipo di indirizzo.
 * TABELLA_25
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 13/08/2018 - versione iniziale
 */
public enum EnumTipoIndirizzoT25 implements EnumNAOANPR {

	/** 1	Residenza */
	RESIDENZA("1"),
	/** 2	Dimora abituale */
	DIMORA_ABITUALE("2"),
	/** 3	Domicilio eletto */
	DOMICILIO_ELETTO("3"),
	/** 4	Residenza estera */
	RESIDENZA_ESTERA("4"),
	/** 5	Presso per localita italiana */
	PRESSO_PER_LOCALITA_ITALIANA("5"),
	/** 6	Presso per localita estera */
	PRESSO_PER_LOCALITA_ESTERA("6"),
	/** 7	Ultima residenza italiana */
	ULTIMA_RESIDENZA_ITALIANA("7"),
	/** 8	Residenza temporanea */
	RESIDENZA_TEMPORANEA("8"),
	/** 9	Altro */
	ALTRO("9"),
	/** 10	Revisione onomastica stradale */
	REVISIONE_ONOMASTICA_STRADALE("10"),
	/** Rettifica post accertamenti */
	RETTIFICA_POST_ACCERTAMENTI("11"),
	;
	
	private final String codice;
	
	
	private EnumTipoIndirizzoT25(String codice) {
		this.codice = codice;
	}

	@Override
	public String getCodice() {
		return codice;
	}

	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static EnumTipoIndirizzoT25 byCodice(String codice) {
		for(EnumTipoIndirizzoT25 daf : values()) {
			if(daf.getCodice().equals(codice)) {
				return daf;
			}
		}
		return null;
	}
	

	/**
	 * Controlla se la residenza passata sia di tipo indirizzo corretto.
	 * 
	 * @param residenza la residenza da controllare
	 * @return <code>true</code> se il tipo indirizzo e corretto; <code>false</code> altrimenti
	 */
	public boolean isCorrectTipoIndirizzo(TipoResidenza residenza) {
		return residenza != null
				&& residenza.getTipoIndirizzo() != null
				&& getCodice().equals(residenza.getTipoIndirizzo());
	}
	

	/**
	 * Controlla se la residenza passata sia di tipo indirizzo corretto.
	 * 
	 * @param residenza la residenza da controllare
	 * @return <code>true</code> se il tipo indirizzo e corretto; <code>false</code> altrimenti
	 */
	public boolean isCorrectTipoIndirizzo(Residenza residenza) {
		return residenza != null
				&& residenza.getTipoIndirizzo() != null
				&& getCodice().equals(residenza.getTipoIndirizzo());
	}
}
