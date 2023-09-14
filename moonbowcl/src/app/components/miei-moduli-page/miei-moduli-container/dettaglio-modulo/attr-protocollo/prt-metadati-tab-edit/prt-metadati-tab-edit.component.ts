/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit, SimpleChanges, OnChanges } from '@angular/core';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { Modulo } from 'src/app/model/dto/modulo';
import { ProtocolloMetadato } from 'src/app/model/dto/protocollo-metadato';
import { AlertService } from 'src/app/modules/alert';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
@Component({
  selector: 'app-miei-moduli-dettaglio-attr-protocollo-metadati-tab-edit',
  templateUrl: './prt-metadati-tab-edit.component.html',
  styleUrls: ['./prt-metadati-tab-edit.component.scss']
})
export class PrtMetadatiTabEditComponent implements OnInit, OnChanges {

  @Input() modulo: Modulo;
  @Input() maMetadati: ModuloAttributo;

  moduloPrtMetadati: any;
  hasModuloPrtMetadati = false;
  navPrtMetadatiActive: string;
  formChangeId = 0;

  title: any = {
    'ISTANZA': 'Istanza',
    'ISTANZA_ALLEGATO': 'Allegato',
    'RICEVUTA': 'Ricevuta'
  };
  titleKeys: string[];

  metadatiFull: ProtocolloMetadato[];

  alertOptions = {
    id: 'alert-utenti-area-ruolo',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(
    private moonboblService: MoonboblService,
    private sharedService: SharedService,
    protected alertService: AlertService) {
    this.titleKeys = Object.keys(this.title);
  }

  ngOnInit(): void {
    log('prt-metadati-tab-edit::ngOnInit() this.maMetadati = ' + this.maMetadati);
    this.initFullMetadati();
    this.initMetadati();
  }

  ngOnChanges(changes: SimpleChanges) {
    log('prt-metadati-tab-edit::ngOnChanges() this.maMetadati = ' + this.maMetadati);
    this.initFullMetadati();
    this.initMetadati();
  }

  initMetadati(): void {
    log('prt-metadati-tab-edit::initMetadati() this.maMetadati = ' + JSON.stringify(this.maMetadati));
    log('prt-metadati-tab-edit::initMetadati() this.maMetadati.valore = ' + this.maMetadati.valore);
    if (this.maMetadati && this.maMetadati.valore && 'N' !== this.maMetadati.valore) {
      this.hasModuloPrtMetadati = true;
      this.moduloPrtMetadati = JSON.parse(this.maMetadati.valore);
      log('prt-metadati-tab-edit::initMetadati() this.moduloPrtMetadati = ' + JSON.stringify(this.moduloPrtMetadati));
      if (!this.navPrtMetadatiActive && this.moduloPrtMetadati && Array.isArray(this.moduloPrtMetadati)) {
        this.navPrtMetadatiActive = this.moduloPrtMetadati[0].type;
      }
      log('prt-metadati-tab::initMetadati() this.navPrtMetadatiActive = ' + this.navPrtMetadatiActive);
    } else {
      this.hasModuloPrtMetadati = false;
    }
  }

  initFullMetadati(): void {
    log('prt-metadati-tab-edit::initFullMetadati()');
    if (!this.metadatiFull) {
      this.moonboblService.getProtocolloMetadati().subscribe({
        'next': (res) => {
          this.metadatiFull = res;
          log('prt-metadati-tab-edit::initFullMetadati() moonboblService.getAreeByIdEnte() this.areeEnte = '
            + JSON.stringify(this.metadatiFull));
        },
        'error': (err) => {
          this.alertService.error('Errore di inizializzazione (areeEnte) ! ' + err.errorMsg, this.alertOptions);
          log('prt-metadati-tab-edit::initFullMetadati() Errore di inizializzazione (areeEnte) ! ' + err.errorMsg);
        }
      });
    }
  }

  onActiveIdChange(newActiveId: any) {
    log('prt-metadati-tab-edit::onActiveIdChange() newActiveId = ' + newActiveId);
  }

  onFormChange(formChangeId: any) {
    log('prt-metadati-tab-edit::onFormChange() formChangeId = ' + formChangeId);
    this.formChangeId = formChangeId;
  }

  onFormSave(maMetadatiSaved: any) {
    log('prt-metadati-tab-edit::onFormSave() maMetadatiSaved = ' + maMetadatiSaved);
    this.maMetadati = maMetadatiSaved;
    this.formChangeId = 0;
    this.initMetadati();
  }

  onNavChanged(event) {
    log('prt-metadati-tab-edit::onNavChanged() event=' + event + ' this.formChangeId = ' + this.formChangeId);
    if (this.formChangeId !== 0 && !confirm('Conferma il cambio di pagina, tutte le modifiche della pagina corrente verrano perse ?')) {
      event.preventDefault();
      return;
    }
    this.formChangeId = 0;
  }

  // metadatiOfTab(tipoDocToSearch: string): any {
  //   let result = {};
  //   if (this.moduloPrtMetadati) {
  //     const searchItem = this.moduloPrtMetadati.find((mpm) => mpm.type === tipoDocToSearch);
  //     if (searchItem) {
  //       result = searchItem.metadati;
  //     }
  //   }
  //   log('prt-metadati-tab-edit::metadatiOfTab(' + tipoDocToSearch + ') result = ' + result);
  //   return result;
  // }
}

function log(a: any) {
  console.log(a);
}
