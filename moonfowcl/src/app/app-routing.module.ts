/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NuovaIstanzaComponent} from './components/core/nuova-istanza/nuova-istanza.component';
import {NotificheComponent} from './components/core/notifiche/notifiche.component';
import {IstanzeComponent} from './components/core/istanze/istanze.component';
import {ErrorComponent} from './components/error/error.component';
import {HomeComponent} from './components/core/home/home.component';
import {ManageFormComponent} from './components/core/manage-form/manage-form.component';
import {CategorieComponent} from './components/core/categorie/categorie.component';
import {NotificaInvioModuloComponent} from './components/core/notifica-invio-modulo/notifica-invio-modulo.component';
import {NotificaSegnalazioneComponent} from './components/core/notifica-segnalazione/notifica-segnalazione.component';
import {RouteGuardService} from './services/route-guard.service';
import {CompletaAzioneComponent} from './components/core/istanze/completa-azione/completa-azione.component';
import {NotificaStatoCompletatoComponent} from './components/core/notifica-stato-completato/notifica-stato-completato.component';
import {DettaglioWorkflowComponent} from './components/core/dettaglio-workflow/dettaglio-workflow.component';
import {BachecaComponent} from './components/core/bacheca/bacheca.component';
import {ScegliEnteComponent} from './components/core/scegli-ente/scegli-ente.component';
import {ForbiddenComponent} from './components/auth/forbidden/forbidden.component';
import {DevGuardService} from './services/dev-guard.service';
import {UserPasswordComponent} from './components/auth/user-password/user-password.component';
import {IstanzaPageComponent} from './modules/istanza/components/istanza-page/istanza-page.component';
import {GoogleComponent} from './components/auth/google/google.component';
import {AiutoComponent} from './components/static/aiuto/aiuto.component';
import {CookieComponent} from './components/static/cookie/cookie.component';
import {PrivacyComponent} from './components/static/privacy/privacy.component';
import {AccessibilitaComponent} from './components/static/accessibilita/accessibilita.component';
import {EmbeddedComponent} from './components/local/embedded/embedded.component';
import {ShibbolethComponent} from './components/local/shibboleth/shibboleth.component';
import {ComunicazioniPageComponent} from './modules/comunicazioni/components/comunicazioni-page/comunicazioni-page.component';
import {ConsumerGuardService} from './services/consumer-guard.service';

/*
   Child routes
 */
const childs: Routes = [

  {
    path: 'auth',
    children: [
      {path: 'user-pwd', component: UserPasswordComponent},
      {path: 'social', component: GoogleComponent},
      {path: 'embedded', component: EmbeddedComponent, canActivate: [DevGuardService]}
    ]
  },
  {
    path: 'local',
    children: [
      {path: 'iride', component: ShibbolethComponent, canActivate: [DevGuardService]},
      {path: 'embedded/istanze/:codiceIstanza/:service/:token', component: EmbeddedComponent, canActivate: [DevGuardService]},
      {path: 'embedded/istanze/:codiceIstanza/:token', component: EmbeddedComponent, canActivate: [DevGuardService]},
      {path: 'embedded/moduli/:codiceModulo/:service/:token', component: EmbeddedComponent, canActivate: [DevGuardService]},
      {path: 'embedded/elenco-istanze/:service/:token', component: EmbeddedComponent, canActivate: [DevGuardService]},
      {path: 'embedded', component: EmbeddedComponent, canActivate: [DevGuardService]}
    ]
  },
  {
    path: 'emb',
    children: [
      {path: 'istanza/:id', component: IstanzaPageComponent, canActivate: [RouteGuardService]},
      {path: 'manage-form/:command/:idIstanza', component: ManageFormComponent, canActivate: [RouteGuardService]},
      {path: 'manage-form/:command/:idModulo/:idVersioneModulo', component: ManageFormComponent, canActivate: [RouteGuardService]},
    ]
  },
  {
    path: 'emb',
    component: HomeComponent,
    canActivate: [RouteGuardService],
    children: [
      {path: 'istanze', component: IstanzeComponent, canActivate: [RouteGuardService]}
    ]
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [RouteGuardService],
    children: [
      {path: 'elenco-moduli', component: NuovaIstanzaComponent, canActivate: [RouteGuardService]},
      {path: 'categorie', component: CategorieComponent, canActivate: [RouteGuardService]},
      {path: 'istanze', component: IstanzeComponent, canActivate: [RouteGuardService]},
      {path: 'bacheca', component: BachecaComponent, canActivate: [RouteGuardService,ConsumerGuardService]},
      /*{ path: 'cerca-istanza', component: CercaIstanzaComponent, canActivate: [RouteGuardService] },*/
      {path: 'notifiche', component: NotificheComponent, canActivate: [RouteGuardService]},
      {path: 'notifica-invio-modulo', component: NotificaInvioModuloComponent, canActivate: [RouteGuardService]},
      {path: 'notifica-stato-completato', component: NotificaStatoCompletatoComponent, canActivate: [RouteGuardService]},
      {path: 'notifica-segnalazione', component: NotificaSegnalazioneComponent, canActivate: [RouteGuardService]},
      // {path: 'richiesta-integrazione/:id', component: CompletaAzioneComponent},
      {path: 'istanze/:id/dettaglio-workflow/:idw', component: DettaglioWorkflowComponent, canActivate: [RouteGuardService]},
      {path: 'istanze/:id/completa-azione/:idw', component: CompletaAzioneComponent, canActivate: [RouteGuardService]},
      {path: 'istanza/:id', component: IstanzaPageComponent, canActivate: [RouteGuardService]},
      {path: 'comunicazioni', component: ComunicazioniPageComponent, canActivate: [RouteGuardService]},
      {path: 'scegli-ente', component: ScegliEnteComponent, canActivate: [RouteGuardService]}
    ]
  }
];

/*
   Base routes
 */
const routes: Routes = [
  { path: 'home', component: HomeComponent, canActivate: [RouteGuardService,ConsumerGuardService]},
  { path: 'manage-form/:command/:idIstanza', component: ManageFormComponent, canActivate: [RouteGuardService] },
  { path: 'istanza/:id', component: IstanzaPageComponent, canActivate: [RouteGuardService] },
  { path: 'manage-form/:command/:idModulo/:idVersioneModulo', component: ManageFormComponent, canActivate: [RouteGuardService] },
  { path: 'notifiche', component: NotificheComponent, canActivate: [RouteGuardService] },
  { path: 'forbidden', component: ForbiddenComponent },
  { path: 'error', component: ErrorComponent, canActivate: [RouteGuardService] },
  { path: 'aiuto', component: AiutoComponent, canActivate: [RouteGuardService,ConsumerGuardService] },
  { path: 'cookie', component: CookieComponent, canActivate: [RouteGuardService,ConsumerGuardService] },
  { path: 'accessibilita', component: AccessibilitaComponent, canActivate: [RouteGuardService,ConsumerGuardService] },
  { path: 'privacy', component: PrivacyComponent, canActivate: [RouteGuardService,ConsumerGuardService] }
];


@NgModule({
  imports: [RouterModule.forRoot(routes, {relativeLinkResolution: 'legacy', scrollPositionRestoration: "enabled"}), RouterModule.forChild(childs)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
