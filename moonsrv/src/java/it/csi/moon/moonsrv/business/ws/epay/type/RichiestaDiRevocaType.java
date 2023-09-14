/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * &lt;p&gt;Classe Java per RichiestaDiRevocaType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="RichiestaDiRevocaType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="IdentificativoDominio" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="ApplicationId" type="{http://www.csi.it/epay/epaywso/types}String50Type"/&amp;gt;
 *         &amp;lt;element name="IdentificativoMessaggioRevoca" type="{http://www.csi.it/epay/epaywso/types}String50Type"/&amp;gt;
 *         &amp;lt;element name="DataOraMessaggioRevoca" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&amp;gt;
 *         &amp;lt;element name="IstitutoAttestante" type="{http://www.csi.it/epay/epaywso/types}SoggettoType"/&amp;gt;
 *         &amp;lt;element name="ImportoPagato" type="{http://www.csi.it/epay/epaywso/types}ImportoType"/&amp;gt;
 *         &amp;lt;element name="IUV" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="CodiceContestoPagamento" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="TipoRevoca" type="{http://www.csi.it/epay/epaywso/types}Numero6CifreType"/&amp;gt;
 *         &amp;lt;sequence&amp;gt;
 *           &amp;lt;element name="XML" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&amp;gt;
 *         &amp;lt;/sequence&amp;gt;
 *         &amp;lt;element name="ListaDatiSingolaRevoca"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="DatiSingolaRevoca" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}DatiSingolaRevocaType" maxOccurs="5"/&amp;gt;
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
@XmlType(name = "RichiestaDiRevocaType", propOrder = {
    "identificativoDominio",
    "applicationId",
    "identificativoMessaggioRevoca",
    "dataOraMessaggioRevoca",
    "istitutoAttestante",
    "importoPagato",
    "iuv",
    "codiceContestoPagamento",
    "tipoRevoca",
    "xml",
    "listaDatiSingolaRevoca"
})
public class RichiestaDiRevocaType {

