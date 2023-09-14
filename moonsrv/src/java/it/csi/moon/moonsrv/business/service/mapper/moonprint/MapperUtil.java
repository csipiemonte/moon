/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.mapper.moonprint;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Utility per mapper
 * 
 * @author Simone
 *
 * Since 1.0.0
 */
public class MapperUtil {
	
	private static final String CLASS_NAME = "MapperUtil";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	
	private MoonprintDocumentWriter localWriter;
	
	final String DEFAULT_VALUE = "";

	public MapperUtil(MoonprintDocumentWriter localWriter) {
		this.localWriter = localWriter;
	}
	
	private MapperUtil() {
		// non usabile
	}
	
    // metodi pubblici 

	public void writeTextValue(String label, JsonNode node, String fieldName ) {
		if( node.has(fieldName) ) {
			localWriter.addItem(label, node.get(fieldName).asText());
		} else {
			localWriter.addItem(label, "");
		}
	}
	
	public String getTextValue(JsonNode node, String fieldName ) {
		String text = "";
		if( node.has(fieldName) ) {
			text = node.get(fieldName).asText();
		}
		return text;
	}
	
	public void writeBooleanValue(String label, String text, JsonNode node, String fieldName) {
		if( node.has(fieldName) && node.get(fieldName).asBoolean() ) {
				localWriter.addItem(label, text);
		}
	}
	
	public void writeTextValueIfHas(String label, JsonNode node, String value ) {
		if ( node.has(value) ) 	{
			if( node.get(value).isTextual() ) 
				localWriter.addItem(label, node.get(value).asText());
			else
				localWriter.addItem(label, "not textual");
		}
	}
	
	public void writeLabelAndTextIfTrue(String label, String text, JsonNode node, String fieldName) {
		if ( node.has(fieldName) && node.get(fieldName).asBoolean() )
			localWriter.addItem(label, text); 
	}
	
	public void writeNumberValueAsDouble (String label, JsonNode node, String fieldName) {
		if( node.has(fieldName) ) {
			if( node.get(fieldName).isNumber() ) 
				localWriter.addItem(label, Double.toString(node.get(fieldName).asDouble()) );
			else
				localWriter.addItem(label, "not number");
		}
	}
	
	public void writeNumberValueAsInteger (String label, JsonNode node, String fieldName) {
		if( node.has(fieldName) ) {
			if( node.get(fieldName).isNumber() ) 
				localWriter.addItem(label, Integer.toString(node.get(fieldName).asInt()) );
			else
				localWriter.addItem(label, "not number");
		}
	}
	
	public void writeDoubleValue(String label, JsonNode node, String fieldName) {
		if( node.has(fieldName) ) {
			if( node.get(fieldName).isDouble() ) // Se Decimale, con "."
				localWriter.addItem(label, Double.toString(node.get(fieldName).asDouble()) );
			else
				localWriter.addItem(label, "not double");
		}
	}
	
	public void writeIntValue(String label, JsonNode node, String fieldName) {
		if( node.has(fieldName) ) {
			if( node.get(fieldName).isInt() ) 
				localWriter.addItem(label, Integer.toString(node.get(fieldName).asInt()) );
			else
				localWriter.addItem(label, "not int");
		}
	}	

	public void writeAsDoubleValue(String label, JsonNode node, String fieldName) {
		DecimalFormat df = new DecimalFormat("##0.00");
		if( node.has(fieldName) ) {
			String numero =  df.format( node.get(fieldName).asDouble() );
			if( node.get(fieldName).isNumber() ) 
				localWriter.addItem(label, numero );
			else
				localWriter.addItem(label, "not number");
		}
	}
	
	public void writeFieldNome(String label, JsonNode node, String fieldName ) {
		if ( node.has(fieldName) && node.get(fieldName).has("nome") )
			localWriter.addItem(label, node.get(fieldName).get("nome").asText());
		else
			localWriter.addItem(label, "");
	}
	
	public void writeFieldProperty(String label, JsonNode node, String fieldName, String property ) {
		if ( node.has(fieldName) && node.get(fieldName).has(property) )
			localWriter.addItem(label, node.get(fieldName).get(property).asText());
		else
			localWriter.addItem(label, "");
	}
	
	public void writeFieldSigla(String label, JsonNode node, String fieldName ) {
		if ( node.has(fieldName) && node.get(fieldName).has("sigla") )
			localWriter.addItem(label, node.get(fieldName).get("sigla").asText());
		else
			localWriter.addItem(label, "");
	}
	
	public String getFieldSigla(JsonNode node, String fieldName ) {
		String filedNome = "";
		if ( node.has(fieldName) && node.get(fieldName).has("sigla") )
			filedNome = node.get(fieldName).get("sigla").asText();
		else if ( node.has(fieldName) && node.get(fieldName).isTextual() )
			filedNome = node.get(fieldName).asText();
		else
			filedNome = "";
		return filedNome;
	}
	
