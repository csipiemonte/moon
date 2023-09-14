/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.demografia.mapper;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.extra.demografia.LuogoMOON;
import it.csi.moon.commons.dto.extra.demografia.Nazione;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.NazioneDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Contruttore di oggetto JSON MoonExtra Demografia LuogoMoon 
 *  da it.csi.apimint.demografia.v1.dto.LuogoEvento        ANPR via RS
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class LuogoMoonMapper {
	
	private static final String CLASS_NAME = "LuogoMoonMapper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private NazioneDAO nazioneDAO;
	
	public LuogoMoonMapper(NazioneDAO nazioneDAO) {
		this.nazioneDAO = nazioneDAO;
	}

	/**
	 * Remap un oggetto ANPR via Outer servizi REST in oggetto MoonExtra
	 * @param Cittadinanza - attributo di un soggetto estratto da ANPR
	 * @return oggetto MoonExtra Cittadinanza compilato (o vuoto se errore)
	 */
	public LuogoMOON remapFromLuogoEventoANPR(it.csi.apimint.demografia.v1.dto.LuogoEvento luogoEvento) {
		if(luogoEvento==null)
			return null;
		
		LuogoMOON result = new LuogoMOON();
		
		if(luogoEvento.getComune()!=null) {
//			Provincia provincia = provinciaDAO.findBySiglia(luogoANPR.getComune().getSiglaProvinciaIstat());
//			Comune comune = comuneDAO.findByNomeCodiceProvincia(luogoANPR.getComune().getNomeComune(), provincia.getCodice());
//			Regione regione = regioneDAO.findByPK(provinciaDAO.getCodiceRegione(provincia));

//			luogo.setComune(comune);
//			luogo.setProvincia(provincia);
//			luogo.setRegione(regione);
			result.setNazione(Nazione.ITALIA);
			result.setDescrizioneLuogo(luogoEvento.getComune().getNomeComune() + "(" + luogoEvento.getComune().getSiglaProvinciaIstat() + ")");
		} else if(luogoEvento.getLocalita()!=null) {
//			luogo.setComuneEstero(luogoANPR.getLocalita().getDescrizioneLocalita());
			result.setDescrizioneLuogo(luogoEvento.getLocalita().getDescrizioneLocalita());
			Nazione nazione=null;
			try {
				nazione = nazioneDAO.findByNome(luogoEvento.getLocalita().getDescrizioneStato());
			} catch (DAOException e) {
				LOG.warn("[" + CLASS_NAME + "::remapFromLuogoEventoANPR] Exception "+e.getMessage());
				nazione = new Nazione(Integer.valueOf(luogoEvento.getLocalita().getCodiceStato()), luogoEvento.getLocalita().getDescrizioneStato());
			}
			result.setNazione(nazione); //.setNazione(nazione!=null?nazione:new Nazione(null,luogoEvento.getLocalita().getDescrizioneStato()));
		} else if(luogoEvento.getLuogoEccezionale()!=null) {
//			luogo.setComuneEstero(luogoANPR.getLuogoEccezionale());
			result.setDescrizioneLuogo(luogoEvento.getLuogoEccezionale());
		}
		return result;
	}

}
