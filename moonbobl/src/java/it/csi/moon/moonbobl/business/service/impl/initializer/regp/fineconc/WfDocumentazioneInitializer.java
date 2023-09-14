/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package  it.csi.moon.moonbobl.business.service.impl.initializer.regp.fineconc;

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
import it.csi.moon.moonbobl.business.service.impl.helper.RapportoFineConcessioneHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RapportoFineConcessioneEntity;
import it.csi.moon.moonbobl.business.service.impl.initializer.DatiIstanzaInitializer;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class WfDocumentazioneInitializer implements DatiIstanzaInitializer {

	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	IstanzeService istanzeService;
	
	private final static String CLASS_NAME = "WfDocumentazioneInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	

	public WfDocumentazioneInitializer() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);  
	}


	@Override
	public String getDati(AzioneInitParams initParams) throws BusinessException {
		try {

			log.info("[" + CLASS_NAME + "::getDati] start");
		
			RapportoFineConcessioneEntity entity = RapportoFineConcessioneHelper.parse(initParams.getDatiIstanza());
			
			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("data"); // data: {
		    
			    g.writeStringField("titolare", entity.getTitolare());
			    g.writeStringField("emailTitolare", entity.getEmail());
			    g.writeStringField("codiceUtenza", entity.getCur());
			    g.writeStringField("emailUfficio", "tutela.acque@regione.piemonte.it");
		  
		    g.writeEndObject(); // } end of data
		    g.close();
		    return writer.toString();
		    
		} catch(Exception e) {
	    	log.error("[" + CLASS_NAME + "::getDati] Errore - ", e);
	    	throw new BusinessException("ERROR Generica Initilizer DatiBeneficiarioInitializer");
	    }

	}


}
