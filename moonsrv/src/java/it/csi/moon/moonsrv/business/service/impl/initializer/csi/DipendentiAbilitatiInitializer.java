/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.initializer.csi;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.moonsrv.business.service.dto.IstanzaInitCompletedParams;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.csi.DipendentiCsiAbilitatiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.DipendentiCsiEntity;
import it.csi.moon.moonsrv.business.service.impl.initializer.DatiIstanzaInitializer;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Initializer acquisire i dati del dipendente e 
 * per verificare se è abilitato alla compilazione del modulo 
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
// @Component inutile se richiamato con Reflection
public class DipendentiAbilitatiInitializer implements DatiIstanzaInitializer {

	private static final String CLASS_NAME = "DipendentiAbilitatiInitializer";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String ERROR_MESSAGE_NON_ABILITATO = "Il dipendente indicato non risulta presente nell'elenco dei soggetti abilitati alla compilazione.";


	@Autowired
	private DipendentiCsiAbilitatiDAO dipendentiCsiAbilitatiDAO;

	public DipendentiAbilitatiInitializer() {
		super();
		//must provide autowiring support to inject SpringBean
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);  
	}


	@Override
	public String getDatiIstanza(IstanzaInitCompletedParams completedParams, ModuloVersionatoEntity modulo) throws BusinessException {
		IstanzaInitParams initParams = completedParams.getIstanzaInitParams();
		LOG.info("[" + CLASS_NAME + "::getDatiIstanza] start");
		try {
			boolean contoTerzi = false;
			String codiceFiscale = initParams.getCodiceFiscale();
			if (initParams.getBlParams()!=null && !StringUtils.isEmpty(initParams.getBlParams().getCodiceFiscale())) {							
				LOG.info("[" + CLASS_NAME + "::getDatiIstanza ] codiceFiscale ("+initParams.getBlParams().getCodiceFiscale()+") ...");
				codiceFiscale = initParams.getBlParams().getCodiceFiscale();
				contoTerzi = true;
			}
			LOG.info("[" + CLASS_NAME + "::getDatiIstanza] codiceFiscale("+initParams.getCodiceFiscale()+") ...");
			
			/*
			 * Verifica se l'utente è abilitato
			 */
			List<DipendentiCsiEntity> listAbilitati = null;
			try {
				listAbilitati  = dipendentiCsiAbilitatiDAO.findAbilitati(modulo.getIdModulo(), codiceFiscale);
			}
			catch(Exception e) {
				LOG.error("[" + CLASS_NAME + "::getDatiIstanza] Errore - ", e);
				throw new BusinessException("Errore su ricerca utenti abilitati");
			}
			DipendentiCsiEntity soggetto = null;
			if (listAbilitati != null && listAbilitati.size() > 0) {
				for (DipendentiCsiEntity elemento : listAbilitati) {
					if (codiceFiscale.equals(elemento.getCodiceFiscale())) {
						soggetto = elemento;
					}
				}
			}
			else {
				LOG.error("[" + CLASS_NAME + "::getDatiIstanza] Errore - Elenco soggetti abilitati vuoto");
				throw new BusinessException("Attenzione: utente non abilitato alla compilazione di questo modulo");
			}
			if (soggetto == null) {
				throw new BusinessException(ERROR_MESSAGE_NON_ABILITATO);
			}
			
			JsonFactory jfactory = new JsonFactory();
			Writer writer = new StringWriter();
			JsonGenerator g = jfactory.createJsonGenerator(writer);
			g.writeStartObject(); // {
			g.writeObjectFieldStart("data"); // data: {
				g.writeObjectFieldStart("richiedente"); // richiedente: {
					g.writeStringField("codiceFiscale", soggetto.getCodiceFiscale()); 
					g.writeStringField("cognome", soggetto.getCognome());
					g.writeStringField("nome", soggetto.getNome()); 
					g.writeStringField("matricola", soggetto.getMatricola());
					
					g.writeStringField("responsabileFunzioneDiretta", soggetto.getResp_funz_diretta());
					g.writeStringField("fo1Liv", soggetto.getFo_i_liv());
					g.writeStringField("fo2Liv", soggetto.getFo_ii_liv());
					g.writeStringField("foDiretta", soggetto.getFunzione_diretta());
					
					g.writeStringField("email", soggetto.getEmail());
					g.writeStringField("indirizzo", soggetto.getIndirizzo());
					
				g.writeEndObject(); // } end of richiedente	    
				if (modulo.getCodiceModulo().equals("AGL_2023"))
				{
					g.writeObjectFieldStart("richiesta");
						g.writeNumberField("giorniRichiestaPrecedente", soggetto.getGg_aggiuntivi());
					g.writeEndObject(); // } end of richiesta
				}
				if (modulo.getCodiceModulo().equals("CSI_PART_TIME"))
				{
					g.writeObjectFieldStart("situazioneAttuale");
						g.writeStringField("categoria", soggetto.getCategoria());
						g.writeStringField("tipoPartTime", soggetto.getTipoOrario());
						if (soggetto.getTipoOrario().equals("M") || soggetto.getTipoOrario().equals("O") ||
								soggetto.getTipoOrario().equals("V") ) {
							g.writeStringField("tipoDiLavoro", "part-time");
						}
						else {
							g.writeStringField("tipoDiLavoro", "full-time");
						}
						g.writeStringField("numeroDiOre", soggetto.getNumeroOrePartTime());
						g.writeStringField("profiloOrario", soggetto.getProfiloOrarioPartTime());
						g.writeStringField("legge104", soggetto.getLegge104());
						g.writeStringField("certmedico", soggetto.getCertmedico());
						
					g.writeEndObject(); // } end of situazioneAttuale
				}
				if (modulo.getCodiceModulo().equals("CSI_TELELAV"))
				{
					g.writeObjectFieldStart("situazioneAttuale");
						String tipoDiLavoro = "noTelelavoro";
						if (soggetto.getTelelavoro().equals("si")) {
							tipoDiLavoro = "telelavoro";
						}
						g.writeStringField("tipoDiLavoro", tipoDiLavoro);
						
						if (soggetto.getTelelavoro().equals("si")) {
							String str = soggetto.getGiornitelelavoro();
							g.writeStringField("valoreOriginale", soggetto.getGiornitelelavoro());
							g.writeObjectFieldStart("giorniTelelavoro");
							for (int i = 0;i < 5; i++){
								String valore = Character.toString(str.charAt(i));
								boolean flag_telelav = false;
								if (valore.equals("0")) {
									flag_telelav = true;
								}
								if (i==0) {g.writeBooleanField("lun", flag_telelav); }
								if (i==1) {g.writeBooleanField("mar", flag_telelav); }
								if (i==2) {g.writeBooleanField("mer", flag_telelav); }
								if (i==3) {g.writeBooleanField("gio", flag_telelav); }
								if (i==4) {g.writeBooleanField("ven", flag_telelav); }

							}
							g.writeEndObject(); // } end of giorniTelelavoro
						}
					g.writeEndObject(); // } end of situazioneAttuale
				}
			g.writeEndObject(); // } end of data
			g.writeEndObject();
			g.close();
			LOG.info("[" + CLASS_NAME + "::getDatiIstanza] ATTENZIONE - JSON CREATO:\n " + writer.toString() );
			return writer.toString();

		} catch(BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getDatiIstanza] Errore - ", e);
			throw e;
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getDatiIstanza] Errore - ", e);
			throw new BusinessException("ERROR Generica Initilizer ");
		}

	}


}
