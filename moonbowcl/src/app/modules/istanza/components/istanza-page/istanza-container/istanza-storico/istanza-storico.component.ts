/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { NOME_AZIONE } from './../../../../../../common/costanti';
import { Component, Input, OnInit, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { faFilePdf } from '@fortawesome/free-solid-svg-icons';
import { NgxSpinnerService } from 'ngx-spinner';
import { Istanza } from 'src/app/model/dto/istanza';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { saveBlobIE } from 'src/app/services/service.utils';
import { faInfoCircle } from '@fortawesome/free-solid-svg-icons';
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
  faInfoCircle = faInfoCircle;

  constructor(private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {

    log('ngOnInit STORICO');

    log('istanza-storico::ngOnInit this.storicoWorkflow=' + this.storicoWorkflow);
    if (!this.storicoWorkflow) {
      log('istanza-storico::ngOnInit this.storicoWorkflow undefined this.initAllegati.emit();');
      this.initStorico.emit();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    log('istanza-storico::ngOnChanges() changes.storicoWorkflow.currentValue=' + changes?.storicoWorkflow?.currentValue);
    log('istanza-storico::ngOnChanges() this.storicoWorkflow=' + this.storicoWorkflow);
  }


  downloadRicevuta(storico: any) {
    console.log('idFile ricevuta' + storico.idFileRendering);
    this.spinnerService.show();
    this.moonboblService.getRicevuta(storico.idFileRendering)
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

        switch(storico.nomeAzione){
          case NOME_AZIONE.GENERA_RICEVUTA:
            link.download = 'RicevutaAccettazione_' + this.istanza.codiceIstanza + '.pdf';
            break;
          case NOME_AZIONE.DINIEGO:
            link.download = 'RicevutaDiniego_' + this.istanza.codiceIstanza + '.pdf';
            break;
          case NOME_AZIONE.ACCOGLIMENTO:
            link.download = 'RicevutaAccoglimento_' + this.istanza.codiceIstanza + '.pdf';
            break;
          default:
              link.download = 'Documento_' + this.istanza.codiceIstanza + '.pdf';
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
      this.spinnerService.hide();
    }, 10000);

  }

  mostraDettaglioWorkflow(idStoricoWorkflow: number) {
    this.router.navigate(['istanze/' + this.istanza.idIstanza + '/dettaglio-workflow/' + idStoricoWorkflow]);
  }
}

function log(a: any) {
//  console.log(a);
}
