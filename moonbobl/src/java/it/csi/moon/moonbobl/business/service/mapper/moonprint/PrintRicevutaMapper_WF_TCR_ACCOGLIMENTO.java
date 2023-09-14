/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonbobl.business.service.mapper.moonprint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RicevutaTcrAccoglimentoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonprint.DocumentoAccoglimento;
import it.csi.moon.moonbobl.dto.moonprint.MoonPrintDocumentoAccoglimento;

import it.csi.moon.moonbobl.util.LoggerAccessor;



/**
 * Contruttore di oggetto JSON per MOOnPrint
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class PrintRicevutaMapper_WF_TCR_ACCOGLIMENTO  {
	
	private final static String CLASS_NAME = "PrintRicevutaMapper_WF_TCR_ACCOGLIMENTO";
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
	
	public MoonPrintDocumentoAccoglimento remap(Istanza istanza, RicevutaTcrAccoglimentoEntity ricevutaEntity, String numeroProtocolloUscita) throws Exception {
		log.debug("[" + CLASS_NAME + "::remap] IN istanza.data="+istanza.getData());
		log.debug("[" + CLASS_NAME + "::remap] IN ricevutaEntity="+ricevutaEntity);
		final String DEFAULT_VALUE = "";
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		
		// Lettura delle due strutture variabile
//		DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
		JsonNode istanzaJsonNode = DatiIstanzaHelper.readIstanzaData(istanza);
		JsonNode data = istanzaJsonNode.get("data");
			
		DocumentoAccoglimento  document = new DocumentoAccoglimento();
		document.title = istanza.getModulo().getDescrizioneModulo();		
		
		// richiesta
		document.richiesta.data = df.format(new Date());
		document.richiesta.numeroIstanza = istanza.getCodiceIstanza();
		
        //anagrafica
		
        document.anagrafica.cognome = ricevutaEntity.getCognome();
        document.anagrafica.nome = ricevutaEntity.getNome();
        document.anagrafica.indirizzo = ricevutaEntity.getIndirizzo();
        document.anagrafica.comune = ricevutaEntity.getCitta();
        document.anagrafica.provincia = ricevutaEntity.getProvincia();
        document.anagrafica.cap = ricevutaEntity.getCap();
        document.anagrafica.codiceFiscale = ricevutaEntity.getCf();

        //datiApprovazione
        document.datiAccoglimento.numProtocolloUscita = numeroProtocolloUscita;
        document.datiAccoglimento.classificazioneDOQUI = ricevutaEntity.getClassificazioneDOQUI();
        document.datiAccoglimento.numeroAccertamento = ricevutaEntity.getNumAccertamento();
        document.datiAccoglimento.numProtocolloIngresso = istanza.getNumeroProtocollo();
        document.datiAccoglimento.annoPagamento = ricevutaEntity.getAnnoPagamento();
        document.datiAccoglimento.dataScadenza = ricevutaEntity.getScadenza();
        if(ricevutaEntity.getTipologiaTemplate().equals("parzialeConElencoDiMotivazioni") || 
        		ricevutaEntity.getTipologiaTemplate().equals("conAnnullamentoParzialeDellAccertamento")	)
        	document.datiAccoglimento.motivi=ricevutaEntity.getMotivazioni();

        
        document.datiAccoglimento.testoLibero = ricevutaEntity.getTestoLibero();
        document.datiAccoglimento.numeroDetermina = ricevutaEntity.getNumeroDetermina();
        document.datiAccoglimento.dataDetermina = ricevutaEntity.getDataDetermina();
        document.datiAccoglimento.numeroImpegno = ricevutaEntity.getNumeroImpegno();
        
		// header
		document.header.left = "Domanda n. %%numeroIstanza%%";
		document.header.center = "";
		document.header.right = "Mod. %%module%%";
		// footer
		document.footer.left = "";
		document.footer.right = "";
		log.debug("[" + CLASS_NAME + "::remap] OUT document="+document);
		return new MoonPrintDocumentoAccoglimento(document);
	}

}