	public String getFieldNome(JsonNode node, String fieldName ) {
		String filedNome = "";
		if ( node.has(fieldName) && node.get(fieldName).has("nome") )
			filedNome = node.get(fieldName).get("nome").asText();
		else if ( node.has(fieldName) && node.get(fieldName).isTextual() )
			filedNome = node.get(fieldName).asText();
		else
			filedNome = "";
		return filedNome;
	}
	public String getFieldCodice(JsonNode node, String fieldName ) {
		String filedNome = "";
		if ( node.has(fieldName) && node.get(fieldName).has("codice") )
			filedNome = node.get(fieldName).get("codice").asText();
		else if ( node.has(fieldName) && node.get(fieldName).isTextual() )
			filedNome = node.get(fieldName).asText();
		else
			filedNome = "";
		return filedNome;
	}

	public void writeFieldNomeIfHas(String label, JsonNode node, String fieldName ) {
		if ( node.has(fieldName) && node.get(fieldName).has("nome") )
			localWriter.addItem(label, node.get(fieldName).get("nome").asText());
	}
	
	public void writeFieldLabel(String label, JsonNode node, String value ) {
		if ( node.has(value) )
			localWriter.addItem(label, node.get(value).get("label").asText());
		else
			localWriter.addItem(label, "");
	}
	
	public void writeNomeFileAllegato(String label, JsonNode node, String value) {
		if ( node.has(value) && node.get(value).has(0) )
			localWriter.addItem(label, node.get(value).get(0).get("originalName").asText());					
		//else
		//	localWriter.addItem(label, "");
	}
	
	public void writePropNameInArray(String label, JsonNode arrayNode, String propName) {
		if(arrayNode.isArray() && arrayNode.size()>0)
		{
			Iterator<JsonNode> iter = arrayNode.elements();
			String strToPrint = "";
			while (iter.hasNext()) { 
				JsonNode elem = iter.next();
				if (elem.has(propName)) {
					strToPrint = strToPrint + " " + elem.get(propName).asText();
				}
			}
			localWriter.addItem(label, strToPrint);
		}
	}
	
	public void writeTextValueInArrayElements(String label, JsonNode arrayNode, String propName) {
		if(arrayNode.isArray() && arrayNode.size()>0) {
			Iterator<JsonNode> iter = arrayNode.elements();
			while (iter.hasNext()) {
				JsonNode elem = iter.next();
				localWriter.addItem(label, elem.get(propName).asText());
			}
		}
	}
	
	public void writePersonaFisica(JsonNode personaFisica) {
		writeTextValue("Nome",personaFisica,"nome");
		writeTextValue("Cognome",personaFisica,"cognome");
		writeTextValue("Codice fiscale",personaFisica,"codiceFiscale");
		writeTextValue("Telefono", personaFisica, "telefono");
		writeTextValue("E-mail", personaFisica, "email");
		writeTextValue("PEC", personaFisica, "pec");
	}
	
	public void writePersonaGiuridica(JsonNode personaGiuridica) {
		writeTextValue("Ragione sociale", personaGiuridica, "ragionesociale");
		writeTextValue("Codice fiscale", personaGiuridica, "codiceFiscale");
		writeTextValue("P.IVA", personaGiuridica, "pIva");
		writeTextValue("Telefono", personaGiuridica, "telefono");
		writeTextValue("E-mail", personaGiuridica, "email");
		writeTextValue("PEC", personaGiuridica, "pec");
	}

	public void writeRappresentante(JsonNode rappresentante) {
		writeTextValue("Cognome", rappresentante, "cognome");
		writeTextValue("Nome", rappresentante, "nome");
		writeTextValue("Codice fiscale", rappresentante, "codiceFiscale");
	}
	
	public void writeDomicilioEstero(JsonNode domicilio) {
		writeFieldNome("Stato", domicilio, "stato" );
		writeTextValue("Citta", domicilio, "cittaestera");
		writeTextValue("Indirizzo", domicilio, "indirizzo");
		writeTextValue("Civico", domicilio, "civico");
	}
	
	public void writeDomicilio(JsonNode domicilio) {
		writeFieldNome("Stato", domicilio, "stato" );
		writeFieldNome("Provincia", domicilio, "provincia");
		writeFieldNome("Comune", domicilio, "comune");
		writeTextValue("Indirizzo", domicilio, "indirizzo");
		writeTextValue("Civico", domicilio, "civico");
		writeTextValue("CAP", domicilio, "cap");
	}

	public String getDate(JsonNode node, String value) {
		String formattedDate = "";
		if ( node.has(value) && !node.get(value).asText().equals("") )
		{
			String data = node.get(value).asText().substring(0, 11);
			Date dn = null;
			try {
				dn = new SimpleDateFormat("yyyy-MM-dd").parse(data);
			} catch (ParseException e) {
				LOG.error("[" + CLASS_NAME + "::getDate] ParseException: value=" + value, e);				
			}
			if (data != null)
				formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dn);	
		}
		return formattedDate;
	}
	public String getTime(JsonNode node, String value) {
		String formattedString = "";
		if ( node.has(value) && !node.get(value).asText().equals("") )
		{
			formattedString = node.get(value).asText().substring(0, 5);
		}
		return formattedString;
	}
	
	public String formatImportoDouble(double importo) {

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        if (Math.round(importo * 100) % 100 == 0) {
            formatter.setMaximumFractionDigits(2);
        }
        return formatter.format(importo);
    }
	public String formatImportoString(String importo) {

        return formatImportoDouble(Double.valueOf(importo).doubleValue());

    }
}
