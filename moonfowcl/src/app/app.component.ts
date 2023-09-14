/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {DOCUMENT} from '@angular/common';
import {AfterViewChecked, AfterViewInit, Component, Inject, isDevMode, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NgEventBus} from 'ng-event-bus';
import {MetaData} from 'ng-event-bus/lib/meta-data';
import {CookieService} from 'ngx-cookie-service';
import {Subscription} from 'rxjs-compat';
import {environment} from 'src/environments/environment';
import {AlertTypeDesc} from './common/alert-type-desc';
import {NavSelection} from './common/nav-selection';
import {UserInfo} from './model/common';
import {ConsumerParams} from './model/common/consumer-params';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {EMB_SERVICE_NAME} from './model/common/embedded-constants';
import {EmbeddedOptions} from './model/common/embedded-options';
import {User} from './model/common/user';
import {FiltroModuli} from './model/dto/filtro-moduli';
import {Modulo} from './model/dto/modulo';
import {Nav} from './model/dto/nav';
import {ConsumerUtils} from './model/util/consumer-utils';
import {AlertService} from './modules/alert';
import {ConfigService} from './services/config.service';
import {LoginService} from './services/login.service';
import {MoonfoblService} from './services/moonfobl.service';
import {SecurityService} from './services/security.service';
import {SharedService} from './services/shared.service';
import {StorageManager} from './util/storage-manager';
import {MoonSpinnerComponent} from './components/moon-spinner/moon-spinner.component';
import {CONSUMER_TEMPLATE} from 'src/app/model/common/consumer-constants';

enum AUTH_MODE {
  USE_PWD = 'user-pwd',
  GOOGLE = 'google'
}


@Component({
  selector: 'app-root',
  // core.component.html
  templateUrl: './app.html',
  styleUrls: ['./app.component.scss'],
  providers: [ConfigService]
})

export class AppComponent implements OnInit, AfterViewChecked, AfterViewInit, OnDestroy {

  isInevidenza = false;
  isNotifiche = false;
  isNuovaIstanza = true;
  isCercaIstanza = false;
  mostraMenuMioFacile = false;
  urlLogo = '';
  public data: string = null;
  currentUser: UserInfo = null;
  public moonSpinnerComponent = MoonSpinnerComponent;

  // codline version
  version = '';
  year: number;
  // template: string = 'moon';

  // versioneModulo: number;
  msgErr = '';

  isEmbedded: boolean = false;
  subscribers: Subscription = new Subscription();

  template = 'moon';


  constructor(
    private router: Router,
    private securityService: SecurityService,
    private moonservice: MoonfoblService,
    private cookieService: CookieService,
    private sharedService: SharedService,
    private loginService: LoginService,
    private eventBus: NgEventBus,
    public alertService: AlertService,
    @Inject(DOCUMENT) private _document: HTMLDocument
  ) {

  }

  // alertOptions = {
  //   id: 'alert-moon',
  //   autoClose: false,
  //   keepAfterRouteChange: true
  // };

  alertId = 'alert-moon';
  alertOptions = {
    id: this.alertId,
    autoClose: false,
    keepAfterRouteChange: false
  };
  alertOptionsAutoClose = {
    id: this.alertId,
    autoClose: true,
    keepAfterRouteChange: false
  };

  setSubscribers() {
    this.subscribers.add(
      this.eventBus.on('alert:set').subscribe((meta: MetaData) => {
        console.log(meta.data);
        if ((meta.data.clear === undefined) || (meta.data.clear && meta.data.clear === true)){
          this.alertService.clear(this.alertId);
        }

        this.getAlert(meta.data);
        console.log('app.component::setSubscribers::alert:set' + JSON.stringify(meta.data));
      }))
      .add(
        this.eventBus.on('microfe:set').subscribe((meta: MetaData) => {
          console.log(meta.data);
          if (meta.data) {
            if (meta.data.embeddedNavigator) {

              console.log(' *** APP.component::setSubscribers: IS EMBEDDED -> if embedded hide header and footer ***');
              this.isEmbedded = true;
            } else if (meta.data.consumerParams) {
              console.log(' *** APP.component::setSubscribers: IS CONSUMER -> update header and footer ***');
              console.log(' *** APP.component::setSubscribers: template before set '+this.template);
              this.template = meta.data.consumerParams.consumer;
              console.log(' *** APP.component::setSubscribers: template after set '+this.template);
            }

          }
        })
      );
  }

