/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer.regp.ccdc;

import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.OneriCostrDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.OneriCostrIbanDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrDomandaEntity;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrIbanEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.business.service.impl.helper.OneriCostrDatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.initializer.DatiIstanzaInitializer;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class VerificaIbanInitializer implements DatiIstanzaInitializer {

	@Autowired
	OneriCostrDAO oneriCostrDAO;
	@Autowired
	OneriCostrIbanDAO oneriCostrIbanDAO;
	
	private final static String CLASS_NAME = "VerificaIbanInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	

	public VerificaIbanInitializer() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);  
	}


	@Override
	public String getDati(AzioneInitParams initParams) throws BusinessException {
		try {

			log.info("[" + CLASS_NAME + "::getDatiIstanza] start");

			//
			//
			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("data"); // data: {
		    
		    /*
		     * Dall'istanza acquisisco l'ente di appartenenza prt_codice_comune
		     */
		    OneriCostrDomandaEntity oneriCostrDomanda = oneriCostrDAO.findByIdIstanza(initParams.getIdIstanza());
		    
		    OneriCostrIbanEntity oneriCostrIbanEntity = oneriCostrIbanDAO.findByCodiceIstat(oneriCostrDomanda.getPrtCodiceComune());
		    
		    g.writeStringField("nomeComune", oneriCostrDomanda.getPrtNomeComune());
		    g.writeStringField("codiceComune", oneriCostrDomanda.getPrtCodiceComune());
		    
		    g.writeStringField("iban", oneriCostrIbanEntity.getIban()); 
		    g.writeStringField("ibanPrecedente", oneriCostrIbanEntity.getIban());
		    
		    
		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		    
		} catch(Exception e) {
	    	log.error("[" + CLASS_NAME + "::getDatiIstanza] Errore - ", e);
	    	throw new BusinessException("ERROR Generica Initilizer EstateRagazzi");
	    }

	}



}
