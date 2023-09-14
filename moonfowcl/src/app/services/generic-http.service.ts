/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {Router} from '@angular/router';
import {catchError} from 'rxjs/operators';
import {NgEventBus} from 'ng-event-bus';
import {AlertTypeDesc} from '../common/alert-type-desc';
import {ErrorRest, Messaggi, ServiziError, TypeErrorRest} from 'src/app/common/messaggi';

@Injectable({
  providedIn: 'root'
})
export class GenericHttpService {

  constructor(private httpClient: HttpClient,
              private eventBus: NgEventBus,
              private router: Router) {
  }

  httpGet<T>(apiURL: string, options?, sendMessage?: boolean): Observable<T> {
    return this.httpClient.get<T>(apiURL, options).pipe(
      catchError((err) => this.handleError(err,sendMessage))
    ) as Observable<T>;
  }

  httpPost<V, T = void>(apiURL: string, body?: V, options?, sendMessage?: boolean): Observable<T> {
    return this.httpClient.post<T>(apiURL, body, options).pipe(
      catchError((err) => this.handleError(err,sendMessage))
    ) as Observable<T>;
  }

  httpPatch<V, T = void>(apiURL: string, body?: T, options?, sendMessage?: boolean): Observable<T> {
    return this.httpClient.patch<T>(apiURL, body, options).pipe(
      catchError((err) => this.handleError(err,sendMessage))
    ) as Observable<T>;
  }

  httpDelete<T>(apiURL: string, options?, sendMessage?: boolean): Observable<T> {
    return this.httpClient.delete<T>(apiURL, options).pipe(
      catchError((err) => this.handleError(err,sendMessage))
    ) as Observable<T>;
  }

  protected handleError(error: HttpErrorResponse, sendMessage? :boolean){
    // Handle the HTTP error here
    let msg = 'Errore';
    // let send: boolean = sendMessage ? sendMessage : true;

    let send : boolean = true;
    if ((sendMessage !== undefined) && !sendMessage){
      send = sendMessage;
    } else{
      send = true;
    }

    console.error('Errore invocazione servizio http: ', error.error);

    switch (error.status) {
      case 0: {
        msg =  ServiziError.GENERIC;
        if (send) this.eventBus.cast('alert:set', { text: msg, type: AlertTypeDesc.ERROR} );
        break;
      }
      case 401: {
        msg = ServiziError.UNAUTHORIZED;
        this.router.navigate(['forbidden']);
        break;
      }
      case 403: {
        msg = ServiziError.UNAUTHORIZED;
        this.router.navigate(['forbidden']);
        break;
      }
      case 404: {
        msg = ServiziError.NOT_FOUND;
        if (send) this.eventBus.cast('alert:set', { text: msg, type: AlertTypeDesc.ERROR} );
        break;
      }
      case 400: {
        console.error(`Errore Backend http code ${error.status}, body: `, error.error);
        msg = error?.error?.code?.startsWith('MOONFO') ? error?.error?.code + ' - ' : '';
        msg += (error?.error?.msg !== undefined ? error.error.msg : error.status);

        if(error?.error?.code === 'MOONFOBL-10011'){
          msg = Messaggi.messaggioUtenteNoAuth;
        }

        if (send) this.eventBus.cast('alert:set', { text: msg, type: AlertTypeDesc.ERROR} );

        break;
      }
      default: {
        console.error(`Errore Backend http code ${error.status}, body: `, error.error);
        msg = (error?.error?.code || '') + ' Invocazione servizi business-codice: ' + (error?.error?.msg !== undefined ? error.error.msg : error.status);
        if (send) this.eventBus.cast('alert:set', { text: msg, type: AlertTypeDesc.ERROR} );
        break;
      }
    }

    return throwError(new ErrorRest(TypeErrorRest.HTTP, msg, error.error?.code, error.error?.title));

  }
}
