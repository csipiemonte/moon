/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Injectable } from '@angular/core';
import hash from 'hash-it';
import {Observable} from 'rxjs';
import * as moment from 'moment';
import {environment} from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export abstract class AbstractCacheService<T> {

  protected readonly CACHE_DURATION_IN_MINUTES = environment.CACHE_DURATION;
  readonly DEFAULT_KEY = 'DEFAULT';

  private cache: {
    [id: string]: {
      expires: Date,
      value: Observable<T>
    }
  } = {};


  getValue(object?: any): Observable<T> {
    const key = object ? hash(object).toString() : this.DEFAULT_KEY;
    const item = this.cache[key];
    if (!item) {
      return null;
    }

    if (moment().isAfter(item.expires)) {
      return null;
    }

    return item.value;
  }

  setValue(value: Observable<T>, object?: any) {
    const key = object ? hash(object).toString() : this.DEFAULT_KEY;
    const expires = moment()
        .add(this.CACHE_DURATION_IN_MINUTES, 'minutes')
        .toDate();
    this.cache[key] = {expires, value};
  }

  clearCache() {
    this.cache = null;
  }
}
