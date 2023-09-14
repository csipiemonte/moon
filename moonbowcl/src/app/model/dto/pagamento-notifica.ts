/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class PagamentoNotifica {
  idPosizioneDebitoria: string;
  importoPagato: number;
  dataEsitoPagamento: Date;
  importoTransato: number;
  importoCommissioni: number;
  idPsp: string;
  ragioneSocialePsp: string;
  dataOraAvvioTransazione: Date;
}
