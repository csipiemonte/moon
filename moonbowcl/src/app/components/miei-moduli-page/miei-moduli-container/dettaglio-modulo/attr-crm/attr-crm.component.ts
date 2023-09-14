/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { Modulo } from 'src/app/model/dto/modulo';
import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { AttrCrmConfNextCrm } from 'src/app/model/dto/attr/AttrCrmConfNextCrm';
import { MsgAttrCrm } from 'src/app/common/messaggi';
import { ObserveService } from 'src/app/services/observe.service';
import { CrmConfNextcrmComponent } from './crm-conf-nextcrm/crm-conf-nextcrm.component';
import { CrmConfReadytouseComponent } from './crm-conf-readytouse/crm-conf-readytouse.component';
import { LIST_CRM_SYSTEM } from 'src/app/common/costanti';
import { CrmConfOtrsComponent } from './crm-conf-otrs/crm-conf-otrs.component';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-crm',
  templateUrl: './attr-crm.component.html',
  styleUrls: ['./attr-crm.component.scss']
})
export class AttrCrmComponent implements OnInit {

  @ViewChild(CrmConfNextcrmComponent) childNextCrm:CrmConfNextcrmComponent;
  @ViewChild(CrmConfReadytouseComponent) childR2u:CrmConfReadytouseComponent;
  @ViewChild(CrmConfOtrsComponent) childOtrs:CrmConfOtrsComponent;
  child : any;

  @Input() moduloSelezionato: Modulo;
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  elencoModuloAttributi: ModuloAttributo[];
  maCRM: ModuloAttributo = new ModuloAttributo();
  maCRMEdit: ModuloAttributo = new ModuloAttributo();
  maCRMsystem: ModuloAttributo = new ModuloAttributo();
  maCRMsystemEdit: ModuloAttributo = new ModuloAttributo();
  maCRMConf: ModuloAttributo = new ModuloAttributo();

  listCrmSystem;

  attrCrmConf: object = new Object();
  attrCrmConfEdit: AttrCrmConfNextCrm = new AttrCrmConfNextCrm();

  titleUpd: Map<string, string> = new Map<string, string>();

  isAdmin = false;
  isOpAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  modeError = false;

  maCRMEditT = false;

  alertId = 'alert-moduli-attr-crm';
  alertOptions = {
    id: this.alertId,
    autoClose: true,
    keepAfterRouteChange: false
  };
  alertOptionsNoAutoClose = {
    id: this.alertId,
    autoClose: false,
    keepAfterRouteChange: false
  };

  constructor(fb: FormBuilder,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService,
    private observeService: ObserveService,
    protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpAdmin = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
  }

  ngOnInit(): void {
    this.listCrmSystem = this.initListCrmSystem();
    this.init();
  }

