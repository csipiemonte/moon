/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class Ruolo {
  idRuolo: number;
  codiceRuolo: string;
  nomeRuolo: string;

  constructor(idRuolo: number, codiceRuolo: string, nomeRuolo: string) {
    this.idRuolo = idRuolo;
    this.codiceRuolo = codiceRuolo;
    this.nomeRuolo = nomeRuolo;
  }

}
