/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class Workflow {
  idWorkflow: number;
  idStatoArrivo: number;
  idStatoWfPartenza: number;
  nomeStatoArrivo: string;
  idAzione: number;
  codiceAzione: string;
  nomeAzione: string;
  destinatario: string;
  idDatiAzione: number;
  isAzioneConDati: boolean;
  isAnnullabile: boolean;
  isArchiviabile: boolean;
}
