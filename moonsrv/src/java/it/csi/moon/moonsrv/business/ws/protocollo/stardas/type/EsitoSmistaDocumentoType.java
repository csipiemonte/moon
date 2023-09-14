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
 * &lt;p&gt;Classe Java per EsitoSmistaDocumentoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="EsitoSmistaDocumentoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="MessageUUID" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&amp;gt;
 *         &amp;lt;element name="IdDocumentoFruitore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String200Type"/&amp;gt;
 *         &amp;lt;element name="TipoTrattamento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&amp;gt;
 *         &amp;lt;element name="EsitoTrattamento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ResultType"/&amp;gt;
 *         &amp;lt;element name="EsitiStep" type="{http://www.csi.it/stardas/services/StardasCallbackService}EsitiStepType"/&amp;gt;
 *         &amp;lt;element name="InformazioniAggiuntive" type="{http://www.csi.it/stardas/services/StardasCommonTypes}InformazioniAggiuntiveType" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EsitoSmistaDocumentoType", namespace = "http://www.csi.it/stardas/services/StardasCallbackService", propOrder = {
    "messageUUID",
    "idDocumentoFruitore",
    "tipoTrattamento",
    "esitoTrattamento",
    "esitiStep",
    "informazioniAggiuntive"
})
public class EsitoSmistaDocumentoType {

    @XmlElement(name = "MessageUUID", required = true)
    protected String messageUUID;
    @XmlElement(name = "IdDocumentoFruitore", required = true)
    protected String idDocumentoFruitore;
    @XmlElement(name = "TipoTrattamento", required = true)
    protected String tipoTrattamento;
    @XmlElement(name = "EsitoTrattamento", required = true)
    protected ResultType esitoTrattamento;
    @XmlElement(name = "EsitiStep", required = true)
    protected EsitiStepType esitiStep;
    @XmlElement(name = "InformazioniAggiuntive")
    protected InformazioniAggiuntiveType informazioniAggiuntive;

    /**
     * Recupera il valore della propriet� messageUUID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageUUID() {
        return messageUUID;
    }

    /**
     * Imposta il valore della propriet� messageUUID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageUUID(String value) {
        this.messageUUID = value;
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
     * Recupera il valore della propriet� tipoTrattamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoTrattamento() {
        return tipoTrattamento;
    }

    /**
     * Imposta il valore della propriet� tipoTrattamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoTrattamento(String value) {
        this.tipoTrattamento = value;
    }

    /**
     * Recupera il valore della propriet� esitoTrattamento.
     * 
     * @return
     *     possible object is
     *     {@link ResultType }
     *     
     */
    public ResultType getEsitoTrattamento() {
        return esitoTrattamento;
    }

    /**
     * Imposta il valore della propriet� esitoTrattamento.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultType }
     *     
     */
    public void setEsitoTrattamento(ResultType value) {
        this.esitoTrattamento = value;
    }

    /**
     * Recupera il valore della propriet� esitiStep.
     * 
     * @return
     *     possible object is
     *     {@link EsitiStepType }
     *     
     */
    public EsitiStepType getEsitiStep() {
        return esitiStep;
    }

    /**
     * Imposta il valore della propriet� esitiStep.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitiStepType }
     *     
     */
    public void setEsitiStep(EsitiStepType value) {
        this.esitiStep = value;
    }

    /**
     * Recupera il valore della propriet� informazioniAggiuntive.
     * 
     * @return
     *     possible object is
     *     {@link InformazioniAggiuntiveType }
     *     
     */
    public InformazioniAggiuntiveType getInformazioniAggiuntive() {
        return informazioniAggiuntive;
    }

    /**
     * Imposta il valore della propriet� informazioniAggiuntive.
     * 
     * @param value
     *     allowed object is
     *     {@link InformazioniAggiuntiveType }
     *     
     */
    public void setInformazioniAggiuntive(InformazioniAggiuntiveType value) {
        this.informazioniAggiuntive = value;
    }

	@Override
	public String toString() {
		return "EsitoSmistaDocumentoType [messageUUID=" + messageUUID + ", idDocumentoFruitore=" + idDocumentoFruitore
				+ ", tipoTrattamento=" + tipoTrattamento + ", esitoTrattamento=" + esitoTrattamento + ", esitiStep="
				+ esitiStep + ", informazioniAggiuntive=" + informazioniAggiuntive + "]";
	}

}
