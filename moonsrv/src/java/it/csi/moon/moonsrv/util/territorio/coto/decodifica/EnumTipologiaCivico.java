/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util.territorio.coto.decodifica;

/**
 * Decodifica per la tipologia del civico della Toponomastica di Torino
 * <br>Mail di Francesco Sardano 16/04/2020
 * <br>il codice tipologia civico  è su db una stringa di 2 caratteri.
 * <br>
 * <br>il codice "9" corrisponde al "Numero civici Virtuale"
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 16/04/2020 - versione iniziale
 */
public enum EnumTipologiaCivico implements EnumToponomasticaCoto {
	
	/** 1	Numero civico residenziale */
	RESIDENZIALE(1,"Numero civico residenziale"),
	/** 2	Numero civico bloccato */
	BLOCCATO(2,"Numero civico bloccato"),
	/** 3	Numero civico misto */
	MISTO(3,"Numero civico misto"),
	/** 4	Numero civico non residenziale */
	NON_RESIDENZIALE(4,"Numero civico non residenziale"),
	/** 9	Numero civico Virtuale */
	VIRTUALE(9,"Numero civico Virtuale"),
	;
	
	private final Integer idTipologia;
	private final String descrizione;	
	
	private EnumTipologiaCivico(Integer idTipologia, String descrizione) {
		this.idTipologia = idTipologia;
		this.descrizione = descrizione;
	}
	
	@Override
	public String getCodice() {
		return String.valueOf(idTipologia);
	}


	public Integer getIdTipologia() {
		return idTipologia;
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
	public static EnumTipologiaCivico byCodice(String codice) {
		for(EnumTipologiaCivico d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	public static EnumTipologiaCivico byIdAzione(Integer idTipologia) {
		for(EnumTipologiaCivico d : values()) {
			if(d.idTipologia.equals(idTipologia)) {
				return d;
			}
		}
		return null;
	}
}
