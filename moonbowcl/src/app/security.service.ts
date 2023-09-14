/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable, isDevMode} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Router } from '@angular/router';
import {ConfigService} from './config.service';
import {StorageManager} from './common/utils/storage-manager';

// fixme import 'rxjs/add/operator/toPromise';

// fixme import {UserInfo} from './model/common/user-info';
import {SrvError} from './common/srv-error';
import {InfoNavigation} from './common/info-navigation';
import {tap} from 'rxjs/operators';
import { Notifica } from './model/dto/notifica';
import { SharedService } from './services/shared.service';
import { Nav } from './model/dto/nav';
import { AUTH_MODES, STORAGE_KEYS } from './common/costanti';
import { AutenticazioneService } from './services/notifiers/autenticazione.service';
import { LoginResponseIF } from './model/common/login-response';
import { environment } from 'buildfiles/environment.local-embedded';

@Injectable()
export class SecurityService {

    // fixme private user: UserInfo;

    constructor(
      private http: HttpClient,
      private router: Router,
      private config: ConfigService,
      private _autenticazioneService: AutenticazioneService,
      private sharedService: SharedService) {
    }

    getCurrentUser() {
      const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/currentUser';
        return this.http.get<any>(url).pipe(
            tap(
                (user: any) => {
                },
                (httpResponse: HttpErrorResponse)  => {
                    throw httpResponse;
                }
            )
        );
    }

    /**
     * effettua l'invalidazione della sessione applicativa, sia sul
     * client che sul server, richiamando un apposito servizio di backend
     */
/*
    localLogout() {
      return this.http.get(this.config.getBEServer() + '/moonbobl/restfacade/be/localLogout', { responseType: 'text' });
    }
    ssoLogout(): void {
      this.router.navigate(['/']).then(result => { window.location.href = this.config.getSSOLogoutURL(); });
    }
    */


    /* fixme
    private static handleError(error: any): Promise<any> {
      console.error('An error occurred', error); // for demo purposes only
      return Promise.reject(error.message || error);
    }
    */
    jumpToCourtesyPage(srvError: SrvError) {
      // logica per Messaggi
      this.router.navigate(['/error'], { queryParams: { message: srvError.messaggioCittadino }, skipLocationChange: true });
    }

    // jump to pagina errore
    jumpToErrorPage(srvError: SrvError) {
      // logica per Messaggi
      this.router.navigate(['/errorPage'], { queryParams: { message: srvError.messaggioCittadino }, skipLocationChange: true });
    }

    jumpToErrorPageNav(srvError: SrvError, infoNavigation: InfoNavigation) {
      // logica per Messaggi
      this.router.navigate(['/errorPage'], { queryParams: {
        message: srvError.messaggioCittadino,
        errorMessage: srvError.errorMessage,
        from: infoNavigation.from,
        page: infoNavigation.page }, skipLocationChange: true });
    }

    goToNotificationError(notifica: Notifica) {
      //const notifica = new Notifica('AVVISO', srvError.errorMessage, errore.error.titolo, null);
      this.sharedService.notifica = notifica;
      this.sharedService.nav = new Nav(this.sharedService.nav.active, 'notifica-segnalazione');
      this.router.navigate(['notifica-segnalazione']);
    }

    loginUserPassword(user: string, password: string) {
      const payload = {
        'login': user,
        'password': password,
        'logonMode': AUTH_MODES.LOGIN_USER_PWD
      };
      const url = this.config.getBEServer() + '/moonbobl/restfacade/auth/login/request';
      return this.http.post<LoginResponseIF>(url, payload);
    }

}
