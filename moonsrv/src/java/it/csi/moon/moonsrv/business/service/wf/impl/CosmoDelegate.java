/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.wf.impl;

import it.csi.cosmo.callback.v1.dto.AggiornaStatoPraticaRequest;
import it.csi.cosmo.callback.v1.dto.Esito;
import it.csi.moon.commons.entity.CosmoLogPraticaEntity;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

public interface CosmoDelegate {

	public enum ConfKeys {
		INCLUDE_ALLEGATI("include_allegati", Boolean.class, null, true),
//		ALLEGATO_FIGLIO("allegato_figlio", Boolean.class, null, false),
		ALLEGATO_FIGLIO("allegato_figlio", Boolean.class, null, true),
		ID_PRATICA("idPratica", String.class, "@@CODICE_ISTANZA@@", null), // codice_istanza
		CODICE_TIPOLOGIA("codiceTipologia", String.class, "@@CODICE_MODULO@@", null), // codice_modulo
		OGGETTO("oggetto", String.class, "@@CODICE_ISTANZA@@ @@COGNOME@@ @@Nome@@", null), // codice_istanza cognome nome
		CODICE_IPA_ENTE("codiceIpaEnte", String.class, "@@CODICE_IPA_ENTE@@", null), 
		RIASSUNTO("riassunto", String.class, "Istanza MOON @@CODICE_ISTANZA@@ (idIstanza:@@ID_ISTANZA@@)<br/>Modulo @@CODICE_MODULO@@  @@VERSIONE_MODULO@@  @@OGGETTO_MODULO@@ (idModulo:@@ID_MODULO@@)<br/>Ente @@CODICE_ENTE@@  @@NOME_ENTE@@<br/>Compilato da @@CODICE_FISCALE@@ @@COGNOME@@ @@Nome@@<br/>@@ATTORE_INS_CONTO_TERZI@@  @@COGNOME_CONTO_TERZI@@ @@Nome_CONTO_TERZI@@", null), 
		UTENTE_CREAZIONE("utenteCreazione", String.class, "@@CODICE_FISCALE@@", null), // cf
		CODICE_TIPO_DOC_ISTANZA("codiceTipoDocIstanza", String.class, "CODICE_TIPO_DOC_ISTANZA", null), // "richiesta-patrocinio"
		CODICE_TIPO_DOC_ALLEGATO("codiceTipoDocAllegato", String.class, "CODICE_TIPO_DOC_ALLEGATO", null), // "allegati-richiesta-patrocinio"
		CODICE_TIPO_DOC_INTEGRAZIONE("codiceTipoDocIntegrazione", String.class, "CODICE_TIPO_DOC_INTEGRAZIONE", null), // "richiesta-patrocinio"
		CODICE_TIPO_DOC_INTEGRAZIONE_ALLEGATO("codiceTipoDocIntegrazioneAllegato", String.class, "CODICE_TIPO_DOC_INTEGRAZIONE_ALLEGATO", null), // "allegati-richiesta-patrocinio"
		;

		private <T> ConfKeys(String key, Class<T> clazz, String textDefaultValue, Boolean booleanDefaultValue) {
			this.key = key;
			this.textDefaultValue = textDefaultValue;
			this.booleanDefaultValue = booleanDefaultValue;
		}

		private String key;
		private String textDefaultValue;
		private Boolean booleanDefaultValue;
		
		public String getKey() {
			return key;
		}
		public String getTextDefaultValue() {
			return textDefaultValue;
		} 
		public boolean getBooleanDefaultValue() {
			return booleanDefaultValue;
		} 
	};
	
	public String creaPraticaEdAvviaProcesso();

	public Esito callbackPutStatoPraticaV1(AggiornaStatoPraticaRequest pratica, CosmoLogPraticaEntity cosmoLogPratica);

	public String inviaIntegrazione() throws BusinessException;
	
}
