/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MoonSpinnerModule } from '../moon-spinner/moon-spinner.module';
import { AlertModule } from '../alert/alert.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IstanzaPageComponent } from './components/istanza-page/istanza-page.component';
import { IstanzaContainerComponent } from './components/istanza-page/istanza-container/istanza-container.component';
import { IstanzaStoricoComponent } from './components/istanza-page/istanza-container/istanza-storico/istanza-storico.component';
import { IstanzaAllegatiComponent } from './components/istanza-page/istanza-container/istanza-allegati/istanza-allegati.component';
import { LogEmailComponent } from './components/istanza-page/istanza-container/log-email/log-email.component';
import { LogCosmoComponent } from './components/istanza-page/istanza-container/log-cosmo/log-cosmo.component';
import { PagamentiComponent } from './components/istanza-page/istanza-container/pagamenti/pagamenti.component';
import { PagamentiTabComponent } from './components/istanza-page/istanza-container/pagamenti/pagamenti-tab/pagamenti-tab.component';
import { IstanzaDettaglioComponent } from './components/istanza-page/istanza-container/istanza-dettaglio/istanza-dettaglio.component';
import { LogServizioCosmoComponent } from './components/istanza-page/istanza-container/log-servizio-cosmo/log-servizio-cosmo.component';
import { OperazioniAvanzateComponent } from './components/istanza-page/istanza-container/operazioni-avanzate/operazioni-avanzate.component';
import { LogMydocsComponent } from './components/istanza-page/istanza-container/log-mydocs/log-mydocs.component';
import { LogTicketComponent } from './components/istanza-page/istanza-container/log-ticket/log-ticket.component';

@NgModule({
  declarations: [
    IstanzaPageComponent,
    IstanzaContainerComponent,
    IstanzaStoricoComponent,
    IstanzaAllegatiComponent,
    LogEmailComponent,
    LogCosmoComponent,
    LogServizioCosmoComponent,
    PagamentiComponent,
    PagamentiTabComponent,
    IstanzaDettaglioComponent,
    OperazioniAvanzateComponent,
    LogMydocsComponent,
    LogTicketComponent
  ],
  imports: [
    CommonModule,
    FontAwesomeModule,
    RouterModule,
    MoonSpinnerModule,
    NgbModule,
    AlertModule
  ],
  exports: [IstanzaPageComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA,
    NO_ERRORS_SCHEMA
  ]
})
export class IstanzaModule { }
