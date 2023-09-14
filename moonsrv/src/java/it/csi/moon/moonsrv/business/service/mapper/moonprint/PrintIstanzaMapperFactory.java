/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.moonprint;

import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.business.service.mapper.moonprint.csi.PrintIstanzaMapper_AVVISO_CSI_2022;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.others.PrintIstanzaMapperDefault;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.Constants;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class PrintIstanzaMapperFactory {

	private static final String CLASS_NAME = "PrintIstanzaMapperFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	/**
	 * Torna il PrintIstanzaMapper secondo il modulo
	 * Permette di richiamare 
	 *  - remap()

	 * 
	 * @param codiceModulo
	 * @param versioneModulo
	 * @return un istanza di un mapper per la stampa PDF
	 * Torna new PrintIstanzaMapperDefault() se il modulo non prevede un mapping specifico
	 * oppure una BusinessException in caso di errore nel construtore del PrintIstanzaMapper
	 * Non torna null value.
	 */
	public PrintIstanzaMapper getPrintIstanzaMapper(String codiceModulo, String versioneModulo) throws BusinessException {
		PrintIstanzaMapper result = null;

		try {
			switch (codiceModulo.toUpperCase()) {
			case Constants.CODICE_MODULO_AVVISO_04_2023 :	
				result = new PrintIstanzaMapper_AVVISO_CSI_2022();
				break;
            default:
				result = new PrintIstanzaMapperDefault();
			}
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPrintIstanzaMapper] BusinessException for ["+codiceModulo+"]" + be.getMessage());
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getPrintIstanzaMapper] Errore PrintIstanzaMapperFactory ", e);
			throw new BusinessException();
		} finally {
			LOG.info("[" + CLASS_NAME + "::getPrintIstanzaMapper] IN:["+codiceModulo+"]  OUT: " + ((result!=null)?result.getClass().getName():"null"));
		}
	}
}
