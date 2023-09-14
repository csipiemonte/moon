/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { Processo } from 'src/app/model/dto/processo';
import { Workflow } from 'src/app/model/dto/workflow';
import { ProcessiView, ProcessiViewEvent } from 'src/app/modules/processi/processi.model';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { faHome, faSave, faTrash, faEdit } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-dettaglio-workflow',
  templateUrl: './dettaglio-workflow.component.html',
  styleUrls: ['./dettaglio-workflow.component.scss']
})
export class DettaglioWorkflowComponent implements OnInit {

  faHome = faHome;
  faSave = faSave;

  @Input() processoSelezionato: Processo;
  @Input() idWorkflowSelezionato: number;
  @Output() backEvent = new EventEmitter<ProcessiViewEvent>();

  spinnerLevel = 0;

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }
  
  isAdmin = false;

  workflow: Workflow;
  workflowEdit: Workflow;

  ngOnInit(): void {
    log('dettaglio-workflow::ngOnInit()...');
    this.initWorkflow();
  }

  initWorkflow() {
    log('dettaglio-workflow::initWorkflow() idWorkflowSelezionato = ' + this.idWorkflowSelezionato);
    this.showSpinner();
    this.moonboblService.getWorkflow(this.idWorkflowSelezionato).subscribe({
      'next': (res) => {
        log('dettaglio-workflow::initWorkflow() moonboblService.getWorkflow() res = ' + res);
        this.workflow = res;
        this.workflowEdit = res;
        this.hideSpinner();
      },
      'error': (err) => {
        alert(err.errorMsg);
        this.hideSpinner();
      }
    });
  }

  showSpinner() {
    log('dettaglio-workflow::showSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel++;
    if (this.spinnerLevel === 1) {
      log('dettaglio-workflow::showSpinner() spinnerService.show().');
      this.spinnerService.show();
    }
  }
  hideSpinner() {
    log('dettaglio-workflow::hideSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel--;
    if (this.spinnerLevel === 0) {
      log('dettaglio-workflow::hideSpinner() spinnerService.hide().');
      this.spinnerService.hide();
    }
  }

  back() {
    log('dettaglio-processo::back()');
    log('dettaglio-processo::back() processoSelezionato = ' + this.processoSelezionato);
    let gotoDettaglio = new ProcessiViewEvent();
    gotoDettaglio.view = ProcessiView.DETTAGLIO;
    gotoDettaglio.processo = this.processoSelezionato;
    gotoDettaglio.idWorkflow = null;
    this.backEvent.emit(gotoDettaglio);
  }

}

function log(a: any) {
  console.log(a);
}
