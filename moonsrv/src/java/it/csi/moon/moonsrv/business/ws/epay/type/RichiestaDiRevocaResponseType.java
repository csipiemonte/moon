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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * &lt;p&gt;Classe Java per RichiestaDiRevocaResponseType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="RichiestaDiRevocaResponseType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="IdentificativoDominio" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="ApplicationId" type="{http://www.csi.it/epay/epaywso/types}String50Type"/&amp;gt;
 *         &amp;lt;element name="IdentificativoMessaggioEsito" type="{http://www.csi.it/epay/epaywso/types}String50Type"/&amp;gt;
 *         &amp;lt;element name="DataOraMessaggioEsito" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&amp;gt;
 *         &amp;lt;element name="RiferimentoMessaggioRevoca" type="{http://www.csi.it/epay/epaywso/types}String50Type"/&amp;gt;
 *         &amp;lt;element name="RiferimentoDataOraRevoca" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&amp;gt;
 *         &amp;lt;element name="IstitutoAttestante" type="{http://www.csi.it/epay/epaywso/types}SoggettoType"/&amp;gt;
 *         &amp;lt;element name="ImportoPagato" type="{http://www.csi.it/epay/epaywso/types}ImportoType"/&amp;gt;
 *         &amp;lt;element name="IUV" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="CodiceContestoPagamento" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="InvioOkRispostaRevoca" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt;
 *         &amp;lt;sequence&amp;gt;
 *           &amp;lt;element name="XML" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&amp;gt;
 *         &amp;lt;/sequence&amp;gt;
 *         &amp;lt;sequence&amp;gt;
 *           &amp;lt;element name="DatiSingoloEsitoRevoca" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}DatiEsitoSingolaRevocaType"/&amp;gt;
 *         &amp;lt;/sequence&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RichiestaDiRevocaResponseType", propOrder = {
    "identificativoDominio",
    "applicationId",
    "identificativoMessaggioEsito",
    "dataOraMessaggioEsito",
    "riferimentoMessaggioRevoca",
    "riferimentoDataOraRevoca",
    "istitutoAttestante",
    "importoPagato",
    "iuv",
    "codiceContestoPagamento",
    "invioOkRispostaRevoca",
    "xml",
    "datiSingoloEsitoRevoca"
})
public class RichiestaDiRevocaResponseType {

    @XmlElement(name = "IdentificativoDominio", required = true)
    protected String identificativoDominio;
    @XmlElement(name = "ApplicationId", required = true)
    protected String applicationId;
    @XmlElement(name = "IdentificativoMessaggioEsito", required = true)
    protected String identificativoMessaggioEsito;
    @XmlElement(name = "DataOraMessaggioEsito", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataOraMessaggioEsito;
    @XmlElement(name = "RiferimentoMessaggioRevoca", required = true)
    protected String riferimentoMessaggioRevoca;
    @XmlElement(name = "RiferimentoDataOraRevoca", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar riferimentoDataOraRevoca;
    @XmlElement(name = "IstitutoAttestante", required = true)
    protected SoggettoType istitutoAttestante;
    @XmlElement(name = "ImportoPagato", required = true)
    protected BigDecimal importoPagato;
    @XmlElement(name = "IUV", required = true)
    protected String iuv;
    @XmlElement(name = "CodiceContestoPagamento", required = true)
    protected String codiceContestoPagamento;
    @XmlElement(name = "InvioOkRispostaRevoca")
    protected boolean invioOkRispostaRevoca;
    @XmlElement(name = "XML", required = true)
    protected byte[] xml;
    @XmlElement(name = "DatiSingoloEsitoRevoca", required = true)
    protected DatiEsitoSingolaRevocaType datiSingoloEsitoRevoca;

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
     * Recupera il valore della propriet� identificativoMessaggioEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoMessaggioEsito() {
        return identificativoMessaggioEsito;
    }

    /**
     * Imposta il valore della propriet� identificativoMessaggioEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoMessaggioEsito(String value) {
        this.identificativoMessaggioEsito = value;
    }

    /**
     * Recupera il valore della propriet� dataOraMessaggioEsito.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataOraMessaggioEsito() {
        return dataOraMessaggioEsito;
    }

    /**
     * Imposta il valore della propriet� dataOraMessaggioEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataOraMessaggioEsito(XMLGregorianCalendar value) {
        this.dataOraMessaggioEsito = value;
    }

    /**
     * Recupera il valore della propriet� riferimentoMessaggioRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiferimentoMessaggioRevoca() {
        return riferimentoMessaggioRevoca;
    }

    /**
     * Imposta il valore della propriet� riferimentoMessaggioRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiferimentoMessaggioRevoca(String value) {
        this.riferimentoMessaggioRevoca = value;
    }

    /**
     * Recupera il valore della propriet� riferimentoDataOraRevoca.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRiferimentoDataOraRevoca() {
        return riferimentoDataOraRevoca;
    }

    /**
     * Imposta il valore della propriet� riferimentoDataOraRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRiferimentoDataOraRevoca(XMLGregorianCalendar value) {
        this.riferimentoDataOraRevoca = value;
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
     * Recupera il valore della propriet� invioOkRispostaRevoca.
     * 
     */
    public boolean isInvioOkRispostaRevoca() {
        return invioOkRispostaRevoca;
    }

    /**
     * Imposta il valore della propriet� invioOkRispostaRevoca.
     * 
     */
    public void setInvioOkRispostaRevoca(boolean value) {
        this.invioOkRispostaRevoca = value;
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
     * Recupera il valore della propriet� datiSingoloEsitoRevoca.
     * 
     * @return
     *     possible object is
     *     {@link DatiEsitoSingolaRevocaType }
     *     
     */
    public DatiEsitoSingolaRevocaType getDatiSingoloEsitoRevoca() {
        return datiSingoloEsitoRevoca;
    }

    /**
     * Imposta il valore della propriet� datiSingoloEsitoRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiEsitoSingolaRevocaType }
     *     
     */
    public void setDatiSingoloEsitoRevoca(DatiEsitoSingolaRevocaType value) {
        this.datiSingoloEsitoRevoca = value;
    }

}
