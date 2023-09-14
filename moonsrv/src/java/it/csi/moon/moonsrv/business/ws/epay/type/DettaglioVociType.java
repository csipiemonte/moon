/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per DettaglioVociType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DettaglioVociType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="DettaglioVoce"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="Tipo" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}TipoDettaglioVoce"/&amp;gt;
 *                   &amp;lt;element name="Descrizione" type="{http://www.csi.it/epay/epaywso/types}String100Type" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="Importo" type="{http://www.csi.it/epay/epaywso/types}ImportoType"/&amp;gt;
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
@XmlType(name = "DettaglioVociType", propOrder = {
    "dettaglioVoce"
})
public class DettaglioVociType {

    @XmlElement(name = "DettaglioVoce", required = true)
    protected DettaglioVociType.DettaglioVoce dettaglioVoce;

    /**
     * Recupera il valore della propriet� dettaglioVoce.
     * 
     * @return
     *     possible object is
     *     {@link DettaglioVociType.DettaglioVoce }
     *     
     */
    public DettaglioVociType.DettaglioVoce getDettaglioVoce() {
        return dettaglioVoce;
    }

    /**
     * Imposta il valore della propriet� dettaglioVoce.
     * 
     * @param value
     *     allowed object is
     *     {@link DettaglioVociType.DettaglioVoce }
     *     
     */
    public void setDettaglioVoce(DettaglioVociType.DettaglioVoce value) {
        this.dettaglioVoce = value;
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
     *         &amp;lt;element name="Tipo" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}TipoDettaglioVoce"/&amp;gt;
     *         &amp;lt;element name="Descrizione" type="{http://www.csi.it/epay/epaywso/types}String100Type" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="Importo" type="{http://www.csi.it/epay/epaywso/types}ImportoType"/&amp;gt;
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
        "tipo",
        "descrizione",
        "importo"
    })
    public static class DettaglioVoce {

        @XmlElement(name = "Tipo", required = true)
        @XmlSchemaType(name = "string")
        protected TipoDettaglioVoce tipo;
        @XmlElement(name = "Descrizione")
        protected String descrizione;
        @XmlElement(name = "Importo", required = true)
        protected BigDecimal importo;

        /**
         * Recupera il valore della propriet� tipo.
         * 
         * @return
         *     possible object is
         *     {@link TipoDettaglioVoce }
         *     
         */
        public TipoDettaglioVoce getTipo() {
            return tipo;
        }

        /**
         * Imposta il valore della propriet� tipo.
         * 
         * @param value
         *     allowed object is
         *     {@link TipoDettaglioVoce }
         *     
         */
        public void setTipo(TipoDettaglioVoce value) {
            this.tipo = value;
        }

        /**
         * Recupera il valore della propriet� descrizione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrizione() {
            return descrizione;
        }

        /**
         * Imposta il valore della propriet� descrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrizione(String value) {
            this.descrizione = value;
        }

        /**
         * Recupera il valore della propriet� importo.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getImporto() {
            return importo;
        }

        /**
         * Imposta il valore della propriet� importo.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setImporto(BigDecimal value) {
            this.importo = value;
        }

    }

}
