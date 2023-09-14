/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StatoEntity;
import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica dello StatoIstanza.
 * 
 * @see StatoEntity
 * Tabella moon_wf_d_stato
 * PK: idStatoWf  corrispondente al DecodificaStatoIstanza.getCodice()
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 23/12/2019 - versione iniziale
 *
 */
public enum DecodificaStatoIstanza implements DecodificaMOON {

	/** 1 - Bozza */
	BOZZA(1,"BOZZA","Modulo non completato, in bozza"),
	/** 2 - Inviato */
	INVIATA(2,"INVIATA","Modulo completato ed inviato"),
	/** 3 - Cancellato */
	CANCELLATO(3,"CANCELLATA","Modulo cancellatO"),
	/** 4 - Archiviato */
	ARCHIVIATO(4,"ARCHIVIATA","Modulo archiviato"),
	/** 10 - COMPLETATA */
	COMPLETATA(10,"COMPLETATA","istanza completata ma non inviata"),
	/** 15 - IN ATTESA INTEGRAZIONE */
	IN_ATTESA_INTEGRAZIONE(15,"IN ATTESA INTEGRAZIONE","istanza in attesa di una integrazione")
	;
	

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private StatoEntity stato = null;
	private String nomeStatoWf = null;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaStatoIstanza(Integer idStatoWf, String nomeStatoWf, String descStatoWf) {
		this.stato = new StatoEntity(idStatoWf, nomeStatoWf, descStatoWf);
	}
	
	@Override
	public String getCodice() {
		return String.valueOf(stato.getIdStatoWf());
	}
	
	public StatoEntity getStatoEntity() {
		return stato;
	}
	public Integer getIdStatoWf() {
		return stato.getIdStatoWf();
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaStatoIstanza byCodice(String codice) {
		for(DecodificaStatoIstanza d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	public static DecodificaStatoIstanza byIdStatoWf(Integer idStatoWf) {
		for(DecodificaStatoIstanza d : values()) {
			if(d.getStatoEntity().getIdStatoWf().equals(idStatoWf)) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Confronta con RepositoryFileEntity
	 */
	public boolean isCorrectStato(Integer idStatoToCompare) {
		if (idStatoToCompare==null) return false;
		if (getIdStatoWf().equals(idStatoToCompare))
			return true;
		return false;
	}
	public boolean isCorrectStato(IstanzaEntity istanza) {
		if (istanza==null) return false;
		if (getIdStatoWf().equals(istanza.getIdStatoWf())) {
			return true;
		}
		return false;
	}

	public String getNomeStatoWf() {
		return nomeStatoWf;
	}

	public void setNomeStatoWf(String nomeStatoWf) {
		this.nomeStatoWf = nomeStatoWf;
	}

}
