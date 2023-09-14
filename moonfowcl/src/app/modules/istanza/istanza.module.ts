/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {CommonModule} from '@angular/common';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {WebSocketEpayNotificaService} from '../../services/web-socket-epay-notifica.service';
import {IstanzaAllegatiComponent} from './components/istanza-page/istanza-container/istanza-allegati/istanza-allegati.component';
import {IstanzaContainerComponent} from './components/istanza-page/istanza-container/istanza-container.component';
import {IstanzaDettaglioComponent} from './components/istanza-page/istanza-container/istanza-dettaglio/istanza-dettaglio.component';
import { IstanzaDocumentiComponent } from './components/istanza-page/istanza-container/istanza-documenti/istanza-documenti.component';
import { IstanzaStoricoComponent } from './components/istanza-page/istanza-container/istanza-storico/istanza-storico.component';
import {
  NotificaPagamentoComponent
} from './components/istanza-page/istanza-container/pagamenti/notifica-pagamento/notifica-pagamento.component';
import { PagamentiTabComponent } from './components/istanza-page/istanza-container/pagamenti/pagamenti-tab/pagamenti-tab.component';
import { PagamentiComponent } from './components/istanza-page/istanza-container/pagamenti/pagamenti.component';
import { IstanzaPageComponent } from './components/istanza-page/istanza-page.component';
import { AltreOperazioniComponent } from './components/istanza-page/istanza-container/altre-operazioni/altre-operazioni.component';

@NgModule({
  declarations: [
    IstanzaPageComponent,
    IstanzaContainerComponent,
    IstanzaStoricoComponent,
    IstanzaAllegatiComponent,
    IstanzaDettaglioComponent,
    PagamentiComponent,
    PagamentiTabComponent,
    NotificaPagamentoComponent,
    IstanzaDocumentiComponent,
    AltreOperazioniComponent],
  imports: [
    CommonModule,
    FontAwesomeModule,
    RouterModule,
    NgbModule
  ],
  exports: [IstanzaPageComponent],
  providers: [WebSocketEpayNotificaService],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA,
    NO_ERRORS_SCHEMA
  ]
})
export class IstanzaModule { }
