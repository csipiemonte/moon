/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class LogServizioCosmo {
    idLogServizio: number;
    idPratica: string;
    idIstanza: number;
    idModulo: number;
    servizio: string;
    dataIns: Date;
    dataAvvio: Date;
    dataUpd: Date;
    richiesta: string;
    risposta: string;
    richiestaObj = new Object();
    rispostaObj = new Object();
}
