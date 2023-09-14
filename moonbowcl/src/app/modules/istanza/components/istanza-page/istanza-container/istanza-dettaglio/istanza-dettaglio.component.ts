/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { LogPraticaCosmo } from 'src/app/model/dto/log-pratica-cosmo';
import { Component, Input, Output, OnInit, OnChanges, SimpleChanges, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { faBoxArchive, faCopy, faEdit, faEye, faFilePdf, faPenRuler, faTrash } from '@fortawesome/free-solid-svg-icons';
import { NgxSpinnerService } from 'ngx-spinner';
import { Istanza } from 'src/app/model/dto/istanza';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import * as _ from 'lodash';
import { SharedService } from 'src/app/services/shared.service';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { TicketCrmRichiesta } from 'src/app/model/dto/ticket-crm-richiesta';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalNotifyComponent } from 'src/app/components/modal/modal-notify/modal-notify.component';
import { LogServizioCosmo } from 'src/app/model/dto/log-servizio-cosmo';
import { saveBlobIE } from 'src/app/services/service.utils';
import { STORAGE_KEYS, SYSTEM_TICKET } from 'src/app/common/costanti';

@Component({
  selector: 'app-istanza-dettaglio',
  templateUrl: './istanza-dettaglio.component.html',
  styleUrls: ['./istanza-dettaglio.component.scss']
})
export class IstanzaDettaglioComponent implements OnInit {

  attributi: any;

  isAdmin = false;
  isModificaIstanzaInviata = false;
  isUtenteAbilitatoModifica = false;

  isLogCosmo = false;
  isLogServizioCosmo = false;
  ticketCrmRichiesta: TicketCrmRichiesta;
  isCreaTicketCrmPossibile = false;


  faEye = faEye;
  faFilePdf = faFilePdf;
  faEdit = faEdit;
  faCopy = faCopy;
  faPenRuler = faPenRuler;
  faTrash = faTrash;
  faBoxArchive = faBoxArchive;

  istanzaFlagTest = false;
  istanzaFlagEliminata = false;
  istanzaFlagArchiviata = false;

  @Input() istanza: Istanza;
  @Input() logCosmo: LogPraticaCosmo[];
  @Input() logServizioCosmo: LogServizioCosmo[];

  @Output('initLogCosmo') initLogCosmo = new EventEmitter();
  @Output('initLogServizioCosmo') initLogServizioCosmo = new EventEmitter();
  @Output('alertService') alert = new EventEmitter();

  constructor(
    private moonboblService: MoonboblService,
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private router: Router,
    private sharedService: SharedService,
    private modalService: NgbModal
  ) {
    this.isAdmin = sharedService.UserLogged?.isTipoADMIN();
    this.isUtenteAbilitatoModifica = this.sharedService.UserLogged?.hasRuoloOperatorMinADV() || this.sharedService.UserLogged?.hasRuoloOperatorSIMPMOD() ||
      this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    log('istanza-dettaglio::ngOnInit() istanza=' + this.istanza);
    if (this.istanza) {
      this.refresh('ngOnInit');
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    log('istanza-dettaglio::ngOnChanges() changes.istanza.currentValue=' + changes.istanza?.currentValue);
    log('istanza-dettaglio::ngOnChanges() this.istanza=' + this.istanza);
    if (this.istanza && changes.istanza) {
      this.refresh('ngOnChanges');
    }
    if (this.istanza && (changes.logCosmo || changes.logServizioCosmo)) {
      this.refreshCOSMO();
    }
  }

  refresh(caller: string) {
    log('istanza-dettaglio::refresh ' + 'modattr' + this.istanza.modulo.idModulo);
    if (this.istanza.flagTest) {
      this.istanzaFlagTest = true;
    }
    if (this.istanza.flagEliminata) {
      this.istanzaFlagEliminata = true;
    }
    if (this.istanza.flagArchiviata) {
      this.istanzaFlagArchiviata = true;
    }
    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + this.istanza.modulo.idModulo));
    if (!datiAtt) {
      log('istanza-dettaglio::refresh ERROR NOT FOUND datiAtt: modattr' + this.istanza.modulo.idModulo);
      return;
    }
    log('istanza-dettaglio::refresh datiAtt = ' + datiAtt);
    log('istanza-dettaglio::refresh datiAtt.attributi = ' + JSON.stringify(datiAtt.attributi));
    this.attributi = datiAtt.attributi;

    if (datiAtt.attributi?.MODIFICA_ISTANZA_INVIATA && datiAtt.attributi.MODIFICA_ISTANZA_INVIATA === 'S') {
      this.isModificaIstanzaInviata = true;
    }
    this.isUtenteAbilitatoModifica = this.isModificaIstanzaInviata && this.isUtenteAbilitatoModifica;

