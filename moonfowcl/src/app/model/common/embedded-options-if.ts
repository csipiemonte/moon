/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {UserInfo} from './user-info';
import {DichiaranteIstanza} from './dichiarante-istanza';
import {UserIF} from './user-if';

export interface EmbeddedOptionsIF {

  urlEsciModulo: string;
  urlTornaIstanze: string;
  urlErrore: string;
  tabIstanze: string[];


}
