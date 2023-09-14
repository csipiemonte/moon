/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {WebSocketAbstractService} from './web-socket-abstract.service';
import {WsEpayNotificaMsg} from '../model/dto/ws-epay-notifica-msg';

@Injectable()
export class WebSocketEpayNotificaService extends WebSocketAbstractService<WsEpayNotificaMsg> {

  constructor() {
    super('/epay');
  }
}
