/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ProcessiView, ProcessiViewEvent} from '../../../processi.model';
import {Processo} from 'src/app/model/dto/processo';
import {Alert, AlertService} from 'src/app/modules/alert';

@Component({
  selector: 'app-processi-container',
  templateUrl: './processi-container.component.html',
  styleUrls: ['./processi-container.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ProcessiContainerComponent implements OnInit {

  currentView: ProcessiView;
  currentMsg: string;
  processoSelezionato: Processo;
  idWorkflowSelezionato: number;

  alertOptions = {
    id: 'alert-processi-container',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(protected alertService: AlertService) {
    log('[app-processi-container::constructor]');
  }

  ngOnInit(): void {
    log('processi-container::ngOnInit() ...');
    this.currentView = ProcessiView.ELENCO;
  }

  // dettaglio(processo: Processo) {
  //   log('processi-container::dettaglio() ...');
  //   log('processi-container::dettaglio() ... processo = ' + JSON.stringify(processo));
  //   this.processoSelezionato = processo;
  //   this.currentView = ProcessiView.DETTAGLIO;
  // }

  // eliminaProcesso(processo: Processo) {
  //   log('processi-container::eliminaProcesso() ...');
  //   log('processi-container::eliminaProcesso() ... processo = ' + JSON.stringify(processo));
  // }


  setView(e: ProcessiViewEvent) {
    log('processi-container::setView() ...');
    log('processi-container::setView() ... e = ' + JSON.stringify(e));

    if (e) {
      if (e.view === ProcessiView.DETTAGLIO) {
        if (e.processo) {
          log('processi-container::setView() e.processo = ' + e.processo);
          this.processoSelezionato = e.processo;
        } else {
          log('processi-container::setView() ERROR richiesto ProcessiView DETTAGLIO senza idProcesso ! set default ELENCO');
          e.view = ProcessiView.ELENCO;
        }
      }
      if (e.view === ProcessiView.DETTAGLIO_WF) {
        if (e.processo && e.idWorkflow) {
          log('processi-container::setView() e.processo = ' + e.processo + '  e.idWorkflow = ' + e.idWorkflow);
          this.processoSelezionato = e.processo;
          this.idWorkflowSelezionato = e.idWorkflow;
        } else {
          log('processi-container::setView() ERROR richiesto ProcessiView DETTAGLIO_WF senza idProcesso & idWorkflow ! set default ELENCO');
          e.view = ProcessiView.ELENCO;
        }
      }
      this.currentView = e.view;
      if (e.alertMessage) {
        //        this.alertService.success(e.alertMessage, this.alertOptions);
        this.alertService.alert(new Alert({ ...this.alertOptions, type: e.alertType, message: e.alertMessage }));
      }
    }
  }

  // modificaProcesso(processo: Processo) {
  //   log('processi-container::modificaProcesso() ...');
  //   log('processi-container::modificaProcesso() ... processo = ' + JSON.stringify(processo));
  //   //    (this.utenti = this.utenti.filter(u => u.idUtente !== utente.idUtente)).push(utente);
  // }

  // eliminaProcesso(processo: Processo) {
  //   log('processi-container::eliminaUtente() ...');
  //   log('processi-container::eliminaUtente() ... processo = ' + JSON.stringify(processo));
  // }

  creaProcesso() {
    log('processi-container::creaProcesso()');
    this.currentView = ProcessiView.CREA;
  }

    dettaglio(processoDettaglio: Processo) {
    log('Processo ' + JSON.stringify(processoDettaglio));
    this.processoSelezionato = processoDettaglio;
    this.currentView = ProcessiView.DETTAGLIO;
    }
}

function log(a: any) {
  console.log(a);
}