  init(): void {

    this.alert.emit({clear: true});

    this.spinnerService.show();
    this.moonboblService.getModuloWithFields(this.moduloSelezionato.idModulo,
                                            this.moduloSelezionato.idVersioneModulo,
                                            'attributiCrm').subscribe(
      (modulo: Modulo) => {
        // Init degli attributi per viewMode
        this.modulo = modulo;
        this.elencoModuloAttributi = modulo.attributi;
        //send data crm al figlio
        log('send data crm: '+this.elencoModuloAttributi);
        this.observeService.sendDataCrm(this.elencoModuloAttributi);

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
    log('attr-crm-component initFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
    this.maCRM = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_CRM', 'N');
    this.maCRMEdit = { ...this.maCRM };
    this.maCRMEditT = this.maCRMEdit ? (this.maCRMEdit['valore'] === 'N' ? false : true) : false;
    log('attr-crm-component initFields() this.maCRM: ' + JSON.stringify(this.maCRM));
    log('attr-crm-component initFields() this.maCRMEdit: ' + JSON.stringify(this.maCRMEdit));
    //
    this.maCRMsystem = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_CRM_SYSTEM', 'NEXTCRM');
    this.maCRMsystemEdit = { ...this.maCRMsystem };
    log('attr-crm-component initFields() this.maCRMsystem: ' + JSON.stringify(this.maCRMsystem));
    log('attr-crm-component initFields() this.maCRMsystemEdit: ' + JSON.stringify(this.maCRMsystemEdit));
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
    this.alertService.clear(this.alertId);
    // tslint:disable-next-line:prefer-const
    let maToSave = [];
    // setto il figlio in base al system
    if(this.maCRMsystemEdit['valore'] =='OTRS'){
      this.child as CrmConfOtrsComponent;
      this.child = this.childOtrs;
    } else if(this.maCRMsystemEdit['valore'] =='R2U'){
      this.child as CrmConfReadytouseComponent ;
      this.child = this.childR2u;
    } else{
      this.child as CrmConfNextcrmComponent ;
      this.child = this.childNextCrm;
    }
    /* valida i campi solo se e' ATTIVO, quindi se spento mi fa il salvataggio
    if (this.maCRMEdit) {
      if (this.maCRMEditT) {
        //chiedo al figlio di validare i campi
        const msgErroriValidazione = this.child.validaInput();
        if (msgErroriValidazione) {
          this.spinnerService.hide();
          this.alert.emit({ text: MsgAttrCrm.ERROR_CONF_ATTR_CRM, type: 'error', autoclose:false});
          //this.alert.emit({ text: msgErroriValidazione, type: 'error', autoclose:false});
          return;
        }
      }
      this.maCRMEdit['valore'] = this.maCRMEditT ? 'S' : 'N';
    }
    */
   //adesso valido i campi a priori se attivo o no
   const msgErroriValidazione = this.child.validaInput();
        if (msgErroriValidazione != null) {
          this.spinnerService.hide();
          //this.alert.emit({ text: MsgAttrCrm.ERROR_CONF_ATTR_CRM, type: 'error', autoclose:false});
          this.alert.emit({ text: msgErroriValidazione, type: 'error', autoclose:false});
          return;
        }
    if (this.maCRMEdit) {    
      this.maCRMEdit['valore'] = this.maCRMEditT ? 'S' : 'N';
    }
    verificaToSave(maToSave, this.maCRM, this.maCRMEdit);
    //
    verificaToSave(maToSave, this.maCRMsystem, this.maCRMsystemEdit);
    //
    //addToSaveAttrConfJsonStr(maToSave, 'PSIT_CRM_CONF_NEXTCRM', this.attrCrmConf, this.crmConfJsonStr);
    const data = this.child.getDataCrmFromChild();
    if(data !=null){
      maToSave.push(data);
    }
    //
    log('salva::aggiornaModuloAttributiCrm maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0) {
      this.moonboblService.aggiornaModuloAttributiCrm(this.modulo, maToSave).subscribe(
        (attributi: ModuloAttributo[]) => {
          this.elencoModuloAttributi = attributi;
          this.init();
          this.spinnerService.hide();
          // this.alertService.success('Salvataggio attributi CRM effettuato con successo !', this.alertOptions);
          this.alert.emit({ text: MsgAttrCrm.SUCCESS_ATTR_CRM, type: 'success', autoclose:true});
        },
        (err: MoonboError) => {
          this.spinnerService.hide();
          // this.alertService.error('Impossibile effettuare il salvataggio attributi next CRM !' + err.errorMsg, this.alertOptions);
          this.alert.emit({ text: MsgAttrCrm.ERROR_ATTR_CRM, type: 'error', autoclose:false});
        }
      );
    } else {
      this.spinnerService.hide();
      // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
      this.alert.emit({ text: MsgAttrCrm.INFO_NO_MODIFY_ATTR_CRM, type: 'info', autoclose:false});
    }
  }

  ngOnDestroy(): void {
    log('destroy attr-crm');
  }

  initListCrmSystem() : any {
    return  LIST_CRM_SYSTEM;
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

function creaModuloAttributo(nome: string, valore: string): ModuloAttributo {
  const obj = new ModuloAttributo();
  obj.nome = nome;
  obj.valore = valore;
  return obj;
}

function log(a: any) {
  // console.log(a);
}


