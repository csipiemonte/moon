/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per StatoRichiestaType.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="StatoRichiestaType"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="IN_CORSO_DI_ACQUISIZIONE"/&amp;gt;
 *     &amp;lt;enumeration value="ERRORE_IN_FASE_DI_ACQUISIZIONE"/&amp;gt;
 *     &amp;lt;enumeration value="DA_ELABORARE"/&amp;gt;
 *     &amp;lt;enumeration value="ERRORE_IN_FASE_DI_ELABORAZIONE"/&amp;gt;
 *     &amp;lt;enumeration value="ELABORATA"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "StatoRichiestaType", namespace = "http://www.csi.it/epay/epaywso/types")
@XmlEnum
public enum StatoRichiestaType {

    IN_CORSO_DI_ACQUISIZIONE,
    ERRORE_IN_FASE_DI_ACQUISIZIONE,
    DA_ELABORARE,
    ERRORE_IN_FASE_DI_ELABORAZIONE,
    ELABORATA;

    public String value() {
        return name();
    }

    public static StatoRichiestaType fromValue(String v) {
        return valueOf(v);
    }

}
