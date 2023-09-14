/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import it.csi.moon.commons.entity.NotificaEntity;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

/*
 * Service per la gestione ddelle notifiche
 *
 */
public interface NotificaService {
	public Long insertNotifica(NotificaEntity notifica) throws BusinessException;
} 
