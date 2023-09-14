/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {NgEventBus} from 'ng-event-bus';
import {AlertTypeDesc} from 'src/app/common/alert-type-desc';
import {environment} from 'src/environments/environment';
import {Costanti} from 'src/app/common/costanti';
import {Istanza} from '../../../../model/dto/istanza';
import {MoonfoblService} from '../../../../services/moonfobl.service';
import {SharedService} from '../../../../services/shared.service';

@Component({
  selector: 'app-tab-importanti',
  templateUrl: './tab-importanti.component.html',
  styleUrls: ['./tab-importanti.component.scss']
})
export class TabImportantiComponent implements OnInit {
  @Input() tab;

  righeIstanzaImportanti: Istanza[] = [];
  pageSizeBozza = environment.pageSize;
  currPageBozza: number;
  msg = null;

  constructor(private moonfoblService: MoonfoblService,
              private sharedService: SharedService,
              private eventBus: NgEventBus) {
  }

  ngOnChanges(val) {
    this.eventBus.cast('alert:set', { clear: true } );
  }

  ngOnInit() {

    this.pageSizeBozza = environment.pageSize;
    this.getIstanze();
    this.sharedService.activeTab = this.tab;
    if (this.sharedService.page) {
      this.currPageBozza =  this. sharedService.page;
    } else {
      this.currPageBozza = 1;
    }
    console.log('Current Page: ' + this.currPageBozza);

  }

  onDeleteRiga(rigaIstanza: Istanza) {
    // elimino elemento
    const idx = this.righeIstanzaImportanti.indexOf(rigaIstanza);
    console.log(' Indice ' + idx);
    this.righeIstanzaImportanti.splice(idx, 1);
    console.log('elimino riga ' + rigaIstanza.idIstanza);

    // Gestione nessun record
    if (this.righeIstanzaImportanti.length === 0  ) {
      this.msg = 'Nessun elemento presente !';
      this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.INFO} );
    }
  }

  onCambiaImportanza(rigaIstanza: Istanza) {
    // console.log('onCambiaImportanza id=' + rigaIstanza.idIstanza);
    // console.log('onCambiaImportanza imp=' + rigaIstanza.importanza);

    // elimino importanza
    this.moonfoblService.cambiaImportanzaIstanza(rigaIstanza.idIstanza, 0).subscribe( response => {
      const respIstanza = response as Istanza;
      rigaIstanza.importanza = respIstanza.importanza;

      // elimino elemento
      const idx = this.righeIstanzaImportanti.indexOf(rigaIstanza);
      console.log(' Indice ' + idx);
      this.righeIstanzaImportanti.splice(idx, 1);
      console.log('elimino riga ' + rigaIstanza.idIstanza);

      // Gestione nessun record
      if (this.righeIstanzaImportanti.length === 0) {
        this.msg = 'Nessun elemento presente !';
        // this.alert.emit({ text: this.msg, type: AlertTypeDesc.INFO });
        this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.INFO} );
      }

    });

  }

  getIstanze() {
    this.moonfoblService.getElencoIstanzeFiltratoEOrdinato(null,null, null, Costanti.ISTANZA_IMPORTANTE_STAR, '-dataCreazione').subscribe(righe => {
        this.righeIstanzaImportanti = righe;
        if (this.righeIstanzaImportanti.length === 0) {
          this.msg = 'Nessuna istanza trovata !';
          this.eventBus.cast('alert:set', { text: this.msg, type: AlertTypeDesc.INFO} );
        }
      }
    );
  }

  incPage() {
    this.currPageBozza = this.currPageBozza + 1;
    console.log('Valore dopo incremento ' + this.currPageBozza);
  }

  pageChanged($event) {
    console.log($event);
    this.sharedService.page = $event;
  }
}
