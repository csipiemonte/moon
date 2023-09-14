/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer.regp.tcr;

import java.io.StringWriter;
import java.io.Writer;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.IstanzeService;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.business.service.impl.helper.RinnovoConcessioniHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.RpTcrOssDisHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.comm.RinnovoConcessioniEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.TasseOsservazioniDiscaricoEntity;
import it.csi.moon.moonbobl.business.service.impl.initializer.DatiIstanzaInitializer;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class NotificaInitializer implements DatiIstanzaInitializer {

	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	IstanzeService istanzeService;
	
	private final static String CLASS_NAME = "NotificaInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	

	public NotificaInitializer() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);  
	}


	@Override
	public String getDati(AzioneInitParams initParams) throws BusinessException {
		try {

			log.info("[" + CLASS_NAME + "::getDati] start");

			Istanza istanza = istanzeService.getIstanzaById(null, initParams.getIdIstanza());

			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("data"); // data: {
		    		    
		    TasseOsservazioniDiscaricoEntity entity = new TasseOsservazioniDiscaricoEntity() ;
		    if (istanza.getModulo().getCodiceModulo().equals("RP_TASSE_OSS_DIS")) {
		       entity = RpTcrOssDisHelper.parse(initParams.getDatiIstanza());
		    }
		    else {
		    	entity = RpTcrOssDisHelper.parseRimborso(initParams.getDatiIstanza());
		    }
		    
		    if(entity.getTipoPersona().equals("personaFisica")) {
		    	g.writeStringField("nome",entity.getpFnome());
		    	g.writeStringField("cognome",entity.getpFcognome());
		    	g.writeStringField("codiceFiscale",entity.getpFcodiceFiscale());
		    }else if(entity.getTipoPersona().equals("personaGiuridica")) {
		    	g.writeStringField("nome",entity.getlRnome());
		    	g.writeStringField("cognome",entity.getlRcognome());
		    	g.writeStringField("codiceFiscale",entity.getlRcodiceFiscale());
		    }
		    
		    String email = RpTcrOssDisHelper.getEmail(initParams.getDatiIstanza());
		    g.writeStringField("email", email);
		    
		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		    
		} catch(Exception e) {
	    	log.error("[" + CLASS_NAME + "::getDati] Errore - ", e);
	    	throw new BusinessException("ERROR Generica Initilizer getDati");
	    }

	}


}
