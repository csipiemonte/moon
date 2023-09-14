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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.dem.ComponentiFamigliaEntity;
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
public class PrintRicevutaMapper_RIC_DEM_CAMBIO_RES  {
	
	private final static String CLASS_NAME = "PrintRicevutaMapper_RIC_DEM_CAMBIO_RES";
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
	public MoonprintDocumentoRicevuta remap(Istanza istanza, RicevutaCambioResidEntity ricevutaEntity) throws Exception {
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
		// accettazione
		document.accettazione.data = ricevutaEntity.getDataRegistrazione();
		document.accettazione.modulo = istanza.getModulo().getCodiceModulo() + " " + istanza.getModulo().getVersioneModulo();
		document.accettazione.funzionarioResponsabile = ricevutaEntity.getFunzionario(); 
		document.accettazione.ufficialeAnagrafe = ricevutaEntity.getOperatore();
		document.accettazione.rif = ricevutaEntity.getNumeroPraticaNao(); 
		
		// anagrafica
		// anagrafica.richiedente

		document.anagrafica.richiedente.cognome = (data.has("richiedente") && data.get("richiedente").has("cognome")) ? data.get("richiedente").get("cognome").getTextValue().toUpperCase() : "";
		document.anagrafica.richiedente.nome = (data.has("richiedente") && data.get("richiedente").has("nome")) ? data.get("richiedente").get("nome").getTextValue().toUpperCase() : "";
				
		document.anagrafica.richiedente.numeroPatente = data.has("richiedente") ? (data.get("richiedente").get("numeroPatente")!=null ? data.get("richiedente").get("numeroPatente").getTextValue() : "") :"";
		document.anagrafica.richiedente.targaVeicolo = "";
		
		String targheVeicoli = "";
		if (data.has("richiedente") && data.get("richiedente").get("veicoli")!= null && data.get("richiedente").get("veicoli").isArray())
		{			
			ArrayNode veicoli = (ArrayNode) data.get("richiedente").get("veicoli");			
			Iterator<JsonNode> it = veicoli.iterator();			
			int i=1;
			String targa = "";
			while (it.hasNext() ) {
				JsonNode elemento = it.next();
				targa = elemento.get("targa") != null ? (i != veicoli.size() ? elemento.get("targa").getTextValue()+", ":elemento.get("targa").getTextValue())  : "";
				targheVeicoli = targheVeicoli + targa;				
				i++;
			}			
			targheVeicoli = targheVeicoli.trim();	
			if (!targheVeicoli.equals("") && targheVeicoli.length() > 1)
			{				
				targheVeicoli = targheVeicoli.substring(targheVeicoli.length() - 1).equals(",") ? StringUtils.chop(targheVeicoli): targheVeicoli;			
			}
			document.anagrafica.richiedente.targaVeicolo = targheVeicoli;
		}
		
		// anagrafica.nucleoFamiliare	
		document.anagrafica.fraseIntestazione = "per sè solo/a.";
		
		Boolean soggettoTrovatoInAnpr = false;
		if (data.has("soggettoTrovatoInAnpr") && data.get("soggettoTrovatoInAnpr").asBoolean()) {
			soggettoTrovatoInAnpr = true;
		}
		Boolean altriComponenti = false;
		if (soggettoTrovatoInAnpr) {
			if (data.has("altriComponentiAnpr") && data.get("altriComponentiAnpr").getTextValue().equals("altriComponentiSi")) {
				altriComponenti = true;
			}
		}
		else {
			if (data.has("altriComponenti") && data.get("altriComponenti").getTextValue().equals("altriComponentiSi")) {
				altriComponenti = true;
			}
			else if (data.has("altriComponenti") && data.get("altriComponenti").getTextValue().equals("altriComponentiNo")) {
				altriComponenti = false;
			}
			else {
				// gestione versione 1.0.0 del modulo
				if (data.has("componente") && data.get("componente") != null && data.get("componente").isArray()){
					altriComponenti = true;
				}
			}
		}
		if (altriComponenti)  {
			ArrayNode elencoDati = null;
			Boolean componenteIsValid = false;
			if (data.has("componente") && data.get("componente") != null)
			{
				elencoDati = (ArrayNode)data.get("componente");
				Iterator<JsonNode> it = elencoDati.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();
					if (elemento.has("cf") && !elemento.get("cf").getTextValue().equals("")) {
						componenteIsValid = true;
					}
				}
			}
			if (!componenteIsValid && data.has("componenteAnpr") && data.get("componenteAnpr") != null)
			{
				elencoDati = (ArrayNode)data.get("componenteAnpr");
			}
			if (elencoDati != null)
			{
				Iterator<JsonNode> it = elencoDati.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();
					if ("si".equalsIgnoreCase(elemento.get("siTrasferisceAlNuovoIndirizzo").getTextValue())) {
						CognomeNomePatVei cognomeNomePatVei = new CognomeNomePatVei();
						cognomeNomePatVei.cognome = elemento.get("cognome")!=null?elemento.get("cognome").getTextValue().toUpperCase():"";
						cognomeNomePatVei.nome = elemento.get("nome")!=null?elemento.get("nome").getTextValue().toUpperCase():"";
						cognomeNomePatVei.numeroPatente = elemento.get("numeroPatente")!=null?elemento.get("numeroPatente").getTextValue():"";						   
						cognomeNomePatVei.targaVeicolo = elemento.get("targhe")!=null?elemento.get("targhe").getTextValue():"";
						document.anagrafica.nucleoFamiliare.add(cognomeNomePatVei);			
					}
				}
				// in componente non è compreso il dichiarante, quindi se si sposta con altri elencoDati deve essere maggiore di 0
				if (document.anagrafica.nucleoFamiliare.size() > 0) {
					document.anagrafica.fraseIntestazione = "per sè e per i seguenti membri del nucleo familiare:"; 
				}
			}
		}

		// anagrafica.nuovoIndirizzo
		document.anagrafica.nuovoIndirizzo.comune = data.has("nuovoindirizzo")?  (data.get("nuovoindirizzo")!=null ? data.get("nuovoindirizzo").get("comune").getTextValue() : ""):"";	
				
		JsonNode nuovoindirizzo = data.get("nuovoindirizzo");
		String indirizzo = nuovoindirizzo.get("civicocompleto").get("nome").getTextValue();
		document.anagrafica.nuovoIndirizzo.indirizzo = indirizzo;
		
		String indirizzoCompleto = "";
		String civicocompletoflagUiu = nuovoindirizzo.get("civicocompleto").get("flagUiu").getTextValue();
		if (civicocompletoflagUiu.equals("S")) {			
			indirizzoCompleto = indirizzo + (nuovoindirizzo.has("pianoNui") ? ", "+nuovoindirizzo.get("pianoNui").get("nome").getTextValue() : "");
		}
		if (civicocompletoflagUiu.equals("N")) {			
			indirizzoCompleto = indirizzo + (nuovoindirizzo.has("piano") ? ", "+nuovoindirizzo.get("piano").get("nome").getTextValue() : "");
		}		
		document.anagrafica.nuovoIndirizzo.indirizzoCompleto = indirizzoCompleto;
		
		if (data.has("richiedente"))
		{
			JsonNode richiedente = data.get("richiedente");
			if (richiedente.has("provenienza") && richiedente.get("provenienza") != null)
			{
				JsonNode provenienza = richiedente.get("provenienza");
				String tipo = getFieldCodice(provenienza,"tipo");
				String luogo = "";
				String stato = "";
				if (provenienza.has("stato") && provenienza.get("stato").has("nome")) {
					stato = provenienza.get("stato").get("nome").getTextValue();
				}
				else {
					stato = provenienza.has("stato") ?  provenienza.get("stato").getTextValue() : DEFAULT_VALUE;
				}
				if (stato == null) {
					stato = "";
				}
				if (tipo.equalsIgnoreCase("italia")) {
					String prov = provenienza.get("provincia").get("nome").getTextValue();
					String comune = provenienza.get("comune").get("nome").getTextValue();
					luogo = comune + " ("+prov+") ";
				}
				else {
					if (!stato.equals("ITALIA"))
					{	
						String citta = (provenienza.has("cittaestera"))? provenienza.get("cittaestera").getTextValue():DEFAULT_VALUE ;
						luogo = citta + ", " +  stato;
					}
					else {
						luogo = stato;
					}
				}
				document.anagrafica.comuneProvenienza = luogo;
			}
		}
		
/*		if (data.has("richiedente") && data.get("richiedente").has("provenienza") && data.get("richiedente").get("provenienza").has("comune"))
		{			
			document.anagrafica.comuneProvenienza = (data.get("richiedente").get("provenienza").get("comune").has("nome")) ? 
					data.get("richiedente").get("provenienza").get("comune").get("nome").getTextValue() : "";		
		}*/
		
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

	public String getFieldNome(JsonNode node, String fieldName ) {
		String filedNome = "";
		if ( node.has(fieldName) && node.get(fieldName).has("nome") )
			filedNome = node.get(fieldName).get("nome").getTextValue();
		else if ( node.has(fieldName) && node.get(fieldName).isTextual() )
			filedNome = node.get(fieldName).getTextValue();
		else
			filedNome = "";
		return filedNome;
	}
	
	public String getFieldCodice(JsonNode node, String fieldName ) {
		String filedNome = "";
		if ( node.has(fieldName) && node.get(fieldName).has("codice") )
			filedNome = node.get(fieldName).get("codice").getTextValue();
		else if ( node.has(fieldName) && node.get(fieldName).isTextual() )
			filedNome = node.get(fieldName).getTextValue();
		else
			filedNome = "";
		return filedNome;
	}
	

}
