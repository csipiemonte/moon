/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/*/
Struttura mnessaggio utilizzato dal web socket
relativo alle notifiche Epay
 */

import {EpayNotificaMsg} from './epay-notifica-msg';

export interface WsEpayNotificaMsg {
  cmd: string;
  iuv: string;
  data: EpayNotificaMsg;
  authToken: string;
}
