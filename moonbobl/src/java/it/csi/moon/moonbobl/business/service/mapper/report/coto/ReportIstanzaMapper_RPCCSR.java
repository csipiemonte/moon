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
public class ReportIstanzaMapper_RPCCSR extends ReportIstanzaMapperDefault implements ReportIstanzaMapper {
	
	private final static String CLASS_NAME = "ReportIstanzaMapper_RPCCSR";
	private final static Logger log = LoggerAccessor.getLoggerBusiness();
	private static final char CSV_SEPARATOR = ';';
	

	@Autowired
	EnteDAO enteDAO;
	
	@Autowired
	IstanzaEstrattoreDAO istanzaEstrattoreDAO;
	

	public String getHeader() {

		StringBuilder sb = new StringBuilder();
		sb.append("Codice istanza").append(CSV_SEPARATOR);
		sb.append("Data").append(CSV_SEPARATOR);
		sb.append("Nome").append(CSV_SEPARATOR);
		sb.append("Cognome").append(CSV_SEPARATOR);
		sb.append("Codice Fiscale").append(CSV_SEPARATOR);
		sb.append("Email").append(CSV_SEPARATOR);
		sb.append("Pec").append(CSV_SEPARATOR);
		sb.append("Telefono").append(CSV_SEPARATOR);
		sb.append("Commissione").append(CSV_SEPARATOR);
		sb.append("Organismo").append(CSV_SEPARATOR);
		return sb.toString();

	}
	
