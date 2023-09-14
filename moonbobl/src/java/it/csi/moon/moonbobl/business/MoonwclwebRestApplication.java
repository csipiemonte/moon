/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business;
/*
 * Classe che rappresenta la rest application
 */
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.moon.moonbobl.business.be.extra.demografia.impl.CittadinanzeApiImpl;
import it.csi.moon.moonbobl.business.be.extra.demografia.impl.NazioniApiImpl;
import it.csi.moon.moonbobl.business.be.extra.demografia.impl.RelazioniParentelaApiImpl;
import it.csi.moon.moonbobl.business.be.extra.istat.impl.IstatApiImpl;
import it.csi.moon.moonbobl.business.be.extra.territorio.impl.ToponomasticaApiImpl;
import it.csi.moon.moonbobl.business.be.impl.CategorieApiImpl;
import it.csi.moon.moonbobl.business.be.impl.CmdApiImpl;
import it.csi.moon.moonbobl.business.be.impl.ComuniApiImpl;
import it.csi.moon.moonbobl.business.be.impl.CurrentUserApiImpl;
import it.csi.moon.moonbobl.business.be.impl.FaqApiImpl;
import it.csi.moon.moonbobl.business.be.impl.FileApiImpl;
import it.csi.moon.moonbobl.business.be.impl.FunzioniApiImpl;
import it.csi.moon.moonbobl.business.be.impl.IstanzeApiImpl;
import it.csi.moon.moonbobl.business.be.impl.LocalLogoutApiImpl;
import it.csi.moon.moonbobl.business.be.impl.ModuliApiImpl;
import it.csi.moon.moonbobl.business.be.impl.ReportApiImpl;
import it.csi.moon.moonbobl.business.be.impl.RuoliApiImpl;
import it.csi.moon.moonbobl.business.be.impl.SupportoApiImpl;
import it.csi.moon.moonbobl.business.be.impl.TipiCodiceIstanzaApiImpl;
import it.csi.moon.moonbobl.business.be.impl.UtentiApiImpl;
import it.csi.moon.moonbobl.business.be.impl.ValutazioneModuloApiImpl;
import it.csi.moon.moonbobl.business.be.impl.WorkflowApiImpl;
import it.csi.moon.moonbobl.exceptions.service.handlers.ResourceNotFoundExceptionHandler;
import it.csi.moon.moonbobl.exceptions.service.handlers.ServiceExceptionHandler;

// il path della annotation ApplicationPath indica la pruma parte della URI ed e' comune a tutte le resource
@ApplicationPath("restfacade")
public class MoonwclwebRestApplication extends Application{
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	
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
    	singletons.add(new TipiCodiceIstanzaApiImpl());
    	singletons.add(new ValutazioneModuloApiImpl());
    	
		 // Elenco resource 
		 singletons.add(new CmdApiImpl());
		 singletons.add(new LocalLogoutApiImpl());
		 singletons.add(new CurrentUserApiImpl());
		 singletons.add(new ComuniApiImpl());
		 singletons.add(new FileApiImpl());
		 
		 singletons.add(new WorkflowApiImpl());
		 singletons.add(new ReportApiImpl());
		 
		 // Extra
		 
		 // ISTAT
		 singletons.add(new IstatApiImpl());
		 
		 // Demografia
		 singletons.add(new CittadinanzeApiImpl());
		 singletons.add(new NazioniApiImpl());
		 singletons.add(new RelazioniParentelaApiImpl());
		 
		 // Territorio
		 singletons.add(new ToponomasticaApiImpl());

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

