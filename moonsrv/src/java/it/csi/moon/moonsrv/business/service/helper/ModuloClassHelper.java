/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.helper;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.entity.ModuloClassEntity;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;



public class ModuloClassHelper extends ClassLoader {

	private static final String CLASS_NAME = "ModuloClassHelper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final ClassLoader CLASS_LOADER_PADRE = ModuloClassHelper.class.getClassLoader();
	private static final String PRINT_ISTANZA_MAPPER_PCK = "it.csi.moon.moonsrv.business.service.mapper.moonprint";
	private static final String PROTOCOLLO_MANAGER_PCK = "it.csi.moon.moonsrv.business.service.impl.protocollo";
	private static final String PRINT_ISTANZA_MAPPER_INTERFACE = PRINT_ISTANZA_MAPPER_PCK + ".PrintIstanzaMapper";
	private static final String PROTOCOLLO_MANAGER_INTERFACE = PROTOCOLLO_MANAGER_PCK + ".StardasProtocollo";
	
	
	public ModuloClassHelper() {
		super(CLASS_LOADER_PADRE);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	
	public ModuloClassEntity verify(Long idModulo, int idTipologia, byte[] b) {
		Class c = null;
		try {
			c = defineClass(null, b, 0, b.length);
		} catch (ClassFormatError cfe) {
			LOG.error("[" + CLASS_NAME + "::verify] ClassFormatError define file.class ", cfe);
			throw new BusinessException("Errore Format Class, file non salvato","MOONSRV-20701");
		}
		String nomeClasse = c.getName();
//		if(!Arrays.stream(c.getInterfaces())
//				.filter(i -> intercacceOfTipologia(idTipologia).equals(i.getName()))
//				.findFirst()
//				.isPresent()) {
//			LOG.error("[" + CLASS_NAME + "::verify] Errore tipologia " + idTipologia + " for class " + nomeClasse);
//			throw new BusinessException("Errore Tipologia Class, file non salvato","MOONSRV-20702");
//		}
		if (notPackageOfTipologia(nomeClasse, idTipologia)) {
			LOG.error("[" + CLASS_NAME + "::verify] Errore tipologia " + idTipologia + " for class " + nomeClasse);
			throw new BusinessException("Errore Tipologia Class, file non salvato","MOONSRV-20702");
		}
		
		ModuloClassEntity ris = new ModuloClassEntity();
		ris.setIdModulo(idModulo);
		ris.setNomeClass(nomeClasse);
		ris.setContenuto(b);
		ris.setTipologia(idTipologia);
		
		return ris;
	}


	private boolean notPackageOfTipologia(String nomeClasse, int idTipologia) {
		return !nomeClasse.startsWith(packageOfTipologia(idTipologia));
	}

	private String packageOfTipologia(int idTipologia) {
		String result = null;
		switch(idTipologia) {
//			1: return null; break;
			case 2: result = PRINT_ISTANZA_MAPPER_PCK; break;
			case 3: result = PROTOCOLLO_MANAGER_PCK; break;
			default: 
				LOG.error("[" + CLASS_NAME + "::packageOfTipologia] Errore tipologia " + idTipologia + " not yet implemented.");
				throw new BusinessException("Errore Tipologia Class, file non salvato","MOONSRV-20702");
		}
		return result;
	}
	
	private String intercacceOfTipologia(int idTipologia) {
		String result = null;
		switch(idTipologia) {
//			1: return null; break;
			case 2: result = PRINT_ISTANZA_MAPPER_INTERFACE; break;
			case 3: result = PROTOCOLLO_MANAGER_INTERFACE; break;
			default: 
				LOG.error("[" + CLASS_NAME + "::intercacceOfTipologia] Errore tipologia " + idTipologia + " not yet implemented.");
				throw new BusinessException("Errore Tipologia Class, file non salvato","MOONSRV-20702");
		}
		return result;
	}
	
}
