/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Faq;
import it.csi.moon.commons.entity.FaqEntity;
import it.csi.moon.commons.mapper.FaqMapper;
import it.csi.moon.moonfobl.business.service.FaqService;
import it.csi.moon.moonfobl.business.service.impl.dao.FaqDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.util.LoggerAccessor;
/**
 * Metodi di business relativi alle FAQ
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class FaqServiceImpl  implements FaqService {
	
	private static final String CLASS_NAME = "FaqServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	public FaqServiceImpl() {
	}

	@Autowired
	FaqDAO faqDAO;
	
	@Override
	public List<Faq> getElencoFaq(Long idModulo) throws BusinessException {
		try {
			List<Faq> result = faqDAO.findByIdModulo(idModulo).stream()
					.map(FaqMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoFaq] Errore generico servizio getElencoFaq",ex);
			throw new ServiceException("Errore generico servizio elenco FAQ");
		} 
	}

	@Override
	public Faq getFaqById(Long idFaq) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getFaqById] BEGIN IN idFaq="+idFaq);
		}
		Faq result = null;
		try {
			FaqEntity entity = faqDAO.findById(idFaq);
			result = FaqMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getFaqById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getFaqById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca FAQ per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getFaqById] END result="+result);
			}
		}
	}

	@Override
	public Faq createFaq(Faq body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Faq updateFaq(Long idFaq, Faq body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}


}