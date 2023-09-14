/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { CodiceDescrizione } from './codice-descrizione';

export class Utente {
    idUtente: number;
    identificativoUtente: string;
    nome: string;
    cognome: string;
    username: string;
    password: string;
    email: string;
    flagAttivo: boolean;
    tipoUtente: CodiceDescrizione;
    dataIns: Date;
    dataUpd: Date;
    attoreIns: string; // new
    attoreUpd: string;
}
