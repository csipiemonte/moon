/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper.report.coto;

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
public class ReportIstanzaMapper_IREN_2023 extends ReportIstanzaMapperDefault implements ReportIstanzaMapper {
	
	private final static String CLASS_NAME = "ReportIstanzaMapper_IREN_2023";
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
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		// Lettura del json
		log.debug("[" + CLASS_NAME + "::remap id istanza]  - " + istanza.getIdIstanza());
		String datastr = istanza.getData().toString();
		String dataInvio = df.format(istanza.getCreated());
		
		
		JsonNode istanzaJsonNode = readIstanzaData(datastr);
		
		JsonNode data = istanzaJsonNode.get("data");
		
		String nProtocollo = istanza.getCodiceIstanza();
		String dataProtocollo = dataInvio;
		String cognomeRichiedente = "";
		String nomeRichiedente = "";
		String codiceFiscaleRichiedente = "";
		String telefono = "";
		String cellulare = "";
		String email = "";
		String numeroComponenti = "";
		String indicatoreIsee25 = "OK";
		String descTipoUtenza = "";
		String numeroCliente = "";
		String numeroContratto = "";
		String via = "";
		String comune = "Torino";
		String civico = "";
		String cap = "";
		String provincia = "TO";
		String intestarioContratto = "";
		String cfIntestarioContratto = "";
		String pIvaIntestarioContratto = "";
		String nominativoProprietario = "";
		String nominativoAmministratore = "";
		String viaAmministratore = "";
		String comuneAmministratore = "";
		String civicoAmministratore = "";
		String capAmministratore = "";
		String provinciaAmministratore = "";
		String telefonoAmministratore = "";
		String cellulareAmministratore = "";
		String emailAmministratore = "";
		String dichiaraVero = " OK";
		String dichiaraPrivacy = "OK";
		String datiIseeVerificati = "";
		String esitoVerificaResidenza = "OK";
		String esitoVerificaNumComponenti = "OK";

		String tipoCompilazione = getTextValue(data, "tipoCompilazione");
		String daVerificare = "N";
		String daVerificareNumComponenti = "N";
		
		Boolean soggettoTrovatoInAnpr =  (getTextValue(data, "soggettoTrovatoInAnpr").equals("S")) ? true : false;
		String numeroComponentiValorizzato = "";
		String numeroComponentiAnpr = "";
		
		if (data.has("bonus"))
		{
			JsonNode bonus = data.get("bonus");
			if (tipoCompilazione.equals("aventeTitolo")) {
				if (soggettoTrovatoInAnpr) {
					// caso aventeTitolo trovato in ANPR
					numeroComponentiAnpr = getTextValue(data, "numeroComponenti");
					String datiModificati = (bonus.get("flagModificaComponenti").asBoolean()) ? "SI" : "NO";
					if (datiModificati.equals("SI")) {
						// leggo il numero di componenti inseriti dal cittadino
						numeroComponentiValorizzato = getTextValue(bonus, "numeroComponentiEffettivo");
					}
				}
				else {
					// caso aventeTitolo non trovato in ANPR
					numeroComponentiValorizzato = getTextValue(bonus, "numeroComponentiEffettivo");
					daVerificare = "S";
				}
			}
			else {
				// caso delegato con verifica successiva
				numeroComponentiValorizzato = getTextValue(bonus, "numeroComponentiEffettivo");
			}
		}
		if (tipoCompilazione.equals("delegato")) {
			// leggo i dati da workflow dopo la verifica
			StoricoWorkflowFilter filter = new StoricoWorkflowFilter();
			filter.setIdIstanza(istanza.getIdIstanza());
			filter.setCodiceAzione("VERIFICA_ANPR");

			StoricoWorkflow storico = workflowService.getStoricoWorkflowByFilter(filter);
			String datAzione =  storico.getDatiAzione();
			JsonNode azioneJsonNode = readIstanzaData(datAzione);
			JsonNode verifica = azioneJsonNode.get("data");
			Boolean trovatoInAnpr =  (getTextValue(verifica, "soggettoTrovatoInAnpr").equals("S")) ? true : false;
			if (! trovatoInAnpr) {
				daVerificare = "S";
			}
			else {
				numeroComponentiAnpr = getTextValue(verifica, "numeroComponenti");
			}
		}
		if (! "".equals(numeroComponentiAnpr)) {
			// imposto il valore uguale a quello acquisito da ANPR
			numeroComponenti = numeroComponentiAnpr;
			if (! "".equals(numeroComponentiValorizzato)) {
				// imposto il valore uguale a quello indicato dal cittadino
				numeroComponenti = numeroComponentiValorizzato;
				if (! numeroComponentiValorizzato.equals(numeroComponentiAnpr)) {
					int intComponentiValorizzati = Integer.parseInt(numeroComponentiValorizzato);
					int intComponentiAnpr = Integer.parseInt(numeroComponentiAnpr);
					if (intComponentiValorizzati > intComponentiAnpr) {
						daVerificareNumComponenti = "S";
					}
				}
			}
		}
		else {
			numeroComponenti = numeroComponentiValorizzato;
		}
		if ("S".equals(daVerificare)) {
			esitoVerificaResidenza = "KO";
		}
		if ("S".equals(daVerificareNumComponenti)) {
			esitoVerificaNumComponenti = "KO";
		}

		
		JsonNode aventeTitolo = data.get("aventeTitolo");
		if (data.has("aventeTitolo"))
		{
			cognomeRichiedente = getTextValue(aventeTitolo, "cognome");
			nomeRichiedente = getTextValue(aventeTitolo, "nome");
			codiceFiscaleRichiedente = getTextValue(aventeTitolo, "codiceFiscale");
		}
		if (data.has("contatti"))
		{
			JsonNode contatto = data.get("contatti");
			telefono = getTextValue(contatto, "telefono1");
			cellulare = getTextValue(contatto, "telefono2");
			email = getTextValue(contatto, "email");
		}
		
