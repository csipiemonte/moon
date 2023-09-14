/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.FaqService;
import it.csi.moon.moonbobl.business.service.impl.dao.FaqDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.FaqEntity;
import it.csi.moon.moonbobl.business.service.mapper.FaqMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Faq;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Metodi di business relativi alle FAQ
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class FaqServiceImpl  implements FaqService {
	
	private final static String CLASS_NAME = "FaqServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
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
			log.error("[" + CLASS_NAME + "::getElencoFaq] Errore generico servizio getElencoFaq",ex);
			throw new ServiceException("Errore generico servizio elenco FAQ");
		} 
	}

	@Override
	public Faq getFaqById(Long idFaq) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getFaqById] BEGIN IN idFaq="+idFaq);
		}
		Faq result = null;
		try {
			FaqEntity entity = faqDAO.findById(idFaq);
			result = FaqMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getFaqById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getFaqById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca FAQ per id");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getFaqById] END result="+result);
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