/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgEventBus} from 'ng-event-bus';
import {AlertTypeDesc} from 'src/app/common/alert-type-desc';
import {STATI, STORAGE_KEYS} from 'src/app/common/costanti';
import {Messaggi} from 'src/app/common/messaggi';
import {ModalBasicComponent} from 'src/app/components/modal-basic/modal-basic.component';
import {Costanti} from 'src/app/common/costanti';
import {CriterioRicercaIstanze} from 'src/app/model/dto/criterio-ricerca-istanze';
import { Istanza } from 'src/app/model/dto/istanza';
import { SessioneIstanze } from 'src/app/model/dto/sessione-istanze';
import { MoonfoblService } from 'src/app/services/moonfobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { environment } from 'src/environments/environment';
import { StorageManager } from 'src/app/util/storage-manager';

@Component({
  selector: 'app-tab-pagamenti',
  templateUrl: './tab-pagamenti.component.html',
  styleUrls: ['./tab-pagamenti.component.scss']
})
export class TabPagamentiComponent implements OnInit {

  @Input() tab;

  righeIstanzaInAttesaPagamento: Istanza[] = [];
  pageSizeInAttesaPagamento = environment.pageSize;
  currPageInAttesaPagamento: number;
  msg = '';
  sessioneIstanze: SessioneIstanze;
  istanzeTotali: number;
  criterioRicercaIstanzeInAttesaPagamento: CriterioRicercaIstanze;

  constructor(private moonfoblService: MoonfoblService,
              private _modalService: NgbModal,
              private sharedService: SharedService,
              private eventBus: NgEventBus
  ) {
    this.criterioRicercaIstanzeInAttesaPagamento = new CriterioRicercaIstanze();
    this.criterioRicercaIstanzeInAttesaPagamento.idTabFo = Costanti.ID_TAB_BOZZE;
    this.criterioRicercaIstanzeInAttesaPagamento.sort = '-dataCreazione';
    this.criterioRicercaIstanzeInAttesaPagamento.statiIstanza = [STATI.IN_PAGAMENTO_ONLINE, STATI.ATTESA_RICEVUTA_PAGAMENTO,STATI.IN_PAGAMENTO, STATI.ATTESA_PAGAMENTO, STATI.DA_PAGARE];
  }


  get getOffset(): number {
    return (this.currPageInAttesaPagamento - 1) * environment.pageSize;
  }

  get getLimit(): number {
    return environment.pageSize;
  }

  resetMsg() {
    this.msg = null;
    this.eventBus.cast('alert:set', { clear: true } );
  }

  onDeleteRiga(rigaIstanza: Istanza) {
    this.resetMsg();
    console.log('elimino riga ' + rigaIstanza.idIstanza);
    const mdRef = this._modalService.open(ModalBasicComponent);
    mdRef.componentInstance.modal_titolo = 'Richiesta conferma';
    mdRef.componentInstance.modal_contenuto = Messaggi.messaggioConfermaEliminazione;
    mdRef.componentInstance.msgContenuto = rigaIstanza.codiceIstanza;
    mdRef.result.then(
      (result) => {
        //OK
        console.log(result);

        this.moonfoblService.eliminaIstanza(rigaIstanza).subscribe(
          res => {
            const idx = this.righeIstanzaInAttesaPagamento.indexOf(rigaIstanza);
            console.log(' Indice ' + idx);
            this.righeIstanzaInAttesaPagamento.splice(idx, 1);
            this.msg = 'Istanza ' + rigaIstanza.codiceIstanza + ' eliminata';
            this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.SUCCESS} );

          }
        );
      },
      // conferma
      (reason) => {
        //Cancel
        this.msg = 'Operazione annullata';
        this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.ERROR} );

      }
    );
  }

  onCambiaImportanza(rigaIstanza: Istanza) {
    let impValue: number;
    // console.log('onCambiaImportanza id=' + rigaIstanza.idIstanza);
    // console.log('onCambiaImportanza imp=' + rigaIstanza.importanza);
    if (rigaIstanza.importanza === 1) {
      impValue = 0;
    } else {
      impValue = 1;
    }
    this.moonfoblService.cambiaImportanzaIstanza(rigaIstanza.idIstanza, impValue).subscribe(response => {
      const respIstanza = response as Istanza;
      rigaIstanza.importanza = respIstanza.importanza;
    });
  }

  ngOnChanges(val) {
    // this.alert.emit({ clear: true });
    this.eventBus.cast('alert:set', { clear: true });
  }

  ngOnInit() {
    this.pageSizeInAttesaPagamento = environment.pageSize;

    this.sessioneIstanze = StorageManager.get(STORAGE_KEYS.ISTANZE_BOZZA);
    this.sharedService.activeTab = this.tab;
    if (this.sharedService.page) {
      this.currPageInAttesaPagamento = this.sharedService.page;
    } else {
      this.currPageInAttesaPagamento = 1;
    }

    this.getIstanzePaginate();

    console.log('Current Page: ' + this.currPageInAttesaPagamento);

  }

  getIstanze() {
    this.moonfoblService.getElencoIstanzeFiltratoEOrdinato(null,null, Costanti.ID_TAB_BOZZE, null, '-dataCreazione').subscribe(righe => {
      this.righeIstanzaInAttesaPagamento = righe;
      if (this.righeIstanzaInAttesaPagamento.length === 0) {
        this.msg = 'Nessuna istanza trovata !';
        this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.INFO} );
      }
    });
  }

  getIstanzePaginate() {

    this.moonfoblService.cercaIstanzePaginato(this.criterioRicercaIstanzeInAttesaPagamento, this.getOffset, this.getLimit).subscribe((risposta) => {
        this.istanzeTotali = risposta.totalElements;
        this.righeIstanzaInAttesaPagamento = risposta.items;
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
    this.currPageInAttesaPagamento = $event;
    this.getIstanzePaginate();
  }

}
