/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {faCopy, faEdit, faEye, faFilePdf} from '@fortawesome/free-solid-svg-icons';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {STATI} from 'src/app/common/costanti';
import {Documento} from 'src/app/model/dto/documento';
import {Istanza} from 'src/app/model/dto/istanza';
import {MoonfoblService} from 'src/app/services/moonfobl.service';
import {saveBlobIE} from 'src/app/services/service.utils';
@Component({
  selector: 'app-istanza-dettaglio',
  templateUrl: './istanza-dettaglio.component.html',
  styleUrls: ['./istanza-dettaglio.component.scss']
})
export class IstanzaDettaglioComponent implements OnInit {

  faEye = faEye;
  faFilePdf = faFilePdf;
  faEdit = faEdit;
  faCopy = faCopy;
  // isDocumentoPresent: boolean = false;
  // documento: Documento;
  // nomeDocumento = "Notifica.pdf";
  STATI = STATI;

  @Input() istanza: Istanza;


  constructor(
    private moonfoblService: MoonfoblService,
    private modalService: NgbModal,
    private router: Router
  ) {
  }

  ngOnInit(): void {
  }

  // downloadNotifica() {
  //   this.moonfoblService.getNotifica(this.istanza.idIstanza)
  //     .subscribe(x => {
  //       // It is necessary to create a new blob object with mime-type explicitly set
  //       // otherwise only Chrome works like it should
  //       const newBlob = new Blob([x], { type: 'application/pdf' });
  //       saveBlobIE(newBlob);
  //       // IE doesn't allow using a blob object directly as link href
  //       // instead it is necessary to use msSaveOrOpenBlob
  //       /* if (window.navigator && window.navigator.msSaveOrOpenBlob) {
  //         window.navigator.msSaveOrOpenBlob(newBlob);
  //         return;
  //       }
  //       */
  //       // For other browsers:
  //       // Create a link pointing to the ObjectURL containing the blob.
  //       const data = window.URL.createObjectURL(newBlob);
  //       const link = document.createElement('a');
  //       link.href = data;
  //       link.download = this.nomeDocumento;
  //       // this is necessary as link.click() does not work on the latest firefox
  //       link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
  //       setTimeout(function () {
  //         // For Firefox it is necessary to delay revoking the ObjectURL
  //         window.URL.revokeObjectURL(data);
  //         link.remove();
  //       }, 100);
  //     });
  //   setTimeout(() => {
  //   }, 10000);
  // }

  mostraRiepilogo() {
    this.router.navigate(['/manage-form', 'SUMMARY', this.istanza.idIstanza]);
  }

  copyClipboard(textToCopy: string) {
    var feedbackIconDettaglio = document.getElementById('feedbackCopyIconDettaglio');
    var copyIconDettaglio = document.getElementById('copyIconDettaglio');

    console.log('copio data' + textToCopy);
    navigator.clipboard.writeText(textToCopy).then().catch(e => console.error(e));
    // this.clipboard.writeText(textToCopy).then((() => console.log('testo copiato')));
    feedbackIconDettaglio.classList.remove('d-none');
    copyIconDettaglio.classList.add('d-none');

    setTimeout(function() {
      feedbackIconDettaglio.classList.add('d-none');
      copyIconDettaglio.classList.remove('d-none');

    }, 2000    )
  }

}
