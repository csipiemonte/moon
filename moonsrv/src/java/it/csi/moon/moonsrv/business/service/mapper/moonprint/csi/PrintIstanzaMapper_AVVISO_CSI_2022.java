/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.moonprint.csi;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.commons.entity.ModuloStrutturaEntity;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.BasePrintIstanzaMapper;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.MapperUtil;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.MoonprintDocumentWriter;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.PrintIstanzaMapper;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Contruttore di oggetto JSON per MOOnPrint - Concorsi
 *
 */
public class PrintIstanzaMapper_AVVISO_CSI_2022 extends BasePrintIstanzaMapper implements PrintIstanzaMapper {
	
	private static final String CLASS_NAME = "PrintIstanzaMapper_AVVISO_CSI_2022";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	/**
	 * Remap l'istanza con il suo modulo in un oggetto per MOOnPrint
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
		// LOG.debug("["+CLASS_NAME+"]:: remap"+ strutturaEntity.getStruttura());
		// Metadata
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		final DateFormat dt = new SimpleDateFormat("HH:mm");
		
		String dataOra = df.format(istanza.getCreated()) + " alle ore " + dt.format(istanza.getCreated());
		localWriter.setMetadataDataPresentazione(dataOra);
		localWriter.setMetadataNumeroIstanza(istanza.getCodiceIstanza());
		localWriter.setMetadataQrContent(istanza.getCodiceIstanza());
		localWriter.setMetadataHeader(istanza.getModulo().getOggettoModulo(), "", "Inviata il: %%dataPresentazione%%");
		localWriter.setMetadataFooter("Istanza n.: %%numeroIstanza%%", null, null);
		// Lettura delle due strutture variabili
		JsonNode istanzaJsonNode = readIstanzaData(istanza);
		
		JsonNode data = istanzaJsonNode.get("data");
		
		MapperUtil util = new MapperUtil(localWriter);
		
		
		/* prendo direttamente dalla struttura del modulo il titolo del componente che tratta delle'elettorato politico e il link dell'informativa sulla privacy
		JsonNode components= readStrutturaModulo(strutturaEntity).get("components");
		String str=components.get(1).get("components").get(2).get("components").get(4).get("label").asText(); //di non essere escluso/a dall'ellettorato politico attivo
		String linkInformativa=components.get(5).get("components").get(10).get("attrs").get(0).get("value").asText();
		*/
		
		// Lettura dei dati del canditato
		localWriter.addSection("Anagrafica del candidato/a", "");
		if (data.has("anagrafica"))
		{
			JsonNode anagrafica = data.get("anagrafica");
			if (anagrafica.has("candidato"))
			{
				JsonNode candidato = anagrafica.get("candidato");
				util.writeTextValue("Nome", candidato, "nome");
				util.writeTextValue("Cognome", candidato, "cognome");
				util.writeTextValue("Codice Fiscale", candidato, "codiceFiscale");
				util.writeTextValue("Nato il", candidato, "nato");
				String stato = (candidato.get("stato").has("nome")) ? candidato.get("stato").get("nome").asText() :"";
				util.writeFieldNome("Stato di nascita", candidato, "stato" );
				if (stato.equals("ITALIA")) {							
					util.writeFieldNome("Provincia", candidato, "provincia");
					util.writeFieldNome("Comune", candidato, "comune");
				}
				else {
					util.writeTextValue("Citta", candidato, "cittaestera");
				}
				
			}
			if (anagrafica.has("documento"))
			{
				localWriter.addSubSection("Documento di riconoscimento", "");
				JsonNode documento = anagrafica.get("documento");
				
				util.writeTextValue("Tipologia", documento, "tipologia");
				util.writeTextValue("Numero", documento, "numero");
				util.writeTextValue("Data scadenza", documento, "dataScadenza");
			}
			
			if (anagrafica.has("residenza"))
			{
				localWriter.addSubSection("Residenza", "");
				JsonNode residenza = anagrafica.get("residenza");
				String stato = (residenza.get("stato").has("nome")) ? residenza.get("stato").get("nome").asText() :"";
				util.writeFieldNome("Stato", residenza, "stato" );
				if (stato.equals("ITALIA")) {							
					util.writeFieldNome("Provincia", residenza, "provincia");
					util.writeFieldNome("Comune", residenza, "comune");
				}
				else {
					util.writeTextValue("Citta", residenza, "cittaestera");
				}
				util.writeTextValue("Indirizzo", residenza, "indirizzo");
				util.writeTextValue("Civico", residenza, "civico");
				util.writeTextValue("CAP", residenza, "cap");
			}
			Boolean diversoResidenza = anagrafica.get("diversoResidenza").asBoolean();
			if (diversoResidenza && anagrafica.has("recapito"))
			{
				localWriter.addSubSection("Recapito", "");
				JsonNode recapito = anagrafica.get("recapito");
				String stato = (recapito.get("stato").has("nome")) ? recapito.get("stato").get("nome").asText() :"";
				util.writeFieldNome("Stato", recapito, "stato" );
				if (stato.equals("ITALIA")) {							
					util.writeFieldNome("Provincia", recapito, "provincia");
					util.writeFieldNome("Comune", recapito, "comune");
				}
				else {
					util.writeTextValue("Citta", recapito, "cittaestera");
				}
				util.writeTextValue("Indirizzo", recapito, "indirizzo");
				util.writeTextValue("Civico", recapito, "civico");
				util.writeTextValue("CAP", recapito, "cap");
			}
			
		}

