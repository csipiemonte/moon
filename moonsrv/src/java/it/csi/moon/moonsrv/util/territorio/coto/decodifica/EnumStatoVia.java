/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util.territorio.coto.decodifica;

/**
 * Decodifica per la tipologia di una via della Toponomastica di Torino
 * <br>
 * <br>{"idVia":12816,"stato":0,"descStato":"Soppressa"
 * <br>{"idVia":66725,"stato":1,"descStato":"Attiva"
 * <br>{"idVia":19822,"stato":2,"descStato":"Mai stata aperta"
 * <br>{"idVia":66651,"stato":3,"descStato":"Non trovata in MNF"
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 16/04/2020 - versione iniziale
 */
public enum EnumStatoVia implements EnumToponomasticaCoto {

	/** 0	Soppressa */
	SPPRESSA(0,"Soppressa"),
	/** 1	Attiva */
	ATTIVA(1,"Attiva"),
	/** 2	Mai stata aperta */
	MAI_STATS_APERTA(2,"Mai stata aperta"),
	/** 3	Non trovata in MNF */
	NON_TROVATA_IN_MF(3,"Non trovata in MNF"),
	;
	
	private final Integer idStato;
	private final String descrizione;	
	
	private EnumStatoVia(Integer idStato, String descrizione) {
		this.idStato = idStato;
		this.descrizione = descrizione;
	}
	
	@Override
	public String getCodice() {
		return String.valueOf(idStato);
	}

	public Integer getIdStato() {
		return idStato;
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
	public static EnumStatoVia byCodice(String codice) {
		for(EnumStatoVia d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	public static EnumStatoVia byIdAzione(Integer idStato) {
		for(EnumStatoVia d : values()) {
			if(d.idStato.equals(idStato)) {
				return d;
			}
		}
		return null;
	}
}