		if (data.has("fornitura"))
		{
			JsonNode fornitura = data.get("fornitura");
			String tipoUtenza = getTextValue(fornitura, "tipoUtenza");
			switch(tipoUtenza) {
			case "individuale":
				descTipoUtenza = "Riscaldamento Domestico Individuale";
				break;
			case "centralizzata":
				descTipoUtenza = "Riscaldamento Centralizzato";
				break;
			}
			numeroCliente = getTextValue(fornitura, "numeroCliente");
			numeroContratto = getTextValue(fornitura, "numeroContratto");	
			if (fornitura.has("indirizzo"))
			{
				JsonNode indirizzo = fornitura.get("indirizzo");
				via = getFieldNome(indirizzo, "via");
				civico = getTextValue(indirizzo, "civico");
				cap = getTextValue(indirizzo, "cap");
				
			}
			if (tipoUtenza.equals("centralizzata"))
			{
				if (fornitura.has("indiretta"))
				{
					JsonNode indiretta = fornitura.get("indiretta");
					intestarioContratto = getTextValue(indiretta, "nominativo");
					cfIntestarioContratto = getTextValue(indiretta, "codiceFiscale");
					pIvaIntestarioContratto = getTextValue(indiretta, "partitaIVA");
					nominativoProprietario = getTextValue(indiretta, "proprietario");
				}
				
				if (fornitura.has("amministratore"))
				{
					JsonNode amministratore = fornitura.get("amministratore");

					nominativoAmministratore = getTextValue(amministratore, "nominativo");
					viaAmministratore = getTextValue(amministratore, "indirizzo");
					comuneAmministratore = getFieldNome(amministratore, "comune");
					civicoAmministratore = getTextValue(amministratore, "civico");
					capAmministratore = getTextValue(amministratore, "cap");
					provinciaAmministratore = getFieldNome(amministratore, "provincia");
					
					telefonoAmministratore = getTextValue(amministratore, "telefono1");
					cellulareAmministratore = getTextValue(amministratore, "telefono2");
					emailAmministratore = getTextValue(amministratore, "email");
					
				}
			}
		}			

		
		StringBuilder row = new StringBuilder();
		row.append(nProtocollo).append(CSV_SEPARATOR);
		row.append(dataProtocollo).append(CSV_SEPARATOR);
		row.append(cognomeRichiedente).append(CSV_SEPARATOR);
		row.append(nomeRichiedente).append(CSV_SEPARATOR);
		row.append(codiceFiscaleRichiedente).append(CSV_SEPARATOR);
		row.append(telefono).append(CSV_SEPARATOR);
		row.append(cellulare).append(CSV_SEPARATOR);
		row.append(email).append(CSV_SEPARATOR);
		row.append(numeroComponenti).append(CSV_SEPARATOR);
		row.append(indicatoreIsee25).append(CSV_SEPARATOR);
		
		row.append(descTipoUtenza).append(CSV_SEPARATOR);
		row.append(numeroCliente).append(CSV_SEPARATOR);
		row.append(numeroContratto).append(CSV_SEPARATOR);
		row.append(via).append(CSV_SEPARATOR);
		row.append(comune).append(CSV_SEPARATOR);
		row.append(civico).append(CSV_SEPARATOR);
		row.append(cap).append(CSV_SEPARATOR);
		row.append(provincia).append(CSV_SEPARATOR);
		
