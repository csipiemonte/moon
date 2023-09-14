/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {EpayNotificaMsg} from '../../../../../../../model/dto/epay-notifica-msg';

@Component({
  selector: 'app-notifica-pagamento',
  templateUrl: './notifica-pagamento.component.html',
  styleUrls: ['./notifica-pagamento.component.scss']
})
export class NotificaPagamentoComponent implements OnInit {
  @Input('notificaPagamento') notificaPagamento: EpayNotificaMsg;

  constructor() {
  }

  ngOnInit(): void {
  }

}
