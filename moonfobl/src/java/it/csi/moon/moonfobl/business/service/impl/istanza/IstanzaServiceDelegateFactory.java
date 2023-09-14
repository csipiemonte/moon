/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.istanza;

import org.apache.log4j.Logger;

import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.Constants;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class IstanzaServiceDelegateFactory {

	private static final String CLASS_NAME = "IstanzaServiceDelegateFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	public IstanzaServiceDelegate getDelegate(String codiceModulo) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getDelegate] IN codiceModulo=" + codiceModulo );
		switch(codiceModulo) {
			case Constants.CODICE_MODULO_BUONO_SPESA_COVID19:
				return new IstanzaDelegate_BUONO_SPESA();
			case Constants.CODICE_MODULO_EDIL_CONT_COSTR:
				return new IstanzaDelegate_CONT_COSTR();
			case Constants.CODICE_MODULO_EDIL_CONT_COMUNE:
				return new IstanzaDelegate_CONT_COMUNE();
			case Constants.CODICE_MODULO_EDIL_CONT_AGGIORNAMENTO_IBAN:
				return new IstanzaDelegate_CONT_AGGIORNAMENTO_IBAN();
			case Constants.CODICE_MODULO_EDIL_CONT_AGG_OPE:
				return new IstanzaDelegate_CONT_AGG_OPE();
			case Constants.CODICE_MODULO_DEM_001:
				return new IstanzaDelegate_DEM_001();
			case Constants.CODICE_MODULO_COMM_RINNOVO:
				return new IstanzaDelegate_COMM_RINNOVO();
			case Constants.CODICE_MODULO_SP02_2021:
				return new IstanzaDelegate_SP02_2021();	
			case Constants.CODICE_MODULO_TAXI:
			case Constants.CODICE_MODULO_TAXI_CI:
				return new IstanzaDelegate_TAXI();	
			case Constants.CODICE_MODULO_DEM_CINDCAF:
			case Constants.CODICE_MODULO_DEM_CRESCAF:
				return new IstanzaDelegate_DEM_CAF();	
			default:
				return new IstanzaDefaultDelegate();
		}
	}

}
