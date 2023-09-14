/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.initializer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonsrv.business.service.dto.IstanzaInitCompletedParams;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Initializer di default senza comunicazione con anpr 
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class DefaultAutInitializer implements DatiIstanzaInitializer {

	private static final String CLASS_NAME = "DefaultAutInitializer";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	public DefaultAutInitializer() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public String getDatiIstanza(IstanzaInitCompletedParams completedParams, ModuloVersionatoEntity modulo) throws BusinessException {
		try {
			LOG.info("[" + CLASS_NAME + "::getDatiIstanza] start");
			IstanzaInitParams initParams = completedParams.getIstanzaInitParams();
			MapModuloAttributi attributi = completedParams.getModuloAttributi();
	        Boolean activated = attributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE);
	  		if (Boolean.TRUE.equals(activated)) {
				String conf = attributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE_CONF);				
				if(!StringUtils.isEmpty(conf)) {
					return getDatiIstanzaOperatoreCompila(completedParams, modulo, conf);
				}
			}
			
			/*
	  		 * Se NON esiste la configurazione su db, proseguo come le vecchie init
	  		 */
			String codiceFiscale = "";
			String nome = "";
			String cognome = "";
			String compilazioneContoTerzi = "N";
			if (initParams.getBlParams()!=null && !StringUtils.isEmpty(initParams.getBlParams().getCodiceFiscale())) {
				
				String codiceFiscaleContoTerzi = initParams.getBlParams().getCodiceFiscale();
				String nomeContoTerzi = initParams.getBlParams().getNome();
				String cognomeContoTerzi = initParams.getBlParams().getCognome();
				codiceFiscale = codiceFiscaleContoTerzi;
				nome = (nomeContoTerzi != null) ? nomeContoTerzi : "";
				cognome = (cognomeContoTerzi != null) ? cognomeContoTerzi : "";
				compilazioneContoTerzi = "S";
				
				LOG.info("[" + CLASS_NAME + "::getDatiIstanza] Init Conto Terzi per CF = " +codiceFiscale);
			}
			else {
				codiceFiscale = initParams.getCodiceFiscale();
				nome = initParams.getNome();
				cognome = initParams.getCognome();
				
				LOG.info("[" + CLASS_NAME + "::getDatiIstanza] Init per CF = " +codiceFiscale);
			}
			

			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.writeStartObject(); // {
			g.writeObjectFieldStart("data"); // data:
				g.writeStringField("compilazioneContoTerzi", compilazioneContoTerzi);
				g.writeObjectFieldStart("richiedente");
					g.writeStringField("codiceFiscale", codiceFiscale);
					g.writeStringField("cognome", cognome);
					g.writeStringField("nome", nome);
				g.writeEndObject(); // } end of richiedente
			g.writeEndObject(); // } end of data
		    g.writeEndObject(); // } end of OBJECT
		    g.close();
		    return writer.toString();
		    
		} catch(BusinessException e) {
			throw e;
		} catch(Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getDatiIstanza] Errore - ", e);
	    	throw new BusinessException("ERROR Generico DefaultAutInitializer");
	    }

	}
	
	public String getDatiIstanzaOperatoreCompila(IstanzaInitCompletedParams completedParams, ModuloEntity modulo, String conf) throws BusinessException {
		IstanzaInitParams initParams = completedParams.getIstanzaInitParams();
		try {
			/*
			 * Leggo i dati passati in input dai popup conto terzi
			 */
			String codiceFiscaleContoTerzi = initParams.getBlParams().getCodiceFiscale();
			String nomeContoTerzi = initParams.getBlParams().getNome();
			String cognomeContoTerzi = initParams.getBlParams().getCognome();
			String compilazioneContoTerzi = "N";
			
			String codiceFiscale = "";
			String nome = "";
			String cognome = "";
			if (codiceFiscaleContoTerzi != null) {
				codiceFiscale = codiceFiscaleContoTerzi;
				nome = (nomeContoTerzi != null) ? nomeContoTerzi : "";
				cognome = (cognomeContoTerzi != null) ? cognomeContoTerzi : "";
				compilazioneContoTerzi = "S";

				LOG.info("[" + CLASS_NAME + "::getDatiIstanza] Init Conto Terzi, con configurazione parametri, per CF = " +codiceFiscale);
			}
			else {
				codiceFiscale = initParams.getCodiceFiscale();
				nome = initParams.getNome();
				cognome = initParams.getCognome();
				
				LOG.info("[" + CLASS_NAME + "::getDatiIstanza] Init, con configurazione parametri, per CF = " +codiceFiscale);
			}
			
			JsonNode confJson = readConfJson(conf);
			
			String codiceFiscaleKey = confJson.get("codice_fiscale_dichiarante_data_key")!=null?confJson.get("codice_fiscale_dichiarante_data_key").asText():"";
            String nomeKey = confJson.get("nome_dichiarante_data_key")!=null?confJson.get("nome_dichiarante_data_key").asText():"";
            String cognomeKey = confJson.get("cognome_dichiarante_data_key")!=null?confJson.get("cognome_dichiarante_data_key").asText():"";
			
			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("data"); // data: {
		    g.writeStringField("compilazioneContoTerzi", compilazioneContoTerzi);
		    g.writeStringField(codiceFiscaleKey, codiceFiscale); // "codiceFiscale"
	        g.writeStringField(cognomeKey,cognome); // "cognome"
			g.writeStringField(nomeKey, nome); // "nome"			    			
		    
		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    
		    g.close();
		    return writer.toString();
		} catch(BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getDatiIstanzaOperatoreCompila] BusinessException");
			throw be;
		} catch(Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getDatiIstanzaOperatoreCompila] ERROR Generico DefaultAutInitializer " + initParams.getCodiceFiscale(), e);
	    	throw new BusinessException("ERROR Generico DefaultAutInitializer");
	    }
	}
	
	private JsonNode readConfJson(String strJson) throws Exception {
		try {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: "+strJson);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readConfJson] ERROR "+e.getMessage());
		    throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}

}
