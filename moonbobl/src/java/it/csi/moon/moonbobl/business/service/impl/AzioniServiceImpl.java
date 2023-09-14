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

import it.csi.moon.moonbobl.business.service.AzioniService;
import it.csi.moon.moonbobl.business.service.impl.dao.AzioneDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AzioneEntity;
import it.csi.moon.moonbobl.business.service.mapper.AzioneMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Azione;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

/**
 * Metodi di business relativi agli stati
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class AzioniServiceImpl implements AzioniService {
	
	private static final String CLASS_NAME = "AzioniServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	AzioneDAO azioneDAO;
	
	@Override
	public List<Azione> getElencoAzioni() {
		try {
			List<Azione> result = azioneDAO.find().stream()
					.map(AzioneMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getElencoAzioni] DAOException ", daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getElencoAzioni] BusinessException");
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoAzioni] Errore generico servizio getElencoAzioni", ex);
			throw new BusinessException("Errore generico servizio elenco Azioni");
		} 
	}

	@Override
	public Azione getAzioneById(Long idAzione) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getAzioneById] BEGIN IN idAzione=" + idAzione);
			}
			AzioneEntity entity = azioneDAO.findById(idAzione);
			return AzioneMapper.buildFromEntity(entity);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getAzioneById] ItemNotFoundDAOException for idAzione=" + idAzione);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getAzioneById] DAOException for idAzione=" + idAzione, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAzioneById] BusinessException for idAzione=" + idAzione);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAzioneById] Errore generico servizio getAzioneById", ex);
			throw new BusinessException("Errore generico servizio getAzioneById");
		} 
	}

	@Override
	public Azione getAzioneByCd(String codiceAzione) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getAzioneByCd] BEGIN IN codiceAzione=" + codiceAzione);
			}
			AzioneEntity entity = azioneDAO.findByCd(codiceAzione);
			return AzioneMapper.buildFromEntity(entity);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getAzioneByCd] ItemNotFoundDAOException for codiceAzione=" + codiceAzione);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getAzioneByCd] DAOException for codiceAzione=" + codiceAzione, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAzioneByCd] BusinessException for codiceAzione=" + codiceAzione);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAzioneByCd] Errore generico servizio getAzioneByCd", ex);
			throw new BusinessException("Errore generico servizio getAzioneByCd");
		}
	}

	@Override
	public Azione createAzione(Azione azione) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::createAzione] BEGIN IN azione=" + azione);
			}
			AzioneEntity entity = AzioneMapper.buildFromObj(azione);
			Long idAzione = azioneDAO.insert(entity);
			azione.setIdAzione(idAzione);
			return azione;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::createAzione] ItemNotFoundDAOException for azione=" + azione);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::createAzione] DAOException for azione=" + azione, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createAzione] BusinessException for azione=" + azione);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createAzione] Errore generico servizio createAzione", ex);
			throw new BusinessException("Errore generico servizio createAzione");
		} 
	}

	@Override
	public Azione updateAzione(Long idAzione, Azione azione) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateAzione] BEGIN IN azione=" + azione);
			}
			AzioneEntity entity = AzioneMapper.buildFromObj(azione);
			int rows = azioneDAO.update(entity);
			if (rows==0) {
				LOG.error("[" + CLASS_NAME + "::updateAzione] ItemNotFoundDAOException for azione=" + azione);
				throw new ItemNotFoundBusinessException();
			}
			return azione;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::updateAzione] DAOException for azione=" + azione, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::updateAzione] BusinessException for azione=" + azione);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::updateAzione] Errore generico servizio updateAzione", ex);
			throw new BusinessException("Errore generico servizio updateAzione");
		} 
	}

	@Override
	public void deleteAzioneById(Long idAzione) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::deleteAzioneById] BEGIN IN idAzione=" + idAzione);
			}
			azioneDAO.delete(idAzione);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::deleteAzioneById] ItemNotFoundDAOException for idAzione=" + idAzione);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::deleteAzioneById] DAOException for idAzione=" + idAzione, daoe);
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::deleteAzioneById] BusinessException for idAzione=" + idAzione);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::deleteAzioneById] Errore generico servizio deleteAzioneById", ex);
			throw new BusinessException("Errore generico servizio deleteAzioneById");
		} 
	}

}