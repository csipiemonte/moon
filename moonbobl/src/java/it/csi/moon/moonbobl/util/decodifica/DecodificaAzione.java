/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.business.service.impl.dto.AzioneEntity;
import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica delle Azione.
 * 
 * @see AzioneEntity
 * Tabella moon_wf_d_azione
 * PK: idAzione corrispondente al DecodificaAzione.getCodice()
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 23/12/2019 - versione iniziale
 *
 */
public enum DecodificaAzione implements DecodificaMOON {

	
	/** 1 - Invia */
	INVIA(1L,"INVIA","Invia","I dati dell'istanza non sono piu modificabili e la gestione passa al ente di destinazione"),
	/** 2 - Salva in bozza */
	SALVA_BOZZA(2L,"SALVA_BOZZA","Salva in bozza","l'istanza e' salvata in bozza per essere completata successivamente"),
	/** 3 - Elimina */
	ELIMINA(3L,"ELIMINA","Elimina","l'istanza viene cancellata e non risulta piu visibile"),
	/** 4 - Archivia */
	ARCHIVIA(4L,"ARCHIVIA","Archivia","l'istanza viene archiviata e non e' piu soggetta ad altri passaggi di stato"),	
	/** 5 - Prendi in carico */
	PRENDI_IN_CARICO(5L,"PRENDI_IN_CARICO","Prendi in carico","Assegni a te stesso l'onere di gestione dell'istanza"),	
	/** 6 - Assegna a  */
	ASSEGNA_A(6L,"ASSEGNA_A","Assegna a","Assegni a qualcun altro l'onere di gestione dell'istanza "),
	/** 7 - Rifiuta  */
	RIFIUTA(7L,"RIFIUTA","Rifiuta","Chiudi l'iter dell'istanza con un rifiuto"),	
	/** 8 - Protocolla  */
	INSERISCI_PROTOCOLLO(8L,"INSERISCI_PROTOCOLLO","Protocolla","Assegna numero di protocollo"),
	/** 9 - Inoltra a  */
	INOLTRA_A(9L,"INOLTRA_A","Inoltra a","Invia l'istanza ad altro soggetto"),	
	/** 10 - Completa */
	COMPLETA(10L,"COMPLETA","Completa","Salva e completa"),	
	/** 11 - Trasmetti a NAO */
	TRASMETTI_A_NAO(11L,"TRASMETTI_A_NAO","Trasmetti a NAO","Invia a sistema NAO"),	
	/** 12 - Concludi con esito positivo */
	CONCLUDI_CON_ESITO_POSITIVO(12L,"CONCLUDI_CON_ESITO_POSITIVO","Concludi con esito positivo","L'iter viene concluso con esito positivo"),	
	/** 13 - Concludi con esito positivo */
	CONCLUDI_CON_ESITO_NEGATIVO(13L,"CONCLUDI_CON_ESITO_NEGATIVO","Concludi con esito negativo","L'iter viene concluso con esito negativo"),
	/** 14 - Torna in istruttoria */
	TORNA_IN_ISTRUTTORIA(14L,"TORNA_IN_ISTRUTTORIA","Torna in istruttoria","L'istanza torna in istruttoria"),	
	/** 15 - Richiedi integrazione */
	RICHIEDI_INTEGRAZIONE(15L,"RICHIEDI_INTEGRAZIONE","Richiedi integrazione","Richiesta al richiedente di una integrazione o modifica"),		
	/** 16 - Compilazione dati per ricevuta */
	PREPARA_RICEVUTA(16L,"PREPARA_RICEVUTA","Prepara ricevuta","Compilazione dati per ricevuta"), 
	/** 17 - Riporta in Bozza */
	RIPORTA_IN_BOZZA(17L,"RIPORTA_IN_BOZZA","Riporta in Bozza","Riporta l'istanza allo stato bozza"), 
	/** 18 - Dai parere positivo */
	DAI_PARERE_POSITIVO(18L,"DAI_PARERE_POSITIVO","Dai parere positivo","Assegna parere positivo"),
	/** 19 - Dai parere negativo */
	DAI_PARERE_NEGATIVO(19L,"DAI_PARERE_NEGATIVO","Dai parere negativo","Assegna parere negativo"),
	/** 20 - Accogli integrazione */	
	ACCOGLI_INTEGRAZIONE(20L,"ACCOGLI_INTEGRAZIONE","Accogli integrazione","Accogli un'integrazione inviata dall'utent"),
	/** 21 - Per Integrazione Fruitore */	
	PER_INTEGRAZIONE_FRUITORE(21L,"PER_INTEGRAZIONE_FRUITORE","Per Integrazione Fruitore","Invia a sistema NAO"),
	/** 22 - Integrazione Fruitore OK */
	INTEGRAZIONE_FRUITORE_OK(22L,"INTEGRAZIONE_FRUITORE_OK","Integrazione Fruitore OK","L'iter viene concluso con esito positivo"),
	/** 23 - Integrazione Fruitore KO */
	INTEGRAZIONE_FRUITORE_KO(23L,"INTEGRAZIONE_FRUITORE_KO","Integrazione Fruitore KO","L'iter viene concluso con esito negativo"),
	/** 24 - Valida dati beneficiario */
	VALIDA_DATI_BENEFICIARIO(24L,"VALIDA_DATI_BENEFICIARIO","Valida dati beneficiario","Valida i dati del beneficiario"),
	/** 25 - Modifica importi */
	MODIFICA_IMPORTI(25L,"MODIFICA_IMPORTI","Modifica importi","Modifica importi inseriti in istanza"),
	/** 26 - Approva */
	APPROVA(26L,"APPROVA","Approva","Approva"),
	/** 27 - Verifica IBAN */
	VERIFICA_IBAN(27L,"VERIFICA_IBAN","Verifica IBAN","Verifica IBAN del comune"),
	/** 28 - Gestisci in pagamento */
	GESTISCI_PAGAMENTO(28L,"GESTISCI_PAGAMENTO","Gestisci in pagamento","Gestisci in pagamento"),
	/** 29 - Respingi oltre limite */
	RESPINGI_OVER_LIMIT(29L,"RESPINGI_OVER_LIMIT","Respingi oltre limite","Respingi per superamento limite plafond bando"),
	/** 30 - Respingi doppia */
	RESPINGI_DOPPIA(30L,"RESPINGI_DOPPIA","Respingi doppia","Respingi doppia"),
	/** 31 - Prosegui */
	PROSEGUI(31L,"PROSEGUI","Prosegui","Prosegui"),
	/** 32 - Torna in validata */
	TORNA_IN_VALIDATA(32L,"TORNA_IN_VALIDATA","Torna in validata","Torna in validata"),
	/** 33 - Genera ricevuta */
	GENERA_RICEVUTA(33L,"GENERA_RICEVUTA","Genera ricevuta","Genera ricevuta"),
	/** 34 - Invia ricevuta */
	INVIA_RICEVUTA(34L,"INVIA_RICEVUTA","Invia ricevuta","Invia ricevuta"),	
	/** 35 - Invia integrazione */
	INVIA_INTEGRAZIONE(35L,"INVIA_INTEGRAZIONE","Invia integrazione","Invia integrazione"),
	/** 36 - Invia integrazione */
	RESPINGI(36L,"RESPINGI","Respingi istanza","Respingi istanza"),
	/** 37 - Effettua pagamento */
	EFFETTUA_PAGAMENTO(37L,"EFFETTUA_PAGAMENTO","Effettua pagamento","Effettua pagamento"),
	/** 38 - Invito a confor. debiti */
	INVITO_CONFOR_DEBITI(38L,"INVITO_CONFOR_DEBITI","Invito a confor. debiti","Invito a conformare debiti"),
	/** 39 - Invia comunicazione */
	INVIA_COMUNICAZIONE(39L,"INVIA_COMUNICAZIONE","Invia comunicazione","Invia comunicazione"),
	/** 40 - Respingi con comunicazione */
	RESPINGI_EMAIL(40L,"RESPINGI_EMAIL","Respingi con comunicazione","Respingi con comunicazione"),
	/** 41 - Annulla	Annulla istanza	*/
	ANNULLA(41L,"ANNULLA","Annulla","Annulla istanza"),
	/** 42	Prendi in carico integrazione	Prendi in carico integrazione */
	PRENDI_IN_CARICO_INTEGRAZIONE(42L,"PRENDI_IN_CARICO_INTEGRAZIONE","Prendi in carico integrazione","Prendi in carico integrazione"),
	/** 43	Richiedi ulteriore integraz.	Richiedi ulteriore integrazione */
	RICHIEDI_ULTERIORE_INTEGRAZIONE(43L,"PRENDI_IN_CARICO_INTEGRAZIONE","Richiedi ulteriore integraz.","Richiedi ulteriore integrazione"),
	/** 44	Presa visione	Presa atto dell'istanza */
	PRESA_ATTO(44L,"PRESA_ATTO","Presa visione","Presa atto dell''istanza"),
	/** 45 - Modifica d'ufficio */
	MODIFICA(45L,"MODIFICA","Modifica d'ufficio","Modifica d'ufficio"),
	/** 46	Torna in inviata	L'istanza torna in inviata */
	TORNA_IN_INVIATA(46L,"TORNA_IN_INVIATA","Torna in inviata","L'istanza torna in inviata"),
	/** 47	Accogli	L'istanza è stata accolta dopo la verifica da parte del sistema */
	ACCOGLI(47L,"ACCOGLI","Accogli","L'istanza è stata accolta dopo la verifica da parte del sistema"),
	/** 48	Annulla per rettifica	L'istanza è stata annullata per rettifica */
	ANNULLA_PER_RETTIFICA(48L,"ANNULLA_PER_RETTIFICA","Annulla per rettifica","L'istanza è stata annullata per rettifica"),
	/** 49	Verifica positiva	Verifica positiva */
	VERIFICA_POSITIVA(49L,"VERIFICA_POSITIVA","Verifica positiva","Verifica positiva"),
	/** 50	Verifica negativa	Verifica negativa */
	VERIFICA_NEGATIVA(50L,"VERIFICA_NEGATIVA","Verifica negativa","Verifica negativa"),
	/** 51	Proponi step	L'istanza viene proposta di passare alla step successivo di progetto */
	PROPONI_STEP(51L,"PROPONI_STEP","Proponi step","L'istanza viene proposta di passare alla step successivo di progetto"),
	/** 52	Proponi unico step	L'istanza viene proposta di passare allo fase unica di progetto */
	PROPONI_UNICO_STEP(52L,"PROPONI_UNICO_STEP","Proponi unico step","L'istanza viene proposta di passare allo fase unica di progetto"),
	/** 53	Approva parzialmente	Approva parzialmente */
	APPROVA_PARZIALMENTE(53L,"APPROVA_PARZIALMENTE","Approva parzialmente","Approva parzialmente"),
	/** 54	Protocolla ricevuta	Protocolla ricevuta */
	PROTOCOLLA_RICEVUTA(54L,"PROTOCOLLA_RICEVUTA","Protocolla ricevuta","Protocolla ricevuta"),
	/** 55	Soggetto mancante */
	SOGGETTO_MANCANTE(55L,"SOGGETTO_MANCANTE","Soggetto mancante","Soggetto mancante"),	
	/** 56	Richiedi ricevuta pagamento */
	RICHIEDI_RICEVUTA(56L,"RICHIEDI_RICEVUTA","Richiedi ricevuta pagamento","Richiedi ricevuta pagamento"),
	/** 57	Invia esito */
	INVIA_ESITO(57L,"INVIA_ESITO","Invia esito","Invio dell'esito da parte dell'operatore o fruitore"),
	/** 58	Accoglimento */
	ACCOGLIMENTO(58L,"ACCOGLIMENTO","Accoglimento","Accoglimento"),
	/** 59	Diniego */
	DINIEGO(59L,"DINIEGO","Diniego","Diniego"),
	/** 60	Approva parzialmente */
	PAGA_ONLINE(60L,"PAGA_ONLINE","Paga online","Paga online"),
	/** 61	Paga sportello */
	PAGA_SPORTELLO(61L,"PAGA_SPORTELLO","Paga sportello","Paga sportello"),
	/** 62	Salva protocollo */
	SALVA_PROTOCOLLO(62L,"SALVA_PROTOCOLLO","Salva protocollo","Salva il protocollo ricevuto dal sistema di protocollazione"),
	/** 63	Crea risposta protocollo */
	CREA_RISPOSTA_CON_PROTOCOLLO(63L,"CREA_RISPOSTA_CON_PROTOCOLLO","crea risposta con protocollo","crea risposta con protocollo"),
	/** 64	Notifica da parte del fruitore API senza cambio di stato */
	NOTIFICA_FRUITORE(64L,"NOTIFICA_FRUITORE","Notifica fruitore","Notifica fruitore"),
	/** 65	Richiedi osservazioni per motivi ostativi */
	RICHIEDI_OSSERVAZIONI(65L,"RICHIEDI_OSSERVAZIONI","Richiedi osservazioni","Richiedi osservazioni per motivi ostativi"),
	/** 66	Invia osservazioni */
	INVIA_OSSERVAZIONI(66L,"INVIA_OSSERVAZIONI","Invia osservazioni","Invia osservazioni"),
	/** 67 Invia Documentazione */
	INVIA_DOCUMENTAZIONE(67L, "INVIA_DOCUMENTAZIONE", "Invia documentazione", "Invia documentazione"),
	/** 68 Invia Documentazione */
	RESTITUISCI_AL_PROPONENTE(67L, "RESTITUISCI_AL_PROPONENTE", "Restituisci al proponente", "Restituisci al proponente"),
	/** 69 Valida */
	VALIDA(69L, "VALIDA", "Valida", "Valida"),
	/** 70 Upload */
	UPLOAD(70L,"UPLOAD_FILE", "Upload", "Upload"),
	/** 71 Carica ricevuta firmata */
	CARICA_RICEVUTA_FIRMATA(71L,"CARICA_RICEVUTA_FIRMATA", "Carica ricevuta firmata", "Carica ricevuta firmata"),
	/** 72 Protocolla */
	PROTOCOLLA(72L,"PROTOCOLLA", "Protocolla", "Richiedi la protocolazione dell'istanza"),
	/** 73 Verifica in ANPR */
	VERIFICA_ANPR(73L,"VERIFICA_ANPR", "Verifica in ANPR", "Verifica in ANPR"),
	;
	
	
	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private AzioneEntity azione = null;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaAzione(Long idAzione, String codiceAzione, String nomeAzione, String descAzione) {
		this.azione = new AzioneEntity(idAzione, codiceAzione, nomeAzione, descAzione);
	}
	
	@Override
	public String getCodice() {
		return azione.getCodiceAzione();
	}
	
	public AzioneEntity getAzioneEntity() {
		return azione;
	}
	public Long getIdAzione() {
		return azione.getIdAzione();
	}
	public String getNomeAzione() {
		return azione.getNomeAzione();
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaAzione byCodice(String codice) {
		for(DecodificaAzione d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	public static DecodificaAzione byIdAzione(Long idAzione) {
		for(DecodificaAzione d : values()) {
			if(d.getAzioneEntity().getIdAzione().equals(idAzione)) {
				return d;
			}
		}
		return null;
	}
	
	public boolean isCorrectCodice(String codiceAzioneToVerify) {
		return getCodice().equals(codiceAzioneToVerify);
	}
}
