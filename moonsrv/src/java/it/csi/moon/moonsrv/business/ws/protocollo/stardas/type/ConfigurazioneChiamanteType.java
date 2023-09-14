/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.protocollo.stardas.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per ConfigurazioneChiamanteType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="ConfigurazioneChiamanteType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="CodiceFiscaleEnte" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&amp;gt;
 *         &amp;lt;element name="CodiceFruitore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&amp;gt;
 *         &amp;lt;element name="CodiceApplicazione" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&amp;gt;
 *         &amp;lt;element name="CodiceTipoDocumento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConfigurazioneChiamanteType", propOrder = {
    "codiceFiscaleEnte",
    "codiceFruitore",
    "codiceApplicazione",
    "codiceTipoDocumento"
})
public class ConfigurazioneChiamanteType {

    @XmlElement(name = "CodiceFiscaleEnte", required = true)
    protected String codiceFiscaleEnte;
    @XmlElement(name = "CodiceFruitore", required = true)
    protected String codiceFruitore;
    @XmlElement(name = "CodiceApplicazione", required = true)
    protected String codiceApplicazione;
    @XmlElement(name = "CodiceTipoDocumento", required = true)
    protected String codiceTipoDocumento;

    /**
     * Recupera il valore della propriet� codiceFiscaleEnte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscaleEnte() {
        return codiceFiscaleEnte;
    }

    /**
     * Imposta il valore della propriet� codiceFiscaleEnte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscaleEnte(String value) {
        this.codiceFiscaleEnte = value;
    }

    /**
     * Recupera il valore della propriet� codiceFruitore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFruitore() {
        return codiceFruitore;
    }

    /**
     * Imposta il valore della propriet� codiceFruitore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFruitore(String value) {
        this.codiceFruitore = value;
    }

    /**
     * Recupera il valore della propriet� codiceApplicazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceApplicazione() {
        return codiceApplicazione;
    }

    /**
     * Imposta il valore della propriet� codiceApplicazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceApplicazione(String value) {
        this.codiceApplicazione = value;
    }

    /**
     * Recupera il valore della propriet� codiceTipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceTipoDocumento() {
        return codiceTipoDocumento;
    }

    /**
     * Imposta il valore della propriet� codiceTipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceTipoDocumento(String value) {
        this.codiceTipoDocumento = value;
    }

}
