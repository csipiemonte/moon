/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util;

public class Constants {
	
	public static final String COMPONENT_NAME = "moonsrv";
	public static final String INTEGRATION_LAYER = "integration";
	public static final String BUSINESS_LAYER = "business";
	public static final String SECURITY_LAYER = "security";
	
//	public static final String APIMINT_CONSUMER_PREFIX_COVID = "covid.";
	
	/*
	 * Frase noreply per invio mail
	 */
	public static final String TESTO_NOREPLY = "Questa e-mail è stata generata automaticamente, si prega di non rispondere a questo messaggio.";

	/**
	 * Codici specifici di alcuni moduli
	 */
	public static final String CODICE_MODULO_BUONO_SPESA_CODIV19 = "BUONO";
	public static final String CODICE_MODULO_ESTA_RAGA = "ESTA_RAGA";
	public static final String CODICE_MODULO_ESTA_RAGA2 = "ESTA_RAGA_2";
	public static final String CODICE_MODULO_ESTA_NIDI_23 = "ESTA_NIDI_23";
	public static final String CODICE_MODULO_EDIL_DEF_COND = "EDIL_DEF_COND";
	public static final String CODICE_MODULO_EDIL_INTEG_PRATICHE = "EDIL_INTEG_PRATICHE";
	public static final String CODICE_MODULO_EDIL_CONT_COSTR = "CONT_COSTR";
	public static final String CODICE_MODULO_CONC_ISTR_TECN = "CONC_ISTR_TECN";
	public static final String CODICE_MODULO_CONC_ISTR_AMM = "CONC_ISTR_AMM";
	public static final String CODICE_MODULO_TRIB_DEST_USO = "TRIB_DEST_USO";
	public static final String CODICE_MODULO_TRIB_DICH_IMU = "TRIB_DICH_IMU";
	public static final String CODICE_MODULO_TRIB_DICH_IMU_2022 = "TRIB_DICH_IMU_2022"; // RITIRATO
	public static final String CODICE_MODULO_RP_RSI_2 = "RP_RSI_2";
	public static final String CODICE_MODULO_EDIL_CONT_COMUNE = "CONT_COMUNE";
	public static final String CODICE_MODULO_EDIL_CONT_AGGIORNAMENTO_IBAN = "CONT_AGGIORNAMENTO_IBAN";
	public static final String CODICE_MODULO_EDIL_CONT_AGG_OPE = "CONT_AGG_OPE";
	public static final String CODICE_MODULO_ELET_SCRUTATORI = "ELET_SCRUTATORI";
	public static final String CODICE_MODULO_ELET_PRESID_SEGGIO = "ELET_PRESID_SEGGIO";
	public static final String CODICE_MODULO_ELET_RINUNCIA_SCRUTATORI = "ELET_RINUNCIA_SCRUTATORI";
	public static final String CODICE_MODULO_ELET_RINUNCIA_PRESIDENTI = "ELET_RINUNCIA_PRESIDENTI";
	public static final String CODICE_MODULO_ELET_DISP_SCRUTATORE = "ELET_DISP_SCRUTATORE";
	public static final String CODICE_MODULO_ELET_DISP_SCRUTATORE_CI = "ELET_DISP_SCRUTATORE_CI";
	public static final String CODICE_MODULO_ELET_DISP_PRESIDENTE = "ELET_DISP_PRESIDENTE";
	public static final String CODICE_MODULO_ELET_DISP_PRESIDENTE_CI = "ELET_DISP_PRESIDENTE_CI";
	public static final String CODICE_MODULO_ELET_RICH_ACCREDITO = "ELET_RICH_ACCREDITO";
	public static final String CODICE_MODULO_COTO_UT_TELERISC = "COTO_UT_TELERISC";
	public static final String CODICE_MODULO_COTO_UT_GAS = "COTO_UT_GAS";
	public static final String CODICE_MODULO_COTO_UT_ENELET = "COTO_UT_ENELET";
	public static final String CODICE_MODULO_COTO_UT_ACQUA = "COTO_UT_ACQUA";
	public static final String CODICE_MODULO_DEM_CAMBIO_RES = "DEM_CAMBIO_RES";
	public static final String CODICE_MODULO_DEM_CAMBIO_RES_CAF = "CAMBIO_RES_CAF";
	public static final String CODICE_MODULO_DEM_001 = "DEM_001";
	public static final String CODICE_MODULO_DISP_SCRUT_PRESID = "DISP_SCRUT_PRESID";
	public static final String CODICE_MODULO_RIL_TERMOSCANNER = "RIL_TERMOSCANNER";
	public static final String CODICE_MODULO_TRIB_IST_RATE = "TRIB_IST_RATE";
	public static final String CODICE_MODULO_TRIB_DICH_INAGI = "TRIB_DICH_INAGI";
	public static final String CODICE_MODULO_TRIB_IST_AUTO = "TRIB_IST_AUTO";
	public static final String CODICE_MODULO_TRIB_IST_DOPPIO = "TRIB_IST_DOPPIO";
	public static final String CODICE_MODULO_TRIB_RICH_RIMB = "TRIB_RICH_RIMB";
	public static final String CODICE_MODULO_TRIB_RICH_RIV = "TRIB_RICH_RIV";
	public static final String CODICE_MODULO_RIMB_MISURA = "RIMB_MISURA";
	public static final String CODICE_MODULO_EDIL_CIL_EL = "CIL-EL";
	public static final String CODICE_MODULO_CMTO_STRAT = "CMTO_STRAT";
	public static final String CODICE_MODULO_COMM_RINNOVO = "COMM_RINNOVO";
	public static final String CODICE_MODULO_CMTO_PTGM = "PTGM";
	public static final String CODICE_MODULO_CMTO_RA = "RA";
	public static final String CODICE_MODULO_COV_MED = "COV_MED";
	public static final String CODICE_MODULO_COV_ERO = "COV_ERO";
	public static final String CODICE_MODULO_AMB_AMIANTO = "AMB_AMIANTO";
	public static final String CODICE_MODULO_TO_SOLIDALE = "TO_SOLIDALE";
	public static final String CODICE_MODULO_CMTO_PRO = "CMTO_PRO";
	public static final String CODICE_MODULO_PUBB_PRIMO = "PUBB_PRIMO";
	public static final String CODICE_MODULO_VIAB_CMTO = "VIAB_CMTO";
	public static final String CODICE_MODULO_TRIB_MOD_VOUC = "TRIB_MOD_VOUC";
	public static final String CODICE_MODULO_TRIB_LAV_EDILI = "TRIB_LAV_EDILI";
	public static final String CODICE_MODULO_TRIB_PONTEGGI = "TRIB_PONTEGGI";
	public static final String CODICE_MODULO_CMTO_INTERPELLO = "CMTO_INTERPELLO";
	public static final String CODICE_MODULO_MOB_EST_AGENTE = "MOB_EST_AGENTE";
	public static final String CODICE_MODULO_MOB_EST_ARCH = "MOB_EST_ARCH";
	public static final String CODICE_MODULO_MOB_EST_DIR_ECO = "MOB_EST_DIR_ECO";
	public static final String CODICE_MODULO_MOB_EST_DIR_AMB = "MOB_EST_DIR_AMB";
	public static final String CODICE_MODULO_MOB_EST_DIR_VIG = "MOB_EST_DIR_VIG";
	public static final String CODICE_MODULO_MOB_EST_TEC = "MOB_EST_TEC";
	public static final String CODICE_MODULO_MOB_EST_TEC_AMB = "MOB_EST_TEC_AMB";
	public static final String CODICE_MODULO_MOBY = "MOBY";
	public static final String CODICE_MODULO_MOBY_21 = "MOBY_21";
	public static final String CODICE_MODULO_RP_FINE_CONC = "RP_FINE_CONC";
	public static final String CODICE_MODULO_RP_TASSE_SE = "RP_TASSE_SE";
	public static final String CODICE_MODULO_RP_TASSE_RI = "RP_TASSE_RI";
	public static final String CODICE_MODULO_TRIB_TARI_COVID = "TRIB_TARI_COVID";
	public static final String VOUCHER = "VOUCHER";
	public static final String RISORSE_UMANE = "RISORSE_UMANE";
	public static final String COORD_VIABILITA = "COORD_VIABILITA";
	public static final String INTERPELLO_POLMETRO = "INTERPELLO_POLMETRO";
	public static final String CODICE_MODULO_SPO_IMP_OCC = "SPO_IMP_OCC";
	public static final String CODICE_MODULO_SPO_IMP_STA = "SPO_IMP_STA";
	public static final String CODICE_MODULO_ESTA_RAGA2021 = "ESTA_RAGA_21";
	public static final String CODICE_MODULO_MOB_EST_COM ="MOB_EST_COM";
	public static final String CODICE_MODULO_MOB_EST_DIR_TEC="MOB_EST_DIR_TEC";
	public static final String CODICE_MODULO_RP_RILINT ="RP_RILINT";
	public static final String CODICE_MODULO_RP_TASSE_OSS_DIS ="RP_TASSE_OSS_DIS";
	public static final String CODICE_MODULO_FL0121="FL0121";
	public static final String CODICE_MODULO_CMTO_CFL_IST_AM = "CMTO_CFL_IST_AM";
	public static final String CODICE_MODULO_RACC_CAN = "RACC_CAN";
	public static final String CODICE_MODULO_RP_TASSE_DOM_RIM = "RP_TASSE_DOM_RIM";
	public static final String CODICE_MODULO_CONC_RESP_VIAB="CONC_RESP_VIAB";
	public static final String CODICE_MODULO_RPCCSR="RPCCSR";
	public static final String CODICE_MODULO_EVENTI_OSP="EVENTI_OSP";
	public static final String CODICE_MODULO_EVENTI_SEGNALAZIONE="EVENTI_SEGNALAZIONE";
	public static final String CODICE_MODULO_RIC_PATR="RIC_PATR";
	public static final String CODICE_MODULO_AMB_H2="AMB_H2";
	public static final String CODICE_MODULO_ESTA_RAGA2022 = "ESTA_RAGA_22";
	public static final String CODICE_MODULO_CORSI_MOB_MAN="CORSI_MOB_MAN";
	public static final String CODICE_MODULO_BANDO_RIV_URB="BANDO_RIV_URB";
	public static final String CODICE_MODULO_AVVISO_1_2022="AVVISO_1_2022";
	public static final String CODICE_MODULO_PNRR_TASK_FORCE="PNRR_TASK_FORCE";
	public static final String CODICE_MODULO_AVVISO_M01_2022="AVVISO_M01_2022";
	public static final String CODICE_MODULO_BORSA_STUDIO_BPCRT_2022 = "BORSA_STUDIO_BPCRT_2022";
	public static final String CODICE_MODULO_AVVISO_02_2022="AVVISO_02_2022";
	public static final String CODICE_MODULO_AVVISO_03_2022="AVVISO_03_2022";
	public static final String CODICE_MODULO_AVVISO_04_2022="AVVISO_04_2022";
	public static final String CODICE_MODULO_AVVISO_UT01_2022="AVVISO_UT01_2022";
	public static final String CODICE_MODULO_AVVISO_M02_2022="AVVISO_M02_2022";
	public static final String CODICE_MODULO_PS_1_2022_IST_AMM="PS_1_2022_IST_AMM";
	public static final String CODICE_MODULO_PS_2_2022_IST_TEC="PS_2_2022_IST_TEC";
	public static final String CODICE_MODULO_TST_IRENA02 = "TST_IRENA02";
	public static final String CODICE_MODULO_PM_ACCESSO_ATTI = "PM_ACCESSO_ATTI";
	public static final String CODICE_MODULO_MOB_EST_DIR_AMM = "MOB_EST_DIR_AMM";
	public static final String CODICE_MODULO_SELEZIONE_1_2022 = "SELEZIONE_1_2022";
	public static final String CODICE_MODULO_BONUS_IREN = "BONUS_IREN";
	public static final String CODICE_MODULO_IREN_2023 = "IREN_2023";
	public static final String CODICE_MODULO_PO_CANDIDATURA = "PO_CANDIDATURA";
	public static final String CODICE_MODULO_PO_PROGETTAZIONE ="PO_PROGETTAZIONE";
	public static final String CODICE_MODULO_PVB_PERMESSO_H ="PVB_PERMESSO_H";
	public static final String CODICE_MODULO_PVB_VERDE ="PVB_VERDE";
	public static final String CODICE_MODULO_PVB_BLU_A ="PVB_BLU_A";
	public static final String CODICE_MODULO_PVB_BLU_B ="PVB_BLU_B";
	public static final String CODICE_MODULO_PVB_CANTIERE ="PVB_CANTIERE";
	public static final String CODICE_MODULO_PVB_DORA ="PVB_DORA";
	public static final String CODICE_MODULO_PVB_VEP_ANNUALE ="PVB_VEP_ANNUALE";
	public static final String CODICE_MODULO_PVB_VEP_TEMPORANEO ="PVB_VEP_TEMPORANEO";
	public static final String CODICE_MODULO_AVVISO_06_2022 ="AVVISO_06_2022";
	public static final String CODICE_MODULO_AVVISO_05_2022 ="AVVISO_05_2022";
	public static final String CODICE_MODULO_TARI_GEST_PRIV_001 ="TARI_GEST_PRIV_001";
	public static final String CODICE_MODULO_QUOTE_LATTE_BOVINO ="RPA_LATTE_BOVINO";
	public static final String CODICE_MODULO_QUOTE_LATTE_OVI_CAPRINO ="RPA_LATTE_OVI-CAPRINO";
	public static final String CODICE_MODULO_PLC_FABBRICANTI ="PLC_FABBRICANTI";
    public static final String CODICE_MODULO_MOB_EST_ISTR_RAG="MOB_EST_ISTR_RAG";
	public static final String CODICE_MODULO_MOB_EST_GEOM ="MOB_EST_GEOM";
	public static final String CODICE_MODULO_AVVISO_07_2022 ="AVVISO_07_2022";
	public static final String CODICE_MODULO_AZINT01 = "AZINT01";
	public static final String CODICE_MODULO_AZINT02 = "AZINT02";
	public static final String CODICE_MODULO_AZINT03 = "AZINT03";
	public static final String CODICE_MODULO_APL_WORLDSKILLS = "APL_WORLDSKILLS";
	public static final String CODICE_MODULO_CRP_PATR_GARANTI = "CRP_PATR_GARANTI";
	public static final String CODICE_MODULO_CRP_PATR_GR = "CRP_PATR_GR";
	public static final String CODICE_MODULO_ISTR01 = "ISTR01";
	public static final String CODICE_MODULO_ISTR02 = "ISTR02";
	public static final String CODICE_MODULO_REG_ASSO = "REG_ASSO";
	public static final String CODICE_MODULO_AVVISO_08_2022="AVVISO_08_2022";
	public static final String CODICE_MODULO_AVVISO_09_2022="AVVISO_09_2022";
	public static final String CODICE_MODULO_AVVISO_10_2022="AVVISO_10_2022";
	public static final String CODICE_MODULO_AVVISO_11_2022="AVVISO_11_2022";
	public static final String CODICE_MODULO_AVVISO_12_2022="AVVISO_12_2022";
	public static final String CODICE_MODULO_ELET_DISP_SCRUT_IPC = "ELET_DISP_SCRUT_IPC";
	public static final String CODICE_MODULO_EDIL_DEF_COND_PPAY = "EDIL_DEF_COND_PPAY";
	public static final String CODICE_MODULO_DEM_CINDCAF = "DEM_CINDCAF";
	public static final String CODICE_MODULO_DEM_CRESCAF = "DEM_CRESCAF";
	public static final String CODICE_MODULO_AMB002 = "AMB002";
	public static final String CODICE_MODULO_AUT_STAZ_MONTA_PRIV = "AUT_STAZ_MONTA_PRIV";
	public static final String CODICE_MODULO_AUT_STAZ_MONTA_PUBBL = "AUT_STAZ_MONTA_PUBBL";
	public static final String CODICE_MODULO_AUT_STAZ_INSEM_ART = "AUT_STAZ_INSEM_ART";
	public static final String CODICE_MODULO_AUT_CENTRO_PROD_MATERIALE = "AUT_CENTRO_PROD_MATERIALE";
	public static final String CODICE_MODULO_AUT_CENTRO_PROD_EMBRIONI = "AUT_CENTRO_PROD_EMBRIONI";
	public static final String CODICE_MODULO_AUT_GRUPPO_RACCOLTA_EMBRIONI = "AUT_GRUPPO_RACCOLTA_EMBRIONI";
	public static final String CODICE_MODULO_REC_MAT_EMBRIONI = "REC_MAT_EMBRIONI";
	public static final String CODICE_MODULO_OPERATORI = "OPERATORI";
	public static final String CODICE_MODULO_AUT_RAZZE_AUTOCTONE = "AUT_RAZZE_AUTOCTONE";
	public static final String CODICE_MODULO_AUT_RIPRODUTTORI_INTERESSE_LOC = "AUT_RIPRODUTTORI_INTERESSE_LOC";
	public static final String CODICE_MODULO_AUT_COMUNICAZIONI_FA_SUINI = "AUT_COMUNICAZIONI_FA_SUINI";
	public static final String CODICE_MODULO_CMTO12 = "CMTO12";
	public static final String CODICE_MODULO_CMTO14 = "CMTO14";
	public static final String CODICE_MODULO_EDURB_SEGN = "EDURB_SEGN";
	public static final String CODICE_MODULO_RIFIUTI_RAE = "RIFIUTI_RAE";
	public static final String CODICE_MODULO_CRP_PATR_ON = "CRP_PATR_ON";
	public static final String CODICE_MODULO_EVENTI_SOMM = "EVENTI_SOMM";
	public static final String CODICE_MODULO_CSI_CONF_INC = "CSI_CONF_INC";
	public static final String CODICE_MODULO_EDURB_CONTR_EDIL = "EDURB_CONTR_EDIL";
	public static final String CODICE_MODULO_ESTA_RAGA23 = "ESTA_RAGA_23";
	public static final String CODICE_MODULO_INTERVISTE_RIFIUTI = "INTERVISTE_RIFIUTI";
	public static final String CODICE_MODULO_EDURB_DEP_FRAZ = "EDURB_DEP_FRAZ";
	public static final String CODICE_MODULO_GARANTE_ANIMALI = "GARANTE_ANIMALI";
	public static final String CODICE_MODULO_GARANTE_DETENUTI = "GARANTE_DETENUTI";
	public static final String CODICE_MODULO_GARANTE_INFANZIA = "GARANTE_INFANZIA";
	public static final String CODICE_MODULO_GARANTE_INFANZIA_MIN = "GARANTE_INFANZIA_MIN";
	public static final String CODICE_MODULO_VINCA = "VINCA_SCR";
	public static final String CODICE_MODULO_PARCHI = "AUT_PARCHI";
	public static final String CODICE_MODULO_EDURB_ISTVIG = "EDURB_ISTVIG";
	public static final String CODICE_MODULO_EDURB_SVI_ENERG = "EDURB_SVI_ENERG";
	public static final String CODICE_MODULO_EDURB_DEF_AGIBIL = "EDURB_DEF_AGIBIL";
	public static final String CODICE_MODULO_ESTA_NIDI = "ESTA_NIDI";
	public static final String CODICE_MODULO_ESTA_BIMBI = "ESTA_BIMBI";
	public static final String CODICE_MODULO_EDURB_DICH_AGGIORN = "EDURB_DICH_AGGIORN";
	public static final String CODICE_MODULO_EDURB_ESP_AVVER = "EDURB_ESP_AVVER";
	public static final String CODICE_MODULO_EDURB_PROROGA_TERMINI = "EDURB_PROROGA_TERMINI";
	public static final String CODICE_MODULO_EDURB_ACC_ATT_ISTRUTT = "EDURB_ACC_ATT_ISTRUTT";
	public static final String CODICE_MODULO_EDURB_TRASF_VINC = "EDURB_TRASF_VINC";
	public static final String CODICE_MODULO_EDIL_COMESECLAV = "EDIL_COMESECLAV";
	public static final String CODICE_MODULO_EDIL_INTEGPRATICHE = "EDIL_INTEGPRATICHE";
	public static final String CODICE_MODULO_EDIL_ISTESECLAV = "EDIL_ISTESECLAV";
	public static final String CODICE_MODULO_CSI_PART_TIME = "CSI_PART_TIME";
	public static final String CODICE_MODULO_CSI_TELELAV = "CSI_TELELAV";
	public static final String CODICE_MODULO_EDURB_INTAGIB = "EDURB_INTAGIB";
	public static final String CODICE_MODULO_EDURB_PREN_CERT_ATT = "EDURB_PREN_CERT_ATT";
	public static final String CODICE_MODULO_EDURB_PREN_CERT_ATT_INT = "EDURB_PREN_CERT_ATT_INT";
	public static final String CODICE_MODULO_EDURB_RETT_PROVV = "EDURB_RETT_PROVV";
	public static final String CODICE_MODULO_EDURB_RIMB_COND = "EDURB_RIMB_COND";
	public static final String CODICE_MODULO_EDURB_RIMB_PDC = "EDURB_RIMB_PDC";
	public static final String CODICE_MODULO_EDURB_SEGN_DISMISS = "EDURB_SEGN_DISMISS";
	public static final String CODICE_MODULO_EDURB_SEGN_SUBENTR = "EDURB_SEGN_SUBENTR";
	public static final String CODICE_MODULO_EDURB_SVI_FIDEJ = "EDURB_SVI_FIDEJ";
	public static final String CODICE_MODULO_TARI_UND_ATT = "TARI_UND_ATT";
	public static final String CODICE_MODULO_TARI_UND_CESS = "TARI_UND_CESS";
	public static final String CODICE_MODULO_TARI_UND_VAR = "TARI_UND_VAR";
	public static final String CODICE_MODULO_TARI_UD_ATT= "TARI_UD_ATT";
	public static final String CODICE_MODULO_TARI_UD_CESS = "TARI_UD_CESS";
	public static final String CODICE_MODULO_TARI_UD_VAR = "TARI_UD_VAR";
	public static final String CODICE_MODULO_TARI_UD_RECL = "TARI_UD_RECL";
	public static final String CODICE_MODULO_TARI_UND_RECL = "TARI_UND_RECL";
	public static final String CODICE_MODULO_TARI_UD_INFO = "TARI_UD_INFO";
	public static final String CODICE_MODULO_TARI_UND_INFO = "TARI_UND_INFO";
	public static final String CODICE_MODULO_TARI_NOAUT = "TARI_NOAUT";
	public static final String CODICE_MODULO_TARI_BACKOFFICE = "TARI_BACKOFFICE";
	public static final String CODICE_MODULO_EDURB_SVI_OPURB = "EDURB_SVI_OPURB";
	public static final String CODICE_MODULO_EDURB_SVI_VINC = "EDURB_SVI_VINC";
	public static final String CODICE_MODULO_AVVISO_01_2023 = "AVVISO_01_2023";
	public static final String CODICE_MODULO_AVVISO_02_2023 = "AVVISO_02_2023";
	public static final String CODICE_MODULO_AVVISO_03_2023 = "AVVISO_03_2023";
	public static final String CODICE_MODULO_AVVISO_04_2023 = "AVVISO_04_2023";
	public static final String CODICE_MODULO_PSICOLOGI0 = "PSICOLOGI0";
	public static final String CODICE_MODULO_PSICOLOGI1 = "PSICOLOGI1";
	public static final String CODICE_MODULO_CMTO15_2023_01 = "CMTO15_2023_01";
	public static final String CODICE_MODULO_1_2023 = "1_2023";
	public static final String CODICE_MODULO_ED_SCOL_POP = "ED_SCOL_POP";
	public static final String CODICE_MODULO_ED_SCOL_PROV = "ED_SCOL_PROV";
	public static final String CODICE_MODULO_ED_IMP = "ED_IMP";
	public static final String CODICE_MODULO_ED_PREVINC = "ED_PREVINC";
	public static final String CODICE_MODULO_ED_AGGPLAN = "ED_AGGPLAN";
	public static final String CODICE_MODULO_ED_FASFAB = "ED_FASFAB";
	public static final String CODICE_MODULO_ED_SCOL_PROV_21 = "ED_SCOL_PROV_21";
	public static final String CODICE_MODULO_STAGE_RP = "STAGE_RP";
	public static final String CODICE_MODULO_MOD_DEMO = "MOD_DEMO";
	public static final String CODICE_MODULO_CSI_ODV_1_2023_AP = "CSI_ODV_1_2023_AP";
	public static final String CODICE_MODULO_CSI_ODV_1_2023_DC = "CSI_ODV_1_2023_DC";
	public static final String CODICE_MODULO_332013EMOL = "332013EMOL";
	public static final String CODICE_MODULO_332013CAIN = "332013CAIN";
	public static final String CODICE_MODULO_392013INCOMP = "392013INCOMP";
	public static final String CODICE_MODULO_392013INCONF = "392013INCONF";
	public static final String CODICE_MODULO_CIRC_OCC_PERM = "CIRC_OCC_PERM";
	public static final String CODICE_MODULO_CIRC_CONC_LOC = "CIRC_CONC_LOC";
	public static final String CODICE_MODULO_CIRC_MAN_VIA = "CIRC_MAN_VIA";
	public static final String CODICE_MODULO_CIRC_CONTRIB = "CIRC_CONTRIB";
	public static final String CODICE_MODULO_CIRC_RENDICONTO = "CIRC_RENDICONTO";
	public static final String CODICE_MODULO_CIRC_RENDIC_FESTEDIVIA = "CIRC_RENDIC_FESTEDIVIA";
	public static final String CODICE_MODULO_CIRC_PAL_SCOL = "CIRC_PAL_SCOL";
	public static final String CODICE_MODULO_BORSA_STUDIO_CMTO = "BORSA_STUDIO_CMTO";
	public static final String CODICE_MODULO_MONIT_COMU = "MONIT_COMU";
	
	
}

