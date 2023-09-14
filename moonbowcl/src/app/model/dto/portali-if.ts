/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export interface PortaliIf {
    idPortale: number;
    codicePortale: string;
    nomePortale: string;
    descrizionePortale: string;
    flagAttivo: boolean;
    dataUpd: Date;
    attoreUpd: string;
}
