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
 *         &amp;lt;element name="Testata" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}TestataRichiesteDiRevocaType"/&amp;gt;
 *         &amp;lt;element name="CorpoRichiesteDiRevoca" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}CorpoRichiesteDiRevocaType"/&amp;gt;
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
    "corpoRichiesteDiRevoca"
})
@XmlRootElement(name = "TrasmettiRichiesteDiRevocaRequest")
public class TrasmettiRichiesteDiRevocaRequest {

    @XmlElement(name = "Testata", required = true)
    protected TestataRichiesteDiRevocaType testata;
    @XmlElement(name = "CorpoRichiesteDiRevoca", required = true)
    protected CorpoRichiesteDiRevocaType corpoRichiesteDiRevoca;

    /**
     * Recupera il valore della propriet� testata.
     * 
     * @return
     *     possible object is
     *     {@link TestataRichiesteDiRevocaType }
     *     
     */
    public TestataRichiesteDiRevocaType getTestata() {
        return testata;
    }

    /**
     * Imposta il valore della propriet� testata.
     * 
     * @param value
     *     allowed object is
     *     {@link TestataRichiesteDiRevocaType }
     *     
     */
    public void setTestata(TestataRichiesteDiRevocaType value) {
        this.testata = value;
    }

    /**
     * Recupera il valore della propriet� corpoRichiesteDiRevoca.
     * 
     * @return
     *     possible object is
     *     {@link CorpoRichiesteDiRevocaType }
     *     
     */
    public CorpoRichiesteDiRevocaType getCorpoRichiesteDiRevoca() {
        return corpoRichiesteDiRevoca;
    }

    /**
     * Imposta il valore della propriet� corpoRichiesteDiRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link CorpoRichiesteDiRevocaType }
     *     
     */
    public void setCorpoRichiesteDiRevoca(CorpoRichiesteDiRevocaType value) {
        this.corpoRichiesteDiRevoca = value;
    }

}