    this.refreshCOSMO();
    this.refreshCRM();
  }

  refreshCOSMO() {
    log('istanza-dettaglio::refreshCOSMO ' + 'attributi?.PSIT_COSMO = ' + this.attributi?.PSIT_COSMO);
    if (this.attributi?.PSIT_COSMO && this.attributi?.PSIT_COSMO === 'S') {
      log('istanza-dettaglio::refreshCOSMO ' + 'datiAtt.attributi.PSIT_COSMO');
      log('istanza-dettaglio::refreshCOSMO ' + 'this.logCosmo = ' + this.logCosmo);
      if (this.logCosmo) { // abbiamo la risposta dei logComo e verifichiamo se vuoto
        if (this.logCosmo.length > 0) {
          // Gestione retry definiti per utenti tipo ADMIN
          if (this.sharedService.UserLogged.isTipoADMIN()) {
            // possibilità di rilanciare il processso di avvio pratica solo a utente di tipo admin
            this.isLogCosmo = this.getIsLogCosmo();
          }
        }
      } else {
        log('istanza-dettaglio::refreshCOSMO this.logCosmo undefined.');
      }

      if (this.logServizioCosmo) { // abbiamo la risposta dei logComo e verifichiamo se vuoto
        if (this.logServizioCosmo.length > 1) {
          // Gestione retry definiti per utenti tipo ADMIN
          if (this.sharedService.UserLogged.isTipoADMIN()) {
            // possibilità di rilanciare il processso di avvio pratica solo a utente di tipo admin
            this.isLogServizioCosmo = this.getIsLogServizioCosmo();

            console.log('isLogServizioCosmo = '+this.isLogServizioCosmo);
          }
        }
      } else {
        log('istanza-dettaglio::refreshCOSMO this.logServizioCosmo undefined.');
      }
    }
  }

  getIsLogCosmo() {
    return (!this.logCosmo[0].creaRisposta || !this.logCosmo[0].creaDocumentoRisposta || !this.logCosmo[0].avviaProcessoRisposta);
  }

  getIsLogServizioCosmo() {
    return (!this.logServizioCosmo[0].risposta || !this.logServizioCosmo[1].risposta);
  }


  refreshCRM() {
    log('istanza-dettaglio::refreshCRM ' + 'attributi?.PSIT_CRM = ' + this.attributi?.PSIT_CRM);
    if (this.attributi?.PSIT_CRM && this.attributi?.PSIT_CRM === 'S') {
      log('istanza-dettaglio::refreshCRM ' + 'datiAtt.attributi.PSIT_CRM');
      this.moonboblService.getTicketCrmRichiesteIstanzaTx(this.istanza.idIstanza).subscribe(
        (tickets) => {
          this.checkRetryTicketCrm(tickets);
          /*
          log('istanza-dettaglio::refreshCRM ' + 'tickets = ' + tickets);
          if (tickets.length > 0) {
            this.ticketCrmRichiesta = tickets[0];
            this.isCreaTicketCrmPossibile = false;
            log('istanza-dettaglio::refreshCRM ' + 'this.ticketCrmRichiesta = ' + this.ticketCrmRichiesta);
          } else {
            if (this.sharedService.UserLogged.isTipoADMIN()) {
              this.isCreaTicketCrmPossibile = true;
            }
          }*/
        },
        (err: MoonboError) => {
          log('istanza-dettaglio::refreshCRM ' + err.errorMsg);
        }
      );
    }
  }

  checkRetryTicketCrm(tickets){
    if (tickets.length > 0) {
      //NEXTCRM
      if(tickets[0].idTicketingSystem == SYSTEM_TICKET.NEXTCRM){
        this.ticketCrmRichiesta = tickets[0];
        this.isCreaTicketCrmPossibile = false;  
      }
      //R2U
      if(tickets[0].idTicketingSystem == SYSTEM_TICKET.R2U){
        for(var t of tickets){
          if( t.codiceEsito !='201' ){
            this.isCreaTicketCrmPossibile = true;
          }
          if ( t.tipoDoc == 1){
            this.ticketCrmRichiesta = t;
          }
        }
      }
      //OTRS
      if(tickets[0].idTicketingSystem == SYSTEM_TICKET.OTRS){
        if( tickets[0].codiceEsito !='200' ){
          this.isCreaTicketCrmPossibile = true;
        }
        this.ticketCrmRichiesta = tickets[0];
      }
      log('istanza-dettaglio::refreshCRM ' + 'this.ticketCrmRichiesta = ' + this.ticketCrmRichiesta);
    } else {
      // a priori del sistema si abilita la retry del ticket
      if (this.sharedService.UserLogged.isTipoADMIN()) {
        this.isCreaTicketCrmPossibile = true;
      }
    }
  }

  mostraDettaglioIstanza() {
    // mostra dettaglio istanza
    this.router.navigate(['istanze/view-form/' + this.istanza.idIstanza]);
  }

  downloadPdf() {
    this.spinnerService.show();
    this.moonboblService.getPdf('' + this.istanza.idIstanza)
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

  downloadJson() {
    this.spinnerService.show();
    // var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(this.istanza.data));
    // let dataStr = "data:text/json;charset=utf-8," + this.istanza.data;
    const dataStr = "data:text/json;charset=utf-8," + this.getJsonDataToDownload();
    const codicIstanza = this.istanza.codiceIstanza;
    let downloadAnchorNode = document.createElement('a');
    downloadAnchorNode.setAttribute("href", dataStr);
    downloadAnchorNode.setAttribute("download", codicIstanza + ".json");
    document.body.appendChild(downloadAnchorNode); // required for firefox
    downloadAnchorNode.click();
    downloadAnchorNode.remove();

    setTimeout(() => {
      this.spinnerService.hide();
    }, 100);
  }

  getJsonDataToDownload() {
    const jsonData = JSON.parse(this.istanza.data);
    let data = null;
    if (jsonData.data.data) {
      data = jsonData.data.data;
      if (_.isEqual(data, jsonData.data.data)) {
        delete jsonData.data.data;
      }
      console.log('json = ' + jsonData);
    }
    return JSON.stringify(jsonData);
  }

  modificaIstanza() {
    this.router.navigate(['modificaistanza/view-form/', this.istanza.idIstanza]);
  }

  creaPraticaEdAvviaProcessoCosmo() {
    log('creaPraticaEdAvviaProcessoCosmo ' + this.istanza.idIstanza);
    this.spinnerService.show();
    this.moonboblService.creaPraticaEdAvviaProcessoCosmo(this.istanza.idIstanza).subscribe(
      () => {
        this.spinnerService.hide();
        // alert('Richiesta di creazione pratica ed avvio processo COSMO effettuata');

        this.notify('Reinvio con esito positivo', function () { this.initLogCosmo.emit() }.bind(this));
        // this.initLogCosmo.emit(); // per refresh

        // this.refreshCOSMO();
      },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        this.spinnerService.hide();
        // this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);
      
        this.alert.emit({ text: 'Reinvio con esito negativo', type: 'error' });
      
      }
    );
  }

  reinviaIntegrazioneCosmo() {
    log('reinviaIntegrazioneCosmo ' + this.istanza.idIstanza);
    this.spinnerService.show();
    this.moonboblService.inviaIntegrazione(this.istanza.idIstanza).subscribe(
      () => {
        this.spinnerService.hide();
        // alert('Richiesta di creazione pratica ed avvio processo COSMO effettuata');

        this.notify('Reinvio con esito positivo', function () { this.initLogServizioCosmo.emit() }.bind(this));
        // this.initLogCosmo.emit(); // per refresh

        // this.refreshCOSMO();
      },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        this.spinnerService.hide();
        // this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);
        this.alert.emit({ text: 'Reinvio con esito negativo', type: 'error' });
      }
    );
  }

  // creaPraticaEdAvviaProcessoCosmo() {
  //   this.notify('Richiesta di creazione pratica ed avvio processo COSMO effettuata', function () { this.initLogCosmo.emit() }.bind(this));
  // }

  creaTicketCRM() {
    log('creaTicketCRM ' + this.istanza.idIstanza);
    this.moonboblService.creaTicketCrm(this.istanza.idIstanza).subscribe(
      () => {
        this.spinnerService.hide();
        // alert('Richiesta di creazione Ticket CRM effettuata');
        this.notify('Richiesta di creazione Ticket CRM effettuata', function () { this.refreshCRM() }.bind(this));
        // this.refreshCRM();
      },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        this.spinnerService.hide();
        // this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);

        this.alert.emit({ text: 'Invio email con esito negativo', type: 'error' });

      }
    );
  }

  notify(contenuto: string, doAction: () => void) {
    const mdRef = this.modalService.open(ModalNotifyComponent);
    mdRef.componentInstance.modal_titolo = 'Azione';
    mdRef.componentInstance.modal_contenuto = contenuto;
    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);
      doAction();
    }, (reason) => {
      console.log(reason);
      doAction();
    });
  }

}


function log(a: any) {
  //  console.log(a);
}
