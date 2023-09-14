/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.renderer.template.datamodel;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RicevutaCambioIndirizzoDataModel extends DataModel
{
    public static class Document extends DataModel.Document
    {
//        @JsonProperty
//        public String title;
        @JsonProperty
        @JsonDeserialize(as = Richiesta.class, contentAs = Richiesta.class)
        public Richiesta richiesta;
        @JsonProperty
        @JsonDeserialize(as = Accettazione.class, contentAs = Accettazione.class)
        public Accettazione accettazione;
        @JsonProperty
        @JsonDeserialize(as = Anagrafica.class, contentAs = Anagrafica.class)
        public Anagrafica anagrafica;
        @JsonProperty
        @JsonDeserialize(as = HeaderFooter.class, contentAs = HeaderFooter.class)
        public HeaderFooter header;
        @JsonProperty
        @JsonDeserialize(as = HeaderFooter.class, contentAs = HeaderFooter.class)
        public HeaderFooter footer;
    }
    
    public static class Anagrafica
    {
        @JsonProperty
        @JsonDeserialize(as = Persona.class, contentAs = Persona.class)
        public Persona richiedente;
        @JsonProperty
        @JsonDeserialize(as = ArrayList.class, contentAs = Persona.class)
        public List<Persona> nucleoFamiliare;
        @JsonProperty
        @JsonDeserialize(as = Indirizzo.class, contentAs = Indirizzo.class)
        public Indirizzo nuovoIndirizzo;
        @JsonProperty
        public String comuneProvenienza;
        @JsonProperty
        public String fraseIntestazione;
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
    
    public static class Accettazione
    {
        @JsonProperty
        public String data;
        @JsonProperty
        public String modulo;
        @JsonProperty
        public String funzionarioResponsabile;
        @JsonProperty
        public String rif;
        @JsonProperty
        public String ufficialeAnagrafe;
    }
    
    public static class Persona
    {
        @JsonProperty
        public String cognome;
        @JsonProperty
        public String nome;
        @JsonProperty
        public String numeroPatente;
        @JsonProperty
        public String targaVeicolo;
    }
    
    public static class Indirizzo
    {
        @JsonProperty
        public String comune;
        @JsonProperty
        public String indirizzo;
        @JsonProperty
        public String indirizzoCompleto;
    }
}