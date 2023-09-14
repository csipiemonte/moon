/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.protocollo;

import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.business.service.impl.protocollo.csi.StardasProtocollo_AVVISO_CSI_2022;
import it.csi.moon.moonsrv.util.Constants;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Factory di ProtocolloFactory gestita secondo codiceProtocollo (presi da attributi modullo).
 *
 * @author Laurent
 *
 * @since 1.0.0
 */

public class ProtocolloFactory {

	private static final String CLASS_NAME = "ProtocolloFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	/**
	 * Torna il Protocollo secondo il modulo
	 * Permette in seguito richiamare
	 *  - protocollaIstanza(Istanza istanza, ProtocolloParams params)
	 * Torna null se il modulo non prevede protocollazione
	 *
	 * @param codiceProtocollo  codice del sistema protocolatore es.: STARDAS
	 * @param codiceModulo
	 * @return
	 */
    public Protocollo getProtocollo(String codiceProtocollo, String codiceModulo) {
    	Protocollo result = null;
    	try {
    		switch (codiceProtocollo) {
    			case "STARDAS" :
    				result = getProtocolloStardas(codiceModulo);
    				break;
    			case "MAGGIOLI_SOAP" :
    				result = getMaggioliSoapProtocollo(codiceModulo);
    				break;
    			default :
    				LOG.error("[" + CLASS_NAME + "::getProtocollo] Nessun systema di Protocollo per "+codiceProtocollo);
    		}
    	} catch (Exception e) {
    		LOG.error("[" + CLASS_NAME + "::getProtocollo] Errore ProtocolloFactory", e);
		} finally {
			LOG.info("[" + CLASS_NAME + "::getProtocollo] OUT protocollo = " + result);
		}
    	return result;
    }

	private Protocollo getProtocolloStardas(String codiceModulo) {
    	Protocollo result = null;
    	try {
    		switch (codiceModulo) {
				case Constants.CODICE_MODULO_AVVISO_04_2023 :
					result = new StardasProtocollo_AVVISO_CSI_2022();
					break;
    			default :
    				result = new StardasProtocollo();
    				break;
    		}
    	} catch (Exception e) {
    		LOG.error("[" + CLASS_NAME + "::getProtocolloStardas] Errore ProtocolloFactory - ", e);
		} finally {
			LOG.debug("[" + CLASS_NAME + "::getProtocolloStardas] OUT protocollo = " + result);
		}
    	return result;
	}

	
	private Protocollo getMaggioliSoapProtocollo(String codiceModulo) {
    	Protocollo result = null;
    	try {
    		switch (codiceModulo) {
//				case Constants.CODICE_MODULO_TRIB_DICH_IMU :
//					result = new StardasProtocollo_TRIB_DICH_IMU();
//					break;
				default :
					result = new MaggioliSoapProtocollo();
					break;
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProtocolloStardas] Errore ProtocolloFactory - ", e);
		} finally {
			LOG.debug("[" + CLASS_NAME + "::getProtocolloStardas] OUT protocollo = " + result);
		}
		return result;
	}

}
