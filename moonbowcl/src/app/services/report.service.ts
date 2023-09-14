/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Injectable } from '@angular/core';
import {HttpClient, HttpClientModule, HttpHeaders, HttpParams} from '@angular/common/http';
import {ConfigService} from '../config.service';
import { Observable } from 'rxjs';

@Injectable()
export class ReportService {

  constructor(private http: HttpClient, private config: ConfigService) {}

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error.message);
    return Promise.reject(error.message || error);
  }

  getReporNumModuliInviati(): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/report';
    return this.http.get(url).toPromise()
      .then(valore => valore as any).catch(this.handleError);
  }

  getReportNumComuni(): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/report/numComuni';
    return this.http.get(url).toPromise()
      .then(numComuni => numComuni as any).catch(this.handleError);
  }

  getReporNumServizi02(): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/report/numServizi02';
    return this.http.get(url).toPromise()
      .then(valore => valore as any).catch(this.handleError);
  }

  getReporTotFreq02(): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/report/totFreq02';
    return this.http.get(url).toPromise()
      .then(valore => valore as any).catch(this.handleError);
  }

  getReporNumServizi36(): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/report/numServizi36';
    return this.http.get(url).toPromise()
      .then(valore => valore as any).catch(this.handleError);
  }

  getReporTotFreq36(): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/report/totFreq36';
    return this.http.get(url).toPromise()
      .then(valore => valore as any).catch(this.handleError);
  }

  public getReport(idModulo): Observable<Blob> {

        const url = this.config.getBEServer() + '/moonbobl/restfacade/be/report/downloadReport/' + idModulo; 
        return this.http.get(url, { responseType: 'blob' });
    }

  public getReportByStreaming(idModulo, filtro): Observable<Blob> {
      const url = this.config.getBEServer() + '/moonbobl/restfacade/be/report/downloadLargeReport/' + idModulo;
      let params = new HttpParams;
      if (filtro){
        if (filtro.idEnte){
          params = new HttpParams().set('filtro', 'idEnte='+filtro.idEnte);        
        }
      }
      return this.http.get(url, {params, responseType: 'blob'});
  }    

}
