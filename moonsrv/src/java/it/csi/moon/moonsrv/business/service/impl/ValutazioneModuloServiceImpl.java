/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.ValutazioneModuloSintesi;
import it.csi.moon.commons.entity.ValutazioneModuloEntity;
import it.csi.moon.moonsrv.business.service.ValutazioneModuloService;
import it.csi.moon.moonsrv.business.service.impl.dao.ValutazioneModuloDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ValutazioneModuloServiceImpl implements ValutazioneModuloService {

	private static final String CLASS_NAME = "ValutazioneModuloServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ValutazioneModuloDAO valutazioneModuloDAO;
	
	@Override
	public void insertValutazioneModulo(ValutazioneModuloEntity entity) throws BusinessException {
		try {
			entity.setDataIns(new Date());
			valutazioneModuloDAO.insert(entity);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::insertValutazioneModulo] Errore generico servizio insertValutazioneModulo",ex);
			throw new ServiceException("Errore generico servizio insert ValutazioneModulo");
		}
	}

	@Override
	public List<ValutazioneModuloSintesi> getValutazioneModuloSintesi(Long idModulo) throws BusinessException {
		try {
			List<ValutazioneModuloSintesi> result = valutazioneModuloDAO.findSintesiByIdModulo(idModulo);
			LOG.debug("[" + CLASS_NAME + "::getValutazioneModuloSintesi] result = " + result);
			if (!result.isEmpty()) {
				Integer numeroTotaleIstanze = result.stream().map(vms -> vms.getNumeroIstanze()).reduce(Integer::sum).orElseThrow();
				if (numeroTotaleIstanze > 0) {
					result = result.stream()
						.map(vms -> completaPercent(vms, numeroTotaleIstanze))
						.collect(Collectors.toList());
				}
			}
			return result;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getValutazioneModuloSintesi] Errore generico servizio getValutazioneModuloSintesi",ex);
			throw new ServiceException("Errore generico servizio getValutazioneModuloSintesi");
		}
	}

	private ValutazioneModuloSintesi completaPercent(ValutazioneModuloSintesi vms, Integer numeroTotaleIstanze) {
		Float percent = 100f * vms.getNumeroIstanze() / numeroTotaleIstanze;
		vms.setPercent(percent);
		return vms;
	}

}
