/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import {ActivatedRoute,  Router} from '@angular/router';
import {Modulo} from '../../model/dto/modulo';
import {MoonboblService} from '../../services/moonbobl.service';
import * as common from '../../model/common';
import { FormioOptions} from '@formio/angular';
import {Messaggi} from '../../common/messaggi';
import { NgxSpinnerService } from 'ngx-spinner';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import { CustomFileService } from 'src/app/plugin/custom-file.service';

@Component({
  selector: 'app-dettaglio-workflow',
  templateUrl: './dettaglio-workflow.component.html',
  styleUrls: ['./dettaglio-workflow.component.css']
})
export class DettaglioWorkflowComponent implements OnInit {

  private idIstanza: number = null;
  private idStoricoWorkflow: number = null;

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
    private moonnservice: MoonboblService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private customFileService: CustomFileService
  ) {
    this.fileService = customFileService;
  }

  currentUser: common.UserInfo = null;

  ngOnInit() {
    this.renderOptions = { breadcrumbSettings: { clickable: false }, language: 'it', fileService: this.fileService };

    this.spinnerService.show();
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
        alerts: {'submitMessage': this.alertMessage},
        errors: {'message': Messaggi.msgErrForm},
      };
      this.idIstanza = +params.get('id');
      this.idStoricoWorkflow = +params.get('idw');
      this.getInfoStoricoWorkflow(this.idStoricoWorkflow);
    });
    this.spinnerService.hide();
  }

  getInfoStoricoWorkflow(idStoricoWorkflow: number) {
    this.moonnservice.getInfoStoricoWorkflow(idStoricoWorkflow)
      .then(infoStoricoWorkflow => {
          this.data =  JSON.parse(infoStoricoWorkflow.datiAzione);
           console.log(this.data);
          this.struttura = JSON.parse(infoStoricoWorkflow.strutturaDatiAzione);
          this.readOnly = true;
        }
      )
      .catch(errore => {
        console.log('***' + errore);
        this.msgErr = errore;
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
  this.router.navigate(['istanza/' + this.idIstanza]);
}

}
