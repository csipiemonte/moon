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
 * &lt;p&gt;Classe Java per DatiSmistaDocumentoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DatiSmistaDocumentoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="ResponsabileTrattamento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}CodiceFiscaleType"/&amp;gt;
 *         &amp;lt;element name="IdDocumentoFruitore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String200Type"/&amp;gt;
 *         &amp;lt;element name="DocumentoElettronico" type="{http://www.csi.it/stardas/services/StardasCommonTypes}DocumentoElettronicoType"/&amp;gt;
 *         &amp;lt;element name="DatiDocumentoXML" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EmbeddedXMLType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Metadati" type="{http://www.csi.it/stardas/services/StardasCommonTypes}MetadatiType" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiSmistaDocumentoType", propOrder = {
    "responsabileTrattamento",
    "idDocumentoFruitore",
    "documentoElettronico",
    "datiDocumentoXML",
    "metadati"
})
public class DatiSmistaDocumentoType {

    @XmlElement(name = "ResponsabileTrattamento", required = true)
    protected String responsabileTrattamento;
    @XmlElement(name = "IdDocumentoFruitore", required = true)
    protected String idDocumentoFruitore;
    @XmlElement(name = "DocumentoElettronico", required = true)
    protected DocumentoElettronicoType documentoElettronico;
    @XmlElement(name = "DatiDocumentoXML")
    protected EmbeddedXMLType datiDocumentoXML;
    @XmlElement(name = "Metadati")
    protected MetadatiType metadati;

    /**
     * Recupera il valore della propriet� responsabileTrattamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponsabileTrattamento() {
        return responsabileTrattamento;
    }

    /**
     * Imposta il valore della propriet� responsabileTrattamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponsabileTrattamento(String value) {
        this.responsabileTrattamento = value;
    }

    /**
     * Recupera il valore della propriet� idDocumentoFruitore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumentoFruitore() {
        return idDocumentoFruitore;
    }

    /**
     * Imposta il valore della propriet� idDocumentoFruitore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumentoFruitore(String value) {
        this.idDocumentoFruitore = value;
    }

    /**
     * Recupera il valore della propriet� documentoElettronico.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoElettronicoType }
     *     
     */
    public DocumentoElettronicoType getDocumentoElettronico() {
        return documentoElettronico;
    }

    /**
     * Imposta il valore della propriet� documentoElettronico.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoElettronicoType }
     *     
     */
    public void setDocumentoElettronico(DocumentoElettronicoType value) {
        this.documentoElettronico = value;
    }

    /**
     * Recupera il valore della propriet� datiDocumentoXML.
     * 
     * @return
     *     possible object is
     *     {@link EmbeddedXMLType }
     *     
     */
    public EmbeddedXMLType getDatiDocumentoXML() {
        return datiDocumentoXML;
    }

    /**
     * Imposta il valore della propriet� datiDocumentoXML.
     * 
     * @param value
     *     allowed object is
     *     {@link EmbeddedXMLType }
     *     
     */
    public void setDatiDocumentoXML(EmbeddedXMLType value) {
        this.datiDocumentoXML = value;
    }

    /**
     * Recupera il valore della propriet� metadati.
     * 
     * @return
     *     possible object is
     *     {@link MetadatiType }
     *     
     */
    public MetadatiType getMetadati() {
        return metadati;
    }

    /**
     * Imposta il valore della propriet� metadati.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadatiType }
     *     
     */
    public void setMetadati(MetadatiType value) {
        this.metadati = value;
    }

}
