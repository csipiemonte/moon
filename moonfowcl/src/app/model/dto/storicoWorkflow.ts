/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class StoricoWorkflow {
  idStoricoWorkflow: number;
  idProcesso: number;
  idStatoPartenza: number;
  idStatoArrivo: number;
  nomeStatoWfPartenza: string;
  nomeStatoWfArrivo: string;
  nomeAzione: string;
  datiAzione: any;
  descDestinatario: string;
  dataInizio: Date;
  dataFine: Date;
  contieneDati: boolean;
  contieneOutput: boolean;
  strutturaDatiAzione: string;
  idFileRendering: number;
  attoreUpd: string;
}
