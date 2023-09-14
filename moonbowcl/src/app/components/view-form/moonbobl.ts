/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';

import { HttpClientModule, HttpParams, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpErrorHandler, HandleError } from '../../services/http-error-handler.service';
import {Modulo} from '../../model/dto/modulo';
import { timeout, retryWhen, catchError } from 'rxjs/operators';
import { handleError, selectiveRetryStrategy } from '../../services/service.utils';
import { SrvError } from '../../common/srv-error';
import { CustomFileService } from 'src/app/plugin/custom-file.service';
@Injectable()
export class Moonbobl {
  private  inc: number;
  serviceUrl = '/moonfobl/restfacade/be/';
  private handleError: HandleError;

  FileService: CustomFileService;

  constructor(
    private http: HttpClient,
    httpErrorHandler: HttpErrorHandler,
    private customFileService: CustomFileService) {
    this.handleError = httpErrorHandler.createHandleError('Moonfobl');
    this.inc = 0;
    this.FileService = customFileService;
  }

  getModulo(idModulo: number): Observable<Modulo> {
    return <Observable<Modulo>>this.http.get<Modulo>(this.serviceUrl + 'moduli/' + idModulo)
      .pipe(
        catchError(this.handleError('getModulo', idModulo))
      );
  }

  getFormDefinition(idStruttura: number): Observable<any | SrvError> {
   const errorHandler = handleError.bind(this);
    this.inc = this.inc + 1;
    console.log(' Inc chiamata: ' + this.inc);
    return this.http.get<any>(this.serviceUrl + 'moduli/struttura/' + idStruttura).pipe(
      timeout(3000),
      retryWhen(selectiveRetryStrategy({ scalingDuration: 100, excludedStatusCodes: [302, 404, 0], maxRetryAttempts: 0 })),
      catchError(errorHandler)
    );
  }




}



