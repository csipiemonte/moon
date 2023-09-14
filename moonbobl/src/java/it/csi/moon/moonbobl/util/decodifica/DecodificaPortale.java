/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.business.service.impl.dto.PortaleEntity;
import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica dello PortaleIstanza.
 * 
 * @see PortaleEntity
 * Tabella moon_wf_d_portale
 * PK: idPortale  
 * DecodificaPortaleIstanza.getCodice() corrisponde a  PortaleEntity.getCodicePortale()
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/03/2020 - versione iniziale
 */
public enum DecodificaPortale implements DecodificaMOON {
	
	ALL_1(1L,"ALL", "*", "", 1L),
	
	//
	// PATRIM COTO-01
	//
	/** 2 - TOFACILE - moon-torinofacile.patrim.csi.it -  */
	TOFACILE_2(2L,"TOFACILE", "moon-torinofacile.patrim.csi.it", "", 2L),
	/** 3 - TOFACILE-BO - moon-bo.patrim.csi.it -  */
	TOFACILE_BO_3(3L,"TOFACILE-BO", "moon-bo.patrim.csi.it", "", 2L),
	/** 4 - CMTO - moon-cittametropolitanatorino.patrim.csi.it -  */
	CMTO_4(4L,"CMTO", "moon-cittametropolitanatorino.patrim.csi.it", "", 3L),
	/** 5 - CMTO-BO - moon-bo-cmto.patrim.csi.it -  */
	CMTO_BO_5(5L,"CMTO-BO", "moon-bo-cmto.patrim.csi.it", "", 3L),
	/** 6 - REGP - moon.patrim.csi.it -  */
	REGP_6(6L,"REGP", "moon.patrim.csi.it", "", 1L),
	/** 7 - REGP-BO - moon-ru.patrim.csi.it -  */
	REGP_BO_7(7L,"REGP-BO", "moon-ru.patrim.csi.it", "", 1L),
	/** 8 - REGP_TEST - moon-internet.patrim.csi.it -  */
	REGP_TEST_8(8L,"REGP_TEST", "moon-internet.patrim.csi.it", "", 1L),
	/** 9 - TOFACILE-BO_TEST - moon-rupar.patrim.csi.it -  */
	TOFACILE_BO_TEST_9(9L,"TOFACILE-BO_TEST", "moon-rupar.patrim.csi.it", "", 2L),
	/* 10 - COTO-EXTRACOM - moon-extracom.patrim.csi.it */
	COTO_EXTRACOM_10(10L,"COTO-EXTRACOM","moon-extracom.patrim.csi.it", "", 2L),
	
	//
	// NIVOLA DEV
	//
	/** 20 - TOFACILE - dev-me-moon.csi.it  */
	DEV_ME_20(20L,"DEV-ME", "dev-me-moon.csi.it", "", 2L),
	/** 20 - TOFACILE - dev-moon.csi.it  */
	DEV_21(21L,"DEV", "dev-moon.csi.it", "", -1L), // Portale Multi Ente, non c'è ente di default
	
	//
	// NIVOLA COBI-01
	//
	/** 100 - NIV_CITTA_FACILE - moon.csi.it -  */
	NIV_CITTA_FACILE_100(100L,"NIV_CITTA_FACILE", "moon.csi.it", "", -1L), // Portale Multi Ente, non c'è ente di default
	/** 101 - NIV_DEMO - demo-moon.csi.it -  */
	NIV_DEMO_101(101L,"NIV_DEMO", "demo-moon.csi.it", "", 101L), // Comune fittizio di Bugliano
	/** 102 - NIV_BIELLA - biella-moon.csi.it -  */
	NIV_BIELLA_102(102L,"NIV_BIELLA", "biella-moon.csi.it", "", 102L),
	
	// CITTA FACILE su COBI
	POC_CITTA_FACILE_200(200L,"POC_CITTA_FACILE", "poc-cittafacile.csi.it", "", -1L),
	PAVIA_POC_CITTA_FACILE_201(201L,"PAVIA_POC_CITTA_FACILE", "pavia-poc-cittafacile.csi.it", "", 201L),
	NOVARA_POC_CITTA_FACILE_202(202L,"NOVARA_POC_CITTA_FACILE", "novara-poc-cittafacile.csi.it", "", 202L),
	
