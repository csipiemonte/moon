/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Processo } from "src/app/model/dto/processo";
import { AlertType } from "../alert/alert.model";
import { Workflow } from "src/app/model/dto/workflow";

export enum ProcessiView {
    ELENCO = 'elenco',
    CREA = 'crea',
    DETTAGLIO = 'dettaglio',
    DETTAGLIO_WF = 'dettaglio-wf',
    MODIFICA = 'modifica',
    ERRORE = 'errore'
}

export class ProcessiViewEvent {
    view: ProcessiView = ProcessiView.ELENCO;
    alertMessage: string;
    alertType: AlertType;
    processo: Processo;
    idWorkflow: number;

    constructor(init?: Partial<ProcessiViewEvent>) {
        Object.assign(this, init);
    }
}

export class StatoWf {
    idStato: number;
    nome: string;
}

export class AzioneWf {
    idAzione: number;
    codiceAzione: string;
    nomeAzione: string;
}

export class EntryWf  {
    idWorkflow: number;
    idStatoWfPartenza: number;
    idStatoWfPartenzaOriginal: number;
    idStatoWfArrivo: number;
    idStatoWfArrivoOriginal: number;
    idAzione: number;
    idAzioneOriginal: number;

    constructor(idWorkflow: number, idStatoPartenza: number, idStatoArrivo: number, idAzione: number) {
        this.idWorkflow = idWorkflow;
        this.idStatoWfPartenza = idStatoPartenza;
        this.idStatoWfPartenzaOriginal = idStatoPartenza;
        this.idStatoWfArrivo = idStatoArrivo;
        this.idStatoWfArrivoOriginal = idStatoArrivo;
        this.idAzione = idAzione;
        this.idAzioneOriginal = idAzione;
    }

    isModified() {
        return this.idWorkflow &&
            !(this.idStatoWfPartenza === this.idStatoWfPartenzaOriginal &&
            this.idStatoWfArrivo === this.idStatoWfArrivoOriginal &&
            this.idAzione === this.idAzioneOriginal);
    }

    patched() {
        this.idStatoWfPartenzaOriginal = this.idStatoWfPartenza;
        this.idStatoWfArrivoOriginal = this.idStatoWfArrivo;
        this.idAzioneOriginal = this.idAzione;
    }
}
