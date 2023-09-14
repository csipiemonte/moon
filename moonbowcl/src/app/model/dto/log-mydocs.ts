/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class LogMyDocs {
    idIstanza: number;
    idRichiesta: number;
    dataRichiesta: Date;
    stato: string;
    tipoDoc: number;
    idAllegatoIstanza;
    idFile: number;
    nomeFile: string;
    idModulo: number;
    idArea: number;
    idEnte: number;
    idStoricoWorkflow: number;
    idAmbitoMydocs: number;
    idTipologiaMydocs: number;
    uuidMydocs: string;
    note: string;
    codiceEsito: string;
    descEsito: string;
    dataUpd: Date;    
}
