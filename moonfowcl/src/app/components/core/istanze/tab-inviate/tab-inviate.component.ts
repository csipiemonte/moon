/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgEventBus} from 'ng-event-bus';
import {AlertTypeDesc} from 'src/app/common/alert-type-desc';
import {environment} from 'src/environments/environment';
import {Messaggi} from '../../../../common/messaggi';
import {Costanti} from 'src/app/common/costanti';
import {CriterioRicercaIstanze} from '../../../../model/dto/criterio-ricerca-istanze';
import {Istanza} from '../../../../model/dto/istanza';
import { MoonfoblService } from '../../../../services/moonfobl.service';
import { SharedService } from '../../../../services/shared.service';
import { ModalBasicComponent } from '../../../modal-basic/modal-basic.component';

@Component({
  selector: 'app-tab-inviate',
  templateUrl: './tab-inviate.component.html',
  styleUrls: ['./tab-inviate.component.scss']
})
export class TabInviateComponent implements OnInit {
  @Input() tab;
  @Input() istanzeRichiestaIntegrazione: Istanza[];

  righeIstanzaInviate: Istanza[] = [];
  pageSize = environment.pageSize;
  currPage: number;
  msg = '';
  istanzeTotali: number;
  criterioRicercaIstanzeInviate: CriterioRicercaIstanze;

  constructor(private moonfoblService: MoonfoblService,
              private eventBus: NgEventBus,
              private sharedService: SharedService,
              private _modalService: NgbModal) {
    this.criterioRicercaIstanzeInviate = new CriterioRicercaIstanze();
    this.criterioRicercaIstanzeInviate.idTabFo = Costanti.ID_TAB_INVIATE;
    //this.criterioRicercaIstanzeInviate.sort = '-dataCreazione';
    this.criterioRicercaIstanzeInviate.sort = '-i.data_creazione';
  }

  get getOffset(): number {
    return (this.currPage - 1) * environment.pageSize;
  }

  onDeleteRiga(rigaIstanza: Istanza) {
    console.log('elimino riga ' + rigaIstanza.idIstanza);
  }

  onRiportaInBozza(rigaIstanza: Istanza) {

    this.resetMsg();
    const mdRef = this._modalService.open(ModalBasicComponent);
    mdRef.componentInstance.modal_titolo = 'Richiesta conferma';
    mdRef.componentInstance.modal_contenuto = Messaggi.messaggioConfermaRiportaInBozza;
    mdRef.componentInstance.msgContenuto =  rigaIstanza.codiceIstanza ;
    mdRef.result.then(
      (result) => {
        console.log(result);

        this.moonfoblService.riportaIstanzaInBozza(rigaIstanza).subscribe(
          res => {
            const idx = this.righeIstanzaInviate.indexOf(rigaIstanza);
            console.log(' Indice ' + idx);
            this.righeIstanzaInviate.splice(idx, 1);
            this.msg = 'Istanza ' + rigaIstanza.codiceIstanza + '  riportata in bozza';
            this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.SUCCESS} );
          }
        );
      },
      // conferma
      (reason) => {
        this.msg = 'Operazione annullata';
        this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.ERROR} );
      }
    );
  }


  onCambiaImportanza(rigaIstanza: Istanza) {
    // console.log('onCambiaImportanza id=' + rigaIstanza.idIstanza);
    // console.log('onCambiaImportanza imp=' + rigaIstanza.importanza);
    let valueImportanza: number;
    if (rigaIstanza.importanza === 1) {
      valueImportanza = 0;
    } else {
      valueImportanza = 1;
    }
    this.moonfoblService.cambiaImportanzaIstanza(rigaIstanza.idIstanza, valueImportanza).subscribe(response => {
      const respIstanza = response as Istanza;
      rigaIstanza.importanza = respIstanza.importanza;
    });
  }

  get getLimit(): number {
    return environment.pageSize;
  }

  resetMsg() {
    this.msg = null;
    this.eventBus.cast('alert:set', { clear: true});
  }

  ngOnChanges(val) {
    this.eventBus.cast('alert:set', { clear: true});
  }

  ngOnInit() {
    this.pageSize = environment.pageSize;
    this.sharedService.activeTab = this.tab;
    if (this.sharedService.page) {
      this.currPage = this.sharedService.page;
    } else {
      this.currPage = 1;
    }
    this.getIstanzePaginate();
    console.log('Current Page: ' + this.currPage);
  }

  getIstanzePaginate() {

    this.moonfoblService.cercaIstanzePaginato(this.criterioRicercaIstanzeInviate, this.getOffset, this.getLimit).subscribe((risposta) => {
        this.istanzeTotali = risposta.totalElements;
        this.righeIstanzaInviate = risposta.items;
        if (this.istanzeTotali === 0) {
          this.msg = 'Nessuna istanza trovata !';
          this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.INFO} );
        }

      }
    );
  }

  pageChanged($event) {
    console.log($event);
    this.sharedService.page = $event;
    this.currPage = $event;
    this.getIstanzePaginate();
  }
}
