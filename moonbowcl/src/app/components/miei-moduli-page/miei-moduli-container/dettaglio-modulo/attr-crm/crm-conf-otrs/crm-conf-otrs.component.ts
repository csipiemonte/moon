/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit } from '@angular/core';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { AttrCrmConfOtrs } from 'src/app/model/dto/attr/AttrCrmConfOtrs';
import { Subscription } from 'rxjs';
import { ObserveService } from 'src/app/services/observe.service';

@Component({
  selector: 'app-crm-conf-otrs',
  templateUrl: './crm-conf-otrs.component.html',
  styleUrls: ['./crm-conf-otrs.component.scss']
})
export class CrmConfOtrsComponent implements OnInit {

  @Input() editModeOtrs :boolean;
  elencoModuloAttributi: ModuloAttributo[];
  maCRMConf: ModuloAttributo = new ModuloAttributo();

  attrCrmConf: object = new Object();
  attrCrmConfEdit: AttrCrmConfOtrs = new AttrCrmConfOtrs();

  titleUpd: Map<string, string> = new Map<string, string>();
  otrsSubscription: Subscription;

  constructor(private observeService: ObserveService) { }

  ngOnInit(): void {
    log('ngOnInit() otrs-component');
    this.otrsSubscription = this.observeService.getDataCrm().subscribe(data => {
      log('ngOnInit() otrs-component dati arrivati: '+data);
      this.elencoModuloAttributi = data;
      this.initFields();
    });
    log('ngOnInit() otrs-component editModeOtrs='+this.editModeOtrs);
  }

  initFields() {
    log('otrs-component initFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maCRMConf = this.elencoModuloAttributi.find(ma => ma.nome === 'PSIT_CRM_CONF_OTRS');
      if (this.maCRMConf) {
        this.attrCrmConf = JSON.parse(this.maCRMConf.valore);
        this.attrCrmConfEdit = JSON.parse(this.maCRMConf.valore); // Object.assign({}, this.maCRMConf);
        this.initCampiDinamici();
        this.titleUpd.set(this.maCRMConf.nome, 'Ultima modifica il ' + this.maCRMConf.dataUpd + ' da ' + this.maCRMConf.attoreUpd);
      }
      log('otrs-component initFields() this.attrCrmConf: ' + JSON.stringify(this.attrCrmConf));
      log('otrs-component initFields() this.attrCrmConfEdit: ' + JSON.stringify(this.attrCrmConfEdit));
    }
    if (!this.maCRMConf) {
      this.maCRMConf = creaModuloAttributo('PSIT_CRM_CONF_OTRS', '{}');
    }
  }

  ngOnDestroy(): void {
    this.otrsSubscription.unsubscribe();
    log('destroy crm-conf-otrs');
  }

  getDataCrmFromChild(): ModuloAttributo{
    return addToSaveAttrConfJsonStr('PSIT_CRM_CONF_OTRS', this.attrCrmConf, this.crmConfJsonStr);
  }

  //non cancellare, chiamato dal padre
  validaInput(): string {
    log('validaInput');
    let msg = null;
    //check campi
    if (!this.attrCrmConfEdit.queue || (this.attrCrmConfEdit.queue && this.attrCrmConfEdit.queue.trim().length === 0)) {
      msg = addMsg(msg, 'queue campo obbligatorio mancante');
    }
    if (this.attrCrmConfEdit.titolo != this.attrCrmConfEdit.subject ) {
      msg = addMsg(msg, 'Il campo titolo e subject devono essere uguali!');
    }
    if (this.attrCrmConfEdit.customerID != this.attrCrmConfEdit.customerUser ) {
      msg = addMsg(msg, 'Il campo customerID e customerUser devono essere uguali!');
    }
    if (!this.attrCrmConfEdit.from || (this.attrCrmConfEdit.from && this.attrCrmConfEdit.from.trim().length === 0)) {
      msg = addMsg(msg, 'from campo obbligatorio mancante');
    }
    if (!this.attrCrmConfEdit.body || (this.attrCrmConfEdit.body && this.attrCrmConfEdit.body.trim().length === 0)) {
      msg = addMsg(msg, 'body campo obbligatorio mancante');
    }
    return msg;
  }

