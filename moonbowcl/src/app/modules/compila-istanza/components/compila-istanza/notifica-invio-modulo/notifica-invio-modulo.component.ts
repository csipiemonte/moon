/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Istanza } from 'src/app/model/dto/istanza';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { ObserveService } from 'src/app/services/observe.service';
import { SharedService } from 'src/app/services/shared.service';
import { Caller } from 'src/app/common/caller';
import { NavSelection } from 'src/app/common/nav-selection';
import { Nav } from 'src/app/model/dto/nav';
import {saveBlobIE} from '../../../../../services/service.utils';

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
  caller;
  Caller = Caller;
  constructor(private sharedService: SharedService,
    private router: Router, 
    private spinnerService: NgxSpinnerService, 
    private moonservice: MoonboblService,
    private observeService: ObserveService,
    private acRoute: ActivatedRoute) { }

  ngOnInit() {

    this.caller = this.acRoute.snapshot.params.caller;

    this.codice = this.sharedService.notifica.codice;
    this.descrizione = this.sharedService.notifica.descrizione;
    this.istanza = this.sharedService.notifica.istanza;
    this.titolo = this.sharedService.notifica.titolo;
    this.titoloNotifica = (this.titolo) ? this.titolo : ((this.descrizione) ? 'L\'istanza è stata inviata' : 'L\'istanza è stata inviata correttamente');

    console.log("codice notifica: "+this.codice);

  }

  downloadPdf() {
    this.spinnerService.show();
    this.moonservice.getPdf(this.istanza.idIstanza)
      .subscribe(x => {
        this.spinnerService.hide();
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: 'application/pdf' });
        saveBlobIE(newBlob)
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

  nuovaCompilazione() {
    this.router.navigate(['categorie']);
  }

  istanze() {
    this.sharedService.nav = new Nav(NavSelection.ISTANZE_DA_COMPLETARE, 'notifica-invio-modulo');  
    this.router.navigate(['istanze']);
  }


}
