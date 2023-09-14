/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Injectable } from '@angular/core';
import {AbstractCacheService} from './abstract-cache.service';
import {Modulo} from '../model/dto/modulo';

@Injectable({
  providedIn: 'root'
})
export class ModuliCacheService extends AbstractCacheService<Modulo[]> {

}

