/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer.regp.ccdc;

import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.OneriCostrDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrDomandaEntity;
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
public class DatiPraticaInitializer implements DatiIstanzaInitializer {

	@Autowired
	OneriCostrDAO oneriCostrDAO;
	
	private final static String CLASS_NAME = "DatiPraticaInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	

	public DatiPraticaInitializer() {
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
		    g.writeStringField("provincia", entity.getPrtNomeProvincia());
		    g.writeStringField("tipologiaIntervento", entity.getPrtTipologiaIntervento()); 
		    g.writeStringField("importoCalcolatoPresunto", entity.getPrtCostoPresunto());
		    */
		   
		    OneriCostrDomandaEntity oneriCostrDomanda = oneriCostrDAO.findByIdIstanza(initParams.getIdIstanza());
		    g.writeStringField("provincia", oneriCostrDomanda.getPrtNomeProvincia());
		    g.writeStringField("comune", oneriCostrDomanda.getPrtNomeComune());
		    g.writeStringField("indirizzo", oneriCostrDomanda.getPrtIndirizzo());
		    g.writeStringField("cap", oneriCostrDomanda.getPrtCap());
		    
		    g.writeStringField("tipologiaTitoloEdilizio", oneriCostrDomanda.getPrtTipologiaTitoloEdilizio()); 
		    g.writeStringField("dataPresentazionePratica",translateData( oneriCostrDomanda.getPrtDataPresentazione()));
		    g.writeStringField("tipologiaIntervento", oneriCostrDomanda.getPrtTipologiaIntervento());
		    g.writeStringField("numeroPratica", oneriCostrDomanda.getPrtNumeroPratica());
		    
		    
		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		    
		} catch(Exception e) {
	    	log.error("[" + CLASS_NAME + "::getDatiIstanza] Errore - ", e);
	    	throw new BusinessException("ERROR Generica Initilizer DatiPraticaInitializer");
	    }

	}

	private String translateData(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		log.debug("[" + CLASS_NAME + "::translateData] IN " + date);
		String result = sdf.format(date);
		log.debug("[" + CLASS_NAME + "::translateData] OUT " + result);
		return result;
	}

}
