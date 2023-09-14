/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import { STORAGE_KEYS } from '../common/costanti';
import {StorageManager} from '../common/utils/storage-manager';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor() {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    /*
    In caso di login non devo passare il token
     */
    if (req.url.includes('/restfacade/be')) {
      const jwt = StorageManager.get(STORAGE_KEYS.MOON_JWT_TOKEN);
      console.log('JwtInterceptor ADD HEADER Moon-Identita-JWT with value ' + jwt);
      if (jwt != null) {
        authReq = req.clone(
          {
            setHeaders: {'Moon-Identita-JWT': jwt}
          }
        );
      } else {
        throw new Error(('Errore Token jwt non valorizzato !'));
      }
    }


    return next.handle(authReq);

  }


}

