/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable, isDevMode} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';

@Injectable()
export class FakeIrideInterceptor implements HttpInterceptor {

  constructor() {
  }

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let authReq = req;
    /*
       In ambiente sviluppo se invoco idp, simulo autenticazione iride
     */
    if (req.url.includes('/moonfobl/restfacade/auth/login/idp') && isDevMode()) {
      const identiTaDigitale = environment.identitaIrideParameter;
      if (identiTaDigitale !== null) {
        authReq = req.clone(
          {
            setHeaders: {'Shib-Iride-IdentitaDigitale': identiTaDigitale}
          }
        );
      } else {
        throw new Error(('Errore Identita Iride valorizzato !'));
      }
    }

    return next.handle(authReq);

  }

}

