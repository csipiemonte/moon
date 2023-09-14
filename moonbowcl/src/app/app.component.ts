/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, isDevMode, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';

import { ConfigService } from './config.service';
import { SecurityService } from './security.service';
import { UserInfo } from './model/common/user-info';
import { handleError } from './services/service.utils';
import { faQuestionCircle } from '@fortawesome/free-solid-svg-icons';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { SharedService } from './services/shared.service';
import { MoonboblService } from './services/moonbobl.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { NavSelection } from './common/nav-selection';
import { Nav } from './model/dto/nav';
import { ObserveService } from './services/observe.service';
import { environment } from 'src/environments/environment';
import {CookieService} from 'ngx-cookie-service';
import {StorageManager} from './common/utils/storage-manager';
import {STORAGE_KEYS} from './common/costanti';
import {AutenticazioneService} from './services/notifiers/autenticazione.service';
import { Subscription } from 'rxjs';


enum AUTH_MODE {
  USE_PWD = 'user-pwd',
  GOOGLE = 'google'
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ConfigService]
})
export class AppComponent implements OnInit, OnDestroy {
  faQuestionCircle = faQuestionCircle;
  faUser = faUser;

  isIstanze = true;
  isNotifiche = false;
  isCercaIstanza = false;
  isReport = false;
  isExportIstanze = false;
  isModuli = false;
  isUtenti = false;
  isScegliEnte = false;
  isProfilo = false;
  isNuovaIstanza = false;
  isProcessi = false;
  showNavMenu = false;
  isMultiEnte = false;
  nomeEnte = '';
  urlLogoEnte = environment.pathAssets + '/im/moon.png';

  isOperatoreCompilaBO = true;

   currentUser: UserInfo = null;

  // subscription al servizio di autenticazione
  private autenticazioneSub: Subscription;

  constructor(
    private router: Router,
    private securityService: SecurityService,
    private config: ConfigService,
    private sharedService: SharedService,
    private spinnerService: NgxSpinnerService,
    private moonboblService: MoonboblService,
    private observeService: ObserveService,
    private cookieService: CookieService,
    private _autenticazioneService: AutenticazioneService
  ) {
    //dopo login usr-pwd aggiorno i dati utente
    this.autenticazioneSub = this._autenticazioneService.getDataUserAuthenticated().subscribe(user => {
      // Imposto user con tutti i dati
      this.currentUser = UserInfo.createUserInfo(user);
      this.currentUser.isAuthenticated = true;
      this.sharedService.UserLogged = this.currentUser;
      this.isMultiEnte = this.currentUser.multiEntePortale;
      this.manageMultiEnteUser(user);
      //console.log('app.component::constructor: user authenticated ' + JSON.stringify(this.currentUser));
      console.log('[app::constructor] user authenticated ' + this.currentUser);
    });
  }



