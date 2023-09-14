/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgEventBus} from 'ng-event-bus';
import {AlertTypeDesc} from 'src/app/common/alert-type-desc';
import {STATI, STORAGE_KEYS} from 'src/app/common/costanti';
import {environment} from 'src/environments/environment';
import {Messaggi} from '../../../../common/messaggi';
import {Costanti} from 'src/app/common/costanti';
import {CriterioRicercaIstanze} from '../../../../model/dto/criterio-ricerca-istanze';
import { Istanza } from '../../../../model/dto/istanza';
import { SessioneIstanze } from '../../../../model/dto/sessione-istanze';
import { MoonfoblService } from '../../../../services/moonfobl.service';
import { SharedService } from '../../../../services/shared.service';
import { ModalBasicComponent } from '../../../modal-basic/modal-basic.component';
import { StorageManager } from 'src/app/util/storage-manager';

@Component({
  selector: 'app-tab-integrazione',
  templateUrl: './tab-integrazione.component.html',
  styleUrls: ['./tab-integrazione.component.scss']
})
export class TabIntegrazioneComponent implements OnInit {

  @Input() tab;
  istanzeRichiestaIntegrazione: Istanza[];

  righeIstanzaIntegrazione: Istanza[] = [];
  pageSizeIntegrazione = environment.pageSize;
  currPageIntegrazione: number;
  msg = '';
  sessioneIstanze: SessioneIstanze;
  istanzeTotali: number;
  criterioRicercaIstanzeIntegrazione: CriterioRicercaIstanze;

  constructor(private moonfoblService: MoonfoblService,
              private _modalService: NgbModal,
              private sharedService: SharedService,
              private eventBus: NgEventBus
  ) {
    this.criterioRicercaIstanzeIntegrazione = new CriterioRicercaIstanze();
    this.criterioRicercaIstanzeIntegrazione.idTabFo = Costanti.ID_TAB_BOZZE;
    this.criterioRicercaIstanzeIntegrazione.sort = '-dataCreazione';
    this.criterioRicercaIstanzeIntegrazione.statiIstanza = [STATI.IN_ATTESA_INTEGRAZIONE, STATI.IN_ATTESA_OSSERVAZIONI];
  }


  get getOffset(): number {
    return (this.currPageIntegrazione - 1) * environment.pageSize;
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
            const idx = this.righeIstanzaIntegrazione.indexOf(rigaIstanza);
            console.log(' Indice ' + idx);
            this.righeIstanzaIntegrazione.splice(idx, 1);
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
    this.pageSizeIntegrazione = environment.pageSize;

    this.sessioneIstanze = StorageManager.get(STORAGE_KEYS.ISTANZE_BOZZA);
    this.sharedService.activeTab = this.tab;
    if (this.sharedService.page) {
      this.currPageIntegrazione = this.sharedService.page;
    } else {
      this.currPageIntegrazione = 1;
    }

    this.getIstanzePaginate();

    console.log('Current Page: ' + this.currPageIntegrazione);

  }

  getIstanze() {
    this.moonfoblService.getElencoIstanzeFiltratoEOrdinato(null,null, Costanti.ID_TAB_BOZZE, null, '-dataCreazione').subscribe(righe => {
      this.righeIstanzaIntegrazione = righe;
      if (this.righeIstanzaIntegrazione.length === 0) {
        this.msg = 'Nessuna istanza trovata !';
        this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.INFO} );
      }
    });
  }

  getIstanzePaginate() {

    this.moonfoblService.cercaIstanzePaginato(this.criterioRicercaIstanzeIntegrazione, this.getOffset, this.getLimit).subscribe((risposta) => {
        this.istanzeTotali = risposta.totalElements;
        this.righeIstanzaIntegrazione = risposta.items;
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
    this.currPageIntegrazione = $event;
    this.getIstanzePaginate();
  }

}
