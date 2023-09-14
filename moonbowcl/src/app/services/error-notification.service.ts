/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Injectable } from '@angular/core';
import {MoonboError} from '../model/common/moonbo-error';
import {BehaviorSubject} from 'rxjs';

/*
 Servizio per notifica errori
 Utilizzato dal componente error-component per
 mostrare gli errori
 */
@Injectable({
  providedIn: 'root'
})
export class ErrorNotificationService {

  public notification = new BehaviorSubject<MoonboError | null>(null);
  constructor() { }
}
