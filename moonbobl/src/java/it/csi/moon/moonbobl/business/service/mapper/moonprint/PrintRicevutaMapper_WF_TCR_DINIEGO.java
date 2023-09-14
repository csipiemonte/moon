/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper.moonprint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RicevutaTcrDiniegoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonprint.CognomeNomePatVei;
import it.csi.moon.moonbobl.dto.moonprint.DocumentoDiniego;
import it.csi.moon.moonbobl.dto.moonprint.DocumentoRicevuta;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoDiniego;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoRicevuta;
import it.csi.moon.moonbobl.util.LoggerAccessor;

public class PrintRicevutaMapper_WF_TCR_DINIEGO {
	private final static String CLASS_NAME = "PrintRicevutaMapper_WF_TCR_DINIEGO";
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
	public MoonprintDocumentoDiniego remap(Istanza istanza, RicevutaTcrDiniegoEntity ricevutaEntity, String numeroProtocolloUscita) throws Exception {
		log.debug("[" + CLASS_NAME + "::remap] IN istanza.data="+istanza.getData());
		log.debug("[" + CLASS_NAME + "::remap] IN ricevutaEntity="+ricevutaEntity);
		final String DEFAULT_VALUE = "";
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		//Lettura delle due strutture variabile
		//DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
		JsonNode istanzaJsonNode = DatiIstanzaHelper.readIstanzaData(istanza);
		
		DocumentoDiniego document = new DocumentoDiniego();		
		document.title = istanza.getModulo().getDescrizioneModulo();
		//log.debug("[" + CLASS_NAME + "::remap] IN ricevutaEntity="+ricevutaEntity);
		
		// richiesta
		document.richiesta.data = df.format(new Date());
		document.richiesta.numeroIstanza = istanza.getCodiceIstanza();
		
		// anagrafica
		// anagrafica.richiedente
		document.anagrafica.nome = ricevutaEntity.getNome();
		document.anagrafica.cognome =ricevutaEntity.getCognome();
		document.anagrafica.indirizzo=ricevutaEntity.getIndirizzo();
		document.anagrafica.comune= ricevutaEntity.getCitta();
		document.anagrafica.provincia=ricevutaEntity.getProvincia();
		document.anagrafica.cap=ricevutaEntity.getCap();
		document.anagrafica.codiceFiscale=ricevutaEntity.getCodiceFiscale();
		
		//datiDiniego
		//log.debug("[" + CLASS_NAME + "::remap] IN getClassificazioneDOQUI="+ricevutaEntity.getClassificazioneDOQUI());
		document.datiDiniego.numProtocolloUscita = numeroProtocolloUscita;
		
		document.datiDiniego.numeroAccertamento=ricevutaEntity.getNumAccertamento();
		document.datiDiniego.numProtocolloIngresso=istanza.getNumeroProtocollo();
		document.datiDiniego.annoPagamento=ricevutaEntity.getAnnoPagamento();
		document.datiDiniego.dataScadenza=ricevutaEntity.getDataScadenza();
		if(ricevutaEntity.getTipologiaTemplate().contains("conMotivazione"))
			document.datiDiniego.motivazioni=ricevutaEntity.getMotivazioni();
		
		document.datiDiniego.classificazioneDOQUI=ricevutaEntity.getClassificazioneDOQUI();
		
		
		// header
		document.header.left = "Domanda n. %%numeroIstanza%%";
		document.header.center = "";
		document.header.right = "Mod. %%module%%";
		// footer
		document.footer.left = "";
		document.footer.right = "";
		log.debug("[" + CLASS_NAME + "::remap] OUT document="+document);
		return new MoonprintDocumentoDiniego(document);
	}
}
