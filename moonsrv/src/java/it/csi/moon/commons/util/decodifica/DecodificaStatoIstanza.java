/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

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

	/** 1 - BOZZA */
	BOZZA(1,"BOZZA","BOZZA","istanza non completa", 1),
	/** 2 - INVIATA */
	INVIATA(2,"INVIATA","INVIATA","istanza inviata all'ente competente", 2),
	/** 3 - ELIMINATA */
	ELIMINATA(3,"ELIMINATA","ELIMINATA","istanza eliminata", 3),
	/** 4 - ARCHIVIATA */
	ARCHIVIATA(4,"ARCHIVIATA","ARCHIVIATA","istanza archiviata", 2),
//	/** 5 - IN ISTRUTTORIA */
//	IN_ISTRUTTORIA(5,"IN ISTRUTTORIA","istanza in lavorazione"),
//	/** 6 - RIFIUTATA */
//	RIFIUTATA(6,"RIFIUTATA","istanza assegnata ad un referente"),
//	/** 7 - IN ATTESA DI PARERE */
//	IN_ATTESA_DI_PARERE(7,"IN ATTESA DI PARERE","istanza in attesa di un parere"),
//	/** 8 - CONCLUSA POSITIVAMENTE */
//	CONCLUSA_POSITIVAMENTE(8,"CONCLUSA POSITIVAMENTE","istanza conclusa positivamente"),
//	/** 9 - ACCOLTA */
//	ACCOLTA(9,"ACCOLTA","istanza accolta"),
	/** 10 - COMPLETATA */
	COMPLETATA(10,"COMPLETATA","DA INVIARE","istanza completata ma non inviata", 1),
//	/** 11 - IN ATTESA ESITO FINALE */
//	IN_ATTESA_ESITO_FINALE(11,"IN ATTESA ESITO FINALE","istanza pronta per la comunicazione di esito fina..."),
//	/** 12 - CONCLUSA NEGATIVAMENTE */
//	CONCLUSA_NEGATIVAMENTE(12,"CONCLUSA NEGATIVAMENTE","istanza conclusa negativamente"),
//	/** 13 - PARERE POSITIVO */
//	PARERE_POSITIVO(13,"PARERE POSITIVO","parere positivo"),
//	/** 14 - PARERE NEGATIVO */
//	PARERE_NEGATIVO(14,"PARERE NEGATIVO","parere negativo"),
	/** 15 - IN ATTESA INTEGRAZIONE */
	IN_ATTESA_INTEGRAZIONE(15,"IN_ATTESA_INTEGRAZIONE","IN ATTESA INTEGRAZIONE","istanza in attesa di una integrazione", 1),
//	/** 16 - INTEGRAZIONE INVIATA */
//	INTEGRAZIONE_INVIATA(16,"INTEGRAZIONE INVIATA","integrazione inviata"),
//	/** 17 - ASSEGNAZIONE PROTOCOLLO */
//	ASSEGNAZIONE_PROTOCOLLO	(17,"ASSEGNAZIONE PROTOCOLLO","in fase di assegnazione protocollo"),
//	/** 18 - PROTOCOLLATA */
//	PROTOCOLLATA(18,"PROTOCOLLATA","assegnato protocollo"),	
//	/** 19 - INTEGRAZIONE FRUITORE */		
//	INTEGRAZIONE_FRUITORE(19,"INTEGRAZIONE FRUITORE","istanza pronta per integrazione fruitore"),	
//	/** 20 - INTEGRAZIONE FRUITORE */		
//	INTEGRAZIONE_FRUITORE_OK(20,"INTEGRAZIONE FRUITORE OK","integrazione fruitore conclusa positivamente"),	
//	/** 21 - INTEGRAZIONE FRUITORE KO */		
//	INTEGRAZIONE_FRUITORE_KO(21,"INTEGRAZIONE FRUITORE KO","integrazione fruitore conclusa negativamente"),
//	/** 22 - DA PAGARE */
	DA_PAGARE(22,"DA_PAGARE","DA PAGARE","pronta per essere esporatata per pagamento", 1),
//	/** 23 - IN PAGAMENTO */
	IN_PAGAMENTO(23,"IN_PAGAMENTO", "IN PAGAMENTO", "esportata per pagamento", 1),
