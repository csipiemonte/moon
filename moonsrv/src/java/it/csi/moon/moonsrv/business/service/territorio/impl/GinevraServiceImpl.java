/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.GinevraDAO;
import it.csi.moon.moonsrv.business.service.territorio.GinevraService;
import it.csi.moon.moonsrv.business.service.territorio.mapper.regp.ViaGinevraMapper;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class GinevraServiceImpl implements GinevraService {

	private static final String CLASS_NAME = "GinevraServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	GinevraDAO ginevraDAO;
	
	//
	// SEDIMI
	@Override
	public List<Via> getSedimi() throws BusinessException {
		List<Via> result = new ArrayList<>();
		try {			
			result = ginevraDAO.getSedimi().stream()
						.map(ViaGinevraMapper::remapSedime)
						.collect(Collectors.toList());
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getSedimi] DAOException");
			throw new BusinessException("Errore generico servizio elenco getSedimi");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getSedimi] Errore generico servizio getSedimi", e);
			throw new BusinessException("Errore generico servizio elenco getSedimi");
		}
	}
	
	@Override
	public Via getSedimeById(Long idTipoVia) throws BusinessException {
		Via result = null;
		try {			
			result = ViaGinevraMapper.remapSedime(ginevraDAO.getSedimeById(idTipoVia));
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getSedimeById] DAOException");
			throw new BusinessException("Errore generico servizio elenco getSedimeById");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getSedimeById] Errore generico servizio getSedimeById", e);
			throw new BusinessException("Errore generico servizio elenco getSedimeById");
		}
	}

	
	//
	// VIE
	@Override
	public List<Via> getVie(String nome, Long idComune) throws BusinessException {
		List<Via> result = new ArrayList<>();
		try {			
			result = ginevraDAO.getViePerNomeContiene(nome, idComune).stream()
						.map(ViaGinevraMapper::remapViaComunale)
						.collect(Collectors.toList());
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getVie] DAOException");
			throw new BusinessException("Errore generico servizio elenco getVie");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getVie] Errore generico servizio getVie", e);
			throw new BusinessException("Errore generico servizio elenco getVie");
		}
	}
	
	@Override
	public Via getViaById(Long idComune, Long idVia) throws BusinessException {
		Via result = null;
		try {			
			result = ViaGinevraMapper.remapViaComunale(ginevraDAO.getViaByIdIndirizzoComunale(idVia));
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getViaById] DAOException");
			throw new BusinessException("Errore generico servizio elenco getViaById");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getViaById] Errore generico servizio getViaById", e);
			throw new BusinessException("Errore generico servizio elenco getViaById");
		}
	}
	
	
	//
	// CIVICI
	@Override
	public List<Via> getCivici(Long idVia, Long numero) throws BusinessException {
		List<Via> result = new ArrayList<>();
		try {			
			result = ginevraDAO.getCivici(idVia, numero).stream()
						.map(ViaGinevraMapper::remapCivico)
						.collect(Collectors.toList());
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getCivici] DAOException");
			throw new BusinessException("Errore generico servizio elenco getCivici");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCivici] Errore generico servizio getCivici", e);
			throw new BusinessException("Errore generico servizio elenco getCivici");
		}
	}
	@Override
	public Via getCivicoById(Long idCivico) throws BusinessException {
		Via result = null;
		try {			
			result = ViaGinevraMapper.remapCivico(ginevraDAO.getCivicoById(idCivico));
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] DAOException");
			throw new BusinessException("Errore generico servizio getCivicoById");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Errore generico servizio getCivicoById", e);
			throw new BusinessException("Errore generico servizio getCivicoById");
		}
	}
	
}
