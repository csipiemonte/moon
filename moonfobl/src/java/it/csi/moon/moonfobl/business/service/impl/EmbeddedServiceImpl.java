/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.moonfobl.business.service.EmbeddedService;
import it.csi.moon.moonfobl.business.service.IstanzeService;
import it.csi.moon.moonfobl.business.service.ModuliService;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.dto.moonfobl.EmbeddedNavParams;
import it.csi.moon.moonfobl.dto.moonfobl.EmbeddedNavigator;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;
import it.csi.moon.moonfobl.util.decodifica.DecodificaEmbeddedService;

@Component
public class EmbeddedServiceImpl implements EmbeddedService {

	private static final String CLASS_NAME = "EmbeddedServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	IstanzeService istanzeService;
	
	@Autowired
	ModuliService moduliService;
	
	@Override
	public EmbeddedNavigator gotoViewIstanza(UserInfo user, String codiceIstanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::gotoViewIstanza] IN codiceIstanza: " + codiceIstanza);
			IstanzaEntity entity = istanzaDAO.findByCd(codiceIstanza);
			istanzeService.validateUserAccess(user, entity);
			
			EmbeddedNavigator result = new EmbeddedNavigator(DecodificaEmbeddedService.ISTANZA_VIEW);
			result.setOptions(null);
			result.setParams(new EmbeddedNavParams.Builder()
					.codiceIstanza(codiceIstanza)
					.idIstanza(entity.getIdIstanza())
					.idModulo(entity.getIdModulo())
					.idVersioneModulo(entity.getIdVersioneModulo())
					.build());
			
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::gotoViewIstanza] BusinessException ", be);
			throw be;
		}
	}

	@Override
	public EmbeddedNavigator gotoEditIstanza(UserInfo user, String codiceIstanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::gotoEditIstanza] IN codiceIstanza: " + codiceIstanza);
			IstanzaEntity entity = istanzaDAO.findByCd(codiceIstanza);
			istanzeService.validateUserAccess(user, entity);
			
			EmbeddedNavigator result = new EmbeddedNavigator(DecodificaEmbeddedService.ISTANZA_EDIT);
			result.setOptions(null);
			result.setParams(new EmbeddedNavParams.Builder()
					.codiceIstanza(codiceIstanza)
					.idIstanza(entity.getIdIstanza())
					.idModulo(entity.getIdModulo())
					.idVersioneModulo(entity.getIdVersioneModulo())
					.build());
			
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::gotoEditIstanza] BusinessException ", be);
			throw be;
		}
	}
	
	
	@Override
	public EmbeddedNavigator gotoNewIstanza(UserInfo user, String codiceModulo) {
		try {
			LOG.debug("[" + CLASS_NAME + "::gotoNewIstanza] IN codiceIstanza: " + codiceModulo);
						
			Modulo modulo = moduliService.getModuloPubblicatoByCodice(codiceModulo);
						
			EmbeddedNavigator result = new EmbeddedNavigator(DecodificaEmbeddedService.ISTANZA_NEW);
			result.setOptions(null);
			result.setParams(new EmbeddedNavParams.Builder()
					.idModulo(modulo.getIdModulo())
					.idVersioneModulo(modulo.getIdVersioneModulo())
					.codiceModulo(modulo.getCodiceModulo())
					.build());
			
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::gotoEditIstanza] BusinessException ", be);
			throw be;
		}
	}
	
	
	@Override
	public EmbeddedNavigator gotoIstanza(UserInfo user, String codiceIstanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::gotoIstanza] IN codiceIstanza: " + codiceIstanza);
			IstanzaEntity entity = istanzaDAO.findByCd(codiceIstanza);
			istanzeService.validateUserAccess(user, entity);
			
			EmbeddedNavigator result = new EmbeddedNavigator(DecodificaEmbeddedService.ISTANZA);
			result.setOptions(null);
			result.setParams(new EmbeddedNavParams.Builder()
					.codiceIstanza(codiceIstanza)
					.idIstanza(entity.getIdIstanza())
					.idModulo(entity.getIdModulo())
					.idVersioneModulo(entity.getIdVersioneModulo())
					.build());
			
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::gotoIstanza] BusinessException ", be);
			throw be;
		}
	}
	

}
