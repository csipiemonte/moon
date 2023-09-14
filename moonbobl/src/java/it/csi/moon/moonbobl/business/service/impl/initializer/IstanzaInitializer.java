/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;;

/**
 * Initializer di oggetto Istanza
 * Oltre ai dati principale dell'istanza, richiama DatiIstanzaInitializer corrispondente al modulo richiesto.
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class IstanzaInitializer {

	private final static String CLASS_NAME = "IstanzaInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	private AzioneInitParams initParams;

	/**
	 * @param istanzaInitParams
	 * @param moduloEntity
	 */
	public void initialize(AzioneInitParams initParams) {
		this.initParams = initParams;
	}
	
	/**
	 * Initializzazione dei attributi di Istanza generici che e la base della risposta dell inizializzazione al di fuori dei data
	 * @return
	 * @throws BusinessException
	 */
	public IstanzaEntity getInitIstanza() {
		IstanzaEntity istanzaE = new IstanzaEntity();
		istanzaE.setAttoreIns(initParams.getCodiceFiscale());
		return istanzaE;
	}


	/**
	 * Crea l'oggetto data string JSON per l'initializzazione del modulo
	 * Usa la Class passa in argomento per recuperare ogetto DatiIstanzaInitializer giusto secondo il modulo che si vuole inizializzare
	 * @return JSON or stringa vuota "" per impostare sempre il "data" nell istanza
	 */
	public String getDati(String initDataNomeClass) {
		String result = "";
		try {
			if (StringUtils.isEmpty(initDataNomeClass)) {
				return "";
			}
			
			Class<?> initializerDatiIstanzaClass = Class.forName(initDataNomeClass);
	        Object initializerDatiIstanza = initializerDatiIstanzaClass.getDeclaredConstructors()[0].newInstance();
	        Method getDatiIstanzaMethod = Class.forName(initDataNomeClass).getMethod("getDati", AzioneInitParams.class);
			result = (String) getDatiIstanzaMethod.invoke(initializerDatiIstanza, initParams);
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::getDati] ERRORE ", e);
			return "";
		}
	}
}
