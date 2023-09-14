/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class MoonboError {
    codice: number;
    // viene mostrato nel componente che gestisce gli errori
    errorMsg: string;
    scope: string;
    // messaggio tecnico di errore
    techMsg: string;
    url: string;
    //
    errorCodice: string;
    errorTitle: string;
}
