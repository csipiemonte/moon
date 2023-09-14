/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UtentiPageComponent } from './components/utenti-page/utenti-page.component';
import { UtentiContainerComponent } from './components/utenti-page/utenti-container/utenti-container.component';
import { UtentiTabellaComponent } from './components/utenti-page/utenti-container/utenti-tabella/utenti-tabella.component';
import { UtentiRigaComponent } from './components/utenti-page/utenti-container/utenti-tabella/utenti-riga/utenti-riga.component';
import { DettaglioUtenteComponent } from './components/utenti-page/utenti-container/dettaglio-utente/dettaglio-utente.component';
import { UtentiCreaComponent } from './components/utenti-page/utenti-container/utenti-crea/utenti-crea.component';
import { DatiGeneraliComponent } from './components/utenti-page/utenti-container/dettaglio-utente/dati-generali/dati-generali.component';
import { AreeRuoloComponent } from './components/utenti-page/utenti-container/dettaglio-utente/aree-ruolo/aree-ruolo.component';
import { ModuliComponent } from './components/utenti-page/utenti-container/dettaglio-utente/moduli/moduli.component';


@NgModule({
  declarations: [
    UtentiPageComponent,
    UtentiContainerComponent,
    UtentiTabellaComponent,
    UtentiRigaComponent,
    DettaglioUtenteComponent,
    UtentiCreaComponent,
    DatiGeneraliComponent,
    AreeRuoloComponent,
    ModuliComponent],
  imports: [
    CommonModule
  ],
  exports: [UtentiPageComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA,
    NO_ERRORS_SCHEMA
  ]
})
export class UtentiModule { }
