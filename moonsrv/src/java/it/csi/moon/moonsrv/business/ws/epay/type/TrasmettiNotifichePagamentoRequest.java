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
 *         &amp;lt;element name="Testata" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}TestataNotifichePagamentoType"/&amp;gt;
 *         &amp;lt;element name="CorpoNotifichePagamento" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}CorpoNotifichePagamentoType"/&amp;gt;
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
    "corpoNotifichePagamento"
})
@XmlRootElement(name = "TrasmettiNotifichePagamentoRequest")
public class TrasmettiNotifichePagamentoRequest {

    @XmlElement(name = "Testata", required = true)
    protected TestataNotifichePagamentoType testata;
    @XmlElement(name = "CorpoNotifichePagamento", required = true)
    protected CorpoNotifichePagamentoType corpoNotifichePagamento;

    /**
     * Recupera il valore della propriet� testata.
     * 
     * @return
     *     possible object is
     *     {@link TestataNotifichePagamentoType }
     *     
     */
    public TestataNotifichePagamentoType getTestata() {
        return testata;
    }

    /**
     * Imposta il valore della propriet� testata.
     * 
     * @param value
     *     allowed object is
     *     {@link TestataNotifichePagamentoType }
     *     
     */
    public void setTestata(TestataNotifichePagamentoType value) {
        this.testata = value;
    }

    /**
     * Recupera il valore della propriet� corpoNotifichePagamento.
     * 
     * @return
     *     possible object is
     *     {@link CorpoNotifichePagamentoType }
     *     
     */
    public CorpoNotifichePagamentoType getCorpoNotifichePagamento() {
        return corpoNotifichePagamento;
    }

    /**
     * Imposta il valore della propriet� corpoNotifichePagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link CorpoNotifichePagamentoType }
     *     
     */
    public void setCorpoNotifichePagamento(CorpoNotifichePagamentoType value) {
        this.corpoNotifichePagamento = value;
    }

}