//	/** 24 - APPROVATA */		
//	APPROVATA(24,"APPROVATA","approvata"),	
//	/** 25 - IN VERIFICA */		
//	IN_VERIFICA(25,"IN VERIFICA","in verifica"),	
//	/** 26 - NON ACCOLTA */
//	NON_ACCOLTA(26,"NON ACCOLTA","istanza non accolta"),
//	/** 27 - IN VERIFICA DATI PRATICA */
//	IN_VERIFICA_DATI_PRATICA(27,"IN VERIFICA DATI PRATICA","E' necessario verificare i dati della pratica rispetto a quanto indicato dall'utente"),
//	/** 28 - IN VERIFICA DATI BENEFICIARIO */
//	IN_VERIFICA_DATI_BENEFICIARIO(28,"IN VERIFICA DATI BENEFICIARIO","E' necessario verificare i dati del beneficiario rispetto all'intestatario della pratica"),
//	/** 29 - IN VERIFICA IMPORTI */
//	IN_VERIFICA_IMPORTI(29,"IN VERIFICA IMPORTI","E' necessario verificare la tipologia intervento e gli importi indicati dall'utente"),
//	/** 30 - VALIDATA */
//	VALIDATA(29,"IN VERIFICA IMPORTI","Istanza validata"),
//	/** 31 - DA APPROVARE */
//	DA_APPROVARE(31,"DA APPROVARE","Istanza da approvare"),
//	/** 32 - DA NOTIFICARE */
//	DA_NOTIFICARE(32,"DA NOTIFICARE","In attesa di invio notifica"),
//	/** 33 - NOTIFICATA */
//	NOTIFICATA(33,"NOTIFICATA","Notifica inviata"),
//	/** 34 - RESPINTA */
//	RESPINTA(34,"RESPINTA","Istanza respinta"),
	/** 47 - IN_PAGAMENTO_ONLINE */
	IN_PAGAMENTO_ONLINE(47,"IN_PAGAMENTO_ONLINE","IN PAGAMENTO ONLINE","In pagamento online su PagoPA", 1),
	/** 48 - ATTESA_PAGAMENTO */
	ATTESA_PAGAMENTO(48,"ATTESA_PAGAMENTO","ATTESA ESITO PAGAMENTO","In attesa di esito del pagamento", 1),
	/** 54 - ATTESA_RICEVUTA_PAGAMENTO */
	ATTESA_RICEVUTA_PAGAMENTO(54,"ATTESA_RICEVUTA_PAGAMENTO","IN ATTESA DI RICEVUTA PAGAMENTO","istanza in attesa di una ricevuta di pagamento", 2),
	/** 60 - RICEVUTA CARICATA */
	RICEVUTA_CARICATA(60,"RICEVUTA_CARICATA","RICEVUTA CARICATA","Ricevuta caricata", 1),
	;
	
	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private Integer idStatoWf = null;
	private String codiceStatoWf = null;
	private String nomeStatoWf = null;
	private String descStatoWf = null;
	private Integer idTabFo = null;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaStatoIstanza(Integer idStatoWf, String codiceStatoWf, String nomeStatoWf, String descStatoWf, Integer idTabFo) {
		this.idStatoWf = idStatoWf;
		this.codiceStatoWf = codiceStatoWf;
		this.nomeStatoWf = nomeStatoWf;
		this.descStatoWf = descStatoWf;
		this.idTabFo = idTabFo;
	}
	
	@Override
	public String getCodice() {
		return String.valueOf(idStatoWf);
	}
	
	public StatoEntity getStatoEntity() {
		return new StatoEntity(idStatoWf, codiceStatoWf, nomeStatoWf, descStatoWf, idTabFo);
	}

	public Integer getIdStatoWf() {
		return idStatoWf;
	}
	public String getCodiceStatoWf() {
		return codiceStatoWf;
	}
	public String getNomeStatoWf() {
		return nomeStatoWf;
	}
	public String getDescStatoWf() {
		return descStatoWf;
	}
	public Integer getIdTabFo() {
		return idTabFo;
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
			if(d.getIdStatoWf().equals(idStatoWf)) {
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

}
