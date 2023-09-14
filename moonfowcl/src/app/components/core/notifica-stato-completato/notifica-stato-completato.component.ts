/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {faCogs, faFilePdf} from '@fortawesome/free-solid-svg-icons';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Messaggi} from 'src/app/common/messaggi';
import {NavSelection} from 'src/app/common/nav-selection';
import {Istanza} from 'src/app/model/dto/istanza';
import {Nav} from 'src/app/model/dto/nav';
import {Notifica} from 'src/app/model/dto/notifica';
import { MoonfoblService } from 'src/app/services/moonfobl.service';
import { saveBlobIE } from 'src/app/services/service.utils';
import { SharedService } from 'src/app/services/shared.service';
import { ValidationService } from 'src/app/services/validation.service';
import { ModalBasicComponent } from '../../modal-basic/modal-basic.component';
import { ModalNotifyComponent } from '../../modal-notify/modal-notify.component';

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
  faFilePdf = faFilePdf;


  constructor(private sharedService: SharedService,
              private router: Router, private moonfoblService: MoonfoblService,
              private _modalService: NgbModal,
              private validationService: ValidationService) {
  }

  ngOnInit() {
    this.codice = this.sharedService.notifica.codice;
    this.descrizione = this.sharedService.notifica.descrizione;
    this.istanza = this.sharedService.notifica.istanza;
    this.titolo = this.sharedService.notifica.titolo;
    this.titoloNotifica1 = (this.titolo) ? this.titolo : ((this.descrizione) ? Messaggi.messaggioEsitoCompletamentoSiDescr1 : Messaggi.messaggioEsitoCompletamentoNoDescr1);
    this.titoloNotifica2 = (this.titolo) ? this.titolo : ((this.descrizione) ? Messaggi.messaggioEsitoCompletamentoSiDescr2 : Messaggi.messaggioEsitoCompletamentoNoDescr2);
    console.log('codice notifica: ' + this.codice);
    this.isBozza = this.istanza.stato.idStato === 1 ? true : false;

  }

  downloadPdf() {

    this.moonfoblService.getPdf(this.istanza.idIstanza)
      .subscribe(x => {

        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: 'application/pdf' });
        saveBlobIE(newBlob);
        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /* if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob);
          return;
        }
        */
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

    }, 10000);

  }

  riportaInBozza() {

    const mdRef = this._modalService.open(ModalBasicComponent);
    mdRef.componentInstance.modal_titolo = 'Richiesta conferma';
    mdRef.componentInstance.modal_contenuto = Messaggi.messaggioConfermaRiportaInBozza;
    mdRef.componentInstance.msgContenuto = this.istanza.codiceIstanza;
    mdRef.result.then(
      (result) => {
        console.log(result);
        this.moonfoblService.riportaIstanzaInBozza(this.istanza).subscribe(
          res => {
            this.titoloNotifica = 'Istanza ' + this.istanza.codiceIstanza + '  riportata in bozza';
            this.goToIstanze();
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

    const mdRef = this._modalService.open(ModalBasicComponent);
    mdRef.componentInstance.modal_titolo = 'Richiesta conferma';
    mdRef.componentInstance.modal_contenuto = Messaggi.messaggioConfermaInvio;
    mdRef.componentInstance.msgContenuto = this.istanza.codiceIstanza;
    mdRef.result.then(
      (result) => {
        //OK
        console.log(result);

        let check = this.validationService.checkDatiIstanza(this.istanza);
        if (check) {
          this.moonfoblService.inviaIstanza(this.istanza).subscribe(
            res => {

              this.preparaNotificaInvio(res);
              this.sharedService.nav = new Nav(this.sharedService.nav.active, 'home/notifica-invio-modulo');
              this.router.navigate([this.sharedService.nav.route]);
            }
          );
        } else {
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

  lavoraIstanza() {
    this.router.navigate(['/home/istanza/' + this.istanza.idIstanza]);
  }

  goToIstanze() {

    this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'home/istanze');
    this.router.navigate(['home']);
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

    this.moonfoblService.getIstanza(idIstanza)
      .subscribe(istanza => {
          this.istanza = istanza;
        }
      );
  }


  gestisci_pagamento() {
    console.log('gestisci_pagamento');
  }
}