		if (data.has("dichiarazioni"))
		{
			localWriter.addSection("Dichiarazioni", "");
			JsonNode dichiarazioni = data.get("dichiarazioni");
			localWriter.addItem("di godere dei diritti civili e politici",
					dichiarazioni.has("dirittiCivili") && dichiarazioni.get("dirittiCivili").asBoolean() ? "Si" : "No");
			localWriter.addItem("di non aver riportato condanne penali",
					dichiarazioni.has("condannePenali") && dichiarazioni.get("condannePenali").asBoolean() ? "Si" : "No");
			if (!dichiarazioni.get("condannePenali").asBoolean()) {
				util.writeTextValue("Condanna riportata, la sentenza e data", dichiarazioni, "condannePenaliSI");
			}
		
			localWriter.addItem("di non avere in corso procedimenti penali pendenti;",
					dichiarazioni.has("procedimentiPenaliPendenti") && dichiarazioni.get("procedimentiPenaliPendenti").asBoolean() ? "Si" : "No");
			if (!dichiarazioni.get("procedimentiPenaliPendenti").asBoolean()) {
				if (dichiarazioni.has("procedimentiInCorso")) {
					util.writeTextValue("Procedimenti penali in corso", dichiarazioni, "procedimentiInCorso");
				}
			}	
			localWriter.addItem("di non essere destinatario di provvedimenti di applicazione di misure di "
					+ "prevenzione o cause ostative di cui al d.lgs. 159/2011",
					dichiarazioni.has("NoMisurePrevenzione") && dichiarazioni.get("NoMisurePrevenzione").asBoolean() ? "Si" : "No");
			
			localWriter.addItem("di non trovarsi in nessuna situazione di conflitto di interesse con csi-piemonte; "
					+ "in particolare di non aver esercitato, nell’ambito di un cessato rapporto di pubblico impiego con "
					+ "una pubblica amministrazione nei tre anni precedenti alla presente procedura poteri autoritativi "
					+ "o negoziali per conto dell’amministrazione medesima nei confronti del csi-piemonte "
					+ "(divieto ex art. 53 comma 16 ter d.lgs.165/2001)",
					dichiarazioni.has("noConflittoInteressi") && dichiarazioni.get("noConflittoInteressi").asBoolean() ? "Si" : "No");
			
			localWriter.addItem("di non trovarsi in nessuna delle condizioni di incompatibilità e inconferibilità "
					+ "previste dalle disposizioni di cui al decreto legislativo 8 aprile 2013 n. 39 in materia di "
					+ "inconferibilità e incompatibilità di incarichi presso le pubbliche amministrazioni e "
					+ "presso gli enti privati in controllo pubblico",
					dichiarazioni.has("noCondizioniIncompatibilita") && dichiarazioni.get("noCondizioniIncompatibilita").asBoolean() ? "Si" : "No");
			
			localWriter.addItem("di non avere rapporti di coniugio o parentela entro il secondo grado "
					+ "(nonni, genitori, figli e nipoti in linea retta – figli dei figli, fratelli e sorelle) "
					+ "con i vertici politici/amministrativi degli enti clienti del csi-piemonte, nonche’ con i fornitori dell consorzio",
					dichiarazioni.has("noRapportiParentela") && dichiarazioni.get("noRapportiParentela").asBoolean() ? "Si" : "No");
		
			if (!dichiarazioni.get("noRapportiParentela").asBoolean()) {
				util.writeTextValue("Rapporti di parentela esistenti", dichiarazioni, "rapportiParentelaEsistenti");
			}
		
			localWriter.addItem("di essere in possesso dell’idoneità fisica all’impiego;",
					dichiarazioni.has("possessoIdoneitaFisica") && dichiarazioni.get("possessoIdoneitaFisica").asBoolean() ? "Si" : "No");
			
			localWriter.addItem("di essere soggetto disabile appartenente alle categorie di cui all’art. 1 legge 68/99",
					dichiarazioni.has("soggettoDisabile") && dichiarazioni.get("soggettoDisabile").asBoolean() ? "Si" : "No");
			
			localWriter.addItem("di non essere stato/a destituito/a o dispensato/a dall’impiego presso una pubblica amministrazione e/o "
					+ "presso soggetti privati tenuti al rispetto di normative pubblicistiche per persistente insufficiente "
					+ "rendimento ovvero licenziato/a a seguito di procedimento disciplinare o per la produzione di documenti falsi",
					dichiarazioni.has("noDestituito") && dichiarazioni.get("noDestituito").asBoolean() ? "Si" : "No");
			
			String cittadinanza = dichiarazioni.get("cittadinanza").asText();
			
			localWriter.addItem("di essere cittadino ",cittadinanza);
			if (cittadinanza.equals("stranieroExtraUe") || cittadinanza.equals("stranieroUe")) {
				localWriter.addItem("di avere buona conoscenza della lingua italiana parlata e scritta ",
						dichiarazioni.has("conoscenzaItaliano") && dichiarazioni.get("conoscenzaItaliano").asBoolean() ? "Si" : "No");
				
				if (cittadinanza.equals("stranieroExtraUe")) {
					localWriter.addItem("di essere in possesso di regolare titolo di soggiorno sul territorio nazionale ai sensi della vigente normativa (D.Lgs. 286/1998 e s.m.i.) ",
							dichiarazioni.has("regolareTitoloSoggiorno") && dichiarazioni.get("regolareTitoloSoggiorno").asBoolean() ? "Si" : "No");
				}
			}
			localWriter.addItem("di essere in possesso del seguente titolo di studio",
					dichiarazioni.has("possessoTitoloDiStudio") && dichiarazioni.get("possessoTitoloDiStudio").asBoolean() ? "Si" : "No");
			
			if (dichiarazioni.get("possessoTitoloDiStudio").asBoolean()) {
				localWriter.addSubSection("Titolo di studio", "");
				JsonNode titolo = dichiarazioni.get("titolo");
				localWriter.addItem("Tipologia titolo",
						titolo.has("tipologia") && titolo.get("tipologia").has("nome")
								? titolo.get("tipologia").get("nome").asText()
								: DEFAULT_VALUE);
				if (titolo.has("altroTipologia")) {
					localWriter.addItem("Altra tipologia titolo di studio", titolo.get("altroTipologia").asText());
				}
				localWriter.addItem("Istituto / Universita'",
						titolo.has("universita") ? titolo.get("universita").asText() : DEFAULT_VALUE);

				// new
				localWriter.addItem("Denominazione titolo di studio",
						titolo.has("denominazione") ? titolo.get("denominazione").asText() : DEFAULT_VALUE);
				localWriter.addItem("Votazione",
						titolo.has("votazione") ? titolo.get("votazione").asText() : DEFAULT_VALUE);

				localWriter.addItem("Anno scolastico/accademico",
						titolo.has("annoAccademico") ? titolo.get("annoAccademico").asText() : DEFAULT_VALUE);
			}
		}
		if (data.has("cv"))
		{
			localWriter.addSection("Curriculum Vitae", "");
			JsonNode cv = data.get("cv");
			
			if (cv.has("titoli") && cv.get("titoli") != null && cv.get("titoli").isArray()) {
				ArrayNode nucleo = (ArrayNode) cv.get("titoli");
				Iterator<JsonNode> it = nucleo.iterator();
				Iterator<JsonNode> itControllo = nucleo.iterator();
				JsonNode controllo = null;
				if (itControllo.hasNext())
					controllo = itControllo.next();
				if (itControllo != null && controllo.has("tipologia") && controllo.get("tipologia").has("nome")
						&& !controllo.get("tipologia").get("nome").asText().isEmpty()) {
					localWriter.addSubSection("Altri titoli di studio (diversi da quelli indicati nella domanda)", "");
					while (it.hasNext()) {
						JsonNode componente = it.next();
						if (componente.has("tipologia") && componente.get("tipologia").has("nome")) 
						{
							localWriter.addItem("Tipologia titolo",
									componente.get("tipologia").has("nome")
											? componente.get("tipologia").get("nome").asText()
											: DEFAULT_VALUE);
	
							// new
							if (componente.get("tipologia").get("nome").asText().equals("Altro Titolo")) {
								localWriter.addItem("Altra tipologia titolo di studio",
										componente.has("altroTipologia") ? componente.get("altroTipologia").asText()
												: DEFAULT_VALUE);
							} else {
								localWriter.addItem("Denominazione titolo di studio",
										componente.has("denominazione") ? componente.get("denominazione").asText()
												: DEFAULT_VALUE);
							}
							localWriter.addItem("Votazione",
									componente.has("votazione") ? componente.get("votazione").asText() : DEFAULT_VALUE);
	
							localWriter.addItem("Istituto/Università",
									componente.has("universita") ? componente.get("universita").asText() : DEFAULT_VALUE);
							localWriter.addItem("Anno scolastico/accademico",
									componente.has("annoAccademico") ? componente.get("annoAccademico").asText()
											: DEFAULT_VALUE);
						}
					}
				}
			}

			if (cv.has("madrelingua") && !cv.get("madrelingua").asText().isEmpty()) {
				localWriter.addSubSection("Madrelingua", "");
				localWriter.addItem("Madrelingua",
						cv.has("madrelingua") ? cv.get("madrelingua").asText() : DEFAULT_VALUE);
			}

			if (cv.has("competenze") && cv.get("competenze") != null && cv.get("competenze").isArray()) {
				ArrayNode nucleo = (ArrayNode) cv.get("competenze");
				Iterator<JsonNode> it = nucleo.iterator();
				Iterator<JsonNode> itControllo = nucleo.iterator();
				JsonNode controllo = null;
				if (itControllo.hasNext())
					controllo = itControllo.next();
				if (itControllo != null && controllo.has("lingue") && !controllo.get("lingue").asText().isEmpty()) {
					localWriter.addSubSection("Competenze linguistiche", "");
					while (it.hasNext()) {
						JsonNode componente = it.next();

						localWriter.addItem("Lingua",
								componente.has("lingue") ? componente.get("lingue").asText() : DEFAULT_VALUE);
						localWriter.addItem("Livello",
								componente.has("livello") ? componente.get("livello").asText() : DEFAULT_VALUE);
					}
				}
			}

			if (cv.has("altreCompetenze") && cv.get("altreCompetenze").has(0)) {
				JsonNode altreCompetenze = cv.get("altreCompetenze").get(0);
				JsonNode competenzeTecniche = altreCompetenze.get("competenzeTecniche");
				JsonNode competenzeOrganizzative = altreCompetenze.get("competenzeOrganizzative");
				JsonNode competenzeRelazionali = altreCompetenze.get("competenzeRelazionali");
				JsonNode competenzePersonali = altreCompetenze.get("competenzePersonali");
				boolean hasTec = !competenzeTecniche.isNull() && !competenzeTecniche.asText().isBlank();
				boolean hasOrg = !competenzeOrganizzative.isNull() && !competenzeOrganizzative.asText().isBlank();
				boolean hasRel = !competenzeRelazionali.isNull() && !competenzeRelazionali.asText().isBlank();
				boolean hasArt = !competenzePersonali.isNull() && !competenzePersonali.asText().isBlank();

				if (hasTec || hasOrg || hasRel || hasArt) {
					localWriter.addSubSection("Altre Competenze", "");
					if (hasTec)
						localWriter.addItem("Competenze tecniche", competenzeTecniche.asText());
					if (hasOrg)
						localWriter.addItem("Competenze organizzative", competenzeOrganizzative.asText());
					if (hasRel)
						localWriter.addItem("Competenze relazionali", competenzeRelazionali.asText());
					if (hasArt)
						localWriter.addItem("Competenze personali", competenzePersonali.asText());
				}

			}
			
			// Esperienze professionali
			localWriter.addSubSection("Esperienze professionali", "");
			localWriter.addItem("Esperienze professionali precedenti",
					cv.has("infoEsperienze") ? cv.get("infoEsperienze").asText() : DEFAULT_VALUE);
			if (cv.has("infoEsperienze") && cv.get("infoEsperienze").asText().equals("si")) {
				if (cv.has("esperienze") && cv.get("esperienze") != null && cv.get("esperienze").isArray()) {
					ArrayNode nucleo = (ArrayNode) cv.get("esperienze");
					int progressivo = 1;
					Iterator<JsonNode> it = nucleo.iterator();
					while (it.hasNext()) {
						JsonNode componente = it.next();

						localWriter.addItem("Esperienza professionale " + progressivo,
								componente.has("originalName") ? componente.get("originalName").asText()
										: DEFAULT_VALUE);
						progressivo++;
						
						localWriter.addItem("Data inizio",
								componente.has("dataInizio") ? componente.get("dataInizio").asText() : DEFAULT_VALUE);
						localWriter.addItem("Data fine",
								componente.has("dataFine") ? componente.get("dataFine").asText() : DEFAULT_VALUE);

						localWriter.addItem("Tipologia rapporto di lavoro ",
								componente.has("rapportoLavoro") ? componente.get("rapportoLavoro").asText()
										: DEFAULT_VALUE);

						if (componente.has("percentualePartTime")) {
							localWriter.addItem("Percentuale part time",
									componente.has("percentualePartTime")
											? componente.get("percentualePartTime").asInt() + ""
											: DEFAULT_VALUE);
						}

						localWriter.addItem("Ente/Azienda",
								componente.has("sede") ? componente.get("sede").asText() : DEFAULT_VALUE);
						
						localWriter.addItem("Profilo",
								componente.has("profilo1") ? componente.get("profilo1").asText() : DEFAULT_VALUE);
						localWriter.addItem("Categoria",
								componente.has("categoria") ? componente.get("categoria").asText() : DEFAULT_VALUE);
						localWriter.addItem("Principali mansioni o responsabilità",
								componente.has("mansioni") ? componente.get("mansioni").asText() : DEFAULT_VALUE);
					}
				}
			}
			
			// Congressi
			localWriter.addSubSection("Corsi /convegni /congressi", "");
			localWriter.addItem("Hai partecipato a corsi o convegni o congressi?",
					cv.has("infoCorsi") ? cv.get("infoCorsi").asText() : DEFAULT_VALUE);
			if (cv.has("infoCorsi") && cv.get("infoCorsi").asText().equals("si")) {			

				util.writeTextValue("Elenco corsi, convegni e congressi", cv, "corsiconvegniDettaglio");
				
			}
			
			localWriter.addSubSection("Certificazioni", "");
			localWriter.addItem("Hai conseguito delle certificazioni?",
					cv.has("certificazioni") ? cv.get("certificazioni").asText() : DEFAULT_VALUE);
			if (cv.has("certificazioni") && cv.get("certificazioni").asText().equals("si")) {			

				util.writeTextValue("Elenco certificazioni", cv, "certificazioniDettaglio");
				
			}
		}
		
