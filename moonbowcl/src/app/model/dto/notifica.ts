/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Istanza } from './istanza';

export class Notifica {

    constructor(codice: string, descrizione: string, titolo: string, istanza: Istanza) {
        this.codice = codice;
        this.descrizione = descrizione;
        this.titolo = titolo;
        this.istanza = istanza;
    }

    codice: string;
    descrizione: string;
    titolo: string;
    istanza: Istanza;
    showBack: boolean;
}