  ngOnInit(): void {
    this.setSubscribers();

    // this.microfeSub = this._microfe.getDataEmbedded().subscribe(user => {
    //   console.log(' *** APP.component::ngOnInit: embedded ' + JSON.stringify(user) + ' ***');
    //   if (user) {
    //     if (user.embeddedNavigator) {

    //       console.log(' *** APP.component::ngOnInit: IS EMBEDDED -> update header and footer ***');
    //       this.isEmbedded = true;
    //     }
    //     else if (user.consumerParams) {
    //       this.template = user.consumerParams.consumer;
    //     }
    //   }
    // });

    this.version = environment.version;
    this.year = environment.year;
    this.msgErr = '';

    console.log('app.component::ngOnInit: check cookie');

    const qryParams = window.location.search;
    const urlParams = new URLSearchParams(qryParams);

    // parametri per compilazione modulo o accesso filtrato
    const codiceModulo = urlParams.get('codice_modulo');
    const codiceEnte = urlParams.get('codice_ente');
    const codiceAmbito = urlParams.get('amb');
    const gope = urlParams.get('gope');

    this.setViewEmbedded();

    const embedded = urlParams.get('embedded');

    if (embedded) {
      this.loginEmbedded(embedded);
    } else {

      if (this.cookieService.check('auth-mode') && !isDevMode()) {

        const authMode = this.cookieService.get('auth-mode');
        console.log('App.component: codiceModulo ' + codiceModulo);
        console.log('App.component: codice_ente ' + codiceEnte);
        console.log('App.component: amb ' + codiceAmbito);
        switch (authMode) {
          case AUTH_MODE.USE_PWD:
            // login user e password
            this.router.navigate(['auth/user-pwd'], { queryParams: { codice_modulo: codiceModulo, codice_ente: codiceEnte, amb: codiceAmbito, gope: gope } });
            break;
          case AUTH_MODE.GOOGLE:
            // Google
            this.router.navigate(['auth/social'], { queryParams: { codice_modulo: codiceModulo, codice_ente: codiceEnte, amb: codiceAmbito } });
            break;

          default:
            console.log('authMode non valorizzato');
            break;
        }
      } else {
        if (this.cookieService.check('loginResponse') && !isDevMode()) {
          console.log('app.component::ngOnInit: Cookie presente');
          console.log('imposto user App root');
          const cookie = this.cookieService.get('loginResponse');
          const cookieDecoded = StorageManager.fromBinary(cookie.charAt(0) === '"' ? JSON.parse(cookie) : cookie);
          const cuser: User = JSON.parse(cookieDecoded);

          StorageManager.clear();
          StorageManager.add(STORAGE_KEYS.JWT_MOON, cuser.idMoonToken);

          // in caso di embedded conserva l'informazione
          StorageManager.add(STORAGE_KEYS.USER, cuser);

          console.log('app.component::ngOnInit: jwt-moon impostato');
          console.log(' *** app.component::ngOnInit: user got from cookie = ' + JSON.stringify(cuser) + ' ***');
          // check elimino il cookie
          // elimino cookie per gestire correttamente le rotte in anngular quando utentele modifica sul browser
          if (!isDevMode()) {
            this.cookieService.delete('loginResponse', '/');
            console.log('Cookie cancellato !');
          }

          // check consumer
          this.setConsumerIfPresent(cuser, urlParams, function () {
            const queryString = window.location.search;
            this.loginService.login(cuser, queryString);
          }.bind(this));


          if (!urlParams.get('consumer')){
            console.log('app.component::ngOnInit: login NO CONSUMER');
            this.loginService.login(cuser, qryParams);
          }


        } else {
          console.log('app.component::ngOnInit: Cookie non presente');
          // controllo se utente presente
          let user = StorageManager.get(STORAGE_KEYS.USER);
          if (!user && !isDevMode()) {
            window.location.href = 'unauthorized.html';
          }
        }
      }
    }
  }