  ngOnInit(): void {
    // this.securityService.getCurrentUser().then(currentUser => this.currentUser = currentUser);
    const errorHandler = handleError.bind(this);
    // Pulisco la sessione
    StorageManager.clear();
    this.urlLogoEnte = environment.pathAssets + '/im/moon.png';
    console.log('app.component::ngOnInit() check cookie');

    const qryParams = window.location.search;
    const urlParams = new URLSearchParams(qryParams);
    const codiceModulo = urlParams.get('codice_modulo');
    const codiceEnte = urlParams.get('codice_ente');
    const codiceAmbito = urlParams.get('amb');

    if (this.cookieService.check('auth-mode') && !isDevMode()) {

      const authMode = this.cookieService.get('auth-mode');

      console.log('app.component::ngOnInit(): codiceModulo ' + codiceModulo);
      console.log('app.component::ngOnInit(): codice_ente ' + codiceEnte);
      console.log('app.component::ngOnInit(): amb ' + codiceAmbito);
      switch (authMode) {
        case AUTH_MODE.USE_PWD:
          // login user e password
          this.router.navigate(['auth/user-pwd'], { queryParams: { codice_modulo: codiceModulo, codice_ente: codiceEnte, amb: codiceAmbito}});
          break;
        case AUTH_MODE.GOOGLE:
          // Google
          this.router.navigate(['auth/social'], { queryParams: { codice_modulo: codiceModulo, codice_ente: codiceEnte, amb: codiceAmbito}});
          break;

        default:
          console.log('app.component::ngOnInit(): authMode non valorizzato');
          break;
      }
    } else {
      console.log('app.component::ngOnInit(): check loginResponse', this.cookieService.check('loginResponse'));
      console.log('app.component::ngOnInit(): check is DEv mode', isDevMode());
      if (this.cookieService.check('loginResponse') && this.cookieService.check('idMoonToken') && !isDevMode()) {
        console.log('app.component::ngOnInit(): Cookie presente');

        const cookieLoginResponse = this.cookieService.get('loginResponse');
        const cookieLoginResponseDecoded = StorageManager.fromBinary(cookieLoginResponse.charAt(0) === '"' ? JSON.parse(cookieLoginResponse) : cookieLoginResponse);
        const loginResonse = JSON.parse(cookieLoginResponseDecoded);
        //
        const cookieIdMoonToken = this.cookieService.get('idMoonToken');
        StorageManager.add(STORAGE_KEYS.MOON_JWT_TOKEN, cookieIdMoonToken);

        this.currentUser = UserInfo.createUserInfo(loginResonse);
        this.sharedService.UserLogged = this.currentUser;
        console.log('app.component::ngOnInit: this.sharedService.UserLogged= '+this.sharedService.UserLogged.urlLogout);
        this.manageMultiEnteUser(this.currentUser);
        this.isMultiEnte = this.currentUser.multiEntePortale;

      } else {
        console.log('app.component::ngOnInit: Cookie non presente');
        if (!isDevMode()) {
          window.location.href = 'unauthorized.html';
        }
      }
    }

  }

  ngOnDestroy(): void {
    this.autenticazioneSub.unsubscribe();
    if (this.observeService) {
      this.observeService.unsubscribeEventResponse();
    }
  }

  manageMultiEnteUser(cuser) {
    if (cuser.multiEntePortale) {
      this.observeService.getEventResponse().subscribe(
        (resp: any) => {
          console.log('SUBSCRIBE RESPONSE: ' + resp?JSON.stringify(resp):'NULL OR undefined');
          this.aggiornaEnte(resp);
        }
      );
    }

    if (!cuser.ente) {
      this.moonboblService.getElencoEnti().subscribe((enti) => {
        if (enti.length > 1) {
          this.sharedService.elencoEnti = enti;
          this.isScegliEnte = true;
          this.showNavMenu = false;
          this.router.navigate(['scegli-ente']);
        }
      });
    } else {
            this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'istanze');
            this.isScegliEnte = false;
            this.showNavMenu = true;
            this.router.navigate(['istanze']);
            // ente corrente
            if (this.currentUser.multiEntePortale) {
              this.nomeEnte = this.currentUser.ente.nomeEnte;
            }
      }
    }
  

  scegliEnte() {
    setTimeout(() => {
          this.isScegliEnte = true;
          this.showNavMenu = false;
          this.router.navigate(['scegli-ente']);
      /*
      this.sharedService.nav = new Nav(NavSelection.ENTE, 'scegli-ente');
      this.ngOnInit();
      */
    }, 500);
  }

  showProfilo() {
    this.router.navigate(['profilo']);
    this.disableAllTab();
    this.isProfilo = true;
    // setTimeout(() => {
    //   this.sharedService.nav = new Nav(NavSelection.PROFILO, 'profilo');
    //   this.ngOnInit();
    // }, 500);
  }

  logout() {
    console.log('app.component::ssoLogout: StorageManager clear');
    StorageManager.clear();
    console.log('app.component::ssoLogout: coockieService check');

    if (this.cookieService.check('loginResponse')) {
      this.cookieService.delete('loginResponse', '/', '', false, 'Lax');
      console.log('app.component::ssoLogout: coockieService loginResponse deleted');
    }
    if (this.cookieService.check('idMoonToken')) {
      this.cookieService.delete('idMoonToken', '/', '', false, 'Lax');
      console.log('app.component::ssoLogout: coockieService idMoonToken deleted');
    }
    if (this.cookieService.check('auth-mode')) {
      this.cookieService.delete('auth-mode', '/', '', false, 'Lax');
      console.log('app.component::ssoLogout: coockieService authMode deleted');
    }
    
    switch (this.sharedService.UserLogged.authMode) {
      case 'USER-PWD':
        this.currentUser = null;
        console.log('logout user-pwd');
        this.router.navigate(['auth/user-pwd']);
        break;
      default:
        console.log('logout iride: ' + this.sharedService.UserLogged.urlLogout);
        this.router.navigate(['/']).then(
          result => {
            window.location.href = this.sharedService.UserLogged.urlLogout;
          }
      );
        break;
    }

  }

