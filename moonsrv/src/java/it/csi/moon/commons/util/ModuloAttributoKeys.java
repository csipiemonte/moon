/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util;

/**
 * Decodifica dei NomeAttributo.
 * La conversione dei tipi è gestita nella creazione della Mappa (MapModuloAttributi)
 * 
 * @see MapModuloAttributi
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 20/02/2020 - versione iniziale
 */
public enum ModuloAttributoKeys implements MapKeyHolder {

	/** Nome della class di inizializzazione  Type String */
	INIT_NOME_CLASS(String.class),
	/** Indica se l'inizializzazione e' obbligatoria e non null !{}  Type Boolean, Valore su DB 'S','N' */
	INIT_OBBLIGATORIA(Boolean.class),
	/** Indica se possibile riportare un instanza di un certo modulo in bozza  Type Boolean, Valore su DB 'S','N' */
	RIPORTA_IN_BOZZA_ABILITATO(Boolean.class),
	/** ANPR SPETO Type Boolean, Valore su DB 'S','N' */
	ANPR_SPENTO(Boolean.class),
	/** */
	ESEGUI_VERIFICA_NAO(String.class),
	/** */
	DATA_ULTIMA_VERIFICA(String.class), 
	/** Modalita di Logon da MoonDirect WCL : CF_2DOCUMENTI | NO_AUTH | LOGIN_PWD | GOOGLE_AUTH */
	MOONDIRECT_LOGON_MODE_WCL(String.class), 
	/** Argomenti della Modalita di Logon da MoonDirect WCL :  { login_pwd:{login:{label:"UserName"},password:{label:"PassWord"},pin:{enable:true,label:"Codice Dispositivo",length=5}}, googleAuth: {enable: true} }   */
	MOONDIRECT_LOGON_MODE_WCL_ARGS(String.class), 
	/** Modalita di Login da MoonDirect BL : GET_FAMIGLIA_ANPR, VALIDA_NUMERO_DOC, VALIDA_DATA_DOC, UNIQUE_ISTANZA */
	MOONDIRECT_LOGIN_BL(String.class), 
	/** HASH_UNIVOCITA Type Boolean, Valore su DB 'S','N' */
	HASH_UNIVOCITA(Boolean.class),
	/** HASH_UNIVOCITA_FIELDS Type String,  json ?  nome _FIELDS o _CONF */
	HASH_UNIVOCITA_FIELDS(String.class),
	/** MSG_UNIVOCITA Type String */
	MSG_UNIVOCITA(String.class),
	/** FORMIO_BREADCRUMB_CLICKABLE Type Boolean, Valore su DB 'S','N' */
	FORMIO_BREADCRUMB_CLICKABLE(Boolean.class),
	
	/** PostSaveInstanzaTask SendEmailDichiaranteIstanzaTask S/N */
	PSIT_EMAIL(Boolean.class), 
	/** PostSaveInstanzaTask SendEmailDichiaranteIstanzaTask configuration json */
	PSIT_EMAIL_CONF(String.class), 
	/** PostSaveInstanzaTask NotificaIstanzaTask S/N */
	PSIT_NOTIFY(Boolean.class), 
	/** PostSaveInstanzaTask NotificaIstanzaTask configuration json */
	PSIT_NOTIFY_CONF(String.class), 
	/** PostSaveInstanzaTask ProtocollaIstanzaTask S/N */
	PSIT_PROTOCOLLO(Boolean.class), 
	/** PostSaveIntegrazioneTask ProtocollaIntegrazioneTask S/N */
	PSIT_PROTOCOLLO_INTEGRAZIONE(Boolean.class), 
	/** PostSaveInstanzaTask AzioneIstanzaTask S/N */
	PSIT_AZIONE(Boolean.class), 
	/** PostSaveInstanzaTask AzioneIstanzaTask configuration codiceAzione */
	PSIT_AZIONE_CONF(String.class), 
	/** PostSaveInstanzaTask CosmoTask S/N */
	PSIT_COSMO(Boolean.class), 
	/** PostSaveInstanzaTask CONSMO Configurazione **/
	PSIT_COSMO_CONF(String.class), 
	/** PostSaveInstanzaTask EstraiDichiaranteTask  S/N */
	PSIT_ESTRAI_DICHIARANTE(Boolean.class), 
	/** PostSaveInstanzaTask Estrai Dichiarante Configurazione */
	PSIT_ESTRAI_DICHIARANTE_CONF(String.class), 
	/** PostSaveInstanzaTask CRMTask  S/N */
	PSIT_CRM(Boolean.class), 
	/** Codice del sistema CRM  Type String   es. NEXTCRM, R2U, OTRS */
	PSIT_CRM_SYSTEM(String.class),
	/** PostSaveInstanzaTask CRM Configurazione */
	PSIT_CRM_CONF(String.class), 
	/** PostSaveInstanzaTask CRM Configurazione NEXTCRM */
	PSIT_CRM_CONF_NEXTCRM(String.class), 
	/** PostSaveInstanzaTask CRM Configurazione R2U */
	PSIT_CRM_CONF_R2U(String.class),
	/** PostSaveInstanzaTask CRM Configurazione OTRS */
	PSIT_CRM_CONF_OTRS(String.class),
	/** Tipologia di filtro applicato in moonbowcl */
	TIPO_FILTER_BO(String.class),
	/** PostSaveInstanzaTask EPAY S/N */
	PSIT_EPAY(Boolean.class), 
	/** PostSaveInstanzaTask EPAY Configurazione **/
	PSIT_EPAY_CONF(String.class),
	/** PostSaveInstanzaTask MYDOCS (DOCME) S/N */
	PSIT_MYDOCS(Boolean.class),
	/** PostSaveInstanzaTask MYDOCS (DOCME) Configurazione ({idAmbito}) **/
	PSIT_MYDOCS_CONF(String.class),
	
