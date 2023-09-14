/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {StorageManager} from '../util/storage-manager';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {environment} from 'src/environments/environment';

@Injectable()
export class PpayInterceptor implements HttpInterceptor {

  constructor() {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = request;
    if (request.url.includes(environment.urlPiemontePay)) {
      const jwt = StorageManager.get(STORAGE_KEYS.JWT_MOON);
      if (jwt != null) {
        authReq = request.clone(
          {
            setHeaders: { 'Moon-Identita-JWT': jwt}
          }
        );
      } else {
        throw new Error(('Errore Token jwt non valorizzato !'));
      }
    }
    return next.handle(authReq);
  }
}
