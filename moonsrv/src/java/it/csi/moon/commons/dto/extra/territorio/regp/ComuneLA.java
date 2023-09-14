/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.commons.dto.extra.territorio.regp;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import it.csi.territorio.svista.limamm.ente.cxfclient.ArrayOfAsl;

/**
 * &lt;p&gt;Classe Java per Comune complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="Comune"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="aslDiRiferimento" type="{ente}List<Asl>"/&amp;gt;
 *         &amp;lt;element name="cap" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="codCatasto" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="codIstat" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="idProvincia" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Comune", propOrder = {
    "aslDiRiferimento",
    "cap",
    "codCatasto",
    "codIstat",
    "id",
    "idProvincia",
    "nome"
})
public class ComuneLA {

    @XmlElement(required = true, nillable = true)
    protected List<AslLA> aslDiRiferimento;
    @XmlElement(required = true, nillable = true)
    protected String cap;
    @XmlElement(required = true, nillable = true)
    protected String codCatasto;
    @XmlElement(required = true, nillable = true)
    protected String codIstat;
    protected long id;
    protected long idProvincia;
    @XmlElement(required = true, nillable = true)
    protected String nome;

    /**
     * Recupera il valore della proprieta aslDiRiferimento.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAsl }
     *     
     */
    public List<AslLA> getAslDiRiferimento() {
        return aslDiRiferimento;
    }

    /**
     * Imposta il valore della proprieta aslDiRiferimento.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAsl }
     *     
     */
    public void setAslDiRiferimento(List<AslLA> value) {
        this.aslDiRiferimento = value;
    }

    /**
     * Recupera il valore della proprieta cap.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCap() {
        return cap;
    }

    /**
     * Imposta il valore della proprieta cap.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCap(String value) {
        this.cap = value;
    }

    /**
     * Recupera il valore della proprieta codCatasto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodCatasto() {
        return codCatasto;
    }

    /**
     * Imposta il valore della proprieta codCatasto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodCatasto(String value) {
        this.codCatasto = value;
    }

    /**
     * Recupera il valore della proprieta codIstat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodIstat() {
        return codIstat;
    }

    /**
     * Imposta il valore della proprieta codIstat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodIstat(String value) {
        this.codIstat = value;
    }

    /**
     * Recupera il valore della proprieta id.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Imposta il valore della proprieta id.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Recupera il valore della proprieta idProvincia.
     * 
     */
    public long getIdProvincia() {
        return idProvincia;
    }

    /**
     * Imposta il valore della proprieta idProvincia.
     * 
     */
    public void setIdProvincia(long value) {
        this.idProvincia = value;
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

}
