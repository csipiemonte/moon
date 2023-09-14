/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Categoria } from './categoria';
import { StatoModulo } from './statoModulo';
import { PortaliIf } from './portali-if';
import { Processo } from './processo';
import { VersioneStato } from './versione-stato';
import { ModuloAttributo } from './attr/moduloAttributo';

enum STATI_MODULO {
  INIT = 'INIT',
  TST = 'TST',
  PUB = 'PUB',
  MOD = 'MOD',
  SOSP = 'SOSP',
  RIT = 'RIT',
  ELI = 'ELI'
}



export class Modulo {
  idModulo: number;
  idVersioneModulo: number;
  codiceModulo: string;
  versioneModulo: string;

  oggettoModulo: string;
  descrizioneModulo: string;
  dataIns: Date;
  dataUpd: Date;
  flagIsRiservato: boolean;
  flagProtocolloIntegrato: boolean;
  attoreUpd: string;
  idModuloStruttura: number;
  tipoStruttura: string;
  struttura: string;
  objAttributi: string;
  attributi: ModuloAttributo[]; // NEW Gestione, solo per aggiornamenti attributi
  categoria: Categoria;
  processo: Processo;
  stato: StatoModulo;
  idTipoCodiceIstanza: number;
  portali: PortaliIf[];
  versioni: VersioneStato[];

  constructor() {
    this.idTipoCodiceIstanza = 1;
    this.versioneModulo = '1.0.0';
    this.tipoStruttura = 'FRM';
    this.categoria = new Categoria();
    this.processo = new Processo();
  }

}

