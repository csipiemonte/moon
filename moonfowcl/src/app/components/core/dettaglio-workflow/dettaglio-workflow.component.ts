/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {faHome} from '@fortawesome/free-solid-svg-icons';
import {FormioOptions} from '@formio/angular';

import {Messaggi} from 'src/app/common/messaggi';
import {UserInfo} from 'src/app/model/common';
import {Modulo} from 'src/app/model/dto/modulo';
import {MoonfoblService} from 'src/app/services/moonfobl.service';
import { Workflow } from 'src/app/model/dto/workflow';
import { StoricoWorkflow } from 'src/app/model/dto/storicoWorkflow';
import { Istanza } from 'src/app/model/dto/istanza';
import { environment } from 'src/environments/environment';
import { CustomFileService } from 'src/app/plugin/custom-file.service';

@Component({
  selector: 'app-dettaglio-workflow',
  templateUrl: './dettaglio-workflow.component.html',
  styleUrls: ['./dettaglio-workflow.component.scss']
})
export class DettaglioWorkflowComponent implements OnInit {


  private idIstanza: number = null;
  private idStoricoWorkflow: number = null;
  public storicoWorkflow: StoricoWorkflow = null;
  public istanza: Istanza = null;

  public struttura: any = null;
  public data: any = null;
  public msgErr: string = null;
  public options: FormioOptions;
  public modulo: Modulo;
  public readOnly = false;
  private alertMessage: string = Messaggi.msgSubmitForm;
  public fileService: CustomFileService;
  renderOptions: any;

  faHome = faHome;

  constructor(
    private moonservice: MoonfoblService,
    private route: ActivatedRoute,
    private router: Router,
    private customFileService: CustomFileService
  ) {
    this.fileService = customFileService;
  }

  currentUser: UserInfo = null;

  ngOnInit() {
    this.renderOptions = { breadcrumbSettings: { clickable: false }, language: 'it', fileService: this.fileService };

    this.route.paramMap.subscribe(
      (params) => {
        if (!(params.get('id'))) {
          console.log('id istanza non definito');
          return;
        }
        if (!(params.get('idw'))) {
          console.log('id storicoworkflow non definito');
          return;
        }
        this.options = {
          alerts: { 'submitMessage': this.alertMessage },
          errors: { 'message': Messaggi.msgErrForm },
        };
        this.idIstanza = +params.get('id');
        this.idStoricoWorkflow = +params.get('idw');
        this.getIstanza(this.idIstanza);
        this.getInfoStoricoWorkflow(this.idStoricoWorkflow);


      });

  }

  getInfoStoricoWorkflow(idStoricoWorkflow: number) {
    this.moonservice.getInfoStoricoWorkflow(idStoricoWorkflow)
      .subscribe(infoStoricoWorkflow => {
        this.storicoWorkflow = infoStoricoWorkflow;
        this.data = JSON.parse(infoStoricoWorkflow.datiAzione);
        console.log(this.data);
        this.struttura = JSON.parse(infoStoricoWorkflow.strutturaDatiAzione);
        this.readOnly = true;
      });
  }


  /* Eventi gestione Form*/

  onChange(eventChanged: any) {

  }

  onNextPage(next: any) {
    console.log('NEXT');
  }

  onPrevPage(prev: any) {
    console.log('PREV');
  }

  onFormLoad(event: any) {
    // console.log('onFormLoad');
  }

  // Ritorna elenco dei form
  goback() {
    this.router.navigate(['/home/istanza/' + this.idIstanza]);
  }

  backToIstanze() {
    this.router.navigate(['home/istanze/']);
  }

  backToDettaglio() {
    this.router.navigate(['/home/istanza/' + this.idIstanza]);
  }

  getIstanza(idIstanza: number) {
    this.moonservice.getIstanza(idIstanza)
      .subscribe(istanza => {
        this.istanza = istanza;
      });
  }


}
