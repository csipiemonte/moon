/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class UtenteModuloAbilitato {
    idUtente: number;
    identificativoUtente: string;
    nome: string;
    cognome: string;
    email: string;
    flagAttivo: boolean;
    tipoUtente: {
        codice: string;
        descrizione: string;
    }
    dataUpdAbilitazione: Date;
    attoreUpdAbilitazione: string;
}