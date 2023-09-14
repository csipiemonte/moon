/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {webSocket, WebSocketSubject} from 'rxjs/webSocket';
import {ConfigService} from './config.service';

export class WebSocketAbstractService<T> {

  protected channel: String | null = null;
  private wsSubject: WebSocketSubject<T> | null = null;
  private config: ConfigService;

  protected constructor(channel: String) {
    this.channel = channel;
    this.config = new ConfigService();
  }

  public getWs(): WebSocketSubject<T> {
    try {
      console.log('Url websocket: ' + this.config.getWsServer() + this.channel);
      return webSocket(this.config.getWsServer() + this.channel);
    } catch (e) {
      console.log('Errore ' + e);
      return null;
    }
  }

}
