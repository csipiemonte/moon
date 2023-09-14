/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, Output, OnInit, SimpleChanges, EventEmitter } from '@angular/core';
import { faCheck, faExclamation, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-istanza-log-email',
  templateUrl: './log-email.component.html',
  styleUrls: ['./log-email.component.scss']
})
export class LogEmailComponent implements OnInit {

  @Input('data') data: any[];
  @Output('initData') initData = new EventEmitter();

  faCheck = faCheck;
  faExclamationTriangle = faExclamationTriangle; // ERR NOT FOUND
  faExclamation = faExclamation;

  isAdmin = false;

  constructor(private sharedService: SharedService) {
    this.isAdmin = sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    log('istanza-log-email::ngOnInit this.data=' + this.data);
    // if (!this.data) {
      log('istanza-log-email::ngOnInit this.data undefined this.initData.emit();');
      this.initData.emit();
    // }
  }

  ngOnChanges(changes: SimpleChanges) {
    log('istanza-log-email::ngOnChanges() changes.data.currentValue=' + changes.data.currentValue);
    log('istanza-log-email::ngOnChanges() this.data=' + this.data);
    this.data = changes.data.currentValue;
  }

  // esito:  OK;OK  OR  OK;KO  OR  KO;KO OR  KO;KO
  faCheckExclamation(fullEsito: string, idx: number): any {
    let result = faExclamation;
    if (fullEsito && fullEsito.substring(3 * idx, 4 * idx) === 'OK') {
      result = faCheck;
    }
    return result;
  }
}

function log(a: any) {
//  console.log(a);
}
