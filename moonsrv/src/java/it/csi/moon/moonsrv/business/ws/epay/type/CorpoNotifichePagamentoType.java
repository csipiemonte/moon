/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per CorpoNotifichePagamentoType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="CorpoNotifichePagamentoType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="ElencoNotifichePagamento"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="NotificaPagamento" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}NotificaPagamentoType" maxOccurs="1000"/&amp;gt;
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
@XmlType(name = "CorpoNotifichePagamentoType", propOrder = {
    "elencoNotifichePagamento"
})
public class CorpoNotifichePagamentoType {

    @XmlElement(name = "ElencoNotifichePagamento", required = true)
    protected CorpoNotifichePagamentoType.ElencoNotifichePagamento elencoNotifichePagamento;

    /**
     * Recupera il valore della propriet� elencoNotifichePagamento.
     * 
     * @return
     *     possible object is
     *     {@link CorpoNotifichePagamentoType.ElencoNotifichePagamento }
     *     
     */
    public CorpoNotifichePagamentoType.ElencoNotifichePagamento getElencoNotifichePagamento() {
        return elencoNotifichePagamento;
    }

    /**
     * Imposta il valore della propriet� elencoNotifichePagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link CorpoNotifichePagamentoType.ElencoNotifichePagamento }
     *     
     */
    public void setElencoNotifichePagamento(CorpoNotifichePagamentoType.ElencoNotifichePagamento value) {
        this.elencoNotifichePagamento = value;
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
     *         &amp;lt;element name="NotificaPagamento" type="{http://www.csi.it/epay/epaywso/epaywso2enti/types}NotificaPagamentoType" maxOccurs="1000"/&amp;gt;
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
        "notificaPagamento"
    })
    public static class ElencoNotifichePagamento {

        @XmlElement(name = "NotificaPagamento", required = true)
        protected List<NotificaPagamentoType> notificaPagamento;

        /**
         * Gets the value of the notificaPagamento property.
         * 
         * &lt;p&gt;
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the notificaPagamento property.
         * 
         * &lt;p&gt;
         * For example, to add a new item, do as follows:
         * &lt;pre&gt;
         *    getNotificaPagamento().add(newItem);
         * &lt;/pre&gt;
         * 
         * 
         * &lt;p&gt;
         * Objects of the following type(s) are allowed in the list
         * {@link NotificaPagamentoType }
         * 
         * 
         */
        public List<NotificaPagamentoType> getNotificaPagamento() {
            if (notificaPagamento == null) {
                notificaPagamento = new ArrayList<>();
            }
            return this.notificaPagamento;
        }

    }

}
