/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormioOptions} from '@formio/angular';
import {faHome} from '@fortawesome/free-solid-svg-icons';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormIOOptions} from 'src/app/model/dto/formio-options';
import {Istanza} from 'src/app/model/dto/istanza';
import {Workflow} from 'src/app/model/dto/workflow';
import {CustomFileService} from 'src/app/plugin/custom-file.service';
import { MoonfoblService } from 'src/app/services/moonfobl.service';
import { ModalConfirmComponent } from 'src/app/components/modal-confirm/modal-confirm.component';

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
  public istanza: Istanza = null;
  public datiWorkflow: Workflow = null;
  private frmChanged = false;
  FileService: CustomFileService;
  private formIoOptions: FormIOOptions;
  faHome = faHome;

  constructor(private moonfoblService: MoonfoblService,
              private router: Router,
              private modalService: NgbModal,
              private route: ActivatedRoute,
              private customFileService: CustomFileService) {
    this.FileService = customFileService;
    this.formIoOptions = new FormIOOptions();
  }

  ngOnInit() {

    this.options = this.formIoOptions;


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

        this.getIstanza(this.idIstanza);
        this.getDatiWorkflow(this.idIstanza, this.idWorkflow);

      });


  }


  getDatiWorkflow(idIstanza: number, idWorkflow: number) {
    this.moonfoblService.getWorkflowByIdWorkflow(idWorkflow).subscribe(
      datiWorkflow => {
        this.datiWorkflow = datiWorkflow;
        this.moonfoblService.getStrutturaAzione(idIstanza, idWorkflow).subscribe(
          data => {
            this.struttura = JSON.parse(data['struttura']);
            if (this.struttura === '') {
              this.msgErr = 'Nessuna struttura trovata !';
            } else {

              console.log(this.struttura);

              // INIZIALIZZAZIONE
              this.moonfoblService.getDatiInizializzaDatiAzione(idIstanza, idWorkflow).subscribe(
                response => {
                  if (response['data'] !== null && response['data'].length > 0) {
                    console.log('responseDATA:' + response['data']);
                    this.data = JSON.parse(response['data']);
                  }
                }
              );

            }
          });
      }
    );

  }

  onSubmit(submission: any) {

    this.data = submission['data'];
    console.log('SUBMISSION ' + JSON.stringify(submission.data) + '**********');
    this.completaAzione({ 'data': submission.data }, this.idWorkflow, this.idIstanza);

  }

  completaAzione(data: any, idWorkflow: number, idIstanza: number) {


    this.moonfoblService.completaAzione(data, idWorkflow, idIstanza).subscribe(
      response => {
        console.log(response);
        this.frmChanged = false;
        console.log('id Workflow completato: ' + this.idWorkflow);
        // caso in cui è stato fatto il passaggio di stato ma viene restituito una url per l'invocazione di un servizio
        // per fare altre operazioni
        if (response && response['url']) {
          console.log('invocazione url = ' + response['url']);
          this.moonfoblService.postUrl(response['url']).subscribe(
            resp => {
              console.log('posUrl response = ' + resp);
              this.mostraConfermaCambioStato(true);
            }, err => {
              console.log(err);
              if (err.status === 200 && err.statusText === 'OK') {
                this.mostraConfermaCambioStato(true);
              } else {
                // CHECK: si effettua il cambio stato anche se il servizio esterno non va a buon fine
                // definito per integrazione con Cosmo ma la configurazione del modulo potrebbe precvedere altre integrazioni
                // verificare se si può ritenere valido in generale
                // this.mostraConfermaCambioStato(false);
                this.mostraConfermaCambioStato(true);
              }

            }
          );
        } else {
          this.mostraConferma(true);
        }
      }, error => {
        console.log(error);
        this.mostraConferma(false);
      }
    );

  }

  mostraConferma(azioneCompletata: boolean) {

    const mdRef = this.modalService.open(ModalConfirmComponent);
    if (azioneCompletata) {

      mdRef.componentInstance.modal_titolo = 'Azione completata';
      mdRef.componentInstance.modal_contenuto = 'I dati sono stati inviati ';

      mdRef.result.then((result) => {
        console.log('Closed with: ${result}' + result);
        this.router.navigate(['home/istanze/']);

      }, (reason) => {
        console.log(reason);
      });
    } else {
      mdRef.componentInstance.modal_titolo = 'Azione non completata';
      mdRef.componentInstance.modal_contenuto = 'I dati non sono stati inviati ';

      mdRef.result.then((result) => {
        console.log('Closed with: ${result}' + result);
        this.router.navigate(['home/istanze/']);
      }, (reason) => {
        console.log(reason);
      });

    }
  }


  mostraConfermaCambioStato(azioneCompletata: boolean) {
    const mdRef = this.modalService.open(ModalConfirmComponent);
    if (azioneCompletata) {
      mdRef.componentInstance.modal_titolo = 'Azione completata';
      mdRef.componentInstance.modal_contenuto = 'I dati sono stati inviati';
      mdRef.result.then((result) => {
        console.log('Closed with: ${result}' + result);
        this.router.navigate(['home/istanze/']);

      }, (reason) => {
        console.log(reason);
      });
    } else {
      mdRef.componentInstance.modal_titolo = 'Azione non completata';
      mdRef.componentInstance.modal_contenuto = 'I dati non sono stati inviati ma è stato effettuato il passaggio di stato';
      mdRef.result.then((result) => {
        console.log('Closed with: ${result}' + result);
        this.router.navigate(['home/istanze/']);
      }, (reason) => {
        console.log(reason);
      });

    }
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

  backToIstanze() {
    this.router.navigate(['home/istanze/']);
  }

  backToDettaglio() {
    this.router.navigate(['/home/istanza/'+this.idIstanza]);
  }

  getIstanza(idIstanza: number) {
    this.moonfoblService.getIstanza(idIstanza)
      .subscribe(istanza => {
          this.istanza = istanza;
        }
      );
  }


}
