/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { Modulo } from 'src/app/model/dto/modulo';
import { faClone, faEdit, faHome, faInfo, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { AttrEpayConf } from 'src/app/model/dto/attr/attrEpayConf';
import { MsgAttrEpay } from 'src/app/common/messaggi';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-epay',
  templateUrl: './attr-epay.component.html',
  styleUrls: ['./attr-epay.component.scss']
})
export class AttrEpayComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  elencoModuloAttributi: ModuloAttributo[];
  maEpay: ModuloAttributo = new ModuloAttributo();
  maEpayEdit: ModuloAttributo = new ModuloAttributo();
  maEpayConf: ModuloAttributo = new ModuloAttributo();

  attrEpayConf: object = new Object();
  attrEpayConfEdit: AttrEpayConf = new AttrEpayConf();

  titleUpd: Map<string, string> = new Map<string, string>();

  isAdmin = false;
  isOpAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  modeError = false;

  maEpayEditT = false;

  alertOptions = {
    id: 'alert-moduli-attr-epay',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(fb: FormBuilder,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpAdmin = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
  }

  ngOnInit(): void {
    
    this.alert.emit({clear: true});
    this.init();
  }

  init(): void {
    this.spinnerService.show();
    this.moonboblService.getModuloWithFields(this.moduloSelezionato.idModulo,
      this.moduloSelezionato.idVersioneModulo,
      'attributiEpay').subscribe(
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
          this.modeError = true;
          this.spinnerService.hide();
        }
      );
  }

  initFields() {
    log('initFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
   this.maEpay = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_EPAY', 'N');
   this.maEpayEdit = { ...this.maEpay };
   this.maEpayEditT = this.maEpayEdit ? (this.maEpayEdit['valore'] === 'N' ? false : true) : false;
   log('initFields() this.maEpay: ' + JSON.stringify(this.maEpay));
   log('initFields() this.maEpayEdit: ' + JSON.stringify(this.maEpayEdit));
    //
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maEpayConf = this.elencoModuloAttributi.find(ma => ma.nome === 'PSIT_EPAY_CONF');
      if (this.maEpayConf) {
        this.attrEpayConf = JSON.parse(this.maEpayConf.valore);
        this.attrEpayConfEdit = JSON.parse(this.maEpayConf.valore); // Object.assign({}, this.maEpayConf);
        this.titleUpd.set(this.maEpayConf.nome, 'Ultima modifica il ' + this.maEpayConf.dataUpd + ' da ' + this.maEpayConf.attoreUpd);
      }
      log('initFields() this.attrEpayConf: ' + JSON.stringify(this.attrEpayConf));
      log('initFields() this.attrEpayConfEdit: ' + JSON.stringify(this.attrEpayConfEdit));
    }
    if (!this.maEpayConf) {
      this.maEpayConf = creaModuloAttributo('PSIT_EPAY_CONF', '{}');
    }
  }


  findOrCreateWithDefaultValue(moduloAttributi: ModuloAttributo[], nome: string, defaultValore: string): ModuloAttributo {
    let result: ModuloAttributo;
    if (moduloAttributi && moduloAttributi.length > 0) {
      result = moduloAttributi.find(ma => ma.nome === nome);
    }
    if (!result) {
      result = creaModuloAttributo(nome, defaultValore);
    } else {
      this.titleUpd.set(nome, 'Ultima modifica il ' + result.dataUpd + ' da ' + result.attoreUpd);
    }
    return result;
  }

  salva() {
    this.spinnerService.show();
    // tslint:disable-next-line:prefer-const
    let maToSave = [];
    if (this.maEpayEdit) {
      this.maEpayEdit['valore'] = this.maEpayEditT ? 'S' : 'N';
    }
    verificaToSave(maToSave, this.maEpay, this.maEpayEdit);
    addToSaveAttrConfJsonStr(maToSave, 'PSIT_EPAY_CONF', this.attrEpayConf, this.epayConfJsonStr);
    log('salva::aggiornaModuloAttributiEpay maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0) {
      this.moonboblService.aggiornaModuloAttributiEpay(this.modulo, maToSave).subscribe(
        (attributi: ModuloAttributo[]) => {
          this.elencoModuloAttributi = attributi;
          this.init();
          this.spinnerService.hide();
          // this.alertService.success('Salvataggio attributi EPAY effettuato con successo !', this.alertOptions);
          this.alert.emit({ text: MsgAttrEpay.SUCCESS_ATTR_EPAY, type: 'success', autoclose:true});
        },
        (err: MoonboError) => {
          this.spinnerService.hide();
          // this.alertService.error('Impossibile effettuare il salvataggio attributi next EPAY !' + err.errorMsg, this.alertOptions);
          this.alert.emit({ text: MsgAttrEpay.ERROR_ATTR_EPAY, type: 'error', autoclose:false});
        }
      );
    } else {
      this.spinnerService.hide();
      // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
      this.alert.emit({ text: MsgAttrEpay.INFO_NO_MODIFY_ATTR_EPAY, type: 'info', autoclose:false});
    }
  }

  get epayConfJsonStr(): string {
    // Non permettiamo il salvataggio di un attributo di tipo string vuoto nel JSON, lo togliamo completamento
    // tslint:disable:max-line-length
    this.attrEpayConfEdit.causale = ((this.attrEpayConfEdit.causale || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.causale.trim();
    this.attrEpayConfEdit.codice_fiscale_ente = ((this.attrEpayConfEdit.codice_fiscale_ente || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.codice_fiscale_ente.trim();
    this.attrEpayConfEdit.tipo_pagamento = ((this.attrEpayConfEdit.tipo_pagamento || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.tipo_pagamento.trim();
    this.attrEpayConfEdit.importo = ((this.attrEpayConfEdit.importo || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.importo.trim();
    this.attrEpayConfEdit.codice_fiscale_piva = ((this.attrEpayConfEdit.codice_fiscale_piva || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.codice_fiscale_piva.trim();
    this.attrEpayConfEdit.cognome = ((this.attrEpayConfEdit.cognome || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.cognome.trim();
    this.attrEpayConfEdit.nome = ((this.attrEpayConfEdit.nome || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.nome.trim();
    this.attrEpayConfEdit.email = ((this.attrEpayConfEdit.email || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.email.trim();
    this.attrEpayConfEdit.ragione_sociale = ((this.attrEpayConfEdit.ragione_sociale || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.ragione_sociale.trim();
    this.attrEpayConfEdit.email = ((this.attrEpayConfEdit.email || '').trim().length === 0) ? undefined : this.attrEpayConfEdit.email.trim();
    // tslint:enable:max-line-length
    return JSON.stringify(this.attrEpayConfEdit);
  }

}

function verificaToSave(result: ModuloAttributo[], ma: ModuloAttributo, maEdit: ModuloAttributo): ModuloAttributo[] {
  if (ma?.valore === maEdit['valore']) {
    log('Unchanged ' + ma['nome'] + ': ' + ma['valore']);
  } else {
    if (maEdit.nome && (maEdit.valore || (!maEdit.valore && ma.valore))) {
      log('Changed ' + ma['nome'] + ' FROM: ' + ma['valore'] + ' TO: ' + maEdit['valore']);
      result.push(maEdit);
    } else {
      log('verificaToSave INVALID ' + ma['nome'] + ' ma: ' + JSON.stringify(ma) + '  maEdit: ' + JSON.stringify(maEdit));
    }
  }
  return result;
}

function addToSaveAttrConfJsonStr(result: ModuloAttributo[], nomeAttributoJson: string, attrConf: Object, confJsonStrEdit: string)
  : ModuloAttributo[] {
  if (attrConf && JSON.stringify(attrConf) === JSON.stringify(JSON.parse(confJsonStrEdit))) {
    log('Unchanged ' + nomeAttributoJson + ': ' + JSON.stringify(attrConf));
  } else {
    log('Changed ' + nomeAttributoJson + ' \nFROM: ' + JSON.stringify(attrConf) + ' \nTO: ' + confJsonStrEdit);
    result.push(creaModuloAttributo(nomeAttributoJson, confJsonStrEdit));
  }
  return result;
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

