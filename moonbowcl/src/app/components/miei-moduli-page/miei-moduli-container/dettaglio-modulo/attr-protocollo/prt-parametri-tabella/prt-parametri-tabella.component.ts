/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { ProtocolloParametro } from 'src/app/model/dto/protocollo-parametro';
import { Utente } from 'src/app/model/dto/utente';
import { AlertService } from 'src/app/modules/alert';
import { UtentePipe } from 'src/app/pipes/utente-pipe';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import * as _ from 'lodash';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-protocollo-parametri-tab',
  templateUrl: './prt-parametri-tabella.component.html',
  styleUrls: ['./prt-parametri-tabella.component.scss']
})
export class PrtParametriTabellaComponent implements OnInit, OnChanges {

  @Input() prtParametri: ProtocolloParametro[];

  mapOperatori: Map<string, Utente> = new Map<string, Utente>();
  isAdmin = false;

  constructor(private moonboblService: MoonboblService,
              private sharedService: SharedService,
              protected alertService: AlertService,
              private utentePipe: UtentePipe) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    this.orderPrtParametri();
//    this.completaMapOperatori();
  }
  ngOnChanges(val) {
    log('prt-parametri-tabella::ngOnChanges() this.prtParametri = ' + this.prtParametri);
    this.orderPrtParametri();
  }

  orderPrtParametri() {
    if (this.prtParametri) {
      log('prt-parametri-tabella::orderPrtParametri() BEFORE this.prtParametri = ' + this.prtParametri);
      this.prtParametri = _.orderBy(this.prtParametri, item => item.order);
      log('prt-parametri-tabella::orderPrtParametri() AFTER this.prtParametri = ' + this.prtParametri);
    } else {
      log('prt-parametri-tabella::orderPrtParametri() this.prtParametri NULL.');
    }
  }

  completaMapOperatori() {
    if (this.prtParametri) {
      const attori = this.prtParametri.map(p => p.attoreUpd);
      const distinctAttori = attori.filter((n, i) => attori.indexOf(n) === i);
      log('prt-parametri-tabella::completaMapOperatori() distinctAttori = ' + distinctAttori);
      distinctAttori.forEach(attore => {
        log('prt-parametri-tabella::completaMapOperatori() LOOP attore = ' + attore);
        if (!this.mapOperatori.get(attore)) {
          this.moonboblService.getUtenteByIdentificativo(attore).subscribe(
            (resOperatore: Utente) => {
              this.mapOperatori.set(attore, resOperatore);
            },
            (err: MoonboError) => {
              log('prt-parametri-tabella::completaMapOperatori() distinctAttori = ' + err.errorMsg);
            }
          );
        }
      });
    }
    log('prt-parametri-tabella::completaMapOperatori() mapOperatori = ' + this.mapOperatori);
  }

  findNomeOperatore(identificativoAttore: string): string {
    const operatore = this.mapOperatori.get(identificativoAttore);
    return operatore ? this.utentePipe.transform(operatore, 'NC') : identificativoAttore;
  }

}

function log(a: any) {
//  console.log(a);
}
