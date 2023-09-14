/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Injectable } from '@angular/core';
import {Observable, ReplaySubject, Subject} from 'rxjs';
import {VersioneStato} from '../model/dto/versione-stato';
/*
  Servizio per gestire soubscribe a eventi
  emessi da componenti ad hoc e di interesse
  dei subscriber
 */

@Injectable({
  providedIn: 'root'
})
export class ObserveService {

  private eventResponse  = new  ReplaySubject<any>(1);
  private eventUrlLogout = new  ReplaySubject<string>(1);

  private eventResponseCrm  = new  ReplaySubject<any>(1);

  // evento di click su icona operazione nel custom component del componente tree grid
  private _clickCambiaStatoOnTreeGridOperation = new Subject<any>();
  private _clickEditOnTreeGridOperation = new Subject<any>();

  getClickCambiaStatoOnTreeGridOperation(): Observable<VersioneStato> {
    return this._clickCambiaStatoOnTreeGridOperation.asObservable();
  }
  sendClickCambiaStatoOnTreeGridOperation(data: VersioneStato) {
    this._clickCambiaStatoOnTreeGridOperation.next(data);
  }

  //
  getClickEditOnTreeGridOperation(): Observable<VersioneStato> {
    return this._clickEditOnTreeGridOperation.asObservable();
  }
  sendClickEditOnTreeGridOperation(data: VersioneStato) {
    this._clickEditOnTreeGridOperation.next(data);
  }

  // private eventEnte = new  ReplaySubject<Ente>(1);

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

  unsubscribeEventResponse(){
    this.eventResponse.unsubscribe();
  }

  unsubscribeEventUrlLogout(){
    this.eventUrlLogout.unsubscribe();
  }
  
  constructor() { }

  sendDataCrm(response: any) {
    this.eventResponseCrm.next(response);
  }

  getDataCrm(): Observable<any> {
    return this.eventResponseCrm.asObservable();
  }  
  unsubscribeEventResponseCrm(){
    this.eventResponseCrm.unsubscribe();
  }

}
