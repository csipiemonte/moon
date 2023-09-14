/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.protocollo.stardas.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per EsitiStepType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="EsitiStepType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="EsitoStep" maxOccurs="unbounded"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="Nome" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String200Type"/&amp;gt;
 *                   &amp;lt;element name="Esito" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ResultType"/&amp;gt;
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
@XmlType(name = "EsitiStepType", namespace = "http://www.csi.it/stardas/services/StardasCallbackService", propOrder = {
    "esitoStep"
})
public class EsitiStepType {

    @XmlElement(name = "EsitoStep", required = true)
    protected List<EsitiStepType.EsitoStep> esitoStep;

    /**
     * Gets the value of the esitoStep property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the esitoStep property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getEsitoStep().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link EsitiStepType.EsitoStep }
     * 
     * 
     */
    public List<EsitiStepType.EsitoStep> getEsitoStep() {
        if (esitoStep == null) {
            esitoStep = new ArrayList<EsitiStepType.EsitoStep>();
        }
        return this.esitoStep;
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
     *         &amp;lt;element name="Nome" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String200Type"/&amp;gt;
     *         &amp;lt;element name="Esito" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ResultType"/&amp;gt;
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
        "nome",
        "esito"
    })
    public static class EsitoStep {

        @XmlElement(name = "Nome", namespace = "http://www.csi.it/stardas/services/StardasCallbackService", required = true)
        protected String nome;
        @XmlElement(name = "Esito", namespace = "http://www.csi.it/stardas/services/StardasCallbackService", required = true)
        protected ResultType esito;

        /**
         * Recupera il valore della propriet� nome.
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
         * Imposta il valore della propriet� nome.
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
         * Recupera il valore della propriet� esito.
         * 
         * @return
         *     possible object is
         *     {@link ResultType }
         *     
         */
        public ResultType getEsito() {
            return esito;
        }

        /**
         * Imposta il valore della propriet� esito.
         * 
         * @param value
         *     allowed object is
         *     {@link ResultType }
         *     
         */
        public void setEsito(ResultType value) {
            this.esito = value;
        }

		@Override
		public String toString() {
			return "EsitoStep [nome=" + nome + ", esito=" + esito + "]";
		}
        
        

    }

	@Override
	public String toString() {
		return "EsitiStepType [esitoStep=" + esitoStep + "]";
	}

}
