/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.entity.ProcessoEntity;
import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica dello ProcessoIstanza.
 * 
 * @see ProcessoEntity
 * Tabella moon_wf_d_processo
 * PK: idProcesso  
 * DecodificaProcessoIstanza.getCodice() corrisponde a  ProcessoEntity.getCodiceProcesso()
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/03/2020 - versione iniziale
 */
public enum DecodificaProcessoIstanza implements DecodificaMOON {
	
	/** 1 - COMP - COMPILAZIONE - Compilazione di una istanza da parte di un cittadino */
	COMPILAZIONE(1L,"COMP", "COMPILAZIONE", "Compilazione di una istanza da parte di un cittadino"),
	/** 2 - GEST - GESTIONE - Gestione dell'iter di approvazione di una istanza da parte dell'ufficio competente */
	GESTIONE(2L,"GEST", "GESTIONE", "Gestione dell'iter di approvazione di una istanza da parte dell'ufficio competente"),
	/** 3 - GEST_EDIL - GESTIONE ISTANZE EDILIZIA - Gestione iter istanze edilizia */
	GEST_EDIL(3L,"GEST_EDIL", "GESTIONE ISTANZE EDILIZIA", "Gestione iter istanze edilizia"),
	/** 4 - GEST_CONT - GESTIONE CONTATTI - Gestione dell'iter di invio dei contatti */
	GEST_CONT(4L,"GEST_CONT", "GESTIONE CONTATTI", "Gestione dell'iter di invio dei contatti");

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private ProcessoEntity processo = null;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaProcessoIstanza(Long idProcesso, String codiceProcesso, String nomeProcesso, String descProcesso) {
		this.processo = new ProcessoEntity(idProcesso, codiceProcesso, nomeProcesso, descProcesso);
	}
	
	@Override
	public String getCodice() {
		return String.valueOf(processo.getCodiceProcesso());
	}
	
	public ProcessoEntity getProcessoEntity() {
		return processo;
	}
	public Long getIdProcesso() {
		return processo.getIdProcesso();
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaProcessoIstanza byCodice(String codice) {
		for(DecodificaProcessoIstanza d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	public static DecodificaProcessoIstanza byIdProcesso(Long idProcesso) {
		for(DecodificaProcessoIstanza d : values()) {
			if(d.getIdProcesso().equals(idProcesso)) {
				return d;
			}
		}
		return null;
	}
}
