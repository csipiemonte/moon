/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {HEADERS_MOON_IDENTITA_JWT} from './../common/costanti';
import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {ConfigService} from './config.service';
import 'rxjs/add/operator/toPromise';
import {UserInfo} from '../model/common/user-info';
import {SrvError} from '../common/srv-error';
import {Notifica} from '../model/dto/notifica';
import {Nav} from '../model/dto/nav';
import {SharedService} from './shared.service';
import {AUTH_MODES, STORAGE_KEYS} from 'src/app/common/costanti';
import {tap} from 'rxjs/operators';
import {StorageManager} from '../util/storage-manager';
import {UserIF} from '../model/common/user-if';
import {SocialUser} from 'angularx-social-login';
import {NgEventBus} from 'ng-event-bus';
import {EMB_SERVICE_NAME} from '../model/common/embedded-constants';


@Injectable()
export class SecurityService {

  private user: UserInfo;

  constructor(
    private http: HttpClient,
    private router: Router,
    private config: ConfigService,
    private sharedService: SharedService,
    private eventBus: NgEventBus) {
  }


  getCurrentUser() {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/currentUser';
    console.log('BE server: ' + this.config.getBEServer());
    console.log(url);
    return this.http.get(url);
  }

  // servizio  autenticazione mediante identity provider shibboleth
  autenticaUtenteByShibboleth(qryParams: string) {
    //  Test locale gruppoOperatoreFo
    //  const url: string = this.config.getBEServer() + '/moonfobl/restfacade/auth/login/idp/gasprp_salute?gope=SOCIAL';
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/auth/login/idp/gasprp_salute' + qryParams;
    // const url: string = this.config.getBEServer() + '/moonfobl/restfacade/auth/login/idp/gasprp_salute?amb=DEFAULT';
    // const url: string = this.config.getBEServer() + '/moonfobl/restfacade/auth/login/idp/gasprp_salute';
    // const url: string = this.config.getBEServer() + '/moonfobl/restfacade/auth/login/idp/gasprp_salute?gope=A_CAF_001';
    return this.http.post(url, {});
  }

  autenticaEmbIstanza(codiceIstanza: string, service: string, token: string) {

    let headers = new HttpHeaders().append(HEADERS_MOON_IDENTITA_JWT, token);
    const httpOptions = { headers };
    let url: string;
    if (service && service.toUpperCase() != EMB_SERVICE_NAME.ISTANZA){
      url = this.config.getBEServer() + '/moonfobl/restfacade/emb/istanze/'+codiceIstanza+'/'+service;
    } else{
      url = this.config.getBEServer() + '/moonfobl/restfacade/emb/istanze/'+codiceIstanza;
    }

    console.log("*** token impostato in header  = "+token);
    console.log("*** url servizio autentica emb istanza  = "+url+" ****");
    return this.http.get(url, httpOptions);
  }

  autenticaEmbIstanze(service: string, token: string) {
    let headers = new HttpHeaders().append(HEADERS_MOON_IDENTITA_JWT, token);
    const httpOptions = { headers };
    let url: string;
    url = this.config.getBEServer() + '/moonfobl/restfacade/emb/istanze';
    return this.http.get(url, httpOptions);
  }

  autenticaEmbModulo(codiceModulo: string, service: string, token: string) {
    let headers = new HttpHeaders().append(HEADERS_MOON_IDENTITA_JWT, token);
    const httpOptions = { headers };
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/emb/moduli/'+codiceModulo+'/'+service;
    return this.http.get(url, httpOptions);
  }


  loginUserPassword(user: string, password: string) {
    const payload = {
      'login': user,
      'password': password,
      'logonMode': AUTH_MODES.LOGIN_USER_PWD
    };
    //TODO: si devo passare anche i 3 par: codiceente, amb ...
    const queryString = window.location.search;
    var url = this.config.getBEServer() + '/moonfobl/restfacade/auth/login/request';
    if(queryString != null && queryString.length>0){
       url = this.config.getBEServer() + '/moonfobl/restfacade/auth/login/request'+queryString;
    }
    
    return this.http.post<UserIF>(url, payload)
      .pipe(tap(
        (response) => {
          StorageManager.add(STORAGE_KEYS.JWT_MOON, response.idMoonToken);
          this.eventBus.cast('authenticazione:set-current-user', response);
        },
        (httpResponse: HttpErrorResponse) => {
          throw httpResponse;
        }
      ));
  }

  loginSocial(socialuser: SocialUser, provider: string) {

    const loginResquest = {
      'logonMode': provider,
      'socialUser': JSON.stringify(socialuser)
    };

    console.log('loginSocial -- chiamo /moonfobl/restfacade/auth/login/idp-social/GOOGLE');
    const url = this.config.getBEServer() + '/moonfobl/restfacade/auth/login/idp-social/' + provider;
    console.log('loginSocial -- URL: ' + url);
    return this.http.post<UserIF>(url, loginResquest)
      .pipe(tap(
        (response) => {
          console.log('stampo response: ' + response);
          StorageManager.add(STORAGE_KEYS.JWT_MOON, response.idMoonToken);
          console.log('agg idtokenmoon: '+response.idMoonToken );
          this.eventBus.cast('authenticazione:set-current-user', response);
          console.log('sendDataUserAuhenticated eseguita ' );
        },
        (httpResponse: HttpErrorResponse) => {
          console.log('stampo httpResponse: ' + httpResponse);
          console.log('stampo httpResponse json: ' + JSON.stringify(httpResponse));
          throw httpResponse;
        }
      ));
  }

  /**
   * effettua l'invalidazione della sessione applicativa, sia sul
   * client che sul server, richiamando un apposito servizio di backend
   */
  localLogout() {
    // fixme delete localStorage.removeItem(STORAGE_KEYS.USER);
    //StorageManager.clear();
    return this.http.get(this.config.getBEServer() + '/moonfobl/restfacade/be/localLogout', { responseType: 'text' }).toPromise();
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  jumpToCourtesyPage(srvError: SrvError) {
    // logica per Messaggi
    this.router.navigate(['/error'], { queryParams: { message: srvError.messaggioCittadino }, skipLocationChange: true });
  }

  goToNotificationError(notifica: Notifica) {
    this.sharedService.notifica = notifica;
    this.sharedService.nav = new Nav(this.sharedService.nav.active, 'home/notifica-segnalazione');
    this.router.navigate(['/home/notifica-segnalazione']);
  }


}