    @XmlElement(name = "IdentificativoDominio", required = true)
    protected String identificativoDominio;
    @XmlElement(name = "ApplicationId", required = true)
    protected String applicationId;
    @XmlElement(name = "IdentificativoMessaggioRevoca", required = true)
    protected String identificativoMessaggioRevoca;
    @XmlElement(name = "DataOraMessaggioRevoca", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataOraMessaggioRevoca;
    @XmlElement(name = "IstitutoAttestante", required = true)
    protected SoggettoType istitutoAttestante;
    @XmlElement(name = "ImportoPagato", required = true)
    protected BigDecimal importoPagato;
    @XmlElement(name = "IUV", required = true)
    protected String iuv;
    @XmlElement(name = "CodiceContestoPagamento", required = true)
    protected String codiceContestoPagamento;
    @XmlElement(name = "TipoRevoca")
    @XmlSchemaType(name = "integer")
    protected int tipoRevoca;
    @XmlElement(name = "XML", required = true)
    protected byte[] xml;
    @XmlElement(name = "ListaDatiSingolaRevoca", required = true)
    protected RichiestaDiRevocaType.ListaDatiSingolaRevoca listaDatiSingolaRevoca;

    /**
     * Recupera il valore della propriet� identificativoDominio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoDominio() {
        return identificativoDominio;
    }

    /**
     * Imposta il valore della propriet� identificativoDominio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoDominio(String value) {
        this.identificativoDominio = value;
    }

    /**
     * Recupera il valore della propriet� applicationId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Imposta il valore della propriet� applicationId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationId(String value) {
        this.applicationId = value;
    }

    /**
     * Recupera il valore della propriet� identificativoMessaggioRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoMessaggioRevoca() {
        return identificativoMessaggioRevoca;
    }

    /**
     * Imposta il valore della propriet� identificativoMessaggioRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoMessaggioRevoca(String value) {
        this.identificativoMessaggioRevoca = value;
    }

    /**
     * Recupera il valore della propriet� dataOraMessaggioRevoca.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataOraMessaggioRevoca() {
        return dataOraMessaggioRevoca;
    }

    /**
     * Imposta il valore della propriet� dataOraMessaggioRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataOraMessaggioRevoca(XMLGregorianCalendar value) {
        this.dataOraMessaggioRevoca = value;
    }

    /**
     * Recupera il valore della propriet� istitutoAttestante.
     * 
     * @return
     *     possible object is
     *     {@link SoggettoType }
     *     
     */
    public SoggettoType getIstitutoAttestante() {
        return istitutoAttestante;
    }

    /**
     * Imposta il valore della propriet� istitutoAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link SoggettoType }
     *     
     */
    public void setIstitutoAttestante(SoggettoType value) {
        this.istitutoAttestante = value;
    }

    /**
     * Recupera il valore della propriet� importoPagato.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoPagato() {
        return importoPagato;
    }

    /**
     * Imposta il valore della propriet� importoPagato.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoPagato(BigDecimal value) {
        this.importoPagato = value;
    }

    /**
     * Recupera il valore della propriet� iuv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIUV() {
        return iuv;
    }

    /**
     * Imposta il valore della propriet� iuv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIUV(String value) {
        this.iuv = value;
    }

    /**
     * Recupera il valore della propriet� codiceContestoPagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceContestoPagamento() {
        return codiceContestoPagamento;
    }

    /**
     * Imposta il valore della propriet� codiceContestoPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceContestoPagamento(String value) {
        this.codiceContestoPagamento = value;
    }

    /**
     * Recupera il valore della propriet� tipoRevoca.
     * 
     */
    public int getTipoRevoca() {
        return tipoRevoca;
    }

    /**
     * Imposta il valore della propriet� tipoRevoca.
     * 
     */
    public void setTipoRevoca(int value) {
        this.tipoRevoca = value;
    }

    /**
     * Recupera il valore della propriet� xml.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getXML() {
        return xml;
    }

    /**
     * Imposta il valore della propriet� xml.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setXML(byte[] value) {
        this.xml = value;
    }

    /**
     * Recupera il valore della propriet� listaDatiSingolaRevoca.
     * 
     * @return
     *     possible object is
     *     {@link RichiestaDiRevocaType.ListaDatiSingolaRevoca }
     *     
     */
    public RichiestaDiRevocaType.ListaDatiSingolaRevoca getListaDatiSingolaRevoca() {
        return listaDatiSingolaRevoca;
    }

    /**
     * Imposta il valore della propriet� listaDatiSingolaRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link RichiestaDiRevocaType.ListaDatiSingolaRevoca }
     *     
     */
    public void setListaDatiSingolaRevoca(RichiestaDiRevocaType.ListaDatiSingolaRevoca value) {
        this.listaDatiSingolaRevoca = value;
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
     *         &amp;lt;element name="DatiSingolaRevoca" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}DatiSingolaRevocaType" maxOccurs="5"/&amp;gt;
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
        "datiSingolaRevoca"
    })
    public static class ListaDatiSingolaRevoca {

        @XmlElement(name = "DatiSingolaRevoca", required = true)
        protected List<DatiSingolaRevocaType> datiSingolaRevoca;

        /**
         * Gets the value of the datiSingolaRevoca property.
         * 
         * &lt;p&gt;
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the datiSingolaRevoca property.
         * 
         * &lt;p&gt;
         * For example, to add a new item, do as follows:
         * &lt;pre&gt;
         *    getDatiSingolaRevoca().add(newItem);
         * &lt;/pre&gt;
         * 
         * 
         * &lt;p&gt;
         * Objects of the following type(s) are allowed in the list
         * {@link DatiSingolaRevocaType }
         * 
         * 
         */
        public List<DatiSingolaRevocaType> getDatiSingolaRevoca() {
            if (datiSingolaRevoca == null) {
                datiSingolaRevoca = new ArrayList<DatiSingolaRevocaType>();
            }
            return this.datiSingolaRevoca;
        }

    }

}
