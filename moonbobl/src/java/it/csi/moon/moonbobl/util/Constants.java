/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import it.csi.moon.moonbobl.business.service.impl.istanza.IstanzaDelegate_SP02_2021;

public class Constants {

	private static Logger log = LoggerAccessor.getLoggerApplication();

	public final static String COMPONENT_NAME = "moonbobl";

	public final static String INTEGRATION_LAYER = "integration";
	public final static String BUSINESS_LAYER = "business";
	public final static String SECURITY_LAYER = "security";


	/*
	 * Frase noreply per invio mail
	 */
	public final static String TESTO_NOREPLY = "Questa e-mail è stata generata automaticamente, si prega di non rispondere a questo messaggio.";

	/**
	 * SESSION ATTRIBUTES
	 */
	public static final String SESSION_MOON_ID_JWT = "Moon-Identita-JWT";
	public static final String SESSION_USERINFO = "appDatacurrentUser";
	public static final String SESSION_PORTALNAME = "appDataPortalName";

	/**
	 * Codici specifici di alcuni moduli
	 */
	public final static String CODICE_MODULO_BUONO_SPESA_COVID19 = "BUONO";
	public final static String APIMINT_CONSUMER_PREFIX_COVID = "covid.";
	public final static String CODICE_MODULO_EDIL_CONT_COSTR = "CONT_COSTR";
	public static final String CODICE_MODULO_EDIL_CONT_COMUNE = "CONT_COMUNE";
	public static final String CODICE_MODULO_EDIL_CONT_AGGIORNAMENTO_IBAN = "CONT_AGGIORNAMENTO_IBAN";
	public static final String CODICE_MODULO_EDIL_CONT_AGG_OPE = "CONT_AGG_OPE";
	public static final String CODICE_MODULO_COTO_DEM_001 = "DEM_001";
	public static final String CODICE_MODULO_DEM_CAMBIO_RES = "DEM_CAMBIO_RES";
	public static final String CODICE_MODULO_DEM_CINDCAF = "DEM_CINDCAF";
	public static final String CODICE_MODULO_DEM_CRESCAF = "DEM_CRESCAF";
	
	public final static String CODICE_MODULO_BUONO_SPESA_CODIV19 = "BUONO";
	public static final String CODICE_MODULO_ESTA_RAGA = "ESTA_RAGA";
	public static final String CODICE_MODULO_ESTA_RAGA2 = "ESTA_RAGA_2";
	public static final String CODICE_MODULO_EDIL_DEF_COND = "EDIL_DEF_COND";
	public static final String CODICE_MODULO_EDIL_INTEG_PRATICHE = "EDIL_INTEG_PRATICHE";
	public static final String CODICE_MODULO_CONC_ISTR_TECN = "CONC_ISTR_TECN";
	public static final String CODICE_MODULO_CONC_ISTR_AMM = "CONC_ISTR_AMM";
	public static final String CODICE_MODULO_TRIB_DEST_USO = "TRIB_DEST_USO";
	public static final String CODICE_MODULO_TRIB_DICH_IMU = "TRIB_DICH_IMU";
	public static final String CODICE_MODULO_RP_RSI_2 = "RP_RSI_2";
	public static final String CODICE_MODULO_ELET_SCRUTATORI = "ELET_SCRUTATORI";
	public static final String CODICE_MODULO_ELET_PRESID_SEGGIO = "ELET_PRESID_SEGGIO";
	public static final String CODICE_MODULO_ELET_RINUNCIA_SCRUTATORI = "ELET_RINUNCIA_SCRUTATORI";
	public static final String CODICE_MODULO_ELET_RINUNCIA_PRESIDENTI = "ELET_RINUNCIA_PRESIDENTI";
	public static final String CODICE_MODULO_ELET_DISP_SCRUTATORE = "ELET_DISP_SCRUTATORE";
	public static final String CODICE_MODULO_ELET_DISP_PRESIDENTE = "ELET_DISP_PRESIDENTE";
	public static final String CODICE_MODULO_ELET_RICH_ACCREDITO = "ELET_RICH_ACCREDITO";
	public static final String CODICE_MODULO_DSC_ELE001 = "DSC_ELE001";
	public static final String CODICE_MODULO_ELET_DISP_SCRUTATORE_CI = "ELET_DISP_SCRUTATORE_CI";
	public static final String CODICE_MODULO_ELET_DISP_PRESIDENTE_CI = "ELET_DISP_PRESIDENTE_CI";
	
