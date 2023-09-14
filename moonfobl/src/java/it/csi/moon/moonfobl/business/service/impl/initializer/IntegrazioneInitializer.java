/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.initializer;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import it.csi.moon.moonfobl.business.service.impl.dto.IntegrazioneInitParams;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * Initializer Integrazione Istanza
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
@Component
public class IntegrazioneInitializer {

	private static final String CLASS_NAME = "IntegrazioneInitializer";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private IntegrazioneInitParams initParams;

	/**
	 * @param istanzaInitParams
	 * @param moduloEntity
	 */
	public void initialize(IntegrazioneInitParams initParams) {
		this.initParams = initParams;
	}
	

	/**
	 * Crea l'oggetto data string JSON per l'initializzazione del modulo di integrazione
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
	        Method getDatiIstanzaMethod = Class.forName(initDataNomeClass).getMethod("getDati", IntegrazioneInitParams.class);
			result = (String) getDatiIstanzaMethod.invoke(initializerDatiIstanza, initParams);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getDati] ERRORE ", e);
			return "";
		}
	}
}
