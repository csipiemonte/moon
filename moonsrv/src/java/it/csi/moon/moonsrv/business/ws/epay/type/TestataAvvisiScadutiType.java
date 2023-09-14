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
 * &lt;p&gt;Classe Java per TestataAvvisiScadutiType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="TestataAvvisiScadutiType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="IdMessaggio" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="CFEnteCreditore" type="{http://www.csi.it/epay/epaywso/types}String35Type"/&amp;gt;
 *         &amp;lt;element name="CodiceVersamento" type="{http://www.csi.it/epay/epaywso/types}CodiceVersamentoType"/&amp;gt;
 *         &amp;lt;element name="NumeroAvvisiScaduti" type="{http://www.csi.it/epay/epaywso/types}Numero6CifreType"/&amp;gt;
 *         &amp;lt;element name="ImportoTotaleAvvisiScaduti" type="{http://www.csi.it/epay/epaywso/types}ImportoType"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TestataAvvisiScadutiType", propOrder = {
    "idMessaggio",
    "cfEnteCreditore",
    "codiceVersamento",
    "numeroAvvisiScaduti",
    "importoTotaleAvvisiScaduti"
})
public class TestataAvvisiScadutiType {

    @XmlElement(name = "IdMessaggio", required = true)
    protected String idMessaggio;
    @XmlElement(name = "CFEnteCreditore", required = true)
    protected String cfEnteCreditore;
    @XmlElement(name = "CodiceVersamento", required = true)
    protected String codiceVersamento;
    @XmlElement(name = "NumeroAvvisiScaduti")
    @XmlSchemaType(name = "integer")
    protected int numeroAvvisiScaduti;
    @XmlElement(name = "ImportoTotaleAvvisiScaduti", required = true)
    protected BigDecimal importoTotaleAvvisiScaduti;

    /**
     * Recupera il valore della propriet� idMessaggio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMessaggio() {
        return idMessaggio;
    }

    /**
     * Imposta il valore della propriet� idMessaggio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMessaggio(String value) {
        this.idMessaggio = value;
    }

    /**
     * Recupera il valore della propriet� cfEnteCreditore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCFEnteCreditore() {
        return cfEnteCreditore;
    }

    /**
     * Imposta il valore della propriet� cfEnteCreditore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCFEnteCreditore(String value) {
        this.cfEnteCreditore = value;
    }

    /**
     * Recupera il valore della propriet� codiceVersamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceVersamento() {
        return codiceVersamento;
    }

    /**
     * Imposta il valore della propriet� codiceVersamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceVersamento(String value) {
        this.codiceVersamento = value;
    }

    /**
     * Recupera il valore della propriet� numeroAvvisiScaduti.
     * 
     */
    public int getNumeroAvvisiScaduti() {
        return numeroAvvisiScaduti;
    }

    /**
     * Imposta il valore della propriet� numeroAvvisiScaduti.
     * 
     */
    public void setNumeroAvvisiScaduti(int value) {
        this.numeroAvvisiScaduti = value;
    }

    /**
     * Recupera il valore della propriet� importoTotaleAvvisiScaduti.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoTotaleAvvisiScaduti() {
        return importoTotaleAvvisiScaduti;
    }

    /**
     * Imposta il valore della propriet� importoTotaleAvvisiScaduti.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoTotaleAvvisiScaduti(BigDecimal value) {
        this.importoTotaleAvvisiScaduti = value;
    }

}
