/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class Documento {
  idFile: number;
  nomeFile: string;
  flEliminato: string;
  flFirmato: string;
  formioKey: string;
  formioNameFile: string;
  hashFile: string;
  contentType: string;
  idTipologia: string;
  idIstanza: number;
  idStoricoWorkflow: number;
  codiceFile: string;
  tipologiaFruitore: string;
  refUrl: string;
  contenuto: string;
  descrizione: string;
  lunghezza: number;
  numeroProtocollo: number;
  dataProtocollo: Date;
  dataCreazione: Date;
}
