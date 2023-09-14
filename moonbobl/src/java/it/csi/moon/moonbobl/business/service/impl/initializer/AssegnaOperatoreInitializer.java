/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class AssegnaOperatoreInitializer implements DatiIstanzaInitializer {

	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	UtenteDAO utenteDAO;
	
	private final static String CLASS_NAME = "AssegnaOperatoreInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	

	public AssegnaOperatoreInitializer() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);  
	}


	@Override
	public String getDati(AzioneInitParams initParams) throws BusinessException {
		try {

			log.info("[" + CLASS_NAME + "::getDati] start");

			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("data"); // data: {
		    
		    g.writeStringField("codiceFiscaleOpe", initParams.getCodiceFiscale()); 
		    g.writeStringField("cognomeOpe", initParams.getCognome());
		    g.writeStringField("nomeOpe", initParams.getNome()); 
		    
		    List<UtenteEntity> elencoOperatori = utenteDAO.utentiAreaByCf(initParams.getCodiceFiscale());
		    
		    g.writeArrayFieldStart("elencoOperatori");
			for(int i=0;i< elencoOperatori.size();i++) {
				String operatore = elencoOperatori.get(i).getIdentificativoUtente() + "-" +
						elencoOperatori.get(i).getNome() + " " + elencoOperatori.get(i).getCognome();
				g.writeStartObject(); // {
				g.writeStringField("operatore", operatore);
				g.writeEndObject();
				
			}
			g.writeEndArray(); 
		    
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
