/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer.coto.dem;

import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.CambioResidenzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.dem.CambioResidenzaEntity;
import it.csi.moon.moonbobl.business.service.impl.initializer.DatiIstanzaInitializer;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class DatiRichiedenteInitializer implements DatiIstanzaInitializer {

	@Autowired
	IstanzaDAO istanzaDAO;
	
	private final static String CLASS_NAME = "DatiRichiedenteInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	

	public DatiRichiedenteInitializer() {
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
		    String operatore = initParams.getNome() + " " + initParams.getCognome();
		    g.writeObjectFieldStart("operatore");
			    g.writeStringField("nome", operatore.toUpperCase()); 
			    g.writeStringField("codice", initParams.getCodiceFiscale().toLowerCase()); 
			g.writeEndObject(); // }
		    
//		    CambioResidenzaHelper cambioResidenzaHelper = new CambioResidenzaHelper();
		    CambioResidenzaEntity entity = CambioResidenzaHelper.parse(initParams.getDatiIstanza());
		    g.writeStringField("richiedente", entity.getRichiedente()); 
		    g.writeStringField("indirizzo", entity.getIndirizzo());
		    g.writeStringField("numeroComponenti", ""+entity.getNumeroComponenti());
		    
		    Date dataInvio = null;
			IstanzaCronologiaStatiEntity datiInvio = istanzaDAO.findInvio(initParams.getIdIstanza());
			if (datiInvio != null) {
				dataInvio = datiInvio.getDataInizio();
			}
			g.writeStringField("dataPresentazione", translateData(dataInvio));

		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		    
		} catch(Exception e) {
	    	log.error("[" + CLASS_NAME + "::getDati] Errore - ", e);
	    	throw new BusinessException("ERROR Generica Initilizer DatiBeneficiarioInitializer");
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
