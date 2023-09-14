/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Stato} from './stato';
import {Modulo} from './modulo';
import {Pagamento} from './pagamento';


enum AZIONE {
  GESTISCI_PAGAMENTO = 'GESTISCI_PAGAMENTO',
  INVIA = 'INVIA'
}

export class Istanza {
  idIstanza: string;
  codiceIstanza: string;
  azione: string;
  stato: Stato;
  created: Date;
  modified: Date;
  data: any;
  modulo: Modulo;
  attoreIns: string;
  attoreUpd: string;
  metadata: any;
  importanza: number;
  flagEliminata: boolean;
  flagArchiviata: boolean;
  dataInvio: Date;
  numeroProtocollo: string;
  dataProtocollo: Date;
  iuv: string;
  codiceAvviso: string;
  dataEsitoPagamento: Date;
  pagamenti: Pagamento[];
  nomeDichiarante: string;
  cognomeDichiarante: string;
  codiceFiscaleDichiarante: string;
}
