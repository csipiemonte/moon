/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {faCogs, faEye} from '@fortawesome/free-solid-svg-icons';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {STATI} from 'src/app/common/costanti';
import {Messaggi} from 'src/app/common/messaggi';
import {NavSelection} from 'src/app/common/nav-selection';
import {ModalBasicComponent} from 'src/app/components/modal-basic/modal-basic.component';
import {saveBlobIE} from 'src/app/services/service.utils';
import { Istanza } from '../../../../../model/dto/istanza';
import { MoonfoblService } from '../../../../../services/moonfobl.service';
import { SharedService } from '../../../../../services/shared.service';

@Component({

  selector: 'tr[riga-istanza]',
  templateUrl: './riga-istanza.component.html',
  styleUrls: ['./riga-istanza.component.scss']
})
export class RigaIstanzaComponent implements OnInit {

  faCogs = faCogs;
  faEye = faEye;
  STATI = STATI;

  inPagamento: boolean = false;

  // riga istanza passata in input
  @Input('riga-istanza-data') rigaIstanza: Istanza;
  // pagina corrente
  @Input('currPage') currPage: number;
  // evento richeista eliminazione istanza
  @Output('onDeleteRiga') rigaIstanzaDeleted = new EventEmitter();

  constructor(
    private moonfoblService: MoonfoblService,
    private router: Router,
    private modalService: NgbModal,
    private sharedService: SharedService) { }

  // evento richiesta cambio importanza
  @Output('onCambiaImportanza') rigaIstanzaCambiaImportanza = new EventEmitter();

  ngOnInit() {
    this.inPagamento = this.rigaIstanza.stato.idStato === STATI.IN_PAGAMENTO  ||  this.rigaIstanza.stato.idStato === STATI.ATTESA_RICEVUTA_PAGAMENTO  ||  this.rigaIstanza.stato.idStato === STATI.IN_PAGAMENTO_ONLINE  || this.rigaIstanza.stato.idStato === STATI.ATTESA_PAGAMENTO;
  }

  mostraDettaglioIstanza() {
    this.router.navigate(['/home/istanza/' + this.rigaIstanza.idIstanza]);
  }

  modificaIstanza() {
    this.sharedService.page = this.currPage;
    this.router.navigate(['/manage-form', 'UPDATE', this.rigaIstanza.idIstanza]);
  }

  cancellaIstanza() {
    // emette evento rigaIstanzaDeleted visto all'esterno come onDeleteRiga
    this.rigaIstanzaDeleted.emit(this.rigaIstanza);
  }

  modificaImportanza($event) {
    $event.preventDefault();
    // emette evento rigaIstanzaCambiaImportanza visto all'esterno come onCambiaImportanza
    this.rigaIstanzaCambiaImportanza.emit(this.rigaIstanza);
  }

  lavoraIstanza() {

    if (this.rigaIstanza.stato.idStato === STATI.DA_INVIARE){
      this.router.navigate(['/home/istanza/' + this.rigaIstanza.idIstanza], { state: { caller: NavSelection.ISTANZE }} );
    }else{
      this.router.navigate(['/home/istanza/' + this.rigaIstanza.idIstanza]);
    }

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
