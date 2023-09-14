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
 * &lt;p&gt;Classe Java per EsitoInserimentoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="EsitoInserimentoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="ElencoPosizioniDebitorieInserite"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="PosizioneDebitoriaInserita" type="{http://www.csi.it/epay/epaywso/types}PosizioneDebitoriaType" maxOccurs="1000"/&amp;gt;
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
@XmlType(name = "EsitoInserimentoType", namespace = "http://www.csi.it/epay/epaywso/types", propOrder = {
    "elencoPosizioniDebitorieInserite"
})
public class EsitoInserimentoType {

    @XmlElement(name = "ElencoPosizioniDebitorieInserite", required = true)
    protected EsitoInserimentoType.ElencoPosizioniDebitorieInserite elencoPosizioniDebitorieInserite;

    /**
     * Recupera il valore della propriet� elencoPosizioniDebitorieInserite.
     * 
     * @return
     *     possible object is
     *     {@link EsitoInserimentoType.ElencoPosizioniDebitorieInserite }
     *     
     */
    public EsitoInserimentoType.ElencoPosizioniDebitorieInserite getElencoPosizioniDebitorieInserite() {
        return elencoPosizioniDebitorieInserite;
    }

    /**
     * Imposta il valore della propriet� elencoPosizioniDebitorieInserite.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoInserimentoType.ElencoPosizioniDebitorieInserite }
     *     
     */
    public void setElencoPosizioniDebitorieInserite(EsitoInserimentoType.ElencoPosizioniDebitorieInserite value) {
        this.elencoPosizioniDebitorieInserite = value;
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
     *         &amp;lt;element name="PosizioneDebitoriaInserita" type="{http://www.csi.it/epay/epaywso/types}PosizioneDebitoriaType" maxOccurs="1000"/&amp;gt;
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
        "posizioneDebitoriaInserita"
    })
    public static class ElencoPosizioniDebitorieInserite {

        @XmlElement(name = "PosizioneDebitoriaInserita", namespace = "http://www.csi.it/epay/epaywso/types", required = true)
        protected List<PosizioneDebitoriaType> posizioneDebitoriaInserita;

        /**
         * Gets the value of the posizioneDebitoriaInserita property.
         * 
         * &lt;p&gt;
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the posizioneDebitoriaInserita property.
         * 
         * &lt;p&gt;
         * For example, to add a new item, do as follows:
         * &lt;pre&gt;
         *    getPosizioneDebitoriaInserita().add(newItem);
         * &lt;/pre&gt;
         * 
         * 
         * &lt;p&gt;
         * Objects of the following type(s) are allowed in the list
         * {@link PosizioneDebitoriaType }
         * 
         * 
         */
        public List<PosizioneDebitoriaType> getPosizioneDebitoriaInserita() {
            if (posizioneDebitoriaInserita == null) {
                posizioneDebitoriaInserita = new ArrayList<PosizioneDebitoriaType>();
            }
            return this.posizioneDebitoriaInserita;
        }

    }

}
