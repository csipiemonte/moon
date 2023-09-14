/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule, HttpClientXsrfModule} from '@angular/common/http';
import { NgModule, LOCALE_ID, APP_INITIALIZER } from '@angular/core';
import localeIt from '@angular/common/locales/it';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
//import { NuovaIstanzaComponent} from '../app/components/nuova-istanza/nuova-istanza.component';

import { CercaIstanzaComponent} from '../app/components/cerca-istanza/cerca-istanza.component';
import { NotificheComponent} from '../app/components/notifiche/notifiche.component';
import { TestPageComponent } from './test-page.component';

import { ConfigService } from './config.service';
import { SecurityService } from './security.service';
import { MoonboblService } from './services/moonbobl.service';
import { ReportService } from './services/report.service';
import { AppRoutingModule } from './app-routing.module';
import {FakeIdentityInterceptor} from './interceptors/fake-identity-interceptor';
import {XsrfInterceptor} from './interceptors/httpXSRFInterceptor';

// Formio
import {FormioAppConfig, FormioModule} from '@formio/angular';
// import {FormManagerModule} from 'angular-formio/manager';
import {Moonbobl} from './components/view-form/moonbobl';
import {HttpErrorHandler} from './services/http-error-handler.service';
import {MessageService} from './services/message.service';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { ModalBasicComponent } from './components/modal/modal-basic/modal-basic.component';
import { ModalConfirmComponent } from './components/modal/modal-confirm/modal-confirm.component';
import { ModalActionComponent } from './components/modal/modal-action/modal-action.component';
import { ModalNotifyComponent } from './components/modal/modal-notify/modal-notify.component';
import { RigaIstanzaComponent } from './components/istanze/tab-bozze/riga-istanza/riga-istanza.component';
import { DettaglioWorkflowComponent } from './components/dettaglio-workflow/dettaglio-workflow.component';
// tslint:disable-next-line: max-line-length
import { RigaIstanzeLavorazioneComponent } from './components/istanze/tab-lavorazione/riga-istanza-lavorazione/riga-istanze-lavorazione.component';
import { RigaIstanzePervenuteComponent } from './components/istanze/tab-pervenute/riga-istanze-pervenute/riga-istanze-pervenute.component';
import { IstanzeComponent } from './components/istanze/istanze.component';
import { TabBozzeComponent } from './components/istanze/tab-bozze/tab-bozze.component';
import { TabLavorazioneComponent } from './components/istanze/tab-lavorazione/tab-lavorazione.component';
import { TabPervenuteComponent } from './components/istanze/tab-pervenute/tab-pervenute.component';
import { ViewFormComponent } from './components/view-form/view-form.component';
import { CompletaAzioneComponent} from './components/completa-azione/completa-azione.component';
import { ReportComponent} from './components/report/report.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { MoonSpinnerComponent } from './components/moon-spinner/moon-spinner.component';
import {HandleExceptionService} from './services/handle-exception.service';
import { RigaCercaComponent } from './components/cerca-istanza/riga-cerca/riga-cerca.component';
import { OwlDateTimeModule, OwlNativeDateTimeModule, OWL_DATE_TIME_LOCALE, DateTimeAdapter, OWL_DATE_TIME_FORMATS, OwlDateTimeIntl } from 'ng-pick-datetime';
import { DateFnsDateTimeAdapter } from './../@global/date';
import { ReactiveFormsModule} from '@angular/forms';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import { ExportIstanzeComponent } from './components/export-istanze/export-istanze.component';
import {DatePipe, registerLocaleData} from '@angular/common';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {library} from '@fortawesome/fontawesome-svg-core';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons';
import { MieiModuliPageComponent } from './components/miei-moduli-page/miei-moduli-page.component';
import {MieiModuliContainerComponent} from './components/miei-moduli-page/miei-moduli-container/miei-moduli-container.component';
import { MieiModuliTabellaComponent } from './components/miei-moduli-page/miei-moduli-container/miei-moduli-tabella/miei-moduli-tabella.component';
import { MieiModuliRigaComponent } from './components/miei-moduli-page/miei-moduli-container/miei-moduli-tabella/miei-moduli-riga/miei-moduli-riga.component';
import { FormioEditorModule } from 'angular-formio-editor';
import { UtentiFilterPipe } from './pipes/utenti-filter-pipe';
import { AutocompleteLibModule } from 'angular-ng-autocomplete';
import { AppConfig } from './components/conf/formio-config';
import { CambiaStatoModuloComponent } from './components/miei-moduli-page/miei-moduli-container/cambia-stato-modulo/cambia-stato-modulo.component';
import { ErrorComponent } from './components/error-component/error-component';
import { DatiGeneraliModuloComponent } from './components/miei-moduli-page/miei-moduli-container/dati-generali-modulo/dati-generali-modulo.component';
import { NuovaVersioneModuloComponent } from './components/miei-moduli-page/miei-moduli-container/nuova-versione-modulo/nuova-versione-modulo.component';
import { ScegliEnteComponent } from './components/scegli-ente/scegli-ente.component';
import { DettaglioModuloComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/dettaglio-modulo.component';
import { DatiGeneraliComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/dati-generali/dati-generali.component';
import { VersioniComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/versioni/versioni.component';
import { AttributiComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attributi/attributi.component';
import { AttrEmailComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-email/attr-email.component';
import { AttrProtocolloComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-protocollo/attr-protocollo.component';
import { AttrNotificatoreComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-notificatore/attr-notificatore.component';
import { AttrWfCosmoComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-wf-cosmo/attr-wf-cosmo.component';
import { AttrWfAzioniComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-wf-azioni/attr-wf-azioni.component';
import { AttrEstraiDichComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-estrai-dich/attr-estrai-dich.component';
import { AttrCrmComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-crm/attr-crm.component';
import { CreaModuloComponent } from './components/miei-moduli-page/miei-moduli-container/crea-modulo/crea-modulo.component';
import { InfoReplaceComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/info-replace/info-replace.component';
import { FormioEditorComponent } from './components/common/formio-editor/formio-editor.component';
import { UtentePipe } from './pipes/utente-pipe';
import { AlertModule } from './modules/alert';
import { CustomPipesModule } from './modules/custom-pipes/custom-pipes.module';
import { ModuliFilterPipe } from './modules/custom-pipes/pipes/moduli-filter-pipe';
import { ProfiloComponent } from './components/profilo/profilo.component';
import {AngularTreeGridModule} from 'angular-tree-grid';
import {TreeGridOpComponent} from './components/common/tree-grid-op/tree-grid-op.component';
import { UtentiAbilitatiComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/utenti-abilitati/utenti-abilitati.component';
import { UtentiModule } from './modules/utenti/utenti.module';
import { ProcessiModule } from './modules/processi/processi.module';
import { UiSwitchModule } from 'ngx-ui-switch';
import { TabDaCompletareComponent } from './components/istanze/tab-da-completare/tab-da-completare.component';
import { RigaIstanzaDaCompletareComponent } from './components/istanze/tab-da-completare/riga-istanza-da-completare/riga-istanza-da-completare.component';
import { CompilaIstanzaModule } from './modules/compila-istanza/compila-istanza.module';
import { ValidationService } from './services/validation.service';
import { NotificaSegnalazioneComponent } from './components/notifica-segnalazione/notifica-segnalazione.component';
import { IstanzaModule } from './modules/istanza/istanza.module';
import { NotificaModificaComponent } from './components/notifica-modifica/notifica-modifica.component';
import { AttrEpayComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-epay/attr-epay.component';
import { PrtParametriTabellaComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-protocollo/prt-parametri-tabella/prt-parametri-tabella.component';
import { PrtParametriRigaComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-protocollo/prt-parametri-tabella/prt-parametri-riga/prt-parametri-riga.component';
import { AttrApiComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-api/attr-api.component';
import { SortCategoriePipe } from './pipes/sort-categorie.pipe';
import { PrtMetadatiTabComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-protocollo/prt-metadati-tab/prt-metadati-tab.component';
import { PrtMetadatiTabEditComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-protocollo/prt-metadati-tab-edit/prt-metadati-tab-edit.component';
import { PrtEmailComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-protocollo/prt-email/prt-email.component';
import { DefaultIntl } from 'src/@global/date/defaultintl';
import {ModuliCacheService} from './services/moduli-cache.service';
import { PrtMetadatiTabellaComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-protocollo/prt-metadati-tab/prt-metadati-tabella/prt-metadati-tabella.component';
import { PrtMetadatiFormComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-protocollo/prt-metadati-tab-edit/prt-metadati-form/prt-metadati-form.component';
import { JwtInterceptor } from './interceptors/jwt-interceptor';
import { ForbiddenComponent } from './components/auth/forbidden/forbidden.component';
import { UserPasswordComponent } from './components/auth/user-password/user-password.component';
import { ShibbolethComponent } from './components/local/shibboleth/shibboleth.component';
import { ModulisticaComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/modulistica/modulistica.component';
import { CrmConfNextcrmComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-crm/crm-conf-nextcrm/crm-conf-nextcrm.component';
import { CrmConfReadytouseComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-crm/crm-conf-readytouse/crm-conf-readytouse.component';
import { CUSTOM_TOKEN_PLUGIN_NAME, CustomTokenPlugin } from './plugin/custom-token.plugin';
import { Formio } from 'formiojs';
import { CrmConfOtrsComponent } from './components/miei-moduli-page/miei-moduli-container/dettaglio-modulo/attr-crm/crm-conf-otrs/crm-conf-otrs.component';


const DATEFNS_FORMATS_IT_LOCALE = {
  parseInput: 'dd/MM/yyyy HH:mm || dd/MM/yyyy', 
  fullPickerInput: 'dd/MM/yyyy HH:mm',
  datePickerInput: 'dd/MM/yyyy',
  timePickerInput: 'HH:mm',
  monthYearLabel: 'MMM yyyy',
  dateA11yLabel: 'dd/MM/yyyy',
  monthYearA11yLabel: 'MMMM yyyy',
};

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
        // fixme       FormManagerModule,
        AppRoutingModule,
        HttpClientModule,
        FormioModule,
        FontAwesomeModule,
        NgbModule,
        NgxSpinnerModule,
        BrowserAnimationsModule,
        NoopAnimationsModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        NgMultiSelectDropDownModule,
        UiSwitchModule,
        HttpClientXsrfModule.withOptions({
            cookieName: 'XSRF-TOKEN',
            headerName: 'X-XSRF-TOKEN',
        }),
        FormioEditorModule,
        AutocompleteLibModule,
        AlertModule,
        CustomPipesModule,
        AngularTreeGridModule,
        UtentiModule,
        ProcessiModule,
        CompilaIstanzaModule,
        IstanzaModule
    ],
    declarations: [
        AppComponent,
        //NuovaIstanzaComponent,
        CercaIstanzaComponent,
        NotificheComponent,
        TestPageComponent,
        ViewFormComponent,
        ModalBasicComponent,
        ModalConfirmComponent,
        ModalNotifyComponent,
        ModalActionComponent,
        RigaIstanzaComponent,
        DettaglioWorkflowComponent,
        RigaIstanzeLavorazioneComponent,
        RigaIstanzePervenuteComponent,
        IstanzeComponent,
        TabBozzeComponent,
        TabLavorazioneComponent,
        TabPervenuteComponent,
        MoonSpinnerComponent,
        CompletaAzioneComponent,
        ReportComponent,
        RigaCercaComponent,
        ExportIstanzeComponent,
        MieiModuliPageComponent,
        MieiModuliContainerComponent,
        MieiModuliTabellaComponent,
        MieiModuliRigaComponent,
        UtentiFilterPipe,
        CreaModuloComponent,
        CambiaStatoModuloComponent,
        ErrorComponent,
        DatiGeneraliModuloComponent,
        NuovaVersioneModuloComponent,
        ScegliEnteComponent,
        DettaglioModuloComponent,
        DatiGeneraliComponent,
        VersioniComponent,
        AttributiComponent,
        AttrEmailComponent,
        AttrProtocolloComponent,
        AttrNotificatoreComponent,
        AttrWfCosmoComponent,
        AttrWfAzioniComponent,
        InfoReplaceComponent,
        AttrWfAzioniComponent,
        FormioEditorComponent,
        AttrEstraiDichComponent,
        AttrCrmComponent,
        UtentePipe,
        SortCategoriePipe,
        ProfiloComponent,
        TreeGridOpComponent,
        ProfiloComponent,
        UtentiAbilitatiComponent,
        TabDaCompletareComponent,
        RigaIstanzaDaCompletareComponent,
        NotificaSegnalazioneComponent,
        NotificaModificaComponent,
        AttrEpayComponent,
        PrtParametriTabellaComponent,
        PrtParametriRigaComponent,
        AttrApiComponent,
        PrtMetadatiTabComponent,
        PrtMetadatiTabEditComponent,
        PrtEmailComponent,
        PrtMetadatiTabellaComponent,
        PrtMetadatiFormComponent,
        ForbiddenComponent,
        UserPasswordComponent,
        ShibbolethComponent,
        ModulisticaComponent,
        CrmConfNextcrmComponent,
        CrmConfReadytouseComponent,
        CrmConfOtrsComponent
    ],
    providers: [ConfigService, SecurityService, MoonboblService, Moonbobl, HttpErrorHandler, MessageService,
      ReportService, HandleExceptionService, ValidationService, ModuliCacheService, CustomTokenPlugin, 
        {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
        { provide: HTTP_INTERCEPTORS, useClass: FakeIdentityInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: XsrfInterceptor, multi: true },
        { provide: LOCALE_ID, useValue: 'it' },
        { provide: OWL_DATE_TIME_LOCALE, useValue: 'it' },
        { provide: DateTimeAdapter, useClass: DateFnsDateTimeAdapter },
        { provide: OWL_DATE_TIME_FORMATS, useValue: DATEFNS_FORMATS_IT_LOCALE },
        { provide: OwlDateTimeIntl, useClass: DefaultIntl },
        DatePipe,
        ModuliFilterPipe,
        UtentiFilterPipe,
        UtentePipe,
      { provide: FormioAppConfig, useValue: AppConfig },
      { provide: APP_INITIALIZER, useFactory: initFormioPlugin, deps: [CustomTokenPlugin], multi: true },
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
  constructor() {
    library.add(faArrowLeft);
    registerLocaleData(localeIt, 'it');
  }
}
