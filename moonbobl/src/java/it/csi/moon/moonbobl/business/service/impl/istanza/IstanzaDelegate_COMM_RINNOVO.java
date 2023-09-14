/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.istanza;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.MapModuloAttributi;

public class IstanzaDelegate_COMM_RINNOVO extends IstanzaDefaultDelegate implements IstanzaServiceDelegate {
	private final static String CLASS_NAME = "IstanzaDefaultDelegate";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	
	
	
	public IstanzaDelegate_COMM_RINNOVO() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	public Istanza getInitIstanza(UserInfo user, ModuloVersionatoEntity moduloEntity, MapModuloAttributi attributi, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getInitIstanzaByIdModulo] IN moduloEntity: " + moduloEntity);
			
			// TODO validare 1.Stato modulo PUB e 2. accessibile dall'utente

			Istanza istanza = moonsrvDAO.initIstanza(httpRequest.getRemoteAddr(), user, moduloEntity.getIdModulo(), moduloEntity.getIdVersioneModulo(), params);

			return istanza;
		} 
		catch (ItemNotFoundDAOException i) {
			log.error("[" + CLASS_NAME + "::getInitIstanzaByIdModulo] Soggetto non trovato - ", i);
			throw new BusinessException("Non è stato possibile recuperare i suoi dati, ripetere l'operazione","404");
		}
		catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getInitIstanzaByIdModulo] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore Init istanza per id");
		}
	}
	

}

