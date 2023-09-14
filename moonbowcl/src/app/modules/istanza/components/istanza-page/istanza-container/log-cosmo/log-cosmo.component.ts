/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, Output, OnInit, SimpleChanges, EventEmitter } from '@angular/core';
import { LogPraticaCosmo } from 'src/app/model/dto/log-pratica-cosmo';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-istanza-log-cosmo',
  templateUrl: './log-cosmo.component.html',
  styleUrls: ['./log-cosmo.component.scss']
})
export class LogCosmoComponent implements OnInit {

  @Input('data') data: LogPraticaCosmo[];
  @Output('initData') initData = new EventEmitter();

  isAdmin = false;
/*
  idLogPratica: number;
  idPratica: number;
  dataIns: Date;
  creaRichiesta: object = new Object();
  creaRisposta: object = new Object();
  creaDocumentoRichiesta: object = new Object();
  creaDocumentoRisposta: object = new Object();
  avviaProcessoRichiesta: object = new Object();
  avviaProcessoRisposta: object = new Object();
  dataAvvio: Date;
  errore: string;
*/
  pratica: object = new Object();

  constructor(private sharedService: SharedService) {
    this.isAdmin = sharedService.UserLogged?.isTipoADMIN();
   }

  ngOnInit(): void {
    log('istanza-log-cosmo::ngOnInit this.data=' + this.data);
    // if (!this.data) {
      log('istanza-log-cosmo::ngOnInit this.data undefined this.initData.emit();');
      this.initData.emit();
    // }
  }

  ngOnChanges(changes: SimpleChanges) {
    log('istanza-log-cosmo::ngOnChanges() changes.data.currentValue=' + changes.data.currentValue);
    log('istanza-log-cosmo::ngOnChanges() this.data=' + this.data);
    if (this.data) {
      this.data.forEach((element) => {
        element.creaRichiestaObj = JSON.parse(element.creaRichiesta);
        element.creaRispostaObj = JSON.parse(element.creaRisposta);
        element.creaDocumentoRichiestaObj = JSON.parse(element.creaDocumentoRichiesta);
        element.creaDocumentoRispostaObj = JSON.parse(element.creaDocumentoRisposta);
        element.avviaProcessoRichiestaObj = JSON.parse(element.avviaProcessoRichiesta);
        element.avviaProcessoRispostaObj = JSON.parse(element.avviaProcessoRisposta);
        element.praticaObj = JSON.parse(element.pratica);
      });
    }
  }
}

function log(a: any) {
//  console.log(a);
}
