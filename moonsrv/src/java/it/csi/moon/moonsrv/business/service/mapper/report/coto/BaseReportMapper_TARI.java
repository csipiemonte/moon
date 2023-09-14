/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.report.coto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

import it.csi.moon.commons.dto.api.IstanzaReport;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaReportDAO;
import it.csi.moon.moonsrv.business.service.mapper.report.BaseReportMapper;
import it.csi.moon.moonsrv.business.service.mapper.report.ReportMapper;

public class BaseReportMapper_TARI extends BaseReportMapper implements ReportMapper{
	
	
	@Autowired
	IstanzaReportDAO istanzaReportDAO;
	
	public static final String NULL_VALUE = "";
	
	protected String codiceEstrazione;
	
	public BaseReportMapper_TARI() {
		// TODO Auto-generated constructor stub
	}
	
	public class EXPORT_VAL {
		public static final String CANALE_COMUNICAZIONE_WEB_AUT = "web autenticato";
		public static final String CANALE_COMUNICAZIONE_AUT_WEB_NO_AUT = "web non autenticato";
		public static final String UTENZA_DOMESTICA = "domestica";
		public static final String UTENZA_NON_DOMESTICA = "nonDomestica";
		public static final String TIP_RICHIESTA_INFORMAZIONI = "informazioni";
		public static final String TIP_RECLAMI = "reclamo";
		public static final String TIP_RICHIESTA_VARIAZIONE = "variazione";
		public static final String TIP_RICHIESTA_ATTIVAZIONE = "attivazione";
		public static final String TIP_RICHIESTA_CESSAZIONE = "cessazione";		
	}

	@Override
	public String getHeader() {	
		
		StringBuilder sb = new StringBuilder();
		sb.append("N. Ticket OTRS").append(CSV_SEPARATOR);
		sb.append("N. Istanza Moon").append(CSV_SEPARATOR);
		sb.append("N. Protocollo").append(CSV_SEPARATOR);
		sb.append("Canale di comunicazione").append(CSV_SEPARATOR);
		sb.append("Cognome").append(CSV_SEPARATOR);
		sb.append("Nome").append(CSV_SEPARATOR);
		sb.append("Ragione Sociale (Denominazione)").append(CSV_SEPARATOR);
		sb.append("Codice Utente").append(CSV_SEPARATOR);
		sb.append("Indirizzo Email").append(CSV_SEPARATOR);		
		sb.append("Tipologia Utenza").append(CSV_SEPARATOR);	
		sb.append("Tipologia Richiesta").append(CSV_SEPARATOR);	
		sb.append("Codice Utenza").append(CSV_SEPARATOR);	
		sb.append("Data Richiesta").append(CSV_SEPARATOR);	
		sb.append("Data Chiusura").append(CSV_SEPARATOR);	
		sb.append("Data invio istanza").append(CSV_SEPARATOR);	
		sb.append("Data ultimo invio integrazione").append(CSV_SEPARATOR);		
		sb.append("Causa del mancato rispetto dello Standard di Qualità").append(CSV_SEPARATOR);		
		sb.append("Causa del mancato Obbligo di Risposta").append(CSV_SEPARATOR);		
		sb.append("Nome Operatore").append(CSV_SEPARATOR);		
		sb.append("Note SemplificatoBO").append(CSV_SEPARATOR);		
		sb.append("Note di lavorazione").append(CSV_SEPARATOR);	
		
		return sb.toString();
	}
	
	@Override
	public String remapIstanza(IstanzaReport istanza) throws Exception {
		return null;
	}
	
