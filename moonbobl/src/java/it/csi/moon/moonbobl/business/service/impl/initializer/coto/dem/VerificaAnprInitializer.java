/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.initializer.coto.dem;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.initializer.DatiIstanzaInitializer;
import it.csi.moon.moonbobl.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class VerificaAnprInitializer implements DatiIstanzaInitializer {

	private final static String CLASS_NAME = "VerificaAnprInitializer";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	public static final String APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO = "demografia.anpr.clientProfile.cittadino";
	public static final String APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_OPERATORE = "demografia.anpr.clientProfile.operatore";
	
	private AzioneInitParams initParams = null;
	private DatiIstanzaHelper datiIstanzaHelper = null;
	private String codiceFiscaleDaVerificare = null;
	private String codiceModulo = null;
	
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	

	public VerificaAnprInitializer() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}


	@Override
	public String getDati(AzioneInitParams initParams) throws BusinessException {
		try {
			this.initParams = initParams;
			log.info("[" + CLASS_NAME + "::getDati] start");
			IstanzaEntity istanzaE = istanzaDAO.findById(initParams.getIdIstanza());
			ModuloEntity moduloE = moduloDAO.findModuloVersionatoById(istanzaE.getIdModulo(), istanzaE.getIdVersioneModulo());
			this.codiceModulo = moduloE.getCodiceModulo();

			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.setCodec(new ObjectMapper());
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("data"); // data: {
		    
		    // Operatore
		    g.writeObjectFieldStart("operatore"); // operatore: {
		    String codiceFiscaleOpe = initParams.getCodiceFiscale();
		    g.writeStringField("codiceFiscale", codiceFiscaleOpe);
		    g.writeStringField("cognome", initParams.getCognome());
		    g.writeStringField("nome", initParams.getNome());
		    g.writeEndObject(); // } end of operatore
		    
		    // Soggetto Dichiarante
		    log.info("[" + CLASS_NAME + "::getDati] codiceFiscaleVerificato = " + getCodiceFiscaleDaVerificare());
//		    GeneralEntity entity = GeneralHelper.parse(initParams.getDatiIstanza());
//		    String codiceFiscaleVerificato = entity.getCodiceFiscale();
		    
		    // VERIFICA ANPR
		    String esitoVerificaAnpr = "VERIFICA_ANPR_ESITO_NEGATIVO";
		    boolean isResidenteComuneTorino = false;
		    int numComponenti = 0;
		    int numMinori = 0;
		    try {
			    List<ComponenteFamiglia> componenti = moonsrvDAO.getFamigliaANPR(getCodiceFiscaleDaVerificare(), APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_OPERATORE, initParams.getIpAddress(), codiceFiscaleOpe);
			    if (componenti!=null && componenti.size()>0) {
				    esitoVerificaAnpr = "RESIDENTE_IN_TORINO";
				    g.writeStringField("comuneResidenza", "TORINO");
				    isResidenteComuneTorino = true;
				    numComponenti = componenti.size();
				    
					Boolean sonoPresentiMinori = false;
					for (ComponenteFamiglia c : componenti) {
						if (isMinorenne(c)) {
							numMinori++;
					    	sonoPresentiMinori = true;
					    }
					}
			    }
		    } catch (Exception e) {
		    	log.warn("[" + CLASS_NAME + "::getDati] Exception ", e);
			}
		    
		    g.writeStringField("codiceFiscaleVerificato", getCodiceFiscaleDaVerificare());
			g.writeStringField("esitoVerificaAnpr", esitoVerificaAnpr);
		    g.writeBooleanField("isResidenteComuneTorino", isResidenteComuneTorino);
		    g.writeStringField("soggettoTrovatoInAnpr", isResidenteComuneTorino?"S":"N");
			g.writeNumberField("numeroComponenti", numComponenti);
			g.writeNumberField("numeroMinori", numMinori);
		    
		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		    
		} catch(Exception e) {
	    	log.error("[" + CLASS_NAME + "::getDati] Errore - ", e);
	    	throw new BusinessException("ERROR Generica Initilizer VerificaAnprInitializer");
	    }

	}

	
	private boolean isMinorenne(ComponenteFamiglia c) {
		return getAge(c.getDataNascita())<18;
	}
		
	public int getAge(String dataNascita) {
		log.error("[" + CLASS_NAME + "::getAge] dataNascita = " + dataNascita);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		LocalDate dataNascitaLD = LocalDate.parse(dataNascita, formatter);
		return Period.between(
				dataNascitaLD, 
				LocalDateTime.now().toLocalDate() //.truncatedTo(ChronoUnit.DAYS)
			).getYears();
	}


	private String getCodiceFiscaleDaVerificare() {
		if (codiceFiscaleDaVerificare==null) {
			switch(codiceModulo) {
				case "IREN_2023":
					codiceFiscaleDaVerificare = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey("aventeTitolo.codiceFiscale");
					break;
				default:
					codiceFiscaleDaVerificare = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey("codiceFiscale");
			}
		}
		return codiceFiscaleDaVerificare;
	}
	
	//
	//
	private DatiIstanzaHelper getDatiIstanzaHelper() throws BusinessException {
		if (datiIstanzaHelper==null) {
			datiIstanzaHelper = new DatiIstanzaHelper();
			datiIstanzaHelper.initDataNode(initParams.getDatiIstanza());
		}
		return datiIstanzaHelper;
	}

}
