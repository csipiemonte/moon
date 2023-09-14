/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ConfigService } from 'src/app/config.service';
import { SecurityService } from 'src/app/security.service';
import { UserInfo } from 'src/app//model/common/user-info';
import { faQuestionCircle } from '@fortawesome/free-solid-svg-icons';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { SharedService } from 'src/app//services/shared.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { NavSelection } from 'src/app/common/nav-selection';
import { Nav } from 'src/app/model/dto/nav';
import { ObserveService } from 'src/app/services/observe.service';
import { environment } from 'src/environments/environment';
import {CookieService} from 'ngx-cookie-service';
import {StorageManager} from 'src/app/common/utils/storage-manager';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {AutenticazioneService} from 'src/app/services/notifiers/autenticazione.service';
import { Subscription } from 'rxjs';

enum AUTH_MODE {
  USE_PWD = 'user-pwd',
  GOOGLE = 'google'
}


@Component({
  selector: 'app-shibboleth',
  templateUrl: './shibboleth.component.html',
  styleUrls: ['./shibboleth.component.scss']
})
export class ShibbolethComponent implements OnInit {

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
    private _autenticazioneService: AutenticazioneService,
    //private loginService: LoginService
  ) { 
    //dopo login usr-pwd aggiorno i dati utente
    this.autenticazioneSub = this._autenticazioneService.getDataUserAuthenticated().subscribe(user => {
      // Imposto user con tutti i dati
      this.currentUser = UserInfo.createUserInfo(user);
      this.currentUser.isAuthenticated = true;
      this.sharedService.UserLogged = this.currentUser;
      this.isMultiEnte = this.currentUser.multiEntePortale;
      this.manageMultiEnteUser(user);
      console.log('[shibboleth::constructor]: user authenticated ' + JSON.stringify(this.currentUser));
    });
  }

  ngOnInit(): void {
    const qryParams = window.location.search;
    console.log('[shibboleth::ngOnInit] qryParams=' + qryParams);

    const urlParams = new URLSearchParams(qryParams);

    this.moonboblService.autenticaUtenteByShibbolet(qryParams).subscribe(
      (response) => {
        console.log('shibbolet.component::ngOnInit: check cookie');
        if (this.cookieService.check('loginResponse') && this.cookieService.check('idMoonToken')) {
          const cookieLoginResponse = this.cookieService.get('loginResponse');
          const cookieLoginResponseDecoded = StorageManager.fromBinary(cookieLoginResponse.charAt(0) === '"' ? JSON.parse(cookieLoginResponse) : cookieLoginResponse);
          const cuser = JSON.parse(cookieLoginResponse);
          //
          const cookieIdMoonToken = this.cookieService.get('idMoonToken');
          StorageManager.clear();
          StorageManager.add(STORAGE_KEYS.MOON_JWT_TOKEN, cookieIdMoonToken);
          StorageManager.add(STORAGE_KEYS.USER_KEY, cuser);

          this.currentUser = UserInfo.createUserInfo(cuser);
          this.sharedService.UserLogged = this.currentUser;
          this.manageMultiEnteUser(this.currentUser);
          this.isMultiEnte = this.currentUser.multiEntePortale;
        } else {
          console.log('shibbolet.component::ngOnInit: Cookie non presente');
          window.location.href = 'unauthorized.html';
        }
      },
      (err) => {
        console.log(err);
      }
    );
  } catch (e) {
    console.log(e);
  
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
          console.log('SUBSCRIBE RESPONSE: ' + resp);
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
/*
  ssoLogout() {
    // reset server session
    this.securityService.localLogout().subscribe(
      data => {
        console.log('logout data = '+ data);
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
  }*/

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
    this.isScegliEnte = false;
    this.nomeEnte = resp?.body?.ente?.nomeEnte;
    if (resp?.body?.ente?.logo != null && resp?.body?.ente?.logo != 'null') {
      this.urlLogoEnte = environment.pathAssets + '/im/logo-ente/' + resp?.body?.ente?.logo;
    } else {
      this.urlLogoEnte = environment.pathAssets + '/im/moon.png';
    }
    this.showNavMenu = true;
    console.log("url logo ente = "+ this.urlLogoEnte);
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
  }

}
