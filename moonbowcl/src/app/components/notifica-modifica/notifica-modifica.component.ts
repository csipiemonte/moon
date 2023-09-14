/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormioComponent, FormioOptions } from '@formio/angular';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { ModalConfirmComponent } from '../modal/modal-confirm/modal-confirm.component';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import { Messaggi } from 'src/app/common/messaggi';
import { CustomFileService } from 'src/app/plugin/custom-file.service';

@Component({
  selector: 'app-notifica-modifica',
  templateUrl: './notifica-modifica.component.html',
  styleUrls: ['./notifica-modifica.component.scss']
})
export class NotificaModificaComponent implements OnInit {

  private idIstanza: number = null;
  private idWorkflow: number = null;
  public struttura: any = null;
  public data: any = null;
  public msgErr: string = null;
  public options: FormioOptions;
  public readOnly = false;
  //private frmChanged = false;
  faHome = faHome;
  salvaPayload: any;

  public fileService: CustomFileService;
  renderOptions: any;
  
  constructor(private moonboblService: MoonboblService,
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private router: Router,
    private modalService: NgbModal,
    private customFileService: CustomFileService
  ) {
    this.fileService = customFileService;
  }

  ngOnInit() {
    this.renderOptions = {
      breadcrumbSettings: { clickable: false },
      language: 'it',
      fileService: this.fileService
    };

    this.spinnerService.show();

    this.options = {
      alerts: { 'submitMessage': Messaggi.msgModified },
      errors: { 'message': '' },
      i18n: {
        language: 'it',
        it: {
          complete: 'Invio completato',
          error: 'Si prega di correggere gli errori prima di inviare.'
        }
      }
    };

    this.route.paramMap.subscribe(
      (params) => {
        if (!(params.get('id'))) {
          console.log('id istanza non definito');
          return;
        }
        this.buildPayload(params);
        this.idIstanza = +params.get('id');
        this.getStrutturaAzione('NOTIFICA');
      });

    this.spinnerService.hide();
  }


  buildPayload(params) {
    if (params) {
      this.salvaPayload = { data: params.get('data'), 
                            idModulo: params.get('idModulo'),
                            idVersioneModulo: params.get('idVersioneModulo'), 
                            idIstanza: params.get('idIstanza'), 
                            codiceStato: params.get('codiceStato')
                          };
    }
  }

  getStrutturaAzione(codiceAzione: string) {
    this.moonboblService.getStrutturaByCodiceAzione(codiceAzione).then(
      data => {
        this.struttura = JSON.parse(data['struttura']);
        if (this.struttura === '') {
          this.msgErr = 'Nessuna struttura trovata !';
        }
        //this.spinnerService.hide();
        console.log('spinner hide');
      }
    ).catch(errore => {
      //this.spinnerService.hide();
      console.log('***' + errore);
    });

  }

  onSubmit(submission: any) {
    //console.log('submit');
  }

  motivaModifica(submission: any, callback: any, idIstanza: Number) {
    this.moonboblService.motivaModifica(submission.data, idIstanza).subscribe(
      response => {
        console.log('motiva modifica ' + response);
        this.mostraConferma(submission, callback);
        //this.salvaModifica(submission, callback);
      }
    );
  }

  salvaModifica(submission: any, callback: any) {
    // this.moonboblService.salvaIstanza( { 'data': JSON.parse(this.salvaPayload.data)}, 
    //                                             this.salvaPayload.idModulo, 
    //                                             this.salvaPayload.idVersioneModulo, 
    //                                             this.salvaPayload.idIstanza, 
    //                                             this.salvaPayload.codiceStato)

    this.moonboblService.salvaIstanzaConMotivazione( { 'data': JSON.parse(this.salvaPayload.data)},
                                                submission,
                                                this.salvaPayload.idModulo, 
                                                this.salvaPayload.idVersioneModulo, 
                                                this.salvaPayload.idIstanza, 
                                                this.salvaPayload.codiceStato)                                                
    .subscribe(
      response => {
        console.log(response);

        this.idIstanza = response['idIstanza'];
        console.log('Id Istanza: ' + this.idIstanza);

        this.spinnerService.hide();
        callback(null, submission);

        setTimeout(() => {
          this.router.navigate(['istanza/' + this.idIstanza]);
        }, 500);

        this.router.navigate(['istanza/' + this.idIstanza]);
      },
      errore => {
        console.log(JSON.stringify(errore));
        this.spinnerService.hide();
        callback({
          message: "Non è possibile salvare il modulo",
          component: null
        }, null)
      }
    );
  }

  mostraConferma(submission, callback) {

    const mdRef = this.modalService.open(ModalConfirmComponent);
    mdRef.componentInstance.modal_titolo = 'Motivazione registrata';
    mdRef.componentInstance.modal_contenuto = 'Proseguire per il salvataggio delle modifiche';

    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);

      this.spinnerService.show();
      this.salvaModifica(submission, callback);
            
    }, (reason) => {
      console.log(reason);
    });
  }

  mostraConfermaModifiche(submission, callback) {

    const mdRef = this.modalService.open(ModalConfirmComponent);
    mdRef.componentInstance.modal_titolo = 'Conferma modifiche';
    mdRef.componentInstance.modal_contenuto = 'Proseguire per il salvataggio delle modifiche';

    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);

      this.spinnerService.show();
      const dataNotificaMotivazione = submission.data;
      if (dataNotificaMotivazione.submit)
        delete dataNotificaMotivazione.submit;
    
      this.salvaModifica(dataNotificaMotivazione, callback);
            
    }, (reason) => {
      console.log(reason);
    });
  }

  backToIstanza() {
    this.router.navigate(['istanza/' + this.idIstanza]);
  }

  onChange(eventChanged: any) {
    //console.log('change');
  }

  onNextPage(next: any) {
    //console.log('NEXT');
  }

  onPrevPage(prev: any) {
    //console.log('PREV');
  }

  onFormLoad(event: any) {
    //this.frmChanged = false;
    //console.log('FORM LOAD');
  }

  onReady(form: FormioComponent) {
    // binding this
    form.options.hooks.beforeSubmit = this.handleBeforeSubmit.bind(this);
  }

  handleBeforeSubmit(submission, callback) {
    this.data = submission['data'];
    console.log('handle before submit ' + JSON.stringify(submission.data) + '**********');
    //this.motivaModifica({ 'data': submission.data }, callback, this.idIstanza);
    //this.salvaModifica(submission, callback);
    this.mostraConfermaModifiche(submission, callback);
  }

}
