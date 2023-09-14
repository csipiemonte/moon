/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.demografia.mapper;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.extra.demografia.Cittadinanza;
import it.csi.moon.commons.dto.extra.demografia.Nazione;
import it.csi.moon.commons.util.AnprConstants;
import it.csi.moon.moonsrv.business.service.demografia.AnprNazioneService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
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
public class AnprCittadinanzeMapper {
	
	private static final String CLASS_NAME = "AnprCittadinanzeMapper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private AnprNazioneService anprNazioneService;
	
	public AnprCittadinanzeMapper(AnprNazioneService anprNazioneService) {
		this.anprNazioneService = anprNazioneService;
	}

	/**
	 * Remap un oggetto ANPR via Outer servizi REST in oggetto MoonExtra
	 * Si prende la prima cittadinanza
	 * 
	 * @param list di Cittadinanza - attributo di un soggetto estratto da ANPR
	 * @return oggetto MoonExtra Cittadinanza compilato (o vuoto se errore)
	 * @throws BusinessException 
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
			if (citt!=null && citt.getCodiceStato()!=null) {
//				Nazione nazione = anprNazioneDAO.findByPK(Integer.valueOf(citt.getCodiceStato()));
				Nazione nazione = anprNazioneService.getNazioneById(Integer.valueOf(citt.getCodiceStato()));
				if (nazione!=null) {
					result = new Cittadinanza(nazione.getCodice(), nazione.getNome(), getFlagUE(nazione.getNome()));
				}
			}
		} catch (NumberFormatException e) {
			LOG.error("["+CLASS_NAME+":translateCittadinanzaANPR] ERRORE NumberFormatException for codiceStato="+citt.getCodiceStato()+" - " + e.getMessage());
		} catch (BusinessException e) {
			LOG.error("["+CLASS_NAME+":translateCittadinanzaANPR] ERRORE NotFound for codiceStato="+citt.getCodiceStato()+" - " + e.getMessage());
		}
		return result;
	}


	private String getFlagUE(String descrizioneStatoANPR) {
		return isDescStatoUE(descrizioneStatoANPR)?"S":"N";
	}

	private boolean isDescStatoUE(String descrizioneStatoANPR) {
		return Arrays.asList(AnprConstants.STATI_ANPR_UE).contains(descrizioneStatoANPR);
	}
	
}
