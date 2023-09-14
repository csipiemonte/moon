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
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { AttrNotifyConf } from 'src/app/model/dto/attr/attrNotifyConf';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { MsgAttrNotificatore } from 'src/app/common/messaggi';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-notificatore',
  templateUrl: './attr-notificatore.component.html',
  styleUrls: ['./attr-notificatore.component.scss']
})
export class AttrNotificatoreComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  elencoModuloAttributi: ModuloAttributo[];
  maNotify: ModuloAttributo = new ModuloAttributo();
  maNotifyEdit: ModuloAttributo = new ModuloAttributo();
  maNotifyConf: ModuloAttributo = new ModuloAttributo();

  attrNotifyConf: object = new Object();
  attrNotifyConfEdit: AttrNotifyConf = new AttrNotifyConf();

  titleUpd: Map<string, string> = new Map<string, string>();

  listErroriPossibili = ['MOONSRV-30414','MOONSRV-30415','MOONSRV-30416','MOONSRV-30417'];
  mappaErroriView: Map<string, string> = new Map<string, string>();
  mappaErroriEdit: Map<string, string> = new Map<string, string>([
    ["MOONSRV-30414",""],["MOONSRV-30415",""],["MOONSRV-30416",""],["MOONSRV-30417",""]
  ]);

  isAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  faHome = faHome;
  faClone = faClone;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  modeError = false;

  maNotifyEditT = false;

  alertOptions = {
    id: 'alert-moduli-attr-notificatore',
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
                                            'attributiNotify').subscribe((modulo: Modulo) => {
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
    this.maNotify = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_NOTIFY', 'N');
    this.maNotifyEdit = { ...this.maNotify };
    this.maNotifyEditT = this.maNotifyEdit ? (this.maNotifyEdit['valore'] === 'N' ? false: true) : false;

    log('initFields() this.maNotify: ' + JSON.stringify(this.maNotify));
    log('initFields() this.maNotifyEdit: ' + JSON.stringify(this.maNotifyEdit));
    //
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maNotifyConf = this.elencoModuloAttributi.find(ma => ma.nome === 'PSIT_NOTIFY_CONF');
      log('initFields() this.maNotifyConf: ' + JSON.stringify(this.maNotifyConf));
      if (this.maNotifyConf) {
        this.attrNotifyConf = JSON.parse(this.maNotifyConf.valore);
        this.attrNotifyConfEdit = JSON.parse(this.maNotifyConf.valore); // Object.assign({}, this.maNotifyConf);
        this.titleUpd.set(this.maNotifyConf.nome, 'Ultima modifica il ' + this.maNotifyConf.dataUpd + ' da ' + this.maNotifyConf.attoreUpd);
        this.initMappaErrori();
      }
      log('initFields() this.attrNotifyConf: ' + JSON.stringify(this.attrNotifyConf));
      log('initFields() this.attrNotifyConfEdit: ' + JSON.stringify(this.attrNotifyConfEdit));
    } else{
      this.inizializeAttrNotifyConfEdit();
    }
    if (!this.maNotifyConf) {
      this.maNotifyConf = creaModuloAttributo('PSIT_NOTIFY_CONF', '{}');
    }
  }

  inizializeAttrNotifyConfEdit(){
    this.attrNotifyConfEdit.email={
      subject: "",
      body: "",
      template_id: "",
      prefRequired: false,
      send: false
    };
    this.attrNotifyConfEdit.sms={
      content: "",
      prefRequired: false,
      send: false
    };
    this.attrNotifyConfEdit.errori={ "MOONSRV-30414":"","MOONSRV-30415":"","MOONSRV-30416":"","MOONSRV-30417":"" };
  }
  initMappaErrori() {
    if(this.attrNotifyConf['errori']){
      for(let e of this.listErroriPossibili){
        if(this.attrNotifyConf['errori'][e]){
          this.mappaErroriView.set(e,this.attrNotifyConf['errori'][e]);
          this.mappaErroriEdit.set(e,this.attrNotifyConf['errori'][e]);
        }
      }
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

    //if (this.maNotifyEdit)  this.maNotifyEdit['valore'] = this.maNotifyEditT ? 'S':'N'; 

    verificaToSave(maToSave, this.maNotify, this.maNotifyEdit);
    addToSaveAttrConfJsonStr(maToSave, 'PSIT_NOTIFY_CONF', this.attrNotifyConf, this.notifyConfJsonStr);
    log('salva::aggiornaModuloAttributiNotificatore maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0) {
      this.moonboblService.aggiornaModuloAttributiNotificatore(this.modulo, maToSave).subscribe(
          (attributi: ModuloAttributo[]) => {
            this.elencoModuloAttributi = attributi;
            this.init();
            this.spinnerService.hide();
            // this.alertService.success('Salvataggio attributi notificatore effettuato con successo !', this.alertOptions);
            this.alert.emit({ text: MsgAttrNotificatore.SUCCESS_ATTR_NOTIFICATORE, type: 'success', autoclose:true});
            },
          (err: MoonboError) => {
            this.spinnerService.hide();
            // this.alertService.error('Impossibile effettuare il salvataggio attributi notificatore !' + err.errorMsg, this.alertOptions);
            this.alert.emit({ text: MsgAttrNotificatore.ERROR_ATTR_NOTIFICATORE, type: 'error', autoclose:false});
          }
        );
    } else {
      this.spinnerService.hide();
      // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
      this.alert.emit({ text: MsgAttrNotificatore.INFO_NO_MODIFY_ATTR_NOTIFICATORE, type: 'info', autoclose:false});
    }
  }

  get notifyConfJsonStr(): string {
    // Non permettiamo il salvataggio di un attributo di tipo strong vuoto nel JSON, lo togliamo completamento
    this.attrNotifyConfEdit.serviceName = ((this.attrNotifyConfEdit.serviceName || '').trim().length === 0) ? undefined : this.attrNotifyConfEdit.serviceName.trim();
    //this.attrNotifyConfEdit.serviceToken = ((this.attrNotifyConfEdit.serviceToken || '').trim().length === 0) ? undefined : this.attrNotifyConfEdit.serviceToken.trim();

    for(let key of this.mappaErroriEdit.keys()){
       let v : any =  document.getElementById(key);
       if(v.value != this.mappaErroriView.get(key)){
        this.attrNotifyConfEdit.errori[key] = v.value;
        //devo aggiornare la view
        this.mappaErroriView.set(key,v.value);
      }
    }
   
    return JSON.stringify(this.attrNotifyConfEdit);
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
   console.log(a);
}