	@Override
	public StreamingOutput getStreamingOutput(Long idModulo,Date dataDa, Date dataA) {
		IstanzeFilter filter = new IstanzeFilter();			
		return new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(os))) {						
					writer.write(getHeader());
					writer.write('\n');										
					int n = istanzaReportDAO.findForApiReport(codiceEstrazione, idModulo, dataDa, dataA, istanza -> {	
						try {
//							writer.write(remapIstanza(istanza));
							// replace all new line / carriage return
							writer.write(remapIstanza(istanza).replaceAll("[\\n\\r]", " "));
							writer.write('\n');														
						} catch (Exception e) {
							LOG.error("[ReportMapperDefault::getStreamingOutput()] Errore findForApiReport id_istanza = " + istanza.getIdIstanza() + e.getMessage(),e);			
						}
					});						
				}
			}
		};
	}
	

	public String getCodiceUtente() {
		String codiceUtente = NULL_VALUE;
		String tipoPersona = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$" + root + ".dichiarante.tipoPersona"));

		switch (tipoPersona) {
		case "personaFisica":
			codiceUtente = jsonPathUtil
					.getStringValue(jsonPathUtil.getValue("$" + root + ".dichiarante.personaFisica.cf"));
			break;
		case "personaGiuridica":
			codiceUtente = jsonPathUtil
					.getStringValue(jsonPathUtil.getValue("$" + root + ".dichiarante.personaGiuridica.cf"));
			break;
		default:
		}
		return codiceUtente;
	}
		
	public String getCognome() {
		String cognome = NULL_VALUE;
		String tipoPersona = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$" + root + ".dichiarante.tipoPersona"));

		switch (tipoPersona) {
		case "personaFisica":
			cognome = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".dichiarante.personaFisica.cognome"));
			break;
		case "personaGiuridica":
			cognome = NULL_VALUE;
			break;
		default:
		}
		return cognome;
	}
	
	public String getNome() {
		String nome = null;
		String tipoPersona = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$" + root + ".dichiarante.tipoPersona"));

		switch (tipoPersona) {
		case "personaFisica":
			nome = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".dichiarante.personaFisica.nome"));
			break;
		case "personaGiuridica":
			nome = NULL_VALUE;
			break;
		default:
		}
		return nome;
	}
	
	public String getRagioneSociale() {
		String ragioneSociale = null;
		String tipoPersona = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$" + root + ".dichiarante.tipoPersona"));

		switch (tipoPersona) {
		case "personaFisica":
			ragioneSociale = NULL_VALUE;
			break;
		case "personaGiuridica":
			ragioneSociale = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".dichiarante.personaGiuridica.ragionesociale"));
			break;
		default:
		}
		return ragioneSociale;
	}
	
//	public String getCodiceUtenzaDomestica() {
//		String utenzaDomestica = null;
//		
//		String utenzaDomesticaAbitazione = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".abitazione.codiceUtenza"));
//		String utenzaDomesticaCantina = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".cantina.codiceUtenza"));
//		String utenzaDomesticaSoffitta = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".soffitta.codiceUtenza"));
//		String utenzaDomesticaBox = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".box.codiceUtenza"));
//		
//		utenzaDomestica = (utenzaDomesticaAbitazione != null)?
//				utenzaDomesticaAbitazione :					
//				(utenzaDomesticaCantina!= null) ? 
//						utenzaDomesticaCantina:
//				(utenzaDomesticaSoffitta != null ) ? 
//						utenzaDomesticaSoffitta:
//				(utenzaDomesticaBox != null)?
//						utenzaDomesticaBox: null;
//		return utenzaDomestica;
//	}
	
	public String getCodiceUtenzaDomestica() {
		String utenzaDomestica = NULL_VALUE;
		
		Boolean isAbitazione = (Boolean) jsonPathUtil.getValue("$"+root+".dichiarazioni.tipoLocali.abitazione");
		Boolean isCantina = (Boolean) jsonPathUtil.getValue("$"+root+".dichiarazioni.tipoLocali.cantina");
		Boolean isSoffitta = (Boolean) jsonPathUtil.getValue("$"+root+".dichiarazioni.tipoLocali.soffitta");
		Boolean isBox = (Boolean) jsonPathUtil.getValue("$"+root+".dichiarazioni.tipoLocali.box");
		
		if(isBox) {
			utenzaDomestica = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".box.codiceUtenza"));
		}
		if(isSoffitta) {
			utenzaDomestica = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".soffitta.codiceUtenza"));
		}
		if(isCantina) {
			utenzaDomestica = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".cantina.codiceUtenza"));
		}				
		if(isAbitazione) {
			utenzaDomestica = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".abitazione.codiceUtenza"));
		}

		return utenzaDomestica;
	}	
	
	
