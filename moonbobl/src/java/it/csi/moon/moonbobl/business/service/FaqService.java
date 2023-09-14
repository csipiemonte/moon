/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.Faq;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alle FAQ
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface FaqService {
	List<Faq> getElencoFaq(Long idModulo) throws BusinessException;
	Faq getFaqById(Long idFaq) throws ItemNotFoundBusinessException, BusinessException;
	Faq createFaq(Faq body) throws BusinessException;
	Faq updateFaq(Long idFaq, Faq body) throws BusinessException;
}