		if (data.has("contatti"))
		{
			localWriter.addSection("Dati di contatto", "");
			JsonNode contatto = data.get("contatti");
			
			localWriter.addItem("Email", contatto.has("email") ? contatto.get("email").asText() : DEFAULT_VALUE);
			localWriter.addItem("Conferma email", contatto.has("confermaEmail") ? contatto.get("confermaEmail").asText() : DEFAULT_VALUE);
			localWriter.addItem("Pec", contatto.has("pec") ? contatto.get("pec").asText() : DEFAULT_VALUE);
			localWriter.addItem("Telefono",	contatto.has("telefono") ? contatto.get("telefono").asText() : DEFAULT_VALUE);
			if (contatto.has("profiloLinkedin")) {
				util.writeTextValue("profilo LinkedIn", contatto, "profiloLinkedin");
				
			}
		}
		
			
		if (data.has("allegati")) {
			JsonNode allegati = data.get("allegati");
			if (allegati.has("docIdentita") && allegati.get("docIdentita").get(0) != null) {
				localWriter.addItem("Documento di identità", allegati.get("docIdentita").get(0).get("originalName").asText());
			}

		}

		return localWriter.write();		

	}

	private static JsonNode readStrutturaModulo(ModuloStrutturaEntity strutturaEntity) throws JsonParseException, JsonMappingException, IOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::readStrutturaModulo] IN strutturaEntity: "+strutturaEntity);

			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode result = objectMapper.readValue(strutturaEntity.getStruttura(), JsonNode.class);

			return result;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::readStrutturaModulo] ERROR "+e.getMessage());
			throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readStrutturaModulo] END");
		}
	}

}
