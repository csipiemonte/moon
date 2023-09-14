/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer;


import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

/**
 * Interface DatiIstanzaInitializer
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface DatiIstanzaInitializer {

	public String getDati(AzioneInitParams initParams) throws BusinessException;
	
}
