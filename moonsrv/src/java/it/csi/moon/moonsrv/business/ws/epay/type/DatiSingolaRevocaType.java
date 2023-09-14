/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per DatiSingolaRevocaType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DatiSingolaRevocaType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="SingoloImportoRevocato" type="{http://www.csi.it/epay/epaywso/types}ImportoType"/&amp;gt;
 *         &amp;lt;element name="IUR" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="Causale" type="{http://www.csi.it/epay/epaywso/types}String140Type"/&amp;gt;
 *         &amp;lt;element name="DatiAggiuntivi" type="{http://www.csi.it/epay/epaywso/types}String140Type"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiSingolaRevocaType", propOrder = {
    "singoloImportoRevocato",
    "iur",
    "causale",
    "datiAggiuntivi"
})
public class DatiSingolaRevocaType {

    @XmlElement(name = "SingoloImportoRevocato", required = true)
    protected BigDecimal singoloImportoRevocato;
    @XmlElement(name = "IUR", required = true)
    protected String iur;
    @XmlElement(name = "Causale", required = true)
    protected String causale;
    @XmlElement(name = "DatiAggiuntivi", required = true)
    protected String datiAggiuntivi;

    /**
     * Recupera il valore della propriet� singoloImportoRevocato.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSingoloImportoRevocato() {
        return singoloImportoRevocato;
    }

    /**
     * Imposta il valore della propriet� singoloImportoRevocato.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSingoloImportoRevocato(BigDecimal value) {
        this.singoloImportoRevocato = value;
    }

    /**
     * Recupera il valore della propriet� iur.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIUR() {
        return iur;
    }

    /**
     * Imposta il valore della propriet� iur.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIUR(String value) {
        this.iur = value;
    }

    /**
     * Recupera il valore della propriet� causale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausale() {
        return causale;
    }

    /**
     * Imposta il valore della propriet� causale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausale(String value) {
        this.causale = value;
    }

    /**
     * Recupera il valore della propriet� datiAggiuntivi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiAggiuntivi() {
        return datiAggiuntivi;
    }

    /**
     * Imposta il valore della propriet� datiAggiuntivi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatiAggiuntivi(String value) {
        this.datiAggiuntivi = value;
    }

}
