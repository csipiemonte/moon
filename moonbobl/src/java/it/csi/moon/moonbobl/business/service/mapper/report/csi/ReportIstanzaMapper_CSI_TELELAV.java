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
public class ReportIstanzaMapper_CSI_TELELAV extends ReportIstanzaMapperDefault implements ReportIstanzaMapper {
	
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
		String dataInizio = "20230701";
		String lun = "N";
		String mar = "N";
		String mer = "N";
		String gio = "N";
		String ven = "N";

		
		String tipoLavoroAttuale = "";

		
		if (data.has("richiedente"))
		{
			JsonNode richiedente = data.get("richiedente");
			matricola = getTextValue(richiedente, "matricola");
		}
		if (data.has("situazioneAttuale"))
		{
			JsonNode situazioneAttuale = data.get("situazioneAttuale");

			String valoreOriginale = getTextValue(situazioneAttuale, "valoreOriginale");
			/*
			 *  telelavoro = 0 tradotto in 'Y'
			 */
			
			if (valoreOriginale.length() >= 4) {
				lun = (valoreOriginale.substring(0, 1).equals("1")) ? "N": "Y";
				mar = (valoreOriginale.substring(1, 2).equals("1")) ? "N": "Y";
				mer = (valoreOriginale.substring(2, 3).equals("1")) ? "N": "Y";
				gio = (valoreOriginale.substring(3, 4).equals("1")) ? "N": "Y";
				ven = (valoreOriginale.substring(4, 5).equals("1")) ? "N": "Y";				
			}
			tipoLavoroAttuale = getTextValue(situazioneAttuale, "tipoDiLavoro");
		}

		if (tipoLavoroAttuale.equals("noTelelavoro") || 
			(tipoLavoroAttuale.equals("telelavoro") && 
				data.has("tipoDiRichiesta1") && getTextValue(data, "tipoDiRichiesta1").equals("prorogaEVariazione") &&
				(data.has("tipoDiVariazione") && getTextValue(data, "tipoDiVariazione").equals("giorni") ||
				 data.has("tipoDiVariazione") && getTextValue(data, "tipoDiVariazione").equals("variazioneGiorniPostazione") ) )) {
			String tipoLavoroRichiesto ="";
			if (data.has("tipologiaDiTelelavoroRichiesto")) {
				tipoLavoroRichiesto = getTextValue(data, "tipologiaDiTelelavoroRichiesto");
			}
			if (tipoLavoroRichiesto.equals("totale")) {
				lun = "y"; mar = "y"; mer = "y"; gio = "y"; ven = "y";
			}
			if (tipoLavoroRichiesto.equals("parziale")) {
				if (data.has("giorniRichiestiDiTelelavoro"))
				{
					JsonNode giorniRichiestiDiTelelavoro = data.get("giorniRichiestiDiTelelavoro");
					lun = (giorniRichiestiDiTelelavoro.get("lun").asBoolean()) ? "Y": "N";
					mar = (giorniRichiestiDiTelelavoro.get("mar").asBoolean()) ? "Y": "N";
					mer = (giorniRichiestiDiTelelavoro.get("mer").asBoolean()) ? "Y": "N";
					gio = (giorniRichiestiDiTelelavoro.get("gio").asBoolean()) ? "Y": "N";
					ven = (giorniRichiestiDiTelelavoro.get("ven").asBoolean()) ? "Y": "N";
				}
			}
		}
		
		StringBuilder row = new StringBuilder();
		row.append(matricola).append(CSV_SEPARATOR);
		row.append(dataInizio).append(CSV_SEPARATOR);
		row.append(lun).append(CSV_SEPARATOR);
		row.append(mar).append(CSV_SEPARATOR);
		row.append(mer).append(CSV_SEPARATOR);
		row.append(gio).append(CSV_SEPARATOR);
		row.append(ven).append(CSV_SEPARATOR);

		return row.toString();

	}
	

	public String getHeader() {

		StringBuilder sb = new StringBuilder();
		sb.append("MATRICOLA").append(CSV_SEPARATOR);
		sb.append("DATA_INIZIO").append(CSV_SEPARATOR);
		sb.append("LUN").append(CSV_SEPARATOR);
		sb.append("MAR").append(CSV_SEPARATOR);
		sb.append("MER").append(CSV_SEPARATOR);
		sb.append("GIO").append(CSV_SEPARATOR);
		sb.append("VEN").append(CSV_SEPARATOR);
				
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

}
