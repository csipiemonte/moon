/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer.regp.ccdc;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.OneriCostrDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrDomandaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.business.service.impl.initializer.DatiIstanzaInitializer;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class ContributiCDCInitializer implements DatiIstanzaInitializer {

	@Autowired
	OneriCostrDAO oneriCostrDAO;
	
	private final static String CLASS_NAME = "ContributiCDCInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	

	public ContributiCDCInitializer() {
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
		    
		    g.writeStringField("codiceFiscale", initParams.getCodiceFiscale()); 
		    g.writeStringField("cognome", initParams.getCognome());
		    g.writeStringField("nome", initParams.getNome()); 
		    /* 
		    OneriCostrDatiIstanzaHelper oneriCostrHelper = new OneriCostrDatiIstanzaHelper();
		    OneriCostrDomandaEntity entity = oneriCostrHelper.parse(initParams.getDatiIstanza());
		    g.writeStringField("tipologiaIntervento", entity.getPrtTipologiaIntervento()); 
		    g.writeStringField("importoCalcolatoPresunto", entity.getPrtCostoPresunto());
		    */
		   
		    OneriCostrDomandaEntity oneriCostrDomanda = oneriCostrDAO.findByIdIstanza(initParams.getIdIstanza());
		    
		    g.writeStringField("tipologiaIntervento", oneriCostrDomanda.getPrtTipologiaIntervento()); 
		 
		    BigDecimal costo = new BigDecimal((double) oneriCostrDomanda.getPrtCostoPresunto()/100).setScale(2, RoundingMode.HALF_UP);
		    		    		    
		    g.writeNumberField("costoPresunto",costo);
		    
//		    DecimalFormat df2 = new DecimalFormat( "#.00" );
		
		    BigDecimal importo = new BigDecimal((double) oneriCostrDomanda.getBoImportoPagato()/100).setScale(2, RoundingMode.HALF_UP);
		    			    		   
		    g.writeNumberField("importoPresunto",importo);
		    		    
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
