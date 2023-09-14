/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { Modulo } from 'src/app/model/dto/modulo';
import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { faClone, faEdit, faHome, faInfo, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { AttrEmailConf } from 'src/app/model/dto/attr/attrEmailConf';
// import { Attributi } from 'src/app/model/dto/attr/attributi';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { MsgAttrEmail } from 'src/app/common/messaggi';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-email',
  templateUrl: './attr-email.component.html',
  styleUrls: ['./attr-email.component.scss']
})
export class AttrEmailComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  elencoModuloAttributi: ModuloAttributo[];
  maEmail: ModuloAttributo = new ModuloAttributo();
  maEmailEdit: ModuloAttributo = new ModuloAttributo();
  maEmailConf: ModuloAttributo = new ModuloAttributo();

  attrEmailConf: object = new Object();
  attrEmailConfEdit: AttrEmailConf = new AttrEmailConf();

  titleUpd: Map<string, string> = new Map<string, string>();

  isAdmin = false;
  isOpAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  faHome = faHome;
  faClone = faClone;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  modeError = false;
  showCc = false;
  showBcc = false;

  maEmailEditT = false;

  alertOptions = {
    id: 'alert-moduli-attr-email',
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
                                            'attributiEmail').subscribe(
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
    this.maEmail = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_EMAIL', 'N');
    this.maEmailEdit = { ...this.maEmail };
    this.maEmailEditT = this.maEmailEdit ? (this.maEmailEdit['valore'] === 'N' ? false : true) : false;
    log('initFields() this.maEmail: ' + JSON.stringify(this.maEmail));
    log('initFields() this.maEmailEdit: ' + JSON.stringify(this.maEmailEdit));
    //
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maEmailConf = this.elencoModuloAttributi.find(ma => ma.nome === 'PSIT_EMAIL_CONF');
      if (this.maEmailConf) {
        this.attrEmailConf = JSON.parse(this.maEmailConf.valore);
        this.attrEmailConfEdit = JSON.parse(this.maEmailConf.valore); // Object.assign({}, this.maEmailConf);
        this.showCc = (this.attrEmailConfEdit.cc || this.attrEmailConfEdit.cc_istanza_data_key) ? true : false;
        this.showBcc = (this.attrEmailConfEdit.bcc || this.attrEmailConfEdit.bcc_istanza_data_key) ? true : false;
        this.titleUpd.set(this.maEmailConf.nome, 'Ultima modifica il ' + this.maEmailConf.dataUpd + ' da ' + this.maEmailConf.attoreUpd);
      }
      log('initFields() this.attrEmailConf: ' + JSON.stringify(this.attrEmailConf));
      log('initFields() this.attrEmailConfEdit: ' + JSON.stringify(this.attrEmailConfEdit));
    }
    if (!this.maEmailConf) {
      this.maEmailConf = creaModuloAttributo('PSIT_EMAIL_CONF', '{}');
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
      this.titleUpd.set(result.nome, 'Ultima modifica il ' + result.dataUpd + ' da ' + result.attoreUpd);
    }
    return result;
  }

  salva() {
    this.spinnerService.show();
    // tslint:disable-next-line:prefer-const
    let maToSave = [];
    if (this.maEmailEdit) {
      this.maEmailEdit['valore'] = this.maEmailEditT ? 'S' : 'N';
    }
    verificaToSave(maToSave, this.maEmail, this.maEmailEdit);
    addToSaveAttrConfJsonStr(maToSave, 'PSIT_EMAIL_CONF', this.attrEmailConf, this.emailConfJsonStr);
    log('salva::maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0) {
      this.moonboblService.aggiornaModuloAttributiEmail(this.modulo, maToSave).subscribe(
        (attributi: ModuloAttributo[]) => {
          this.elencoModuloAttributi = attributi;
          this.init();
          this.spinnerService.hide();
          // this.alertService.success('Salvataggio attributi email effettuato con successo !', this.alertOptions);
          this.alert.emit({ text: MsgAttrEmail.SUCCESS_ATTR_EMAIL, type: 'success', autoclose:true});
        },
        (err: MoonboError) => {
          this.spinnerService.hide();
          // this.alertService.error('Impossibile effettuare il salvataggio attributi email !' + err.errorMsg, this.alertOptions);
          this.alert.emit({ text: MsgAttrEmail.ERROR_ATTR_EMAIL, type: 'error', autoclose:false});
        }
      );
    } else {
      this.spinnerService.hide();
      // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
      this.alert.emit({ text: MsgAttrEmail.INFO_NO_MODIFY_ATTR_EMAIL, type: 'info', autoclose:false});

    }
  }

  get emailConfJsonStr(): string {
    // Non permettiamo il salvataggio di un attributo di tipo string vuoto nel JSON, lo togliamo completamento
    // tslint:disable:max-line-length
    this.attrEmailConfEdit.to = ((this.attrEmailConfEdit.to || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.to.trim();
    this.attrEmailConfEdit.to_istanza_data_key = ((this.attrEmailConfEdit.to_istanza_data_key || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.to_istanza_data_key.trim();
    this.attrEmailConfEdit.cc = ((this.attrEmailConfEdit.cc || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.cc.trim();
    this.attrEmailConfEdit.cc_istanza_data_key = ((this.attrEmailConfEdit.cc_istanza_data_key || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.cc_istanza_data_key.trim();
    this.attrEmailConfEdit.bcc = ((this.attrEmailConfEdit.bcc || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.bcc.trim();
    this.attrEmailConfEdit.bcc_istanza_data_key = ((this.attrEmailConfEdit.bcc_istanza_data_key || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.bcc_istanza_data_key.trim();
    this.attrEmailConfEdit.subject = ((this.attrEmailConfEdit.subject || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.subject.trim();
    this.attrEmailConfEdit.text = ((this.attrEmailConfEdit.text || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.text.trim();
    this.attrEmailConfEdit.html = ((this.attrEmailConfEdit.html || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.html.trim();
    this.attrEmailConfEdit.protocollo_to = ((this.attrEmailConfEdit.protocollo_to || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.protocollo_to.trim();
    this.attrEmailConfEdit.subject_rinvio = ((this.attrEmailConfEdit.subject_rinvio || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.subject_rinvio.trim();
    this.attrEmailConfEdit.text_rinvio = ((this.attrEmailConfEdit.text_rinvio || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.text_rinvio.trim();
    this.attrEmailConfEdit.html_rinvio = ((this.attrEmailConfEdit.html_rinvio || '').trim().length === 0) ? undefined : this.attrEmailConfEdit.html_rinvio.trim();
    // tslint:enable:max-line-length
    return JSON.stringify(this.attrEmailConfEdit);
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
