/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.initializer.coto.dem;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonfobl.business.service.impl.initializer.DatiIstanzaInitializer;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;


/**
 * Initializer
 *
 * @author Laurent
 * 
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class VerificaAnprInitializer implements DatiIstanzaInitializer {

	private static final String CLASS_NAME = "VerificaAnprInitializer";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
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
			LOG.info("[" + CLASS_NAME + "::getDati] start");
			IstanzaEntity istanzaE = istanzaDAO.findById(initParams.getIdIstanza());
			ModuloEntity moduloE = moduloDAO.findModuloVersionatoById(istanzaE.getIdModulo(), istanzaE.getIdVersioneModulo());
			this.codiceModulo = moduloE.getCodiceModulo();
			
			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.setCodec(new ObjectMapper());
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("data"); // data: {
		    
		    // Soggetto Dichiarante
		    LOG.info("[" + CLASS_NAME + "::getDati] codiceFiscaleVerificato = " + getCodiceFiscaleDaVerificare());
		    
		    // VERIFICA ANPR
		    String esitoVerificaAnpr = "VERIFICA_ANPR_ESITO_NEGATIVO";
		    boolean isResidenteComuneTorino = false;
		    int numComponenti = 0;
		    int numMinori = 0;
	    	String userJwt = null;
	    	String clientProfileKey = APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_OPERATORE;
	    	String codiceFiscaleOpe = this.initParams.getUser()==null?null:this.initParams.getUser().getCodFiscDichIstanza();
		    try {
		    	if (hasCompilatoPerSeStessoConJwtSPID()) {
		    		userJwt = initParams.getUser().getJwt();
		    		clientProfileKey = APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO;
		    	}
			    List<ComponenteFamiglia> componenti = moonsrvDAO.getFamigliaANPR(getCodiceFiscaleDaVerificare(), userJwt, clientProfileKey, initParams.getIpAddress(), codiceFiscaleOpe);
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
		    	LOG.warn("[" + CLASS_NAME + "::getDati] Exception ", e);
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
	    	LOG.error("[" + CLASS_NAME + "::getDati] Errore - ", e);
	    	throw new BusinessException("ERROR Generica Initilizer VerificaAnprInitializer");
	    }
	}


	private boolean hasCompilatoPerSeStessoConJwtSPID() {
		if (this.initParams.getUser()==null || StringUtils.isBlank(this.initParams.getUser().getJwt()))
			return false;
		if (StringUtils.isNotBlank(this.initParams.getUser().getCodFiscDichIstanza()) &&
				this.initParams.getUser().getCodFiscDichIstanza().equals(getCodiceFiscaleDaVerificare()))
			return true;
		return false;
	}	

	
	private boolean isMinorenne(ComponenteFamiglia c) {
		return getAge(c.getDataNascita())<18;
	}
		
	public int getAge(String dataNascita) {
		LOG.error("[" + CLASS_NAME + "::getAge] dataNascita = " + dataNascita);
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
