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
 * &lt;p&gt;Classe Java per EndpointType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="EndpointType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="Protocol"&amp;gt;
 *           &amp;lt;simpleType&amp;gt;
 *             &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *               &amp;lt;enumeration value="http"/&amp;gt;
 *               &amp;lt;enumeration value="https"/&amp;gt;
 *             &amp;lt;/restriction&amp;gt;
 *           &amp;lt;/simpleType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="Host" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="Context" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="Port" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="CredenzialiAutenticazione" minOccurs="0"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="NomeUtente" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *                   &amp;lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
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
@XmlType(name = "EndpointType", namespace = "http://www.csi.it/epay/epaywso/types", propOrder = {
    "protocol",
    "host",
    "context",
    "port",
    "credenzialiAutenticazione"
})
public class EndpointType {

    @XmlElement(name = "Protocol", required = true, defaultValue = "http")
    protected String protocol;
    @XmlElement(name = "Host", required = true)
    protected String host;
    @XmlElement(name = "Context", required = true)
    protected String context;
    @XmlElement(name = "Port")
    protected Integer port;
    @XmlElement(name = "CredenzialiAutenticazione")
    protected EndpointType.CredenzialiAutenticazione credenzialiAutenticazione;

    /**
     * Recupera il valore della propriet� protocol.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Imposta il valore della propriet� protocol.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocol(String value) {
        this.protocol = value;
    }

    /**
     * Recupera il valore della propriet� host.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        return host;
    }

    /**
     * Imposta il valore della propriet� host.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Recupera il valore della propriet� context.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContext() {
        return context;
    }

    /**
     * Imposta il valore della propriet� context.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContext(String value) {
        this.context = value;
    }

    /**
     * Recupera il valore della propriet� port.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Imposta il valore della propriet� port.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPort(Integer value) {
        this.port = value;
    }

    /**
     * Recupera il valore della propriet� credenzialiAutenticazione.
     * 
     * @return
     *     possible object is
     *     {@link EndpointType.CredenzialiAutenticazione }
     *     
     */
    public EndpointType.CredenzialiAutenticazione getCredenzialiAutenticazione() {
        return credenzialiAutenticazione;
    }

    /**
     * Imposta il valore della propriet� credenzialiAutenticazione.
     * 
     * @param value
     *     allowed object is
     *     {@link EndpointType.CredenzialiAutenticazione }
     *     
     */
    public void setCredenzialiAutenticazione(EndpointType.CredenzialiAutenticazione value) {
        this.credenzialiAutenticazione = value;
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
     *         &amp;lt;element name="NomeUtente" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
     *         &amp;lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
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
        "nomeUtente",
        "password"
    })
    public static class CredenzialiAutenticazione {

        @XmlElement(name = "NomeUtente", namespace = "http://www.csi.it/epay/epaywso/types", required = true)
        protected String nomeUtente;
        @XmlElement(name = "Password", namespace = "http://www.csi.it/epay/epaywso/types", required = true)
        protected String password;

        /**
         * Recupera il valore della propriet� nomeUtente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNomeUtente() {
            return nomeUtente;
        }

        /**
         * Imposta il valore della propriet� nomeUtente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNomeUtente(String value) {
            this.nomeUtente = value;
        }

        /**
         * Recupera il valore della propriet� password.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPassword() {
            return password;
        }

        /**
         * Imposta il valore della propriet� password.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPassword(String value) {
            this.password = value;
        }

    }

}
