/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.notificatore;

import java.util.Optional;

import it.csi.moon.moonsrv.business.service.dto.IstanzaInitCompletedParams;
import it.csi.moon.moonsrv.business.service.dto.NotificatoreDatiInit;
import it.csi.moon.moonsrv.business.service.notificatore.impl.NotificatoreConfHelper;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Metodi di business del Notificatore
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 15/05/2020 - Versione initiale
 */
public interface NotificatoreService {
/*
	public String inviaMessaggio(Message message) throws BusinessException;
	public ContactPreference contacts(String identitaDigitale, String codiceFiscale) throws BusinessException;
	public List<Service> services(String identitaDigitale) throws BusinessException;
	public List<UserPreferencesService> servicesByUser(String cfTracciamento, String codiceFiscale) throws BusinessException;
	public UserPreferencesService serviceByUserServiceName(String cfTracciamento, String codiceFiscale, String serviveName) throws ItemNotFoundBusinessException, BusinessException;
	public Status status(String idMessaggio) throws BusinessException;
*/	
	public Optional<NotificatoreDatiInit> getDatiInit(IstanzaInitCompletedParams params) throws BusinessException;

	public void inviaRichiestaNotify(Long idIstanza);
	
	public NotificatoreConfHelper retrieveConfForDatiInit(IstanzaInitCompletedParams completedParams);
}
