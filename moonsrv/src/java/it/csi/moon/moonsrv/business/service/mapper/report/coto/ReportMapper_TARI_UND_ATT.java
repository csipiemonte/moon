/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.report.coto;

import it.csi.moon.commons.dto.api.IstanzaReport;
import it.csi.moon.moonsrv.business.service.mapper.report.ReportMapper;
import it.csi.moon.moonsrv.business.service.mapper.report.coto.BaseReportMapper_TARI.EXPORT_VAL;

public class ReportMapper_TARI_UND_ATT extends BaseReportMapper_TARI implements ReportMapper {
	
	private final static String CLASS_NAME = "ReportMapper_TARI_UND_ATT";

	public ReportMapper_TARI_UND_ATT(String codiceEstrazione) {
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
		String protocollo = (istanza.getNumeroProtocollo() != null) ? istanza.getNumeroProtocollo() : NULL_VALUE;
		row.append(protocollo).append(CSV_SEPARATOR);
				
		//Canale di comunicazione
		String canale = EXPORT_VAL.CANALE_COMUNICAZIONE_WEB_AUT;
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
		String tipologiaUtenza = EXPORT_VAL.UTENZA_NON_DOMESTICA;
		row.append(tipologiaUtenza).append(CSV_SEPARATOR);
		
		//Tipologia Richiesta
		String tipologiaRichiesta = EXPORT_VAL.TIP_RICHIESTA_ATTIVAZIONE;
		row.append(tipologiaRichiesta).append(CSV_SEPARATOR);
		
		//Codice Utenza
//		String codiceUtenza = getCodiceUtenzaNonDomestica();
		String codiceUtenza = getCodiceUtenza(istanza, EXPORT_VAL.UTENZA_NON_DOMESTICA);
		row.append(codiceUtenza).append(CSV_SEPARATOR);		
		
		//Data Richiesta
		String dataRichiesta = (istanza.getCreated() != null) ? fDataOra.format(istanza.getCreated()): NULL_VALUE; 
		row.append(dataRichiesta).append(CSV_SEPARATOR);				
		
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
		String noteSemplificatoBo = NULL_VALUE;
		row.append(noteSemplificatoBo).append(CSV_SEPARATOR);		
		
		//Note di lavorazione
		String noteLavorazione =  (istanza.getDatiAzione() != null) ? jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".noteDiLavorazione")): NULL_VALUE;
		noteLavorazione = noteLavorazione.replace(REPLACE_PATTERN, "");
		row.append(noteLavorazione).append(CSV_SEPARATOR);				
		
		return row.toString();
	}
	
}
