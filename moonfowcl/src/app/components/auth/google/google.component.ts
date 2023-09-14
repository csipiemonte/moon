/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {GoogleLoginProvider, SocialAuthService, SocialUser} from 'angularx-social-login';
import {ActivatedRoute, Router} from '@angular/router';
import {SecurityService} from 'src/app/services/security.service';
import {LoginService} from 'src/app/services/login.service';
import {User} from 'src/app/model/common/user';
import {ModalAlertComponent} from '../../modal-alert/modal-alert.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {environment} from 'src/environments/environment';
import {NgEventBus} from 'ng-event-bus';

@Component({
  selector: 'app-google',
  templateUrl: './google.component.html',
  styleUrls: ['./google.component.scss']
})

export class GoogleComponent implements OnInit {

  socialUser!: SocialUser;
  codiceModulo: string;
  codiceEnte: string;
  codiceAmbito: string;
  urlAccediGoogle = environment.pathAssets + '/im/top-user-google-signin.png';

  constructor(
    private router: Router,
    private socialAuthService: SocialAuthService,
    private securityService: SecurityService,
    private loginService: LoginService,
    private acRoute: ActivatedRoute,
    private modalService: NgbModal,
    private eventBus: NgEventBus
  ) {

  }

  ngOnInit(): void {
    this.codiceModulo = this.acRoute.snapshot.queryParamMap.get('codice_modulo');
    this.codiceEnte = this.acRoute.snapshot.queryParamMap.get('codice_ente');
    this.codiceAmbito = this.acRoute.snapshot.queryParamMap.get('amb');

    console.log('Google.component: codice_modulo ' + this.codiceModulo);
    console.log('Google.component: codice_ente ' + this.codiceEnte);
    console.log('Google.component: amb ' + this.codiceAmbito);
  }

  public socialSignIn(socialProvider: string) {
    let socialPlatformProvider;

    //if (socialProvider === 'facebook') {
    //  socialPlatformProvider = FacebookLoginProvider.PROVIDER_ID;
    //}

    if (socialProvider === 'GOOGLE') {
      socialPlatformProvider = GoogleLoginProvider.PROVIDER_ID;
    }

    this.socialAuthService.signIn(socialPlatformProvider).then((socialuser) => {
        console.log('Prfovider: =' + socialProvider);
        console.log('Social user: ' + JSON.stringify(socialuser));
        this.login(socialuser, socialProvider);
      },
      (err) => {
        console.log('Errore in signIn Social');
        console.error(JSON.stringify(err));
      });
  }

  async login(socialuser: SocialUser,socialProvider: string) {

    try {

      if (socialProvider === 'GOOGLE') {
        //x validare il google token
        const loginResponse = await this.securityService.loginSocial(socialuser,socialProvider).toPromise() ;
        console.log('loginSocial eseguita');
        loginResponse.authMode = 'GOOGLE';
        this.eventBus.cast('authenticazione:set-current-user', loginResponse);

        console.log('_autenticazioneService.sendDataUserAuhenticated eseguita');

        const queryString = this.getQueryString();
        console.log('getQueryString eseguita');
        const user = new User(loginResponse);
        console.log('chiamo login');
        this.loginService.login(user, queryString);
        console.log(' login eseguita');
      }
    } catch (e) {

      switch (e.status) {
        case 401:
          const mdRef = this.modalService.open(ModalAlertComponent);
          mdRef.componentInstance.modal_titolo = 'Errore';
          mdRef.componentInstance.modal_contenuto = 'Utente non autorizzato. User/password non corretti!';
          mdRef.result.then((result) => {
            console.log('Closed with: ${result}' + result);
            this.router.navigate(['/auth/social']);

          }, (reason) => {
            console.log(reason);
          });
          break;
        default:
          const mdRef2 = this.modalService.open(ModalAlertComponent);
          mdRef2.componentInstance.modal_titolo = 'Errore';
          mdRef2.componentInstance.modal_contenuto = 'Errore nel servizio di autenticazione!';
          console.log('STAMPA ERRORE google component: ' + JSON.stringify(e));
          mdRef2.result.then((result) => {
            console.log('Closed with: ${result}' + result);
            this.router.navigate(['/auth/social']);

          }, (reason) => {
            console.log(reason);
          });
      }

    }
  }

  getQueryString(): string {
    let q: string;
    q = this.accodaQueryParam(q, 'codice_modulo', this.codiceModulo);
    q = this.accodaQueryParam(q, 'codice_ente', this.codiceEnte);
    q = this.accodaQueryParam(q, 'amb', this.codiceAmbito);
    console.log('Querystring: ' + q);
    return q;
  }

  accodaQueryParam(queryStringResult: string, nomeParam: string, valore: any): string {
    if (valore) {
      if (queryStringResult && queryStringResult.length > 0) {
        queryStringResult = queryStringResult + '&' + nomeParam + '=' + valore;
      } else {
        queryStringResult = nomeParam + '=' + valore;
      }
    }
    return queryStringResult;
  }

}
