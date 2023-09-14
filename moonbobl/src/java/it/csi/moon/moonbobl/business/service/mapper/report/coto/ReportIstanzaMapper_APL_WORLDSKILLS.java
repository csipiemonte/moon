/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper.report.coto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaEstrattoreDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.EntiFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapper;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapperDefault;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaEstratta;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Contruttore di oggetto JSON
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class ReportIstanzaMapper_APL_WORLDSKILLS extends ReportIstanzaMapperDefault implements ReportIstanzaMapper {
	
	private final static String CLASS_NAME = "ReportIstanzaMapper_APL_WORLDSKILLS";
	private final static Logger log = LoggerAccessor.getLoggerBusiness();
	private static final char CSV_SEPARATOR = ';';
	

	@Autowired
	EnteDAO enteDAO;
	
	@Autowired
	IstanzaEstrattoreDAO istanzaEstrattoreDAO;
	

	public String getHeader() {

		StringBuilder sb = new StringBuilder();
		sb.append("Mestiere").append(CSV_SEPARATOR);	
		sb.append("Nome").append(CSV_SEPARATOR);	
		sb.append("Cognome").append(CSV_SEPARATOR);	
		sb.append("Istituto/Impresa").append(CSV_SEPARATOR);	
		sb.append("E-mail").append(CSV_SEPARATOR);	
		sb.append("Tel./Cell").append(CSV_SEPARATOR);	
		sb.append("Indirizzo (Comp)").append(CSV_SEPARATOR);	
		sb.append("Provincia").append(CSV_SEPARATOR);	
		sb.append("Data nascita").append(CSV_SEPARATOR);	
		sb.append("Cod. Fiscale").append(CSV_SEPARATOR);	
		sb.append("T-Shirt").append(CSV_SEPARATOR);	
		sb.append("pantalone").append(CSV_SEPARATOR);	
		sb.append("Esigenze Alimentari").append(CSV_SEPARATOR);	
		sb.append("ospitality compe").append(CSV_SEPARATOR);
		sb.append("Riserva").append(CSV_SEPARATOR);	
		sb.append("Mail").append(CSV_SEPARATOR);	
		sb.append("Tel").append(CSV_SEPARATOR);	
		sb.append("Referente/Direttore").append(CSV_SEPARATOR);	
		sb.append("mail referente").append(CSV_SEPARATOR);	
		sb.append("Tutor").append(CSV_SEPARATOR);	
		sb.append("Indirizzo").append(CSV_SEPARATOR);	
		sb.append("prov").append(CSV_SEPARATOR);	
		sb.append("Tel./Cell.").append(CSV_SEPARATOR);	
		sb.append("Email").append(CSV_SEPARATOR);	
		sb.append("Hospitality Tutor").append(CSV_SEPARATOR);
		return sb.toString();

	}
	
	public String remapIstanza(IstanzaEstratta istanza) throws Exception {
		final String DEFAULT_VALUE = "";
		final DateFormat fDataOra = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

		log.debug("[" + CLASS_NAME + "::remap id istanza]  - " + istanza.getIdIstanza());
		
		StringBuilder row = new StringBuilder();
		

		String istitutoImpresa = "";
		
		String datastr = istanza.getData().toString();
		JsonNode istanzaJsonNode = readIstanzaData(datastr);

		JsonNode data = istanzaJsonNode.get("data");
		ArrayList<String> listCompetitor = new ArrayList<String>();
		ArrayList<String> listTutor = new ArrayList<String>();
		
		if (data.has("richiedente")) {
			JsonNode richiedente = data.get("richiedente");
			if (richiedente.has("ente")) {
				JsonNode ente = richiedente.get("ente");
				istitutoImpresa = ente.get("denominazione").getTextValue();
			}		
		}
		String nomeReferente = (data.has("nome")) ? data.get("nome").getTextValue() : "";
		String cognomeReferente = (data.has("cognome")) ? data.get("cognome").getTextValue() : "";
		String referente = nomeReferente + " " + cognomeReferente;
		String referenteMail = (data.has("email")) ? data.get("email").getTextValue() : "";

		if (data.has("elencoCompetitor")) {
			ArrayNode elencoCompetitor = (ArrayNode)data.get("elencoCompetitor");
			Iterator<JsonNode>it = elencoCompetitor.iterator();
			int num = 1;
			while (it.hasNext()) {
				StringBuilder rowCompetitor1 = new StringBuilder();
				StringBuilder rowCompetitor2 = new StringBuilder();
				StringBuilder riserva1 = new StringBuilder();
				StringBuilder riserva2 = new StringBuilder();
				StringBuilder tutorString = new StringBuilder();
				JsonNode componente = it.next();
				Boolean teamCompetition = false;
				String mestiere = (componente.has("mestiere") && componente.get("mestiere").has("nome")) ? 
						componente.get("mestiere").get("nome").getTextValue() : "";
				if (componente.has("competitor1")) {
					JsonNode competitor1 = componente.get("competitor1");
					String nome = (competitor1.has("nome")) ? competitor1.get("nome").getTextValue() : "";
					String cognome = (competitor1.has("cognome")) ? competitor1.get("cognome").getTextValue() : "";
					String email = (competitor1.has("email")) ? competitor1.get("email").getTextValue() : "";
					String tel = (competitor1.has("telefono")) ? competitor1.get("telefono").getTextValue() : "";
					String dataNascita = (competitor1.has("dataNascita")) ? competitor1.get("dataNascita").getTextValue() : "";
					String codFiscale = (competitor1.has("codiceFiscale")) ? competitor1.get("codiceFiscale").getTextValue() : "";
					String pantalone = (competitor1.has("pantaloni")) ? competitor1.get("pantaloni").asText() : "";
					String tShirt = (competitor1.has("tshirt")) ? competitor1.get("tshirt").getTextValue() : "";
					
					String esigenzeAlimentariCompetitor1  = (competitor1.has("esigenze")) ? competitor1.get("esigenze").asText() : "";
					String hospitalityCompetitor1  = (competitor1.has("hospitality") && competitor1.get("hospitality").asBoolean()) ? 
							"si" : "no";
					String indirizzo = "";
					String provincia = "";
					if (competitor1.has("residenza")) {
						JsonNode residenza = competitor1.get("residenza");
						String statoRes = (residenza.has("stato") && residenza.get("stato").has("nome")) ? 
								residenza.get("stato").get("nome").getTextValue() : "";
						String comune = "";
						String via = (residenza.has("indirizzo")) ? residenza.get("indirizzo").getTextValue() : "";
						String civico = (residenza.has("civico")) ? residenza.get("civico").getTextValue() : "";
						String cap = (residenza.has("cap")) ? residenza.get("cap").getTextValue() : "";
						
						if (statoRes.equalsIgnoreCase("ITALIA")) {
							comune = (residenza.has("comune") && residenza.get("comune").has("nome")) ? 
									residenza.get("comune").get("nome").getTextValue() : "";
							provincia = (residenza.has("provincia") && residenza.get("provincia").has("nome")) ? 
									residenza.get("provincia").get("nome").getTextValue() : "";
						}else {
							comune = (residenza.has("citta")) ? residenza.get("citta").getTextValue() : "";
						}
						indirizzo = via + " " + civico + " "+ cap + " "+ comune;
					}
					
					rowCompetitor1.append(mestiere).append(CSV_SEPARATOR);
					rowCompetitor1.append(nome).append(CSV_SEPARATOR);	
					rowCompetitor1.append(cognome).append(CSV_SEPARATOR);	
					rowCompetitor1.append(istitutoImpresa).append(CSV_SEPARATOR);	
					rowCompetitor1.append(email).append(CSV_SEPARATOR);	
					rowCompetitor1.append(" "+tel).append(CSV_SEPARATOR);	
					rowCompetitor1.append(indirizzo).append(CSV_SEPARATOR);	
					rowCompetitor1.append(provincia).append(CSV_SEPARATOR);	
					rowCompetitor1.append(dataNascita).append(CSV_SEPARATOR);	
					rowCompetitor1.append(codFiscale).append(CSV_SEPARATOR);	
					rowCompetitor1.append(tShirt).append(CSV_SEPARATOR);	
					rowCompetitor1.append(pantalone).append(CSV_SEPARATOR);	
					rowCompetitor1.append(esigenzeAlimentariCompetitor1).append(CSV_SEPARATOR);
					rowCompetitor1.append(hospitalityCompetitor1).append(CSV_SEPARATOR);
					
				}
				if (componente.has("competitor2")) {
					JsonNode competitor2 = componente.get("competitor2");
					String nome = (competitor2.has("nome")) ? competitor2.get("nome").getTextValue() : "";
					String cognome = (competitor2.has("cognome")) ? competitor2.get("cognome").getTextValue() : "";
					String email = (competitor2.has("email")) ? competitor2.get("email").getTextValue() : "";
					String tel = (competitor2.has("telefono")) ? competitor2.get("telefono").getTextValue() : "";
					String dataNascita = (competitor2.has("dataNascita")) ? competitor2.get("dataNascita").getTextValue() : "";
					String codFiscale = (competitor2.has("codiceFiscale")) ? competitor2.get("codiceFiscale").getTextValue() : "";
					if (codFiscale != "") {
						teamCompetition = true;
					}
					String pantalone = (competitor2.has("pantaloni")) ? competitor2.get("pantaloni").getTextValue() : "";
					String tShirt = (competitor2.has("tshirt")) ? competitor2.get("tshirt").getTextValue() : "";
					String esigenzeAlimentariCompetitor2  = (competitor2.has("esigenze")) ? competitor2.get("esigenze").asText() : "";
					String hospitalityCompetitor2  = (competitor2.has("hospitality") && competitor2.get("hospitality").asBoolean()) ? 
							"si" : "no";
					String indirizzo = "";
					String provincia = "";
					if (competitor2.has("residenza")) {
						JsonNode residenza = competitor2.get("residenza");
						String statoRes = (residenza.has("stato") && residenza.get("stato").has("nome")) ? 
								residenza.get("stato").get("nome").getTextValue() : "";
						String comune = "";
						String via = (residenza.has("indirizzo")) ? residenza.get("indirizzo").getTextValue() : "";
						String civico = (residenza.has("civico")) ? residenza.get("civico").getTextValue() : "";
						String cap = (residenza.has("cap")) ? residenza.get("cap").getTextValue() : "";
						
						if (statoRes.equalsIgnoreCase("ITALIA")) {
							comune = (residenza.has("comune") && residenza.get("comune").has("nome")) ? 
									residenza.get("comune").get("nome").getTextValue() : "";
							provincia = (residenza.has("provincia") && residenza.get("provincia").has("nome")) ? 
									residenza.get("provincia").get("nome").getTextValue() : "";
						}else {
							comune = (residenza.has("citta")) ? residenza.get("citta").getTextValue() : "";
						}
						indirizzo = via + " " + civico + " "+ cap + " "+ comune;
					}
					
					rowCompetitor2.append(mestiere).append(CSV_SEPARATOR);
					rowCompetitor2.append(nome).append(CSV_SEPARATOR);	
					rowCompetitor2.append(cognome).append(CSV_SEPARATOR);	
					rowCompetitor2.append(istitutoImpresa).append(CSV_SEPARATOR);	
					rowCompetitor2.append(email).append(CSV_SEPARATOR);	
					rowCompetitor2.append(" "+tel).append(CSV_SEPARATOR);	
					rowCompetitor2.append(indirizzo).append(CSV_SEPARATOR);	
					rowCompetitor2.append(provincia).append(CSV_SEPARATOR);	
					rowCompetitor2.append(dataNascita).append(CSV_SEPARATOR);	
					rowCompetitor2.append(codFiscale).append(CSV_SEPARATOR);	
					rowCompetitor2.append(tShirt).append(CSV_SEPARATOR);	
					rowCompetitor2.append(pantalone).append(CSV_SEPARATOR);	
					rowCompetitor2.append(esigenzeAlimentariCompetitor2).append(CSV_SEPARATOR);
					rowCompetitor2.append(hospitalityCompetitor2).append(CSV_SEPARATOR);
					
				}
				if (componente.has("riserva1")) {
					JsonNode riserva = componente.get("riserva1");
					String nomeRiserva1 = (riserva.has("nome")) ? riserva.get("nome").getTextValue() : "";
					String cognomeRiserva1 = (riserva.has("cognome")) ? riserva.get("cognome").getTextValue() : "";
					String nominativoRiserva1 = nomeRiserva1 + " " + cognomeRiserva1;
					String emailRiserva1 = (riserva.has("email")) ? riserva.get("email").getTextValue() : "";
					String telRiserva1 = (riserva.has("telefono")) ? riserva.get("telefono").getTextValue() : "";
					
					riserva1.append(nominativoRiserva1).append(CSV_SEPARATOR);
					riserva1.append(emailRiserva1).append(CSV_SEPARATOR);	
					riserva1.append(" "+telRiserva1).append(CSV_SEPARATOR);	
				}
				
				if (componente.has("riserva2")) {
					JsonNode riserva = componente.get("riserva2");
					String nomeRiserva2 = (riserva.has("nome")) ? riserva.get("nome").getTextValue() : "";
					String cognomeRiserva2 = (riserva.has("cognome")) ? riserva.get("cognome").getTextValue() : "";
					String nominativoRiserva2 = nomeRiserva2 + " " + cognomeRiserva2;
					String emailRiserva2 = (riserva.has("email")) ? riserva.get("email").getTextValue() : "";
					String telRiserva2 = (riserva.has("telefono")) ? riserva.get("telefono").getTextValue() : "";
					
					riserva2.append(nominativoRiserva2).append(CSV_SEPARATOR);
					riserva2.append(emailRiserva2).append(CSV_SEPARATOR);	
					riserva2.append(" "+telRiserva2).append(CSV_SEPARATOR);	
				}
				
				if (componente.has("tutor")) {
					JsonNode tutor = componente.get("tutor");
					String nomeTutor = (tutor.has("nome")) ? tutor.get("nome").getTextValue() : "";
					String cognomeTutor = (tutor.has("cognome")) ? tutor.get("cognome").getTextValue() : "";
					String tutorNome = nomeTutor + " " + cognomeTutor;
					
					String via = (tutor.has("indirizzo")) ? tutor.get("indirizzo").getTextValue() : "";
					String civico = (tutor.has("civico")) ? tutor.get("civico").getTextValue() : "";
					String cap = (tutor.has("cap")) ? tutor.get("cap").getTextValue() : "";
					String comune = (tutor.has("comune") && tutor.get("comune").has("nome")) ? 
								tutor.get("comune").get("nome").getTextValue() : "";
					String provincia = (tutor.has("provincia") && tutor.get("provincia").has("nome")) ? 
								tutor.get("provincia").get("nome").getTextValue() : "";
					String indirizzo = via + " " + civico + " "+ cap + " "+ comune;
					String tutorEmail = (tutor.has("email")) ? tutor.get("email").getTextValue() : "";
					String tutorTel = (tutor.has("telefono")) ? tutor.get("telefono").getTextValue() : "";
					String hospitalityTutor  = (tutor.has("hospitality") && tutor.get("hospitality").asBoolean()) ? "si" : "no";
					
					tutorString.append(tutorNome).append(CSV_SEPARATOR);
					tutorString.append(indirizzo).append(CSV_SEPARATOR);	
					tutorString.append(provincia).append(CSV_SEPARATOR);	
					tutorString.append(" "+tutorTel).append(CSV_SEPARATOR);
					tutorString.append(tutorEmail).append(CSV_SEPARATOR);
					tutorString.append(hospitalityTutor).append(CSV_SEPARATOR);
				}
				
				// per ogni mestiere registro un record
				StringBuilder strIniziale = new StringBuilder();
				strIniziale.append(rowCompetitor1);
				strIniziale.append(riserva1);
				strIniziale.append(referente).append(CSV_SEPARATOR);
				strIniziale.append(referenteMail).append(CSV_SEPARATOR);
				strIniziale.append(tutorString);
				listCompetitor.add(strIniziale.toString());
				
				if (teamCompetition) {
					StringBuilder strIniziale2 = new StringBuilder();
					strIniziale2.append(rowCompetitor2);
					strIniziale2.append(riserva2);
					strIniziale2.append(referente).append(CSV_SEPARATOR);
					strIniziale2.append(referenteMail).append(CSV_SEPARATOR);
					strIniziale2.append(tutorString);
					listCompetitor.add(strIniziale2.toString());
				}
				
			}
			
		}
		
		if (listCompetitor.size() > 0) {
			for(int i = 0; i < listCompetitor.size(); i++) {  
				row.append(listCompetitor.get(i));
				if (i < listCompetitor.size()-1) {
					row.append("\n");
				}
			}  		
		}
		
		return row.toString();
		
		
		/*
		if (data.has("elencoTutor")) {
			ArrayNode elencoTutor = (ArrayNode)data.get("elencoTutor");
			Iterator<JsonNode>iterator = elencoTutor.iterator();
			int num = 1;
			while (iterator.hasNext()) {
				JsonNode tutor = iterator.next();
				StringBuilder tutorSb1 = new StringBuilder();
				String nomeTutor = (tutor.has("nome")) ? tutor.get("nome").getTextValue() : "";
				String cognomeTutor = (tutor.has("cognome")) ? tutor.get("cognome").getTextValue() : "";
				String tutorNome = nomeTutor + " " + cognomeTutor;
				String mestiereTutor = (tutor.has("mestiere") && tutor.get("mestiere").has("nome")) ? 
						tutor.get("mestiere").get("nome").getTextValue() : "";
				
				String via = (tutor.has("indirizzo")) ? tutor.get("indirizzo").getTextValue() : "";
				String civico = (tutor.has("civico")) ? tutor.get("civico").getTextValue() : "";
				String cap = (tutor.has("cap")) ? tutor.get("cap").getTextValue() : "";
				String comune = (tutor.has("comune") && tutor.get("comune").has("nome")) ? 
							tutor.get("comune").get("nome").getTextValue() : "";
				String provincia = (tutor.has("provincia") && tutor.get("provincia").has("nome")) ? 
							tutor.get("provincia").get("nome").getTextValue() : "";
				String indirizzo = via + " " + civico + " "+ cap + " "+ comune;
				String tutorEmail = (tutor.has("email")) ? tutor.get("email").getTextValue() : "";
				String tutorTel = (tutor.has("telefono")) ? tutor.get("telefono").getTextValue() : "";
				String hospitalityTutor1  = (tutor.has("hospitality") && tutor.get("hospitality").asBoolean()) ? "si" : "no";
				
				tutorSb1.append(tutorNome).append(CSV_SEPARATOR);
				tutorSb1.append(indirizzo).append(CSV_SEPARATOR);	
				tutorSb1.append(provincia).append(CSV_SEPARATOR);	
				tutorSb1.append(" "+tutorTel).append(CSV_SEPARATOR);
				tutorSb1.append(tutorEmail).append(CSV_SEPARATOR);
				tutorSb1.append(hospitalityTutor1).append(CSV_SEPARATOR);
				
				listTutor.add(tutorSb1.toString());
				
			}
		}
		*/
		
		/*
		if (listCompetitor.size() > 0) {
			int m=0;
			for(int i = 0; i < listCompetitor.size(); i++) {  
				row.append(listCompetitor.get(i));
				if (listTutor.size() < listCompetitor.size() && m == listTutor.size()) {
					m = 0;
				}
				
				row.append(listTutor.get(m));
				m++;
				if (i < listCompetitor.size()-1) {
					row.append("\n");
				}
			}  		
		}
		*/
		
		

	}
		

}