  setConsumerIfPresent(user: User, urlParams: URLSearchParams, callback) {
    const consumer = urlParams.get('consumer');
    const consumerParams: ConsumerParams = this.getConsumerParams(consumer, urlParams)
    const consumerUtils = new ConsumerUtils(consumerParams);

    if (consumer && consumerParams.codiceEnte) {
      if (consumerUtils.isValidParameterSet() && consumerUtils.isValidConsumer()) {
        user.consumerParams = consumerParams;
        // update user with consumer
        // fixme invocazione servizio per ricevere i parametri ci configurazione
        this.moonservice.getParametriConfigurazione(consumer, consumerParams.codiceEnte).subscribe(
          (params) => {
            console.log(" *** setting parametri di configurazione consumer ***");
            consumerParams.parameters = params;
            user.consumerParams = consumerParams;
            StorageManager.add(STORAGE_KEYS.USER, user);
            this.eventBus.cast('microfe:set', user);

            callback();
          }
        );
      } else {
        window.location.href = 'invalid.html';
      }
    } else{
      callback();
    }
  }

  getConsumerParams(consumer: string, urlParams: URLSearchParams) {
    // parametri per accesso custom da portale specifico ( es: cittafacile )
    const service = urlParams.get('service');
    const backUrl = urlParams.get('back_url');
    const codiceIstanza = urlParams.get('codice_istanza');
    const codiceModulo = urlParams.get('codice_modulo');
    const codiceEnte = urlParams.get('codice_ente');
    const faq = urlParams.get('faq');
    const assistenza = urlParams.get('assistenza');
    const consumerParams = new ConsumerParams();
    consumerParams.consumer = consumer;
    consumerParams.service = service;
    consumerParams.backUrl = backUrl;
    consumerParams.codiceEnte = codiceEnte;
    consumerParams.codiceIstanza = codiceIstanza;
    consumerParams.codiceModulo = codiceModulo;
    consumerParams.faq = faq;
    consumerParams.assistenza = assistenza;
    return consumerParams;
  }


  loginMonoEnte(codiceModulo) {
    /*
       Determino versione del modulo
     */
    if (codiceModulo) {
      const filtro = new FiltroModuli();
      if (isNaN(codiceModulo)) {
        filtro.codiceModulo = codiceModulo;
      } else {
        filtro.idModulo = codiceModulo;
      }
      this.moonservice.getModuloFiltrato(filtro).subscribe(
        (m: Modulo[]) => {
          this.router.navigate(['manage-form/NEW/' + m[0].idModulo + '/' + m[0].idVersioneModulo]);
        }
      );
    } else { // id Modulo non valorizzato
      this.router.navigate(['home']);
    }
  }


  loginMultiEnte() {
    this.moonservice.getElencoEnti().subscribe((enti) => {
      if (enti.length > 1) {
        this.sharedService.elencoEnti = enti;
        this.sharedService.nav = new Nav(NavSelection.ENTE, 'home/scegli-ente');
        this.router.navigate(['home']);
      }
    });
  }

  setViewEmbedded() {
    console.log("*** SET EMBEDDED IF PRESENT IN STORAGE ***")
    const user = StorageManager.get(STORAGE_KEYS.USER);
    if (user && user.embeddedNavigator) {
      this.isEmbedded = true
    }
  }


  inEvidenza() {
    this.router.navigate(['inevidenza']);
    this.isInevidenza = true;
    this.isNotifiche = false;
    this.isNuovaIstanza = false;
    this.isCercaIstanza = false;
  }

  notifiche() {
    this.router.navigate(['notifiche']);
    this.isInevidenza = false;
    this.isNotifiche = true;
    this.isNuovaIstanza = false;
    this.isCercaIstanza = false;
  }

