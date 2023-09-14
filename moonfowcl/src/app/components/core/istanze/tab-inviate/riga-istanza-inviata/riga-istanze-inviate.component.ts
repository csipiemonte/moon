/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {Istanza} from '../../../../../model/dto/istanza';
import {MoonfoblService} from '../../../../../services/moonfobl.service';
import {faCopy} from '@fortawesome/free-solid-svg-icons';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Messaggi} from 'src/app/common/messaggi';
import {ModalBasicComponent} from 'src/app/components/modal-basic/modal-basic.component';
import {saveBlobIE} from 'src/app/services/service.utils';

@Component({
  selector: 'tr[riga-istanza-inviata]',
  templateUrl: './riga-istanza-inviata.component.html',
  styleUrls: ['./riga-istanza-inviata.component.scss']
})
export class RigaIstanzeInviateComponent implements OnInit {

  // riga istanza passata in input
  @Input('riga-istanza-data') rigaIstanza: Istanza;
  // pagina corrente
  @Input('currPage') currPage: number;
  // evento richeista eliminazione istanza
  @Output('onDeleteRiga') rigaIstanzaDeleted = new EventEmitter();
  // evento richiesta cambio importanza
  @Output('onCambiaImportanza') rigaIstanzaCambiaImportanza = new EventEmitter();
  @Output('onRiportaInBozza') rigaIstanzaRiportaInBozza = new EventEmitter();

  faCopy = faCopy;

  constructor(
    private moonfoblService: MoonfoblService,
    private modalService: NgbModal,
    private router: Router) { }

  ngOnInit() {
  }

  mostraIstanza() {
    this.router.navigate(['/manage-form', 'UPDATE', this.rigaIstanza.idIstanza]);
  }

  mostraDettaglioIstanza() {
    this.router.navigate(['/home/istanza/' + this.rigaIstanza.idIstanza]);
  }

  modificaIstanza() {
    this.router.navigate(['/manage-form', 'UPDATE', this.rigaIstanza.idIstanza]);
  }

  cancellaIstanza() {
    // emette evento rigaIstanzaDeleted visto all'esterno come onDeleteRiga
    this.rigaIstanzaDeleted.emit(this.rigaIstanza);
  }

  riportaInBozza() {
    // console.log('riportaInBozza istanza ' + this.rigaIstanza.idIstanza );
    // emette evento rigaIstanzaDeleted visto all'esterno come onDeleteRiga
    this.rigaIstanzaRiportaInBozza.emit(this.rigaIstanza);
  }

  modificaImportanza($event) {
    $event.preventDefault();
    //    console.log('modificaImportanza BEGIN ID ' + this.rigaIstanza.idIstanza + ' was ' + this.rigaIstanza.importanza);
    // emette evento rigaIstanzaCambiaImportanza visto all'esterno come onCambiaImportanza
    this.rigaIstanzaCambiaImportanza.emit(this.rigaIstanza);
    //    console.log('modificaImportanza END ID ' + this.rigaIstanza.idIstanza + ' is ' + this.rigaIstanza.importanza);
  }

  stampa() {
    console.log('stampa');
  }

  downloadPdf() {
    this.moonfoblService.getPdf(this.rigaIstanza.idIstanza)
      .subscribe(x => {

        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], {type: 'application/pdf'});
        saveBlobIE(newBlob);
        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /* if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob);
          return;
        }
        */
        //TODO soluzione problema navigator ie
        // const nav = (window.navigator as any);
        // if (nav && nav.msSaveOrOpenBlob) {
        //   nav.msSaveOrOpenBlob(newBlob);
        //   return;
        // }


        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        var link = document.createElement('a');
        link.href = data;
        link.download = this.rigaIstanza.codiceIstanza + '.pdf';
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

  eseguiIntegrazioneIstanza() {
    this.router.navigate(['/home/richiesta-integrazione/' + this.rigaIstanza.idIstanza]);
    console.log(this.rigaIstanza.idIstanza);
  }

  duplicaIstanza(idIstanza :string){
    var msg = '';
    const mdRef = this.modalService.open(ModalBasicComponent);
    mdRef.componentInstance.modal_titolo = 'Richiesta conferma';
    mdRef.componentInstance.modal_contenuto = Messaggi.messaggioConfermaDuplicaIstanza;
    mdRef.componentInstance.msgContenuto =  idIstanza;
    mdRef.result.then(
      (result) => {
        this.moonfoblService.confermaDuplicaIstanza(idIstanza);
      }
    );
  }

}
