/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {faCogs} from '@fortawesome/free-solid-svg-icons';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {STATI} from 'src/app/common/costanti';
import {Messaggi} from 'src/app/common/messaggi';
import {NavSelection} from 'src/app/common/nav-selection';
import {ModalBasicComponent} from 'src/app/components/modal-basic/modal-basic.component';
import {saveBlobIE} from 'src/app/services/service.utils';
import { CriterioRicercaIstanze } from '../../../../model/dto/criterio-ricerca-istanze';
import { Istanza } from '../../../../model/dto/istanza';
import { MoonfoblService } from '../../../../services/moonfobl.service';

@Component({

  selector: 'tr[riga-istanza-cerca]',
  templateUrl: './riga-cerca.component.html',
  styleUrls: ['./riga-cerca.component.scss']
})
export class RigaCercaComponent implements OnInit {

  faCogs = faCogs;
  STATI = STATI;


  //is gruppo operatore
  @Input('is-operatore') isOperatore: boolean;
  // riga istanza passata in input
  @Input('riga-istanza-data') rigaIstanza: Istanza;
  // pagina corrente
  @Input('currPage') currPage: number;
  //criteri ricerca
  @Input('criterioRicerca') criterioRicerca: CriterioRicercaIstanze;
  // evento richeista eliminazione istanza
  @Output('onDeleteRiga') rigaIstanzaDeleted = new EventEmitter();
  // evento richiesta cambio importanza
  @Output('onCambiaImportanza') rigaIstanzaCambiaImportanza = new EventEmitter();

  constructor(
    private moonfoblService: MoonfoblService,
    private modalService: NgbModal,
    private router: Router) { }

  ngOnInit() {
    console.log("ID STATO = "+this.rigaIstanza.stato.idStato);
  }


  // mostraDettaglioIstanza() {
  //   // this.router.navigate() ;  // mostra dettaglio istanza impostare poi read only
  //   // tslint:disable-next-line:max-line-length
  //   this.router.navigate(['/manage-form', 'UPDATE',  this.rigaIstanza.idIstanza]);
  // }

  mostraDettaglioIstanza() {
    this.router.navigate(['/home/istanza/' + this.rigaIstanza.idIstanza]);
  }


  modificaIstanza() {
    this.router.navigate(['/manage-form', 'UPDATE',  this.rigaIstanza.idIstanza]);
  }

  cancellaIstanza() {
    // emette evento rigaIstanzaDeleted visto all'esterno come onDeleteRiga
    this.rigaIstanzaDeleted.emit(this.rigaIstanza);
  }

  modificaImportanza($event) {
    $event.preventDefault();
    this.rigaIstanzaCambiaImportanza.emit(this.rigaIstanza);
  }

  // lavoraIstanza() {
  //   if (this.rigaIstanza.stato.idStato === STATI.DA_INVIARE){
  //     this.router.navigate(['/home/istanza/' + this.rigaIstanza.idIstanza], { state: { caller: NavSelection.CERCA_ISTANZA }} );
  //   }else{
  //     this.router.navigate(['/home/istanza/' + this.rigaIstanza.idIstanza]);
  //   }
  // }

  downloadPdf() {
    this.moonfoblService.getPdf(this.rigaIstanza.idIstanza)
      .subscribe(x => {

        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], {type: 'application/pdf'});
        saveBlobIE(newBlob);
        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /*if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(newBlob);
            return;
        }
        */
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
