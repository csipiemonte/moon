/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per SoggettoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="SoggettoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;choice&amp;gt;
 *           &amp;lt;element name="PersonaFisica" type="{http://www.csi.it/epay/epaywso/types}PersonaFisicaType"/&amp;gt;
 *           &amp;lt;element name="PersonaGiuridica" type="{http://www.csi.it/epay/epaywso/types}PersonaGiuridicaType"/&amp;gt;
 *         &amp;lt;/choice&amp;gt;
 *         &amp;lt;element name="IdentificativoUnivocoFiscale" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="Indirizzo" type="{http://www.csi.it/epay/epaywso/types}String70Type" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Civico" type="{http://www.csi.it/epay/epaywso/types}String16Type" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="CAP" type="{http://www.csi.it/epay/epaywso/types}String16Type" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Localita" type="{http://www.csi.it/epay/epaywso/types}String35Type" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Provincia" type="{http://www.csi.it/epay/epaywso/types}String35Type" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Nazione" type="{http://www.csi.it/epay/epaywso/types}NazioneType" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="EMail" type="{http://www.csi.it/epay/epaywso/types}EMailAddress" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoggettoType", namespace = "http://www.csi.it/epay/epaywso/types", propOrder = {
    "personaFisica",
    "personaGiuridica",
    "identificativoUnivocoFiscale",
    "indirizzo",
    "civico",
    "cap",
    "localita",
    "provincia",
    "nazione",
    "eMail"
})
public class SoggettoType {

    @XmlElement(name = "PersonaFisica")
    protected PersonaFisicaType personaFisica;
    @XmlElement(name = "PersonaGiuridica")
    protected PersonaGiuridicaType personaGiuridica;
    @XmlElement(name = "IdentificativoUnivocoFiscale", required = true)
    protected String identificativoUnivocoFiscale;
    @XmlElement(name = "Indirizzo")
    protected String indirizzo;
    @XmlElement(name = "Civico")
    protected String civico;
    @XmlElement(name = "CAP")
    protected String cap;
    @XmlElement(name = "Localita")
    protected String localita;
    @XmlElement(name = "Provincia")
    protected String provincia;
    @XmlElement(name = "Nazione")
    protected String nazione;
    @XmlElement(name = "EMail")
    protected String eMail;

    /**
     * Recupera il valore della propriet� personaFisica.
     * 
     * @return
     *     possible object is
     *     {@link PersonaFisicaType }
     *     
     */
    public PersonaFisicaType getPersonaFisica() {
        return personaFisica;
    }

    /**
     * Imposta il valore della propriet� personaFisica.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonaFisicaType }
     *     
     */
    public void setPersonaFisica(PersonaFisicaType value) {
        this.personaFisica = value;
    }

    /**
     * Recupera il valore della propriet� personaGiuridica.
     * 
     * @return
     *     possible object is
     *     {@link PersonaGiuridicaType }
     *     
     */
    public PersonaGiuridicaType getPersonaGiuridica() {
        return personaGiuridica;
    }

    /**
     * Imposta il valore della propriet� personaGiuridica.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonaGiuridicaType }
     *     
     */
    public void setPersonaGiuridica(PersonaGiuridicaType value) {
        this.personaGiuridica = value;
    }

    /**
     * Recupera il valore della propriet� identificativoUnivocoFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoUnivocoFiscale() {
        return identificativoUnivocoFiscale;
    }

    /**
     * Imposta il valore della propriet� identificativoUnivocoFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoUnivocoFiscale(String value) {
        this.identificativoUnivocoFiscale = value;
    }

    /**
     * Recupera il valore della propriet� indirizzo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * Imposta il valore della propriet� indirizzo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzo(String value) {
        this.indirizzo = value;
    }

    /**
     * Recupera il valore della propriet� civico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCivico() {
        return civico;
    }

    /**
     * Imposta il valore della propriet� civico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCivico(String value) {
        this.civico = value;
    }

    /**
     * Recupera il valore della propriet� cap.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAP() {
        return cap;
    }

    /**
     * Imposta il valore della propriet� cap.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAP(String value) {
        this.cap = value;
    }

    /**
     * Recupera il valore della propriet� localita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalita() {
        return localita;
    }

    /**
     * Imposta il valore della propriet� localita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalita(String value) {
        this.localita = value;
    }

    /**
     * Recupera il valore della propriet� provincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Imposta il valore della propriet� provincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvincia(String value) {
        this.provincia = value;
    }

    /**
     * Recupera il valore della propriet� nazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazione() {
        return nazione;
    }

    /**
     * Imposta il valore della propriet� nazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazione(String value) {
        this.nazione = value;
    }

    /**
     * Recupera il valore della propriet� eMail.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEMail() {
        return eMail;
    }

    /**
     * Imposta il valore della propriet� eMail.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEMail(String value) {
        this.eMail = value;
    }

}
