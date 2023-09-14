/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util.decodifica;

import it.csi.moon.commons.util.decodifica.DecodificaPortale;
import it.csi.moon.moonfobl.dto.moonfobl.LogonMode;

/**
 * Decodifica degli Attributi base LogonMode inizializzabile per Portale
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 30/11/2020 - versione iniziale
 */
public enum DecodificaLogonModePortale implements DecodificaMOON {
	
	/** 2 - TOFACILE - moon-torinofacile.patrim.csi.it -  */
	TOFACILE_2("TOFACILE", 2L, "logotorinofacile.png", "Modulistica della Città di Torino", "http://www.torinofacile.it/"),
	/** 3 - TOFACILE-BO - moon-bo.patrim.csi.it -  */
	TOFACILE_BO_3("TOFACILE-BO", 2L, "logotorinofacile.png", "Modulistica della Città di Torino", "http://www.torinofacile.it/"),
	/** 4 - CMTO - moon-cittametropolitanatorino.patrim.csi.it -  */
	CMTO_4("CMTO", 3L, "logo_cmto.jpg", "Modulistica della Città Metropolitana di Torino", "http://www.cittametropolitana.torino.it/"),
	/** 5 - CMTO-BO - moon-bo-cmto.patrim.csi.it -  */
	CMTO_BO_5("CMTO-BO", 3L, "logo_cmto.jpg", "Modulistica della Città Metropolitana di Torino", "http://www.cittametropolitana.torino.it/"),
	/** 6 - REGP - moon.patrim.csi.it -  */
	REGP_6("REGP", 1L, "logo_RegPiem.jpg", "Modulistica della Regione Piemonte", "http://https://www.regione.piemonte.it/web/"),
	/** 7 - REGP-BO - moon-ru.patrim.csi.it -  */
	REGP_BO_7("REGP-BO", 1L, "logo_RegPiem.jpg", "Modulistica della Regione Piemonte", "http://https://www.regione.piemonte.it/web/"),
	/** 8 - REGP_TEST - moon-internet.patrim.csi.it -  */
	REGP_TEST_8("REGP_TEST", 1L, "logo_RegPiem.jpg", "Modulistica della Regione Piemonte", "http://https://www.regione.piemonte.it/web/"),
	/** 9 - TOFACILE-BO_TEST - moon-rupar.patrim.csi.it -  */
	TOFACILE_BO_TEST_9("TOFACILE-BO_TEST", 2L, "logotorinofacile.png", "Modulistica della Città di Torino", "http://www.torinofacile.it/"),
	/* 10 - COTO-EXTRACOM - moon-extracom.patrim.csi.it */
	COTO_EXTRACOM_10("COTO-EXTRACOM", 2L, "logotorinofacile.png", "Modulistica della Città di Torino", "http://www.torinofacile.it/"),
	
	//
	// NIVOLA
	//
	/** 100 - NIV_CITTA_FACILE - moon.csi.it -  */
	NIV_CITTA_FACILE_100("NIV_CITTA_FACILE", -1L, "", "", ""), // Portale Multi Ente, non c'è ente di default
	/** 101 - NIV_DEMO - demo-moon.csi.it -  */
	NIV_DEMO_101("NIV_DEMO", 101L, "", "", ""), // Comune fittizio di Bugliano
	/** 102 - NIV_BIELLA - biella-moon.csi.it -  */
	NIV_BIELLA_102("NIV_BIELLA", 102L, "", "", ""),
	//
	/** 110 - NIV_CITTA_FACILE - rupar-moon.csi.it -  */
	NIV_RU_CITTA_FACILE_110("NIV_RU_CITTA_FACILE", -1L, "", "", ""), // Portale Multi Ente, non c'è ente di default
	/** 111 - NIV_DEMO - rupar-demo-moon.csi.it -  */
	NIV_RU_DEMO_111("NIV_RU_DEMO", 101L, "", "", ""), // Comune fittizio di Bugliano
	/** 112 - NIV_BIELLA - rupar-biella-moon.csi.it -  */
	NIV_RU_BIELLA_112("NIV_RU_BIELLA", 102L, "", "", ""),
	;
	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private String codicePortale = null;
	private LogonMode logonMode = null;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	
	private DecodificaLogonModePortale(String codicePortale, Long idEnte, String logo, String titoloHeader, String urlLogout) {
		this.codicePortale = codicePortale;
		this.logonMode = new LogonMode(idEnte, logo, titoloHeader, urlLogout);
//		this.logonMode.setUrlLogout(EnvProperties.readFromFile(EnvProperties.LOGOUT_URL_PREFIX+logonMode.getNomePortale()));
	}
	
	
	@Override
	public String getCodice() {
		return String.valueOf(codicePortale);
	}

	public LogonMode getLogonMode() {
		return this.logonMode;
	}

	public static DecodificaLogonModePortale byId(String codicePortale) {
		for (DecodificaLogonModePortale e : DecodificaLogonModePortale.values()) {
			if (e.getCodice().equals(codicePortale)) {
				return e;
			}
		}
		return null;
	}
	public static DecodificaLogonModePortale byPortale(DecodificaPortale portale) {
		return DecodificaLogonModePortale.byId(portale.getCodice());
	}
	
}
