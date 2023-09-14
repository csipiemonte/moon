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

import it.csi.moon.moonbobl.business.service.TipiCodiceIstanzaService;
import it.csi.moon.moonbobl.business.service.impl.dao.TipoCodiceIstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.TipoCodiceIstanzaEntity;
import it.csi.moon.moonbobl.business.service.mapper.TipoCodiceIstanzaMapper;
import it.csi.moon.moonbobl.dto.moonfobl.TipoCodiceIstanza;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Metodi di business relativi alle decodifiche della codifica delle istanze di un modulo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class TipiCodiceIstanzaServiceImpl  implements TipiCodiceIstanzaService {
	
	private static final String CLASS_NAME = "TipiCodiceIstanzaServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	public TipiCodiceIstanzaServiceImpl() {
	}

	@Autowired
	TipoCodiceIstanzaDAO tipoCodiceIstanzaDAO;
	
	@Override
	public List<TipoCodiceIstanza> getElencoTipoCodiceIstanza() throws BusinessException {
		try {
			List<TipoCodiceIstanza> result = tipoCodiceIstanzaDAO.find().stream()
					.map(TipoCodiceIstanzaMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoTipoCodiceIstanza] Errore generico servizio getElencoTipoCodiceIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco TipoCodiceIstanza");
		} 
	}

	@Override
	public TipoCodiceIstanza getTipoCodiceIstanzaById(Integer idTipoCodiceIstanza) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getTipoCodiceIstanzaById] BEGIN IN idTipoCodiceIstanza="+idTipoCodiceIstanza);
		}
		TipoCodiceIstanza result = null;
		try {
			TipoCodiceIstanzaEntity entity = tipoCodiceIstanzaDAO.findById(idTipoCodiceIstanza);
			result = TipoCodiceIstanzaMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaById] Errore invocazione DAO", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaById] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca getTipoCodiceIstanzaById");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getTipoCodiceIstanzaById] END result="+result);
			}
		}
	}

	@Override
	public TipoCodiceIstanza getTipoCodiceIstanzaByCodice(String descCodice) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getTipoCodiceIstanzaByCodice] BEGIN IN descCodice="+descCodice);
		}
		TipoCodiceIstanza result = null;
		try {
			TipoCodiceIstanzaEntity entity = tipoCodiceIstanzaDAO.findByCodice(descCodice);
			result = TipoCodiceIstanzaMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaByCodice] Errore invocazione DAO", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaByCodice] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca getTipoCodiceIstanzaByCodice");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getTipoCodiceIstanzaByCodice] END result="+result);
			}
		}
	}


}