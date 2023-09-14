/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { FormioComponent, FormioOptions } from '@formio/angular';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MoonboblService } from '../../services/moonbobl.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ModalConfirmComponent } from '../modal/modal-confirm/modal-confirm.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {Workflow} from '../../model/dto/workflow';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import { Messaggi } from 'src/app/common/messaggi';
import { CustomFileService } from 'src/app/plugin/custom-file.service';

@Component({
  selector: 'app-completa-azione',
  templateUrl: './completa-azione.component.html',
  styleUrls: ['./completa-azione.component.scss']
})
export class CompletaAzioneComponent implements OnInit {

  private idIstanza: number = null;
  private idWorkflow: number = null;
  public struttura: any = null;
  public data: any = null;
  public msgErr: string = null;
  public options: FormioOptions;
  public readOnly = false;
  public datiWorkflow: Workflow = null;
  private frmChanged = false;
  private inizializzaDatiAzione = true;
  faHome = faHome;

  public form: FormioComponent;

  FileService: CustomFileService;
  renderOptions: any;

  constructor(private moonboblService: MoonboblService,
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private router: Router,
    private modalService: NgbModal,
    private customFileService: CustomFileService) {
    this.FileService = customFileService;
  }

  ngOnInit() {

    this.renderOptions = {
      breadcrumbSettings: { clickable: false },
      language: "it",
      fileService: this.FileService,
    };

    this.options = {
      alerts: { 'submitMessage': Messaggi.msgAzioneOK},
      errors: { 'message': Messaggi.msgAzioneKO }
    };

    this.spinnerService.show();
    this.route.paramMap.subscribe(
      (params) => {
        if (!(params.get('id'))) {
          console.log('id istanza non definito');
          return;
        }
        if (!(params.get('idw'))) {
          console.log('id workflow non definito');
          return;
        }
        this.idIstanza = +params.get('id');
        this.idWorkflow = +params.get('idw');

        this.getdatiWorkflow(this.idWorkflow);
        this.getStrutturaAzione(this.idIstanza, this.idWorkflow);

      });

      this.spinnerService.hide();
  }

  getdatiWorkflow(idWorkflow: number) {
    this.moonboblService.getWorkflowByIdWorkflow(idWorkflow).then(
      datiWorkflow => {
        this.datiWorkflow = datiWorkflow;
      this.spinnerService.hide();
      console.log('spinner hide');
      }

    ).catch(errore => {
      this.spinnerService.hide();
      console.log('***' + errore);
    });

  }

  getStrutturaAzione(idIstanza: number, idWorkflow: number) {
    this.moonboblService.getStrutturaAzione(idIstanza, idWorkflow).then(
      data => {
        this.struttura = JSON.parse(data['struttura']);
        if (this.struttura === '') {
          this.msgErr = 'Nessuna struttura trovata !';
        } else {
          if (this.inizializzaDatiAzione) {
            this.spinnerService.show();
            this.moonboblService.getDatiInizializzaDatiAzione(idIstanza, idWorkflow).subscribe(
              response => {
                if (response['data'] !== null && response['data'].length > 0) {
                  console.log('responseDATA:' + response['data']);
                  this.data = JSON.parse(response['data']);
                }
                this.spinnerService.hide();
              },
              errore => {
                this.spinnerService.hide();
                /*
                const srvError: SrvError = new SrvError();
                srvError.code = '' + errore.status;
                srvError.errorMessage = errore.error.msg;
                srvError.messaggioCittadino = 'Servizio inizializzazione  fallito, impossibile procedere con compilazione modulo';
                srvError.sessionExpired = false;
                this.securityService.jumpToErrorPageNav(srvError, this.infoNavigation);
                */
              }
            );
          }
        }
      this.spinnerService.hide();
      console.log('spinner hide');
      }

    ).catch(errore => {
      this.spinnerService.hide();
      console.log('***' + errore);
    });

  }
  // onSubmit(submission: any) {
  //   this.data = submission['data'];
  //   console.log('SUBMISSION ' + JSON.stringify(submission.data) + '**********');
  //   this.completaAzione({'data' : submission.data}, this.idWorkflow, this.idIstanza);  
  //   //  window.scrollTo(0, 0);
  //   //  this.router.navigate(['istanza/' + this.idIstanza ]);
  // }

  onReady(form: FormioComponent) {
    // binding this
    form.options.hooks.beforeSubmit = this.handleBeforeSubmit.bind(this);
  }

  handleBeforeSubmit(submission, callback) {

    this.data = submission['data'];
    console.log('SUBMISSION ' + JSON.stringify(submission.data) + '**********');
    this.spinnerService.show();
    this.moonboblService.completaAzione({'data' : submission.data}, this.idWorkflow, this.idIstanza).subscribe(
      response => {
        console.log(response);
        this.frmChanged = false;
        console.log('id Workflow completato: ' + this.idWorkflow);
        // window.scrollTo(0, 0);

        //console.log(`pubblica mydocs`);        

        // const idStoricoWorkflow : number = response['idStoricoWorkflow'];

        // this.moonboblService.pubblicaMyDocs(this.idIstanza, idStoricoWorkflow).subscribe(
        //   response => {
        //     console.log(`*** pubblicaMyDocs response: ${response}`);
        //   } ,
        //   error => {        
        //    console.log(`*** pubblicaMyDocs error: ${error}`);
        //   }
        // );

        this.spinnerService.hide();
        this.mostraConferma();
        // callback(null, submission);
      },
      error => {
        this.spinnerService.hide();
        JSON.stringify(error);
        callback({
          message: Messaggi.msgAzioneKO,
          component: null
        }, null);
      }
    );
  }

  completaAzione(data: any, idWorkflow: number, idIstanza: number) {
    this.moonboblService.completaAzione(data, idWorkflow, idIstanza).subscribe(
      response => {
        console.log(response);
        this.frmChanged = false;
        console.log('id Workflow completato: ' + this.idWorkflow);
        window.scrollTo(0, 0);
        this.mostraConferma();
      }
    );
    // this.mostraConferma();
  }

  mostraConferma() {

    const mdRef = this.modalService.open(ModalConfirmComponent);
    mdRef.componentInstance.modal_titolo = 'Azione completata';
    mdRef.componentInstance.modal_contenuto = 'I dati sono stati inviati ';
    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);  
      this.router.navigate(['istanza/' + this.idIstanza]);
    }, (reason) => {
      this.spinnerService.hide();
      console.log(reason);
    });

  }

  tornaLavoraIstanza() {
    this.router.navigate(['istanza/' + this.idIstanza]);
  }

  onChange(eventChanged: any) {
    if (eventChanged['changed']) {
      if (eventChanged['changed'].isValid === false) {
        console.log('Non valido');
      } else {
        if (eventChanged['changed']['data'] != this.data) {
          this.data = eventChanged['changed']['data'];
          this.frmChanged = true;
          console.log('dati cambiati =' + JSON.stringify(eventChanged['changed']['data']));
          console.log('dati cambiati =' + JSON.stringify(this.data));
        }
      }
    }
  }

  onNextPage(next: any) {
    console.log('NEXT');
  }

  onPrevPage(prev: any) {
    console.log('PREV');
  }

  onFormLoad(event: any) {
    this.frmChanged = false;
  }
}
