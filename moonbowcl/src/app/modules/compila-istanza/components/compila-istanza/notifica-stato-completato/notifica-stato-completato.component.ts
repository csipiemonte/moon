/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { faCogs } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { Messaggi } from 'src/app/common/messaggi';
import { ModalConfirmComponent } from 'src/app/components/modal/modal-confirm/modal-confirm.component';
import { ModalNotifyComponent } from 'src/app/components/modal/modal-notify/modal-notify.component';
import { Istanza } from 'src/app/model/dto/istanza';
import { Nav } from 'src/app/model/dto/nav';
import { Notifica } from 'src/app/model/dto/notifica';
import { SecurityService } from 'src/app/security.service';
import { HandleExceptionService } from 'src/app/services/handle-exception.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { ObserveService } from 'src/app/services/observe.service';
import { SharedService } from 'src/app/services/shared.service';
import { ValidationService } from 'src/app/services/validation.service';
import { saveBlobIE } from 'src/app/services/service.utils';
import { Caller } from 'src/app/common/caller';
@Component({
  selector: 'app-notifica-stato-completato',
  templateUrl: './notifica-stato-completato.component.html',
  styleUrls: ['./notifica-stato-completato.component.scss']
})
export class NotificaStatoCompletatoComponent implements OnInit {

  codice: string;
  descrizione: string;
  istanza: Istanza;
  titolo: string;
  titoloNotifica: string;
  titoloNotifica1: string;
  titoloNotifica2: string;
  msg: string;
  isBozza: boolean;
  faCogs = faCogs;
  caller;


  constructor(private sharedService: SharedService,
    private router: Router, 
    private spinnerService: NgxSpinnerService, 
    private moonservice: MoonboblService,
    private handleException: HandleExceptionService,
    private _modalService: NgbModal,
    private securityService: SecurityService,
    private validationService: ValidationService,
    private observeService: ObserveService,
    private acRoute: ActivatedRoute) { }

  ngOnInit() {

    // this.sharedService.nav = new Nav(3, 'home/notifica-stato-completato');
    // this.router.navigate([this.sharedService.nav.route]);

    this.caller = this.acRoute.snapshot.params.caller;

    this.codice = this.sharedService.notifica.codice;
    this.descrizione = this.sharedService.notifica.descrizione;
    this.istanza = this.sharedService.notifica.istanza;
    this.titolo = this.sharedService.notifica.titolo;
    //this.titoloNotifica = (this.titolo) ? this.titolo : ((this.descrizione) ? 'L\'istanza è stata completata' : 'L\'istanza è stata completata correttamente');
    this.titoloNotifica1 = (this.titolo) ? this.titolo : ((this.descrizione) ? Messaggi.messaggioEsitoCompletamentoSiDescr1 : Messaggi.messaggioEsitoCompletamentoNoDescr1);
    this.titoloNotifica2 = (this.titolo) ? this.titolo : ((this.descrizione) ? Messaggi.messaggioEsitoCompletamentoSiDescr2 : Messaggi.messaggioEsitoCompletamentoNoDescr2);

    console.log("codice notifica: " + this.codice);

    this.isBozza = this.istanza.stato.idStato === 1 ? true : false;

    //rilettura json per check prima di invio
    this.getIstanza(Number(this.istanza.idIstanza));

  }

  downloadPdf() {
    this.spinnerService.show();
    this.moonservice.getPdf(this.istanza.idIstanza)
      .subscribe(x => {
        this.spinnerService.hide();
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: 'application/pdf' });
        saveBlobIE(newBlob);
        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /*if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob);
          return;
        }*/

        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        const link = document.createElement('a');
        link.href = data;
        link.download = this.istanza.codiceIstanza + '.pdf';
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
      });

    setTimeout(() => {
      this.spinnerService.hide();
    }, 10000);

  }

  riportaInBozza() {

    const mdRef = this._modalService.open(ModalConfirmComponent);
    mdRef.componentInstance.modal_titolo = 'Richiesta conferma';
    mdRef.componentInstance.modal_contenuto = Messaggi.messaggioConfermaRiportaInBozza;
    mdRef.componentInstance.msgContenuto = this.istanza.codiceIstanza;
    mdRef.result.then(
      (result) => {
        //OK
        console.log(result);
        this.spinnerService.show();

        this.moonservice.riportaIstanzaInBozza(this.istanza).subscribe(
          res => {
            this.spinnerService.hide();
            this.titoloNotifica = 'Istanza ' + this.istanza.codiceIstanza + '  riportata in bozza';

            if (this.caller === Caller.DA_INVIARE){
                this.goToIstanze();
            }
            else{
              this.goToCategorie();
            }
           
          },
          err => {
            this.spinnerService.hide();
            //this.titoloNotifica = this.handleException.handleNotBlockingError(err).message;
            const notifica = new Notifica(err.error.code, err.error.msg, err.error.title, null);
            this.securityService.goToNotificationError(notifica);
          }

        );
      },
      // conferma
      (reason) => {
        //Cancel
        this.msg = 'Operazione annullata';
      }

    );
  }

  inviaIstanza() {

    const mdRef = this._modalService.open(ModalConfirmComponent);
    mdRef.componentInstance.modal_titolo = 'Richiesta conferma';
    mdRef.componentInstance.modal_contenuto = Messaggi.messaggioConfermaInvio;
    mdRef.componentInstance.msgContenuto = this.istanza.codiceIstanza;
    mdRef.result.then(
      (result) => {
        //OK
        console.log(result);
        this.spinnerService.show();

        let check = this.validationService.checkDatiIstanza(this.istanza);
        if (check) {
          this.moonservice.inviaIstanza(this.istanza).subscribe(
            res => {
              this.spinnerService.hide();
              this.preparaNotificaInvio(res);
              this.sharedService.nav = new Nav(this.sharedService.nav.active, 'notifica-invio-modulo');
              this.router.navigate([this.sharedService.nav.route+'/'+this.caller]);
            },
            err => {
              this.spinnerService.hide();
              const notifica = new Notifica(err.error.code, err.error.msg, err.error.title, null);

              //TOCHECK
              this.securityService.goToNotificationError(notifica);
              console.log(err);
            }

          );
        }
        else {

          this.spinnerService.hide();
          const errModal = this._modalService.open(ModalNotifyComponent);
          errModal.componentInstance.modal_titolo = 'Errore';
          errModal.componentInstance.modal_contenuto = Messaggi.messaggioDatiNonCompleti;
        }
      },
      // conferma
      (reason) => {
        //Cancel
        this.msg = 'Operazione annullata';
      }

    );
  }

  // lavoraIstanza() {  
  //   this.router.navigate(['/home/dettaglio-istanza/' + this.istanza.idIstanza]);
  // }

  goToIstanze() {
    this.router.navigate(['istanze']);    
  }

  goToCategorie() {
    this.router.navigate(['categorie']);
  }

  preparaNotificaInvio(response: Object) {

    const codice = response['codice'];
    const descrizione = response['descrizione'];
    const titolo = response['titolo'];
    const istanza: Istanza = response['istanza'];
    const notifica = new Notifica(codice, descrizione, titolo, istanza);
    this.sharedService.notifica = notifica;

  }


  getIstanza(idIstanza: number) {
      this.moonservice.getIstanza(idIstanza)
      .then(istanza => {
        this.istanza = istanza;
      }
      )
      .catch(errore => {
        console.log('***' + errore);
        const notifica = new Notifica(errore.error.code, errore.error.msg, errore.error.title, null);

        //TOCHECK
        this.securityService.goToNotificationError(notifica);        
      });

  }

}
