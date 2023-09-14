/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.initializer;

import java.io.StringWriter;
import java.io.Writer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.IntegrazioneInitParams;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @author Danilo
 * 
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class ModIntegrazioneInitializer implements DatiIntegrazioneInitializer {

	@Autowired
	IstanzaDAO istanzaDAO;
	
	private static final String CLASS_NAME = "IntegrazioneInitializer";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	

	public ModIntegrazioneInitializer() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);  
	}


	@Override
	public String getDati(IntegrazioneInitParams initParams) throws BusinessException {
		try {

			LOG.info("[" + CLASS_NAME + "::getDati] start");

			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.setCodec(new ObjectMapper());
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("data"); // data: {	
		    g.writeStringField("nome", initParams.getNome());
		    g.writeStringField("cognome", initParams.getCognome());
		    g.writeStringField("codiceFiscale", initParams.getCodiceFiscale());
		    g.writeStringField("email", initParams.getEmail());		    		   
		    g.writeStringField("testo", initParams.getTesto());		   
		    		   		    
		    g.writeFieldName("allegati");			   
		    g.writeTree(initParams.getAllegati());
		    
		    g.writeFieldName("emailCc");			   
		    g.writeTree(initParams.getEmailCc());
		    
		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		    
		} catch(Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getDati] Errore - ", e);
	    	throw new BusinessException("ERROR Generica Initilizer ModIntegrazioneInitializer");
	    }

	}	

}
