/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class LogEmail {
    idLogEmail: number;
    dataLogEmail: Date;
    tipologia: {
        codice: string;
        descrizione: string;
    };
    idEnte: number;
    idModulo: number;
    idIstanza: number;
    emailDestinatario: string;
    tipoEmail: string;
    esito: string;
}
