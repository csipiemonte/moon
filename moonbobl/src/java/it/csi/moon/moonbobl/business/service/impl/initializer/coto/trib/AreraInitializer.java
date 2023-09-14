/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer.coto.trib;

import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.IstanzeService;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.RinnovoConcessioniHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.TariAreraHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.tari.TariAreraEntity;
import it.csi.moon.moonbobl.business.service.impl.initializer.DatiIstanzaInitializer;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class AreraInitializer implements DatiIstanzaInitializer {

	@Autowired
	IstanzeService istanzeService;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	
	private final static String CLASS_NAME = "AreraInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	

	public AreraInitializer() {
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
		    
		    TariAreraEntity entity = TariAreraHelper.parse(initParams.getDatiIstanza());
		    g.writeStringField("codiceUtente", entity.getCodiceUtente());
		    String tipoContribuente = entity.getTipoContribuente();
		    g.writeStringField("tipoContribuente",tipoContribuente);
		    if (tipoContribuente.equals("personaFisica")) {
		    	g.writeStringField("nome", entity.getNome()); 
		    	g.writeStringField("cognome", entity.getCognome());
		    }
		    else {
			    g.writeStringField("ragioneSociale", entity.getRagioneSociale());		    	
		    }
		    g.writeStringField("email", entity.getEmail());
		    g.writeStringField("codiceUtenza", entity.getCodiceUtenza());
		    g.writeStringField("tipologiaUtenza", entity.getTipologiaUtenza());
		    g.writeStringField("tipologiaRichiesta", entity.getTipologiaRichiesta());
		    
		    Istanza istanza = istanzeService.getIstanzaById(null, initParams.getIdIstanza());
		    String dataRicezione = translateData(istanza.getCreated());
		    if (istanza.getModulo().getCodiceModulo().equals("TARI_BACKOFFICE"))
		    {
		    	dataRicezione = entity.getDataRicezione();
		    }
		    g.writeStringField("dataRicezione", dataRicezione);
		    
		    // La data di chiusura è la data del giorno, a meno che non sia già stata imputata nel passato
		    Date dataChiusura = new Date();
		    String dataChiusuraStr = translateData(dataChiusura);
		    
			List<String> listAzioni = Arrays.asList("RESPINGI","ACCOGLI","CHIUDI_SENZA_COMUNICAZIONE");
			try {
				StoricoWorkflowEntity storicoWf = storicoWorkflowDAO.findFirstStoricoListAzioni(istanza.getIdIstanza(),listAzioni);
				String datiAzioneStorico = storicoWf.getDatiAzione();
				TariAreraEntity entityAzione = TariAreraHelper.parseAzioneAccogliRespingi(datiAzioneStorico);
				dataChiusuraStr = entityAzione.getDataChiusura();
			}
			catch (ItemNotFoundDAOException e) {
				// se non trova azioni passate non fa nulla
			}
			
		    g.writeStringField("dataChiusura", dataChiusuraStr);
		    
		    
		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		    
		} catch(Exception e) {
	    	log.error("[" + CLASS_NAME + "::getDati] Errore - ", e);
	    	throw new BusinessException("ERROR Generica Initilizer getDati");
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
