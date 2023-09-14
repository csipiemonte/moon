/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {environment} from 'src/environments/environment';

export const AppConfig = {

  get apiUrl() {
    if (environment.apiUrlIncludeContext) {
      return `${window.location.protocol}//${window.location.hostname}:${window.location.port}/moonfobl`;
    } else {
      return `${window.location.protocol}//${window.location.hostname}:${window.location.port}`;
    }
  }

};



