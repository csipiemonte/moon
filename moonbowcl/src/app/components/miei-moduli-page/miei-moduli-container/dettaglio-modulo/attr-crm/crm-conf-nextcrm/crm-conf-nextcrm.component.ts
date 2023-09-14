/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import { AttrCrmConfNextCrm } from 'src/app/model/dto/attr/AttrCrmConfNextCrm';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { ObserveService } from 'src/app/services/observe.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-crm-conf-nextcrm',
  templateUrl: './crm-conf-nextcrm.component.html',
  styleUrls: ['./crm-conf-nextcrm.component.scss']
})
export class CrmConfNextcrmComponent implements OnInit, OnDestroy {

  @Input() editModeNextCrm :boolean;
  elencoModuloAttributi: ModuloAttributo[];
  maCRMConf: ModuloAttributo = new ModuloAttributo();

  attrCrmConf: object = new Object();
  attrCrmConfEdit: AttrCrmConfNextCrm = new AttrCrmConfNextCrm();

  titleUpd: Map<string, string> = new Map<string, string>();
  nextcrmSubscription: Subscription;
  constructor(private observeService: ObserveService) { }

  ngOnInit(): void {
    log('ngOnInit() nextcrm-component');
   this.nextcrmSubscription =  this.observeService.getDataCrm().subscribe(data => {
      log('ngOnInit() nextcrm-component dati arrivati: '+data);
      this.elencoModuloAttributi = data;
      this.initFields();
    });
    log('ngOnInit() nextcrm-component editModeNextCrm='+this.editModeNextCrm);
  }

  ngOnChanges(val) {
    console.log('crm-conf-nextcrm on change ' + val.value);
  }

  initFields() {
    log('nextcrm-component initFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
  
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maCRMConf = this.elencoModuloAttributi.find(ma => ma.nome === 'PSIT_CRM_CONF_NEXTCRM');
      if (this.maCRMConf) {
        this.attrCrmConf = JSON.parse(this.maCRMConf.valore);
        this.attrCrmConfEdit = JSON.parse(this.maCRMConf.valore); // Object.assign({}, this.maCRMConf);
        this.titleUpd.set(this.maCRMConf.nome, 'Ultima modifica il ' + this.maCRMConf.dataUpd + ' da ' + this.maCRMConf.attoreUpd);
      }
      log('nextcrm-component initFields() this.attrCrmConf: ' + JSON.stringify(this.attrCrmConf));
      log('nextcrm-component initFields() this.attrCrmConfEdit: ' + JSON.stringify(this.attrCrmConfEdit));
    }
    if (!this.maCRMConf) {
      this.maCRMConf = creaModuloAttributo('PSIT_CRM_CONF_NEXTCRM', '{}');
    }
  }

  getDataCrmFromChild(): ModuloAttributo{
    return addToSaveAttrConfJsonStr( 'PSIT_CRM_CONF_NEXTCRM', this.attrCrmConf, this.crmConfJsonStr);
  }

  //non cancellare, chiamato dal padre
  validaInput(): string {
    log('validaInput');
    let msg = null;
    // group_id
    if (!this.attrCrmConfEdit.group_id || (this.attrCrmConfEdit.group_id && this.attrCrmConfEdit.group_id.trim().length === 0)) {
        msg = addMsg(msg, 'group_id campo obbligatorio mancante');
    } else {
        if (!(isNumeric(this.attrCrmConfEdit.group_id.trim()) || isRefField(this.attrCrmConfEdit.group_id.trim()))) {
          msg = addMsg(msg, 'group_id campo non numerico o rifField');
        }
    }
    
    // asset_id
    if (!this.attrCrmConfEdit.asset_id || (this.attrCrmConfEdit.asset_id && this.attrCrmConfEdit.asset_id.trim().length === 0)) {
        msg = addMsg(msg, 'asset_id campo obbligatorio mancante');
    } else {
      if (!(isNumeric(this.attrCrmConfEdit.asset_id.trim()) || !isRefField(this.attrCrmConfEdit.asset_id.trim()))) {
        msg = addMsg(msg, 'asset_id campo non numerico o rifField');
      }
    }
    return msg;
  }
  ngOnDestroy(): void {
    this.nextcrmSubscription.unsubscribe();
    log('destroy crm-conf-nextcrm');
  }

