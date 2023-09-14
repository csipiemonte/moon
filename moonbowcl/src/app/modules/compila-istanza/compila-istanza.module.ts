/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CompilaIstanzaComponent } from './components/compila-istanza/compila-istanza.component';
import { NgbModule, NgbNavModule } from '@ng-bootstrap/ng-bootstrap';
import { AutocompleteLibModule } from 'angular-ng-autocomplete';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CategoriaComponent } from './components/compila-istanza/categorie/categoria/categoria.component';
import { CategorieComponent } from './components/compila-istanza/categorie/categorie.component';
import { NuovaIstanzaComponent } from './components/compila-istanza/nuova-istanza/nuova-istanza.component';
import { CustomPipesModule } from '../custom-pipes/custom-pipes.module';
// fixme  import { FormManagerModule } from '@formio/angular/manager';
import { RouterModule } from '@angular/router';
import { MoonSpinnerModule } from '../moon-spinner/moon-spinner.module';
import { NotificaInvioModuloComponent } from './components/compila-istanza/notifica-invio-modulo/notifica-invio-modulo.component';
import { NotificaStatoCompletatoComponent } from './components/compila-istanza/notifica-stato-completato/notifica-stato-completato.component';
import { CompilaOperatoreFormComponent } from './components/compila-istanza/forms/compila-operatore-form/compila-operatore-form.component';
import { AlertModule } from '../alert';

@NgModule({
    declarations: [CompilaIstanzaComponent, CategorieComponent, CategoriaComponent,
        NuovaIstanzaComponent, NotificaInvioModuloComponent, NotificaStatoCompletatoComponent, CompilaOperatoreFormComponent],
    imports: [
        CommonModule,
        NgbNavModule,
        AutocompleteLibModule,
        FontAwesomeModule,
        ReactiveFormsModule,
        //fixme    FormManagerModule,
        FormsModule,
        CustomPipesModule,
        RouterModule,
        MoonSpinnerModule,
        NgbModule,
        AlertModule
    ],
    exports: [CompilaIstanzaComponent],
    //providers:[NgxSpinnerService],
    schemas: [
        CUSTOM_ELEMENTS_SCHEMA,
        NO_ERRORS_SCHEMA
    ]
})
export class CompilaIstanzaModule { }
