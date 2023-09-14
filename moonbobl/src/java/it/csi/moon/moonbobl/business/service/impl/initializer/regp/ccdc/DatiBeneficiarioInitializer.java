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
public class DatiBeneficiarioInitializer implements DatiIstanzaInitializer {

	@Autowired
	OneriCostrDAO oneriCostrDAO;
	
	private final static String CLASS_NAME = "DatiBeneficiarioInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	

	public DatiBeneficiarioInitializer() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);  
	}


	@Override
	public String getDati(AzioneInitParams initParams) throws BusinessException {
		try {

			log.info("[" + CLASS_NAME + "::getDatiIstanza] start");

			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("data"); // data: {
		    
		    g.writeStringField("codiceFiscaleOpe", initParams.getCodiceFiscale()); 
		    g.writeStringField("cognomeOpe", initParams.getCognome());
		    g.writeStringField("nomeOpe", initParams.getNome()); 
		    
		    OneriCostrDomandaEntity oneriCostrDomanda = oneriCostrDAO.findByIdIstanza(initParams.getIdIstanza());
		    
		    g.writeStringField("tipologiaBeneficiario", oneriCostrDomanda.getBenTipologiaBeneficiario());
		    if (oneriCostrDomanda.getBenTipologiaBeneficiario().equals("personaFisica")) 
		    {
			    g.writeStringField("cognomeBeneficiario", oneriCostrDomanda.getBenCognome());
			    g.writeStringField("nomeBeneficiario", oneriCostrDomanda.getBenNome());
			    g.writeStringField("codiceFiscaleBeneficiario", oneriCostrDomanda.getBenCodiceFiscale());
		    }
		    else {
		    	g.writeStringField("ragioneSocialeBeneficiario", oneriCostrDomanda.getBenRagioneSociale());
			    g.writeStringField("partitaIvaBeneficiario", oneriCostrDomanda.getBenPiva());
			    g.writeStringField("dataFineEsercizioBeneficiario", translateData(oneriCostrDomanda.getBenDataFineEsercizio()));
			    g.writeStringField("dimensioneImpresaBeneficiario", oneriCostrDomanda.getBenDimensione());		    
			    g.writeStringField("codiceAtecoBeneficiario", oneriCostrDomanda.getBenCodiceAteco());
			    g.writeStringField("formaGiuridicaBeneficiario", oneriCostrDomanda.getBenFormaGiuridica());

		    }
		   
		    g.writeStringField("nazioneSedeBeneficiario", oneriCostrDomanda.getBenSedeNazione());
		    g.writeStringField("regioneSedeBeneficiario", oneriCostrDomanda.getBenSedeRegione());
		    g.writeStringField("provinciaSedeBeneficiario", oneriCostrDomanda.getBenSedeProvincia());
		    g.writeStringField("comuneSedeBeneficiario", oneriCostrDomanda.getBenSedeComune());
		    g.writeStringField("indirizzoSedeBeneficiario", oneriCostrDomanda.getBenSedeIndirizzo());
		    g.writeStringField("capSedeBeneficiario", oneriCostrDomanda.getBenSedeCap());
		    
		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		    
		} catch(Exception e) {
	    	log.error("[" + CLASS_NAME + "::getDatiIstanza] Errore - ", e);
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