  // nuova-istanza
  nuovaIstanza() {
    this.router.navigate(['nuova-istanza']);
    this.isInevidenza = false;
    this.isNotifiche = false;
    this.isNuovaIstanza = true;
    this.isCercaIstanza = false;
  }

  // cerca-istanza
  cercaIstanza() {
    /*
      ripulisco sessione
     */
    StorageManager.remove(STORAGE_KEYS.CRITERIO_RICERCA);
    StorageManager.remove(STORAGE_KEYS.ISTANZE_SEARCH_RESULT);
    this.router.navigate(['cerca-istanza']);
    this.isInevidenza = false;
    this.isNotifiche = false;
    this.isNuovaIstanza = false;
    this.isCercaIstanza = true;
  }


  //EMBEDDED
  loginEmbedded(embedded: string) {
    try {

      console.log("*** embedded = " + embedded);
      const embObj = JSON.parse(embedded);
      console.log("*** emb object = " + embObj);

      const service = embObj?.service;
      const codiceIstanza = embObj?.params?.codiceIstanza;
      const codiceModulo = embObj?.params?.codiceModulo;
      const token = embObj?.token;

      console.log('[app.component ::loginEmbedded] service=' + service);
      console.log('[app.component ::loginEmbedded] codiceIstanza=' + codiceIstanza);
      console.log('[app.component ::loginEmbedded] codiceModulo=' + codiceModulo);
      console.log('[app.component ::loginEmbedded] token=' + token);


      switch (service) {
        case EMB_SERVICE_NAME.ISTANZE:
          this.goToIstanze(service, token);
          break;
        case EMB_SERVICE_NAME.NEW_ISTANZA:
          if (codiceModulo) {
            this.goToNewIstanza(codiceModulo, service, token);
          } else {
            console.log('[embedded::ngOnInit] codiceModulo non presente');
          }
          break;
        case EMB_SERVICE_NAME.VIEW_ISTANZA:
        case EMB_SERVICE_NAME.EDIT_ISTANZA:
        case EMB_SERVICE_NAME.ISTANZA:
          if (codiceIstanza) {
            console.log('[app.component ::loginEmbedded] go to istanza for service =' + service);
            this.goToIstanza(codiceIstanza, service, token);
          } else {
            console.log('[embedded::ngOnInit] codiceistanza non presente');
          }
          break;
        default:
          console.log('service non valorizzato');
          break;
      }
    } catch (e) {
      console.log(e);
    }

  }

  saveCookieAndlogin() {

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const url_esci_modulo = urlParams.get('url_esci_modulo');
    const url_ritorna_istanze = urlParams.get('url_ritorna_istanze');

    const cookie = this.cookieService.get('loginResponse');
    const cookieDecoded = StorageManager.fromBinary(cookie.charAt(0) === '"' ? JSON.parse(cookie) : cookie);
    const cuser: User = JSON.parse(cookieDecoded);

    cuser.embeddedNavigator.options = new EmbeddedOptions();

    cuser.embeddedNavigator.options.urlEsciModulo = url_esci_modulo;
    cuser.embeddedNavigator.options.urlTornaIstanze = url_ritorna_istanze;

    StorageManager.clear();
    StorageManager.add(STORAGE_KEYS.JWT_MOON, cuser.idMoonToken);
    StorageManager.add(STORAGE_KEYS.USER, cuser);
    // elimino cookie per gestire correttamente le rotte in anngular quando utentele modifica sul browser
    if (!isDevMode()) {
      this.cookieService.delete('loginResponse', '/');
      console.log('Cookie cancellato !');
    }
    console.log('embedded.component::saveCookieAndlogin: jwt-moon impostato');
    this.eventBus.cast('authenticazione:set-current-user', cuser);

    console.log('embedded.component::saveCookieAndlogin: user = ' + cuser);
    // this._microfe.sendDataEmbedded(cuser);
    this.eventBus.cast('microfe:set', cuser);

    this.loginService.login(cuser, queryString);
  }

