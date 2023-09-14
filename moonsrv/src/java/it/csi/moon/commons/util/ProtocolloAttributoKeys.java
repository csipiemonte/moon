/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util;

/**
 * Decodifica dei attributi di configurazione del protocollo presenti nella tabella moon_pr_d_parametri.
 * La conversione dei tipi è gestita nella creazione della Mappa (MapProtocolloAttributi)
 * 
 * @see MapProtocolloAttributi
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/06/2020 - versione iniziale
 */
public enum ProtocolloAttributoKeys implements MapKeyHolder {

	/** Codice del sistema di protocollazione in INPUT per le ISTANZE  Type String   es. STARDAS */
	SISTEMA_PROTOCOLLO_ISTANZA(String.class, 1),
	/** Nome dell'attributo nel modulo nel caso di estrazione numero protocollo dall'istanza, Type String   es. protocollo.numero */
	NUMERO_PROTOCOLLO_FROM_ISTANZA(String.class, 2),
	/** Nome dell'attributo nel modulo nel caso di estrazione data protocollo dall'istanza, Type String   es. protocollo.anno */
	DATA_PROTOCOLLO_FROM_ISTANZA(String.class, 3),
	
	/** CodiceFiscale del Responsabile al Trattamento Dati                       used in STARDAS */
	CODICE_FISCALE_RESPONSABILE_TRATTAMENTO(String.class, 10),
	/** CodiceFiscale dell'ente         es. 00514490010                          used in STARDAS */
	CODICE_FISCALE_ENTE(String.class, 11),
	/** Codice Fruitore                 es. MOON                                 used in STARDAS */
	CODICE_FRUITORE(String.class, 12),
	/** Codice Applicazione             es. MOONSRV                              used in STARDAS */
	CODICE_APPLICAZIONE(String.class, 13),
	/** Codice Tipo Documento           es. DICH_IMU                             used in STARDAS, MAG_ProtocolloSoap */
	CODICE_TIPO_DOCUMENTO(String.class, 14),
	/** Codice Tipo Documento           es. DICH_IMU_ALL                         used in STARDAS */
	CODICE_TIPO_DOCUMENTO_ALLEGATI(String.class, 15),
	/** Protocolla Allegati             es. S/N                                  used in STARDAS */
	PROTOCOLLA_ALLEGATI(Boolean.class, 16),
	/** Protocolla XML Resonconto       es. S/N                                  used in STARDAS */
	PROTOCOLLA_XML_RESOCONTO(Boolean.class, 17),
	/** Codice Tipo Documento Integrazione  es.                                  used in STARDAS, MAG_ProtocolloSoap */
	CODICE_TIPO_DOC_INTEGRAZIONE(String.class, 18),
	/** Codice Tipo Allegato Integrazione   es.                                  used in STARDAS */
	CODICE_TIPO_DOC_INTEGRAZIONE_ALLEGATI(String.class, 19),
	/** Codice Tipo File Uscita             es.                                  used in STARDAS, MAG_ProtocolloSoap */
	CODICE_TIPO_DOCUMENTO_USCITA(String.class, 20),
	/** Abilita la verifica firma via DoSign es. S/N                                             */
	VERIFICA_FIRMA_DOSIGN(Boolean.class, 21),
	/** Use Metadati Configured         es. S/N                                  used in STARDAS */
	USE_METADATI_CONFIGURED(Boolean.class, 22),
	/** Additional Allegati (es.From RepositoryFile) es. repository_file_tipologia:6             */
	ADDITIONAL_ALLEGATI(String.class, 23),

	/** Codice Amministrazione           es.                                     used in MAG_ProtocolloSoap */
	CODICE_AMMINISTRAZIONE(String.class, 40),
	/** Codice AOO                       es. AOOb-PG                             used in MAG_ProtocolloSoap */
	CODICE_AOO(String.class, 41),
	/** Classifica                       es. 1.1                                 used in MAG_ProtocolloSoap */
	CLASSIFICA(String.class, 42),
	/** Oggetto                          es. Richiesta di autorizzazione         used in MAG_ProtocolloSoap */
	OGGETTO(String.class, 43),
	/** Origine                          es. A=arrivo,I=interno,P=partenza       used in MAG_ProtocolloSoap */
	ORIGINE(String.class, 44),
	/** MittenteInterno                  es. SEGRETERIA                          used in MAG_ProtocolloSoap */
	MITTENTE_INTERNO(String.class, 45),

	/** MittenteDestinatario.CodiceFiscale es. BRNNMR54L53I444J                  used in MAG_ProtocolloSoap */
	MITDEST_CODICE_FISCALE(String.class, 50),
	/** MittenteDestinatario.CognomeNome es. ROSSI                               used in MAG_ProtocolloSoap */
	MITDEST_COGNOME_NOME(String.class, 51),
	/** MittenteDestinatario.Nome        es. MARIO                               used in MAG_ProtocolloSoap */
	MITDEST_NOME(String.class, 52),
	/** MittenteDestinatario.Indirizzo   es. Vicolo Gumer, 7                     used in MAG_ProtocolloSoap */
	MITDEST_INDIRIZZO(String.class, 53),
	/** MittenteDestinatario.Localita    es. Bolzano                             used in MAG_ProtocolloSoap */
	MITDEST_LOCALITA(String.class, 54),
	/** MittenteDestinatario.CodiceComuneResidenza es. 021008                    used in MAG_ProtocolloSoap */
	MITDEST_CD_CM_RESID(String.class, 55),
	/** MittenteDestinatario.Recapiti.Recapito.ValoreRecapito (EMAIL) es. lorenzo.valenti@maggioli.it  used in MAG_ProtocolloSoap */
	MITDEST_RECAPITO_EMAIL(String.class, 56),
	;

	
	private String key;
	private Class<?> type;
	private Integer order;
	
	private ProtocolloAttributoKeys(Class<?> type, Integer order) {
		this.type = type;
		this.key = /*this.getClass().getSimpleName() + "_KEY_" +*/ this.name();
		this.order = order;
	}

	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static ProtocolloAttributoKeys byName(String nome) {
		for(ProtocolloAttributoKeys k : values()) {
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
	public Integer getOrder() {
		return this.order;
	}
	
	@Override
	public boolean isCorrectType(Object obj) {
		return type.isInstance(obj);
	}

	@Override
	public boolean isNullOrCorrectType(Object obj) {
		return obj == null || type.isInstance(obj);
	}

}
