/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SharedService } from 'src/app/services/shared.service';
import { faCheck, faX } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-istanza-log-ticket',
  templateUrl: './log-ticket.component.html',
  styleUrls: ['./log-ticket.component.scss']
})
export class LogTicketComponent implements OnInit {

  @Input('data') data: any[];
  //@Output('initData') initData = new EventEmitter();
  isAdmin = false;
  faCheck = faCheck;
  faX = faX;

  constructor(private sharedService: SharedService) {
    this.isAdmin = sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    //console.log('istanza-log-email::ngOnInit this.data=' + this.data);
    //this.initData.emit();
  }

}
