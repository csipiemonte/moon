/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ProcessiView } from './../../../../processi.model';
import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Processo} from '../../../../../../model/dto/processo';
import {AzioneWf, EntryWf, ProcessiViewEvent, StatoWf} from '../../../../processi.model';
import { faHome, faSave, faEye, faTrash, faEdit, faSearchPlus, faPlus } from '@fortawesome/free-solid-svg-icons';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { Workflow } from 'src/app/model/dto/workflow';
import * as _ from 'lodash';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-dettaglio-processo',
  templateUrl: './dettaglio-processo.component.html',
  styleUrls: ['./dettaglio-processo.component.scss']
})
export class DettaglioProcessoComponent implements OnInit {

  faHome = faHome;
  faSave = faSave;
  faEye = faEye;
  faTrash = faTrash;
  faEdit = faEdit;
  faSearchPlus = faSearchPlus;
  faPlus = faPlus;
  @Input() processoSelezionato: Processo;
  @Output() backEvent = new EventEmitter<ProcessiViewEvent>();

  constructor(
    private moonboblService: MoonboblService,
    private sharedService: SharedService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  entriesWf: EntryWf[] = [];
  workflows: Workflow[] = [];
  newWorkflow: Workflow;

  elencoStatiWf: StatoWf[];
  elencoAzioniWf: AzioneWf[];

  isUtenteAbilitatoModifica: true;
  isAdmin = false;
  thumbnailProcesso: any;

  ngOnInit(): void {
    log('dettaglio-processo::ngOnInit()...');
    this.initStatiWf();
    this.initAzioni();
    this.initWorkflows();
    this.initNewWorkflow();
  }

  initNewWorkflow() {
    this.newWorkflow = new Workflow({
      idProcesso: this.processoSelezionato.idProcesso,
      idStatoWfPartenza: -1,
      idStatoWfArrivo: -1,
      idAzione: -1
    });
  }

  initStatiWf() {
    log('dettaglio-processo::initStatiWf()...');
    // this.showSpinner();
    this.moonboblService.getElencoStati().subscribe({
      'next': (res) => {
        log('dettaglio-processo::initWorkflows() moonboblService.getElencoStati() res = ' + res);
        this.elencoStatiWf = res;
        // this.entriesWf = this.workflows.map(w => ({
        //   idStatoWfPartenza: w.idStatoWfPartenza,
        //   idStatoWfArrivo: w.idStatoWfArrivo,
        //   idAzione: w.idAzione
        // }));
        // this.hideSpinner();
      },
      'error': (err) => {
        alert(err.errorMsg);
        // this.hideSpinner();
      }
    });
  }

  initAzioni() {
    log('dettaglio-processo::initAzioni()...');
    // this.showSpinner();
    this.moonboblService.getElencoAzioni().subscribe({
      'next': (res) => {
        log('dettaglio-processo::initWorkflows() moonboblService.getElencoAzioni() res = ' + res);
        this.elencoAzioniWf = res;
        // this.entriesWf = this.workflows.map(w => ({
        //   idStatoWfPartenza: w.idStatoWfPartenza,
        //   idStatoWfArrivo: w.idStatoWfArrivo,
        //   idAzione: w.idAzione
        // }));
        // this.hideSpinner();
      },
      'error': (err) => {
        alert(err.errorMsg);
        // this.hideSpinner();
      }
    });
  }

  initWorkflows() {
    log('dettaglio-processo::initWorkflows() this.processoSelezionato.idProcesso=' + this.processoSelezionato.idProcesso);
    if (!this.processoSelezionato) {
      this.workflows = null;
      return;
    }
    // this.showSpinner();
    this.moonboblService.getWorkflowsByIdProcesso(this.processoSelezionato.idProcesso).subscribe({
      'next': (res) => {
        log('dettaglio-processo::initWorkflows() moonboblService.getElencoWorkflows() res = ' + res);
        this.workflows = res;
        this.entriesWf = this.workflows.map(w => workflow2EntryWf(w));
        // this.hideSpinner();
      },
      'error': (err) => {
        alert(err.errorMsg);
        // this.hideSpinner();
      }
    });
  }

  back() {
    this.backEvent.emit(new ProcessiViewEvent());
  }

  patchRigaWf(i: number, e: EntryWf) {
    log('dettaglio-processo::patchRigaWf() e=' + JSON.stringify(e));
    const w = this.entryWf2Workflow(e);
    log('dettaglio-processo::patchRigaWf() w=' + JSON.stringify(w));
    // this.showSpinner();
    this.moonboblService.patchWorkflow(w).subscribe({
      'next': (res) => {
        log('dettaglio-processo::patchRigaWf() moonboblService.patchWorkflow() res = ' + res);
        this.entriesWf[i].patched();
        this.restImageProcesso();
        // this.hideSpinner();
      },
      'error': (err) => {
        alert(err.errorMsg);
        // this.hideSpinner();
      }
    });
  }

  eliminaRigaWf(i: number, e: EntryWf) {
    log('dettaglio-processo::eliminaRigaWf() e=' + JSON.stringify(e));
    const w = this.entryWf2Workflow(e);
    log('dettaglio-processo::eliminaRigaWf() w=' + JSON.stringify(w));
    // this.showSpinner();
    this.moonboblService.eliminaWorkflow(w).subscribe({
      'next': (res) => {
        log('dettaglio-processo::eliminaRigaWf() moonboblService.eliminaWorkflow() res = ' + res);
        this.entriesWf.splice(i, 1);
        this.restImageProcesso();
        // this.hideSpinner();
      },
      'error': (err) => {
        alert(err.errorMsg);
        // this.hideSpinner();
      }
    });
  }

  aggiungiRigaWf() {
    // this.newWorkflow.idProcesso = this.processoSelezionato.idProcesso;
    log('dettaglio-processo::aggiungiRigaWf() this.newWorkflow=' + JSON.stringify(this.newWorkflow));
    // this.showSpinner();
    this.moonboblService.salvaWorkflow(this.newWorkflow).subscribe({
      'next': (res) => {
        log('dettaglio-processo::aggiungiRigaWf() moonboblService.postWorkflow() res = ' + res);
        this.entriesWf.push(workflow2EntryWf(res));
        this.initNewWorkflow();
        this.restImageProcesso();
        // this.hideSpinner();
      },
      'error': (err) => {
        alert(err.errorMsg);
        // this.hideSpinner();
      }
    });
  }

  entryWf2Workflow(e: EntryWf): Workflow {
    let w = new Workflow(_.pick(e, 'idWorkflow', 'idStatoWfPartenza', 'idStatoWfArrivo', 'idAzione'));
    w.idProcesso = this.processoSelezionato.idProcesso;
    return w;
  }

  restImageProcesso() {
    this.thumbnailProcesso = null;
  }
  caricaImageProcesso() {
    if (!this.processoSelezionato.idProcesso || this.thumbnailProcesso) {
      return;
    }
    this.moonboblService.getImageProcesso(this.processoSelezionato.idProcesso)
      .subscribe((baseImage: any) => {
        log('[dettaglio-processo::caricaImageProcesso] getImageProcesso.subscribe ...');
        const reader = new FileReader();
        reader.onload = (e) => this.thumbnailProcesso = e.target.result;
        reader.readAsDataURL(new Blob([baseImage], { type: 'image/png' }));
      });
  }

  dettaglioRigaWf(e: EntryWf) {
    log('dettaglio-processo::dettaglioRigaWf()');
    log('dettaglio-processo::dettaglioRigaWf() idWorkflow = ' + e.idWorkflow);
    let gotoDettaglioWf = new ProcessiViewEvent();
    gotoDettaglioWf.view = ProcessiView.DETTAGLIO_WF;
    gotoDettaglioWf.processo = this.processoSelezionato;
    gotoDettaglioWf.idWorkflow = e.idWorkflow;
    this.backEvent.emit(gotoDettaglioWf);
  }
}

function workflow2EntryWf(w: Workflow): EntryWf {
  return new EntryWf(
    w.idWorkflow,
    w.idStatoWfPartenza,
    w.idStatoWfArrivo,
    w.idAzione
  );
}

function log(a: any) {
  console.log(a);
}
