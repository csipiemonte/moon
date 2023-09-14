/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per TipoRichiestaType.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="TipoRichiestaType"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="INSERISCI_LISTA_DI_CARICO"/&amp;gt;
 *     &amp;lt;enumeration value="AGGIORNA_POSIZIONI_DEBITORIE"/&amp;gt;
 *     &amp;lt;enumeration value="TRASMETTI_NOTIFICHE_PAGAMENTO"/&amp;gt;
 *     &amp;lt;enumeration value="TRASMETTI_AVVISI_SCADUTI"/&amp;gt;
 *     &amp;lt;enumeration value="TRASMETTI_FLUSSO_RENDICONTAZIONE"/&amp;gt;
 *     &amp;lt;enumeration value="TRASMETTI_FLUSSO_RENDICONTAZIONE_ESTESO"/&amp;gt;
 *     &amp;lt;enumeration value="TRASMETTI_FLUSSO_RENDICONTAZIONE_COMPLETO"/&amp;gt;
 *     &amp;lt;enumeration value="TRASMETTI_RICHIESTE_DI_REVOCA"/&amp;gt;
 *     &amp;lt;enumeration value="TRASMETTI_RT"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "TipoRichiestaType", namespace = "http://www.csi.it/epay/epaywso/types")
@XmlEnum
public enum TipoRichiestaType {

    INSERISCI_LISTA_DI_CARICO,
    AGGIORNA_POSIZIONI_DEBITORIE,
    TRASMETTI_NOTIFICHE_PAGAMENTO,
    TRASMETTI_AVVISI_SCADUTI,
    TRASMETTI_FLUSSO_RENDICONTAZIONE,
    TRASMETTI_FLUSSO_RENDICONTAZIONE_ESTESO,
    TRASMETTI_FLUSSO_RENDICONTAZIONE_COMPLETO,
    TRASMETTI_RICHIESTE_DI_REVOCA,
    TRASMETTI_RT;

    public String value() {
        return name();
    }

    public static TipoRichiestaType fromValue(String v) {
        return valueOf(v);
    }

}
