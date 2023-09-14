/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {PagamentoNotifica} from './pagamento-notifica';

export class Pagamento {
  idEpay: string;
  idIstanza: number;
  idModulo: number;
  idTipologiaEpay: number;
  idStoricoWorkflow: number;
  richiesta: string;
  iuv: string;
  codiceAvviso: string;
  importo: number;
  dataInserimento: Date;
  dataAnnullamento: Date;

  notifica: PagamentoNotifica;
}
