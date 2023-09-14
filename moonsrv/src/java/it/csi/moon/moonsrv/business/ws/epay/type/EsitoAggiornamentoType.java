/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per EsitoAggiornamentoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="EsitoAggiornamentoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="ElencoPosizioniDebitorieAggiornate"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="PosizioneDebitoriaAggiornata" type="{http://www.csi.it/epay/epaywso/types}PosizioneDebitoriaType" maxOccurs="1000"/&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EsitoAggiornamentoType", namespace = "http://www.csi.it/epay/epaywso/types", propOrder = {
    "elencoPosizioniDebitorieAggiornate"
})
public class EsitoAggiornamentoType {

    @XmlElement(name = "ElencoPosizioniDebitorieAggiornate", required = true)
    protected EsitoAggiornamentoType.ElencoPosizioniDebitorieAggiornate elencoPosizioniDebitorieAggiornate;

    /**
     * Recupera il valore della propriet� elencoPosizioniDebitorieAggiornate.
     * 
     * @return
     *     possible object is
     *     {@link EsitoAggiornamentoType.ElencoPosizioniDebitorieAggiornate }
     *     
     */
    public EsitoAggiornamentoType.ElencoPosizioniDebitorieAggiornate getElencoPosizioniDebitorieAggiornate() {
        return elencoPosizioniDebitorieAggiornate;
    }

    /**
     * Imposta il valore della propriet� elencoPosizioniDebitorieAggiornate.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoAggiornamentoType.ElencoPosizioniDebitorieAggiornate }
     *     
     */
    public void setElencoPosizioniDebitorieAggiornate(EsitoAggiornamentoType.ElencoPosizioniDebitorieAggiornate value) {
        this.elencoPosizioniDebitorieAggiornate = value;
    }


    /**
     * &lt;p&gt;Classe Java per anonymous complex type.
     * 
     * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * &lt;pre&gt;
     * &amp;lt;complexType&amp;gt;
     *   &amp;lt;complexContent&amp;gt;
     *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *       &amp;lt;sequence&amp;gt;
     *         &amp;lt;element name="PosizioneDebitoriaAggiornata" type="{http://www.csi.it/epay/epaywso/types}PosizioneDebitoriaType" maxOccurs="1000"/&amp;gt;
     *       &amp;lt;/sequence&amp;gt;
     *     &amp;lt;/restriction&amp;gt;
     *   &amp;lt;/complexContent&amp;gt;
     * &amp;lt;/complexType&amp;gt;
     * &lt;/pre&gt;
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "posizioneDebitoriaAggiornata"
    })
    public static class ElencoPosizioniDebitorieAggiornate {

        @XmlElement(name = "PosizioneDebitoriaAggiornata", namespace = "http://www.csi.it/epay/epaywso/types", required = true)
        protected List<PosizioneDebitoriaType> posizioneDebitoriaAggiornata;

        /**
         * Gets the value of the posizioneDebitoriaAggiornata property.
         * 
         * &lt;p&gt;
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the posizioneDebitoriaAggiornata property.
         * 
         * &lt;p&gt;
         * For example, to add a new item, do as follows:
         * &lt;pre&gt;
         *    getPosizioneDebitoriaAggiornata().add(newItem);
         * &lt;/pre&gt;
         * 
         * 
         * &lt;p&gt;
         * Objects of the following type(s) are allowed in the list
         * {@link PosizioneDebitoriaType }
         * 
         * 
         */
        public List<PosizioneDebitoriaType> getPosizioneDebitoriaAggiornata() {
            if (posizioneDebitoriaAggiornata == null) {
                posizioneDebitoriaAggiornata = new ArrayList<>();
            }
            return this.posizioneDebitoriaAggiornata;
        }

    }

}
