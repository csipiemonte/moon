/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.demografia.mapper;

import java.util.List;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.extra.demografia.Cittadinanza;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.CittadinanzeDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Contruttore di oggetto JSON MoonExtra Demografia Cittadinanza 
 *  da it.csi.apimint.demografia.v1.dto.Cittadinanza        ANPR via RS
 *  da List<it.csi.apimint.demografia.v1.dto.Cittadinanza>  ANPR via RS
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class NaoCittadinanzeMapper {
	
	private static final String CLASS_NAME = "CittadinanzeMapper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private CittadinanzeDAO cittadinanzeDAO;
	
	public NaoCittadinanzeMapper(CittadinanzeDAO cittadinanzeDAO) {
		this.cittadinanzeDAO = cittadinanzeDAO;
	}

	/**
	 * Remap un oggetto ANPR via Outer servizi REST in oggetto MoonExtra
	 * Si prende la prima cittadinanza
	 * 
	 * @param list di Cittadinanza - attributo di un soggetto estratto da ANPR
	 * @return oggetto MoonExtra Cittadinanza compilato (o vuoto se errore)
	 */
	public Cittadinanza remapFromCittadinanzaANPR(List<it.csi.apimint.demografia.v1.dto.Cittadinanza> list) {
		if (list==null || list.isEmpty())
			return null;
		return remapFromCittadinanzaANPR(list.get(0));
	}
	
	/**
	 * Remap un oggetto ANPR via Outer servizi REST in oggetto MoonExtra
	 * @param Cittadinanza - attributo di un soggetto estratto da ANPR
	 * @return oggetto MoonExtra Cittadinanza compilato (o vuoto se errore)
	 */
	public Cittadinanza remapFromCittadinanzaANPR(it.csi.apimint.demografia.v1.dto.Cittadinanza citt) {
		Cittadinanza result = null;
		try {
			if (citt==null || citt.getCodiceStato()==null) {
				LOG.debug("["+CLASS_NAME+":translateCittadinanzaANPR] IN citt OR citt.getCodiceStato() NULL.");
				return null;
			}
			result = cittadinanzeDAO.findByPK(Integer.parseInt(citt.getCodiceStato()));
			return result;
		} catch (NumberFormatException e) {
			LOG.error("["+CLASS_NAME+":translateCittadinanzaANPR] ERRORE NumberFormatException for codiceStato="+((citt==null)?"":citt.getCodiceStato())+" - " + e.getMessage());
			return null;
		} catch (DAOException e) {
			LOG.error("["+CLASS_NAME+":translateCittadinanzaANPR] ERRORE NotFound for codiceStato="+((citt==null)?"":citt.getCodiceStato())+" - " + e.getMessage());
			return null;
		}
	}

}
