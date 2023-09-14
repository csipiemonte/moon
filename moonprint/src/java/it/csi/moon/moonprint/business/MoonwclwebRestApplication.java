/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.business;
/*
 * Classe che rappresenta la rest application
 */
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.moon.moonprint.business.be.impl.PdfApiImpl;
import it.csi.moon.moonprint.business.be.impl.PingApiImpl;
import it.csi.moon.moonprint.exceptions.service.handlers.ResourceNotFoundExceptionHandler;
import it.csi.moon.moonprint.exceptions.service.handlers.ServiceExceptionHandler;

// il path della annotation ApplicationPath indica la pruma parte della URI ed e' comune a tutte le resource
@ApplicationPath("restfacade/be")
public class MoonwclwebRestApplication extends Application{
	private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();
    
    public MoonwclwebRestApplication(){
    	/*
    	 * Posso registrare piu di una resource, purche' ognuna risponda ad un path diverso.
    	 *
    	 */
    	 // Elenco resource 
         singletons.add(new PingApiImpl());
         
         // MoonPrint
         singletons.add(new PdfApiImpl());

        
         empty.add(ResourceNotFoundExceptionHandler.class);
         empty.add(ServiceExceptionHandler.class);
    }
    @Override
    public Set<Class<?>> getClasses() {
         return empty;
    }
    @Override
    public Set<Object> getSingletons() {
         return singletons;
    }

}