/*
  ssoLogout() {
    // reset server session
    this.securityService.localLogout().subscribe(
      data => {
        console.log('logout data = ' + data);
        StorageManager.clear();
        const urlLogOut = data as string;
        console.log('Url logout ' + urlLogOut);
        this.router.navigate(['/']).then(
          result => {
            window.location.href = urlLogOut;
          }
        );
      },
      err => {
        if (err.error) {
          const notifica = new Notifica(err.error.code, err.error.msg, err.error.title, null);
          notifica.showBack = false;
          this.securityService.goToNotificationError(notifica);
        } else {
          const notifica = new Notifica(err.status, err.statusText, 'ERRORE SISTEMA', null);
          notifica.showBack = false;
          this.securityService.goToNotificationError(notifica);
        }
      }
    );
  }
  */

  istanze() {
    this.router.navigate(['istanze']);
    this.disableAllTab();
    this.isIstanze = true;
  }

  notifiche() {
    this.router.navigate(['notifiche']);
    this.disableAllTab();
    this.isNotifiche = true;
  }

  nuovaIstanza() {
    //this.router.navigate(['nuova-istanza']);
    this.router.navigate(['categorie']);
    this.disableAllTab();
    this.isNuovaIstanza = true;
  }

  cercaIstanza() {
    this.router.navigate(['cerca-istanza']);
    this.disableAllTab();
    this.isCercaIstanza = true;
  }

  report() {
    this.router.navigate(['report']);
    this.disableAllTab();
    this.isReport = true;
  }

  exportIstanze() {
    this.router.navigate(['export-istanze']);
    this.disableAllTab();
    this.isExportIstanze = true;
  }

  moduli() {
    this.router.navigate(['mieiModuli']);
    this.disableAllTab();
    this.isModuli = true;
  }

  utenti() {
    this.router.navigate(['utenti']);
    this.disableAllTab();
    this.isUtenti = true;
  }

  aggiornaEnte(resp) {
    console.log("aggiornaEnte() url logo = " + resp?JSON.stringify(resp):'NULL OR undefined');
    this.isScegliEnte = false;
    this.nomeEnte = resp?.body?.ente?.nomeEnte;
    if (resp?.body?.ente?.logo != null && resp?.body?.ente?.logo != 'null') {
      this.urlLogoEnte = environment.pathAssets + '/im/logo-ente/' + resp?.body?.ente?.logo;
    } else {
      this.urlLogoEnte = environment.pathAssets + '/im/moon.png';
    }
    this.showNavMenu = true;
    console.log("aggiornaEnte() url logo = " + this.urlLogoEnte);
  }

  disableAllTab() {
    this.isIstanze = false;
    this.isNotifiche = false;
    this.isCercaIstanza = false;
    this.isReport = false;
    this.isExportIstanze = false;
    this.isModuli = false;
    this.isUtenti = false;
    this.isNuovaIstanza = false;
    this.isProcessi = false;
  }


    processi() {
      this.router.navigate(['processi']);
      this.disableAllTab();
      this.isProcessi = true;
    }
}
