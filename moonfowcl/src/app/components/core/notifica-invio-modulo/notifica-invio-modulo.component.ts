/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {faFilePdf} from '@fortawesome/free-solid-svg-icons';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Messaggi} from 'src/app/common/messaggi';
import {NavSelection} from 'src/app/common/nav-selection';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {User} from 'src/app/model/common/user';
import {Istanza} from 'src/app/model/dto/istanza';
import {Nav} from 'src/app/model/dto/nav';
import {MoonfoblService} from 'src/app/services/moonfobl.service';
import {saveBlobIE} from 'src/app/services/service.utils';
import {StorageManager} from 'src/app/util/storage-manager';
import {SharedService} from '../../../services/shared.service';
import {ModalCloseComponent} from '../../modal-close/modal-close.component';
import {ModalFeedbackComponent} from '../../modal-feedback/modal-feedback.component';
import { Modulo } from 'src/app/model/dto/modulo';

@Component({
  selector: 'app-notifica-invio-modulo',
  templateUrl: './notifica-invio-modulo.component.html',
  styleUrls: ['./notifica-invio-modulo.component.scss']
})
export class NotificaInvioModuloComponent implements OnInit {

  codice: string;
  descrizione: string;
  istanza: Istanza;
  titolo: string;
  titoloNotifica: string;
  isConsumer : boolean = false;
  faFilePdf = faFilePdf;
  durataProcedimento: string;


  constructor(private sharedService: SharedService,
              private router: Router, private moonfoblService: MoonfoblService,
              private modalService: NgbModal) { }

  ngOnInit() {

    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    if (user && user.consumerParams) {
      this.isConsumer = true;
    }

    this.codice = this.sharedService.notifica.codice;
    this.descrizione = this.sharedService.notifica.descrizione;
    this.istanza = this.sharedService.notifica.istanza;
    this.titolo = this.sharedService.notifica.titolo;
    this.titoloNotifica = (this.titolo) ? this.titolo : ((this.descrizione) ? 'L\'istanza è stata inviata' : 'L\'istanza è stata inviata correttamente');

    console.log('codice notifica: ' + this.codice);

    // setTimeout(() => {
    //   this.inviaFeedback(this.istanza, function () {
    //     const mdRefThanks = this.modalService.open(ModalCloseComponent);
    //     mdRefThanks.componentInstance.modal_titolo = Messaggi.messaggioThanksFeedback;
    //     mdRefThanks.componentInstance.modal_buttons = false;
    //     mdRefThanks.result.then((result) => {
    //       log('notifica-invio-modulo::inviaFeedback() Closed with: ${result}' + result);
    //     }, (errore) => {
    //       log('notifica-invio-modulo::inviaFeedback() ERRORE ' + errore);
    //     });
    //   }.bind(this));
    // }, 1000);

  }


  setDurataProcedimento(){
    const modulo : Modulo  = this.istanza.modulo;
    this.moonfoblService
    .getModuloWithFields(
      modulo.idModulo,
      modulo.idVersioneModulo,
      "attributiWCL"
    )
    .subscribe((data) => {
      console.log(`data = ${data}`);

      let durataProcedimentoConf = JSON.parse(
        this.moonfoblService.getModuloAttributo(
          data as Modulo,
          "DURATA_PROCEDIMENTO_CONF"
        ));  
        
      if (durataProcedimentoConf) {
        if (durataProcedimentoConf[`testo`] && durataProcedimentoConf[`testo`].length > 0){
          this.durataProcedimento = durataProcedimentoConf[`testo`];
        }
        else if (durataProcedimentoConf[`durata`] && durataProcedimentoConf[`durata`].length > 0){
          this.durataProcedimento = `giorni `+durataProcedimentoConf[`durata`];
        }
      }

        
    });
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

  goToIstanze() {
    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    if (user && user.consumerParams) {
      if (user.consumerParams) {
        if (user.consumerParams.backUrl) {
          let url = user.consumerParams.backUrl;
          window.location.href = url;
        }
      }   } else {
      this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'home/istanze');
      this.router.navigate(['home']);
    }
  }

  goToNuovaIstanza() {
    this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'home/categorie');
    this.router.navigate(['home']);
  }
  

  // had to add as fixed component instead of modal

  // inviaFeedback(istanza: Istanza, callback) {
  //   const mdRefFeedabck = this.modalService.open(ModalFeedbackComponent);
  //   mdRefFeedabck.componentInstance.modal_titolo = Messaggi.messaggioFeedback;
  //   // mdRefFeedabck.componentInstance.modal_contenuto = Messaggi.messaggioFeedback;
  //   mdRefFeedabck.componentInstance.istanza = istanza;
  //   mdRefFeedabck.result.then((result) => {
  //     log('istanza-container::inviaFeedback() Closed with: ${result}' + result);
  //     callback();
  //   }, (errore) => {
  //     log('istanza-container::inviaFeedback() ERRORE ' + errore);
  //   });

  // }

  // inviaValutazione() {
  //   setTimeout(() => {
  //     this.inviaFeedback(this.istanza, function () {
  //       const mdRefThanks = this.modalService.open(ModalCloseComponent);
  //       mdRefThanks.componentInstance.modal_titolo = Messaggi.messaggioThanksFeedback;
  //       mdRefThanks.componentInstance.modal_buttons = false;
  //       mdRefThanks.result.then((result) => {
  //         log('notifica-invio-modulo::inviaFeedback() Closed with: ${result}' + result);
  //       }, (errore) => {
  //         log('notifica-invio-modulo::inviaFeedback() ERRORE ' + errore);
  //       });
  //     }.bind(this));
  //   }, 1000);
  // }
}

function log(a: any) {
  console.log(a);
}

