/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { AttrApaEmailConf } from 'src/app/model/dto/attr/attrApaEmailConf';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { Modulo } from 'src/app/model/dto/modulo';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-api',
  templateUrl: './attr-api.component.html',
  styleUrls: ['./attr-api.component.scss']
})
export class AttrApiComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;

  modulo: Modulo = new Modulo();
  elencoModuloAttributi: ModuloAttributo[];

  maApiEmailConf: ModuloAttributo = new ModuloAttributo();

  attrApaEmailConf: object = new Object();
  attrApaEmailConfEdit: AttrApaEmailConf = new AttrApaEmailConf();

  titleUpd: Map<string, string> = new Map<string, string>();

  isAdmin = false;
  isOpAdmin = false;
  isModificabile = false;
  isOwner = true;

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpAdmin = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
  }

  ngOnInit(): void {
    this.init();
  }

  init(): void {
    this.spinnerService.show();
    this.moonboblService.getModuloWithFields(this.moduloSelezionato.idModulo,
      this.moduloSelezionato.idVersioneModulo,
      'attributiApi').subscribe(
        (modulo: Modulo) => {
          // Init degli attributi per viewMode
          this.modulo = modulo;
          this.elencoModuloAttributi = modulo.attributi;
          this.initFields();

          // tslint:disable-next-line:max-line-length
          if (this.isAdmin || ((this.modulo.stato.codice === 'INIT' || this.modulo.stato.codice === 'TST' || this.modulo.stato.codice === 'MOD') && this.isOwner)) {
            this.isModificabile = true;
          }

          this.spinnerService.hide();
        },
        (err: MoonboError) => {
          alert(err.errorMsg);
          this.spinnerService.hide();
        }
      );
  }

  initFields() {
    log('initFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
    //    this.maEpay = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_EPAY', 'N');
    //    this.maEpayEdit = { ...this.maEpay };
    //    this.maEpayEditT = this.maEpayEdit ? (this.maEpayEdit['valore'] === 'N' ? false : true) : false;
    //    log('initFields() this.maEpay: ' + JSON.stringify(this.maEpay));
    //    log('initFields() this.maEpayEdit: ' + JSON.stringify(this.maEpayEdit));
    //
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maApiEmailConf = this.elencoModuloAttributi.find(ma => ma.nome === 'APA_EMAIL_CONF');
      if (this.maApiEmailConf) {
        this.attrApaEmailConf = JSON.parse(this.maApiEmailConf.valore);
        this.attrApaEmailConfEdit = JSON.parse(this.maApiEmailConf.valore);
        this.titleUpd.set(this.maApiEmailConf.nome, 'Ultima modifica il ' + this.maApiEmailConf.dataUpd
          + ' da ' + this.maApiEmailConf.attoreUpd);
      }
      log('initFields() this.attrApaEmailConf: ' + JSON.stringify(this.attrApaEmailConf));
      log('initFields() this.attrApaEmailConfEdit: ' + JSON.stringify(this.attrApaEmailConfEdit));
    }
    if (!this.maApiEmailConf) {
      this.maApiEmailConf = creaModuloAttributo('APA_EMAIL_CONF', '{}');
    }
  }

}

function creaModuloAttributo(nome: string, valore: string): ModuloAttributo {
  const obj = new ModuloAttributo();
  obj.nome = nome;
  obj.valore = valore;
  return obj;
}

function log(a: any) {
  // console.log(a);
}