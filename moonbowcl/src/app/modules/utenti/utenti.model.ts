/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { UtenteEnteAbilitato } from 'src/app/model/dto/utente ente-abilitato';
import { AlertType } from "../alert/alert.model";

export enum UtentiView {
    ELENCO = 'elenco',
    CREA = 'crea',
    DETTAGLIO = 'dettaglio',
    MODIFICA = 'modifica',
    ERRORE = 'errore'
}

export class UtentiViewEvent {
    view: UtentiView = UtentiView.ELENCO;
    alertMessage: string;
    alertType: AlertType;
    utente: UtenteEnteAbilitato;

    constructor(init?: Partial<UtentiViewEvent>) {
        Object.assign(this, init);
    }
}
