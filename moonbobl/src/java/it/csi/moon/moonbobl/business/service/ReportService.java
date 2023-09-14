/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service;

import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

import java.util.ArrayList;
import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.RilevazioneServiziInfanzia;

/**
 * @author alberto
 * Metodi di business relativi ai report
 */
public interface ReportService {
	public Integer getNumeroModuliInviati(UserInfo user, Long idModulo) throws BusinessException;	
	public Integer getNumeroComuniCompilanti(UserInfo user, Long idModulo) throws BusinessException;
	public Integer getCount(UserInfo user, Long idModulo, String tipoValore) throws BusinessException;
	
	public List<RilevazioneServiziInfanzia> getRowsCSV(UserInfo user, Long idModulo) throws BusinessException;
	
	public List<String> getRowsCSV(IstanzeFilter filter, String codiceModulo) throws BusinessException, Exception;
	public List<Istanza> getElencoIstanze(IstanzeFilter filter) throws BusinessException, Exception;
}
