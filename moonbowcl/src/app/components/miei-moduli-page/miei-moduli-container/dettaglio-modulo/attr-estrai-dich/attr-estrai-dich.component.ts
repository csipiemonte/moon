/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { AttrEstraiDichiaranteConf } from 'src/app/model/dto/attr/attrEstraiDichiaranteConf';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { Modulo } from 'src/app/model/dto/modulo';
import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { faClone, faEdit, faHome, faInfo, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { MsgAttrEstraiDichiarante } from 'src/app/common/messaggi';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-estrai-dich',
  templateUrl: './attr-estrai-dich.component.html',
  styleUrls: ['./attr-estrai-dich.component.scss']
})
export class AttrEstraiDichComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  elencoModuloAttributi: ModuloAttributo[];
  maEstraiDich: ModuloAttributo = new ModuloAttributo();
  maEstraiDichEdit: ModuloAttributo = new ModuloAttributo();
  maEstraiDichConf: ModuloAttributo = new ModuloAttributo();

  attrEstraiDichConf: object = new Object();
  attrEstraiDichConfEdit: AttrEstraiDichiaranteConf = new AttrEstraiDichiaranteConf();

  titleUpd: Map<string, string> = new Map<string, string>();

  isAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  modeError = false;

  maEstraiDichEditT = false;

  alertOptions = {
    id: 'alert-moduli-attr-estrai-dich',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(fb: FormBuilder,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService,
    protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    this.alert.emit({clear: true});
    this.init();
  }

  init(): void {
    this.spinnerService.show();
    this.moonboblService.getModuloWithFields(this.moduloSelezionato.idModulo,
                                            this.moduloSelezionato.idVersioneModulo,
                                            'attributiEstraiDich').subscribe((modulo: Modulo) => {
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
          // informazioni sulla chiamata
          // this.errNotificationError.notification.next(err);
          // alert(err.errorMsg);
          this.alert.emit({ text: err.errorMsg, type: 'error', autoclose:false});
          this.modeError = true;
          this.spinnerService.hide();
        }
      );
  }

  initFields() {
    log('initFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
    this.maEstraiDich = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_ESTRAI_DICHIARANTE', 'N');
    this.maEstraiDichEdit = { ...this.maEstraiDich };
    //this.maEstraiDichEditT = this.maEstraiDichEdit ? (this.maEstraiDichEdit['valore'] === 'N' ? false: true) : false;
    log('initFields() this.maEstraiDich: ' + JSON.stringify(this.maEstraiDich));
    log('initFields() this.maEstraiDichEdit: ' + JSON.stringify(this.maEstraiDichEdit));
    //
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maEstraiDichConf = this.elencoModuloAttributi.find(ma => ma.nome === 'PSIT_ESTRAI_DICHIARANTE_CONF');
      log('initFields() this.maEstraiDichConf: ' + JSON.stringify(this.maEstraiDichConf));
      if (this.maEstraiDichConf) {
        this.attrEstraiDichConf = JSON.parse(this.maEstraiDichConf.valore);
        this.attrEstraiDichConfEdit = JSON.parse(this.maEstraiDichConf.valore); // Object.assign({}, this.maEstraiDichConf);
        this.titleUpd.set(this.maEstraiDichConf.nome, 'Ultima modifica il ' + this.maEstraiDichConf.dataUpd + ' da '
          + this.maEstraiDichConf.attoreUpd);
      }
      log('initFields() this.attrEstraiDichConf: ' + JSON.stringify(this.attrEstraiDichConf));
      log('initFields() this.attrEstraiDichConfEdit: ' + JSON.stringify(this.attrEstraiDichConfEdit));
    }
    if (!this.maEstraiDichConf) {
      this.maEstraiDichConf = creaModuloAttributo('PSIT_ESTRAI_DICHIARANTE_CONF', '{}');
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
    //if (this.maEstraiDichEdit)  this.maEstraiDichEdit['valore'] = this.maEstraiDichEditT ? 'S':'N'; 
    verificaToSave(maToSave, this.maEstraiDich, this.maEstraiDichEdit);
    addToSaveAttrConfJsonStr(maToSave, 'PSIT_ESTRAI_DICHIARANTE_CONF', this.attrEstraiDichConf, this.EstraiDichiaranteConfJsonStr);
    log('salva::aggiornaModuloAttributiEstraiDichiarante maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0) {
      this.moonboblService.aggiornaModuloAttributiEstraiDichiarante(this.modulo, maToSave).subscribe(
        (attributi: ModuloAttributo[]) => {
          this.elencoModuloAttributi = attributi;
          this.init();
          this.spinnerService.hide();
          // this.alertService.success('Salvataggio attributi estrai dichiarante effettuato con successo !', this.alertOptions);
          this.alert.emit({ text: MsgAttrEstraiDichiarante.SUCCESS_ATTR_ESTRAI_DICH, type: 'success', autoclose:true});
        },
        (err: MoonboError) => {
          this.spinnerService.hide();
          // this.alertService.error('Impossibile effettuare il salvataggio attributi estrai dichiarante !' + err.errorMsg, this.alertOptions);
          this.alert.emit({ text: MsgAttrEstraiDichiarante.ERROR_ATTR_ESTRAI_DICH, type: 'error', autoclose:false});
        }
      );
    } else {
      this.spinnerService.hide();
      // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
      this.alert.emit({ text: MsgAttrEstraiDichiarante.INFO_NO_MODIFY_ATTR_ESTRAI_DICH, type: 'info', autoclose:false});
    }
  }

  get EstraiDichiaranteConfJsonStr(): string {
    // Non permettiamo il salvataggio di un attributo di tipo strong vuoto nel JSON, lo togliamo completamento
    // tslint:disable-next-line:prefer-const
    let locAttrEstraiDichConf = new AttrEstraiDichiaranteConf();
    // tslint:disable:max-line-length
    if (((this.attrEstraiDichConfEdit.codice_fiscale_dichiarante_data_key || '').trim().length > 0)) { locAttrEstraiDichConf.codice_fiscale_dichiarante_data_key = this.attrEstraiDichConfEdit.codice_fiscale_dichiarante_data_key.trim(); }
    if (((this.attrEstraiDichConfEdit.cognome_dichiarante_data_key || '').trim().length > 0)) { locAttrEstraiDichConf.cognome_dichiarante_data_key = this.attrEstraiDichConfEdit.cognome_dichiarante_data_key.trim(); }
    if (((this.attrEstraiDichConfEdit.nome_dichiarante_data_key || '').trim().length > 0)) { locAttrEstraiDichConf.nome_dichiarante_data_key = this.attrEstraiDichConfEdit.nome_dichiarante_data_key.trim(); }
    // tslint:enable:max-line-length
    return JSON.stringify(locAttrEstraiDichConf);
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