	/** Nome del PreForm da visualizzare nel caso di ContoTerzi Type String */
	CONTO_TERZI(String.class),
	/** Nome del PreForm da visualizzare nel caso di Compilazione da BO Type String */
	COMPILA_BO(String.class),
	/** Modulistica permette di potere scaricare il PDF dopo l'invio */
	STAMPA_PDF(String.class),
	/** Abilitazione dell'autosave lato FO nella compilazione dell'istanza */
	ISTANZA_AUTO_SAVE(Boolean.class),
	/** Abilita la stampa dinamica, anche se è presente un mapper */
	STAMPA_DINAMICA(Boolean.class),
	/** Abilita la modifica di un istanza anche se inviata su BO ai utenti authorizzati */
	MODIFICA_ISTANZA_INVIATA(Boolean.class),
	/** ProtocollaBO S/N */
	PROTOCOLLA_BO(Boolean.class), 
	/** ProtocollaIntegrazioneBO S/N */
	PROTOCOLLA_INTEGRAZIONE_BO(Boolean.class), 	
	
	/** Durata del procedimento Configurazione notifica-invio-modulo*/
	DURATA_PROCEDIMENTO_CONF(String.class), 
	
	/** API PostAzione SEND_EMAIL Configurazione **/
	APA_EMAIL_CONF(String.class), 
	
	/** Protocollo Metadati */
	PRT_METADATI(String.class),
	
	/** PostCallbackProtocolloTask Protocollo_IN SendEmailDichiaranteProtocolloTask S/N **/
	PCPT_IN_EMAIL(Boolean.class), 
	/** PostCallbackProtocolloTask Protocollo_IN SendEmailDichiaranteProtocolloTask configuration json */
	PCPT_IN_EMAIL_CONF(String.class),
	/** PostCallbackProtocolloTask AzioneIstanzaTask S/N */
	PCPT_AZIONE(Boolean.class), 
	/** PostCallbackProtocolloTask AzioneIstanzaTask configuration codiceAzione */
	PCPT_AZIONE_CONF(String.class), 
	;

	
	private String key;
	private Class<?> type;
	
	private ModuloAttributoKeys(Class<?> type) {
		this.type = type;
		this.key = /*this.getClass().getSimpleName() + "_KEY_" +*/ this.name();
	}

	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static ModuloAttributoKeys byName(String nome) {
		for(ModuloAttributoKeys k : values()) {
			if(k.name().equals(nome)) {
				return k;
			}
		}
		return null;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}
	public Class<?> getType() {
		return this.type;
	}
	
	@Override
	public boolean isCorrectType(Object obj) {
		return type.isInstance(obj);
	}

	@Override
	public boolean isNullOrCorrectType(Object obj) {
		return obj == null || type.isInstance(obj);
	}


	public boolean isCorrectName(String nameDaVerificare) {
		return key.equals(nameDaVerificare);
	}
}
