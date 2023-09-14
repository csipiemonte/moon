/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule} from '@angular/common/http';
import {APP_INITIALIZER, Injector, LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AppComponent} from './app.component';
import {NuovaIstanzaComponent} from './components/core/nuova-istanza/nuova-istanza.component';
import {CercaIstanzaComponent} from './components/core/cerca-istanza/cerca-istanza.component';
import {NotificheComponent} from './components/core/notifiche/notifiche.component';
import {ConfigService} from './services/config.service';
import {SecurityService} from './services/security.service';
import {AppRoutingModule} from './app-routing.module';
import {MoonfoblService} from './services/moonfobl.service';
// Formio
import {FormioAppConfig, FormioModule} from '@formio/angular';
import {MessageService} from './services/message.service';
import {NgbDateParserFormatter, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {ModalBasicComponent} from './components/modal-basic/modal-basic.component';
import {ModalConfirmComponent} from './components/modal-confirm/modal-confirm.component';
import {RigaIstanzaComponent} from './components/core/istanze/tab-bozze/riga-istanza/riga-istanza.component';
import {AppConfig} from './conf/formio-config';
// tslint:disable-next-line:max-line-length
import {
  RigaIstanzeImportantiComponent
} from './components/core/istanze/tab-importanti/riga-istanza-importante/riga-istanze-importanti.component';
import {RigaIstanzeInviateComponent} from './components/core/istanze/tab-inviate/riga-istanza-inviata/riga-istanze-inviate.component';
import {IstanzeComponent} from './components/core/istanze/istanze.component';
import {TabBozzeComponent} from './components/core/istanze/tab-bozze/tab-bozze.component';
import {TabImportantiComponent} from './components/core/istanze/tab-importanti/tab-importanti.component';
import {TabInviateComponent} from './components/core/istanze/tab-inviate/tab-inviate.component';
import {BrowserAnimationsModule, NoopAnimationsModule} from '@angular/platform-browser/animations';
import {ErrorComponent} from './components/error/error.component';
import {BackButtonDirective} from './directives/back-button.directive';
import {DatePipe, registerLocaleData} from '@angular/common';
import localeIt from '@angular/common/locales/it';
import {CustomDateParserFormatter} from './model/util/custom-date-parser-formatter';
import {RigaCercaComponent} from './components/core/cerca-istanza/riga-cerca/riga-cerca.component';
import {HeaderComponent} from './components/core/header/header.component';
import {FooterComponent} from './components/core/footer/footer.component';
import {HomeComponent} from './components/core/home/home.component';
import {ArchivioComponent} from './components/core/archivio/archivio.component';
import {ManageFormComponent} from './components/core/manage-form/manage-form.component';
import {CategorieComponent} from './components/core/categorie/categorie.component';
import {CategoriaComponent} from './components/core/categorie/categoria/categoria.component';
import {FilterModulesPipe} from './pipes/filter-modules.pipe';
import {NotificaInvioModuloComponent} from './components/core/notifica-invio-modulo/notifica-invio-modulo.component';
import {NotificaSegnalazioneComponent} from './components/core/notifica-segnalazione/notifica-segnalazione.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {CUSTOM_TOKEN_PLUGIN_NAME, CustomTokenPlugin} from './plugin/custom-token.plugin';
import {Formio} from 'formiojs';
import {CompletaAzioneComponent} from './components/core/istanze/completa-azione/completa-azione.component';
import {ContoTerziFormComponent} from './components/core/conto-terzi-form/conto-terzi-form.component';
import {NotificaStatoCompletatoComponent} from './components/core/notifica-stato-completato/notifica-stato-completato.component';
import {DettaglioWorkflowComponent} from './components/core/dettaglio-workflow/dettaglio-workflow.component';
import {BachecaComponent} from './components/core/bacheca/bacheca.component';
import {ShibbolethComponent} from './components/local/shibboleth/shibboleth.component';
import {JwtInterceptor} from './interceptors/jwt-interceptor';
import {ForbiddenComponent} from './components/auth/forbidden/forbidden.component';
import {CookieService} from 'ngx-cookie-service';
import {UserPasswordComponent} from './components/auth/user-password/user-password.component';
import {ScegliEnteComponent} from './components/core/scegli-ente/scegli-ente.component';
import {SimulatePortaleInterceptor} from './interceptors/simulate-portale-interceptor';
import {ModalNotifyComponent} from './components/modal-notify/modal-notify.component';
import {SortCategoriePipe} from './pipes/sort-categorie.pipe';
import {AlertModule} from './modules/alert';
import {IstanzaModule} from './modules/istanza/istanza.module';
import {PpayInterceptor} from './interceptors/ppay.interceptor';
import {DecodeEsitoPpayPipe} from './pipes/decode-esito-ppay.pipe';
import {TabIntegrazioneComponent} from './components/core/istanze/tab-integrazione/tab-integrazione.component';
import {
  RigaIstanzaIntegrazioneComponent
} from './components/core/istanze/tab-integrazione/riga-istanza-integrazione/riga-istanza-integrazione.component';
import {ModalAlertComponent} from './components/modal-alert/modal-alert.component';
import {GoogleComponent} from './components/auth/google/google.component';

import {FakeIrideInterceptor} from './interceptors/fake-iride.interceptor';
import {environment} from 'buildfiles/environment.local-embedded';
import {AiutoComponent} from './components/static/aiuto/aiuto.component';
import {CookieComponent} from './components/static/cookie/cookie.component';
import {AccessibilitaComponent} from './components/static/accessibilita/accessibilita.component';
import {PrivacyComponent} from './components/static/privacy/privacy.component';
import {EmbeddedComponent} from './components/local/embedded/embedded.component';

// aggiungi qui nuovi import
import {NgEventBus} from 'ng-event-bus';
import {NgSelectModule} from '@ng-select/ng-select';
import {CittafacileHeaderComponent} from './components/core/fruitori-esterni/cittafacile/cittafacile-header/cittafacile-header.component';
import {CittafacileFooterComponent} from './components/core/fruitori-esterni/cittafacile/cittafacile-footer/cittafacile-footer.component';
import {ModalFeedbackComponent} from './components/modal-feedback/modal-feedback.component';
import {ModalCloseComponent} from './components/modal-close/modal-close.component';
import {NgHttpLoaderModule} from 'ng-http-loader';
import {MoonSpinnerComponent} from './components/moon-spinner/moon-spinner.component';
import {GoogleLoginProvider, SocialAuthServiceConfig, SocialLoginModule} from 'angularx-social-login';
import {ConsumerFooterComponent} from './components/core/fruitori-esterni/consumer/consumer-footer/consumer-footer.component';
import {ConsumerHeaderComponent} from './components/core/fruitori-esterni/consumer/consumer-header/consumer-header.component';
import {TabPagamentiComponent} from './components/core/istanze/tab-pagamenti/tab-pagamenti.component';
import {
  RigaIstanzaPagamentiComponent
} from './components/core/istanze/tab-pagamenti/riga-istanza-pagamenti/riga-istanza-pagamenti.component';
import { ModalPayComponent } from './components/modal-pay/modal-pay.component';

// END IMPORT


export function initFormioPlugin(plugin: CustomTokenPlugin) {
  return () => {
    Formio.registerPlugin(plugin, CUSTOM_TOKEN_PLUGIN_NAME);
  };
}

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    FormioModule,
    FontAwesomeModule,
    NgbModule,
    BrowserAnimationsModule,
    NoopAnimationsModule,
    NgHttpLoaderModule.forRoot(),
    HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN',
    }),
    AlertModule,
    IstanzaModule,
    SocialLoginModule,
    NgSelectModule
  ],
  declarations: [
    AppComponent,
    NuovaIstanzaComponent,
    CercaIstanzaComponent,
    NotificheComponent,
    ModalBasicComponent,
    ModalConfirmComponent,
    RigaIstanzaComponent,
    RigaIstanzeImportantiComponent,
    RigaIstanzeInviateComponent,
    IstanzeComponent,
    TabBozzeComponent,
    TabImportantiComponent,
    TabInviateComponent,
    ErrorComponent,
    BackButtonDirective,
    ModalConfirmComponent,
    RigaCercaComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    ArchivioComponent,
    ManageFormComponent,
    CategorieComponent,
    CategoriaComponent,
    FilterModulesPipe,
    NotificaInvioModuloComponent,
    NotificaSegnalazioneComponent,
    CompletaAzioneComponent,
    ContoTerziFormComponent,
    NotificaStatoCompletatoComponent,
    DettaglioWorkflowComponent,
    BachecaComponent,
    ScegliEnteComponent,
    ShibbolethComponent,
    ForbiddenComponent,
    UserPasswordComponent,
    ModalNotifyComponent,
    ModalNotifyComponent,
    SortCategoriePipe,
    DecodeEsitoPpayPipe,
    TabIntegrazioneComponent,
    RigaIstanzaIntegrazioneComponent,
    ModalAlertComponent,
    GoogleComponent,
    AiutoComponent,
    CookieComponent,
    AccessibilitaComponent,
    PrivacyComponent,
    EmbeddedComponent,
    CittafacileHeaderComponent,
    CittafacileFooterComponent,
    ModalFeedbackComponent,
    ModalCloseComponent,
    MoonSpinnerComponent,
    ConsumerFooterComponent,
    ConsumerHeaderComponent,
    TabPagamentiComponent,
    RigaIstanzaPagamentiComponent,
    ModalPayComponent
  ],
  providers: [ConfigService, SecurityService, MoonfoblService, MessageService, DatePipe,
    CustomTokenPlugin, CookieService,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: PpayInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: FakeIrideInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: SimulatePortaleInterceptor, multi: true},
    {provide: LOCALE_ID, useValue: 'it'},
    {provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter},
    {provide: FormioAppConfig, useValue: AppConfig},
    {provide: APP_INITIALIZER, useFactory: initFormioPlugin, deps: [CustomTokenPlugin], multi: true},
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(environment.googleProvider),
          },
        ],
      } as SocialAuthServiceConfig,
    },
    NgEventBus
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(injector: Injector) {
    registerLocaleData(localeIt, 'it');
  }
}