  get crmConfJsonStr(): string {
    // Non permettiamo il salvataggio di un attributo di tipo string vuoto nel JSON, lo togliamo completamento
    // tslint:disable:max-line-length
    this.attrCrmConfEdit.titolo = ((this.attrCrmConfEdit.titolo || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.titolo.trim();
    this.attrCrmConfEdit.queue = ((this.attrCrmConfEdit.queue || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.queue.trim();
    this.attrCrmConfEdit.type = ((this.attrCrmConfEdit.type || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.type.trim();
    this.attrCrmConfEdit.state = ((this.attrCrmConfEdit.state || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.state.trim();
    this.attrCrmConfEdit.priority = ((this.attrCrmConfEdit.priority || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.priority.trim();
   
    this.attrCrmConfEdit.customerID = ((this.attrCrmConfEdit.customerID || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.customerID.trim();
    this.attrCrmConfEdit.customerUser = ((this.attrCrmConfEdit.customerUser || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.customerUser.trim();
    //
    this.attrCrmConfEdit.communicationChannel = ((this.attrCrmConfEdit.communicationChannel || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.communicationChannel.trim();
    this.attrCrmConfEdit.senderType = ((this.attrCrmConfEdit.senderType || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.senderType.trim();
    this.attrCrmConfEdit.from = ((this.attrCrmConfEdit.from || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.from.trim();
    this.attrCrmConfEdit.subject = ((this.attrCrmConfEdit.subject || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.subject.trim();
    this.attrCrmConfEdit.body = ((this.attrCrmConfEdit.body || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.body.trim();
    this.attrCrmConfEdit.contentType = ((this.attrCrmConfEdit.contentType || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.contentType.trim();
    
    //non salvo campi dinamici vuoti
    var tmp:any[] = [];
    for(var i = 0; i < this.attrCrmConfEdit.campiDinamici.length; i++){
      if(this.attrCrmConfEdit.campiDinamici[i].chiave.length != 0){
        tmp.push(this.attrCrmConfEdit.campiDinamici[i])
     }
    }
    if(tmp.length >0) this.attrCrmConfEdit.campiDinamici = tmp;

    return JSON.stringify(this.attrCrmConfEdit);
  }

  initCampiDinamici(){
    if(this.attrCrmConf['campiDinamici'] == null){
      this.attrCrmConfEdit.campiDinamici = [{chiave: '', valore: ''}];
    }
  }

  addCampo() {
    this.attrCrmConfEdit.campiDinamici.push({
      chiave: '',
      valore: ''
    });
  }

  removeCampo(i: number) {
    this.attrCrmConfEdit.campiDinamici.splice(i, 1);
  }


}

function creaModuloAttributo(nome: string, valore: string): ModuloAttributo {
  const obj = new ModuloAttributo();
  obj.nome = nome;
  obj.valore = valore;
  return obj;
}
function addToSaveAttrConfJsonStr( nomeAttributoJson: string, attrConf: Object, confJsonStrEdit: string)
  : ModuloAttributo {
   var result: ModuloAttributo = null;
  if (attrConf && JSON.stringify(attrConf) === JSON.stringify(JSON.parse(confJsonStrEdit))) {
    log('Unchanged ' + nomeAttributoJson + ': ' + JSON.stringify(attrConf));
  } else {
    log('Changed ' + nomeAttributoJson + ' \nFROM: ' + JSON.stringify(attrConf) + ' \nTO: ' + confJsonStrEdit);
    //result.push(creaModuloAttributo(nomeAttributoJson, confJsonStrEdit));
    result = creaModuloAttributo(nomeAttributoJson, confJsonStrEdit);
  }
  return result;
}
function addMsg(msg: string, newMsg: string): string {
  return (msg ? msg : '') + '\n<br>' + newMsg;
}
function log(a: any) {
  console.log(a);
}