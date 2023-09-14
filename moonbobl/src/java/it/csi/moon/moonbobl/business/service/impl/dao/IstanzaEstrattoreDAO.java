/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.function.Consumer;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaEstratta;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 * 
 * @see IstanzaEntity
 * @see IstanzaCronologiaStatiEntity
 * @see IstanzaDatiEntity
 * 
 * @author Laurent
 *
 */
public interface IstanzaEstrattoreDAO {

	public int estraiIstanze(IstanzeFilter filter, Consumer<IstanzaEstratta> consumer);
	
}
