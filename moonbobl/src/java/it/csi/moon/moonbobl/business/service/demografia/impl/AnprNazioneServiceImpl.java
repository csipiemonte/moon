/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.demografia.impl;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.demografia.AnprNazioneService;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.AnprStatoEsteroDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.AnprStatoEsteroEntity;
import it.csi.moon.moonbobl.dto.extra.demografia.Nazione;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

/**
 * Layer di logica servizi che estrai gli StatiEsteri ANPR in oggetto Nazione a secondo dell'utilizzo
 * 
 * @author Laurent
 * 
 * @since 1.0.0
*/
@Component
public class AnprNazioneServiceImpl implements AnprNazioneService {
	
	private final static String CLASS_NAME = "AnprNazioneServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	@Qualifier("moon")
	AnprStatoEsteroDAO anprStatoEsteroDAO;
	
	public List<Nazione> listaNazioniResidenza() throws BusinessException {
		return listaNazioni(se -> "S".equalsIgnoreCase(se.getResidenza()));
	}
	public List<Nazione> listaNazioniCittadinanza() throws BusinessException {
		return listaNazioni(se -> "S".equalsIgnoreCase(se.getCittadinanza()));
	}
	public List<Nazione> listaNazioniNascita() throws BusinessException {
		return listaNazioni(se -> "S".equalsIgnoreCase(se.getNascita()));
	}
	//
	private List<Nazione> listaNazioni(Predicate<AnprStatoEsteroEntity> predicate) {
		return anprStatoEsteroDAO.findAll().stream().filter(predicate).map(se -> remapSe2Nazione(se)).collect(Collectors.toList());
	}

	
	@Override
	public Nazione getNazioneById(Integer codiceIstat) throws BusinessException {
		if (codiceIstat==null)
			return null;
		String strCodiceIstat = String.valueOf(codiceIstat);
		for(AnprStatoEsteroEntity se : anprStatoEsteroDAO.findAll()) {
			if (strCodiceIstat.equalsIgnoreCase(se.getCodIstat())) {
				return remapSe2Nazione(se);
			}
		}
		return null;
	}
	
	
	//
	// Mapper
	private Nazione remapSe2Nazione(AnprStatoEsteroEntity se) {
		return new Nazione(se.getCodIstat(), se.getDenominazione());
	}

}
