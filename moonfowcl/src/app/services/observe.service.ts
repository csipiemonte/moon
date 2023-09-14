/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {Observable, ReplaySubject} from 'rxjs';
import {Header} from '../common/header';

@Injectable({
  providedIn: 'root'
})
export class ObserveService {

  private eventResponse = new ReplaySubject<any>(1);
  private eventUrlLogout = new ReplaySubject<string>(1);
  private eventHeader = new ReplaySubject<Header>(1);

  constructor() {
  }

  setEventResponse(response: any) {
    this.eventResponse.next(response);
  }

  getEventResponse(): Observable<any> {
    return this.eventResponse.asObservable();
  }

  setEventUrlLogout(urlLogout: string) {
    this.eventUrlLogout.next(urlLogout);
  }

  getEventUrlLogout(): Observable<string> {
    return this.eventUrlLogout.asObservable();
  }

  setEventHeader(header: Header) {
    this.eventHeader.next(header);
  }

  getEventHeader(): Observable<Header> {
    return this.eventHeader.asObservable();
  }

  unsubscribeEventResponse() {
    this.eventResponse.unsubscribe();
  }

  unsubscribeEventUrlLogout() {
    this.eventUrlLogout.unsubscribe();
  }


  // getEventEnte(): Observable<Ente> {
  //   return this.eventEnte.asObservable();
  // }

  // setEventEnte(ente: Ente) {
  //   this.eventEnte.next(ente);
  // }

  unsubscribeEventHeader() {
    this.eventHeader.unsubscribe();
  }
}
