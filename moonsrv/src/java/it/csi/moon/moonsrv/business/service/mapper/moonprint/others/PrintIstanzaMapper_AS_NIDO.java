/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.mapper.moonprint.others;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.commons.entity.ModuloStrutturaEntity;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.BasePrintIstanzaMapper;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.MapperUtil;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.MoonprintDocumentWriter;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.PrintIstanzaMapper;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Contruttore di oggetto JSON per MOOnPrint
 * 
 * @author Danilo,Simone
 *
 * @since 1.0.0
 */
public class PrintIstanzaMapper_AS_NIDO extends BasePrintIstanzaMapper implements PrintIstanzaMapper {

	private static final String CLASS_NAME = "PrintIstanzaMapper_AS_NIDO";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	/**
	 * Remap l'istanza con il suo modulo  in un oggetto per MOOnPrint
	 * 
	 * @param istanza
	 * @param strutturaEntity
	 * 
	 * @return MoonprintDocument che contiene Module.Document di MOOnPrint
	 * 
	 * @throws Exception 
	 */
	public MoonprintDocument remap(Istanza istanza, ModuloStrutturaEntity strutturaEntity) throws Exception {
		final String DEFAULT_VALUE = "";
		MoonprintDocumentWriter localWriter = new MoonprintDocumentWriter();
		localWriter.setTitle(istanza.getModulo().getDescrizioneModulo());

		// Metadata
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		localWriter.setMetadataDataPresentazione(df.format(istanza.getCreated()));
		localWriter.setMetadataNumeroIstanza(istanza.getCodiceIstanza());
		localWriter.setMetadataQrContent(istanza.getCodiceIstanza());
		localWriter.setMetadataHeader(istanza.getModulo().getOggettoModulo(), "", "Inviata il: %%dataPresentazione%%");
		localWriter.setMetadataFooter("Istanza n.: %%numeroIstanza%%", null, null);

		// Lettura delle due strutture variabile
		JsonNode istanzaJsonNode = readIstanzaData(istanza);
		JsonNode data = istanzaJsonNode.get("data");
		MapperUtil util = new MapperUtil(localWriter);
		
		// Richiedente		
		if (data.has("richiedente")) {

			localWriter.addSection("Richiedente", "");			
			JsonNode richiedente = data.get("richiedente");

			localWriter.addSubSection("Dati anagrafici", "");
			localWriter.addItem("Nome", richiedente.has("nome") ? richiedente.get("nome").asText() : DEFAULT_VALUE);
			localWriter.addItem("Cognome", richiedente.has("cognome") ? richiedente.get("cognome").asText() : DEFAULT_VALUE);
			localWriter.addItem("Codice fiscale", richiedente.has("codiceFiscale") ? richiedente.get("codiceFiscale").asText() : DEFAULT_VALUE );			
			localWriter.addItem("Sesso", richiedente.has("datiSesso") ? (richiedente.get("datiSesso").asText().equals("M") ? "Maschio":"Femmina")  : DEFAULT_VALUE );			
			localWriter.addItem("Data di nascita", richiedente.has("dataNascita") ? richiedente.get("dataNascita").asText() : DEFAULT_VALUE );			
		}


		if (data.has("informativa") && data.get("informativa") != null)
		{
			localWriter.addSection(true,"Informativa privacy", "");	


			String testoInformativa ="Ai sensi del Regolamento UE n. 2016/679, artt. 13 e 14, relativi alla protezione delle persone"
					+ " fisiche con riguardo al trattamento dei dati personali e delle disposizioni della normativa nazionale,"
					+ " si informa che: \r\n "+ 
					"\r\n" + 
					"- Titolare del trattamento dei dati "+'\u00E8'+" il ...." + 
					" ";
			localWriter.addSubSectionTextToHtml("Informativa sul trattamento dei dati personali", "", "",testoInformativa);
			localWriter.addItem("Dichiaro di aver preso visione dell'informativa sul trattamento dei dati personali", DEFAULT_VALUE);
		}

		return localWriter.write();
	}



}
