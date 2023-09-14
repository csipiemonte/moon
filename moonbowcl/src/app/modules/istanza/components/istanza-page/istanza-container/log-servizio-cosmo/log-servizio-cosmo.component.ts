/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, Output, OnInit, SimpleChanges, EventEmitter } from '@angular/core';
import { LogPraticaCosmo } from 'src/app/model/dto/log-pratica-cosmo';
import { LogServizioCosmo } from 'src/app/model/dto/log-servizio-cosmo';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-istanza-log-servizio-cosmo',
  templateUrl: './log-servizio-cosmo.component.html',
  styleUrls: ['./log-servizio-cosmo.component.scss']
})
export class LogServizioCosmoComponent implements OnInit {

  @Input('data') data: LogServizioCosmo[];
  @Output('initData') initData = new EventEmitter();

  isAdmin = false;
  pratica: object = new Object();

  constructor(private sharedService: SharedService) {
    this.isAdmin = sharedService.UserLogged?.isTipoADMIN();
   }

  ngOnInit(): void {
    log('istanza-log-servizio-cosmo::ngOnInit this.data=' + this.data);
    // if (!this.data) {
      log('istanza-log-servizio-cosmo::ngOnInit this.data undefined this.initData.emit();');
      this.initData.emit();
    // }
  }

  ngOnChanges(changes: SimpleChanges) {
    log('istanza-log-cosmo::ngOnChanges() changes.data.currentValue=' + changes.data.currentValue);
    log('istanza-log-cosmo::ngOnChanges() this.data=' + this.data);
    if (this.data) {
      this.data.forEach((element) => {
        element.richiestaObj = JSON.parse(element.richiesta);
        element.rispostaObj = JSON.parse(element.risposta);

      });
    }
  }
}

function log(a: any) {
//  console.log(a);
}
