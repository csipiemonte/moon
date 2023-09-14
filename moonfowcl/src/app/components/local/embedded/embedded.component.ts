/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {NgEventBus} from 'ng-event-bus';
import {CookieService} from 'ngx-cookie-service';
import {EMB_SERVICE_NAME} from 'src/app/model/common/embedded-constants';
import {EmbeddedOptions} from 'src/app/model/common/embedded-options';
import {User} from 'src/app/model/common/user';
import {LoginService} from 'src/app/services/login.service';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {SecurityService} from '../../../services/security.service';
import { StorageManager } from '../../../util/storage-manager';


@Component({
  selector: 'app-embedded',
  templateUrl: './embedded.component.html',
  styleUrls: ['./embedded.component.css']
})
export class EmbeddedComponent implements OnInit {

  constructor(
    private securityService: SecurityService,
    private cookieService: CookieService,
    private loginService: LoginService,
    private eventBus: NgEventBus
  ) {

  }


  ngOnInit() {

    try {

      const qryParams = window.location.search;
      const urlParams = new URLSearchParams(qryParams);
      const embedded = urlParams.get('embedded');

      console.log("*** embedded = " + embedded);
      const embObj = JSON.parse(embedded);
      console.log("*** emb object = " + embObj);

      // const service = this.acRoute.snapshot.params.service;
      // const codiceIstanza = this.acRoute.snapshot.params.codiceIstanza;
      // const codiceModulo = this.acRoute.snapshot.params.codiceModulo;
      // const token = this.acRoute.snapshot.params.token;

      const service = embObj?.service;
      const codiceIstanza = embObj?.params?.codiceIstanza;
      const codiceModulo = embObj?.params?.codiceModulo;
      const token = embObj?.token;

      console.log('[embedded::ngOnInit] service=' + service);
      console.log('[embedded::ngOnInit] codiceIstanza=' + codiceIstanza);
      console.log('[embedded::ngOnInit] codiceModulo=' + codiceModulo);
      console.log('[embedded::ngOnInit] token=' + token);

      switch (service) {

        case EMB_SERVICE_NAME.ISTANZE:
          this.goToIstanze(service,token);
          break;

        case EMB_SERVICE_NAME.NEW_ISTANZA:
          if (codiceModulo) {
            this.goToNewIstanza(codiceModulo,service,token);
          } else {
            console.log('[embedded::ngOnInit] codiceModulo non presente');
          }

          break;

        case EMB_SERVICE_NAME.VIEW_ISTANZA:
        case EMB_SERVICE_NAME.EDIT_ISTANZA:
        case EMB_SERVICE_NAME.ISTANZA:

          if (codiceIstanza) {
            this.goToIstanza(codiceIstanza,service,token);
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

    console.log('embedded.component::ngOnInit: jwt-moon impostato');
    this.eventBus.cast('authenticazione:set-current-user', cuser);
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

  fromBinary(encoded) {
    const binary = atob(encoded);
    return decodeURIComponent(escape(binary));
  }

}
