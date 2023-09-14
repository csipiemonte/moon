/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dto.ModuloClassEntity;


public class ModuloClassHelper extends ClassLoader {

	private final static ClassLoader classLoaderPadre = ModuloClassHelper.class.getClassLoader();
	private static final String PrintIstanzaMapper = "PrintIstanzaMapper";
	
	public ModuloClassHelper() {
		super(classLoaderPadre);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	
	public ModuloClassEntity verify(Long idModulo, int idTipologia, byte[] b) {
		Class c = defineClass(null,  b, 0,b.length);
		String nomeClasse = c.getName();
		Class[] interfacce = c.getInterfaces();
		
		switch(interfacce[0].getName()) {
		case PrintIstanzaMapper:
			if(idTipologia !=2 ) {
				
			}
			break;

		default:
			break;
		}
		
		ModuloClassEntity ris = new ModuloClassEntity();
		ris.setIdModulo(idModulo);
		ris.setNomeClass(nomeClasse);
		ris.setContenuto(b);
		ris.setTipologia(idTipologia);
		
		return ris;
	}
	
}
