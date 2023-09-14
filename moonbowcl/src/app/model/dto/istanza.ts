/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Stato} from './stato';
import {Modulo} from './modulo';
import { Pagamento } from './pagamento';

export class Istanza {
  idIstanza: string;
  codiceIstanza: string;
  codiceFiscaleDichiarante: string;
  nomeDichiarante: string;
  cognomeDichiarante: string;
  stato: Stato;
  statoBo: Stato;
  created: Date;
  dataStato: Date;
  modified: Date;
  data: any;
  modulo: Modulo;
  attoreIns: string;
  attoreUpd: string;
  flagEliminata: boolean;
  flagArchiviata: boolean;
  flagTest: boolean = false;
  importanza: number;
  metadata: any;
  numeroProtocollo: string;
  dataProtocollo: Date;
  dataEsitoPagamento: Date;
  pagamenti: Pagamento[];
  isPagato: boolean;

  operatore: string;
  idEnte: number;
  idEArea: number;

  utenteRichiedente: string;
  utenteCompilante: string;
}
