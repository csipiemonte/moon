/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class VerificaPagamento {
    idEpay: string;
    codiceEsito: string;
    descrizioneEsito: string;
    descrizioneStatoPagamento: string;
    iuvOriginario: string;
    iuvEffettivo: string;
    rtXml: string;
}