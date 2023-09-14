/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business;
/*
 * Classe che rappresenta la rest application
 */
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.moon.moonfobl.business.be.extra.demografia.impl.CittadinanzeApiImpl;
import it.csi.moon.moonfobl.business.be.extra.demografia.impl.NazioniApiImpl;
import it.csi.moon.moonfobl.business.be.extra.demografia.impl.RelazioniParentelaApiImpl;
import it.csi.moon.moonfobl.business.be.extra.dirittostudio.impl.ScuoleApiImpl;
import it.csi.moon.moonfobl.business.be.extra.edilizia.impl.PraticheApiImpl;
import it.csi.moon.moonfobl.business.be.extra.istat.impl.IstatApiImpl;
import it.csi.moon.moonfobl.business.be.extra.territorio.impl.ToponomasticaApiImpl;
import it.csi.moon.moonfobl.business.be.impl.CmdApiImpl;
import it.csi.moon.moonfobl.business.be.impl.CurrentUserApiImpl;
import it.csi.moon.moonfobl.business.be.impl.FaqApiImpl;
import it.csi.moon.moonfobl.business.be.impl.FileApiImpl;
import it.csi.moon.moonfobl.business.be.impl.IstanzeApiImpl;
import it.csi.moon.moonfobl.business.be.impl.LocalLogoutApiImpl;
import it.csi.moon.moonfobl.business.be.impl.LoginApiImpl;
import it.csi.moon.moonfobl.business.be.impl.ModuliApiImpl;
import it.csi.moon.moonfobl.business.be.impl.MoonPrintApiImpl;
import it.csi.moon.moonfobl.business.be.impl.SupportoApiImpl;
import it.csi.moon.moonfobl.exceptions.service.handlers.ResourceNotFoundExceptionHandler;
import it.csi.moon.moonfobl.exceptions.service.handlers.ServiceExceptionHandler;

// il path della annotation ApplicationPath indica la pruma parte della URI ed e' comune a tutte le resource
@ApplicationPath("restfacade")
public class MoonwclwebRestApplication extends Application{
	private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> empty = new HashSet<>();
    
    public MoonwclwebRestApplication(){
    	/*
    	 * Posso registrare più di una resource, purche' ognuna risponda ad un path diverso.
    	 *
    	 */
    	 // Elenco resource 
         singletons.add(new CmdApiImpl());
         singletons.add(new LoginApiImpl());
         singletons.add(new LocalLogoutApiImpl());
         singletons.add(new CurrentUserApiImpl());
         singletons.add(new FileApiImpl());
         singletons.add(new MoonPrintApiImpl()); // mock di moonprint
         
         // Moon
         singletons.add(new ModuliApiImpl());
         singletons.add(new IstanzeApiImpl());
         singletons.add(new FaqApiImpl());
         singletons.add(new SupportoApiImpl());
         
         // Extra
         
         // ISTAT
         singletons.add(new IstatApiImpl());
         
         // Demografia
         singletons.add(new CittadinanzeApiImpl());
         singletons.add(new NazioniApiImpl());
         singletons.add(new RelazioniParentelaApiImpl());
         
         // Territorio
         singletons.add(new ToponomasticaApiImpl());
         
         // Educazione
         singletons.add(new ScuoleApiImpl());
         
         //Edilizia
         singletons.add(new PraticheApiImpl());
         
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

