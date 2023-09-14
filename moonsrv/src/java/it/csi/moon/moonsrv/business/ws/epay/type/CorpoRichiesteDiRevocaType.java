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
 * &lt;p&gt;Classe Java per CorpoRichiesteDiRevocaType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="CorpoRichiesteDiRevocaType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="RichiestaDiRevocaResponse" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}RichiestaDiRevocaResponseType"/&amp;gt;
 *         &amp;lt;element name="ElencoRichiesteDiRevoca"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="RichiestaDiRevoca" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}RichiestaDiRevocaType" maxOccurs="1000"/&amp;gt;
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
@XmlType(name = "CorpoRichiesteDiRevocaType", propOrder = {
    "richiestaDiRevocaResponse",
    "elencoRichiesteDiRevoca"
})
public class CorpoRichiesteDiRevocaType {

    @XmlElement(name = "RichiestaDiRevocaResponse", required = true)
    protected RichiestaDiRevocaResponseType richiestaDiRevocaResponse;
    @XmlElement(name = "ElencoRichiesteDiRevoca", required = true)
    protected CorpoRichiesteDiRevocaType.ElencoRichiesteDiRevoca elencoRichiesteDiRevoca;

    /**
     * Recupera il valore della propriet� richiestaDiRevocaResponse.
     * 
     * @return
     *     possible object is
     *     {@link RichiestaDiRevocaResponseType }
     *     
     */
    public RichiestaDiRevocaResponseType getRichiestaDiRevocaResponse() {
        return richiestaDiRevocaResponse;
    }

    /**
     * Imposta il valore della propriet� richiestaDiRevocaResponse.
     * 
     * @param value
     *     allowed object is
     *     {@link RichiestaDiRevocaResponseType }
     *     
     */
    public void setRichiestaDiRevocaResponse(RichiestaDiRevocaResponseType value) {
        this.richiestaDiRevocaResponse = value;
    }

    /**
     * Recupera il valore della propriet� elencoRichiesteDiRevoca.
     * 
     * @return
     *     possible object is
     *     {@link CorpoRichiesteDiRevocaType.ElencoRichiesteDiRevoca }
     *     
     */
    public CorpoRichiesteDiRevocaType.ElencoRichiesteDiRevoca getElencoRichiesteDiRevoca() {
        return elencoRichiesteDiRevoca;
    }

    /**
     * Imposta il valore della propriet� elencoRichiesteDiRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link CorpoRichiesteDiRevocaType.ElencoRichiesteDiRevoca }
     *     
     */
    public void setElencoRichiesteDiRevoca(CorpoRichiesteDiRevocaType.ElencoRichiesteDiRevoca value) {
        this.elencoRichiesteDiRevoca = value;
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
     *         &amp;lt;element name="RichiestaDiRevoca" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}RichiestaDiRevocaType" maxOccurs="1000"/&amp;gt;
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
        "richiestaDiRevoca"
    })
    public static class ElencoRichiesteDiRevoca {

        @XmlElement(name = "RichiestaDiRevoca", required = true)
        protected List<RichiestaDiRevocaType> richiestaDiRevoca;

        /**
         * Gets the value of the richiestaDiRevoca property.
         * 
         * &lt;p&gt;
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the richiestaDiRevoca property.
         * 
         * &lt;p&gt;
         * For example, to add a new item, do as follows:
         * &lt;pre&gt;
         *    getRichiestaDiRevoca().add(newItem);
         * &lt;/pre&gt;
         * 
         * 
         * &lt;p&gt;
         * Objects of the following type(s) are allowed in the list
         * {@link RichiestaDiRevocaType }
         * 
         * 
         */
        public List<RichiestaDiRevocaType> getRichiestaDiRevoca() {
            if (richiestaDiRevoca == null) {
                richiestaDiRevoca = new ArrayList<>();
            }
            return this.richiestaDiRevoca;
        }

    }

}
