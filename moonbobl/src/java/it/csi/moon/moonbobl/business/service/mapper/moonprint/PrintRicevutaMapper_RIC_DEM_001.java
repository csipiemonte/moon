/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper.moonprint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.dem.RicevutaCambioResidEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonprint.CognomeNomePatVei;
import it.csi.moon.moonbobl.dto.moonprint.DocumentoRicevuta;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoRicevuta;
import it.csi.moon.moonbobl.util.LoggerAccessor;

/**
 * Contruttore di oggetto JSON per MOOnPrint
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class PrintRicevutaMapper_RIC_DEM_001  {
	
	private final static String CLASS_NAME = "PrintRicevutaMapper_RIC_DEM_001";
	private final static Logger log = LoggerAccessor.getLoggerBusiness();
	
    /**
     * Remap l'istanza con il suo modulo  in un oggetto per MOOnPrint
     * 
     * @param istanza
     * @param strutturaEntity
     * 
     * @return MoonprintDocumentoRicevuta che contiene Module.Document di MOOnPrint
     * 
     * @throws Exception 
     */
	public MoonprintDocumentoRicevuta remap(/*CambioResidenzaEntity cambioResidenzaEntity*/ Istanza istanza, RicevutaCambioResidEntity ricevutaEntity) throws Exception {
		log.debug("[" + CLASS_NAME + "::remap] Per GeneraRicevuta ");
		log.debug("[" + CLASS_NAME + "::remap] IN istanza.data="+istanza.getData());
		log.debug("[" + CLASS_NAME + "::remap] IN ricevutaEntity="+ricevutaEntity);
		final String DEFAULT_VALUE = "";
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		// Lettura delle due strutture variabile
//		DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
		JsonNode istanzaJsonNode = DatiIstanzaHelper.readIstanzaData(istanza);
		JsonNode data = istanzaJsonNode.get("data");
		
		DocumentoRicevuta document = new DocumentoRicevuta();
		
		document.title = istanza.getModulo().getDescrizioneModulo();
		
		// richiesta
		document.richiesta.data = df.format(istanza.getCreated());
		document.richiesta.numeroIstanza = istanza.getCodiceIstanza();
		
		// accettazione
		document.accettazione.data = ricevutaEntity.getDataRegistrazione();
		document.accettazione.modulo = istanza.getModulo().getCodiceModulo() + " " + istanza.getModulo().getVersioneModulo();
		document.accettazione.funzionarioResponsabile = ricevutaEntity.getFunzionario(); 
		document.accettazione.ufficialeAnagrafe = ricevutaEntity.getOperatore();
		document.accettazione.rif = ricevutaEntity.getNumeroPraticaNao(); 

		// anagrafica
		// anagrafica.richiedente
		document.anagrafica.richiedente.cognome = data.get("cognome")!=null ? data.get("cognome").getTextValue() : "";
		document.anagrafica.richiedente.nome = data.get("nome")!=null ? data.get("nome").getTextValue() : "";
		document.anagrafica.richiedente.numeroPatente = ""; 
		document.anagrafica.richiedente.targaVeicolo = "";
		// anagrafica.nucleoFamiliare
		document.anagrafica.fraseIntestazione = "per sè solo/a.";
		if (data.has("dgNucleofamiliare") && data.get("dgNucleofamiliare") != null	&& data.get("dgNucleofamiliare").isArray()) {
			// DEM_001
			ArrayNode elencoDati = (ArrayNode)data.get("dgNucleofamiliare");
			Iterator<JsonNode> it = elencoDati.iterator();
			while (it.hasNext()) {
				JsonNode elemento = it.next();
				if (!elemento.get("dgNucleofamiliareCambioIndirizzo").getTextValue().equals("N")) {
					if (elemento.get("dgNucleofamiliareCodiceFiscale")!=null && elemento.get("dgNucleofamiliareCodiceFiscale").getTextValue().equals(data.get("codiceFiscale").getTextValue())) {
						document.anagrafica.richiedente.numeroPatente = elemento.get("numeroPatente")!=null?elemento.get("numeroPatente").getTextValue():"";
						document.anagrafica.richiedente.targaVeicolo = elemento.get("targhe")!=null?elemento.get("targhe").getTextValue():"";
					} else {
						CognomeNomePatVei cognomeNomePatVei = new CognomeNomePatVei();
						cognomeNomePatVei.cognome = elemento.get("dgNucleofamiliareCognome")!=null?elemento.get("dgNucleofamiliareCognome").getTextValue():"";
						cognomeNomePatVei.nome = elemento.get("dgNucleofamiliareNome")!=null?elemento.get("dgNucleofamiliareNome").getTextValue():"";
						cognomeNomePatVei.numeroPatente = elemento.get("numeroPatente")!=null?elemento.get("numeroPatente").getTextValue():"";
						cognomeNomePatVei.targaVeicolo = elemento.get("targhe")!=null?elemento.get("targhe").getTextValue():"";
						document.anagrafica.nucleoFamiliare.add(cognomeNomePatVei);
					}
				}
			}
			// in dgNucleofamiliare viene elencato anche il dichiarante, quindi se si sposta con altri elencoDati deve essere maggiore di 1
			if (elencoDati.size() > 1) {
				document.anagrafica.fraseIntestazione = "per sè e per i seguenti membri del nucleo familiare:"; 
			}
		}

		// anagrafica.nuovoIndirizzo
		document.anagrafica.nuovoIndirizzo.comune = data.get("indNuovoPanelComune")!=null ? data.get("indNuovoPanelComune").getTextValue() : "";
		
		String indirizzo = data.get("indNuovoCivico")!=null && data.get("indNuovoCivico").get("nome")!=null ? data.get("indNuovoCivico").get("nome").getTextValue() : "";
		document.anagrafica.nuovoIndirizzo.indirizzo = indirizzo;
		
		String indirizzoCompleto = indirizzo;
		if (data.has("indNuovoCivico") && data.get("indNuovoCivico").has("flagUiu")) {

			if ("S".equals(data.get("indNuovoCivico").get("flagUiu").getTextValue())) {
				String pianoNui = (data.has("indNuovoPianoNui") && data.get("indNuovoPianoNui").has("nome") ) ? ", " +data.get("indNuovoPianoNui").get("nome").getTextValue() : "";
				indirizzoCompleto = indirizzo + pianoNui;				

			} else {	
				String piano = (data.has("indNuovoPiano") && data.get("indNuovoPiano").has("codice") ) ?  ", " +data.get("indNuovoPiano").get("codice").getTextValue() : "" ;
				indirizzoCompleto = indirizzo + piano;
			}
			document.anagrafica.nuovoIndirizzo.indirizzoCompleto = indirizzoCompleto;
			log.info("[" + CLASS_NAME + "::remap] OUT indirizzoCompleto = "+indirizzoCompleto);
		}
		document.anagrafica.comuneProvenienza = "Torino";
		
		// header
		document.header.left = "Domanda n. %%numeroIstanza%%";
		document.header.center = "";
		document.header.right = "Mod. %%module%%";
		// footer
		document.footer.left = "";
		document.footer.right = "";
		log.debug("[" + CLASS_NAME + "::remap] OUT document="+document);
		return new MoonprintDocumentoRicevuta(document);
	}


}
