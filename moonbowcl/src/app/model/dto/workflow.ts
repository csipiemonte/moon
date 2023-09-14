/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Stato } from "./stato";

export class Workflow {
  idWorkflow: number;
  idStatoWfPartenza: number;
  nomeStatoPartenza: string;
  idStatoWfArrivo: number;
  nomeStatoArrivo: string;
  idAzione: number;
  codiceAzione: string;
  nomeAzione: string;
  destinatario: string;
  idTipoUtenteDestinatario: number;
  idDatiAzione: number;
  isAzioneConDati: boolean;
  isAnnullabile: boolean;
  isArchiviabile: boolean;
  isAutomatico: boolean;
  idProcesso: number;
  idCondizione: number;

  statoPartenza: Stato;
  statoArrivo: Stato;
  
  constructor(init?: Partial<Workflow>) {
    Object.assign(this, init);
  }

}
