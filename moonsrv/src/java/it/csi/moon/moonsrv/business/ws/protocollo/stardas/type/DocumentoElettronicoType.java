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
 * &lt;p&gt;Classe Java per DocumentoElettronicoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DocumentoElettronicoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="NomeFile" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String1000Type"/&amp;gt;
 *         &amp;lt;choice&amp;gt;
 *           &amp;lt;element name="ContenutoBinario" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EmbeddedBinaryType"/&amp;gt;
 *           &amp;lt;element name="RiferimentoECM" type="{http://www.csi.it/stardas/services/StardasCommonTypes}RiferimentoECMType"/&amp;gt;
 *         &amp;lt;/choice&amp;gt;
 *         &amp;lt;element name="DocumentoFirmato" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt;
 *         &amp;lt;element name="MimeType" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoElettronicoType", propOrder = {
    "nomeFile",
    "contenutoBinario",
    "riferimentoECM",
    "documentoFirmato",
    "mimeType"
})
public class DocumentoElettronicoType {

    @XmlElement(name = "NomeFile", required = true)
    protected String nomeFile;
    @XmlElement(name = "ContenutoBinario")
    protected EmbeddedBinaryType contenutoBinario;
    @XmlElement(name = "RiferimentoECM")
    protected RiferimentoECMType riferimentoECM;
    @XmlElement(name = "DocumentoFirmato")
    protected boolean documentoFirmato;
    @XmlElement(name = "MimeType", required = true)
    protected String mimeType;

    /**
     * Recupera il valore della propriet� nomeFile.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeFile() {
        return nomeFile;
    }

    /**
     * Imposta il valore della propriet� nomeFile.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeFile(String value) {
        this.nomeFile = value;
    }

    /**
     * Recupera il valore della propriet� contenutoBinario.
     * 
     * @return
     *     possible object is
     *     {@link EmbeddedBinaryType }
     *     
     */
    public EmbeddedBinaryType getContenutoBinario() {
        return contenutoBinario;
    }

    /**
     * Imposta il valore della propriet� contenutoBinario.
     * 
     * @param value
     *     allowed object is
     *     {@link EmbeddedBinaryType }
     *     
     */
    public void setContenutoBinario(EmbeddedBinaryType value) {
        this.contenutoBinario = value;
    }

    /**
     * Recupera il valore della propriet� riferimentoECM.
     * 
     * @return
     *     possible object is
     *     {@link RiferimentoECMType }
     *     
     */
    public RiferimentoECMType getRiferimentoECM() {
        return riferimentoECM;
    }

    /**
     * Imposta il valore della propriet� riferimentoECM.
     * 
     * @param value
     *     allowed object is
     *     {@link RiferimentoECMType }
     *     
     */
    public void setRiferimentoECM(RiferimentoECMType value) {
        this.riferimentoECM = value;
    }

    /**
     * Recupera il valore della propriet� documentoFirmato.
     * 
     */
    public boolean isDocumentoFirmato() {
        return documentoFirmato;
    }

    /**
     * Imposta il valore della propriet� documentoFirmato.
     * 
     */
    public void setDocumentoFirmato(boolean value) {
        this.documentoFirmato = value;
    }

    /**
     * Recupera il valore della propriet� mimeType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Imposta il valore della propriet� mimeType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

}
