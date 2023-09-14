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
 * &lt;p&gt;Classe Java per CorpoAvvisiScadutiType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="CorpoAvvisiScadutiType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="ElencoAvvisiScaduti"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="AvvisoScaduto" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}AvvisoScadutoType" maxOccurs="1000"/&amp;gt;
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
@XmlType(name = "CorpoAvvisiScadutiType", propOrder = {
    "elencoAvvisiScaduti"
})
public class CorpoAvvisiScadutiType {

    @XmlElement(name = "ElencoAvvisiScaduti", required = true)
    protected CorpoAvvisiScadutiType.ElencoAvvisiScaduti elencoAvvisiScaduti;

    /**
     * Recupera il valore della propriet� elencoAvvisiScaduti.
     * 
     * @return
     *     possible object is
     *     {@link CorpoAvvisiScadutiType.ElencoAvvisiScaduti }
     *     
     */
    public CorpoAvvisiScadutiType.ElencoAvvisiScaduti getElencoAvvisiScaduti() {
        return elencoAvvisiScaduti;
    }

    /**
     * Imposta il valore della propriet� elencoAvvisiScaduti.
     * 
     * @param value
     *     allowed object is
     *     {@link CorpoAvvisiScadutiType.ElencoAvvisiScaduti }
     *     
     */
    public void setElencoAvvisiScaduti(CorpoAvvisiScadutiType.ElencoAvvisiScaduti value) {
        this.elencoAvvisiScaduti = value;
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
     *         &amp;lt;element name="AvvisoScaduto" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}AvvisoScadutoType" maxOccurs="1000"/&amp;gt;
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
        "avvisoScaduto"
    })
    public static class ElencoAvvisiScaduti {

        @XmlElement(name = "AvvisoScaduto", required = true)
        protected List<AvvisoScadutoType> avvisoScaduto;

        /**
         * Gets the value of the avvisoScaduto property.
         * 
         * &lt;p&gt;
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the avvisoScaduto property.
         * 
         * &lt;p&gt;
         * For example, to add a new item, do as follows:
         * &lt;pre&gt;
         *    getAvvisoScaduto().add(newItem);
         * &lt;/pre&gt;
         * 
         * 
         * &lt;p&gt;
         * Objects of the following type(s) are allowed in the list
         * {@link AvvisoScadutoType }
         * 
         * 
         */
        public List<AvvisoScadutoType> getAvvisoScaduto() {
            if (avvisoScaduto == null) {
                avvisoScaduto = new ArrayList<>();
            }
            return this.avvisoScaduto;
        }

    }

}
