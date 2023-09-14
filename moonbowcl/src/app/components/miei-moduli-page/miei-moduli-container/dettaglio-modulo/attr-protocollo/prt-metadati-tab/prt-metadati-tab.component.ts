/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit, SimpleChanges, OnChanges } from '@angular/core'; 
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-protocollo-metadati-tab',
  templateUrl: './prt-metadati-tab.component.html',
  styleUrls: ['./prt-metadati-tab.component.scss']
})
export class PrtMetadatiTabComponent implements OnInit, OnChanges {

  @Input() maMetadati: ModuloAttributo;

  moduloPrtMetadati: any;
  hasModuloPrtMetadati = false;
  navPrtMetadatiActive: string;

  title: any = {
    'ISTANZA': 'Istanza',
    'ISTANZA_ALLEGATO': 'Allegato',
    'RICEVUTA': 'Ricevuta'
  };

  constructor() { }

  ngOnInit(): void {
    log('prt-metadati-tab::ngOnInit() this.maMetadati = ' + JSON.stringify(this.maMetadati));
    this.initMetadati();
  }

  ngOnChanges(changes: SimpleChanges) {
    log('prt-metadati-tab::ngOnChanges() this.maMetadati = ' + JSON.stringify(this.maMetadati));
    this.initMetadati();
  }

  initMetadati(): void {
    log('prt-metadati-tab::initMetadati() this.maMetadati = ' + JSON.stringify(this.maMetadati));
    log('prt-metadati-tab::initMetadati() this.maMetadati.valore = ' + this.maMetadati.valore);
    if (this.maMetadati && this.maMetadati.valore && 'N' !== this.maMetadati.valore) {
      this.hasModuloPrtMetadati = true;
      this.moduloPrtMetadati = JSON.parse(this.maMetadati.valore);
      log('prt-metadati-tab::initMetadati() this.moduloPrtMetadati = ' + JSON.stringify(this.moduloPrtMetadati));
      if (!this.navPrtMetadatiActive && this.moduloPrtMetadati && Array.isArray(this.moduloPrtMetadati)
        && this.moduloPrtMetadati.length > 0) {
        this.navPrtMetadatiActive = this.moduloPrtMetadati[0].type;
      }
      log('prt-metadati-tab::initMetadati() this.navPrtMetadatiActive = ' + this.navPrtMetadatiActive);
    } else {
      this.hasModuloPrtMetadati = false;
    }
  }

  onActiveIdChange(newActiveId: any) {
    log('prt-metadati-tab::onActiveIdChange() newActiveId=' + newActiveId);
  }
}

function log(a: any) {
  console.log(a);
}