  get crmConfJsonStr(): string {
    // Non permettiamo il salvataggio di un attributo di tipo string vuoto nel JSON, lo togliamo completamento
    // tslint:disable:max-line-length
    this.attrCrmConfEdit.title = ((this.attrCrmConfEdit.title || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.title.trim();
    this.attrCrmConfEdit.group_id = ((this.attrCrmConfEdit.group_id || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.group_id.trim();
//    this.attrCrmConfEdit.priority_id = ((this.attrCrmConfEdit.priority_id || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.priority_id.trim();
    this.attrCrmConfEdit.note = ((this.attrCrmConfEdit.note || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.note.trim();
//    this.attrCrmConfEdit.type_id = ((this.attrCrmConfEdit.type_id || '').trim().length === 0) ? undefined : this.type_id.bcc.trim();
    this.attrCrmConfEdit.asset_id = ((this.attrCrmConfEdit.asset_id || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.asset_id.trim();
    this.attrCrmConfEdit.additional_info = ((this.attrCrmConfEdit.additional_info || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.additional_info.trim();
    this.attrCrmConfEdit.article_istanza_subject = ((this.attrCrmConfEdit.article_istanza_subject || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.article_istanza_subject.trim();
    this.attrCrmConfEdit.article_istanza_body = ((this.attrCrmConfEdit.article_istanza_body || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.article_istanza_body.trim();
//    this.attrCrmConfEdit.include_allegati = ((this.attrCrmConfEdit.include_allegati || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.include_allegati.trim();
    this.attrCrmConfEdit.article_allegato_subject = ((this.attrCrmConfEdit.article_allegato_subject || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.article_allegato_subject.trim();
    this.attrCrmConfEdit.codiceFiscale = ((this.attrCrmConfEdit.codiceFiscale || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.codiceFiscale.trim();
    this.attrCrmConfEdit.firstname = ((this.attrCrmConfEdit.firstname || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.firstname.trim();
    this.attrCrmConfEdit.lastname = ((this.attrCrmConfEdit.lastname || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.lastname.trim();
    this.attrCrmConfEdit.email = ((this.attrCrmConfEdit.email || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.email.trim();
    this.attrCrmConfEdit.phone = ((this.attrCrmConfEdit.phone || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.phone.trim();
    this.attrCrmConfEdit.mobile = ((this.attrCrmConfEdit.mobile || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.mobile.trim();
    // tslint:enable:max-line-length
    return JSON.stringify(this.attrCrmConfEdit);
  }


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

function addMsg(msg: string, newMsg: string): string {
  return (msg ? msg : '') + '\n<br>' + newMsg;
}
function isNumeric(num) {
  const value1 = num.toString();
  const value2 = parseFloat(num).toString();
  return (value1 === value2);
}
function isRefField(refField) {
  return (refField && refField.length > 4 &&
    refField.substring(0, 2) === '##' &&
    refField.slice(-2) === '##');
}
function addToSaveAttrConfJsonStr( nomeAttributoJson: string, attrConf: Object, confJsonStrEdit: string)
  : ModuloAttributo {
    var result: ModuloAttributo = null;
  if (attrConf && JSON.stringify(attrConf) === JSON.stringify(JSON.parse(confJsonStrEdit))) {
    log('Unchanged ' + nomeAttributoJson + ': ' + JSON.stringify(attrConf));
  } else {
    log('Changed ' + nomeAttributoJson + ' \nFROM: ' + JSON.stringify(attrConf) + ' \nTO: ' + confJsonStrEdit);
    result = creaModuloAttributo(nomeAttributoJson, confJsonStrEdit);
  }
  return result;
}
