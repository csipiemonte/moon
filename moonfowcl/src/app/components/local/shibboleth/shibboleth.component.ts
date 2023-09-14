/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {AfterViewChecked, AfterViewInit, Component, OnInit} from '@angular/core';
import {NgEventBus} from 'ng-event-bus';
import {CookieService} from 'ngx-cookie-service';
import {Subscription} from 'rxjs-compat';
import {ConsumerParams} from 'src/app/model/common/consumer-params';
import {User} from 'src/app/model/common/user';
import {ConsumerUtils} from 'src/app/model/util/consumer-utils';
import {LoginService} from 'src/app/services/login.service';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {SecurityService} from '../../../services/security.service';
import {StorageManager} from '../../../util/storage-manager';
import {MoonfoblService} from '../../../services/moonfobl.service';


@Component({
  selector: 'app-shibboleth',
  templateUrl: './shibboleth.component.html',
  styleUrls: ['./shibboleth.component.css']
})
export class ShibbolethComponent implements OnInit, AfterViewChecked, AfterViewInit{

  private microfeSub: Subscription;

  constructor(
    private securityService: SecurityService,
    private cookieService: CookieService,
    private loginService: LoginService,
    private eventBus: NgEventBus,
    private moonfoblservice: MoonfoblService
  ) {
  }


  ngOnInit() {
    try {
      const qryParams = window.location.search;
      console.log('[shibboleth::ngOnInit] qryParams=' + qryParams);

      const urlParams = new URLSearchParams(qryParams);

      this.securityService.autenticaUtenteByShibboleth(qryParams).subscribe(
        (response) => {
          console.log('shibboleth.component::ngOnInit: check cookie');
          if (this.cookieService.check('loginResponse')) {
            const cookie = this.cookieService.get('loginResponse');
            const cookieDecoded = StorageManager.fromBinary(cookie.charAt(0) === '"' ? JSON.parse(cookie) : cookie);
            const cuser = JSON.parse(cookieDecoded);
            StorageManager.clear();
            if (cuser && cuser.idMoonToken) {
              StorageManager.add(STORAGE_KEYS.JWT_MOON, cuser.idMoonToken);
              StorageManager.add(STORAGE_KEYS.USER, cuser);
            }
            try {
              this.cookieService.delete('loginResponse', '/');
            } catch (e) {
              console.error('Errore cancellazione cookie ', e);
            }

            // check consumer
            this.setConsumerIfPresent(cuser, urlParams, function () {
              const queryString = window.location.search;
              this.loginService.login(cuser, queryString);
            }.bind(this));

            const queryString = window.location.search;
            this.loginService.login(cuser, queryString);
          } else {
            console.log('shibboleth.component::ngOnInit: Cookie non presente');
            window.location.href = 'unauthorized.html';
          }
        },
        (err) => {
          console.log('shibboleth.component::ngOnInit: ERROR on securityService.autenticaUtenteByShibboleth() ' + err);
        }
      );
    } catch (e) {
      console.log('shibboleth.component::ngOnInit: ERROR ' + e);
    }

  }

  setConsumerIfPresent(user: User, urlParams: URLSearchParams, callback) {
    const consumer = urlParams.get('consumer');
    const consumerParams: ConsumerParams = this.getConsumerParams(consumer, urlParams)
    const consumerUtils = new ConsumerUtils(consumerParams);

    if (consumer && consumerParams.codiceEnte) {
      if (consumerUtils.isValidParameterSet() && consumerUtils.isValidConsumer()) {
        // update user with consumer
        this.moonfoblservice.getParametriConfigurazione(consumer, consumerParams.codiceEnte).subscribe(
          (params) => {
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


  fromBinary(encoded) {
    const binary = atob(encoded);
    return decodeURIComponent(escape(binary));
  }

  ngAfterViewChecked() {
    console.log('app.component::ngAfterViewChecked not implemented');
  }


  // ngAfterViewChecked() {
  ngAfterViewInit() {
    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    if (user) {
      if (!user.embeddedNavigator) {
        console.log('app.component::ngAfterViewChecked: sent data user authenticated to header subscriber');
        this.eventBus.cast('authenticazione:set-current-user', user);
      }
      if (user.embeddedNavigator || user.consumerParams) {
        this.eventBus.cast('microfe:set', user);
        console.log('app.component::ngAfterViewChecked: sent data embedded / consumer information to subscriber if needed');
      }
    } else {
      console.log('app.component::ngAfterViewChecked: user null');
    }
  }

  ngOnDestroy() {
    if (this.microfeSub) {
      this.microfeSub.unsubscribe();
    }
  }


}
