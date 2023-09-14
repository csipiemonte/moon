/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AttrCrmConfR2U } from 'src/app/model/dto/attr/AttrCrmConfR2U';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { ObserveService } from 'src/app/services/observe.service';

@Component({
  selector: 'app-crm-conf-readytouse',
  templateUrl: './crm-conf-readytouse.component.html',
  styleUrls: ['./crm-conf-readytouse.component.scss']
})
export class CrmConfReadytouseComponent implements OnInit {

  @Input() editModeR2U :boolean;
  elencoModuloAttributi: ModuloAttributo[];
  maCRMConf: ModuloAttributo = new ModuloAttributo();

  attrCrmConf: object = new Object();
  attrCrmConfEdit: AttrCrmConfR2U = new AttrCrmConfR2U();

  titleUpd: Map<string, string> = new Map<string, string>();
  r2uSubscription: Subscription;
  constructor(private observeService: ObserveService) { }

  ngOnInit(): void {
    log('ngOnInit() readytouse-component');
    this.r2uSubscription = this.observeService.getDataCrm().subscribe(data => {
      log('ngOnInit() readytouse-component dati arrivati: '+data);
      this.elencoModuloAttributi = data;
      this.initFields();
    });
    log('ngOnInit() readytouse-component editModeR2U='+this.editModeR2U);
  }

  ngOnChanges(val) {
    console.log('crm-conf-readytouse on change ' + val.value);
  }

  initFields() {
    log('readytouse-component initFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maCRMConf = this.elencoModuloAttributi.find(ma => ma.nome === 'PSIT_CRM_CONF_R2U');
      if (this.maCRMConf) {
        this.attrCrmConf = JSON.parse(this.maCRMConf.valore);
        this.attrCrmConfEdit = JSON.parse(this.maCRMConf.valore); // Object.assign({}, this.maCRMConf);
        this.titleUpd.set(this.maCRMConf.nome, 'Ultima modifica il ' + this.maCRMConf.dataUpd + ' da ' + this.maCRMConf.attoreUpd);
      }
      log('readytouse-component initFields() this.attrCrmConf: ' + JSON.stringify(this.attrCrmConf));
      log('readytouse-component initFields() this.attrCrmConfEdit: ' + JSON.stringify(this.attrCrmConfEdit));
    }
    if (!this.maCRMConf) {
      this.maCRMConf = creaModuloAttributo('PSIT_CRM_CONF_R2U', '{}');
    }
  }

  ngOnDestroy(): void {
    this.r2uSubscription.unsubscribe();
    log('destroy crm-conf-readytouse');
  }

  getDataCrmFromChild(): ModuloAttributo{
    return addToSaveAttrConfJsonStr('PSIT_CRM_CONF_R2U', this.attrCrmConf, this.crmConfJsonStr);
  }

  //non cancellare, chiamato dal padre
  validaInput(): string {
    log('validaInput');
    let msg = null;
    //check campi
    /*
    //livello1
    if (!this.attrCrmConfEdit.livello1 || (this.attrCrmConfEdit.livello1 && this.attrCrmConfEdit.livello1.trim().length === 0)) {
      msg = addMsg(msg, 'livello1 campo obbligatorio mancante');
    }
    //livello2
    if (!this.attrCrmConfEdit.livello2 || (this.attrCrmConfEdit.livello2 && this.attrCrmConfEdit.livello2.trim().length === 0)) {
      msg = addMsg(msg, 'livello2 campo obbligatorio mancante');
    }
    //livello3
    if (!this.attrCrmConfEdit.livello3 || (this.attrCrmConfEdit.livello3 && this.attrCrmConfEdit.livello3.trim().length === 0)) {
      msg = addMsg(msg, 'livello3 campo obbligatorio mancante');
    }
    */
    return msg;
  }

  get crmConfJsonStr(): string {
    // Non permettiamo il salvataggio di un attributo di tipo string vuoto nel JSON, lo togliamo completamento
    // tslint:disable:max-line-length
    //this.attrCrmConfEdit.title = ((this.attrCrmConfEdit.title || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.title.trim();
    this.attrCrmConfEdit.riepilogo = ((this.attrCrmConfEdit.riepilogo || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.riepilogo.trim();
    this.attrCrmConfEdit.dettaglio = ((this.attrCrmConfEdit.dettaglio || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.dettaglio.trim();
    this.attrCrmConfEdit.tipologia = ((this.attrCrmConfEdit.tipologia || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.tipologia.trim();
    this.attrCrmConfEdit.urgenza = ((this.attrCrmConfEdit.urgenza || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.urgenza.trim();
    this.attrCrmConfEdit.impatto = ((this.attrCrmConfEdit.impatto || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.impatto.trim();
    this.attrCrmConfEdit.assegnatario = ((this.attrCrmConfEdit.assegnatario || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.assegnatario.trim();

    this.attrCrmConfEdit.emailForFailure = ((this.attrCrmConfEdit.emailForFailure || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.emailForFailure.trim();
    this.attrCrmConfEdit.oggettoEmail = ((this.attrCrmConfEdit.oggettoEmail || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.oggettoEmail.trim();
    this.attrCrmConfEdit.bodyEmail = ((this.attrCrmConfEdit.bodyEmail || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.bodyEmail.trim();
    //
    this.attrCrmConfEdit.nome = ((this.attrCrmConfEdit.nome || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.nome.trim();
    this.attrCrmConfEdit.cognome = ((this.attrCrmConfEdit.cognome || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.cognome.trim();
    this.attrCrmConfEdit.email = ((this.attrCrmConfEdit.email || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.email.trim();
    this.attrCrmConfEdit.phone = ((this.attrCrmConfEdit.phone || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.phone.trim();
    this.attrCrmConfEdit.mobile = ((this.attrCrmConfEdit.mobile || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.mobile.trim();
    this.attrCrmConfEdit.companyName = ((this.attrCrmConfEdit.companyName || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.companyName.trim();
    //this.attrCrmConfEdit.companyId = ((this.attrCrmConfEdit.companyId || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.companyId.trim();
    this.attrCrmConfEdit.cfi = ((this.attrCrmConfEdit.cfi || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.cfi.trim();
    this.attrCrmConfEdit.mexNewUser = ((this.attrCrmConfEdit.mexNewUser || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.mexNewUser.trim();
    // tslint:enable:max-line-length
    //
    this.attrCrmConfEdit.livello1 = ((this.attrCrmConfEdit.livello1 || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.livello1.trim();
    this.attrCrmConfEdit.livello2 = ((this.attrCrmConfEdit.livello2 || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.livello2.trim();
    this.attrCrmConfEdit.livello3 = ((this.attrCrmConfEdit.livello3 || '').trim().length === 0) ? undefined : this.attrCrmConfEdit.livello3.trim();
    return JSON.stringify(this.attrCrmConfEdit);
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
function log(a: any) {
  console.log(a);
}
function addMsg(msg: string, newMsg: string): string {
  return (msg ? msg : '') + '\n<br>' + newMsg;
}