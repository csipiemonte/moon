/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnInit, Output, SimpleChanges} from '@angular/core';
import {Router} from '@angular/router';
import {faFilePdf} from '@fortawesome/free-solid-svg-icons';
import {NOME_AZIONE} from 'src/app/common/costanti';
import {Istanza} from 'src/app/model/dto/istanza';
import {MoonfoblService} from 'src/app/services/moonfobl.service';
import {saveBlobIE} from 'src/app/services/service.utils';

@Component({
  selector: 'app-istanza-storico',
  templateUrl: './istanza-storico.component.html',
  styleUrls: ['./istanza-storico.component.scss']
})
export class IstanzaStoricoComponent implements OnInit {

  @Input('storicoWorkflow') storicoWorkflow: any[];
  @Input('istanza') istanza: Istanza;
  @Output('initStorico') initStorico = new EventEmitter();

  faFilePdf = faFilePdf;

  constructor(
    private moonfoblService: MoonfoblService,
    private router: Router) { }

  ngOnInit(): void {
    log('istanza-storico::ngOnInit this.storicoWorkflow=' + this.storicoWorkflow);
    if (!this.storicoWorkflow) {
      log('istanza-storico::ngOnInit this.storicoWorkflow undefined this.storicoWorkflow.emit();');
      this.initStorico.emit();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    log('istanza-storico::ngOnChanges() changes.storicoWorkflow.currentValue=' + changes.storicoWorkflow?.currentValue);
    log('istanza-storico::ngOnChanges() this.storicoWorkflow=' + this.storicoWorkflow);
    if (!this.storicoWorkflow || (this.storicoWorkflow && this.storicoWorkflow.length === 0)) {
      log('istanza-storico::ngOnInit this.storicoWorkflow undefined this.storicoWorkflow.emit();');
      this.initStorico.emit();
    }
  }

  downloadRicevuta(storico: any) {
    console.log('idFile ricevuta' + storico.idFileRendering);

    this.moonfoblService.getRicevuta(storico.idFileRendering)
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

        switch (storico.nomeAzione) {
          case NOME_AZIONE.GENERA_RICEVUTA:
            link.download = 'RicevutaAccettazione_' + this.istanza.codiceIstanza + '.pdf';
            break;
          case NOME_AZIONE.DINIEGO:
            link.download = 'RicevutaDiniego_' + this.istanza.codiceIstanza + '.pdf';
            break;
          case NOME_AZIONE.ACCOGLIMENTO:
            link.download = 'RicevutaAccoglimento_' + this.istanza.codiceIstanza + '.pdf';
            break;
        }
        //link.download = 'provaAlessandro_' + this.istanza.codiceIstanza + '.pdf';

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

  mostraDettaglioWorkflow(idStoricoWorkflow: number) {
    this.router.navigate(['home/istanze/' + this.istanza.idIstanza + '/dettaglio-workflow/' + idStoricoWorkflow]);
  }
}

function log(a: any) {
  console.log(a);
}
