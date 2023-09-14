/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.renderer.template.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TCR_DISC_ACCOGLIMENTODataModel extends DataModel
{
    public static class Document extends DataModel.Document
    {
//        @JsonProperty
//        public String title;
        @JsonProperty
        @JsonDeserialize(as = DatiAccoglimento.class, contentAs = DatiAccoglimento.class)
        public DatiAccoglimento datiAccoglimento;
        @JsonProperty
        @JsonDeserialize(as = Anagrafica.class, contentAs = Anagrafica.class)
        public Anagrafica anagrafica;
        @JsonProperty
        @JsonDeserialize(as = HeaderFooter.class, contentAs = HeaderFooter.class)
        public HeaderFooter header;
        @JsonProperty
        @JsonDeserialize(as = HeaderFooter.class, contentAs = HeaderFooter.class)
        public HeaderFooter footer;
        @JsonProperty
        @JsonDeserialize(as = Richiesta.class, contentAs = Richiesta.class)
        public Richiesta richiesta;
    }
    
    public static class Anagrafica
    {
        @JsonProperty
        public String cognome;
        @JsonProperty
        public String nome;
        @JsonProperty
        public String indirizzo;
        @JsonProperty
        public String comune;
        @JsonProperty
        public String provincia;
        @JsonProperty
        public String cap;
        @JsonProperty
        public String codiceFiscale;
    }
    
    public static class DatiAccoglimento
    {
        @JsonProperty
        public String classificazioneDOQUI;
        @JsonProperty
        public String numeroAccertamento;
        @JsonProperty
        public String numProtocolloIngresso;
        @JsonProperty
	 	public String numProtocolloUscita;
        @JsonProperty
        public String annoPagamento;
        @JsonProperty
        public String dataScadenza;
    }
    
    public static class HeaderFooter
    {
        @JsonProperty
        public String left;
        @JsonProperty
        public String center;
        @JsonProperty
        public String right;
    }
    
    public static class Richiesta
    {
        @JsonProperty
        public String data;
        @JsonProperty
        public String numeroIstanza;
    }
    
 
    

}