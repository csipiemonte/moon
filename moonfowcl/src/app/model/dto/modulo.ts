/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/*
DTO relativo a Modulo
 */

import {Categoria} from './categoria';
import {Attributo} from './attributo';
import {StatoModulo} from './statoModulo';

export class Modulo {
  idModulo: number;
  idVersioneModulo: number;
  codiceModulo: string;
  versioneModulo: string;
  oggettoModulo = null;
  descrizioneModulo: string;
  dataIns: Date;
  dataUpd: Date;
  flagIsRiservato: boolean;
  //flagUsaProgressivo: boolean;
  flagProtocolloIntegrato: boolean;
  attoreUpd: string;
  idModuloStruttura: number;
  tipoStruttura: string;
  struttura: string;
  // ultimo step selezionato
  currentStep: number;
  categoria: Categoria;
  stato: StatoModulo;
  riportaInBozzaAbilitato: boolean;
  attributi: Attributo[];
}
