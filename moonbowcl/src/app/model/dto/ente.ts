/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class Ente {
    idEnte: number;
    codiceEnte: string;
    nomeEnte: string;
    descrizioneEnte: string;
    flagAttivo: boolean;
    idTipoEnte: number;
    logo: string;
    indirizzo: string;
    ataUpd: Date;
    attoreUpd: string;
    idEntePadre: number;
    codiceIpa: string;
}