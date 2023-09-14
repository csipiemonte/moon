/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia;

import java.util.List;

import it.csi.apimint.demografia.v1.dto.Soggetto;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
* DAO ANPR via servizi REST via API Manager Outer
* 
* @author Francesco Zucaro
* @author Laurent Pissard
* 
* @since 1.0.0
*/
public interface AnprApimintDAO {

//	public void setClientProfileKey(String clientProfileKey);
	public List<Soggetto> getFamigliaANPR(String codiceFiscale, String userJwt) throws DAOException; // CITTADINO
	public List<Soggetto> getFamigliaANPR(String codiceFiscale, String userJwt, String clientProfile) throws DAOException;
	
}
