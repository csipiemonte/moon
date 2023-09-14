/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {SocialAuthService} from 'angularx-social-login';
import {NgEventBus} from 'ng-event-bus';
import {MetaData} from 'ng-event-bus/lib/meta-data';
import {CookieService} from 'ngx-cookie-service';
import {Subscription} from 'rxjs-compat';
import {CONSUMER_TEMPLATE} from 'src/app/model/common/consumer-constants';
import {ConsumerParams} from 'src/app/model/common/consumer-params';
import {User} from 'src/app/model/common/user';
import {environment} from '../../../../environments/environment';
import {NavSelection} from '../../../common/nav-selection';
import {SrvError} from '../../../common/srv-error';
import {UserInfo} from '../../../model/common';
import {Nav} from '../../../model/dto/nav';
import {ConfigService} from '../../../services/config.service';
import {ObserveService} from '../../../services/observe.service';
import {SecurityService} from '../../../services/security.service';
import {SharedService} from '../../../services/shared.service';
import {StorageManager} from '../../../util/storage-manager';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  providers: [ConfigService]
})
export class HeaderComponent implements OnInit, OnDestroy {
  @Input() currentUser: UserInfo;
  mostraMenuMioFacile = false;
  sottotitolo = '';
  urlLogo = '';
  nomeEnte = '';
  isMultiEnte = false;
  urlLogoEnte = environment.pathAssets + '/im/logo_moon.svg';
  urlLogoutQueryParam = null;
  activeId: number;
  caller: string;
  isNavNotVisible: boolean = false;
  disabled: string = "";
  subscribers: Subscription = new Subscription();
  template: string = '';
  ID_BACHECA = NavSelection.BACHECA;
  consumerParams: ConsumerParams;

  constructor(
    private securityService: SecurityService,
    private config: ConfigService,
    private router: Router,
    private socialAuthService: SocialAuthService,
    private sharedService: SharedService,
    private observeService: ObserveService,
    private cookieService: CookieService,
    private eventBus: NgEventBus
  ) {
    this.activeId = this.sharedService.nav.active;
    this.ID_BACHECA = NavSelection.BACHECA;
    this.setSubscribers();
  }

  setSubscribers() {

    this.subscribers.add(
      this.eventBus.on('authenticazione:set-current-user').subscribe((meta: MetaData) => {
        console.log(meta.data);
        this.currentUser = new User(meta.data);
        this.currentUser.isAuthenticated = true;
        console.log('header.component::constructor: user authenticated ' + JSON.stringify(this.currentUser));
      }));

    if (this.template === '') this.template = CONSUMER_TEMPLATE.MOON;
    this.subscribers.add(
      this.eventBus.on('microfe:set').subscribe((meta: MetaData) => {
        console.log(meta.data);
        if (meta.data.consumerParams) {
          console.log(' *** HEADER.component::setSubscribers: consumer ' + JSON.stringify(meta.data.consumerParams) + ' ***');
          console.log(' *** HEADER.component::setSubscribers: template  before set' + this.template + ' ***');
          this.template = meta.data.consumerParams.consumer;
          console.log(' *** HEADER.component::setSubscribers: template after set' + this.template + ' ***');
          this.consumerParams = meta.data.consumerParams;
        }
      })
    );

    this.subscribers.add(
      this.observeService.getEventUrlLogout().subscribe(
        (urlLogout: string) => {
          console.log('SUBSCRIBE URL LOGOUT: ' + urlLogout);
          this.urlLogoutQueryParam = urlLogout;
        }
      ));

    this.subscribers.add(
      this.eventBus.on('multi-ente:set').subscribe((meta: MetaData) => {
        console.log(meta.data);
        this.setMultiEnte(meta.data);
        this.isNavNotVisible = false;
      }));

    this.subscribers.add(
      this.eventBus.on('multi-ente:choose').subscribe((meta: MetaData) => {
        console.log(meta.data);
        this.isNavNotVisible = true;
      }));

    this.subscribers.add(
      this.eventBus.on('active-nav-bar:disable').subscribe((meta: MetaData) => {
        console.log(meta.data);
        if (meta.data) {
          this.activeId = undefined;
        }
      }));

    this.subscribers.add(
      this.eventBus.on('active-nav-bar:enable').subscribe((meta: MetaData) => {
        console.log(meta.data);
        if (meta.data) {
          this.activeId = meta.data;
        }
      }));

    this.subscribers.add(
      this.eventBus.on('nav-bar:hide').subscribe((meta: MetaData) => {
        console.log(meta.data);
        if (meta.data) {
          this.isNavNotVisible = true;
        } else {
          this.isNavNotVisible = false;
        }
      }));

    this.subscribers.add(
      this.eventBus.on('nav-bar:anchor:disable').subscribe((meta: MetaData) => {
        console.log(meta.data);
        if (meta.data) {
          this.disabled = "disabled";
        } else {
          this.disabled = "";
        }
      }));
  }


