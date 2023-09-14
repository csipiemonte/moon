/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export interface EpayNotificaMsg {
  idNotificaPagamento: Number;
  idNotificaPagamTesta: Number;
  idPosizioneDebitoria: String;
  annoDiRiferimento: Number;
  iuv: String;
  importoPagato: Number;
  dataScadenza: Date;
  descCausaleVersamento: String;
  dataEsitoPagamento: Date;
  soggettoDebitore: String;
  soggettoVersante: String;
  datiTransazionePsp: String;
  datiSpecificiRiscossione: String;
  note: String;
  codiceAvviso: String;
  dataIns: Date;
}
