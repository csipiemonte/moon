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

import it.csi.moon.moonbobl.business.service.ModulisticaService;
import it.csi.moon.moonbobl.business.service.impl.dao.LogonModeDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.PortaleModuloLogonModeDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.PortaleModuloLogonModeEntity;
import it.csi.moon.moonbobl.business.service.mapper.PortaleMapper;
import it.csi.moon.moonbobl.dto.moonfobl.LogonMode;
import it.csi.moon.moonbobl.dto.moonfobl.PortaleLogonMode;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaLogonMode;
import it.csi.moon.moonbobl.util.decodifica.DecodificaPortale;

@Component
public class ModulistiaServiceImpl implements ModulisticaService {

	private final static String CLASS_NAME = "PortaliServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	LogonModeDAO logonModeDAO;
	@Autowired
	PortaleModuloLogonModeDAO portaleModuloLogonModeDAO;
	
	@Override
	public List<LogonMode> getElencoLogonMode() {
		try {
			return logonModeDAO.find().stream()
				.collect(Collectors.toList());
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoLogonMode] Errore generico servizio getElencoLogonMode", ex);
			throw new BusinessException("Errore generico servizio elenco LogonMode");
		} 
	}

	@Override
	public List<PortaleLogonMode> getPortaliLogonModeByIdModulo(UserInfo user, Long idModulo) {
		try {
			return portaleModuloLogonModeDAO.findByIdModulo(idModulo).stream()
				.map(pmlm -> mapPortaleLogonMode(pmlm))
				.collect(Collectors.toList());
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getPortaliLogonModeByIdModulo] Errore generico servizio getPortaliLogonModeByIdModulo", ex);
			throw new BusinessException("Errore generico servizio elenco PortaliLogonMode ByIdModulo");
		}
	}

	private PortaleLogonMode mapPortaleLogonMode(PortaleModuloLogonModeEntity pmlm) {
		PortaleLogonMode result = new PortaleLogonMode();
		result.setIdModulo(pmlm.getIdModulo());
		result.setPortale(PortaleMapper.buildFromEntity(DecodificaPortale.byIdPortale(pmlm.getIdPortale()).getPortaleEntity()));
		result.setLogonMode(new LogonMode(DecodificaLogonMode.byIdLogonMode(pmlm.getIdLogonMode())));
		result.setFiltro(pmlm.getFiltro());
		return result;
	}

	@Override
	public PortaleLogonMode inserisciPortaleModuloLogonMode(UserInfo user, PortaleModuloLogonModeEntity pmlm) {
		try {
			portaleModuloLogonModeDAO.insert(pmlm);
			return mapPortaleLogonMode(pmlm);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getPortaliLogonModeByIdModulo] Errore generico servizio getPortaliLogonModeByIdModulo", ex);
			throw new BusinessException("Errore generico servizio elenco PortaliLogonMode ByIdModulo");
		}
	}

	@Override
	public void cancellaPortaleModuloLogonMode(UserInfo user, Long idPortale, Long idModulo) {
		try {
			portaleModuloLogonModeDAO.delete(idPortale, idModulo);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::cancellaPortaleModuloLogonMode] Errore generico servizio cancella PortaleModuloLogonMode", ex);
			throw new BusinessException("Errore generico servizio cancella PortaleModuloLogonMode");
		}
	}

}
