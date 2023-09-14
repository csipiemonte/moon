/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util.territorio.coto.decodifica;

/**
 * Decodifica per la tipologia del civico della Toponomastica di Torino
 * <br>
 * <br>{ "idCivico": 11421, "idVia": 286, "indirizzo": "CORSO ADRIATICO 6/E", "stato": 1, "descrizioneStato": "Attivo", "esponente": "/E" }, 
 * <br>{ "idCivico": 139300, "idVia": 286, "indirizzo": "CORSO ADRIATICO 6 scala A", "stato": 0, "descrizioneStato": "Soppresso", "esponente": "scala A" }
 * <br>Oggi su DB di test e produzione esistono civici con stato 0,1 e 2.
 * <br>Lo stato 9, utilizzato durante la dismissione Mainframe, identificava la numerazione civica non trovata negli scarichi massivi MF
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 12/06/2023 - versione iniziale
 */
public enum EnumStatoCivico implements EnumToponomasticaCoto {

	/** 0	Soppressa */
	SPPRESSO(0,"Soppresso"),
	/** 1	Attiva */
	ATTIVO(1,"Attivo"),
	/** 2   Soppresso per cambio denominazione via o rinumeraz */
	SOPPRESSO_PER_CAMBIO_DENOMINAZIONE_VIA_O_RINUMERAZ(2," Soppresso per cambio denominazione via o rinumeraz"),
	/** 3   Soppresso per errore materiale */
	SOPPRESSO_PER_ERRORE_MATERIALE(3,"Soppresso per errore materiale"),
	/** 9   Perso */
	PERSO(3,"Perso"),
	;
	
	private final Integer idStato;
	private final String descrizione;	
	
	private EnumStatoCivico(Integer idStato, String descrizione) {
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
	public static EnumStatoCivico byCodice(String codice) {
		for(EnumStatoCivico d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	public static EnumStatoCivico byIdAzione(Integer idStato) {
		for(EnumStatoCivico d : values()) {
			if(d.idStato.equals(idStato)) {
				return d;
			}
		}
		return null;
	}
}