  ngOnInit() {

    this.setInitialRouting();

    this.setMultiEnte(this.currentUser);

    const hostname = window.location.hostname;
    console.log('hostname: ' + hostname);
    if (hostname === 'moon-torinofacile.patrim.csi.it' || hostname === 'tst-moon-torinofacile.patrim.csi.it') {
      this.mostraMenuMioFacile = true;
      this.urlLogo = environment.pathAssets + '/im/logo-ente/' + 'logotorinofacile.png';
      this.sottotitolo = 'Modulistica Online della Città di Torino';
    } else if (hostname === 'tst-moon-internet.patrim.csi.it' || hostname === 'moon.patrim.csi.it') {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo-ente/' + 'logo_RegPiem.jpg';
      this.sottotitolo = 'Modulistica Online della Regione Piemonte';
    } else if (hostname === 'tst-moon-ru.patrim.csi.it' || hostname === 'moon-ru.patrim.csi.it') {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo-ente/' + 'logo_RegPiem.jpg';
      this.sottotitolo = 'Modulistica Online della Regione Piemonte';
    } else if (hostname === 'tst-moon-cittametropolitanatorino.patrim.csi.it' ||
      hostname === 'moon-cittametropolitanatorino.patrim.csi.it' ||
      hostname === 'tst-moon-bo-cmto.patrim.csi.it' ||
      hostname === 'moon-bo-cmto.patrim.csi.it') {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo-ente/' + 'logo_cmto.jpg';
      this.sottotitolo = 'Modulistica Online della Città Metropolitana di Torino';
      // *** città di Biella
    } else if (hostname === 'tst-biella-moon.csi.it' || hostname === 'biella-moon.csi.it') {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo-ente/' + 'logo-Biella.jpg';
      this.sottotitolo = 'Modulistica Online della Città di Biella';
      // *** città facile per ora lascio logo default
    } else if (hostname === 'tst-moon.csi.it' || hostname === 'moon.csi.it') {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo_moon.svg';
      this.sottotitolo = 'Modulistica Online';
      // *** Ambiente demo
    } else if (hostname === 'tst-demo-moon.csi.it' || hostname === 'demo-moon.csi.it') {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo-ente/' + 'logo-Bugliano.png';
      this.sottotitolo = 'Modulistica Online Ambiente DEMO - CSI Piemonte';
    } else if (hostname === 'cittametropolitanatorino-moon.csi.it' ||
      hostname === 'rupar-cittametropolitanatorino-moon.csi.it') {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo-ente/' + 'logo_cmto.jpg';
      this.sottotitolo = 'Modulistica Online della Città Metropolitana di Torino';
      // *** città di Biella
    } else if (hostname === 'localhost') {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo_moon.svg';
      this.sottotitolo = 'La tua corsia preferenziale per presentare un\'istanza';
    } else if (hostname === 'regionepiemonte-moon.csi.it' ||
      hostname === 'piemontetu-moon.csi.it' ||
      hostname === 'rupar-regionepiemonte-moon.csi.it' ||
      hostname === '') {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo-ente/' + 'logo_RegPiem.jpg';
      this.sottotitolo = 'Modulistica Online della Regione Piemonte';
    } else if (hostname === 'torinofacile-moon.csi.it' || hostname === 'extracom-moon.csi.it') {
      this.mostraMenuMioFacile = true;
      this.urlLogo = environment.pathAssets + '/im/logo-ente/' + 'logotorinofacile.png';
      this.sottotitolo = 'Modulistica Online della Città di Torino';
    } else {
      this.mostraMenuMioFacile = false;
      this.urlLogo = environment.pathAssets + '/im/logo_moon.svg';
      this.sottotitolo = 'Modulistica Online';
    }

  }

  getUrlLogout() {
    if (this.urlLogoutQueryParam) {
      return this.urlLogoutQueryParam;
    } else {
      return this.currentUser.urlLogout;
    }
  }

