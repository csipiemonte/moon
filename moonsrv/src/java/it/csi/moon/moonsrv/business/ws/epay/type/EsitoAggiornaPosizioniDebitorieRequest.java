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
 *     &amp;lt;extension base="{http://www.csi.it/epay/epaywso/types}ResponseType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="TestataEsito" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}TestataEsitoType"/&amp;gt;
 *         &amp;lt;element name="EsitoAggiornamento" type="{http://www.csi.it/epay/epaywso/types}EsitoAggiornamentoType" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/extension&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "testataEsito",
    "esitoAggiornamento"
})
@XmlRootElement(name = "EsitoAggiornaPosizioniDebitorieRequest")
public class EsitoAggiornaPosizioniDebitorieRequest
    extends ResponseType
{

    @XmlElement(name = "TestataEsito", required = true)
    protected TestataEsitoType testataEsito;
    @XmlElement(name = "EsitoAggiornamento")
    protected EsitoAggiornamentoType esitoAggiornamento;

    /**
     * Recupera il valore della propriet� testataEsito.
     * 
     * @return
     *     possible object is
     *     {@link TestataEsitoType }
     *     
     */
    public TestataEsitoType getTestataEsito() {
        return testataEsito;
    }

    /**
     * Imposta il valore della propriet� testataEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link TestataEsitoType }
     *     
     */
    public void setTestataEsito(TestataEsitoType value) {
        this.testataEsito = value;
    }

    /**
     * Recupera il valore della propriet� esitoAggiornamento.
     * 
     * @return
     *     possible object is
     *     {@link EsitoAggiornamentoType }
     *     
     */
    public EsitoAggiornamentoType getEsitoAggiornamento() {
        return esitoAggiornamento;
    }

    /**
     * Imposta il valore della propriet� esitoAggiornamento.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoAggiornamentoType }
     *     
     */
    public void setEsitoAggiornamento(EsitoAggiornamentoType value) {
        this.esitoAggiornamento = value;
    }

}
