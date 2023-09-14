/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { EnteAreaRuolo } from "./ente-area-ruolo";

export class UtenteEnteAbilitato {
    idUtente: number;
    identificativoUtente: string;
    nome: string;
    cognome: string;
    email: string;
    flagAttivo: boolean;
    tipoUtente: {
        codice: string;
        descrizione: string;
    };
    dataIns: Date;
    dataUpd: Date;
    attoreIns: string;
    attoreUpd: string;

    entiAreeRuoli: EnteAreaRuolo[];
    wclKey: string;
}
