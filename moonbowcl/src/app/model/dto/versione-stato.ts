/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class VersioneStato {
  id: number;
  idParent: number;

  idVersioneModulo: number;
  idModulo: number;

  versioneModulo: string;
  dataUpd: Date;
  attoreUpd: string;

  codice: string;
  descrizione: string;
  dataInizioValidita: Date;
  dataFineValidita: Date;

  constructor() {
  }
}

