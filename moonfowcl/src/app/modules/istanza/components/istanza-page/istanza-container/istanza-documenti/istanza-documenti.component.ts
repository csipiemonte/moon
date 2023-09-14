/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {faPaperclip} from '@fortawesome/free-solid-svg-icons';

import {Documento} from 'src/app/model/dto/documento';
import {MoonfoblService} from 'src/app/services/moonfobl.service';
import {saveBlobIE} from 'src/app/services/service.utils';
import {environment} from 'src/environments/environment';

@Component({
  selector: 'app-istanza-documenti',
  templateUrl: './istanza-documenti.component.html',
  styleUrls: ['./istanza-documenti.component.scss']
})
export class IstanzaDocumentiComponent implements OnInit {

  @Input('documenti') documenti: any[];
  @Output('initDocumenti') initDocumenti = new EventEmitter();

  faPaperclip = faPaperclip;

  constructor(
    private moonfoblService: MoonfoblService,
  ) {
  }

  ngOnInit(): void {
    log('istanza-documenti::ngOnInit this.documenti=' + this.documenti);
    if (!this.documenti) {
      log('istanza-documenti::ngOnInit this.documenti undefined this.initDocumenti.emit();');
      this.initDocumenti.emit();
    }
  }

  downloadDocumento(documento: Documento) {

    console.log('formioNameFile =' + documento.formioNameFile);
    console.log('idFile =' + documento.idFile);

    this.moonfoblService.getNotificaByIdFile(documento.idFile)
      .subscribe(x => {

        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], {type: documento.contentType});
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
        link.download = documento.descrizione ? documento.descrizione : documento.nomeFile;
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

  private normalizzaDocumento(formioNameFile: string) {
    let estensione = formioNameFile.substring(formioNameFile.lastIndexOf('.') + 1, formioNameFile.length);
    let name = formioNameFile.substring(37,formioNameFile.lastIndexOf('_'));
    return name + '.' + estensione;
  }

}

function log(a: any) {
  console.log(a);
}
