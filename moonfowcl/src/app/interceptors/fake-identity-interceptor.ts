/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';

@Injectable()
export class FakeIdentityInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // inserimento parametri solo per ambiente locale
    // test = produzione
    if (!environment.production) {
      if (req.url.includes('/idp/gasprp_salute')) {
        console.log('Interceptor fake iride');
        let newHttpParams = req.params;
        newHttpParams = newHttpParams.append('Simulate-Portale', environment.simulatePortale);
        const authReq = req.clone({params: newHttpParams});
        return next.handle(authReq);
      }
    } else {
      return next.handle(req);
    }
    return next.handle(req);
  }


}

