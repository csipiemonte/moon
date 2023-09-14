/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util;

public class Constants {
	
	public static final String COMPONENT_NAME = "moonfobl";
	
	public static final String INTEGRATION_LAYER = "integration";
	public static final String BUSINESS_LAYER = "business";
	public static final String SECURITY_LAYER = "security";
	
	/**
	 * Codici specifici di alcuni moduli
	 */
	public static final String CODICE_MODULO_DEM_001 = "DEM_001";
	public static final String CODICE_MODULO_BUONO_SPESA_COVID19 = "BUONO";
	public static final String CODICE_MODULO_EDIL_CONT_COSTR = "CONT_COSTR";
	public static final String CODICE_MODULO_EDIL_CONT_COMUNE = "CONT_COMUNE";
	public static final String CODICE_MODULO_EDIL_CONT_AGGIORNAMENTO_IBAN = "CONT_AGGIORNAMENTO_IBAN";
	public static final String CODICE_MODULO_EDIL_CONT_AGG_OPE = "CONT_AGG_OPE";
	public static final String CODICE_MODULO_COMM_RINNOVO = "COMM_RINNOVO";
	public static final String CODICE_MODULO_SP02_2021 = "SP02_2021";
	public static final String CODICE_MODULO_TAXI = "TAXI";
	public static final String CODICE_MODULO_TAXI_CI = "TAXI_CI";
	public static final String CODICE_MODULO_DEM_CINDCAF = "DEM_CINDCAF";
	public static final String CODICE_MODULO_DEM_CRESCAF = "DEM_CRESCAF";
	
	/**
	 * SESSION ATTRIBUTES
	 */
	public static final String SESSION_MOON_ID_JWT = "Moon-Identita-JWT";
	public static final String SESSION_USERINFO = "appDatacurrentUser";
	public static final String SESSION_LOGIN_TIPO_DOC_RICONOSCIMENTO = "LOGIN_TIPO_DOC_RICONOSCIMENTO";
	public static final String SESSION_LOGIN_DOC_RICONOSCIMENTO = "LOGIN_DOC_RICONOSCIMENTO";
	public static final String SESSION_JOINED_CODICE_FISCALI = "JOINED_CODICI_FISCALI";
	public static final String SESSION_PORTALNAME = "appDataPortalName";

	/**
	 * Codici specifici di alcuni processi
	 */
	public static final String CODICE_PROCESSO_COTO_RESID = "COTO_RESID";
	public static final String CODICE_PROCESSO_COTO_RIN = "COTO_RIN";
	public static final String CODICE_PROCESSO_ASL_AUTOR = "ASL_AUTOR";
	public static final String CODICE_PROCESSO_COSMO = "GEST_COSMO";


}
