/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business;
/*
 * Classe che rappresenta la rest application
 */
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.moon.moonsrv.business.be.extra.demografia.impl.CittadinanzeApiImpl;
import it.csi.moon.moonsrv.business.be.extra.demografia.impl.DemografiaApiImpl;
import it.csi.moon.moonsrv.business.be.extra.demografia.impl.NazioniApiImpl;
import it.csi.moon.moonsrv.business.be.extra.istat.impl.IstatApiImpl;
import it.csi.moon.moonsrv.business.be.extra.notificatore.impl.NotificatoreApiImpl;
import it.csi.moon.moonsrv.business.be.extra.tecno.impl.RemedyApiImpl;
import it.csi.moon.moonsrv.business.be.extra.territorio.impl.GinevraApiImpl;
import it.csi.moon.moonsrv.business.be.extra.territorio.impl.LimammApiImpl;
import it.csi.moon.moonsrv.business.be.extra.territorio.impl.ToponomasticaApiImpl;
import it.csi.moon.moonsrv.business.be.extra.wf.impl.CosmoApiImpl;
import it.csi.moon.moonsrv.business.be.impl.ApiImpl;
import it.csi.moon.moonsrv.business.be.impl.CategorieApiImpl;
import it.csi.moon.moonsrv.business.be.impl.CmdApiImpl;
import it.csi.moon.moonsrv.business.be.impl.EmailApiImpl;
import it.csi.moon.moonsrv.business.be.impl.FaqApiImpl;
import it.csi.moon.moonsrv.business.be.impl.FunzioniApiImpl;
import it.csi.moon.moonsrv.business.be.impl.IstanzeApiImpl;
import it.csi.moon.moonsrv.business.be.impl.ModuliApiImpl;
import it.csi.moon.moonsrv.business.be.impl.ModuliExpApiImpl;
import it.csi.moon.moonsrv.business.be.impl.RepositoryFileApiImpl;
import it.csi.moon.moonsrv.business.be.impl.RuoliApiImpl;
import it.csi.moon.moonsrv.business.be.impl.SupportoApiImpl;
import it.csi.moon.moonsrv.business.be.impl.TipiCodiceIstanzaApiImpl;
import it.csi.moon.moonsrv.business.be.impl.TrasmissioneApiImpl;
import it.csi.moon.moonsrv.business.be.impl.UtentiApiImpl;
import it.csi.moon.moonsrv.exceptions.service.handlers.ResourceNotFoundExceptionHandler;
import it.csi.moon.moonsrv.exceptions.service.handlers.ServiceExceptionHandler;

// il path della annotation ApplicationPath indica la pruma parte della URI ed e' comune a tutte le resource
@ApplicationPath("restfacade/be")
public class MoonwclwebRestApplication extends Application{
	private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> empty = new HashSet<>();
    
    public MoonwclwebRestApplication(){

    	// Moon resource
    	singletons.add(new UtentiApiImpl());
    	singletons.add(new ModuliApiImpl());
    	singletons.add(new IstanzeApiImpl());
    	singletons.add(new CategorieApiImpl());
    	singletons.add(new FunzioniApiImpl());
    	singletons.add(new RuoliApiImpl());
    	singletons.add(new FaqApiImpl());
    	singletons.add(new SupportoApiImpl());    	
    	singletons.add(new TrasmissioneApiImpl());
    	singletons.add(new TipiCodiceIstanzaApiImpl());
    	singletons.add(new RepositoryFileApiImpl());
    	
    	// Service 
    	singletons.add(new CmdApiImpl());
    	singletons.add(new ApiImpl());
    	singletons.add(new EmailApiImpl());
    	singletons.add(new ModuliExpApiImpl());
    	 
    	// 100 - ISTAT
    	singletons.add(new IstatApiImpl());
    	 
    	// 200 - Demografia
    	singletons.add(new CittadinanzeApiImpl());
    	singletons.add(new NazioniApiImpl());
    	singletons.add(new DemografiaApiImpl());
    	 
    	// 300 - Territorio
    	singletons.add(new ToponomasticaApiImpl());
    	singletons.add(new GinevraApiImpl());
    	singletons.add(new LimammApiImpl());

    	// 400 - Notificatore
    	singletons.add(new NotificatoreApiImpl());
    	
    	// 500 - WF COSMO
    	singletons.add(new CosmoApiImpl());
    	
    	// 600 - TECNO
    	singletons.add(new RemedyApiImpl());        	
    	 
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

