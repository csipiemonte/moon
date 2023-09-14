/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CercaIstanzaComponent } from '../app/components/cerca-istanza/cerca-istanza.component';
import { NotificheComponent } from '../app/components/notifiche/notifiche.component';
import { TestPageComponent } from './test-page.component';
import { ViewFormComponent } from './components/view-form/view-form.component';
import { IstanzeComponent } from './components/istanze/istanze.component';
import { CompletaAzioneComponent } from './components/completa-azione/completa-azione.component';
import { ReportComponent } from './components/report/report.component';
import { DettaglioWorkflowComponent } from './components/dettaglio-workflow/dettaglio-workflow.component';
import { ExportIstanzeComponent } from './components/export-istanze/export-istanze.component';
import { MieiModuliPageComponent } from './components/miei-moduli-page/miei-moduli-page.component';
import { ScegliEnteComponent } from './components/scegli-ente/scegli-ente.component';
import { AppComponent } from './app.component';
import { ProfiloComponent } from './components/profilo/profilo.component';
import { UtentiPageComponent } from './modules/utenti/components/utenti-page/utenti-page.component';
//import { CategorieComponent } from './components/categorie/categorie.component';
import { CompilaIstanzaComponent } from './modules/compila-istanza/components/compila-istanza/compila-istanza.component';
import { CategorieComponent } from './modules/compila-istanza/components/compila-istanza/categorie/categorie.component';
import { NuovaIstanzaComponent } from './modules/compila-istanza/components/compila-istanza/nuova-istanza/nuova-istanza.component';
import { NotificaInvioModuloComponent } from './modules/compila-istanza/components/compila-istanza/notifica-invio-modulo/notifica-invio-modulo.component';
import { NotificaStatoCompletatoComponent } from './modules/compila-istanza/components/compila-istanza/notifica-stato-completato/notifica-stato-completato.component';
import { NotificaSegnalazioneComponent } from './components/notifica-segnalazione/notifica-segnalazione.component';
import { IstanzaPageComponent } from './modules/istanza/components/istanza-page/istanza-page.component';
import { IstanzaContainerComponent } from './modules/istanza/components/istanza-page/istanza-container/istanza-container.component';
import { NotificaModificaComponent } from './components/notifica-modifica/notifica-modifica.component';
import { UserPasswordComponent } from './components/auth/user-password/user-password.component';
import { RouteGuardService } from './services/route-guard.service';
import { ShibbolethComponent } from './components/local/shibboleth/shibboleth.component';
import {ProcessiPageComponent} from './modules/processi/components/processi-page/processi-page.component';

const childs: Routes = [
  {
    path: 'local',
    children: [
      {path: 'iride', component: ShibbolethComponent}
    ]
  } 
];

const routes: Routes = [
  { path: '', redirectTo: 'istanze', pathMatch: 'full' },
  { path: 'istanze', component: IstanzeComponent , canActivate: [RouteGuardService]},
  { path: 'istanze/view-form/:id', component: ViewFormComponent , canActivate: [RouteGuardService]},
  { path: 'istanzebozza/view-form/:id', component: ViewFormComponent , canActivate: [RouteGuardService]},
  { path: 'modificaistanza/view-form/:id', component: ViewFormComponent , canActivate: [RouteGuardService]},
  { path: 'istanzebozza-da-inviare/view-form/:id', component: ViewFormComponent , canActivate: [RouteGuardService]},
  { path: 'istanza/:id', component: IstanzaContainerComponent , canActivate: [RouteGuardService]},
  { path: 'istanze/:id/completa-azione/:idw', component: CompletaAzioneComponent , canActivate: [RouteGuardService]},
  { path: 'istanze/notifica-modifica/:id', component: NotificaModificaComponent , canActivate: [RouteGuardService]},
  { path: 'istanze/:id/dettaglio-workflow/:idw', component: DettaglioWorkflowComponent , canActivate: [RouteGuardService]},
  { path: 'nuova-istanza', component: NuovaIstanzaComponent , canActivate: [RouteGuardService]},
  { path: 'nuova-istanza/view-form/:idModulo/:idVersioneModulo', component: ViewFormComponent , canActivate: [RouteGuardService]},
  { path: 'categorie', component: CategorieComponent , canActivate: [RouteGuardService]},
  { path: 'cerca-istanza', component: CercaIstanzaComponent , canActivate: [RouteGuardService]},
  { path: 'notifiche', component: NotificheComponent , canActivate: [RouteGuardService]},
  { path: 'report', component: ReportComponent , canActivate: [RouteGuardService]},
  { path: 'export-istanze', component: ExportIstanzeComponent , canActivate: [RouteGuardService]},
  { path: 'mieiModuli', component: MieiModuliPageComponent , canActivate: [RouteGuardService]},
  { path: 'testpage', component: TestPageComponent , canActivate: [RouteGuardService]},
  { path: 'utenti', component: UtentiPageComponent , canActivate: [RouteGuardService]},
  { path: 'scegli-ente', component: ScegliEnteComponent , canActivate: [RouteGuardService]},
  { path: 'profilo', component: ProfiloComponent , canActivate: [RouteGuardService]},
  { path: 'notifica-invio-modulo/:caller', component: NotificaInvioModuloComponent , canActivate: [RouteGuardService]},
  { path: 'notifica-stato-completato/:caller', component: NotificaStatoCompletatoComponent , canActivate: [RouteGuardService]},
  { path: 'notifica-segnalazione', component: NotificaSegnalazioneComponent , canActivate: [RouteGuardService]},
  { path: 'auth/user-pwd',component: UserPasswordComponent },
  { path: 'processi', component: ProcessiPageComponent , canActivate: [RouteGuardService]}
];


@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: true, relativeLinkResolution: 'legacy' }),RouterModule.forChild(childs)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
