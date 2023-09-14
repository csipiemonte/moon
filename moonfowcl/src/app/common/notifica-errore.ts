/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class NotificaErrore {
  codice: string;
  descrizione: string;
  titolo: string;

  constructor(codice: string, descrizione: string, titolo: string) {
    this.codice = codice;
    this.descrizione = descrizione;
    this.titolo = titolo;
  }
}
