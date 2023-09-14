/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.istanza;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class IstanzaDelegate_DEM_CINDCAF extends IstanzaDefaultDelegate implements IstanzaServiceDelegate{
	
	private static final String CLASS_NAME = "IstanzaDelegate_DEM_CINDCAF";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	public IstanzaDelegate_DEM_CINDCAF(){
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	public Istanza getInitIstanza(UserInfo user, ModuloVersionatoEntity moduloEntity, MapModuloAttributi attributi, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN moduloEntity: " + moduloEntity);	
				
			Istanza istanza = moonsrvDAO.initIstanza(httpRequest.getRemoteAddr(), user, moduloEntity.getIdModulo(), moduloEntity.getIdVersioneModulo(), params);
			
			calcAndVerifHashUnivocita(user, istanza, moduloEntity, attributi);
						
			return istanza;		
		
		} catch (UnivocitaIstanzaBusinessException univocitaBEx) {
			LOG.error("[" + CLASS_NAME + "::invia] UnivocitaIstanzaBusinessException");
			throw univocitaBEx;			
		}		
		catch (ItemNotFoundDAOException i) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] Soggetto non trovato - ", i);
			throw new BusinessException("Non è stato possibile recuperare i suoi dati presso l'archivio anagrafico, ripetere l'operazione","404");
		}
		catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore Init istanza per id");
		}
	}

}