	public String remapIstanza(IstanzaEstratta istanza, String codiceEnte) throws Exception {
		final String DEFAULT_VALUE = "";
		final DateFormat fDataOra = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

		log.debug("[" + CLASS_NAME + "::remap id istanza]  - " + istanza.getIdIstanza());
		
		StringBuilder row = new StringBuilder();
		
		String nome = "";
		String cognome = "";
		String codiceFiscale = "";
		String email = "";
		String pec = "";
		String telefono = "";
		String commissione = "";
		
		String datastr = istanza.getData().toString();
		JsonNode istanzaJsonNode = readIstanzaData(datastr);

		JsonNode data = istanzaJsonNode.get("data");
			
		if (data.has("anagrafica")) {
			JsonNode anagNode = data.get("anagrafica");				
			if (anagNode.has("nome")) {
				nome = anagNode.get("nome").getTextValue();
			}
			if (anagNode.has("cognome")) {
				cognome = anagNode.get("cognome").getTextValue();
			}
			if (anagNode.has("codiceFiscale")) {
				codiceFiscale = anagNode.get("codiceFiscale").getTextValue();
			}
			if (anagNode.has("contatti")) {
				JsonNode contNode = anagNode.get("contatti");					
				if (contNode.has("email")) {
					email = contNode.get("email").getTextValue();
				}					
				if (contNode.has("pec")) {
					pec = contNode.get("pec").getTextValue();
				}
				if (contNode.has("telefono")) {
					telefono = contNode.get("telefono").getTextValue();
				}
			}
		}

		switch(codiceEnte) {
			case "CCR_AF_IST": commissione = "Commissione affari istituzionali"; 
			break;
			case "CCR_AF_FIN": commissione = "Commissione affari finanziari"; 
			break;
			case "CCR_AF_EU": commissione = "Commissione Affari europei e internazionali"; 
			break;
			case "CCR_IN_MOB": commissione = "Commissione Infrastrutture, Mobilità e Governo del territorio"; 
			break;
			case "CCR_AMB_EN": commissione = "Commissione Ambiente, Energia e Sostenibilità"; 
			break;
			case "CCR_CULT": commissione = "Commissione Cultura"; 
			break;
			case "CCR_SPORT": commissione = "Commissione Sport"; 
			break;
			case "CCR_SAL": commissione = "Commissione salute"; 
			break;
			case "CCR_PO_SOC": commissione = "Commissione Politiche sociali"; 
			break;
			case "CCR_ISTR_UNI_RIC": commissione = "Commissione Istruzione, Università e Ricerca"; 
			break;
			case "CCR_LAV_PROF": commissione = "Commissione Lavoro e Formazione Professionale"; 
			break;
			case "CCR_PO_AGR": commissione = "Commissione politiche agricole"; 
			break;
			case "CCR_AT_PRO": commissione = "Commissione per lo Sviluppo Economico"; 
			break;
			case "CCR_PROT_CIV": commissione = "Commissione Protezione civile"; 
			break;
			case "CCR_TEC_DIG": commissione = "Commissione per la Innovazione tecnologiche e la Digitalizzazione"; 
			break;
			case "CCR_IMMIGRAZIONE": commissione = "Commissione Immigrazione"; 
			break;
			case "CCR_TURISMO": commissione = "Commissione Politiche per il Turismo"; 
			break;			
			default:
				commissione = "";
		}

		ArrayList<String> listOrganismi = new ArrayList<String>();
		if (data.has("manifestazioni")) {
			JsonNode manifestazioni = data.get("manifestazioni");
			ArrayNode elenco = (ArrayNode)manifestazioni.get("elenco");
			Iterator<JsonNode> it = elenco.iterator();
			while (it.hasNext()) {
				JsonNode commissioneDiInteresse = it.next();
				
				/* 01 commissioneAffari */
				if (commissioneDiInteresse.has("commissioneAffari") && codiceEnte.equals("CCR_AF_IST")) {
					JsonNode commissioneAffari = commissioneDiInteresse.get("commissioneAffari");			          
					if (commissioneAffari.has("collegioIndirizzo") && commissioneAffari.get("collegioIndirizzo").asBoolean()) 
						listOrganismi.add("Collegio di indirizzo e controllo dell'ARAN Articolo 46, comma 7, D.lgs. n. 165/2001");
					if (commissioneAffari.has("comitatoTecnico") && commissioneAffari.get("comitatoTecnico").asBoolean())
						listOrganismi.add("Comitato tecnico consultivo per l'applicazione della legislazione in materia di minoranze linguistiche D.P.R. 2 maggio 2001 n. 345");
					if (commissioneAffari.has("consiglioAmministrazione") && commissioneAffari.get("consiglioAmministrazione").asBoolean())
						listOrganismi.add("Consiglio di Amministrazione di FORMEZ PA, ai sensi dell'articolo 4, comma 1, lettera h), DL n. 80/2021");
					if (commissioneAffari.has("comitatoSettore") && commissioneAffari.get("comitatoSettore").asBoolean())	
						listOrganismi.add("Comitato di settore Regioni-Sanità Art. 56, co. 2, decreto legislativo 150/2009");							
				}
				
				/* 02 affariFinanziari */
				if (commissioneDiInteresse.has("affariFinanziari") && codiceEnte.equals("CCR_AF_FIN")) {
					JsonNode affariFinanziari = commissioneDiInteresse.get("affariFinanziari");			          
					if (affariFinanziari.has("conferenzaPermanente") && affariFinanziari.get("conferenzaPermanente").asBoolean()) 
						listOrganismi.add("Conferenza permanente per il coordinamento della finanza pubblica");
					if (affariFinanziari.has("consiglioAmministrazione") && affariFinanziari.get("consiglioAmministrazione").asBoolean())
						listOrganismi.add("Consiglio di amministrazione della Cassa depositi e prestiti");
											
				}
				
				/* 03 affariEuropei */
				if (commissioneDiInteresse.has("affariEuropei") && codiceEnte.equals("CCR_AF_EU")) {
					JsonNode affariEuropei = commissioneDiInteresse.get("affariEuropei");			          
					if (affariEuropei.has("cabinaRegia") && affariEuropei.get("cabinaRegia").asBoolean()) 
						listOrganismi.add("Cabina di Regia del Fondo per lo Sviluppo e la Coesione (FSC) Articolo 1, comma 703, lettera c), Legge 23 dicembre 2014, n. 190");
					if (affariEuropei.has("comitatoDirettivo") && affariEuropei.get("comitatoDirettivo").asBoolean())
						listOrganismi.add("Comitato direttivo dell'Agenzia per la coesione territoriale");
						
				}
			
				/* 04 commissioneInfrastrutture */
				if (commissioneDiInteresse.has("commissioneInfrastrutture") && codiceEnte.equals("CCR_IN_MOB")) {
					JsonNode commissioneInfrastrutture = commissioneDiInteresse.get("commissioneInfrastrutture");			          
					if (commissioneInfrastrutture.has("consiglioSuperiore") && commissioneInfrastrutture.get("consiglioSuperiore").asBoolean()) 
						listOrganismi.add("Consiglio superiore dei lavori pubblici DPR 27 aprile 2006 e DM 3 ottobre 2006");
					if (commissioneInfrastrutture.has("consiglioGenerale") && commissioneInfrastrutture.get("consiglioGenerale").asBoolean())
						listOrganismi.add("Consiglio generale e Assemblea dell'A.C.I.");
					if (commissioneInfrastrutture.has("conferenzaNazionale") && commissioneInfrastrutture.get("conferenzaNazionale").asBoolean())
						listOrganismi.add("Conferenza nazionale di coordinamento delle Autorità di sistema portuale art. 11ter, L. 84 del 1994, come modificato dal D.lgs n. 169 del 2016");
					if (commissioneInfrastrutture.has("comitatoSpeciale") && commissioneInfrastrutture.get("comitatoSpeciale").asBoolean())	
						listOrganismi.add("Comitato Speciale del Consiglio Superiore dei Lavori Pubblici, di cui all'art. 45 del DL n. 77 del 2021 (PNRR)");							
				}
				
				/* 05 ambienteEnergia */
				if (commissioneDiInteresse.has("ambienteEnergia") && codiceEnte.equals("CCR_AMB_EN")) {
					JsonNode ambienteEnergia = commissioneDiInteresse.get("ambienteEnergia");			          
					if (ambienteEnergia.has("unitaCoordinamento") && ambienteEnergia.get("unitaCoordinamento").asBoolean()) 
						listOrganismi.add("Unità di Coordinamento per l'attuazione del Piano d'azione per il miglioramento della qualità dell'aria Art. 2 Protocollo Intesa sottoscritto il 4 giugno 2019");
											
				}
				
				/* 06 cultura */

				
				/* 07 sport */
			
				/* 08 commissioneSalute */
				if (commissioneDiInteresse.has("commissioneSalute") && codiceEnte.equals("CCR_SAL")) {
					JsonNode commissioneSalute = commissioneDiInteresse.get("commissioneSalute");			          
					if (commissioneSalute.has("comitatoScientifico") && commissioneSalute.get("comitatoScientifico").asBoolean()) 
						listOrganismi.add("Comitato Scientifico dell’Istituto Superiore di Sanità Art. 9, DPR 70/2001");
					if (commissioneSalute.has("commissioneConsultiva") && commissioneSalute.get("commissioneConsultiva").asBoolean())
						listOrganismi.add("Commissione consultiva tecnico scientifica dell'Agenzia Italiana del Farmaco DM 20 settembre 2004, n. 245, Art. 19");
					if (commissioneSalute.has("comitatoPrezziRimborso") && commissioneSalute.get("comitatoPrezziRimborso").asBoolean())
						listOrganismi.add("Comitato Prezzi e Rimborso dell’Agenzia Italiana del Farmaco DM 20 settembre 2004, n. 245, Art. 19");
					if (commissioneSalute.has("comitatoStrategico") && commissioneSalute.get("comitatoStrategico").asBoolean())	
						listOrganismi.add("Comitato strategico di indirizzo del Centro Nazionale per la Prevenzione ed il Controllo delle Malattie (CCM) DPR 28 marzo 2013, n. 44");
					if (commissioneSalute.has("comitatoParitetico") && commissioneSalute.get("comitatoParitetico").asBoolean())	
						listOrganismi.add("Comitato paritetico permanente per la verifica dell’erogazione dei Livelli Essenziali di Assistenza Art. 9, intesa CSR 23/5/2005");
					if (commissioneSalute.has("tavoloTecnico") && commissioneSalute.get("tavoloTecnico").asBoolean())	
						listOrganismi.add("Tavolo tecnico per la verifica degli adempimenti di cui all'art. 12 dell'Intesa Stato-Regioni del 23 marzo 2005");
					if (commissioneSalute.has("comitatoTecnico") && commissioneSalute.get("comitatoTecnico").asBoolean())	
						listOrganismi.add("Comitato Tecnico Sanitario c) sezione per la ricerca sanitaria (5 rappr.) D.P.R. 28 marzo 2013, n. 44");
					if (commissioneSalute.has("commissioneNazionale") && commissioneSalute.get("commissioneNazionale").asBoolean())	
						listOrganismi.add("Commissione nazionale per l'aggiornamento dei LEA e la promozione dell'appropriatezza nel Servizio sanitario nazionale Articolo 1, comma 556, Legge 28 dicembre 2015, n. 208 (legge di stabilità 2016)");
					if (commissioneSalute.has("consiglioAmministrazione") && commissioneSalute.get("consiglioAmministrazione").asBoolean())	
						listOrganismi.add("Consiglio di amministrazione dell'Agenzia Nazionale per i Servizi Sanitari Regionali (AGENAS) D.Lgs. 115/1998, Art. 2");
					if (commissioneSalute.has("Aifa") && commissioneSalute.get("Aifa").asBoolean())	
						listOrganismi.add("Agenzia Italiana del farmaco (AIFA)");
					if (commissioneSalute.has("consiglioAmministrazioneSanita") && commissioneSalute.get("consiglioAmministrazioneSanita").asBoolean())	
						listOrganismi.add("Consiglio di Amministrazione dell’Istituto Superiore di Sanità Art. 6, DPR 70/2001");	
				}
				
				/* 09 politicheSociali */
				if (commissioneDiInteresse.has("politicheSociali") && codiceEnte.equals("CCR_PO_SOC")) {
					JsonNode politicheSociali = commissioneDiInteresse.get("politicheSociali");			          
					if (politicheSociali.has("cabinaRegia") && politicheSociali.get("cabinaRegia").asBoolean()) 
						listOrganismi.add("Cabina di regia interistituzionale del Piano di azione nazionale contro la tratta e il grave sfruttamento 2019-2021");					
				}
								
				/* 10 universita */
				if (commissioneDiInteresse.has("istruzioneUniRicerca") && codiceEnte.equals("CCR_ISTR_UNI_RIC")) {
					JsonNode universita = commissioneDiInteresse.get("istruzioneUniRicerca");			          
					if (universita.has("consiglioSuperiore") && universita.get("consiglioSuperiore").asBoolean()) 
						listOrganismi.add("Consiglio Superiore della Pubblica Istruzione");
					if (universita.has("consiglioAmministrazione") && universita.get("consiglioAmministrazione").asBoolean())
						listOrganismi.add("Consiglio di amministrazione del Consiglio Nazionale delle Ricerche (CNR) D.Lgs. 4 giugno 2003, n. 127");
										
				}
				
				/* 11 lavoro */
				if (commissioneDiInteresse.has("lavoro") && codiceEnte.equals("CCR_LAV_PROF")) {
					JsonNode lavoro = commissioneDiInteresse.get("lavoro");			          
					if (lavoro.has("consiglioAmministrazioneINAPP") && lavoro.get("consiglioAmministrazioneINAPP").asBoolean()) 
						listOrganismi.add("Consiglio di Amministrazione dell'Istituto Nazionale per l’Analisi delle Politiche Pubbliche (INAPP) Articolo 10, comma 1, D.Lgs. 14 settembre 2015, n. 150");
					if (lavoro.has("consiglioIndirizzo") && lavoro.get("consiglioIndirizzo").asBoolean())
						listOrganismi.add("Consiglio di indirizzo e vigilanza dell'INPS D.M. 2 aprile 2012, attuativo dell'art. 21, co. 6, D.L. 6 dicembre 2011, n. 201");
					if (lavoro.has("consiglioAmministrazioneANPAL") && lavoro.get("consiglioAmministrazioneANPAL").asBoolean())
						listOrganismi.add("Consiglio di Amministrazione dell'Agenzia Nazionale per le Politiche Attive del Lavoro (ANPAL) Art. 6 Dlgs. 14 settembre 2015, n. 150");						
				}


				/* 12 politicheAgricole */
				if (commissioneDiInteresse.has("politicheAgricole") && codiceEnte.equals("CCR_PO_AGR")) {
					JsonNode politicheAgricole = commissioneDiInteresse.get("politicheAgricole");			          
					if (politicheAgricole.has("consiglioAmministrazione") && politicheAgricole.get("consiglioAmministrazione").asBoolean()) 
						listOrganismi.add("Consiglio di amministrazione dell'Ente Nazionale Risi di Milano Art. 4 - sexiesdecies, D.L. 171/2008 Decreto Interministeriale 1 febbraio 2006");
					if (politicheAgricole.has("RicercaInAgricoltura") && politicheAgricole.get("RicercaInAgricoltura").asBoolean())
						listOrganismi.add("Consiglio di Amministrazione del Consiglio per la ricerca in agricoltura e l'analisi dell'economia agraria (CREA) Art. 1, co. 381 Legge 190/2014");
					if (politicheAgricole.has("Ismea") && politicheAgricole.get("Ismea").asBoolean())
						listOrganismi.add("Consiglio di Amministrazione dell'Ismea (Istituto per Studi, Ricerche e Informazioni sul Mercato Agricolo) Art. 4 - sexiesdecies, D.L. 171/2008 D.Lgs. 419/1999 e D.P.R.200/2001");					
				}
	
				
				/* 13 commissioneSviluppoEconomico */
				if (commissioneDiInteresse.has("commissioneSviluppoEconomico") && codiceEnte.equals("CCR_AT_PRO")) {
					JsonNode commissioneSviluppoEconomico = commissioneDiInteresse.get("commissioneSviluppoEconomico");			          
					if (commissioneSviluppoEconomico.has("consiglioGestioneFondo") && commissioneSviluppoEconomico.get("consiglioGestioneFondo").asBoolean()) 
						listOrganismi.add("Consiglio di gestione del Fondo di garanzia per le piccole e medie imprese, di cui alla legge 23 dicembre 1996, n. 662 e all'articolo 1, comma 100, lettera a) della Legge 27 dicembre 2013, n. 147 (legge di stabilità 2014)");
					if (commissioneSviluppoEconomico.has("comitatoInterventi") && commissioneSviluppoEconomico.get("comitatoInterventi").asBoolean())
						listOrganismi.add("Comitato per gli interventi di concessione di anticipazioni finanziarie per l’acquisizione temporanea di partecipazioni nel capitale di rischio di P.M.I. Art. 2 Convenzione tra il Ministero delle Attività Produttive e MCC S.p.A. dell'11 marzo 2003");
					if (commissioneSviluppoEconomico.has("comitatoAgevolazioni") && commissioneSviluppoEconomico.get("comitatoAgevolazioni").asBoolean())
						listOrganismi.add("Comitato Agevolazioni presso SIMEST");
					if (commissioneSviluppoEconomico.has("comitatoAttrazione") && commissioneSviluppoEconomico.get("comitatoAttrazione").asBoolean())	
						listOrganismi.add("Comitato per l'Attrazione degli Investimenti Esteri");								
				}
				
				/* 14 Protezione civile */
			
				/* 15 innovazioneTecDig */
				if (commissioneDiInteresse.has("innovazioneTecDig") && codiceEnte.equals("CCR_TEC_DIG")) {
					JsonNode innovazioneTecDig = commissioneDiInteresse.get("innovazioneTecDig");			          
					if (innovazioneTecDig.has("segreteriaTecnica") && innovazioneTecDig.get("segreteriaTecnica").asBoolean()) 
						listOrganismi.add("Segreteria tecnica del Comitato per la banda ultralarga DPCM 9 novembre 2015 DPCM 7 agosto 2018 DPCM 15 novembre 2019");
					if (innovazioneTecDig.has("comitatoIndirizzo") && innovazioneTecDig.get("comitatoIndirizzo").asBoolean())
						listOrganismi.add("Comitato di indirizzo dell'Agenzia per l'Italia digitale");
									
				}
			

				/* 16 commissioneAffari */
				if (commissioneDiInteresse.has("immigrazione") && codiceEnte.equals("CCR_IMMIGRAZIONE")) {
					JsonNode immigrazione = commissioneDiInteresse.get("immigrazione");			          
					if (immigrazione.has("tavoloDiCoordinamentoNazionaleSuiFlussiMigratoriNonProgrammatiArt3DM16Ottobre2014") && immigrazione.get("tavoloDiCoordinamentoNazionaleSuiFlussiMigratoriNonProgrammatiArt3DM16Ottobre2014").asBoolean()) 
						listOrganismi.add("Tavolo di Coordinamento Nazionale sui flussi migratori non programmati Art. 3, D.M. 16 ottobre 2014");
					if (immigrazione.has("tavoloIntegrazione") && immigrazione.get("tavoloIntegrazione").asBoolean())
						listOrganismi.add("Tavolo Integrazione");
										
				}
	
				/* 17 turismo */
				if (commissioneDiInteresse.has("turismo") && codiceEnte.equals("CCR_TURISMO")) {
					JsonNode turismo = commissioneDiInteresse.get("turismo");			          
					if (turismo.has("comitatoPermanentePerLaPromozioneDelTurismo") && turismo.get("comitatoPermanentePerLaPromozioneDelTurismo").asBoolean()) 
						listOrganismi.add("Comitato permanente per la promozione del turismo");
					if (turismo.has("consiglioAmministrazione") && turismo.get("consiglioAmministrazione").asBoolean())
						listOrganismi.add("Consiglio di amministrazione dell'ENIT - Agenzia Nazionale del Turismo ai sensi dell' art. 5, co. 1 del nuovo Statuto, approvato con D.P.C.M. 21 maggio 2015, n. 1478");						
				}
				
			}
		}

		if (listOrganismi.size() > 0) {
			for(int i = 0; i < listOrganismi.size(); i++) {   
			    row.append(istanza.getCodiceIstanza()).append(CSV_SEPARATOR);
				row.append(fDataOra.format(istanza.getCreated())).append(CSV_SEPARATOR);
				row.append(nome).append(CSV_SEPARATOR);
				row.append(cognome).append(CSV_SEPARATOR);
				row.append(codiceFiscale).append(CSV_SEPARATOR);
			    row.append(email).append(CSV_SEPARATOR);
				row.append(pec).append(CSV_SEPARATOR);
				row.append(telefono).append(CSV_SEPARATOR);	
				row.append(commissione).append(CSV_SEPARATOR);	
				row.append(listOrganismi.get(i)).append(CSV_SEPARATOR);
				row.append("\n");
			}  		
		}
		else {
			row.append(istanza.getCodiceIstanza()).append(CSV_SEPARATOR);
			row.append(fDataOra.format(istanza.getCreated())).append(CSV_SEPARATOR);
			row.append(nome).append(CSV_SEPARATOR);
			row.append(cognome).append(CSV_SEPARATOR);
			row.append(codiceFiscale).append(CSV_SEPARATOR);
		    row.append(email).append(CSV_SEPARATOR);
			row.append(pec).append(CSV_SEPARATOR);
			row.append(telefono).append(CSV_SEPARATOR);	
			row.append(commissione).append(CSV_SEPARATOR);	
			row.append(CSV_SEPARATOR);
			row.append("\n");
		}
		return row.toString();

	}
		