//	public String getCodiceUtenzaNonDomestica() {
//		String utenzaNonDomestica = null;
//		
//		String utenzaNonDomesticaAttPrevalente = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".attivitaPrevalente.codiceUtenza"));
//		String utenzaNonDomesticaAccessori = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".accessori.codiceUtenza"));
//		String utenzaNonDomesticaBox = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".box.codiceUtenza"));
//		utenzaNonDomestica = (utenzaNonDomesticaAttPrevalente != null)?
//				utenzaNonDomesticaAttPrevalente:					
//				(utenzaNonDomesticaAccessori != null) ? 
//						utenzaNonDomesticaAccessori:
//				(utenzaNonDomesticaBox != null ) ? 
//						utenzaNonDomesticaBox: null;
//		return utenzaNonDomestica;
//	}
//	
	
	public String getCodiceUtenzaNonDomestica() {
		String utenzaNonDomestica = NULL_VALUE;
		
		Boolean isAttivitaPrevalente = (Boolean) jsonPathUtil.getValue("$"+root+".dichiarazioni.tipoLocali.attivitaPrevalente");
		Boolean isAccessori = (Boolean) jsonPathUtil.getValue("$"+root+".dichiarazioni.tipoLocali.accessori");
		Boolean isBox = (Boolean) jsonPathUtil.getValue("$"+root+".dichiarazioni.tipoLocali.box");
		
		if(isBox) {
			utenzaNonDomestica = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".box.codiceUtenza"));
		}
		if(isAccessori) {
			utenzaNonDomestica = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".accessori.codiceUtenza"));
		}				
		if(isAttivitaPrevalente) {
			utenzaNonDomestica = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".attivitaPrevalente.codiceUtenza"));
		}

		return utenzaNonDomestica;
	}

	
	public String getCodiceUtenza(IstanzaReport istanza, String tipoUtenza) {
		
        String codiceUtenza = NULL_VALUE;
        
        // check dati azione if present
//    	if (istanza.getDatiAzione() != null) {
//    		// set dati azione
//			setJsonPathUtil(istanza.getDatiAzione().toString());
//			codiceUtenza = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".codiceUtenza"));
//		}
//    	else {
    		// set dati istanza
//    		setJsonPathUtil(istanza.getData().toString());
//            if (StringUtils.isBlank(tipoUtenza)) {
//            	codiceUtenza =  jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".codiceUtenza"));
//            }
//            else {            	
//            	if (tipoUtenza.equals(EXPORT_VAL.UTENZA_DOMESTICA)) {
//            		codiceUtenza = getCodiceUtenzaDomestica();
//            	}
//            	else if (tipoUtenza.equals(EXPORT_VAL.UTENZA_NON_DOMESTICA)) {
//            		codiceUtenza = getCodiceUtenzaNonDomestica();        	
//            	}        	
//            }
    		
			JsonNode istanzaJsonNode;
			try {
				String indirizzoCompleto = "";
				istanzaJsonNode = readData(istanza.getData().toString());
				JsonNode data = istanzaJsonNode.get("data");
				
				if(data.has("dichiarazioni")) {	
					JsonNode dichiarazioni = data.get("dichiarazioni");
					if(dichiarazioni.has("tipoLocali")) {	
						JsonNode tipoLocali = dichiarazioni.get("tipoLocali");
						JsonNode locale = null;			
						
						if ( (tipoLocali.has("attivitaPrevalente") && tipoLocali.get("attivitaPrevalente").asBoolean()) || 
							 (tipoLocali.has("abitazione") && tipoLocali.get("abitazione").asBoolean()) ) {
							if ( tipoLocali.has("attivitaPrevalente") && tipoLocali.get("attivitaPrevalente").asBoolean()) {
								locale = data.get("attivitaPrevalente");
							}
							if (tipoLocali.has("abitazione") && tipoLocali.get("abitazione").asBoolean() ) {
								locale = data.get("abitazione");
							}
						}
						else {
							if (tipoLocali.has("accessori") && tipoLocali.get("accessori").asBoolean()) {
								locale = data.get("accessori");
							}
							else {
								if (tipoLocali.has("cantina") && tipoLocali.get("cantina").asBoolean()) {
									locale = data.get("cantina");
								}
								else {
									if (tipoLocali.has("soffitta") && tipoLocali.get("soffitta").asBoolean()) {
										locale = data.get("soffitta");
									}
									else {
										if (tipoLocali.has("box") && tipoLocali.get("box").asBoolean()) {
											locale = data.get("box");
										}
									}
								}
							}
						}	
						
						if (locale != null) {
							JsonNode indirizzo = null;
							if (locale.has("abitazionePrincipale")) {
								JsonNode abitazionePrincipale = locale.get("abitazionePrincipale");
								indirizzo = abitazionePrincipale.get("indirizzo");
							}
							else {
								JsonNode immobile = locale.get("immobile");
								indirizzo = immobile.get("indirizzo");
							}
							if (indirizzo != null) {
								if (indirizzo.has("civicoNonInElenco") && indirizzo.get("civicoNonInElenco").asBoolean()) {
								
									String nomeVia = (indirizzo.has("via") && indirizzo.get("via").has("nome")) ? 
											indirizzo.get("via").get("nome").asText() : "";
									String civico = (indirizzo.has("civicoManuale")) ? 
											indirizzo.get("civicoManuale").asText() : "";
									indirizzoCompleto = nomeVia + " " + civico;
								}
								else {
									String nomeVia = (indirizzo.has("civicocompleto") && indirizzo.get("civicocompleto").has("nome")) ? 
											indirizzo.get("civicocompleto").get("nome").asText() : "";
									String pianoNui = (indirizzo.has("pianoNui") && indirizzo.get("pianoNui").has("nome")) ? 
											indirizzo.get("pianoNui").get("nome").asText() : "";
									String piano = (indirizzo.has("piano") && indirizzo.get("piano").has("nome")) ? 
											indirizzo.get("piano").get("nome").asText() : "";
									indirizzoCompleto = nomeVia + " " + pianoNui + piano;
								}
							}
						}
					}
					codiceUtenza = indirizzoCompleto;
				}
				else {
					// caso modulo da sportello
					JsonNode indirizzo = data.get("indirizzo");
					if (indirizzo.has("civicoNonInElenco") &&  indirizzo.get("civicoNonInElenco").asBoolean()) {
					
						String nomeVia = (indirizzo.has("via") && indirizzo.get("via").has("nome")) ? 
								indirizzo.get("via").get("nome").asText() : "";
						String civico = (indirizzo.has("civicoManuale")) ? 
								indirizzo.get("civicoManuale").asText() : "";
						indirizzoCompleto = nomeVia + " " + civico;
					}
					else {
						String nomeVia = (indirizzo.has("civicocompleto") && indirizzo.get("civicocompleto").has("nome")) ? 
								indirizzo.get("civicocompleto").get("nome").asText() : "";
						String pianoNui = (indirizzo.has("pianoNui") && indirizzo.get("pianoNui").has("nome")) ? 
								indirizzo.get("pianoNui").get("nome").asText() : "";
						String piano = (indirizzo.has("piano") && indirizzo.get("piano").has("nome")) ? 
								indirizzo.get("piano").get("nome").asText() : "";
						indirizzoCompleto = nomeVia + " " + pianoNui + piano;
					}
					codiceUtenza = indirizzoCompleto;
				}	

			} catch (Exception e) {
				LOG.error("[ReportMapperDefault::getCodiceUtenza] Errore findForApiReport id_istanza=" + istanza.getIdIstanza() + "  tipoUtenza=" + tipoUtenza + "  " + e.getMessage(),e);	
			}
		//}

		// reset parser to dati istanza
		setJsonPathUtil(istanza.getData().toString());
		return codiceUtenza;
	}

	
	
}
