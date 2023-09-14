/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable, isDevMode} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';

@Injectable()
export class SimulatePortaleInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // In sviluppo passo il portale al login per simulare accesso portale
    if (isDevMode && req.url.includes('/restfacade/')) {
      let newHttpParams = req.params;
      newHttpParams = newHttpParams.append('Simulate-Portale', environment.simulatePortale);
      const authReq = req.clone({params: newHttpParams});
      return next.handle(authReq);
    } else {
      return next.handle(req);
    }
  }


}

