/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore;

import java.util.List;

import it.csi.apirest.notify.preferences.v1.dto.ContactPreference;
import it.csi.apirest.notify.preferences.v1.dto.Service;
import it.csi.apirest.notify.preferences.v1.dto.UserPreferencesService;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
* DAO Notificatore via servizi REST 
* 
* @author Salvatore Cavalli
* @author Laurent Pissard
* 
* @since 1.0.0
*/
public interface NotificatorePrefDAO {
	
	public ContactPreference contatti(String endpoint, String token, String identitaDigitale,String codiceFiscale) throws ItemNotFoundDAOException, DAOException;
	public List<Service> services(String endpoint, String token, String cfTracciamento) throws DAOException;
	
	public List<UserPreferencesService> servicesByUser(String endpoint, String token,String cfTracciamento, String codiceFiscale) throws DAOException;
	public UserPreferencesService serviceByUserServiceName(String endpoint, String token, String cfTracciamento, String codiceFiscale, String serviveName) throws ItemNotFoundDAOException, DAOException;
	
}
