/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.LogEmail;

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
