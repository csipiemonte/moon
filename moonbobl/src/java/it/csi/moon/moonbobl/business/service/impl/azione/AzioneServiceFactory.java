/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione;

import org.apache.log4j.Logger;

import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_AFC;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_CAMBIO_RES;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_COMM_RINNOVO;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_DEM_001;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_DSC_ELE001;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_ELET_DISPONIBILITA;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_ELET_DISPONIBILITA_PRESIDENTE;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_ELET_PRESID_SEGGIO;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_ELET_RINUNCIA_SCRUTATORI;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_ELET_SCRUTATORI;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_TRIB_COSAP;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_TRIB_INAGI;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_TRIB_TARI;
import it.csi.moon.moonbobl.business.service.impl.azione.coto.Azione_TRIB_TARI_ARERA;
import it.csi.moon.moonbobl.business.service.impl.azione.regp.Azione_AMB_AMIANTO;
import it.csi.moon.moonbobl.business.service.impl.azione.regp.Azione_COV_MED;
import it.csi.moon.moonbobl.business.service.impl.azione.regp.Azione_RP_FINE_CONC;
import it.csi.moon.moonbobl.business.service.impl.azione.regp.Azione_TCR;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;

public class AzioneServiceFactory {

	private final static String CLASS_NAME = "AzioneServiceFactory";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	public AzioneService getService(String codiceModulo) throws BusinessException {
		log.debug("[" + CLASS_NAME + "::getService] IN codiceModulo=" + codiceModulo );
		switch(codiceModulo) {
			case Constants.CODICE_MODULO_COTO_DEM_001:
			case Constants.CODICE_MODULO_DEM_CINDCAF:
				return new Azione_DEM_001();
			case Constants.CODICE_MODULO_DEM_CAMBIO_RES:
			case Constants.CODICE_MODULO_DEM_CRESCAF:			
				return new Azione_CAMBIO_RES();
			case Constants.CODICE_MODULO_COMM_RINNOVO:
				return new Azione_COMM_RINNOVO();
			case Constants.CODICE_MODULO_AMB_AMIANTO:
				return new Azione_AMB_AMIANTO();
			case Constants.CODICE_MODULO_COV_MED:
			case Constants.CODICE_MODULO_COV_STAT:
				return new Azione_COV_MED();
			case Constants.CODICE_MODULO_TRIB_MOD_VOUC:
			case Constants.CODICE_MODULO_TRIB_LAV_EDILI:
			case Constants.CODICE_MODULO_TRIB_PONTEGGI:
				return new Azione_TRIB_COSAP();
			case Constants.CODICE_MODULO_TRIB_DICH_INAGI:
				return new Azione_TRIB_INAGI();
			case Constants.CODICE_MODULO_TRIB_TARI_COVID:     				
				return new Azione_TRIB_TARI();				
			case Constants.CODICE_MODULO_ELET_SCRUTATORI:
				return new Azione_ELET_SCRUTATORI();
			case Constants.CODICE_MODULO_ELET_PRESID_SEGGIO:
				return new Azione_ELET_PRESID_SEGGIO();
			case Constants.CODICE_MODULO_ELET_RINUNCIA_SCRUTATORI:
				return new Azione_ELET_RINUNCIA_SCRUTATORI();
			case Constants.CODICE_MODULO_DSC_ELE001:
				return new Azione_DSC_ELE001();	
			case Constants.CODICE_MODULO_ELET_DISP_SCRUTATORE:
			case Constants.CODICE_MODULO_ELET_DISP_SCRUTATORE_CI:
				return new Azione_ELET_DISPONIBILITA();
			case Constants.CODICE_MODULO_ELET_DISP_PRESIDENTE:
			case Constants.CODICE_MODULO_ELET_DISP_PRESIDENTE_CI:
				return new Azione_ELET_DISPONIBILITA_PRESIDENTE();
			case Constants.CODICE_MODULO_RP_TCR_OSS_DIS:
			case Constants.CODICE_MODULO_RP_TASSE_DOM_RIM:
				return new Azione_TCR();
			case Constants.CODICE_MODULO_AFC_SP01:
			case Constants.CODICE_MODULO_AFC_SP02:	
				return new Azione_AFC();
			case Constants.CODICE_MODULO_RP_FINE_CONC:	
				return new Azione_RP_FINE_CONC();
			case Constants.CODICE_MODULO_TARI_NOAUT:     
			case Constants.CODICE_MODULO_TARI_BACKOFFICE:
			case Constants.CODICE_MODULO_TARI_UD_ATT:
			case Constants.CODICE_MODULO_TARI_UD_CESS:
			case Constants.CODICE_MODULO_TARI_UD_INFO:
			case Constants.CODICE_MODULO_TARI_UD_RECL:
			case Constants.CODICE_MODULO_TARI_UD_VAR:
			case Constants.CODICE_MODULO_TARI_UND_ATT:
			case Constants.CODICE_MODULO_TARI_UND_CESS:
			case Constants.CODICE_MODULO_TARI_UND_INFO:
			case Constants.CODICE_MODULO_TARI_UND_RECL:
			case Constants.CODICE_MODULO_TARI_UND_VAR:
				return new Azione_TRIB_TARI_ARERA();
			default:
				return new Azione_Default();
		}
	}

}
