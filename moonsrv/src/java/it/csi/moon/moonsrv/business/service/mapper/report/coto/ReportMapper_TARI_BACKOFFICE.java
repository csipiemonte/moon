/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.report.coto;

import org.apache.tika.utils.StringUtils;

import it.csi.moon.commons.dto.api.IstanzaReport;
import it.csi.moon.moonsrv.business.service.mapper.report.ReportMapper;

public class ReportMapper_TARI_BACKOFFICE extends BaseReportMapper_TARI implements ReportMapper {
	
	private final static String CLASS_NAME = "ReportMapper_TARI_BACKOFFICE";

	public ReportMapper_TARI_BACKOFFICE(String codiceEstrazione) {
		this.codiceEstrazione = codiceEstrazione;
	}
	
	@Override
	public String remapIstanza(IstanzaReport istanza) throws Exception {
	
		StringBuilder row = new StringBuilder();

		//setting jsonPathUtil per json dati istanza
		setJsonPathUtil(istanza.getData().toString());		
		setRoot(".data");
		
		//N. Ticket OTRS
		String ticket = (istanza.getNumeroTicketOtrs() != null) ? istanza.getNumeroTicketOtrs(): NULL_VALUE;
		row.append(ticket).append(CSV_SEPARATOR);
		
		//N. Pratica Moon
		row.append(istanza.getCodiceIstanza()).append(CSV_SEPARATOR);
			
		//N. Protocollo
		String protocollo =  (!StringUtils.isBlank(istanza.getNumeroProtocollo())) ? istanza.getNumeroProtocollo(): jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".protocolloCompleto"));
		row.append(protocollo).append(CSV_SEPARATOR);
				
		//Canale di comunicazione
		String canale = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".canaleDiComunicazione.nome"));
		row.append(canale).append(CSV_SEPARATOR);
		
		//Cognome
		String cognome = getCognome();
		row.append(cognome).append(CSV_SEPARATOR);
		
		//Nome
		String nome = getNome();
		row.append(nome).append(CSV_SEPARATOR);
		
		//Ragione sociale (Denominazione)
		String ragioneSociale = getRagioneSociale();
		row.append(ragioneSociale).append(CSV_SEPARATOR);
		
		//Codice Utente
		String codiceUtente = getCodiceUtente();		
		row.append(codiceUtente).append(CSV_SEPARATOR);
		
		//Indirizzo Email
		String email = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".richiedente.email"));
		row.append(email).append(CSV_SEPARATOR);
				
		//Tipologia Utenza
		String tipologiaUtenza = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".tipologiaUtenza"));
		if (!StringUtils.isBlank(tipologiaUtenza)){
			tipologiaUtenza = EXPORT_VAL.UTENZA_DOMESTICA.equals(tipologiaUtenza)?EXPORT_VAL.UTENZA_DOMESTICA:EXPORT_VAL.UTENZA_NON_DOMESTICA;
		}
		row.append(tipologiaUtenza).append(CSV_SEPARATOR);
		
		//Tipologia Richiesta
		String tipologiaRichiesta = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".tipologiaRichiesta"));
		row.append(tipologiaRichiesta).append(CSV_SEPARATOR);
		
		//Codice Utenza
//		String codiceUtenza = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".codiceUtenza"));
		String codiceUtenza = getCodiceUtenza(istanza, null);
		row.append(codiceUtenza).append(CSV_SEPARATOR);		
		
		//Data Richiesta
		String dataRichiesta = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".dataRicezione")); 
		String dataRichiestaFormatted = StringUtils.isBlank(dataRichiesta) ? dataRichiesta : (dataRichiesta.substring(6, 10)+"-"+dataRichiesta.substring(3,5)+"-"+dataRichiesta.substring(0, 2));
//		String dataRichiestaFormatted = "";
		row.append(dataRichiestaFormatted).append(CSV_SEPARATOR);				
		
		//Data Chiusura
		String dataChiusura = (istanza.getDataChiusura() != null )? fDataOra.format(istanza.getDataChiusura()): NULL_VALUE;
		row.append(dataChiusura).append(CSV_SEPARATOR);			
		
		//Data Invio Istanza
		String dataInvio = (istanza.getCreated() != null) ? fDataOra.format(istanza.getCreated()): NULL_VALUE;
		row.append(dataInvio).append(CSV_SEPARATOR);
				
		//Data ultimo invio integrazione
		String dataUltimoInvioIntegrazione = (istanza.getDataUltimoInvioIntegrazione() != null )? fDataOra.format(istanza.getDataUltimoInvioIntegrazione()): NULL_VALUE;
		row.append(dataUltimoInvioIntegrazione).append(CSV_SEPARATOR);				
		
		
		//setting condizionale jsonPathUtil per json dati azione accoglimento /chiusura
		if (istanza.getDatiAzione() != null) {
			setJsonPathUtil(istanza.getDatiAzione().toString());
		}
		
		//Causa del mancato Rispetto dello Standard di Qualità	
		String causaMancatoRispettoQualita = (istanza.getDatiAzione() != null) ? jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".causaDelMancatoRispettoDelloStandardDiQualita")): NULL_VALUE;
		causaMancatoRispettoQualita = causaMancatoRispettoQualita.replace(REPLACE_PATTERN, "");
		row.append(causaMancatoRispettoQualita).append(CSV_SEPARATOR);			
		
		//Causa del mancato Obbligo di Risposta
		String causaMancatoObbligoRisposta = (istanza.getDatiAzione() != null) ? jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".causaDelMancatoObbligoDiRisposta")): NULL_VALUE;
		causaMancatoObbligoRisposta = causaMancatoObbligoRisposta.replace(REPLACE_PATTERN, "");
		row.append(causaMancatoObbligoRisposta).append(CSV_SEPARATOR);			

		//Nome Operatore
		String nomeOperatore = (istanza.getNomeOperatore() != null) ? istanza.getNomeOperatore(): NULL_VALUE;
		row.append(nomeOperatore).append(CSV_SEPARATOR);			
		
		//Note SemplificatoBO
		//valutazione dati modulo
		setJsonPathUtil(istanza.getData().toString());
		String noteSemplificatoBo = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".note"));;
		row.append(noteSemplificatoBo).append(CSV_SEPARATOR);		
		
		//Note di lavorazione
		//valutazione dati azione
		if (istanza.getDatiAzione() != null) {
			setJsonPathUtil(istanza.getDatiAzione().toString());
		}
		String noteLavorazione =  (istanza.getDatiAzione() != null) ? jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".noteDiLavorazione")): NULL_VALUE;
		noteLavorazione = noteLavorazione.replace(REPLACE_PATTERN, "");
		row.append(noteLavorazione.replace(CSV_SEPARATOR, CSV_SEPARATOR)).append(CSV_SEPARATOR);
		
		return row.toString();
	}

}
