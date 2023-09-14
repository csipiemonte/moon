/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ProtocolloMetadatoStato } from './protocollo-metadato-stato';

export class ProtocolloMetadatoKeyConf {

    constructor(nomeMetadato: string, statoMetadato: ProtocolloMetadatoStato) {
        this.nomeMetadato = nomeMetadato;
        this.statoMetadato = statoMetadato;
    }

    nomeMetadato: string;
    statoMetadato: ProtocolloMetadatoStato;
}
