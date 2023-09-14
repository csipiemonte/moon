/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class LogPraticaCosmo {
    idLogPratica: number;
    idPratica: string;
    idIstanza: number;
    idx: number;
    idModulo: number;
    dataIns: Date;
    dataAvvio: Date;
    dataUpd: Date;
    creaRichiesta: string;
    creaRisposta: string;
    creaDocumentoRichiesta: string;
    creaDocumentoRisposta: string;
    avviaProcessoRichiesta: string;
    avviaProcessoRisposta: string;
    errore: string;
    pratica: string;

    creaRichiestaObj = new Object();
    creaRispostaObj = new Object();
    creaDocumentoRichiestaObj = new Object();
    creaDocumentoRispostaObj = new Object();
    avviaProcessoRichiestaObj = new Object();
    avviaProcessoRispostaObj = new Object();
    praticaObj = new Object();
}
