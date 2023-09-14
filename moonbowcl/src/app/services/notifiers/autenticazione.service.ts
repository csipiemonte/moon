/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {LoginResponseIF} from '../../model/common/login-response';

@Injectable({
  providedIn: 'root'
})
export class AutenticazioneService {
  private _communicateUserAuthenticated = new Subject<LoginResponseIF>();

  constructor() {
  }

  // Servizi notifica User Authenticated
  getDataUserAuthenticated(): Observable<LoginResponseIF> {
    return this._communicateUserAuthenticated.asObservable();
  }

  sendDataUserAuhenticated(data) {
    this._communicateUserAuthenticated.next(data);
  }

}
