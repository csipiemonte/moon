/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper.report.csi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapper;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapperDefault;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaEstratta;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Contruttore di oggetto JSON per export
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class ReportIstanzaMapper_CSI_PART_TIME extends ReportIstanzaMapperDefault implements ReportIstanzaMapper {
	
	private final static String CLASS_NAME = "ReportIstanzaMapper_CSI_PART_TIME";
	private final static Logger log = LoggerAccessor.getLoggerBusiness();
	private static final char CSV_SEPARATOR = ';';
	
	
    /**
     * export dati presenti nel formato richiesto da IREN 
     * 
     * @param List<Istanza>
     * 
     * @throws Exception 
     */	
	public String remapIstanza(IstanzaEstratta istanza) throws Exception {
		// Lettura del json
		log.debug("[" + CLASS_NAME + "::remap id istanza]  - " + istanza.getIdIstanza());
		String datastr = istanza.getData().toString();
		
		JsonNode istanzaJsonNode = readIstanzaData(datastr);
		JsonNode data = istanzaJsonNode.get("data");
		
		String matricola = "";
		String dataInizio = "20230901";
		String dataFine = "20240831";
		String categoria = "20";
		String tipoPartTime = "";
		String tipoMotivazione = "";
		String dettaglioMotivazione = "";
		String dataNascitaFiglio1 = "";
		String dataNascitaFiglio2 = "";
		String dataNascitaFiglio3 = "";
		String dataNascitaFiglio4 = "";
		String tipoDomanda = "";
		String statoDomanda = "0";
		String ore = "";
		String profiloOrario = "";
		String codiceProfiloOrario = "";
		String legge104 = "";
		String certMedico = "";
		
		String tipoLavoroAttuale = "";
		String profiloOrarioAttuale = "";
		String numeroDiOreAttuale = "";
		String legge104Attuale = "";
		
		if (data.has("richiedente"))
		{
			JsonNode richiedente = data.get("richiedente");
			matricola = getTextValue(richiedente, "matricola");
		}
		if (data.has("situazioneAttuale"))
		{
			JsonNode situazioneAttuale = data.get("situazioneAttuale");
			legge104Attuale = getTextValue(situazioneAttuale, "legge104");
			tipoLavoroAttuale = getTextValue(situazioneAttuale, "tipoDiLavoro");
			String tipoPartTimeAttuale = getTextValue(situazioneAttuale, "tipoPartTime");
			if (tipoLavoroAttuale.equals("part-time")) {
				profiloOrarioAttuale = getTextValue(situazioneAttuale, "profiloOrario");
				numeroDiOreAttuale = getTextValue(situazioneAttuale, "numeroDiOre");
				String tipoPartTimeLong = profiloOrarioAttuale.substring(0, Math.min(profiloOrarioAttuale.length(), 3));
				switch(tipoPartTimeLong) {
				case"PTO":
					tipoPartTime = "O";
					break;
				case"PTM":
					tipoPartTime = "M";
					break;
				case"PTV":
					tipoPartTime = "V";
					break;
				case"NON":
					tipoPartTime = tipoPartTimeAttuale;
					break;
				}
			}
		}
		if (data.has("motivazione"))
		{
			JsonNode motivazione = data.get("motivazione");
			if (motivazione.has("tipo"))
			{
				JsonNode tipo = motivazione.get("tipo");
				String tipoAssFigliSotto3 = (tipo.get("figliSotto3Anni").getBooleanValue()) ? "X" : "";
				String tipoAssFigliSopra3 = (tipo.get("figliSopra3Anni").getBooleanValue()) ? "X" : "";
				String tipoStudio = (tipo.get("studio").getBooleanValue()) ? "X" : "";
				String tipoGraviMotiviSalute = (tipo.get("graviMotiviSalute").getBooleanValue()) ? "X" : "";
				String tipoGraviMotiviFamiliari = (tipo.get("graviMotiviFamiliari").getBooleanValue()) ? "X" : "";
				String tipoAltro = (tipo.get("altro").getBooleanValue()) ? "X" : "";
				if (tipo.get("graviMotiviSalute").getBooleanValue()) {
					tipoMotivazione = "2";
				}
				else {
					if (tipo.get("graviMotiviFamiliari").getBooleanValue()) {
						tipoMotivazione = "3";
					}
					else {
						if (tipo.get("figliSotto3Anni").getBooleanValue() || 
								tipo.get("figliSopra3Anni").getBooleanValue()) {
							tipoMotivazione = "1";
							int i=1;
							if (tipo.get("figliSopra3Anni").getBooleanValue() && 
									data.has("figliSopra3Anni") && data.get("figliSopra3Anni").isArray()) {
								ArrayNode elenco = (ArrayNode) data.get("figliSopra3Anni");
								Iterator<JsonNode> it = elenco.iterator();
								while (it.hasNext()) {
									JsonNode figlio = it.next();
									if (i==1) {
										dataNascitaFiglio1 = convertiData(getTextValue(figlio, "dataNascita"));
									}
									if (i==2) {
										dataNascitaFiglio2 = convertiData(getTextValue(figlio, "dataNascita"));
									}
									if (i==3) {
										dataNascitaFiglio3 = convertiData(getTextValue(figlio, "dataNascita"));
									}
									if (i==4) {
										dataNascitaFiglio4 = convertiData(getTextValue(figlio, "dataNascita"));
									}
							        i++;
								}
							}
							/* non azzero il contatore "i" perchè possono essere indicati solo 4 figli
							 * e l'elenco prosegue dai figli sopra
							 */
							if (tipo.get("figliSotto3Anni").getBooleanValue() && 
									data.has("figliSotto3Anni") && data.get("figliSotto3Anni").isArray()) {
								ArrayNode elenco = (ArrayNode) data.get("figliSotto3Anni");
								Iterator<JsonNode> it = elenco.iterator();
								while (it.hasNext()) {
									JsonNode figlio = it.next();
									if (i==1) {
										dataNascitaFiglio1 = convertiData(getTextValue(figlio, "dataNascita"));
									}
									if (i==2) {
										dataNascitaFiglio2 = convertiData(getTextValue(figlio, "dataNascita"));
									}
									if (i==3) {
										dataNascitaFiglio3 = convertiData(getTextValue(figlio, "dataNascita"));
									}
									if (i==4) {
										dataNascitaFiglio4 = convertiData(getTextValue(figlio, "dataNascita"));
									}
							        i++;
								}
							}
						}
						else {
							if (tipo.get("studio").getBooleanValue()) {
								tipoMotivazione = "4";
							}
							else {
								tipoMotivazione = "5";
								dettaglioMotivazione = getTextValue(motivazione, "specificareAltro");
							}
						}
					}
				}
				dettaglioMotivazione += getTextValue(data, "noteAggiuntive");
				dettaglioMotivazione = dettaglioMotivazione.replace("\r", " ").replace("\n", " ");
				dettaglioMotivazione = dettaglioMotivazione.replace(";", ",");
				
				int dettMotivazioneLength = dettaglioMotivazione.length() > 300 ? 300: dettaglioMotivazione.length();				
				dettaglioMotivazione = dettaglioMotivazione.substring(0, dettMotivazioneLength);
				if (tipoMotivazione.equals("2") || tipoMotivazione.equals("3")) {
					// caso motivi salute o familiari
					if (data.has("legge104"))
					{
						String legge104Response = getTextValue(data, "legge104");
						switch(legge104Response) {
						case"si":
							legge104 = "Y";
							break;
						case"no":
							legge104 = "N";
							break;
						default:
							legge104 = "";
							break;
						}
					}
					if (data.has("certificatoMedico"))
					{
						String certMedResponse = getTextValue(data, "certificatoMedico");
						switch(certMedResponse) {
						case"si":
							certMedico = "Y";
							break;
						case"no":
							certMedico = "N";
							break;
						default:
							certMedico = "";
							break;
						}
					}
				}
			}
		}
		
		if (tipoLavoroAttuale.equals("full-time")) {
			if (data.has("tipoDiRichiesta") && getTextValue(data, "tipoDiRichiesta").equals("trasformazione"))
			{
				tipoDomanda = "2";
			}
		}
		else {
			if (data.has("tipoDiRichiesta1"))
			{
				if (getTextValue(data, "tipoDiRichiesta1").equals("prorogaEVariazione")) {
					tipoDomanda = "3";
				}
				else {
					tipoDomanda = "1";
				}
			}
		}
		if (tipoDomanda.equals("1")) {
			//proroga, decodifico partendo dal profilo attuale
			ore = numeroDiOreAttuale;
			profiloOrario = profiloOrarioAttuale;
			switch(profiloOrario) {
			case "PTO 20H SETT, 4H*5, FL. 45' 08.15-09.00, NO PM":
				codiceProfiloOrario = "9"; break;
			case "PTO 20H SETT PT, 5*4H, NO PM, 1H FLEX 8.15-9.15":
				codiceProfiloOrario = "10"; break;
			case "PTO 25H SETT, 5H*5, FL 45' 08.15-09.00, NO PM":
				codiceProfiloOrario = "7"; break;
			case "PTO 25H SETT, 5H*5, FL 1H 08.15-09.15, NO PM":
				codiceProfiloOrario = "8"; break;
			case "PTM, 28 H SETT, LUN/GIO, FL 8.15-9.15 PM 45' 12-14.45":
				codiceProfiloOrario = "27"; break;
			case "PTO 30H SETT, FL 45' 8.15-9.00, PM 45' 12.30-14.30":
				codiceProfiloOrario = "5"; break;
			case "PTO 30H SETT,6H*5, FL.30' 9-9.30, PM 45' 12.30-14.30":
				codiceProfiloOrario = "153"; break;
			case "PTO 30H SETT FLEX08.15-09.15 PM 12.00-14.45":
				codiceProfiloOrario = "6"; break;
			case "PTV 31H - LU/GIO - 7h45, FLEX 1h 8.15-9.15, PM45' 12.00-14.45":
				codiceProfiloOrario = "13"; break;
			case "NON BOLLANTE - 30H SETT.":
				codiceProfiloOrario = ""; break;
			case "NON BOLLANTI - 25H SETT.":
				codiceProfiloOrario = ""; break;
			}
		}
		else {
			ore = getTextValue(data, "numeroDiOreRichieste");
			if (data.has("profilo"))
			{
				JsonNode profilo = data.get("profilo");
				profiloOrario = getTextValue(profilo, "value");
				switch(profiloOrario) {
				case "4h da lun a ven no int. 45 min. fles.":
					codiceProfiloOrario = "9"; break;
				case "4h da lun a ven no int. 1h fles.":
					codiceProfiloOrario = "10"; break;
				case "5h da lun a ven no int. 45 min. fles.":
					codiceProfiloOrario = "7"; break;
				case "5h da lun a ven no int. 1h fles.":
					codiceProfiloOrario = "8"; break;
				case "6h15 lun-gio, 7h45 mar-mer, 2h45 int. 1h fles.":
					codiceProfiloOrario = "27"; break;
				case "6h da lun a ven 2h int. 45 min. fles.":
					codiceProfiloOrario = "5"; break;
				case "6h da lun a ven 2h int. 30 min. fles. 9-9,30":
					codiceProfiloOrario = "153"; break;
				case "6h da lun a ven 2h45 int. 1h fles.":
					codiceProfiloOrario = "6"; break;
				case "7h45 da lun a gio 2h45 int. 1h fles.":
					codiceProfiloOrario = "13"; break;
				case "7h45 da lun a gio 2h int. 45 min. fles.":
					codiceProfiloOrario = "12"; break;
				case "NON BOLLANTE - 30H SETT.":
					codiceProfiloOrario = ""; break;
				case "NON BOLLANTI - 25H SETT.":
					codiceProfiloOrario = ""; break;
				}
			}
		}
		
		StringBuilder row = new StringBuilder();
		row.append(matricola).append(CSV_SEPARATOR);
		row.append(dataInizio).append(CSV_SEPARATOR);
		row.append(dataFine).append(CSV_SEPARATOR);
		row.append(categoria).append(CSV_SEPARATOR);
		row.append(tipoPartTime).append(CSV_SEPARATOR);
		row.append(tipoMotivazione).append(CSV_SEPARATOR);
		row.append(dettaglioMotivazione).append(CSV_SEPARATOR);
		row.append(dataNascitaFiglio1).append(CSV_SEPARATOR);
		row.append(dataNascitaFiglio2).append(CSV_SEPARATOR);
		row.append(dataNascitaFiglio3).append(CSV_SEPARATOR);
		row.append(dataNascitaFiglio4).append(CSV_SEPARATOR);
		row.append(tipoDomanda).append(CSV_SEPARATOR);
		row.append(statoDomanda).append(CSV_SEPARATOR);
		row.append(ore).append(CSV_SEPARATOR);
		row.append(codiceProfiloOrario).append(CSV_SEPARATOR);
		row.append(legge104).append(CSV_SEPARATOR);
		row.append(certMedico).append(CSV_SEPARATOR);

		return row.toString();

	}
	
	
	private String convertiData(String dateValue) throws ParseException {
		String dataStr = "";
		String format = "dd-MM-yyyy";
		if(dateValue.length() > 10) {
			dateValue = dateValue.substring(0, 10);
		}
		if (dateValue.matches("\\d{2}-\\d{2}-\\d{4}")) {
			format = "dd-MM-yyyy";
		}
		else if (dateValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
			format = "yyyy-MM-dd";
		}
		else if (dateValue.matches("\\d{2}/\\d{2}/\\d{4}")) {
			format = "dd/MM/yyyy";
		}
		Date dn = new SimpleDateFormat(format).parse(dateValue);	
		dataStr = new SimpleDateFormat("yyyyMMdd").format(dn);	
		return dataStr;
	}


	public String getHeader() {

		StringBuilder sb = new StringBuilder();
		sb.append("MATRICOLA").append(CSV_SEPARATOR);
		sb.append("DATA_INIZIO").append(CSV_SEPARATOR);
		sb.append("DATA_FINE").append(CSV_SEPARATOR);
		sb.append("CATEGORIA").append(CSV_SEPARATOR);
		sb.append("TIPO PARTTIME").append(CSV_SEPARATOR);
		sb.append("MOTIVAZIONE").append(CSV_SEPARATOR);
		sb.append("DETTAGLIO MOTIVAZIONE").append(CSV_SEPARATOR);
		sb.append("DATA NASCITA FIGLIO 1").append(CSV_SEPARATOR);
		sb.append("DATA NASCITA FIGLIO 2").append(CSV_SEPARATOR);
		sb.append("DATA NASCITA FIGLIO 3").append(CSV_SEPARATOR);
		sb.append("DATA NASCITA FIGLIO 4").append(CSV_SEPARATOR);
		sb.append("TIPO DOMANDA").append(CSV_SEPARATOR);
		sb.append("STATO DOMANDA").append(CSV_SEPARATOR);
		sb.append("ORE").append(CSV_SEPARATOR);
		sb.append("PROFILO ORARIO").append(CSV_SEPARATOR);
		sb.append("LEGGE 104").append(CSV_SEPARATOR);
		sb.append("CERTIFICATO MEDICO").append(CSV_SEPARATOR);
				
		return sb.toString();

	}
	
	private String getTextValue(JsonNode node, String fieldName ) {
		String text = "";
		if( node.has(fieldName) ) {
			if( node.get(fieldName).isTextual() ) 
				text = node.get(fieldName).getTextValue();
			else
				text = node.get(fieldName).asText();
		} else {
			text = "";
		}
		return text;
	}

	private String getFieldNome(JsonNode node, String fieldName ) {
		String filedNome = "";
		if ( node.has(fieldName) && node.get(fieldName).has("nome") )
			filedNome = node.get(fieldName).get("nome").getTextValue();
		else if ( node.has(fieldName) && node.get(fieldName).isTextual() )
			filedNome = node.get(fieldName).getTextValue();
		else
			filedNome = "";
		return filedNome;
	}
}