  goToNewIstanza(codiceModulo: string, service: string, token: string) {
    this.securityService.autenticaEmbModulo(codiceModulo, service.toLowerCase(), token).subscribe(
      (response) => {

        console.log('*** RESPONSE EMBEDDED  autenticaEmbModulo' + JSON.stringify(response));
        console.log('embedded.component::ngOnInit: check cookie');
        if (this.cookieService.check('loginResponse')) {

          this.saveCookieAndlogin();

        } else {
          console.log('embedded.component::ngOnInit: Cookie non presente');
          window.location.href = 'unauthorized.html';
        }
      },
      (err) => {
        // inviare messaggio token non valido
        console.log("*** errore autenticazione modulo ***");
        console.log(err);
        window.location.href = 'unauthorized.html';
      }
    );
  }

  goToIstanze(service: string, token: string) {
    this.securityService.autenticaEmbIstanze(service.toLowerCase(), token).subscribe(
      (response) => {

        console.log('*** RESPONSE EMBEDDED autenticaEmbIstanze' + JSON.stringify(response));
        console.log('embedded.component::ngOnInit: check cookie');
        if (this.cookieService.check('loginResponse')) {
          this.saveCookieAndlogin();
        } else {
          console.log('embedded.component::ngOnInit: Cookie non presente');
          window.location.href = 'unauthorized.html';
        }
      },
      (err) => {
        // inviare messaggio token non valido
        console.log(err);
        window.location.href = 'unauthorized.html';
      }
    );
  }

  goToIstanza(codiceIstanza: string, service: string, token: string) {
    this.securityService.autenticaEmbIstanza(codiceIstanza, service.toLowerCase(), token).subscribe(
      (response) => {

        console.log('*** RESPONSE EMBEDDED autenticaEmbIstanza' + JSON.stringify(response));
        console.log('embedded.component::ngOnInit: check cookie');
        if (this.cookieService.check('loginResponse')) {

          this.saveCookieAndlogin();

        } else {
          console.log('embedded.component::ngOnInit: Cookie non presente');
          window.location.href = 'unauthorized.html';
        }
      },
      (err) => {
        // inviare messaggio token non valido
        console.log(err);
        window.location.href = 'unauthorized.html';
      }
    );
  }

  // ngAfterViewChecked() {
  ngAfterViewInit() {
    console.log('app.component::ngOnInit: not implemented');
  }

  ngAfterViewChecked() {
    // ngAfterViewInit() {
    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    if (user) {
      if (!user?.embeddedNavigator) {
        console.log('app.component::ngAfterViewChecked: sent data user authenticated to header subscriber');
        this.eventBus.cast('authenticazione:set-current-user', user);
      }

      if (user?.embeddedNavigator || user?.consumerParams) {
        // this._microfe.sendDataEmbedded(user);
        this.eventBus.cast('microfe:set', user);

        console.log('app.component::ngOnInit: sent data embedded / consumer information to subscriber if needed');
      }
    }
  }

  ngOnDestroy() {

    this.subscribers.unsubscribe();

    // if (this.microfeSub) {
    //   this.microfeSub.unsubscribe();
    // }
  }


  getAlert(message) {
    const type = message.type;
    const text = message.text;
    let options = {};

    if (message.autoclose) {
      options = this.alertOptionsAutoClose;
    } else {
      options = this.alertOptions;
    }
    if (message.clear){
      this.alertService.clear(this.alertId);
    }else{
      switch (type) {
        case AlertTypeDesc.SUCCESS: {
          this.alertService.success(text, this.alertOptions);
          break;
        }
        case AlertTypeDesc.INFO: {
          this.alertService.info(text, this.alertOptions);
          break;
        }
        case AlertTypeDesc.ERROR: {
          this.alertService.error(text, this.alertOptions);
          break;
        }
        case AlertTypeDesc.WARN: {
          this.alertService.warn(text, this.alertOptions);
          break;
        }
        default: {
          this.alertService.warn(text, this.alertOptions);
          break;
        }
      }

    }
  }


}
