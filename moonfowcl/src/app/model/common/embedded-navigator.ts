/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {UserInfo} from './user-info';
import {DichiaranteIstanza} from './dichiarante-istanza';
import {UserIF} from './user-if';
import {EmbeddedOptions} from './embedded-options';
import {EmbeddedNavParams} from './embedded-nav-params';

export class EmbeddedNavigator {

  service: string;
  options: EmbeddedOptions;
  params: EmbeddedNavParams;

  constructor(service: string, options: EmbeddedOptions, params: EmbeddedNavParams) {
    this.service = service;
    this.options = options;
    this.params = params;
  }

}
