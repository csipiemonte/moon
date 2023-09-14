/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &amp;lt;element name="Testata" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}TestataAvvisiScadutiType"/&amp;gt;
 *         &amp;lt;element name="CorpoAvvisiScaduti" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}CorpoAvvisiScadutiType"/&amp;gt;
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
    "testata",
    "corpoAvvisiScaduti"
})
@XmlRootElement(name = "TrasmettiAvvisiScadutiRequest")
public class TrasmettiAvvisiScadutiRequest {

    @XmlElement(name = "Testata", required = true)
    protected TestataAvvisiScadutiType testata;
    @XmlElement(name = "CorpoAvvisiScaduti", required = true)
    protected CorpoAvvisiScadutiType corpoAvvisiScaduti;

    /**
     * Recupera il valore della propriet� testata.
     * 
     * @return
     *     possible object is
     *     {@link TestataAvvisiScadutiType }
     *     
     */
    public TestataAvvisiScadutiType getTestata() {
        return testata;
    }

    /**
     * Imposta il valore della propriet� testata.
     * 
     * @param value
     *     allowed object is
     *     {@link TestataAvvisiScadutiType }
     *     
     */
    public void setTestata(TestataAvvisiScadutiType value) {
        this.testata = value;
    }

    /**
     * Recupera il valore della propriet� corpoAvvisiScaduti.
     * 
     * @return
     *     possible object is
     *     {@link CorpoAvvisiScadutiType }
     *     
     */
    public CorpoAvvisiScadutiType getCorpoAvvisiScaduti() {
        return corpoAvvisiScaduti;
    }

    /**
     * Imposta il valore della propriet� corpoAvvisiScaduti.
     * 
     * @param value
     *     allowed object is
     *     {@link CorpoAvvisiScadutiType }
     *     
     */
    public void setCorpoAvvisiScaduti(CorpoAvvisiScadutiType value) {
        this.corpoAvvisiScaduti = value;
    }

}
