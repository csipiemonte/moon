/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.LogEmail;

/**
 * Metodi di business relativi ai log email
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface LogEmailService {
	List<LogEmail> getElencoLogEmailByIdIstanza(Long idIstanza);
}
