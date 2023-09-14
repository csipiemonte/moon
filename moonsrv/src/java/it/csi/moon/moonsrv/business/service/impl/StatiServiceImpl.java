/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Stato;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.mapper.StatoMapper;
import it.csi.moon.moonsrv.business.service.StatiService;
import it.csi.moon.moonsrv.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
/**
 * Metodi di business relativi agli stati
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class StatiServiceImpl implements StatiService {
	
	private static final String CLASS_NAME = "StatiServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	StatoDAO statoDAO;
	
	@Override
	public List<Stato> getElencoStati() {
		try {
			List<Stato> result = statoDAO.find().stream()
					.map(StatoMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getElencoStati] DAOException ", daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getElencoStati] BusinessException");
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoStati] Errore generico servizio getElencoStati", ex);
			throw new BusinessException("Errore generico servizio elenco Stati");
		} 
	}

	@Override
	public Stato getStatoById(Integer idStato) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getStatoById] BEGIN IN idStato=" + idStato);
			}
			StatoEntity entity = statoDAO.findById(idStato);
			return StatoMapper.buildFromEntity(entity);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] ItemNotFoundDAOException for idStato=" + idStato);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] DAOException for idStato=" + idStato, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] BusinessException for idStato=" + idStato);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] Errore generico servizio getStatoById", ex);
			throw new BusinessException("Errore generico servizio getStatoById");
		} 
	}

	@Override
	public Stato getStatoByCd(String codiceStato) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getStatoByCd] BEGIN IN codiceStato=" + codiceStato);
			}
			StatoEntity entity = statoDAO.findByCd(codiceStato);
			return StatoMapper.buildFromEntity(entity);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] ItemNotFoundDAOException for codiceStato=" + codiceStato);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] DAOException for codiceStato=" + codiceStato, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] BusinessException for codiceStato=" + codiceStato);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] Errore generico servizio getStatoByCd", ex);
			throw new BusinessException("Errore generico servizio getStatoByCd");
		}
	}

	@Override
	public Stato createStato(Stato stato) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::createStato] BEGIN IN stato=" + stato);
			}
			StatoEntity entity = StatoMapper.buildFromObj(stato);
			Integer idStato = statoDAO.insert(entity);
			stato.setIdStato(idStato);
			return stato;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::createStato] ItemNotFoundDAOException for stato=" + stato);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::createStato] DAOException for stato=" + stato, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createStato] BusinessException for stato=" + stato);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createStato] Errore generico servizio createStato", ex);
			throw new BusinessException("Errore generico servizio createStato");
		} 
	}

	@Override
	public Stato updateStato(Integer idStato, Stato stato) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateStato] BEGIN IN stato=" + stato);
			}
			StatoEntity entity = StatoMapper.buildFromObj(stato);
			int rows = statoDAO.update(entity);
			if (rows==0) {
				LOG.error("[" + CLASS_NAME + "::updateStato] ItemNotFoundDAOException for stato=" + stato);
				throw new ItemNotFoundBusinessException();
			}
			return stato;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::updateStato] DAOException for stato=" + stato, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::updateStato] BusinessException for stato=" + stato);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::updateStato] Errore generico servizio updateStato", ex);
			throw new BusinessException("Errore generico servizio updateStato");
		} 
	}

	@Override
	public void deleteStatoById(Integer idStato) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::deleteStatoById] BEGIN IN idStato=" + idStato);
			}
			statoDAO.delete(idStato);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] ItemNotFoundDAOException for idStato=" + idStato);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] DAOException for idStato=" + idStato, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] BusinessException for idStato=" + idStato);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] Errore generico servizio deleteStatoById", ex);
			throw new BusinessException("Errore generico servizio deleteStatoById");
		} 
	}

}