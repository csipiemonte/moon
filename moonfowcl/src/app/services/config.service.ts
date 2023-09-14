/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {environment} from 'src/environments/environment';

/**
 * Servizio di configurazione
 */
@Injectable()
export class ConfigService {

  /**
   * server del backend nel formato http://server:port
   */
  getBEServer(): string {
    return environment.beServer;
  }


  /**
   * URL a cui saltare in caso di logout locala
   */
  getOnAppExitURL(): string {
    return environment.onAppExitURL;
  }

  getTimeout(): number {
    return environment.timeout;
  }

  getUrlSportello(): string {
    return environment.urlPiemontePay;
  }


  getWsServer() {
    if (environment.production) {
      if (window.location.port) {
        return 'wss://' + window.location.hostname + ':' + window.location.port + '/moonfobl/websocket';
      } else {
        return 'wss://' + window.location.hostname + '/moonfobl/websocket';
      }
    } else {  // url locale
      return environment.wsServer + '/moonfobl/websocket';
    }
  }
}
