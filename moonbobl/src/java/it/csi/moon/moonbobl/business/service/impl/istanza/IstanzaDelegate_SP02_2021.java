/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.istanza;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
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

public class IstanzaDelegate_SP02_2021 extends IstanzaDefaultDelegate implements IstanzaServiceDelegate{
	
	private final static String CLASS_NAME = "IstanzaDelegate_SP02_2021";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	public IstanzaDelegate_SP02_2021(){
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	public Istanza getInitIstanza(UserInfo user, ModuloVersionatoEntity moduloEntity, MapModuloAttributi attributi, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getInitIstanza] IN moduloEntity: " + moduloEntity);	
				
			Istanza istanza = moonsrvDAO.initIstanza(httpRequest.getRemoteAddr(), user, moduloEntity.getIdModulo(), moduloEntity.getIdVersioneModulo(), params);
			
			calcAndVerifHashUnivocita(user, istanza, moduloEntity, attributi);
						
			return istanza;		
		
		} catch (UnivocitaIstanzaBusinessException univocitaBEx) {
			log.error("[" + CLASS_NAME + "::invia] UnivocitaIstanzaBusinessException");
			throw univocitaBEx;			
		}		
		catch (ItemNotFoundDAOException i) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] Soggetto non trovato - ", i);
			throw new BusinessException("Non è stato possibile recuperare i suoi dati presso l'archivio anagrafico, ripetere l'operazione","404");
		}
		catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore Init istanza per id");
		}
	}

}
