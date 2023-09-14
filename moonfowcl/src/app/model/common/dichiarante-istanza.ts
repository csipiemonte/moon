/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class DichiaranteIstanza {
  nome: string;
  cognome: string;
  codiceFiscale: string;


  constructor(nome: string, cognome: string, codiceFiscale: string) {
    this.nome = nome;
    this.cognome = cognome;
    this.codiceFiscale = codiceFiscale;
  }
}