	public static final String CODICE_MODULO_COTO_UT_TELERISC = "COTO_UT_TELERISC";
	public static final String CODICE_MODULO_COTO_UT_GAS = "COTO_UT_GAS";
	public static final String CODICE_MODULO_COTO_UT_ENELET = "COTO_UT_ENELET";
	public static final String CODICE_MODULO_COTO_UT_ACQUA = "COTO_UT_ACQUA";
	public static final String CODICE_MODULO_DEM_001 = "DEM_001";
	public static final String CODICE_MODULO_DISP_SCRUT_PRESID = "DISP_SCRUT_PRESID";
	public static final String CODICE_MODULO_RIL_TERMOSCANNER = "RIL_TERMOSCANNER";
	public static final String CODICE_MODULO_COMM_RINNOVO = "COMM_RINNOVO";
	public static final String CODICE_MODULO_AMB_AMIANTO = "AMB_AMIANTO";
	public static final String CODICE_MODULO_COV_MED = "COV_MED";
	public static final String CODICE_MODULO_COV_STAT = "COV_STAT";
	public static final String CODICE_MODULO_TRIB_MOD_VOUC = "TRIB_MOD_VOUC";
	public static final String CODICE_MODULO_TRIB_LAV_EDILI = "TRIB_LAV_EDILI";
	public static final String CODICE_MODULO_TRIB_PONTEGGI = "TRIB_PONTEGGI";
	public static final String CODICE_MODULO_TRIB_DICH_INAGI = "TRIB_DICH_INAGI";
	public static final String CODICE_MODULO_TRIB_TARI_COVID = "TRIB_TARI_COVID";
	public static final String CODICE_MODULO_RP_TCR_OSS_DIS = "RP_TASSE_OSS_DIS";
	public static final String CODICE_MODULO_RP_TASSE_DOM_RIM = "RP_TASSE_DOM_RIM";
	
	public static final String CODICE_MODULO_SP02_2021 = "CODICE_MODULO_SP02_2021";
	public static final String CODICE_MODULO_TAXI = "CODICE_MODULO_TAXI";
	public static final String CODICE_MODULO_TAXI_CI = "CODICE_MODULO_TAXI_CI";
	public static final String CODICE_MODULO_RPCCSR = "RPCCSR";
	public static final String CODICE_MODULO_APL_WORLDSKILLS = "APL_WORLDSKILLS";
	public static final String CODICE_MODULO_AFC_SP01 = "AFC_SP01";
	public static final String CODICE_MODULO_AFC_SP02 = "AFC_SP02";
	public static final String CODICE_MODULO_IREN_2023 = "IREN_2023";
	public static final String CODICE_MODULO_RP_FINE_CONC = "RP_FINE_CONC";
	public static final String CODICE_MODULO_AGL_2023 = "AGL_2023";  //modulo CSI
	public static final String CODICE_MODULO_CSI_PART_TIME = "CSI_PART_TIME";
	public static final String CODICE_MODULO_CSI_TELELAV = "CSI_TELELAV";
	public static final String CODICE_MODULO_TARI_NOAUT = "TARI_NOAUT";
	public static final String CODICE_MODULO_TARI_BACKOFFICE = "TARI_BACKOFFICE";
	public static final String CODICE_MODULO_TARI_UD_ATT = "TARI_UD_ATT";
	public static final String CODICE_MODULO_TARI_UD_CESS = "TARI_UD_CESS";
	public static final String CODICE_MODULO_TARI_UD_INFO = "TARI_UD_INFO";
	public static final String CODICE_MODULO_TARI_UD_RECL = "TARI_UD_RECL";
	public static final String CODICE_MODULO_TARI_UD_VAR = "TARI_UD_VAR";
	public static final String CODICE_MODULO_TARI_UND_ATT = "TARI_UND_ATT";
	public static final String CODICE_MODULO_TARI_UND_CESS = "TARI_UND_CESS";
	public static final String CODICE_MODULO_TARI_UND_INFO = "TARI_UND_INFO";
	public static final String CODICE_MODULO_TARI_UND_RECL = "TARI_UND_RECL";
	public static final String CODICE_MODULO_TARI_UND_VAR = "TARI_UND_VAR";
	

	//public final static String APIMINT_ENDPOINT = "notificatore.endpoint";
	public final static String APIMINT_ENDPOINT = "https://tst-notify.ecosis.csi.it";
	public final static String APIMINT_USERNAME = "notificatore.username";
	public final static String APIMINT_PASSWORD = "notificatore.password";
	public final static String APIMINT_TIMEOUT = "notificatore.timeout";
	
	public final static String FILTRO_RICERCA_DATI_COMUNE = "comune";
	public final static String FILTRO_RICERCA_DATI_ASR = "asr";
	public final static String FILTRO_RICERCA_DATI_CCR = "CCR";
	
	
	public final static String FILTRO_EPAY_PAID = "paid";
	public final static String FILTRO_EPAY_UNPAID = "unpaid";


	public static String getWsEndpointParams(String param) {
		Properties properties = new Properties();
		InputStream stream = Constants.class.getClassLoader()
			.getResourceAsStream("/wsEndpointParams.properties");
		String ret = "";
		try {
			properties.load(stream);
			ret = properties.getProperty(param);
			log.info("Constants::getWsEndpointParams param:" + param + " ret:" + ret );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
