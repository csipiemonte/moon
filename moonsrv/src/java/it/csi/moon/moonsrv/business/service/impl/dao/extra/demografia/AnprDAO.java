/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia;

import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.FamigliaANPR;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO ANPR via API Manager WebService
 * Implementazioni :
 *  - orchanprWS           :: via WebService Direct (no API Manager)
 *  - orchanprApiManagerWS :: via WebService via API Manager Entreprise
 * 
 * @deprecated (Use AnprApimintDAO)
 * 
 * @author Laurent Pissard
 * 
 * @since 1.0.0
 */
@Deprecated
public interface AnprDAO {

	public FamigliaANPR getFamigliaANPR(String codiceFiscale) throws DAOException;
	
}