	PAVIA_CITTA_FACILE_203(203L,"PAVIA_CITTA_FACILE", "servizi-pavia.portali.csi.it", "", 201L),
	NOVARA_CITTA_FACILE_204(204L,"NOVARA_CITTA_FACILE", "servizi-novara.portali.csi.it", "", 202L),
	PINEROLO_CITTA_FACILE_205(205L,"PINEROLO_CITTA_FACILE", "servizi-pinerolo.portali.csi.it", "", 203L),
	// CITTA FACILE su CITFAC
//	PAVIA_CITTA_FACILE_203(203L,"PAVIA_CITTA_FACILE", "servizi-pavia.portali.csi.it", "", 201L),
//	NOVARA_CITTA_FACILE_204(204L,"NOVARA_CITTA_FACILE", "servizi-novara.portali.csi.it", "", 202L),
	POC_CITTA_FACILE_210(210L,"SERVIZI_CITFAC", "servizi-citfac-moon.csi.it", "", -1L),
	
	
	//
	/** 110 - NIV_CITTA_FACILE - rupar-moon.csi.it -  */
	NIV_RU_CITTA_FACILE_110(110L,"NIV_RU_CITTA_FACILE", "rupar-moon.csi.it", "", -1L), // Portale Multi Ente, non c'è ente di default
	/** 111 - NIV_DEMO - rupar-demo-moon.csi.it -  */
	NIV_RU_DEMO_111(111L,"NIV_RU_DEMO", "rupar-demo-moon.csi.it", "", 101L), // Comune fittizio di Bugliano
	/** 112 - NIV_BIELLA - rupar-biella-moon.csi.it -  */
	NIV_RU_BIELLA_112(112L,"NIV_RU_BIELLA", "rupar-biella-moon.csi.it", "", 102L),

	
	//
	// NIVOLA APAF-01
	//
	/** 200 - NIV_APAF - apaf-moon.csi.it  */
	NIV_APAF_200(200L,"NIV_APAF", "apaf-moon.csi.it", "", 200L), // Portale APAF
	/** 210 - NIV_RU_APAF - rupar-apaf-moon.csi.it  */
	NIV_RU_APAF_210(210L,"NIV_RU_APAF", "rupar-apaf-moon.csi.it", "", 200L), // Portale APAF
	
	
	//
	// NIVOLA RP-01  DB-03
	//
	/** 1006 - NIV_REGP - regionepiemonte-moon.csi.it  */
	NIV_REGP_1006(1006L,"NIV_REGP", "regionepiemonte-moon.csi.it", "", 1L),
	/** 1008 - NIV_PITU - piemontetu-moon.csi.it  */
	NIV_PITU_1008(1008L,"NIV_PITU", "piemontetu-moon.csi.it", "", 1L),
	/** 1007 - NIV_RU_REGP - rupar-regionepiemonte-moon.csi.it  */
	NIV_RU_REGP_1007(1007L,"NIV_RU_REGP", "rupar-regionepiemonte-moon.csi.it", "", 1L),
	
	//
	// NIVOLA CMTO-01  DB-04
	//
	/** 1004 - NIV_CMTO - cittametropolitanatorino-moon.csi.it  */
	NIV_CMTO_1004(1004L,"NIV_CMTO", "cittametropolitanatorino-moon.csi.it", "", 3L),
	/** 1005 - NIV_RU_CMTO - rupar-cittametropolitanatorino-moon.csi.it  */
	NIV_RU_CMTO_1005(1005L,"NIV_RU_CMTO", "rupar-cittametropolitanatorino-moon.csi.it", "", 3L),
	 
	//
	// NIVOLA COTO-01  DB-05
	//
	/** 1002 - NIV_TOFACILE - torinofacile-moon.csi.it  */
	NIV_TOFACILE_1002(1002L,"NIV_TOFACILE", "torinofacile-moon.csi.it", "", 2L),
	/** 1010 - NIV_COTO_EXTRACOM - extracom-moon.csi.it  */
	NIV_COTO_EXTRACOM_1010(1010L,"NIV_COTO_EXTRACOM", "extracom-moon.csi.it", "", 2L),
	/** 1003 - NIV_RU_TOFACILE - rupar-torino-moon.csi.it  */
	NIV_RU_TOFACILE_1003(1003L,"NIV_RU_TOFACILE", "rupar-torino-moon.csi.it", "", 2L),
	;
	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private PortaleEntity portale = null;
	private Long idEnte = null;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaPortale(Long idPortale, String codicePortale, String nomePortale, String descPortale, Long idEnte) {
		this.portale = new PortaleEntity(idPortale, codicePortale, nomePortale, descPortale);
		this.idEnte = idEnte;
	}
	
	@Override
	public String getCodice() {
		return String.valueOf(portale.getCodicePortale());
	}
	
	public PortaleEntity getPortaleEntity() {
		return portale;
	}
	public Long getIdPortale() {
		return portale.getIdPortale();
	}
	public String getNomePortale() {
		return portale.getNomePortale();
	}
	public Long getIdEnte() {
		return idEnte;
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaPortale byCodice(String codice) {
		for(DecodificaPortale d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
	public static DecodificaPortale byIdPortale(Long idPortale) {
		for(DecodificaPortale d : values()) {
			if(d.getIdPortale().equals(idPortale)) {
				return d;
			}
		}
		return null;
	}	
// DA NIVOLA : NON SI PUO piu Fare !
//	public static DecodificaPortale byIdPortale(Long idPortale) {
//		for(DecodificaPortale d : values()) {
//			if(d.getIdPortale().equals(idPortale)) {
//				return d;
//			}
//		}
//		return null;
//	}	
	public static DecodificaPortale byNomePortale(String nomePortale) {
		for(DecodificaPortale d : values()) {
			if(d.getNomePortale().equals(nomePortale)) {
				return d;
			}
		}
		return null;
	}
	
	public boolean hasNomePortale(String nomePortale) {
		if (getNomePortale().equalsIgnoreCase(nomePortale)) {
			return true;
		}
		return false;
	}
}
