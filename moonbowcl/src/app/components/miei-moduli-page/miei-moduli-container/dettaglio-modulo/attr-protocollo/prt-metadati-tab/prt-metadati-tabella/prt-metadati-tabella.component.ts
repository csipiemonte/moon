/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-protocollo-metadati-tab-view-tabella',
  templateUrl: './prt-metadati-tabella.component.html',
  styleUrls: ['./prt-metadati-tabella.component.scss']
})
export class PrtMetadatiTabellaComponent implements OnInit, OnChanges {

  @Input() metadati: any;

  metadatiKeys: string[];

  constructor() { }

  ngOnInit(): void {
    log('prt-metadati-tabella::ngOnInit() this.metadati = ' + this.metadati);
    this.initMetadati();
  }

  ngOnChanges(changes: SimpleChanges) {
    log('pprt-metadati-tabella::ngOnChanges() this.metadati = ' + this.metadati);
    this.initMetadati();
  }

  initMetadati(): void {
    log('prt-metadati-tabellab::initMetadati() this.metadati = ' + JSON.stringify(this.metadati));
    if (this.metadati) {
      this.metadatiKeys = Object.keys(this.metadati);
    }
  }

}

function log(a: any) {
  console.log(a);
}