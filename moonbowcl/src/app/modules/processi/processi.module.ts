/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA } from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { ProcessiPageComponent } from './components/processi-page/processi-page.component';
import { ProcessiContainerComponent } from './components/processi-page/processi-container/processi-container.component';
import { ProcessiTabellaComponent } from './components/processi-page/processi-container/processi-tabella/processi-tabella.component';
import { ProcessiRigaComponent } from './components/processi-page/processi-container/processi-tabella/processi-riga/processi-riga.component';
import { ProcessiCreaComponent } from './components/processi-page/processi-container/processi-crea/processi-crea.component';
import { DettaglioProcessoComponent } from './components/processi-page/processi-container/dettaglio-processo/dettaglio-processo.component';
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AutocompleteLibModule } from 'angular-ng-autocomplete';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MoonSpinnerModule } from '../moon-spinner/moon-spinner.module';
import { AlertModule } from './../alert/alert.module';
import { UiSwitchModule } from 'ngx-ui-switch';
import { CustomPipesModule } from '../custom-pipes/custom-pipes.module';
import { DettaglioWorkflowComponent } from './components/processi-page/processi-container/dettaglio-processo/dettaglio-workflow/dettaglio-workflow.component';


@NgModule({
  declarations: [
    ProcessiPageComponent,
    ProcessiContainerComponent,
    ProcessiTabellaComponent,
    ProcessiRigaComponent,
    ProcessiCreaComponent,
    DettaglioProcessoComponent,
    DettaglioWorkflowComponent
  ],
  imports: [
    CommonModule,
    NgbNavModule,
    AutocompleteLibModule,
    FontAwesomeModule,
    BrowserModule,
    FormsModule,
    MoonSpinnerModule,
    AlertModule,
    NgbModule,
    UiSwitchModule,
    CustomPipesModule
  ],
  exports: [ProcessiPageComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA,
    NO_ERRORS_SCHEMA
  ]
})
export class ProcessiModule { }