  /*
   Chiude sessione applicativa e SSO Shibboleth
   */
  ssoLogout() {
    // reset server session
    console.log('header.component::ssoLogout: StorageManager clear');
    StorageManager.clear();
    console.log('header.component::ssoLogout: coockieService check');

    if (this.cookieService.check('loginResponse')) {
      this.cookieService.delete('loginResponse', '/', '', false, 'Lax');
      console.log('header.component::ssoLogout: coockieService loginResponse deleted');
    }
    if (this.cookieService.check('auth-mode')) {
      this.cookieService.delete('auth-mode', '/', '', false, 'Lax');
      console.log('header.component::ssoLogout: coockieService authMode deleted');
    }
    switch (this.currentUser.authMode) {
      case 'USER-PWD':
        this.currentUser = null;
        console.log('logout user-pwd');
        this.router.navigate(['auth/user-pwd']);
        break;
      case 'GOOGLE':
        this.currentUser = null;
        try {
          this.socialAuthService.signOut();
        } catch (e) {
          console.log(e);
        }
        console.log('logout google');
        // default: moon
        this.urlLogoEnte = environment.pathAssets + '/im/logo_moon.svg';
        this.isMultiEnte = false;
        this.router.navigate(['auth/social']);

        break;
      default:
        console.log('logout iride: ' + this.getUrlLogout());
        window.location.href = this.getUrlLogout();
        break;
    }
  }


  localLogout(nextUrl) {

    console.log('header.component::localLogout: check nextUrl');
    if (nextUrl === '') {
      nextUrl = this.config.getOnAppExitURL();
    }
    console.log('header.component::localLogout: url ' + nextUrl);
    // reset server session
    this.securityService.localLogout().then(
      response => {
        StorageManager.clear();
        this.router.navigate(['/']).then(
          result => {

            console.log('header.component::localLogout: go to url ' + nextUrl);
            window.location.href = nextUrl;
          }
        );
      },
      err => {
        console.log('header.component::localLogout: logout to ' + nextUrl + ' fallito');

        const srvError: SrvError = new SrvError();
        srvError.code = '' + err.status;
        srvError.errorMessage = err.message;
        srvError.messaggioCittadino = 'Esci da applicazione Moon fallito !';
        srvError.sessionExpired = false;
        this.securityService.jumpToCourtesyPage(srvError);
      }
    );

  }

  setMultiEnte(userInfo: UserInfo) {
    if (userInfo && userInfo.multiEntePortale) {
      this.isMultiEnte = true;
      this.nomeEnte = userInfo?.ente?.nomeEnte;
      if (userInfo?.ente?.logo) {
        this.urlLogoEnte = environment.pathAssets + '/im/logo-ente/' + userInfo.ente.logo;
      } else {
        // default: moon
        this.urlLogoEnte = environment.pathAssets + '/im/logo_moon.svg';
      }
    }
  }

  scegliEnte() {

    setTimeout(() => {
      this.eventBus.cast('multi-ente:choose', 'scelta ente');
      this.sharedService.nav = new Nav(NavSelection.ENTE, 'home/scegli-ente');

      this.router.navigate(['home']);
    }, 1000);

  }

  ngOnDestroy() {
    this.subscribers.unsubscribe();
  }

  hasPhotoUrl() {
    return this.currentUser?.photoUrl;

  }

  setInitialRouting() {
    if (this.sharedService.nav.active === NavSelection.ENTE) {
      this.isNavNotVisible = true;
    } else {
      this.isNavNotVisible = false;
    }
    this.activeId = this.sharedService.nav.active;
  }

  get(tag) {

    let navItem;

    switch (tag) {
      case 'ENTE':
        navItem = NavSelection.ENTE;
        break;
      case 'BACHECA':
        navItem = NavSelection.BACHECA;
        break;
      case 'ISTANZE':
        navItem = NavSelection.ISTANZE;
        break;
      case 'NUOVA_ISTANZA':
        navItem = NavSelection.NUOVA_ISTANZA;
        break;
      case 'CERCA_ISTANZA':
        navItem = NavSelection.CERCA_ISTANZA;
        break;
      case 'COMUNICAZIONI':
        navItem = NavSelection.COMUNICAZIONI;
        break;
      default:
        navItem = NavSelection.BACHECA;
    }

    return navItem;

  }

  goToAiuto() {
    this.activeId = null;
    this.router.navigate(["aiuto"]);
  }

  scrollTo(element: any): void {
    (document.getElementById(element) as HTMLElement).scrollIntoView({ behavior: "smooth", block: "start", inline: "nearest" });
  }

}
