/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { faPaperclip } from '@fortawesome/free-solid-svg-icons';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { saveBlobIE } from 'src/app/services/service.utils';


@Component({
  selector: 'app-istanza-allegati',
  templateUrl: './istanza-allegati.component.html',
  styleUrls: ['./istanza-allegati.component.scss']
})
export class IstanzaAllegatiComponent implements OnInit {

  @Input('allegati') allegati: any[];
  @Output('initAllegati') initAllegati = new EventEmitter();

  faPaperclip = faPaperclip;

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService
  ) { }

  ngOnInit(): void {

    log('ngOnInit ALLEGATI');

    log('istanza-allegati::ngOnInit this.allegati=' + this.allegati);
    if (!this.allegati) {
      log('istanza-allegati::ngOnInit this.allegati undefined this.initAllegati.emit();');
      this.initAllegati.emit();
    }

  }

  ngOnChanges(changes: SimpleChanges) {
    log('istanza-allegati::ngOnChanges() changes.allegati.currentValue=' + changes?.allegati?.currentValue);
    log('istanza-allegati::ngOnChanges() this.allegati=' + this.allegati);
  }

  downloadAllegato(formioNameFile: string, contentType: string) {
    console.log('formioNameFile ' + formioNameFile);
    this.spinnerService.show();
    this.moonboblService.getAllegato(formioNameFile)
      .subscribe(x => {
        this.spinnerService.hide();
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: contentType });
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

        link.download = this.normalizzaDaNomeFileFormio(formioNameFile);

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

  private normalizzaDaNomeFileFormio(formioNameFile: string) {
    let estensione = formioNameFile.substring(formioNameFile.lastIndexOf('.') + 1, formioNameFile.length);
    let name = formioNameFile.substring(0, formioNameFile.lastIndexOf('.') - 37);
    return name + "." + estensione;
  }


}

function log(a: any) {
//  console.log(a);
}
