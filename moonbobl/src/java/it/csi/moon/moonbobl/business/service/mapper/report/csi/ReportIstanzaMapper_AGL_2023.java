/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper.report.csi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.csi.moon.moonbobl.business.service.WorkflowService;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.ProvinciaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowFilter;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapper;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapperDefault;
import it.csi.moon.moonbobl.dto.extra.istat.Provincia;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaEstratta;
import it.csi.moon.moonbobl.dto.moonfobl.StoricoWorkflow;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Contruttore di oggetto JSON per MOOnPrint
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class ReportIstanzaMapper_AGL_2023 extends ReportIstanzaMapperDefault implements ReportIstanzaMapper {
	
	private final static String CLASS_NAME = "ReportIstanzaMapper_AGL_2023";
	private final static Logger log = LoggerAccessor.getLoggerBusiness();
	private static final char CSV_SEPARATOR = ';';
	
	
	@Autowired
	private WorkflowService workflowService;
	
    /**
     * export dati presenti nel formato richiesto da IREN 
     * 
     * @param List<Istanza>
     * 
     * @throws Exception 
     */	
	public String remapIstanza(IstanzaEstratta istanza) throws Exception {
		final String DEFAULT_VALUE = "";
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		// Lettura del json
		log.debug("[" + CLASS_NAME + "::remap id istanza]  - " + istanza.getIdIstanza());
		String datastr = istanza.getData().toString();
		String dataInvio = df.format(istanza.getCreated());
		
		JsonNode istanzaJsonNode = readIstanzaData(datastr);
		JsonNode data = istanzaJsonNode.get("data");
		String nIstanza = istanza.getCodiceIstanza();
		
		String matricola = "";
		String cognome = "";
		String nome = "";
		String foPrimoliv = "";
		String funzioneDiretta = "";
		String respFunzDiretta = "";
		String ggAggiuntiviPrecedenti = "";
		String dataArrivoRichiesta = dataInvio;
		
		String giorniRichiesti = "";
		String giorniTotali = "";
		String motivTipoLogistica = "";
		String motivTipoCaregiving = "";
		String motivTipoAssistenzaFigli = "";
		String motivTipoAssistenzaFigliUnder14 = "";
		String motivazioneNote = "";
		String dichiarazioneAltroGenitore = "";
		
		
		if (data.has("richiedente"))
		{
			JsonNode richiedente = data.get("richiedente");
			matricola = getTextValue(richiedente, "matricola");
			cognome = getTextValue(richiedente, "cognome");
			nome = getTextValue(richiedente, "nome");
			foPrimoliv = getTextValue(richiedente, "fo1Liv");
			funzioneDiretta = getTextValue(richiedente, "foDiretta");
			respFunzDiretta = getTextValue(richiedente, "responsabileFunzioneDiretta");
		}
		
		if (data.has("richiesta"))
		{
			JsonNode richiesta = data.get("richiesta");
			
			ggAggiuntiviPrecedenti = getTextValue(richiesta, "giorniRichiestaPrecedente");
			if (richiesta.has("motivazione"))
			{
				JsonNode motivazione = richiesta.get("motivazione");
				if (motivazione.has("tipologia"))
				{
					JsonNode tipologia = motivazione.get("tipologia");
					motivTipoLogistica = (tipologia.get("logistica").getBooleanValue()) ? "X" : "";
					motivTipoCaregiving = (tipologia.get("caregiving").getBooleanValue()) ? "X" : "";
					motivTipoAssistenzaFigli = (tipologia.get("assistenzaFigli").getBooleanValue()) ? "X" : "";
					motivTipoAssistenzaFigliUnder14 = 
							(tipologia.get("assistenzaFiglimilleproroghe").getBooleanValue()) ? "X" : "";
					
					if (tipologia.get("assistenzaFiglimilleproroghe").getBooleanValue()) {
						giorniRichiesti = getTextValue(richiesta, "giorniSupplementariMilleproroghe");
						if ("full".equals(giorniRichiesti)) {
							giorniTotali = "full";
						}
						else {
							int ggRichiestiNew = Integer.parseInt(giorniRichiesti);
							int ggTotali = 10 + ggRichiestiNew;
							giorniTotali = Integer.toString(ggTotali);
						}
						dichiarazioneAltroGenitore = "X";
					}
					else {
						giorniRichiesti = getTextValue(richiesta, "giorniSupplementari");
						int ggRichiestiNew = Integer.parseInt(giorniRichiesti);
						int ggTotali = 10 + ggRichiestiNew;
						giorniTotali = Integer.toString(ggTotali);
					}
				}
				motivazioneNote = getTextValue(motivazione, "note");
                motivazioneNote = motivazioneNote.replace("\r", " ").replace("\n", " ");
                motivazioneNote = motivazioneNote.replace(";", ",");
			}
		}
			
		StringBuilder row = new StringBuilder();
		row.append(matricola).append(CSV_SEPARATOR);
		row.append(cognome).append(CSV_SEPARATOR);
		row.append(nome).append(CSV_SEPARATOR);
		row.append(foPrimoliv).append(CSV_SEPARATOR);
		row.append(funzioneDiretta).append(CSV_SEPARATOR);
		row.append(respFunzDiretta).append(CSV_SEPARATOR);
		row.append(ggAggiuntiviPrecedenti).append(CSV_SEPARATOR);
		row.append(dataArrivoRichiesta).append(CSV_SEPARATOR);
		row.append(giorniRichiesti).append(CSV_SEPARATOR);
		row.append(giorniTotali).append(CSV_SEPARATOR);
		row.append(motivTipoLogistica).append(CSV_SEPARATOR);
		row.append(motivTipoCaregiving).append(CSV_SEPARATOR);
		row.append(motivTipoAssistenzaFigli).append(CSV_SEPARATOR);
		row.append(motivTipoAssistenzaFigliUnder14).append(CSV_SEPARATOR);
		row.append(motivazioneNote).append(CSV_SEPARATOR);
		row.append(dichiarazioneAltroGenitore).append(CSV_SEPARATOR);

		return row.toString();

	}
	
	
	public String getHeader() {

		StringBuilder sb = new StringBuilder();
		sb.append("MATRICOLA").append(CSV_SEPARATOR);
		sb.append("COGNOME").append(CSV_SEPARATOR);
		sb.append("NOME").append(CSV_SEPARATOR);
		sb.append("FUNZIONE 1 LVL").append(CSV_SEPARATOR);
		sb.append("FO DIRETTA").append(CSV_SEPARATOR);
		sb.append("RESP DIRETTO").append(CSV_SEPARATOR);
		sb.append("GG AGL AGGIUNTIVI FINO AL 31/03 (0/7)").append(CSV_SEPARATOR);
		sb.append("DATA ARRIVO RICHIESTA").append(CSV_SEPARATOR);
		sb.append("GG RICHIESTI IN PIU' NEW").append(CSV_SEPARATOR);
		sb.append("GG TOTALI").append(CSV_SEPARATOR);
		sb.append("ESIGENZE TECNICO LOGISTICHE DI SPOSTAMENTO").append(CSV_SEPARATOR);
		sb.append("CAREGIVING").append(CSV_SEPARATOR);
		sb.append("ASSISTENZA FIGLI").append(CSV_SEPARATOR);
		sb.append("ASSISTENZA FIGLI UNDER 14").append(CSV_SEPARATOR);
		sb.append("MOTIVAZIONI").append(CSV_SEPARATOR);
		sb.append("FLAG AUTODICHIARAZIONE UNDER 14").append(CSV_SEPARATOR);
		
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
