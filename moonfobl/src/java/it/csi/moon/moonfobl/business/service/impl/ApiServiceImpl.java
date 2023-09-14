/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.ResponsePaginated;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.mapper.IstanzaMapperApi;
import it.csi.moon.commons.mapper.ModuloMapperApi;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.moonfobl.business.service.ApiService;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.IstanzeService;
import it.csi.moon.moonfobl.business.service.helper.JwtIdentitaUtil;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * Metodi di business API per fruitori
 * 
 * @author Danilo Mosca
 *
 * @since 1.0.0
 */
@Component
public class ApiServiceImpl implements ApiService {

	private static final String CLASS_NAME = "ApiServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	JwtIdentitaUtil jwtIdentitaUtil;
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	StatoDAO statoDAO;	
	@Autowired
	AuditService auditService;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	@Override
	public List<Istanza> getIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter, UserInfo user) throws BusinessException {
		long start = System.currentTimeMillis();
		MapModuloAttributi attributi = null;
		List<Istanza> elencoIstanze = new ArrayList<>();
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanze] BEGIN");
			List<IstanzaEntity> elenco = istanzaDAO.find(filter, null, sorter);
			if (elenco != null && elenco.size() > 0) {
				statoDAO.initCache();
				moduloDAO.initCache();
//				moduloAttributiDAO.initCache();
				for (IstanzaEntity entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),entity.getIdVersioneModulo());
//					List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO
//							.findByIdModulo(entity.getIdModulo());
//					attributi = new MapModuloAttributi(attributiModuloE);
					
					elencoIstanze.add(IstanzaMapperApi.buildFromIstanzaEntity(entity, statoE, moduloE/*, attributi*/));
//					elencoIstanze.add(isApi(user)?
//							IstanzaMapperApi.buildFromIstanzaEntity(entity, statoE, moduloE, attributi):
//							IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, attributi));
				}
			}
			LOG.debug("[" + CLASS_NAME + "::getIstanze] user = " + user);
			LOG.debug("[" + CLASS_NAME + "::getIstanze] isApi ? " + String.valueOf(isApi(user)));
			return elencoIstanze;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] DAOException ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start);
			LOG.debug("[" + CLASS_NAME + "::getIstanze] END in " + sec + " milliseconds.");
		}
	}

	private boolean isApi(UserInfo user) {
		return user.getIdFruitore()!=null && user.isApi();
	}
	
	@Override
	public ResponsePaginated<Istanza> getIstanzePaginate(IstanzeFilter filter, Optional<IstanzeSorter> sorter, UserInfo user) {
		long start = System.currentTimeMillis();
		List<Istanza> elencoIstanze;
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] BEGIN");
			Integer totalElements = istanzaDAO.count(filter, null);
			elencoIstanze = getIstanze(filter, sorter, user);
			ResponsePaginated<Istanza> result = new ResponsePaginated<>();
			result.setItems(elencoIstanze);
			result.setPage((int) Math.floor(filter.getOffset().doubleValue() / filter.getLimit().doubleValue())); // 0 based
			result.setPageSize(filter.getLimit());
			result.setTotalElements(totalElements);
			result.setTotalPages(
					((Double) Math.ceil(totalElements.doubleValue() / filter.getLimit().doubleValue())).intValue());
			return result;
		} catch (BusinessException be) {
			throw be;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] DAOException ", e);
			throw new BusinessException("Errore recupero elenco istanza paginate");
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start);
			LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] END in " + sec + " milliseconds.");
		}
	}
	
	@Override
	public Istanza getIstanza(String codiceIstanza, UserInfo user) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN codiceIstanza: " + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN user: " + user);
			}
			
			Istanza result = istanzeService.getIstanzaByCd(user, codiceIstanza);
			
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getIstanza] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getIstanza] errore generico DAO ");
			throw new BusinessException();
		}
	}

	@Override
	public byte[] getIstanzaPdf(String codiceIstanza, UserInfo user, String ipAddress) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN codiceIstanza=" + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN user=" + user);
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN ipAddress=" + ipAddress);
			}

//			Integer idFruitore = jwtIdentitaUtil.getIdFruitoreFromToken(userProfile);
			IstanzaEntity istanza = istanzaDAO.findByCd(codiceIstanza);			
//			validaEnteFruitore(istanza.getIdEnte(), idFruitore);
			
			try {
				auditService.getPdf(ipAddress, user, istanza.getIdIstanza());
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getPdfById] errore servizio Audit", e);
			}
			
			byte[] result = istanzeService.getPdfIstanza(user, istanza.getIdIstanza());
			LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] bytes.length=" + result.length);
			
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaPdf] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getIstanzaPdf] errore generico DAO ");
			throw new BusinessException();
		}
	}

	@Override
	public List<Modulo> getElencoModuli(ModuliFilter filter) throws BusinessException {
		try {
			return moonsrvDAO.getModuli(filter).stream()
					.map(ModuloMapperApi::purgeForApi)
					.collect(Collectors.toList());
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoModuli] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco moduli");
 		}
	}
	
}
