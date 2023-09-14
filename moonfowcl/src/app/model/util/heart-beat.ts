/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class HeartBeat {
  private timerId;
  private timeout = 30000;
  private handler;

  constructor(handler, timeout) {
    this.handler = handler;
    this.timeout = timeout;
  }

  startBeat() {
    this.timerId = setInterval(this.handler, this.timeout);
  }

  stopBeat() {
    if (this.timerId) {
      console.log(`${this.timerId} annullato `);
      clearTimeout(this.timerId);
    }
  }
}
