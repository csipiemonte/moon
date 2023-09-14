/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.initializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.moonsrv.business.service.dto.IstanzaInitCompletedParams;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

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

	private static final String CLASS_NAME = "IstanzaInitializer";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private IstanzaInitCompletedParams completedParams;
	private ModuloVersionatoEntity moduloVersionato;


	/**
	 * Inizializzazione con i oggetti standard per uso della factory giusta in seguito
	 * Da inizializzazione prima di richiamare i 2 get :
	 *  - getInitIstanza()
	 *  - getDatiIstanza()
	 * @param istanzaInitParams
	 * @param moduloVersionatoEntity
	 */
	public void initialize(IstanzaInitCompletedParams completedParams, ModuloVersionatoEntity moduloVersionatoEntity) {
		this.completedParams = completedParams;
		this.moduloVersionato = moduloVersionatoEntity;
	}
	
	/**
	 * Initializzazione dei attributi di Istanza generici che e la base della risposta dell inizializzazione al di fuori dei data
	 * @return
	 * @throws BusinessException
	 */
	public IstanzaEntity getInitIstanza() {
		IstanzaEntity istanzaE = new IstanzaEntity();
		istanzaE.setIdModulo(moduloVersionato.getIdModulo());
		istanzaE.setIdVersioneModulo(getInitParams().getIdVersioneModulo());
		istanzaE.setAttoreIns(getInitParams().getCodiceFiscale());
		return istanzaE;
	}

	/**
	 * Crea l'oggetto data string JSON per l'initializzazione del modulo
	 * Usa la Class passa in argomento per recuperare ogetto DatiIstanzaInitializer giusto secondo il modulo che si vuole inizializzare
	 * Il metodo rilancia ItemNotFoundBusinessException per gestire errore
	 * in caso di soggetto non trovato in ANPR.  
	 * -
	 * @return JSON or stringa vuota "" per impostare sempre il "data" nell istanza
	 * @throws Throwable 
	 */
	public String getDatiIstanza(String initDataNomeClass, Boolean flagCompilaBo) throws BusinessException {
		String result = "";
		try {
			if (StringUtils.isEmpty(initDataNomeClass) && ((flagCompilaBo  == null) || Boolean.FALSE.equals(flagCompilaBo))) {
				return result;
			} else {
				if (StringUtils.isEmpty(initDataNomeClass) && Boolean.TRUE.equals(flagCompilaBo)) {
					initDataNomeClass = DefaultAutInitializer.class.getCanonicalName();
				} else {
					if (initDataNomeClass!=null) {
						initDataNomeClass = initDataNomeClass.trim();
					}
				}
			}
			
			Class<?> initializerDatiIstanzaClass = Class.forName(initDataNomeClass);
	        Object initializerDatiIstanza = initializerDatiIstanzaClass.getDeclaredConstructors()[0].newInstance();
	        Method getDatiIstanzaMethod = Class.forName(initDataNomeClass).getMethod("getDatiIstanza", IstanzaInitCompletedParams.class, ModuloVersionatoEntity.class);
			result = (String) getDatiIstanzaMethod.invoke(initializerDatiIstanza, completedParams, moduloVersionato);
			return result;
		} catch (InvocationTargetException e) {			
			// gestisco se mi arriva business exception dagli initializer instanziati
			if (e.getTargetException() instanceof BusinessException) {
				LOG.error("[" + CLASS_NAME + "::getDatiIstanza] ERRORE instanceof BusinessException " + e.getCause());
				throw (BusinessException) e.getCause();
			} else {
				LOG.error("[" + CLASS_NAME + "::getDatiIstanza] ERRORE NOT instanceof BusinessException " + e.getCause());
				return "";
			}			
		} catch (Throwable e) {
			LOG.error("[" + CLASS_NAME + "::getDatiIstanza] ERRORE ", e);
			return "";
		}
	}
	
	protected IstanzaInitParams getInitParams() {
		return completedParams.getIstanzaInitParams();
	}
} 
