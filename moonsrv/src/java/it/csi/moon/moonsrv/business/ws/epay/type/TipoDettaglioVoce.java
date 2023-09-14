/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


package it.csi.moon.moonsrv.business.ws.epay.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per TipoDettaglioVoce.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="TipoDettaglioVoce"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="IMPORTO_TRANSATO"/&amp;gt;
 *     &amp;lt;enumeration value="IMPORTO_AUTORIZZATO"/&amp;gt;
 *     &amp;lt;enumeration value="IMPORTO_COMMISSIONI"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "TipoDettaglioVoce")
@XmlEnum
public enum TipoDettaglioVoce {

    IMPORTO_TRANSATO,
    IMPORTO_AUTORIZZATO,
    IMPORTO_COMMISSIONI;

    public String value() {
        return name();
    }

    public static TipoDettaglioVoce fromValue(String v) {
        return valueOf(v);
    }

}
