/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.Portale;

/**
 * Metodi di business relativi alle portali
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface PortaliService {
	List<Portale> getElencoPortali();
	Portale getPortaleById(Long idPortale);
	Portale getPortaleByCd(String codicePortale);
	Portale getPortaleByNome(String nomePortale);
	Portale createPortale(Portale body);
	Portale updatePortale(Long idPortale, Portale body);
	
	//
	// Moduli
	//
	List<Modulo> getModuliByIdPortale(Long idPortale);
	
	//
	// Enti
	//
	List<Ente> getEntiByIdPortale(Long idPortale);
}
