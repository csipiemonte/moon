/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.initializer;



import it.csi.moon.moonfobl.business.service.impl.dto.IntegrazioneInitParams;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

/**
 * Interface DatiIintegrazioneInitializer
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public interface DatiIntegrazioneInitializer {
	public String getDati(IntegrazioneInitParams initParams) throws BusinessException;
	
}