		row.append(intestarioContratto).append(CSV_SEPARATOR);
		row.append(cfIntestarioContratto).append(CSV_SEPARATOR);
		row.append(pIvaIntestarioContratto).append(CSV_SEPARATOR);
		row.append(nominativoProprietario).append(CSV_SEPARATOR);
		row.append(nominativoAmministratore).append(CSV_SEPARATOR);
		row.append(viaAmministratore).append(CSV_SEPARATOR);
		row.append(comuneAmministratore).append(CSV_SEPARATOR);
		row.append(civicoAmministratore).append(CSV_SEPARATOR);
		row.append(capAmministratore).append(CSV_SEPARATOR);
		row.append(provinciaAmministratore).append(CSV_SEPARATOR);
		row.append(telefonoAmministratore).append(CSV_SEPARATOR);
		row.append(cellulareAmministratore).append(CSV_SEPARATOR);
		row.append(emailAmministratore).append(CSV_SEPARATOR);
		
		row.append(dichiaraVero).append(CSV_SEPARATOR);
		row.append(dichiaraPrivacy).append(CSV_SEPARATOR);
		row.append(datiIseeVerificati).append(CSV_SEPARATOR);
		row.append(esitoVerificaResidenza).append(CSV_SEPARATOR);
		row.append(esitoVerificaNumComponenti).append(CSV_SEPARATOR);
		return row.toString();

	}
	
	
	public String getHeader() {

		StringBuilder sb = new StringBuilder();
		sb.append("N° PROTOCOLLO").append(CSV_SEPARATOR);
		sb.append("DATA PROTOCOLLO").append(CSV_SEPARATOR);
		sb.append("COGNOME RICHIEDENTE").append(CSV_SEPARATOR);
		sb.append("NOME RICHIEDENTE").append(CSV_SEPARATOR);
		sb.append("CODICE FISCALE RICHIEDENTE").append(CSV_SEPARATOR);
		sb.append("TELEFONO").append(CSV_SEPARATOR);
		sb.append("CELLULARE").append(CSV_SEPARATOR);
		sb.append("E.MAIL").append(CSV_SEPARATOR);
		sb.append("NUMERO COMPONENTI IL NUCLEO FAMILIARE").append(CSV_SEPARATOR);
		sb.append("Indicatore ISEE fino a 25.000 euro").append(CSV_SEPARATOR);
		sb.append("UTENZA DIRETTA O CENTRALIZZATA (indiretta)").append(CSV_SEPARATOR);
		sb.append("NUMERO CLIENTE").append(CSV_SEPARATOR);
		sb.append("NUMERO CONTRATTO").append(CSV_SEPARATOR);
		sb.append("VIA").append(CSV_SEPARATOR);
		sb.append("COMUNE").append(CSV_SEPARATOR);
		sb.append("CIVICO (n.)").append(CSV_SEPARATOR);
		sb.append("CAP").append(CSV_SEPARATOR);
		sb.append("PROVINCIA").append(CSV_SEPARATOR);
		sb.append("NOMINATIVO INTESTARIO CONTRATTO").append(CSV_SEPARATOR);
		sb.append("CF INTESTARIO CONTRATTO").append(CSV_SEPARATOR);
		sb.append("P.IVA INTESTATARIO CONTRATTO").append(CSV_SEPARATOR);
		sb.append("NOMINATIVO PROPRIETARIO APPARTAMENTO").append(CSV_SEPARATOR);
		sb.append("NOMINATIVO AMMINISTRATORE DI CONDOMINIO").append(CSV_SEPARATOR);
		sb.append("VIA").append(CSV_SEPARATOR);
		sb.append("COMUNE").append(CSV_SEPARATOR);
		sb.append("CIVICO (n.)").append(CSV_SEPARATOR);
		sb.append("CAP").append(CSV_SEPARATOR);
		sb.append("PROVINCIA").append(CSV_SEPARATOR);
		sb.append("TELEFONO").append(CSV_SEPARATOR);
		sb.append("CELLULARE").append(CSV_SEPARATOR);
		sb.append("E.MAIL").append(CSV_SEPARATOR);
		sb.append("Dichiara che le informazioni riportate nella presente istanza corrispondono al vero").append(CSV_SEPARATOR);
		sb.append("Dichiara di essere informato ai sensi dell'Informativa Privacy").append(CSV_SEPARATOR);
		sb.append("Dati ISEE verificati").append(CSV_SEPARATOR);
		sb.append("Esito verifica residenza").append(CSV_SEPARATOR);
		sb.append("Esito verifica numero componenti").append(CSV_SEPARATOR);
		
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
