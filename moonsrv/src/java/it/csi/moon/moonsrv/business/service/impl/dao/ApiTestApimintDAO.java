/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;
import java.util.Map;

import it.csi.moon.commons.dto.api.FruitoreIstanza;
import it.csi.moon.commons.dto.api.FruitoreModuloVersione;
import it.csi.moon.commons.dto.api.StatoAcquisizioneRequest;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
* DAO Test MoonAPI via servizi REST via API Manager Outer
* 
* @author Francesco Zucaro
* @author Laurent Pissard
* 
* @since 1.0.0
*/
public interface ApiTestApimintDAO {

	public List<String> getIstanze(String codiceModulo, String stato, boolean test, String apimintUrl, 
			String clientProfile) throws DAOException;
	
	public FruitoreIstanza getIstanza(String codice, String apimintUrl, 
			String clientProfile) throws DAOException;

	public String updateStatoAcquisizione(String codiceIstanza, String codiceAzione, String apimintUrl, 
			StatoAcquisizioneRequest body,
			String clientProfile) throws DAOException;

	public List<FruitoreModuloVersione> getModuli(Map<String, String> queryParams, String apimintUrl, String clientProfile) throws DAOException;

	public FruitoreModuloVersione getModulo(String codiceModulo, String versioneModulo, String apimintUrl, String clientProfile) throws DAOException;
	
}
