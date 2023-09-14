/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.commons.dto.extra.territorio.regp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per Asl complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="Asl"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="codAsl" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="nomeBreve" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Asl", propOrder = {
    "codAsl",
    "nome",
    "nomeBreve"
})
public class AslLA {

    @XmlElement(required = true, nillable = true)
    protected String codAsl;
    @XmlElement(required = true, nillable = true)
    protected String nome;
    @XmlElement(required = true, nillable = true)
    protected String nomeBreve;

    
    public AslLA() {
		super();
    }
    public AslLA(String codAsl, String nome, String nomeBreve) {
		super();
		this.codAsl = codAsl;
		this.nome = nome;
		this.nomeBreve = nomeBreve;
	}

	/**
     * Recupera il valore della proprieta codAsl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAsl() {
        return codAsl;
    }

    /**
     * Imposta il valore della proprieta codAsl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAsl(String value) {
        this.codAsl = value;
    }

    /**
     * Recupera il valore della proprieta nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprieta nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprieta nomeBreve.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeBreve() {
        return nomeBreve;
    }

    /**
     * Imposta il valore della proprieta nomeBreve.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeBreve(String value) {
        this.nomeBreve = value;
    }

}