	@Override
	public StreamingOutput getStreamingOutput(UserInfo user, String filtro, Long idModulo) {
		String codiceEnte = estraiCodiceEnte(filtro);// filtro =  idEnte=CR_XXX_ZZZ  => codiceEnte=CR_XXX_ZZZ
		
		IstanzeFilter filter = new IstanzeFilter();
		filter.setIdModuli(Arrays.asList(idModulo));
		String sort = "i.data_creazione";
		filter.setSort(sort);

		return new StreamingOutput() {
			public void write(OutputStream os) throws IOException, WebApplicationException {
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(os))) {	
					writer.write(getHeader());
					writer.write('\n');
					List<String> codiciEnte = (!StringUtils.isEmpty(codiceEnte)) ?
						Arrays.asList(codiceEnte) :
						getElencoCodiciEnteCommisioniUtenteAbilitato(user);
					codiciEnte.stream().forEach(c -> estraiAndWriteIstanzeByCodiceEnte(filter, writer, c));
				}
			}

			private List<String> getElencoCodiciEnteCommisioniUtenteAbilitato(UserInfo user) {
				EntiFilter enteFilter = new EntiFilter();
				enteFilter.setIdTipoEnte(7); // 7-Commissione
				enteFilter.setUtenteAbilitato(user.getIdentificativoUtente());
				return enteDAO.find(enteFilter).stream().map(e -> e.getCodiceEnte()).collect(Collectors.toList());
			}

			private void estraiAndWriteIstanzeByCodiceEnte(IstanzeFilter filter, Writer writer, String codiceEnte) {
				//filter.setStrContainsInDati("\""+codiceEnte+"\":true");	
				filter.setStrContainsInDati("\"codice\":\""+codiceEnte+"\"");	
				istanzaEstrattoreDAO.estraiIstanze(filter, istanza -> {	
					try {
						writer.write(remapIstanza(istanza, codiceEnte));
						//writer.write('\n');														
					}catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				});
			}
		};
	}

	protected String estraiCodiceEnte(String strContainsInDati) {

		if (strContainsInDati != null && !strContainsInDati.equals("")) {
			String[] arrOfParams = strContainsInDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0) {
				for (int i = 0; i < listParams.size(); i++) {
					String param = listParams.get(i);
					String[] arrKeyValue = param.split("=");
					if (arrKeyValue[0].equals("idEnte")) {
						return arrKeyValue[1];
					}
				}
			}
		}
		return null;
	}

}
