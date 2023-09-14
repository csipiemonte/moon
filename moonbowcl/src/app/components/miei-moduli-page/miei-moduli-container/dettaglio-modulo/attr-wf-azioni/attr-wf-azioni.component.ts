/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Workflow } from './../../../../../model/dto/workflow';
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
import { AlertService } from 'src/app/modules/alert/alert.service';
import { MsgAttrAzioni } from 'src/app/common/messaggi';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-wf-azioni',
  templateUrl: './attr-wf-azioni.component.html',
  styleUrls: ['./attr-wf-azioni.component.scss']
})
export class AttrWfAzioniComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  elencoModuloAttributi: ModuloAttributo[];
  maAzione: ModuloAttributo = new ModuloAttributo();
  maAzioneEdit: ModuloAttributo = new ModuloAttributo();

  workflow: Workflow;
  hasWorkflowDaEseguireDopoInviata = false;
  titleUpd: Map<string, string> = new Map<string, string>();

  isAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  faHome = faHome;
  faClone = faClone;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  modeError = false;

  maAzioneEditT = false;

  alertOptions = {
    id: 'alert-moduli-attr-wf-azioni',
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
                                            'attributiAzione').subscribe({
      'next': (modulo: Modulo) => {
        try {
          // Init degli attributi per viewMode
          this.modulo = modulo;
          this.elencoModuloAttributi = modulo.attributi;
          this.initFields();
          // tslint:disable-next-line:max-line-length
          if (this.isAdmin || ((this.modulo.stato.codice === 'INIT' || this.modulo.stato.codice === 'TST' || this.modulo.stato.codice === 'MOD') && this.isOwner)) {
            this.isModificabile = true;
          }

          this.spinnerService.hide();
        } catch (error) {
          this.spinnerService.hide();
        }
      },
      'error': (err: MoonboError) => {
          // informazioni sulla chiamata
          // this.errNotificationError.notification.next(err);
          // alert(err.errorMsg);
          this.alert.emit({ text: err.errorMsg, type: 'error', autoclose:false});
          this.modeError = true;
          this.spinnerService.hide();
      }
    });
    this.initWorkflowDaEseguireDopoInviata();
  }

  initWorkflowDaEseguireDopoInviata() {
    this.moonboblService.getAzioneWorkflowDaEseguireDopoInviata(this.moduloSelezionato.idModulo).then(
      (workflow) => {
        log('attr-wf-azioni::initWorkflowDaEseguireDopoInviata() AzioneWorkflowDaEseguireDopoInviata: ' + JSON.stringify(workflow));
        this.workflow = workflow;
        this.hasWorkflowDaEseguireDopoInviata = true;
        log('attr-wf-azioni::initWorkflowDaEseguireDopoInviata() workflow: ' + JSON.stringify(this.workflow));
        log('attr-wf-azioni::initWorkflowDaEseguireDopoInviata() hasWorkflowDaEseguireDopoInviata: ' + this.hasWorkflowDaEseguireDopoInviata);
      }
    );
  }

  initFields() {
    log('attr-wf-azioni::initFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
    this.maAzione = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_AZIONE', 'N');
    this.maAzioneEdit = { ...this.maAzione };
    this.maAzioneEditT = this.maAzioneEdit ? (this.maAzioneEdit['valore'] === 'N' ? false: true) : false;
    log('attr-wf-azioni::initFields() this.maAzione: ' + JSON.stringify(this.maAzione));
    log('attr-wf-azioni::initFields() this.maAzioneEdit: ' + JSON.stringify(this.maAzioneEdit));
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
    if (this.maAzioneEdit)  this.maAzioneEdit['valore'] = this.maAzioneEditT ? 'S':'N'; 
    verificaToSave(maToSave, this.maAzione, this.maAzioneEdit);
    log('attr-wf-azioni::salva() Save maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0) {
      this.moonboblService.aggiornaModuloAttributiAzione(this.modulo, maToSave).subscribe(
        (attributi: ModuloAttributo[]) => {
          this.elencoModuloAttributi = attributi;
          this.init();
          this.spinnerService.hide();
          // this.alertService.success('Salvataggio attributi WF azioni effettuato con successo !', this.alertOptions);

          this.alert.emit({ text: MsgAttrAzioni.SUCCESS_ATTR_AZIONI, type: 'success', autoclose:true});
        },
        (err: MoonboError) => {
          this.spinnerService.hide();
          // this.alertService.error('Impossibile effettuare il salvataggio attributi WF azioni !' + err.errorMsg, this.alertOptions);
          this.alert.emit({ text: MsgAttrAzioni.ERROR_ATTR_AZIONI, type: 'error', autoclose:false});
        }
      );
    } else {
      this.spinnerService.hide();
      // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
      this.alert.emit({ text: MsgAttrAzioni.INFO_NO_MODIFY_ATTR_AZIONI, type: 'info', autoclose:false});
    }
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

