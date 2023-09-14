/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { HttpEvent, HttpHandler, HttpInterceptor, HttpParams, HttpRequest } from '@angular/common/http';
import { Injectable, isDevMode } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable()
export class FakeIdentityInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!environment.production) {
      console.log('Interceptor fake iride');
      let newHttpParams = req.params;
      newHttpParams = newHttpParams
        .append('Shib-Iride-IdentitaDigitale', environment.identitaIrideParameter)
        .append('Simulate-Portale', environment.simulatePortale);
      const authReq = req.clone({ params: newHttpParams });

      //console.log('request = '+JSON.stringify(authReq));

      return next.handle(authReq);
    } else {
      return next.handle(req);
    }
  }


}

